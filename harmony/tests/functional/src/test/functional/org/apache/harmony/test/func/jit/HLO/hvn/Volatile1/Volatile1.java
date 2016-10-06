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
package org.apache.harmony.test.func.jit.HLO.hvn.Volatile1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 27.07.2006
 */

public class Volatile1 extends Test {
    
    public static void main(String[] args) {
        System.exit((new Volatile1()).test(args));
    }
        
    public int test() {
        log.info("Start Volatile1 test...");
        TestThread1 thread1 = new TestThread1();
        TestThread2 thread2 = new TestThread2(thread1);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
             return fail("TEST FAILED: unexpected " + e);
        }
        if (thread2.error) return fail("TEST FAILED: unexpected " +
                "InterruptedException in thread2");
        log.info("value1=" + thread1.value1);
        log.info("value2=" + thread1.value2);
        if(thread1.value1 != thread1.value2) return pass();
        else return fail("TEST FAILED: hash value numbering " +
                         "was incorrectly performed");
    }
    
}

class TestThread1 extends Thread {
    
    int value1 = 0;
    int value2 = 0;
    
    volatile int vol;
    volatile boolean flag = true;
    
    int num = 10;
    
    public void run() {
        int l = 7;
        vol = 10;
        value1 = (vol*num)%l;
        for (int i=0; flag; i++) {
            value2 = (vol*num)%l;
        }
    }
}

class TestThread2 extends Thread {
    
    TestThread1 thread1;
    boolean error = false;
    
    TestThread2(TestThread1 thread1) {
        this.thread1 = thread1;
    }
    
    public void run() {
        try {
            sleep(200);
        } catch (InterruptedException e) {
            error = true;
        }
        thread1.vol = 100;
        try {
            sleep(200);
        } catch (InterruptedException e) {
            error = true;
        }
        thread1.flag = false;
    }
}



