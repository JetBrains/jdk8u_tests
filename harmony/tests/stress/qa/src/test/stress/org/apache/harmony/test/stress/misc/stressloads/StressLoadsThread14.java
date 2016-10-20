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
 * @author Alexander D. Shipilov
 * @version $Revision: 1.5 $
 */

package org.apache.harmony.test.stress.misc.stressloads;

import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.misc.MiscTestError;

/* Thread interruption. */

public class StressLoadsThread14 extends Thread {
    int number_of_threads;

    StressLoadsThread14Thread array_of_threads[];

    static int inter_count;

    int i;

    public StressLoadsThread14() {
        this.number_of_threads = 20;
    }

    public StressLoadsThread14(int number_of_threads) {
        this.number_of_threads = number_of_threads;
    }

    public void run() {
        try {
            while (true) {
                inter_count = 0;
                array_of_threads = new StressLoadsThread14Thread[number_of_threads];
                for (i = 0; i < number_of_threads; i++) {
                    array_of_threads[i] = new StressLoadsThread14Thread();
                }
                for (i = 0; i < number_of_threads; i++) {
                    array_of_threads[i].start();
                }
                for (i = 0; i < number_of_threads; i++) {
                    array_of_threads[i].join();
                }
                if (inter_count != number_of_threads) {
                    int j = 0;
                    ReliabilityRunner.debug(this.toString()
                            + ": Fail. inter_count==" + inter_count
                            + ", number_of_threads==" + number_of_threads);
                    ReliabilityRunner.mainTest.addError(StressLoadsRunner.loadsRunnerTest, new MiscTestError());
                    return;
                }
            }
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread14 test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }
}

class StressLoadsThread14Thread extends Thread {
    static int i;

    public void run() {
        try {
            for (int k = 0; k < 1000000; k++) {
                synchronized (StressLoadsThread14Synch.class) {
                    StressLoadsThread14Thread.i = StressLoadsThread14.inter_count;
                }
            }
            synchronized (StressLoadsThread14Synch.class) {
                StressLoadsThread14.inter_count++;
            }
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread14 test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }
}

class StressLoadsThread14Synch {
    static int i = 1;
}
