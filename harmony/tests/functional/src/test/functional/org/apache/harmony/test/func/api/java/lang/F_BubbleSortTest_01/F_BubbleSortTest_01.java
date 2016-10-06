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
 * This test check methods abs, max, min, round for java.lang.Math.
 * 
 * The sorting algorithm used in this test is derived from definition 
 * of bubble sort in 
 * Dictionary of Algorithms and Data Structures [online], Paul E. Black, ed., 
 * U.S. National Institute of Standards and Technology. 
 * Available from: http://www.nist.gov/dads/HTML/bubblesort.html
 */
package org.apache.harmony.test.func.api.java.lang.F_BubbleSortTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * This test check methods abs, max, min, round for java.lang.Math.
 * 
 */
public class F_BubbleSortTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_BubbleSortTest_01().test(args));
    }

    public int test() {

        if (sortDouble() != Result.PASS) {
            return fail("");
        }
        if (sortLong() != Result.PASS) {
            return fail("");
        }
        if (sortFloat() != Result.PASS) {
            return fail("");
        }

        return pass();
    }

    private int sortFloat() {
        float f1, f2;
        float fla[] = new float[100];

        for (int i = 0; i < 100; i++) {
            fla[i] = (float)(Math.random() * 1000);
            if (i % 2 == 0) {
                fla[i] = -fla[i];
            }
        }

        for (int i = 0; i < 99; i++) {
            for (int j = 99; j > i; j--) {
                f1 = fla[j];
                f2 = fla[j - 1];
                fla[j] = Math.max(f1, f2);
                fla[j - 1] = Math.min(f1, f2);
            }
        }

        for (int i = 1; i < 100; i++) {
            if (fla[i - 1] > fla[i]) {
                return fail("sortFloat sorting failed");
            }
        }

        return Result.PASS;
    }

    private int sortLong() {
        long lga[] = new long[100];
        long lg1, lg2;

        for (int i = 0; i < 100; i++) {
            lga[i] = Math.round(Math.random() * 1000);
            if (i % 2 == 0) {
                lga[i] = -lga[i];
            }
        }

        for (int i = 0; i < 99; i++) {
            for (int j = 99; j > i; j--) {
                lg1 = lga[j];
                lg2 = lga[j - 1];
                lga[j] = Math.max(Math.abs(lg1), Math.abs(lg2));
                lga[j - 1] = Math.min(Math.abs(lg1), Math.abs(lg2));
            }
        }

        for (int i = 1; i < 100; i++) {
            if (lga[i - 1] < 0) {
                lg1 = -lga[i - 1];
            } else {
                lg1 = lga[i - 1];
            }
            if (lga[i] < 0) {
                lg2 = -lga[i];
            } else {
                lg2 = lga[i];
            }
            if (lg1 > lg2) {
                return fail("sortLong failed with abs()");
            }
        }

        for (int i = 0; i < 99; i++) {
            for (int j = 99; j > i; j--) {
                lg1 = lga[j];
                lg2 = lga[j - 1];
                lga[j] = Math.max(lg1, lg2);
                lga[j - 1] = Math.min(lg1, lg2);
            }
        }

        for (int i = 1; i < 100; i++) {
            if (lga[i - 1] > lga[i]) {
                return fail("sortLong failed without abs()");
            }
        }

        return Result.PASS;
    }

    private int sortDouble() {
        double db1;
        double db2;
        double dba[] = new double[100];
        
        for (int i = 0; i < 100; i++) {
            dba[i] = Math.random() * 1000;
            if (i % 2 == 0) {
                dba[i] = -dba[i];
            }
        }

        for (int i = 0; i < 99; i++) {
            for (int j = 99; j > i; j--) {
                db1 = dba[j];
                db2 = dba[j - 1];
                dba[j] = Math.max(db1, db2);
                dba[j - 1] = Math.min(db1, db2);
            }
        }

        for (int i = 1; i < 100; i++) {
            if (dba[i - 1] > dba[i]) {
                return fail("sortFloat failed");
            }
        }

        return Result.PASS;
    }
}