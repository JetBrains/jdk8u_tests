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
/*
 * Created on 26.01.2006
 *  
 * testing some bitwise BigInteger methods.
 */

package org.apache.harmony.test.func.api.java.math.F_BigIntegerSetBitsTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.math.*;

/**
 * Created on 26.01.2006
 *  
 * testing some bitwise BigInteger methods.
 */

public class F_BigIntegerSetBitsTest_01 extends ScenarioTest {
    String[][] xystr = { { "-1023", "15" }, { "14", "1" }, { "16", "6" },
        { "0", "0" }, { "1", "100" }, { "2147483647", "1000" },
        { "2147483646", "1000" }, { "2147483648", "1000" },
        { "-2147483647", "1000" }, { "2147483647", "2147483640" },
        { "214748364700", "2047403640" }, { "602548120213", "8193" },
        { "8193", "602548120213" }, { "503", "0" }, { "513", "0" },
        { "0", "33" } };

    /*
     * Using bitwise operations return value x with n bits starting with p-th
     * bit are replaced with lat n bits of y
     */
    BigInteger setBitsBitwise(BigInteger x, BigInteger y, int startBit,
        int numBits) {
        return ((x.and((BigInteger.ZERO.not()).shiftLeft(startBit + 1))).or((y
            .and(((BigInteger.ZERO.not()).shiftLeft(numBits)).not()))
            .shiftLeft(startBit + 1 - numBits)).or(x.and((BigInteger.ZERO.not()
            .shiftLeft(startBit + 1 - numBits)).not())));
    }

    /*
     * Using operations for direct access to BigInteger number's bits return
     * value x with n bits starting with p-th bit are replaced with lat n bits
     * of y
     */
    BigInteger setBitsDirectly(BigInteger x, BigInteger y, int startBit,
        int numBits) {
        BigInteger result = x;
        for (int i = 0; i < numBits; i++) {
            if (result.testBit(startBit - i) != y.testBit(numBits - 1 - i)) {
                result = result.flipBit(startBit - i);
            }
        }
        return result;
    }

    /*
     * Compare setbits(x, y, p, n) results of bitwise calculations and
     * calculations using operations for direct access to BigInteger number's
     * bits
     */
    boolean checkSetBits(BigInteger x, BigInteger y, int startBit, int numBits) {
        if (setBitsBitwise(x, y, startBit, numBits).compareTo(
            setBitsDirectly(x, y, startBit, numBits)) != 0) {
            log.info("Bitwise calculations and "
                    + "calculations using operations for direct access "
                    + "to BigInteger number's bits "
                    + "returned different results");
            return false;
        }
        return true;
    }

    /* Calculate rightmost one bit using bitwise operations */
    int getLowestSetBitCustom(BigInteger x) {
        int countBits = 0;
        BigInteger tmp = x;
        while ((tmp.and(BigInteger.ONE)).compareTo(BigInteger.ONE) != 0) {
            tmp = tmp.shiftRight(1);
            countBits++;
        }
        return countBits;
    }

    /*
     * Compare calculation of rightmost one bit using bitwise operations and
     * getLowestSetBit() method of BigInteger
     */
    boolean checkLowestSetBit(BigInteger x) {
        if (getLowestSetBitCustom(x) != x.getLowestSetBit()) {
            log.info("Bitwise calculations of leftmost one bit "
                + "and calculations using getLowestSetBit() "
                + "returned different results");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.exit(new F_BigIntegerSetBitsTest_01().test(args));
    }

    public int test() {
        try {
            try {
                for (int i = 0; i < xystr.length; i++) {
                    if (!checkLowestSetBit(new BigInteger(xystr[1][0]))
                        || !checkLowestSetBit(new BigInteger(xystr[1][1]))) {
                        return fail("Test failed");
                    }
                    for (int startBit = 0; startBit < 100; startBit++) {
                        for (int numBits = 0; numBits < startBit + 2; numBits++) {
                            if (!checkSetBits(new BigInteger(xystr[i][0]),
                                new BigInteger(xystr[i][1]), startBit, numBits)) {
                                return fail("Test failed");
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                log.info("Unexpected exception:");
                ex.printStackTrace();
                return fail("Test failed");
            }
        } catch (Error er) {
            log.info("Unexpected error:");
            er.printStackTrace();
            return fail("Test failed");
        }
        return pass();
    }
}