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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyAdd.Reassociation.FloatingPoint;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 07.07.2006
 */

public class FloatingPoint extends MultiCase {
        
        float varFloat1 = -1*(float) Math.pow(10, 35);
        float varFloat2 = -100f;
        float varFloat5 = 1111.999999f;
        float varFloat7 = Float.MAX_VALUE;
        
        double varDouble3 = Double.MAX_VALUE/10;
        double varDouble4 = 100;
        double varDouble6 = 2;
        double varDouble8 = -1;
        
        /* Testing reassociation of the following expressions:
           c1 + (c2 + s) -> (c1 + c2) + s
           c1 + (s + c2) -> (c1 + c2) + s
           c1 + (c2 - s) -> (c1 + c2) - s
           c1 + (s - c2) -> (c1 - c2) + s 
           (c1 + s) + c2 -> (c1 + c2) + s 
           (s + c1) + c2 -> s + (c1 + c2)
           (c1 - s) + c2 -> (c1 + c2) - s
           (s - c1) + c2 -> s + (-c1 + c2) */
        
        public static void main(String[] args) {
            log.info("Start FloatingPoint test...");
            System.exit((new FloatingPoint()).test(args));
        }
        
        public Result test1() {
            log.info("Test1 simplifying c1 + (c2 + s) -> (c1+c2) + s :");
            final float constFloat1 = Float.MAX_VALUE;
            final float constFloat2 = 100f;
            float result =  constFloat1 + (constFloat2 + varFloat1);
            log.info("result = " + result);
            float check = Float.MAX_VALUE - (float)Math.pow(10, 35) + 100f;
            if (result == check) return passed();
            else return failed("TEST FAILED: result != " + check);
        }
        
        public Result test2() {
            log.info("Test2 simplifying  c1 + (s + c2) -> (c1 + c2) + s :");
            final float constFloat1 = Float.MAX_VALUE;
            final float constFloat2 = 100;
            float result =  constFloat1 + (varFloat2 + constFloat2);
            log.info("result = " + result);
            if (result == Float.MAX_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Float.MAX_VALUE);
        }
        
        public Result test3() {
            log.info("Test3 simplifying c1 + (c2 - s) -> (c1 + c2) - s :");
            final double constDouble1 = Double.MAX_VALUE/10;
            final double constDouble2 = Double.MAX_VALUE;
            double result =  constDouble1 + (constDouble2 - varDouble3);
            log.info("result = " + result);
            if (result == Double.MAX_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Double.MAX_VALUE);
        }
        
        public Result test4() {
            log.info("Test4 simplifying c1 + (s - c2) -> (c1-c3) + s :");
            final double constDouble1 = -101;
            final double constDouble2 = Double.MAX_VALUE;
            double result = constDouble1 + (varDouble4 - constDouble2);
            log.info("result = " + result);
            double check = -(Double.MAX_VALUE+1);
            if (result == check) return passed();
            else return failed("TEST FAILED: result != " + check);
        }
        
        public Result test5() {
            log.info("Test5 simplifying (c1 + s) + c2 -> (c1 + c2) + s :");
            final float constFloat1 = Float.MIN_VALUE;
            final float constFloat2 = -1000f;
            float result =  (constFloat1 + varFloat5) + constFloat2;
            log.info("result = " + result);
            float check = Float.MIN_VALUE + 111.999999f;
            if (result == check) return passed();
            else return failed("TEST FAILED: result != " + check);
        }
        
        public Result test6() {
            log.info("Test6 simplifying (s + c1) + c2 -> s + (c1 + c2) :");
            final double constDouble1 = Double.MIN_VALUE;
            final double constDouble2 = -1;
            double result =  (varDouble6 + constDouble1) + constDouble2;
            log.info("result = " + result);
            double check = Double.MIN_VALUE+1;
            if (result == check) return passed();
            else return failed("TEST FAILED: result != " + check);
        }
        
        public Result test7() {
            log.info("Test7 simplifying (c1 - s) + c2 -> (c1 + c2) - s :");
            final float constFloat1 = Float.MAX_VALUE;
            final float constFloat2 = -1f;
            float result =  (constFloat1 - varFloat7) + constFloat2;
            log.info("result = " + result);
            if (result == -1f) return passed();
            else return failed("TEST FAILED: result != " + -1f);
        }
        
        public Result test8() {
            log.info("Test8 simplifying (s - c1) + c2 -> s + (-c1 + c2) :");
            final double constDouble1 = -1;
            final double constDouble2 = -Double.MAX_VALUE;
            double result =  (varDouble8 - constDouble1) + constDouble2;
            log.info("result = " + result);
            if (result == -Double.MAX_VALUE) return passed();
            else return failed("TEST FAILED: result != " + -Double.MAX_VALUE);
        }
}
