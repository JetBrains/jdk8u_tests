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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyDiv.Overflow;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 07.07.2006
 */

public class Overflow extends MultiCase {
        
        float floatMAX = Float.MAX_VALUE;
        float floatMIN= Float.MIN_VALUE;
        float floatPositive = 1.000000E36f;
        float floatNegative = -1.000000E36f;
        
        double doubleMAX = Double.MAX_VALUE;
        double doubleMIN = Double.MIN_VALUE;
        double doublePositive = 1.000000E300;
        double doubleNegative = -1.000000E200;

        
        public static void main(String[] args) {
            log.info("Start Overflow test...");
            log.info("Tests that div operation performed correctly " +
                    "in case of narrowing type conversion in numerator");
            System.exit((new Overflow()).test(args));
        }
        
      //----------------------------------------------------------------------//
        
        public Result test1() {
            final int constInt1 = 100;
            final int constInt2 = 10;
            int result = ((int)(floatMAX*constInt1))/constInt2;
            log.info("result = " + result);
            if (result == Integer.MAX_VALUE/10) return passed();
            else return failed("TEST FAILED: result != " + Integer.MAX_VALUE/10);
        }
        
        public Result test2() {
            final int constInt1 = 4;
            final int constInt2 = 2;
            int result = ((int)(constInt1*floatMIN))/constInt2;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != " + 0);
        }
        
        public Result test3() {
            final long constLong1 = 1000;
            final long constLong2 = 10;
            long result = ((long)(constLong1*floatMAX))/constLong2;
            log.info("result = " + result);
            if (result == Long.MAX_VALUE/10) return passed();
            else return failed("TEST FAILED: result != " + Integer.MAX_VALUE/10);
        }
        
        public Result test4() {
            final long constLong1 = 400;
            final long constLong2 = 2;
            long result = ((long)(floatMIN*constLong1))/constLong2;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != " + 0);
        }
        
        public Result test5() {
            final int constInt1 = 100;
            final int constInt2 = 2;
            int result = ((int)(floatPositive*constInt1))/constInt2;
            log.info("result = " + result);
            if (result == 1073741823) return passed();
            else return failed("TEST FAILED: result != " + 1073741823);
        }
        
        public Result test6() {
            final int constInt1 = 40;
            final int constInt2 = 4;
            int result = ((int)(constInt1*floatNegative))/constInt2;
            log.info("result = " + result);
            if (result == -536870912) return passed();
            else return failed("TEST FAILED: result != " + -536870912);
        }
        
        public Result test7() {
            final long constLong1 = 49;
            final long constLong2 = 7;
            long result = ((long)(floatPositive*constLong1))/constLong2;
            log.info("result = " + result);
            if (result == 1317624576693539401L) return passed();
            else return failed("TEST FAILED: result != " + 1317624576693539401L);
        }
        
        public Result test8() {
            final long constLong1 = 4000;
            final long constLong2 = 8;
            long result = ((long)(constLong1*floatNegative))/constLong2;
            log.info("result = " + result);
            if (result == -1152921504606846976L) return passed();
            else return failed("TEST FAILED: result != " + -1152921504606846976L);
        }
        
        public Result test9() {
            final int constInt1 = 100;
            final int constInt2 = 10;
            int result = ((int)(doubleMAX*constInt1))/constInt2;
            log.info("result = " + result);
            if (result == Integer.MAX_VALUE/10) return passed();
            else return failed("TEST FAILED: result != " + Integer.MAX_VALUE/10);
        }
        
        public Result test10() {
            final int constInt1 = 4;
            final int constInt2 = 2;
            int result = ((int)(constInt1*doubleMIN))/constInt2;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != " + 0);
        }
        
        public Result test11() {
            final long constLong1 = 1000;
            final long constLong2 = 10;
            long result = ((long)(constLong1*doubleMAX))/constLong2;
            log.info("result = " + result);
            if (result == Long.MAX_VALUE/10) return passed();
            else return failed("TEST FAILED: result != " + Integer.MAX_VALUE/10);
        }
        
