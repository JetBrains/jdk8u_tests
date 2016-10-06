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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyBranch.NoSimplify;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 17.07.2006
 */

public class NoSimplify extends MultiCase {
    
    public static void main(String[] args) {
        log.info("Start NoSimplify test ...");
        System.exit((new NoSimplify()).test(args));
    }
    
    /*
     * s-c1 == c2 -> s == c2+c1
     * s+c1 == c2 -> s == c2-c1
     * c1-s == c2 -> -s == c2+c1
     * c1+s == c2 -> s == c2-c1 
     */
    
    float float_1 = 1;
    double double_1 = 1;
    
    public Result test1() {
        final float const1 = Float.POSITIVE_INFINITY;
        final float const2 = -Float.POSITIVE_INFINITY;
        boolean flag = false;
        if (float_1 - const1 == const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test2() {
        final float const1 = Float.POSITIVE_INFINITY;
        final float const2 = Float.POSITIVE_INFINITY;
        boolean flag = false;
        if (float_1 + const1 == const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test3() {
        final double const1 = Double.POSITIVE_INFINITY;
        final double const2 = Double.POSITIVE_INFINITY;
        boolean flag = false;
        if (const1 - double_1 == const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test4() {
        final double const1 = Double.POSITIVE_INFINITY;
        final double const2 = Double.POSITIVE_INFINITY;
        boolean flag = false;
        if (const1 + double_1 == const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
     /* s-c1 < c2 -> s < c2+c1
     * s+c1 < c2 -> s < c2-c1
     * c1-s < c2 -> -s < c2+c1
     * c1+s < c2 -> s < c2-c1
     */
     
    byte byte_0 = 0;
    int int_0 = 0;
    int long_0 = 0;
    
    public Result test5() {
        final byte const1 = Byte.MAX_VALUE;
        final byte const2 = 10;
        boolean flag = false;
        if ((byte)(byte_0 - const1) < const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test6() {
        final byte const1 = -10;//
        final byte const2 = Byte.MAX_VALUE;
        boolean flag = false;
        if ((byte)(byte_0 + const1) < const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test7() {
        final int const1 = -10;//
        final int const2 = Integer.MAX_VALUE;
        boolean flag = false;
        if (const1 - int_0 < const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test8() {
        final int const1 = Integer.MAX_VALUE;
        final int const2 = -10;
        boolean flag = false;
        if (const1 + int_0 > const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    /* s-c1 > c2 -> s > c2+c1
     * s+c1 > c2 -> s > c2-c1
     * c1-s > c2 -> -s > c2+c1
     * c1+s > c2 -> s > c2-c1
     */
    
    public Result test9() {
        final long const1 = -Long.MAX_VALUE;
        final long const2 = -2; 
        boolean flag = false;
        if (long_0 - const1 > const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test10() {
        final long const1 = Long.MAX_VALUE;
        final long const2 = -200;
        boolean flag = false;
        if (long_0 + const1 > const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test11() {
        final int const1 = Integer.MAX_VALUE;
        final int const2 = Integer.MIN_VALUE;
        boolean flag = false;
        if (const1 - int_0 > const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test12() {
        final int const1 = Integer.MAX_VALUE;
        final int const2 = -200;
        boolean flag = false;
        if (const1 + int_0 > const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    /* c1 >= c2-s -> c1-c2 >= -s
     * c1 >= c2+s -> c1-c2 >= s
     * c1 >= s-c2 -> c1+c2 >= s
     * c1 >= s+c2 -> c1-c2 >= s
     */
    
    public Result test13() {
        final long const1 = Long.MAX_VALUE;
        final long const2 = -2; 
        boolean flag = false;
        if (const1 >= const2 - long_0) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test14() {
        final int const1 = Integer.MIN_VALUE-1;
        final int const2 = Integer.MIN_VALUE;
        boolean flag = false;
        if (const1 >= const2 + int_0) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test15() {
        final int const1 = Integer.MAX_VALUE;
        final int const2 = Integer.MIN_VALUE;
        boolean flag = false;
        if (const1 >= int_0 - const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test16() {
        final double const1 = Double.POSITIVE_INFINITY;
        final double const2 = Double.POSITIVE_INFINITY;
        boolean flag = false;
        if (const1 >= int_0 + const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    /* c1 <= c2-s -> c1-c2 <= -s
     * c1 <= c2+s -> c1-c2 <= s
     * c1 <= s-c2 -> c1+c2 <= s
     * c1 <= s+c2 -> c1-c2 <= s
     */
    
    public Result test17() {
        final short const1 = Short.MIN_VALUE;
        final short const2 = 2;
        boolean flag = false;
        if (const1 <= (short)(const2 - long_0)) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test18() {
        final int const1 = Integer.MIN_VALUE;
        final int const2 = Integer.MAX_VALUE;
        boolean flag = false;
        if (const1 <= const2 + int_0) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test19() {
        final int const1 = Integer.MIN_VALUE;
        final int const2 = -1;
        boolean flag = false;
        if (const1 <= int_0 - const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test20() {
        final float const1 = Float.NEGATIVE_INFINITY;
        final float const2 = Float.POSITIVE_INFINITY;
        boolean flag = false;
        if (const1 <= int_0 + const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
}


