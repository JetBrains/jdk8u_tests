/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/*
 * Created on 10.02.2005
 * Last modification G.Seryakova
 * Last modified on 10.02.2005
 * 
 * 
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_ThreadTest_06;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_ThreadTest_06 extends ScenarioTest {
    boolean isWaiting = true;
    String res = "";

    public static void main(String[] args) {
        System.exit(new F_ThreadTest_06().test());
    }

    class MyThread extends Thread {

        public MyThread(String name) {
            super(name);
        }

        public void run() {
            long i = 0;
            try {
                while (true) {
                    if (isWaiting) {
                        i++;
                        Runtime.getRuntime().maxMemory();
                        try {
                            ClassLoader.getSystemResources("uuu");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Runtime.getRuntime().totalMemory();
                    } else {
                        throw new InterruptedException();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                setRes(getName() + ", priority is " + getPriority()
                + ", number is " + i + " ");
            }
        }
    }
    
    private synchronized void setRes(String str) {
        res = res + str;
    }

    public int test() {
        Thread thr1 = new MyThread("Thread with max priority");
        thr1.setPriority(Thread.MAX_PRIORITY);
        Thread thr2 = new MyThread("Thread with min priority");
        thr2.setPriority(Thread.MIN_PRIORITY);
        thr1.start();
        thr2.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isWaiting = false;
        
        try {
            thr2.join();
            thr1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pass(res);
    }
}