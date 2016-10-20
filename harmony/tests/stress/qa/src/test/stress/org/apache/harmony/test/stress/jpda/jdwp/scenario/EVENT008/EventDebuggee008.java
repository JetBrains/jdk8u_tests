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
 * Created on 17.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT008;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

    public class EventDebuggee008 extends StressDebuggee {
        public static final long FREE_MEMORY_LIMIT = EVENT008_FREE_MEMORY_LIMIT; 
        public static final int THREAD_NUMBER_LIMIT = EVENT008_THREAD_NUMBER_LIMIT; 
        public static final String THREAD_NAME_PATTERN = "EventDebuggee008_Thread_"; 
        public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = EVENT008_ARRAY_SIZE_FOR_MEMORY_STRESS; 

        static EventDebuggee008 eventDebuggee008This;

        static volatile boolean allThreadsToContinue = false;
        static volatile boolean allThreadsToFinish = false;
        static int createdThreadsNumber = 0;
        static volatile int startedThreadsNumber = 0;
        static volatile int continuedThreadsNumber = 0;

        static EventDebuggee008_Thread[] eventDebuggee008Threads = null;

        public void run() {
            
            logWriter.println("--> EventDebuggee008: START...");
            eventDebuggee008This = this;

            logWriter.println("--> EventDebuggee008: Create and start big number of threads...");
            Runtime currentRuntime = Runtime.getRuntime();
            long freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee008: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

            try {
                eventDebuggee008Threads = new EventDebuggee008_Thread[THREAD_NUMBER_LIMIT]; 
                for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                    String threadName = THREAD_NAME_PATTERN + i;
                    eventDebuggee008Threads[i]= new EventDebuggee008_Thread(threadName);
                    eventDebuggee008Threads[i].start();
                    createdThreadsNumber++;
                    freeMemory = currentRuntime.freeMemory();
                    if ( freeMemory < FREE_MEMORY_LIMIT ) {
                        logWriter.println
                        ("--> EventDebuggee008: FREE_MEMORY_LIMIT (" + 
                                FREE_MEMORY_LIMIT + ") is reached!");
                        break;   
                    }
                }
            } catch ( Throwable thrown) {
                logWriter.println
                ("--> EventDebuggee008: Exception while creating threads: " + thrown);
            }
            logWriter.println
            ("--> EventDebuggee008: Created threads number = " + createdThreadsNumber);
            
            while ( startedThreadsNumber != createdThreadsNumber ) {
                waitMlsecsTime(10);
            }
            logWriter.println
            ("--> EventDebuggee008: All created threads are started!");
            
            freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee008: freeMemory (bytes) AFTER creating and starting threads = " + 
                    freeMemory);

            printlnForDebug("EventDebuggee008: sendSignalAndWait(SIGNAL_READY_01)");
            sendSignalAndWait(SIGNAL_READY_01);
            printlnForDebug("EventDebuggee008: After sendSignalAndWait(SIGNAL_READY_01)");

            logWriter.println("--> EventDebuggee008: Creating memory stress until OutOfMemory");
            createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);
            printlnForDebug("EventDebuggee008: sendSignalAndWait(SIGNAL_READY_02)");
            sendSignalAndWait(SIGNAL_READY_02);
            printlnForDebug("EventDebuggee008: After sendSignalAndWait(SIGNAL_READY_02)");
            
            logWriter.println
            ("--> EventDebuggee008: Send signal to all threads to continue and call test method...");
            allThreadsToContinue = true;

            while ( startedThreadsNumber != continuedThreadsNumber ) {
                waitMlsecsTime(10);
            }
            logWriter.println
            ("--> EventDebuggee008: All threads are continued!");

            printlnForDebug("EventDebuggee008: sendSignalAndWait(SIGNAL_READY_03)");
            sendSignalAndWait(SIGNAL_READY_03);
            printlnForDebug("EventDebuggee008: After sendSignalAndWait(SIGNAL_READY_03)");

            logWriter.println
            ("--> EventDebuggee008: Wait for all started threads to finish...");
            allThreadsToFinish = true;

            for (int i=0; i < createdThreadsNumber; i++) {
                while ( eventDebuggee008Threads[i].isAlive() ) {
                    waitMlsecsTime(10);
                }
            }
            logWriter.println
            ("--> EventDebuggee008: All threads finished!");

            freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee008: freeMemory (bytes) AFTER all threads finished= " + 
                    freeMemory);

            printlnForDebug("EventDebuggee008: sendThreadSignalAndWait(SIGNAL_READY_04)");
            sendThreadSignalAndWait(SIGNAL_READY_04);
            printlnForDebug("EventDebuggee008: After sendThreadSignalAndWait(SIGNAL_READY_04)");

            logWriter.println("--> EventDebuggee008: FINISH...");

        }

        public static void main(String [] args) {
            runDebuggee(EventDebuggee008.class);
        }

    }

    class EventDebuggee008_Thread extends Thread {
        long[] longArray = null;
        String myName = null;
        public EventDebuggee008_Thread(String name) {
            super(name);
            myName = name;
            longArray = new long[1000];
        }
        
        void testMethod() {
            int intVar = 1;
            long longVar = 2;
        }

        void testMethod_2() {
            int intVar = 1;
            long longVar = 2;
        }

        public void run() {
            EventDebuggee008 parent = EventDebuggee008.eventDebuggee008This;
            synchronized (parent) { 
                EventDebuggee008.startedThreadsNumber++;
            }
            while ( ! EventDebuggee008.allThreadsToContinue ) {
                EventDebuggee008.waitMlsecsTime(100);
            }
            
            testMethod();
            
            synchronized (parent) { 
                EventDebuggee008.continuedThreadsNumber++;
            }

            while ( ! EventDebuggee008.allThreadsToFinish ) {
                EventDebuggee008.waitMlsecsTime(100);
            }
            
        }
    }


