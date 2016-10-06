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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_07;

import org.apache.harmony.test.func.share.ScenarioTest;

public class F_StringTest_07 extends ScenarioTest {

    public int test() {

        String s1 = "abc";

        Object c = new Character('a');
        Object s = new String("a");

        try {

            try {

                int tmp = s1.compareTo(c);

                return fail("Test failed: ClassCastException wasn't caught for non-String Object!");

            } catch (ClassCastException cce) {
                log
                    .info("Info: ClassCastException caught for non-String Object");
            }

            try {

                int tmp = s1.compareTo(s);
                if (tmp != 2)
                    return fail("Test failed: Wrong compare");

            } catch (ClassCastException cce) {
                cce.printStackTrace();
                return fail("Test failed: ClassCastException caught for String Object!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail("Error: Unexpected Exception caught");
        }

        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_07().test(args));
    }
}