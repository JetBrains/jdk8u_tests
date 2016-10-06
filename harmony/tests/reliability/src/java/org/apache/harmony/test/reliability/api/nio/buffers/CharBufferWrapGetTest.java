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
import java.nio.ByteBuffer;
import java.util.Random;


/**
 * Goal: find memory leaks caused by CharBuffer.wrap()/put()/get() method.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize one wrap/put/get in a cycle

 *    2. starts a cycle of param[0] iterations. On each iteration:
 *            - Allocates a random byte buffer
 *            - puts string into the buffer
 *            - Sets this buffer's mark at random position
 *            - wraps a subsequence into the buffer
 *            - check the result
 *            - runs System.gc()
 */

public class CharBufferWrapGetTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ITERATIONS = 1000000;

    //private char array
    private char[] array = new char[0];

    public static void main(String[] args) {
        System.exit(new CharBufferWrapGetTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
        String str = "qwertyuiopasdfghjklzxcvbnm1234567890";
        CharBuffer cbuf = null;
        CharBuffer subbuf = null;
        String substr = "";
        char out[] = {};
        String cbufstr = "";
        Random rn = new Random();
        int size = 0;

        for (int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {

            size = rn.nextInt(str.length());
            cbuf = CharBuffer.allocate(512 + size);
            if (i % 2 == 0) {
                cbuf = ByteBuffer.allocateDirect(512 + size).asCharBuffer();
            }
            out = new char[cbuf.length()];
            cbuf.put(str);
            cbuf.position(size);
            cbuf.mark();
            subbuf = CharBuffer.wrap((CharBuffer)cbuf.subSequence(size, size + 1)); 
            cbufstr = String.valueOf(cbuf.get(size));
            out = new char[subbuf.length()];
            cbuf.get(out);
            substr = new String(out);
            cbuf.rewind();
            if(!substr.equals(cbufstr)) {
                return fail("Expected on step " + i + " : \"" + substr + "\" but got \"" + cbufstr + "\"");
            }

            // if (i % 100000 == 0) {
            //    log.add("Iteration: " + i);
            // }


            if (callSystemGC != 0) {
                System.gc();
            }
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }

    }

}

