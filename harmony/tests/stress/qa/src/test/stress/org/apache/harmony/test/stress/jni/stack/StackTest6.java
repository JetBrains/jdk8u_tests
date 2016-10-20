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
 * @version $Revision: 1.8 $
 */

public class StackTest6 extends MTStackTest {

    private int arrSize;

    private static final Random RAND = new Random();

    private static native int[] nativeMethod(int[] arr, int len, int cnt,
            int threadId);

    protected native void init(int cnt);

    public StackTest6(int threadId) {
        super(threadId);
    }

    public StackTest6() {
        super(-1);
    }

    public void doTest() {
        arrSize = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "stack.StackTest6.arrSize", 1000).intValue();

        int[] arr = null;
        try {
            arr = new int[arrSize];
        } catch (OutOfMemoryError e) {
            testLog("Working thread",
                    "OOME caught while creating temporary array in thread "
                            + threadId + ". Increase buffer size");
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
        try {
            for (int i = arrSize; i > 0; i--) {
                arr[arrSize - i] = RAND.nextInt();
            }
            nativeMethod(arr, arrSize, 0, threadId);
        } catch (OutOfMemoryError e) {
        } catch (Throwable t) {
            // there should be no exceptions, if we get any - it's bad
            testLog("Working thread",
                    "StackTest6: ERROR: Exception caught in thread " + threadId);
            testLog("Working thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

}
