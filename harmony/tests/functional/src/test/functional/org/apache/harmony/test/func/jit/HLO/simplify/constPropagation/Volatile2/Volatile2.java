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
package org.apache.harmony.test.func.jit.HLO.simplify.constPropagation.Volatile2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 31.05.2006
 */

public class Volatile2 extends Test {
    
    public static void main(String[] args) {
        System.exit((new Volatile2()).test(args));
    }
        
    public int test() {
        log.info("Start Volatile2 test...");
        TestThread thread = new TestThread();
        Modifier modifier = new Modifier(thread);
        thread.start();
        modifier.start();
        try {
            thread.join();
            modifier.join();
        } catch (InterruptedException e) {
             return fail("TEST FAILED: unexpected " + e);
        } 
        log.info("Check1 = " + thread.check1);
        log.info("Check2 = " + thread.check2[1]);
        log.info("Check3 = " + thread.check3);
        log.info("Check4 = " + thread.check4.intValue());
        if ((thread.check1 == 1.5f) && (thread.check2[1] == 13) 
                && (thread.check3 == 1) && (thread.check4.intValue() == 1)) 
            return pass();
        else return fail("TEST FAILED: " 
            + "optimization was applied to volatile variable");
    }

}


class TestThread extends Thread {
    
    float check1 = 0f;
    Integer check4 = new Integer(0);
    int check3 = 0;
    int[] check2 = new int[10];
    boolean flag = true;
    
    volatile float vol = 0f;
    
    public void run() {
        vol = 9f;
        do {
            check1 = (((vol+vol+vol)*10f - 10*vol + 1f)/7f)/2f/vol;
            check2[(int) vol] = 13; 
            check3 = (int) vol | 0;
            check4 = new Integer((int) vol);
        } while (flag);
    }
}


class Modifier extends Thread {
    
    public boolean error = false;
    private TestThread thread;
    
    public Modifier (TestThread thread) {
        this.thread = thread;
    }
    
    public void run() {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            error = true;
        }
        thread.vol = 1f;
        try {
            sleep(100);
        } catch (InterruptedException e) {
            error = true;
        }
        thread.flag = false;
    }
}



