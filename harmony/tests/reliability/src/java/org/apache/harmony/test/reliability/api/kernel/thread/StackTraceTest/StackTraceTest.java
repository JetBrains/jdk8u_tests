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

package org.apache.harmony.test.reliability.api.kernel.thread.StackTraceTest;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;

/*
 * Goal: check that Thread's getStackTrace() and getAllStackTraces() do not
 *       cause crashes or hangs or memory leaks.
 *        
 * The test does:
 *      
 *   1. Starts N_OF_THREADS threads of a dozen of various types.
 *      Types of threads differ in which locks they hold and free in waits 
 *      when stackTrace methods are called.
 *         
 *   2. For each started thread calls getStackTrace() (and getAllStackTraces()).
 *      
 *   3. Interrupts all started threads and waits for their completion. 
 *   
 *   4. Expected result: no crashes or hangs.
 */


public class StackTraceTest extends Test {

    static int N_OF_THREADS = 5;

    public static void main(String[] args) {
        System.exit(new StackTraceTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
       
        return (test(true) && test(false)) ? pass("OK") : fail("");
    }

    boolean test(boolean daemon) {
    
        boolean failed = false;
      
        ThreadCounter.clear();
       
        Thread[][] t = new Thread[11][N_OF_THREADS];
    
        Object[] o1 = new Object[10];
        int[] o2 = new int[100];
        
        for (int i = 0; i < t[0].length; ++i){
            t[0][i] = new A();
            t[1][i] = new B();
            t[2][i] = new C();
            t[3][i] = new D();
            t[4][i] = new E();
            t[5][i] = new F();
            t[6][i] = new G();
            t[7][i] = new H();
            // all these I-type threads will lock/wait Object[] object
            t[8][i] = new I(o1); 
            // all these I-type threads will lock/wait int[] object
            t[9][i] = new I(o2);
            t[10][i] = new J();
        }

        for (int i = 0; i < t.length; ++i){
            for (int j = 0; j < t[i].length; ++j) {
                t[i][j].setDaemon(daemon);
                t[i][j].start();
            }
        }

        // No synchronization. We call stack trace methods independently
        // from where the started threads are. The only expectation is 
        // that they are actually running.
       
        while (ThreadCounter.getRunning() < t.length * t[0].length) {
            Thread.yield();
        }
        // System.out.println("All threads are running...");
        
        for (int i = 0; i < t.length; ++i){
        
            for (int j = 0; j < t[i].length; ++j) {
            
                t[i][j].getStackTrace();
                
                // currentThread() call has no special meaning, just in case
                if (Thread.currentThread() == null) {
                    if (!failed) { // log failure only once to avoid overwhelming in log
                        log.add("currentThread() returned null unexpectedly");
                    }
                    failed = true;
                }
              
                Thread.getAllStackTraces();
                Thread.yield();
            }
        }

        for (int i = 0; i < t.length; ++i){
            for (int j = 0; j < t[i].length; ++j) {
                t[i][j].interrupt();
                try {
                    t[i][j].join();
                    // System.out.println("Joined thread " + i + " / " + j);
                } catch (InterruptedException ie) {
                    System.out.println("InteruptedException while joining all started A,B,C,D... threads");
                }
            }
        }
        
        return !failed;
    }
   
    public void parseParams(String[] params) {
    }
}

    // A thread invoking synchronized static method which holds A.class lock 
    // and waits infinitely (actually, until the thread is interrupted)

class A extends BaseThread {

    public void run() {
        m(this);
    }
   
    static synchronized void m(Thread t) {
        try {
            ThreadCounter.incRunning();
            while (!t.isInterrupted()) {
                A.class.wait();
            }
        } catch (InterruptedException ie) {
            // System.out.println("A: interrupted, InterruptedException");
            return;
        }
    }
}

    // A thread invoking static method which holds newly created Object lock 
    // and waits infinitely (actually, until the thread is interrupted)

class B extends BaseThread {

    public void run() {
        m(this);
    }
   
    static void m(Thread t) {
        try {
            B b = new B();
            synchronized(b) {
                ThreadCounter.incRunning();
                while (!t.isInterrupted()) {
                    b.wait();
                }
            }
        } catch (InterruptedException ie) {
            // System.out.println("B: interrupted, InterruptedException");
            return;
        }
    }
}

