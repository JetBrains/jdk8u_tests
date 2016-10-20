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
 * Created on 29.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY007;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class MemoryDebuggee007 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = MEMORY007_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = MEMORY007_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "MemoryDebuggee007_Thread_"; 
    public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = MEMORY007_ARRAY_SIZE_FOR_MEMORY_STRESS; 

    static MemoryDebuggee007 memoryDebuggee007This;

    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    static MemoryDebuggee007_Thread[] memoryDebuggee007Threads = null;

    public void run() {
        
        logWriter.println("--> MemoryDebuggee007: START...");
        memoryDebuggee007This = this;

        printlnForDebug("MemoryDebuggee007: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("MemoryDebuggee007: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> MemoryDebuggee007: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee007: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            memoryDebuggee007Threads = new MemoryDebuggee007_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                memoryDebuggee007Threads[i]= new MemoryDebuggee007_Thread(threadName);
                memoryDebuggee007Threads[i].start();
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> MemoryDebuggee007: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> MemoryDebuggee007: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> MemoryDebuggee007: Created threads number = " + createdThreadsNumber);
        
        while ( startedThreadsNumber != createdThreadsNumber ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> MemoryDebuggee007: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee007: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("MemoryDebuggee007: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("MemoryDebuggee007: After sendSignalAndWait(SIGNAL_READY_02)");

        logWriter.println("--> MemoryDebuggee007: Creating memory stress until OutOfMemory");
        createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);
        
        printlnForDebug("MemoryDebuggee007: sendSignalAndWait(SIGNAL_READY_03)");
        sendSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("MemoryDebuggee007: After sendSignalAndWait(SIGNAL_READY_03)");

        logWriter.println
        ("--> MemoryDebuggee007: Send signal to all threads and wait their to finish...");
        allThreadsToFinish = true;
        
        for (int i=0; i < createdThreadsNumber; i++) {
            while ( memoryDebuggee007Threads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
        }
        logWriter.println
        ("--> MemoryDebuggee007: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee007: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("MemoryDebuggee007: sendSignalAndWait(SIGNAL_READY_04)");
        sendSignalAndWait(SIGNAL_READY_04);
        printlnForDebug("MemoryDebuggee007: After sendSignalAndWait(SIGNAL_READY_04)");

        logWriter.println("--> MemoryDebuggee007: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(MemoryDebuggee007.class);
    }

}

class MemoryDebuggee007_Thread extends Thread {
    long[] longArray = null;
    public MemoryDebuggee007_Thread(String name) {
        super(name);
        longArray = new long[1000];
    }

    public void run() {
        MemoryDebuggee007 parent = MemoryDebuggee007.memoryDebuggee007This;
        synchronized (parent) { 
            MemoryDebuggee007.startedThreadsNumber++;
        }
        while ( ! MemoryDebuggee007.allThreadsToFinish ) {
            MemoryDebuggee007.waitMlsecsTime(100);
        }
    }
}


