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
 * @version: $Revision: 1.7 $
 */

package org.apache.harmony.test.stress.classloader.share.MethodArgs;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class testMethodArgs_R extends TestCase {
    public static final int ARRSIZE = 1000;

    public static final String TESTN0 = "testMethodArgs";

    public static final String TESTNN = "testMethodArgs_";

    public static final int LOOPCNT = 1000;

    public static final int PASSANS = ReliabilityRunner.RESULT_PASS;

    public static boolean logInfo = true;

    public void test() {
        // boolean pass = true;
        final int ARGS_COUNT = 2;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.share."
                            + "MethodArgs.testMethodArgs_R.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - number of iterations
        // testArgs[1] - number of opened classes

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();
        int cntClss = testArgs.length <= 1 ? ARRSIZE : Integer.valueOf(
                testArgs[1]).intValue();

        if (cntLoop > LOOPCNT) {
            cntLoop = LOOPCNT;
        }
        if (cntClss > ARRSIZE) {
            cntClss = ARRSIZE;
        }

        if (logInfo) {
            ReliabilityRunner.debug("Use class which contains method with many arguments");
            ReliabilityRunner.debug("Number of used classes: " + cntClss);
            ReliabilityRunner.debug("Number of iterations: " + cntLoop);
        }
        Class[] arrClss;
        Object[] arrObj;

        String pkgN = this.getClass().getPackage().getName();
        String name;
        for (int t = 0; t < cntLoop; t++) {
            arrClss = new Class[cntClss];
            arrObj = new Object[cntClss];
            for (int i = 0; i < cntClss; i++) {
                try {
                    name = pkgN.concat(".").concat(TESTNN).concat(
                            Integer.toString(i % 20));
                    arrClss[i] = Class.forName(name);
                    arrObj[i] = arrClss[i].newInstance();
                    testMethodArgs tt = (testMethodArgs) arrObj[i];
                    int x = tt.run_call(PASSANS);
                    if (x != PASSANS) {
                        ReliabilityRunner.debug("Bad answer: " + x + "  step " + i
                                + "  iteration: " + t);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                    int ans = tt.getF();
                    if (ans != PASSANS) {
                        ReliabilityRunner.debug("getF() returns unexpected value: " + ans
                                + " should be " + PASSANS);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
            arrObj = null;
            arrClss = null;

        }
    }
}
