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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.REFTYPE002;


import java.lang.reflect.Array;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class RefTypeDebuggee002 extends StressDebuggee {
       
    static Class[] classesArray = new Class[REFTYPE002_CLASSES_ARRAY_LENGTH];
    static Object[] objectArray = new Object[REFTYPE002_CLASSES_ARRAY_LENGTH];
    static Object[] objectArray1 = new Object[REFTYPE002_CLASSES_ARRAY_LENGTH];

    public void run() {
        logWriter.println("--> RefTypeDebuggee002: START...");

        int[] newArrayDimensions = new int[REFTYPE002_ARRAY_DIMENSION];
        int[] newArrayDimensionsForClasses = new int[REFTYPE002_ARRAY_DIMENSION_FOR_CLASSES];
        
        for (int i=0; i < newArrayDimensions.length; i++) {
            newArrayDimensions[i]= 1;
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
        logWriter.println("--> RefTypeDebuggee002: freeMemory (bytes) = " + freeMemory);
        
        
        printlnForDebug("RefTypeDebuggee002: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("RefTypeDebuggee002: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("\n");
        logWriter.println
        ("-->  RefTypeDebuggee002: classesArray after ReferenceArray::SetValues command:");
        int classesArrayLength = -1;
        for (int i=0; i < classesArray.length; i++) {
            classesArrayLength++;
            if ( classesArray[i] == null ) {
                break;   
            }
        }
        logWriter.println("\n");
        logWriter.println("--> RefTypeDebuggee002: classesArrayLength = " + classesArrayLength);
        
        loadArrays(newArrayDimensions);
                
        printlnForDebug("RefTypeDebuggee002: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("RefTypeDebuggee002: After sendSignalAndWait(SIGNAL_READY_02)");
        
        logWriter.println("--> RefTypeDebuggee002: Create arrays second time...");
        try {
            loadSomeClasses(newArrayDimensionsForClasses, classesArrayLength);
        } catch (ClassNotFoundException exp) {
            logWriter.print("Exception during loading classes: " + exp);
        }
        
        printlnForDebug("RefTypeDebuggee002: sendSignalAndWait(SIGNAL_READY_03)");
        sendSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("RefTypeDebuggee002: After sendSignalAndWait(SIGNAL_READY_03)");
        
        logWriter.println("--> RefTypeDebuggee002: Creating memory stress..");
        createMemoryStress(1000000, 1000000);
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee002: freeMemory (bytes) after memory stress = " + freeMemory);
        
        printlnForDebug("RefTypeDebuggee002: sendSignalAndWait(SIGNAL_READY_04)");
        sendSignalAndWait(SIGNAL_READY_04);
        printlnForDebug("RefTypeDebuggee002: After sendSignalAndWait(SIGNAL_READY_04)");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee002: freeMemory (bytes) before FINISH = " + freeMemory);
        logWriter.println("--> RefTypeDebuggee002: FINISH...");

    }

    void loadArrays(int[] newArrayDimensions) {
        logWriter.println("--> RefTypeDebuggee002: Create arrays...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee002: freeMemory (bytes) before creating arrays = " + freeMemory);
        
        long createdArrays = 0;
        try {
            for (int i=0; i < REFTYPE002_CLASSES_ARRAY_LENGTH; i++) {
                if ( classesArray[i] == null ) {
                    break;   
                }
                objectArray[i] = Array.newInstance(classesArray[i], newArrayDimensions);
                createdArrays++;
            }

        } catch ( Throwable thrown) {
            logWriter.println
            ("--> RefTypeDebuggee002: Exception while Array.newInstance(): " + thrown);
        }
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee002: freeMemory (bytes) after creating arrays = " + freeMemory);
        
        logWriter.println("--> RefTypeDebuggee002: createdArrays = " + createdArrays);
    }
    
    void loadSomeClasses(int[] newArrayDimensions, int startIndex) throws ClassNotFoundException {
        Class[] clsArray = new Class[35];
        Class cls;
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee002: freeMemory (bytes) before loading some classes = " + freeMemory);
        
        for (int i=1; i<34; i++) {       
            cls = Class.forName("org.apache.harmony.test.stress.jpda.jdwp.scenario.REFTYPE002.class"+i);
            objectArray[startIndex+i] = Array.newInstance(cls, newArrayDimensions);
        }
        
        RefType002_TestClass01 testClass01 = new RefType002_TestClass01();
        RefType002_TestClass02 testClass02 = new RefType002_TestClass02();
        RefType002_TestClass03 testClass03 = new RefType002_TestClass03();
        RefType002_TestClass04 testClass04 = new RefType002_TestClass04();
        RefType002_TestClass05 testClass05 = new RefType002_TestClass05();
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee002: freeMemory (bytes) after loading some classes = " + freeMemory);
    }
    
    public static void main(String [] args) {
        runDebuggee(RefTypeDebuggee002.class);
    }

}
  

class RefType002_TestClass01 {}
class RefType002_TestClass02 {}
class RefType002_TestClass03 {}
class RefType002_TestClass04 {}
class RefType002_TestClass05 {}

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
