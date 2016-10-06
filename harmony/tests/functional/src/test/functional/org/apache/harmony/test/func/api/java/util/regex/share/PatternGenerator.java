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

package org.apache.harmony.test.func.api.java.util.regex.share;

import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternGenerator {

    private static Random rnd = new Random();

    private static int GROUPS = 0;

    private static boolean LOOKBEHIND = false;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; ++i) {
            String regex = getRandomRegex(100);
            try {
                Pattern.compile(regex);
                regex = regex.replaceAll("\\\\", "\\\\\\\\");
                System.out.println(regex);
            } catch (PatternSyntaxException e) {
                regex = regex.replaceAll("\\\\", "\\\\\\\\");
                System.err.println(regex);
                e.printStackTrace();
            }
        }
    }

    private static String getRandomCharacter() {
        switch (rnd.nextInt(8)) {
        case 0:
            String s = "" + (char) ('a' + rnd.nextInt('z' - 'a'));
            return rnd.nextBoolean() ? s.toUpperCase() : s.toLowerCase();
        case 1:
            String[] arr = new String[] { "\\\\", "\\t", "\\n", "\\r", "\\f",
                    "\\a", "\\e" };
            return arr[rnd.nextInt(arr.length)];
        case 2:
            return "\\0" + rnd.nextInt(8);
        case 3:
            return "\\0" + rnd.nextInt(8) + rnd.nextInt(8);
        case 4:
            return "\\0" + rnd.nextInt(4) + rnd.nextInt(8) + rnd.nextInt(8);
        case 5:
            return "\\x" + getRandomHexChar() + getRandomHexChar();
        case 6:
            return "\\u" + getRandomHexChar() + getRandomHexChar()
                    + getRandomHexChar() + getRandomHexChar();
        case 7: //quoted special characters
            char[] c = new char[] { '?', '[', ']', '}', '{', '|', '=', '<', ',' };
            return "\\" + c[rnd.nextInt(c.length)];
        }
        return "";
    }

    private static String getRandomCharacterGroup() {
        switch (rnd.nextInt(2)) {
        case 0:
            StringBuffer sb = new StringBuffer();
            for (int i = 0, limit = 5 + rnd.nextInt(15); i < limit; ++i) {
                sb.append(getRandomCharacter());
            }
            return sb.toString();
        case 1:
            String interval = "";
            while (true) {
                interval = getRandomCharacter() + "-" + getRandomCharacter();
                try {
                    Pattern.compile("[" + interval + "]");
                    return interval;
                } catch (PatternSyntaxException e) {
                }
            }
        //           case 2:
        //           return "\\Q" + getRandomString() + "\\E";
        }
        return "";
    }

    private static String getRandomCharacterClass(int depth) {
        --depth;
        StringBuffer sb = new StringBuffer();
        if (rnd.nextBoolean()) {
            sb.append('^');
        }

        switch (rnd.nextInt((depth <= 0) ? 2 : 4)) {
        case 0:
            for (int i = 0, limit = rnd.nextInt(3) + 1; i < limit; ++i) {
                if (rnd.nextBoolean()) {
                    sb.append('|');
                }
                sb.append(getRandomCharacterGroup());
            }
            break;
        case 1:
            String[] arr = new String[] { ".", "\\d", "\\D", "\\s", "\\S",
                    "\\w", "\\W", "\\p{Lower}", "\\p{Upper}", "\\p{ASCII}",
                    "\\p{Alpha}", "\\p{Digit}", "\\p{Alnum}", "\\p{Punct}",
                    "\\p{Graph}", "\\p{Print}", "\\p{Blank}", "\\p{Cntrl}",
                    "\\p{XDigit}", "\\p{Space}", "\\p{InGreek}", "\\p{Lu}",
                    "\\p{L}", "\\p{Sc}", "\\P{InGreek}" };
            sb.append(arr[rnd.nextInt(arr.length)]);
            return sb.toString(); //return now to avoid bracketing
        case 2:
            for (int i = 0, limit = rnd.nextInt(2) + 1; i < limit; ++i) {
                if (rnd.nextBoolean()) {
                    sb.append('|');
                }
                sb.append(getRandomCharacterGroup());
            }
            sb.append(getRandomCharacterClass(depth));
            break;
        case 3:
            for (int i = 0, limit = rnd.nextInt(2) + 1; i < limit; ++i) {
                if (rnd.nextBoolean()) {
                    sb.append('|');
                }
                sb.append(getRandomCharacterGroup());
            }
            sb.append("&&");
            sb.append(getRandomCharacterClass(depth));
        }
        return "[" + sb.toString() + "]";
    }

    private static String getQuantifiedGroup(int depth) {
        String ret = "";
        int casesApplicable = 3;
        if (GROUPS == 0) {
            casesApplicable = 2;
        }
        if (LOOKBEHIND) {
            casesApplicable = 1;
        }

        switch (rnd.nextInt(casesApplicable)) {
        case 0:
            ret = getRandomCharacterClass(depth);
            break;
        case 1:
            String[] arr = new String[] { "^", "$", "\\b", "\\B", "\\A", "\\G",
                    "\\Z", "\\z" };
            ret = arr[rnd.nextInt(arr.length)];
            break;
        case 2:
            ret = "\\" + (rnd.nextInt(GROUPS) + 1);
            break;
        }

        if (rnd.nextBoolean()) {
            return ret; //non-quantified
        }

        switch (rnd.nextInt(6)) {
        case 0:
            ret += '?';
            break;
        case 1:
            ret += '*';
            break;
        case 2:
            ret += '+';
            break;
        case 3:
            ret += "{" + rnd.nextInt(10) + "}";
            break;
        case 4:
            ret += "{" + rnd.nextInt(10) + ",}";
            break;
        case 5:
            int start = rnd.nextInt(10);
            ret += "{" + start + "," + (start + rnd.nextInt(10)) + "}";
            break;
        }

        switch (rnd.nextInt(3)) {
        case 0:
            break;
        case 1:
            ret += '?';
            break;
        case 2:
            ret += '+';
            break;
        }

        return ret;
    }

    private static String getFlags() {
        StringBuffer sb = new StringBuffer();
        boolean dashPlaced = false;

        for (int i = 0, limit = 2 + rnd.nextInt(3); i < limit; ++i) {

            switch (rnd.nextInt(7)) {
            case 0:
                sb.append('i');
                break;
            case 1:
                sb.append('d');
                break;
            case 2:
                sb.append('m');
                break;
            case 3:
                sb.append('s');
                break;
            case 4:
                sb.append('u');
                break;
            case 5:
                sb.append('x');
                break;
            case 6:
                if (!dashPlaced) {
                    sb.append('-');
                    dashPlaced = true;
                }
                break;
            }
        }

        return sb.toString();
    }

