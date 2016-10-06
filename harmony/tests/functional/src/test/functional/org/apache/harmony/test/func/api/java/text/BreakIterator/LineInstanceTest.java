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

package org.apache.harmony.test.func.api.java.text.BreakIterator;

import java.text.BreakIterator;
import java.util.Locale;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class LineInstanceTest extends MultiCase {

    private final boolean developMode = false;

    private String getEngText() {
        return "The Functional test suite is a "
                + "collection of "
                + "micro scenarios for "
                + "testing various functional parts of "
                + "an implementation.";
    }

    private String getJPText() {
        return "The Functional test suite is a "
                + "collection of "
                + "micro scenarios for "
                + "testing various functional parts of "
                + "an implementation.";
    }

    private void printBreaksArray(BreakIterator it) {
        if (developMode) {
            it.first();
            System.out.println("{");
            do {
                System.out.print(it.current() + ", ");
            } while (it.next() != BreakIterator.DONE);

            System.out.println("};");
        }
    }

    public Result failed(String msg) {
        log.add("FAIL: " + msg);
        return super.failed(msg);
    }

    private Result verifyIterator(BreakIterator it, int[] breaks) {
        printBreaksArray(it);

        Result result = passed();
        // reset
        it.first();

        int bi = 0;
        // check simple forward
        while (true) {
            int current = it.current();
            if (current == breaks[bi]) {
                bi++;
                int next = it.next();
                if (next == BreakIterator.DONE) {
                    if (breaks.length != bi) {
                        result = failed("BI stops before <end> reached");
                    }
                    break;
                } else if (bi == breaks.length) {
                    result = failed("<end> reached but BI did not stop");
                    break;
                } else if (next != breaks[bi]) {
                    result = failed("BI.next() returned " + next
                            + ", expected " + breaks[bi]);
                    break;
                }
            } else {
                result = failed("Missed break, try to sync...");
                int i;
                for (i = 0; i < breaks.length; i++) {
                    if (breaks[i] == current) {
                        break;
                    }
                }
                if (i == breaks.length) {
                    result = failed("Sync failed");
                    break;
                } else {
                    log.add("Sync: " + bi + " -> " + i);
                    bi = i;
                }
            }
        }

        log.add("Walk backwards");
        bi = breaks.length - 1;
        // now let's walk backwards
        while (true) {
            int current = it.current();
            if (current == breaks[bi]) {
                bi--;
                int prev = it.previous();
                if (prev == BreakIterator.DONE) {
                    if (bi > -1) {
                        result = failed("BI stops before <end> reached");
                    }
                    break;
                } else if (bi <= -1) {
                    result = failed("<end> reached but BI did not stop");
                    break;
                } else if (prev != breaks[bi]) {
                    result = failed("BI.next() returned " + prev
                            + ", expected " + breaks[bi]);
                    break;
                }
            } else {
                result = failed("Missed break, try to sync...");
                int i;
                for (i = 0; i < breaks.length; i++) {
                    if (breaks[i] == current) {
                        break;
                    }
                }
                if (i == breaks.length) {
                    result = failed("Sync failed");
                    break;
                } else {
                    log.add("Sync: " + bi + " -> " + i);
                    bi = i;
                }
            }
        }

        return result;
    }

    public Result testEngDef() {
        BreakIterator it = BreakIterator.getLineInstance();
        String t = getEngText();
        it.setText(t);

 	 int[] breaks = { 0, 4, 15, 20, 26, 29, 31, 42, 45, 51, 61, 65, 73, 81, 92, 98, 101, 104, 119, };

        return verifyIterator(it, breaks);
    }

    public Result testJPDef() {
        BreakIterator it = BreakIterator.getLineInstance();
        String t = getJPText();
        it.setText(t);

	 int[] breaks = { 0, 4, 15, 20, 26, 29, 31, 42, 45, 51, 61, 65, 73, 81, 92, 98, 101, 104, 119, };
        return verifyIterator(it, breaks);
    }

    public Result testJPLocale() {
        BreakIterator it = BreakIterator.getLineInstance(Locale.JAPANESE);
        String t = getJPText();
        it.setText(t);

	 int[] breaks = { 0, 4, 15, 20, 26, 29, 31, 42, 45, 51, 61, 65, 73, 81, 92, 98, 101, 104, 119, };
        return verifyIterator(it, breaks);
    }

    public Result testEngLocale() {
        BreakIterator it = BreakIterator.getLineInstance(Locale.ENGLISH);
        String t = getEngText();
        it.setText(t);

	 int[] breaks = { 0, 4, 15, 20, 26, 29, 31, 42, 45, 51, 61, 65, 73, 81, 92, 98, 101, 104, 119, };
        return verifyIterator(it, breaks);
    }

    public static void main(String[] args) {
        System.exit(new LineInstanceTest().test(args));
    }

}
