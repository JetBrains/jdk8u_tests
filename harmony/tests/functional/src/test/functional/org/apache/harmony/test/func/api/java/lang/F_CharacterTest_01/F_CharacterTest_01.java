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
package org.apache.harmony.test.func.api.java.lang.F_CharacterTest_01;

import java.util.HashMap;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario 
 * Collects digits in the string argument
 */
public class F_CharacterTest_01 extends ScenarioTest {
    private HashMap result = new HashMap();

    public F_CharacterTest_01() {
        super();
    }

    /**
     * @return HashMap with digits from string argument
     */
    public HashMap getResult() {
        return result;
    }

    public int test() {
        char[] letters = new char[testArgs[0].length()];
        testArgs[0].getChars(0, testArgs[0].length(), letters, 0);
        for (int i = 0; i < letters.length; i++) {
            if (Character.isDigit(letters[i]))
                result.put("" + i, new Character(letters[i]));
        }
        if (result.size() == 10) {
            return pass();
        } else {
            return fail("Incorrect number of digits in the String argument");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_CharacterTest_01().test(args));
    }
}