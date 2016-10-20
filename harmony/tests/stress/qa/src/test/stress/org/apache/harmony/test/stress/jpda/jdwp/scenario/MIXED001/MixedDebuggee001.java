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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED001;

import java.lang.reflect.Array;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;




public class MixedDebuggee001 extends StressDebuggee {
    
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke";
        
    public static int MIXED001_CLASS_ARRAY_LENGTH = 1024;
    static Class[] classesArray = new Class[MIXED001_CLASS_ARRAY_LENGTH];
    static Object[] objectArray = new Object[MIXED001_CLASS_ARRAY_LENGTH];
    
    
    static int createdThreadsNumber = 0;
    
    static MixedDebuggee001 mixedDebuggee001This;
    
    static int flagStopThreads = 0;
            
    private Mixed01_TestThread[] threadArray = new Mixed01_TestThread[MIXED001_THREADS_NUMBER];
    
    static int methodToInvoke (long timeMlsecsToRun, int valueToReturn) {
        try {
            Thread.sleep(timeMlsecsToRun);
        } catch (Throwable thrown ) {
            // ignore
        }
        return valueToReturn;    
    }
    
    public void run() {
        logWriter.println("--> MixedDebuggee001: START...");

        Mixed001_TestClass01 testClass01 = new Mixed001_TestClass01();
        Mixed001_TestClass02 testClass02 = new Mixed001_TestClass02();
        Mixed001_TestClass03 testClass03 = new Mixed001_TestClass03();
        Mixed001_TestClass04 testClass04 = new Mixed001_TestClass04();
        Mixed001_TestClass05 testClass05 = new Mixed001_TestClass05();

        int[] newArrayDimensions = new int[MIXED001_ARRAY_DIMENSION];
        int[] newArrayDimensionsForClasses = new int[MIXED001_ARRAY_DIMENSION_FOR_MORE_CLASSES];
        
        mixedDebuggee001This = this;
        
        for (int i=0; i < newArrayDimensions.length; i++) {
            newArrayDimensions[i] = 1;
        }
        
        for (int i=0; i < newArrayDimensionsForClasses.length; i++) {
            newArrayDimensionsForClasses[i] = 1;
        }
        
        for (int i=0; i < classesArray.length; i++) {
            classesArray[i]= null;
            objectArray[i]= null;
        }
        
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee001: freeMemory (bytes) = " + freeMemory);
        
        
        printlnForDebug("MixedDebuggee001: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("MixedDebuggee001: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("\n");
        logWriter.println
        ("-->  MixedDebuggee001: classesArray after ReferenceArray::SetValues command:");
        int classesArrayLength = -1;
        for (int i=0; i < classesArray.length; i++) {
            classesArrayLength++;
            if ( classesArray[i] == null ) {
                break;   
            }
        }
        logWriter.println("\n");
        logWriter.println("--> MixedDebuggee001: classesArrayLength = " + classesArrayLength);

        logWriter.println("\n");
                
        loadArrays(newArrayDimensions);
        printlnForDebug("MixedDebuggee001: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("MixedDebuggee001: After sendSignalAndWait(SIGNAL_READY_02)");
              
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee001: freeMemory (bytes) = " + freeMemory);

        logWriter.println("--> MixedDebuggee001: creating threads... ");
        try {
            for (int i = 0; i < MIXED001_THREADS_NUMBER; i++) {
                threadArray[i] = new Mixed01_TestThread("thread" + i, i);
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < MIXED001_FREE_MEMORY_LIMIT ) {
                    printlnForDebug("FREE_MEMORY_LIMIT (" + MIXED001_FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
                
            }
        } catch (Throwable thrown) {
            logWriter.println("--> MixedDebuggee001: Exception while creating threads: "
                            + thrown);
            
        }
        
        while (suspendedThreadsNumber != createdThreadsNumber) {
            try {
                waitMlsecsTime(100);
            } catch (Exception e) {
                //ignore
            }
        }
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee001: freeMemory (bytes) after creating threads = " + freeMemory);
        
        printlnForDebug("MixedDebuggee001: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("MixedDebuggee001: After sendThreadSignalAndWait(SIGNAL_READY_03)");
              
        logWriter.println("--> MixedDebuggee001: load some number of classes...");
        try {
            loadSomeClasses(newArrayDimensionsForClasses, classesArrayLength);
        } catch (ClassNotFoundException exp) {
            logWriter.print("Exception during loading classes: " + exp);
        }
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee001: freeMemory (bytes) after loading some number of classes = " + freeMemory);
                
        printlnForDebug("MixedDebuggee001: sendThreadSignalAndWait(SIGNAL_READY_04)");
        sendThreadSignalAndWait(SIGNAL_READY_04);
        printlnForDebug("MixedDebuggee001: After sendThreadSignalAndWait(SIGNAL_READY_04)");
        
        logWriter.println
        ("--> MixedDebuggee001: Wait for all threads to finish...");
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
        ("--> MixedDebuggee001: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MixedDebuggee001: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MixedDebuggee001: freeMemory (bytes) before FINISH = " + freeMemory);
        logWriter.println("--> MixedDebuggee001: FINISH...");

    }

    void loadArrays(int[] newArrayDimensions) {
        logWriter.println("--> MixedDebuggee001: Create arrays...");
        
        long createdArrays = 0;
        try {
            for (int i=0; i < MIXED001_CLASS_ARRAY_LENGTH; i++) {
                if ( classesArray[i] == null ) {
                    break;   
                }
                objectArray[i] = Array.newInstance(classesArray[i], newArrayDimensions);
                createdArrays++;
                
            }

        } catch ( Throwable thrown) {
            logWriter.println
            ("--> MixedDebuggee001: Exception while Array.newInstance(): " + thrown);
        }
        logWriter.println("--> MixedDebuggee001: createdArrays = " + createdArrays);
    }
       
       
    void loadSomeClasses(int[] newArrayDimensions, int startIndex) throws ClassNotFoundException {
        Class[] clsArray = new Class[35];
        Class cls;
          
        for (int i=1; i<34; i++) {       
            cls = Class.forName("org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED001.class"+i);
            objectArray[startIndex+i] = Array.newInstance(cls, newArrayDimensions);
        }
    }
    
    public static void main(String [] args) {
        runDebuggee(MixedDebuggee001.class);
    }

}
  

class Mixed001_TestClass01 {}
class Mixed001_TestClass02 {}
class Mixed001_TestClass03 {}
class Mixed001_TestClass04 {}
class Mixed001_TestClass05 {}


class class1{} class class18{}
class class2{}  class class19{}
class class3{}  class class20{}
class class4{}  class class21{}
class class5{}  class class22{}
class class6{}  class class23{}
class class7{}  class class24{}
class class8{}  class class25{}
class class9{}  class class26{}
class class10{} class class27{}
class class11{} class class28{} 
class class12{} class class29{}
class class13{} class class30{}
class class14{} class class31{}
class class15{} class class32{}
class class16{} class class33{}
class class17{} class class34{}

class Mixed01_TestThread extends Thread {
    
    public static Mixed01_TestThread[] thisObject = new Mixed01_TestThread[StressDebuggee.MIXED001_THREADS_NUMBER];
    
    public Mixed01_TestThread(String name, int i) {
        super(name);
        
        thisObject[i] = this;
        start();
    }
    public void run() {
        testMethod();
        
    }
    
    public void testMethod() {
        MixedDebuggee001 parent = MixedDebuggee001.mixedDebuggee001This;
        parent.suspendThreadByEvent();
    }
}