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
package org.apache.harmony.test.func.api.java.lang.F_StringBufferTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario test Parses the string argument and juggle with
 * parts of string getting by String class methods and by StringBuffer
 * class methods
 */
public class F_StringBufferTest_02 extends ScenarioTest {
    private String str = new String();

    public F_StringBufferTest_02() {
        super();
    }

    /**
     * operates with String argument using some methods of 
     * String, Character, StringBuffer classes
     */
    public int test() {
        StringBuffer digit_buffer = new StringBuffer();
        StringBuffer uch_buffer = new StringBuffer();
        StringBuffer lch_buffer = new StringBuffer();
        StringBuffer other_buffer = new StringBuffer();
        try {
            for (int i = 0; i < testArgs[0].length(); i++) {
                if (Character.isDigit(testArgs[0].charAt(i))) {
                    digit_buffer.append(testArgs[0].charAt(i));
                } else if (Character.isLetter(testArgs[0].charAt(i))) {
                    if (Character.isLowerCase(testArgs[0].charAt(i))) {
                        lch_buffer.append(testArgs[0].charAt(i));
                    } else if (Character.isUpperCase(testArgs[0].charAt(i))) {
                        uch_buffer.append(testArgs[0].charAt(i));
                    }
                } else if (!Character.isSpaceChar(testArgs[0].charAt(i))) {
                    other_buffer.insert(other_buffer.length(), testArgs[0]
                        .charAt(i));
                }
            }
            if (String.valueOf(digit_buffer).intern() != String.valueOf(
                digit_buffer).intern())
                return pass("String.intern() of the same objects works incorrect");
            if (String.valueOf(digit_buffer).intern() == String.valueOf(
                digit_buffer.reverse()).intern())
                return fail("String.intern() of different objects works incorrect");
            digit_buffer.append(new StringBuffer(String.valueOf(digit_buffer
                .reverse())));
            char[] charray = new char[digit_buffer.length()];
            digit_buffer.getChars(0, digit_buffer.length(), charray, 0);
            long real_long;
            try {
                Long lnumber = new Long(new String(charray, 0, digit_buffer
                    .length()).substring(0, new String(charray, 0, digit_buffer
                    .length()).lastIndexOf('2', digit_buffer.length())));
                real_long = lnumber.longValue();
            } catch (NumberFormatException e) {
                return error("Long number is correct but throws exception\n");
            }
            try {
                Long lnumber = new Long(new String(charray, 0, digit_buffer
                    .length()).substring(0, new String(charray, 0, digit_buffer
                    .length()).lastIndexOf('9', digit_buffer.length())));
                real_long = lnumber.longValue();
                return error("Long number is incorrect and must be throw exception\n");
            } catch (NumberFormatException e) {
            }
            return pass("\nOrigin string = ".concat(testArgs[0])
                + "\nlong = ".concat((new StringBuffer()).append(real_long)
                    .toString())
                + "\nsymbols StringBuffer() = "
                    .concat(new String(other_buffer))
                + "\nnumbers StringBuffer() = " + digit_buffer
                + "\nUpperChar StringBuffer() = " + uch_buffer
                + "\nLowerChar StringBuffer() = " + lch_buffer);
        } catch (NullPointerException e) {
            return error("Wrong configuration: \"args\" is empty");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_StringBufferTest_02().test(args));
    }
}