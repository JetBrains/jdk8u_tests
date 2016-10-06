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
package org.apache.harmony.test.func.api.java.lang.F_CharacterTest_09;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario 
 * Checks the string arguments consists start java identifier 
 * return true if argument starts with it and false otherwise
 */
public class F_CharacterTest_09 extends ScenarioTest {
    private HashMap result = new HashMap();

    public F_CharacterTest_09() {
        super();
    }

    /**
     * @return HashMap with string arguments starting from java identifier
     */
    public HashMap getResult() {
        return result;
    }

    public int test() {
        for (int i = 0; i < testArgs.length; i++) {
            boolean identifier = false;
            char[] letters = new char[testArgs[i].length()];
            testArgs[i].getChars(0, testArgs[i].length(), letters, 0);
            if (Character.isJavaIdentifierStart(letters[0])) {
                result.put(testArgs[i], "false");
            } else {
                result.put(testArgs[i], "true");
            }
        }
        int countJava = 0;
        int countNonJava = 0;
        for (Iterator it = result.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            if (result.get(key).toString().toLowerCase().compareToIgnoreCase(
                "false") == 0
                && result.get(key).toString().toLowerCase(Locale.getDefault())
                    .compareToIgnoreCase("false") == 0)
                countJava++;
            if (result.get(key).toString().toUpperCase(Locale.getDefault())
                .compareToIgnoreCase("TRUE") == 0)
                countNonJava++;
        }
        if (countJava == 3 && countNonJava == 4) {
            return pass();
        } else {
            return fail("Some problems occur during collection the String arguments starting with java identifier");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_CharacterTest_09().test(args));
    }
}