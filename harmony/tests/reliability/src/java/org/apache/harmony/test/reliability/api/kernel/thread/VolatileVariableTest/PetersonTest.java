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
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.reliability.api.kernel.thread.VolatileVariableTest;
import org.apache.harmony.test.reliability.share.Test;

/**
 *  Goal: check that VM supports volatile variables
 *  
 *  The test implements well-known concurrent programming algorithm 
 *  for mutual exclusion by G.Peterson.
 *  (http://en.wikipedia.org/wiki/Peterson%27s_algorithm)
 *
 *  The test does:
 *     1. Reads parameter, which is:
 *            param[0] - number of iterations to run critical region in each thread
 *     2. Starts and, then, joins all started threads
 *
 *     3. Checks that in each thread all checks PASSed.
 *
 *     4. Each thread, being started:
 *         a. Runs param[0] iterations in a cycle, on each iteration:
 *         b. Checks the flag
 *         c. Changes flag when receive control
 *         d. Runs critical regoin of code
 *
 */


public class PetersonTest extends Test {
    public int NUMBER_OF_ITERATIONS = 100;
    public int[] statuses = {PStatus.PASS, PStatus.PASS};
    volatile public long commonVar = 0;
    volatile public static int finished;
    
    public static void main(String[] args) {
        System.exit(new PetersonTest().test(args));
    }

    public int test(String[] params) {    
        parseParams(params);

        Petersonthread th0 = new Petersonthread(0, this);
        Petersonthread th1 = new Petersonthread(1, this);
        finished = 2;
        th0.start();
        th1.start();
        while(finished > 0) {
            Thread.yield();    
        };
        
        // For each thread check whether operations PASSed in the thread
        for (int i = 0; i < statuses.length; ++i){
            if (statuses[i] != PStatus.PASS){
                return fail("Status of thread " + i + ": is FAIL");
            }
            // log.add("Status of thread " + i + ": is PASS");
        }
        return pass("OK");

    }


    public void parseParams(String[] params) {
        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }                                                   
    }

}


class Petersonthread extends Thread {

    volatile static int[] flags = { 0, 0 };
    volatile static int worker; // working thread number

    static long[] vars = {0x00000000FFFFFFFFL, 0xFFFFFFFF00000000L};
    private int threadNum;
    private int threadIteration;
    private PetersonTest base;

    public Petersonthread(int num, PetersonTest test) {
        super();
        threadNum = num;
        threadIteration = test.NUMBER_OF_ITERATIONS;
        base = test;
    }

    /*
     * check and close (if posible) semaphore before critical region of code
     */
    public void regionIn(int current) {
        flags[current] = 1;
        int j = 1 - current;
        worker = j;
        while ((flags[j] == 1) && worker == j) {}
    }

    /* open semaphore after critical region of code */
    public void regionOut(int current) {
        flags[current] = 0;
    }

    public void run() {
        // base.log.add("Thread #" + threadNum + " started " + threadIteration);

        while (threadIteration-- > 0) {
            regionIn(threadNum);

            // critical region
            base.commonVar = vars[threadNum];
            Thread.yield(); //to give a chance to another thread

            if (base.commonVar != vars[threadNum]) {
                base.statuses[threadNum] = PStatus.FAIL;
                return;
            }

            // end of critical region
            regionOut(threadNum);
        }
        worker = 1 - threadNum;
        // base.log.add("Thread #" + threadNum + " finished ");
        PetersonTest.finished--;
    }
}


class PStatus {
    public static final int FAIL = -10;
    public static final int PASS = 10;
}



