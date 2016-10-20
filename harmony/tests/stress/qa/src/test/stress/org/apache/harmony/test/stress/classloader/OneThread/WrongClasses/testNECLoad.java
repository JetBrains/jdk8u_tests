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

package org.apache.harmony.test.stress.classloader.OneThread.WrongClasses;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class testNECLoad extends TestCase {

    public static final int LOOPCNT = 10000;

    public static final int LOOPCLSS = 2000;

    public static final String TESTNN = "testNotExistClass";

    public static boolean logIn = true;

    public int test1(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if(args[i] != null) {
                System.setProperty("org.apache.harmony.test."
                        + "stress.classloader.OneThread."
                        + "WrongClasses.testNECLoad.arg" + i, args[i]);
            }
        }
        test();
        return 104;
    }
    
    public void test() {
        final int ARGS_COUNT = 3;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "WrongClasses.testNECLoad.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - number of iterations
        // testArgs[1] - correct classes package name
        // testArgs[2] - number of used classes

        int notPass = 0;
        if (logIn) {
            ReliabilityRunner.debug("Try to load not exist classes ");
        }

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();

        String pkgN = testArgs.length <= 1 ? "" : testArgs[1];

        int cntClss = testArgs.length <= 2 ? LOOPCLSS : Integer.valueOf(
                testArgs[2]).intValue();
        String name;
        String nm;
        for (int t = 0; t < cntLoop; t++) {
            if ((t % 1000 == 0) && logIn) {
                ReliabilityRunner.debug("Step: " + t + "...");
            }
            for (int i = 0; i < cntClss; i++) {
                nm = Integer.toString(i);
                int sw = 0;

                while (sw < 3) {
                    switch (sw) {
                    case 0:
                        name = TESTNN;
                        break;
                    case 1:
                        if (pkgN.equals("")) {
                            name = TESTNN.concat(".").concat(TESTNN).concat(nm);
                        } else {
                            name = pkgN.concat(".").concat(TESTNN).concat(nm);
                        }
                        break;
                    default:
                        if (pkgN.equals("")) {
                            name = TESTNN.concat(nm).concat(".").concat(TESTNN)
                                    .concat(nm);
                        } else {
                            name = pkgN.concat(".").concat(TESTNN).concat(nm)
                                    .concat(".").concat(TESTNN).concat(nm);
                        }
                        break;
                    }
                    try {
                        Class cl = Class.forName(name);
                        cl.newInstance();
                        notPass++;
                        ReliabilityRunner.debug("Test failed: not exist class " + name
                                + " was loaded (step: " + t + ")");
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    } catch (ClassNotFoundException e) {
                    } catch (Throwable e) {
                        ReliabilityRunner.debug(e.toString());
                        ReliabilityRunner.mainTest.addError(this, e);
                    }
                    sw++;
                }
            }
        }
    }
}
