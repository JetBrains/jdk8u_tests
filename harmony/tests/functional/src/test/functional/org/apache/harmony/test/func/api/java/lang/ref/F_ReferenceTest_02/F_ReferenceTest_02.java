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
/*
 * Created on 22.08.2005
 * 
 * This tests WeakReference object and Reference queue.
 * 1.Create several WeakReferences.
 * 2.Invoke Reference.enqueue() for some of them
 * 3.Invoked gc().
 * 4.Check that queue contains correct number of references
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_ReferenceTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.ref.*;
import java.util.*;

/**
 * This tests WeakReference object and Reference queue.
 * 1.Create several WeakReferences.
 * 2.Invoke Reference.enqueue() for some of them
 * 3.Invoked gc().
 * 4.Check that queue contains correct number of references
 */
public class F_ReferenceTest_02 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ReferenceTest_02().test(args));
    }

    private static final int NUM = 200;
    public int test() {
        
        ReferenceQueue rq = new ReferenceQueue();
        ArrayList wrList = new ArrayList(NUM);
        
        for(int i=0; i<NUM; ++i) {
            Object obj = new String("The string" +(i+1));
            WeakReference wr = new WeakReference(obj, rq);
            obj = null;
            wrList.add(wr);
        }

        
          
        for(int j=0; j<NUM; ++j) {
            ((WeakReference)wrList.get(j)).enqueue();
            System.gc();
            try {
                Thread.sleep(20);
            }
            catch(InterruptedException e) { }
            
        }
        
        for(int k=0; k<NUM; ++k) {
            Reference ref = rq.poll();
            if(ref == null) {
                return fail("queue is empty after deletion of " + k + " elements");
            }
            
            
            if(!wrList.remove(ref))
                return fail("reference "+ ref+" was enqueued twice or it is extra object in queue.");
            
        }
        
        if(!wrList.isEmpty()) {
            return fail("Not all references was enqueued");
        }
        log.info("test where System.gc() was invoked every time after first call of Reference.enqueue() passed.");
        ///////////////////////////
        rq = new ReferenceQueue();
        wrList = new ArrayList(NUM);
        
        for(int i=0; i<NUM; ++i) {
            Object obj = new String("The string" +(i+1));
            WeakReference wr = new WeakReference(obj, rq);
            obj = null;
            wrList.add(wr);
        }

          
        for(int j=0; j<NUM; ++j) {
            ((WeakReference)wrList.get(j)).enqueue();
            if(j % 8 == 0) {
                System.gc();
                   try {
                     Thread.sleep(20);
                }
                   catch(InterruptedException e) { }
            }
        }
        
        for(int k=0; k<NUM; ++k) {
            Reference ref = rq.poll();
            if(ref == null) {
                return fail("queue is empty after deletion of " + k + " elements");
            }
            
            if(!wrList.remove(ref))
                return fail("reference "+ ref+" was enqueued twice or it is extra object in queue.");
            
        }
        
        if(!wrList.isEmpty()) {
            return fail("Not all references was enqueued");
        }
        log.info("test where System.gc() was invoked every 8th time passed.");
        ///////////////////////////
        
        rq = new ReferenceQueue();
        wrList = new ArrayList(NUM);
        
        for(int i=0; i<NUM; ++i) {
            Object obj = new String("The string" +(i+1));
            WeakReference wr = new WeakReference(obj, rq);
            obj = null;
            wrList.add(wr);
        }

          
        for(int j=0; j<NUM; ++j) {
            ((WeakReference)wrList.get(j)).enqueue();
            if(j== NUM/2) {
                System.gc();
                   try {
                     Thread.sleep(20);
                }
                   catch(InterruptedException e) { }
            }
        }
        
        
        for(int k=0; k<NUM; ++k) {
            Reference ref = rq.poll();
            if(ref == null) {
                return fail("queue is empty after deletion of " + k + " elements");
            }
            
            if(!wrList.remove(ref))
                return fail("reference "+ ref+" was enqueued twice or it is extra object in queue.");
            
        }
        
        if(!wrList.isEmpty()) {
            return fail("Not all references was enqueued");
        }
        
        log.info("test where System.gc() was invoked once for j = NUM/2 passed.");
        //////////////////////////
        return pass();
    }
}

