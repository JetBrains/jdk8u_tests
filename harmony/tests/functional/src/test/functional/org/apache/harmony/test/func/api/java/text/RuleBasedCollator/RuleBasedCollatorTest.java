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
/** 
 */  

package org.apache.harmony.test.func.api.java.text.RuleBasedCollator;

import java.text.CollationKey;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.Test;

/**
 */
public class RuleBasedCollatorTest extends Test {

    private static void comparatorSort(Collator c, String[] data) {

        for (int i = 0; i < data.length - 1; i++)
            for (int j = 0; j < data.length - 1; j++)
                if (c.compare(data[j], data[j + 1]) > 0) {
                    String t = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = t;
                }
    }

    private static void comparableSort(Comparable[] data) {
        for (int i = 0; i < data.length - 1; i++)
            for (int j = 0; j < data.length - 1; j++)
                if (data[j].compareTo(data[j + 1]) > 0) {
                    Comparable t = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = t;
                }
    }

    private static void keySort(Collator c, String[] data) {
        CollationKey[] keys = new CollationKey[data.length];
        for (int i = 0; i < keys.length; i++)
            keys[i] = c.getCollationKey(data[i]);
        comparableSort(keys);
        for (int i = 0; i < data.length; i++)
            data[i] = keys[i].getSourceString();
    }

    private static String[][] getSequences(String[] ref) {
        int i = ref.length;
        int n = 1;
        while (i > 1)
            n *= i--;
        String[][] result = new String[n][];
        boolean[] free = new boolean[ref.length];

        for (i = 0; i < free.length; i++)
            free[i] = true;

        recSeq(result, ref, new int[ref.length], free, 0, 0);

        return result;
    }

    private static String getRules(String[] ref) {
        String result = "";
        for (int i = 0; i < ref.length; i++)
            result += "<" + ref[i];
        return result;
    }

    private static int recSeq(String[][] res, String[] src, int[] used,
            boolean[] free, int level, int current) {
        if (level >= src.length) {
            res[current] = new String[src.length];
            for (int i = 0; i < src.length; i++)
                res[current][i] = src[used[i]];
            return current + 1;
        } else {
            for (int i = 0; i < free.length; i++)
                if (free[i]) {
                    used[level] = i;
                    free[i] = false;
                    current = recSeq(res, src, used, free, level + 1, current);
                    free[i] = true;
                }
            return current;
        }
    }

    private interface ISorter {
        public void Sort(Collator c, String[] data);
    }

    private int doOnePass(ISorter sorter, boolean testClone) throws ParseException {
        String[] ref = { "k", "l", "ll", "m" };
        String[][] arr = getSequences(ref);
        String rules = getRules(ref);

        RuleBasedCollator coll = new RuleBasedCollator(rules);

        if (testClone)
            coll = (RuleBasedCollator) coll.clone();

        if (coll.getRules() != rules)
            return fail("RuleBasedCollator.getRules() != rules");

        for (int i = 0; i < arr.length; i++) {
            sorter.Sort(coll, arr[i]);
            boolean failure = false;
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] != ref[j])
                    failure = true;
            }
            if (failure) {
                String result = "";
                String reference = "";
                for (int j = 0; j < arr[i].length; j++) {
                    reference += "'" + ref[j] + "', ";
                    result += "'" + arr[i][j] + "', ";
                }
                
                   return fail("Expected order: " + reference +
                       "\nResult: " + result);
            }
        }
        return pass();
    }

    public int test() {

        try {
            if (doOnePass(new ISorter() {
                public void Sort(Collator c, String[] data) {
                    comparatorSort(c, data);
                }
            }, false) != Result.PASS) {
                return fail("RuleBasedCollator.compare() return incorrect value");
            }
            if (doOnePass(new ISorter() {
                public void Sort(Collator c, String[] data) {
                    keySort(c, data);
                }
            }, false) != Result.PASS) {
                return fail("RuleBasedCollator.getCollationKey().compareTo() "
                        + "return incorrect value");
            }
            if (doOnePass(new ISorter() {
                public void Sort(Collator c, String[] data) {
                    comparatorSort(c, data);
                }
            }, true) != Result.PASS) {
                return fail("RuleBasedCollator.clone().compare() "
                        + "return incorrect value");
            }

            if (doOnePass(new ISorter() {
                public void Sort(Collator c, String[] data) {
                    keySort(c, data);
                }
            }, true) != Result.PASS) {
                return fail("RuleBasedCollator.clone().getCollationKey().compareTo() "
                        + "return incorrect value");
            }
            return pass();

        } catch (Throwable e) {
            e.printStackTrace();
            return fail(e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.exit(new RuleBasedCollatorTest().test());
    }
}