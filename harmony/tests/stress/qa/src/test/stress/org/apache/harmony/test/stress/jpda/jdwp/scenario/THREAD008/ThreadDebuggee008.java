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
 * Created on 21.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD008;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ThreadDebuggee008 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = THREAD008_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = THREAD008_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "ThreadDebuggee008_Thread_"; 

    static ThreadDebuggee008 threadDebuggee008This;

    static int startedThreadsNumber = 0;

    static ThreadDebuggee008_Thread[] threadDebuggee008Threads = null;
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
        logWriter.println("--> ThreadDebuggee008: START...");
        threadDebuggee008This = this;

        logWriter.println("--> ThreadDebuggee008: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee008: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            threadDebuggee008Threads = new ThreadDebuggee008_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                threadDebuggee008Threads[i]= new ThreadDebuggee008_Thread(threadName);
                threadDebuggee008Threads[i].start();
                startedThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> ThreadDebuggee008: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> ThreadDebuggee008: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> ThreadDebuggee008: Started threads number = " + startedThreadsNumber);
        
        while ( suspendedThreadsNumber != startedThreadsNumber ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ThreadDebuggee008: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee008: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee008: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ThreadDebuggee008: After sendThreadSignalAndWait(SIGNAL_READY_01)");

        logWriter.println
        ("--> ThreadDebuggee008: Wait for all threads to finish...");
        int aliveThreads = 1;
        while ( aliveThreads > 0 ) {
            waitMlsecsTime(100);
            aliveThreads = 0;
            for (int i=0; i < startedThreadsNumber; i++) {
                if ( threadDebuggee008Threads[i].isAlive() ) {
                    aliveThreads++;
                }
            }
        }
        
        logWriter.println
        ("--> ThreadDebuggee008: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee008: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee008: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ThreadDebuggee008: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        logWriter.println("--> ThreadDebuggee008: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ThreadDebuggee008.class);
    }

}

class ThreadDebuggee008_Thread extends Thread {
    long[] longArray = null;
    public ThreadDebuggee008_Thread(String name) {
        super(name);
        longArray = new long[1000];
    }

    public void run() {
        ThreadDebuggee008 parent = ThreadDebuggee008.threadDebuggee008This;
        parent.suspendThreadByEvent();
    }
}


