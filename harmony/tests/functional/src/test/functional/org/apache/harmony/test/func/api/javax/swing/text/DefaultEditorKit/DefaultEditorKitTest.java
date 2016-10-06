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

package org.apache.harmony.test.func.api.javax.swing.text.DefaultEditorKit;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.Action;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.ViewFactory;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDefaultEditorKit;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.text.DefaultEditorKit.share.*;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class DefaultEditorKitTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new DefaultEditorKitTest().test(args));
    }

    public Result testActionsConstructors() {
        InstrumentedUILog.clear();
        new InstrumentedBeepAction();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultEditorKit.BeepAction.putValue", "Name",
                        "beep" },
                /* 2 */{ "DefaultEditorKit.BeepAction.firePropertyChange",
                        "Name", null, "beep" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected BeepAction() to cause another sequence of events");
        }

        InstrumentedUILog.clear();
        new InstrumentedCopyAction();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultEditorKit.CopyAction.putValue", "Name",
                        "copy-to-clipboard" },
                /* 2 */{ "DefaultEditorKit.CopyAction.firePropertyChange",
                        "Name", null, "copy-to-clipboard" },

        })) {
            InstrumentedUILog.printLog();
            return failed("expected CopyAction() to cause another sequence of events");
        }

        InstrumentedUILog.clear();
        new InstrumentedCutAction();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultEditorKit.CutAction.putValue", "Name",
                        "cut-to-clipboard" },
                /* 2 */{ "DefaultEditorKit.CutAction.firePropertyChange",
                        "Name", null, "cut-to-clipboard" },

        })) {
            InstrumentedUILog.printLog();
            return failed("expected CutAction() to cause another sequence of events");
        }

        InstrumentedUILog.clear();
        new InstrumentedDefaultKeyTypedAction();
        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */{
                                "DefaultEditorKit.DefaultKeyTypedAction.putValue",
                                "Name", "default-typed" },
                        /* 2 */{
                                "DefaultEditorKit.DefaultKeyTypedAction.firePropertyChange",
                                "Name", null, "default-typed" },

                })) {

            InstrumentedUILog.printLog();
            return failed("expected DefaultKeyTypedAction() to cause another sequence of events");
        }

        InstrumentedUILog.clear();
        new InstrumentedInsertBreakAction();
        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */{
                                "DefaultEditorKit.InsertBreakAction.putValue",
                                "Name", "insert-break" },
                        /* 2 */{
                                "DefaultEditorKit.InsertBreakAction.firePropertyChange",
                                "Name", null, "insert-break" },

                })) {
            InstrumentedUILog.printLog();
            return failed("expected InsertBreakAction() to cause another sequence of events");
        }

        InstrumentedUILog.clear();
        new InstrumentedInsertContentAction();
        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */{
                                "DefaultEditorKit.InsertContentAction.putValue",
                                "Name", "insert-content" },
                        /* 2 */{
                                "DefaultEditorKit.InsertContentAction.firePropertyChange",
                                "Name", null, "insert-content" },

                })) {
            InstrumentedUILog.printLog();
            return failed("expected InsertContentAction() to cause another sequence of events");
        }

        InstrumentedUILog.clear();
        new InstrumentedInsertTabAction();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultEditorKit.InsertTabAction.putValue", "Name",
                        "insert-tab" },
                /* 2 */{
                        "DefaultEditorKit.InsertTabAction.firePropertyChange",
                        "Name", null, "insert-tab" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected InsertTabAction() to cause another sequence of events");
        }

        InstrumentedUILog.clear();
        new InstrumentedPasteAction();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultEditorKit.PasteAction.putValue", "Name",
                        "paste-from-clipboard" },
                /* 2 */{ "DefaultEditorKit.PasteAction.firePropertyChange",
                        "Name", null, "paste-from-clipboard" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected PasteAction() to cause another sequence of events");
        }

        return passed();
    }

    public Result testConstructor() {
        InstrumentedUILog.clear();

        new InstrumentedDefaultEditorKit();

        if (!InstrumentedUILog.equals(new Object[][] {})) {
            InstrumentedUILog.printLog();
            return failed("expected new DefaultEditorKit() not to call any other methods");
        }

        return passed();
    }

    public Result testCreateDefaultDocument() {
        InstrumentedDefaultEditorKit idek = new InstrumentedDefaultEditorKit();
        InstrumentedUILog.clear();

        idek.createDefaultDocument();

        if (!InstrumentedUILog
                .equals(new Object[][] { { "DefaultEditorKit.createDefaultDocument" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected DefaultEditorKit.createDefaultDocument() not to call any other methods");
        }

        return passed();
    }

    public Result testGetActions() {
        InstrumentedDefaultEditorKit idek = new InstrumentedDefaultEditorKit();
        InstrumentedUILog.clear();

        Action[] a = idek.getActions();

        if (!InstrumentedUILog
                .equals(new Object[][] { { "DefaultEditorKit.getActions" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected DefaultEditorKit.getActions() not to call any other methods");
        }

        if (a == null || a.length != 53) {
            return failed("expected 53 actions by default : " + a.length);
        }

        return passed();
    }

    public Result testGetViewFactory() {
        InstrumentedDefaultEditorKit idek = new InstrumentedDefaultEditorKit();
        InstrumentedUILog.clear();

        if (idek.getViewFactory() != null) {
            return failed("expected view factory to be null");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "DefaultEditorKit.getViewFactory" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected DefaultEditorKit.getViewFactory() not to call any other methods");
        }

        return passed();
    }

    public Result testRead() {
        InstrumentedDefaultEditorKit idek = new InstrumentedDefaultEditorKit();
        StringReader sr = new StringReader("Qwerty Uiop");
        InstrumentedDocument id = new InstrumentedDocument();

        InstrumentedUILog.clear();

        try {
            idek.read(sr, id, 0);
            if (!InstrumentedUILog
                    .equals(new Object[][] {
                            /* 1 */{ "DefaultEditorKit.read", sr, id, "0" },
                            /* 2 */{ "Document.getLength" },
                            /* 3 */{ "Document.insertString", "0",
                                    "Qwerty Uiop", null },
                            /* 4 */{ "Document.getDocumentProperties" },
                            /* 5 */{ "Document.getDocumentFilter" },
                            /* 6 */{ "Document.getDocumentProperties" },
                            /* 7 */{ "Document.getDocumentProperties" },
                            /* 8 */{ "Document.insertUpdate",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, null },
                            /* 9 */{ "Document.getDefaultRootElement" },
                            /* 10 */{ "Document.getText", "0", "11" },
                            /* 11 */{ "Document.getDocumentProperties" },
                            /* 12 */{ "Document.getDocumentProperties" },
                            /* 13 */{ "Document.getText", "0", "11", InstrumentedUILog.ANY_NON_NULL_VALUE },
                            /* 14 */{ "Document.fireInsertUpdate",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE },
                            /* 15 */{ "Document.fireUndoableEditUpdate",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE },
                            /* 16 */{ "Document.getDocumentProperties" },

                    })
            && !InstrumentedUILog
            .equals(new Object[][] {
                    /* 1 */{ "DefaultEditorKit.read", sr, id, "0" },
                    /* 2 */{ "Document.getLength" },
                    /* 3 */{ "Document.insertString", "0",
                            "Qwerty Uiop", null },
                    /* 4 */{ "Document.getDocumentProperties" },
                    /* 5 */{ "Document.getDocumentFilter" },
                    /* 6 */{ "Document.getDocumentProperties" },
                    /* 7 */{ "Document.getDocumentProperties" },
                    /* 8 */{ "Document.insertUpdate",
                            InstrumentedUILog.ANY_NON_NULL_VALUE, null },
                    /* 9 */{ "Document.getDefaultRootElement" },
                    /* 10 */  // { "Document.getText", "0", "11" },
                    /* 11 */{ "Document.getDocumentProperties" },
                    /* 12 */{ "Document.getDocumentProperties" },
                    /* 13 */{ "Document.getText", "0", "11", InstrumentedUILog.ANY_NON_NULL_VALUE },
                    /* 14 */{ "Document.fireInsertUpdate",
                            InstrumentedUILog.ANY_NON_NULL_VALUE },
                    /* 15 */{ "Document.fireUndoableEditUpdate",
                            InstrumentedUILog.ANY_NON_NULL_VALUE },
                    /* 16 */{ "Document.getDocumentProperties" },

            })        
            
            ) {
                InstrumentedUILog.printLog();
                return failed("expected read('Qwerty Uiop', document, 0) to call another sequence of events");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return failed("unexpected IOException");
        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("unexpected BadLocationException");
        }

        return passed();
    }

}

