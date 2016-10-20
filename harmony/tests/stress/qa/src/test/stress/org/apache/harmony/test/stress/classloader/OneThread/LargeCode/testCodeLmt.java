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

/**
 * @author: Vera Y.Petrashkova
 * @version: $revision$
 */

package org.apache.harmony.test.stress.classloader.OneThread.LargeCode;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import java.lang.reflect.*;

public class testCodeLmt extends TestCase {
    public static final int ARRSIZE = 2000;

    public static final int LOOPCNT = 10000;

    public void test() {
        final int ARGS_COUNT = 1;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "LargeCode.testCodeLmt.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();
        ReliabilityRunner.debug("Use class with large method code: number of iterations is "
                        + cntLoop);

        Object[] arObjs;
        Class<?>[] arClss;
        Method m;

        for (int t = 0; t < cntLoop; t++) {
            if (t % 1000 == 0) {
                ReliabilityRunner.debug("Step: " + t + "...");
            }
            arClss = new Class[ARRSIZE];
            arObjs = new Object[ARRSIZE];
            String nm = "";
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    nm = this.getClass().getPackage().getName().concat(".")
                            .concat("fClass_c")
                            .concat(Integer.toString(i % 10));
                    arClss[i] = Class.forName(nm);
                    arObjs[i] = arClss[i].newInstance();

                    m = arClss[i].getMethod("get");
                    int ans = ((Integer) (m.invoke(arObjs[i]))).intValue();
                    if (ans != ReliabilityRunner.RESULT_PASS) {
                        ReliabilityRunner.debug("Test failed: incorrect result: " + ans
                                + " class: " + nm);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            arClss = null;
            arObjs = null;
            arClss = new Class[ARRSIZE];
            arObjs = new Object[ARRSIZE];
            for (int i = 0; i < ARRSIZE; i++) {
                try {
                    nm = this.getClass().getPackage().getName().concat(".")
                            .concat("fClass_w")
                            .concat(Integer.toString(i % 10));
                    arClss[i] = Class.forName(nm);
                    arObjs[i] = arClss[i].newInstance();

                    m = arClss[i].getMethod("get");
                    ((Integer) (m.invoke(arObjs[i]))).intValue();
                    ReliabilityRunner.debug("Test failed: InvocationTargetException must be thrown. "
                            + " Class: " + nm);
                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                } catch (InvocationTargetException e) {
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            arClss = null;
            arObjs = null;
        }
    }
}
