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

package org.apache.harmony.test.func.api.javax.swing.text.DefaultHighlighter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDefaultHighlighter;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedGraphics;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedHighlightPainter;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedJTextComponent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedShape;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedView;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class DefaultHighlighterTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new DefaultHighlighterTest().test(args));
    }

    public Result testConstructor() {
        InstrumentedUILog.clear();

        new InstrumentedDefaultHighlighter();

        if (!InstrumentedUILog.equals(new Object[][] {})) {
            return failed("expected DefaultHighlighter() not to call any methods");
        }

        InstrumentedUILog.printLogAsArray();
        return passed();
    }

    public Result testHighlight() {
        InstrumentedDefaultHighlighter idh = new InstrumentedDefaultHighlighter();
        InstrumentedHighlightPainter ihp = new InstrumentedHighlightPainter();
        InstrumentedJTextComponent ijtc = new InstrumentedJTextComponent() {
            public Document getDocument() {
                super.getDocument();
                return new InstrumentedDocument();
            }
        };
        ijtc.setHighlighter(idh);
        idh.install(ijtc);

        if (idh.getHighlights() != null && idh.getHighlights().length != 0) {
            return failed("expected getHighlights to be empty by default");
        }

        InstrumentedUILog.clear();

        Object handle = null;
        try {
            handle = idh.addHighlight(0, 1, ihp);

            if (!InstrumentedUILog
                    .equals(new Object[][] {
                            /* 1 */{ "DefaultHighlighter.addHighlight", "0",
                                    "1", ihp },
                            /* 2 */{ "JTextComponent.getDocument" },
                            /* 3 */{ "Document.getDocumentProperties" },
                            /* 4 */{ "Document.createPosition", "0" },
                            /* 5 */{ "Document.createPosition", "1" },
                            /* 6 */{ "Document.getDocumentProperties" },
                            /* 7 */{ "Document.createDefaultRoot" },
                            /* 8 */{ "Document.createBranchElement", null,
                                    null },
                            /* 9 */{ "Document.createLeafElement",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, null,
                                    "0", "1" },
                            /* 10 */{ "Document.createPosition", "0" },
                            /* 11 */{ "Document.createPosition", "1" },
                            /* 12 */{ "JTextComponent.getUI" },
                            /* 13 */{ "DefaultHighlighter.getDrawsLayeredHighlights" },
                            /* 14 */{ "Document.createPosition", "0" },
                            /* 15 */{ "Document.createPosition", "1" },
                            /* 16 */{ "TextUI.damageRange", ijtc, "0", "1" }, })
                    && !InstrumentedUILog
                            .equals(new Object[][] {
                                    /* 1 */{
                                            "DefaultHighlighter.addHighlight",
                                            "0", "1", ihp },
                                    /* 2 */{ "JTextComponent.getDocument" },
                                    /* 3 */{ "Document.getDocumentProperties" },
                                    /* 4 */{ "Document.createPosition", "0" },
                                    /* 5 */{ "Document.createPosition", "1" },
                                    /* 6 */{ "Document.getDocumentProperties" },
                                    /* 7 */{ "Document.createDefaultRoot" },
                                    /* 8 */{ "Document.createBranchElement",
                                            null, null },
                                    /* 9 */{
                                            "Document.createLeafElement",
                                            InstrumentedUILog.ANY_NON_NULL_VALUE,
                                            null, "0", "1" },
                                    /* 10 */{ "Document.createPosition", "0" },
                                    /* 11 */{ "Document.createPosition", "1" },
                                    /* 12 */{ "DefaultHighlighter.getDrawsLayeredHighlights" },
                                    /* 13 */{ "Document.createPosition", "0" },
                                    /* 14 */{ "Document.createPosition", "1" },
                                    /* 15 */{ "JTextComponent.getDocument" },
                                    /* 16 */{ "Document.getDocumentProperties" },
                                    /* 17 */{ "Document.createPosition", "0" },
                                    /* 18 */{ "Document.createPosition", "1" },
                                    /* 19 */{ "Document.getDocumentProperties" },
                                    /* 20 */{ "Document.createDefaultRoot" },
                                    /* 21 */{ "Document.createBranchElement",
                                            null, null },
                                    /* 22 */{
                                            "Document.createLeafElement",
                                            InstrumentedUILog.ANY_NON_NULL_VALUE,
                                            null, "0", "1" },
                                    /* 23 */{ "Document.createPosition", "0" },
                                    /* 24 */{ "Document.createPosition", "1" },
                                    /* 25 */{ "Document.createPosition", "0" },
                                    /* 26 */{ "Document.createPosition", "1" },
                                    /* 27 */{ "JTextComponent.getDocument" },
                                    /* 28 */{ "Document.getDocumentProperties" },
                                    /* 29 */{ "Document.createPosition", "0" },
                                    /* 30 */{ "Document.createPosition", "1" },
                                    /* 31 */{ "Document.getDocumentProperties" },
                                    /* 32 */{ "Document.createDefaultRoot" },
                                    /* 33 */{ "Document.createBranchElement",
                                            null, null },
                                    /* 34 */{
                                            "Document.createLeafElement",
                                            InstrumentedUILog.ANY_NON_NULL_VALUE,
                                            null, "0", "1" },
                                    /* 35 */{ "Document.createPosition", "0" },
                                    /* 36 */{ "Document.createPosition", "1" },
                                    /* 37 */{ "JTextComponent.getUI" },
                                    /* 38 */{ "JTextComponent.getDocument" },
                                    /* 39 */{ "Document.getDocumentProperties" },
                                    /* 40 */{ "Document.createPosition", "0" },
                                    /* 41 */{ "Document.createPosition", "1" },
                                    /* 42 */{ "Document.getDocumentProperties" },
                                    /* 43 */{ "Document.createDefaultRoot" },
                                    /* 44 */{ "Document.createBranchElement",
                                            null, null },
                                    /* 45 */{
                                            "Document.createLeafElement",
                                            InstrumentedUILog.ANY_NON_NULL_VALUE,
                                            null, "0", "1" },
                                    /* 46 */{ "Document.createPosition", "0" },
                                    /* 47 */{ "Document.createPosition", "1" }, })) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected addHighlight() to call another sequence of events");
            }

            if (handle == null) {
                return failed("expected addHighlight not to return null");
            }

            //ok now, check that there's 1 highlight
            if (idh.getHighlights().length != 1) {
                return failed("expected 1 highlight here");
            }

            InstrumentedUILog.clear();
            idh.changeHighlight(handle, 0, 2);

            if (!InstrumentedUILog.equals(new Object[][] {
                    /* 1 */{ "DefaultHighlighter.changeHighlight", handle,
                            "0", "2" },
                    /* 2 */{ "JTextComponent.getDocument" },
                    /* 3 */{ "Document.getDocumentProperties" },
                    /* 4 */{ "Document.createPosition", "0" },
                    /* 5 */{ "Document.createPosition", "1" },
                    /* 6 */{ "Document.getDocumentProperties" },
                    /* 7 */{ "Document.createDefaultRoot" },
                    /* 8 */{ "Document.createBranchElement", null, null },
                    /* 9 */{ "Document.createLeafElement",
                            InstrumentedUILog.ANY_NON_NULL_VALUE, null, "0",
                            "1" },
                    /* 10 */{ "Document.createPosition", "0" },
                    /* 11 */{ "Document.createPosition", "1" },
                    /* 12 */{ "JTextComponent.getUI" },
                    /* 13 */{ "TextUI.damageRange", ijtc, "1", "2" },
                    /* 14 */{ "Document.createPosition", "0" },
                    /* 15 */{ "Document.createPosition", "2" }, })
                    && !InstrumentedUILog.equals(new Object[][] {
                            /* 1 */{ "DefaultHighlighter.changeHighlight",
                                    handle, "0", "2" },
                            /* 2 */{ "JTextComponent.getDocument" },
                            /* 3 */{ "Document.getDocumentProperties" },
                            /* 4 */{ "Document.createPosition", "0" },
                            /* 5 */{ "Document.createPosition", "1" },
                            /* 6 */{ "Document.getDocumentProperties" },
                            /* 7 */{ "Document.createDefaultRoot" },
                            /* 8 */{ "Document.createBranchElement", null,
                                    null },
                            /* 9 */{ "Document.createLeafElement",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, null,
                                    "0", "1" },
                            /* 10 */{ "Document.createPosition", "0" },
                            /* 11 */{ "Document.createPosition", "1" },
                            /* 12 */{ "JTextComponent.getDocument" },
                            /* 13 */{ "Document.getDocumentProperties" },
                            /* 14 */{ "Document.createPosition", "0" },
                            /* 15 */{ "Document.createPosition", "1" },
                            /* 16 */{ "Document.getDocumentProperties" },
                            /* 17 */{ "Document.createDefaultRoot" },
                            /* 18 */{ "Document.createBranchElement", null,
                                    null },
                            /* 19 */{ "Document.createLeafElement",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, null,
                                    "0", "1" },
                            /* 20 */{ "Document.createPosition", "0" },
                            /* 21 */{ "Document.createPosition", "1" },
                            /* 22 */{ "Document.createPosition", "1" },
                            /* 23 */{ "Document.createPosition", "2" },
                            /* 24 */{ "JTextComponent.getDocument" },
                            /* 25 */{ "Document.getDocumentProperties" },
                            /* 26 */{ "Document.createPosition", "0" },
                            /* 27 */{ "Document.createPosition", "1" },
                            /* 28 */{ "Document.getDocumentProperties" },
                            /* 29 */{ "Document.createDefaultRoot" },
                            /* 30 */{ "Document.createBranchElement", null,
                                    null },
                            /* 31 */{ "Document.createLeafElement",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, null,
                                    "0", "1" },
                            /* 32 */{ "Document.createPosition", "0" },
                            /* 33 */{ "Document.createPosition", "1" },
                            /* 34 */{ "Document.createPosition", "0" },
                            /* 35 */{ "Document.createPosition", "2" },
                            /* 36 */{ "JTextComponent.getUI" },
                            /* 37 */{ "JTextComponent.getDocument" },
                            /* 38 */{ "Document.getDocumentProperties" },
                            /* 39 */{ "Document.createPosition", "0" },
                            /* 40 */{ "Document.createPosition", "1" },
                            /* 41 */{ "Document.getDocumentProperties" },
                            /* 42 */{ "Document.createDefaultRoot" },
                            /* 43 */{ "Document.createBranchElement", null,
                                    null },
                            /* 44 */{ "Document.createLeafElement",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, null,
                                    "0", "1" },
                            /* 45 */{ "Document.createPosition", "0" },
                            /* 46 */{ "Document.createPosition", "1" }, })

            ) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected changeHighlight() to call another sequence of events");
            }

            InstrumentedUILog.clear();
            idh.removeHighlight(handle);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "DefaultHighlighter.removeHighlight", handle },
            /* 2 */{ "JTextComponent.getUI" },
            /* 3 */{ "TextUI.damageRange", ijtc, "0", "2" }, })
                    && !InstrumentedUILog.equals(new Object[][] {
                            /* 1 */{ "DefaultHighlighter.removeHighlight",
                                    handle },
                            /* 2 */{ "JTextComponent.getDocument" },
                            /* 3 */{ "Document.getDocumentProperties" },
                            /* 4 */{ "Document.createPosition", "0" },
                            /* 5 */{ "Document.createPosition", "1" },
                            /* 6 */{ "Document.getDocumentProperties" },
                            /* 7 */{ "Document.createDefaultRoot" },
                            /* 8 */{ "Document.createBranchElement", null,
                                    null },
                            /* 9 */{ "Document.createLeafElement",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, null,
                                    "0", "1" },
                            /* 10 */{ "Document.createPosition", "0" },
                            /* 11 */{ "Document.createPosition", "1" },
                            /* 12 */{ "JTextComponent.getUI" },
                            /* 13 */{ "JTextComponent.getDocument" },
                            /* 14 */{ "Document.getDocumentProperties" },
                            /* 15 */{ "Document.createPosition", "0" },
                            /* 16 */{ "Document.createPosition", "1" },
                            /* 17 */{ "Document.getDocumentProperties" },
                            /* 18 */{ "Document.createDefaultRoot" },
                            /* 19 */{ "Document.createBranchElement", null,
                                    null },
                            /* 20 */{ "Document.createLeafElement",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, null,
                                    "0", "1" },
                            /* 21 */{ "Document.createPosition", "0" },
                            /* 22 */{ "Document.createPosition", "1" }, })) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected removeHighlight() to call another sequence of events");
            }

            idh.addHighlight(0, 1, ihp);
            idh.addHighlight(5, 7, ihp);

            InstrumentedUILog.clear();
            idh.removeAllHighlights();
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "DefaultHighlighter.removeAllHighlights" },
            /* 2 */{ "JTextComponent.getUI" },
            /* 3 */{ "DefaultHighlighter.getDrawsLayeredHighlights" },
            /* 4 */{ "TextUI.damageRange", ijtc, "0", "7" }, }) && 
            
            !InstrumentedUILog.equals(new Object[][] {
                    /* 1 */ { "DefaultHighlighter.removeAllHighlights"}, 
                    /* 2 */ { "JTextComponent.getUI"}, 
                    /* 3 */ { "DefaultHighlighter.getDrawsLayeredHighlights"}, 
                    /* 4 */ { "JTextComponent.getDocument"}, 
                    /* 5 */ { "Document.getDocumentProperties"}, 
                    /* 6 */ { "Document.createPosition", "0"}, 
                    /* 7 */ { "Document.createPosition", "1"}, 
                    /* 8 */ { "Document.getDocumentProperties"}, 
                    /* 9 */ { "Document.createDefaultRoot"}, 
                    /* 10 */ { "Document.createBranchElement", null, null}, 
                    /* 11 */ { "Document.createLeafElement", InstrumentedUILog.ANY_NON_NULL_VALUE, null, "0", "1"}, 
                    /* 12 */ { "Document.createPosition", "0"}, 
                    /* 13 */ { "Document.createPosition", "1"}, 
                    /* 14 */ { "Document.createPosition", "0"}, 
                    /* 15 */ { "Document.createPosition", "7"}, 
                    /* 16 */ { "JTextComponent.getDocument"}, 
                    /* 17 */ { "Document.getDocumentProperties"}, 
                    /* 18 */ { "Document.createPosition", "0"}, 
                    /* 19 */ { "Document.createPosition", "1"}, 
                    /* 20 */ { "Document.getDocumentProperties"}, 
                    /* 21 */ { "Document.createDefaultRoot"}, 
                    /* 22 */ { "Document.createBranchElement", null, null}, 
                    /* 23 */ { "Document.createLeafElement", InstrumentedUILog.ANY_NON_NULL_VALUE, null, "0", "1"}, 
                    /* 24 */ { "Document.createPosition", "0"}, 
                    /* 25 */ { "Document.createPosition", "1"}, 
                    /* 26 */ { "JTextComponent.getUI"}, 
                    /* 27 */ { "JTextComponent.getDocument"}, 
                    /* 28 */ { "Document.getDocumentProperties"}, 
                    /* 29 */ { "Document.createPosition", "0"}, 
                    /* 30 */ { "Document.createPosition", "1"}, 
                    /* 31 */ { "Document.getDocumentProperties"}, 
                    /* 32 */ { "Document.createDefaultRoot"}, 
                    /* 33 */ { "Document.createBranchElement", null, null}, 
                    /* 34 */ { "Document.createLeafElement", InstrumentedUILog.ANY_NON_NULL_VALUE, null, "0", "1"}, 
                    /* 35 */ { "Document.createPosition", "0"}, 
                    /* 36 */ { "Document.createPosition", "1"}, 
            })
            
            ) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected removeAllHighlights to call another sequence of events");
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("unexpected BadLocationException");
        }
        return passed();
    }

    public Result testPaint() {
        InstrumentedDefaultHighlighter idh = new InstrumentedDefaultHighlighter();
        InstrumentedGraphics ig = new InstrumentedGraphics();

        InstrumentedUILog.clear();
        idh.paint(ig);

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultHighlighter.paint", ig }, })) {
            InstrumentedUILog.printLog();
            return failed("expected paint() not to call anything");
        }

        return passed();
    }

    public Result testInstall() {
        InstrumentedDefaultHighlighter idh = new InstrumentedDefaultHighlighter();
        InstrumentedJTextComponent ijtc = new InstrumentedJTextComponent();

        InstrumentedUILog.clear();
        idh.install(ijtc);

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultHighlighter.install", ijtc },
        /* 2 */{ "DefaultHighlighter.removeAllHighlights" },
        /* 3 */{ "JTextComponent.getUI" },
        /* 4 */{ "DefaultHighlighter.getDrawsLayeredHighlights" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected install() to call another sequence of events");
        }

        return passed();
    }

    public Result testLayeredHighlights() {
        InstrumentedDefaultHighlighter idh = new InstrumentedDefaultHighlighter();
        InstrumentedHighlightPainter ihp = new InstrumentedHighlightPainter();
        InstrumentedGraphics ig = new InstrumentedGraphics();
        InstrumentedShape is = new InstrumentedShape();
        InstrumentedView iv = new InstrumentedView();

        InstrumentedJTextComponent ijtc = new InstrumentedJTextComponent() {
            public Document getDocument() {
                super.getDocument();
                return new InstrumentedDocument();
            }
        };

        ijtc.setHighlighter(idh);
        idh.install(ijtc);

        try {
            idh.addHighlight(0, 1, ihp);
            idh.addHighlight(5, 7, ihp);

            if (!idh.getDrawsLayeredHighlights()) {
                return failed("expected getDrawsLayeredHighlights to be true by default");
            }

            InstrumentedUILog.clear();
            idh.setDrawsLayeredHighlights(false);
            if (!InstrumentedUILog
                    .equals(new Object[][] {
                    /* 1 */{ "DefaultHighlighter.setDrawsLayeredHighlights",
                            "false" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected setDrawsLayeredHighlights(false) not to call anything");
            }

            if (idh.getDrawsLayeredHighlights()) {
                return failed("expected getDrawsLayeredHighlights() to return what was set");
            }

            InstrumentedUILog.clear();
            idh.paintLayeredHighlights(ig, 0, 10, is, ijtc, iv);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "DefaultHighlighter.paintLayeredHighlights", ig, "0",
                    "10", is, ijtc, iv }, })) {
                InstrumentedUILog.printLog();
                return failed("expected paintLayeredHighlights(...) not to call anything");
            }

            InstrumentedUILog.clear();
            idh.setDrawsLayeredHighlights(true);
            if (!InstrumentedUILog
                    .equals(new Object[][] {
                    /* 1 */{ "DefaultHighlighter.setDrawsLayeredHighlights",
                            "true" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected setDrawsLayeredHighlights(true) not to call anything");
            }

            if (!idh.getDrawsLayeredHighlights()) {
                return failed("expected getDrawsLayeredHighlights() to return what was set");
            }

            InstrumentedUILog.clear();
            idh.paintLayeredHighlights(ig, 0, 10, is, ijtc, iv);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "DefaultHighlighter.paintLayeredHighlights", ig, "0",
                    "10", is, ijtc, iv }, })) {
                InstrumentedUILog.printLog();
                return failed("expected paintLayeredHighlights(...) not to call anything");
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return passed();
    }

}