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
 * Created on 28.12.2004
 * Last modification G.Seryakova
 * Last modified on 28.12.2004
 * 
 * This test emulate work with reference objects.
 * 1.Object are created
 * 2.Invoke enqueue() and clear() for emulate GC.
 * 3.Check that object was enqueued.
 * 
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_ReferenceTest_01;

import java.lang.ref.*;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * This test emulate work with reference objects.
 * 1.Object are created
 * 2.Invoke enqueue() and clear() for emulate GC.
 * 3.Check that object was enqueued.
 * 
 */
public class F_ReferenceTest_01 extends ScenarioTest {
    ReferenceQueue rq = new ReferenceQueue();
    
    public static void main(String[] args) {
        System.exit(new F_ReferenceTest_01().test(args));
    }

    public int test() {
        String str = new String("This is test");
        
        if (!Task1(new WeakReference(str, rq), new String("This is test"))) {
            return fail("For WeakReference with queue");
        }
        
        if (!Task2(new WeakReference(str), str)) {
            return fail("For WeakReference without queue");
        }
        
        if (!Task1(new SoftReference(str, rq), str)) {
            return fail("For SoftReference with queue");
        }
        
        if (!Task2(new SoftReference(str), str)) {
            return fail("For SoftReference without queue");
        }
        
        if (!Task1(new PhantomReference(str, rq), str)) {
            return fail("For PhantomReference with queue");
        }
        
        return pass();
    }

    private boolean Task1(Reference refObj, String str) {
        boolean result = true;
        Reference ref; 

        
        if (!str.equals(refObj.get()) && !PhantomReference.class.isInstance(refObj)) {
            fail("Method get() doesn't return expected object");
            return false;
        }

//        emulating of GC
        refObj.clear();
        if (!refObj.enqueue()) {
            fail("Invoking enqueue() return false on first invorking.");
            result = false;;
        }
        
        if (refObj.enqueue()) {
            fail("Invoking enqueue() return true on second invorking.");
            result = false;;
        }
        
        if (!refObj.isEnqueued()) {
            fail("After invoking enqueue() and clear() object isn't enqueued.");
            result = false;
        }
        
        ref = rq.poll();
        if (ref == null) {
            fail("poll() doesn't return reference object."); 
            result = false;             
        }
        
        if (ref.get() != null) {
            fail("Reference object wasn't cleared.");
            result = false;
        }
        
        return result;
    }
    
    private boolean Task2(Reference refObj, String str) {
        boolean result = true;

        if (!str.equals(refObj.get())) {
            fail("Method get() doesn't return expected object");
            return false;
        }

//        emulating of GC
        refObj.clear();
        if (refObj.enqueue()) {
            fail("enqueue() return true for reference object was not registered with a queue.");
            result = false;;
        }
        
        if (refObj.isEnqueued()) {
            fail("isEnqueued() return true for reference object was not registered with a queue.");
            result = false;
        }
                
        if (refObj.get() != null) {
            fail("Reference object wasn't cleared.");
            result = false;
        }
        
        return result;
    }
}
