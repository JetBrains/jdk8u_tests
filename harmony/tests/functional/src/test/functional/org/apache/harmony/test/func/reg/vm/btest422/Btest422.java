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
package org.apache.harmony.test.func.reg.vm.btest422;

import java.util.logging.Logger;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;



import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest422 extends RegressionTest {

    public int isFinalized;

    class PhantomObject {
        private String name;
        Btest422 checker;

        PhantomObject(String name, Btest422 checker) {
            this.name = name;
            this.checker = checker;
        }

        public void finalize() {
            checker.isFinalized = 1;
        }
    }


    public static void main(String[] args) {
        System.exit(new Btest422().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        PhantomObject prObj = new PhantomObject(new String("This is test object name"), this);
        ReferenceQueue rq = new ReferenceQueue();

        PhantomReference pr = new PhantomReference(prObj, rq);
        prObj = null;
        
        int count = 0;
        try {
            while (!pr.isEnqueued() && count < 50) {
                System.gc();
                count++;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println("The process was interrupted");
            return fail();  
        }               
        
        if (isFinalized != 1) {
           System.err.println("Finalizer hasn't been executed.");
           return fail();             
        } else {
            System.err.println("Finalizer has been executed.");
           return pass();             
        }

        //pr.clear();

    }

}


