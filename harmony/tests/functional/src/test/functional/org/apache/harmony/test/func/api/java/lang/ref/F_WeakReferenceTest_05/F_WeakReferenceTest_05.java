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
 * Created on 09.11.2005
 * 
 * This tests WeakReference object and ReferenceQueue.
 * 1.Create Object. Put into WeakReference.
 * 2 Create array of WeakReferences, put each element into next element as referent.
 * 3.Invoked gc().
 * 4. Check if the 1st element is enqueued.
 * 5. Clear whole array except last element.
 * 6. Invoke gc(). Check if at least one reference object is enqueued.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_WeakReferenceTest_05;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.ref.*;

/**
 * This tests WeakReference object and ReferenceQueue.
 * 1.Create Object. Put into WeakReference.
 * 2 Create array of WeakReferences, put each element into next element as referent.
 * 3.Invoked gc().
 * 4. Check if the 1st element is enqueued.
 * 5. Clear whole array except last element.
 * 6. Invoke gc(). Check if at least one reference object is enqueued.
 */
public class F_WeakReferenceTest_05 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_WeakReferenceTest_05().test(args));
    }

    private static final int NUM = 1000;
    private WeakReference last = null;
    public int test() {
        
         WeakReference [] sr = new WeakReference[NUM];
         Object obj = new String("The string");
         ReferenceQueue rq = new ReferenceQueue(); 
         for(int i=0; i<NUM;++i) {
            sr[i] = new WeakReference(obj, rq);
            obj = sr[i];
            Thread.yield();
         }
         
         obj = null;
         
         int j=0;
         Reference ref = null;
         for(; j < 100; ++j) {
             System.gc();
            try {
                Thread.sleep(20);
            }
            catch(InterruptedException e) {
                 return fail("interrupted excetipion thrown");
            }
             ref = rq.poll();
             if(ref!= null) {
                 if(ref == sr[0]) {  
                     break;
                 }
                 else {
                     return fail("Wrong reference object was enqueued");
                 }
             }
         }
         if(ref==null) {
             return fail("None reference object was enqueued");
          }
     
         last = sr[NUM-1]; 
         sr = null;
         ref = null;
         for(j=0; j < 100; ++j) {
             System.gc();
            try {
                Thread.sleep(20);
            }
            catch(InterruptedException e) {
                 return fail("interrupted excetipion thrown");
            }
             ref = rq.poll();
             if(ref!= null) {
                 break;
             }
         }
         if(ref==null) {
             return fail("None reference object was enqueued");
          }
                  
        return pass();
    }
}

