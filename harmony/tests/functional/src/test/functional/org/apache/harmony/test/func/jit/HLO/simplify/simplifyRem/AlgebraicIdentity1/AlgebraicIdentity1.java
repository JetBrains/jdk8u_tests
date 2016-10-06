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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyRem.AlgebraicIdentity1;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 26.07.2006
 */

public class AlgebraicIdentity1 extends MultiCase {

    byte b = Byte.MIN_VALUE;
    short s = -100;
    int i = 2;
    long l = Long.MAX_VALUE;
    char c = 1;
        
    float f1 = Float.NaN;
    float f2 = Float.MAX_VALUE;
    float f3 = Float.MIN_VALUE;
    float f4 = Float.NEGATIVE_INFINITY;
    float f5 = -Float.MAX_VALUE;
        
    double d1 = Double.NaN;
    double d2 = Double.MAX_VALUE;
    double d3 = Double.MIN_VALUE;
    double d4 = Double.POSITIVE_INFINITY;
    double d5 = -Double.MAX_VALUE;
        
    public static void main(String[] args) {
        log.info("Start AlgebraicIdentity1 test...");
        log.info("Tests s % +-1 -> 0 simplification");
        System.exit((new AlgebraicIdentity1()).test(args));
    }
    
    
  //-----------------------------------------------------//
    
    
    public Result testDouble1() {
        double one = 1;
        double result = d1%one;
        log.info("result = " + result);
        if (Double.isNaN(result)) return passed();
        else return failed("TEST FAILED: result != Double.NaN");
    }
    
    public Result testDouble2() {
        double one = 1;
        double result = d2%one;
        log.info("result = " + result);
        if (Double.compare(result, 0.0) == 0) return passed();
        else return failed("TEST FAILED: result != 0.0");
    }
    
    public Result testDouble3() {
        double one = 1;
        double result = d3%one;
        log.info("result = " + result);
        if (Double.compare(result, Double.MIN_VALUE) == 0) return passed();
        else return failed("TEST FAILED: result != Double.MIN_VALUE");
    }
    
    public Result testDouble4() {
        double one = 1;
        double result = d4%one;
        log.info("result = " + result);
        if (Double.isNaN(result)) return passed();
        else return failed("TEST FAILED: result != Double.NaN");
    }
    
    public Result testDouble5() {
        double one = 1;
        double result = d5%one;
        log.info("result = " + result);
        if (Double.compare(result, -0.0) == 0) return passed();
        else return failed("TEST FAILED: result != -0.0");
    }
    
    
  //-----------------------------------------------------//
    
    
    public Result testFloat1() {
        float one = -1;
        float result = f1%one;
        log.info("result = " + result);
        if (Float.isNaN(result)) return passed();
        else return failed("TEST FAILED: result != Float.NaN");
    }
    
    public Result testFloat2() {
        float one = -1;
        float result = f2%one;
        log.info("result = " + result);
        if (Float.compare(result, 0.0f) == 0) return passed();
        else return failed("TEST FAILED: result != 0.0f");
    }
    
    public Result testFloat3() {
        float one = -1;
        float result = f3%one;
        log.info("result = " + result);
        if (Float.compare(result, Float.MIN_VALUE) == 0) return passed();
        else return failed("TEST FAILED: result != Float.MIN_VALUE");
    }
    
    public Result testFloat4() {
        float one = -1;
        float result = f4%one;
        log.info("result = " + result);
        if (Float.isNaN(result)) return passed();
        else return failed("TEST FAILED: result != Float.NaN");
    }
    
    public Result testFloat5() {
        float one = -1;
        float result = f5%one;
        log.info("result = " + result);
        if (Float.compare(result, -0.0f) == 0) return passed();
        else return failed("TEST FAILED: result != -0.0f");
    }
    
    
  //-----------------------------------------------------//
    
    
    public Result testLong() {
        long one = 1;
        long result = l%one;
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testInt() {
        int one = 1;
        int result = i%one;
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testShort() {
        short one = 1;
        short result = (short) (s%one);
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testByte() {
        byte one = 1;
        byte result = (byte) (b%one);
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testChar() {
        char one = 1;
        char result = (char) (c%one);
        log.info("result = " + (int) result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testNegLong() {
        long one = -1;
        long result = l%one;
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testNegInt() {
        int one = -1;
        int result = i%one;
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testNegShort() {
        short one = -1;
        short result = (short) (s%one);
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testNegByte() {
        byte one = -1;
        byte result = (byte) (b%one);
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result testNegChar() {
        int one = -1;
        char result = (char) (c%one);
        log.info("result = " + (int) result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
}
