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
 * Created on 04.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD001;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class ThreadDebuggee001 extends StressDebuggee {
    public static final String THREAD_NAME = "ThreadDebuggee001_Thread"; 
    public static final String METHOD_TO_INVOKE_NAME = "methodToInvoke"; 

    static ThreadDebuggee001 threadDebuggee001This;

    static ThreadDebuggee001_Thread threadDebuggee001Thread = null;
    
    static int methodToInvokeCallNumber = 0;
    
    static int methodToInvoke (long timeMlsecsToRun, int valueToReturn) {
        methodToInvokeCallNumber++;
        try {
            Thread.sleep(timeMlsecsToRun);
        } catch (Throwable thrown ) {
            // ignore
        }
        return valueToReturn;    
    }

    public void run() {
        logWriter.println("--> ThreadDebuggee001: START...");
        threadDebuggee001This = this;

        logWriter.println("--> ThreadDebuggee001: Create and start thread for invoke method...");
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee001: freeMemory (bytes) BEFORE creating thread = " + freeMemory);

        threadDebuggee001Thread= new ThreadDebuggee001_Thread(THREAD_NAME);
        threadDebuggee001Thread.start();
        
        while ( suspendedThreadsNumber != 1 ) {
            waitMlsecsTime(100);
        }
        logWriter.println
        ("--> ThreadDebuggee001: Created thread is started!");
        
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee001: freeMemory (bytes) AFTER creating and starting thread = " + 
                freeMemory);

        logWriter.println
        ("--> ThreadDebuggee001: wait for thread to finish..."); 
        while ( threadDebuggee001Thread.isAlive() ) {
            waitMlsecsTime(100);
        }

        logWriter.println
        ("--> ThreadDebuggee001: Thread finished!");

        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> ThreadDebuggee001: freeMemory (bytes) AFTER thread finished= " + 
                freeMemory);

        printlnForDebug("ThreadDebuggee001: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("ThreadDebuggee001: After sendSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> ThreadDebuggee001: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(ThreadDebuggee001.class);
    }

}

class ThreadDebuggee001_Thread extends Thread {
    public ThreadDebuggee001_Thread(String name) {
        super(name);
    }

    public void run() {
        ThreadDebuggee001 parent = ThreadDebuggee001.threadDebuggee001This;
        parent.suspendThreadByEvent();
    }
}


