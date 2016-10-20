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

/* Throwing exception intra-method (shallow unwinding). */
public class StressLoadsThread1b extends Thread {

    public void run() {
        this.loadStress();
    }

    public void loadStress() {
        try {
            while (true) {
                try {
                    this.throwEx();
                } catch (Exception ex) {
                }
                try {
                    this.throwNPE();
                } catch (NullPointerException ex) {
                }
                try {
                    this.throwAIOOBE();
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
                try {
                    this.throwAE();
                } catch (ArithmeticException ex) {
                }
            }
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread1b test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }

    public void throwEx() throws Exception {
        throw new Exception();
    }

    public void throwNPE() throws NullPointerException {
        Object obj = null;
        obj.toString();
    }

    public void throwAIOOBE() throws ArrayIndexOutOfBoundsException {
        int[] arr = new int[1];
        arr[-1] = 1;
    }

    public void throwAE() throws ArithmeticException {
        int i = 1 / 0;
    }
}
