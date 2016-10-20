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
 * @version: $Revision: 1.8 $
 */
package org.apache.harmony.test.stress.classloader.MixThreads.SupIntf;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.NotSynchThreads.SupIntf.testManySupIntf_02;

public class testManySupIntf2 extends TestCase {

    public static final int THREADS_COUNT = 128;

    public static final int LOOP_COUNT = 1000;

    private static int lmtThrd;

    private static int lmtLoop;

    public static int[] tableResults = new int[THREADS_COUNT];

    public static int mode = 1;

    public static int loopTest = 1000;

    public void test() {
        final int ARGS_COUNT = 5;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.MixThreads."
                            + "SupIntf.testManySupIntf2.arg" + testArgsCount);
            if (preTestArgs[testArgsCount] == null) {
                break;
            }
        }
        String[] testArgs = new String[testArgsCount];
        for (int k = 0; k < testArgsCount; k++) {
            testArgs[k] = preTestArgs[k];
        }

        // testArgs[0] - mode
        // testArgs[1] - loop count
        // testArgs[2] - thread number
        // testArgs[3] - correct classes package name
        // testArgs[4] - number of steps
        mode = testArgs.length <= 0 ? 1 : (Integer.valueOf(testArgs[0]))
                .intValue();
        lmtLoop = testArgs.length <= 1 ? LOOP_COUNT : (Integer
                .valueOf(testArgs[1])).intValue();
        lmtThrd = testArgs.length <= 2 ? THREADS_COUNT : (Integer
                .valueOf(testArgs[2])).intValue();

        if (lmtThrd > THREADS_COUNT) {
            lmtThrd = THREADS_COUNT;
        }
        if ((testArgs.length - 3) <= 0) {
            testThread2.argsForTest = new String[0];
        } else {
            testThread2.argsForTest = new String[testArgs.length - 3];
            for (int t = 3; t < testArgs.length; t++) {
                testThread2.argsForTest[t - 3] = testArgs[t];
            }
            int l = testThread2.argsForTest.length;
            if (l <= 2) {
                mode = 0;
                loopTest = 1;
            } else {
                if (mode == 0) {
                    loopTest = 1;
                } else {
                    loopTest = (Integer.valueOf(testThread2.argsForTest[1]))
                            .intValue();
                    testThread2.argsForTest[1] = Integer.toString(1);
                }
            }
        }

        ReliabilityRunner.debug("Start testManySupIntf");
        ReliabilityRunner.debug("Number of iterations: " + lmtLoop);
        ReliabilityRunner.debug("Number of created thread: " + lmtThrd);

        int failAns = 0;

        try {

            for (int i = 0; i < lmtLoop; i++) {

                Thread tThread[] = new Thread[lmtThrd];
                for (int n = 0; n < tableResults.length; n++) {
                    tableResults[n] = 0;
                }

                for (int j = 0; j < lmtThrd; j++) {
                    tThread[j] = new testThread2(j, ((j % 2) == 0 ? true
                            : false));
                    tThread[j].start();
                }

                for (int j = 0; j < lmtThrd; j++) {
                    tThread[j].join();
                }

                for (int j = 0; j < lmtThrd; j++) {
                    if ((tableResults[j] % 104) != 0) {
                        ReliabilityRunner.debug("Failed thread: " + j
                                + "  incorrect result: " + tableResults[j]);
                        failAns++;
                    }
                }
            }
        } catch (Throwable e) {
            ReliabilityRunner.debug(e.toString());
            ReliabilityRunner.mainTest.addError(this, e);
        }

        if (failAns != 0) {
            ReliabilityRunner
                    .debug("Test fails: some tests finish with failed answer");
            ReliabilityRunner.mainTest.addError(this,
                    new ClassLoaderTestError());
        }
    }
}

class testThread2 extends Thread {

    private int index;

    private boolean mSynch;

    public static String[] argsForTest;

    public testThread2(int index, boolean mode) {
        this.index = index;
        this.mSynch = mode;
    }

    public void run() {
        test02 t = new test02();
        test02.cnt++;
        if (!mSynch) {
            testThread2.argsForTest[1] = Integer
                    .toString(testManySupIntf2.loopTest);
            testManySupIntf2.tableResults[index] = t.test1(argsForTest);
        } else {
            try {
                synchronized (test02.class) {
                    int ans = t.test1(argsForTest);
                    testManySupIntf2.tableResults[index] += ans;
                }
            } catch (Throwable e) {
                e.printStackTrace(System.out);
                testManySupIntf2.tableResults[index] += 105;
            }
        }
    }
}

class test02 extends testManySupIntf_02 {

    public static int cnt = 0;

    public int test1(String[] aa) {
        return super.test1(aa);
    }

}
