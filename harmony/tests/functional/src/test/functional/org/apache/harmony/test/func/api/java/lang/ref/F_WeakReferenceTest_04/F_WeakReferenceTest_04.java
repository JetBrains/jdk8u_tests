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
 * Created on 16.08.2005
 * 
 * This tesst WeakReference object.
 * 1.Create Object. Put into WeakReference.
 * 2.Invoked gc().
 * 3. Check if WeakReference.get() returns null.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_WeakReferenceTest_04;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.ref.*;

/**
 * This tests WeakReference object.
 * 1.Create Object. Put into WeakReference by invoking ctor with 1 argument.
 * 2.Invoked gc().
 * 3. Check if WeakReference.get() returns null.
 */
public class F_WeakReferenceTest_04 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_WeakReferenceTest_04().test(args));
    }

    private static final int NUM = 100;

    public int test() {
        Object obj = new String("The string");
        WeakReference wr = new WeakReference(obj);
        obj = null;
        if(!"The string".equals(wr.get())) {
             return fail("FAILED: no referent");
        }
        for(int i=0; i<NUM;++i) {
           System.gc();
           try {
                 Thread.sleep(20);
           }
           catch(InterruptedException e) {
                return fail("interrupted excetipion thrown");
           }
           if(wr.get()==null) {
              return pass();
           }
        }
        

        return fail("Weak reference was not cleared after " + NUM + " invokations of System.gc()");
    }
}

