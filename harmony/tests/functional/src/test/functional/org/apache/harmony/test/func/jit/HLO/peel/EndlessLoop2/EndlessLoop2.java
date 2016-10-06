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
package org.apache.harmony.test.func.jit.HLO.peel.EndlessLoop2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 15.06.2006 
 */

public class EndlessLoop2 extends Test {
    
    
    public static void main(String[] args) {
        System.exit(new EndlessLoop2().test(args));
    }

    public int test() {
        log.info("Start EndlessLoop2 test ...");
        for(int k=0; k<10000; k++) {
            (new TestThread()).run();
            (new TestThread()).run();
        }
        log.info("Number of runned threads: " + TestThread.i);
        if (TestThread.i==20000) return pass();
        else return fail("TEST FAILED: number of runned threads must be 20000");
    }
}

class TestThread extends EndlessLoop2 {
    
    static int i = 0;
    Object obj = new Object();
    
    public void run() {
        label:
        for(;;) {
            for(;;) {
                try {
                    i++;
                    obj.wait();
                    obj = null;
                } catch (InterruptedException e) {
                    continue label;
                } catch (IllegalMonitorStateException e) {
                    continue label;    
                } finally {
                    if(obj != null) break label;
                }
                continue label;    
            }
        }
    }
}
