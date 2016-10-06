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
 * Created on 30.08.2005
 * 
 * This tests if WeakReference object is weaker than SoftReference.
 * 1.Create WeakReference and SoftReference.
 * 2.Invoked gc().
 * 3.WeakReference should be cleared but SoftReference sould not.
 * 
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_ReferenceTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.ref.*;

/**
 * This tests if WeakReference object is weaker than SoftReference.
 * 1.Create WeakReference and SoftReference.
 * 2.Invoked gc().
 * 3.WeakReference should be cleared but SoftReference sould not.
 */
public class F_ReferenceTest_03 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ReferenceTest_03().test(args));
    }

    public int test() {
        
        Object obj_soft = new String("The string related to soft reference");
        Object obj_weak = new String("The string related to weak reference");     

        SoftReference sr = new SoftReference(obj_soft);
        WeakReference wr = new WeakReference(obj_weak);

        obj_soft = null;
        obj_weak = null;

        
        System.gc();
        try{
            Thread.sleep(200);
        }
        catch(InterruptedException e) {return fail("InterruptedException thrown");}

        if(wr.get()==null) {
           if(sr.get()==null) {
                   return fail("soft reference has the same \"weakness\" as weak reference");
               
             }
             else {
               return pass();
             }
        }
        
        return fail("weak reference is not cleared");

    }
}

