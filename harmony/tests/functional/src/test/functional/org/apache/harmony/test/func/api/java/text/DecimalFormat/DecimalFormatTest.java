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
/** 
 */  

package org.apache.harmony.test.func.api.java.text.DecimalFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.harmony.test.func.api.java.text.share.framework.TextTestFramework;
import org.apache.harmony.share.Result;

/**
 */
public class DecimalFormatTest extends TextTestFramework {

    private Number[] getParsedIntDouble(boolean testIntInstance) {
        if (!testIntInstance) {
            Number[] parsedIntDouble = { new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(3l),
                    new Long(31l), new Long(314l), new Long(3141l),
                    new Long(31415l), new Long(314159l), new Long(3141592l),
                    new Long(31415926l), new Long(314159265l), new Long(3l),
                    new Long(31l), new Long(314l), new Long(3l), new Long(31l),
                    new Long(314l), new Long(3l), new Long(31l),
                    new Long(314159265358979390l),
                    new Long(3141592653589793800l),
                    new Double(3.141592653589794E19d),
                    new Double(3.141592653589794E20d),
                    new Double(3.141592653589794E21d),
                    new Double(3.1415926535897943E22d),
                    new Double(3.141592653589794E23d),
                    new Double(3.141592653589794E24d),
                    new Double(3.141592653589794E25d),
                    new Double(3.141592653589794E26d),
                    new Double(3.141592653589794E27d),
                    new Double(3.141592653589794E28d), };
            return parsedIntDouble;
        } else {
            Number[] parsedIntDouble = { new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(3l),
                    new Long(31l), new Long(314l), new Long(3141l),
                    new Long(31415l), new Long(314159l), new Long(3141592l),
                    new Long(31415926l), new Long(314159265l), new Long(3l),
                    new Long(31l), new Long(314l), new Long(3l), new Long(31l),
                    new Long(314l), new Long(3l), new Long(31l),
                    new Long(314159265358979390l),
                    new Long(3141592653589793800l),
                    new Double(3.141592653589794E19d),
                    new Double(3.141592653589794E20d),
                    new Double(3.141592653589794E21d),
                    new Double(3.1415926535897943E22d),
                    new Double(3.141592653589794E23d),
                    new Double(3.141592653589794E24d),
                    new Double(3.141592653589794E25d),
                    new Double(3.141592653589794E26d),
                    new Double(3.141592653589794E27d),
                    new Double(3.141592653589794E28d) };
            return parsedIntDouble;
        }
    }

