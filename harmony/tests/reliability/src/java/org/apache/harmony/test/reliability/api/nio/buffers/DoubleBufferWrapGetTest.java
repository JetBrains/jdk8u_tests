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
import java.nio.DoubleBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.Random;


/**
 * Goal: find memory leaks caused by DoubleBuffer.wrap()/allocate() and ByteOrder.nativeOrder() methods.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize one wrap/put/get in a cycle
 *          param[1] - size of allocated buffer 

 *    2. starts a cycle of param[0] iterations. On each iteration:
 *            - Allocates a double buffer
 *            - Allocates a direct byte buffer and creates a view of this byte buffer as a double buffer
 *            - reverses the native byte order 
 *            - wraps a double array
 *            - check the result
 *            - runs System.gc()
 */

public class DoubleBufferWrapGetTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ITERATIONS = 100000;

    public int BUFFER_SIZE = 100;

    //private char array
    private char[] array = new char[0];

    public static void main(String[] args) {
        System.exit(new DoubleBufferWrapGetTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        for (int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {
            DoubleBuffer[] array = getDoubleBufferArray();
            Random rn = new Random();
            int size = 0;
            array = getDoubleBufferArray();
            for(int j = 0; j< array.length ; j++) {

                size = array.length - rn.nextInt(array.length);
                if(size == 0) {
                    size = 1;
                }
                array[j].limit(size);
                double[] d = new double[size];
                d[size -1] = Double.MAX_VALUE;
                double maxvalue = DoubleBuffer.wrap(d).get(size-1);
                if(maxvalue != Double.MAX_VALUE) {
                    return fail("Expected on step " + i + " : \"" + Double.MAX_VALUE + "\" but got \"" + maxvalue + "\"");
                }
            }


            // if (i % 10000 == 0) {
            //    log.add("Iteration: " + i);
            // }


            if (callSystemGC != 0) {
                System.gc();
            }
        }
        return pass("OK");
    }

    public DoubleBuffer[] getDoubleBufferArray() {
        DoubleBuffer[] db = new DoubleBuffer[5];

        db[0] = DoubleBuffer.allocate(BUFFER_SIZE); 
        ByteBuffer bb = ByteBuffer.allocate(BUFFER_SIZE);
        db[1] = bb.asDoubleBuffer();  
        db[2] = bb.order(changeOrder()).asDoubleBuffer();
        ByteBuffer dirbuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        db[3] = dirbuf.asDoubleBuffer();
        db[4] = dirbuf.order(changeOrder()).asDoubleBuffer();
        return db;
    }

    public ByteOrder changeOrder() {
        ByteOrder order = ByteOrder.BIG_ENDIAN;
        if(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            order = ByteOrder.LITTLE_ENDIAN ;
        }
        return order;
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }

        if (params.length >= 2) {
            BUFFER_SIZE = Integer.parseInt(params[1]);
        }
    }

}

