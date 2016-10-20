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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED002;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;


public class MixedDebuggee002 extends StressDebuggee {
   
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke";
    
    public static int MIXED002_ARRAY_LENGTH = 1000;
    
    public static Mixed002_TestClass[] anotherClassArray = new Mixed002_TestClass[MIXED002_ARRAY_LENGTH];
    
    public static Mixed002_TestClass[] classArray = new Mixed002_TestClass[MIXED002_ARRAY_LENGTH];
    
    static int createdThreadsNumber = 0;
    
    static MixedDebuggee002 mixedDebuggee002This;
                    
    private Mixed002_TestThread[] threadArray = new Mixed002_TestThread[MIXED002_THREADS_NUMBER];
    
    static int methodToInvoke (long timeMlsecsToRun, int valueToReturn) {
        try {
            Thread.sleep(timeMlsecsToRun);
        } catch (Throwable thrown ) {
            // ignore
        }
        return valueToReturn;    
    }
    
    public void run() {
        logWriter.println("--> MixedDebuggee002: START...");
       
        mixedDebuggee002This = this;
      
        printlnForDebug("MixedDebuggee002: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("MixedDebuggee002: After sendSignalAndWait(SIGNAL_READY_01)");               
        
        logWriter.println("--> MixedDebuggee002: Create big array of objects...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MixedDebuggee002: freeMemory (bytes) BEFORE creating array of objects = " + freeMemory);
        
        for (int i=0; i < classArray.length; i++) {
            classArray[i] = new Mixed002_TestClass(); 
            freeMemory = currentRuntime.freeMemory();
        }
                
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MixedDebuggee002: freeMemory (bytes) AFTER creating big array of objects = " + 
                freeMemory);
        
        
        printlnForDebug("MixedDebuggee002: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("MixedDebuggee002: After sendSignalAndWait(SIGNAL_READY_02)");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee002: freeMemory (bytes) before creating threads = " + freeMemory);

        logWriter.println("--> MixedDebuggee002: starting threads...");
        try {
            for (int i = 0; i < MIXED002_THREADS_NUMBER; i++) {
                threadArray[i] = new Mixed002_TestThread("thread" + i, i);
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < MIXED002_FREE_MEMORY_LIMIT ) {
                    printlnForDebug("FREE_MEMORY_LIMIT (" + MIXED002_FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
                
            }
        } catch (Throwable thrown) {
            logWriter.println("--> MixedDebuggee002: Exception while creating threads: "
                            + thrown);
        }
        
        while (suspendedThreadsNumber != createdThreadsNumber) {
            try {
                waitMlsecsTime(100);
            } catch (Exception e) {
                //ignore
            }
        }
        logWriter.println("--> MixedDebuggee002: threads are started and suspended");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee002: freeMemory (bytes) after creating threads = " + freeMemory);

        
        printlnForDebug("MixedDebuggee002: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("MixedDebuggee002: After sendThreadSignalAndWait(SIGNAL_READY_03)");
              
        logWriter.println("-> MixedDebuggee002: creating array of objects with null values..");
        
        for (int i=0; i < anotherClassArray.length; i++) {
            anotherClassArray[i] = null; 
        }
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee002: freeMemory (bytes) after another loading classes = " + freeMemory);

                
        printlnForDebug("MixedDebuggee002: sendThreadSignalAndWait(SIGNAL_READY_04)");
        sendThreadSignalAndWait(SIGNAL_READY_04);
        printlnForDebug("MixedDebuggee002: After sendThreadSignalAndWait(SIGNAL_READY_04)");
        
        logWriter.println
        ("--> MixedDebuggee002: Wait for all threads to finish...");
        int aliveThreads = 1;
        while ( aliveThreads > 0 ) {
            waitMlsecsTime(100);
            aliveThreads = 0;
            for (int i=0; i < createdThreadsNumber; i++) {
                if ( threadArray[i].isAlive() ) {
                    aliveThreads++;
                }
            }
        }
        
        logWriter.println
        ("--> MixedDebuggee002: All threads are finished!");
        

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MixedDebuggee002: freeMemory (bytes) AFTER all threads finished = " + 
                freeMemory);
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee002: freeMemory (bytes) before FINISH = " + freeMemory);
        logWriter.println("--> MixedDebuggee002: FINISH...");

    }
   
    public static void main(String [] args) {
        runDebuggee(MixedDebuggee002.class);
    }

}

class Mixed002_TestThread extends Thread {
    
    public static Mixed002_TestThread[] thisObject = new Mixed002_TestThread[StressDebuggee.MIXED002_THREADS_NUMBER];
    
    public Mixed002_TestThread(String name, int i) {
        super(name);
        
        thisObject[i] = this;
        start();
    }
    public void run() {
        testMethod();
        
    }
    
    public void testMethod() {
        MixedDebuggee002 parent = MixedDebuggee002.mixedDebuggee002This;
        parent.suspendThreadByEvent();
    }
}

class Mixed002_TestClass {
    
}

