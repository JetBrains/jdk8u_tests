/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Igor A. Pyankov
 * @version $Revision: 1.6 $
 */

package org.apache.harmony.test.reliability.api.kernel.thread.VolatileVariableTest;

import org.apache.harmony.test.reliability.share.Test;

/**
 * Goal: check that VM supports volatile variables.
 * 
 * The test implements well-known concurrent programming algorithm for mutual
 * exclusion by T.J.Dekker. (http://en.wikipedia.org/wiki/Dekker's_algorithm)
 * 
 * The test does: 1. Reads parameter, which is: param[0] - number of iterations
 * to run critical region in each thread 2. Starts and, then, joins all started
 * threads
 * 
 * 3. Checks that in each thread all checks PASSed.
 * 
 * 4. Each thread, being started: a. Runs param[0] iterations in a cycle, on
 * each iteration: b. Checks the flag c. Changes flag when receive control d.
 * Runs critical regoin of code
 * 
 */
public class DekkerTest extends Test {
    public int NUMBER_OF_ITERATIONS = 100;

    public int[] statuses = { DStatus.PASS, DStatus.PASS };

    public volatile long commonVar = 0;

    public static volatile int started;

    public static void main(String[] args) {
        System.exit(new DekkerTest().test(args));
    }

    public int test(String[] params) {
        parseParams(params);

        Dekkerthread th0 = new Dekkerthread("0", this);
        Dekkerthread th1 = new Dekkerthread("1", this);
        started = 0;
        th0.start();
        th1.start();
        while (started < 2) {
        }
        ;

        try {
            th0.join();
            // log.add("Thread #0 joined()");
            th1.join();
            // log.add("Thread #1 joined()");
        } catch (InterruptedException ie) {
            return fail("interruptedException while join of threads");
        }

        // For each thread check whether operations PASSed in the thread
        for (int i = 0; i < statuses.length; ++i) {
            if (statuses[i] != DStatus.PASS) {
                return fail("Status of thread " + i + ": is FAIL");
            }
            // log.add("Status of thread " + i + ": is PASS");
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }
    }

}

class Dekkerthread extends Thread {
    volatile static boolean flag0 = false;

    volatile static boolean flag1 = false;

    volatile static int worker; // working thread number

    static long[] vars = { 0x00000000FFFFFFFFL, 0xFFFFFFFF00000000L };

    private int threadNum;

    private int threadIteration;

    private DekkerTest base;

    public Dekkerthread(String arg, DekkerTest test) {
        super();
        if (arg.equals("0")) {
            threadNum = 0;
        } else {
            threadNum = 1;
        }
        threadIteration = test.NUMBER_OF_ITERATIONS;
        base = test;
    }

    /*
     * check and close (if posible) semaphore before critical region of code
     */
    public void regionIn(int current) {
        if (current == 0) {
            flag0 = true;
        } else {
            flag1 = true;
        }

        int j = 1 - current;
        if (j == 0) {
            while (flag0) {
                if (current != worker) {
                    if (current == 0) {
                        flag0 = false;
                    } else {
                        flag1 = false;
                    }
                    while (current != worker) {
                    }
                    if (current == 0) {
                        flag0 = true;
                    } else {
                        flag1 = true;
                    }
                }
            }
        } else {
            while (flag1) {
                if (current != worker) {
                    if (current == 0) {
                        flag0 = false;
                    } else {
                        flag1 = false;
                    }
                    while (current != worker) {
                    }
                    if (current == 0) {
                        flag0 = true;
                    } else {
                        flag1 = true;
                    }
                }
            }
        }

    }

    /* open semaphore after critical region of code */
    public void regionOut(int current) {
        worker = 1 - current; // change working thread
        if (current == 0) {
            flag0 = false;
        } else {
            flag1 = false;
        }
    }

    public void run() {

        // base.log.add("Thread #" + threadNum + " started " + threadIteration);
        DekkerTest.started++;
        while (threadIteration-- > 0) {
            regionIn(threadNum);
            // critical region

            base.commonVar = vars[threadNum];
            Thread.yield(); // to give a chance to another thread
            if (base.commonVar != vars[0] &&
                base.commonVar != vars[1]) {
                base.statuses[threadNum] = DStatus.FAIL;
                regionOut(threadNum);
                return;
            }
            // end of critical region
            regionOut(threadNum);
        }

        // base.log.add("Thread #" + threadNum + " finished ");
    }
}

class DStatus {
    public static final int FAIL = -10;

    public static final int PASS = 10;
}