    private Number[] getParsedIntLong(boolean testIntInstance) {
        if (!testIntInstance) {
            Number[] parsedIntLong = { new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l), new Long(9l), new Long(9l),
                    new Long(9l), new Long(9l), new Long(9l), new Long(9l),
                    new Long(9l), new Long(9l), new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l), };
            return parsedIntLong;
        } else {
            Number[] parsedIntLong = { new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l), new Long(9l), new Long(9l),
                    new Long(9l), new Long(9l), new Long(9l), new Long(9l),
                    new Long(9l), new Long(9l), new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l), };
            return parsedIntLong;
        }
    }

    private Number[] getParsedDoubleDouble(boolean testIntInstance) {
        if (!testIntInstance) {
            Number[] parsedDoubleDouble = { new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(0l),
                    new Double(0.0030d), new Double(0.031d),
                    new Double(0.314d), new Double(3.142d),
                    new Double(31.416d), new Double(314.159d),
                    new Double(3141.593d), new Double(31415.927d),
                    new Double(314159.265d), new Double(3141592.654d),
                    new Double(3.1415926536E7d), new Double(3.14159265359E8d),
                    new Double(3.141d), new Double(31.415d),
                    new Double(314.159d), new Double(3.141d),
                    new Double(31.415d), new Double(314.159d),
                    new Double(3.141d), new Double(31.415d),
                    new Long(314159265358979390l),
                    new Long(3141592653589793800l),
                    new Double(3.141592653589794E19d),
                    new Double(3.141592653589794E20d),
                    new Double(3.141592653589794E21d),
                    new Double(3.1415926535897943E22d),
                    new Double(3.141592653589794E23d),
                    new Double(3.141592653589794E24d),
                    new Double(3.141592653589794E25d),
                    new Double(3.141592653589794E26d),
                    new Double(3.141592653589794E27d),
                    new Double(3.141592653589794E28d), };
            return parsedDoubleDouble;
        } else {
            Number[] parsedDoubleDouble = { new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(0l),
                    new Long(0l), new Long(0l), new Long(0l), new Long(0l),
                    new Long(0l), new Double(0.031d), new Double(0.314d),
                    new Double(3.142d), new Double(31.416d),
                    new Double(314.159d), new Double(3141.593d),
                    new Double(31415.927d), new Double(314159.265d),
                    new Double(3141592.654d), new Double(3.1415926536E7d),
                    new Double(3.14159265359E8d), new Double(3.141d),
                    new Double(31.415d), new Double(314.159d),
                    new Double(3.141d), new Double(31.415d),
                    new Double(314.159d), new Double(3.141d),
                    new Double(31.415d), new Long(314159265358979390l),
                    new Long(3141592653589793800l),
                    new Double(3.141592653589794E19d),
                    new Double(3.141592653589794E20d),
                    new Double(3.141592653589794E21d),
                    new Double(3.1415926535897943E22d),
                    new Double(3.141592653589794E23d),
                    new Double(3.141592653589794E24d),
                    new Double(3.141592653589794E25d),
                    new Double(3.141592653589794E26d),
                    new Double(3.141592653589794E27d),
                    new Double(3.141592653589794E28d), };
            return parsedDoubleDouble;
        }

    }

    private Number[] getParsedDoubleLong(boolean testIntInstance) {
        if (!testIntInstance) {
            Number[] parsedDoubleLong = { new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l), new Double(9.223d),
                    new Double(9.223d), new Double(9.223d), new Double(9.223d),
                    new Double(9.223d), new Double(9.223d), new Double(9.223d),
                    new Double(9.223d), new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l), };
            return parsedDoubleLong;
        } else {
            Number[] parsedDoubleLong = { new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l), new Double(9.223d),
                    new Double(9.223d), new Double(9.223d), new Double(9.223d),
                    new Double(9.223d), new Double(9.223d), new Double(9.223d),
                    new Double(9.223d), new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l),
                    new Long(9223372036854775783l), };
            return parsedDoubleLong;
        }
    }

    private String[] getFormattedDouble(boolean testIntInstance) {
        if (!testIntInstance) {
            String[] formattedDouble = { "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0.003", "0.031", "0.314", "3.142",
                    "31.416", "314.159", "3,141.593", "31\u00a0415.927",
                    "314\u00a0159,265", "3\u00a0141\u00a0592,654", "31\u00a0415\u00a0926,536",
                    "314\u00a0159\u00a0265,359", "3,141,592,653,59", "31,415,926,535,9",
                    "314,159,265,358,98", "B,@C@,DHA,EDB,DGH,FH",
                    "B@,C@D,HAE,DBD,GHF,HC", "B@C,@DH,AED,BDG,HFH,C",
                    "B,@C@,DHA,EDB,DGH,FHC", "B@,C@D,HAE,DBD,GHF,HC?",
                    "B@C'@DH'AED'BDG'HFH'BH?", "B'@C@'DHA'EDB'DGH'FHB'G??",
                    "B@'C@D'HAE'DBD'GHF'HC?'???",
                    "B@C'@DH'AED'BDG'HFH'C??'???",
                    "B'@C@'DHA'EDB'DGH'FHC'???'???",
                    "B@'C@D'HAE'DBD'GHF'HCB'???'???",
                    "B@C'@DH'AED'BDG'HFH'C??'???'???",
                    "B'@C@'DHA'EDB'DGH'FHC'???'???'???",
                    "B@.C@D.HAE.DBD.GHF.HC?.???.???.???",
                    "B@C.@DH.AED.BDG.HFH.C??.???.???.???",
                    "B.@C@.DHA.EDB.DGH.FHC.???.???.???.???",
                    "B@.C@D.HAE.DBD.GHF.HC?.???.???.???.???", };
            return formattedDouble;
        } else {
            String[] formattedDouble = { "0", "0", "0", "0", "0", "0", "0",
                    "0", "0", "0", "0", "0.031", "0.314", "3.142", "31.416",
                    "314.159", "3,141.593", "31\u00a0415.927", "314\u00a0159,265",
                    "3\u00a0141\u00a0592,654", "31\u00a0415\u00a0926,536", "314\u00a0159\u00a0265,359",
                    "3,141,592,653,59", "31,415,926,535,9",
                    "314,159,265,358,98", "B,@C@,DHA,EDB,DGH,FH",
                    "B@,C@D,HAE,DBD,GHF,HC", "B@C,@DH,AED,BDG,HFH,C",
                    "B,@C@,DHA,EDB,DGH,FHC", "B@,C@D,HAE,DBD,GHF,HC?",
                    "B@C'@DH'AED'BDG'HFH'BH?", "B'@C@'DHA'EDB'DGH'FHB'G??",
                    "B@'C@D'HAE'DBD'GHF'HC?'???",
                    "B@C'@DH'AED'BDG'HFH'C??'???",
                    "B'@C@'DHA'EDB'DGH'FHC'???'???",
                    "B@'C@D'HAE'DBD'GHF'HCB'???'???",
                    "B@C'@DH'AED'BDG'HFH'C??'???'???",
                    "B'@C@'DHA'EDB'DGH'FHC'???'???'???",
                    "B@.C@D.HAE.DBD.GHF.HC?.???.???.???",
                    "B@C.@DH.AED.BDG.HFH.C??.???.???.???",
                    "B.@C@.DHA.EDB.DGH.FHC.???.???.???.???",
                    "B@.C@D.HAE.DBD.GHF.HC?.???.???.???.???", };
            return formattedDouble;
        }
    }

    private String[] getFormattedLong(boolean testIntInstance) {
        if (!testIntInstance) {
            String[] formattedLong = { "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783", "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783",
                    "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783", "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783",
                    "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "H,AAB,BFA,?BE,GDC,FFD,FGB", "H,AAB,BFA,?BE,GDC,FFD,FGB",
                    "H,AAB,BFA,?BE,GDC,FFD,FGB", "H,AAB,BFA,?BE,GDC,FFD,FGB",
                    "H,AAB,BFA,?BE,GDC,FFD,FGB", "H'AAB'BFA'?BE'GDC'FFD'FGB",
                    "H'AAB'BFA'?BE'GDC'FFD'FGB", "H'AAB'BFA'?BE'GDC'FFD'FGB",
                    "H'AAB'BFA'?BE'GDC'FFD'FGB", "H'AAB'BFA'?BE'GDC'FFD'FGB",
                    "H'AAB'BFA'?BE'GDC'FFD'FGB", "H'AAB'BFA'?BE'GDC'FFD'FGB",
                    "H'AAB'BFA'?BE'GDC'FFD'FGB", "H.AAB.BFA.?BE.GDC.FFD.FGB",
                    "H.AAB.BFA.?BE.GDC.FFD.FGB", "H.AAB.BFA.?BE.GDC.FFD.FGB",
                    "H.AAB.BFA.?BE.GDC.FFD.FGB", };
            return formattedLong;
        } else {
            String[] formattedLong = { "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783", "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783",
                    "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783", "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783",
                    "9\u00a0223\u00a0372\u00a0036\u00a0854\u00a0775\u00a0783", "9,223,372,036,854,775,783",
                    "9,223,372,036,854,775,783", "9,223,372,036,854,775,783",
                    "H,AAB,BFA,?BE,GDC,FFD,FGB", "H,AAB,BFA,?BE,GDC,FFD,FGB",
                    "H,AAB,BFA,?BE,GDC,FFD,FGB", "H,AAB,BFA,?BE,GDC,FFD,FGB",
                    "H,AAB,BFA,?BE,GDC,FFD,FGB", "H'AAB'BFA'?BE'GDC'FFD'FGB",
                    "H'AAB'BFA'?BE'GDC'FFD'FGB", "H'AAB'BFA'?BE'GDC'FFD'FGB",
                    "H'AAB'BFA'?BE'GDC'FFD'FGB", "H'AAB'BFA'?BE'GDC'FFD'FGB",
                    "H'AAB'BFA'?BE'GDC'FFD'FGB", "H'AAB'BFA'?BE'GDC'FFD'FGB",
                    "H'AAB'BFA'?BE'GDC'FFD'FGB", "H.AAB.BFA.?BE.GDC.FFD.FGB",
                    "H.AAB.BFA.?BE.GDC.FFD.FGB", "H.AAB.BFA.?BE.GDC.FFD.FGB",
                    "H.AAB.BFA.?BE.GDC.FFD.FGB", };
            return formattedLong;
        }
    }

    private Object[] getTargets(DecimalFormat format,
            DecimalFormatSymbols symbols) {
        Object[] targets = { format, format, symbols, symbols, format, symbols,
                symbols, symbols, symbols, symbols, symbols, format, format,
                symbols, format, format, symbols, symbols, symbols, symbols,
                symbols, symbols, symbols, format, symbols, symbols, symbols,
                format, symbols, symbols, symbols, format, symbols, symbols,
                format, format, format, symbols, symbols, format, symbols,
                symbols };
        return targets;
    }

    private String[] getFields() {
        String[] fields = { "PositivePrefix", "NegativeSuffix", "NaN",
                "CurrencySymbol", "NegativeSuffix", "CurrencySymbol",
                "PatternSeparator", "ZeroDigit", "InternationalCurrencySymbol",
                "Percent", "CurrencySymbol", "MaximumFractionDigits",
                "GroupingSize", "InternationalCurrencySymbol",
                "NegativePrefix", "MinimumIntegerDigits", "DecimalSeparator",
                "GroupingSeparator", "DecimalSeparator", "PerMill",
                "CurrencySymbol", "Infinity", "GroupingSeparator",
                "MaximumFractionDigits", "Digit", "ZeroDigit",
                "CurrencySymbol", "NegativePrefix", "MonetaryDecimalSeparator",
                "CurrencySymbol", "GroupingSeparator", "NegativePrefix",
                "InternationalCurrencySymbol", "MinusSign",
                "MaximumIntegerDigits", "Multiplier", "MinimumFractionDigits",
                "InternationalCurrencySymbol", "GroupingSeparator",
                "NegativeSuffix", "MonetaryDecimalSeparator", "CurrencySymbol" };

        return fields;
    }

    private Object[] getValues() {
        Object[] values = { "", ")", "?", "\u02c6", "-", "USD",
                (new Character(';')), (new Character('0')), "SKK",
                (new Character('%')), "\u00f0\u00f3\u00e1", (new Integer(3)),
                (new Integer(3)), "AED", "-", (new Integer(1)),
                (new Character('.')), (new Character('\u00a0')),
                (new Character(',')), (new Character('\u2030')), "$C", "?",
                (new Character(',')), (new Integer(2)), (new Character('#')),
                (new Character('?')), "\u00a4", "", (new Character(',')), "HK$",
                (new Character('\'')), "(", "SVC", (new Character('-')),
                (new Integer(309)), (new Integer(1)), (new Integer(0)), "SIT",
                (new Character('.')), "", (new Character('.')), "Q" };

        return values;
    }

    private Class[] getTypes() {
        Class[] types = { String.class, String.class, String.class,
                String.class, String.class, String.class, char.class,
                char.class, String.class, char.class, String.class, int.class,
                int.class, String.class, String.class, int.class, char.class,
                char.class, char.class, char.class, String.class, String.class,
                char.class, int.class, char.class, char.class, String.class,
                String.class, char.class, String.class, char.class,
                String.class, String.class, char.class, int.class, int.class,
                int.class, String.class, char.class, String.class, char.class,
                String.class };

        return types;
    }

    private int[] getDigits() {
        int[] digits = { 3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3,
                8, 4, 6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 9, 5, 0, 2, 8, 8, 4, 1, 9,
                7, 1, 6 };
        return digits;
    }

    public int test_a(boolean testIntInstance) {
        try {

            DecimalFormat format = (DecimalFormat) (testIntInstance ? NumberFormat
                    .getIntegerInstance(Locale.US)
                    : NumberFormat.getInstance(Locale.US));
            DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();

            Object[] targets = getTargets(format, symbols);
            String[] fields = getFields();
            Object[] values = getValues();
            Class[] types = getTypes();
            int[] digits = getDigits();

            Number[] parsedIntDouble = getParsedIntDouble(testIntInstance);

            Number[] parsedIntLong = getParsedIntLong(testIntInstance);

            Number[] parsedDoubleDouble = getParsedDoubleDouble(testIntInstance);

            Number[] parsedDoubleLong = getParsedDoubleLong(testIntInstance);

            String[] formattedDouble = getFormattedDouble(testIntInstance);

            String[] formattedLong = getFormattedLong(testIntInstance);

            double dbl = 0;
            double delta = 1e-13;
            long lng = 9223372036854775783l;

            for (int i = 0; i < targets.length; i++) {
                setValueImpl(targets[i], fields[i], values[i], types[i]);
                format.setDecimalFormatSymbols(symbols);
                dbl *= 10;
                dbl += delta * digits[i];

                //format.applyPattern("###e00");

                String dblStr = format.format(dbl);
                if (!formattedDouble[i].equals(dblStr))
                    throw new Throwable(
                            "DecimalFormat.format(double) returns incorrect value\n"
                                    + "Expected:'" + formattedDouble[i] + "'\n"
                                    + "Returned:'" + dblStr + "'");

                String lngStr = format.format(lng);
                if (!formattedLong[i].equals(lngStr))
                    throw new Throwable(
                            "DecimalFormat.format(long) returns incorrect value\n"
                                    + "Expected:'" + formattedLong[i] + "'\n"
                                    + "Returned:'" + lngStr + "'");

                format.setParseIntegerOnly(true);
                Number parsedD1 = format.parse(dblStr);
                Number parsedD2 = format.parse(lngStr);

                format.setParseIntegerOnly(false);
                Number parsedD3 = format.parse(dblStr);
                Number parsedD4 = format.parse(lngStr);

                if (!parsedIntDouble[i].equals(parsedD1))
                    throw new Throwable(
                            "DecimalFormat.parse(string) returns incorrect value\n"
                                    + "Expected:'" + parsedIntDouble[i] + "'\n"
                                    + "Returned:'" + parsedD1 + "'\n"
                                    + "Parsing:'" + dblStr
                                    + "' parseIntegerOnly = true");

                if (!parsedIntLong[i].equals(parsedD2))
                    throw new Throwable(
                            "DecimalFormat.parse(string) returns incorrect value\n"
                                    + "Expected:'" + parsedIntLong[i] + "'\n"
                                    + "Returned:'" + parsedD2 + "'\n"
                                    + "Parsing:'" + lngStr
                                    + "' parseIntegerOnly = true");

                if (!parsedDoubleDouble[i].equals(parsedD3))
                    throw new Throwable(
                            "DecimalFormat.parse(string) returns incorrect value\n"
                                    + "Expected:'" + parsedDoubleDouble[i]
                                    + "'\n" + "Returned:'" + parsedD3  + "'\n"
                                    + "Parsing:'" + dblStr
                                    + "' parseIntegerOnly = false");

                if (!parsedDoubleLong[i].equals(parsedD4))
                    throw new Throwable(
                            "DecimalFormat.parse(string) returns incorrect value\n"
                                    + "Expected:'" + parsedDoubleLong[i]
                                    + "'\n" + "Returned:'" + parsedD4 + "'\n"
                                    + "Parsing:'" + lngStr
                                    + "' parseIntegerOnly = false");

            }
        } catch (Throwable e) {
            //e.printStackTrace();
            return fail(e.getMessage());
        }
        return pass();

    }

    public int test() {
        //return test_b();
        /**/
        int numberFormatResult = test_a(false);
        if (numberFormatResult != Result.PASS) {
            return numberFormatResult;
        }
        int integerFormatResult = test_a(true);
        return integerFormatResult;

    }

    private String numberToJavaString(Number number) {
        if (number.getClass().getName().equals("java.lang.Long"))
            return new String("new Long(" + number + "l)");
        else if (number.getClass().getName().equals("java.lang.Double"))
            return new String("new Double(" + number + "d)");
        else
            return new String("new " + number.getClass().getName() + "("
                    + number + ")");
    }

    public int test_b() {
        try {
            DecimalFormat format = (DecimalFormat) NumberFormat
                    .getIntegerInstance(Locale.US);
            DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();

            Object[] targets = getTargets(format, symbols);
            String[] fields = getFields();
            Object[] values = getValues();
            Class[] types = getTypes();
            int[] digits = getDigits();

            double dbl = 0;
            double delta = 1e-13;
            long lng = 9223372036854775783l;

            PrintStream parsedID = new PrintStream(new FileOutputStream(
                    new File("parsedID.csv")));
            PrintStream parsedIL = new PrintStream(new FileOutputStream(
                    new File("parsedIL.csv")));
            PrintStream parsedDD = new PrintStream(new FileOutputStream(
                    new File("parsedDD.csv")));
            PrintStream parsedDL = new PrintStream(new FileOutputStream(
                    new File("parsedDL.csv")));
            PrintStream formatedD = new PrintStream(new FileOutputStream(
                    new File("formatedD.csv")));
            PrintStream formatedL = new PrintStream(new FileOutputStream(
                    new File("formatedL.csv")));

            for (int i = 0; i < targets.length; i++) {
                setValueImpl(targets[i], fields[i], values[i], types[i]);
                format.setDecimalFormatSymbols(symbols);
                dbl *= 10;
                dbl += delta * digits[i];

                //format.applyPattern("###e00");

                String dblStr = format.format(dbl);
                String lngStr = format.format(lng);

                format.setParseIntegerOnly(true);
                Number parsedD1 = format.parse(dblStr);
                Number parsedD2 = format.parse(lngStr);

                format.setParseIntegerOnly(false);
                Number parsedD3 = format.parse(dblStr);
                Number parsedD4 = format.parse(lngStr);

                formatedD.print("\"" + dblStr + "\", ");
                formatedL.print("\"" + lngStr + "\", ");
                parsedDD.print(numberToJavaString(parsedD3) + ", ");
                parsedDL.print(numberToJavaString(parsedD4) + ", ");
                parsedID.print(numberToJavaString(parsedD1) + ", ");
                parsedIL.print(numberToJavaString(parsedD2) + ", ");

                System.out.print(parsedD1.getClass().getName() + ", ");
                System.out.print(parsedD2.getClass().getName() + ", ");
                System.out.print(parsedD3.getClass().getName() + ", ");
                System.out.print(parsedD4.getClass().getName() + "\n");

                //System.out.print(digits[i] + ", ");

            }
            formatedD.close();
            formatedL.close();
            parsedDD.close();
            parsedDL.close();
            parsedID.close();
            parsedIL.close();

        } catch (Throwable e) {
            e.printStackTrace();
            return fail(e.getMessage());
        }
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new DecimalFormatTest().test());
    }
}