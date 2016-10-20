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
 * Created on 14.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY002;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class MemoryDebuggee002 extends StressDebuggee {
    public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = MEMORY002_ARRAY_SIZE_FOR_MEMORY_STRESS; 
    
    public void run() {
        
        logWriter.println("--> MemoryDebuggee002: START...");

        Memory002_TestClass01 testClass01 = new Memory002_TestClass01();
        Memory002_TestClass02 testClass02 = new Memory002_TestClass02();
        Memory002_TestClass03 testClass03 = new Memory002_TestClass03();
        Memory002_TestClass04 testClass04 = new Memory002_TestClass04();
        Memory002_TestClass05 testClass05 = new Memory002_TestClass05();

        logWriter.println("--> MemoryDebuggee002: Creating memory stress until OutOfMemory");
        createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);
        
        printlnForDebug("MemoryDebuggee002: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("MemoryDebuggee002: After sendSignalAndWait(SIGNAL_READY_01)");
        

        logWriter.println("--> MemoryDebuggee002: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(MemoryDebuggee002.class);
    }

}

class Memory002_TestClass01 {}
class Memory002_TestClass02 {}
class Memory002_TestClass03 {}
class Memory002_TestClass04 {}
class Memory002_TestClass05 {}


