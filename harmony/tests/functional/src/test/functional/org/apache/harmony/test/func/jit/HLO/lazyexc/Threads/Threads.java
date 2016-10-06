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
package org.apache.harmony.test.func.jit.HLO.lazyexc.Threads;

import org.apache.harmony.share.Test;


/**
 */

/*
 * Created on 19.04.2006
 */

public class Threads extends Test {
    
    private final int count = 50;
    
    public static void main(String[] args) {
        System.exit((new Threads()).test(args));
    }
        
    public int test() {
        log.info("Start Threads lazyexc test...");
        log.info("Thread number: " + count);
        TestThread[] threads = new TestThread[count];
        try {
            for (int i=0; i<count; i++) {
                threads[i] = new TestThread();
            }
            for (int i=0; i<count; i++) {
                threads[i].start();
            }
            for (int i=0; i<count; i++) {
                threads[i].join();
            }
        } catch (Throwable e) {
            return fail("TEST FAILED: unexpected " + e);
        }
        return pass();
    }
}

class TestThread extends Thread { 
    
    private final int count = 10000;
    
    public void run() {
        for(int i=0; i<count; i++) {
            try {
                throw new Error();
            } catch (Error e) {
                
            }
        }
    }
}

