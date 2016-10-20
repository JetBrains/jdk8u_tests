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

/* Synchronizing on private objects. */
public class StressLoadsThread4b extends Thread {
    int iterations;

    Thread threadsArray[];

    int i;

    public StressLoadsThread4b(int iterations) {
        this.iterations = iterations;
    }

    public StressLoadsThread4b() {
        this.iterations = 100;
    }

    public void run() {
        try {
            threadsArray = new Thread[iterations];

            while (true) {
                for (i = 0; i < iterations; i++) {
                    threadsArray[i] = new StressLoadsThread4bThread();
                    threadsArray[i].start();
                }

                for (i = 0; i < iterations; i++) {
                    threadsArray[i].join();
                }
            }
        } catch (OutOfMemoryError er) {
            ReliabilityRunner.debug("Warning: out of memory");
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread4b test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }
}

class StressLoadsThread4bThread extends Thread {
    private Object synchObject = new Object();

    public void run() {
        try {
            synchronized (synchObject) {
                synchObject.toString();
            }
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread4b test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }
}
