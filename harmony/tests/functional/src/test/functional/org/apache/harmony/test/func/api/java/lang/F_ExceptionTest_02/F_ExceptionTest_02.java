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
 * Created on 14.02.2005
 * Last modification G.Seryakova
 * Last modified on 14.02.2005
 *  
 * Test for NumberFormatException, IllegalArgumentException and RuntimeException.
 */
package org.apache.harmony.test.func.api.java.lang.F_ExceptionTest_02;

import java.util.StringTokenizer;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Test for NumberFormatException, IllegalArgumentException and
 * RuntimeException.
 * 
 */
public class F_ExceptionTest_02 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ExceptionTest_02().test(args));
    }

    public int test() {
        String inputString = "57,k;-22,5;2,2;22,67";
        String token;
        int ind = 0;

        StringTokenizer strToken = new StringTokenizer(inputString, ";");

        while (strToken.hasMoreTokens()) {
            token = strToken.nextToken();
            ind++;
            try {
                int res = multiply(token.substring(0, token.indexOf(",")),
                    token.substring(token.indexOf(",") + 1));
                if (res < 10) {
                    throw new RuntimeException();
                }
                if (res > 100) {
                    throw new RuntimeException("Result is more than 100.");
                }
            } catch (NumberFormatException e) {
                log.info(e.toString());
                if (ind != 1) {
                    return fail("fail.");
                }
            } catch (IllegalArgumentException e) {
                log.info(e.toString());
                if (ind != 2) {
                    return fail("fail.");
                }
            } catch (RuntimeException e) {
                log.info(e.toString());
                if (ind != 3 && ind != 4) {
                    return fail("fail.");
                }
            }
        }

        return pass();
    }

    private int multiply(String first, String second)
        throws IllegalArgumentException {
        log.info(first + " * " + second);
        int m1, m2;
        m1 = parse(first);
        m2 = parse(second);
        if (m1 < 0 || m2 < 0) {
            throw new IllegalArgumentException();
        }
        return m1 * m2;
    }

    private int parse(String str) throws NumberFormatException {
        int res;
        try {
            res = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("String \"" + str
                + "\" doesn't convert to int.");
        }
        return res;
    }
}