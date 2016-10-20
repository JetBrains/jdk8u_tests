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
 * Created on 07.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD006;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ThreadDebuggee006 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = THREAD006_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = THREAD006_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "ThreadDebuggee006_Thread_"; 
    public static final int STRESS_THREAD_NUMBER_LIMIT = THREAD006_STRESS_THREAD_NUMBER_LIMIT;
    public static final int ARRAY_SIZE_FOR_THREAD_STRESS = THREAD006_ARRAY_SIZE_FOR_THREAD_STRESS; 

    static ThreadDebuggee006 threadDebuggee006This;

    static int startedThreadsNumber = 0;

    static ThreadDebuggee006_Thread[] threadDebuggee006Threads = null;
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke"; 
    
    public static boolean stressThreadsToFinish = false;
    public static volatile int startedStressThreadsNumber = 0;
    static ThreadDebuggee006_StressThread[] stressThreads = null; 
    
    static int methodToInvoke (long timeMlsecsToRun, int valueToReturn) {
        try {
            Thread.sleep(timeMlsecsToRun);
        } catch (Throwable thrown ) {
            // ignore
        }
        return valueToReturn;    
    }

    public void run() {
        logWriter.println("--> ThreadDebuggee006: START...");
        threadDebuggee006This = this;

        logWriter.println("--> ThreadDebuggee006: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee006: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            threadDebuggee006Threads = new ThreadDebuggee006_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                threadDebuggee006Threads[i]= new ThreadDebuggee006_Thread(threadName);
                threadDebuggee006Threads[i].start();
                startedThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> ThreadDebuggee006: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> ThreadDebuggee006: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> ThreadDebuggee006: Started threads number = " + startedThreadsNumber);
        
        while ( suspendedThreadsNumber != startedThreadsNumber ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ThreadDebuggee006: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee006: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee006: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ThreadDebuggee006: After sendThreadSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> ThreadDebuggee006: Creating threads stress...");
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee006: freeMemory (bytes) BEFORE creating threads stress = " + freeMemory);

        try {
            stressThreads = new ThreadDebuggee006_StressThread[STRESS_THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < STRESS_THREAD_NUMBER_LIMIT; i++) {
                String threadName = "Stress_Thread_" + i;
                stressThreads[i]= new ThreadDebuggee006_StressThread(threadName);
                stressThreads[i].start();
                startedStressThreadsNumber++;
            }
            logWriter.println
            ("--> ThreadDebuggee005: NO Exception while starting stress threads!");
        } catch ( OutOfMemoryError outOfMem ) {
            logWriter.println
            ("--> ThreadDebuggee005: OutOfMemoryError while starting stress threads: " + outOfMem);
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> ThreadDebuggee005: Exception while starting stress threads: " + thrown);
        }
        logWriter.println
        ("--> ThreadDebuggee006: Started stress threads number = " + startedStressThreadsNumber);
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee006: freeMemory (bytes) AFTER creating threads stress = " + freeMemory);

        printlnForDebug("ThreadDebuggee006: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ThreadDebuggee006: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        logWriter.println
        ("--> ThreadDebuggee006: Wait for all threads to finish...");
        int aliveThreads = 1;
        while ( aliveThreads > 0 ) {
            waitMlsecsTime(100);
            aliveThreads = 0;
            for (int i=0; i < startedThreadsNumber; i++) {
                if ( threadDebuggee006Threads[i].isAlive() ) {
                    aliveThreads++;
                }
            }
        }
        
        logWriter.println
        ("--> ThreadDebuggee006: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee006: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee006: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("ThreadDebuggee006: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> ThreadDebuggee006: FINISH...");
        System.exit(SUCCESS);

    }

    public static void main(String [] args) {
        runDebuggee(ThreadDebuggee006.class);
    }

}

class ThreadDebuggee006_Thread extends Thread {
    long[] longArray = null;
    public ThreadDebuggee006_Thread(String name) {
        super(name);
        longArray = new long[1000];
    }

    public void run() {
        ThreadDebuggee006 parent = ThreadDebuggee006.threadDebuggee006This;
        parent.suspendThreadByEvent();
    }
}

class ThreadDebuggee006_StressThread extends Thread {
    long[] longArray = null;
    public ThreadDebuggee006_StressThread(String name) {
        super(name);
        longArray = new long[ThreadDebuggee006.ARRAY_SIZE_FOR_THREAD_STRESS];
    }

    public void run() {
        while ( ! ThreadDebuggee006.stressThreadsToFinish ) {
            ThreadDebuggee006.waitMlsecsTime(100);
        }
    }
}


