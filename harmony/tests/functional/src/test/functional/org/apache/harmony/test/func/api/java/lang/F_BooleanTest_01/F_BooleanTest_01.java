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
/*
 * Created on 16.11.2004
 * Last modification G.Seryakova
 * Last modified on 16.11.2004
 * 
 * Check correctness of types contvertations for Boolean.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_BooleanTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Check correctness of types contvertations for Boolean.
 * 
 */
public class F_BooleanTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_BooleanTest_01().test(args));
    }

    public int test() {

        boolean bool = true;
        Boolean boolObj;
        String strValue;
        Object objArr[] = new Object[2];

        objArr[0] = Boolean.valueOf(bool);
        strValue = Boolean.toString(bool);
        boolObj = new Boolean(strValue);
        bool = boolObj.booleanValue();
        boolObj = new Boolean(bool);
        strValue = boolObj.toString();
        objArr[1] = Boolean.valueOf(strValue);

        if (!Boolean.TRUE.equals(objArr[0]) || !Boolean.TRUE.equals(objArr[1])
            || Boolean.TRUE.hashCode() != 1231) {
            return fail("Failed for true");
        }

        bool = false;

        objArr[0] = Boolean.valueOf(bool);
        strValue = Boolean.toString(bool);
        boolObj = new Boolean(strValue);
        bool = boolObj.booleanValue();
        boolObj = new Boolean(bool);
        strValue = boolObj.toString();
        objArr[1] = Boolean.valueOf(strValue);

        if (!Boolean.FALSE.equals(objArr[0])
            || !Boolean.FALSE.equals(objArr[1])
            || Boolean.FALSE.hashCode() != 1237) {
            return fail("Failed for false");
        }

        return pass();
    }
}