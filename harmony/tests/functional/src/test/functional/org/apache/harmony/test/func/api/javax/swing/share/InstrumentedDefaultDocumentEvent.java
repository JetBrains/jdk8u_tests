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

import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

public class InstrumentedDefaultDocumentEvent extends DefaultDocumentEvent {
    public InstrumentedDefaultDocumentEvent() {
        this(new InstrumentedAbstractDocument(), 0, 1, EventType.CHANGE);
    }
    
    public InstrumentedDefaultDocumentEvent(EventType et) {
        this(new InstrumentedAbstractDocument(), 0, 1, et);
    }


    public InstrumentedDefaultDocumentEvent(AbstractDocument abstractDocument,
            int arg0, int arg1, EventType arg2) {
        abstractDocument.super(arg0, arg1, arg2);
    }

    public boolean addEdit(UndoableEdit arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.addEdit",
                arg0 });
        return super.addEdit(arg0);
    }

    public ElementChange getChange(Element arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.getChange",
                arg0 });
        return super.getChange(arg0);
    }

    public Document getDocument() {
        InstrumentedUILog
                .add(new Object[] { "DefaultDocumentEvent.getDocument" });
        return super.getDocument();
    }

    public int getLength() {
        InstrumentedUILog
                .add(new Object[] { "DefaultDocumentEvent.getLength" });
        return super.getLength();
    }

    public int getOffset() {
        InstrumentedUILog
                .add(new Object[] { "DefaultDocumentEvent.getOffset" });
        return super.getOffset();
    }

    public String getPresentationName() {
        InstrumentedUILog
                .add(new Object[] { "DefaultDocumentEvent.getPresentationName" });
        return super.getPresentationName();
    }

    public String getRedoPresentationName() {
        InstrumentedUILog
                .add(new Object[] { "DefaultDocumentEvent.getRedoPresentationName" });
        return super.getRedoPresentationName();
    }

    public EventType getType() {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.getType" });
        return super.getType();
    }

    public String getUndoPresentationName() {
        InstrumentedUILog
                .add(new Object[] { "DefaultDocumentEvent.getUndoPresentationName" });
        return super.getUndoPresentationName();
    }

    public boolean isSignificant() {
        InstrumentedUILog
                .add(new Object[] { "DefaultDocumentEvent.isSignificant" });
        return super.isSignificant();
    }

    public void redo() throws CannotRedoException {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.redo" });
        super.redo();
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.toString" });
        return super.toString();
    }

    public void undo() throws CannotUndoException {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.undo" });
        super.undo();
    }

    public void die() {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.die" });
        super.die();
    }

    public void end() {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.end" });
        super.end();
    }

    public boolean canRedo() {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.canRedo" });
        return super.canRedo();
    }

    public boolean canUndo() {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.canUndo" });
        return super.canUndo();
    }

    public boolean isInProgress() {
        InstrumentedUILog
                .add(new Object[] { "DefaultDocumentEvent.isInProgress" });
        return super.isInProgress();
    }

    protected UndoableEdit lastEdit() {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.lastEdit" });
        return super.lastEdit();
    }

    public boolean replaceEdit(UndoableEdit arg0) {
        InstrumentedUILog.add(new Object[] {
                "DefaultDocumentEvent.replaceEdit", arg0 });
        return super.replaceEdit(arg0);
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.hashCode" });
        return super.hashCode();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultDocumentEvent.equals",
                arg0 });
        return super.equals(arg0);
    }
}