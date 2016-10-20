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
 * Created on 06.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD005;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ThreadDebuggee005 extends StressDebuggee {
    public static final String THREAD_NAME = "ThreadDebuggee005_Thread"; 
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke"; 
    public static final int STRESS_THREAD_NUMBER_LIMIT = THREAD005_STRESS_THREAD_NUMBER_LIMIT;
    public static final int ARRAY_SIZE_FOR_THREAD_STRESS = THREAD005_ARRAY_SIZE_FOR_THREAD_STRESS; 

    static ThreadDebuggee005 threadDebuggee005This;

    static ThreadDebuggee005_Thread threadDebuggee005Thread = null;
    
    public static boolean stressThreadsToFinish = false;
    public static volatile int startedStressThreadsNumber = 0;
    static ThreadDebuggee005_StressThread[] stressThreads = null; 
    
    static int methodToInvokeCallNumber = 0;
    
    static int methodToInvoke (long timeMlsecsToRun, int valueToReturn) {
        methodToInvokeCallNumber++;
        try {
            Thread.sleep(timeMlsecsToRun);
        } catch (Throwable thrown ) {
            // ignore
        }
        return valueToReturn;    
    }

    public void run() {
        logWriter.println("--> ThreadDebuggee005: START...");
        threadDebuggee005This = this;

        logWriter.println("--> ThreadDebuggee005: Create and start thread for invoke method...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee005: freeMemory (bytes) BEFORE creating thread = " + freeMemory);

        threadDebuggee005Thread= new ThreadDebuggee005_Thread(THREAD_NAME);
        threadDebuggee005Thread.start();
        
        while ( suspendedThreadsNumber != 1 ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ThreadDebuggee005: Created thread is started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee005: freeMemory (bytes) AFTER creating and starting thread = " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee005: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ThreadDebuggee005: After sendThreadSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> ThreadDebuggee005: Creating threads stress...");
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee005: freeMemory (bytes) BEFORE creating threads stress = " + freeMemory);

        try {
            stressThreads = new ThreadDebuggee005_StressThread[STRESS_THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < STRESS_THREAD_NUMBER_LIMIT; i++) {
                String threadName = "Stress_Thread_" + i;
                stressThreads[i]= new ThreadDebuggee005_StressThread(threadName);
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
        ("--> ThreadDebuggee005: Started stress threads number = " + startedStressThreadsNumber);
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee005: freeMemory (bytes) AFTER creating threads stress = " + freeMemory);

        printlnForDebug("ThreadDebuggee002: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ThreadDebuggee002: After sendThreadSignalAndWait(SIGNAL_READY_02");
        
        logWriter.println
        ("--> ThreadDebuggee005: wait for thread to finish..."); 
        while ( threadDebuggee005Thread.isAlive() ) {
            waitMlsecsTime(100);
        }

        logWriter.println
        ("--> ThreadDebuggee005: Thread finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee005: freeMemory (bytes) AFTER thread finished= " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee005: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("ThreadDebuggee005: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> ThreadDebuggee005: FINISH...");
        System.exit(SUCCESS);

    }

    public static void main(String [] args) {
        runDebuggee(ThreadDebuggee005.class);
    }

}

class ThreadDebuggee005_Thread extends Thread {
    public ThreadDebuggee005_Thread(String name) {
        super(name);
    }

    public void run() {
        ThreadDebuggee005 parent = ThreadDebuggee005.threadDebuggee005This;
        parent.suspendThreadByEvent();
    }
}

class ThreadDebuggee005_StressThread extends Thread {
    long[] longArray = null;
    public ThreadDebuggee005_StressThread(String name) {
        super(name);
        longArray = new long[ThreadDebuggee005.ARRAY_SIZE_FOR_THREAD_STRESS];
    }

    public void run() {
        while ( ! ThreadDebuggee005.stressThreadsToFinish ) {
            ThreadDebuggee005.waitMlsecsTime(100);
        }
    }
}


