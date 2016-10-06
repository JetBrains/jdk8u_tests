/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
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
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

package org.apache.harmony.test.reliability.api.kernel.thread.ThreadSuspendResume;

import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.apache.harmony.test.reliability.share.Test;
import org.apache.harmony.test.reliability.share.Result;

/**
 *  Goal: check suspend/resume functionality in VM
 *        The test does:
 *        1. Reads parameters, which are:
 *           param[0] - number of threads
 *           param[1] - how many times to suspend/resume threads
 *        2. Creates param[0]-sized array of threads and starts them. Threads are created 
 *           twice: 1st as user threads, 2-nd - as daemon threads.
 *           There are 4 kinds of created threads: 
 *            a. math operations - does several java.lang.Math operations in a cycle 
 *            b. math operations - does some simple calculations in a cycle
 *            c. exceptions throwing - exceptions chosen by random are thwon in a cycle
 *            d. synchronized sections - this type of thread contains 3 sinchronized sections,
 *               2 sections contain wait-notify functionality.
 *            - b,c are instrumented with sleep(0) function to allow main thread easily suspend
 *              started threads (introduced suspend points into the code).
 *           
 *        3. Makes param[1] suspend/resume operations on started threads 
 *        5. Calls .join() for each thread to sure that all threads have been killed
 * 
 *        No hang, fail or crash is excpected.
 *    
 */

public class ThreadSuspendResume extends Test {
    static final int NUMBER_OF_MILIS_WAITING = 30;
    static final int NUMBER_OF_THREADS = 30;
    static final int NUMBER_OF_ITERATIONS = 20; // suspend-resume iterations
    static int numberOfIterations = NUMBER_OF_ITERATIONS;
    static int numberOfThreads = NUMBER_OF_THREADS;
    static final int NUMBER_OF_CASE_BLOCKS = 4;
    volatile public static boolean failed = false; 
    volatile public static boolean stopActivities = false;

    public static void main(String[] args) {
        System.exit(new ThreadSuspendResume().test(args));
    }

    public int test(String[] params) {
        parseParams(params);
        int res = testInternal(false);
        if (res != Result.PASS){
            return res;
        }
        res = testInternal(true);
        if (res != Result.PASS){
            return fail("FAILED");
        }else{
            return pass("OK");
        }
            
    }
    
    int testInternal(boolean daemon){
        Random rm = new Random();
        // initialize variables of MonSynchThread 
        MonSynchThread.mut1 = new Integer(1); 
        MonSynchThread.mut2 = new Integer(2);
        MonSynchThread.mut3 = new Integer(2);

        
        // create different type threads
        Thread thrds[] = new Thread[numberOfThreads];
        for (int k = 0; k < thrds.length; k++) {
            switch (k%4){
                case 0:
                    thrds[k] = new ExceptionThrowThread();
                    break;
                case 1: 
                    thrds[k] = new MonSynchThread();//ClassLoadingThread();
                    break;
                case 2:
                    thrds[k] = new HardWorkingThreadSimple();
                    break;
                case 3:
                    thrds[k] = new HardWorkingThread();
                    break;
            }
            if (daemon == true){
                thrds[k].setDaemon(true);
            }
        } 
        
        // start created threads
        for (int k = 0; k < thrds.length; k++) {
            thrds[k].start();
        }
        
        // load Sestem class to avoid deadlock in ClassLoader due to 
        // unsafe suspend/resume operations
        Class sysClass = System.class;
        sysClass = null;
        
        // suspend/resume section
        //System.out.println("Startign suspension/resuming");
        int cycles = numberOfIterations * 2;
        for (int j = 0; j<cycles; j++){
            //System.out.println("Iteration " + j);
            if (j%2 == 0){
                for (int i = 0; i< thrds.length; i++){
                    try{
                        //System.out.println("Trying to suspend " + thrds[i].getId());
                        thrds[i].suspend();
                        try {
                            Thread.currentThread().sleep(rm.nextInt(NUMBER_OF_MILIS_WAITING) + 1);
                        } catch (InterruptedException e) {
                            fail("Thread " +  thrds[i].getId() + " was interrupted");
                        }                        
                    } catch (SecurityException e){
                        return fail("Failed to suspend thread " +  thrds[i].getId());
                    }
                }
                System.gc();
            } else{
                for (int i = 0; i< thrds.length; i++){
                    try{
                        //System.out.println("Trying to resume " + thrds[i].getId());
                        thrds[i].resume();
                    } catch (SecurityException e){
                        return fail("Failed to suspend thread " +  thrds[i].getId());
                    }
                }
                System.gc();
            }
        }
        
        // stop threads
        stopActivities = true;
        
        // join created threads
        for (int k = 0; k < thrds.length; k++) {
            try{
                thrds[k].join();
            }catch (InterruptedException ie) {
                return fail("Failed to join thread " +  thrds[k].getId());
            }
        }
        
        if (failed == true){
            return Result.FAIL;
        }
        return Result.PASS;
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            numberOfIterations = Integer.parseInt(params[1]);
        }
    }

}

