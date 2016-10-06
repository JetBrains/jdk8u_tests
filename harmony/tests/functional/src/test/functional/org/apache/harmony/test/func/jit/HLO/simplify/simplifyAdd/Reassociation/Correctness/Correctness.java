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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyAdd.Reassociation.Correctness;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 07.07.2006
 */

public class Correctness extends MultiCase {
        
        byte varByte = 1;
        short varShort = -1;
        char varChar = 111;
        int varInt = 600;
        float varFloat = 1E1f;
        double varDouble = -0.55555;
        
        
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
            log.info("Start Correctness test...");
            System.exit((new Correctness()).test(args));
        }
        
        public Result test1() {
            log.info("Test1 simplifying c1 + (c2 + s) -> (c1+c2) + s :");
            final byte constByte1 = 125;
            final byte constByte2 = 1;
            byte result =  (byte)(constByte1 + (constByte2 + varByte));
            log.info("result = " + result);
            if (result == Byte.MAX_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Byte.MAX_VALUE);
        }
        
        public Result test2() {
            log.info("Test2 simplifying  c1 + (s + c2) -> (c1 + c2) + s :");
            final short constShort1 = -32766;
            final short constShort2 = -1;
            short result = (short)(constShort1 + (varShort + constShort2));
            log.info("result = " + result);
            if (result == Short.MIN_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Short.MIN_VALUE);
        }
        
        public Result test3() {
            log.info("Test3 simplifying c1 + (c2 - s) -> (c1 + c2) - s :");
            final char constChar1 = 333;
            final char constChar2 = 222;
            char result = (char)(constChar1 + (constChar2 - varChar));
            log.info("result = " + result);
            if (result == 444) return passed();
            else return failed("TEST FAILED: result != " + 444);
        }
        
        public Result test4() {
            log.info("Test4 simplifying c1 + (s - c2) -> (c1-c3) + s :");
            final int constInt1 = -500;
            final int constInt2 = 100;
            int result = constInt1 + (varInt - constInt2);
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != " + 0);
        }
        
        public Result test5() {
            log.info("Test5 simplifying (c1 + s) + c2 -> (c1 + c2) + s :");
            final float constFloat1 = 0.9191919191919191f;
            final float constFloat2 = 0.1212121212121212f;
            float result =  (constFloat1 + varFloat) + constFloat2;
            log.info("result = " + result);
            if (result == 11.0404040404040403f) return passed();
            else return failed("TEST FAILED: result != " + 11.0404040404040403f);
        }
        
        public Result test6() {
            log.info("Test6 simplifying (s + c1) + c2 -> s + (c1 + c2) :");
            final double constDouble1 = -1E2;
            final double constDouble2 = -1E3;
            double result =  (varDouble + constDouble1) + constDouble2;
            log.info("result = " + result);
            if (result == -1100.55555) return passed();
            else return failed("TEST FAILED: result != " + -1100.55555);
        }
        
        public Result test7() {
            log.info("Test7 simplifying (c1 - s) + c2 -> (c1 + c2) - s :");
            final char constChar1 = 100;
            final char constChar2 = 11;
            char result =  (char) ((constChar1 - varChar) + constChar2);
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != " + 0);
        }
        
        public Result test8() {
            log.info("Test8 simplifying (s - c1) + c2 -> s + (-c1 + c2) :");
            final double constDouble1 = -0.55555;
            final double constDouble2 = 1;
            double result =  (varDouble - constDouble1) + constDouble2;
            log.info("result = " + result);
            if (result == 1.0) return passed();
            else return failed("TEST FAILED: result != " + 1.0);
        }
}
