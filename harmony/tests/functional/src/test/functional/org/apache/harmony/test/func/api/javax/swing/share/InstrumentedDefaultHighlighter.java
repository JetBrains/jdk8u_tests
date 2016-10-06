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

import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;


public class InstrumentedDefaultHighlighter  extends DefaultHighlighter {
    
    public Object addHighlight(int arg0, int arg1, HighlightPainter arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.addHighlight", "" +  arg0, "" +  arg1,  arg2} );
        return super.addHighlight(arg0, arg1, arg2);
    }
    public void changeHighlight(Object arg0, int arg1, int arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.changeHighlight",  arg0, "" +  arg1, "" +  arg2} );
        super.changeHighlight(arg0, arg1, arg2);
    }
    public void deinstall(JTextComponent arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.deinstall",  arg0} );
        super.deinstall(arg0);
    }
    public boolean getDrawsLayeredHighlights() {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.getDrawsLayeredHighlights"} );
        return super.getDrawsLayeredHighlights();
    }
    public Highlight[] getHighlights() {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.getHighlights"} );
        return super.getHighlights();
    }
    public void install(JTextComponent arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.install",  arg0} );
        super.install(arg0);
    }
    public void paint(Graphics arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.paint",  arg0} );
        super.paint(arg0);
    }
    public void paintLayeredHighlights(Graphics arg0, int arg1, int arg2,
            Shape arg3, JTextComponent arg4, View arg5) {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.paintLayeredHighlights",  arg0,
                "" + arg1, "" + arg2, arg3, arg4, arg5} );
        super.paintLayeredHighlights(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    public void removeAllHighlights() {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.removeAllHighlights"} );
        super.removeAllHighlights();
    }
    public void removeHighlight(Object arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.removeHighlight",  arg0} );
        super.removeHighlight(arg0);
    }
    public void setDrawsLayeredHighlights(boolean arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.setDrawsLayeredHighlights", "" +  arg0} );
        super.setDrawsLayeredHighlights(arg0);
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.hashCode"} );
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.clone"} );
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.equals",  arg0} );
        return super.equals(arg0);
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"DefaultHighlighter.toString"} );
        return super.toString();
    }
}
