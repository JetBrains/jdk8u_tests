/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Igor A. Pyankov
 * @version $Revision: 1.2 $
 *
 */
package org.apache.harmony.test.reliability.api.kernel.thread.WeakReferenceandThreadTest;

import org.apache.harmony.test.reliability.share.Test;
import java.lang.ref.WeakReference;

/**
 *   Goal: check that GC correctly works with weak reference variables 
 *   The test does:
 *             1. Reads parameter, which is:
 *                param[0] - number of starting threads
 *                param[1] - number of attemps to call GC
 *                   
 *             2. Starts param[0] threads
 * 
 *             3. Each thread creates obejct, gets weak reference to the object,
 *                clear strong reference to tht object and call GC param[1] times.
 *                Weak reference must be cleaned.
 * 
 *             4. Checks that each thread died and has status PASS.
 *      
 */
public class WeakReferenceandThreadTest extends Test {

    private static int NUMBER_OF_THREADS = 120;
    public static int[] status;
    public static int ATTEMPTS = 10;

    public static volatile boolean started = false;

    public static void main(String[] args) {
        System.exit(new WeakReferenceandThreadTest().test(args));
    }

    public WeakReferenceandThreadTest() {
        super();
    }

    public int test(String[] params) {
        boolean passed = true;
        int numberAlive = NUMBER_OF_THREADS;
        parseParams(params);
        status = new int[NUMBER_OF_THREADS];
        Thread t[] = new Thread[NUMBER_OF_THREADS];

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            t[i] = new RefCreater(i);
            t[i].start();
        }

        while (!started) {}; 

        while (numberAlive > 0) {
            numberAlive = 0;
            for (int k = 0; k < NUMBER_OF_THREADS; k++) {
                if (t[k].isAlive()) {
                    numberAlive++;
                }
                //Thread.yield();
            }
            //log.add("na " + numberAlive);
        }

        for (int k = 0; k < NUMBER_OF_THREADS; k++) {
            if (status[k] != WStatus.PASS) {
                log.add("Status of thread " + k + " is " + status[k]);
                passed = false;
            }
        }

        if (passed) {
            return pass("OK");
        } else {
            return fail("Test failed");
        }
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            NUMBER_OF_THREADS = Integer.parseInt(params[0]);
            if (params.length >= 2) {
                ATTEMPTS = Integer.parseInt(params[1]);
            }
        }
    }

}

class RefCreater extends Thread {
    private int number; // Thread number

    public RefCreater(int number) {
        this.number = number;
    }

    public void run() {      
        // the signal - thread started.
        WeakReferenceandThreadTest.started = true; 
          
        Long obj = new Long(number);
        WeakReference wr = new WeakReference(obj);
        obj = null;

        try {
            if (((Long) wr.get()).intValue() != number) {
                // no referent
                WeakReferenceandThreadTest.status[number] = WStatus.FAIL1;
                return;
            }
        } catch (NullPointerException npe) {
            WeakReferenceandThreadTest.status[number] = WStatus.PASS;
            return;
        }

        for (int i = 0; i < WeakReferenceandThreadTest.ATTEMPTS; i++) {
            System.gc();
            Thread.yield();

            if (wr.get() == null) {
                // // Weak reference cleared
                WeakReferenceandThreadTest.status[number] = WStatus.PASS;
                return;
            }

            // Weak reference was not cleared after number attempts
            WeakReferenceandThreadTest.status[number] = WStatus.FAIL2;
            return;
        }
    }
}


class WStatus {
    public static final int FAIL1 = -1; // no referent created
    public static final int FAIL2 = -2; // weakreference was not cleared
    public static final int PASS = 1;   // ok
}
