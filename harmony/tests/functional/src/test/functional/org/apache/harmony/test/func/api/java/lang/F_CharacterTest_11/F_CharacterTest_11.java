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
package org.apache.harmony.test.func.api.java.lang.F_CharacterTest_11;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario test 
 * Checks the string arguments after impact by
 * Character.forDigit(Character.digit(char, int), int)
 */
public class F_CharacterTest_11 extends ScenarioTest {

    public F_CharacterTest_11() {
        super();
    }

    public int test() {
        String[] compare = new String[testArgs.length];
        for (int i = 0; i < testArgs.length; i++) {
            StringBuffer a = new StringBuffer();
            char[] letters = new char[testArgs[i].length()];
            testArgs[i].getChars(0, testArgs[i].length(), letters, 0);
            for (int j = 0; j < letters.length; j++) {
                a.append(Character.forDigit(Character.digit(letters[j],
                    Character.MAX_RADIX), Character.MAX_RADIX));
            }
            compare[i] = a.toString();
        }
        if (!compare[0].equals(testArgs[0]) && compare[1].equals(testArgs[1])) {
            return pass();
        } else {
            return fail("Incorrect work Character.forDigit(Character.digit(char, int), int), String arguments after impact became wrong (not forecast).");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_CharacterTest_11().test(args));
    }
}