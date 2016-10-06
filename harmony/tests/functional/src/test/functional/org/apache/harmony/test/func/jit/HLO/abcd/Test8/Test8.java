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
package org.apache.harmony.test.func.jit.HLO.abcd.Test8;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 12.07.2006 
 */

public class Test8 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Test8().test(args));
    }

    public int test() {
        log.info("Start Test8 ...");
        TestThread2 thread2 = new TestThread2();
        TestThread1 thread1 = new TestThread1();
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
        if (thread1.exception || thread2.exception) return fail("TEST FAILED: "
                + "unexpected InterruptedException ocurred in some thread");
        if (thread1.flag) return pass();
        else return fail("TEST FAILED: ArrayIndexOutOfBoundsException " +
                "wasn't thrown");
    }

}

class TestThread1 extends Thread {
    
    boolean exception = false;    
    boolean flag = false;
        
    protected static volatile int arr[];
    
    public void run() {
        try {
            arr = new int[100100];
            for(int i=0; i<100000;) {
                arr[i] = i;
            }
        } catch (ArrayIndexOutOfBoundsException ae) {
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }
    }
}

class TestThread2 extends TestThread1 {
    
    boolean exception = false;    
    
    public void run() {
        try {
            sleep(500);
            arr = new int[0];
        } catch (InterruptedException e) {
            exception = true;
        }
    }
}

