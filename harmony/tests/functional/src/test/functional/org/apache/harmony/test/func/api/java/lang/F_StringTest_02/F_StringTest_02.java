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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario 
 * Checks validation of e-mail with regular expressions
 */
public class F_StringTest_02 extends ScenarioTest {

    public F_StringTest_02() {
        super();
    }

    /**
     * checks if correct e-mail address with regular expressions
     * 
     * @param email - java.lang.String e-mail address
     * @return true if email is correct, false - otherwise
     */
    protected boolean checkEmail(String email) {
        try {
            return email.matches("\\w*@\\w*.\\w*");
        } catch (NullPointerException e) {
            error("Exception: arg is empty");
            return false;
        }
    }

    public int test() {
        if (checkEmail(testArgs[0])) {
            return pass();
        } else {
            return fail("Incorrect format e-mail or not String argument");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_02().test(args));
    }
}