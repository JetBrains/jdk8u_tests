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
 * @version $Revision: 1.1 $
 */
package org.apache.harmony.test.reliability.api.kernel.thread.Calculation;
import org.apache.harmony.test.reliability.share.Test;

/**
 * Goal: check that VM works correctly with nested thread.
 *       Each set of nested threads calculate factorial fucntion.
 * The test does:
 *     1. Reads parameters, which are:
 *        param[0] - number of task which calculate factorial
 *        param[1] - argument factorial fucntion.
 *        (result is value of long, so there is no point 
 *        in seting param[1] more then 20 since 21! is overflow long)               
 *     2. Starts param[0] threads, which starts the next one and so on ...
 *     3. Each thread (except the last), being started:
 *         a. Calculates intermediate result
 *         b. Starts next thread and pass calculated results and auxilary variables to it                 
 *         d. Finishes 
 *     4. Last thread save results into array    
 *
 */

public class CalcTest extends Test {
    int depth = 20;
    volatile int started = 0;
    volatile int tasks = 20;    
    volatile Object lock = new Object();
    long[] results;
    long sample;

    public static void main(String[] args) {
        System.exit(new CalcTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
        started = 0;
        sample = 1L;
        results = new long[tasks];
        
        for (int i = 0; i < depth;) {
            sample *= ++i;
        }               
          
        // create param[0] tasks
        for (int i = 0; i < tasks; i++) {
            CreateTask(i, depth, 1L, lock);
        }

        // wait for finish
        while (started > 0) {}
        
        // check result
        for (int i = 0; i < tasks; i++) {
            if (results[i] != sample) {
                log.add("Thread #" + i + " got wrong result " + results[i]
                    + " instead of " + sample);
                return fail("Test failed");
            }
        }

        return pass("OK");
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            tasks = Integer.parseInt(params[0]);
            if (params.length >= 2) {
                depth = Integer.parseInt(params[1]);
            }
        }
    }


    synchronized protected void CreateTask( int resultindex,  int iteration, long value, Object lock) {        
        CalcTask task = new CalcTask(this, resultindex, iteration, value, lock);
    }
}

class CalcTask extends Thread {
    CalcTest parent;
    int iteration;
    long value;
    Object lock;
    int resultindex;
    
    public CalcTask ( CalcTest parent, int resultindex, int iteration, long val, Object lock) {
        this.parent = parent;
        this.iteration = iteration;
        this.value = val;
        this.lock = lock;
        this.resultindex = resultindex;
        setDaemon(true);
        setPriority(Thread.NORM_PRIORITY);
        synchronized (lock) {
            parent.started++;
        }    
        start();
    }

    public void run() {
                 
        if (iteration > 0) {
            value *= iteration--;
            parent.CreateTask(resultindex, iteration, value, lock);
        } else {
            //CalcTest.log.add("value= " + value);
            parent.results[resultindex] = value; 
        }
        synchronized (lock) {
            parent.started--;
        }   
    }
}

