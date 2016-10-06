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
 * @author osalmina
 */

package org.apache.harmony.test.reliability.api.text;


import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Locale;
import java.util.Random;

import org.apache.harmony.test.reliability.share.Test;

/**
 * GOAL: find resource leaks (or intermittent failures, or cache problems) or
 * incorrect methods results, connected with use of methods
 * java.text.CollationKey.compareTo(...) and java.text.CollationKey.equals(...)
 * 
 * The test does: 
 * 
 * 1. Reads parameters, which are: 
 * param[0] - number of threads to be run in parallel 
 * param[1] - number of iterations in each thread.
 * 
 * 2. Gets array of strings to be compared by method compareTo(...). Some of
 * this strings are obtainted randomly, which returns all chars, for which
 * isJavaIdentifierPart(ch) returns true.
 * 
 * 3. In each iteration: 
 * - Gets CollaionKey elements via Collator.getCollationKey(String source)and
 * RuleBasedCollator.getCollationKey(String source). 
 * - Sort array of strings and
 * compare come CollationKey elements by method Collationkey.equals. 
 * - Runs System.gc()
 */

public class CollationKeyTest extends Test {

    public static int callSystemGC = 1;

    public static int NUMBER_OF_ITERATIONS = 100;

    public int numThreads = 10;

    public int[] statuses;

    public static void main(String[] args) {
        System.exit(new CollationKeyTest().test(args));
    }

    public int test(String[] params) {
        parseParams(params);
        Thread[] t = new Thread[numThreads];
        statuses = new int[t.length];

        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new CollationKeyRun(i, this));
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

class CollationKeyRun implements Runnable {
    public int id;

    public CollationKeyTest base;

    public CollationKeyRun(int id, CollationKeyTest base) {
        this.id = id;
        this.base = base;
    }

    public void run() {
        String[] WordsToCompare = new String[9];

        String eWithCircumflex = new String("\u00EA");
        String eWithAcute = new String("\u00E9");
        String ash = new String("\u00E6");

        WordsToCompare[0] = "p" + eWithAcute + "ch" + eWithAcute;
        WordsToCompare[1] = "p" + eWithCircumflex + "ch" + ash;
        WordsToCompare[2] = "p" + ash + "che";
        WordsToCompare[3] = "peche";
        WordsToCompare[4] = "Peche";
        WordsToCompare[5] = "which";
        WordsToCompare[6] = "which";
        for (int k = 0; k < CollationKeyTest.NUMBER_OF_ITERATIONS; k++) {

            WordsToCompare[7] = "which" + StringCr.getRandomString((char) 20);
            WordsToCompare[8] = "which" + StringCr.getRandomString((char) 10);

            Collator myCollator = Collator.getInstance();
            // base.log.add("Default locale" +
            // (Locale.getDefault()).toString());
            CollationKey[] mykeys = new CollationKey[WordsToCompare.length];

            for (int i = 0; i < WordsToCompare.length; i++) {
                mykeys[i] = myCollator.getCollationKey(WordsToCompare[i]);
            }

            for (int i = 0; i < mykeys.length - 1; i++) {
                if (mykeys[i].compareTo(mykeys[i + 1])
                    * mykeys[i + 1].compareTo(mykeys[i]) > 0) {
                    base.statuses[id] = Status.FAIL;
                    base.log.add("Thread "+ id
                        + ": CollationKey.compareTo(...) return incorrect result when used for"
                        + "Strings \""
                        + mykeys[i].getSourceString() + "\" and "
                        + "\"" + mykeys[i + 1].getSourceString() + "\"");
                }
            }

            sort(mykeys);

            for (int i = 0; i < mykeys.length; i++)
                if (!mykeys[i].equals(mykeys[i])) {
                    base.statuses[id] = Status.FAIL;
                    base.log.add("Thread "+ id
                        + ": CollationKey.equals(...) return incorrect result when used to compare"
                        + "CollationKey element with itself. String \""
                        + mykeys[i].getSourceString() + "\", "
                        + "array number " + i);
                    return;
                }

            RuleBasedCollator RCollator = (RuleBasedCollator) RuleBasedCollator
                .getInstance();
            CollationKey[] Rkeys = new CollationKey[mykeys.length];

            for (int i = 0; i < WordsToCompare.length; i++) {
                Rkeys[i] = RCollator.getCollationKey(mykeys[i]
                    .getSourceString());
            }

            for (int i = 0; i < Rkeys.length; i++)
                if (!Rkeys[i].equals(mykeys[i])) {
                    base.statuses[id] = Status.FAIL;
                    base.log.add("Thread "+ id
                        + ": CollationKey.equals(...) return incorrect result when used to compare equal "
                        + "keys, obtained differently.  First string: \""
                        + Rkeys[i].getSourceString() + "\", "+"array number " + i
                        + ". Second string: \""
                        + mykeys[i].getSourceString() + "\", "+ "array namber. " + i
                        + "Iteration number " + k);
                    return;
                }

            Locale[] allLocals = Collator.getAvailableLocales();
            CollationKey[] _keys = new CollationKey[WordsToCompare.length];

            for (int j = 0; j < allLocals.length; j++) {
                Collator _myCollator = Collator.getInstance(allLocals[j]);
                // base.log.add("Used locale" + (allLocals[j]).toString());
                for (int i = 0; i < WordsToCompare.length; i++) {
                    _keys[i] = _myCollator.getCollationKey(WordsToCompare[i]);
                }
                sort(_keys);

                Random rand = new Random();
                int FIntToComp = rand.nextInt(_keys.length);
                int SIntToComp = rand.nextInt(_keys.length);
                boolean ColKeyEquals = _keys[FIntToComp]
                    .equals(mykeys[SIntToComp]);
                if (ColKeyEquals) {
                    if (_keys[FIntToComp].compareTo(mykeys[SIntToComp]) != 0) {
                        base.statuses[id] = Status.FAIL;
                        base.log.add("Thread "+ id
                            + ":CollationKey.compareTo returned not 0 for equal keys "
                            + _keys[FIntToComp].toString()
                            + " (array number " + FIntToComp
                            + ", string \""
                            + _keys[FIntToComp].getSourceString()
                            + "\") and "
                            + mykeys[SIntToComp].toString()
                            + " (array number " + SIntToComp
                            + ", string \""
                            + mykeys[SIntToComp].getSourceString()
                            + "\") is " + ColKeyEquals);
                    }
                }
            }

            if (CollationKeyTest.callSystemGC != 0) {
                System.gc();
            }
        }
        base.statuses[id] = Status.PASS;
    }

    private void sort(CollationKey[] _keys) {
        boolean bool = false;
        while (bool == false) {
            for (int i = 0; i < _keys.length - 1; i++) {
                bool = true;
                if (_keys[i].compareTo(_keys[i + 1]) > 0) {
                    CollationKey CK = _keys[i];
                    _keys[i] = _keys[i + 1];
                    _keys[i + 1] = CK;
                    bool = false;
                }
            }
        }

        for (int i = 0; i < _keys.length; i++) {
            // base.log.add(_keys[i].getSourceString());
        }
    }
}