        public Result test12() {
            final long constLong1 = 400;
            final long constLong2 = 2;
            long result = ((long)(doubleMIN*constLong1))/constLong2;
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != " + 0);
        }
        
        public Result test13() {
            final int constInt1 = 100;
            final int constInt2 = 2;
            int result = ((int)(doublePositive*constInt1))/constInt2;
            log.info("result = " + result);
            if (result == 1073741823) return passed();
            else return failed("TEST FAILED: result != " + 1073741823);
        }
        
        public Result test14() {
            final int constInt1 = 800;
            final int constInt2 = 8;
            int result = ((int)(constInt1*doubleNegative))/constInt2;
            log.info("result = " + result);
            if (result == -268435456) return passed();
            else return failed("TEST FAILED: result != " + -268435456);
        }
        
        public Result test15() {
            final long constLong1 = 49;
            final long constLong2 = 7;
            long result = ((long)(doublePositive*constLong1))/constLong2;
            log.info("result = " + result);
            if (result == 1317624576693539401L) return passed();
            else return failed("TEST FAILED: result != " + 1317624576693539401L);
        }
        
        public Result test16() {
            final long constLong1 = 4000;
            final long constLong2 = 8;
            long result = ((long)(constLong1*doubleNegative))/constLong2;
            log.info("result = " + result);
            if (result == -1152921504606846976L) return passed();
            else return failed("TEST FAILED: result != " + -1152921504606846976L);
        }
        
      //-------------------------------------------------------------------------//
        
      /*   ...
       *   *2i
       *   i2*
       *   ...
       */     
        
        public Result test17() {
            byte result1 = (byte)(((byte) Float.MAX_VALUE)/Integer.MAX_VALUE);
            byte result2 = (byte)(((byte) floatMAX)/Integer.MAX_VALUE);
            log.info("result1 = " + result1);
            log.info("result2 = " + result2);
            if (result1 == result2) return passed();
            else return failed("TEST FAILED: result1 != result2");
        }
        
        public Result test18() {
            short result1 = (short)(((short) Double.MAX_VALUE)/Integer.MAX_VALUE);
            short result2 = (short)(((short) doubleMAX)/Integer.MAX_VALUE);
            log.info("result1 = " + result1);
            log.info("result2 = " + result2);
            if (result1 == result2) return passed();
            else return failed("TEST FAILED: result1 != result2");
        }
        
        public Result test19() {
            char result1 = (char)(((char) Float.MAX_VALUE)/Integer.MAX_VALUE);
            char result2 = (char)(((char) floatMAX)/Integer.MAX_VALUE);
            log.info("result1 = " + (int) result1);
            log.info("result2 = " + (int) result2);
            if (result1 == result2) return passed();
            else return failed("TEST FAILED: result1 != result2");
        }
        
        public Result test20() {
            byte result1 = (byte)(((byte) Double.MIN_VALUE)/Integer.MIN_VALUE);
            byte result2 = (byte)(((byte) doubleMIN)/Integer.MIN_VALUE);
            log.info("result1 = " + result1);
            log.info("result2 = " + result2);
            if (result1 == result2) return passed();
            else return failed("TEST FAILED: result1 != result2");
        }
        
        public Result test21() {
            short result1 = (short)(((short) Float.MIN_VALUE)/Integer.MIN_VALUE);
            short result2 = (short)(((short) floatMIN)/Integer.MIN_VALUE);
            log.info("result1 = " + result1);
            log.info("result2 = " + result2);
            if (result1 == result2) return passed();
            else return failed("TEST FAILED: result1 != result2");
        }
        
        public Result test22() {
            char result1 = (char)(((char) Double.MIN_VALUE)/Byte.MAX_VALUE);
            char result2 = (char)(((char) doubleMIN)/Byte.MAX_VALUE);
            log.info("result1 = " + (int) result1);
            log.info("result2 = " + (int) result2);
            if (result1 == result2) return passed();
            else return failed("TEST FAILED: result1 != result2");
        }
        
    //-------------------------------------------------------------------------//    
        
}
