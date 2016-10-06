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
 * Created on 13.04.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.share;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.event.ChangeListener;

public class InstrumentedButtonModel extends DefaultButtonModel {
    public void addActionListener(ActionListener arg0) {
        InstrumentedUILog.add(new Object[] { "ButtonModel.addActionListener", arg0});
        super.addActionListener(arg0);
    }
    public void addChangeListener(ChangeListener arg0) {
        InstrumentedUILog.add(new Object[] { "ButtonModel.addChangeListener", arg0});
        super.addChangeListener(arg0);
    }
    public void addItemListener(ItemListener arg0) {
        InstrumentedUILog.add(new Object[] { "ButtonModel.addItemListener", arg0});
        super.addItemListener(arg0);
    }
    protected void fireActionPerformed(ActionEvent arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.fireActionPerformed", arg0});
        super.fireActionPerformed(arg0);
    }
    protected void fireItemStateChanged(ItemEvent arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.fireItemStateChanged", arg0});
        super.fireItemStateChanged(arg0);
    }
    public void fireStateChanged() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.fireStateChanged"});
        super.fireStateChanged();
    }
    public String getActionCommand() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.getActionCommand"});
        return super.getActionCommand();
    }
    public ActionListener[] getActionListeners() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.getActionListeners"});
        return super.getActionListeners();
    }
    public ChangeListener[] getChangeListeners() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.getChangeListeners"});
        return super.getChangeListeners();
    }
    public ButtonGroup getGroup() {
        InstrumentedUILog.add("ButtonModel 10");
        return super.getGroup();
    }
    public ItemListener[] getItemListeners() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.getItemListeners"});
        return super.getItemListeners();
    }
    public EventListener[] getListeners(Class arg0) {
        InstrumentedUILog.add("ButtonModel 12");
        return super.getListeners(arg0);
    }
    public int getMnemonic() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.getMnemonic"});
        return super.getMnemonic();
    }
    public Object[] getSelectedObjects() {
        InstrumentedUILog.add("ButtonModel 14");
        return super.getSelectedObjects();
    }
    public boolean isArmed() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.isArmed"});
        return super.isArmed();
    }
    public boolean isEnabled() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.isEnabled"});
        return super.isEnabled();
    }
    public boolean isPressed() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.isPressed"});
        return super.isPressed();
    }
    public boolean isRollover() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.isRollover"});
        return super.isRollover();
    }
    public boolean isSelected() {
        InstrumentedUILog.add(new Object[] {"ButtonModel.isSelected"});
        return super.isSelected();
    }
    public void removeActionListener(ActionListener arg0) {
        InstrumentedUILog.add(new Object[] { "ButtonModel.removeActionListener", arg0});
        super.removeActionListener(arg0);
    }
    public void removeChangeListener(ChangeListener arg0) {
        InstrumentedUILog.add(new Object[] { "ButtonModel.removeChangeListener", arg0});
        super.removeChangeListener(arg0);
    }
    public void removeItemListener(ItemListener arg0) {
        InstrumentedUILog.add(new Object[] { "ButtonModel.removeItemListener", arg0});
        super.removeItemListener(arg0);
    }
    public void setActionCommand(String arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.setActionCommand", arg0});
        super.setActionCommand(arg0);
    }
    public void setArmed(boolean arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.setArmed", "" + arg0});
        super.setArmed(arg0);
    }
    public void setEnabled(boolean arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.setEnabled", "" + arg0});
        super.setEnabled(arg0);
    }
    public void setGroup(ButtonGroup arg0) {
        InstrumentedUILog.add("ButtonModel 26");
        super.setGroup(arg0);
    }
    public void setMnemonic(int arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.setMnemonic", "" + arg0});
        super.setMnemonic(arg0);
    }
    public void setPressed(boolean arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.setPressed", "" + arg0});
        super.setPressed(arg0);
    }
    public void setRollover(boolean arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.setRollover", "" + arg0});
        super.setRollover(arg0);
    }
    public void setSelected(boolean arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.setSelected", "" + arg0});
        super.setSelected(arg0);
    }
    public int hashCode() {
        InstrumentedUILog.add("ButtonModel 31");
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add("ButtonModel 33");
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"ButtonModel.equals", arg0} );
        return super.equals(arg0);
    }
    public String toString() {
        InstrumentedUILog.add("ButtonModel 35");
        return super.toString();
    }
}
