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
 * This test emulate work with pantom reference object.
 * 1.Object is created
 * 2.Garbage Collector is invoked by run System.gc() until pantom reference object is enqueued
 * 3.Check that the object was enqueued and objects finalizer was executed.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_PhantomReferenceTest_01;

import java.lang.ref.*;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * This test emulate work with pantom reference object. 1.Object is created
 * 2.Garbage Collector is invoked by run System.gc() until pantom reference
 * object is enqueued 3.Check that the object was enqueued and objects finalizer
 * was executed.
 * 
 */
public class F_PhantomReferenceTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_PhantomReferenceTest_01().test(args));
    }

    class PhantomObject {
        private String     name;
        PhantomStatusCheck checker;

        PhantomObject(String name, PhantomStatusCheck checker) {
            this.name = name;
            this.checker = checker;
        }

        public void finalize() {
            checker.SetFinalizerStatus();
        }
    }

    class PhantomStatusCheck {

        private int isFinalized;

        PhantomStatusCheck() {
            isFinalized = 0;
        }

        public void SetFinalizerStatus() {
            isFinalized = 1;
        }

        public int getFinalizedStatus() {
            return isFinalized;
        }
    }

    public int test() {
        PhantomStatusCheck statChecker = new PhantomStatusCheck();
        PhantomObject prObj = new PhantomObject(new String(
            "This is test object name"), statChecker);
        ReferenceQueue rq = new ReferenceQueue();

        PhantomReference pr = new PhantomReference(prObj, rq);
        prObj = null;
        if (pr.get() != null) {
            pr.clear();
            return fail("Method PhantomReference.get() doesn't return null");
        }

        Reference ref;
        int count = 0;
        try {
            while (!pr.isEnqueued() && count < 50) {
                System.gc();
                count++;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            return fail("The process was interrupted");
        }

        boolean isEnq = pr.isEnqueued();
        if ((ref = rq.poll()) == null) {
            pr.clear();
            if (!isEnq) {
                return fail("Phantom reference didn't be enqueued");
            } else {
                return fail("Phantom reference didn't be enqueued but isEnqueued is true");
            }
        } else if (!isEnq) {
            pr.clear();
            return fail("Phantom reference was enqueued but isEnqueued is false");
        }
               
        
        if (statChecker.getFinalizedStatus() == 0) {
            pr.clear();
            return fail("Finalizer didn't be executed");
        }

        statChecker = null;
        pr.clear();
        pr = null;
        return pass();
    }
}