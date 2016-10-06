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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyMul.FoldZero;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 20.07.2006
 */

public class FoldZero extends MultiCase {
        
        float f1 = Float.NaN;
        float f2 = Float.MAX_VALUE;
        float f3 = Float.NEGATIVE_INFINITY;
        float f4 = -0.0f;
        
        double d1 = Double.NaN;
        double d2 = Double.MIN_VALUE;
        double d3 = Double.POSITIVE_INFINITY;
        double d4 = Double.NEGATIVE_INFINITY;
        
        int i = 0;
        long l = 1;
        
        /* 
         * 0 * s2 -> 0
         * s1 * 0 -> 0
         */
    
        public static void main(String[] args) {
            log.info("Start FoldZero test...");
            System.exit((new FoldZero()).test(args));
        }
        
        public Result test1() {
            float result = f1*0;
            log.info("result = " + result);
            if (Float.compare(result, Float.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != Float.NaN");
        }
        
        public Result test2() {
            float result = 0*f2;
            log.info("result = " + result);
            if (Float.compare(result, 0.0f) == 0) return passed();
            else return failed("TEST FAILED: result != 0.0f");
        }
        
        public Result test3() {
            float result = f3*0;
            log.info("result = " + result);
            if (Float.compare(result, Float.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != Float.NaN");
        }
        
        public Result test4() {
            double result = 0*d1;
            log.info("result = " + result);
            if (Double.compare(result, Double.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != Double.NaN");
        }
        
        public Result test5() {
            double result = 0*d2;
            log.info("result = " + result);
            if (Double.compare(result, 0.0) == 0) return passed();
            else return failed("TEST FAILED: result != 0.0d");
        }
        
        public Result test6() {
            double result = d3*0;
            log.info("result = " + result);
            if (Double.compare(result, Double.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != Double.NaN");
        }
        
        public Result test7() {
            double result = (l/d4)*0L;
            log.info("result = " + result);
            if (Double.compare(result, -0.0d) == 0) return passed();
            else return failed("TEST FAILED: result != -0.0d");
        }
        
        public Result test8() {
            float result = 0*(f3/i);
            log.info("result = " + result);
            if (Double.compare(result, Float.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != Float.NaN");
        }
        
        public Result test9() {
            double result = l*-0.0;
            log.info("result = " + result);
            if (Double.compare(result, -0.0d) == 0) return passed();
            else return failed("TEST FAILED: result != -0.0d");
        }
        
        public Result test10() {
            float result = f4*-0.0f;
            log.info("result = " + result);
            if (Double.compare(result, 0.0f) == 0) return passed();
            else return failed("TEST FAILED: result != 0.0f");
        }
}

