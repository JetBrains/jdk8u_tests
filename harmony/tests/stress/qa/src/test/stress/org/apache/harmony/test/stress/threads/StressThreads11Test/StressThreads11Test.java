/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
package org.apache.harmony.test.stress.threads.StressThreads11Test;

import org.apache.harmony.share.Test;

/**
 * @author Ilia A. Leviev
 * @version $Revision: 1.2 $
 */

class Test11 extends Thread {
    Lock obj_t, obj_t2, obj_t3;

    static int flag_f = 0;

    static int flag_s = 0;

    static int count = 0;

    Test11(String name, Lock obj_arg, Lock obj_arg2, Lock obj_arg3) {
        super(name);
        obj_t = obj_arg;
        obj_t2 = obj_arg2;
        obj_t3 = obj_arg3;
    }

    public void run() {

        try {
            synchronized (obj_t) {
                obj_t.wait();
                obj_t.f++;

                synchronized (obj_t3) {
                    count++;
                    if (obj_t.f > 1) {
                        flag_f++;
                    }
                    if (obj_t.f <= 0) {
                        flag_s--;
                    }
                    if (count == StressThreads11Test.threadNumber) {

                        synchronized (obj_t2) {
                            obj_t2.notifyAll();
                        }
                    }
                    obj_t.f = 0;
                }
            }

        } catch (InterruptedException erd) {
            erd.printStackTrace();
        }

    }
}

class Lock {
    int f = 0;
}

public class StressThreads11Test extends Test {
    public int test() {
        return (test(testArgs));
    }

    static int threadNumber = 1001;

    public int test(String[] args) {

        Lock obj3 = new Lock();
        Lock obj2 = new Lock();
        Test11[] a = new Test11[threadNumber];
        Lock[] arrayLock = new Lock[threadNumber];

        for (int j = 0; j < threadNumber; j++) {

            try {
                arrayLock[j] = new Lock();
                Test11 p = new Test11("workThread" + j, arrayLock[j], obj2,
                        obj3);
                a[j] = p;
                p.start();
                p.sleep(1);
            } catch (Exception er) {
                er.printStackTrace();
            }
        }

        for (int j = 0; j < threadNumber; j++) {
            synchronized (arrayLock[j]) {
                arrayLock[j].notify();
            }
        }
        synchronized (obj2) {
            try {
                obj2.wait();
            } catch (InterruptedException er) {
                er.printStackTrace();
            }
        }

        if ((Test11.flag_s == 0) && (Test11.flag_f == 0)) {
            return 104;
        }
        return 105;
    }

    public static void main(String[] args) {
        int result = new StressThreads11Test().test(args);
        System.exit(result);
    }

}
