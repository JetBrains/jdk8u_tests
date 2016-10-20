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

package org.apache.harmony.test.stress.jni.exceptions;

import org.apache.harmony.test.stress.jni.exceptions.share.MTExceptionsTest;
import org.apache.harmony.test.stress.jni.exceptions.share.JNITestException;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.8 $
 */

public class ExceptionsTest13 extends MTExceptionsTest {

    private static native void nativeMethod(int cnt, int threadId)
            throws JNITestException;

    protected native void init(int threadsCount);

    public ExceptionsTest13(int id) {
        super(id);
    }

    public ExceptionsTest13() {
        super(-1);
    }

    public void doTest() {
        int depth = Integer.getInteger(
                "org.apache.harmony.test.stress.jni."
                        + "exceptions.ExceptionsTest13.depth", 100).intValue();

        try {
            nativeMethod(depth, threadId);
            testLog("Working thread",
                    "ExceptionsTest13 FAILED: No exception caught in Java code");
            ReliabilityRunner.debug("Test failed");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        } catch (JNITestException e) {
            if (getCounter(threadId) != depth) {
                testLog("Working thread",
                        "ExceptionsTest13 FAILED: Invalid exceptions counter value: "
                                + getCounter(threadId) + " instead of " + depth);
                ReliabilityRunner.debug("Test failed");
                ReliabilityRunner.mainTest.addError(this, new JNITestError());
            }
        } catch (Throwable t) {
            // there should be no other exceptions, if we get any - it's bad
            testLog("Working thread",
                    "ExceptionsTest13: ERROR: Unexpected exception caught in Java code:");
            testLog("Working thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }
}
