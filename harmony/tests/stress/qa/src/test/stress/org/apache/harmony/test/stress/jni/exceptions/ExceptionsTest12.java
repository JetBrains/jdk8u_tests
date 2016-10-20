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

import org.apache.harmony.test.stress.jni.exceptions.share.STExceptionsTest;
import org.apache.harmony.test.stress.jni.exceptions.share.JNITestException;
import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.6 $
 */

public class ExceptionsTest12 extends STExceptionsTest {

    private static native void nativeMethod(int cnt) throws JNITestException;

    private static native void init();

    public void doTest() {
        try {
            testLog("Working thread", "Calling a native method...");
            nativeMethod(0);
            testLog("Working thread",
                    "ExceptionsTest12 FAILED: expected exception"
                            + " was not caught in Java code");
            ReliabilityRunner.debug("Test failed");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        } catch (JNITestException e) {
            if (getCounter() == 0) {
                testLog("Working thread",
                        "ExceptionsTest12 PASSED: Expected exception caught.");
            } else {
                testLog("Working thread",
                        "ExceptionsTest12 FAILED: Invalid exceptions counter value: "
                                + getCounter() + " instead of 0");
                ReliabilityRunner.debug("Test failed");
                ReliabilityRunner.mainTest.addError(this, new JNITestError());
            }
        } catch (OutOfMemoryError e) {
            testLog("Working thread",
                    "ExceptionsTest12 PASSED: Expected exception caught.");
            testLog("Working thread", e);
        } catch (Throwable t) {
            // there should be no other exceptions, if we get any - it's bad
            testLog("Working thread",
                    "ExceptionsTest12: ERROR: Exception caught in Java code");
            testLog("Working thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

    static {
        init();
    }
}