private static String getNonCapturingRegex(int depth) {
        --depth;
        StringBuffer sb = new StringBuffer();
        for (int i = 0, limit = 2 + rnd.nextInt(3); i < limit; ++i) {
            if (rnd.nextBoolean()) {
                sb.append('|');
            }

            switch (rnd.nextInt(9)) {
            case 0:
                sb.append(getQuantifiedGroup(depth));
                break;
            case 1:
                sb.append("(?" + getFlags() + ")");
                break;
            case 2:
                sb.append("(?" + getFlags()+ ":").append(getQuantifiedGroup(depth))
                        .append(')');
                break;
            case 3:
                sb.append("(?=").append(getQuantifiedGroup(depth)).append(')');
                break;
            case 4:
                sb.append("(?!").append(getQuantifiedGroup(depth)).append(')');
                break;
            case 5:
                LOOKBEHIND = true;
                sb.append("(?<=").append(getQuantifiedGroup(depth)).append(')');
                LOOKBEHIND = false;
                break;
            case 6:
                LOOKBEHIND = true;
                sb.append("(?<!").append(getQuantifiedGroup(depth)).append(')');
                LOOKBEHIND = false;
                break;
            case 7:
                sb.append("(?>").append(getQuantifiedGroup(depth)).append(')');
                break;
            case 8:
                sb.append(getNonCapturingRegex(depth));
                break;
            }
        }
        return sb.toString();
    }    private static String getCapturingRegex(int depth) {
        --depth;
        StringBuffer sb = new StringBuffer();
        int openBrackets = 0;
        for (int i = 0, limit = 2 + rnd.nextInt(10); i < limit; ++i) {
            switch (rnd.nextInt(5)) {
            case 0:
                sb.append('(');
                openBrackets++;
                break;
            case 1:
                if (openBrackets > 0 && sb.charAt(sb.length() - 1) != '(') {
                    sb.append(')');
                    ++GROUPS;
                    openBrackets--;
                }
                break;
            default:
                sb.append(getNonCapturingRegex(depth));
            }
        }
        for (int i = 0; i < openBrackets; ++i) {
            sb.append(')');
        }
        return sb.toString();
    }

    private static String getRandomRegex(int depth) {
        GROUPS = 0;
        switch (rnd.nextInt(2)) {
        case 0:
            return getCapturingRegex(depth);
        case 1:
            return getNonCapturingRegex(depth);
        }
        return "";
    }

    private static String getRandomHexChar() {
        int i = rnd.nextInt(16);
        return "" + (i < 10 ? "" + i : "" + (char) ('A' + i - 10));
    }

    private static String getRandomString() {
        String chars = "\t!\"#$%&'()*+,-./0123456789;:<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmopqrstuvwxyz{|}~";
        StringBuffer sb = new StringBuffer();
        for (int i = 0, limit = 1 + rnd.nextInt(20); i < limit; ++i) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}