class HardWorkingThread extends Thread{
    static final int MAX_ITER_NUMBER = 10000;
    public void run(){
        Random rm = new Random();
        while (ThreadSuspendResume.stopActivities == false){
            Math.log(rm.nextDouble());
            Math.exp(rm.nextDouble());
            Math.sqrt(rm.nextDouble());
            Math.cos(rm.nextDouble());
        }
    }
}


class HardWorkingThreadSimple extends Thread{
    static final int MAX_ITER_NUMBER = 10000;
    public void run(){

        while (ThreadSuspendResume.stopActivities == false){
            long l = 0;
            for (int i = 0; i < (MAX_ITER_NUMBER - 1) ; i++){
                l += i;
                l *= i;
                long g = l%(i+1);
                if (g == 0){
                    g++;
                }
                double k = l/g;
                double h = k/3.14;
            }
        }
    }
}

class ExceptionThrowThread extends Thread{
    public void run(){
        while (ThreadSuspendResume.stopActivities == false){
            try{
                sleep(1);
                genNextException();
            }catch(Exception e){
                try {
                    sleep(1);
                    genNextException();
                } catch (Exception ex) {
                    // Expected
                    // do something                        
                }
                // do something
                e.getLocalizedMessage();
                e.getStackTrace();
            }
        }
    }
    void genNextException() throws Exception{
        Random rm = new Random();
        int choice = rm.nextInt(5);
        switch(choice){
            case 0:
                throw new TimeoutException();
            case 1:
                throw new IllegalAccessException();
            case 2:
                int t= 10;
                int z = 7/(10 -t);
                System.out.println(z);
                ThreadSuspendResume.log.add("Failed to throw exception case 4");
                ThreadSuspendResume.failed = true;
                break;
            case 3:
                char b[] = new char[10];
                b[b.length] = 0; // ArrayIndexOutOfBoundsException 
                ThreadSuspendResume.log.add("Failed to throw exception case 5");
                ThreadSuspendResume.failed = true;
                break;
            case 4:
                // throw nothing
                break;
            default:
                throw new RuntimeException();
        }
    }
}

    // Test on basic synchronisation logic 
class MonSynchThread extends Thread{
    static final int THREAD_WAIT_TIME = 100;
    static Object mut1 = null; 
    static Object mut2 = null;
    static Object mut3 = null;

    public void run(){
        
        while (ThreadSuspendResume.stopActivities == false){
            try{
                sleep(1);
            }catch(Exception e){
                ThreadSuspendResume.log.add("Thread " + this.getId() + " was interrupted.");
                ThreadSuspendResume.failed = true;
                break;
            }

            // go through synchronized, 1-st passed wait calls notifyAll()  
            synchronized(mut1){
                
                try {
                    mut1.wait(THREAD_WAIT_TIME);
                } catch (InterruptedException e) {
                    ThreadSuspendResume.log.add("Thread " + this.getId() + " was interrupted.");
                    ThreadSuspendResume.failed = true;
                    break;
                }
            
                mut1.notifyAll();
            }

            try{
                sleep(1);
            }catch(Exception e){
                ThreadSuspendResume.log.add("Thread " + this.getId() + " was interrupted.");
                ThreadSuspendResume.failed = true;
                break;
            }
            
            // go through synchronized, every passed calls notify()
            synchronized(mut2){
                
                try {
                    mut2.wait(THREAD_WAIT_TIME);
                } catch (InterruptedException e) {
                    ThreadSuspendResume.log.add("Thread " + this.getId() + " was interrupted.");
                    ThreadSuspendResume.failed = true;
                    break;
                }
            
                mut2.notify();
            }

            // simple synchronization
            synchronized(mut3){
                try{
                    sleep(1);
                }catch(Exception e){
                    ThreadSuspendResume.log.add("Thread " + this.getId() + " was interrupted.");
                    ThreadSuspendResume.failed = true;
                    break;
                }
                //
                int c = 100;
                int k = 0; 
                while((c--) > 0){
                    int i = 100;
                    int j = 200;
                    k /= (i*j);
                }
            }
            
        }
    }
}

