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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.harmony.test.reliability.share.Test;

/**
 * Goal: find resource leaks or intermittent failures or java.text.DecimalFormat cache problems.
 *       Methods applyPattern(...) and format(...) are mainly tested.
 *
 *        The test does:
 *   1. Reads parameters, which are:
 *        param[0] - number of threads to be run in parallel
 *        param[1] - number of iterations in each thread.
 *
 *   2. Gets all available locales via Locale.getAvailableLocales().
 *   3. Create different patterns for the number 1234.5678.
 *   4. Apply all these patterns for each locale in each thread NUMBER_OF_ITERATION times.
 *   5. Runs System.gc().
 *
 */

public class DecimalFormat_Locales extends Test {

    public static int callSystemGC = 1;

    public static int NUMBER_OF_ITERATIONS = 100;

    public int numThreads = 10;

    public int[] statuses;

    public static void main(String[] args) {
        System.exit(new DecimalFormat_Locales().test(args));
    }

    public int test(String[] params) {
        parseParams(params);

        Thread[] t = new Thread[numThreads];

        statuses = new int[t.length];

        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new DecimalFormatRunner(i, this));
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

class DecimalFormatRunner implements Runnable {
    public int id;
    public DecimalFormat_Locales base;
    

    public DecimalFormatRunner(int id, DecimalFormat_Locales base) {
        this.id = id;
        this.base = base;
    }

    public void run() {
        Locale[] allLocales = Locale.getAvailableLocales();
        String[] Patterns = new String[10];
        Patterns[0] = "###,###.###";
        Patterns[1] = "###.##";
        Patterns[2] = "000000.000";
        Patterns[3] = "$###,###.###";
        Patterns[4] = "\u00A5###,###.###";
        Patterns[5] = "#####%";
        Patterns[6] = "\u00A4###,###.###";
        Patterns[7] = "#####\u2030 ";
        Patterns[8] = "0.###E0";
        Patterns[9] = "'#%'0.###E0";
        
        double value = 1234.5678;
        for(int i = 0; i < allLocales.length; i++){
            for(int k = 0; k < base.NUMBER_OF_ITERATIONS; k++){
                
                NumberFormat nf = NumberFormat.getNumberInstance(allLocales[i]);
                Thread.yield();
                DecimalFormat df = (DecimalFormat)nf;
                Thread.yield();
                for(int j = 0; j < Patterns.length; j++){
                    df.applyPattern(Patterns[j]);
                    Thread.yield();
                    String output = df.format(value);
                    //base.log.add(Patterns[j] + "  " + output + " " + allLocales[i]);
                }
            
                if (base.callSystemGC != 0){
                    System.gc();
                }

            }
        }
        base.statuses[id] = Status.PASS;
    }
}