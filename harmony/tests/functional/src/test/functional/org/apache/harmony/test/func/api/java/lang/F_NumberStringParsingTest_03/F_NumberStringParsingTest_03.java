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
 * This test parse string of int numbers.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_NumberStringParsingTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;
import java.util.StringTokenizer;

/**
 * This test parse string of int numbers.
 * 
 */
public class F_NumberStringParsingTest_03 extends ScenarioTest {
    private Integer rightIntResult[]  = new Integer[10];

    public static void main(String[] args) {
        System.exit(new F_NumberStringParsingTest_03().test(args));
    }

    public F_NumberStringParsingTest_03() {
        rightIntResult[0] = new Integer("256");
        rightIntResult[1] = new Integer("-128");
        rightIntResult[2] = new Integer("-32758");
        rightIntResult[3] = new Integer("34");
        rightIntResult[4] = new Integer("0");
        rightIntResult[5] = new Integer("65536");
        rightIntResult[8] = new Integer("-2147483648");
        rightIntResult[9] = new Integer("2147483647");
    }

    public int parseDecIntString1() {
        String decNumbers = new String(
        "256,-128,-32758,34,0,65536,2147483648,-2147483649,-2147483648,2147483647");
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(decNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = new Integer(Integer.parseInt(next));
                if (num == 6 || num == 7) {
                    return fail(num + " int element failed (must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next;
                if (num < 6 || num > 7) {
                    return fail(new Integer(num).toString()
                        + " int element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (rightIntResult[i].compareTo(resultArr[i]) != 0) {
                    return fail(new Integer(i).toString()
                        + " int element failed.");
                }
            }
        }

        Integer tmp;
        for (int i = 0; i < 10; i++) {
            if (Integer.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Integer)resultArr[i];
                resultString.append(tmp.intValue());
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (int) not equal." + resultString.toString());
        }

        return Result.PASS;
    }
    
    public int parseDecIntString2() {
        String decNumbers = new String(
        "256,-128,-32758,34,0,65536,2147483648,-2147483649,-2147483648,2147483647");
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(decNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = Integer.valueOf(next);
                if (num == 6 || num == 7) {
                    return fail(num + " int element failed (must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next;
                if (num < 6 || num > 7) {
                    return fail(new Integer(num).toString()
                        + " int element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (rightIntResult[i].compareTo(resultArr[i]) != 0) {
                    return fail(new Integer(i).toString()
                        + " int element failed.");
                }
            }
        }

        Integer tmp;
        for (int i = 0; i < 10; i++) {
            if (Integer.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Integer)resultArr[i];
                resultString.append(Integer.toString(tmp.intValue()));
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (int) not equal." + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseHexIntString() {
        String hexNumbers = new String(
            "100,-80,-7ff6,22,0,10000,80000000,-80000001,-80000000,7fffffff");
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(hexNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = new Integer(Integer.parseInt(next, 16));
                if (num == 6 || num == 7) {
                    return fail(num + " int element failed (must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next;
                if (num < 6 || num > 7) {
                    return fail(new Integer(num).toString()
                        + " int element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (!rightIntResult[i].equals(resultArr[i])) {
                    return fail(i + " int element failed.");
                }
            }
        }

        Integer tmp;
        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                tmp = (Integer)resultArr[i];
                resultString.append(Integer.toString(tmp.intValue(), 16));
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!hexNumbers.equalsIgnoreCase(resultString.toString())) {
            return fail("Strings (int) not equal. Result - " + resultString.toString()
                + " expected result - " + hexNumbers );
        }

        return Result.PASS;
    }
    
    public int parseOctalIntString() {
        String octalNumbers = new String(
            "400,-200,-77766,42,0,200000,20000000000,-20000000001,-20000000000,17777777777");
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(octalNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = Integer.valueOf(next, 8);
                if (num == 6 || num == 7) {
                    return fail(num + " int element failed (must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next;
                if (num < 6 || num > 7) {
                    return fail(new Integer(num).toString()
                        + " int element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (!rightIntResult[i].equals(resultArr[i])) {
                    return fail(i + " int element failed.");
                }
            }
        }

        Integer tmp;
        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                tmp = (Integer)resultArr[i];
                resultString.append(Integer.toString(tmp.intValue(), 8));
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!octalNumbers.equalsIgnoreCase(resultString.toString())) {
            return fail("Strings (int) not equal. Result - " + resultString.toString()
                + " expected result - " + octalNumbers );
        }

        return Result.PASS;
    }

    public int parseMixIntString() {
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
                resultString.append(Integer.decode(next));
            } catch (NumberFormatException e) {
                return fail(num + " int element failed.");
            }
            resultString.append(",");
            num++;
        }

        resultString.deleteCharAt(resultString.length() - 1);

        if (!expectedString.equals(resultString.toString())) {
            return fail("Strings (int) not equal. Result - " + resultString.toString()
                + " expected result - " + expectedString);
        }

        return Result.PASS;
    } 
    
    public int checkStrings() {
        String strNumbers = new String(
            "256,-128,-32758,34,0,65536,44474836,-2147483,-2147483648,2147483647");
        String expectedBinString = new String(
            "100000000,11111111111111111111111110000000,11111111111111111000000000001010,100010," + 
            "0,10000000000000000,10101001101010000111010100,11111111110111110011101101100101," + 
            "10000000000000000000000000000000,1111111111111111111111111111111,");
        String expectedOctalString = new String(
        "400,37777777600,37777700012,42,0,200000,251520724,37767635545,20000000000,17777777777,");
        String expectedHexString = new String(
        "100,ffffff80,ffff800a,22,0,10000,2a6a1d4,ffdf3b65,80000000,7fffffff,");
        StringBuffer resultBinString = new StringBuffer();
        StringBuffer resultOctalString = new StringBuffer();
        StringBuffer resultHexString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(strNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultBinString.append(Integer.toBinaryString(Integer.decode(next).intValue()));
                resultOctalString.append(Integer.toOctalString(Integer.parseInt(next)));
                resultHexString.append(Integer.toHexString(Integer.valueOf(next).intValue()));
            } catch (NumberFormatException e) {
                return fail(num + " int element failed.");
            }
            resultBinString.append(",");
            resultOctalString.append(",");
            resultHexString.append(",");
            num++;
        }

        if (!expectedBinString.equals(resultBinString.toString())) {
            return fail("BinStrings (int) not equal. Result - " + resultBinString.toString()
                + " expected result - " + expectedBinString);
        }
        
        if (!expectedOctalString.equals(resultOctalString.toString())) {
            return fail("OctalStrings (int) not equal. Result - " + resultOctalString.toString()
                + " expected result - " + expectedOctalString);
        }
        
        if (!expectedHexString.equals(resultHexString.toString())) {
            return fail("HexStrings (int) not equal. Result - " + resultHexString.toString()
                + " expected result - " + expectedHexString);
        }

        return Result.PASS;
    }

    public int test() {
        int ind = 0;

        if (parseDecIntString1() != Result.PASS) {
            fail("parseDecIntString1() failed.");
            ind++;
        }
        
        if (parseDecIntString2() != Result.PASS) {
            fail("parseDecIntString2() failed.");
            ind++;
        }

        if (parseHexIntString() != Result.PASS) {
            fail("parseHexIntNumbers() failed.");
            ind++;
        }
        
        if (parseOctalIntString() != Result.PASS) {
            fail("parseOctalIntString() failed.");
            ind++;
        }

        if (parseMixIntString() != Result.PASS) {
            fail("parseMixIntString() failed.");
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