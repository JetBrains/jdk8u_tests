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
package org.apache.harmony.test.func.jpda.jdwp.scenario.TS03;

import org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee;

/**
 * Created on 19.04.2005 
 */
public class TS03Debuggee extends QARawDebuggee {
    /* 
     * RELATIVE_LINE_NUMBER_FOR_BREAKPOINT constant
     * must correspond to the line number with 
     * 'logWriter.println("TS03Debuggee: Notify thread")' call
     * inside 'ts03DebuggeeRunThread' method. 
     */
    public static final int RELATIVE_LINE_NUMBER_FOR_BREAKPOINT = 17;
    
    Object synchObject = new Object();
    
    public TS03Debuggee() {
        Thread mainThread = Thread.currentThread();
        mainThread.setName("MainThreadToCheck");
    }
   
    private void ts03DebuggeeRunThread() {
        WorkerThread thread = new WorkerThread("worker", synchObject);
        synchronized(synchObject) {
            thread.start();
            try {
                synchObject.wait();
            } catch (Throwable thrown ) {
                // ignore
            }
        }
        logWriter.print("TS03Debuggee: Worker thread is started");        
        /* 
         * The relative line (inside 'ts03DebuggeeRunThread' method)
         * number with 'logWriter.println("TS03Debuggee: Notify thread")'
         *  call must correspond to
         * RELATIVE_LINE_NUMBER_FOR_BREAKPOINT constant.
         */
        logWriter.println("TS03Debuggee: Notify thread");
        synchronized(synchObject) {
            synchObject.notify();
        }
        
        if (thread.isAlive()) {
            try {
                thread.join(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see jpda.share.Debuggee#run()
     */
    public void run() {        
        logWriter.println("TS03Debuggee: Start...");
        
        ts03DebuggeeDummyMethod();

        logWriter.println("TS03Debuggee: Finish...");
    }
    
    private void ts03DebuggeeDummyMethod() {
        ts03DebuggeeRunThread();
    }
            
    public static void main(String[] args) {
        int i = 5;
        runDebuggee(TS03Debuggee.class);
    }
    
    class WorkerThread extends Thread {
        
        Object synchObject;

        public WorkerThread(String name, Object synchObject) {
            super(name);
            this.synchObject = synchObject;
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
                synchronized(synchObject) {
                    synchObject.notify();
                    synchObject.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

