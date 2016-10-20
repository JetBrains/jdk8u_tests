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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT002;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ObjectDebuggee002 extends StressDebuggee {
    
    public static final String THREAD_NAME_PATTERN = "ObjectDebuggee002_Thread_"; 

    static ObjectDebuggee002 objectDebuggee002This;
    static volatile int startedThreadsNumber = 0;
    
    static ObjectDebuggee002_Thread[] objectDebuggee002Threads = null;

    public void run() {
        logWriter.println("--> ObjectDebuggee002: START...");
        objectDebuggee002This = this;

        printlnForDebug("ObjectDebuggee002: sendSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ObjectDebuggee002: After sendSignalAndWait(SIGNAL_READY_01)");
        
        logWriter.println("--> ObjectDebuggee002: Create and start big number of threads...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee002: freeMemory (bytes) BEFORE creating threads = " + freeMemory);

        String signalToSend = SIGNAL_READY_02;
        try {
            objectDebuggee002Threads = new ObjectDebuggee002_Thread[OBJECT002_THREAD_NUMBER_LIMIT]; 
            for (int i=0; i < OBJECT002_THREAD_NUMBER_LIMIT; i++) {
                String threadName = THREAD_NAME_PATTERN + i;
                objectDebuggee002Threads[i] = new ObjectDebuggee002_Thread(threadName);
                synchronized (objectDebuggee002Threads[i]) {
                    objectDebuggee002Threads[i].start();
                    try {
                        objectDebuggee002Threads[i].wait();
                    } catch (Exception thrown) {
                        logWriter.println("##ObjectDebuggee002 - FAILURE: Unexpected Exception in objectDebuggee002Threads[" +
                                i + "].wait(): " + thrown);
                        logWriter.println("##ObjectDebuggee002 - Started thread name = '" + threadName + "'");
                        signalToSend = SIGNAL_FAILURE;
                        break;
                    }
                }
                startedThreadsNumber++;
                freeMemory = currentRuntime.freeMemory();
                if ( freeMemory < OBJECT002_FREE_MEMORY_LIMIT ) {
                    logWriter.println
                    ("--> ObjectDebuggee002: FREE_MEMORY_LIMIT (" + 
                            OBJECT002_FREE_MEMORY_LIMIT + ") is reached!");
                    break;   
                }
            }
        } catch ( Throwable thrown) {
            logWriter.println
            ("--> ObjectDebuggee002: Exception while creating threads: " + thrown);
        }
        logWriter.println("--> ObjectDebuggee002: All created threads are started! Threads number = " +
                startedThreadsNumber);
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee002: freeMemory (bytes) AFTER creating and starting threads = " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee002: sendSignalAndWait(" + signalToSend + ")");
        sendThreadSignalAndWait(signalToSend);
        printlnForDebug("ObjectDebuggee002: After sendSignalAndWait(" + signalToSend + ")");

        logWriter.println("--> ObjectDebuggee002: Send signals to all threads to finish and wait for finish...");
        for (int i=0; i < startedThreadsNumber; i++) {
            synchronized (objectDebuggee002Threads[i]) {
                objectDebuggee002Threads[i].notify();
            }
            while ( objectDebuggee002Threads[i].isAlive() ) {
                waitMlsecsTime(100);
            }
        }
        logWriter.println("--> ObjectDebuggee002: All threads finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ObjectDebuggee002: freeMemory (bytes) AFTER all threads finished= " + 
                freeMemory);

        printlnForDebug("ObjectDebuggee002: sendSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("ObjectDebuggee002: After sendSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> ObjectDebuggee002: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ObjectDebuggee002.class);
    }

}

class ObjectDebuggee002_Thread extends Thread {
    long[] longArray = null;
    String threadName = null;

    public ObjectDebuggee002_Thread(String name) {
        super(name);
        threadName = name;
        longArray = new long[1000];
    }

    public void run() {
        ObjectDebuggee002 parent = ObjectDebuggee002.objectDebuggee002This;
        synchronized (this) {
            this.notifyAll();
            try {
                this.wait();
            } catch (Exception thrown) {
                parent.logWriter.println("## ObjectDebuggee002 - WARNING: Unexpected Exception in thread '" +
                        threadName + "' : " + thrown); 
                parent.logWriter.println("## ObjectDebuggee002: Ignore Exception!"); 
            }
        }
    }
}


