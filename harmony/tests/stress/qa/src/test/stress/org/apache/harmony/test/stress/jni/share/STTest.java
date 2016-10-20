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

import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

import junit.framework.TestCase;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.8 $
 */

public abstract class STTest extends TestCase {

    public void test() {
        try {
            doTest();
        } catch (Throwable t) {
            testLog("Log", "Uncaught exception while running test:");
            testLog("Log", t);
            ReliabilityRunner.debug("Test error");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

    public abstract void doTest();

    // Logging method to provide correct logging from multiple threads

    protected static void testLog(String header, Object obj) {
        if (obj instanceof java.lang.String) {
            ReliabilityRunner.debug(header + ": " + obj);
        } else if (obj instanceof java.lang.Throwable) {
            StringBuffer buf = new StringBuffer();
            Throwable t = (Throwable) obj;
            buf.append(t.getMessage());
            buf.append('\n');

            try {
                StackTraceElement[] trace = t.getStackTrace();
                for (int i = 0; i < trace.length; i++) {
                    buf.append("\t" + trace[i].toString());
                    buf.append('\n');
                }
            } catch (Throwable e) {
                buf.append("<no stack trace available>\n");
            }

            synchronized (STTest.class) {
                ReliabilityRunner.debug(header + ": Exception stack trace: "
                        + t.getClass().getName());
                ReliabilityRunner.debug(buf.toString());
            }
        }
    }

    static {
        System.loadLibrary("jnitests");
    }

}
