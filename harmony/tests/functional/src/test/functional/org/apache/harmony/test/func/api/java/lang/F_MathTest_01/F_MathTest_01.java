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
 * Created on 29.10.2004
 * Last modification G.Seryakova
 * Last modified on 24.11.2004
 * 
 * This test check methods abs, ceil, floor, round, max, min, pow for java.lang.Math.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_MathTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * This test check methods abs, ceil, floor, round, max, min, pow for
 * java.lang.Math.
 * 
 */
public class F_MathTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_MathTest_01().test(args));
    }

    public int test() {

        if (testDouble() != Result.PASS) {
            return fail("");
        }
        if (testFloat() != Result.PASS) {
            return fail("");
        }

        return pass();
    }

    private int testFloat() {
        float f1 = 398340000F;
        int rnd = Math.round(f1);

        if ((f1 - rnd) != 0) {
            if (Math.abs(f1 - rnd) > 0.5) {
                return fail("testFloat failed round(float)");
            }
        }

        return Result.PASS;
    }

    private int testDouble() {
        double db1 = 3.569697D;
        double db2 = 6.7D;
        double res, res2, ceil, floor;

        res = Math.pow(db1, db2);
        ceil = Math.ceil(res);
        floor = Math.floor(res);
        res2 = (Math.pow(db1, 3.0D) * Math.pow(db1, db2 - 3.0D));

        if (res != res2) {
            return fail("testDouble failed pow(double, double)");
        }

        if (((res - ceil) != 0) || ((res - floor) != 0)) {
            if (Math.abs(ceil - floor) != 1) {
                return fail("testDouble failed ceil(double) or floor(double)");
            }
        }

        return Result.PASS;
    }

    private int sumGeometricProgression() {
        double start = 2.5D;
        double inc = 3.75D;
        double sum = 0;
        int num = 1;

        while (!Double.isInfinite(sum)) {
            if (sum >= Double.MAX_VALUE) {
                return fail("Double.isInfinite() failed");
            }
            sum = sum + (start * Math.pow(inc, num - 1));
            num++;
        }

        return Result.PASS;
    }

}