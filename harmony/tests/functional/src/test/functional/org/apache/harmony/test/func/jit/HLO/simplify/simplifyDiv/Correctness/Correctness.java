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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyDiv.Correctness;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 07.07.2006
 */

public class Correctness extends MultiCase {
    
    byte b = 32;
    short s = 4;
    int i = -1;
    long l = -7;
    char c = 1;
    float f = 22.22f;
    double d = 333.333;
        
    public static void main(String[] args) {
        log.info("Start Correctness test...");
        System.exit((new Correctness()).test(args));
    }        
        
    /*
     *  Test the following div operation simplification: 
     *  if (c1%c2 == 0), then (s*c1)/c2 -> s*(c1/c2)
     */
    
    public Result test1() {
        final int constInt1 = Integer.MAX_VALUE;
        final int constInt2 = Integer.MAX_VALUE;
        int result = (i*constInt1)/constInt2;
        log.info("result = " + result);
        if (result == -1) return passed();
        else return failed("TEST FAILED: result != " + -1);
    }
        
    public Result test2() {
        final int constByte1 = 4;
        final int constByte2 = 2;
            byte result = (byte) ((constByte1*b)/constByte2);
        log.info("result = " + result);
            if (result == 64) return passed();
            else return failed("TEST FAILED: result != " + 64);
        }
    
        
        public Result test3() {
            final long constLong1 = 1317624576693539401L;
            final long constLong2 = 7;
            long result = (constLong1*l)/constLong2;
            log.info("result = " + result);
            if (result == -1317624576693539401L) return passed();
            else return failed("TEST FAILED: result != " + -1317624576693539401L);
        }
        
        public Result test4() {
            final short constShort1 = -8192;
            final short constShort2 = 8;
            short result = (short)((s*constShort1)/constShort2);
            log.info("result = " + result);
            if (result == -4096) return passed();
            else return failed("TEST FAILED: result != " + -4096);
        }
        
        public Result test5() {
            final char constChar1 = Character.MAX_VALUE;
            final char constChar2 = 5;
            char result = (char)((c*constChar1)/constChar2);
            log.info("result = " + (int) result);
            if (result == 13107) return passed();
            else return failed("TEST FAILED: result != " + 13107);
        }
        
        public Result test6() {
            final float constFloat1 = 5;
            final float constFloat2 = 5;
            float result = (f*constFloat1)/constFloat2;
            log.info("result = " + result);
            if (result == f) return passed();
            else return failed("TEST FAILED: result != " + f);
        }
        
        public Result test7() {
            final double constDouble1 = 500;
            final double constDouble2 = 5;
            double result = (d*constDouble1)/constDouble2;
            log.info("result = " + result);
            if (result == 33333.3) return passed();
            else return failed("TEST FAILED: result != " + 33333.3);
        }
        
        
    /*
     *  Test that div by zero doesn't cause ArithmeticException 
     *  for the floating point values
     */
        
        double dd = Double.NaN;
        float ff = Float.NEGATIVE_INFINITY;
        
        public Result testDivZero1() {
            float zero = 0;
            float result = ff/zero;
            log.info("result = " + result);
            if (result == ff) return passed();
            else return failed("TEST FAILED: result != " + ff);
        }
        
        public Result testDivZero2() {
            int zero = 0;
            double result = dd/zero;
            log.info("result = " + result);
            if (Double.isNaN(result)) return passed();
            else return failed("TEST FAILED: result != " + dd);
        }
        
        public Result testDivZero3() {
            float zero = 0;
            long result = (long) (l/zero);
            log.info("result = " + result);
            if (result == Long.MIN_VALUE) return passed();
            else return failed("TEST FAILED: result != Long.MIN_VALUE");
        }
        
        public Result testDivZero4() {
            double zero = 0;
            double result = i/zero;
            log.info("result = " + result);
            if (result == Float.NEGATIVE_INFINITY) return passed();
            else return failed("TEST FAILED: result != Float.NEGATIVE_INFINITY");
        }
        
}
