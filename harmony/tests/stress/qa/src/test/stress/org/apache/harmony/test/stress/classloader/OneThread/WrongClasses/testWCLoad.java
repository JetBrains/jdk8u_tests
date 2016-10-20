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

public class testWCLoad extends TestCase {

    public static final int LOOPCNT = 10000;

    public static boolean logIn = true;

    public int test1(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if(args[i] != null) {
                System.setProperty("org.apache.harmony.test."
                        + "stress.classloader.OneThread."
                        + "WrongClasses.testWCLoad.arg" + i, args[i]);
            }
        }
        test();
        return 104;
    }

    public void test() {
        final int ARGS_COUNT = 100;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.OneThread."
                            + "WrongClasses.testWCLoad.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - number of iterations
        // testArgs[1] - number of used classes
        // testArgs[2] - first class

        int notPass = 0;

        int cntLoop = testArgs.length <= 0 ? LOOPCNT : Integer.valueOf(
                testArgs[0]).intValue();

        int cntClss = testArgs.length <= 1 ? 0 : Integer.valueOf(testArgs[1])
                .intValue();
        if ((cntClss == 0) || ((testArgs.length - 2) <= 0)) {
            ReliabilityRunner.debug("Test fails: Used classes were not defined");
            ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
        }
        String[] clssN = new String[testArgs.length - 2];
        System.arraycopy(testArgs, 2, clssN, 0, testArgs.length - 2);
        if (clssN.length < cntClss) {
            cntClss = clssN.length;
        }
        if (logIn) {
            ReliabilityRunner.debug("Try to load wrong classes. Number of used classes: "
                            + cntClss);
        }
        for (int t = 0; t < cntLoop; t++) {
            if ((t % 1000 == 0) && logIn) {
                ReliabilityRunner.debug("Step: " + t + "...");
            }
            for (int i = 0; i < clssN.length; i++) {
                try {
                    Class cl = Class.forName(clssN[i]);
                    cl.newInstance();
                    notPass++;
                    ReliabilityRunner.debug("Test failed: wrong class " + clssN[i]
                            + " was loaded (step: " + t + ")");
                    ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                } catch (LinkageError e) {
                } catch (Throwable e) {
                    ReliabilityRunner.debug(e.toString());
                    ReliabilityRunner.mainTest.addError(this, e);
                }
            }
        }
    }
}
