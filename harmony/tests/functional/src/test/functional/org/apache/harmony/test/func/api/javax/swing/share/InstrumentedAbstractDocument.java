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

package org.apache.harmony.test.func.api.javax.swing.share;

import java.io.PrintStream;
import java.util.Dictionary;
import java.util.EventListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

public class InstrumentedAbstractDocument extends AbstractDocument {
    public InstrumentedAbstractDocument() {
        super(new InstrumentedContent() {
            public Position createPosition(final int arg0)
                    throws BadLocationException {
                //InstrumentedUILog.add(new Object[] {
                // "AbstractDocument.Content.createPosition", "" + arg0 });

                try {
                    super.createPosition(arg0);
                } catch (Throwable e) {
                }
                return new Position() {
                    private int pos = arg0;

                    public int getOffset() {

                        return pos;
                    }
                };
            }
        });
    }

    public InstrumentedAbstractDocument(Content arg0) {
        super(arg0);
    }

    public InstrumentedAbstractDocument(Content arg0, AttributeContext arg1) {
        super(arg0, arg1);
    }

    public Element getDefaultRootElement() {
        InstrumentedUILog
                .add(new Object[] { "AbstractDocument.getDefaultRootElement" });
        return null;
    }

    public Element getParagraphElement(int arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.getParagraphElement", "" + arg0 });
        return null;
    }

    public void addDocumentListener(DocumentListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.addDocumentListener", arg0 });
        super.addDocumentListener(arg0);
    }

    public void addUndoableEditListener(UndoableEditListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.addUndoableEditListener", arg0 });
        super.addUndoableEditListener(arg0);
    }

    protected Element createBranchElement(Element arg0, AttributeSet arg1) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.createBranchElement", arg0, arg1 });
        return super.createBranchElement(arg0, arg1);
    }

    public Element createLeafElement(Element arg0, AttributeSet arg1, int arg2,
            int arg3) {
        InstrumentedUILog.add(new Object[] { "createLeafElement", arg0, arg1,
                "" + arg2, "" + arg3 });
        return super.createLeafElement(arg0, arg1, arg2, arg3);
    }

    public Position createPosition(int arg0) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.createPosition",
                "" + arg0 });

        return super.createPosition(arg0);

    }

    public void dump(PrintStream arg0) {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.dump", arg0 });
        super.dump(arg0);
    }

    public void fireChangedUpdate(DocumentEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.fireChangedUpdate", arg0 });
        super.fireChangedUpdate(arg0);
    }

    public void fireInsertUpdate(DocumentEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.fireInsertUpdate", arg0 });
        super.fireInsertUpdate(arg0);
    }

    public void fireRemoveUpdate(DocumentEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.fireRemoveUpdate", arg0 });
        super.fireRemoveUpdate(arg0);
    }

    public void fireUndoableEditUpdate(UndoableEditEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.fireUndoableEditUpdate", arg0 });
        super.fireUndoableEditUpdate(arg0);
    }

    public int getAsynchronousLoadPriority() {
        InstrumentedUILog
                .add(new Object[] { "AbstractDocument.getAsynchronousLoadPriority" });
        return super.getAsynchronousLoadPriority();
    }

    public Element getBidiRootElement() {
        InstrumentedUILog
                .add(new Object[] { "AbstractDocument.getBidiRootElement" });
        return super.getBidiRootElement();
    }

    public DocumentFilter getDocumentFilter() {
        InstrumentedUILog
                .add(new Object[] { "AbstractDocument.getDocumentFilter" });
        return super.getDocumentFilter();
    }

    public DocumentListener[] getDocumentListeners() {
        InstrumentedUILog
                .add(new Object[] { "AbstractDocument.getDocumentListeners" });
        return super.getDocumentListeners();
    }

    public Dictionary getDocumentProperties() {
        InstrumentedUILog
                .add(new Object[] { "AbstractDocument.getDocumentProperties" });
        return super.getDocumentProperties();
    }

    public int getLength() {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.getLength" });
        return super.getLength();
    }

    public EventListener[] getListeners(Class arg0) {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.getListeners",
                arg0 });
        return super.getListeners(arg0);
    }

    public Element[] getRootElements() {
        InstrumentedUILog
                .add(new Object[] { "AbstractDocument.getRootElements" });
        return super.getRootElements();
    }

    public void getText(int arg0, int arg1, Segment arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.getText",
                "" + arg0, "" + arg1, arg2 });
        super.getText(arg0, arg1, arg2);
    }

    public String getText(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.getText",
                "" + arg0, "" + arg1 });
        return super.getText(arg0, arg1);
    }

    public UndoableEditListener[] getUndoableEditListeners() {
        InstrumentedUILog
                .add(new Object[] { "AbstractDocument.getUndoableEditListeners" });
        return super.getUndoableEditListeners();
    }

    public void insertString(int arg0, String arg1, AttributeSet arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.insertString",
                "" + arg0, arg1, arg2 });
        super.insertString(arg0, arg1, arg2);
    }

    public void insertUpdate(DefaultDocumentEvent arg0, AttributeSet arg1) {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.insertUpdate",
                arg0, arg1 });
        try {
            super.insertUpdate(arg0, arg1);
        } catch (NullPointerException e) {
        }
    }

    public void postRemoveUpdate(DefaultDocumentEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.postRemoveUpdate", arg0 });
        try {
            super.postRemoveUpdate(arg0);
        } catch (NullPointerException e) {
        }
    }

    public void remove(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.remove",
                "" + arg0, "" + arg1 });
        super.remove(arg0, arg1);
    }

    public void removeDocumentListener(DocumentListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.removeDocumentListener", arg0 });
        super.removeDocumentListener(arg0);
    }

    public void removeUndoableEditListener(UndoableEditListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.removeUndoableEditListener", arg0 });
        super.removeUndoableEditListener(arg0);
    }

    public void removeUpdate(DefaultDocumentEvent arg0) {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.removeUpdate",
                arg0 });
        super.removeUpdate(arg0);
    }

    public void render(Runnable arg0) {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.render", arg0 });
        super.render(arg0);
    }

    public void replace(int arg0, int arg1, String arg2, AttributeSet arg3)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.replace",
                "" + arg0, "" + arg1, arg2, arg3 });
        super.replace(arg0, arg1, arg2, arg3);
    }

    public void setAsynchronousLoadPriority(int arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.setAsynchronousLoadPriority", "" + arg0 });
        super.setAsynchronousLoadPriority(arg0);
    }

    public void setDocumentFilter(DocumentFilter arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.setDocumentFilter", arg0 });
        super.setDocumentFilter(arg0);
    }

    public void setDocumentProperties(Dictionary arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractDocument.setDocumentProperties", arg0 });
        super.setDocumentProperties(arg0);
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.equals", arg0 });
        return super.equals(arg0);
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.hashCode" });
        return super.hashCode();
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "AbstractDocument.toString" });
        return super.toString();
    }

    public void writeLockExposed() {
        writeLock();
    }

    public void writeUnLockExposed() {
        writeUnlock();
    }

    public void readLockExposed() {
        readLock();
    }

    public void readUnLockExposed() {
        readUnlock();
    }

    public Thread getCurrentWriterExposed() {
        return getCurrentWriter();
    }

}