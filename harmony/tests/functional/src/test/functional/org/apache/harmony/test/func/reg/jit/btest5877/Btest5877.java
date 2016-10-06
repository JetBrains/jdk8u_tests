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
/**
*/
 
package org.apache.harmony.test.func.reg.jit.btest5877;

public class Btest5877 {   
    public static int flag=0;
    
    public static void main (String args[]) {
        StressTest1k t1 = new StressTest1k();
        t1.start();
        StressTest2k t2 = new StressTest2k();
        t2.start();
        while (true) { // if this cycle make FINIT, test will not hang up
            if (Btest5877.flag == 1) {
                break;
            }
        }
        System.out.println("Test pass");
        return;
    }
}

/* Forcing gc */
class StressTest1k extends Thread {
    public void run() {
        System.out.println("Thread 1 started"); 
        System.gc();
    }
}

/* Thread for exit from application without System.exit(). */
class StressTest2k extends Thread {
    public void run() {
        try {
            System.out.println("Thread 2 started"); 
            // this System.out is not relevant to hanging.
            Thread.sleep(2000);
            Btest5877.flag=1;
        }
        catch (Exception ex) {
        }
    }
}

