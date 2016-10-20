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
import org.apache.harmony.test.stress.jni.stack.share.MTStackTest;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.9 $
 */

public class StackTest15 extends MTStackTest {

    private static int arrSize;

    private static int attCount;

    private static final Random RAND = new Random();

    private native int[] nativeMethod(int[] arr, int len, int cnt, int threadId);

    protected native void init(int cnt);

    public StackTest15(int threadId) {
        super(threadId);
    }

    public StackTest15() {
        super(-1);
    }

    public void doTest() {
        synchronized (StackTest15.class) {
            if (arrSize == 0) {
                arrSize = Integer.getInteger(
                        "org.apache.harmony.test.stress."
                                + "jni.stack.StackTest15.arrSize", 1000)
                        .intValue();
                attCount = Integer.getInteger(
                        "org.apache.harmony.test.stress."
                                + "jni.stack.StackTest15.attempts", 200)
                        .intValue();
            }
        }
        int[] arr = new int[arrSize];
        try {
            // create an array with elements initialized with random values
            for (int i = arrSize; i > 0; i--) {
                arr[arrSize - i] = RAND.nextInt();
            }
            // call a native method to cause StackOverFlowError and perform
            // calculations on the array
            nativeMethod(arr, arrSize, attCount, threadId);
        } catch (StackOverflowError e) {
        } catch (Throwable t) {
            testLog("Working thread",
                    "StackTest15 FAILED: Exception caught in thread "
                            + threadId);
            testLog("Working thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

    private int[] doCalc(int[] arr, int cnt) throws Throwable {
        int[] res = new int[arrSize];
        for (int i = 0; i < arrSize; i++)
            res[i] = arr[arrSize - i - 1];
        try {
            return nativeMethod(res, arrSize, 0, threadId);
        } catch (StackOverflowError e) {
            try {
                for (int i = 0; i < attCount; i++) {
                    nativeMethod(res, arrSize, 0, threadId);
                }
            } catch (StackOverflowError err) {
            }
        } catch (Throwable t) {
            testLog("Working thread",
                    "StackTest15 FAILED: Exception caught in thread "
                            + threadId);
            testLog("Working thread", t);
            throw t;
        }
        return res;
    }

}
