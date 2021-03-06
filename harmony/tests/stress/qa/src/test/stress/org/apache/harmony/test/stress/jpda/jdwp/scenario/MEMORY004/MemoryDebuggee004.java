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
 * Created on 15.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY004;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class MemoryDebuggee004 extends StressDebuggee {
    public static final int SMALL_ARRAY_LENGTH = MEMORY004_SMALL_ARRAY_LENGTH; 
    public static final int BIG_ARRAY_LENGTH = MEMORY004_BIG_ARRAY_LENGTH; 
    public static final long FREE_MEMORY_LIMIT = MEMORY004_FREE_MEMORY_LIMIT; 
    
    static Object[] smallObjectArray = null;
    static Object[] bigObjectArray = null;
    static int bigObjectArrayActualLength = 0;
    
    long[] longArray = null;
    
    public MemoryDebuggee004 () {
        super();
        longArray = new long[100];
    }

    public void run() {
        
        logWriter.println("--> MemoryDebuggee004: START...");

        logWriter.println("--> MemoryDebuggee004: Create smallObjectArray...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee004: freeMemory (bytes) BEFORE creating smallObjectArray = " + freeMemory);

        smallObjectArray = new Object[SMALL_ARRAY_LENGTH];
        
        for (int i=0; i < SMALL_ARRAY_LENGTH; i++) {
            smallObjectArray[i]= new MemoryDebuggee004();
        }
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee004: freeMemory (bytes) AFTER creating smallObjectArray = " + freeMemory);
        
        printlnForDebug("MemoryDebuggee004: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("MemoryDebuggee004: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> MemoryDebuggee004: Create bigObjectArray...");
        currentRuntime = Runtime.getRuntime();
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee004: freeMemory (bytes) BEFORE creating bigObjectArray = " + freeMemory);

        try {
            bigObjectArray = new Object[BIG_ARRAY_LENGTH];
            for (int i=0; i < BIG_ARRAY_LENGTH; i++) {
                bigObjectArray[i]= new MemoryDebuggee004();
                bigObjectArrayActualLength++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    printlnForDebug("FREE_MEMORY_LIMIT (" + FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> MemoryDebuggee004: Exception while creating bigObjectArray: " + thrown);
        }
        logWriter.println
        ("--> MemoryDebuggee004: Created bigObjectArray elements number = "
                + bigObjectArrayActualLength);

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee004: freeMemory (bytes) AFTER creating bigObjectArray = " + freeMemory);

        printlnForDebug("MemoryDebuggee004: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("MemoryDebuggee004: After sendSignalAndWait(SIGNAL_READY_02)");

        logWriter.println("--> MemoryDebuggee004: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(MemoryDebuggee004.class);
    }

}



