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
 * Created on 14.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT005;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

    public class EventDebuggee005 extends StressDebuggee {
        public static final long FREE_MEMORY_LIMIT = EVENT005_FREE_MEMORY_LIMIT; 
        public static final int THREAD_NUMBER_LIMIT = EVENT005_THREAD_NUMBER_LIMIT; 
        public static final String THREAD_NAME_PATTERN = "EventDebuggee005_Thread_"; 
        public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = EVENT005_ARRAY_SIZE_FOR_MEMORY_STRESS; 

        static EventDebuggee005 eventDebuggee005This;

        static volatile boolean allThreadsToContinue = false;
        static volatile boolean allThreadsToFinish = false;
        static int createdThreadsNumber = 0;
        static volatile int startedThreadsNumber = 0;

        static EventDebuggee005_Thread[] eventDebuggee005Threads = null;

        public void run() {
            
            logWriter.println("--> EventDebuggee005: START...");
            eventDebuggee005This = this;

            logWriter.println("--> EventDebuggee005: Create and start big number of threads...");
            Runtime currentRuntime = Runtime.getRuntime();
            long freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee005: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

            try {
                eventDebuggee005Threads = new EventDebuggee005_Thread[THREAD_NUMBER_LIMIT]; 
                for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                    String threadName = THREAD_NAME_PATTERN + i;
                    eventDebuggee005Threads[i]= new EventDebuggee005_Thread(threadName);
                    eventDebuggee005Threads[i].start();
                    createdThreadsNumber++;
                    freeMemory = currentRuntime.freeMemory();
                    if ( freeMemory < FREE_MEMORY_LIMIT ) {
                        logWriter.println
                        ("--> EventDebuggee005: FREE_MEMORY_LIMIT (" + 
                                FREE_MEMORY_LIMIT + ") is reached!");
                        break;   
                    }
                }
            } catch ( Throwable thrown) {
                logWriter.println
                ("--> EventDebuggee005: Exception while creating threads: " + thrown);
            }
            logWriter.println
            ("--> EventDebuggee005: Created threads number = " + createdThreadsNumber);
            
            while ( startedThreadsNumber != createdThreadsNumber ) {
                waitMlsecsTime(10);
            }
            logWriter.println
            ("--> EventDebuggee005: All created threads are started!");
            
            freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee005: freeMemory (bytes) AFTER creating and starting threads = " + 
                    freeMemory);

            printlnForDebug("EventDebuggee005: sendSignalAndWait(SIGNAL_READY_01)");
            sendSignalAndWait(SIGNAL_READY_01);
            printlnForDebug("EventDebuggee005: After sendSignalAndWait(SIGNAL_READY_01)");
            
            // Create memory stress
            logWriter.println("--> EventDebuggee005: Creating memory stress until OutOfMemory");
            createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);
            printlnForDebug("EventDebuggee005: sendSignalAndWait(SIGNAL_READY_02)");
            sendSignalAndWait(SIGNAL_READY_02);
            printlnForDebug("EventDebuggee005: After sendSignalAndWait(SIGNAL_READY_02)");

            logWriter.println
            ("--> EventDebuggee005: Send signal to all threads to continue and call test method...");
            allThreadsToContinue = true;

            logWriter.println
            ("--> EventDebuggee005: Wait for all started threads to finish...");

            for (int i=0; i < createdThreadsNumber; i++) {
                while ( eventDebuggee005Threads[i].isAlive() ) {
                    waitMlsecsTime(10);
                }
            }
            logWriter.println
            ("--> EventDebuggee005: All threads finished!");

            freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee005: freeMemory (bytes) AFTER all threads finished= " + 
                    freeMemory);

            printlnForDebug("EventDebuggee005: sendThreadSignalAndWait(SIGNAL_READY_03)");
            sendThreadSignalAndWait(SIGNAL_READY_03);
            printlnForDebug("EventDebuggee005: After sendThreadSignalAndWait(SIGNAL_READY_03)");

            logWriter.println("--> EventDebuggee005: FINISH...");

        }

        public static void main(String [] args) {
            runDebuggee(EventDebuggee005.class);
        }

    }

    class EventDebuggee005_Thread extends Thread {
        long[] longArray = null;
        String myName = null;
        public EventDebuggee005_Thread(String name) {
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
            EventDebuggee005 parent = EventDebuggee005.eventDebuggee005This;
            synchronized (parent) { 
                EventDebuggee005.startedThreadsNumber++;
            }
            while ( ! EventDebuggee005.allThreadsToContinue ) {
                EventDebuggee005.waitMlsecsTime(100);
            }
            
            testMethod();
            testMethod_2();
            
        }
    }


