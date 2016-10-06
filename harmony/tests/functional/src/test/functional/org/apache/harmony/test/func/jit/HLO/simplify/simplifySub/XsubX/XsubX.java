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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifySub.XsubX;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 13.07.2006
 */

public class XsubX extends MultiCase {
    
        float nan = Float.NaN;
        float pi = Float.POSITIVE_INFINITY;
        float ni = Float.NEGATIVE_INFINITY;
        
        double dnan = Double.NaN;
        double dpi = Double.POSITIVE_INFINITY;
        double dni = Double.NEGATIVE_INFINITY;

        /*  Tests that x - x -> 0 simplification isn't 
         *  performed for floating-point values   
         */
        
        public static void main(String[] args) {
            log.info("Start XsubX test...");
            System.exit((new XsubX()).test(args));
        }
        
        public Result testNaN() {
            float result = nan - nan;
            log.info("result = " + result);
            if (Float.compare(result, Float.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        public Result testPI() {
            float result = pi - pi;
            log.info("result = " + result);
            if (Float.compare(result, Float.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        public Result testNI() {
            float result = ni - ni;
            log.info("result = " + result);
            if (Float.compare(result, Float.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        public Result testDNaN() {
            double result = dnan - dnan;
            log.info("result = " + result);
            if (Double.compare(result, Double.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        public Result testDPI() {
            double result = dpi - dpi;
            log.info("result = " + result);
            if (Double.compare(result, Double.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        public Result testDNI() {
            double result = dni - dni;
            log.info("result = " + result);
            if (Double.compare(result, Double.NaN) == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        /*  Tests that x - x -> 0 simplification is performed correctly */ 
        
        public Result testInt() {
            int result = (int)pi - (int)pi;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        public Result testChar() {
            int result = (char)nan - (char)nan;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        public Result testShort() {
            int result = (int)dni - (int)dni;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
        
        byte b = -128;
        
        public Result testByte() {
            int result = b - b;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != ");
        }
}
