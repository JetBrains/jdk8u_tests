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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

public class InstrumentedUndoableEdit implements UndoableEdit {
    private int i = 0;

    public InstrumentedUndoableEdit() {
    }

    public InstrumentedUndoableEdit(int i) {
        this.i = i;
    }

    public void die() {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".die" });
    }

    public void redo() throws CannotRedoException {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".redo" });
    }

    public void undo() throws CannotUndoException {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".undo" });
    }

    public boolean canRedo() {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".canRedo" });
        return false;
    }

    public boolean canUndo() {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".canUndo" });
        return false;
    }

    public boolean isSignificant() {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".isSignificant" });
        return false;
    }

    public String getPresentationName() {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".getPresentationName" });
        return null;
    }

    public String getRedoPresentationName() {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".getRedoPresentationName" });
        return null;
    }

    public String getUndoPresentationName() {
        InstrumentedUILog.add(new Object[] { "UndoableEdit"
                + (i == 0 ? "" : "" + i) + ".getUndoPresentationName" });
        return null;
    }

    public boolean addEdit(UndoableEdit arg0) {
        InstrumentedUILog.add(new Object[] {
                "UndoableEdit" + (i == 0 ? "" : "" + i) + ".addEdit", arg0 });
        return false;
    }

    public boolean replaceEdit(UndoableEdit arg0) {
        InstrumentedUILog
                .add(new Object[] {
                        "UndoableEdit" + (i == 0 ? "" : "" + i)
                                + ".replaceEdit", arg0 });
        return false;
    }

}