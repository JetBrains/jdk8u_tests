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
 * @version $Revision: 1.8 $
 */

package org.apache.harmony.test.stress.misc.stressloads;

import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.CommonClassLoader;

/* Creating class loaders and loading classes. */
public class StressLoadsThread6 extends Thread {
    int quickCreationNumber; // number of class loaders which be created quick

    int classLoadersNumber; // number of class loaders which be created at all

    int sleepTime; // time to sleep between class loaders creation in

    // milliseconds.

    public StressLoadsThread6(int quickCreationNumber, int classLoadersNumber,
            int sleepTime) {
        this.quickCreationNumber = quickCreationNumber;
        this.classLoadersNumber = classLoadersNumber;
        this.sleepTime = sleepTime;
    }

    public StressLoadsThread6() {
        this.quickCreationNumber = 1000;
        this.classLoadersNumber = 2000;
        this.sleepTime = 100;
    }

    public void run() {

        try {
            // quick creation
            for (int i = 0; i < quickCreationNumber; i++) {
                new CommonClassLoader().loadClass(
                        "org/apache/harmony/test/stress/misc/"
                                + "stressloads/StressLoadsThread6Class.class",
                        "org.apache.harmony.test.stress.misc."
                                + "stressloads.StressLoadsThread6Class");
            }

            // slow creation
            for (int i = quickCreationNumber; i < classLoadersNumber; i++) {
                new CommonClassLoader().loadClass(
                        "org/apache/harmony/test/stress/misc/"
                                + "stressloads/StressLoadsThread6Class.class",
                        "org.apache.harmony.test.stress.misc."
                                + "stressloads.StressLoadsThread6Class");
                sleep(sleepTime);
            }
        } catch (OutOfMemoryError er) {
            ReliabilityRunner.debug("Warning: out of memory");
            return;
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread6 test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
            return;
        }
    }
}
