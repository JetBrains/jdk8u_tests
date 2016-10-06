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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario test 
 * Statistic - counts how many times symbols are met in the text
 */
public class F_StringTest_03 extends ScenarioTest {
    private String response = new String();

    public F_StringTest_03() {
        super();
    }

    /**
     * counts matches in the file
     * 
     * @param source - java.lang.String text
     * @return true if count, false - otherwise
     */
    protected boolean parse(String source) {
        int count = 0;
        String line = new String(source);
        if (response.length() == 1) {
            char[] letters = line.toCharArray();
            for (int i = 0; i < letters.length; i++) {
                if (response.equals(String.valueOf(letters[i]))) {
                    count++;
                }
            }
        } else {
            count += line.split(response).length - 1;
        }
        response = "In the text \n-----------------------------------------\n"
            + source
            + "\n-----------------------------------------\n letter \""
            + response + "\"" + " is met " + count + " times.";
        return true;
    }

    public int test() {
        try {
            response = testArgs[1];
            if (parse(testArgs[0])) {
                return pass(response);
            } else {
                return fail("Some problems has occured during parsing of source file or not String argument");
            }
        } catch (NullPointerException e) {
            return error("Exception: some of args is empty");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_03().test(args));
    }
}