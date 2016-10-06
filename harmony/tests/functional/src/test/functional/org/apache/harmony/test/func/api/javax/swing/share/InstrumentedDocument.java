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
/*
 * Created on 25.05.2005
 *  
 */

package org.apache.harmony.test.func.api.javax.swing.share;

import java.io.PrintStream;
import java.util.Dictionary;
import java.util.EventListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;

public class InstrumentedDocument extends PlainDocument {

    protected AbstractElement createDefaultRoot() {
        InstrumentedUILog.add(new Object[] { "Document.createDefaultRoot"});
        return super.createDefaultRoot();
    }

    public Element getDefaultRootElement() {
        InstrumentedUILog
                .add(new Object[] { "Document.getDefaultRootElement" });
        return super.getDefaultRootElement();
    }

    public Element getParagraphElement(int arg0) {
        InstrumentedUILog.add(new Object[] { "Document.getParagraphElement", "" + arg0 });
        return super.getParagraphElement(arg0);
    }

    public void insertString(int arg0, String arg1, AttributeSet arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Document.insertString",
                "" + arg0, arg1, arg2 });
        super.insertString(arg0, arg1, arg2);
    }

    protected void insertUpdate(DefaultDocumentEvent arg0, AttributeSet arg1) {
        InstrumentedUILog.add(new Object[] { "Document.insertUpdate", arg0,
                arg1 });
        super.insertUpdate(arg0, arg1);
    }

    protected void removeUpdate(DefaultDocumentEvent arg0) {
        InstrumentedUILog.add("1605");
        super.removeUpdate(arg0);
    }

    public void addDocumentListener(DocumentListener arg0) {
        InstrumentedUILog.add(new Object[] { "Document.addDocumentListener",
                arg0 });
        super.addDocumentListener(arg0);
    }

    public void addUndoableEditListener(UndoableEditListener arg0) {
        InstrumentedUILog.add("1607");
        super.addUndoableEditListener(arg0);
    }

    protected Element createBranchElement(Element arg0, AttributeSet arg1) {
        InstrumentedUILog.add(new Object[] { "Document.createBranchElement", arg0, arg1});
   return super.createBranchElement(arg0, arg1);
    }

    protected Element createLeafElement(Element arg0, AttributeSet arg1,
            int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Document.createLeafElement", arg0, arg1, "" + arg2, "" + arg3});
        return super.createLeafElement(arg0, arg1, arg2, arg3);
    }

    public synchronized Position createPosition(int arg0)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Document.createPosition",
                "" + arg0 });
        return super.createPosition(arg0);
    }

    public void dump(PrintStream arg0) {
        InstrumentedUILog.add(new Object[] { "Document.dump", arg0});
        super.dump(arg0);
    }

    protected void fireChangedUpdate(DocumentEvent arg0) {
        InstrumentedUILog.add("1611");
        super.fireChangedUpdate(arg0);
    }

    protected void fireInsertUpdate(DocumentEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "Document.fireInsertUpdate", arg0 });
        super.fireInsertUpdate(arg0);
    }

    protected void fireRemoveUpdate(DocumentEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "Document.fireRemoveUpdate", arg0 });
        super.fireRemoveUpdate(arg0);
    }

    protected void fireUndoableEditUpdate(UndoableEditEvent arg0) {
        InstrumentedUILog.add(new Object[] { "Document.fireUndoableEditUpdate",
                arg0 });
        super.fireUndoableEditUpdate(arg0);
    }

    public int getAsynchronousLoadPriority() {
        InstrumentedUILog.add("1615");
        return super.getAsynchronousLoadPriority();
    }

    public Element getBidiRootElement() {
        InstrumentedUILog.add(new Object[] { "Document.getBidiRootElement" });
        return super.getBidiRootElement();
    }

    public DocumentFilter getDocumentFilter() {
        InstrumentedUILog.add(new Object[] { "Document.getDocumentFilter" });
        return super.getDocumentFilter();
    }

    public DocumentListener[] getDocumentListeners() {
        InstrumentedUILog.add(new Object[] { "Document.getDocumentListeners" });
        return super.getDocumentListeners();
    }

    public Dictionary getDocumentProperties() {
        InstrumentedUILog
                .add(new Object[] { "Document.getDocumentProperties" });
        return super.getDocumentProperties();
    }

    public int getLength() {
        InstrumentedUILog.add(new Object[] { "Document.getLength" });
        return super.getLength();
    }

    public EventListener[] getListeners(Class arg0) {
        InstrumentedUILog.add("1621");
        return super.getListeners(arg0);
    }

    public Element[] getRootElements() {
        InstrumentedUILog.add("1622");
        return super.getRootElements();
    }

    public void getText(int arg0, int arg1, Segment arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Document.getText", "" + arg0,
                "" + arg1, arg2 });
        super.getText(arg0, arg1, arg2);
    }

    public String getText(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Document.getText", "" + arg0,
                "" + arg1 });
        return super.getText(arg0, arg1);
    }

    public UndoableEditListener[] getUndoableEditListeners() {
        InstrumentedUILog.add(new Object[] { "Document.getUndoableEditListeners"});
        return super.getUndoableEditListeners();
    }

    protected void postRemoveUpdate(DefaultDocumentEvent arg0) {
        InstrumentedUILog.add("1626");
        super.postRemoveUpdate(arg0);
    }

    public void remove(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add("1627");
        super.remove(arg0, arg1);
    }

    public void removeDocumentListener(DocumentListener arg0) {
        InstrumentedUILog.add("1628");
        super.removeDocumentListener(arg0);
    }

    public void removeUndoableEditListener(UndoableEditListener arg0) {
        InstrumentedUILog.add("1629");
        super.removeUndoableEditListener(arg0);
    }

    public void render(Runnable arg0) {
        InstrumentedUILog.add("1630");
        super.render(arg0);
    }

    public void replace(int arg0, int arg1, String arg2, AttributeSet arg3)
            throws BadLocationException {
        InstrumentedUILog.add("1631");
        super.replace(arg0, arg1, arg2, arg3);
    }

    public void setAsynchronousLoadPriority(int arg0) {
        InstrumentedUILog.add("1632");
        super.setAsynchronousLoadPriority(arg0);
    }

    public void setDocumentFilter(DocumentFilter arg0) {
        InstrumentedUILog.add("1633");
        super.setDocumentFilter(arg0);
    }

    public void setDocumentProperties(Dictionary arg0) {
        InstrumentedUILog.add("1634");
        super.setDocumentProperties(arg0);
    }
}