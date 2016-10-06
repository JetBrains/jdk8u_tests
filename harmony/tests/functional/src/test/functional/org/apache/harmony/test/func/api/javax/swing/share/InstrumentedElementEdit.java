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
import javax.swing.text.Element;
import javax.swing.text.AbstractDocument.ElementEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;


public class InstrumentedElementEdit extends  ElementEdit {
    public InstrumentedElementEdit(Element arg0, int arg1, Element[] arg2, Element[] arg3) {
        super(arg0, arg1, arg2, arg3);
    }
    public Element[] getChildrenAdded() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.getChildrenAdded"} );
        return super.getChildrenAdded();
    }
    public Element[] getChildrenRemoved() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.getChildrenRemoved"} );
        return super.getChildrenRemoved();
    }
    public Element getElement() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.getElement"} );
        return super.getElement();
    }
    public int getIndex() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.getIndex"} );
        return super.getIndex();
    }
    public void redo() throws CannotRedoException {
        InstrumentedUILog.add(new Object[] {"ElementEdit.redo"} );
        super.redo();
    }
    public void undo() throws CannotUndoException {
        InstrumentedUILog.add(new Object[] {"ElementEdit.undo"} );
        super.undo();
    }
    public void die() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.die"} );
        super.die();
    }
    public boolean canRedo() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.canRedo"} );
        return super.canRedo();
    }
    public boolean canUndo() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.canUndo"} );
        return super.canUndo();
    }
    public boolean isSignificant() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.isSignificant"} );
        return super.isSignificant();
    }
    public String getPresentationName() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.getPresentationName"} );
        return super.getPresentationName();
    }
    public String getRedoPresentationName() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.getRedoPresentationName"} );
        return super.getRedoPresentationName();
    }
    public String getUndoPresentationName() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.getUndoPresentationName"} );
        return super.getUndoPresentationName();
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.toString"} );
        return super.toString();
    }
    public boolean addEdit(UndoableEdit arg0) {
        InstrumentedUILog.add(new Object[] {"ElementEdit.addEdit",  arg0} );
        return super.addEdit(arg0);
    }
    public boolean replaceEdit(UndoableEdit arg0) {
        InstrumentedUILog.add(new Object[] {"ElementEdit.replaceEdit",  arg0} );
        return super.replaceEdit(arg0);
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"ElementEdit.hashCode"} );
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] {"ElementEdit.clone"} );
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"ElementEdit.equals",  arg0} );
        return super.equals(arg0);
    }
}
