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
package org.apache.harmony.test.func.jit.HLO.abcd.Test7;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 12.07.2006 
 */

public class Test7 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Test7().test(args));
    }

    public int test() {
        log.info("Start Test7 ...");
        TestThread2 thread2 = new TestThread2();
        TestThread1 thread1 = new TestThread1(thread2);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            log.info("Unexpected InterruptedException occurred");
            log.add(e);
            return fail("TEST FAILED");
        }
        if (thread2.exception) return fail("TEST FAILED: " +
                "unexpected InterruptedException in thread2");
        if (thread2.flag) return pass();
        else return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
                "wasn't thrown");
    }

}

class TestThread1 extends Thread {
    
    TestThread2 thread2;
    
    TestThread1(TestThread2 thread2) {
        this.thread2 = thread2;
    }
    
    public void run() {
        thread2.meth();
    }
}

class TestThread2 extends Thread {
    
    private volatile int i = 0;    
    boolean exception = false;    
    boolean flag = false;

    public void run() {
        try {
            sleep(500);
            i = 300000;
        } catch (InterruptedException e) {
            exception = true;
        }
    }
    
    public void meth() {
        final int arr[] = new int[100500];
        try {
            for(i=100000; i>0; ) {
                arr[i] = i;
            }
        } catch (ArrayIndexOutOfBoundsException ae) {
            flag = true;
        }
    }
}

