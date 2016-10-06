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
package org.apache.harmony.test.func.reg.vm.btest7321;

import org.apache.harmony.test.share.reg.RegressionTest;
import java.util.logging.Logger;

/**
 */
public class Btest7321 extends RegressionTest {
    public int test(Logger logger, String[] args) {

        // create finalizable object
        new Finalizator();

        // run gc and wait fianalization (it's shouldn't hang up VM)
        System.gc();
        System.runFinalization();
        
        // check pass criteria
        synchronized (Finalizator.LockObj) {
            ThreadGroup mainThreadGroup = Thread.currentThread().getThreadGroup();
        
            while (Finalizator.finalizerThreadGroup == null) {
                try {
                    Finalizator.LockObj.wait();
                } catch (InterruptedException ex) {
                    // do nothing
                }
            }
            
            if (Finalizator.finalizerThreadGroup.equals(mainThreadGroup)) {
                return fail();
            } else {
                return pass();
            }
        }
    }
    
    // to run test from console
    public static void main(String[] args) {
        System.exit(new Btest7321().test(Logger.global, args));
    }
}

class Finalizator {
    // Lock for save change of PASS criteria
    static Object LockObj = new Object();
    
    // finalizer thread group
    static ThreadGroup finalizerThreadGroup = null;
     
    protected void finalize() throws Throwable {
        synchronized (LockObj) {
            finalizerThreadGroup = Thread.currentThread().getThreadGroup();
            LockObj.notify();
        }
    }
}