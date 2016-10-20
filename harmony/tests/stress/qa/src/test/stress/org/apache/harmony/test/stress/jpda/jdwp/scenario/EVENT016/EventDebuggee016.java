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
 * Created on 22.08.2006 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT016;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class EventDebuggee016 extends StressDebuggee {
    public static final String TESTED_THREAD_NAME_PATTERN = "EventDebuggee016_Thread_"; 
    public static final int THE_SAME_KIND_TESTED_THREADS_NUMBER = 2; 
    public static final int ALL_TESTED_THREADS_NUMBER = THE_SAME_KIND_TESTED_THREADS_NUMBER * 3; 
    public static final int CHECKED_THREADS_NUMBER = THE_SAME_KIND_TESTED_THREADS_NUMBER * 2; 
    public static final String STARTER_THREAD_NAME_PATTERN = "EventDebuggee016_StarterThread_"; 

    static EventDebuggee016 eventDebuggee016This;

    static volatile boolean[] threadsToFinish = new boolean[ALL_TESTED_THREADS_NUMBER];

    static EventDebuggee016_Thread[] eventDebuggee016Threads = 
        new EventDebuggee016_Thread[ALL_TESTED_THREADS_NUMBER];
    static EventDebuggee016_StarterThread[] starterThreads = 
        new EventDebuggee016_StarterThread[ALL_TESTED_THREADS_NUMBER];

    static EventDebuggee016_Thread[] checkedThreads = new EventDebuggee016_Thread[CHECKED_THREADS_NUMBER];

    private void createTestedThreads() {
        logWriter.println("--> EventDebuggee016: Create tested threads...");
        for (int i=0; i < ALL_TESTED_THREADS_NUMBER; i++) {
            eventDebuggee016Threads[i] = new EventDebuggee016_Thread(TESTED_THREAD_NAME_PATTERN+i, i);
            if ( i < CHECKED_THREADS_NUMBER ) {
                checkedThreads[i] = eventDebuggee016Threads[i];
            }
            starterThreads[i] = new EventDebuggee016_StarterThread(STARTER_THREAD_NAME_PATTERN+i, i);
        }
        logWriter.println("--> EventDebuggee016: All tested threads created - " + ALL_TESTED_THREADS_NUMBER);
        
    }

    private void startTestedThreads() {
        logWriter.println("--> EventDebuggee016: Start tested threads...");
        for (int i=0; i < ALL_TESTED_THREADS_NUMBER; i++) {
            threadsToFinish[i] = false;
            starterThreads[i].start();
            while ( ! eventDebuggee016Threads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
        }
        logWriter.println("--> EventDebuggee016: Tested threads started!");
    }
        
    private void finishTestedThreads() {
        logWriter.println("--> EventDebuggee016: Finish tested threads...");
        for (int i=0; i < ALL_TESTED_THREADS_NUMBER; i++) {
            threadsToFinish[i] = true;
            while ( eventDebuggee016Threads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
            while ( starterThreads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
        }
        logWriter.println("--> EventDebuggee016: Tested threads finished!");
    }
        
    public void run() {
        
    try {
        
        logWriter.println("--> EventDebuggee016: START...");
        eventDebuggee016This = this;

        createTestedThreads();
        
        printlnForDebug("EventDebuggee016: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("EventDebuggee016: After sendThreadSignalAndWait(SIGNAL_READY_01)");

        startTestedThreads();

        printlnForDebug("EventDebuggee016: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("EventDebuggee016: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        finishTestedThreads();
        createTestedThreads();
        
        printlnForDebug("EventDebuggee016: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("EventDebuggee016: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        
        startTestedThreads();

        printlnForDebug("EventDebuggee016: sendThreadSignalAndWait(SIGNAL_READY_04)");
        sendThreadSignalAndWait(SIGNAL_READY_04);
        printlnForDebug("EventDebuggee016: After sendThreadSignalAndWait(SIGNAL_READY_04)");

        finishTestedThreads();
        createTestedThreads();
        
        printlnForDebug("EventDebuggee016: sendThreadSignalAndWait(SIGNAL_READY_05)");
        sendThreadSignalAndWait(SIGNAL_READY_05);
        printlnForDebug("EventDebuggee016: After sendThreadSignalAndWait(SIGNAL_READY_05)");

        startTestedThreads();
        finishTestedThreads();

        printlnForDebug("EventDebuggee016: sendThreadSignalAndWait(SIGNAL_READY_06)");
        sendThreadSignalAndWait(SIGNAL_READY_06);
        printlnForDebug("EventDebuggee016: After sendThreadSignalAndWait(SIGNAL_READY_06)");

    } catch (Throwable thrown) {
        logWriter.println("## EventDebuggee016: FAILURE - Unexpected Exception:");
        printStackTraceToLogWriter(thrown);
        printlnForDebug("EventDebuggee016: sendThreadSignalAndWait(SIGNAL_FAILURE)");
        sendThreadSignalAndWait(SIGNAL_FAILURE);
    }
        logWriter.println("--> EventDebuggee016: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(EventDebuggee016.class);
    }

}

class EventDebuggee016_Thread extends Thread {
    int myNumber;
    public EventDebuggee016_Thread(String name, int number) {
        super(name);
        myNumber = number;
    }
    
    public void run() {
        while ( ! EventDebuggee016.threadsToFinish[myNumber] ) {
            EventDebuggee016.waitMlsecsTime(100);
        }
    }
}

class EventDebuggee016_StarterThread extends Thread {
    
    int threadToStarNumber;
    public EventDebuggee016_StarterThread(String name, int threadNumber) {
        super(name);
        threadToStarNumber = threadNumber;
    }

    public void run() {
        EventDebuggee016.eventDebuggee016Threads[threadToStarNumber].start();
    }
}


