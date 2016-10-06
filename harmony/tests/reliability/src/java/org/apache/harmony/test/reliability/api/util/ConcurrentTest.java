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
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

/*
 * Goal: Test java.util.concurrent package
 * The main idea is to test loading of the same class in many threads
 * All synchronizations are to be done on classloader level in VM
 * The main members of the test are: initiating, defining classloaders, 
 * delegation model
 * 
 *  passed parameters:
 *  parameter[0] - number of threads to start
 *  
 *  The test Thread does the following, 2 stages:
 *  1 stage:
 *  * runs NumberOfThreads interacting w each other:
 *    a. CopyOnWriteArrayList + Exchanger
 *    b. Sending and receiving among threads via PriorityBlockingQueue
 *    c. Semaphore is used for synch purpose of weak place in code
 *  * 
 *  2 stage:
 *  * runs NumberOfThreads by scheduler ScheduledThreadPoolExecutor
 *  * threads do the a,b,c, actions
 *  
 *  */

package org.apache.harmony.test.reliability.api.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.harmony.test.reliability.share.Test;

public class ConcurrentTest extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 30;
    static int numberOfThreads = NUMBER_OF_THREADS;
    static Semaphore sm = null;

    static CyclicBarrier cb;
    
    public static void main(String[] params){
        System.exit(new ConcurrentTest().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);
        cb = new CyclicBarrier(numberOfThreads);
        int thirdOfThreads = (int)(numberOfThreads/3); 
        sm = new Semaphore(thirdOfThreads);

        // 1-st run as usual threads
        Thread[] thrds = new Thread[numberOfThreads];
        for (int i = 0; i<thrds.length; i++){
            thrds[i] = new cnRunner();
            thrds[i].start();
        }

        for (int i = 0; i<thrds.length; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                return fail("FAILED");
            }
        }

        if (failed == true){
            return fail("FAILED");
        }

        // 2-nd run - threads are launched with scheduler - short cycle = 100 milliseconds
        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(numberOfThreads);
        ArrayList<Thread> ar = new ArrayList<Thread>();
        for (int i=0; i<numberOfThreads; i++){
            Thread rn = new cnRunner(); 
            ar.add(rn);
            stpe.schedule(rn, cnRunner.rm.nextInt(100), TimeUnit.MILLISECONDS);
        }
        try {
            stpe.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // Expected
        }
        
        if (failed == true){
            return fail("FAILED");
        }
        
        return pass("OK");
    }
    
    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
    }
}

class cnRunner extends Thread{
    static Random rm = new Random();
    static AtomicLong stats = new AtomicLong(0);
    static Exchanger<String> exch = new Exchanger<String>();
    static CopyOnWriteArrayList<String> tArray = new CopyOnWriteArrayList<String>();
    static PriorityBlockingQueue<String> bQue = new PriorityBlockingQueue<String>();

    public void run (){
        // Stage 1: test CopyOnWriteArrayList + Exchanger
        String currThrdId = new String ("");
        try {
            stats.getAndAdd(ConcurrentTest.cb.getNumberWaiting());
            currThrdId = new Long(Thread.currentThread().getId()).toString(); 
            tArray.add(currThrdId);
            ConcurrentTest.cb.await();            
        } catch (InterruptedException e) {
            ConcurrentTest.log.add("Failed to wait all starting threads, stage 1!");
            ConcurrentTest.failed = true;
        } catch (BrokenBarrierException e) {
            ConcurrentTest.log.add("Failed to wait all starting threads, stage 1!");
            ConcurrentTest.failed = true;
        }
        
        if (ConcurrentTest.cb.isBroken()){
            ConcurrentTest.log.add("Failed to wait all starting threads!");
            ConcurrentTest.failed = true;
        }
        
        // The task for thread: try to find its name for 100 exchange operations
        int counter = rm.nextInt(300)+100;
        try {
            Thread.sleep(rm.nextInt(100)+1);
        } catch (InterruptedException e1) {
            ConcurrentTest.failed = true;
        }
        String thrdId = tArray.remove(0); 
        while (counter > 0){
            if (thrdId == currThrdId){
                break;
            }
            try {
                thrdId = exch.exchange(thrdId, rm.nextInt(100)+1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                ConcurrentTest.failed = true;
            } catch (TimeoutException e) {
                // Expected
            }
            counter--;
        }
        //System.out.println(counter);

        try {
            stats.getAndAdd(ConcurrentTest.cb.getNumberWaiting());
            ConcurrentTest.cb.await();
        } catch (InterruptedException e) {
            ConcurrentTest.log.add("Failed to wait all starting threads, stage 1!");
            ConcurrentTest.failed = true;
        } catch (BrokenBarrierException e) {
            ConcurrentTest.log.add("Failed to wait all starting threads, stage 1!");
            ConcurrentTest.failed = true;
        }

        // Stage 2: test PriorityBlockingQueue 
        //devide threads to sending and receiving
        // to thread: Who are you - sender or receiver?
        counter = rm.nextInt(100);
        int threadChoice = rm.nextInt(2);
        if (threadChoice == 0){
            while (counter > 0){
                //emit into queue
                String toQue = "Que_" + new Long(Thread.currentThread().getId()).toString() + "_" + rm.nextInt(100);
                if (counter == 50){
                    // process one exception
                    toQue = null;
                }
                try{
                    bQue.put(toQue);
                }catch(NullPointerException e){
                    // Expected
                }
                counter--;
                //System.out.println("Emmited " + counter);
            }
        } else{
            while (counter > 0){
                //read from queue
                try {
                    bQue.poll(rm.nextInt(100)+1, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    ConcurrentTest.failed = true;
                }
                counter--;
                //System.out.println("Read " + counter);
            }
        }
        
        try {
            stats.getAndAdd(ConcurrentTest.cb.getNumberWaiting());
            ConcurrentTest.cb.await();
        } catch (InterruptedException e) {
            ConcurrentTest.log.add("Failed to wait all starting threads, stage 2!");
            ConcurrentTest.failed = true;
        } catch (BrokenBarrierException e) {
            ConcurrentTest.log.add("Failed to wait all starting threads, stage 2!");
            ConcurrentTest.failed = true;
        }

        // Stage 3: test Semaphore 
        // limit Semathor with 1/3 of number of threads
        // replaced doing something with rundomized time thread sleep
        // sm is located in ConcurrentTest bacause we need to initialize it
        // with 1/3 number of threads
        counter = 1000;
        while (counter > 0){
            try {
                stats.getAndAdd(ConcurrentTest.sm.availablePermits());
                ConcurrentTest.sm.acquire();
                //System.out.println("in");
            } catch (InterruptedException e) {
                ConcurrentTest.log.add("Some thread is interrupted, stage 3!");
                ConcurrentTest.failed = true;
            }
            
            try {
                Thread.currentThread().sleep(rm.nextInt(5)+1);
            } catch (InterruptedException e) {
                ConcurrentTest.log.add("Some thread is interrupted, stage 3!");
                //ConcurrentTest.failed = true;
            }
            
            stats.getAndAdd(ConcurrentTest.sm.getQueueLength());
            ConcurrentTest.sm.release();
            //System.out.println("out");
            counter--;        
        }

        // final actions
        if (ConcurrentTest.cb.isBroken()){
            ConcurrentTest.log.add("Failed to wait all starting threads, final!");
            ConcurrentTest.failed = true;
        }
        
        ConcurrentTest.cb.reset();
    }

    
}
    
