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

package org.apache.harmony.test.share.stress;

import java.io.PrintStream;
import java.util.Hashtable;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;

import org.apache.harmony.test.share.stress.util.Parser;

/**
 * Class <code>ReliabilityRunner</code> runs a stress test scenario built from
 * simple building blocks. It receives a list of activities via command line or
 * <code>org.apache.harmony.test.ReliabilityRunner.params</code> property.
 * 
 * Timeouts in seconds are specified by the properties
 * <code>org.apache.harmony.test.ReliabilityRunner.timeToWork</code>,
 * <code>org.apache.harmony.test.ReliabilityRunner.timeToAbort</code>. After
 * the first timeout is expired, tests are signaled to terminate gracefully. VM
 * is aborted after the expiration of the second timeout.
 * 
 * Decorator syntax is the following: FIXME
 * 
 * @author Alexander D. Shipilov, Alexei Fedotov
 * @version $Revision: 1.14 $
 * 
 */
public class ReliabilityRunner extends TestCase implements TestListener {
    public static final int RESULT_PASS = 104; // FIXME shouldn't be public ?

    public static final int RESULT_FAIL = 105;

    public static final int RESULT_ERROR = 106;

    static final String PARAM_PREFIX = ReliabilityRunner.class.getName();

    static final String PARAM_PROPERTY = PARAM_PREFIX + ".params";

    private static final PrintStream log = System.out;

    public static TestListener mainTest;

    static boolean debug = true;

    /**
     * Main method to run ReliabilityRunner in a standalone mode.
     * 
     * @param args
     *            an array of command line arguments
     */
    public static void main(String[] args) {
        System.exit((new ReliabilityRunner()).test(args));
        // new ReliabilityRunner().test(args);
    }

    /**
     * Encapsulates DRL harness and JUnit reporting specific features.
     */
    public int test(String[] args) {
        /*
         * If VM arguments doesn't contain scenario, construct string of
         * parameters from program arguments string.
         */
        if (System.getProperty(PARAM_PROPERTY) == null) {
            StringBuffer params = new StringBuffer(args[0]);
            for (int i = 1; i < args.length; i++) {
                params.append(" ");
                params.append(args[i]);
            }
            debug("Test parameters = " + params.toString());
            System.setProperty(PARAM_PROPERTY, params.toString());
        }

        long startTime = System.currentTimeMillis();
        result = new TestResult();
        result.addListener(this);
        mainTest = this;
        run(result);

        if (result.wasSuccessful()) {
            debug("Test passed, time = "
                    + (System.currentTimeMillis() - startTime));
            return RESULT_PASS;
        } else {
            debug("Test failed");
            return RESULT_FAIL;
        }
    }

    /**
     * Report failure and terminate VM.
     */
    public void addFailure(Test test, AssertionFailedError t) {
        log.println("[failure] in " + test);
        t.printStackTrace(log);
        System.exit(RESULT_FAIL);
    }

    /**
     * Report error and terminate VM.
     */
    public void addError(Test test, Throwable t) {
        log.println("[error] in " + test);
        t.printStackTrace(log);
        System.exit(RESULT_ERROR);
    }

    public static void debug(String msg) {
        if (debug) {
            log.println("[debug] " + msg);
        }
    }

    /**
     * Called on test launch.
     */
    public void startTest(Test test) {
        debug("started " + test);
    }

    /**
     * Called on test completion.
     */
    public void endTest(Test test) {
        debug("completed " + test);
    }

