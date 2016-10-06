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

package org.apache.harmony.test.func.api.javax.swing.text.AbstractDocument;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAbstractDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAbstractElement;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDefaultDocumentEvent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUndoableEdit;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class DefaultDocumentEventTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new DefaultDocumentEventTest().test(args));
    }

    public Result testConstructor() {
        InstrumentedAbstractDocument ad = new InstrumentedAbstractDocument();

        DocumentEvent.EventType[] types = new DocumentEvent.EventType[] {
                DocumentEvent.EventType.CHANGE, DocumentEvent.EventType.INSERT,
                DocumentEvent.EventType.REMOVE };

        for (int i = 0; i < types.length; ++i) {
            InstrumentedUILog.clear();
            new InstrumentedDefaultDocumentEvent(ad, 0, 3, types[i]);
            if (!InstrumentedUILog.equals(new Object[][] {})) {
                InstrumentedUILog.printLog();
                return failed("expected DocumentEventType constructor with "
                        + types[i] + " not to call any more methods");
            }
        }
        new InstrumentedDefaultDocumentEvent(ad, 0, 3, null); //null is ok

        return passed();
    }

    public Result testAddEdit() {
        InstrumentedDefaultDocumentEvent idde = new InstrumentedDefaultDocumentEvent();

        InstrumentedUndoableEdit iue = new InstrumentedUndoableEdit();

        InstrumentedUILog.clear();

        idde.addEdit(iue);

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultDocumentEvent.addEdit", iue },
        /* 2 */{ "DefaultDocumentEvent.lastEdit" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected addEdit to call lastEdit");
        }

        InstrumentedUndoableEdit iue1 = new InstrumentedUndoableEdit();
        InstrumentedUILog.clear();
        idde.addEdit(iue1);

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultDocumentEvent.addEdit", iue1 },
        /* 2 */{ "DefaultDocumentEvent.lastEdit" },
        /* 3 */{ "UndoableEdit.addEdit", iue1 },
        /* 4 */{ "UndoableEdit.replaceEdit", iue }, })) {
            InstrumentedUILog.printLog();
            return failed("expected second addEdit to call lastEdit, addEdit, replaceEdit");
        }

        try {
            idde.addEdit(null);
            return failed("expected addEdit(null) to cause NPE");
        } catch (NullPointerException e) {
        }

        return passed();
    }

    public Result testGetChange() {
        InstrumentedDefaultDocumentEvent idde = new InstrumentedDefaultDocumentEvent();
        InstrumentedAbstractElement iae = new InstrumentedAbstractElement();

        InstrumentedUILog.clear();

        idde.getChange(iae);

        if (!InstrumentedUILog.equals(new Object[][] { {
                "DefaultDocumentEvent.getChange", iae }

        })) {
            InstrumentedUILog.printLog();
            return failed("expected getChange() not to call any other methods");
        }

        return passed();
    }

    public Result testGetDocument() {
        InstrumentedAbstractDocument ad = new InstrumentedAbstractDocument();

        if (new InstrumentedDefaultDocumentEvent(ad, 0, 3,
                DocumentEvent.EventType.CHANGE).getDocument() != ad) {
            return failed("expected getDocument() to return constructor argument");
        }
        ;

        return passed();
    }

    public Result testGetLength() {
        int length = Math.abs((int) Math.random());

        if (new InstrumentedDefaultDocumentEvent(
                new InstrumentedAbstractDocument(), 0, length,
                DocumentEvent.EventType.CHANGE).getLength() != length) {
            return failed("expected getLength() to return constructor argument("
                    + length + ")");
        }
        ;

        return passed();
    }

    public Result testGetOffset() {
        int offset = Math.abs((int) Math.random());

        if (new InstrumentedDefaultDocumentEvent(
                new InstrumentedAbstractDocument(), offset, 3,
                DocumentEvent.EventType.CHANGE).getOffset() != offset) {
            return failed("expected getOffset() to return constructor argument("
                    + offset + ")");
        }
        ;

        return passed();
    }

    public Result testGetType() {
        DocumentEvent.EventType type = new DocumentEvent.EventType[] {
                DocumentEvent.EventType.CHANGE, DocumentEvent.EventType.INSERT,
                DocumentEvent.EventType.REMOVE }[Math.abs((int) Math.random()) % 3];

        if (new InstrumentedDefaultDocumentEvent(
                new InstrumentedAbstractDocument(), 0, 3, type).getType() != type) {
            return failed("expected getType() to return constructor argument("
                    + type + ")");
        }
        ;

        return passed();
    }

    public Result testIsSignificant() {
        if (!new InstrumentedDefaultDocumentEvent().isSignificant()) {
            return failed("expected significant to be true by default");
        }

        return passed();
    }

    public Result testUndo() {
        InstrumentedDefaultDocumentEvent idde = new InstrumentedDefaultDocumentEvent() {
            public boolean isInProgress() {
                super.isInProgress();
                return false;
            }
        };
        idde.addEdit(new InstrumentedUndoableEdit(1));
        idde.addEdit(new InstrumentedUndoableEdit(2));

        InstrumentedUILog.clear();
        try {
            idde.undo();
        } catch (NullPointerException e) {
        }
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultDocumentEvent.undo" },
        /* 2 */{ "DefaultDocumentEvent.canUndo" },
        /* 3 */{ "DefaultDocumentEvent.isInProgress" },
        /* 4 */{ "UndoableEdit2.undo" },
        /* 5 */{ "UndoableEdit1.undo" },
        /* 6 */{ "AbstractDocument.fireChangedUpdate", idde }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultDocumentEvent.undo" },
                /* 2 */{ "DefaultDocumentEvent.canUndo" },
                /* 3 */{ "DefaultDocumentEvent.isInProgress" },
                /* 4 */{ "UndoableEdit2.undo" },
                /* 5 */{ "UndoableEdit1.undo" },
                /* 6 */{ "DefaultDocumentEvent.getType" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultDocumentEvent.undo" },
                /* 2 */{ "DefaultDocumentEvent.canUndo" },
                /* 3 */{ "DefaultDocumentEvent.isInProgress" },
                /* 4 */{ "UndoableEdit2.undo" },
                /* 5 */{ "UndoableEdit1.undo" },
                /* 6 */{ "DefaultDocumentEvent.getType" },
                /* 7 */{ "DefaultDocumentEvent.getType" },
                /* 8 */{ "DefaultDocumentEvent.getType" },
                /* 9 */{ "AbstractDocument.fireChangedUpdate", InstrumentedUILog.ANY_NON_NULL_VALUE }, })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected first undo to call another sequence of events");
        }

        InstrumentedUILog.clear();
        try {
            idde.undo();
            return failed("expected second undo to throw CannotUndoException");
        } catch (CannotUndoException e) {
        }

        idde.addEdit(new InstrumentedUndoableEdit(3));
        try {
            idde.undo();
            return failed("expected second undo to throw CannotUndoException");
        } catch (CannotUndoException e) {
        }

        idde.redo();
        try {
            idde.undo();
        } catch (NullPointerException e) {
        }

        return passed();
    }

    public Result testRedo() {
        InstrumentedDefaultDocumentEvent idde = new InstrumentedDefaultDocumentEvent() {
            public boolean isInProgress() {
                super.isInProgress();
                return false;
            }
        };
        InstrumentedUndoableEdit iue1 = new InstrumentedUndoableEdit(1);
        InstrumentedUndoableEdit iue2 = new InstrumentedUndoableEdit(2);

        idde.addEdit(iue1);
        idde.addEdit(iue2);

        InstrumentedUILog.clear();
        try {
            idde.redo();
            return failed("can't redo before undo");
        } catch (CannotRedoException e) {
        } catch (NullPointerException e) {
        }
        try {
            idde.undo();
        } catch (NullPointerException e) {
        }

        InstrumentedUILog.clear();
        idde.redo();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultDocumentEvent.redo" },
        /* 2 */{ "DefaultDocumentEvent.canRedo" },
        /* 3 */{ "DefaultDocumentEvent.isInProgress" },
        /* 4 */{ "UndoableEdit1.redo" },
        /* 5 */{ "UndoableEdit2.redo" },
        /* 6 */{ "AbstractDocument.fireChangedUpdate", idde }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "DefaultDocumentEvent.redo" },
                        /* 2 */{ "DefaultDocumentEvent.canRedo" },
                        /* 3 */{ "DefaultDocumentEvent.isInProgress" },
                        /* 4 */{ "UndoableEdit1.redo" },
                        /* 5 */{ "UndoableEdit2.redo" },
                        /* 6 */{ "DefaultDocumentEvent.getType" },
                        /* 7 */{ "AbstractDocument.fireChangedUpdate",
                                InstrumentedUILog.ANY_NON_NULL_VALUE }, })) {
            InstrumentedUILog.printLog();
            return failed("expected second undo to call another sequence of events");
        }

        try {
            idde.redo();
            return failed("can't redo 2 times");
        } catch (CannotRedoException e) {
        }

        try {
            idde.undo();
        } catch (NullPointerException e) {
        }
        idde.redo();
        return passed();
    }

}