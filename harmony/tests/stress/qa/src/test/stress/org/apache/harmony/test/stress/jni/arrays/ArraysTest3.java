/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

package org.apache.harmony.test.stress.jni.arrays;

import org.apache.harmony.test.stress.jni.arrays.share.MTArraysTest;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.8 $
 */

public class ArraysTest3 extends MTArraysTest {

    /**
     * Length of arrays to be allocated in native code
     */
    private int arrSize;

    /**
     * Max. number of arrays to be allocated
     */
    private int maxArrCount;

    /**
     * Temporary array for performing calculations in Java code
     */
    private int[] tmpArray;

    /**
     * Temporary C array
     */
    private long tmpPtr;

    /**
     * Temporary int[][] array to store arrays
     */
    private int[][] arrays;

    private static native void init();

    private native int doNativeCalc(int len, int maxCnt, int[][] arrays,
            long tmpPtr);

    public ArraysTest3(int id) {
        super(id);
    }
    
    public ArraysTest3() {
        super(-1);
    }

    public void doTest() {
        try {
            arrSize = Integer.getInteger(
                    "org.apache.harmony.test."
                            + "stress.jni.arrays.ArraysTest3.arrSize", 10000)
                    .intValue();
            maxArrCount = Integer.getInteger(
                    "org.apache.harmony.test."
                            + "stress.jni.arrays.ArraysTest3.maxCnt", 2000)
                    .intValue();

            tmpArray = new int[arrSize];
            arrays = new int[maxArrCount][];
            tmpPtr = allocIntArray(arrSize);

            if (tmpPtr == 0) {
                throw new OutOfMemoryError("Cannot allocate temporary C array");
            }

            int res = doNativeCalc(arrSize, maxArrCount, arrays, tmpPtr);

            if (res == 0) {
                /**
                 * If native method returns zero the test fails
                 */
                testLog("Working thread", "ArraysTest3 FAILED");
                ReliabilityRunner.debug("Test failed");
                ReliabilityRunner.mainTest.addError(this, new JNITestError());
            }
        } catch (Throwable t) {
            /**
             * If any exception is caught the test fails
             */
            testLog("Working thread",
                    "ArraysTest3: ERROR: Unexpected exception caught: ");
            testLog("Working thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

    /**
     * Performs calculation (inversion) on the given arrays
     * 
     * @param arr
     *            The array which is to be processed
     * @return The array resulting of calculation
     */
    private int[] doCalc(int[] arr) {
        for (int i = 0; i < arrSize; i++) {
            tmpArray[i] = arr[arrSize - 1 - i];
        }
        return tmpArray;
    }

    static {
        // Initialize native data prior to invocation of nativeMethod()
        init();
    }
}
