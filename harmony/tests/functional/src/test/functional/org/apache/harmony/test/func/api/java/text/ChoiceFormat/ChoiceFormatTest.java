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

package org.apache.harmony.test.func.api.java.text.ChoiceFormat;

import java.text.ChoiceFormat;

import org.apache.harmony.test.func.api.java.text.share.framework.TextTestFramework;

/**
 */
public class ChoiceFormatTest extends TextTestFramework {

    public String testDoubleAdjacent(double d1, double d2) {
        if (Double.isInfinite(d1) && Double.isInfinite(d2)
                || Double.isNaN(d1) && Double.isNaN(d2))
            return "ok";
        if (d2 <= d1) {
            return "fail";
        }
        double delta = d2 - d1;
        if (delta == 0)
            return "fail";
        delta /= 2;
        double dm = d1 + delta;
        double dp = d2 - delta;
        if (d1 < dm && dm < d2 || d1 < dp && dp < d2) {
            return delta + "fail";
        } else {
            return "ok";
        }
    }

    public boolean testNextDouble() throws Throwable {
        double[] doubles = { 0, Double.NaN, 1, -1, 1.5, -1.5,
                Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.MAX_VALUE, Double.MAX_VALUE / 2, Double.MAX_VALUE / -2,
                Double.MIN_VALUE, -Double.MAX_VALUE, -Double.MIN_VALUE };

        for (int i = 0; i < doubles.length; i++) {
            double next = ChoiceFormat.nextDouble(doubles[i]);
            double prev = ChoiceFormat.nextDouble(doubles[i], false);
            double next2 = ChoiceFormat.nextDouble(doubles[i], true);

            if (!"ok".equals(testDoubleAdjacent(doubles[i], next))) {
                throw new Throwable("ChoiceFormat.nextDouble(" + doubles[i]
                        + ") returns incorrect value (" + next + ")");
            }
            if (!"ok".equals(testDoubleAdjacent(prev, doubles[i]))) {
                throw new Throwable("ChoiceFormat.nextDouble(" + doubles[i]
                        + ", false) returns incorrect value (" + prev + ")");
            }
            if ((!Double.isNaN(next) || !Double.isNaN(next2)) && next != next2) {
                throw new Throwable("ChoiceFormat.nextDouble(" + doubles[i]
                        + ", true) returns incorrect value\n" + next + "!=" +
                        next2);
            }

        }
        return true;
    }

    public java.lang.String [] getValues() {
        java.lang.String [] values = { "is negative", "is negative",
                "is zero or fraction ", "is zero or fraction ", "is one ",
                "is 1+ ", "is two ", "is more than 2.", "is more than 2.",
                "is negative" };
        return values;
    }

    public int test() {
        try {

            String pattern = "-1#is negative| 0#is zero or fraction | 1#is one "
                    + "|1.0<is 1+ |2#is two |2<is more than 2.";
            
            ChoiceFormat f1 = new ChoiceFormat(pattern);
            testFormat(f1, pattern);
            
            ChoiceFormat f2 = new ChoiceFormat("");
            f2.applyPattern(pattern);
            testFormat(f2, pattern);
            
        } catch (Throwable e) {
            return fail(e.getMessage());
        }

        return pass();

    }

    private void testFormat(ChoiceFormat f, String pattern) throws Throwable {

        double[] numbers = { Double.NEGATIVE_INFINITY, -1.0, 0, 0.9, 1, 1.5, 2,
                2.1, Double.POSITIVE_INFINITY};

        //Framework fw = new Framework();

        String[] values = getValues();
        //Object [] values = new Object[numbers.length];
        testNextDouble();
        for (int i = 0; i < numbers.length; i++) {
            String format = f.format(numbers[i]);
            if (!values[i].equals(format)) {
                throw new Throwable("ChoiceFormat.format(" + numbers[i]
                        + ") ==" + format + " != " + values[i] + "\nPattern: '" + pattern + "'");
            }
        }
        //System.out.println(fw
        //        .generateGetter("values", values, String.class));

    }

    public static void main(String[] args) {
        System.exit(new ChoiceFormatTest().test());
    }
}