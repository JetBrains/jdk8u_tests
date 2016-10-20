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

import org.apache.harmony.test.stress.jni.share.STTest;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.7 $
 */

public class GlobalRefsTest6 extends STTest {

    private int numRefs;

    private int invCount;

    private static native void init();

    private native void nativeMethod(int cnt);

    public void doTest() {
        // try to invoke native method several times to create a specified
        // number of references
        numRefs = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "globalrefs.GlobalRefsTest6.numRefs", 50000000)
                .intValue();

        invCount = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "globalrefs.GlobalRefsTest6.invCount", 20).intValue();

        for (int i = 0; i < invCount; i++) {
            try {
                int[] buffer = new int[10000];
                nativeMethod(numRefs);
            } catch (Throwable t) {
                if (!(t instanceof OutOfMemoryError)) {
                    testLog("Working thread",
                            "GlobalRefsTest6 ERROR: Exception caught:");
                    testLog("Working thread", t);
                    ReliabilityRunner.debug("Test error");
                    ReliabilityRunner.mainTest
                            .addError(this, new JNITestError());
                }
            }
        }
        testLog("Working thread", "GlobalRefsTest6 PASSED");
    }

    static {
        init();
    }
}
