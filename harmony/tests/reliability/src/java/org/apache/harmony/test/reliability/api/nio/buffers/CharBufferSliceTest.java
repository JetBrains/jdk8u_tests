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

package org.apache.harmony.test.reliability.api.nio.buffers;

import org.apache.harmony.test.reliability.share.Test;
import java.nio.CharBuffer;
import java.util.Random;

/**
 * Goal: find resource leaks or intermittent failures caused by CharBuffer.slice() / duplicate() / wrap() operations.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize in a cycle
 *    2. Excutes a cycle of param[0] iterations, on each iteration:
 *         Allocates 2 char buffers: wrap(CharSequence csq) / wrap(char[] array) 
 *         For each of the char buffer:
 *            - sets random position and mark at its position
 *            - creates slice
 *            - checks position
 *            - creates duplicate
 *            - checks position
 *            - set random position for the duplicate
 *            - resets buffer's position to the previously-marked position
 *            - checks the duplicate position
 *            - creates duplicate's slice
 *            - checks position
 */

public class CharBufferSliceTest extends Test {

    public int NUMBER_OF_ITERATIONS = 1000000;

    final static int size = 1000;

    static int cnt = 0;

    public static void main(String[] args) {
        System.exit(new CharBufferSliceTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        String str = "qwertyuiopasdfghjklzxcvbnm";
        CharBuffer buff1 = null;
        CharBuffer slice1 = null;
        CharBuffer buff2 = null;
        CharBuffer slice2 = null;
        CharBuffer buff3 = null;
        CharBuffer slice3 = null;
        Random rn = new Random();
        int size = 0;
        int initial_size = 0;

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            size = rn.nextInt(str.length());
            buff1 = CharBuffer.wrap(str);
            buff1.position(size);
            buff1.mark();
            initial_size = size;
            slice1 = buff1.slice();

            if (slice1.position() != 0) {
                return fail("Iteration " + i
                    + " : Slice 1 position (should be 0): "
                    + slice1.position());
            }

            size = rn.nextInt(str.length());
            buff3 = buff1.duplicate();

            if (initial_size != buff3.position()) {
                return fail("Iteration " + i
                    + " : position after buff1.duplicate(): "
                    + buff3.position() + " , but should be " + initial_size);
            }

            buff3.position(size);
            initial_size = size;
            buff1.reset();

            if (initial_size != buff3.position()) {
                return fail("Iteration " + i
                    + " : position after buff1.reset(): "
                    + buff3.position() + " , but should be " + initial_size);
            }

            slice3 = buff3.slice();

            if (slice3.position() != 0) {
                return fail("Iteration " + i
                    + " : Slice 3 position (should be 0): "
                    + slice3.position());
            }

            size = rn.nextInt(str.length());
            buff2 = CharBuffer.wrap(str.toCharArray());
            buff2.position(size);
            buff2.mark();
            initial_size = size;
            slice2 = buff2.slice();

            if (slice2.position() != 0) {
                return fail("Iteration " + i
                    + " : Slice 2 position (should be 0): "
                    + slice2.position());
            }

            size = rn.nextInt(str.length());
            buff3 = buff2.duplicate();

            if (initial_size != buff3.position()) {
                return fail("Iteration " + i
                    + " : position after buff2.duplicate(): "
                    + buff3.position() + " , but should be " + initial_size);
            }

            buff3.position(size);
            initial_size = size;
            buff2.reset();

            if (initial_size != buff3.position()) {
                return fail("Iteration " + i
                    + " : position after buff2.reset(): "
                    + buff3.position() + " , but should be " + initial_size);
            }

            slice3 = buff3.slice();

            if (slice3.position() != 0) {
                return fail("Iteration " + i
                    + " : Slice 3 position (should be 0): "
                    + slice3.position());
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

