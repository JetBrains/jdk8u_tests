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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.Position.Bias;


public class InstrumentedTextUI extends TextUI {

    public void damageRange(JTextComponent arg0, int arg1, int arg2) {
    InstrumentedUILog.add(new Object[] {"TextUI.damageRange",  arg0, "" +  arg1, "" +  arg2} );
    }

    public int viewToModel(JTextComponent arg0, Point arg1) {
        InstrumentedUILog.add(new Object[] {"TextUI.viewToModel",  arg0,  arg1} );
        return 0;
    }

    public Rectangle modelToView(JTextComponent arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"TextUI.modelToView",  arg0, "" +  arg1} );
        return null;
    }

    public EditorKit getEditorKit(JTextComponent arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.getEditorKit",  arg0} );
        return null;
    }

    public View getRootView(JTextComponent arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.getRootView",  arg0} );
        return null;
    }

    public Rectangle modelToView(JTextComponent arg0, int arg1, Bias arg2) throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"TextUI.modelToView",  arg0, "" +  arg1,  arg2} );
        return null;
    }

    public int viewToModel(JTextComponent arg0, Point arg1, Bias[] arg2) {
        InstrumentedUILog.add(new Object[] {"TextUI.viewToModel",  arg0,  arg1,  arg2} );
        return 0;
    }

    public void damageRange(JTextComponent arg0, int arg1, int arg2, Bias arg3, Bias arg4) {
    InstrumentedUILog.add(new Object[] {"TextUI.damageRange",  arg0, "" +  arg1, "" +  arg2,  arg3,  arg4} );
    }

    public int getNextVisualPositionFrom(JTextComponent arg0, int arg1, Bias arg2, int arg3, Bias[] arg4) throws BadLocationException {
        InstrumentedUILog.add(new Object[] {"TextUI.getNextVisualPositionFrom",  arg0, "" +  arg1,  arg2, "" +  arg3,  arg4} );
        return 0;
    }
    
    

    public String getToolTipText(JTextComponent arg0, Point arg1) {
        InstrumentedUILog.add(new Object[] {"TextUI.getToolTipText",  arg0,  arg1} );
        return super.getToolTipText(arg0, arg1);
    }
    public int getAccessibleChildrenCount(JComponent arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.getAccessibleChildrenCount",  arg0} );
        return super.getAccessibleChildrenCount(arg0);
    }
    public void installUI(JComponent arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.installUI",  arg0} );
        super.installUI(arg0);
    }
    public void uninstallUI(JComponent arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.uninstallUI",  arg0} );
        super.uninstallUI(arg0);
    }
    public boolean contains(JComponent arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] {"TextUI.contains",  arg0, "" +  arg1, "" +  arg2} );
        return super.contains(arg0, arg1, arg2);
    }
    public Dimension getMaximumSize(JComponent arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.getMaximumSize",  arg0} );
        return super.getMaximumSize(arg0);
    }
    public Dimension getMinimumSize(JComponent arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.getMinimumSize",  arg0} );
        return super.getMinimumSize(arg0);
    }
    public Dimension getPreferredSize(JComponent arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.getPreferredSize",  arg0} );
        return super.getPreferredSize(arg0);
    }
    public Accessible getAccessibleChild(JComponent arg0, int arg1) {
        InstrumentedUILog.add(new Object[] {"TextUI.getAccessibleChild",  arg0, "" +  arg1} );
        return super.getAccessibleChild(arg0, arg1);
    }
    public void paint(Graphics arg0, JComponent arg1) {
        InstrumentedUILog.add(new Object[] {"TextUI.paint",  arg0,  arg1} );
        super.paint(arg0, arg1);
    }
    public void update(Graphics arg0, JComponent arg1) {
        InstrumentedUILog.add(new Object[] {"TextUI.update",  arg0,  arg1} );
        super.update(arg0, arg1);
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"TextUI.hashCode"} );
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] {"TextUI.clone"} );
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"TextUI.equals",  arg0} );
        return super.equals(arg0);
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"TextUI.toString"} );
        return super.toString();
    }
}
