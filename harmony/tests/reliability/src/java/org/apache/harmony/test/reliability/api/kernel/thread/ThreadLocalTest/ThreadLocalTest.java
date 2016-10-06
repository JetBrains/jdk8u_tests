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

package org.apache.harmony.test.reliability.api.kernel.thread.ThreadLocalTest;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;
import java.util.Vector;

/*
 * Goal: check that ThreadLocals work as expected, i.e. really are thread-own
 *       copies of objects which can't be affected by other than the calling thread.
 * 
 * The test does:
 *      
 *   1. Reads parameters:
 *      params[0] - number of child threads of each thread in the tree
 *      params[1] - depth of the tree
 *
 *   2. Creates a Thread tree:
 *           r o o t      level params[1].
 *          /   |   \ 
 *         t    t    t    level params[1]-1. Each thread has param[0] child threads    
 *       / | \ ... / | \   
 *                        level 0.
 *
 *      Each created thread is started.
 *      Each thread has instance ThreadLocal variable. There is static LocalVariable as well.
 *      Each thread holds references to all its childs, parent and itself ('this').
 *      
 *   3. Each running thread sets its private objects to thread local variables of:
 *      child threads, parent and itself and checks that variables are set correctly.
 *
 */


public class ThreadLocalTest extends Test {

    static int N_OF_THREADS = 2;
    
    static int DEPTH = 6;

    public static void main(String[] args) {
        System.exit(new ThreadLocalTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
       
        // threads: non-daemon/daemon
        
        return (test(false) && test(false)) ? pass("OK") : fail("");
    }

    boolean test(boolean daemon) {

        ThreadCounter.clear();
        
        // First, tree building and tree's threads running starts here:
        ThreadWithLocals t = new ThreadWithLocals(0, null, DEPTH, daemon);
        t.setDaemon(daemon);
        t.start();

        // At this point all threads are running and doing (or already completed) 
        // set()s/get()s and checks.
        // Lets wait they _all_ complete checks and fall into infinite cycle,
        // that means that we can join() them and finish the test.
        
        while(ThreadCounter.getCreated() != ThreadCounter.getFinishing()){
            Thread.yield();
        }
        
        // System.out.println("All " + ThreadCounter.getCreated() + " threads are finishing");
        
        // At this point, all threads are in infinite cycle waiting 
        // for the exit flag set, lets set the flag thus allow them to complete
       
        ThreadCounter.setExitAllowed();
        
        // System.out.println("Exit allowed, joining all... ");

        // Join all threads
        ThreadCounter.joinAllThreads();
        
        // System.out.println("All threads joined");

        // Finally, return results of checks done by the threads
        return ThreadCounter.getStatus();
    }


    public void parseParams(String[] params){
        if (params.length >= 1) {
            N_OF_THREADS = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            DEPTH = Integer.parseInt(params[1]);
        }
    }
    
}

class ThreadWithLocals extends Thread {

    static Random RND = new Random(10);
   
    volatile boolean running = false;
    
    ThreadWithLocals[] t = null; 
    int ID;
    int[] Arr;
    SomeObject Obj;
    
    ThreadLocal tl_var_instance = new ThreadLocal<SomeObject>() {
                                                                };

    static ThreadLocal tl_var_static = new ThreadLocal<int[]>() {
                                                                };
    
    ThreadWithLocals (int ID, ThreadWithLocals parent, int depth, boolean daemon) {
        super();
        this.running = false;
        this.ID = ID;
        this.Arr = new int[1];
        this.Arr[0] = ID;
        this.Obj = new SomeObject(ID);

        // System.out.println("Creating " + ID);
        
        ThreadCounter.incCreated(this);
 
        if (depth > 0) {
            // 't' holds references to N_OF_THREADS child threads and parent and itself:
            t = new ThreadWithLocals[ThreadLocalTest.N_OF_THREADS + 2];
            t[0] = parent;
            t[1] = this;
            for (int i = 2; i < t.length; ++i){
                // (ID * t.length + i) is used as formula for child IDs to make IDs unique 
                t[i] = new ThreadWithLocals(ID * t.length + i, this, depth - 1, daemon);
                
                // System.out.println("Thread " + ID + " -> " + (ID * t.length + i) + " child thread");
                t[i].setDaemon(daemon);
                t[i].start();
            }
        } else {
            // this is leaf thread - it has no child threads, so, holds only 
            // references to parent and itself
            t = new ThreadWithLocals[2];
            t[0] = parent;
            t[1] = this;
        }
    }
    
    synchronized boolean isRunning(){
        return running;
    }

    synchronized void setRunning(){
        running = true;
    }

