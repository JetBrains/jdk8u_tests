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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT009;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ObjectDebuggee009 extends StressDebuggee {
    
    static ObjectDebuggee009 objectDebuggee009This;
    
    public static TestClass[] classArray = new TestClass[OBJECT009_ARRAY_LENGTH];
    public static TestClass[] anotherClassArray = new TestClass[OBJECT009_ARRAY_LENGTH];

    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    public void run() {
        logWriter.println("--> ObjectDebuggee009: START...");
        objectDebuggee009This = this;

        printlnForDebug("ObjectDebuggee009: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ObjectDebuggee009: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> ObjectDebuggee009: Create big array of objects...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee009: freeMemory (bytes) BEFORE creating array of objects = " + freeMemory);
        
        for (int i=0; i < classArray.length; i++) {
            classArray[i] = new TestClass();
            freeMemory = currentRuntime.freeMemory();
        }
                
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee009: freeMemory (bytes) AFTER creating big array of objects = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee009: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        
        logWriter.println("--> ObjectDebuggee009: Create another big array of objects...");
        for (int i=0; i < anotherClassArray.length; i++) {
            anotherClassArray[i] = null; 
        }
        
        logWriter.println("--> ObjectDebuggee009: Create memory stress...");        
        createMemoryStress(1000000, 1000000);
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee009: freeMemory (bytes) AFTER creating another big array of objects and memory stressing = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee009: sendSignalAndWait(SIGNAL_READY_03)");
        sendSignalAndWait(SIGNAL_READY_03);
        
        printlnForDebug("ObjectDebuggee009: After sendSignalAndWait(SIGNAL_READY_03)");
                
        logWriter.println("--> ObjectDebuggee009: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ObjectDebuggee009.class);
    }

}

class TestClass {
    
}


