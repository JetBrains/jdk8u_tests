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
 * @author Igor A. Pyankov
 * @version $Revision: 1.5 $
 */

package org.apache.harmony.test.reliability.api.kernel.thread.RecursiveThreadTest;

import org.apache.harmony.test.reliability.share.Test;

/**
 *  Goal: check that VM supports volatile variables
 *  The test does:
 *     1. Reads parameters, which are:
 *            param[0] - depth of recursion of threads
 *            param[1] - size of Object array for each thread
 *     2. Starts first thread, which starts the next one and so on ...
 *
 *     3. Each thread (except the last), being started:
 *         a. Starts child thread 
 *         b. Creates param[1]-sized array of Object and fills it
 *         c. Changes parent's flag to allow it finish
 *         d. Finishes run if child thread allowed 
 *
 */


public class RecursiveTest extends Test {
    
    public static int depth = 500;
    public static int size = 1000;
    public static volatile boolean finish = false;

    public static void main(String[] args) {
        System.exit(new RecursiveTest().test(args));
    }

    public int test(String[] params) {
        finish = false;
        parseParams(params);
        recurThread grandpa = new recurThread();
        recurThread dad = (new recurThread(grandpa));
        dad.start();
        /*
         try {
         dad.join();
         log.add("Thread dad joined()");
         } catch (InterruptedException ie) {
         return fail("interruptedException while join of thread");
         }
         */
        while (!finish) {};
        return pass("OK");
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            depth = Integer.parseInt(params[0]);
            if (params.length >= 2) {
                size =  Integer.parseInt(params[1]);
            }
        }        
    }

}

class recurThread extends Thread {
    recurThread parent;

    volatile boolean flag;

    public recurThread() {
        flag = true;
    }

    public recurThread(recurThread pt) {
        parent = pt;
        flag = false;
    }

    private void doSomething() {
        // just wasting memory
        Object[] array = new Object[RecursiveTest.size];
        for (int i = 0; i < RecursiveTest.size; i++) {
            array[i] = new char[10];
        }
    }

    public void run() {
        RecursiveTest.depth--;
        //RecursiveTest.log.add("d=" + RecursiveTest.depth);
        if (RecursiveTest.depth > 0) { //is this the last thread?
            //no, starts another
            recurThread rt = new recurThread(this);
            rt.start();
            doSomething();
        } else { //yes
            flag = true;
            RecursiveTest.finish = true; // stop test
        }
        parent.flag = true; //allow to parent thread finish
        
        //waiting for permission to finish from child
        while (!flag) { 
            Thread.yield();   
        };
        return;
    }

}

