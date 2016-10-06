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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyRem.ConstFold;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 26.07.2006
 */

public class ConstFold extends MultiCase {
        
    public static void main(String[] args) {
        log.info("Start ConstFold test...");
        System.exit((new ConstFold()).test(args));
    }    
    
    public Result test1() {
        final short one = Short.MIN_VALUE;
        final short two = 77;
        short three = 27;
        short four = 678;
        short five = 3;
        short result = (short)(one%two%three%four%five);
        log.info("result = " + result);
        if (result == -1) return passed();
        else return failed("TEST FAILED: result != -1");
    }
        
    public Result test2() {
        final float one = Float.MAX_VALUE;
        float two = Float.MIN_VALUE;
        float result = one%two;
        log.info("result = " + result);
        if (Float.compare(result, 0.0f) == 0) return passed();
        else return failed("TEST FAILED: result != 0.0f");
    }
        
    public Result test3() {
        final byte one = 127;
        byte two = 7;
        final byte three = 3;
        byte four = 8;
        byte result = (byte)(one%two%three%four);
        log.info("result = " + result);
        if (result == 1) return passed();
        else return failed("TEST FAILED: result != 1");
    }
    
    public Result test4() {
        double one = -Double.MAX_VALUE;
        double two = -3;
        final double three = 100;
        double result = one%two%three;
        log.info("result = " + result);
        if (Double.compare(result, -2.0) == 0) return passed();
        else return failed("TEST FAILED: result != -2.0");
    }
    
    public Result test5() {
        float one = Float.MIN_VALUE;
        float two = 1.401298464324817f;
        final float three = 1E-10f;
        float result = one%two%three;
        log.info("result = " + result);
        if (Float.compare(result, Float.MIN_VALUE) == 0) return passed();
        else return failed("TEST FAILED: result != " + Float.MIN_VALUE);
    }
    
    public Result test6() {
        final float inf = Float.POSITIVE_INFINITY;
        long l = 200;
        float result = l%inf;
        log.info("result = " + result);
        if (Float.compare(result, 200f) == 0) return passed();
        else return failed("TEST FAILED: result != 200f");
    }
        
    public Result test7() {
        final double inf = Double.NEGATIVE_INFINITY;
        int i = -100;
        double result = i%inf;
        log.info("result = " + result);
        if (Double.compare(result, -100.0) == 0) return passed();
        else return failed("TEST FAILED: result != -100.0");
    }
        
    public Result test8() {
        final long l = Long.MIN_VALUE;
        long one = -1;
        long result = l%one;
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
        
    public Result test9() {
        final short s = Short.MIN_VALUE;
        short one = -1;
        short result = (short)(s%one);
        log.info("result = " + result);
        if (result == 0) return passed();
        else return failed("TEST FAILED: result != 0");
    }
    
    public Result test10() {
        final char one = 65000;
        char two = 7;
        char three = 3;
        char four = 10;
        short result = (short)(one%two%three%four);
        log.info("result = " + result);
        if (result == 2) return passed();
        else return failed("TEST FAILED: result != 2");
    }
        
}
