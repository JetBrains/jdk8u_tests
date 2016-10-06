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
package org.apache.harmony.test.func.jit.HLO.peel.Volatile;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 06.07.2006 
 */

public class Volatile extends Test {
    
    public static void main(String[] args) {
        System.exit(new Volatile().test(args));
    }

    public int test() {
        log.info("Start Volatile test ...");
        TestThread1 thread1 = new TestThread1();
        TestThread2 thread2 = new TestThread2();
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
        else return pass();
    }

}

class TestThread1 extends Thread {
    
    int i = 0;
    static volatile boolean flag = true;
    
    public void run() {
        while(flag) {
            i++;
        }
    }
}

class TestThread2 extends Thread {
    
    boolean exception = false;    
    
    public void run() {
        try {
            sleep(500);
            TestThread1.flag = false;
        } catch (InterruptedException e) {
            exception = true;
        }
    }
}

