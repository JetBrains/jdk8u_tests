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
package org.apache.harmony.test.stress.classloader.SynchThreads.FieldsLmt;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.classloader.share.ClassLoaderTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.NotSynchThreads.FieldsLmt.testFieldsLmt_0;

public class testFieldsLmt extends TestCase {

    public static final int THREADS_COUNT = 128;

    public static final int LOOP_COUNT = 1000;

    private static int lmtThrd;

    private static int lmtLoop;

    public static int[] tableResults = new int[THREADS_COUNT];

    public static int mode = 1;

    public static int loopTest = 1000;

    public void test() {
        final int ARGS_COUNT = 4;

        String[] preTestArgs = new String[ARGS_COUNT];
        int testArgsCount;
        for (testArgsCount = 0; testArgsCount < ARGS_COUNT; testArgsCount++) {
            preTestArgs[testArgsCount] = System
                    .getProperty("org.apache.harmony.test."
                            + "stress.classloader.SynchThreads."
                            + "FieldsLmt.testFieldsLmt.arg" + testArgsCount);
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
        // testArgs[3] - number of steps
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
            testThread.argsForTest = new String[0];
        } else {
            testThread.argsForTest = new String[testArgs.length - 3];
            for (int t = 3; t < testArgs.length; t++) {
                testThread.argsForTest[t - 3] = testArgs[t];
            }
            int l = testThread.argsForTest.length;
            if (l <= 1) {
                mode = 0;
                loopTest = 1;
            } else {
                if (mode == 0) {
                    loopTest = 1;
                } else {
                    loopTest = (Integer.valueOf(testThread.argsForTest[0]))
                            .intValue();
                    testThread.argsForTest[0] = Integer.toString(0);
                }
            }
        }

        ReliabilityRunner.debug("Start testFieldsLmt");
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
                    tThread[j] = new testThread(j);
                    tThread[j].start();
                }

                for (int j = 0; j < lmtThrd; j++) {
                    tThread[j].join();
                }

                for (int j = 0; j < lmtThrd; j++) {
                    if (tableResults[j] != (ReliabilityRunner.RESULT_PASS * loopTest)) {
                        ReliabilityRunner.debug("Failed thread: " + j + "  "
                                + tableResults[j] + " must be "
                                + (loopTest * ReliabilityRunner.RESULT_PASS));
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

class testThread extends Thread {

    private int index;

    public static String[] argsForTest;

    public testThread(int index) {
        this.index = index;
    }

    public void run() {
        test00 t = new test00();
        test00.cnt++;
        try {
            for (int i = 0; i < testFieldsLmt.loopTest; i++) {
                synchronized (test00.class) {
                    int ans = t.test1(argsForTest);
                    testFieldsLmt.tableResults[index] += ans;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            testFieldsLmt.tableResults[index] += 105;
        }

    }
}

class test00 extends testFieldsLmt_0 {

    public static int cnt = 0;

    public int test1(String[] aa) {
        return super.test1(aa);
    }

}
