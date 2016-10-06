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

package org.apache.harmony.test.reliability.api.kernel.threadgroup;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;

/*
 * Goal: check that ThreadGroup's enumerate(ThreadGroup, true), enumerate(Thread, true) 
 *       and destroy() work without problems (crashes) in multi-threaded environment.
 *
 * The test does:
 *   1. Reads parameters:
 *      param[1] - number of direct subgroups of each ThreadGroup in the ThreadGroup tree
 *      param[2] - number of Threads - direct childs of each ThreadGroup in the tree
 *      param[3] - defines tree depth
 *      param[4] - number of Threads calling enumerate() methods in parallel to working
 *                 threads of the tree.
 *      
 *   2. Builds a tree of ThreadGroups and Threads:
 *      - each ThreadGroup (but leaf ThreadGroup) has N_OF_SUBGROUPS child ThreadGroups
 *      - in addition, each ThreadGroup has N_OF_THREADS child Threads
 *      - tree depth is TREE_DEPTH + 1
 *      - Threads are started and running
 *      
 *   3. Creates N_OF_ENUMERATING_THREADS "enumerating" Threads, each calling 
 *      enumerate(ThreadGroup, true) and enumerate(Thread, true) in a cycle 
 *      until there are no active Threads or all threads were supposedly finished. 
 *      
 *   4. When no active Threads remain, each "enumerating" Thread calls 
 *      destroy() for the root ThreadGroup.
 *   
 *   5. Expected result: no crashes or hangs.
 *   
 */

public class EnumerateTest extends Test {
    
    public static int N_OF_SUBGROUPS = 2;
    public static int N_OF_THREADS = 2;
    public static int TREE_DEPTH = 5;
    
    public static int N_OF_ENUMERATING_THREADS = 10;

    public static void main(String[] args) {
        System.exit(new EnumerateTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        // Two test calls:
        // 1. ThreadGroups / Threads are daemons
        // 2. ThreadGroups / Threads are non-daemons
    
        return (test(true) && test(false)) ? pass("OK") : fail("");
    }

    boolean test(boolean daemon) {
    
        ThreadCounter.clear(); // clear static counters of created and being running threads

        ThreadGroup rootTG = new ThreadGroup("0");
        
        rootTG.setDaemon(daemon);
       
        // build ThreadGroup tree
        new TGTreeBuilder().buildTree(rootTG, N_OF_SUBGROUPS, N_OF_THREADS, TREE_DEPTH);

        // start "enumerating" threads
        Thread[] t = new Thread[N_OF_ENUMERATING_THREADS];
        
        for (int i = 0; i < t.length; ++i){
            t[i] = new EnumeratingThread(rootTG, i);
            t[i].start();
        }
        
        for (int i = 0; i < t.length; ++i){
            try {
                t[i].join();
            } catch (InterruptedException ie) {
                log.add("InterruptedException while join()-ing enumerating threads");
                return false;
            }
        }

        // finally, call destroy() expecting ITSE, since 'rootTG' is actually
        // destroyed by one of enumerating threads
        try {
            rootTG.destroy();
        } catch (IllegalThreadStateException itse){
            // System.out.println("Main thread : IllegalThreadStateException");
        }
        
        return true;
    }
    
    public void parseParams(String[] params) {
        
        if (params.length >= 1) {
            N_OF_SUBGROUPS = Integer.parseInt(params[0]);
        }

        if (params.length >= 2) {
            N_OF_THREADS = Integer.parseInt(params[1]);
        }

        if (params.length >= 3) {
            TREE_DEPTH = Integer.parseInt(params[2]);
        }
       
        if (params.length >= 3) {
            N_OF_ENUMERATING_THREADS = Integer.parseInt(params[3]);
        }        
    }
}


class EnumeratingThread extends Thread {

    static Random Rnd = new Random(1);

    ThreadGroup tg = null;
    int ID = 0;

    EnumeratingThread(ThreadGroup tg, int ID){
        this.tg = tg;
        this.ID = ID;
    }

    public void run() {

        prioritize(tg);
        
        if (ID % 2 == 0) {
            enumerateThreads(tg);
            enumerateGroups(tg);
        } else {
            enumerateGroups(tg);
            enumerateThreads(tg);
        }

        // Only one "enumerating" Thread will really destroy 'tg'...
        while (!tg.isDestroyed()){
            try {
                tg.destroy();
            } catch (IllegalThreadStateException itse){
                Thread.yield();
            }
        }
    }
   
