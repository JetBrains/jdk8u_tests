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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyBranch.CmpOfSub;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 17.07.2006
 */

public class CmpOfSub extends MultiCase {
    
    public static void main(String[] args) {
        log.info("Start CmpOfSub test ...");
        System.exit((new CmpOfSub()).test(args));
    }
    
    // Test (x > y-1) -> (x >= y)
    
    long x1 = 2;
    long y1 = 2;
    
    public Result test1() {
        boolean flag = false;
        if (x1 > y1-1) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    int x2 = 2;
    int y2 = 2;
    
    public Result test2() {
        boolean flag = false;
        if (x2 > y2-1) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    short x3 = Short.MAX_VALUE;
    short y3 = 1;
    
    public Result test3() {
        boolean flag = false;
        if (x3 > y3-1) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    double x4 = 0.0000009;
    double y4 = 1;
    
    public Result test4() {
        boolean flag = false;
        if (x4 > y4-1) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    // Test (x-1 >= y) -> (x > y)
    
    int x5 = 10001;
    int y5 = 10000;
    
    public Result test5() {
        boolean flag = false;
        if (x5-1 >= y5) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    byte x6 = 1;
    byte y6 = 0;
    
    public Result test6() {
        boolean flag = false;
        if (x6-1 >= y6) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    long x7 = 1;
    long y7 = 0;
    
    public Result test7() {
        boolean flag = false;
        if (x7-1 >= y7) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    float x8 = 1.5559f;
    float y8 = 0.5559f;
    
    public Result test8() {
        boolean flag = false;
        if (x8-1 >= y8) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
   // Test (x >= y-(-1)) -> (x > y)

    long x9 = 11;
    long y9 = 10;
    
    public Result test9() {
        final long l = -1;
        boolean flag = false;
        if (x9 >= y9 - l) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
   
   // Test (x-(-1) > y) -> (x >= y)
    
    byte x10 = 10;
    byte y10 = 10;
    
    public Result test10() {
        final byte b = -1;
        boolean flag = false;
        if (x10-b > y10) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
    
   // Test (x > -1+y) -> (x >= y)
    
    byte x11 = 127;
    byte y11 = -127;
    
    public Result test11() {
        boolean flag = false;
        if (x11 > -1+y11) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
       
   // Test (-1+x >= y) -> (x > y)
    
    int x12 = 101;
    int y12 = -10;
    
    public Result test12() {
        boolean flag = false;
        if (-1+x12 >= y12) {
            flag = true;
        }
        if (flag) return passed();
        else return failed("TEST FAILED: comparison error");    
    }
    
}


