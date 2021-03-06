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

/* Suspend and resume. */
public class StressLoadsThread15 extends Thread {

    public void run() {
        StressLoadsThread15Thread threadToSuspend;
        while (true) {
            try {
                threadToSuspend = new StressLoadsThread15Thread();
                threadToSuspend.start();
                threadToSuspend.suspend();
                threadToSuspend.resume();
                threadToSuspend.interrupt();
            } catch (OutOfMemoryError er) {
            } catch (Throwable thr) {
                ReliabilityRunner.debug("StressLoadsThread15 test error");
                ReliabilityRunner.mainTest.addError(
                        StressLoadsRunner.loadsRunnerTest, thr);
                return;
            }
        }
    }
}

class StressLoadsThread15Thread extends Thread {
    static int i = 0;

    public void run() {
        try {
            while (true) {
                if (Thread.interrupted())
                    break;
                this.i = 1;
            }
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread15 test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }
}
