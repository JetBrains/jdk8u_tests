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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_08;

import org.apache.harmony.test.func.share.ScenarioTest;

public class F_StringTest_08 extends ScenarioTest {

    public int maxSubstr(boolean case_sens, boolean ignoreCase, String s1,
        String s2) {

        int max = 0;

        for (int toffset = -1; toffset < s1.length() + 1; toffset++) {
            for (int len = -1; len < s1.length() - toffset + 2; len++) {
                for (int ooffset = -1; ooffset < s2.length() - len + 2; ooffset++) {

                    if (case_sens == true) {
                        if (s1.regionMatches(ignoreCase, toffset, s2, ooffset,
                            len))
                            if (len > max)
                                max = len;
                    } else {
                        if (s1.regionMatches(toffset, s2, ooffset, len))
                            if (len > max)
                                max = len;
                    }
                }
            }

        }

        return max;
    }

    public int test() {

        final int STR_LEN = 150;
        char data1[] = new char[STR_LEN];
        char data2[] = new char[STR_LEN];

        for (char c = 0; c < STR_LEN - 1; c++) {

            data1[c] = c;
            data2[c] = (char)(STR_LEN - c);
        }
        String s1 = new String(data1);
        String s2 = new String(data2);
        String s3 = s1.toLowerCase();
        String s4 = "aabc";
        String s5 = "xaabcy";
        String s6 = "aabxaabc";
        String s7 = "xyz";

        int r = 0;

        try {
            if ((r = maxSubstr(false, false, s1, s2)) != 1) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(true, true, s1, s2)) != 1) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(true, true, s1, s3)) != STR_LEN) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(true, false, s1, s2)) != 1) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(true, false, s1, s2)) != 1) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(false, false, s4, s5)) != 4) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(false, false, s5, s4)) != 4) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(false, false, s6, s5)) != 5) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(false, false, s6, s4)) != 4) {
                log.info(""+r);
                return fail("Test failed");
            }

            if ((r = maxSubstr(false, false, s7, s4)) != 0) {
                log.info(""+r);
                return fail("Test failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return fail("Test failed, exception caught");
        }
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_08().test(args));
    }
}