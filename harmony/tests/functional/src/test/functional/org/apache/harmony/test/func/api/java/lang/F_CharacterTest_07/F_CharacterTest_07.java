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
package org.apache.harmony.test.func.api.java.lang.F_CharacterTest_07;

import java.util.HashMap;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario 
 * Converts upper case chars from the string argument 
 * to lower case chars and collects its
 */
public class F_CharacterTest_07 extends ScenarioTest {
    private HashMap result = new HashMap();

    public F_CharacterTest_07() {
        super();
    }

    /**
     * @return HashMap with lower case chars from the string argument
     */
    public HashMap getResult() {
        return result;
    }

    public int test() {
        char[] letters = new char[testArgs[0].length()];
        testArgs[0].getChars(0, testArgs[0].length(), letters, 0);
        for (int i = 0; i < letters.length; i++) {
            letters[i] = Character.toLowerCase(letters[i]);
            if (Character.isLowerCase(letters[i]))
                result.put("" + i, new Character(letters[i]));
        }
        if (result.size() == 120) {
            return pass();
        } else {
            return fail("Some problems occur during convertion or collection and number of lower case chars in the String argument is incorrect");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_CharacterTest_07().test(args));
    }
}