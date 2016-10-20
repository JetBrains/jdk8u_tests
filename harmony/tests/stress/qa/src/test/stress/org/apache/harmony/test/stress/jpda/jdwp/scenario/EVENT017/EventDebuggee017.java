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
 * Created on 23.08.2006 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT017;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class EventDebuggee017 extends StressDebuggee {
    public static final String TESTED_THREAD_NAME_PATTERN = "EventDebuggee017_Thread_"; 
    public static final int THE_SAME_KIND_TESTED_THREADS_NUMBER = 2; 
    public static final int ALL_TESTED_THREADS_NUMBER = THE_SAME_KIND_TESTED_THREADS_NUMBER * 3; 
    public static final int CHECKED_THREADS_NUMBER = THE_SAME_KIND_TESTED_THREADS_NUMBER * 2; 
    public static final String STARTER_THREAD_NAME_PATTERN = "EventDebuggee017_StarterThread_"; 

    static volatile boolean[] threadsToFinish = new boolean[ALL_TESTED_THREADS_NUMBER];

    static EventDebuggee017_Thread[] eventDebuggee017Threads = 
        new EventDebuggee017_Thread[ALL_TESTED_THREADS_NUMBER];
    static EventDebuggee017_StarterThread[] starterThreads = 
        new EventDebuggee017_StarterThread[ALL_TESTED_THREADS_NUMBER];

    static EventDebuggee017_Thread[] checkedThreads = new EventDebuggee017_Thread[CHECKED_THREADS_NUMBER];

    private void createTestedThreads() {
        logWriter.println("--> EventDebuggee017: Create tested threads...");
        for (int i=0; i < ALL_TESTED_THREADS_NUMBER; i++) {
            if ( eventDebuggee017Threads[i] != null ) {
                while ( eventDebuggee017Threads[i].isAlive() ) {
                    waitMlsecsTime(100);
                }
            }
            eventDebuggee017Threads[i] = new EventDebuggee017_Thread(TESTED_THREAD_NAME_PATTERN+i, i);
            if ( i < CHECKED_THREADS_NUMBER ) {
                checkedThreads[i] = eventDebuggee017Threads[i];
            }
            starterThreads[i] = new EventDebuggee017_StarterThread(STARTER_THREAD_NAME_PATTERN+i, i);
        }
        logWriter.println("--> EventDebuggee017: All tested threads created - " + ALL_TESTED_THREADS_NUMBER);
        
    }

    private void startTestedThreads() {
        logWriter.println("--> EventDebuggee017: Start tested threads...");
        for (int i=0; i < ALL_TESTED_THREADS_NUMBER; i++) {
            threadsToFinish[i] = false;
            starterThreads[i].start();
            while ( ! eventDebuggee017Threads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
            while ( starterThreads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
        }
        logWriter.println("--> EventDebuggee017: Tested threads started!");
    }
        
    private void finishTestedThreads() {
        logWriter.println("--> EventDebuggee017: Finish tested threads...");
        for (int i=0; i < ALL_TESTED_THREADS_NUMBER; i++) {
            threadsToFinish[i] = true;
        }
        logWriter.println("--> EventDebuggee017: Tested threads are finishing...!");
    }
        
    public void run() {
        
    try {
        
        logWriter.println("--> EventDebuggee017: START...");
        for (int i=0; i < ALL_TESTED_THREADS_NUMBER; i++) {
            eventDebuggee017Threads[i] = null;
            if ( i < CHECKED_THREADS_NUMBER ) {
                checkedThreads[i] = null;
            }
            starterThreads[i] = null;
        }

        createTestedThreads();
        
        printlnForDebug("EventDebuggee017: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("EventDebuggee017: After sendThreadSignalAndWait(SIGNAL_READY_01)");

        startTestedThreads();
        finishTestedThreads();

        printlnForDebug("EventDebuggee017: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("EventDebuggee017: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        createTestedThreads();
        
        printlnForDebug("EventDebuggee017: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("EventDebuggee017: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        
        startTestedThreads();
        finishTestedThreads();

        printlnForDebug("EventDebuggee017: sendThreadSignalAndWait(SIGNAL_READY_04)");
        sendThreadSignalAndWait(SIGNAL_READY_04);
        printlnForDebug("EventDebuggee017: After sendThreadSignalAndWait(SIGNAL_READY_04)");

        createTestedThreads();
        
        printlnForDebug("EventDebuggee017: sendThreadSignalAndWait(SIGNAL_READY_05)");
        sendThreadSignalAndWait(SIGNAL_READY_05);
        printlnForDebug("EventDebuggee017: After sendThreadSignalAndWait(SIGNAL_READY_05)");

        startTestedThreads();
        finishTestedThreads();

        printlnForDebug("EventDebuggee017: sendThreadSignalAndWait(SIGNAL_READY_06)");
        sendThreadSignalAndWait(SIGNAL_READY_06);
        printlnForDebug("EventDebuggee017: After sendThreadSignalAndWait(SIGNAL_READY_06)");

    } catch (Throwable thrown) {
        logWriter.println("## EventDebuggee017: FAILURE - Unexpected Exception:");
        printStackTraceToLogWriter(thrown);
        printlnForDebug("EventDebuggee017: sendThreadSignalAndWait(SIGNAL_FAILURE)");
        sendThreadSignalAndWait(SIGNAL_FAILURE);
    }
        logWriter.println("--> EventDebuggee017: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(EventDebuggee017.class);
    }

}

class EventDebuggee017_Thread extends Thread {
    int myNumber;
    public EventDebuggee017_Thread(String name, int number) {
        super(name);
        myNumber = number;
    }
    
    public void run() {
        while ( ! EventDebuggee017.threadsToFinish[myNumber] ) {
            EventDebuggee017.waitMlsecsTime(100);
        }
    }
}

class EventDebuggee017_StarterThread extends Thread {
    
    int threadToStarNumber;
    public EventDebuggee017_StarterThread(String name, int threadNumber) {
        super(name);
        threadToStarNumber = threadNumber;
    }

    public void run() {
        EventDebuggee017.eventDebuggee017Threads[threadToStarNumber].start();
    }
}


