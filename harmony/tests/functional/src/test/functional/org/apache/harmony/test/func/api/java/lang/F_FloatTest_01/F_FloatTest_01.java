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
 * Created on 24.11.2004
 * Last modification G.Seryakova
 * Last modified on 24.11.2004
 * 
 * Check correctness of types contvertations for Float.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_FloatTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Check correctness of types contvertations for Float.
 * 
 */
public class F_FloatTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_FloatTest_01().test(args));
    }

    public int test() {
        float flt = 1.0E25F;
        Float flObj1, flObj2;

        flObj1 = new Float(flt);
        flt = flObj1.floatValue();
        flObj2 = Float.valueOf(Float.toString(flt));

        if (!flObj1.equals(flObj2) || Float.isInfinite(flt) || Float.isNaN(flt)) {
            return fail("Fail for float");
        }

        flt = Float.NaN;

        flObj1 = new Float(flt);
        flt = flObj1.floatValue();
        flObj2 = Float.valueOf(Float.toString(flt));

        if (!flObj1.equals(flObj2) || Float.isInfinite(flt)
            || !Float.isNaN(flt)) {
            return fail("Fail for NaN");
        }

        flt = Float.POSITIVE_INFINITY;

        flObj1 = new Float(flt);
        flt = flObj1.floatValue();
        flObj2 = Float.valueOf(Float.toString(flt));

        if (!flObj1.equals(flObj2) || !Float.isInfinite(flt)
            || Float.isNaN(flt)) {
            return fail("Fail for infinity");
        }

        return pass();
    }
}