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
package org.apache.harmony.test.func.api.java.lang.F_CharacterTest_08;

import java.util.HashMap;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario 
 * Checks the string arguments if it is java identifier 
 * then return true and false otherwise
 */
public class F_CharacterTest_08 extends ScenarioTest {
    private HashMap result = new HashMap();

    public F_CharacterTest_08() {
        super();
    }

    /**
     * @return HashMap with non java identifier string arguments
     */
    public HashMap getResult() {
        return result;
    }

    public int test() {
        for (int i = 0; i < testArgs.length; i++) {
            boolean identifier = false;
            char[] letters = new char[testArgs[i].length()];
            testArgs[i].getChars(0, testArgs[i].length(), letters, 0);
            result.put(testArgs[i], "true");
            for (int j = 0; j < letters.length; j++) {
                if (!Character.isJavaIdentifierPart(letters[j])) {
                    result.put(testArgs[i], "false");
                    break;
                }
            }
        }
        if (result.size() == 6) {
            return pass();
        } else {
            return fail("Some problems occur during collection the non java identifier String arguments");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_CharacterTest_08().test(args));
    }
}