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
 * This test parse string byte numbers.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_NumberStringParsingTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;
import java.util.StringTokenizer;

/**
 * This test parse string of byte numbers.
 * 
 */
public class F_NumberStringParsingTest_02 extends ScenarioTest {
    private Byte rightByteResult[] = new Byte[10];
    private String decNumbers = new String(
            "123,85,-56,-34,0,-64,128,-129,-128,127");
    private String binaryNumbers = new String(
            "1111011,1010101,-111000,-100010,0,-1000000,10000000,-10000001,-10000000,1111111");
    private String mixNumbers = new String(
            "123,#55,-56,-0x22,0,-0X40,0X80,-129,-0200,0177");

    public static void main(String[] args) {
        System.exit(new F_NumberStringParsingTest_02().test(args));
    }

    public F_NumberStringParsingTest_02() {
        rightByteResult[0] = new Byte("123");
        rightByteResult[1] = new Byte("85");
        rightByteResult[2] = new Byte("-56");
        rightByteResult[3] = new Byte("-34");
        rightByteResult[4] = new Byte("0");
        rightByteResult[5] = new Byte("-64");
        rightByteResult[8] = new Byte("-128");
        rightByteResult[9] = new Byte("127");
    }

    public int parseDecByteString1() {
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(decNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = new Byte(Byte.parseByte(next));
                if (num == 6 || num == 7) {
                    return fail(num + " byte element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                if (num < 6 || num > 7) {
                    return fail(num + " byte element failed.");
                }
                resultArr[num] = next.trim();
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (!resultArr[i].equals(rightByteResult[i])) {
                    return fail(i + " byte element failed.");
                }
            }
        }

        Byte tmp;
        for (int i = 0; i < 10; i++) {
            if (Byte.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Byte) resultArr[i];
                resultString.append(tmp.byteValue());
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (int) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseDecByteString2() {
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(decNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = Byte.valueOf(next);
                if (num == 6 || num == 7) {
                    return fail(num + " byte element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = next.trim();
                if (num < 6 || num > 7) {
                    return fail(num + " byte element failed.");
                }
            }
            num++;
        }

        Byte tmp;
        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                tmp = (Byte) resultArr[i];
                if (tmp.compareTo(rightByteResult[i]) != 0) {
                    return fail(i + " byte element failed.");
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            if (Byte.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Byte) resultArr[i];
                resultString.append(tmp.intValue());
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (int) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseBinaryByteString1() {
        Object resultArr[] = new Object[10];
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(binaryNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultArr[num] = new Byte(Byte.parseByte(next, 2));
                if (num == 6 || num == 7) {
                    return fail(num + " byte element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                if (num < 6 || num > 7) {
                    return fail(num + " byte element failed.");
                }
                resultArr[num] = Integer.valueOf(next, 2).toString();
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (!resultArr[i].equals(rightByteResult[i])) {
                    return fail(i + " byte element failed.");
                }
            }
        }

        Byte tmp;
        for (int i = 0; i < 10; i++) {
            if (Byte.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Byte) resultArr[i];
                resultString.append(tmp.toString());
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (int) not equal. Must be " + decNumbers
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
                resultArr[num] = Byte.valueOf(next, 2);
                if (num == 6 || num == 7) {
                    return fail(num + " byte element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                resultArr[num] = Integer.valueOf(next, 2).toString();
                if (num < 6 || num > 7) {
                    return fail(num + " byte element failed.");
                }
            }
            num++;
        }

        for (int i = 0; i < 10; i++) {
            if (i < 6 || i > 7) {
                if (rightByteResult[i].compareTo(resultArr[i]) != 0) {
                    return fail(i + " byte element failed.");
                }
            }
        }

        Byte tmp;
        for (int i = 0; i < 10; i++) {
            if (Byte.class.isAssignableFrom(resultArr[i].getClass())) {
                tmp = (Byte) resultArr[i];
                resultString.append(Byte.toString(tmp.byteValue()));
            } else {
                resultString.append(resultArr[i]);
            }
            resultString.append(",");
        }
        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (int) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int parseMixByteString() {
        StringBuffer resultString = new StringBuffer();
        String next;

        StringTokenizer strToken = new StringTokenizer(mixNumbers, ",");

        int num = 0;
        while (strToken.hasMoreTokens()) {
            next = strToken.nextToken();
            try {
                resultString.append(Byte.decode(next));
                if (num == 6 || num == 7) {
                    return fail(num + " byte element failed(must be Exception).");
                }
            } catch (NumberFormatException e) {
                if (num < 6 || num > 7) {
                    return fail(num + " byte element failed.");
                }
                resultString.append(Integer.decode(next));                
            }
            resultString.append(",");
            num++;
        }

        resultString.deleteCharAt(resultString.length() - 1);

        if (!decNumbers.equals(resultString.toString())) {
            return fail("Strings (int) not equal. Must be " + decNumbers
                    + ", but is" + resultString.toString());
        }

        return Result.PASS;
    }

    public int test() {
        int ind = 0;

        if (parseDecByteString1() != Result.PASS) {
            fail("parseDecByteString1() failed.");
            ind++;
        }

        if (parseDecByteString2() != Result.PASS) {
            fail("parseDecByteString2() failed.");
            ind++;
        }

        if (parseBinaryByteString1() != Result.PASS) {
            fail("parseBinaryByteString1() failed.");
            ind++;
        }

        if (parseBinaryByteString2() != Result.PASS) {
            fail("parseBinaryByteString2() failed.");
            ind++;
        }
        
        if (parseMixByteString() != Result.PASS) {
            fail("parseMixByteString() failed.");
            ind++;
        }

        if (ind == 0) {
            return pass();
        } else {
            return fail("");
        }
    }
}