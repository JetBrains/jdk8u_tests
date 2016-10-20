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
 * Created on 20.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT014;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class EventDebuggee014 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = EVENT014_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = EVENT014_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "EventDebuggee014_Thread_"; 
    public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = EVENT014_ARRAY_SIZE_FOR_MEMORY_STRESS; 

    static EventDebuggee014 eventDebuggee014This;

    static volatile boolean allThreadsToContinue = false;
    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    static EventDebuggee014_Thread[] eventDebuggee014Threads = null;

    public void run() {
        
        logWriter.println("--> EventDebuggee014: START...");
        eventDebuggee014This = this;

        logWriter.println("--> EventDebuggee014: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee014: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            eventDebuggee014Threads = new EventDebuggee014_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                eventDebuggee014Threads[i]= new EventDebuggee014_Thread(threadName);
                eventDebuggee014Threads[i].start();
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> EventDebuggee014: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> EventDebuggee014: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> EventDebuggee014: Created threads number = " + createdThreadsNumber);
        
        while ( startedThreadsNumber != createdThreadsNumber ) {
            waitMlsecsTime(10);
        }
        logWriter.println
        ("--> EventDebuggee014: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee014: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("EventDebuggee014: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("EventDebuggee014: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println
        ("--> EventDebuggee014: Send signal to all threads to continue and call test method...");
        allThreadsToContinue = true;

        logWriter.println
        ("--> EventDebuggee014: Send 'SIGNAL_READY_02' signal to debugger - ready to create " +
                "memory stress.");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("EventDebuggee014: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        logWriter.println("--> EventDebuggee014: Creating memory stress until OutOfMemory");
        createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);

        logWriter.println
        ("--> EventDebuggee014: Send 'SIGNAL_READY_03' signal to debugger - memory stress created!");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("EventDebuggee014: After sendThreadSignalAndWait(SIGNAL_READY_03)");
        
        logWriter.println
        ("--> EventDebuggee014: Wait for all started threads to finish...");

        for (int i=0; i < createdThreadsNumber; i++) {
            while ( eventDebuggee014Threads[i].isAlive() ) {
                waitMlsecsTime(10);
            }
        }
        logWriter.println
        ("--> EventDebuggee014: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee014: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("EventDebuggee014: sendThreadSignalAndWait(SIGNAL_READY_04)");
        sendThreadSignalAndWait(SIGNAL_READY_04);
        printlnForDebug("EventDebuggee014: After sendThreadSignalAndWait(SIGNAL_READY_04)");

        logWriter.println("--> EventDebuggee014: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(EventDebuggee014.class);
    }

}

class EventDebuggee014_Thread extends Thread {
    long[] longArray = null;
    String myName = null;
    public EventDebuggee014_Thread(String name) {
        super(name);
        myName = name;
        longArray = new long[1000];
    }
    
    void testMethod() {
        int intVar_1 = 1;
        int intVar_2 = 1;
        int intVar_3 = 1;
        return;
    }

    public void run() {
        EventDebuggee014 parent = EventDebuggee014.eventDebuggee014This;
        synchronized (parent) { 
            EventDebuggee014.startedThreadsNumber++;
        }
        while ( ! EventDebuggee014.allThreadsToContinue ) {
            EventDebuggee014.waitMlsecsTime(100);
        }
        
        testMethod();
        
        testMethod();
    }
}


