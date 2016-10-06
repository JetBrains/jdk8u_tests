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

package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.Test;

class ThreadSerializationTestFramework extends Test {
    private int threads;
    int[] results;
    String[] filenames;
    SerializationTestFramework tested;
    
    
    /**
     * @param threads number of threads to 
     */
    public ThreadSerializationTestFramework(int threads, String[] files, SerializationTestFramework tested) {
        super();
        this.threads = threads;
        results = new int[threads];
        filenames = new String[threads];
        System.arraycopy(files, 0, filenames, 0, threads);
        this.tested = tested;
    }
    
    public int testOut() {
        return test(false);
     }

    public int testIn() {
        return test(true);
     }
    
    private int test(boolean in) {
        Thread[] children = new Thread[threads];
        
//        System.err.println("got in ThreadSerializationTestFramework");
        
        for(int i = 0; i < threads; ++i)
        {
            
            //the idea is if we test output, we create a set of 'ThreadForOutTest' threads
            //and if we test input, a set of 'ThreadForInTest' ones
            (children[i] = (in ? (Thread) new ThreadForInTest(this, i) : 
                                 (Thread) new ThreadForOutTest(this, i))).start();
        }
        
        
        String failmsg = null;
        for(int i = 0; i < threads; ++i)
        {
           try {
            children[i].join();
        } catch (InterruptedException e) {
            failmsg = e.getMessage();
        }
        }

        if(failmsg != null)
        {
            return fail(failmsg);
        }
        
        for(int i = 0; i < threads; i++)
        {
            if(results[i] != Result.PASS) {
                return fail("something goes wrong");
                }
            }
        
        return pass();
    }

    public int test() {
        log.info("this method shouldn't be called");
        return Result.FAIL;
    }
}

class ThreadForOutTest extends Thread {
    ThreadSerializationTestFramework frameworkRef = null;
    int threadNo ;

    public ThreadForOutTest(ThreadSerializationTestFramework frameworkRef,
            int threadNo) {
        super();
        this.frameworkRef = frameworkRef;
        this.threadNo = threadNo;
    }
    
    public void run() {
//        System.err.println(Thread.currentThread().getName() + " started"); 
        frameworkRef.results[threadNo] = frameworkRef.tested.testOut(frameworkRef.filenames[threadNo]);
    }
}

class ThreadForInTest extends Thread {
    ThreadSerializationTestFramework frameworkRef = null;
    int threadNo ;

    public ThreadForInTest(ThreadSerializationTestFramework frameworkRef,
            int threadNo) {
        super();
        this.frameworkRef = frameworkRef;
        this.threadNo = threadNo;
    }
    
    public void run() {
//        System.err.println(Thread.currentThread().getName() + " started"); 
        frameworkRef.results[threadNo] = frameworkRef.tested.testIn(frameworkRef.filenames[threadNo]);
    }
}