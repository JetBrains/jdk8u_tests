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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_10;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.util.regex.Pattern;
import java.util.regex.*;
import java.util.Random;

public class F_StringTest_10 extends ScenarioTest {

    public int test() {

        Random rand = new Random();

        int simple = rand.nextInt(50);

        String str = "";
        int flagSimple = 0;

        for (int i = 0; i < simple; i++) {
            str += "1";

            if ((i != 0) & (i != 1) & (i != 2) & (i != simple))
                if (simple % i == 0) {
                    flagSimple++;
                }

        }
        /* Case simple == 4 should be processed separately */
        if (simple == 4)
            flagSimple++;

        try {
            if (flagSimple == 0) {
                log.info("generated random number is simple");
                if (str.matches("^(11+)\\1+$"))
                    return fail("test failed to detect correctly if number is simple or not");
            }

            if (flagSimple != 0) {
                log.info("generated random number is not simple");
                if (!str.matches("^(11+)\\1+$"))
                    return fail("test failed to detect correctly if number is simple or not");

            }

        } catch (Error e) {
            log.info(e.toString());
            e.printStackTrace();
            return error("Test failed, error caught");
        }
        return pass();

    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_10().test(args));
    }
}