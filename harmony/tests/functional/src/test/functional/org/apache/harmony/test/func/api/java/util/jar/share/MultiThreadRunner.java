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
 * Created on 01.12.2004
 *
 */
package org.apache.harmony.test.func.api.java.util.jar.share;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.Test;

/**
 *  
 */
public class MultiThreadRunner {
    public static int run(IOMultiCase runned, String[] args) {
        runned.parseArgs(args);
//        if(!(runned instanceof BufferedInputStreamTest)) {
//            return Result.PASS; 
//        }
        
        if (Utils.THREADS <= 1) {
            return runned.test(args);
        }

        Thread[] children = new Thread[Utils.THREADS];
        int results[] = new int[Utils.THREADS];

        for (int i = 0; i < Utils.THREADS; ++i) {
            children[i] = new ThreadTest(runned, args, results, i);
            children[i].start();
        }

        String failmsg = null;
        for (int i = 0; i < Utils.THREADS; ++i) {
            try {
                children[i].join();
            } catch (InterruptedException e) {
                failmsg = e.getMessage();
            }
        }

        if (failmsg != null) {
            return runned.fail(failmsg);
        }

        for (int i = 0; i < Utils.THREADS; i++) {
            if (results[i] != Result.PASS) {
                return runned.fail("something is wrong");
            }
        }

        return runned.pass();
    }
    
    //multi-threaded tests' threads should try to enter (de)serialization
    // section simultaneously
    //put a call of this method just before readObject() or writeObject() call.
    static Object barrier = new Object();

    static int threadsAtBarrier = 0;
    static int threadsLeftBarrier = 0;

    public static void waitAtBarrier() {
        waitAtBarrier(Utils.THREADS);
    }
    
    public static void waitAtBarrier(int threads) {
        if (threads <= 1)
            return;
//        System.err.println(Thread.currentThread().getName()
//                + " waiting at the barrier, total threads to meet : "
//                + THREADS);
        synchronized (barrier) {
            while (threadsLeftBarrier > 0) {
//                System.err.println(Thread.currentThread().getName()
//                        + " waiting leaving threads");
                
                try {
                    barrier.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            threadsAtBarrier++;
            if (threadsAtBarrier == threads) {
                barrier.notifyAll();
            } else
                while (threadsAtBarrier < threads) {
//                    System.err.println(Thread.currentThread().getName()
//                            + " here 1");
                    try {
//                        System.err.println(Thread.currentThread().getName()
//                                + " here 2");
                        barrier.wait();
//                        System.err.println(Thread.currentThread().getName()
//                                + " here 3");
                    } catch (InterruptedException e) {
                        System.err.println("thread interrupted"
                                + e.getMessage());
                    }
//                    System.err.println(Thread.currentThread().getName()
//                            + " here 4");
            }
            threadsLeftBarrier++;
//            System.err.println(Thread.currentThread().getName()
//                    + " here 5");
            if (threadsLeftBarrier == threads) { //cleanup for the next barrier
//                System.err.println(Thread.currentThread().getName()
//                        + " here 6");

                threadsLeftBarrier = 0;
                threadsAtBarrier = 0;
                barrier.notifyAll();
            }
        }
    }
    
}

class ThreadTest extends Thread {
    int threadNo;

    int[] results;

    Test runned;

    String[] args;

    public ThreadTest(Test runned, String[] args, int[] results, int threadNo) {
        super();
        this.runned = runned;
        this.args = args;
        this.results = results;
        this.threadNo = threadNo;
    }

    public void run() {
        results[threadNo] = runned.test(args);
    }
}