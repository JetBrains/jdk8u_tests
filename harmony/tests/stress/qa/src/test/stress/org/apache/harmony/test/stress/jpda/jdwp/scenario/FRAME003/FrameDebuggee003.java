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
 * Created on 03.11.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.FRAME003;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class FrameDebuggee003 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = FRAME003_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = FRAME003_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "FrameDebuggee003_Thread_"; 
    public static int RECURSION_NUMBER = FRAME003_RECURSION_NUMBER;
    public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = FRAME003_ARRAY_SIZE_FOR_MEMORY_STRESS; 
    
    static FrameDebuggee003 frameDebuggee003This;

    private FrameDebuggee003_Thread[] startedThreads = 
        new FrameDebuggee003_Thread[THREAD_NUMBER_LIMIT];
    
    static int startedThreadsNumber = 0;
    static int threadsNumberToResume = 0;
    static volatile boolean threadsToContinue = false;

    public void run() {
        
        logWriter.println("--> FrameDebuggee003: START...");
        frameDebuggee003This = this;
        
        logWriter.println("--> FrameDebuggee003: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();

        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> FrameDebuggee003: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                startedThreads[i]= new FrameDebuggee003_Thread(i);
                startedThreads[i].start();
                startedThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> FrameDebuggee003: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> FrameDebuggee003: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> FrameDebuggee003: Started threads number = " + startedThreadsNumber);
        threadsNumberToResume = startedThreadsNumber/2;
        logWriter.println
        ("--> FrameDebuggee003: threads number to resume = " + threadsNumberToResume);
        
        while ( suspendedThreadsNumber != startedThreadsNumber ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> FrameDebuggee003: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> FrameDebuggee003: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("FrameDebuggee003: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("FrameDebuggee003: After sendThreadSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> FrameDebuggee003: Creating memory stress until OutOfMemory");
        createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);

        printlnForDebug("FrameDebuggee003: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("FrameDebuggee003: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        logWriter.println
        ("--> FrameDebuggee003: Send signal to resumed by debugger threads to continue...");
        threadsToContinue = true;

        logWriter.println
        ("--> FrameDebuggee003: Wait for all started threads to finish...");

        for (int i=0; i < startedThreadsNumber; i++) {
            while ( startedThreads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
        }
        logWriter.println
        ("--> FrameDebuggee003: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> FrameDebuggee003: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("FrameDebuggee003: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("FrameDebuggee003: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> FrameDebuggee003: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(FrameDebuggee003.class);
    }
}

class FrameDebuggee003_Thread extends Thread {
    
    int myNumber;
    static FrameDebuggee003 parent = null;
    
    public FrameDebuggee003_Thread(int myNumber) {
        super(FrameDebuggee003.THREAD_NAME_PATTERN + myNumber);
        this.myNumber = myNumber;
    }
    
    public void run() {
        parent = FrameDebuggee003.frameDebuggee003This;
        recursiveMethod(FrameDebuggee003.RECURSION_NUMBER);
    }
    
    static void staticThreadMethod(boolean firstCall, FrameDebuggee003_Thread thisThread) {
        int dummyIntVar = 1;   
        long dummyLongVar = 1;
        if ( firstCall ) {
            staticThreadMethod(false, thisThread);
        } else {
            parent.suspendThreadByEvent();
            if ( thisThread.myNumber < FrameDebuggee003.threadsNumberToResume ) {
                while ( ! FrameDebuggee003.threadsToContinue ) {
                    FrameDebuggee003.waitMlsecsTime(100);
                }
                thisThread.suspendResumedThreads(2);
            }
        }
    }
    
    void suspendResumedThreads(int recursionsNumber) {
        int dummyIntVar = 1;   
        long dummyLongVar = 1;
        if (recursionsNumber > 0) {
            suspendResumedThreads(recursionsNumber-1);
        } else {
            parent.suspendThreadByEvent();
        }
    }

    void recursiveMethod(int recursionsNumber) {
        int dummyIntVar = 1;   
        long dummyLongVar = 1;   
        if (recursionsNumber > 0) {
            recursiveMethod(recursionsNumber-1);
        } else {
            staticThreadMethod(true, this);
        }
    }
}


