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
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Goal: find resource leaks or intermittent failures caused by ByteBuffer.compact() / put() / get() / flip() operations.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize in a cycle
 *    2. Excutes a cycle of param[0] iterations, on each iteration:
 *        Creates byte buffer of random capacity:
 *        For each of the byte buffer:
 *          - fill up this buffer
 *          - check result
 */

public class ByteBufferCompactTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ITERATIONS = 1000000;

    final static int size = 1000;

    static int cnt = 0;

    public static void main(String[] args) {
        System.exit(new ByteBufferCompactTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        ByteBuffer buf = null;
        Random rn = new Random();
        int allocate = 0;

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            allocate = rn.nextInt(size);
            buf = ByteBuffer.allocate(allocate);
            buf.clear();

            while (fillUp(buf) >= 0 || buf.position() != 0) {
                cnt++;
                buf.flip();
                checkByteBuffer(buf);
                buf.compact();
                if (cnt > size) {
                    break;
                }
            }

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

    //fill up a sequence of bytes into the buffer
    public int fillUp(ByteBuffer buf) {
        int pos = 0;
        int count = 0;
        while (buf.hasRemaining() && pos < size) {
            buf.put((byte) pos);
            pos++;
            count++;
        }
        if (pos == size && count == 0) {
            return -1;
        }
        return count;
    }

    //reads a sequence of bytes from the buffer
    public int checkByteBuffer(ByteBuffer buf) {
        int cnt = 0;
        byte bt = 0;
        int bp = 0;
        while (buf.hasRemaining()) {
            bt = buf.get();
            bp = buf.position() - 1;
            if (bt != (byte) (bp)) {
                return fail("Failed: incorrect byte in position " + bp);
            }

        }
        return 0;
    }


}

