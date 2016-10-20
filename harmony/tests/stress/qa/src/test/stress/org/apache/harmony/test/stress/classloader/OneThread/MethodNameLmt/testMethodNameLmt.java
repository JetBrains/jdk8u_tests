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
package org.apache.harmony.test.stress.classloader.OneThread.MethodNameLmt;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.MethodNameLmtClasses.testMNL_CA.testMNL_C;

public class testMethodNameLmt extends TestCase {

    public static final int CNTLOOP = 1000;

    public static final int CNTCLSS = 30;

    public static final String TESTNAME_C = "testMNL_C";

    public void test() {
        final int ARGS_COUNT = 3;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "MethodNameLmt.testMethodNameLmt.arg"
                            + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - correct classes package name
        // testArgs[1] - number of steps
        // testArgs[2] - number of used classes

        String pkgN = testArgs.length <= 0 ? this.getClass().getPackage()
                .getName() : testArgs[0];
        int lmtLoop = testArgs.length <= 1 ? CNTLOOP : (Integer
                .valueOf(testArgs[1])).intValue();
        int lmtClss = testArgs.length <= 2 ? CNTCLSS : (Integer
                .valueOf(testArgs[2])).intValue();

        if (lmtClss > CNTCLSS) {
            lmtClss = CNTCLSS;
        }
        ReliabilityRunner.debug("Verify: loading classes which declare methods with long names");
        ReliabilityRunner.debug("Number of iterations: " + lmtLoop);
        ReliabilityRunner.debug("Number of used classes: " + lmtClss);

        Object[] arClss;
        int[] ar;
        String name = "";

        for (int t = 0; t < lmtLoop; t++) {
            arClss = new Object[lmtClss];
            ar = new int[lmtClss];

            for (int i = 0; i < lmtClss; i++) {
                try {
                    name = pkgN.concat(".").concat(TESTNAME_C).concat(
                            Integer.toString(i).concat(".").concat(TESTNAME_C));
                    arClss[i] = Class.forName(name).newInstance();

                    ar[i] = ((testMNL_C) arClss[i]).allSum();
                } catch (OutOfMemoryError e) {
                    ReliabilityRunner.debug(e.toString() + " on step: 1");
                    ReliabilityRunner.mainTest.addError(this, e);
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            for (int i = lmtClss - 1; i >= 0; i--) {
                try {
                    name = pkgN.concat(".").concat(TESTNAME_C).concat(
                            Integer.toString(i).concat(".").concat(TESTNAME_C));
                    arClss[i] = Class.forName(name).newInstance();
                } catch (OutOfMemoryError e) {
                    ReliabilityRunner.debug(e.toString() + " on step: 2");
                    ReliabilityRunner.mainTest.addError(this, e);
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }

            for (int i = 0; i < lmtClss; i++) {
                try {
                    name = arClss[i].getClass().getName();
                    int tt = ((testMNL_C) arClss[i]).allSum();
                    if (tt != ar[lmtClss - 1 - i]) {
                        ReliabilityRunner.debug("Test failed: class " + name
                                + ": allSum must return " + ar[lmtClss - 1 - i]
                                + " but returns " + tt);
                        ReliabilityRunner.mainTest.addError(this,
                                new ClassLoaderTestError());
                    }
                } catch (OutOfMemoryError e) {
                    ReliabilityRunner.debug(e.toString() + " on step: 3");
                    ReliabilityRunner.mainTest.addError(this, e);
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }

            arClss = null;
            ar = null;
        }
    }
}
