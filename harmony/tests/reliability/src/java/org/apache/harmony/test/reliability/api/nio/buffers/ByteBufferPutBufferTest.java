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
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Goal: find memory leaks caused by IntBuffer.put(IntBuffer) / clear() method.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations a cycle
 *          param[1] - the new buffer's capacity, in bytes  

 *    2. Allocates a new byte buffer of param[1] size.
 *    3. Allocates a new direct byte buffer of param[1] size.
 *    4. starts a cycle of param[0] iterations. On each iteration:
 *            - Creates a view of this byte buffer as an int buffer
 *            - transfers direct source buffer into direct buffer
 *            - transfers non direct source buffer into non-direct buffer
 *            - transfers non-direct source buffer into direct buffer
 *            - transfers direct source buffer into non-direct buffer
 *          - runs System.gc()
 *
 */

public class ByteBufferPutBufferTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ITERATIONS = 10000;

    public int BUFFER_CAPACITY = 1000;

    public static void main(String[] args) {
        System.exit(new ByteBufferPutBufferTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        IntBuffer direct_buf = null;
        IntBuffer nondirect_buf = null;
        ByteBuffer direct_bytebuf = ByteBuffer.allocateDirect(BUFFER_CAPACITY);
        ByteBuffer nondirect_bytebuf = ByteBuffer.allocate(BUFFER_CAPACITY);

        for (int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {

            // if (i % 1000 == 0) {
            //     log.add("Iteration: " + i);
            // }

            direct_buf = direct_bytebuf.asIntBuffer();
            nondirect_buf = nondirect_bytebuf.asIntBuffer();

            while (nondirect_buf.hasRemaining()) {
                nondirect_buf.put(nondirect_buf.get()); 
            }
            nondirect_buf.clear();

            while (direct_buf.hasRemaining()) {
                direct_buf.put(direct_buf.get()); 
            }
            direct_buf.clear();

            while (nondirect_buf.hasRemaining()) {
                direct_buf.put(nondirect_buf.get()); 
            }
            direct_buf.clear();
            nondirect_buf.clear();

            while (direct_buf.hasRemaining()) {
                nondirect_buf.put(direct_buf.get()); 
            }

            direct_buf.clear();
            nondirect_buf.clear();

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

        if (params.length >= 2) {
            BUFFER_CAPACITY = Integer.parseInt(params[1]);
        }

    }


}

