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
 * Created on 22.11.2004
 * Last modification G.Seryakova
 * Last modified on 24.11.2004
 * 
 * Check correctness of types contvertations for Double.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_DoubleTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Check correctness of types contvertations for Double.
 * 
 */
public class F_DoubleTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_DoubleTest_01().test(args));
    }

    public int test() {
        double dbl = 1.0E25D;
        Double dbObj1, dbObj2;

        dbObj1 = new Double(dbl);
        dbl = dbObj1.doubleValue();
        dbObj2 = Double.valueOf(Double.toString(dbl));

        if (!dbObj1.equals(dbObj2) || Double.isInfinite(dbl)
            || Double.isNaN(dbl)) {
            return fail("Fail for double");
        }

        dbl = Double.NaN;

        dbObj1 = new Double(dbl);
        dbl = dbObj1.doubleValue();
        dbObj2 = Double.valueOf(Double.toString(dbl));

        if (!dbObj1.equals(dbObj2) || Double.isInfinite(dbl)
            || !Double.isNaN(dbl)) {
            return fail("Fail for NaN");
        }

        dbl = Double.POSITIVE_INFINITY;

        dbObj1 = new Double(dbl);
        dbl = dbObj1.doubleValue();
        dbObj2 = Double.valueOf(Double.toString(dbl));

        if (!dbObj1.equals(dbObj2) || !Double.isInfinite(dbl)
            || Double.isNaN(dbl)) {
            return fail("Fail for infinity");
        }

        return pass();
    }
}