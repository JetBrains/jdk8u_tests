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

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;
import java.util.Random;

import org.apache.harmony.test.stress.misc.MiscTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.4 $
 */

public class MatrixCalcTest extends TestCase {

    /**
     * Number of columns/rows
     */
    private static final int N = 1000;

    /**
     * Number of threads to be created for calculations (optimal number is
     * number of physical concurrent execution units)
     */
    private static final int THREADS_COUNT = 4;

    /**
     * Utility method for creating random matrix
     */

    private static final Random rnd = new Random();

    /**
     * This method generates two random matrices and performs multiplication on
     * them in multiple threads, the result is compared with the correct result
     * calculated in a single thread
     */
    public void test() {
        int[][] m1;
        int[][] m2;
        int[][] res1;
        int[][] res2;
        long time1;
        long time2;
        ReliabilityRunner.debug("Generating m1...");
        m1 = generateMatrix();
        ReliabilityRunner.debug("Generating m2...");
        m2 = generateMatrix();
        ReliabilityRunner.debug("Calculating result in a single thread...");
        time1 = System.currentTimeMillis();
        res1 = calcSTResult(m1, m2);
        time1 = System.currentTimeMillis() - time1;
        ReliabilityRunner.debug("Calculating result in " + THREADS_COUNT
                + " threads...");
        time2 = System.currentTimeMillis();
        res2 = calcMTResult(m1, m2);
        time2 = System.currentTimeMillis() - time2;
        ReliabilityRunner.debug("ST time is " + time1 + "ms");
        ReliabilityRunner.debug("MT time is " + time2 + "ms");

        ReliabilityRunner.debug("Comparing the results...");

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (res1[i][j] != res2[i][j]) {
                    ReliabilityRunner.debug("Element at (" + i + ", " + j
                            + ") is invalid");
                    ReliabilityRunner.debug("Test failed");
                    ReliabilityRunner.mainTest
                            .addError(this, new MiscTestError());
                }
            }
        }
        ReliabilityRunner.debug("OK");
    }

    private int[][] generateMatrix() {
        int[][] res = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                res[i][j] = rnd.nextInt();
            }
        }
        return res;
    }

    /**
     * Calculate single-threaded result
     */
    private int[][] calcSTResult(int[][] m1, int[][] m2) {
        int[][] res = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int sum = 0;
                for (int k = 0; k < N; k++) {
                    sum += m1[i][k] + m2[k][j];
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    /**
     * Calculate the the result in multiple threads
     */
    private int[][] calcMTResult(final int[][] m1, final int[][] m2) {
        final int[][] res = new int[N][N];

        class Calc extends Thread {
            /**
             * threadId (with THREADS_COUNT)is used to determine which elements
             * of resulting matrix are calculated within this thread: threadId =
             * (i * N + j) % THREADS_COUNT
             */
            private int threadId;

            public Calc(int threadId) {
                super();
                this.threadId = threadId;
            }

            public void run() {
                int i = 0;
                int j = threadId;

                while (j >= N) { // If j is too big, decrease it by N
                    j -= N;
                    i++;
                }

                while (i < N) {
                    int sum = 0;
                    for (int k = 0; k < N; k++) {
                        sum += m1[i][k] + m2[k][j];
                    }
                    res[i][j] = sum;

                    j += THREADS_COUNT;

                    while (j >= N) {
                        j -= N;
                        i++;
                    }
                }
            }
        }

        Thread[] th = new Thread[THREADS_COUNT];

        for (int i = 0; i < THREADS_COUNT; i++) {
            th[i] = new Calc(i);
            th[i].start();
        }

        for (int i = 0; i < THREADS_COUNT; i++) {
            try {
                th[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}
