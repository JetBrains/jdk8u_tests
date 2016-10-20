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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT013;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class EventDebuggee013 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = EVENT013_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = EVENT013_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "EventDebuggee013_Thread_"; 

    static EventDebuggee013 eventDebuggee013This;

    static volatile boolean allThreadsToContinue = false;
    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    static EventDebuggee013_Thread[] eventDebuggee013Threads = null;

    public void run() {
        
        logWriter.println("--> EventDebuggee013: START...");
        eventDebuggee013This = this;

        logWriter.println("--> EventDebuggee013: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee013: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            eventDebuggee013Threads = new EventDebuggee013_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                eventDebuggee013Threads[i]= new EventDebuggee013_Thread(threadName);
                eventDebuggee013Threads[i].start();
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> EventDebuggee013: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> EventDebuggee013: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> EventDebuggee013: Created threads number = " + createdThreadsNumber);
        
        while ( startedThreadsNumber != createdThreadsNumber ) {
            waitMlsecsTime(10);
        }
        logWriter.println
        ("--> EventDebuggee013: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee013: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("EventDebuggee013: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("EventDebuggee013: After sendSignalAndWait(SIGNAL_READY_01)");

        logWriter.println
        ("--> EventDebuggee013: Send signal to all threads to continue and call test method...");
        allThreadsToContinue = true;

        logWriter.println
        ("--> EventDebuggee013: Wait for all started threads to finish...");

        for (int i=0; i < createdThreadsNumber; i++) {
            while ( eventDebuggee013Threads[i].isAlive() ) {
                waitMlsecsTime(10);
            }
        }
        logWriter.println
        ("--> EventDebuggee013: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> EventDebuggee013: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("EventDebuggee013: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("EventDebuggee013: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        logWriter.println("--> EventDebuggee013: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(EventDebuggee013.class);
    }

}

class EventDebuggee013_Thread extends Thread {
    long[] longArray = null;
    String myName = null;
    public EventDebuggee013_Thread(String name) {
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
        EventDebuggee013 parent = EventDebuggee013.eventDebuggee013This;
        synchronized (parent) { 
            EventDebuggee013.startedThreadsNumber++;
        }
        while ( ! EventDebuggee013.allThreadsToContinue ) {
            EventDebuggee013.waitMlsecsTime(100);
        }
        
        testMethod();
        
        testMethod();
    }
}