    // A thread invoking instance method which holds 'this' object lock 
    // and waits infinitely (actually, until the thread is interrupted)

class C extends BaseThread {

    public void run() {
        m();
    }
   
    void m() {
        try {
            synchronized(this) {
                ThreadCounter.incRunning();
                while (!this.isInterrupted()) {
                    this.wait();
                }
            }
        } catch (InterruptedException ie) {
            // System.out.println("C: interrupted, InterruptedException");
            return;
        }
    }
}

    // A thread invoking synchronized instance method which holds 'this' 
    // and newly created Object locks and waits infinitely (actually, until 
    // the thread is interrupted)

class D extends BaseThread {

    public void run() {
        m();
    }
   
    synchronized void m() {
        try {
            synchronized(new D()) {
                ThreadCounter.incRunning();
                while (!this.isInterrupted()) {
                    this.wait();
                }
            }
        } catch (InterruptedException ie) {
            // System.out.println("D: interrupted, InterruptedException");
            return;
        }
    }
}

    // A thread invoking instance method which just sleeps in 'try' section
    // infinitely (actually, until the thread is interrupted)

class E extends BaseThread {

    public void run() {
        m();
    }
   
    void m() {
        try {
            ThreadCounter.incRunning();
            while (!this.isInterrupted()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException ie) {
            // System.out.println("E: interrupted, InterruptedException");
            return;
        }
    }
}

    // A thread invoking instance method which just sleeps in 'catch' section
    // infinitely (actually, until the thread is interrupted)

class F extends BaseThread {

    public void run() {
        m();
    }
   
    void m() {
        try {
            try {
                throw new NullPointerException("npe");
            } catch (NullPointerException npe) {
                ThreadCounter.incRunning();
                while (!this.isInterrupted()) {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException ie) {
            // System.out.println("F: interrupted, InterruptedException");
            return;
        }
    }
}

    // A thread invoking instance method which just sleeps in 'finally' section
    // infinitely (actually, until the thread is interrupted)

class G extends BaseThread {

    public void run() {
        m();
    }
   
    void m() {
        try {
            try {
                throw new NullPointerException("npe");
            } catch (NullPointerException npe) {
            } finally {
                ThreadCounter.incRunning();
                while (!this.isInterrupted()) {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException ie) {
            // System.out.println("G: interrupted, InterruptedException");
            return;
        }
    }
}

    // A thread invoking instance method which holds static newly created Object
    // and waits infinitely (actually, until the thread is interrupted)

class H extends BaseThread {

    static H h = new H();
 
    public void run() {
        m();
    }
   
    void m() {
        try {
            synchronized (h) {
                ThreadCounter.incRunning();
                while (!this.isInterrupted()) {
                    h.wait(100);
                }
            }
        } catch (InterruptedException ie) {
            // System.out.println("H: interrupted, InterruptedException");
            return;
        }
    }
}

    // A thread invoking instance method which holds a lock to array of 
    // objects and waits infinitely (actually, until the thread is interrupted)

class I extends BaseThread {

    Object o = null;

    public I(Object o) {
        this.o = o;
    }

    public void run() {
        m();
    }

    void m() {
        try {
            synchronized (o) {
                ThreadCounter.incRunning();
                while (!this.isInterrupted()) {
                    o.wait();
                }
            }
        } catch (InterruptedException ie) {
            // System.out.println("I1: interrupted, InterruptedException");
            return;
        }
    }
}


    // A thread invoking instance method which is recursively invokes itself  
    // at the end holds 'this' lock and waits infinitely (actually, until
    // the thread is interrupted).

class J extends BaseThread {

    public void run() {
        m(20);
    }

    void m(int depth) {
        if (depth > 0){
            m(depth -1);
        } else {
            synchronized (this) {
                try {
                    ThreadCounter.incRunning();
                    while (!this.isInterrupted()) {
                        this.wait();
                    }
                } catch (InterruptedException ie) {
                    // System.out.println("J: interrupted, InterruptedException");
                }
            }
        }
    }
}

class BaseThread extends Thread {
    // just in case - if some common functionality will be needed
}

class ThreadCounter {

    static volatile int running = 0;
    
    static synchronized int getRunning() {
        return running;
    }

    static synchronized void incRunning() {
        running++;
    }
  
    static synchronized void clear() {
        running = 0;
    }
}
