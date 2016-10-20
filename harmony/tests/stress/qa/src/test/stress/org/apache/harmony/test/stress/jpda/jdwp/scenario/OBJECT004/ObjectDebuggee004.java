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
 * Created on 16.09.2006 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT004;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ObjectDebuggee004 extends StressDebuggee {
    
    static ObjectDebuggee004 objectDebuggee004This;
    
    public static Object04_TestClass[] classArray = new Object04_TestClass[OBJECT004_ARRAY_LENGTH];

    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    public void run() {
        
        logWriter.println("--> ObjectDebuggee004: START...");
        objectDebuggee004This = this;

        printlnForDebug("ObjectDebuggee004: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ObjectDebuggee004: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> ObjectDebuggee004: Create big array of objects...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee004: freeMemory (bytes) BEFORE creating array of objects = " + freeMemory);
        
        int createdObjects = 0;
        Object04_TestClass lastObject = null;
        try {
            for (int i=0; i < classArray.length; i++) {
                if (i==2) break;
                classArray[i] = new Object04_TestClass();
                lastObject = classArray[i];
                createdObjects++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < OBJECT004_FREE_MEMORY_LIMIT ) {
                    printlnForDebug("FREE_MEMORY_LIMIT (" + OBJECT004_FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch (Throwable thrown) {
            logWriter.println
            ("--> ObjectDebuggee004: Exception while creating array of Objects: " + thrown);
        }
        if ( createdObjects != classArray.length ) {
            for (int i=createdObjects; i < classArray.length; i++) {
                classArray[i] = lastObject;
            }
        }
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee004: freeMemory (bytes) AFTER creating big array of objects = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee004: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ObjectDebuggee004: After sendSignalAndWait(SIGNAL_READY_02)");
                
        logWriter.println("--> ObjectDebuggee004: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ObjectDebuggee004.class);
    }

}

class Object04_TestClass {
    
}


