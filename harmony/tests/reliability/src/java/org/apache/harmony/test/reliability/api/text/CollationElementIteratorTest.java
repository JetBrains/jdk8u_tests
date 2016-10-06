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

import javax.swing.text.Segment;

/**
 * Goal: find resource leaks or intermittent failures or java.text.CollationElementIterator cache problems.
 *       Test simple invokes all implemented methods of CollationElementIterator in some combination.
 *
 *       The test does:
 *     1. Reads parameters, which are:
 *        param[0] - number of threads to be run in parallel
 *        param[1] - number of iterations in each thread.
 *
 *     2. Gets all available locales via Locale.getAvailableLocales().
 *        Creates two strings of length 50 and 20 (their chars are those 
 *        for which isJavaIdentifierPart(ch) returns true, randomly chosen).    
 *          Use first string to invoke all CollationElementIterator methods for all base locales 
 *        (static fields of Locale class, such as Locale.CANADA).
 *        Use second string to invoke some CollationElementIterator methods for all available locales.
 *    
 *     3.  Runs System.gc()
 *
 */

public class CollationElementIteratorTest extends Test{
    

    public static int callSystemGC = 1;
    public static int NUMBER_OF_ITERATIONS = 100;
    public int numThreads = 10;
    public int[] statuses;

    public static void main(String[] args) {
        System.exit(new CollationElementIteratorTest().test(args));
    }
    
    public int test(String[] params) {
        parseParams(params);

        Thread[] t = new Thread[numThreads];

        statuses = new int[t.length];

        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new ColElemItRun(i, this));
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



class ColElemItRun implements Runnable {
    public int id;
    public CollationElementIteratorTest base;
    
    public ColElemItRun(int id, CollationElementIteratorTest base) {
        this.id = id;
        this.base = base;
    }
    
    public void run() {
        
        Locale[] allLocales = Locale.getAvailableLocales();
        Thread.yield();
        //base.log.add("Number of available locales"+allLocales.length);
        //base.log.add("Number of base locales is "+ baseLocales.Loc.length);
        
        for (int k = 0; k < CollationElementIteratorTest.NUMBER_OF_ITERATIONS; k++) {
            //base.log.add("Iteration number "+k);    
            String testString1 = StringCrC.getRandomString((char)50);
                        
            for (int i = 0; i < baseLocales.Loc.length; i++){            
                Invoke_ColElemIt_Methods(baseLocales.Loc[i],testString1, 0);
            }
            
            String testString2 = StringCrC.getRandomString((char)20);
            for (int i = 0; i < allLocales.length; i++) {
                Invoke_ColElemIt_Methods(allLocales[i],testString2, 1);                            
            } 
            if (CollationElementIteratorTest.callSystemGC != 0){
                System.gc();
            }
        }
        base.statuses[id] = Status.PASS;
    }

    private void Invoke_ColElemIt_Methods(Locale locale, String str, int i) {
        //base.log.add("Locale is "+locale);  
        RuleBasedCollator ruleBasedCollator = (RuleBasedCollator) Collator
            .getInstance(locale);
        Thread.yield();

                
        switch (i) {
            case 0:
                CollationElementIterator collationElementIterator = ruleBasedCollator
                    .getCollationElementIterator(str);
                Thread.yield();


                for (int j = 0; j < str.length(); j++) {
                    int order = collationElementIterator.next();
                    Thread.yield();
                    int prOrder = CollationElementIterator.primaryOrder(order);
                    Thread.yield();
                    int exp = collationElementIterator.getMaxExpansion(order);
                    Thread.yield();
                    int position = collationElementIterator.getOffset();
                    Thread.yield();
                    //base.log.add("Primary Order of key with position " + position
                    //        + " is " + prOrder
                    //        + ". Maximum length of any expansion is " + exp);
                }

                collationElementIterator.reset();
                Thread.yield();

                for (int j = 0; j < str.length(); j++) {
                    int order = collationElementIterator.next();
                    int secOrder = CollationElementIterator.secondaryOrder(order);
                    int exp = collationElementIterator.getMaxExpansion(order);
                    int position = collationElementIterator.getOffset();
                    //base.log.add("Secondary Order of key with position " + position
                    //        + " is " + secOrder
                    //        + ". Maximum length of any expansion is " + exp);
                }

                collationElementIterator.setOffset(0);
                Thread.yield();

                for (int j = 0; j < str.length(); j++) {
                    int order = collationElementIterator.next();
                    int tOrder = CollationElementIterator.tertiaryOrder(order);
                    int exp = collationElementIterator.getMaxExpansion(order);
                    int position = collationElementIterator.getOffset();
                    //base.log.add("Tertiary Order of key with position " + position
                    //        + " is " + tOrder
                    //        + ". Maximum length of any expansion is " + exp);
                }

                break;
            case 1:

                Segment CI = new Segment();
                CollationElementIterator collationElementIterator1 = ruleBasedCollator
                    .getCollationElementIterator(CI);
                Thread.yield();
                collationElementIterator1.setText(str);
            
                collationElementIterator1.next();
                collationElementIterator1.previous();

                for (int j = 0; j < str.length(); j++) {
                    int order = collationElementIterator1.next();
                    int prOrder = CollationElementIterator.primaryOrder(order);
                    int position = collationElementIterator1.getOffset();
                    //base.log.add("Primary Order of key with position " + position
                    //        + " is " + prOrder);
                }
                break;

        }
    }

}

final class baseLocales {
    static final Locale[] Loc = { Locale.CANADA, Locale.CANADA_FRENCH,
                                    Locale.CHINA, Locale.CHINESE, Locale.ENGLISH, Locale.FRANCE,
                                    Locale.FRENCH, Locale.GERMAN, Locale.GERMANY, Locale.ITALIAN,
                                    Locale.ITALY, Locale.JAPAN, Locale.JAPANESE, Locale.KOREA,
                                    Locale.KOREAN, Locale.PRC, Locale.SIMPLIFIED_CHINESE,
                                    Locale.TAIWAN, Locale.TRADITIONAL_CHINESE, Locale.UK, Locale.US };
}

