/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Oleg Oleinik
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.reliability.api.nio.charset;

import org.apache.harmony.test.reliability.share.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Vector;

/**
 * Goal: find memory leaks caused by wrong management of Charset cache in case of
 *       accessing Charset via non-IANA names, like "UTF8", "UTF16".
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize one encoder in a cycle
 *
 *    2. For each of encodingName which does not cause UnsupportedEncodingException but 
 *       nevertheless differ from canonical name or alias, it calls String(new byte[0], encodingName)
 *       in a cycle for param[0] iterations. It tries to force Charset cache to create new Encoder
 *       object instead of re-using already created, since, it is possible that cache works
 *       using canonical names or aliases, but, misses "unofficial" names, like "UTF8".
 *
 */

public class WrongCharsetNameTest extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ENCODING_ITERATIONS = 100;

    // These are non-IANA names of charsets which are accepted as names for 
    // creation of encoder (UnsupportedEncodingException is not thrown)

    // Must contain at least "UTF8", "UTF16".
    public String[] encodingName;

    private String s = "";
    private CharBuffer ch_buf = null;


    public static void main(String[] args) {
        System.exit(new WrongCharsetNameTest().test(args));
    }


    public int test(String[] params) {

        parseParams(params);

        // First, receive names of encodings which are not canonical or alias names, 
        // but are acceptable as valid encoding names.

        encodingName = EncodingNames.getNames();

        // log.add("Selected " + encodingName.length + " names");

        for (int j = 0; j < encodingName.length; ++j){

            //log.add("Iteration " + encodingName[j]);
                
            for (int i = 0; i < NUMBER_OF_ENCODING_ITERATIONS; ++i){

                // log.add("Iteration " + i + ", " + encodingName[j]);

                try {

                    s = new String(new byte[0], encodingName[j]);

                } catch (UnsupportedEncodingException uee){

                    log.add("Unsupported " + encodingName[j]);

                }

                if (callSystemGC !=0 ){
                    System.gc();
                }
            }
        }
        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ENCODING_ITERATIONS = Integer.parseInt(params[0]);
        }        

    }

}


class EncodingNames {

    public static String[] getNames(){

        // lets select only those chrset names from encodingNIOIONames 
        // which are valid (i.e. forName() returns Charset) but, are not 
        // canonical name or alias.

        Vector v = new Vector();

        // System.out.println("Total: " + encodingNIOIONames.length);

        for (int i = 0; i < encodingNIOIONames.length; ++i){

            try {

                Charset c = Charset.forName(encodingNIOIONames[i]);

                if (c.name().equals(encodingNIOIONames[i])){
                    continue;
                }

                Object[] aliases = c.aliases().toArray();

                boolean isAlias = false;

                for (int j = 0; j < aliases.length; ++j){
                    if (aliases[j].equals(encodingNIOIONames[i])){
                        isAlias = true;
                        break;
                    }
                }

                if (!isAlias){
                    v.add(encodingNIOIONames[i]);
                }

            } catch(IllegalCharsetNameException icne){
            } catch (UnsupportedCharsetException uce){
            }
        }

        return (String[]) v.toArray(new String[0]);

    }


