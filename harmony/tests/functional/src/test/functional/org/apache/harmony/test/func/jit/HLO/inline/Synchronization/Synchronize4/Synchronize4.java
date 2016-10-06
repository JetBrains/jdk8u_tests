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
package org.apache.harmony.test.func.jit.HLO.inline.Synchronization.Synchronize4;

import java.util.ArrayList;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 7.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Synchronize4 extends Test {
    
    int count;
    boolean flag = false;
    
    boolean error = false;
    static ArrayList check  = new ArrayList(); 
    
    public static void main(String[] args) {
        System.exit((new Synchronize4()).test());
    }

    public int test() {
        log.info("Start Synchronize4 test...");
        try {
            TestThread1 thread1 = new TestThread1(this);
            TestThread2 thread2 = new TestThread2(this);
            thread1.join();
            thread2.join();
            for(int i = 0; i < check.size(); i = i+2) {
                if (((Integer) check.get(i)).intValue() != 
                    ((Integer) check.get(i + 1)).intValue()) {
                        return fail("TEST FAILED: synchronization is broken");
                }
            }
            if (!error) return pass();
            else return fail("TEST FAILED: InterruptedException " +
                    "ocurred in some thread");
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }

    
    final synchronized void inlineMe2() throws InterruptedException {
        if(!flag) wait();
        check.add(new Integer(count));
        flag = false;
        notify();
    }
        
    final synchronized void inlineMe1(int n) throws InterruptedException {
        if(flag) wait();
        count = n;
        flag = true;
        check.add(new Integer(count));
        notify();
    }
    
    class TestThread1 extends Thread {
        
        Synchronize4 obj;
        
        TestThread1 (Synchronize4 test) {
            obj = test;
            start();
        }
        
        public void run() {
            for (int i=0; i<100; i++) {
                for (int j=i*100; j<i*100+100; j++) {
                    try {
                        obj.inlineMe1(j);
                    } catch (InterruptedException e) {
                        log.info("Unexpected " + e + " in thread 1");
                        log.add(e);
                        error = true;
                    }
                }
            }
        }
    }
    
    class TestThread2 extends Thread {
        
        Synchronize4 obj;
        
        TestThread2 (Synchronize4 test) {
            obj = test;
            start();
        }
        
        public void run() {
            for (int i=0; i<100; i++) {
                for (int j=i*100; j<i*100+100; j++) {
                    try {
                        obj.inlineMe2();
                    } catch (InterruptedException e) {
                        log.info("Unexpected " + e + " in thread 2");
                        log.add(e);
                        error=true;
                    }
                }
            }
        }
    }
}



