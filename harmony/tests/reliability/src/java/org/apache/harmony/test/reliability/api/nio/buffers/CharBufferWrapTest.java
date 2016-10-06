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
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.nio.buffers;

import org.apache.harmony.test.reliability.share.Test;
import java.nio.CharBuffer;

/**
 * Goal: find memory leaks caused by CharBuffer.wrap() method.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize one wrap in a cycle
 
 *    2. CharBuffer.wrap() throws IndexOutOfBoundsException 
 *    3. starts a cycle of param[0] iterations.
 *    4. runs System.gc()
 *
 */

public class CharBufferWrapTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_WRAP_ITERATIONS = 10000000;

    //private char array
    private char[] array = new char[0];

    public static void main(String[] args) {
        System.exit(new CharBufferWrapTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        for (int i = 0; i < NUMBER_OF_WRAP_ITERATIONS; ++i) {

            // if (i % 1000000 == 0) {
            //     log.add("Iteration: " + i);
            // }

            try {
                CharBuffer.wrap(array, 1, 2);
                return fail("CharBuffer.wrap doesn't throw IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
            }

            if (callSystemGC != 0) {
                System.gc();
            }
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_WRAP_ITERATIONS = Integer.parseInt(params[0]);
        }

    }

}