    public static String[] encodingNIOIONames = 
        {
            "ISO-8859-1",
            "ISO8859_1",
            "ISO-8859-2",
            "ISO8859_2",
            "ISO-8859-4",
            "ISO8859_4",
            "ISO-8859-5",
            "ISO8859_5",
            "ISO-8859-7",
            "ISO8859_7",
            "ISO-8859-9",
            "ISO8859_9",
            "ISO-8859-13",
            "ISO8859_13",
            "ISO-8859-15",
            "ISO8859_15",
            "KOI8-R",
            "KOI8_R",
            "US-ASCII",
            "ASCII",
            "UTF-8",
            "UTF8",    // for sure
            "UTF-16",
            "UTF16",   // for sure
            "UTF-16BE",
            "UnicodeBigUnmarked",
            "UTF-16LE",
            "UnicodeLittleUnmarked",
            "windows-1250",
            "Cp1250",
            "windows-1251",
            "Cp1251",
            "windows-1252",
            "Cp1252",
            "windows-1253",
            "Cp1253",
            "windows-1254",
            "Cp1254",
            "windows-1257",
            "Cp1257",
            "UnicodeBig",
            "UnicodeLittle",
            "Big5",
            "Big5",
            "Big5-HKSCS",
            "Big5_HKSCS",
            "EUC-JP",
            "EUC_JP",
            "EUC-KR",
            "EUC_KR",
            "GB18030",
            "GB18030",
            "GB2312",
            "EUC_CN",
            "GBK",
            "GBK",
            "IBM-Thai",
            "Cp838",
            "IBM00858",
            "Cp858",
            "IBM01140",
            "Cp1140",
            "IBM01141",
            "Cp1141",
            "IBM01142",
            "Cp1142",
            "IBM01143",
            "Cp1143",
            "IBM01144",
            "Cp1144",
            "IBM01145",
            "Cp1145",
            "IBM01146",
            "Cp1146",
            "IBM01147",
            "Cp1147",
            "IBM01148",
            "Cp1148",
            "IBM01149",
            "Cp1149",
            "IBM037",
            "Cp037",
            "IBM1026",
            "Cp1026",
            "IBM1047",
            "Cp1047",
            "IBM273",
            "Cp273",
            "IBM277",
            "Cp277",
            "IBM278",
            "Cp278",
            "IBM280",
            "Cp280",
            "IBM284",
            "Cp284",
            "IBM285",
            "Cp285",
            "IBM297",
            "Cp297",
            "IBM420",
            "Cp420",
            "IBM424",
            "Cp424",
            "IBM437",
            "Cp437",
            "IBM500",
            "Cp500",
            "IBM775",
            "Cp775",
            "IBM850",
            "Cp850",
            "IBM852",
            "Cp852",
            "IBM855",
            "Cp855",
            "IBM857",
            "Cp857",
            "IBM860",
            "Cp860",
            "IBM861",
            "Cp861",
            "IBM862",
            "Cp862",
            "IBM863",
            "Cp863",
            "IBM864",
            "Cp864",
            "IBM865",
            "Cp865",
            "IBM866",
            "Cp866",
            "IBM868",
            "Cp868",
            "IBM869",
            "Cp869",
            "IBM870",
            "Cp870",
            "IBM871",
            "Cp871",
            "IBM918",
            "Cp918",
            "ISO-2022-CN",
            "ISO2022CN",
            "ISO-2022-JP",
            "ISO2022JP",
            "ISO-2022-KR",
            "ISO2022KR",
            "ISO-8859-3",
            "ISO8859_3",
            "ISO-8859-6",
            "ISO8859_6",
            "ISO-8859-8",
            "ISO8859_8",
            "Shift_JIS",
            "SJIS",
            "TIS-620",
            "TIS620",
            "windows-1255",
            "Cp1255",
            "windows-1256",
            "Cp1256",
            "windows-1258",
            "Cp1258",
            "windows-31j",
            "MS932",
            "x-Big5_Solaris",
            "Big5_Solaris",
            "x-euc-jp-linux",
            "EUC_JP_LINUX",
            "x-EUC-TW",
            "EUC_TW",
            "x-eucJP-Open",
            "EUC_JP_Solaris",
            "x-IBM1006",
            "Cp1006",
            "x-IBM1025",
            "Cp1025",
            "x-IBM1046",
            "Cp1046",
            "x-IBM1097",
            "Cp1097",
            "x-IBM1098",
            "Cp1098",
            "x-IBM1112",
            "Cp1112",
            "x-IBM1122",
            "Cp1122",
            "x-IBM1123",
            "Cp1123",
            "x-IBM1124",
            "Cp1124",
            "x-IBM1381",
            "Cp1381",
            "x-IBM1383",
            "Cp1383",
            "x-IBM33722",
            "Cp33722",
            "x-IBM737",
            "Cp737",
            "x-IBM856",
            "Cp856",
            "x-IBM874",
            "Cp874",
            "x-IBM875",
            "Cp875",
            "x-IBM921",
            "Cp921",
            "x-IBM922",
            "Cp922",
            "x-IBM930",
            "Cp930",
            "x-IBM933",
            "Cp933",
            "x-IBM935",
            "Cp935",
            "x-IBM937",
            "Cp937",
            "x-IBM939",
            "Cp939",
            "x-IBM942",
            "Cp942",
            "x-IBM942C",
            "Cp942C",
            "x-IBM943",
            "Cp943",
            "x-IBM943C",
            "Cp943C",
            "x-IBM948",
            "Cp948",
            "x-IBM949",
            "Cp949",
            "x-IBM949C",
            "Cp949C",
            "x-IBM950",
            "Cp950",
            "x-IBM964",
            "Cp964",
            "x-IBM970",
            "Cp970",
            "x-ISCII91",
            "ISCII91",
            "x-ISO2022-CN-CNS",
            "ISO2022_CN_CNS",
            "x-ISO2022-CN-GB",
            "ISO2022_CN_GB",
            "x-iso-8859-11",
            "x-iso-8859-11",
            "x-JISAutoDetect",
            "JISAutoDetect",
            "x-Johab",
            "x-Johab",
            "x-MacArabic",
            "MacArabic",
            "x-MacCentralEurope",
            "MacCentralEurope",
            "x-MacCroatian",
            "MacCroatian",
            "x-MacCyrillic",
            "MacCyrillic",
            "x-MacDingbat",
            "MacDingbat",
            "x-MacGreek",
            "MacGreek",
            "x-MacHebrew",
            "MacHebrew",
            "x-MacIceland",
            "MacIceland",
            "x-MacRoman",
            "MacRoman",
            "x-MacRomania",
            "MacRomania",
            "x-MacSymbol",
            "MacSymbol",
            "x-MacThai",
            "MacThai",
            "x-MacTurkish",
            "MacTurkish",
            "x-MacUkraine",
            "MacUkraine",
            "x-MS950-HKSCS",
            "MS950_HKSCS",
            "x-mswin-936",
            "MS936",
            "x-PCK",
            "PCK",
            "x-windows-874",
            "MS874",
            "x-windows-949",
            "MS949",
            "x-windows-950",
            "MS950"
        };

}
