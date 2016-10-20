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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.REFTYPE004;


import java.lang.reflect.Array;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class RefTypeDebuggee004 extends StressDebuggee {
       
    static Class[] classesArray = new Class[REFTYPE004_CLASSES_ARRAY_LENGTH];
    static Object[] objectArray = new Object[REFTYPE004_CLASSES_ARRAY_LENGTH];
    static Object[] objectArray1 = new Object[REFTYPE004_CLASSES_ARRAY_LENGTH];

    public void run() {
        logWriter.println("--> RefTypeDebuggee004: START...");
      
        int[] newArrayDimensions = new int[REFTYPE004_ARRAY_DIMENSION];
        
        for (int i=0; i < newArrayDimensions.length; i++) {
            newArrayDimensions[i]= 1;
        }
        for (int i=0; i < classesArray.length; i++) {
            classesArray[i]= null;
            objectArray[i]= null;
        }
        
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee004: freeMemory (bytes) = " + freeMemory);
        
        
        printlnForDebug("RefTypeDebuggee004: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("RefTypeDebuggee004: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("\n");
        logWriter.println
        ("-->  RefTypeDebuggee004: classesArray after ReferenceArray::SetValues command:");
        int classesArrayLength = -1;
        for (int i=0; i < classesArray.length; i++) {
            classesArrayLength++;
            if ( classesArray[i] == null ) {
                break;   
            }
        }
        logWriter.println("\n");
        logWriter.println("--> RefTypeDebuggee004: classesArrayLength = " + classesArrayLength);

        logWriter.println("\n");
        logWriter.println("--> RefTypeDebuggee004: Create arrays...");
        
        loadArrays(newArrayDimensions);
               

        printlnForDebug("RefTypeDebuggee004: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("RefTypeDebuggee004: After sendSignalAndWait(SIGNAL_READY_02)");
        
        logWriter.println("--> RefTypeDebuggee004: load some classes...");
        
        RefType004_TestClass01 testClass01 = new RefType004_TestClass01();
        RefType004_TestClass02 testClass02 = new RefType004_TestClass02();
        RefType004_TestClass03 testClass03 = new RefType004_TestClass03();
        RefType004_TestClass04 testClass04 = new RefType004_TestClass04();
        RefType004_TestClass05 testClass05 = new RefType004_TestClass05();
        
        logWriter.println("--> RefTypeDebuggee004: create memory stress..");
        createMemoryStress(1000000, 1000000);
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee004: freeMemory (bytes) after loading some classes and memory stressing = " + freeMemory);
        
        printlnForDebug("RefTypeDebuggee004: sendSignalAndWait(SIGNAL_READY_03)");
        sendSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("RefTypeDebuggee004: After sendSignalAndWait(SIGNAL_READY_03)");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee004: freeMemory (bytes) before FINISH = " + freeMemory);
        logWriter.println("--> RefTypeDebuggee004: FINISH...");

    }

    void loadArrays(int[] newArrayDimensions) {
        logWriter.println("--> RefTypeDebuggee004: Create arrays...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee004: freeMemory (bytes) before creating arrays = " + freeMemory);
        
        long createdArrays = 0;
        try {
            for (int i=0; i < REFTYPE004_CLASSES_ARRAY_LENGTH; i++) {
                if ( classesArray[i] == null ) {
                    break;   
                }
                objectArray[i] = Array.newInstance(classesArray[i], newArrayDimensions);
                createdArrays++;
            }

        } catch ( Throwable thrown) {
            logWriter.println
            ("--> RefTypeDebuggee004: Exception while Array.newInstance(): " + thrown);
        }
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> RefTypeDebuggee004: freeMemory (bytes) after creating arrays = " + freeMemory);
        
        logWriter.println("--> RefTypeDebuggee004: createdArrays = " + createdArrays);
    }
    
    
    public static void main(String [] args) {
        runDebuggee(RefTypeDebuggee004.class);
    }

}

class RefType004_TestClass01 {}
class RefType004_TestClass02 {}
class RefType004_TestClass03 {}
class RefType004_TestClass04 {}
class RefType004_TestClass05 {}

class NewTestClass1 {}
class NewTestClass2 {}
class NewTestClass3 {}

