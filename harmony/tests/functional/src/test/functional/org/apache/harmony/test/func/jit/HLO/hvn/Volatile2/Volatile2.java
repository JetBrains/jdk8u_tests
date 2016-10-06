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
package org.apache.harmony.test.func.jit.HLO.hvn.Volatile2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 27.07.2006
 */

public class Volatile2 extends Test {
    
    public static void main(String[] args) {
        System.exit((new Volatile2()).test(args));
    }
        
    public int test() {
        log.info("Start Volatile2 test...");
        TestThread1 thread1 = new TestThread1();
        TestThread2 thread2 = new TestThread2(thread1);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
             return fail("TEST FAILED: " + e);
        }
        log.info("value1=" + thread1.value1);
        log.info("value2=" + thread1.value2);
        if(thread1.value1 != thread1.value2) return pass();
        else return fail("TEST FAILED: possibly hash value numbering " +
                         "was incorrectly performed");
    }
    
}

class TestThread1 extends Thread {
    
    volatile int vol = 10;
    volatile boolean flag1 = true;
    volatile boolean flag2 = false;
    
    int value1 = 0;
    int value2 = 0;
    int num = 3;
    
    public void run() {
        if (vol > num) {
            value1++;
        }
        flag2 = true;
        while (flag1) {
            // wait while vol is changed 
        }
        if (vol > num) {
            value2++;
        }
    }
}

class TestThread2 extends Thread {
    
    TestThread1 thread1;
    
    TestThread2(TestThread1 thread1) {
        this.thread1 = thread1;
    }
    
    public void run() {
        while(true) {
            if (thread1.flag2) {
                thread1.vol = -10;
                thread1.flag1 = false;
                break;
            }
        }
    }
}


