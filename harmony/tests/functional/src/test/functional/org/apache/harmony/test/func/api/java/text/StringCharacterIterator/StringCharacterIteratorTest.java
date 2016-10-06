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

package org.apache.harmony.test.func.api.java.text.StringCharacterIterator;

import java.text.StringCharacterIterator;

import org.apache.harmony.test.func.api.java.text.share.framework.TextTestData;
import org.apache.harmony.share.Test;

/**
 */
public class StringCharacterIteratorTest extends Test {

    public static void main(String[] args) {
        System.exit(new StringCharacterIteratorTest().test(args));
    }

    public boolean testIterator(StringCharacterIterator sci, String s,
            int beginIndex, int endIndex) {

        try {

            if (sci.first() != s.charAt(beginIndex)) {
                fail("StringCharacterIterator.first() return incorrect value.");
                return false;
            } else if (sci.getIndex() != sci.getBeginIndex()
                    || sci.getIndex() != beginIndex) {
                fail("StringCharacterIterator.getIndex() after "
                        + "StringCharacterIterator.first()\nreturn incorrect value.");
                return false;
            }

            if (sci.last() != s.charAt(endIndex - 1)) {
                fail("StringCharacterIterator.last() return incorrect value.");
                return false;
            } else if (sci.getIndex() != sci.getEndIndex() - 1
                    || sci.getIndex() != endIndex - 1) {
                fail("StringCharacterIterator.getIndex() after "
                        + "StringCharacterIterator.last()\nreturn incorrect value.");
                return false;
            }

            for (int delta = 1; delta < s.length() / 2; delta++) {

                int i = beginIndex;
                sci.setIndex(i);

                while (sci.current() != StringCharacterIterator.DONE) {
                    if (!testIteratorValidity(s, sci, i, beginIndex, endIndex)) {
                        fail("Invalid iterator state");
                        return false;
                    }

                    char next = sci.next();
                    for (int j = 0; j < delta - 1; j++)
                        next = sci.next();

                    i += delta;
                    if (endIndex > i) {
                        if (s.charAt(i) != next
                                || !testIteratorValidity(s, sci, i, beginIndex,
                                        endIndex)) {
                            fail("StringCharacterIterator.next() "
                                    + "return incorrect value.");
                            return false;
                        }
                    } else if (next != StringCharacterIterator.DONE) {
                        fail("Iterator do not recognize EOS.");
                        return false;
                    }
                }

                if (sci.previous() != s.charAt(endIndex - 1)) {
                    fail("StringCharacterIterator.previous() "
                            + "return incorrect value after passing EOS.");
                    return false;
                }

                i = endIndex - 1;
                sci.setIndex(i);
                if (!testIteratorValidity(s, sci, i, beginIndex, endIndex)) {
                    fail("StringCharacterIterator.setIndex() "
                            + "leads to invalid object state");
                    return false;
                }

                while (sci.current() != StringCharacterIterator.DONE && i >= beginIndex) {
                    if (!testIteratorValidity(s, sci, i, beginIndex, endIndex)) {
                        fail("Invalid iterator state");
                        return false;
                    }

                    char prev = sci.previous();
                    for (int j = 0; j < delta - 1; j++)
                        prev = sci.previous();

                    i -= delta;
                    if (i >= beginIndex) {
                        if (s.charAt(i) != prev
                                || !testIteratorValidity(s, sci, i, beginIndex,
                                        endIndex)) {
                            fail("StringCharacterIterator.next() "
                                    + "return incorrect value.");
                            return false;
                        }
                    } else if (prev != StringCharacterIterator.DONE) {
                        fail("Iterator do not recognize EOS.");
                        return false;
                    }
                }
            }
        } catch (Throwable e) {
            fail("Test internal error.");
            fail(e.toString());
            e.printStackTrace();
            return false;
        }

        return true;

    }

    private String getFailMsg(int adv, int pos, String s) {
        return "Invalid iterator status on simple "
                + (adv == 1 ? "forward" : "backward") + " iteration.\n"
                + "At: " + pos + "";
    }

    private boolean testIteratorValidity(String s, StringCharacterIterator i,
            int pos, int beginIndex, int endIndex) {
        if (i.current() != s.charAt(pos)) {
            Test.log.add("i.current() != s.charAt(pos)");
            return false;
        }
        if (i.getIndex() != pos) {
            Test.log.add("i.getIndex() != pos");
            return false;
        }

        if (i.getBeginIndex() != beginIndex) {
            Test.log.add("i.getBeginIndex() != beginIndex");
            return false;
        }
        if ((i.getEndIndex() != endIndex)) {
            Test.log.add("(i.getEndIndex() != endIndex)");
            return false;
        }
        return true;
    }

    /*
       * (non-Javadoc)
       * 
       * @see org.apache.harmony.share.Test#test()
       */

    public int test() {
        String s = TextTestData.getTestString();
        if (!testIterator(new StringCharacterIterator(s), s, 0, s.length()))
            return fail("StringCharacterIterator(String)");
        if (!testIterator(new StringCharacterIterator(s, s.length() / 2), s, 0,
                s.length()))
            return fail("StringCharacterIterator(String, int)");
        if (!testIterator(new StringCharacterIterator(s, s.length() / 4, s
                .length() * 3 / 4, s.length() / 2), s, s.length() / 4, s
                .length() * 3 / 4))
            return fail("StringCharacterIterator(String, int, int, int)");
        return pass();
    }
}