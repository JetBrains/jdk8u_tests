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

package org.apache.harmony.test.func.api.java.util.regex.Pattern;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.harmony.test.func.api.java.util.regex.share.CharSeq;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class PatternTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new PatternTest().test(args));
    }

    public static String[] getCorrectPatterns() {
        return new String[] {
                "|(?<![^\\062\\0271\\ta\\e\\07\\<\\=\\0114\\x13p\\a\\05\\0362t\\x71|\\a-\\?]{0,})|[|\\uEE04\\xC2\\uF721\\f\\ej\\uD552\\u538C\\022\\047w\\x3F\\\\|\\065-\\uBF15[|\\02-\\uEDFC|\\00-\\0325\\p{Blank}]]\\Z(?![^|\\r\\00\\u4166l\\u9468\\06\\07x\\0160w\\0353\\n\\a\\0227\\xD3\\?&&[\\0167-\\0172[^\\x80\\x79\\07\\fK\\0053\\020\\x95\\\\\\045\\?\\05\\032\\05\\036\\uDBA6\\u2E73\\0045Q]]])(?=[|\\03-\\u2F01&&[^\\03-\\u52F1|\\05\\045\\xD1\\0210\\xAE\\xA6h\\02n\\0374Ep\\xE6\\e\\{\\x5D\\0310\\x72\\03[^|U\\u3F25\\uACA1\\xA4\\007\\?\\000\\]\\06|\\02-\\,&&[|\\\\-\\0203|\\u1F83\\x7F\\xC9\\f\\xBD\\t\\u33AD\\u2EFC\\0217\\u4BE8\\0151\\?\\u63AE\\004\\x12|\\0132-\\0210]]]])|(?!${1,})(?=^*+)",
                "(?<=[|\\u248F\\x2Ej\\01\\,\\02\\02\\0256n\\u7B84\\0363\\xBC[^v\\\\\\023\\04\\071\\\\\\|\\006\\0146\\,\\072\\=\\07y\\03\\03\\u67AD\\02\\0203]]{3}+)|(?<=\\p{Lu})|(?<=[^|\\042\\x7D\\f\\u0357\\uC47B\\02\\xC7\\\\\\xB8\\|\\0225\\002[^\\<-\\xF8[^|\\017-\\x30\\01\\u1306\\0072\\077\\}\\013\\01\\005&&\\p{L}]]]{2,8}?)(?>^)",
                "(?xsus)(?=\\p{InGreek})|(?-x:[^|\\uFC70\\02j\\n\\x9B\\0326\\x42\\<\\f\\uF03B\\n\\03&&\\p{Digit}]*+)|(?<![^|\\005\\e\\u492FM\\u1304\\x82\\|\\xE5\\003\\072\\xBA\\0353\\04\\0164\\02\\07\\f\\013\\n-\\0232|\\u9A0F-\\u9E2F])|(?!\\z+)(?i-xi)|(?<![\\023-\\x7E[^\\04-\\x37[^q-\\|[|\\t-F&&[|\\0046-\\|\\0306-\\u8457&&[|\\020\\xF6\\r\\{\\r\\\\\\xCD\\]\\0314\\05\\xDD\\06\\00\\014\\]\\065g-\\u727D]]]]]])|(?dds)|(?<![^\\<-\\0260\\r-\\\\]?)(?=\\B{5,})(?!^\\d*+)(?<![\\x16-\\062[|\\f-\\046X\\02\\02\\u5DC0\\00\\x49\\=\\0071\\u57CE\\uA785\\0365\\x6B\\u6069\\0145\\04&&[|\\030\\x41\\0154\\077\\?\\004\\f\\{\\0031\\\\\\,|\\u293F\\057\\u8BB5\\u521E\\013\\013\\uAC1F\\017\\030\\xB1\\xE4\\012\\01\\,\\uDA71\\04]]]+)",
                "|(?<![^\\x4B-\\xAF|\\073\\t\\]\\x05\\0314a\\075\\0240\\0146\\u446C\\xC5[^\\n\\0167\\0145\\0111\\0023\\02\\0227\\uDB38\\xB5\\x63\\036\\<h\\0376\\004\\{\\046\\|[|\\0211\\x9B\\n\\023\\x0B\\03\\a\\0003\\0067]]])|(?<=[\\03\\]\\0122\\x43Y\\|\\xC1\\0344\\u90C0|\\03\\]\\035\\u25B1\\u7715\\0240\\05G\\0160\\002E\\?\\00&&[^|\\0205-\\uD5D4[^\\,-\\0064^\\p{Alpha}]]])|(?xx:[\\02-\\x7E\\x7E\\x84\\=\\0222\\r\\0246W\\0236-\\xD4]?+)",
                "(?![^\\<C\\f\\0146\\02\\07\\0320\\01j\\xDB\\xDF\\04\\03\\0050\\0270\\x45\\}\\00\\x3A\\06&&[|\\02-\\x3E\\r-\\}|X-\\|]]{7,}+)[|\\\\\\x98\\05\\ts\\<\\?\\u4FCFr\\,\\0025\\}\\004|\\0025-\\052\\a-\\061]|(?<![|\\01-\\u829E])|(?<!\\p{Alpha})|^|(?-s:[^\\x15\\\\\\x24F\\0365\\03\\020\\0324\\a\\,\\a\\u97D8\\07\\uFAFC[\\x38\\a\\051\\u306B\\040[\\0224-\\0306[^\\0020-\\u6A57]]]]??)(?uxix:[^|\\{\\[\\0367\\t\\e\\x8C\\{\\[\\074c\\]V[|b\\fu\\xA1\\0221\\n\\x60\\uC73B\\026q\\0270\\x26\\04M\\021\\01\\05\\u9994\\r\\054e\\026\\x96\\0175\\<\\07f\\066s[^D-\\x5D]]])(?xx:^{5,}+)(?uuu)(?=^\\D)|(?!\\G)(?>\\G*?)(?![^|\\]\\070\\ne\\{\\t\\[\\053\\?\\\\\\x51\\a\\075\\0023-\\[&&[|\\022-\\xEA\\00-\\u41C2&&[^|a-\\xCC&&[^\\037\\uECB3\\u3D9A\\x31\\|\\<b\\0206\\uF2EC\\01m\\,\\ak\\a\\03&&\\p{Punct}]]]])(?-dxs:[|\\06-\\07|\\e-\\x63&&[|Tp\\u18A3\\00\\|\\xE4\\05\\061\\015\\0116C|\\r\\{\\}\\006\\xEA\\0367\\xC4\\01\\0042\\0267\\xBB\\01T\\}\\0100\\?[|\\[-\\u459B|\\x23\\x91\\rF\\0376[|\\?-\\x94\\0113-\\\\\\s]]]]{6}?)(?<=[^\\t-\\x42H\\04\\f\\03\\0172\\?i\\u97B6\\e\\f\\uDAC2])(?=\\B*+)(?>[^\\016\\r\\{\\,\\uA29D\\034\\02[\\02-\\[|\\t\\056\\uF599\\x62\\e\\<\\032\\uF0AC\\0026\\0205Q\\|\\\\\\06\\0164[|\\057-\\u7A98&&[\\061-g|\\|\\0276\\n\\00\\x01t\\065\\x40\\05\\042\\011\\e\\xE8\\x64B\\04\\u6D0EDW^\\p{Lower}]]]]?)(?<=[^\\n\\\\\\t\\u8E13\\,\\0114\\x50\\u656E\\0004\\xA5\\]&&[\\03-\\026|\\uF39D\\0211J\\03\\fuN\\01\\0060\\nl\\0354\\{i\\u3BC2\\x1A\\01\\u14FE]])(?<=[^|\\uAE62\\x33\\001V\\0004\\x0B\\054H\\|\\}&&^\\p{Space}])(?sxx)(?<=[\\f\\006\\x9A\\055\\0221\\a\\r\\xB4]*+)|(?x-xd:^{5}+)()",
                "(?>\\B)|(?!\\Z)[^H\\0306\\012\\025\\u218A\\07\\=\\04\\xAB]{4,}?|(?<![^|\\0077-A])|(?xmm:\\z*+)|(?xxmd)()",
                "|(?sd:\\Z{5})(?-ss:[^|\\062-\\xC0|\\]-c]{7,14}+)",
                "(?<!^\\p{Graph})(?!\\b)^(?!^\\p{Graph}++)",
                "\\b|(?>[\\023-\\<&&\\p{Print}])|(?<![|\\053-\\0072]{7}?)(?!${5,}+)(?<!\\p{Graph})(?-md)",
                "|(?>[^\\e-r^\\p{Print}])(?x-ds)(?>^\\s{6,}+)",
                "(?<=[^\\0260-\\u3B35&&[\\a\\?\\0271\\xDCh\\uC2EDl\\050\\x9D\\0201]])|(?=\\A{3,})(?!\\A{2}?)|(?-x)",
                "(?![^\\05\\uC0FC\\0121\\00\\0205\\0333\\04\\0326\\x08\\uDC8E\\{\\056\\f\\<\\00\\xD5\\}\\0203\\xEBQ\\0160\\n\\063\\u3EC1\\nR\\f\\|\\07\\0240&&[^\\r-\\u6EED\\0240\\u9A9B\\x10\\u4688\\xEE\\0120\\?\\041\\u87B9&&[\\071\\t\\05\\076\\07\\0022\\0152\\00\\e\\03\\x67\\x9A\\075\\f\\]\\u52D4\\,^\\p{Upper}]]]{8,})^",
                "(?xdi:\\B)(?uxd)|^\\p{Space}",
                "(?<![^\\]\\u0568\\xAE\\xC5\\,x\\<\\0160\\a\\u5F3A\\007\\xF1b\\,e\\p{Upper}]?+)|^+|(?=\\B{7})", };
    }

    private static String[] getIncorrectPatterns() {
        String[] patterns = getCorrectPatterns();

        for (int i = 0; i < patterns.length; ++i) {
            patterns[i] += (patterns[i].length() % 2 == 0) ? ')' : '(';
        }
        return patterns;
    }

    public Result testCompile() {
        String[] patterns = getCorrectPatterns();

        for (int i = 0; i < patterns.length; ++i) {
            try {
                Pattern.compile(patterns[i]);
            } catch (Throwable e) {
                return failed("compilation of pattern :" + patterns[i]
                        + ": failed");
            }
        }

        patterns = getIncorrectPatterns();

        for (int i = 0; i < patterns.length; ++i) {
            try {
                Pattern.compile(patterns[i]);
                return failed("compilation of pattern :" + patterns[i]
                        + ": succeeded");
            } catch (Throwable e) {
            }
        }

        return passed();
    }

    private int getRandomFlags() {
        int flags = 0;
        int[] allFlags = { Pattern.CASE_INSENSITIVE, Pattern.MULTILINE,
                Pattern.DOTALL, Pattern.UNICODE_CASE, Pattern.CANON_EQ };

        for (int i = 0; i < allFlags.length; ++i) {
            if (new Double(Math.random()).hashCode() % 2 == 0) {
                flags |= allFlags[i];
            }
        }

        return flags;
    }

    public Result testCompileFlags() {
        String[] patterns = getCorrectPatterns();

        int flags = 0;
        for (int i = 0; i < patterns.length; ++i) {
            try {
                flags = getRandomFlags();
                Pattern.compile(patterns[i], flags);
            } catch (ArrayIndexOutOfBoundsException e) { //some bad 
                //implementations throw it - contact me for the minimal test case 
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
                return failed("compilation of pattern :" + patterns[i]
                        + ": with flags " + flags + " failed");
            }
        }

        patterns = getIncorrectPatterns();

        for (int i = 0; i < patterns.length; ++i) {
            try {
                flags = getRandomFlags();
                Pattern.compile(patterns[i], flags);
                return failed("compilation of pattern :" + patterns[i]
                        + ": with flags " + flags + " succeeded");
            } catch (Throwable e) {
            }
        }

        patterns = getCorrectPatterns(); //if flags contain some values that
        // are not pattern compile flags,
        //it shoudn't produce an error

        for (int i = 0; i < patterns.length; ++i) {
            try {
                flags = getRandomFlags();
                flags = (flags == 0) ? 16 : flags << 8;
                Pattern.compile(patterns[i], flags);
            } catch (Throwable e) {
                return failed("compilation of pattern :" + patterns[i]
                        + ": with flags " + flags + " failed 2");
            }
        }

        return passed();
    }

    public Result testFlags() {
        int flags;
        String s;

        for (int i = 0; i < 1000; ++i) {
            flags = getRandomFlags();
            s = "abcd" + Math.random();
            try {
                Pattern p = Pattern.compile(s, flags);
                if (p.flags() != flags) {
                    return failed("compile '" + s + "' with flags " + flags
                            + " .flags() returned another value");
                }

            } catch (Throwable e) {
                return failed("compile '" + s + "' with flags " + flags
                        + " failed");
            }
        }
        for (int i = 0; i < 1000; ++i) {
            flags = getRandomFlags();
            flags = (flags == 0) ? 16 : flags << 8;

            s = "abcd" + Math.random();
            try {
                Pattern p = Pattern.compile(s, flags);
                if (p.flags() != flags) {
                    return failed("compile '" + s + "' with flags " + flags
                            + " .flags() returned another value");
                }

            } catch (Throwable e) {
                return failed("compile '" + s + "' with flags " + flags
                        + " failed");
            }
        }
        return passed();
    }

    public Result testPattern() {
        int flags;
        String s;

        for (int i = 0; i < 1000; ++i) {
            flags = getRandomFlags();

            s = "AbCd\u0000" + Math.random();

            try {
                Pattern p = flags == 0 ? Pattern.compile(s) : Pattern.compile(
                        s, flags);
                if (p.pattern() != s) {
                    return failed("compile '" + s.replace((char) 0, '|')
                            + "' with flags " + flags
                            + " .pattern() returned another value");
                }

            } catch (Throwable e) {
                return failed("compile '" + s.replace((char) 0, '|')
                        + "' with flags " + flags + " failed");
            }
        }

        return passed();
    }

    public Result testSplit() {
        Pattern p;
        String[] patterns = new String[] { "e", "x", "", "ex", "hex regex",
                "ZZZ" };
        int[] limits = new int[] { Integer.MIN_VALUE, -1, 0, 1, 2,
                Integer.MAX_VALUE };
        String[][][] expectedResults = new String[][][] {
                { { "h", "x r", "g", "x", }, { "h", "x r", "g", "x", },
                        { "h", "x r", "g", "x", }, { "hex regex", },
                        { "h", "x regex", }, { "h", "x r", "g", "x", }, },
                { { "he", " rege", "", }, { "he", " rege", "", },
                        { "he", " rege", }, { "hex regex", },
                        { "he", " regex", }, { "he", " rege", "", }, },
                {
                        { "", "h", "e", "x", " ", "r", "e", "g", "e", "x", "", },
                        { "", "h", "e", "x", " ", "r", "e", "g", "e", "x", "", },
                        { "", "h", "e", "x", " ", "r", "e", "g", "e", "x", },
                        { "hex regex", },
                        { "", "hex regex", },
                        { "", "h", "e", "x", " ", "r", "e", "g", "e", "x", "", }, },
                { { "h", " reg", "", }, { "h", " reg", "", }, { "h", " reg", },
                        { "hex regex", }, { "h", " regex", },
                        { "h", " reg", "", }, },
                { { "", "", }, { "", "", }, {}, { "hex regex", }, { "", "", },
                        { "", "", }, },
                { { "hex regex", }, { "hex regex", }, { "hex regex", },
                        { "hex regex", }, { "hex regex", }, { "hex regex", }, } };

        for (int i = 0; i < patterns.length; ++i) {
            p = Pattern.compile(patterns[i]);
            for (int j = 0; j < limits.length; ++j) {
                String[] sarr = p.split("hex regex", limits[j]);
                if (!Arrays.equals(sarr, expectedResults[i][j])) {
                    return failed("'hex regex' split " + limits[j]
                            + " produced " + toString(sarr) + ", expected "
                            + toString(expectedResults[i][j]));
                }
            }
        }

        String[] sarr;
        String[] expected;

        p = Pattern.compile("E");
        sarr = p.split(new CharSeq("hex regex"));
        expected = new String[] { "hex regex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex regex' split on E produced " + toString(sarr)
                    + ", expected " + toString(expected));
        }

        p = Pattern.compile("E", Pattern.CASE_INSENSITIVE);
        sarr = p.split(new CharSeq("hex regex"));
        expected = new String[] { "h", "x r", "g", "x" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex regex' split on E with CASE_INSENSITIVE produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        //COMMENTS
        p = Pattern.compile("e#comments");
        sarr = p.split(new CharSeq("hex regex"));
        expected = new String[] { "hex regex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex regex' split on e#comments produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        p = Pattern.compile("e#comments", Pattern.COMMENTS);
        sarr = p.split(new CharSeq("hex regex"));
        expected = new String[] { "h", "x r", "g", "x" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex regex' split on 'e#comments' with COMMENTS flag produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        //MULTILINE
        p = Pattern.compile("^r");
        sarr = p.split(new CharSeq("hex\r\nregex"));
        expected = new String[] { "hex\r\nregex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\nregex' split on '^r' produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        p = Pattern.compile("^r", Pattern.MULTILINE);
        sarr = p.split(new CharSeq("hex\r\nregex"));
        expected = new String[] { "hex\r\n", "egex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\\r\\nregex' split on '^r' with MULTILINE produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        //DOTALL
        p = Pattern.compile("x.r");
        sarr = p.split(new CharSeq("hex\nregex"));
        expected = new String[] { "hex\nregex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\nregex' split on 'x.r' produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        p = Pattern.compile("x.r", Pattern.DOTALL);
        sarr = p.split(new CharSeq("hex\nregex"));
        expected = new String[] { "he", "egex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\nregex' split on 'x.r' with DOTALL 1 produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        p = Pattern.compile("x.r", Pattern.DOTALL);
        sarr = p.split(new CharSeq("hex\\nregex"));
        expected = new String[] { "hex\\nregex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\\nregex' split on 'x.r' with DOTALL 2 produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        //UNICODE_CASE
        p = Pattern.compile("\u0531"); //ARMENIAN CAPITAL LETTER AYB
        sarr = p.split(new CharSeq("hex\u0561regex"));
        expected = new String[] { "hex\u0561regex" };

        
        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\\u0561regex' split on '\\u0531' produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        p = Pattern.compile("\u0531", Pattern.UNICODE_CASE); //ARMENIAN CAPITAL
        // LETTER AYB
        sarr = p.split(new CharSeq("hex\u0561regex"));
        expected = new String[] { "hex\u0561regex" };
        if(System.getProperty("java.version").startsWith("1.5")) {
            expected = new String[] { "hex", "regex" };
        }

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\\u0561regex' split on '\\u0531' produced with UNICODE_CASE "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        p = Pattern.compile("\u0531", Pattern.CASE_INSENSITIVE); //ARMENIAN
        // CAPITAL
        // LETTER AYB
        sarr = p.split(new CharSeq("hex\u0561regex"));
        expected = new String[] { "hex\u0561regex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\\u0561regex' split on '\\u0531' produced with CASE_INSENSITIVE "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        p = Pattern.compile("\u0531", Pattern.UNICODE_CASE
                | Pattern.CASE_INSENSITIVE); //ARMENIAN CAPITAL LETTER AYB
        sarr = p.split(new CharSeq("hex\u0561regex"));
        expected = new String[] { "hex", "regex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\\u0561regex' split on '\\u0531' produced with UNICODE_CASE | CASE_INSENSITIVE "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        //canon_eq

        p = Pattern.compile("a\u0300"); //LATIN SMALL LETTER A WITH GRAVE
        sarr = p.split(new CharSeq("hex\u00E0regex"));
        expected = new String[] { "hex\u00E0regex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hexa\\u0060regex' split on '\\u00E0' produced "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        p = Pattern.compile("a\u0300", Pattern.CANON_EQ); //LATIN SMALL LETTER
        // A WITH GRAVE
        sarr = p.split(new CharSeq("hex\u00E0regex"));
        expected = new String[] { "hex", "regex" };

        if (!Arrays.equals(sarr, expected)) {
            return failed("'hex\\u00E0regex' split on 'a\\u0300' produced with CANON_EQ "
                    + toString(sarr) + ", expected " + toString(expected));
        }

        return passed();
    }

    public Result testMatches() {
        Object[][] tests = new Object[][] { { "a*b", "ab", Boolean.TRUE }, 
                {"a.*", "a", Boolean.TRUE},
                {"a", "abcd", Boolean.FALSE},
        };

        for (int i = 0; i < tests.length; ++i) {
            if (Pattern.matches((String) tests[i][0],
                    (CharSequence) tests[i][1]) != ((Boolean) tests[i][2])
                    .booleanValue()) {
                return failed("expected Pattern.matches('" + tests[i][0]
                        + "', '" + tests[i][1] + "') to return " + tests[i][2]);
            }
        }

        return passed();
    }

    static private String toString(String[] sarr) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        for (int k = 0; k < sarr.length; ++k) {
            if (k != 0) {
                sb.append(',');
            }
            sb.append('"');
            sb.append(sarr[k]);
            sb.append('"');
        }
        sb.append(']');
        return sb.toString();
    }
}