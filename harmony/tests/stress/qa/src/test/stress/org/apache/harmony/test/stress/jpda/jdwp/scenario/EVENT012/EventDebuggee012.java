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
 * Created on 18.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT012;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class EventDebuggee012 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = EVENT012_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = EVENT012_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "EventDebuggee012_Thread_"; 
    public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = EVENT012_ARRAY_SIZE_FOR_MEMORY_STRESS; 

    static EventDebuggee012 eventDebuggee012This;

    static volatile boolean allThreadsToContinue = false;
    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    static EventDebuggee012_Thread[] eventDebuggee012Threads = null;

    public void run() {
        
        logWriter.println("--> EventDebuggee012: START...");
        eventDebuggee012This = this;

        logWriter.println("--> EventDebuggee012: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee012: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            eventDebuggee012Threads = new EventDebuggee012_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                eventDebuggee012Threads[i]= new EventDebuggee012_Thread(threadName);
                eventDebuggee012Threads[i].start();
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> EventDebuggee012: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> EventDebuggee012: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> EventDebuggee012: Created threads number = " + createdThreadsNumber);
        
        while ( startedThreadsNumber != createdThreadsNumber ) {
            waitMlsecsTime(10);
        }
        logWriter.println
        ("--> EventDebuggee012: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee012: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("EventDebuggee012: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("EventDebuggee012: After sendSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> EventDebuggee012: Creating memory stress until OutOfMemory");
        createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);
        printlnForDebug("EventDebuggee012: sendSignalAndWait(SIGNAL_READY_02)");
        sendSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("EventDebuggee012: After sendSignalAndWait(SIGNAL_READY_02)");

        logWriter.println
        ("--> EventDebuggee012: Send signal to all threads to continue and call test method...");
        allThreadsToContinue = true;

        logWriter.println
        ("--> EventDebuggee012: Wait for all started threads to finish...");

        for (int i=0; i < createdThreadsNumber; i++) {
            while ( eventDebuggee012Threads[i].isAlive() ) {
                waitMlsecsTime(10);
            }
        }
        logWriter.println
        ("--> EventDebuggee012: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee012: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("EventDebuggee012: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("EventDebuggee012: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> EventDebuggee012: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(EventDebuggee012.class);
    }

}

class EventDebuggee012_Thread extends Thread {
    long[] longArray = null;
    String myName = null;
    public EventDebuggee012_Thread(String name) {
        super(name);
        myName = name;
        longArray = new long[1000];
    }
    
    void testMethod() {
        int intVar = 1;
        long longVar = 2;
    }

    public void run() {
        EventDebuggee012 parent = EventDebuggee012.eventDebuggee012This;
        synchronized (parent) { 
            EventDebuggee012.startedThreadsNumber++;
        }
        while ( ! EventDebuggee012.allThreadsToContinue ) {
            EventDebuggee012.waitMlsecsTime(100);
        }
        
        testMethod();
        
        testMethod();
    }
}


