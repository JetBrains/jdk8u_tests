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
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class WaitNotifyTest extends TestCase {

    private static Object lock1 = new Object();

    private static Object lock2 = new Object();

    private static final int THREADS_COUNT = 200;

    private static final int REPEAT_COUNT = 50;

    private static class TestThread extends Thread {
        public void run() {
            synchronized (lock2) {
                synchronized (lock1) {
                    lock1.notify();
                }
                try {
                    lock2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void test() {
        for (int i = 0; (REPEAT_COUNT < 0) || (i < REPEAT_COUNT); i++) {
            ReliabilityRunner.debug("i=" + i);
            try {
                for (int j = 0; j < THREADS_COUNT; j++) {
                    synchronized (lock1) {
                        (new TestThread()).start();
                        lock1.wait();
                    }
                }
                synchronized (lock2) {
                    lock2.notifyAll();
                }
            } catch (InterruptedException e) {
                ReliabilityRunner.debug(e.toString());
                ReliabilityRunner.mainTest.addError(this, e);
            }
        }
    }
}
