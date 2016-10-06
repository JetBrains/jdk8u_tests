/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Oleg Oleinik
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.kernel.thread.ExcptHandlerTest;

import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: check that Thread exception handling mechanism works as expected 
 *       when various types of runtime exceptions/errors are thrown.
 *       Actually, not very close to often real-life situation...
 * 
 * The test does:
 *      
 *   1. Creates N_OF_THREADS threads of a dozen of various types. Each thread
 *      typically locks some object and causes runtime exceptions/errors to be thrown.
 *      
 *      Caused exceptions are: StackOverflowError, NullPointerException, 
 *      ArrayIndexOutOfBoundsException, NegativeArraySizeException, ArithmeticException, 
 *      ArrayStoreException, IllegalMonitorStateException, ClassCastException, RuntimeException.
 *         
 *   2. For each Thread either UncaughtExceptionHandler or DefaultUncaughtExceptionHandler
 *      is set. The handlers just wrap and re-throw the caught exception, expecting that
 *      VM ignores the exception.
 *
 *   3. Starts threads and waits for there completion.
 *   
 *   4. Expected result: no crashes, no hangs (including due to that objects are
 *      not unlocked when exceptions are thrown).
 */


public class ExcptHandlerTest extends Test {

    static int N_OF_THREADS = 5;

    static volatile int counter = 0;

    public static void main(String[] args) {
        System.exit(new ExcptHandlerTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
        
        // threads: daemon/non-daemon x handler/default handler
        
        return (test(true, true) && test(false, true) && 
            test(true, false) && test(false, false)) ? pass("OK") : fail("");
    }

    boolean test(boolean daemon, boolean defaultHandler) {
   
        clearInvokedHandlers();
       
        Thread[][] t = new Thread[11][N_OF_THREADS];
       
        ThreadGroup tg = new ThreadGroup("");
        
        int[][] obj1 = {{1, 1}, {1, 1}};
        Object[][] obj2 = {{"a", "b"}, {"c", "d"}};
        Object[][] obj3 = {{"", ""}, {"", ""}};
        
        // First, create threads of various exception types
      
        for (int i = 0; i < t[0].length; ++i){
            t[0][i] = new StackOverflowT(tg);
            t[1][i] = new NullPointerT(tg);
            t[2][i] = new IndexOutOfBoundsT(tg);
            t[3][i] = new NegativeArraySizeT(tg);
            t[4][i] = new ArithmeticT(tg);
            t[5][i] = new ArrayStoreT(tg);
            t[6][i] = new IllegalStateT1(tg, obj1);
            t[7][i] = new IllegalStateT2(tg, obj2);
            t[8][i] = new IllegalStateT3(tg, obj3);
            t[9][i] = new RuntimeExcptT(tg);
            t[10][i] = new ClassCastT(tg);
        }
     
        MyUncoughtExceptionHandler h = new MyUncoughtExceptionHandler();

        // Then, set handlers and start threads
        
        for (int i = 0; i < t.length; ++i){
            for (int j = 0; j < t[i].length; ++j) {
                t[i][j].setDaemon(daemon);
                if (defaultHandler){
                    t[i][j].setDefaultUncaughtExceptionHandler(h);
                } else {
                    t[i][j].setUncaughtExceptionHandler(h);
                }
                t[i][j].start();
            }
        }
        
        // Then, wait for threads completion

        for (int i = 0; i < t.length; ++i){
            for (int j = 0; j < t[i].length; ++j) {
                try {
                    t[i][j].join();
                    // System.out.println("Joined thread " + t[i][j] + " / " + j);
                } catch (InterruptedException ie) {
                    // System.out.println("InteruptedException while joining all started threads");
                }
            }
        }
        
        // Finally, check that expected number of uncaught exception 
        // handlers was called
        
        int invoked_handlers = getInvokedHandlers();
       
        if (invoked_handlers != t.length * t[0].length) {
            log.add("" + invoked_handlers + " exception handlers were invoked, while " +
                t.length * t[0].length + " expected");
            return false;
        }
       
        return true;
    }
  
    public void parseParams(String[] params){
        if (params.length >= 1) {
            N_OF_THREADS = Integer.parseInt(params[0]);
        }
    }
    
    static synchronized int getInvokedHandlers(){
        return counter;
    }

    static synchronized void incInvokedHandlers(){
        ++counter;
    }
   
    static synchronized void clearInvokedHandlers(){
        counter = 0;
    }

}


    // A thread which causes StackOverflowError to be thrown through
    // recursive method invocation.

class StackOverflowT extends Thread {

    Object[] o = new Object[10];
  
    StackOverflowT(ThreadGroup parent){
        super(parent, "StackOverflowT");
    }

    public void run() {
        m(0);
    }
   
