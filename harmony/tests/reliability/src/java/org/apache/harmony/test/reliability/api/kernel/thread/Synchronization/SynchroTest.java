/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Igor A. Pyankov
 * @version $Revision: 1.3 $
 */
package org.apache.harmony.test.reliability.api.kernel.thread.Synchronization;
import org.apache.harmony.test.reliability.share.Test;

/**
 *  Goal: check  thread synchronization
 *  The test does:
 *      1. Reads parameter, which is:
 *                   param[0] - number of iterations to run each thread
 *      2. Create 7 threads which work with the same object
 *            - The object has several variables that are changed by various methods
 *              with "synchronized" block.
 *      3. Checks that each thread PASSed.
 *      4. Each thread, being started:
 *         a. Runs param[0] iterations in a cycle, on each iteration:
 *         b. call method with "synchronized" block.
 *         c. stops if variable changed in "synchronized" block.
 */

public class SynchroTest extends Test {
    static int iteration = 50;    
    static volatile int finish; 
    
    SynchroThread[] synchroThread = new SynchroThread[7];
    

    public static void main(String[] args) {
        System.exit(new SynchroTest().test(args));
    }

    public int test(String[] params) {
        
        parseParams(params);
        WorkingClass workingObj = new WorkingClass();
        
        synchroThread[0] = new SynchroThread(workingObj, 1);
        synchroThread[1] = new SynchroThread(workingObj, 2);
        synchroThread[2] = new SynchroThread(workingObj, 3);
        synchroThread[3] = new SynchroThread(workingObj, 4);
        synchroThread[4] = new SynchroThread(workingObj, 12);
        synchroThread[5] = new SynchroThread(workingObj, 34);
        synchroThread[6] = new SynchroThread(workingObj, 1234);
                
        finish = synchroThread.length; 
        for (int i = 0; i < synchroThread.length; i++) {
            synchroThread[i].start();    
        }        
        while(finish > 0) {};
               
        for (int i = 0; i < synchroThread.length; i++) {
            if (synchroThread[i].status == SynStatus.FAIL) {
                log.add("Status of thread " + i + " is FAIL");
                return fail("Synchronization is broken");                   
            }
        }        
        return pass("OK");
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            iteration = Integer.parseInt(params[0]);            
        }        
    }
   
}    

class SynchroThread extends Thread {
    WorkingClass workingObj; 
    int selector;   
    int iteration;
    int status;

    public SynchroThread(WorkingClass wc, int sel) {
        workingObj = wc;
        selector = sel;
        iteration = SynchroTest.iteration;
        status = SynStatus.PASS;
    }   

    public void run() {
        boolean res = true;

        while (res &&  iteration-- > 0 ) {
            switch (selector) {
                case 1:
                    res = workingObj.do_1();
                case 2:
                    res = workingObj.do_2();
                case 12:
                    res = workingObj.do_12();
                case 3:
                    res = workingObj.do_3();
                case 4:
                    res = workingObj.do_4();
                case 34:
                    res = workingObj.do_34();
                case 1234:
                    res = workingObj.do_1234();
            }            
            if (!res) {
                status = SynStatus.FAIL;
            }            
        }
        synchronized (workingObj) {
            SynchroTest.finish--;   
        }
        return;
    }

}

class WorkingClass {    
    public int var1;
    public int var2;
    public int var3;
    public int var4;       
    public Object lock12;
    public Object lock34;
    public Object lock1234;
    
    
    WorkingClass (){
        var1 = 0;
        var2 = 0;
        var3 = 0;
        var4 = 0;
        lock12   = new Object();
        lock34   = new Object();
        lock1234 = new Object();
    }
    
        
    public boolean do_1() {
        boolean result;
        synchronized (lock12) {
            var1 = 1;            
            Thread.yield();
            result = (var1 == 1);                         
        }
        return result;
    }
    
    
    public boolean do_2() {
        boolean result;
        synchronized (lock12) {
            var2 = 2;            
            Thread.yield();
            result = (var2 == 2);         
        }
        return result;
    }
    
    public boolean do_12() {
        boolean result;
        synchronized (lock12) {
            var1 = 12;
            var2 = 12;            
            Thread.yield();
            result = ((var1 == 12) &&  (var2 == 12));                        
        }
        return result;
    }
    
    public boolean do_3() {
        boolean result;
        synchronized (lock34) {
            var3 = 3;            
            Thread.yield();
            result = (var3 == 3);                        
        } 
        return result;
    }
    
    public boolean do_4() {
        boolean result;
        synchronized (lock34) {
            var4 = 4;            
            Thread.yield();
            result = (var4 == 4);
        }
        return result;
    }
    
    public boolean do_34() {
        boolean result;
        synchronized (lock34) {
            var3 = 34;
            var4 = 34;            
            Thread.yield();
            result = ((var3 == 34) &&  (var3 == 34));           
        }
        return result;
    }
    
    public boolean do_1234() {
        boolean result;
        synchronized (lock12) {            
            synchronized (lock34) {
                var1 = 1234;
                var2 = 1234;                           
                var3 = 1234;
                var4 = 1234;                
                Thread.yield();
                result = ((var1 == 1234) && (var2 == 1234) && (var3 == 1234) && (var4 == 1234));   
            }            
        }
        return result;
    }    
}

class SynStatus {
    public static final int FAIL = -1;
    public static final int PASS = 1;
}


