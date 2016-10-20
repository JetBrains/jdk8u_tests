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

package org.apache.harmony.test.stress.jni.stack;

import java.util.Random;
import org.apache.harmony.test.stress.jni.stack.share.STStackTest;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.8 $
 */

public class StackTest4 extends STStackTest {

    private int arrSize;

    private int attCount;

    private static final Random RAND = new Random();

    private static native int[] nativeMethod(int[] arr, int len, int cnt);

    protected native void init(int cnt);

    public void doTest() {
        arrSize = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "stack.StackTest4.arrSize", 7000).intValue();
        attCount = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "stack.StackTest4.attempts", 200).intValue();

        int[] arr = null;
        try {
            arr = new int[arrSize];
        } catch (OutOfMemoryError e) {
            testLog("Working thread",
                    "OOME caught while creating temporary array. Increase buffer size");
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
        try {
            // create an array with elements initialized with random values
            for (int i = arrSize; i > 0; i--) {
                arr[arrSize - i] = RAND.nextInt();
            }
            // call a native method to cause StackOverFlowError and perform
            // calculations on the array
            nativeMethod(arr, arrSize, attCount);
        } catch (StackOverflowError e) {
        } catch (OutOfMemoryError e) {
        } catch (Throwable t) {
            testLog("Working thread", "StackTest4 FAILED: Exception caught "
                    + t.getClass().getName());
            testLog("Working thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
        testLog("Working thread", "StackTest4 PASSED");
    }
}