    void m(int depth) {
        if (depth == 0) {
            // lets see what happens if we lock an object,
            // no specific checks whether or not the object is unlocked
            // when exception is generated (if not threads try to lock
            // a single object in other cases below)
            synchronized (o){ 
                m(depth + 1);
            }
        } else {
            m(depth + 1);
        }
    }
}

    // A thread which causes NullPinterException to be thrown

class NullPointerT extends Thread {

    static int[] arr = new int[10];
   
    Object f = null;
    String s = null;

    NullPointerT(ThreadGroup parent){
        super(parent, "NullPointerT");
    }

    public void run() {
        synchronized(arr){ 
            s = getF().toString();
        }
    }
    
    Object getF() {
        Object[] o = {new Object(), null}; 
        for (int i = 0; i < o.length; ++i) {
            f = o[i];
        }
        return f;
    }
}

    //A thread which causes ArrayIndexOutOfBoundsException to be thrown

class IndexOutOfBoundsT extends Thread {

    Object f = null;
   
    static Object obj = new Object();

    IndexOutOfBoundsT(ThreadGroup parent){
        super(parent, "IndexOutOfBoundsT");
    }

    public void run() {
        synchronized(obj){ 
            Object[] o = {new Object(), null}; 
            for (int i = 0; i < 3; ++i) {
                f = o[i];
            }
        }
    }
}

    // A thread which causes NegativeArraySizeException to be thrown

class NegativeArraySizeT extends Thread {

    int i = -1;

    int[] j = null; 
    
    NegativeArraySizeT(ThreadGroup parent){
        super(parent, "NegativeArraySizeT");
    }

    NegativeArraySizeT (int i) {
        this.i = i;
    }
    
    public void run() {
        synchronized(this){ 
            j = new int[i];
        }
    }
}

    //A thread which causes ArithmeticException to be thrown

class ArithmeticT extends Thread {

    int i = 0, k = 10;

    boolean[] j = {true, false};
        
    ArithmeticT(int i, int k) {
        this.i = i;
        this.k = k;
    }
    
    ArithmeticT(ThreadGroup parent){
        super(parent, "ArithmeticT");
    }
    
    public void run() {
        synchronized(j){
            j = new boolean[k/i];
        }
    }
}

    //A thread which causes ArrayStoreException to be thrown

class ArrayStoreT extends Thread {

    static Object[] obj = new String[2];
    
    ArrayStoreT(ThreadGroup parent){
        super(parent, "ArrayStoreT");
    }

    public void run() {
        synchronized (obj){
            obj[1] = this;
        }
    }
}

    // A thread which causes IllegalMonitorStateException to be thrown
    // when wait() is called for different object than was locked

class IllegalStateT1 extends Thread {

    int[][] obj = {{1, 1}, {1, 1}};
    
    IllegalStateT1(ThreadGroup parent, int[][] obj){
        super(parent, "IllegalStateT1");
        this.obj = obj;
    }

    public void run() {
        synchronized(obj[0]) {
            try {
                obj.wait();
            } catch (InterruptedException ie){
            }
        }
    }
}

    //A thread which causes IllegalMonitorStateException to be thrown
    //when notify() is called for different object than was locked

class IllegalStateT2 extends Thread {

    static Object[][] obj = {{"", ""}, {"", ""}};
    
    IllegalStateT2(ThreadGroup parent, Object[][] obj){
        super(parent, "IllegalStateT2");
        this.obj = obj;
    }

    public void run() {
        synchronized(obj[0][1]){
            obj.notify();
        }
    }
}

    //A thread which causes IllegalMonitorStateException to be thrown
    //when notifyAll() is called for different object than was locked

class IllegalStateT3 extends Thread {

    Object[][] obj = {{"", ""}, {"", ""}};
    
    IllegalStateT3(ThreadGroup parent, Object[][] obj){
        super(parent, "IllegalStateT3");
        this.obj = obj;
    }

    public void run() {
        synchronized(obj) {
            obj[0][0].notifyAll();
        }
    }
}

    //A thread which explicitely throws instance of RuntimeException

class RuntimeExcptT extends Thread {

    RuntimeExcptT(ThreadGroup parent){
        super(parent, "RuntimeExcptT");
    }
   
    public void run() {
        throw new RuntimeException("");
    }
}

    //A thread which causes ClassCastException to be thrown

class ClassCastT extends Thread {

    Object obj = new String("");

    ClassCastT(ThreadGroup parent){
        super(parent, "ClassCastT");
    }
   
    public void run() {
        ((ClassCastT)getObj()).obj = new String("abc"); 
    }
    
    Object getObj() {
        return this.obj;
    }
}

    // Exception handler - does nothing but wraps handled exception and re-throws
    // expecting that VM will just ignore it

class MyUncoughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    public void uncaughtException(Thread t, Throwable e) {
        // System.out.println("Thread " + t.getName() + ", exception " + e);
        ExcptHandlerTest.incInvokedHandlers();
        throw new RuntimeException(e);
    }
}

