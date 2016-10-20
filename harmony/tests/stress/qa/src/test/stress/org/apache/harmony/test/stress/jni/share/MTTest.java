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

package org.apache.harmony.test.stress.jni.share;

import java.lang.reflect.*;

import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.8 $
 */

/**
 * Shared class implementing framework for running multi-threaded JNI tests
 */
public abstract class MTTest extends STTest {

    /**
     * Identifier of current thread to be used in native code
     */
    protected int threadId;

    protected static native int getCounter(int id);

    public MTTest(int threadId) {
        this.threadId = threadId;
    }

    public void test() {
        int threadsCount = Integer.getInteger(
                "org.apache.harmony.test."
                        + "stress.jni.share.MTTest.threadsCount", 100)
                .intValue();
        int timeout = Integer.getInteger(
                "org.apache.harmony.test." + "stress.jni.share.MTTest.timeout",
                60 * 1000 * 20).intValue();

        testLog("Main thread", "Creating " + threadsCount + " threads");
        // Main thread creates a specified number of threads and then it waits
        // them to die
        Thread[] th = new Thread[threadsCount];
        try {
            // Creating Thread objects
            try {
                // formal constructor parameter
                Class[] fparam = new Class[1];
                // actual constructor parameter
                Object[] aparam = new Object[1];
                fparam[0] = int.class;

                for (int i = 0; i < th.length; i++) {
                    aparam[0] = new Integer(i);
                    Constructor mttestConstructor = getClass().getConstructor(
                            fparam);
                    th[i] = new ThreadRunner((MTTest) mttestConstructor
                            .newInstance(aparam));
                }
            } catch (Throwable e) {
                testLog("Main thread",
                        "ERROR: Error creating java.lang.Thread objects");
                testLog("Main thread", e);
                ReliabilityRunner.debug("Test error");
                ReliabilityRunner.mainTest.addError(this, new JNITestError());
            }
            // Starting threads
            testLog("Main thread", "Starting " + threadsCount + " threads");
            try {
                for (int i = 0; i < th.length; i++) {
                    th[i].setDaemon(true);
                    th[i].start();
                }
            } catch (Throwable e) {
                testLog("Main thread", "ERROR: Error starting threads");
                testLog("Main thread", e);
                ReliabilityRunner.debug("Test error");
                ReliabilityRunner.mainTest.addError(this, new JNITestError());
            }
            testLog("Main thread", "Started " + threadsCount + " threads");

            for (int i = 0; i < th.length; i++) {
                th[i].join(timeout);
                if (th[i].isAlive()) {
                    testLog("Main thread", "Thread " + i
                            + " is still alive after " + timeout + "ms");
                }
            }
        } catch (Throwable t) {
            testLog("Main thread", "ERROR: Unexpected exception caught");
            testLog("Main thread", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

    private class ThreadRunner extends java.lang.Thread {
        MTTest obj;

        ThreadRunner(MTTest obj) {
            this.obj = obj;
        }

        public void run() {
            obj.doTest();
        }
    }
}
