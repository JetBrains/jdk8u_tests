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
package org.apache.harmony.test.func.jit.HLO.inline.Synchronization.Synchronize5;

import org.apache.harmony.test.func.jit.HLO.share.UsefulMethods;
import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 16.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Synchronize5 extends Test {
    
    final TestThread[] threads = new TestThread[5];
    
    public static void main(String[] args) {
        System.exit((new Synchronize5()).test());
    }

    public int test() {
        log.info("Start Synchronize5 test..."); 
        try {
            int threadCount = threads.length;
            log.info("threadCount = " + threadCount);
            for (int i=0; i<threadCount; i++) {
                threads[i] = new TestThread(i);
                threads[i].run();
            }
            for (int i=0; i<threadCount; i++) {
                if(!threads[i].wasException) 
                    return fail("TEST FAILED: RuntimeException " +
                            "expected in thread " + i);
                if (threads[i].incorrectTrace) {
                    log.info("Incorrect stack trace in thread " + i + ":");
                    log.info(threads[i].exception.toString());
                    log.add(threads[i].exception);
                    return fail("TEST FAILED");
                }    
            }
            return pass();
        } catch (Throwable e) { 
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }
}

class AuxiliaryClass  { 
    
    private Integer i;
    
    final void inlineMethod() {
        i.intValue();
    }
    
}

class TestThread implements Runnable {
    
    public boolean wasException = false; 
    public boolean incorrectTrace = false; 
    public Exception exception = null;
                             
    private int threadNumber;
    private final AuxiliaryClass au = new AuxiliaryClass();
    
    TestThread(int i) {
        threadNumber = i;
    }
    
    public void run() {
        System.out.println("Thread " + threadNumber + " started");
        synchronized (au) {
            try {
                au.inlineMethod();
            } catch (NullPointerException e) {
                wasException = true;
                System.out.println("Thread " + threadNumber + ": " + e);
                if (!UsefulMethods.checkStackTrace(e.getStackTrace(), 
                        "inlineMethod")) {
                    incorrectTrace = true;
                    exception = e;
                }
            }
        }
    }
}
  