    /**
     * JUnit test entry point. Receives parameters via property
     * <code>PARAM_PROPERTY</code>. Starts time keeping, parses parameters
     * and launches a generator.
     * 
     */
    public void runTest() {
        /* Get parameters string */
        String params = System.getProperty(PARAM_PROPERTY);
        assertNotNull(PARAM_PROPERTY + " doesn't contain parameters", params);

        /* Start time keeping thread */
        TimeKeeper tk = new TimeKeeper();
        tk.start();

        /* Generate import list */
        String paramsWithoutPackages = Parser.generatePackageList(params);

        /* Run threads */
        storage.put(java.lang.Thread.currentThread(), paramsWithoutPackages);
        (new org.apache.harmony.test.share.stress.generator.Thread())
                .run(result);

        joinAll();
        debug("Tests completed greacefully");
        tk.interrupt();
    }

    void joinAll() {
        boolean continueJoin;
        int numBefore, numAfter;
        Thread threadListBefore[], threadListAfter[];
        do {
            try { // time to start testing threads
                Thread.sleep(100);
            } catch (Throwable t) {

            }

            continueJoin = false;
            numBefore = Thread.currentThread().getThreadGroup().activeCount();
            threadListBefore = new Thread[numBefore];
            numBefore = Thread.enumerate(threadListBefore);

            for (int i = 0; (i < threadListBefore.length) && (i < numBefore); i++) {
                Thread t = threadListBefore[i];
                if ((t == Thread.currentThread()) || t.isDaemon()) {
                    continue;
                } else {
                    try {
                        t.join();
                    } catch (InterruptedException ie) {
                    }
                }
            }

            /* Check for newly created threads */
            numAfter = Thread.currentThread().getThreadGroup().activeCount();
            threadListAfter = new Thread[numAfter];
            numAfter = Thread.enumerate(threadListAfter);

            boolean accountedThread;
            for (int i = 0; i < numAfter; i++) {
                accountedThread = false;
                for (int j = 0; j < numBefore; j++) {
                    if (threadListAfter[i] == threadListBefore[j]) {
                        accountedThread = true;
                    }
                }
                if (!accountedThread) {
                    continueJoin = true;
                    break;
                }
            }
        } while (continueJoin);
    }

    /**
     * List of package names to put near class names.
     */
    public static String[] packageList = null;

    /**
     * Synchronized storage.
     */
    public static Hashtable storage = new Hashtable();

    /**
     * Accumulates test results. Signals when the test should terminate. This
     * object is shared between all testcases.
     */
    public static TestResult result;

    /**
     * Get reliability test state. FIXME should be replaced with result?
     * 
     * @return true if reliability test is active; false otherwise.
     */
    public synchronized static boolean isActive() {
        return !result.shouldStop();
    }

    /**
     * The class creates a thread which runs for a given period of time and then
     * terminates the VM.
     * 
     * @author Alexander D. Shipilov
     * @version $Revision: 1.14 $
     * 
     */
    private class TimeKeeper extends java.lang.Thread {
        private final int DEFAULT_SECS_TO_WORK = 100;

        private final int DEFAULT_SECS_TO_ABORT = 600;

        private long timeToWork;

        private long timeToAbort;

        TimeKeeper() {
            /* Get time to work */
            timeToWork = Integer.getInteger(PARAM_PREFIX + ".timeToWork",
                    DEFAULT_SECS_TO_WORK).intValue();

            /* Get time to abort */
            timeToAbort = Integer.getInteger(PARAM_PREFIX + ".timeToAbort",
                    DEFAULT_SECS_TO_ABORT).intValue();
            debug("timeToWork = " + timeToWork + ", timeToAbort = "
                    + timeToAbort);
            setDaemon(true);
        }

        public void run() {
            try {
                sleep(timeToWork * 1000);
                debug("Stopping the test");
                result.stop();
                sleep(timeToAbort * 1000);
            } catch (InterruptedException exc) {
                debug("TimeKeeper is interrupted");
                return;
            }
            /*
             * Terminate VM unless all test threads completed gracefully.
             */
            // fail("Cannot stop all threads, possibly deadlock occurs. VM will
            // be aborted.");
            debug("Cannot stop all threads, possibly deadlock occurs. VM will be aborted.");
            System.exit(RESULT_ERROR);
        }
    }
}
