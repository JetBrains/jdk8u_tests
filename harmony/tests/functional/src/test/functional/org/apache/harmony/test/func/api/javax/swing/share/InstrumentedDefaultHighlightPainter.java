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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

public class InstrumentedDefaultHighlightPainter extends
        DefaultHighlightPainter {
    public InstrumentedDefaultHighlightPainter() {
        this(Color.CYAN);
    }

    public InstrumentedDefaultHighlightPainter(Color arg0) {
        super(arg0);
    }

    public Color getColor() {
        InstrumentedUILog
                .add(new Object[] { "DefaultHighlightPainter.getColor" });
        return super.getColor();
    }

    public void paint(Graphics arg0, int arg1, int arg2, Shape arg3,
            JTextComponent arg4) {
        InstrumentedUILog.add(new Object[] { "DefaultHighlightPainter.paint",
                arg0, "" + arg1, "" + arg2, arg3, arg4 });
        super.paint(arg0, arg1, arg2, arg3, arg4);
    }

    public Shape paintLayer(Graphics arg0, int arg1, int arg2, Shape arg3,
            JTextComponent arg4, View arg5) {
        InstrumentedUILog.add(new Object[] {
                "DefaultHighlightPainter.paintLayer", arg0, "" + arg1,
                "" + arg2, arg3, arg4, arg5 });
        return super.paintLayer(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    public int hashCode() {
        InstrumentedUILog
                .add(new Object[] { "DefaultHighlightPainter.hashCode" });
        return super.hashCode();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "DefaultHighlightPainter.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultHighlightPainter.equals",
                arg0 });
        return super.equals(arg0);
    }

    public String toString() {
        InstrumentedUILog
                .add(new Object[] { "DefaultHighlightPainter.toString" });
        return super.toString();
    }
}