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
 * @author Nikolay Bannikov
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.reliability.api.kernel.string;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;


/**
 *  Goal: find resource leaks and/or race conditions in pool of internal string representations
 *
 *  Idea: first, make string pool big enough to make intern() operations long-executing, then, 
 *        run many threads each calling intern() for the same string, expecting several threads
 *        will run intern() simultaneously. If there is a race condition, it will be revealed.
 * 
 *  The test does:
 *        1. In class static initializer create N_OF_POOL_STRINGS strings each of 
 *           STRING_LENGTH length - to make string pool big enough (slow working).
 *
 *        2. Start N_OF_THREADS threads, each thread:
 *           calls s.intern() for the same string 's', expecting to stimulate race condition.
 *           calls s.intern() from within syncronized(s), expecting there can be a lock.
 *           checks that the content of s remains the same as well as its intern reference 
 *           despite of all invocations.
 *
 */

public class InternThreadingTest extends Test {

    static Random rnd = new Random(10);

    static final int N_OF_THREADS = 50;

    static final int STRING_LENGTH = 200;

    static final int N_OF_POOL_STRINGS = 50000;

    static String[] many_strings = new String[N_OF_POOL_STRINGS];


    // we initialize big number of strings and add them into pool of internal
    // representations. What for? - to make pool big enough to take long time 
    // when searching for an entry - when new entry is added or just returned.

    static {
  
        for (int i = 0; i < many_strings.length; ++i){

            // fill-in internal string pool with random-content strings each of
            // STRING_LENGTH length:
 
            many_strings[i] = getRandomString();
            many_strings[i].intern();
        }
    }
 
    public static void main(String[] args) {
        System.exit(new InternThreadingTest().test(args));
    }


    public int test(String[] params) {

        Thread[] t = new Thread[N_OF_THREADS];

        char[] c = getRandomCharArray();

        for (int i = 0; i < t.length; ++i) {

            // why passing char array? - to avoid operations with Strings which
            // can call intern() inside, before we need this in this test...

            t[i] = new InternStringThread(c);
            t[i].start();
        }

        for (int i = 0; i < t.length; ++i) {
            try {
                t[i].join();
            } catch (InterruptedException ie) {
            }
        }

        return pass("OK");
    }


 
    static String getRandomString() {
        return new String(getRandomCharArray());
    }

    static char[] getRandomCharArray() {

        char[] c = new char[rnd.nextInt(STRING_LENGTH)];

        for (int j = 0; j < c.length; ++j){
            c[j] = (char)rnd.nextInt(Character.MAX_VALUE);
        }

        return c;
    }

}


class InternStringThread extends Thread {

    char[] c;

    public InternStringThread(char[] c) {
        this.c = c;
    }

    public void run() {

        // THIS IS THE KEY CALL - we expect that each of started thread 
        // calls s.intern() for the same-content string, expecting will
        // run into string pool race:

        String s = new String(c);
        String intern_string = s.intern();

        String str;

        // just in case - lets see what happens if we call intern() while keeping
        // lock of the string... :

        synchronized(intern_string){
            str = intern_string.intern();
        }

        if (str != intern_string) {
            throw new RuntimeException("Wrong intern returned!");
        }

        if (!s.equals(str)) {
            throw new RuntimeException("Wrong string content!");
        }

        // create more entries in string pool in multi-threading environment,
        // expecting racing in pool will appear:

        //InternThreadingTest.getRandomString().intern();

    }
     
}








