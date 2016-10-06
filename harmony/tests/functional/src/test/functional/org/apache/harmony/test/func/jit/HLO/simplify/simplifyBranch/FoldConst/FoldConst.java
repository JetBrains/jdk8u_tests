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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyBranch.FoldConst;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 18.07.2006
 */

public class FoldConst extends MultiCase {
    
    public static void main(String[] args) {
        log.info("Start FoldConst test ...");
        System.exit((new FoldConst()).test(args));
    }
    
    long varLong = 3000000000000000000L;
    int varInt = 1000000000;
    char varChar = 5000;
    short varShort = -1;
    byte varByte = 100;
    
    /*
     * s-c1 == c2 -> s == c2+c1
     * s+c1 == c2 -> s == c2-c1
     * c1-s == c2 -> -s == c2+c1
     * c1+s == c2 -> s == c2-c1 
     */
    
    public Result test1() {
        final long const1 = 1000;
        final long const2 = 2999999999999999000L;
        boolean flag = false;
        if (varLong - const1 == const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test2() {
        final int const1 = 2;
        final int const2 = 1000000002;
        boolean flag = false;
        if (varInt + const1 == const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test3() {
        final char const1 = 8000;
        final char const2 = 3000;
        boolean flag = false;
        if (const1 - varChar == const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test4() {
        final short const1 = 3;
        final short const2 = 2;
        boolean flag = false;
        if (const1 + varShort == const2) {
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
    
    public Result test5() {
        final byte const1 = 99;
        final byte const2 = 2;
        boolean flag = false;
        if (varByte - const1 < const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test6() {
        final byte const1 = 26;
        final byte const2 = 127;
        boolean flag = false;
        if (varByte + const1 < const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test7() {
        final int const1 = 20000000;
        final int const2 = -90000000;
        boolean flag = false;
        if (const1 - varInt < const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test8() {
        final int const1 = 2;
        final int const2 = 2;
        boolean flag = false;
        if (const1 + varInt > const2) {
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
        final char const1 = 2000;
        final char const2 = 2999; 
        boolean flag = false;
        if (varChar - const1 > const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test10() {
        final long const1 = 2;
        final long const2 = 3000000000000000001L;
        boolean flag = false;
        if (varLong + const1 > const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test11() {
        final int const1 = 2000000001;
        final int const2 = 1000000000;
        boolean flag = false;
        if (const1 - varInt > const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test12() {
        final short const1 = 0;
        final short const2 = -2;
        boolean flag = false;
        if (const1 + varShort > const2) {
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
        final char const1 = 10000;
        final char const2 = 5000; 
        boolean flag = false;
        if (const1 >= const2 - varChar) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test14() {
        final int const1 = Integer.MAX_VALUE;
        final int const2 = 1;
        boolean flag = false;
        if (const1 >= const2 + varInt) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test15() {
        final int const1 = 2000000000;
        final int const2 = 10;
        boolean flag = false;
        if (const1 >= varInt - const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test16() {
        final byte const1 = 127;
        final byte const2 = 27;
        boolean flag = false;
        if (const1 >= varByte + const2) {
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
        final short const1 = Short.MIN_VALUE+2;
        final short const2 = 2;
        boolean flag = false;
        if (const1 <= const2 - varShort) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test18() {
        final int const1 = Integer.MAX_VALUE;
        final int const2 = 1147483647;
        boolean flag = false;
        if (const1 <= const2 + varInt) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test19() {
        final int const1 = -2000000000;
        final int const2 = 100;
        boolean flag = false;
        if (const1 <= varInt - const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    public Result test20() {
        final byte const1 = 50;
        final byte const2 = 50;
        boolean flag = false;
        if (const1 <= varByte + const2) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
}


