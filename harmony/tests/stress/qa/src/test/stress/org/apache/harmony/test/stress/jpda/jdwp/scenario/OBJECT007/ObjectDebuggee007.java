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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT007;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ObjectDebuggee007 extends StressDebuggee {
   
    static ObjectDebuggee007 objectDebuggee007This;
    
    public static Object07_TestClass [] classArray = new Object07_TestClass [OBJECT007_ARRAY_LENGTH];
    public static Object07_TestClass [] anotherClassArray = new Object07_TestClass [OBJECT007_ARRAY_LENGTH];

    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    public void run() {
        logWriter.println("--> ObjectDebuggee007: START...");
        objectDebuggee007This = this;

        printlnForDebug("ObjectDebuggee007: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ObjectDebuggee007: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> ObjectDebuggee007: Create big array of objects...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee007: freeMemory (bytes) BEFORE creating array of objects = " + freeMemory);
        
        for (int i=0; i < classArray.length; i++) {
            classArray[i] = new Object07_TestClass (); 
            freeMemory = currentRuntime.freeMemory();
        }
                
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee007: freeMemory (bytes) AFTER creating big array of objects = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee007: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        
        logWriter.println("--> ObjectDebuggee007: Create another big array of objects...");
        for (int i=0; i < anotherClassArray.length; i++) {
            anotherClassArray[i] = new Object07_TestClass (); 
        }
        
        logWriter.println("--> ObjectDebuggee007: Create memory stress..");
        createMemoryStress(1000000, 1000000);
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee007: freeMemory (bytes) AFTER creating another big array of objects and memory stress = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee007: sendSignalAndWait(SIGNAL_READY_03)");
        sendSignalAndWait(SIGNAL_READY_03);
        
        printlnForDebug("ObjectDebuggee007: After sendSignalAndWait(SIGNAL_READY_03)");
                
        logWriter.println("--> ObjectDebuggee007: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ObjectDebuggee007.class);
    }

}

class Object07_TestClass   {
    
}


