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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD007;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ThreadDebuggee007 extends StressDebuggee {
    public static final String THREAD_NAME = "ThreadDebuggee007_Thread"; 
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke"; 

    static ThreadDebuggee007 threadDebuggee007This;

    static ThreadDebuggee007_Thread threadDebuggee007Thread = null;
    
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
        logWriter.println("--> ThreadDebuggee007: START...");
        threadDebuggee007This = this;

        logWriter.println("--> ThreadDebuggee007: Create and start thread for invoke method...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee007: freeMemory (bytes) BEFORE creating thread = " + freeMemory);

        threadDebuggee007Thread= new ThreadDebuggee007_Thread(THREAD_NAME);
        threadDebuggee007Thread.start();
        
        while ( suspendedThreadsNumber != 1 ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ThreadDebuggee007: Created thread is started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee007: freeMemory (bytes) AFTER creating and starting thread = " + 
                freeMemory);

        logWriter.println
        ("--> ThreadDebuggee007: wait for thread to finish..."); 
        while ( threadDebuggee007Thread.isAlive() ) {
            waitMlsecsTime(100);
        }

        logWriter.println
        ("--> ThreadDebuggee007: Thread finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee007: freeMemory (bytes) AFTER thread finished= " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee007: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ThreadDebuggee007: After sendSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> ThreadDebuggee007: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ThreadDebuggee007.class);
    }

}

class ThreadDebuggee007_Thread extends Thread {
    public ThreadDebuggee007_Thread(String name) {
        super(name);
    }

    public void run() {
        ThreadDebuggee007 parent = ThreadDebuggee007.threadDebuggee007This;
        parent.suspendThreadByEvent();
    }
}


