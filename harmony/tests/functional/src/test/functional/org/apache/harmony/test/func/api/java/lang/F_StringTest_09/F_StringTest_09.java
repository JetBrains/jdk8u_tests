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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_09;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.*;

public class F_StringTest_09 extends ScenarioTest {

    public int checkEscapedChars() {

        String str = "abc";

        String str1;
        int flag = 0;
        int countEscape=0;

        char arr[] = new char[1];

        for (char i = 0; i < 127; i++) {
            arr[0] = i;
            String str_re = new String(arr);
            str_re = "\\" + str_re;
            
            
            
            try {
                try {
                    str1 = str.replaceFirst(str_re, "x");
                    countEscape++;
                } catch (PatternSyntaxException pse) {
                    //log.info("Info: malformed regexp " + str_re);
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag++;
            }

        }
        
        if (countEscape!=86){
            log.info("Some escaped constructions were not interpreted correctly!");
            
        }
        
        if (flag != 0)
            log.info("Wrong exception thrown when using malformed regexp ");
            
        if((flag!=0) | countEscape!=86)
            return 1;
        
        
        return 0;

    }

    public int test() {

        String str = "abcy";
        String str1;

        int flagWrong = 0;
        int intCount = 0;
        
        
            
        try {

            try {

                str1 = str.replaceFirst("\\y", "x");

                flagWrong++;
                log
                        .info("Test failed: PatternSyntaxException wasn't caught when using malformed regexp: \\\\y\n\n");

            } catch (PatternSyntaxException pse) {
                log.info("PatternSyntaxException caught correctly");
            }

            try {
                if (!str.replaceFirst("\\u0061", "x").equals("xbcy")) {
                    log.info("Test failed to replace \\\\u0061 regexp\n\n");
                    flagWrong++;
                }

            } catch (PatternSyntaxException pse) {
                log.info("Test failed");
                pse.printStackTrace();
                flagWrong++;
            }

            try {
                try {
                    if (!str.replaceFirst("", "x").equals("xabcy")) {
                        log
                                .info("Test failed to replace empty regexp with character x \n\n");
                        flagWrong = 1;
                    }

                } catch (PatternSyntaxException pse) {
                    log
                            .info("Info: PatternSyntaxException caught when using empty string as a regexp in replaceAll(regexp, String) method, jrockit's behavior differs\n\n");
                    pse.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                log
                        .info("Test failed: wrong exception thrown when using empty regexp in replaceFirst (regexp, String) method! (no exception at all expected)\n\n");
                flagWrong++;

            }

        } catch (Exception e) {
            e.printStackTrace();
            flagWrong++;
        }

        if (checkEscapedChars() != 0) {
            flagWrong++;
            log.info("Escaped chars check failed\n\n");
        }
        if (flagWrong == 0)
            return pass();
        else
            return fail("Test failed");

    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_09().test(args));
    }
}