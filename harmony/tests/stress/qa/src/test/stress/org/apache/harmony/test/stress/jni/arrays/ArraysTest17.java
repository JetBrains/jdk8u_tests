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

public class ArraysTest17 extends STArraysTest {

    /**
     * Length arrays to be allocated
     */
    private int arrSize;

    /**
     * Max. number of arrays to be allocated
     */
    private int maxArrCount;

    private Object[][] arrays;

    private Object[] tmp1;

    private static native void init();

    private native void doNativeCalc(int len, int maxCnt, Object[] tmp1,
            Object[][] arrays);

    public void doTest() {
        try {
            arrSize = Integer.getInteger(
                    "org.apache.harmony.test."
                            + "stress.jni.arrays.ArraysTest17.arrSize", 10000)
                    .intValue();
            maxArrCount = Integer.getInteger(
                    "org.apache.harmony.test."
                            + "stress.jni.arrays.ArraysTest17.maxCnt",
                    2000000000 / 10000).intValue();

            tmp1 = new Object[arrSize];
            arrays = new Object[maxArrCount][];

            doNativeCalc(arrSize, maxArrCount, tmp1, arrays);
        } catch (Throwable t) {
            testLog("Working thread", "ArraysTest17: ERROR: Exception caught:");
            testLog("Working thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

    static {
        init();
    }
}
