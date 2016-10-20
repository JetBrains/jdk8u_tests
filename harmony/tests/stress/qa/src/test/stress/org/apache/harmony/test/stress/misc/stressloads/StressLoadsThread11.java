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

/* Show frame. Params should be ~(20, 1000) */
public class StressLoadsThread11 extends Thread {
    int numFrames;

    int sleepTime;

    java.awt.Frame frames[];

    int i;

    public StressLoadsThread11() {
        this.numFrames = 20;
        this.sleepTime = 1000;
    }

    public StressLoadsThread11(int numFrames, int sleepTime) {
        this.numFrames = numFrames;
        this.sleepTime = sleepTime;
    }

    public void run() {
        frames = new java.awt.Frame[numFrames];
        while (true) {
            try {
                for (i = 0; i < numFrames; i++) {
                    frames[i] = new java.awt.Frame();
                    frames[i].show();
                }

                try {
                    sleep(sleepTime);
                } catch (InterruptedException ex) {
                    ReliabilityRunner.mainTest.addError(
                            StressLoadsRunner.loadsRunnerTest, ex);
                    return;
                }

                for (i = 0; i < numFrames; i++) {
                    frames[i].hide();
                    frames[i] = null;
                }
            } catch (Throwable thr) {
                ReliabilityRunner.debug("StressLoadsThread11 test error");
                ReliabilityRunner.mainTest.addError(
                        StressLoadsRunner.loadsRunnerTest, thr);
                return;
            }
        }
    }
}
