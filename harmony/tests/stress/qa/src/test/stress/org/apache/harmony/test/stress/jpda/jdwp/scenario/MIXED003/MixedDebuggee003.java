/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/**
 * @author Aleksander V. Budniy
 * @version $Revision: 1.2 $
 */

/**
 * Created on 08.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED003;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class MixedDebuggee003 extends StressDebuggee {
   
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke";
            
    static int createdThreadsNumber = 0;
    static int createdRecursiveThreadsNumber = 0;
    
    static MixedDebuggee003 mixedDebuggee003This;
                
    private Mixed03_TestThread[] threadArray = new Mixed03_TestThread[MIXED003_THREADS_NUMBER];
    private Mixed03_RecursiveThread[] recursiveThreadArray = new Mixed03_RecursiveThread[MIXED003_RECURSIVE_THREADS_NUMBER];
    
    static int methodToInvoke (long timeMlsecsToRun, int valueToReturn) {
        try {
            Thread.sleep(timeMlsecsToRun);
        } catch (Throwable thrown ) {
            // ignore
        }
        return valueToReturn;    
    }
    
    
    public void run() {
        logWriter.println("--> MixedDebuggee003: START...");
               
        mixedDebuggee003This = this;
    
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
                
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee003: freeMemory (bytes) before creating threads = " + freeMemory);

        logWriter.println("--> MixedDebuggee003: starting threads...");
        int maxThreadsNumber = MIXED003_THREADS_NUMBER;
        if ( maxThreadsNumber < MIXED003_RECURSIVE_THREADS_NUMBER ) {
            maxThreadsNumber = MIXED003_RECURSIVE_THREADS_NUMBER;
        }
        try {
            for (int i = 0; i < maxThreadsNumber; i++) {
                if ( i < MIXED003_THREADS_NUMBER ) {
                    threadArray[i] = new Mixed03_TestThread("thread" + i, i);
                    createdThreadsNumber++;
                }
                if ( i < MIXED003_RECURSIVE_THREADS_NUMBER ) {
                    recursiveThreadArray[i] = new Mixed03_RecursiveThread("recursiveThread" + i, i);
                    createdRecursiveThreadsNumber++;
                }
            }
        } catch (Throwable thrown) {
            logWriter.println("--> MixedDebuggee003: Exception while creating threads: "
                            + thrown);
            
        }
        
        while (suspendedThreadsNumber != createdThreadsNumber + createdRecursiveThreadsNumber) {
            try {
                waitMlsecsTime(100);
            } catch (Exception e) {
                //ignore
            }
        }
        logWriter.println("--> MixedDebuggee003: "+ createdThreadsNumber+" threads " + createdRecursiveThreadsNumber + " recursive threads are started and suspended");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee003: freeMemory (bytes) after threads starting = " + freeMemory);
        
        printlnForDebug("MixedDebuggee003: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("MixedDebuggee003: After sendThreadSignalAndWait(SIGNAL_READY_01)");
               
        logWriter.println
        ("--> MixedDebuggee003: Wait for all threads to finish...");
        int aliveThreads = 1;
        while ( aliveThreads > 0 ) {
            waitMlsecsTime(100);
            aliveThreads = 0;
            for (int i=0; i < createdThreadsNumber; i++) {
                if ( threadArray[i].isAlive() ) {
                    aliveThreads++;
                }
            }
            for (int i=0; i < createdRecursiveThreadsNumber; i++) {
                if ( recursiveThreadArray[i].isAlive() ) {
                    aliveThreads++;
                }
            }
            
        }
        
        logWriter.println
        ("--> MixedDebuggee003: All threads finished!");
       
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MixedDebuggee003: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);
        printlnForDebug("MixedDebuggee003: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("MixedDebuggee003: After sendTSignalAndWait(SIGNAL_READY_02)");
        
        logWriter.println("--> MixedDebuggee003: FINISH...");

    }
   
    public static void main(String [] args) {
        runDebuggee(MixedDebuggee003.class);
    }

}

class Mixed03_TestThread extends Thread {
    
    public static Mixed03_TestThread[] thisObject = new Mixed03_TestThread[StressDebuggee.MIXED003_THREADS_NUMBER];
    
    int localValue = 888;
    
    public Mixed03_TestThread(String name, int i) {
        super(name);
        
        thisObject[i] = this;
        start();
    }
    public void run() {
        testMethod();
        
    }
    
    public void testMethod() {
        MixedDebuggee003 parent = MixedDebuggee003.mixedDebuggee003This;
        parent.suspendThreadByEvent();
        
    }
}

class Mixed03_RecursiveThread extends Thread {
    public static Mixed03_RecursiveThread[] thisObject = new Mixed03_RecursiveThread[StressDebuggee.MIXED003_RECURSIVE_THREADS_NUMBER];
    
    int localValue = 888;
    
    public Mixed03_RecursiveThread(String name, int i) {
        super(name);
        
        thisObject[i] = this;
        start();
    }
    public void run() {
        testRecursiveMethod(5);
        
    }
    
    public void testRecursiveMethod(int i) {
        i--;
        if(i == 0) {
            int localValue = 888;
            MixedDebuggee003 parent = MixedDebuggee003.mixedDebuggee003This;
            parent.suspendThreadByEvent();
        } else {
            testRecursiveMethod(i);
        }
    }
}


class Mixed03_TestClass {
    
}

