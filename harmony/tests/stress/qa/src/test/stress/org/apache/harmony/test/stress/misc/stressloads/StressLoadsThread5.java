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

/* Wait and notify. */
public class StressLoadsThread5 extends Thread {
    int iterations;

    public StressLoadsThread5() {
        this.iterations = 100;
    }

    public StressLoadsThread5(int iterations) {
        this.iterations = iterations;
    }

    public void run() {
        Object waitObject = new Object(); // wait object

        try {
            while (true) {
                for (int i = 0; i < iterations; i++) {
                    try {
                        new StressLoadsThread5Thread(waitObject).start();
                        synchronized (waitObject) {
                            waitObject.notify();
                        }
                    } catch (OutOfMemoryError er) {
                    }
                }
                synchronized (waitObject) {
                    waitObject.notifyAll();
                }
            }
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread5 test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }
}

class StressLoadsThread5Thread extends Thread {
    Object waitObject;

    public void run() {
        try {
            synchronized (waitObject) {
                waitObject.wait(10000);
            }
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread5 test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }

    public StressLoadsThread5Thread(Object waitObject) {
        this.waitObject = waitObject;
    }
}
