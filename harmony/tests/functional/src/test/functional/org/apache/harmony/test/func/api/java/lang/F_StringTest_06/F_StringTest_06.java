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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_06;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.io.*;
import java.util.Locale;

public class F_StringTest_06 extends ScenarioTest {

    /**
     * checks if Unicode string is uppercased correctly by toUpperCase(Locale
     * locale) method using the rules of the given locale. Several locales are
     * checked
     */

    public int checkUpperCase(Locale locale, String inputFileName) {

        char data[] = new char[65536];

        for (char c = 0; c < 65535; c++) {

            data[c] = c;
        }

        String str_upper = new String(data).toUpperCase(locale);
        char data_upper[] = new char[65535];
        data_upper = str_upper.toCharArray();

        try {
            //        FileOutputStream fw = new FileOutputStream(inputFileName);
            //        ObjectOutputStream oos= new ObjectOutputStream(fw);
            //        for (int i=0; i<65535; i++){
            //            oos.writeChar(data_upper[i]);
            //        }
            //        oos.close();
            //        
            FileInputStream fis = new FileInputStream(inputFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            char data_read[] = new char[65535];

            int flagWrong = 0;
            for (int i = 0; i < 65535; i++) {

                data_read[i] = ois.readChar();

                if (data_upper[i] != data_read[i]) {

                    flagWrong = 1;

                    log
                        .info("Uppercased string doesn't match the template! Used locale  "
                            + locale);
                    log.info("upper " + (int)(data_upper[i]) + ",  should be "
                        + (int)data_read[i]);

                }

            }
            if (flagWrong == 1)
                return 1;

        } catch (Exception eio) {
            eio.printStackTrace();
            return error("test failed - file reading error");
        }

        return 0;
    }

    public int test() {

        String path = testArgs[0];

        String lang[] = { "us", "ru", "de", "vi", "zh", "ja", "he", "ka", "tr",
            "lt", "az", "gr", "hy" };
        int flagFail = 0;

        for (int i = 0; i < lang.length; i++) {

            try {

                Locale curr_locale = new Locale(lang[i]);
                log.info("\nChecking toUpperCase method for locale " + lang[i]
                    + "\n");

                if (checkUpperCase(curr_locale, path + "upper_" + lang[i]) != 0) {
                    flagFail = 1;

                }

            }

            catch (Exception e) {
                e.printStackTrace();
                log.info("failed - " + e.getMessage());

            }
        }

        try {
            Locale curr_locale = Locale.getDefault();
            log.info("\nChecking toUpperCase method for default locale: "
                + curr_locale + "\n");
            if (checkUpperCase(curr_locale, path + "upper_" + "us" ) != 0) {
                flagFail = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("failed - " + e.getMessage());
        }

        if (flagFail == 1)
            return fail("Test failed to uppercase the unicode string correctly");
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_06().test(args));
    }
}