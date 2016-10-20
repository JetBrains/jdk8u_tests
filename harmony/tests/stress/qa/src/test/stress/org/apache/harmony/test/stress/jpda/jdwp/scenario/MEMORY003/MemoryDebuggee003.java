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
 * @author Anatoly F. Bondarenko
 * @version $Revision: 1.2 $
 */

/**
 * Created on 09.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY003;


import java.lang.reflect.Array;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class MemoryDebuggee003 extends StressDebuggee {
    public static final int ARRAY_DIMENSION = MEMORY003_ARRAY_DIMENSION; 
    public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = MEMORY003_ARRAY_SIZE_FOR_MEMORY_STRESS; 
    
    static int classesArrayLength = 1024;
    static Class[] classesArray = new Class[classesArrayLength];
    static Object[] objectArray = new Object[classesArrayLength];

    public void run() {
        
        logWriter.println("--> MemoryDebuggee003: START...");

        Memory003_TestClass01 testClass01 = new Memory003_TestClass01();
        Memory003_TestClass02 testClass02 = new Memory003_TestClass02();
        Memory003_TestClass03 testClass03 = new Memory003_TestClass03();
        Memory003_TestClass04 testClass04 = new Memory003_TestClass04();
        Memory003_TestClass05 testClass05 = new Memory003_TestClass05();

        int[] newArrayDimensions = new int[ARRAY_DIMENSION];
        
        for (int i=0; i < newArrayDimensions.length; i++) {
            newArrayDimensions[i]= 1;
        }
        for (int i=0; i < classesArray.length; i++) {
            classesArray[i]= null;
            objectArray[i]= null;
        }
        
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MemoryDebuggee003: freeMemory (bytes) = " + freeMemory);
        
        
        printlnForDebug("MemoryDebuggee003: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("MemoryDebuggee003: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("\n");
        logWriter.println
        ("-->  MemoryDebuggee003: classesArray after ReferenceArray::SetValues command:");
        int classesArrayLength = -1;
        for (int i=0; i < classesArray.length; i++) {
            classesArrayLength++;
            if ( classesArray[i] == null ) {
                break;   
            }
        }
        logWriter.println("\n");
        logWriter.println("--> MemoryDebuggee003: classesArrayLength = " + classesArrayLength);

        logWriter.println("\n");
        logWriter.println("--> MemoryDebuggee003: Create arrays...");
        
        long createdArrays = 0;
        try {
            for (int i=0; i < classesArray.length; i++) {
                if ( classesArray[i] == null ) {
                    break;   
                }
                objectArray[i] = Array.newInstance(classesArray[i], newArrayDimensions);
                createdArrays++;
            }

        } catch ( Throwable thrown) {
            logWriter.println
            ("--> MemoryDebuggee003: Exception while Array.newInstance(): " + thrown);
        }
        logWriter.println("--> MemoryDebuggee003: createdArrays = " + createdArrays);
        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MemoryDebuggee003: freeMemory (bytes) = " + freeMemory);

        printlnForDebug("MemoryDebuggee003: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("MemoryDebuggee003: After sendSignalAndWait(SIGNAL_READY_02)");

        logWriter.println("--> MemoryDebuggee003: Creating memory stress until OutOfMemory");
        createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);
        
        printlnForDebug("MemoryDebuggee003: sendSignalAndWait(SIGNAL_READY_03)");
        sendSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("MemoryDebuggee003: After sendSignalAndWait(SIGNAL_READY_03)");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println("--> MemoryDebuggee003: freeMemory (bytes) before FINISH = " + freeMemory);
        logWriter.println("--> MemoryDebuggee003: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(MemoryDebuggee003.class);
    }

}

class Memory003_TestClass01 {}
class Memory003_TestClass02 {}
class Memory003_TestClass03 {}
class Memory003_TestClass04 {}
class Memory003_TestClass05 {}


