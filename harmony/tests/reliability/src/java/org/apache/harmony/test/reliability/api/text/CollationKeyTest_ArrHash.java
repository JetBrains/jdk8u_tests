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
import java.util.Locale;
import java.util.Random;

import org.apache.harmony.test.reliability.share.Test;

/**
 * GOAL: find resource leaks (or intermittent failures, or cache problems) or
 * incorrect methods results, connected with use of methods
 * java.text.CollationKey.hashCode() and java.text.CollationKey.toByteArray()
 * 
 * The test does:
 * 
 * 1. Reads parameters, which are: param[0] - number of threads to be run in
 * parallel param[1] - number of iterations in each thread.
 * 
 * 2. Gets array of strings to be compared by method compareTo(...). Some of
 * this strings are obtainted randomly, which returns all chars, for which
 * isJavaIdentifierPart(ch) returns true.
 * 
 * 3. - Gets CollaionKey elements via Collator.getCollationKey(String source)for
 * default Locale and for all available Locales, obtained by Collator.getAvailableLocales(). 
 * - Sort array of strings and checks, that
 *         - - correspondent array, obtained by CollatioKey.toByteArray() have the same order.
 *         - - if some of these arrays are equal, correspondent CollationKeys must be equal and 
 *             their hashcodes also must be equal. 
 * - Runs System.gc()
 */

public class CollationKeyTest_ArrHash extends Test {

    public static int callSystemGC = 1;

    public static int NUMBER_OF_ITERATIONS = 100;

    public int numThreads = 10;

    public int[] statuses;

    public static void main(String[] args) {
        System.exit(new CollationKeyTest_ArrHash().test(args));
    }

    public int test(String[] params) {
        parseParams(params);
        Thread[] t = new Thread[numThreads];
        statuses = new int[t.length];

        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new CollationKey_ArrHashRun(i, this));
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

class CollationKey_ArrHashRun implements Runnable {
    public int id;

    public CollationKeyTest_ArrHash base;

    public CollationKey_ArrHashRun(int id, CollationKeyTest_ArrHash base) {
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

        Locale[] allLocals = Collator.getAvailableLocales();
        //base.log.add("Number of available locales is" + allLocals.length);

        for (int k = 0; k < CollationKeyTest_ArrHash.NUMBER_OF_ITERATIONS; k++) {

            WordsToCompare[7] = "which" + StringCr.getRandomString((char) 20);
            WordsToCompare[8] = "which" + StringCr.getRandomString((char) 10);

            Collator myCollator = Collator.getInstance();
            // base.log.add("Default locale" +
            // (Locale.getDefault()).toString());
            CollationKey[] mykeys = new CollationKey[WordsToCompare.length];

            for (int i = 0; i < WordsToCompare.length; i++) {
                mykeys[i] = myCollator.getCollationKey(WordsToCompare[i]);
            }

            sort(mykeys);
            Thread.yield();
            check_ArrHash(mykeys, Locale.getDefault());
            Thread.yield();

            CollationKey[] _keys = new CollationKey[WordsToCompare.length];
            for (int j = 0; j < allLocals.length; j++) {
                Collator _myCollator = Collator.getInstance(allLocals[j]);
                // base.log.add("Used locale" + (allLocals[j]).toString());
                for (int i = 0; i < WordsToCompare.length; i++) {
                    _keys[i] = _myCollator.getCollationKey(WordsToCompare[i]);
                }
                sort(_keys);
                Thread.yield();
                check_ArrHash(_keys, allLocals[j]);
                Thread.yield();
            }
            if (base.callSystemGC != 0) {
                System.gc();
            }
        }
        
        if(base.statuses[id] != Status.FAIL) base.statuses[id] = Status.PASS;
    }

    private void check_ArrHash(CollationKey[] SomeKeys, Locale L) {
        byte[][] SomeBytes = new byte[SomeKeys.length][];

        for (int i = 0; i < SomeKeys.length; i++) {
            SomeBytes[i] = new byte[SomeKeys[i].toByteArray().length];
        }

        for (int i = 0; i < SomeKeys.length; i++) {
            for (int j = 0; j < SomeKeys[i].toByteArray().length; j++) {
                SomeBytes[i][j] = SomeKeys[i].toByteArray()[j];
            }
        }

        // for (int i = 0; i < SomeBytes.length; i++) {
        // print(SomeBytes[i]);
        // }

        for (int k = 0; k < SomeBytes.length - 1; k++) {
            int fkey, skey;
            int compared = 0;

            int i = 0;
            do {
                fkey = SomeBytes[k][i] & 0xFF;
                skey = SomeBytes[k + 1][i] & 0xFF;
                if (fkey < skey) {
                    compared = -1;
                    //base.log.add("Used Locale " + L + " TRUE "
                    //        + SomeKeys[k].getSourceString() + " "
                    //        + SomeKeys[k + 1].getSourceString());
                }
                if (skey < fkey) {
                    base.log.add("String \"" + SomeKeys[k].getSourceString()
                        + "\" with number " + k
                        + " is greater than string \""
                        + SomeKeys[k + 1].getSourceString()
                        + "\" with number " + (k + 1));
                    compared = 1;
                    print(SomeBytes[k]);
                    print(SomeBytes[k + 1]);
                    base.log.add("Used Locale " + L + " FALSE "
                        + SomeKeys[k].getSourceString() + " "
                        + SomeKeys[k + 1].getSourceString());
                    base.statuses[id] = Status.FAIL;
                }
                i++;
            } while ((i != (SomeBytes[k].length - 1))
                && (i != (SomeBytes[k + 1].length - 1)) && (compared == 0));

            if (compared == 0) {
                int HC = SomeKeys[k].hashCode();
                int HC1 = SomeKeys[k + 1].hashCode();
                if (HC != HC1) {
                    base.log.add("Method CollationKey.hashCode gives"
                        + " different results for equal keys:"
                        + SomeKeys[k].toString() + " "
                        + SomeKeys[k + 1].toString());
                    print(SomeBytes[k]);
                    print(SomeBytes[k + 1]);

                    base.statuses[id] = Status.FAIL;
                }
                if (!(SomeKeys[k].equals(SomeKeys[k + 1]))) {
                    base.log.add("Strings, obtained by CollationKey.toByteArray() are equal,"
                        + " but CollationKey.equals returned false");
                    print(SomeBytes[k]);
                    print(SomeBytes[k + 1]);
                    base.statuses[id] = Status.FAIL;
                }
            }
        }
    }

    private void print(byte[] bs) {
        String toPrint = "";
        for (int j = 0; j < bs.length; j++) {
            toPrint = toPrint + " " + bs[j];
        }
        base.log.add(toPrint);

    }

    private void sort(CollationKey[] _keys) {

        boolean bool = false;
        while (bool == false) {
            bool = true;
            for (int i = 0; i < _keys.length - 1; i++) {
                if (_keys[i].compareTo(_keys[i + 1]) > 0) {
                    CollationKey CK = _keys[i];
                    _keys[i] = _keys[i + 1];
                    _keys[i + 1] = CK;
                    bool = false;
                }
            }
        }

        // for (int i = 0; i < _keys.length; i++) {
        // base.log.add(_keys[i].getSourceString());
        // }
    }
}

