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
 * @author Aleksander V. Budniy
 * @version $Revision: 1.2 $
 */

/**
 * Created on 16.09.2006 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT001;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ObjectDebuggee001 extends StressDebuggee {
    public static final long FREE_MEMORY_LIMIT = OBJECT001_FREE_MEMORY_LIMIT; 
    public static final int THREAD_NUMBER_LIMIT = OBJECT001_THREAD_NUMBER_LIMIT; 
    public static final String THREAD_NAME_PATTERN = "ObjectDebuggee001_Thread_"; 

    static ObjectDebuggee001 objectDebuggee001This;

    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    static ObjectDebuggee001_Thread[] objectDebuggee001Threads = null;

    public void run() {
        
        logWriter.println("--> ObjectDebuggee001: START...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee001: freeMemory at the beginning = " + freeMemory);

        
        objectDebuggee001This = this;

        printlnForDebug("ObjectDebuggee001: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ObjectDebuggee001: After sendThreadSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> ObjectDebuggee001: Create and start big number of threads...");
        currentRuntime = Runtime.getRuntime();
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee001: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            objectDebuggee001Threads = new ObjectDebuggee001_Thread[THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                objectDebuggee001Threads[i]= new ObjectDebuggee001_Thread(threadName);
                objectDebuggee001Threads[i].start();
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> ObjectDebuggee001: FREE_MEMORY_LIMIT (" + 
                            FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> ObjectDebuggee001: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> ObjectDebuggee001: Created threads number = " + createdThreadsNumber);
        
        while ( startedThreadsNumber != createdThreadsNumber ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ObjectDebuggee001: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee001: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee001: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ObjectDebuggee001: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        logWriter.println
        ("--> ObjectDebuggee001: Send signal to all threads and wait for threads to finish...");
        allThreadsToFinish = true;
        
        for (int i=0; i < createdThreadsNumber; i++) {
            while ( objectDebuggee001Threads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
        }
        logWriter.println
        ("--> ObjectDebuggee001: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee001: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee001: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("ObjectDebuggee001: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> ObjectDebuggee001: FINISH...");
        System.exit(0);

    }

    public static void main(String [] args) {
        runDebuggee(ObjectDebuggee001.class);
    }

}

class ObjectDebuggee001_Thread extends Thread {
    long[] longArray = null;
    public ObjectDebuggee001_Thread(String name) {
        super(name);
        longArray = new long[1000];
    }

    public void run() {
        ObjectDebuggee001 parent = ObjectDebuggee001.objectDebuggee001This;
        synchronized (parent) { 
            ObjectDebuggee001.startedThreadsNumber++;
        }
        while ( ! ObjectDebuggee001.allThreadsToFinish ) {
            ObjectDebuggee001.waitMlsecsTime(100, this);
        }
    }
}


