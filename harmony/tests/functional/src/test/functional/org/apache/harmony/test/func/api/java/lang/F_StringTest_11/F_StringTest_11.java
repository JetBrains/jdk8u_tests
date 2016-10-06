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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_11;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario test 
 * Tests 5 String constructors and 3 methods
 */
public class F_StringTest_11 extends ScenarioTest {
    private String str = new String();

    public F_StringTest_11() {
        super();
    }

    public int test() {
        byte[] b = new byte[94];
        for (int i = 33; i < b.length + 33; i++) {
            b[i - 33] = (byte)i;
        }
        String ascii = new String(b);
        log.info("String byte from 33 to 127 = " + ascii);
        String capitals = new String(b, 32, 26);
        log.info("Latin Capital letters (from 65 to 91) = " + capitals);
        String smalls = new String(b, 64, 26);
        log.info("Latin Small letters (from 97 to 123) = " + smalls);
        String digitals = new String(b, 15, 10);
        log.info("Digitals (from 48 to 58) = " + digitals + "\n");
        try {
            ascii = new String(b, Charset.forName("US-ASCII").name());
            log.info("Charset US-ASCII = " + ascii);
            ascii = new String(b, 32, 26, Charset.forName("US-ASCII").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 64, 26, Charset.forName("US-ASCII").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("ISO-8859-1").name());
            log.info("Charset ISO-8859-1 = " + ascii);
            ascii = new String(b, 32, 26, Charset.forName("ISO-8859-1").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 64, 26, Charset.forName("ISO-8859-1").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("UTF-8").name());
            log.info("Charset UTF-8 = " + ascii);
            ascii = new String(b, 32, 26, Charset.forName("UTF-8").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 64, 26, Charset.forName("UTF-8").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("UTF-16BE").name());
            log.info("Charset UTF-16BE = " + ascii);
            ascii = new String(b, 32, 26, Charset.forName("UTF-16BE").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 64, 26, Charset.forName("UTF-16BE").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("UTF-16LE").name());
            log.info("Charset UTF-16LE = " + ascii);
            ascii = new String(b, 32, 26, Charset.forName("UTF-16LE").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 64, 26, Charset.forName("UTF-16LE").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("UTF-16").name());
            log.info("Charset UTF-16 = " + ascii);
            ascii = new String(b, 32, 26, Charset.forName("UTF-16").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 64, 26, Charset.forName("UTF-16").name());
            log.info("\t\tSmall = " + ascii);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return fail("new String(byte[], String) or String(byte[], int, int, String) is crashed.");
        }
        log.info("\n==========================================================\n");
        b = new byte[256];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte)i;
        }
        ascii = new String(b);
        byte[] b1 = new byte[257];
        for (int i = 0; i < b1.length; i++) {
            b1[i] = (byte)i;
        }
        String ascii257 = new String(b1);
        if (ascii.compareTo((Object)ascii257) == 0)
            return fail("String.compareTo(differing Object) works incorrectly.");
        if (ascii.compareTo((Object)ascii) != 0)
            return fail("String.compareTo(equal Object) works incorrectly.");
        if (ascii257.compareTo((Object)ascii257) != 0)
            return fail("byte[257]: String.compareTo(equal Object) works incorrectly.");

        log.info("\n==========================================================\n");
        log.info("String byte from 0 to 256 = " + ascii.toString());
        capitals = new String(b, 192, 32);
        log.info("Russian Capital letters (from 192 to 223) = " + capitals);
        smalls = new String(b, 224, 32);
        log.info("Russian Small letters (from 224 to 256) = " + smalls + "\n");
        try {
            ascii = new String(b, Charset.forName("US-ASCII").name());
            log.info("Charset US-ASCII = " + ascii.toString());
            ascii = new String(b, 192, 32, Charset.forName("US-ASCII").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 224, 32, Charset.forName("US-ASCII").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("ISO-8859-1").name());
            log.info("Charset ISO-8859-1 = " + ascii.toString());
            ascii = new String(b, 192, 32, Charset.forName("ISO-8859-1").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 224, 32, Charset.forName("ISO-8859-1").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("UTF-8").name());
            log.info("Charset UTF-8 = " + ascii.toString());
            ascii = new String(b, 192, 32, Charset.forName("UTF-8").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 224, 32, Charset.forName("UTF-8").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("UTF-16BE").name());
            log.info("Charset UTF-16BE = " + ascii.toString());
            ascii = new String(b, 192, 32, Charset.forName("UTF-16BE").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 224, 32, Charset.forName("UTF-16BE").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("UTF-16LE").name());
            log.info("Charset UTF-16LE = " + ascii.toString());
            ascii = new String(b, 192, 32, Charset.forName("UTF-16LE").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 224, 32, Charset.forName("UTF-16LE").name());
            log.info("\t\tSmall = " + ascii);
            ascii = new String(b, Charset.forName("UTF-16").name());
            log.info("Charset UTF-16 = " + ascii.toString());
            ascii = new String(b, 192, 32, Charset.forName("UTF-16").name());
            log.info("\t\tCapital = " + ascii);
            ascii = new String(b, 224, 32, Charset.forName("UTF-16").name());
            log.info("\t\tSmall = " + ascii);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return fail("new String(byte[256], String) is crashed.");
        }
        log.info("\n==========================================================\n");
        log.info("String byte from 0 to 257 = " + ascii257.toString());
        try {
            ascii257 = new String(b1, Charset.forName("US-ASCII").name());
            log.info("Charset US-ASCII = " + ascii257.toString());
            ascii257 = new String(b1, Charset.forName("ISO-8859-1").name());
            log.info("Charset ISO-8859-1 = " + ascii257.toString());
            ascii257 = new String(b1, Charset.forName("UTF-8").name());
            log.info("Charset UTF-8 = " + ascii257.toString());
            ascii257 = new String(b1, Charset.forName("UTF-16BE").name());
            log.info("Charset UTF-16BE = " + ascii257.toString());
            ascii257 = new String(b1, Charset.forName("UTF-16LE").name());
            log.info("Charset UTF-16LE = " + ascii257.toString());
            ascii257 = new String(b1, Charset.forName("UTF-16").name());
            log.info("Charset UTF-16 = " + ascii257.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return fail("new String(byte[257], String) is crashed.");
        }

        try {
            str = testArgs[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            str = "To change the template for this generated file";
        }
        b = str.getBytes();
        log.info("\n\nThe String argument = \"" + new String(b) + "\"\n");
        for (int i = 0; i < b.length; i++) {
            if ((((int)b[i] > 58 && (int)b[i] < 64) || (int)b[i] < 48
                || ((int)b[i] > 91 && (int)b[i] < 97) || (int)b[i] > 123)
                && (int)b[i] != 32) {
                log
                    .info("The String argument include non Latin string symbols:\t(char)"
                        + (char)b[i] + " = (code)" + (int)b[i]);
            }
        }
        try {
            str = testArgs[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            str = "";
        }
        b = str.getBytes();
        log.info("\n\nThe String argument = \"" + new String(b) + "\"\n");
        for (int i = 0; i < b.length; i++) {
            if (((int)b[i] > 256 || (int)b[i] < 192) && (int)b[i] != 32) {
                log
                    .info("The String argument include non Russian string symbols: (char)"
                        + (char)b[i] + " = (code)" + (int)b[i]);
            }
        }
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_11().test(args));
    }
}