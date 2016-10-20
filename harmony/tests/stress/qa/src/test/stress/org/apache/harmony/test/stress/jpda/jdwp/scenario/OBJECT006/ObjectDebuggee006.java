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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT006;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ObjectDebuggee006 extends StressDebuggee {
    
    static ObjectDebuggee006 objectDebuggee006This;
    
    public static Object06_TestClass [] classArray = new Object06_TestClass [OBJECT006_ARRAY_LENGTH];
    public static Object06_TestClass [] anotherClassArray = new Object06_TestClass [OBJECT006_ARRAY_LENGTH];

    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    public void run() {
        logWriter.println("--> ObjectDebuggee006: START...");
        objectDebuggee006This = this;

        printlnForDebug("ObjectDebuggee006: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ObjectDebuggee006: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> ObjectDebuggee006: Create big array of objects...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee006: freeMemory (bytes) BEFORE creating array of objects = " + freeMemory);
        
        for (int i=0; i < classArray.length; i++) {
            classArray[i] = new Object06_TestClass (); 
            freeMemory = currentRuntime.freeMemory();
        }
                       
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee006: freeMemory (bytes) AFTER creating big array of objects = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee006: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        
        
        logWriter.println("--> ObjectDebuggee006: Create another big array of objects...");
        for (int i=0; i < anotherClassArray.length; i++) {
            anotherClassArray[i] = new Object06_TestClass (); 
        }
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee006: freeMemory (bytes) AFTER creating second big array of objects = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee006: sendSignalAndWait(SIGNAL_READY_03)");
        sendSignalAndWait(SIGNAL_READY_03);
        
        printlnForDebug("ObjectDebuggee006: After sendSignalAndWait(SIGNAL_READY_03)");
                
        logWriter.println("--> ObjectDebuggee006: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ObjectDebuggee006.class);
    }

}

class Object06_TestClass  {
    
}


