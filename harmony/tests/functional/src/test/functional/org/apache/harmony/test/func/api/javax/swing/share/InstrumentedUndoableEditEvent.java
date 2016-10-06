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

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;

public class InstrumentedUndoableEditEvent extends UndoableEditEvent {
    public InstrumentedUndoableEditEvent() {
        this(new InstrumentedAbstractDocument(), new InstrumentedUndoableEdit());
    }

    
    public InstrumentedUndoableEditEvent(Object arg0, UndoableEdit arg1) {
        super(arg0, arg1);
    }

    public UndoableEdit getEdit() {
        InstrumentedUILog.add(new Object[] { "UndoableEditEvent.getEdit" });
        return super.getEdit();
    }

    public Object getSource() {
        InstrumentedUILog.add(new Object[] { "UndoableEditEvent.getSource" });
        return super.getSource();
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "UndoableEditEvent.toString" });
        return super.toString();
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "UndoableEditEvent.hashCode" });
        return super.hashCode();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "UndoableEditEvent.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog
                .add(new Object[] { "UndoableEditEvent.equals", arg0 });
        return super.equals(arg0);
    }

}