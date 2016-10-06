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

package org.apache.harmony.test.func.api.java.util.regex.Matcher;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.harmony.test.func.api.java.util.regex.Pattern.PatternTest;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class MatcherTest extends MultiCase {
    private final static String baseString = "The Functional test suite is a "
                + "collection of "
                + "micro scenarios for "
                + "testing various functional parts of "
                + "an implementation.";

    public static void main(String[] args) {
        System.exit(new MatcherTest().test(args));
    }

    public Result testStartEnd() {
        int firstComma = baseString.indexOf(',');
        int commas = 4;

        Matcher m = Pattern.compile(",.").matcher(baseString);

        for (int i = 0; i < commas + 1; ++i) {
            for (int j = 0; j < i; ++j) {
                boolean expected = j != 3;
                if (m.find() != expected) {
                    return failed("expected " + j + "-th find() in " + i
                            + "-th cycle to return " + expected);
                }
            }
            m.reset();
            if (!m.find()) {
                return failed("expected find() to return true");
            }

            if (m.start() != firstComma || m.end() != firstComma + 2) {
                return failed("iteration " + i + " broke the contract");
            }
        }

        //test find(int)
        int commaPos = -1;
        for (int i = 0; i < baseString.length(); ++i) {
            commaPos = baseString.indexOf(',', i);
            boolean expected = commaPos != -1;
            
            if (m.find(i) != expected) {
                return failed("expected find(" + i + ") to return " + expected);
            }

            if(expected) {
                if (m.start() != commaPos || m.end() != commaPos + 2) {
                    return failed("find(" + i + ") broke the contract : " + m.start());
                }
            } else {
                try {
                    m.start();
                    return failed("expected start() to throw IllegalStateException if there's no match");
                } catch(IllegalStateException e) {
                }
                try {
                    m.end();
                    return failed("expected end() to throw IllegalStateException if there's no match");
                } catch(IllegalStateException e) {
                }
            }
        }

        return passed();
    }

    public Result testReplaceAll() {
        Matcher m = Pattern.compile("a+b|ba+").matcher("baaab");
        if (!m.replaceAll("o").equals("ob")) {
            return failed("case 1 failed");
        }

        try {
            m.group();
            return failed("no group() allowed after replaceAll()");
        } catch (IllegalStateException e) {
        }

        m = Pattern.compile("ba+?|a+b").matcher("baaab");
        if (!m.replaceAll("o").equals("oo")) {
            return failed("case 2 failed");
        }

        m = Pattern.compile("a").matcher("b");
        if (!m.replaceAll("o").equals("b")) {
            return failed("case 3 failed");
        }

        m = Pattern.compile("(.)").matcher("abcd");
        if (!m.replaceAll("$1$1").equals("aabbccdd")) {
            return failed("case 4 failed");
        }

        m = Pattern.compile("(.?)(.)").matcher("abcdx");
        if (!m.replaceAll("$2$2$2$1$1").equals("bbbaadddccxxx")) {
            return failed("case 5 failed");
        }

        m = Pattern.compile("(.)(.)").matcher("abcdx");
        if (!m.replaceAll("$2$2$2$1$1").equals("bbbaadddccx")) {
            return failed("case 6 failed");
        }

        m = Pattern.compile("(.)b\\1").matcher("ababb");
        try {
            m.replaceAll("$2$2$2$1$1");
            return failed("case 7 failed");
        } catch (IndexOutOfBoundsException e) {
        }
        m = Pattern.compile("(.)(b\\1)").matcher("ababb");
        if (!m.replaceAll("$2$2$2$1$1").equals("bababaaabb")) {
            return failed("case 8 failed");
        }

        return passed();
    }

    public Result testPattern() {
        String[] patterns = PatternTest.getCorrectPatterns();
        for (int i = 0; i < patterns.length; ++i) {
            Pattern p = Pattern.compile(patterns[i]);
            Matcher m = p.matcher(baseString);
            if (m.pattern() != p) {
                return failed("assertion failed for pattern " + p.pattern());
            }
        }
        return passed();
    }

    public Result testMatches() {
        if (Pattern.compile("(..).*\\1").matcher(baseString).matches()) {
            return failed("case 1 failed");
        }
        if (!Pattern.compile(".*(..).*\\1.*").matcher(baseString).matches()) {
            return failed("case 2 failed");
        }
        if (!Pattern
                .compile(".*(..).*\\1.*\\1.*\\1.*\\1.*\\1.*\\1.*\\1.*\\1.*")
                .matcher(baseString).matches()) {
            return failed("case 3 failed");
        }
        if (Pattern.compile(
                ".*(..).*\\1.*\\1.*\\1.*\\1.*\\1.*\\1.*\\1.*\\1.*\\1.*")
                .matcher(baseString).matches()) {
            return failed("case 4 failed");
        }
        if (!Pattern.compile(".*(...).*\\1.*\\1.*\\1.*\\1.*").matcher(
                baseString).matches()) {
            return failed("case 5 failed");
        }
        if (Pattern.compile(".*(...).*\\1.*\\1.*\\1.*\\1.*\\1.*").matcher(
                baseString).matches()) {
            return failed("case 6 failed");
        }
        if (!Pattern.compile(".*(....).*\\1.*\\1.*\\1.*").matcher(baseString)
                .matches()) {
            return failed("case 7 failed");
        }
        if (Pattern.compile(".*(....).*\\1.*\\1.*\\1.*\\1.*").matcher(
                baseString).matches()) {
            return failed("case 8 failed");
        }
        return passed();
    }

    public Result testAppendReplacement() {
        Pattern p = Pattern.compile("implementation");
        Matcher m = p.matcher(baseString);

        StringBuffer sb = new StringBuffer();
        String[] names = new String[] { "Functional", "collection" };
        int currentName = 0;
        while (m.find()) {
            m.appendReplacement(sb, names[currentName++]);
        }

        if (!sb
                .toString()
                .equals(
                        "The Functional test suite is a "
						                + "collection of "
						                + "micro scenarios for "
						                + "testing various functional parts of "
						                + "an implementation.")) {
            return failed("expected another value after two replacements");
        }

        try {
            m.appendReplacement(sb, "");
            return failed("expected IllegalStateException on third appendReplacement()");
        } catch (IllegalStateException e) {
        }

        m.appendTail(sb);

        if (!sb
                .toString()
                .equals(
                        "The Functional test suite is a "
						                + "collection of "
						                + "micro scenarios for "
						                + "testing various functional parts of "
						                + "an implementation.")) {
            return failed("expected another value after two replacements and appendTail");
        }

        sb.setLength(0);
        p = Pattern.compile("(?:b)(o)o(k)");
        m = p.matcher(baseString);

        while (m.find()) {
            m.appendReplacement(sb, "d$1c");
            try {
                m.appendReplacement(sb, "x$3");
                return failed("expected IndexOutOfBoundsException in second replace cycle");
            } catch (IndexOutOfBoundsException e) {
            }
        }
        m.appendTail(sb);

        if (!sb
                .toString()
                .equals(
                        "The Functional test suite is a "
						                + "collection of "
						                + "micro scenarios for "
						                + "testing various functional parts of "
						                + "an implementation.")) {
            return failed("expected another value after group replacements and appendTail, got: "
                    + sb.toString());
        }

        return passed();
    }

    public Result testGroup() {
        Pattern p = Pattern.compile("b(..)k|(.)(.)r");
        Matcher m = p.matcher(baseString);

        if (m.groupCount() != 3) {
            return failed("groups: " + m.groupCount());
        }

        String[][] expected = new String[][] { { "ver", null, "v", "e" },
                { "tir", null, "t", "i" }, { "her", null, "h", "e" },
                { "ter", null, "t", "e" }, { "bank", "an", null, null },
                { " or", null, " ", "o" }, { "book", "oo", null, null },
                { "her", null, "h", "e" }, { "ter", null, "t", "e" },
                { "s r", null, "s", " " }, { "tur", null, "t", "u" },
                { " or", null, " ", "o" }, { "ver", null, "v", "e" },
                { "book", "oo", null, null }, { "tur", null, "t", "u" },
                { " or", null, " ", "o" }, { "ver", null, "v", "e" }, };

        String[] match = new String[4];
        int i = 0;

        while (m.find()) {
            if (m.group() == m.group(0)) {
                return failed("expected group() and group(0) to be not identical");
            }
            if (!m.group().equals(m.group(0))) {
                return failed("expected group() and group(0) to be equal");
            }
            match[0] = m.group(0);
            match[1] = m.group(1);
            match[2] = m.group(2);
            match[3] = m.group(3);
            if (!Arrays.equals(expected[i++], match)) {
                return failed("wrong value returned at step " + i);
            }
        }

        if (expected.length != i) {
            return failed("less matches than expected");
        }

        p = Pattern.compile("((.))");
        m = p.matcher(baseString);
        if (m.groupCount() != 2) {
            return failed("groups: " + m.groupCount());
        }
        m.find();
        if (m.group(0) == m.group(1)) {
            return failed("expected group(0) and group(1) to be not identical");
        }

        return passed();
    }

}