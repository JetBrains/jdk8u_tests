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
 * Created on 14.10.2004
 * Last modification G.Seryakova
 * Last modified on 15.11.2004
 * 
 * This test emulate work with weak reference objects.
 * 1.Objects are created
 * 2.Garbage Collector is invoked by run System.gc() until weak reference object is enqueued
 * 3.Check that all objects were enqueued.
 * 
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_WeakReferenceTest_01;

import java.util.*;
import java.lang.ref.*;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * This test emulate work with weak reference objects. 1.Objects are created
 * 2.Garbage Collector is invoked by run System.gc() until weak reference object
 * is enqueued 3.Check that all objects were enqueued.
 * 
 */
public class F_WeakReferenceTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_WeakReferenceTest_01().test(args));
    }

    public int test() {
        String str;
        Reference ref;
        int num = 0;
        WeakReference wr;
        ReferenceQueue rq = new ReferenceQueue();
        HashMap whm = new HashMap();

        for (int i = 0; i < 100; i++) {
            str = new String("This is test" + i);
            wr = new WeakReference(str, rq);
            whm.put(wr, new Integer(i));

            if (wr.get() != str) {
                return fail("Method WeakReference.get() doesn't return expected object");
            }

            str = null;
            while ((ref = rq.poll()) != null) {
                num++;
                whm.remove(ref);
            }
        }

        System.gc();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            return fail("The process was interrupted");
        }

        while ((ref = rq.poll()) != null) {
            num++;
            whm.remove(ref);
        }

        if (num < 100) {
            return fail("Not all weak reference objects was collected. " + num + " objects from 100 was collected.");
        }

        return pass();
    }
}