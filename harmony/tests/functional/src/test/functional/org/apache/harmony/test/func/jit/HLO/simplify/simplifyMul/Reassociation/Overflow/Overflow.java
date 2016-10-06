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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyMul.Reassociation.Overflow;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 28.06.2006
 */

public class Overflow extends MultiCase {
    
        int varInt = -1;
        byte varByte = Byte.MAX_VALUE;
        long varLong = 0L;
        short varShort = -1;

        /* Testing reassociation of the following expressions:
           c1 * (c2 + s) -> (c1*c2) + c1*s
           c1 * (s + c2) -> (c1*c2) + c1*s
           c1 * (c2 * s) -> (c1*c2) * s
           c1 * (s * c2) -> (c1*c2) * s
           c1 * (c2 - s) -> (c1*c2) - c1*s
           c1 * (s - c2) -> (c1*(-c2)) + c1*s2 */

        public static void main(String[] args) {
            log.info("Start Overflow simplification test...");
            System.exit((new Overflow()).test(args));
        }
        
        public Result test1() {
            log.info("Test1 simplifying c1 * (c2 + s) -> (c1*c2) + c1*s :");
            final int constInt1 = Integer.MAX_VALUE;
            final int constInt2 = 2;
            int result = constInt1 * (constInt2 + varInt);
            log.info("result = " + result);
            if (result == Integer.MAX_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Integer.MAX_VALUE);
        }
        
        public Result test2() {
            log.info("Test2 simplifying  c1 * (s + c2) -> (c1*c2) + c1*s :");
            final byte constByte1 = 100;
            final byte constByte2 = Byte.MIN_VALUE;
            byte result = (byte) (constByte1 * (varByte + constByte2));
            log.info("result = " + result);
            if (result == -100) return passed();
            else return failed("TEST FAILED: result != " + (-100));
        }
        
        public Result test3() {
            log.info("Test3 simplifying  c1 * (c2 * s) -> (c1*c2) * s :");
            final long constLong1 = 2L;
            final long constLong2 = Long.MAX_VALUE;
            long result = constLong1 * (varLong * constLong2);
            log.info("result = " + result);
            if (result == 0L) return passed();
            else return failed("TEST FAILED: result != " + 0);
        }
        
        public Result test4() {
            log.info("Test4 simplifying c1 * (s * c2) -> (c1*c2) * s :");
            final short constShort1 = 16384;
            final short constShort2 = 2;
            short result = (short)(constShort1 * (varShort * constShort2));
            log.info("result = " + result);
            if (result == Short.MIN_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Short.MIN_VALUE);
        }
        
        public Result test5() {
            log.info("Test5 simplifying c1 * (c2 - s) -> (c1*c2) - c1*s :");
            final int constInt1 = Integer.MAX_VALUE;
            final int constInt2 = -2;
            int result =  constInt1 * (constInt2 - varInt);
            log.info("result = " + result);
            if (result == -Integer.MAX_VALUE) return passed();
            else return failed("TEST FAILED: result != " + -Integer.MAX_VALUE);
        }
        
        public Result test6() {
            log.info("Test6 simplifying  c1 * (s - c2) -> (c1*(-c2)) + c1*s :");
            final byte constByte1 = 77;
            final byte constByte2 = Byte.MAX_VALUE;
            byte result = (byte) (constByte1 * (varByte - constByte2));
            log.info("result = " + result);
            if (result == 0) return passed();
            else return failed("TEST FAILED: result != " + 0);
        }
}
