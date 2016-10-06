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

import java.util.Vector;

import javax.swing.text.BadLocationException;
import javax.swing.text.GapContent;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;

public class InstrumentedGapContent extends GapContent {
    public InstrumentedGapContent() {
        super();

    }
    public InstrumentedGapContent(int arg0) {
        super(arg0);
    }
    public Object allocateArray(int arg0) {
        InstrumentedUILog.add(new Object[] {"GapContent.allocateArray", "" +  arg0} );
        return super.allocateArray(arg0);
    }

    public Position createPosition(int arg0) throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"GapContent.createPosition", "" +  arg0} );
        return super.createPosition(arg0);
    }

    public int getArrayLength() {
        InstrumentedUILog.add(new Object[] {"GapContent.getArrayLength"} );
        return super.getArrayLength();
    }

    public void getChars(int arg0, int arg1, Segment arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"GapContent.getChars", "" +  arg0, "" +  arg1,  arg2} );
        super.getChars(arg0, arg1, arg2);
    }

    public Vector getPositionsInRange(Vector arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] {"GapContent.getPositionsInRange",  arg0, "" +  arg1, "" +  arg2} );
        return super.getPositionsInRange(arg0, arg1, arg2);
    }

    public String getString(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"GapContent.getString", "" +  arg0, "" +  arg1} );
        return super.getString(arg0, arg1);
    }

    public UndoableEdit insertString(int arg0, String arg1)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"GapContent.insertString", "" +  arg0,  arg1} );
        return super.insertString(arg0, arg1);
    }

    public int length() {
        InstrumentedUILog.add(new Object[] {"GapContent.length"} );
        return super.length();
    }

    public UndoableEdit remove(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"GapContent.remove", "" +  arg0, "" +  arg1} );
        return super.remove(arg0, arg1);
    }

    public void resetMarksAtZero() {
        InstrumentedUILog.add(new Object[] {"GapContent.resetMarksAtZero"} );
        super.resetMarksAtZero();
    }

    public void shiftEnd(int arg0) {
        InstrumentedUILog.add(new Object[] {"GapContent.shiftEnd", "" +  arg0} );
        super.shiftEnd(arg0);
    }

    public void shiftGap(int arg0) {
        InstrumentedUILog.add(new Object[] {"GapContent.shiftGap", "" +  arg0} );
        super.shiftGap(arg0);
    }

    public void shiftGapEndUp(int arg0) {
        InstrumentedUILog.add(new Object[] {"GapContent.shiftGapEndUp", "" +  arg0} );
        super.shiftGapEndUp(arg0);
    }

    public void shiftGapStartDown(int arg0) {
        InstrumentedUILog.add(new Object[] {"GapContent.shiftGapStartDown", "" +  arg0} );
        super.shiftGapStartDown(arg0);
    }

    public void updateUndoPositions(Vector arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] {"GapContent.updateUndoPositions",  arg0, "" +  arg1, "" +  arg2} );
        super.updateUndoPositions(arg0, arg1, arg2);
    }

    protected void replace(int arg0, int arg1, Object arg2, int arg3) {
        InstrumentedUILog.add(new Object[] {"GapContent.replace", "" +  arg0, "" +  arg1,  arg2, "" +  arg3} );
        super.replace(arg0, arg1, arg2, arg3);
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"GapContent.hashCode"} );
        return super.hashCode();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] {"GapContent.clone"} );
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"GapContent.equals",  arg0} );
        return super.equals(arg0);
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] {"GapContent.toString"} );
        return super.toString();
    }
    
    public int getGapStartExposed() {
        return this.getGapStart();
    }
    
    public int getGapEndExposed() {
        return this.getGapEnd();
    }

}