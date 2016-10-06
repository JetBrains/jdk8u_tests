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
 * Created on 15.03.2005
 * Last modification G.Seryakova
 * Last modified on 15.03.2005
 * 
 * This test parse string of long numbers.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_NumberStringParsingTest_04;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;
import java.util.StringTokenizer;

/**
 * This test parse string of long numbers.
 * 
 */
public class F_NumberStringParsingTest_04 extends ScenarioTest {
    private Long    rightLongResult[] = new Long[10];

    public static void main(String[] args) {
        System.exit(new F_NumberStringParsingTest_04().test(args));
    }

    public F_NumberStringParsingTest_04() {
        rightLongResult[0] = new Long("256");
        rightLongResult[1] = new Long("-128");
        rightLongResult[2] = new Long("4294967296");
        rightLongResult[3] = new Long("-65536");
        rightLongResult[4] = new Long("0");
        rightLongResult[5] = new Long("67868569");
        rightLongResult[8] = new Long("-9223372036854775808");
        rightLongResult[9] = new Long("9223372036854775807");
    }

    public int parseDecLongString1() {
        String decNumbers = new String(
        "256,-128,4294967296,-65536,0,67868569,9223372036854775808,-9223372036854775809,-9223372036854775808,9223372036854775807");
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(decNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = new Long(Long.parseLong(next));
                if (num == 6 || num == 7) {
                    return fail(num + " int element failed (must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next;
                if (num < 6 || num > 7) {
                    return fail(num + " long element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (rightLongResult[i].compareTo(resultArr[i]) != 0) {
                    return fail(i + " long element failed.");
                }
            }
        }

        Long tmp;
        for (int i = 0; i < 10; i++) {
            if (Long.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Long)resultArr[i];
                resultString.append(tmp.toString());
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (long) not equal." + resultString.toString());
        }

        return Result.PASS;
    }
    
    public int parseDecLongString2() {
        String decNumbers = new String(
        "256,-128,4294967296,-65536,0,67868569,9223372036854775808,-9223372036854775809,-9223372036854775808,9223372036854775807");
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(decNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = Long.valueOf(next);
                if (num == 6 || num == 7) {
                    return fail(num + " long element failed (must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next;
                if (num < 6 || num > 7) {
                    return fail(num + " long element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (rightLongResult[i].compareTo(resultArr[i]) != 0) {
                    return fail(i + " long element failed.");
                }
            }
        }

        Long tmp;
        for (int i = 0; i < 10; i++) {
            if (Long.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Long)resultArr[i];
                resultString.append(Long.toString(tmp.longValue()));
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (long) not equal." + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseHexLongString() {
        String hexNumbers = new String(
        "100,-80,100000000,-10000,0,40b9799,8000000000000000,-8000000000000001,-8000000000000000,7fffffffffffffff");

        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(hexNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = new Long(Long.parseLong(next, 16));
                if (num == 6 || num == 7) {
                    return fail(num + " long element failed (must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next;
                if (num < 6 || num > 7) {
                    return fail(num + " long element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (!rightLongResult[i].equals(resultArr[i])) {
                    return fail(i + " long element failed.");
                }
            }
        }

        Long tmp;
        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                tmp = (Long)resultArr[i];
                resultString.append(Long.toString(tmp.longValue(), 16));
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!hexNumbers.equalsIgnoreCase(resultString.toString())) {
            return fail("Strings (long) not equal. Result - " + resultString.toString()
                + " expected result - " + hexNumbers );
        }

        return Result.PASS;
    }
    
    public int parseOctalLongString() {
        String octalNumbers = new String(
            "400,-200,40000000000,-200000,0,402713631,1000000000000000000000,-1000000000000000000001,-1000000000000000000000,777777777777777777777");
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(octalNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = Long.valueOf(next, 8);
                if (num == 6 || num == 7) {
                    return fail(num + " long element failed (must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next;
                if (num < 6 || num > 7) {
                    return fail(num + " long element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (rightLongResult[i].compareTo((Long)resultArr[i]) != 0) {
                    return fail(i + " long element failed.");
                }
            }
        }

        Long tmp;
        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                tmp = (Long)resultArr[i];
                resultString.append(Long.toString(tmp.longValue(), 8));
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!octalNumbers.equalsIgnoreCase(resultString.toString())) {
            return fail("Strings (long) not equal. Result - " + resultString.toString()
                + " expected result - " + octalNumbers );
        }

        return Result.PASS;
    }

    public int parseMixLongString() {
        String expectedString = new String(
            "256,-128,-32758,34,0,65536,44474836,-2147483,-2147483648,2147483647");
        String strNumbers = new String(
            "0400,-128,-077766,34,#0,0X10000,0x2a6a1d4,-2147483,-#80000000,017777777777");
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(strNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultString.append(Long.decode(next));
            } catch (NumberFormatException e) {
                return fail(num + " long element failed.");
            }
            resultString.append(",");
            num++;
        }

        resultString.deleteCharAt(resultString.length() - 1);

        if (!expectedString.equals(resultString.toString())) {
            return fail("Strings (long) not equal. Result - " + resultString.toString()
                + " expected result - " + expectedString);
        }

        return Result.PASS;
    }   
    
    public int checkStrings() {
        String strNumbers = new String(
                "256,-128,-32758,34,0,65536,44474836,-2147483,-2147483648,2147483647");
        String expectedBinString = new String("100000000," + 
                "1111111111111111111111111111111111111111111111111111111110000000," + 
                "1111111111111111111111111111111111111111111111111000000000001010," + 
                "100010,0,10000000000000000,10101001101010000111010100," + 
                "1111111111111111111111111111111111111111110111110011101101100101," + 
                "1111111111111111111111111111111110000000000000000000000000000000," + 
                "1111111111111111111111111111111,");
        String expectedOctalString = new String("400,1777777777777777777600," + 
                "1777777777777777700012,42,0,200000,251520724,1777777777777767635545," + 
                "1777777777760000000000,17777777777,");
        String expectedHexString = new String("100,ffffffffffffff80,ffffffffffff800a," + 
                "22,0,10000,2a6a1d4,ffffffffffdf3b65,ffffffff80000000,7fffffff,");
        StringBuffer resultBinString = new StringBuffer();
        StringBuffer resultOctalString = new StringBuffer();
        StringBuffer resultHexString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(strNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultBinString.append(Long.toBinaryString(Long.decode(next).longValue()));
                resultOctalString.append(Long.toOctalString(Long.parseLong(next)));
                resultHexString.append(Long.toHexString(Long.valueOf(next).longValue()));
            } catch (NumberFormatException e) {
                return fail(num + " long element failed.");
            }
            resultBinString.append(",");
            resultOctalString.append(",");
            resultHexString.append(",");
            num++;
        }

        if (!expectedBinString.equals(resultBinString.toString())) {
            return fail("BinStrings (long) not equal. Result - " + resultBinString.toString()
                + " expected result - " + expectedBinString);
        }
        
        if (!expectedOctalString.equals(resultOctalString.toString())) {
            return fail("OctalStrings (long) not equal. Result - " + resultOctalString.toString()
                + " expected result - " + expectedOctalString);
        }
        
        if (!expectedHexString.equals(resultHexString.toString())) {
            return fail("HexStrings (long) not equal. Result - " + resultHexString.toString()
                + " expected result - " + expectedHexString);
        }

        return Result.PASS;
    }


    public int test() {
        int ind = 0;

        if (parseDecLongString1() != Result.PASS) {
            fail("parseDecLongString1() failed.");
            ind++;
        }
        
        if (parseDecLongString2() != Result.PASS) {
            fail("parseDecLongString2() failed.");
            ind++;
        }

        if (parseHexLongString() != Result.PASS) {
            fail("parseHexLongNumbers() failed.");
            ind++;
        }
        
        if (parseOctalLongString() != Result.PASS) {
            fail("parseOctalLongString() failed.");
            ind++;
        }

        if (parseMixLongString() != Result.PASS) {
            fail("parseMixLongString() failed.");
            ind++;
        }
        
        if (checkStrings() != Result.PASS) {
            fail("checkStrings() failed.");
            ind++;
        }

        if (ind == 0) {
            return pass();
        } else {
            return fail("");
        }
    }
}