/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Oleg Oleinik
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.reliability.api.nio.charset;


import org.apache.harmony.test.reliability.share.Test;
import java.io.UnsupportedEncodingException;
import java.util.SortedMap;
import java.nio.charset.Charset;

/**
 * Goal: check that Charset's: forName(), availableCharsets(), encode(), decode()
 * are thread safe and manage internal Charset cashes without leading to cach overflow.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of threads to run in parallel
 *          param[1] - number of iterations to run Charset operations in each thread
 *
 *    2. Starts and, then, joins all started threads
 *
 *    3. Checks that in each thread all checks PASSed.
 *
 *    4. Each thread, being started:
 *
 *        a. Runs param[1] iterations in a cycle, on each iteration:
 *        b. Receives all available charsets via Charset.availableCharsets()
 *        c. For each Charset in returned list:
 *           1) calls forName(canonical name), checks that returned Charset is as expected, 
 *              i.e. its name() returns correct Charset's canonical name.
 *           2) calls forName(alias name) for each Charset's alias, checks that returned 
 *              Charset is as expected, i.e. its name() returns correct Charset's canonical name.
 *           3) calls encode/decode for each Charset returned via alias, compares that resulted string
 *              is as after encoding/decoding by original Charset.
 *
 */

public class CharsetSyncCacheTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ITERATIONS = 100;

    public int numThreads = 10;
        
    public int[] statuses;

    public static void main(String[] args) {
        System.exit(new CharsetSyncCacheTest().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);
                                
        // Start 'numThreads' threads each reading from file, inflating/deflating

        Thread[] t = new Thread[numThreads];

        statuses = new int[t.length];
                                
        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new CharsetForNameRunner(i, this));
            t[i].start();
            //log.add("Thread " + i + " started");
        }
                
        // Correctly wait for all threads to finish

        for (int i = 0; i < t.length; ++i){
            try {
                t[i].join();
                //log.add("Thread " + i + ": joined() ");

            } catch (InterruptedException ie){
                return fail("interruptedException while join() of thread #" + i);
            }
        }

        // For each thread check whether operations/checks PASSed in the thread

        for (int i = 0; i < statuses.length; ++i){
            if (statuses[i] != Status.PASS){
                return fail("thread #" + i + " returned not PASS status");
            }
            //log.add("Status of thread " + i + ": is PASS");
        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numThreads = Integer.parseInt(params[0]);
        }        

        if (params.length >= 2) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[1]);
        }        

    }

}


class CharsetForNameRunner implements Runnable {

    public int id;
    public CharsetSyncCacheTest base;

    public final String STRING_TO_ENCODE = "1aBc0#$%&fghfgh1242345";

    public CharsetForNameRunner(int id, CharsetSyncCacheTest base) {
        this.id = id;
        this.base = base;
    }

    public void run() {

        int k = 0;

        while (k++ < base.NUMBER_OF_ITERATIONS) {

            SortedMap allCharsets = Charset.availableCharsets();

            // base.log.add("Thread " + id + ": iteration " + k + ": received available Charsets");

            Object[] names = allCharsets.keySet().toArray();

            // for each of received Charset load each charset by alias and forName()
            // look at what we received - must be Charset with the same canonical name:

            for (int i = 0; i < names.length; ++i){

                // FIRST, load Charset by canonical name and forName()
 
                Charset _chset = (Charset) allCharsets.get(names[i]);
                Thread.yield();
 
                Charset chset = Charset.forName((String) names[i]);
                if(!chset.canEncode()) {
                    continue;
                }  
                Thread.yield();

                // base.log.add("Thread " + id + ": iteration " + k + ": forName(" + names[i] + ")");
                // look at what returned - names must be the same - canonical

                if (!chset.name().equals(_chset.name())){
                    base.statuses[id] = Status.FAIL;
                    base.log.add("Thread " + id + ", forName(" + names[i] + ") returned Charset \"" +
                        chset.name() + "\", instead of \"" + _chset.name() + "\"");
                    return;
                }

                if (!chset.name().equals((String) names[i])){
                    base.statuses[id] = Status.FAIL;
                    base.log.add("Thread " + id + ", forName(" + names[i] + ") returned Charset " +
                        "with value of name() different from canonical \"" + names[i] + "\"");
                    return;
                }

                String str = chset.decode(chset.encode(STRING_TO_ENCODE)).toString();

                Object[] aliases = _chset.aliases().toArray();

                // SECOND, load Charset by each of alias of this Charset and forName()

                for (int j = 0; j < aliases.length; ++j){

                    chset = Charset.forName((String) aliases[j]);
                    Thread.yield();

                    // base.log.add("Thread " + id + ": iteration " + k + ": forName(" + aliases[j] + "), alias");

                    // look at what returned - names must be the same - canonical

                    if (!chset.name().equals((String) names[i])){
                        base.statuses[id] = Status.FAIL;
                        base.log.add("Thread " + id + ", forName(" + aliases[j] + ") by alias returned " + 
                            "Charset with different value of name()");
                        return;
                    }

                    String str2 = chset.decode(chset.encode(STRING_TO_ENCODE)).toString();

                    if (!str.equals(str2)){
                        base.statuses[id] = Status.FAIL;
                        base.log.add("Thread " + id + ", forName(" + aliases[j] + ") by alias, encode/decode " + 
                            "returned \"" + str2 + "\" instead of \"" + str + "\"");
                        return;
                    }
                       
                } // alias
                    
            } // charset
            
            if (base.callSystemGC != 0){
                System.gc();
            }
    
        } // while

        base.statuses[id] = Status.PASS;
    }

}


class Status {
    public static final int FAIL = -10;
    public static final int PASS = 10;
}
