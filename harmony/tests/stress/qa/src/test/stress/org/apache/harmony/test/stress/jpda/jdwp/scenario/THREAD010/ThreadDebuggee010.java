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
 * Created on 24.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD010;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ThreadDebuggee010 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = THREAD010_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = THREAD010_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "ThreadDebuggee010_Thread_"; 
    public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = THREAD010_ARRAY_SIZE_FOR_MEMORY_STRESS; 

    static ThreadDebuggee010 threadDebuggee010This;

    static int startedThreadsNumber = 0;

    static ThreadDebuggee010_Thread[] threadDebuggee010Threads = null;
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke"; 

    
    int methodToInvoke (long timeMlsecsToRun, int valueToReturn) {
        try {
            Thread.sleep(timeMlsecsToRun);
        } catch (Throwable thrown ) {
            // ignore
        }
        return valueToReturn;    
    }

    public void run() {
        logWriter.println("--> ThreadDebuggee010: START...");
        threadDebuggee010This = this;

        logWriter.println("--> ThreadDebuggee010: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee010: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            threadDebuggee010Threads = new ThreadDebuggee010_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                threadDebuggee010Threads[i]= new ThreadDebuggee010_Thread(threadName);
                threadDebuggee010Threads[i].start();
                startedThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> ThreadDebuggee010: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> ThreadDebuggee010: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> ThreadDebuggee010: Started threads number = " + startedThreadsNumber);
        
        while ( suspendedThreadsNumber != startedThreadsNumber ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ThreadDebuggee010: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee010: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee010: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ThreadDebuggee010: After sendThreadSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> ThreadDebuggee010: Creating memory stress until OutOfMemory");
        createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);

        printlnForDebug("ThreadDebuggee010: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ThreadDebuggee010: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        logWriter.println
        ("--> ThreadDebuggee010: Wait for all threads to finish...");
        int aliveThreads = 1;
        while ( aliveThreads > 0 ) {
            waitMlsecsTime(100);
            aliveThreads = 0;
            for (int i=0; i < startedThreadsNumber; i++) {
                if ( threadDebuggee010Threads[i].isAlive() ) {
                    aliveThreads++;
                }
            }
        }
        
        logWriter.println
        ("--> ThreadDebuggee010: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee010: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee010: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("ThreadDebuggee010: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> ThreadDebuggee010: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ThreadDebuggee010.class);
    }

}

class ThreadDebuggee010_Thread extends Thread {
    long[] longArray = null;
    public ThreadDebuggee010_Thread(String name) {
        super(name);
        longArray = new long[1000];
    }

    public void run() {
        ThreadDebuggee010 parent = ThreadDebuggee010.threadDebuggee010This;
        parent.suspendThreadByEvent();
    }
}