    void prioritize(ThreadGroup tg) {
        int prio = Thread.MIN_PRIORITY + Rnd.nextInt((Thread.MAX_PRIORITY - Thread.MIN_PRIORITY));
        tg.setMaxPriority(prio);
    }

    void enumerateThreads(ThreadGroup tg) {
        // We are actually not interested in receiving all Threads,
        // instead, lets see what happens if we are interested only
        // in one - won't there be problems with leaving Thread objects
        // outside of array?
        Thread[] t = new Thread[1];

        // There is no synchronization between enumerating and being enumerated Threads 
        // (intentionally).
        // We enumerate active threads while less threads are/were actually run than 
        // were created (to avoid situation that some threads were start()-ed but are not
        // yet run()-ning and are not considered active), or there are still active threads,
        // or, threads are not considered active (no clear definition of "active" in specification) 
        // but not yet all finished.
        
        while(tg.enumerate(t, true) > 0 || tg.activeCount() > 0 || 
            ThreadCounter.getRunning() < ThreadCounter.getCreated() ||
            ThreadCounter.getFinishing() < ThreadCounter.getCreated()) {
            Thread.yield();
        }
        // System.out.println("All threads finished " + ID);
        
        // At this point we can expect that all threads finished - all were run, 
        // no active or running remains.
    }

    void enumerateGroups(ThreadGroup tg) {
        // The same as with Threads - we are actually not interested in receiving all groups
        ThreadGroup[] t = new ThreadGroup[1];
        
        tg.activeGroupCount(); // just in case, no specific purpose of this call
        tg.enumerate(t, true);
    }
}


class TGTreeBuilder {

    // tree building is done via recursive invocation of buildTree(...)
    
    public void buildTree(ThreadGroup parent, int n_subgroups, int n_threads, int depth) {

        // first, create threads which are direct childs of the 'parent'
        createThreads(n_threads, parent);
        
        if (depth > 0) {
            for (int i = 0; i < n_subgroups; ++i){
                // second, create ThreadGroups which are direct childs of the 'parent'
                ThreadGroup tg = new ThreadGroup(parent, "");
                tg.setDaemon(parent.isDaemon());
                // for each child ThreadGroup build a subtree
                buildTree(tg, n_subgroups, n_threads, depth - 1);
            }
        }
    }
    
    // Creates and starts n_threads of AThread class
    public Thread[] createThreads(int n_threads, ThreadGroup parent){
        Thread[] t = new Thread[n_threads]; 
        for (int i = 0; i < t.length; ++i){
            t[i] = new AThread(parent);
            t[i].setDaemon(parent.isDaemon());
            t[i].start();
        }
        return t;
    }
}

    // AThread is just ordinary thread which should execute some time-consuming
    // operations, no metter which. What is important that there is a chance that 
    // "enumeration" threads call enumerate() while AThreads are active.

class AThread extends Thread {

    public AThread(ThreadGroup parent) {
        super(parent, "");
        ThreadCounter.incCreated();
    }
  
    public void run() {
        ThreadCounter.incRunning();
        A.doSomething();
        enumerate(new Thread[1]); // test call
        ThreadCounter.incFinishing();
    }
}

class A {

    static int sizeI = 10;
    static int sizeJ = 100;

    static Random Rnd = new Random(1);
 
    void setSize(int i, int j){
        sizeI = i;
        sizeJ = j;
    }
    
    // The method just fills-in 2 dimensional String array with sleep()s
    // between iterations. i.e. literally does something.
    
    static void doSomething(){
    
        String[][] str = new String[sizeI][sizeJ];
        
        for (int i = 0; i < str.length; ++i) {
      
            for (int j = 0; j < str[i].length; ++j) {
                str[i][j] = "" + i + "" + j;
            }
        
            Thread.yield();

            int rnd = Rnd.nextInt(10);
            try {
                Thread.sleep(rnd);
            } catch (InterruptedException ie) {
            }
        }
    }
}

class ThreadCounter {

    static volatile int running = 0;
    static volatile int created = 0;
    static volatile int finishing = 0;

    static synchronized int getRunning() {
        return running;
    }

    static synchronized int getCreated() {
        return created;
    }
    
    static synchronized int getFinishing() {
        return finishing;
    }

    static synchronized void incRunning() {
        ++running;
    }
  
    static synchronized void incCreated() {
        ++created;
    }
    
    static synchronized void incFinishing() {
        ++finishing;
    }

    static synchronized void clear() {
        running = 0;
        created = 0;
        finishing = 0;
    }
}
