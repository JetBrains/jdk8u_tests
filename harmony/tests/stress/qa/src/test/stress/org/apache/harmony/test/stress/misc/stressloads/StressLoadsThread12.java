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

/* Throwing random exceptions. */
public class StressLoadsThread12 extends Thread {
    static Exception ex = new Exception();

    static java.util.Random rand = new java.util.Random();

    int rec;

    public StressLoadsThread12() {
        this.rec = 200;
    }

    public StressLoadsThread12(int rec) {
        this.rec = rec;
    }

    public void run() {
        while (true) {
            try {
                recursiveFunc(rec);
            } catch (Exception ex) {
            } catch (Error er) {
                ReliabilityRunner.debug("StressLoadsThread12 test error");
                ReliabilityRunner.mainTest.addError(
                        StressLoadsRunner.loadsRunnerTest, er);
                return;
            }
        }
    }

    void recursiveFunc(int rec) throws Exception {
        if (rec == 0) {
            throw throwRandomException();
        }
        recursiveFunc(rec - 1);
    }

    Exception throwRandomException() throws Exception {
        if (rand.nextBoolean() == true)
            return this.ex;
        else
            return new Exception();
    }
}
