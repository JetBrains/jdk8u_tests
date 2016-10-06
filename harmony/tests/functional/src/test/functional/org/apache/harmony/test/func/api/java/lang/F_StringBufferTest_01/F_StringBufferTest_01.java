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
package org.apache.harmony.test.func.api.java.lang.F_StringBufferTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario test 
 * Parses the string argument and compares part
 * of string getting by String class and by StringBuffer class
 */
public class F_StringBufferTest_01 extends ScenarioTest {
    private String       str    = "";
    private StringBuffer buffer = new StringBuffer();

    public F_StringBufferTest_01() {
        super();
    }

    /**
     * counts matches in the file
     * 
     * @param source - java.lang.String name of the text file
     * @return true if count, false - otherwise
     */
    protected boolean compare(String source) {
        buffer = (new StringBuffer(source)).replace(0, source.lastIndexOf(
            "$Separator", source.lastIndexOf("$Separator") - 1), "");
        char[] letters = buffer.toString().toCharArray();
        for (int i = 0; i < letters.length; i++) {
            if (Character.getType(letters[i]) == Character.CURRENCY_SYMBOL) {
                str = buffer.substring(0, buffer.lastIndexOf("$Separator"));
                if (str.compareTo(buffer.replace(
                    buffer.lastIndexOf("$Separator"), letters.length, "")
                    .toString()) != 0)
                    return false;
                break;
            }
        }
        if (!source.substring(
            source.lastIndexOf("$Separator", source.lastIndexOf("$Separator") - 1),
            source.lastIndexOf("$Separator")).replaceAll("Separator", "_")
            .equals(str.replaceFirst("Separator", "_")))
            return false;
        return true;
    }

    public int test() {
        try {
            if (compare(testArgs[0])) {
                return pass("StringBuffer("
                    + str.replaceAll("Separator", "_")
                    + ") = String("
                    + testArgs[0].substring(
                        testArgs[0].lastIndexOf("$", testArgs[0]
                            .lastIndexOf("$Separator") - 1),
                        testArgs[0].lastIndexOf("$Separator")).replaceFirst(
                        "Separator", "_") + ")");
            } else {
                return fail("Some problems has occured during comparing of String and StringBuffer substrings");
            }
        } catch (NullPointerException e) {
            return error("Exception: some of args is empty");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_StringBufferTest_01().test(args));
    }
}