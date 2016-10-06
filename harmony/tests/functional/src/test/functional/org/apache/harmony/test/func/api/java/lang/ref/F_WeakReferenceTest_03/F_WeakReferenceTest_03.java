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
 * Created on 14.01.2005
 * Last modification G.Seryakova
 * Last modified on 14.01.2005
 * 
 * This test WeakReference object.
 * 1.Objects was created in threads.
 * 2.Invoked method remove(long timeout) of ReferenceQueue.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.ref.F_WeakReferenceTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.ref.*;

/**
 * This test PhantomReference object.
 * 1.Objects was created in threads. 
 * 2.Invoked method remove() of ReferenceQueue.
 * 
 */
public class F_WeakReferenceTest_03 extends ScenarioTest {
    private ReferenceQueue rq = new ReferenceQueue();

    public static void main(String[] args) {
        System.exit(new F_WeakReferenceTest_03().test(args));
    }

    class myThread extends Thread {
        public void run() {
            int N = 1000000;
            WeakReference wr[] = new WeakReference[N];
            Reference ref = null;

            for (int i = 0; i < N; i++) {
                WeakObject wrObj = new WeakObject(new String(
                    "This is test object name"));
                wr[i] = new WeakReference(wrObj, rq);
                wrObj = null;
            }
        }
    }

    public int test() {
        Thread myObjects[] = new myThread[10];
        try {
            for (int i = 0; i < 10; i++) {
                myObjects[i] = new myThread();
                myObjects[i].start();
            }
            Reference rf = rq.remove(600000);
            if (rf == null) {
                return fail("No objects was enqueued");
            } else {
                for (int i = 0; i < 10; i++) {
                    myObjects[i].interrupt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail("test failed");
        }

        return pass();
    }
}

class WeakObject {
    private static int cnt = 0;
    public String      name;

    WeakObject(String name) {
        this.name = name;
        cnt++;
    }
}
