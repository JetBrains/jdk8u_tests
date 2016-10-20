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
package org.apache.harmony.test.stress.threads;

import org.apache.harmony.test.stress.threads.share.StressTestParameters;
import org.apache.harmony.test.stress.threads.share.StressTestThreadSO;

import org.apache.harmony.share.Test;
import org.apache.harmony.share.DRLLogging;

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.3 $
 */

public class StressWaitNotifyTest extends Test {

    /**
     * Fail flag.
     */
    static boolean failFlag = false;

    /**
     * String with fail info.
     */
    static String failInfo;

    /**
     * Threads will be register in this array that they worked fine.
     */
    static int verificationArray[];

    /**
     * Array of threads.
     */
    static StressWaitNotifyTestThread threads[];

    /**
     * Ballast object to prevent repeated OOME.
     */
    static Object ballastObj = new int[100][100][100];

    public int test() {
        return test(testArgs);
    }

    /**
     * Method parses 6 parameters (String[] args).<br/> -numThreads, number of
     * concurrent threads<br/> -smallObjSize, size of small padding objects<br/>
     * -freeMem, quantity of free memory in small objects<br/> -oOMEr (on or
     * off), OutOfMemoryError (low memory conditions)<br/> -sOEr (on or off),
     * StackOverflowError (dedicated thread throws SOE)<br/> -iterations,
     * number of iterations<br/>
     */
    public int test(String[] args) {

        /**
         * Class with parameters.
         */
        StressTestParameters params = new StressTestParameters(log);

        /**
         * Object to wait on.
         */
        Object waitObject;

        /* Parse parameters. */
        if (!params.parse(args)) {
            return error("Unexpected error during parsing parameters");
        }

        try {
            verificationArray = new int[params.numThreads];
            for (int k = 0; k < params.numThreads; k++) {
                verificationArray[k] = 0;
            }
        } catch (OutOfMemoryError er) {
            ballastObj = null;
            return error("OutOfMemoryError during creating verificationArray."
                    + " Try to decrease number of threads.");
        }

        /* Log params to the output. */
        printParams(params);

        /* Create arrays. */
        try {
            threads = new StressWaitNotifyTestThread[params.numThreads];
        } catch (OutOfMemoryError er) {
            ballastObj = null;
            return error("OutOfMemoryError during creating threads array."
                    + " Try to decrease number of threads.");
        }

        /* Create threads. */
        try {
            for (int i = 0; i < params.numThreads; i++) {
                threads[i] = new StressWaitNotifyTestThread(i, params);
            }
        } catch (OutOfMemoryError er) {
            ballastObj = null;
            return error("OutOfMemoryError during creating threads."
                    + " Try to decrease number of threads.");
        }

        log.info("Threads created (ok)");

        /* StackOverflow conditions if needed. */
        if (params.sOEr == 1) {
            try {
                StressTestThreadSO sOThread = new StressTestThreadSO(log);
                sOThread.setDaemon(true);
                sOThread.start();
            } catch (Throwable t) {
                ballastObj = null;
                return error("Unexpected error" + t.toString());
            }
        }

        /* Pad memory if needed. */
        if (params.oOMEr == 1) {
            try {
                StressTestParameters.padMemory(params.smallObjSize,
                        params.freeMem);
            } catch (Throwable t) {
                ballastObj = null;
                return error("Unexpected error" + t.toString());
            }
        }

        /* Start threads. */
        try {
            for (int i = 0; i < params.numThreads; i++) {
                threads[i].start();
            }
        } catch (OutOfMemoryError er) {
            ballastObj = null;
            return error("OutOfMemoryError in main thread during starting"
                    + "threads - try to release more memory");
        }

        /* Join to all threads. */

        /* Almost excepts OutOfMemoryError. */
        while (true) {
            try {
                for (int j = 0; j < params.numThreads; j++) {
                    threads[j].join();
                }
                break;
            } catch (OutOfMemoryError er) {
            } catch (Throwable t) {
                return error("Error: " + t.toString());
            }
        }

        /* Release resources. */
        StressTestParameters.releaseMemory();

        /* Check fail flag */
        if (failFlag) {
            return fail(failInfo);
        }

        /* Check register array. It should contain all 1. */
        if (StressTestParameters.checkVerifArray(verificationArray,
                params.numThreads)) {
            return pass("Test pass");
        } else {
            return fail("Test fail");
        }
    }

    /**
     * Method logging input parameters to the output.
     */
    private int printParams(StressTestParameters p) {
        log.info("Number of threads: " + p.numThreads);
        log.info("Size of small padding objects: " + p.smallObjSize);
        log.info("Free memory (in int[" + p.smallObjSize + "]: " + p.freeMem);
        log.info("Iterations number: " + p.iterations);

        if (p.oOMEr == 0) {
            log.info("Low memory conditions off");
        } else if (p.oOMEr == 1) {
            log.info("Low memory conditions on");
        } else {
            return error("Wrong oOMEr parameter");
        }

        if (p.sOEr == 0) {
            log.info("Stack overflow conditions off");
        } else if (p.sOEr == 1) {
            log.info("Stack overflow conditions on");
        } else {
            return error("Wrong sOEr parameter");
        }

        return 0;
    }

    public static void main(String[] args) {
        System.exit(new StressWaitNotifyTest().test(args));
    }
}

/**
 * Thread class.
 */

class StressWaitNotifyTestThread extends Thread {

    int currentThread; // number of current thread

    StressTestParameters p; // Class with parameters

    /**
     * Constructor
     */
    public StressWaitNotifyTestThread(int currentThread, StressTestParameters p) {
        this.currentThread = currentThread; // number of current thread
        this.p = p; // Class with parameters
    }

    public void run() {
        if ((currentThread % 2) == 0) { // waiting thread
            try {
                for (int i = 0; i < p.iterations; i++) {
                    synchronized (this) {
                        this.wait(10);
                    }

                    synchronized (StressWaitNotifyTest.threads[currentThread + 1]) {
                        StressWaitNotifyTest.threads[currentThread + 1]
                                .notify();
                    }
                }

                /* Store 1 into result array. */
                StressWaitNotifyTest.verificationArray[currentThread] = 1;

            } catch (OutOfMemoryError er) {
                StressWaitNotifyTest.ballastObj = null;
                error("OutOfMemoryError in one of threads. Try to increase"
                        + " quantity of free memory.");
                return;
            } catch (Throwable t) {
                StressWaitNotifyTest.ballastObj = null;
                error("Unexpected error in one of threads: " + t.toString());
                return;
            }
        } else {
            try {
                for (int i = 0; i < p.iterations; i++) {
                    synchronized (StressWaitNotifyTest.threads[currentThread
                            + -1]) {
                        StressWaitNotifyTest.threads[currentThread + -1]
                                .notify();
                    }

                    synchronized (this) {
                        this.wait(10);
                    }
                }

                /* Store 1 into result array. */
                StressWaitNotifyTest.verificationArray[currentThread] = 1;

            } catch (OutOfMemoryError er) {
                StressWaitNotifyTest.ballastObj = null;
                error("OutOfMemoryError in one of threads. Try to increase"
                        + " quantity of free memory.");
                return;
            } catch (Throwable t) {
                StressWaitNotifyTest.ballastObj = null;
                error("Unexpected error in one of threads: " + t.toString());
                return;
            }
        }
    }

    private void error(String failInfo) {
        StressWaitNotifyTest.failFlag = true;
        StressWaitNotifyTest.failInfo = failInfo;
        return;
    }
}
