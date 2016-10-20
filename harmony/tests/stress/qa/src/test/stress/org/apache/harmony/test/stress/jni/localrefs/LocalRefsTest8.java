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

package org.apache.harmony.test.stress.jni.localrefs;

import org.apache.harmony.test.stress.jni.share.MTTest;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.8 $
 */

public class LocalRefsTest8 extends MTTest {

    private int objCount;

    private int invCount;

    private int numRefs;

    // temporary array to create object sequence
    private static Object[] objs = null;

    public LocalRefsTest8(int threadId) {
        super(threadId);
    }

    public LocalRefsTest8() {
        super(-1);
    }

    private static native void init();

    private native void nativeMethod(int cnt, int len);

    public void doTest() {
        objCount = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "localrefs.LocalRefsTest8.objCount", 10000000)
                .intValue();

        invCount = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "localrefs.LocalRefsTest8.invCount", 20).intValue();

        numRefs = objCount / invCount;

        synchronized (LocalRefsTest8.class) {
            if (objs == null) {
                objs = new Object[objCount];
                for (int i = 0; i < objCount; i++)
                    objs[i] = new Object();
            }
        }

        for (int i = 0; i < invCount; i++) {
            try {
                nativeMethod(numRefs, objCount);
            } catch (Throwable t) {
                if (!(t instanceof OutOfMemoryError)) {
                    testLog("Working thread",
                            "LocalRefsTest8: ERROR: Exception caught:");
                    testLog("Working thread", t);
                    ReliabilityRunner.debug("Test error");
                    ReliabilityRunner.mainTest
                            .addError(this, new JNITestError());
                }
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
