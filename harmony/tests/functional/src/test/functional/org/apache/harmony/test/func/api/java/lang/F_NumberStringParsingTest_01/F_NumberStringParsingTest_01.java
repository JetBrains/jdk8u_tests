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
 * Created on 19.11.2004
 * Last modification G.Seryakova
 * Last modified on 15.03.2005
 * 
 * This test parse string short numbers.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_NumberStringParsingTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;
import java.util.StringTokenizer;

/**
 * This test parse string of short numbers.
 * 
 */
public class F_NumberStringParsingTest_01 extends ScenarioTest {
    private Short rightShortResult[] = new Short[10];
    private String decNumbers = new String(
            "256,85,-562,-34,0,-6400,32768,-32769,-32768,32767");
    private String binaryNumbers = new String(
            "100000000,1010101,-1000110010,-100010,0,-1100100000000," + 
            "1000000000000000,-1000000000000001,-1000000000000000,111111111111111");
    private String mixNumbers = new String(
            "256,#55,-562,-0x22,0,-0X1900,0X8000,-32769,-0100000,077777");

    public static void main(String[] args) {
        System.exit(new F_NumberStringParsingTest_01().test(args));
    }

    public F_NumberStringParsingTest_01() {
        rightShortResult[0] = new Short("256");
        rightShortResult[1] = new Short("85");
        rightShortResult[2] = new Short("-562");
        rightShortResult[3] = new Short("-34");
        rightShortResult[4] = new Short("0");
        rightShortResult[5] = new Short("-6400");
        rightShortResult[8] = new Short("-32768");
        rightShortResult[9] = new Short("32767");
    }

    public int parseDecShortString1() {
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(decNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = new Short(Short.parseShort(next));
                if (num == 6 || num == 7) {
                    return fail(num + " short element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                if (num < 6 || num > 7) {
                    return fail(num + " short element failed.");
                }
                resultArr[num] = next.trim();
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (!resultArr[i].equals(rightShortResult[i])) {
                    return fail(i + " short element failed.");
                }
            }
        }

        Short tmp;
        for (int i = 0; i < 10; i++) {
            if (Short.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Short) resultArr[i];
                resultString.append(tmp.shortValue());
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (short) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseDecShortString2() {
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(decNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = Short.valueOf(next);
                if (num == 6 || num == 7) {
                    return fail(num + " short element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next.trim();
                if (num < 6 || num > 7) {
                    return fail(num + " short element failed.");
                }
            }
            num++;
        }

        Short tmp;
        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                tmp = (Short) resultArr[i];
                if (tmp.compareTo(rightShortResult[i]) != 0) {
                    return fail(i + " short element failed.");
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            if (Short.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Short) resultArr[i];
                resultString.append(tmp.intValue());
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (short) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseBinaryShortString1() {
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(binaryNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = new Short(Short.parseShort(next, 2));
                if (num == 6 || num == 7) {
                    return fail(num + " short element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                if (num < 6 || num > 7) {
                    return fail(num + " short element failed.");
                }
                resultArr[num] = Integer.valueOf(next, 2).toString();
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (!resultArr[i].equals(rightShortResult[i])) {
                    return fail(i + " short element failed.");
                }
            }
        }

        Short tmp;
        for (int i = 0; i < 10; i++) {
            if (Short.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Short) resultArr[i];
                resultString.append(tmp.toString());
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (short) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseBinaryByteString2() {
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(binaryNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = Short.valueOf(next, 2);
                if (num == 6 || num == 7) {
                    return fail(num + " short element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = Integer.valueOf(next, 2).toString();
                if (num < 6 || num > 7) {
                    return fail(num + " short element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (rightShortResult[i].compareTo(resultArr[i]) != 0) {
                    return fail(i + " short element failed.");
                }
            }
        }

        Short tmp;
        for (int i = 0; i < 10; i++) {
            if (Short.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Short) resultArr[i];
                resultString.append(Short.toString(tmp.shortValue()));
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (short) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseMixShortString() {
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(mixNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultString.append(Short.decode(next));
                if (num == 6 || num == 7) {
                    return fail(num + " short element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                if (num < 6 || num > 7) {
                    return fail(num + " short element failed.");
                }
                resultString.append(Integer.decode(next));                
            }
            resultString.append(",");
            num++;
        }

        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (short) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int test() {
        int ind = 0;

        if (parseDecShortString1() != Result.PASS) {
            fail("parseDecShortString1() failed.");
            ind++;
        }

        if (parseDecShortString2() != Result.PASS) {
            fail("parseDecShortString2() failed.");
            ind++;
        }

        if (parseBinaryShortString1() != Result.PASS) {
            fail("parseBinaryShortString1() failed.");
            ind++;
        }

        if (parseBinaryByteString2() != Result.PASS) {
            fail("parseBinaryShortString2() failed.");
            ind++;
        }
        
        if (parseMixShortString() != Result.PASS) {
            fail("parseMixShortString() failed.");
            ind++;
        }

        if (ind == 0) {
            return pass();
        } else {
            return fail("");
        }
    }
}