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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT004;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

    public class EventDebuggee004 extends StressDebuggee {
        public static final long FREE_MEMORY_LIMIT = EVENT004_FREE_MEMORY_LIMIT; 
        public static final int THREAD_NUMBER_LIMIT = EVENT004_THREAD_NUMBER_LIMIT; 
        public static final String THREAD_NAME_PATTERN = "EventDebuggee004_Thread_"; 

        static EventDebuggee004 eventDebuggee004This;

        static volatile boolean allThreadsToContinue = false;
        static volatile boolean allThreadsToFinish = false;
        static int createdThreadsNumber = 0;
        static volatile int startedThreadsNumber = 0;
        static volatile int continuedThreadsNumber = 0;

        static EventDebuggee004_Thread[] eventDebuggee004Threads = null;

        public void run() {
            
            logWriter.println("--> EventDebuggee004: START...");
            eventDebuggee004This = this;

            logWriter.println("--> EventDebuggee004: Create and start big number of threads...");
            Runtime currentRuntime = Runtime.getRuntime();
            long freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee004: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

            try {
                eventDebuggee004Threads = new EventDebuggee004_Thread[THREAD_NUMBER_LIMIT]; 
                for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                    String threadName = THREAD_NAME_PATTERN + i;
                    eventDebuggee004Threads[i]= new EventDebuggee004_Thread(threadName);
                    eventDebuggee004Threads[i].start();
                    createdThreadsNumber++;
                    freeMemory = currentRuntime.freeMemory();
                    if ( freeMemory < FREE_MEMORY_LIMIT ) {
                        logWriter.println
                        ("--> EventDebuggee004: FREE_MEMORY_LIMIT (" + 
                                FREE_MEMORY_LIMIT + ") is reached!");
                        break;   
                    }
                }
            } catch ( Throwable thrown) {
                logWriter.println
                ("--> EventDebuggee004: Exception while creating threads: " + thrown);
            }
            logWriter.println
            ("--> EventDebuggee004: Created threads number = " + createdThreadsNumber);
            
            while ( startedThreadsNumber != createdThreadsNumber ) {
                waitMlsecsTime(10);
            }
            logWriter.println
            ("--> EventDebuggee004: All created threads are started!");
            
            freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee004: freeMemory (bytes) AFTER creating and starting threads = " + 
                    freeMemory);

            printlnForDebug("EventDebuggee004: sendSignalAndWait(SIGNAL_READY_01)");
            sendSignalAndWait(SIGNAL_READY_01);
            printlnForDebug("EventDebuggee004: After sendSignalAndWait(SIGNAL_READY_01)");

            logWriter.println
            ("--> EventDebuggee004: Send signal to all threads to continue and call test method...");
            allThreadsToContinue = true;

            while ( startedThreadsNumber != continuedThreadsNumber ) {
                waitMlsecsTime(10);
            }
            logWriter.println
            ("--> EventDebuggee004: All threads are continued!");

            printlnForDebug("EventDebuggee004: sendSignalAndWait(SIGNAL_READY_02)");
            sendSignalAndWait(SIGNAL_READY_02);
            printlnForDebug("EventDebuggee004: After sendSignalAndWait(SIGNAL_READY_02)");

            logWriter.println
            ("--> EventDebuggee004: Wait for all started threads to finish...");
            allThreadsToFinish = true;

            for (int i=0; i < createdThreadsNumber; i++) {
                while ( eventDebuggee004Threads[i].isAlive() ) {
                    waitMlsecsTime(10);
                }
            }
            logWriter.println
            ("--> EventDebuggee004: All threads finished!");

            freeMemory = currentRuntime.freeMemory();
            logWriter.println
            ("--> EventDebuggee004: freeMemory (bytes) AFTER all threads finished= " + 
                    freeMemory);

            printlnForDebug("EventDebuggee004: sendThreadSignalAndWait(SIGNAL_READY_03)");
            sendThreadSignalAndWait(SIGNAL_READY_03);
            printlnForDebug("EventDebuggee004: After sendThreadSignalAndWait(SIGNAL_READY_03)");

            logWriter.println("--> EventDebuggee004: FINISH...");

        }

        public static void main(String [] args) {
            runDebuggee(EventDebuggee004.class);
        }

    }

    class EventDebuggee004_Thread extends Thread {
        long[] longArray = null;
        String myName = null;
        public EventDebuggee004_Thread(String name) {
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
            EventDebuggee004 parent = EventDebuggee004.eventDebuggee004This;
            synchronized (parent) { 
                EventDebuggee004.startedThreadsNumber++;
            }
            while ( ! EventDebuggee004.allThreadsToContinue ) {
                EventDebuggee004.waitMlsecsTime(100);
            }
            
            testMethod();
            
            synchronized (parent) { 
                EventDebuggee004.continuedThreadsNumber++;
            }

            while ( ! EventDebuggee004.allThreadsToFinish ) {
                EventDebuggee004.waitMlsecsTime(100);
            }
            
        }
    }


