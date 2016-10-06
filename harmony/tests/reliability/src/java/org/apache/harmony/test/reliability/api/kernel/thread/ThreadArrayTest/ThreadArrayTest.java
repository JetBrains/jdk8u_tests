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
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.kernel.thread.ThreadArrayTest;

import org.apache.harmony.test.reliability.share.Test;

/**
 * Goal: check VM thread subsystem/GC cooperations 
 *  The test does: 
 *    1. Reads parameters, which are:
 *        param[0] - number of threads,
 *        param[1] - how times each thread starts
 *    2. Creates param[0]-sized array of threads and starts each of them
 *    3. call .join() for each thread 
 *    4. repeat steps 2-3 param[1] times
 *  No exceptions must be trown.
 *   
 */

public class ThreadArrayTest extends Test {

    public static int THREADS_COUNT = 128;
    public static int CYCLES_COUNT = 1000;

    public static int[] table;

    public static void main(String[] args) {
        System.exit(new ThreadArrayTest().test(args));
    }

    public int test(String[] params) {
        parseParams(params);
        table = new int[THREADS_COUNT];

        for (int i = 0; i < CYCLES_COUNT; i++) {
            Thread t[] = new Thread[THREADS_COUNT];
            for (int k = 0; k < t.length; k++) {
                t[k] = new ThreadTest(k);
                t[k].start();
            }
            try {
                for (int k = 0; k < t.length; k++) {
                    t[k].join();
                }
            } catch (InterruptedException ie) {
                return fail("interruptedException while join of threads");
            }
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            THREADS_COUNT = Integer.parseInt(params[0]);
            if (params.length >= 2) {
                CYCLES_COUNT = Integer.parseInt(params[1]);
            }
        }
    }

}


class ThreadTest extends Thread {
    private int index;

    public ThreadTest(int index) {
        this.index = index;
    }

    public void run() {
        ThreadArrayTest.table[index]++;
        new Object();
    }
}