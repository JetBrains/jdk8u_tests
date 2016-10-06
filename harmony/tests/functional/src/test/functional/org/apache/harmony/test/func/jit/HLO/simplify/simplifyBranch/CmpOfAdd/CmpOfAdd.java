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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyBranch.CmpOfAdd;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 17.07.2006
 */

public class CmpOfAdd extends MultiCase {
    
    public static void main(String[] args) {
        log.info("Start CmpOfAdd test ...");
        System.exit((new CmpOfAdd()).test(args));
    }
    
    // Test (x >= y+1) -> (x > y)
    
    long x1 = -1000;
    long y1 = -1001;
    
    public Result test1() {
        boolean flag = false;
        if (x1 >= y1+1) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    int x2 = 1;
    int y2 = -1;
    
    public Result test2() {
        boolean flag = false;
        if (x2 >= y2+1) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    // Test (x+1 > y) -> (x >= y)
    
    long x3 = 2;
    long y3 = 1;
    
    public Result test3() {
        boolean flag = false;
        if (x3+1 > y3) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    byte x4 = -128;
    byte y4 = -128;
    
    public Result test4() {
        boolean flag = false;
        if (x4+1 > y4) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    // Test (x > y+(-1)) -> (x >= y)
    
    int x5 = -1000;
    int y5 = -1000;
    
    public Result test5() {
        boolean flag = false;
        final byte b = -1;
        if (x5 > y5+b) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    float x6 = 0.5f;
    float y6 = 0.5f;
    
    public Result test6() {
        boolean flag = false;
        final long l = -1;
        if (x6 > y6+l) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    // Test (x+(-1) >= y) -> (x > y)
    
    int x7 = 1001;
    int y7 = 1000;
    
    public Result test7() {
        boolean flag = false;
        final int i = -1;
        if (x7+i >= y7) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    char x8 = 0;
    char y8 = 0;
    
    public Result test8() {
        boolean flag = false;
        final char c = 1;
        if (x8+c >= y8) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    // Test (x >= 1+y) -> (x > y)
    
    long x9 = 100;
    long y9 = 50;
    
    public Result test9() {
        boolean flag = false;
        if (x9 >= 1+y9) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    short x10 = -1000;
    short y10 = -1001;
    
    public Result test10() {
        boolean flag = false;
        if (x10 >= 1+y10) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    // Test (1+x > y) -> (x >= y)
    
    int x11 = 5000;
    int y11 = 5000;
    
    public Result test11() {
        boolean flag = false;
        if (1+x11 > y11) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    long x12 = 700000000;
    long y12 = 500000000;
    
    public Result test12() {
        boolean flag = false;
        if (1+x12 > y12) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    // Test (x > (-1)+y) -> (x >= y)
    
    long x13 = 300001;
    long y13 = 300001;
    
    public Result test13() {
        boolean flag = false;
        final long l = -1;
        if (x13 > -1+y13) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    // Test ((-1)+x >= y) -> (x > y)
    
    int x14 = 700000000;
    int y14 = 500000000;
    
    public Result test14() {
        boolean flag = false;
        final int i = -1;
        if (i+x14 >= y14) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
}


