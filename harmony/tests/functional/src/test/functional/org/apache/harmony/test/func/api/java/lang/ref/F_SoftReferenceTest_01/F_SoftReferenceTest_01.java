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
 * This test emulate work with soft reference object.
 * 1.Objects are created
 * 2.Check that at least one object was enqueued.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_SoftReferenceTest_01;

import java.lang.ref.*;
import java.util.*;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * This test emulate work with soft reference object. 1.Objects are created
 * 2.Check that at least one object was enqueued.
 * 
 */
public class F_SoftReferenceTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_SoftReferenceTest_01().test(args));
    }
    static final int NUM = 1000000;
    class SoftReferenceTestObject {
        byte [] body;
        final int dim = 65536;  //64 KB
        SoftReferenceTestObject(int i) {
           body = new byte[dim];  
           Arrays.fill(body, (byte) i);
        }
        
        public boolean equals(Object obj) {
            if (obj instanceof SoftReferenceTestObject) {
                 return (Arrays.equals(body,
                             ((SoftReferenceTestObject)obj).body));
            } else {
                 return false;
            }
        }
    }

    public int test() {
        Reference ref;
        SoftReferenceTestObject testObject;
        long time = 0;
        int num;
        ReferenceQueue rq = new ReferenceQueue();
        SoftReference[] srArr = new SoftReference[NUM];

        for (int i = 0; i < NUM; i++) {
            testObject = new SoftReferenceTestObject(i);
            srArr[i] = new SoftReference(testObject, rq);
            if (!testObject.equals(srArr[i].get())) {
                return fail("Method SoftReference.get() doesn't return expected object");
            }
            testObject = null;
            if (((ref = rq.poll()) != null)) {
                log.info(""+i);
                return pass();
            }
        }

        num = 0;
        while (((ref = rq.poll()) == null) && (num++) < 50) {
            System.gc();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return fail("The process was interrupted");
            }
        }

        if (ref == null) {
            return fail("Object was not collected");
        }

        return pass();
    }
}