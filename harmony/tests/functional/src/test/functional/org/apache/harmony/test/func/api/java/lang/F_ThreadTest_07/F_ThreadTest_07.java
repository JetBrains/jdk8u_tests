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
 * Last modified on 02.03.2005
 * 
 * Test for set daemon for threads.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_ThreadTest_07;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Test for set daemon for threads.
 * 
 */
public class F_ThreadTest_07 extends ScenarioTest {
    int     res            = 0;

    public static void main(String[] args) {
        System.exit(new F_ThreadTest_07().test());
    }

    class MyThread extends Thread {
        boolean parentIsDaemon = false;
        boolean isDaemon;

        public MyThread(String name, boolean isDaemon) {
            super(name);
            this.isDaemon = isDaemon;
        }

        public void run() {
            if (isDaemon() ^ isDaemon) {
                log.info("Parent thread (" + getName()
                    + ") isDaemon() must be:" + isDaemon + ", it is: "
                    + isDaemon());
                res++;
            } else {
                log.info("Parent thread is daemon:" + isDaemon());
            }

            parentIsDaemon = isDaemon();
            Thread child = new Thread() {
                public void run() {
                    log.info("IN CHILD: child thread (" + getName()
                        + ") is daemon:" + isDaemon() + ", must be "
                        + parentIsDaemon);
                    if (isDaemon() ^ parentIsDaemon) {
                        res++;
                    }
                }
            };
            child.setName("Child for " + getName());
            child.start();
        }
    }

    public int test() {
        Thread thr1 = new MyThread("Thread is not daemon.", false);
        thr1.setDaemon(false);
        thr1.start();

        Thread thr2 = new MyThread("Thread is daemon.", true); 
        thr2.setDaemon(true);
        thr2.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
        }
        
        if (res > 0) {
            return fail("");
        } else {
            return pass();
        }
    }
}