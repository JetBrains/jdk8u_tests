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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyAdd.Reassociation.Overflow;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 30.05.2006
 */

public class Overflow extends MultiCase {
    
        int varInt = -101;
        byte varByte = -1;
        long varLong = 1317624576693539401L;
        short varShort = 100;
        
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
            log.info("Start Overflow simplification test...");
            System.exit((new Overflow()).test(args));
        }
        
        public Result test1() {
            log.info("Test1 simplifying c1 + (c2 + s) -> (c1+c2) + s :");
            final int constInt1 = Integer.MAX_VALUE;
            final int constInt2 = 100;
            int result =  constInt1 + (constInt2 + varInt);
            log.info("result = " + result);
            if (result == Integer.MAX_VALUE - 1) return passed();
            else return failed("TEST FAILED: result != " + (Integer.MAX_VALUE - 1));
        }
        
        public Result test2() {
            log.info("Test2 simplifying  c1 + (s + c2) -> (c1+c2) + s :");
            final byte constByte1 = 1;
            final byte constByte2 = Byte.MAX_VALUE;
            byte result = (byte) (constByte1 + (varByte + constByte2));
            log.info("result = " + result);
            if (result == Byte.MAX_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Byte.MAX_VALUE);
        }
        
        public Result test3() {
            log.info("Test3 simplifying c1 + (c2 - s) -> (c1+c2) - s :");
            final long constLong1 = 188232082384791343L;
            final long constLong2 = Long.MAX_VALUE;
            long result = constLong1 + (constLong2 - varLong);
            log.info("result = " + result);
            if (result == 8093979542546027749L) return passed();
            else return failed("TEST FAILED: result != " + 8093979542546027749L);
        }
        
        public Result test4() {
            log.info("Test4 simplifying c1 + (s - c2) -> (c1-c2) + s :");
            final short constShort1 = -100;
            final short constShort2 = Short.MIN_VALUE;
            short result = (short)(constShort1 + (varShort - constShort2));
            log.info("result = " + result);
            if (result == Short.MIN_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Short.MIN_VALUE);
        }
        
        public Result test5() {
            log.info("Test5 simplifying (c1 + s) + c2 -> (c1 + c2) + s :");
            final int constInt1 = Integer.MAX_VALUE;
            final int constInt2 = 100;
            int result =  (constInt1 + varInt) + constInt2;
            log.info("result = " + result);
            if (result == Integer.MAX_VALUE - 1) return passed();
            else return failed("TEST FAILED: result != " + (Integer.MAX_VALUE - 1));
        }
        
        public Result test6() {
            log.info("Test6 simplifying (s + c1) + c2 -> s + (c1 + c2) :");
            final byte constByte1 = 1;
            final byte constByte2 = Byte.MAX_VALUE;
            byte result = (byte) ((varByte + constByte1) + constByte2);
            log.info("result = " + result);
            if (result == Byte.MAX_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Byte.MAX_VALUE);
        }
        
        public Result test7() {
            log.info("Test7 simplifying (c1 - s) + c2 -> (c1 + c2) - s :");
            final long constLong1 = 188232082384791343L;
            final long constLong2 = Long.MAX_VALUE;
            long result = (constLong1 - varLong) + constLong2;
            log.info("result = " + result);
            if (result == 8093979542546027749L) return passed();
            else return failed("TEST FAILED: result != " + 8093979542546027749L);
        }
        
        public Result test8() {
            log.info("Test8 simplifying (s - c1) + c2 -> s + (-c1 + c2) :");
            final short constShort1 = 100;
            final short constShort2 = Short.MIN_VALUE;
            short result = (short)((varShort - constShort1) + constShort2);
            log.info("result = " + result);
            if (result == Short.MIN_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Short.MIN_VALUE);
        }
        
        public Result test9() {
            log.info("Test9 simplifying (c1-c2)+(c1-c2)+(c1-c2)+(c1-c2) -> 4*c1 - 4*c2 :");
            final int constInt1 = Integer.MIN_VALUE;
            final int constInt2 = -1610612736;
            int result =  (constInt1 - constInt2) + (constInt1 - constInt2) 
                            + (constInt1 - constInt2) + (constInt1 - constInt2);
            log.info("result = " + result);
            if (result == Integer.MIN_VALUE) return passed();
            else return failed("TEST FAILED: result != " + Integer.MAX_VALUE);
        }
}
