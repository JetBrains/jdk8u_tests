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
 * Created on 19.04.2005 
 * @author Alexei S.Vaskin
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.test.stress.jpda.jdwp.share.debuggee;

public class QADebuggee extends QARawDebuggee {
    int int_value;
    long long_value;
    String string_value;
    char[] array_value = {'t', 'e', 's', 't'};
    public static final int constant_value = 777;   
    static int instance_count = 0;
    static boolean singletone = false;
    
    public QADebuggee() {
        ++instance_count;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
        --instance_count;
        super.finalize();
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
        stepMethod();        
        logWriter.print("Local variable's 'local_int_x' value is: " + local_int_x);
        logWriter.print("Local variable's 'local_long_y' value is: " + local_long_y);
        logWriter.print("Static field's 'instance_count' value is: " + instance_count);
        logWriter.print("Instance field's 'int_value' value is: " + int_value);
        logWriter.print("Instance field's 'long_value' value is: " + long_value);
        logWriter.print("Instance field's 'string_value' value is: " + string_value);
        try {
            runThread();
        } catch (InterruptedException e) {
            logWriter.printError(e);
        }
        for (int iteration = 1; iteration <= 10; iteration++) {
            logWriter.print("Iteration #" + iteration);
            try {
                exceptionMethod1("org.apache.harmony.share.NonExistentClass");
            } catch (ClassNotFoundException e) {
                logWriter.print("Couldn't load class: " + e.getMessage());
            }
        }
        for (int iteration = 1; iteration <= 10; iteration++) {
            loopMethod(iteration);
        }
        int expressionVar = invokeMethod(15);
        logWriter.print("Expression variable 'expressionVar' value is: " + expressionVar);
        boolean isSingletone = QADebuggee.getSingletone();
        logWriter.print("Expression variable 'isSingletone' value is: " + isSingletone);
        wrap_up();
    }    
    
    private void stepMethod() {
        logWriter.print("A method 'stepMethod' has been called");
    }
    
    private void runThread() throws InterruptedException {
        WorkerThread thread = new WorkerThread("worker");
        logWriter.print("Worker thread is started");        
        thread.start();
        Thread.sleep(500);
        if (thread.isAlive()) {
            try {
                thread.join(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }        
    }
    
    
    private void exceptionMethod1(String clazz) throws ClassNotFoundException {
        throw new ClassNotFoundException("A class '" + clazz + "' can't be loaded.");
    }
    
    
//    private void exceptionMethod2(String fileName) {
//        try {
//            FileInputStream fis = new FileInputStream(fileName);
//        } catch (FileNotFoundException e) {
//            logWriter.printError(e);
//        }        
//    }
    
    private int invokeMethod(int arg) {
        return arg * arg;
    }
    
    private void loopMethod(int i) {
        logWriter.print("A loop method has been called for the " + i + " time");
    }
    
    private void wrap_up() {
        logWriter.println("Finished");
    }
    
    public int getInstances() {
        return instance_count;
    }
    
    public static boolean getSingletone() {
        return singletone;
    }

    public static void main(String[] args) {
        int i = 5;
        runDebuggee(QADebuggee.class);
    }
    
    class WorkerThread extends Thread {
        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public WorkerThread(String name) {
            super(name);
        }
        
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

