/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */
/*
 * Created on 13.01.2005
 *
 */
package org.apache.harmony.test.func.api.java.io.share;

/**
 *  
 */

import org.apache.harmony.share.DRLLogging;
import org.apache.harmony.share.Test;

public abstract class PerformanceTestFramework extends Test {
    static int ATOMIC_TESTS = 10000;

    static int TEST_SETS = 5;

    static boolean TEST_SPEED = true;
    
    static float MULTIPLICATOR = 1;

    protected void beforeTest(int tests) {
    }

    protected void afterTest(int tests) {
    }

    protected abstract int test(int tests);

    
    final public int test(String[] arg0, DRLLogging arg1) {
        parseArgs(arg0);
        return super.test(arg0, arg1);
    }

    final public int test(String[] arg0) {
        parseArgs(arg0);
        return super.test(arg0);
    }
    
    protected void parseArgs(String[] params) {
        if (params == null || params.length == 0) {
            return;
        }
        try {
            for (int i = 0; i < params.length; ++i) {
                 if ("-atomic".equalsIgnoreCase(params[i])) {
                    try {
                        ATOMIC_TESTS = Integer
                                .parseInt(params[++i]);
                    } catch (Throwable e) {
                        ATOMIC_TESTS = 1000000;
                    }
                } else if ("-sets".equalsIgnoreCase(params[i])) {
                    try {
                        TEST_SETS = Integer
                                .parseInt(params[++i]);
                    } catch (Throwable e) {
                        TEST_SETS = 5;
                    }
                } else if ("-mode".equalsIgnoreCase(params[i])) {
                    TEST_SPEED = "speed".equals(params[++i]);
                } else if ("-multiplicator".equalsIgnoreCase(params[i])) {
                    try {
                        MULTIPLICATOR = Float.parseFloat(params[++i]);
                    } catch (Throwable e) {
                        MULTIPLICATOR = 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Unexpected exception " + e);
        }
    }
    
    protected void onAfterAllTests(int tests) {
    }
    
    protected void onBeforeAllTests(int tests) {
    }
    

    public final int test() {
        long[] results = new long[TEST_SETS];
        
        int tests = Math.round(ATOMIC_TESTS * MULTIPLICATOR);
        
        onBeforeAllTests(tests);
        
        for (int i = 0; i < TEST_SETS; ++i) {
            beforeTest(tests);
            long initial = 0;
            MemoryThread mt = new MemoryThread();
            if (TEST_SPEED) {
                initial = getTimeInMillis();
            } else {
                initial = MemoryThread.getProcessMemory();
                mt.start();
            }
            test(tests);
            if (TEST_SPEED) {
                results[i] = getTimeInMillis() - initial;
            } else {
                mt.requestStop();
                try {
                    mt.join();
                } catch (InterruptedException e) {
                    return fail("error joining memory thread");
                }
                results[i] = mt.getMaxMemory() - initial;
            }

            //            System.err.println("result " + i + ": " + results[i]);
            afterTest(tests);
        }
        
        onAfterAllTests(tests);

        if (TEST_SPEED) {
            System.out.println(getClass().getName());
            System.out.println("repetitions is:\t" + TEST_SETS);
            System.out.println("quantity is:\t" + tests);
            for (int i = 0; i < TEST_SETS; ++i) {
                System.out.println("step:\t" + i + "\tres:\t" + results[i]);
            }
            //performance
            // tests
            // can't
            // fail

            return pass("AvgDuration: " + calculateAverage(results));

        } else {
            System.out.println(getClass().getName());
            System.out.println("repetitions is:\t" + TEST_SETS);
            System.out.println("quantity is:\t" + tests);
            for (int i = 0; i < TEST_SETS; ++i) {
                System.out.println("step:\t" + i + "\tres:\t" + results[i]);
            }
            //performance
            // tests
            // can't
            // fail

            return pass("AvgMemory: " + calculateAverage(results));
        }
    }

    protected static long calculateAverage(long[] results) {
        //hope there will be no overflow
        //TODO: throw away too outstanding results
        System.err.println("results length is: " + results.length);
        long avg = 0;
        for (int i = 0; i < results.length; ++i) {
            avg += results[i];
        }
        avg /= results.length;
        return avg;
    }

    private final static synchronized long getTimeInMillis() {
        return System.currentTimeMillis();
    }
}

class MemoryThread extends Thread {
    private long maxMemory = getProcessMemory();

    public static long getProcessMemory() {
        return Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();

    }

    private boolean stopped = false;

    public void run() {
        stopped = false;

        while (!stopRequested()) {
            long currentMemory = getProcessMemory();
            if (maxMemory < currentMemory) {
                maxMemory = currentMemory;
            }
            try {
                sleep(1);
            } catch (InterruptedException e) {
                stopped = true;
            }
        }
    }

    public synchronized void requestStop() {
        stopped = true;
    }

    private synchronized boolean stopRequested() {
        return stopped;
    }

    /**
     * @return Returns the maxMemory.
     */
    public long getMaxMemory() {
        if (isAlive()) {
            throw new IllegalStateException("thread is alive");
        }
        return maxMemory;
    }
}
