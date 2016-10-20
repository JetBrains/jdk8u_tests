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
 * Created on 21.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD009;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ThreadDebuggee009 extends StressDebuggee {
    public static final String THREAD_NAME = "ThreadDebuggee009_Thread"; 
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke"; 
    public static final int ARRAY_SIZE_FOR_MEMORY_STRESS = THREAD009_ARRAY_SIZE_FOR_MEMORY_STRESS; 

    static ThreadDebuggee009 threadDebuggee009This;

    static ThreadDebuggee009_Thread threadDebuggee009Thread = null;
    
    static int methodToInvokeCallNumber = 0;
    
    int methodToInvoke (long timeMlsecsToRun, int valueToReturn) {
        methodToInvokeCallNumber++;
        try {
            Thread.sleep(timeMlsecsToRun);
        } catch (Throwable thrown ) {
            // ignore
        }
        return valueToReturn;    
    }

    public void run() {
        logWriter.println("--> ThreadDebuggee009: START...");
        threadDebuggee009This = this;

        logWriter.println("--> ThreadDebuggee009: Create and start thread for invoke method...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee009: freeMemory (bytes) BEFORE creating thread = " + freeMemory);

        threadDebuggee009Thread= new ThreadDebuggee009_Thread(THREAD_NAME);
        threadDebuggee009Thread.start();
        
        while ( suspendedThreadsNumber != 1 ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ThreadDebuggee009: Created thread is started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee009: freeMemory (bytes) AFTER creating and starting thread = " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee009: sendThreadSignalAndWait(SIGNAL_READY_01)");
        sendThreadSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ThreadDebuggee009: After sendThreadSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> ThreadDebuggee009: Creating memory stress until OutOfMemory");
        createMemoryStress(1000000, ARRAY_SIZE_FOR_MEMORY_STRESS);

        printlnForDebug("ThreadDebuggee009: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("ThreadDebuggee009: After sendThreadSignalAndWait(SIGNAL_READY_02");
        
        logWriter.println
        ("--> ThreadDebuggee009: wait for thread to finish..."); 
        while ( threadDebuggee009Thread.isAlive() ) {
            waitMlsecsTime(100);
        }

        logWriter.println
        ("--> ThreadDebuggee009: Thread finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee009: freeMemory (bytes) AFTER thread finished= " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee009: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("ThreadDebuggee009: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> ThreadDebuggee009: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ThreadDebuggee009.class);
    }

}

class ThreadDebuggee009_Thread extends Thread {
    public ThreadDebuggee009_Thread(String name) {
        super(name);
    }

    public void run() {
        ThreadDebuggee009 parent = ThreadDebuggee009.threadDebuggee009This;
        parent.suspendThreadByEvent();
    }
}


