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
 * Created on 14.04.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.share;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 *  
 */
public class InstrumentedAbstractAction extends AbstractAction {
    public static final Number MNEMONIC_KEY_VALUE = new Integer('a');

    public static final String NAME = "AbstractActionName";

    public static final String SHORT_DESCRIPTION = "AbstractActionShortDescription";

    public static final Icon SMALL_ICON = new ColorIcon(Color.RED);

    public static final String ACTION_COMMAND_KEY = "AbstractActionCommandKey";

    {
        putValue("MnemonicKey", MNEMONIC_KEY_VALUE);
        putValue("Name", NAME);
        putValue("ShortDescription", SHORT_DESCRIPTION);
        putValue("SmallIcon", SMALL_ICON);
        putValue("ActionCommandKey", ACTION_COMMAND_KEY);
    }

    public void actionPerformed(ActionEvent arg0) {
        InstrumentedUILog.add("AbstractAction 1");
    }

    public synchronized void addPropertyChangeListener(
            PropertyChangeListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "AbstractAction.addPropertyChangeListener", arg0 });
        super.addPropertyChangeListener(arg0);
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add("AbstractAction 3");
        return super.clone();
    }

    protected void firePropertyChange(String arg0, Object arg1, Object arg2) {
        InstrumentedUILog.add("AbstractAction 4");
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public Object[] getKeys() {
        InstrumentedUILog.add("AbstractAction 5");
        return super.getKeys();
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        InstrumentedUILog.add("AbstractAction 6");
        return super.getPropertyChangeListeners();
    }

    public Object getValue(String arg0) {
        InstrumentedUILog
                .add(new Object[] { "AbstractAction", "getValue", arg0 });
        return super.getValue(arg0);
    }

    public boolean isEnabled() {
        InstrumentedUILog.add(new Object[] { "AbstractAction", "isEnabled" });
        return super.isEnabled();
    }

    public void putValue(String arg0, Object arg1) {
        InstrumentedUILog.add("AbstractAction 9");
        super.putValue(arg0, arg1);
    }

    public synchronized void removePropertyChangeListener(
            PropertyChangeListener arg0) {
        InstrumentedUILog.add("AbstractAction 10");
        super.removePropertyChangeListener(arg0);
    }

    public void setEnabled(boolean arg0) {
        InstrumentedUILog.add("AbstractAction 11");
        super.setEnabled(arg0);
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "AbstractAction", "hashCode" });
        return super.hashCode();
    }

    protected void finalize() throws Throwable {
        InstrumentedUILog.add("AbstractAction 13");
        super.finalize();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add("AbstractAction 14");
        return super.equals(arg0);
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "AbstractAction", "toString" });
        return super.toString();
    }
}