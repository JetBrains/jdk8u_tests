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
 * @author Nikolay Bannikov, Olessia Salmina
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.nio.buffers;


import org.apache.harmony.test.reliability.share.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Random;
import java.util.SortedMap;

/**
 * Goal: find memory leaks caused by java.nio.charset.CharsetEncoder.flush(...) method.
 * 
 * The test does: 
 * 1. Reads parameters, which are: 
 * param[0] - number of iterations to call/initialize one ByteBuffer.allocate in a cycle 
 * param[1] - the new buffer's capacity, in bytes 
 * 
 * 2. Excutes a cycle of param[0] iterations, on each iteration:
 *         a. Allocates large ByteBuffer of param[1] size. 
 *         b. Creates a new Encoder for next available charset (obtained by Charset.availableCharsets()) 
 *         c. Encodes a 3-char string (randomly obtained) in REPLACE mode into “large” ByteBuffer. 
 *         d. Allocates small ByteBuffer of size 1 (byte). 
 *         e. Calls CharsetEncoder.flush() into the small ByteBuffer. 
 *         f. checks the results is CoderResult.OVERFLOW. 
 *         g. Allocates another large ByteBuffer of param[1] size. 
 *         h. Calls CharsetEncoder.flush() into the large ByteBuffer.
 *         i. checks the results is CoderResult.OVERFLOW. 
 *         j. Class System.gc().
 * 
 * 
 */

public class ByteBufferallocateTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ALLOCATE_ITERATIONS = 30000;

    public int FIRST_BUFFER_CAPACITY = 1;

    public int SECOND_BUFFER_CAPACITY = 2000;

    public static void main(String[] args) {
        System.exit(new ByteBufferallocateTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
        SortedMap allCharsets = Charset.availableCharsets();
        Object[] names = allCharsets.keySet().toArray();
        // log.add("names.length: " + names.length);

        for (int ChNumber = 0; ChNumber < names.length; ChNumber++) {
            Charset charset = (Charset) allCharsets.get(names[ChNumber]);
            
            CharsetEncoder encoder = null;
            CharsetEncoder encoder1 = null;
            CharBuffer cb = null;
            ByteBuffer bb = null;
            ByteBuffer buf1 = null;
            ByteBuffer buf2 = null;
            CoderResult result = null;
            //log.add(""+charset);
            // log.add("names.length: " + names.length);
            // log.add("ChNumber: " + ChNumber);
            if(charset.isRegistered() && charset.canEncode()) {

                for (int i = 0; i < NUMBER_OF_ALLOCATE_ITERATIONS; ++i) {
                    // if (i % 1000 == 0) {
                    //    log.add("Iteration: " + i);
                    // }
                
                    String str = StringCrBuf.getRandomString((char) 3);
                    encoder = charset.newEncoder().onMalformedInput(
                        CodingErrorAction.REPLACE).onUnmappableCharacter(
                        CodingErrorAction.REPLACE);
                    cb = CharBuffer.wrap(str);

                    bb = ByteBuffer.allocate(SECOND_BUFFER_CAPACITY);
                    encoder.encode(cb, bb, true);

                    buf1 = ByteBuffer.allocate(FIRST_BUFFER_CAPACITY);
                    result = encoder.flush(buf1);

                    if (result == CoderResult.OVERFLOW) {
                        // Ignore: too smal buffer
                    	result = null;
                    }
                    buf2 = ByteBuffer.allocate(SECOND_BUFFER_CAPACITY);
                    // we intentionally call second flush(), and ignore the fact that it possible does not throw the exception,
                    // why?  to try to cause abnormal completion.
                    try {
                        result = encoder.flush(buf2);                     
                    } catch (IllegalStateException ise) {
                        result = null;
                    }                
                    if (result == CoderResult.OVERFLOW) {
                        return fail("Result shouldn't be overflow");
                    }
                    if (callSystemGC != 0) {
                        System.gc();
                    }
                    buf1.clear();
                    buf2.clear();
                    bb.clear();
                }
            }
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ALLOCATE_ITERATIONS = Integer.parseInt(params[0]);
        }

        if (params.length >= 1) {
            SECOND_BUFFER_CAPACITY = (Integer.parseInt(params[1])>20)?Integer.parseInt(params[1]):20;
        }

    }

}

class StringCrBuf {
    public static String getRandomString(char size) {
        char[] ch = new char[size];
        Random rand = new Random();
        int i = 0;
        while (i < size) {

            int c = rand.nextInt(0xFFFF);
            if (Character.isJavaIdentifierPart((char) c)) {
                ch[i++] = (char) c;
            }
        }
        return new String(ch);
    }
}