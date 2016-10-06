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

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.AbstractDocument.Content;
import javax.swing.undo.UndoableEdit;

public class InstrumentedContent implements Content {
    public Position createPosition(final int arg0) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Content.createPosition",
                "" + arg0 });
        return new Position() {
            private int pos = arg0;

            public int getOffset() {
                return pos;
            }
        };
    }

    public void getChars(int arg0, int arg1, Segment arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Content.getChars", "" + arg0,
                "" + arg1, arg2 });
    }

    public String getString(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Content.getString", "" + arg0,
                "" + arg1 });
        return null;
    }

    public UndoableEdit insertString(int arg0, String arg1)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Content.insertString", "" + arg0,
                arg1 });
        return null;
    }

    public int length() {
        InstrumentedUILog.add(new Object[] { "Content.length" });
        return 1234;
    }

    public UndoableEdit remove(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "Content.remove", "" + arg0,
                "" + arg1 });
        return null;
    }
}