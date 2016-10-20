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

package org.apache.harmony.test.stress.jni.globalrefs;

import org.apache.harmony.test.stress.jni.share.MTTest;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.8 $
 */

public class GlobalRefsTest7 extends MTTest {

    private int objCount;

    // temporary array to create object sequence
    private static Object[] objs = null;

    public GlobalRefsTest7(int threadId) {
        super(threadId);
    }

    public GlobalRefsTest7() {
        super(-1);
    }

    private static native void init();

    private native int nativeMethod(int cnt, int len);

    public void doTest() {
        int[] buffer1 = new int[10000];
        int[] buffer2 = new int[10000];
        int cnt = -1;
        objCount = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "globalrefs.GlobalRefsTest7.objCount", 10000000)
                .intValue();

        synchronized (GlobalRefsTest7.class) {
            if (objs == null) {
                objs = new Object[objCount];
                for (int i = 0; i < objCount; i++)
                    objs[i] = new Object();
            }
        }

        // try to create a lot of global refs and cause an exception
        try {
            cnt = nativeMethod(-1, objCount);
            testLog("Working thread",
                    "GlobalRefsTest7 FAILED: No expected exception caught");
            ReliabilityRunner.debug("Test failed");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        } catch (Throwable t) {
            buffer1 = null;
            // the exception is caught and logged
            if (!(t instanceof OutOfMemoryError)) {
                testLog(
                        "Working thread",
                        "GlobalRefsTest7: ERROR: Unexpected exception caught during first native method invocation:");
                testLog("Working thread", t);
                ReliabilityRunner.debug("Test failed");
                ReliabilityRunner.mainTest.addError(this, new JNITestError());
            }
        }

        // then the native method is called again several times
        try {
            for (int i = 0; i < cnt; i++) {
                nativeMethod(cnt, objCount);
            }
        } catch (Throwable t) {
            buffer2 = null;
            if (!(t instanceof OutOfMemoryError)) {
                testLog(
                        "Working thread",
                        "GlobalRefsTest7: ERROR: Unexpected exception caught during further native method invocations:");
                testLog("Working thread", t);
                ReliabilityRunner.debug("Test error");
                ReliabilityRunner.mainTest.addError(this, new JNITestError());
            }
        }
    }

    private Object[] getObjectArray() {
        return objs;
    }

    static {
        init();
    }
}
