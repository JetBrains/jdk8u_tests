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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifySub.Reassociation.Correctness;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 07.07.2006
 */

public class Correctness extends MultiCase {
    
        int varInt = -100;
        byte varByte = Byte.MAX_VALUE;
        long varLong = -1L;
        short varShort = 100;
        char varChar = 1000;
        float varFloat = 0.33f;
        double varDouble = 0.777;

        /* Testing reassociation of the following expressions:
           (C1 - s1) - C2 -> (C1 - C2) - s1
           (s1 - C1) - C2 -> s1 + (-(C1 + C2))
           (s + C1) - C2 -> s + (C1 - C2)
           (C1 + s) - C2 -> s + (C1 - C2)
           C1 - (C2 - s) -> (C1-C2) + s
           C1 - (s - C2) -> (C1+C2) - s 
           C1 - (s + C2) -> (C1-C2) - s
           C1 - (C2 + s) -> (C1-C2) - s */
        
        public static void main(String[] args) {
            log.info("Start Correctness test...");
            System.exit((new Correctness()).test(args));
        }
        
        public Result test1() {
            log.info("Test1 simplifying (C1 - s1) - C2 -> (C1 - C2) - s1 :");
            final int constInt1 = -101;
            final int constInt2 = -1;
            int result =  (constInt1 - varInt) - constInt2;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != 0");
        }
        
        public Result test2() {
            log.info("Test2 simplifying  (s1 - C1) - C2 -> s1 + (-(C1 + C2)) :");
            final byte constByte1 = 2;
            final byte constByte2 = 25;
            byte result = (byte) ((varByte - constByte1) - constByte2);
            log.info("result = " + result);
            if (result == 100) return passed();
            else return failed("TEST FAILED: result != 100");
        }
        
        public Result test3() {
            log.info("Test3 simplifying (s + C1) - C2 -> s + (C1 - C2) :");
            final long constLong1 = Long.MAX_VALUE-1;
            final long constLong2 = Long.MAX_VALUE;
            long result = (varLong + constLong1) - constLong2;
            log.info("result = " + result);
            if (result == -2L) return passed();
            else return failed("TEST FAILED: result != -2");
        }
        
        public Result test4() {
            log.info("Test4 simplifying (C1 + s) - C2 -> s + (C1 - C2) :");
            final short constShort1 = 10000;
            final short constShort2 = 5000;
            short result = (short)((constShort1 + varShort) - constShort2);
            log.info("result = " + result);
            if (result == 5100) return passed();
            else return failed("TEST FAILED: result != 5100");
        }
        
        public Result test5() {
            log.info("Test5 simplifying C1 - (C2 - s) -> (C1-C2) + s :");
            final float constFloat1 = 0.98f;
            final float constFloat2 = 0.57f;
            float result =  constFloat1 - (constFloat2 - varFloat);
            log.info("result = " + result);
            if (result == 0.74f) return passed();
            else return failed("TEST FAILED: result != 0.74f");
        }
        
        public Result test6() {
            log.info("Test6 simplifying  C1 - (s - C2) -> (C1+C2) - s :");
            final char constChar1 = 65000;
            final char constChar2 = 500;
            char result = (char) (constChar1 - (varChar - constChar2));
            log.info("result = " + (int) result);
            if (result == 64500) return passed();
            else return failed("TEST FAILED: result != 64500");
        }
        
        public Result test7() {
            log.info("Test7 simplifying C1 - (s + C2) -> (C1-C2) - s :");
            final double constDouble1 = 2.9;
            final double constDouble2 = 1.0;
            double result = constDouble1 - (varLong + constDouble2);
            log.info("result = " + result);
            if (result == 2.9) return passed();
            else return failed("TEST FAILED: result != 2.9");
        }
        
        public Result test8() {
            log.info("Test8 simplifying C1 - (C2 + s) -> (C1-C2) - s :");
            final short constShort1 = Short.MAX_VALUE;
            final short constShort2 = 101;
            short result = (short)(constShort1 - (constShort2 + varShort));
            log.info("result = " + result);
            if (result == 32566) return passed();
            else return failed("TEST FAILED: result != 32566");
        }
}
