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
 * @author Olessia Salmina
 * @version $Revision: 1.7 $
 */

package org.apache.harmony.test.reliability.api.nio.charset;

import org.apache.harmony.test.reliability.share.Test;
import org.apache.harmony.test.reliability.api.nio.charset.auxiliary.*;
import java.util.SortedMap;
import java.nio.charset.Charset;

    //import java.util.Random;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

/**
 * Goal: find resource leaks or intermittent failures or Encoder/Decoder cache
 *     problems, connected with use of abstract methods
 *     java.nio.charset.Charset.newEncoder() and
 *     java.nio.charset.Charset.newDecoder().
 * 
 * The test does: 
 *     1. Reads parameters, which are: 
 *        param[0] - number of threads to be run in parallel 
 *        param[1] - number of iterations with encoding and decoding in each thread. 
 * 
 *     2. Receives all charsets through availableCharsets().
 * 
 *     3. Creates 3 strings of length 0xFFFF: 
 *        string[0] = all chars are those for which isJavaIdentifierPart(ch) returns true,
 *                    from 0 to 0xFFFF. 
 *        string[1] = all chars in range 0..0xFFFF, including malformed. 
 *        string[2] = all chars are those for which isJavaIdentifierPart(ch) 
 *                    returns true, randomly chosen.
 * 
 *    4. For each charset and each string:
 *       - creates CharsetDecoder and CharsetEncoder objects via newEncoder(), newDecoder()
 *       - specifies CodingErrorAction.IGNORE for both CharsetEncoder and CharsetDecoder
 *       - encodes and decodes each string param[1] times and compare results after
 *         first iteration and after the last one, expecting they are the same
 *       - runs System.gc()
 */

public class EncDecTest extends Test {

    public static int callSystemGC = 1;

    public static int NUMBER_OF_ITERATIONS = 100;
    public static int numThreads = 10;
    public static int[] statuses;
    public static final SortedMap allCharsets = Charset.availableCharsets();

    public static void main(String[] args) {
        System.exit(new EncDecTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
        // Start 'numThreads' threads each encoding/decoding
        Thread[] t = new Thread[numThreads];
        statuses = new int[t.length];

        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new Encoder_DecoderRunner(i, this));
            t[i].start();
            //log.add("Thread " + i + " started");
        }

        // Correctly wait for all threads to finish
        for (int i = 0; i < t.length; i++) {
            try {
                t[i].join();
                //log.add("Thread " + i + ": joined() ");

            } catch (InterruptedException ie) {
                return fail("InterruptedException while join() of thread #" + i);
            }
        }

        // For each thread check whether operations/checks PASSed in the thread

        for (int i = 0; i < statuses.length; ++i) {
            if (statuses[i] != Status.PASS) {
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

class Encoder_DecoderRunner implements Runnable {

    public int id;

    public EncDecTest base;

    public Encoder_DecoderRunner(int id, EncDecTest base) {
        this.id = id;
        this.base = base;
    }

    public void run() {

        Object[] names = base.allCharsets.keySet().toArray();
        String[] strs_to_encode = new String[3];
        strs_to_encode[0] = StringCreator.getValidString((char)0,(char)0xFFFF, StringCreator.START_TO_END);
        strs_to_encode[1] = StringCreator.getInvalidString();
        strs_to_encode[2] = StringCreator.getValidString((char)0,(char)0xFFFF, StringCreator.RANDOM);

        base.statuses[id] = Status.PASS;
        
        for (int i = 0; i < names.length; i++) {
            Charset chset = (Charset) base.allCharsets.get(names[i]);
            if(!chset.canEncode()) {
                continue;
            }            
            // base.log.add("Charset " + names[i]);
            Thread.yield();

            for (int j = 0; j < strs_to_encode.length; j++) {
                CharsetEncoder ce = chset.newEncoder().reset();
                CharsetDecoder de = chset.newDecoder().reset();
                CharBuffer chars_to_encode = CharBuffer.wrap(strs_to_encode[j]
                    .subSequence(0, strs_to_encode[j].length()));

                ByteBuffer encoded_bytes = null;
                CharBuffer decoded_chars = null;
                Thread.yield();
                try {
                    //base.log.add("String was:"+chars_to_encode.toString());
                    ce.onMalformedInput(CodingErrorAction.IGNORE);
                    ce.onUnmappableCharacter(CodingErrorAction.IGNORE);

                    de.onMalformedInput(CodingErrorAction.IGNORE);
                    de.onUnmappableCharacter(CodingErrorAction.IGNORE);

                    encoded_bytes = ce.encode(chars_to_encode);
                    decoded_chars = de.decode(encoded_bytes);

                } catch (Throwable t) {
                    // base.log.add(t);
                }
                // base.log.add("After encoding-decoding, 0 iteration: " +
                // decoded_chars.toString());
                String s = "";
                if (decoded_chars == null) {
                    continue;
                } else {
                    s = decoded_chars.toString();
                }

                // base.log.add("Thread " + id + ", Charset: " + names[i] +
                //              ", string length initial = " + s.length());
                Thread.yield();

                for (int k = 0; k < base.NUMBER_OF_ITERATIONS; ++k) {
                    try {
                        ce.reset();
                        de.reset();
                        ce.onMalformedInput(CodingErrorAction.IGNORE);
                        ce.onUnmappableCharacter(CodingErrorAction.IGNORE);
                        de.onMalformedInput(CodingErrorAction.IGNORE);
                        de.onUnmappableCharacter(CodingErrorAction.IGNORE);
                        encoded_bytes = ce.encode(decoded_chars);
                        decoded_chars = de.decode(encoded_bytes);

                        // base.log.add("After encoding-decoding, "+ k +"
                        // iteration: "+ decoded_chars.toString());
                        Thread.yield();
                    } catch (Throwable t) {
                        // base.log.add(t);
                    }
                    if (base.callSystemGC != 0) {
                        System.gc();
                    }
                }

                // base.log.add("Thread " + id + ", Charset: " + names[i] +
                // ", string length last = " +
                // decoded_chars.toString().length());
                // for these Charsets it is known at the moment of test creation
                // that they return different results after the first and
                // further encoding/decodings

                if (   !(names[i].equals("ISO-2022-JP")
                    || names[i].equals("x-ISCII91")
                    || names[i].equals("x-IBM1383")
                    || names[i].equals("x-IBM948") 
                    || names[i].equals("x-IBM950")
                    || names[i].equals("x-ISO-2022-CN-CNS")                      
                    || ((String) names[i]).startsWith("x-iscii-"))) {
                    if (!s.equals(decoded_chars.toString())) {
                        base.statuses[id] = Status.FAIL;
                        base.log.add("Thread " + id
                            + ", Charset: " + names[i]
                            + " chars after first "
                            + " encoding/decoding are not equal to chars after "
                            + base.NUMBER_OF_ITERATIONS + " encoding/decoding");
                        /*next statement closed to find all bad charsets 
                         return;
                         */ 
                    }
                }
            } // for strings            
        } // for Charset names        
    }
}



