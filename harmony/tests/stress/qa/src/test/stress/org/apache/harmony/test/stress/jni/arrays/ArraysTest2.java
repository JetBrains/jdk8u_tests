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

import org.apache.harmony.test.stress.jni.arrays.share.STArraysTest;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.7 $
 */

public class ArraysTest2 extends STArraysTest {

    /**
     * Length arrays to be allocated
     */
    private int arrSize;

    /**
     * Max. number of arrays to be allocated
     */
    private int maxArrCount;

    private int[] tmpArray;

    private int[][] arrays;

    private long tmpPtr;

    private static native void init();

    private native int doNativeCalc(int len, int maxCnt, int[][] arrays,
            long tmpPtr);

    public void doTest() {
        arrSize = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.jni.arrays.ArraysTest2.arrSize", 1000000)
                .intValue();
        maxArrCount = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.jni.arrays.ArraysTest2.maxCnt",
                2000000000 / 1000000).intValue();

        tmpArray = new int[arrSize];
        arrays = new int[maxArrCount][];
        tmpPtr = allocIntArray(arrSize);

        if (tmpPtr == 0) {
            throw new OutOfMemoryError("Cannot allocate temporary C array");
        }
        try {
            int res;
            res = doNativeCalc(arrSize, maxArrCount, arrays, tmpPtr);

            if (res == 0) {
                testLog("Working thread",
                        "ArraysTest2 FAILED: native method returned 0");
                ReliabilityRunner.debug("Test failed");
                ReliabilityRunner.mainTest.addError(this, new JNITestError());
            }
        } catch (Throwable t) {
            testLog("Working thread", "ArraysTest2: ERROR:");
            testLog("Working thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

    private int[] doCalc(int[] arr) {
        for (int i = 0; i < arrSize; i++) {
            tmpArray[i] = arr[arrSize - 1 - i];
        }
        return tmpArray;
    }

    static {
        init();
    }
}
