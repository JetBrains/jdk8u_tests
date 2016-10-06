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
 * Created on 13.01.2005
 * Last modification G.Seryakova
 * Last modified on 13.01.2005
 * 
 * This test emulate work with pantom reference object.This is more simple F_PhantomReferenceTest_01.
 * 1.Object is created
 * 2.Garbage Collector is invoked by run System.gc().
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_PhantomReferenceTest_02;

import java.lang.ref.*;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * This test emulate work with pantom reference object.This is more simple
 * F_PhantomReferenceTest_01. 1.Object is created 2.Garbage Collector is invoked
 * by run System.gc().
 * 
 */
public class F_PhantomReferenceTest_02 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_PhantomReferenceTest_02().test(args));
    }

    public int test() {
        String prObj = new String("This is test object name");
        ReferenceQueue rq = new ReferenceQueue();
        PhantomReference pr = new PhantomReference(prObj, rq);
        prObj = null;
        if (pr.get() != null) {
            log.info("Method PhantomReference.get() doesn't return null");
        }
        log.info("0");
        Reference ref;
        int count = 0;
        try {
            while (count < 20) {
                count++;
                log.info(""+count);
                System.gc();
                log.info(""+count);
                Thread.sleep(10);
                log.info(""+count);
            }
        } catch (InterruptedException e) {
            log.info("The process was interrupted");
        }
        log.info("3");
        if ((ref = rq.poll()) == null) {
            log.info("Phantom reference didn't be enqueued");
        }
        if (pr.isEnqueued()) {
            log.info("Phantom reference was enqueued");
        }
        log.info("4");
        pr.clear();
        return pass();
    }
}