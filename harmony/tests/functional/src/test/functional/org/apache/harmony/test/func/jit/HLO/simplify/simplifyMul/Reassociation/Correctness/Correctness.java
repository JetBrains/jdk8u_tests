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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyMul.Reassociation.Correctness;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 07.07.2006
 */

public class Correctness extends MultiCase {
    
        byte varByte = 50;
        short varShort = -2048;
        int varInt = -1;
        long varLong = 3L;
        char varChar = 2048;
        float varFloat = 1f;
        double varDouble = 1;

        /* Testing reassociation of the following expressions:
           c1 * (c2 + s) -> (c1*c2) + c1*s
           c1 * (s + c2) -> (c1*c2) + c1*s
           c1 * (c2 * s) -> (c1*c2) * s
           c1 * (s * c2) -> (c1*c2) * s
           c1 * (c2 - s) -> (c1*c2) - c1*s
           c1 * (s - c2) -> (c1*(-c2)) + c1*s2
           
          (c1 + s) * c2 -> (c1*c2) + c2*s
          (s + c1) * c2 -> (c1*c2) + c2*s
          (c1 * s) * c2 -> (c1*c2) * s
          (s * c1) * c2 -> (c1*c2) * s
          (c1 - s) * c2 -> (c1*c2) - c2*s
          (s - c1) * c2 -> (c1*(-c2)) + c2*s2 */

        public static void main(String[] args) {
            log.info("Start Correctness test...");
            System.exit((new Correctness()).test(args));
        }
        
        public Result test1() {
            log.info("Test1 simplifying c1 * (c2 + s) -> (c1*c2) + c1*s :");
            final int constInt1 = 10000;
            final int constInt2 = 3;
            int result = constInt1 * (constInt2 + varInt);
            log.info("result = " + result);
            if (result == 20000) return passed();
            else return failed("TEST FAILED: result != 20000");
        }
        
        public Result test2() {
            log.info("Test2 simplifying  c1 * (s + c2) -> (c1*c2) + c1*s :");
            final byte constByte1 = 2;
            final byte constByte2 = 10;
            byte result = (byte) (constByte1 * (varByte + constByte2));
            log.info("result = " + result);
            if (result == 120) return passed();
            else return failed("TEST FAILED: result != 120");
        }
        
        public Result test3() {
            log.info("Test3 simplifying  c1 * (c2 * s) -> (c1*c2) * s :");
            final long constLong1 = 2L;
            final long constLong2 = 100000L;
            long result = constLong1 * (varLong * constLong2);
            log.info("result = " + result);
            if (result == 600000) return passed();
            else return failed("TEST FAILED: result != 600000");
        }
        
        public Result test4() {
            log.info("Test4 simplifying c1 * (s * c2) -> (c1*c2) * s :");
            final short constShort1 = 8;
            final short constShort2 = 2;
            short result = (short)(constShort1 * (varShort * constShort2));
            log.info("result = " + result);
            if (result == Short.MIN_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Short.MIN_VALUE);
        }
        
        public Result test5() {
            log.info("Test5 simplifying c1 * (c2 - s) -> (c1*c2) - c1*s :");
            final int constInt1 = 10000;
            final int constInt2 = 1;
            int result =  constInt1 * (constInt2 - varInt);
            log.info("result = " + result);
            if (result == 20000) return passed();
            else return failed("TEST FAILED: result != 20000");
        }
        
        public Result test6() {
            log.info("Test6 simplifying  c1 * (s - c2) -> (c1*(-c2)) + c1*s :");
            final byte constByte1 = 3;
            final byte constByte2 = 20;
            byte result = (byte) (constByte1 * (varByte - constByte2));
            log.info("result = " + result);
            if (result == 90) return passed();
            else return failed("TEST FAILED: result != 90");
        }
        
        public Result test7() {
            log.info("Test7 simplifying (c1 + s) * c2 -> (c1*c2) + c2*s :");
            final int constInt1 = 10;
            final int constInt2 = 55;
            int result = (constInt1 + varInt) * constInt2;
            log.info("result = " + result);
            if (result == 495) return passed();
            else return failed("TEST FAILED: result != " + 495);
        }
        
        public Result test8() {
            log.info("Test8 simplifying (s + c1) * c2 -> (c1*c2) + c2*s :");
            final byte constByte1 = 1;
            final byte constByte2 = 2;
            byte result = (byte) ((varByte + constByte1) * constByte2);
            log.info("result = " + result);
            if (result == 102) return passed();
            else return failed("TEST FAILED: result != 102");
        }
        
        public Result test9() {
            log.info("Test9 simplifying (c1 * s) * c2 -> (c1*c2) * s :");
            final long constLong1 = 300000L;
            final long constLong2 = 222L;
            long result = (constLong1 * varLong) * constLong2;
            log.info("result = " + result);
            if (result == 199800000L) return passed();
            else return failed("TEST FAILED: result != 199800000");
        }
        
        public Result test10() {
            log.info("Test10 simplifying (s * c1) * c2 -> (c1*c2) * s :");
            final char constChar1 = 3;
            final char constChar2 = 2;
            char result = (char)((varShort * constChar1) * constChar2);
            log.info("result = " + (int) result);
            if (result == 53248) return passed();
            else return failed("TEST FAILED: result != 53248");
        }
        
        public Result test11() {
            log.info("Test11 simplifying (c1 - s) * c2 -> (c1*c2) - c2*s :");
            final float constFloat1 = 0.5f;
            final float constFloat2 = 32767.3f;
            float result =  (constFloat1 - varFloat) * constFloat2;
            log.info("result = " + result);
            if (result == -16383.65f) return passed();
            else return failed("TEST FAILED: result !=  -16383.65");
        }
        
        public Result test12() {
            log.info("Test12 simplifying  (s - c1) * c2 -> (c1*(-c2)) + c2*s2 :");
            final double constDouble1 = 0.77;
            final double constDouble2 = 3;
            double result = (varDouble - constDouble1) * constDouble2;
            log.info("result = " + result);
            if (result == 0.69) return passed();
            else return failed("TEST FAILED: result != 0.69");
        }
}
