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

package org.apache.harmony.test.func.api.javax.swing.text.GapContent;

import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.undo.UndoableEdit;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedGapContent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedSegment;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class GapContentTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new GapContentTest().test(args));
    }

    public Result testConstructor() {
        InstrumentedUILog.clear();
        new InstrumentedGapContent();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "GapContent.allocateArray", "10" },
                /* 2 */{ "GapContent.replace", "0", "0",
                        InstrumentedUILog.ANY_NON_NULL_VALUE, "1" },
                /* 3 */{ "GapContent.shiftGap", "0" },
                /* 4 */{ "GapContent.resetMarksAtZero" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected new GapContent() to call another sequence of events");
        }

        InstrumentedUILog.clear();
        new InstrumentedGapContent(123);
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "GapContent.allocateArray", "123" },
                /* 2 */{ "GapContent.replace", "0", "0",
                        InstrumentedUILog.ANY_NON_NULL_VALUE, "1" },
                /* 3 */{ "GapContent.shiftGap", "0" },
                /* 4 */{ "GapContent.resetMarksAtZero" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected new GapContent(123) to call another sequence of events");
        }

        return passed();
    }

    public Result testAllocateArray() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);

        InstrumentedUILog.clear();
        igc.allocateArray(123);
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "GapContent.allocateArray", "123" }, })) {
            return failed("expected allocateArray() not to call anything");
        }

        if (igc.getArrayLength() != 10) {
            return failed("expected getArrayLength() to return 10 after allocateArray(123), got "
                    + igc.getArrayLength());
        }

        if (igc.length() != 1) {
            return failed("expected length() to return 1 after allocateArray(123), got "
                    + igc.length());
        }

        InstrumentedUILog.clear();
        igc.allocateArray(7);
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "GapContent.allocateArray", "7" }, })) {
            return failed("expected allocateArray() not to call anything");
        }

        if (igc.getArrayLength() != 10) {
            return failed("expected getArrayLength() to return 10 after allocateArray(7), got "
                    + igc.getArrayLength());
        }

        if (igc.length() != 1) {
            return failed("expected length() to return 1 after allocateArray(7), got "
                    + igc.length());
        }

        return passed();
    }

    public Result testCreatePosition() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);

        InstrumentedUILog.clear();

        try {
            Position pos = igc.createPosition(7);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "GapContent.createPosition", "7" }, })) {
                return failed("expected createPosition(int) not to call anything");
            }
            if (pos == null) {
                return failed("expected createPosition() to return not null by default");
            }
            if (pos.getOffset() != 7) {
                return failed("expected createPosition(7).getOffset() to return 7, got "
                        + pos.getOffset());
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testGetChars() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);
        InstrumentedSegment is = new InstrumentedSegment();

        try {
            igc.getChars(2, 5, is);
            return failed("expected getChars() to throw BadLocationException if gap is not filled");
        } catch (BadLocationException e) {
        }

        try {

            InstrumentedUILog.clear();
            igc.insertString(0, "0123456789");
            InstrumentedUILog.clear();
            igc.getChars(2, 5, is);

            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "GapContent.getChars", "2", "5", is },
            /* 2 */{ "GapContent.length" },
            /* 3 */{ "GapContent.getArrayLength" },
            /* 4 */{ "GapContent.length" },
            /* 5 */{ "GapContent.getArrayLength" }, })) {
                return failed("expected getChars(...) to call another sequence of events");
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testGetPositionsInRange() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);
        Vector v = new Vector();

        InstrumentedUILog.clear();

        igc.getPositionsInRange(v, 0, 5);
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "GapContent.getPositionsInRange", v, "0", "5" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected getPositionsInRange() not to call anything");
        }

        if (v.size() != 0) {
            return failed("expected no elements in vector");
        }

        try {
            igc.insertString(0, "0123456789");
        } catch (BadLocationException e) {
        }

        InstrumentedUILog.clear();
        igc.getPositionsInRange(v, 0, 5);
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "GapContent.getPositionsInRange", v, "0", "5" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected second getPositionsInRange() not to call anything");
        }

        if (v.size() != 0) {
            return failed("expected no elements in vector");
        }

        return passed();
    }

    public Result testString() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);
        try {
            igc.getString(0, 5);
            return failed("expected BadLocationException on empty gap");
        } catch (BadLocationException e) {
        }

        InstrumentedUILog.clear();
        try {
            igc.insertString(0, "0123456789");

            if (!InstrumentedUILog.equals(new Object[][] {
                    /* 1 */{ "GapContent.insertString", "0", "0123456789" },
                    /* 2 */{ "GapContent.length" },
                    /* 3 */{ "GapContent.getArrayLength" },
                    /* 4 */{ "GapContent.replace", "0", "0",
                            InstrumentedUILog.ANY_NON_NULL_VALUE, "10" },
                    /* 5 */{ "GapContent.shiftGap", "0" },
                    /* 6 */{ "GapContent.resetMarksAtZero" },
                    /* 7 */{ "GapContent.getArrayLength" },
                    /* 8 */{ "GapContent.shiftEnd", "11" },
                    /* 9 */{ "GapContent.getArrayLength" },
                    /* 10 */{ "GapContent.allocateArray", "24" },
                    /* 11 */{ "GapContent.getArrayLength" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected insertString(0, 0123456789) to call another sequence of events");
            }
            InstrumentedUILog.clear();

            if (!"23456".equals(igc.getString(2, 5))) {
                return failed("expected getString(2, 5) to return '23456', got "
                        + igc.getString(2, 5));
            }
            if (!InstrumentedUILog.equals(new Object[][] {
                    /* 1 */{ "GapContent.getString", "2", "5" },
                    /* 2 */{ "GapContent.getChars", "2", "5",
                            InstrumentedUILog.ANY_NON_NULL_VALUE },
                    /* 3 */{ "GapContent.length" },
                    /* 4 */{ "GapContent.getArrayLength" },
                    /* 5 */{ "GapContent.length" },
                    /* 6 */{ "GapContent.getArrayLength" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected getString(2, 5) to call another sequence of events");
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("unexpected exception");
        }

        return passed();
    }

    public Result testLength() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);
        try {
            igc.insertString(0, "0123456789");
        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("unexpected exception");
        }

        InstrumentedUILog.clear();

        if (igc.length() != 11) {
            return failed("expected gap length to be 11, got " + igc.length());
        }

        return passed();
    }

    public Result testRemove() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);
        try {
            igc.insertString(0, "0123456789");

            InstrumentedUILog.clear();

            UndoableEdit ue = igc.remove(2, 5);
            if (!InstrumentedUILog
                    .equals(new Object[][] {
                            /* 1 */{ "GapContent.remove", "2", "5" },
                            /* 2 */{ "GapContent.length" },
                            /* 3 */{ "GapContent.getArrayLength" },
                            /* 4 */{ "GapContent.getString", "2", "5" },
                            /* 5 */{ "GapContent.getChars", "2", "5",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE },
                            /* 6 */{ "GapContent.length" },
                            /* 7 */{ "GapContent.getArrayLength" },
                            /* 8 */{ "GapContent.length" },
                            /* 9 */{ "GapContent.getArrayLength" },
                            /* 10 */{ "GapContent.getPositionsInRange", null,
                                    "2", "5" },
                            /* 11 */{ "GapContent.replace", "2", "5",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, "0" },
                            /* 12 */{ "GapContent.shiftGap", "7" },
                            /* 13 */{ "GapContent.resetMarksAtZero" },
                            /* 14 */{ "GapContent.shiftGapStartDown", "2" },
                            /* 15 */{ "GapContent.resetMarksAtZero" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected remove(2,5) to call another sequence of events");
            }

            if (!ue.canUndo() || ue.canRedo()) {
                return failed("wrong UndoableEdit properties");
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("unexpected exception");
        }

        return passed();
    }

    public Result testResetmarksAtZero() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);
        try {
            igc.insertString(0, "0123456789");

            InstrumentedUILog.clear();

            igc.resetMarksAtZero();
            if (!InstrumentedUILog
                    .equals(new Object[][] { { "GapContent.resetMarksAtZero" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected resetMarksAtZero not to call anything");
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("unexpected exception");
        }

        return passed();
    }

    public Result testShifts() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);
        try {
            igc.insertString(0, "0123456789");

            InstrumentedUILog.clear();
            igc.shiftEnd(33);

            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "GapContent.shiftEnd", "33" },
            /* 2 */{ "GapContent.getArrayLength" },
            /* 3 */{ "GapContent.allocateArray", "68" },
            /* 4 */{ "GapContent.getArrayLength" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected shiftEnd(33) to call another sequence of events");
            }

            if (!"11 68 10 67".equals("" + igc.length() + " "
                    + igc.getArrayLength() + " " + igc.getGapStartExposed()
                    + " " + igc.getGapEndExposed())) {
                return failed("after shiftEnd(33) expected length, arraylength, gapStart, gapEnd to be : 11 68 10 67 , got "
                        + +igc.length()
                        + " "
                        + igc.getArrayLength()
                        + " "
                        + igc.getGapStartExposed()
                        + " "
                        + igc.getGapEndExposed());
            }

            InstrumentedUILog.clear();
            igc.shiftGap(11);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "GapContent.shiftGap", "11" },
            /* 2 */{ "GapContent.resetMarksAtZero" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected shiftGap(11) to call another sequence of events");
            }

            if (!"11 68 11 68".equals("" + igc.length() + " "
                    + igc.getArrayLength() + " " + igc.getGapStartExposed()
                    + " " + igc.getGapEndExposed())) {
                return failed("after shiftGap(11) expected length, arraylength, gapStart, gapEnd to be : 11 68 11 68 , got "
                        + +igc.length()
                        + " "
                        + igc.getArrayLength()
                        + " "
                        + igc.getGapStartExposed()
                        + " "
                        + igc.getGapEndExposed());
            }

            InstrumentedUILog.clear();
            igc.shiftGapEndUp(44);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "GapContent.shiftGapEndUp", "44" },
            /* 2 */{ "GapContent.resetMarksAtZero" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected shiftGapEndUp(44) to call another sequence of events");
            }

            if (!"35 68 11 44".equals("" + igc.length() + " "
                    + igc.getArrayLength() + " " + igc.getGapStartExposed()
                    + " " + igc.getGapEndExposed())) {
                return failed("after shiftGapEndUp(44) expected length, arraylength, gapStart, gapEnd to be : 35 68 11 44 , got "
                        + +igc.length()
                        + " "
                        + igc.getArrayLength()
                        + " "
                        + igc.getGapStartExposed()
                        + " "
                        + igc.getGapEndExposed());
            }

            InstrumentedUILog.clear();
            igc.shiftGapStartDown(7);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "GapContent.shiftGapStartDown", "7" },
            /* 2 */{ "GapContent.resetMarksAtZero" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected shiftGapStartDown(7) to call another sequence of events");
            }
            if (!"31 68 7 44".equals("" + igc.length() + " "
                    + igc.getArrayLength() + " " + igc.getGapStartExposed()
                    + " " + igc.getGapEndExposed())) {
                return failed("after shiftGapStartDown(44) expected length, arraylength, gapStart, gapEnd to be : 31 68 7 44 , got "
                        + +igc.length()
                        + " "
                        + igc.getArrayLength()
                        + " "
                        + igc.getGapStartExposed()
                        + " "
                        + igc.getGapEndExposed());
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("unexpected exception");
        }

        return passed();
    }

    public Result testUpdateUndoPositions() {
        InstrumentedGapContent igc = new InstrumentedGapContent(10);
        Vector v = new Vector();

        InstrumentedUILog.clear();

        igc.updateUndoPositions(v, 0, 5);

        if (!InstrumentedUILog.equals(new Object[][] { {
                "GapContent.updateUndoPositions", v, "0", "5" } })) {
            InstrumentedUILog.printLog();
            return failed("expected updateUndoPositions not to call any other methods");
        }

        return passed();
    }

}