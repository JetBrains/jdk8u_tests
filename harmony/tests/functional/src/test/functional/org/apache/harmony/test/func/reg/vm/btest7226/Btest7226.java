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
/**
 */
package org.apache.harmony.test.func.reg.vm.btest7226;

import org.apache.harmony.test.share.reg.RegressionTest;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 */
public class Btest7226 extends RegressionTest {

    final static private int NUM = 1000000;

    public int test(Logger logger, String[] args) {
        ReferenceQueue rq = new ReferenceQueue();
        SoftReference[] srArr = new SoftReference[NUM];

        for (int i = 0; i < NUM; i++) {
            SoftReferenceTestObject testObject = new SoftReferenceTestObject(i);
            srArr[i] = new SoftReference(testObject, rq);

            if (!testObject.equals(srArr[i].get())) {
                System.err.println("Method SoftReference.get() doesn't return expected object");
                return fail();
            }
            Reference ref = rq.poll();

            if ( ref != null ) {                    
                System.err.println("PASSED on i="+i);
                return pass();
            }
        }
        System.err.println("END");
        return fail();
    }

    // to run test from console
    public static void main(String[] args) {
        System.exit(new Btest7226().test(Logger.global, args));
    }

    private class SoftReferenceTestObject {
        private final int dim = 65536;  //64 KB
        private byte[] body;
        
        SoftReferenceTestObject(int i) {
            body = new byte[dim];  
            Arrays.fill(body, (byte) i);
        }

        public boolean equals(Object obj) {
            if (obj instanceof SoftReferenceTestObject) {
                return (Arrays.equals(body, ((SoftReferenceTestObject)obj).body));
            } else {
                return false;
            }
        }
    }
}
