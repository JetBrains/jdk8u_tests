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
 * Created on 20.04.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.share;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.plaf.ButtonUI;

/**
 *
 */
public class InstrumentedButtonUI extends ButtonUI {

    public boolean contains(JComponent arg0, int arg1, int arg2) {
        InstrumentedUILog.add("ButtonUI 1");
        return super.contains(arg0, arg1, arg2);
    }
    public Accessible getAccessibleChild(JComponent arg0, int arg1) {
        InstrumentedUILog.add("ButtonUI 2");
        return super.getAccessibleChild(arg0, arg1);
    }
    public int getAccessibleChildrenCount(JComponent arg0) {
        InstrumentedUILog.add("ButtonUI 3");
        return super.getAccessibleChildrenCount(arg0);
    }
    public Dimension getMaximumSize(JComponent arg0) {
        InstrumentedUILog.add("ButtonUI 4");
        return super.getMaximumSize(arg0);
    }
    public Dimension getMinimumSize(JComponent arg0) {
        InstrumentedUILog.add("ButtonUI 5");
        return super.getMinimumSize(arg0);
    }
    public Dimension getPreferredSize(JComponent arg0) {
        InstrumentedUILog.add("ButtonUI 6");
        return super.getPreferredSize(arg0);
    }
    public void installUI(JComponent arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonUI.installUI", arg0});
        super.installUI(arg0);
    }
    public void paint(Graphics arg0, JComponent arg1) {
        InstrumentedUILog.add("ButtonUI 8");
        super.paint(arg0, arg1);
    }
    public void uninstallUI(JComponent arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonUI.uninstallUI", arg0});
        super.uninstallUI(arg0);
    }
    public void update(Graphics arg0, JComponent arg1) {
        InstrumentedUILog.add("ButtonUI 10");
        super.update(arg0, arg1);
    }
    public int hashCode() {
        InstrumentedUILog.add("ButtonUI 11");
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add("ButtonUI 13");
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonUI.equals", arg0});
        return super.equals(arg0);
    }
    public String toString() {
        InstrumentedUILog.add("ButtonUI 15");
        return super.toString();
    }
}
