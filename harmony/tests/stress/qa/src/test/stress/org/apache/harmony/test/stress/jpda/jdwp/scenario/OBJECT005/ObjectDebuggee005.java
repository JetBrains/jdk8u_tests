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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT005;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ObjectDebuggee005 extends StressDebuggee {
    
    static ObjectDebuggee005 objectDebuggee005This;
    
    public static Object05_TestClass[] classArray = new Object05_TestClass[OBJECT005_ARRAY_LENGTH];

    public void run() {
        logWriter.println("--> ObjectDebuggee005: START...");
        objectDebuggee005This = this;

        printlnForDebug("ObjectDebuggee005: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ObjectDebuggee005: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> ObjectDebuggee005: Create big array of objects and memory stressing...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee005: freeMemory (bytes) BEFORE creating array of objects = " + freeMemory);
        
        for (int i=0; i < classArray.length; i++) {
            classArray[i] = new Object05_TestClass(); 
            freeMemory = currentRuntime.freeMemory();
        }
        
        logWriter.println("--> ObjectDebuggee005: Create memory stress...");
        createMemoryStress(1000000, 1000000);
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee005: freeMemory (bytes) AFTER creating big array of objects and memory stress = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee005: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ObjectDebuggee005: After sendSignalAndWait(SIGNAL_READY_02)");
                
        logWriter.println("--> ObjectDebuggee005: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ObjectDebuggee005.class);
    }

}

class Object05_TestClass  {
    
}


