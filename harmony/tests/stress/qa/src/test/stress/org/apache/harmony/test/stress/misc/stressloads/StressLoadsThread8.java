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
 * @author Vladimir Nenashev
 * @version $Revision: 1.5 $
 */

package org.apache.harmony.test.stress.misc.stressloads;

import org.apache.harmony.test.stress.misc.stressloads.StressLoadsRunner;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class StressLoadsThread8 extends Thread {

    private native long malloc(int size);

    private native void free(long pointer);

    private static final int MAX_SIZE = 1000000;

    private static java.util.Random rnd = new java.util.Random();

    public void run() {
        try {
            while (true) {
                int size = rnd.nextInt(MAX_SIZE);
                long ptr = malloc(size);
                Thread.sleep(50);
                free(ptr);
            }
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread8 test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }

    static {
        System.loadLibrary("jnitests");
    }
}
