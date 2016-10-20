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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT003;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ObjectDebuggee003 extends StressDebuggee {
   
    public static final String THREAD_NAME_PATTERN = "ObjectDebuggee003_Thread_"; 

    static ObjectDebuggee003 objectDebuggee003This;

    static volatile boolean allThreadsToFinish = false;
    static int createdThreadsNumber = 0;
    static volatile int startedThreadsNumber = 0;

    static ObjectDebuggee003_Thread[] objectDebuggee003Threads = null;

    public void run() {
        logWriter.println("--> ObjectDebuggee003: START...");
        objectDebuggee003This = this;

        printlnForDebug("ObjectDebuggee003: sendSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ObjectDebuggee003: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> ObjectDebuggee003: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee003: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        try {
            objectDebuggee003Threads = new ObjectDebuggee003_Thread[OBJECT003_THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < OBJECT003_THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                objectDebuggee003Threads[i]= new ObjectDebuggee003_Thread(threadName);
                objectDebuggee003Threads[i].start();
                createdThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < OBJECT003_FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> ObjectDebuggee003: FREE_MEMORY_LIMIT (" + 
                            OBJECT003_FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> ObjectDebuggee003: Exception while creating threads: " + thrown);
        }
        logWriter.println
        ("--> ObjectDebuggee003: Created threads number = " + createdThreadsNumber);
        
        while ( startedThreadsNumber != createdThreadsNumber ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ObjectDebuggee003: All created threads are started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee003: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee003: sendSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ObjectDebuggee003: After sendSignalAndWait(SIGNAL_READY_02)");

        logWriter.println
        ("--> ObjectDebuggee003: Send signal to all threads and wait their to finish...");
        allThreadsToFinish = true;
        
        for (int i=0; i < createdThreadsNumber; i++) {
            while ( objectDebuggee003Threads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
        }
        logWriter.println
        ("--> ObjectDebuggee003: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee003: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee003: sendSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("ObjectDebuggee003: After sendSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> ObjectDebuggee003: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ObjectDebuggee003.class);
    }

}

class ObjectDebuggee003_Thread extends Thread {
    long[] longArray = null;
    public ObjectDebuggee003_Thread(String name) {
        super(name);
        longArray = new long[1000];
    }

    public void run() {
        ObjectDebuggee003 parent = ObjectDebuggee003.objectDebuggee003This;
        synchronized (parent) { 
            ObjectDebuggee003.startedThreadsNumber++;
        }
        while ( ! ObjectDebuggee003.allThreadsToFinish ) {
            ObjectDebuggee003.waitMlsecsTime(100, this);
        }
    }
}


