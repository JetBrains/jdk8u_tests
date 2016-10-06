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
package org.apache.harmony.test.func.reg.vm.btest7128;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest7128_2 extends RegressionTest {
    private static ThreadLocal tl = new MyThreadLocal2();
    public static int flag = 0;

    class MyThread extends Thread {
        public void run() {
            tl.set(null);
            Object o;
            tl.get();
        }
    }

    public int test(Logger logger, String[] args) {
        Thread t = new MyThread();
        t.start();
        try {
            t.join();
        } catch (InterruptedException ie) {
            System.out.println("Thread has been interrupted\n" + ie);
            fail();
        }
        if (flag > 0) {
            System.out.println("FAILED: initialValue is invoked though "
                + "ThreadLocal.set() is called before ThreadLocal.get()");
            return fail();
        } else {
            return pass();
        }
    }
    
    public static void main(String[] args) {
        System.exit(new Btest7128_2().test(Logger.global, args));
    }
}

class MyThreadLocal2 extends ThreadLocal {
    protected synchronized Object initialValue() {
        Btest7128_2.flag++;
        return new Object();
    }
}

