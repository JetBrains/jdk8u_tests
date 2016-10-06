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

import java.util.Dictionary;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAbstractDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAttributeContext;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAttributeSet;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedContent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDefaultDocumentEvent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDictionary;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDocumentListener;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedElement;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedSegment;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class AbstractDocumentTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new AbstractDocumentTest().test(args));
    }

    public Result testConstructorContent() {
        InstrumentedContent ic = new InstrumentedContent();

        InstrumentedUILog.clear();

        new InstrumentedAbstractDocument(ic);

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractDocument.getDocumentProperties" },
        /* 2 */{ "AbstractDocument.createPosition", "0" },
        /* 3 */{ "Content.createPosition", "0" },
        /* 4 */{ "AbstractDocument.createPosition", "1" },
        /* 5 */{ "Content.createPosition", "1" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected InstrumentedAbstractDocument(content) to call another sequence of events");
        }

        try {
            new InstrumentedAbstractDocument(null);
            return failed("expected InstrumentedAbstractDocument(null) to throw NPE");
        } catch (NullPointerException e) {
        }

        return passed();
    }

    public Result testConstructorContentContext() {
        InstrumentedContent ic = new InstrumentedContent();
        InstrumentedAttributeContext iac = new InstrumentedAttributeContext();

        InstrumentedUILog.clear();

        new InstrumentedAbstractDocument(ic, iac);

        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */{ "AttributeContext.getEmptySet" },
                        /* 2 */{ "AbstractDocument.getDocumentProperties" },
                        /* 3 */{ "AttributeContext.getEmptySet" },
                        /* 4 */{ "AttributeContext.addAttributes", null,
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 5 */{ "AbstractDocument.createPosition", "0" },
                        /* 6 */{ "Content.createPosition", "0" },
                        /* 7 */{ "AbstractDocument.createPosition", "1" },
                        /* 8 */{ "Content.createPosition", "1" },
                        /* 9 */{
                                "AttributeContext.addAttribute",
                                null,
                                javax.swing.text.StyleConstants.CharacterConstants.BidiLevel,
                                new Integer(0) }, })) {
            InstrumentedUILog.printLog();
            return failed("expected InstrumentedAbstractDocument(content, context) to call another sequence of events");
        }

        try {
            new InstrumentedAbstractDocument(null, iac);
            return failed("expected InstrumentedAbstractDocument(null, context) to throw NPE");
        } catch (NullPointerException e) {
        }

        try {
            new InstrumentedAbstractDocument(ic, null);
            return failed("expected InstrumentedAbstractDocument(content, null) to throw NPE");
        } catch (NullPointerException e) {
        }

        return passed();
    }

    //more in DocumentListenersTest
    public Result testAddDocumentListener() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();

        InstrumentedDocumentListener idl = new InstrumentedDocumentListener();

        InstrumentedUILog.clear();

        iad.addDocumentListener(idl);

        if (!InstrumentedUILog.equals(new Object[][] { {
                "AbstractDocument.addDocumentListener", idl } })) {

            InstrumentedUILog.printLog();

            return failed("expected addDocumentListener not to call any more methods");
        }

        return passed();
    }

    public Result testCreateLeafElement() {
        InstrumentedElement ie = new InstrumentedElement();
        InstrumentedAttributeSet ias = new InstrumentedAttributeSet();
        InstrumentedAbstractDocument id = new InstrumentedAbstractDocument();
        id.writeLockExposed();

        try {
            InstrumentedUILog.clear();

            id.createLeafElement(ie, ias, 10, 20);

            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "createLeafElement", ie, ias, "10", "20" },
            /* 2 */{ "AttributeSet.getAttributeCount" },
            /* 3 */{ "AttributeSet.getAttributeNames" },
            /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" },
            /* 5 */{ "AbstractDocument.createPosition", "10" },
            /* 6 */{ "Content.createPosition", "10" },
            /* 7 */{ "AbstractDocument.createPosition", "20" },
            /* 8 */{ "Content.createPosition", "20" }, })

                    && !InstrumentedUILog.equals(new Object[][] {
                            { "createLeafElement",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE },
                            { "AttributeSet.getAttributeCount" },
                            { "AttributeSet.getAttributeNames" },
                            { "AttributeSet.getAttribute", "AttributeName1" },
                            { "AttributeSet.createPosition", "10" },
                            { "AttributeSet.createPosition", "20" }, })

            ) {

                InstrumentedUILog.printLog();

                return failed("expected createLeafElements to call another sequence of events");
            }

            id.createLeafElement(ie, ias, -1, 20); //no exception
            id.createLeafElement(ie, ias, 6, 1);
        } finally {
            id.writeUnLockExposed();
        }

        return passed();
    }

    public Result testCreatePosition() {
        InstrumentedAbstractDocument id = new InstrumentedAbstractDocument();

        id.writeLockExposed();

        try {
            InstrumentedUILog.clear();

            try {
                id.createPosition(1); //null, because it is just forward to
                // method
                // returning null
            } catch (BadLocationException e) {
                InstrumentedUILog.printLog();
                e.printStackTrace();
                return failed("createPosition thrown exception");
            }

            if (!InstrumentedUILog.equals(new Object[][] { //createposition
                    // returns null
                            /* 1 */{ "AbstractDocument.createPosition", "1" },
                            /* 2 */{ "Content.createPosition", "1" }, })

            && !InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "AbstractDocument.createPosition", "1" }, })

            ) {
                InstrumentedUILog.printLog();

                return failed("expected AbstractDocument.createPosition to call Content.createPosition");
            }

        } finally {
            id.writeUnLockExposed();
        }

        return passed();
    }

    public Result testGetBidiRootElement() {
        InstrumentedAbstractDocument id = new InstrumentedAbstractDocument();

        InstrumentedUILog.clear();

        if (id.getBidiRootElement() == null) {
            return failed("expected bidiRoot to be not null");
        }

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractDocument.getBidiRootElement" },
        /* 2 */{ "AbstractDocument.createPosition", "1" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractDocument.getBidiRootElement" }, })) {
            InstrumentedUILog.printLog();

            return failed("expected getBidiRootElement to call createPosition or nothing");
        }

        return passed();
    }

    public Result testGetCurrentWriter() {
        final InstrumentedAbstractDocument id = new InstrumentedAbstractDocument();

        InstrumentedUILog.clear();

        if (id.getCurrentWriterExposed() != null) {
            return failed("expected getCurrentWriter to be null by default");
        }

        if (!InstrumentedUILog.equals(new Object[][] {})) {
            InstrumentedUILog.printLog();

            return failed("expected getCurrentWriter not to call any other methods");
        }

        InstrumentedUILog.clear();

        try {

            Thread thr = new Thread() {
                public synchronized void run() {
                    notifyAll();
                    id.writeLockExposed();
                    try {
                        wait();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    id.writeUnLockExposed();
                }
            };

            synchronized (thr) {
                thr.start();
                thr.wait();
            }

            if (id.getCurrentWriterExposed() != thr) {
                return failed("expected getCurrentWriter return owning thread");
            }
            if (!InstrumentedUILog.equals(new Object[][] {})) {
                InstrumentedUILog.printLog();

                return failed("expected getCurrentWriter not to call any other methods");
            }

            synchronized (thr) {
                thr.notifyAll();
            }

            thr.join();

            if (id.getCurrentWriterExposed() != null) {
                return failed("expected getCurrentWriter to return null ");
            }

            //ok - next try - thread doesn't call writeUnlock() on exit
            thr = new Thread() {
                public synchronized void run() {
                    id.writeLockExposed();
                }
            };
            thr.start();
            thr.join();
            if (id.getCurrentWriterExposed() != thr) {
                return failed("expected getCurrentWriter to not null even if thread is dead");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            return failed("error starting thread");
        }

        return passed();
    }

    public Result testDocumentProperties() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();
        InstrumentedUILog.clear();

        Dictionary d = iad.getDocumentProperties();

        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractDocument.getDocumentProperties" } })) {
            InstrumentedUILog.printLog();

            return failed("expected getCurrentWriter not to call any other methods");
        }

        if (d.size() != 1 || d.get("i18n") != Boolean.FALSE) {
            return failed("expected documentproperties to contain only i18n => false");
        }

        return passed();
    }

    public Result testGetLength() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();
        InstrumentedUILog.clear();

        if (iad.getLength() != 1233) {
            return failed("expected document length to be content length -1");
        }

        if (!InstrumentedUILog.equals(new Object[][] {
                { "AbstractDocument.getLength" }, { "Content.length" } })) {
            InstrumentedUILog.printLog();
            return failed("expected getLength to call content.length");
        }

        return passed();
    }

    public Result testGetProperty() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();
        InstrumentedUILog.clear();

        iad.getProperty(Math.class);

        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractDocument.getDocumentProperties" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected getProperty to be getDocumentProperties().get(key)");
        }

        return passed();
    }

    public Result testGetText() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();
        InstrumentedUILog.clear();

        try {
            if (iad.getText(0, 1) != null) {
                return failed("expected getText(0, 1) to return null");
            }
            if (!InstrumentedUILog.equals(new Object[][] {
                    { "AbstractDocument.getText", "0", "1" },
                    { "Content.getString", "0", "1" }, })) {
                InstrumentedUILog.printLog();
                return failed("expected AbstractDocument.getText to call Content.getString");
            }

            //trying to fire exception
            iad.getText(0, 0);
            iad.getText(-1, 1);

        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("got unexpected exception");
        } finally {
        }

        try {
            iad.getText(0, -1);
            return failed("expected getText(0, -1) to throw BadLocationException");
        } catch (BadLocationException e) {
        }

        return passed();
    }

    public Result testGetTextSegment() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();
        InstrumentedSegment is = new InstrumentedSegment();
        InstrumentedUILog.clear();

        try {
            iad.getText(0, 1, is);
            if (!InstrumentedUILog.equals(new Object[][] {
                    { "AbstractDocument.getText", "0", "1", is },
                    { "Content.getChars", "0", "1", is }, })) {
                InstrumentedUILog.printLog();
                return failed("expected AbstractDocument.getText to call Content.getString");
            }

            //trying to fire exception
            iad.getText(0, 0, is);
            iad.getText(-1, 1, is);

        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("got unexpected exception");
        } finally {
        }

        try {
            iad.getText(0, -1, is);
            return failed("expected getText(0, -1, Segment) to throw BadLocationException");
        } catch (BadLocationException e) {
        }

        return passed();
    }

    public Result testInsertString() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();
        InstrumentedAttributeSet ias = new InstrumentedAttributeSet();
        InstrumentedUILog.clear();

        try {
            iad.insertString(1, "abcd", ias);
            if (!InstrumentedUILog
                    .equals(new Object[][] {
                            /* 1 */{ "AbstractDocument.insertString", "1",
                                    "abcd", ias },
                            /* 2 */{ "AbstractDocument.getDocumentFilter" },
                            /* 3 */{ "Content.insertString", "1", "abcd" },
                            /* 4 */{ "AbstractDocument.getDocumentProperties" },
                            /* 5 */{ "AbstractDocument.getDocumentProperties" },
                            /* 6 */{ "AbstractDocument.insertUpdate",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE, ias },
                            /* 7 */{ "AbstractDocument.getDocumentProperties" },
                            /* 8 */{ "AbstractDocument.getDocumentProperties" },
                            /* 9 */{ "AbstractDocument.getText", "1", "4",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE },
                            /* 10 */{ "Content.getChars", "1", "4",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE },
                            /* 11 */{ "AbstractDocument.getDocumentProperties" },
                            /* 12 */{ "AbstractDocument.fireInsertUpdate",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE }, })) {
                InstrumentedUILog.printLog();
                return failed("expected another trace for insertString(1, abcd, ...)");
            }

            InstrumentedUILog.clear();
            iad.insertString(1, "", ias);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "AbstractDocument.insertString", "1", "", ias }, })) {
                InstrumentedUILog.printLog();
                return failed("expected no actions for insertString(1, '', ...)");
            }

            InstrumentedUILog.clear();
            iad.insertString(1, null, ias);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "AbstractDocument.insertString", "1", null, ias }, })) {
                InstrumentedUILog.printLog();
                return failed("expected no actions for insertString(1, null, ...)");
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
            return failed("unexpected exception");
        } finally {
        }

        return passed();

    }

    public Result testInsertUpdate() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument() {
            public Dictionary getDocumentProperties() {
                super.getDocumentProperties();
                return new InstrumentedDictionary();

            }

            public Element getParagraphElement(int pos) {
                super.getParagraphElement(pos);
                return new InstrumentedElement();
            }
        };
        InstrumentedAttributeSet ias = new InstrumentedAttributeSet();
        InstrumentedDefaultDocumentEvent idde = new InstrumentedDefaultDocumentEvent(null);
        InstrumentedUILog.clear();

        try {
            iad.insertUpdate(idde, ias);
            return failed("expected error because event type is null");
        } catch (Error e) {
        }

        idde = new InstrumentedDefaultDocumentEvent(iad, 0, 1,
                DocumentEvent.EventType.CHANGE);
        InstrumentedUILog.clear();
        try {
            iad.insertUpdate(idde, ias);
        } catch (ArrayIndexOutOfBoundsException npe) { //this seems to be
            // unavoidable NPE
            // - will analyze later
        }

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractDocument.insertUpdate", idde, ias },
        /* 2 */{ "AbstractDocument.getDocumentProperties" },
        /* 3 */{ "Dictionary.get", "i18n" },
        /* 4 */{ "DefaultDocumentEvent.getOffset" },
        /* 5 */{ "DefaultDocumentEvent.getLength" },
        /* 6 */{ "AbstractDocument.getParagraphElement", "0" },
        /* 7 */{ "Element.getStartOffset" },
        /* 8 */{ "AbstractDocument.getParagraphElement", "1" },
        /* 9 */{ "Element.getEndOffset" },
        /* 10 */{ "AbstractDocument.getDocumentProperties" },
        /* 11 */{ "Dictionary.get", InstrumentedUILog.ANY_NON_NULL_VALUE }, //java.awt.font.TextAttribute(run_direction)
                /* 12 */{ "AbstractDocument.getLength" },
                /* 13 */{ "Content.length" },

        })) {
            InstrumentedUILog.printLog();
            return failed("expected insertUpdate() to produce another trace");
        }
        return passed();

    }

    public Result testPostRemoveUpdate() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument() {
            public Dictionary getDocumentProperties() {
                super.getDocumentProperties();
                return new InstrumentedDictionary();

            }

            public Element getParagraphElement(int pos) {
                super.getParagraphElement(pos);
                return new InstrumentedElement();
            }
        };
        InstrumentedDefaultDocumentEvent idde = new InstrumentedDefaultDocumentEvent(null);
        InstrumentedUILog.clear();
        try {
            iad.postRemoveUpdate(idde);
            InstrumentedUILog.printLogAsArray();
            return failed("expected error because event type is null");
        } catch (Error e) {
        }

        idde = new InstrumentedDefaultDocumentEvent(iad, 0, 1,
                DocumentEvent.EventType.CHANGE);
        InstrumentedUILog.clear();

        try {
            iad.postRemoveUpdate(idde);
        } catch (ArrayIndexOutOfBoundsException ee) { //this seems to be
            // unavoidable exception
            // - will analyze later
        }

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractDocument.postRemoveUpdate", idde },
        /* 2 */{ "AbstractDocument.getDocumentProperties" },
        /* 3 */{ "Dictionary.get", "i18n" },
        /* 4 */{ "DefaultDocumentEvent.getOffset" },
        /* 5 */{ "DefaultDocumentEvent.getLength" },
        /* 6 */{ "AbstractDocument.getParagraphElement", "0" },
        /* 7 */{ "Element.getStartOffset" },
        /* 8 */{ "AbstractDocument.getParagraphElement", "1" },
        /* 9 */{ "Element.getEndOffset" },
        /* 10 */{ "AbstractDocument.getDocumentProperties" },
        /* 11 */{ "Dictionary.get", InstrumentedUILog.ANY_NON_NULL_VALUE }, //java.awt.font.TextAttribute(run_direction)
                /* 12 */{ "AbstractDocument.getLength" },
                /* 13 */{ "Content.length" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected insertUpdate() to produce another trace");
        }
        return passed();
    }

    public Result testRemoveUpdate() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument() {
            public Dictionary getDocumentProperties() {
                super.getDocumentProperties();
                return new InstrumentedDictionary();

            }

            public Element getParagraphElement(int pos) {
                super.getParagraphElement(pos);
                return new InstrumentedElement();
            }
        };
        InstrumentedDefaultDocumentEvent idde = new InstrumentedDefaultDocumentEvent();
        InstrumentedUILog.clear();
        iad.removeUpdate(idde);
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractDocument.removeUpdate", idde }, })) {
        }

        idde = new InstrumentedDefaultDocumentEvent(iad, 0, 1,
                DocumentEvent.EventType.CHANGE);
        InstrumentedUILog.clear();

        try {
            iad.removeUpdate(idde);
        } catch (NullPointerException npe) { //this seems to be unavoidable NPE
            // - will analyze later
        }

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractDocument.removeUpdate", idde }, })) {
            InstrumentedUILog.printLog();
            return failed("expected removeUpdate() not to call any more methods");
        }
        return passed();
    }

    public Result testPutProperty() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();

        InstrumentedUILog.clear();

        iad.putProperty("eee", "ggg");

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractDocument.getDocumentProperties" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected putProperty to call getDocumentProperties");
        }
        return passed();
    }

    public Result testNestedLocks() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();

        iad.readLockExposed();
        iad.readLockExposed();
        iad.readUnLockExposed();
        iad.readUnLockExposed();

        iad.writeLockExposed();
        iad.writeLockExposed();
        iad.writeUnLockExposed();

        if (iad.getCurrentWriterExposed() != Thread.currentThread()) {
            return failed("expected currentWriter to be current thread after writeLock, writeLock, writeUnlock");
        }

        iad.writeUnLockExposed();
        if (iad.getCurrentWriterExposed() != null) {
            return failed("expected currentWriter to be null after writeLock, writeLock, writeUnlock, writeUnlock");
        }

        try {
            iad.writeLockExposed();
            iad.readLockExposed();
            iad.writeUnLockExposed();
            iad.readUnLockExposed();
            return failed("expected error in writeLock, readLock, writeUnlock, readUnlock");
        } catch (Error e) {
        }

        if (iad.getCurrentWriterExposed() != null) {
            return failed("expected currentWriter to be null after writeLock, readLock, writeUnlock, readUnlock");
        }

        return passed();
    }

    public Result testLockDeadWriter() {
        final InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument();

        try {
            Thread thr = new Thread() {
                public void run() {
                    iad.writeLockExposed();
                }
            };

            thr.start();
            thr.join();

            thr = new Thread() {
                public void run() {
                    iad.writeLockExposed();
                }
            };

            thr.start();
            Thread.sleep(1000);

            if (!thr.isAlive()) {
                return failed("expected second writer to hang even if first writer is dead");
            }
            thr.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
            return failed("unexpected exception");
        }

        return passed();

    }

    public Result testRemove() {
        InstrumentedAbstractDocument iad = new InstrumentedAbstractDocument() {
            public Element getDefaultRootElement() {
                return new InstrumentedElement();
            }
        };

        InstrumentedUILog.clear();
        try {

            iad.remove(0, 1);

            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "AbstractDocument.remove", "0", "1" },
            /* 2 */{ "AbstractDocument.getDocumentFilter" },
            /* 3 */{ "AbstractDocument.getLength" },
            /* 4 */{ "Content.length" },
            /* 5 */{ "Element.isLeaf" },
            /* 6 */{ "Element.getElementIndex", "0" },
            /* 7 */{ "Element.getElement", "0" }, })) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected remove(0,1) to call another sequence of events");
            }
        } catch (NullPointerException e) { //expected
        } catch (BadLocationException e) {
        } finally {
        }

        return passed();
    }
}