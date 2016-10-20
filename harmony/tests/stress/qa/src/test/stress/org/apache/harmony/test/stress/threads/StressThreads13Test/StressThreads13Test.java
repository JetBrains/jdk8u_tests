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
package org.apache.harmony.test.stress.threads.StressThreads13Test;

import org.apache.harmony.share.Test;

/**
 * @author Ilia A. Leviev
 * @version $Revision: 1.2 $
 */

class Test13 extends Thread {
    Lock obj_t, obj_t2;

    static int flag = 0;

    static int count = 0;

    Test13(String name, Lock obj_arg, Lock obj_arg2) {
        super(name);
        obj_t = obj_arg;
        obj_t2 = obj_arg2;
    }

    public void run() {
        try {
            synchronized (obj_t) {
                obj_t.wait();
            }
            int buff = toTest.getField();
            synchronized (obj_t) {

                count++;
                if (buff != 104) {
                    flag++;
                }
                if (count == StressThreads13Test.threadNumber) {
                    synchronized (obj_t2) {
                        obj_t2.notifyAll();
                    }
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

class toTest {
    static long field_t1 = 0;

    static {

        field_t1 = System.currentTimeMillis();
    }

    static int getField() {
        long field_t2 = System.currentTimeMillis();
        if (field_t2 < field_t1) {
            return 105;
        }
        return 104;

    }

}

public class StressThreads13Test extends Test {
    public int test() {
        return (test(testArgs));
    }

    static int threadNumber = 1101;

    public int test(String[] args) {

        Lock obj = new Lock();
        Lock obj2 = new Lock();
        Test13[] a = new Test13[threadNumber];

        for (int j = 0; j < threadNumber; j++) {

            try {
                Test13 p = new Test13("workThread" + j, obj, obj2);
                a[j] = p;
                p.start();
                p.sleep(1);
            } catch (Exception er) {
                er.printStackTrace();
            }
        }
        synchronized (obj) {
            obj.notifyAll();
            System.out.println("notifyAll...");
        }

        synchronized (obj2) {
            try {
                obj2.wait();
            } catch (InterruptedException er) {
                er.printStackTrace();
            }
        }
        if (Test13.flag == 0) {
            return 104;
        }
        return 105;
    }

    public static void main(String[] args) {
        int result = new StressThreads13Test().test(args);
        System.exit(result);
    }

}
