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
 * @author Olessia Salmina
 */

package org.apache.harmony.test.reliability.api.text;


import org.apache.harmony.test.reliability.share.Test;
import java.util.*;
import java.text.*;


/**
 * Goal: find resource leaks or intermittent failures or java.text.RuleBasedCollator cache problems.
 *       Test creates RuleBasedCollator, using the set of rules, which is obtained as an integration
 *     of rules for available locales.
 *     
 *     The test does:
 *     1. Reads parameters, which are:
 *        param[0] - number of threads to be run in parallel
 *        param[1] - number of iterations in each thread.
 *
 *     2. Gets all available locales via Locale.getAvailableLocales().
 *     3. Creates RuleBasedCollator for next in turn locale via 
 *          (RuleBasedCollator) Collator.getInstance(Locale ...)
 *     4. Get rule for this RuleBasedCollator using method getRules().
 *     5. Add obtained rules to finish RuleBasedCollator rules set.
 *     6. Runs System.gc().
 */

public class getAvailableLocals_RBC extends Test{    

    public static int callSystemGC = 1;
    public static int NUMBER_OF_ITERATIONS = 100;
    public int numThreads = 10;
    public int[] statuses;

    public static void main(String[] args) {
        System.exit(new getAvailableLocals_RBC().test(args));
    }
    
    public int test(String[] params) {
        parseParams(params);

        Thread[] t = new Thread[numThreads];

        statuses = new int[t.length];

        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new RuleBasedCollatorRun(i, this));
            t[i].start();
            //log.add("Thread " + i + " started");
        }

        // Correctly wait for all threads to finish

        for (int i = 0; i < t.length; ++i) {
            try {
                t[i].join();
                //log.add("Thread " + i + ": joined() ");

            } catch (InterruptedException ie) {
                return fail("interruptedException while join() of thread #" + i);
            }
        }

        // For each thread check whether operations/checks PASSed in the thread

        for (int i = 0; i < statuses.length; ++i) {
            if (statuses[i] != Status.PASS) {
                return fail("thread #" + i + " returned not PASS status");
            }
            //log.add("Status of thread " + i + ": is PASS");
        }

        return pass("OK");
    }
    
    public void parseParams(String[] params) {
        if (params.length >= 1) {
            numThreads = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[1]);
        }
    }

}


class RuleBasedCollatorRun implements Runnable {
    public int id;
    public getAvailableLocals_RBC base;
    
    public RuleBasedCollatorRun(int id, getAvailableLocals_RBC base) {
        
        this.id = id;
        this.base = base;
    }

    public void run() {        
        for (int k = 0; k < getAvailableLocals_RBC.NUMBER_OF_ITERATIONS; k++) {
            //base.log.add("Iteration number "+k);
            Locale[] allLocales = Locale.getAvailableLocales();
            Thread.yield();

            RuleBasedCollator finishRBC = (RuleBasedCollator) Collator
                .getInstance();
            
            //base.log.add("Default Locale "+(Locale.getDefault()).toString());
            Thread.yield();
            
            for (int i = 0; i < allLocales.length; i++) {
                RuleBasedCollator someRBC = (RuleBasedCollator) Collator
                    .getInstance(allLocales[i]);
                Thread.yield();
                
                //base.log.add("Added rules for Locale "+(allLocales[i]).toString());
                Thread.yield();
                
                String finishRBC_Rules = finishRBC.getRules();
                Thread.yield();

                String someRBC_Rules = someRBC.getRules();
                Thread.yield();

                if((i % 8) == 0){
                    try {
                        finishRBC = new RuleBasedCollator(finishRBC_Rules
                            + someRBC_Rules);
                    } catch (ParseException e) {
                        //    e.printStackTrace();
                    }
                }
            }
            if (getAvailableLocals_RBC.callSystemGC != 0){
                System.gc();
            }
        }
        base.statuses[id] = Status.PASS;
    }
}