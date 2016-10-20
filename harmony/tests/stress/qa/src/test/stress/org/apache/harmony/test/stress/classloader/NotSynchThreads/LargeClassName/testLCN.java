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

package org.apache.harmony.test.stress.classloader.NotSynchThreads.LargeClassName;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.LargeClassName.testLCN_R_00;
import org.apache.harmony.test.stress.classloader.share.LargeClassName.testLCN_W_00;

public class testLCN extends TestCase {

    public static final int THREADS_COUNT = 128;

    public static final int LOOP_COUNT = 1000;

    private static int lmtThrd;

    private static int lmtLoop;

    public static int[] tableResults = new int[THREADS_COUNT];

    public static int whatRun = 0;

    public void test() {
        final int ARGS_COUNT = 6;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.NotSynchThreads."
                            + "LargeClassName.testLCN.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - right or wrong
        // testArgs[1] - loop count
        // testArgs[2] - thread number
        // testArgs[3] - number of iterations
        // testArgs[4] - number of used classes
        // testArgs[5] - mode (one class loader or many will be used)

        whatRun = testArgs.length <= 0 ? 0 : (Integer.valueOf(testArgs[0]))
                .intValue();
        if (whatRun > 1) {
            whatRun = 1;
        } else if (whatRun < 0) {
            whatRun = 0;
        }
        lmtLoop = testArgs.length <= 1 ? LOOP_COUNT : (Integer
                .valueOf(testArgs[1])).intValue();
        lmtThrd = testArgs.length <= 2 ? THREADS_COUNT : (Integer
                .valueOf(testArgs[2])).intValue();

        if (lmtThrd > THREADS_COUNT) {
            lmtThrd = THREADS_COUNT;
        }
        if ((testArgs.length - 3) <= 0) {
            testThread.argsForTest = new String[0];
        } else {
            testThread.argsForTest = new String[testArgs.length - 3];
            for (int t = 3; t < testArgs.length; t++) {
                testThread.argsForTest[t - 3] = testArgs[t];
            }
        }

        ReliabilityRunner.debug("Start testLCN");
        ReliabilityRunner.debug("Number of iterations: " + lmtLoop);
        ReliabilityRunner.debug("Number of created thread: " + lmtThrd);

        if (whatRun == 0) {
            testLCN_R_00.logInfo = false;
        } else {
            testLCN_W_00.logInfo = false;
        }

        try {
            for (int i = 0; i < lmtLoop; i++) {
                Thread tThread[] = new Thread[lmtThrd];
                for (int j = 0; j < lmtThrd; j++) {
                    tThread[j] = new testThread(j);
                    tThread[j].start();
                }
                for (int j = 0; j < lmtThrd; j++) {
                    tThread[j].join();
                }
                for (int j = 0; j < lmtThrd; j++) {
                    if (tableResults[j] != 104) {
                        ReliabilityRunner.debug("Failed thread: " + j + " " + tableResults[j]);
                        ReliabilityRunner.mainTest.addError(this, new ClassLoaderTestError());
                    }
                }
            }
        } catch (Throwable e) {
            ReliabilityRunner.debug(e.toString());
            ReliabilityRunner.mainTest.addError(this, e);
        }
    }

    static class testThread extends Thread {

        private int index;

        public static String[] argsForTest;

        public testThread(int index) {
            this.index = index;
        }

        public void run() {
            testLCN.tableResults[index] = (testLCN.whatRun == 0) ? new testLCN_R_00()
                    .test(argsForTest)
                    : new testLCN_W_00().test(argsForTest);
        }
    }
}
