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
 * Created on 16.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY006;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class MemoryDebuggee006 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = MEMORY006_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = MEMORY006_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "MemoryDebuggee006_Thread_"; 

    static MemoryDebuggee006 memoryDebuggee006This;

    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    static MemoryDebuggee006_Thread[] memoryDebuggee006Threads = null;

    public void run() {
        
        logWriter.println("--> MemoryDebuggee006: START...");
        memoryDebuggee006This = this;

        printlnForDebug("MemoryDebuggee006: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("MemoryDebuggee006: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> MemoryDebuggee006: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee006: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            memoryDebuggee006Threads = new MemoryDebuggee006_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                memoryDebuggee006Threads[i]= new MemoryDebuggee006_Thread(threadName);
                memoryDebuggee006Threads[i].start();
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> MemoryDebuggee006: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> MemoryDebuggee006: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> MemoryDebuggee006: Created threads number = " + createdThreadsNumber);
        
        while ( startedThreadsNumber != createdThreadsNumber ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> MemoryDebuggee006: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee006: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("MemoryDebuggee006: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("MemoryDebuggee006: After sendSignalAndWait(SIGNAL_READY_02)");

        logWriter.println
        ("--> MemoryDebuggee006: Send signal to all threads and wait their to finish...");
        allThreadsToFinish = true;

        for (int i=0; i < createdThreadsNumber; i++) {
            while ( memoryDebuggee006Threads[i].isAlive() ) {
                waitMlsecsTime(10);
            }
        }
        logWriter.println
        ("--> MemoryDebuggee006: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> MemoryDebuggee006: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("MemoryDebuggee006: sendSignalAndWait(SIGNAL_READY_03)");
        sendSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("MemoryDebuggee006: After sendSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> MemoryDebuggee006: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(MemoryDebuggee006.class);
    }

}

class MemoryDebuggee006_Thread extends Thread {
    long[] longArray = null;
    public MemoryDebuggee006_Thread(String name) {
        super(name);
        longArray = new long[1000];
    }

    public void run() {
        MemoryDebuggee006 parent = MemoryDebuggee006.memoryDebuggee006This;
        synchronized (parent) { 
            MemoryDebuggee006.startedThreadsNumber++;
        }
        while ( ! MemoryDebuggee006.allThreadsToFinish ) {
            MemoryDebuggee006.waitMlsecsTime(100);
        }
    }
}