    public void run() {
        
        setRunning();
        
        // Each thread calls set()/get() and checks local variables of all
        // threads listed in 't' (e.g. child threads, parent and itself) expecting that
        // in concurrently running threads environment when other threads are changing 
        // the same variables, the variables are really _this_ thread-local (e.g. other 
        // threads' modifications do not affect _this_ thread's variables).
        
        for (int i = 0; i < t.length; ++i) {
            
            if (t[i] != null) {
                
                // thread local variables live when the thread is alive,
                // this check is to avoid that t[i] is started but not actually running;
                // avoiding that some thread finished and is not alive is guaranteed
                // below by hangUntilExitAllowed().
                
                while(!t[i].isRunning()) {
                    Thread.yield();
                }
                
                // System.out.println(" Thread ID=" + ID + " - all necessary threads are alive");
                
                int rndNumber = RND.nextInt(100) + 1;                
                for (int j = 0; j < rndNumber; ++j){
                    t[i].tl_var_static.set(this.Arr); // TEST call
                    Thread.yield();
                }
                
                rndNumber = RND.nextInt(100) + 1;
                for (int j = 0; j < rndNumber; ++j){
                    t[i].tl_var_instance.set(this.Obj); // TEST call
                    Thread.yield();
                }
            }
        }
       
        ThreadCounter.joinStatus(check()); // Add status of checks of this thread to the common flag
       
        // Signal that set()s/get()s/checks are finished; When all created threads signalled 
        // they finished, the main thread ThreadLocalTest can allow threads exit from 
        // hangUntilExitAllowed() infinite cycle.
        ThreadCounter.incFinishing();        
        
        // System.out.println(" Thread ID=" + ID + " is going to hang before exit...");
        
        ThreadCounter.hangUntilExitAllowed(); // Fall into infinite cycle waiting for certain flag set
    }


    boolean check() {
    
        boolean passed = true;
     
        for (int i = 0; i < t.length; ++i){
            if (t[i] == null) {
                continue;
            }
            
            int rndNumber = RND.nextInt(100) + 1;
            
            boolean printed1 = false;
            boolean printed2 = false;
            
            for (int j = 0; j < rndNumber; ++j){

                int[] arrThreadLocal = ((int[])t[i].tl_var_static.get()); // TEST call
                
                // TEST checks - if variables are really thread local then the values
                // of the variables must be this.Arr and this.Obj despite of other threads
                // set their values to the same variables
                
                if (arrThreadLocal != this.Arr) { 
                    if (!printed1) {
                        System.out.println("Thread ID=" + this.ID + " checked value of thread ID=" + t[i].ID + 
                            "'s _static_ ThreadLocal variable: get() returned wrong object " + 
                            arrThreadLocal + " instead of " + this.Arr);
                        printed1 = true;
                    }
                    passed &= false;
                }
        
                SomeObject objThreadLocal = ((SomeObject)t[i].tl_var_instance.get()); // TEST call
               
                if (objThreadLocal != this.Obj) {
                    if (!printed2) {
                        System.out.println("Thread ID=" + this.ID + " checked value of thread ID=" + t[i].ID + 
                            "'s _instance_ ThreadLocal variable: get() returned wrong object " + 
                            objThreadLocal + " instead of " + this.Obj);
                        printed2 = true;
                    }
                    passed &= false;
                }
            }
           
            // System.out.println("Thread ID=" + ID + " checked " + t[i].ID + " thread's locals");
        }
      
        return passed;
    }

}

class SomeObject {
    int i = 0;
    
    SomeObject(int i) {
        this.i = i;
    }
    
    int getI() {
        return i;
    }
}

class ThreadCounter {

    static volatile boolean status = true;
    static volatile int finishing = 0;
    static volatile int created = 0;

    static volatile boolean isExitAllowed = false;
   
    static Vector threadsVector = new Vector();

    
    static synchronized void joinStatus(boolean passed) {
        status &= passed;
    }
   
    static synchronized boolean getStatus() {
        return status;
    }

    static synchronized void incCreated(Thread t) {
        created++;
        threadsVector.add(t);
    }
    
    static synchronized void incFinishing() {
        finishing++;
    }

    static synchronized int getFinishing() {
        return finishing;
    }

    static synchronized int getCreated() {
        return created;
    }

    static synchronized void clear() {
        created = 0;
        finishing = 0;
        status = true;
        isExitAllowed = false;
        threadsVector = new Vector();
    }
    
    static void setExitAllowed(){
        isExitAllowed = true;
    }
   
    static void hangUntilExitAllowed(){
        while(!isExitAllowed) {
            Thread.yield();
        }
    }

    static synchronized void joinAllThreads() {
        if (threadsVector != null) {
            Object[] tArray = threadsVector.toArray();
            for (int i = 0; i < tArray.length; ++i){
                try {
                    ((Thread)tArray[i]).join();
                } catch (InterruptedException ie){
                }
            }
            threadsVector = null;
        }
    }
}




