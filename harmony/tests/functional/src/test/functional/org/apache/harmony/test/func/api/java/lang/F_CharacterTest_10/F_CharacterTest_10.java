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
package org.apache.harmony.test.func.api.java.lang.F_CharacterTest_10;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 *  
 * scenario test
 * Checks the string arguments consists non unicode
 * and start non unicode identifier
 */
public class F_CharacterTest_10 extends ScenarioTest {
    private HashMap result = new HashMap();

    public F_CharacterTest_10() {
        super();
    }

    /**
     * @return HashMap of string arguments with non unicode identifier
     */
    public HashMap getResult() {
        return result;
    }

    public int test() {
        String[] words = testArgs[0].split(" ", 11);
        for (int i = 0; i < words.length; i++) {
            log.info("word = " + words[i]);
            if (!Character.isUnicodeIdentifierStart(words[i].charAt(0))) {
                result.put(words[i], "start-non-unicode");
                continue;
            }
            boolean identifier = true;
            char[] letters = new char[words[i].length()];
            words[i].getChars(0, words[i].length(), letters, 0);
            for (int j = 0; j < letters.length; j++) {
                if (!Character.isUnicodeIdentifierPart(letters[j])) {
                    identifier = false;
                    break;
                }
            }
            if (identifier) {
                result.put(words[i], "unicode");
            } else {
                result.put(words[i], "non-unicode");
            }
        }
        int countNonUnicode = 0;
        int countStartNonUnicode = 0;
        for (Iterator it = result.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            if (result.get(key).toString().equalsIgnoreCase("non-unicode"))
                countNonUnicode++;
            if (result.get(key).toString()
                .equalsIgnoreCase("start-non-unicode"))
                countStartNonUnicode++;
        }
        if (countStartNonUnicode == 5 && countNonUnicode == 3) {
            return pass("PASSED: " + result.size() + " arguments contain "
                + (countNonUnicode + countStartNonUnicode)
                + " words with non unicode symbols, " + countStartNonUnicode
                + " from it is starts with non unicode symbol.");
        } else {
            return fail("Some problems occur during collection the String arguments with unicode and non unicode identifier");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_CharacterTest_10().test(args));
    }
}