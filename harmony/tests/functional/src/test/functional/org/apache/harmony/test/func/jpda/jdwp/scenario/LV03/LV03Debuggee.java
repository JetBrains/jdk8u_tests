/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.jpda.jdwp.scenario.LV03;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQALogWriter;
import org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee;

/**
 * Created on 19.04.2005 
 */
public class LV03Debuggee extends QARawDebuggee {
    int int_value;
    long long_value;
    String string_value;
    char[] array_value = {'t', 'e', 's', 't'};
    public static final int constant_value = 777;   
    
    Object threadWaitObject = new Object();
    
    public LV03Debuggee() {
        Thread mainThread = Thread.currentThread();
        mainThread.setName("MainThreadToCheck");
    }
   
    /* (non-Javadoc)
     * @see jpda.share.Debuggee#run()
     */
    public void run() {        
        int local_int_x = 8;
        long local_long_y = 88888888L; 
        int_value = 9;
        long_value = 999999L;
        string_value = "Hello, World!";
        
        WorkerThread thread = new WorkerThread("worker", threadWaitObject, logWriter);
        logWriter.print("Worker thread is started");        
        thread.start();
            
        synchronized(threadWaitObject) {
            try {
                //wait for thread to achieve required point
                threadWaitObject.wait();
            } catch (InterruptedException ex) {
                logWriter.println("Excepiton: during main thread waiting");
            }
            //release thread
            logWriter.println("Notify thread");
            threadWaitObject.notify();
        }
        
        if (thread.isAlive()) {
            try {
                thread.join(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logWriter.println("Debuggee has Finished");
    }    
            
    public static void main(String[] args) {
        int i = 5;
        runDebuggee(LV03Debuggee.class);
    }
    
class WorkerThread extends Thread {
        
        Object threadWaitObject;
        JDWPQALogWriter logWriter;
        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public WorkerThread(String name, Object threadWaitObject, JDWPQALogWriter logWriter) {
            super(name);
            this.threadWaitObject = threadWaitObject;
            this.logWriter = logWriter;
        }
        
        public void run() {
            wrapperMethod();
        }
        
        public void wrapperMethod() {
            long wrapperMethodVar = 35;
            waitMethod();
        }
        
        public void waitMethod() {
            double waitMethodVar = 3.5;
            try {
                logWriter.println("Thread before wait");
                synchronized(threadWaitObject) {
                    //notify main thread
                    threadWaitObject.notify();
                    //wait 
                    threadWaitObject.wait();
                }
                logWriter.println("Thread after wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

