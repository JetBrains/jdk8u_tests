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
 * Created on 06.04.2005
 *  
 */
package org.apache.harmony.test.func.api.javax.swing.AbstractButton;

import java.awt.Color;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;

import org.apache.harmony.test.func.api.javax.swing.share.ButtonInternalsMadePublic;
import org.apache.harmony.test.func.api.javax.swing.share.ColorIcon;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAbstractAction;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAbstractButton;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedButtonModel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedButtonUI;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.Util;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class AbstractButtonTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new AbstractButtonTest().test(args));
    }

    protected AbstractButton getInstrumentedAbstractButton() {
        InstrumentedAbstractButton iab = new InstrumentedAbstractButton();
        iab.getUI().installUI(iab);

        return new InstrumentedAbstractButton();
    }

    protected final ButtonInternalsMadePublic getButtonWithPublicInternals() {
        return (ButtonInternalsMadePublic) getInstrumentedAbstractButton();
    }

    public Result testCheckHorizontalKey() {
        AbstractButton iab = getInstrumentedAbstractButton();

        ButtonInternalsMadePublic bimp = (ButtonInternalsMadePublic) iab;

        bimp.checkHorizontalKey(SwingConstants.RIGHT, "");
        bimp.checkHorizontalKey(SwingConstants.LEFT, "");
        bimp.checkHorizontalKey(SwingConstants.CENTER, "");
        bimp.checkHorizontalKey(SwingConstants.LEADING, "");
        bimp.checkHorizontalKey(SwingConstants.TRAILING, "");
        bimp.checkHorizontalKey(SwingConstants.TRAILING, null);

        try {
            bimp.checkHorizontalKey(-123, null);
            return failed("expected IllegalArgumentException on iab.checkHorizontalKey(-123, null)");
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null) {
                return failed("expected IllegalArgumentException with message == null on iab.checkHorizontalKey(-123, null)");
            }
        }

        try {
            bimp.checkHorizontalKey(123, "abc");
            return failed("expected IllegalArgumentException on iab.checkHorizontalKey(123, abc)");
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != "abc") {
                return failed("expected IllegalArgumentException with message == 'abc' on iab.checkHorizontalKey(123, 'abc')");
            }
        }

        return passed();
    }

    public Result testCheckVerticalKey() {
        ButtonInternalsMadePublic bimp = getButtonWithPublicInternals();

        bimp.checkVerticalKey(SwingConstants.TOP, "");
        bimp.checkVerticalKey(SwingConstants.CENTER, "");
        bimp.checkVerticalKey(SwingConstants.BOTTOM, "");
        bimp.checkVerticalKey(SwingConstants.BOTTOM, null);

        try {
            bimp.checkVerticalKey(-123, null);
            return failed("expected IllegalArgumentException on iab.checkVerticalKey(-123, null)");
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null) {
                return failed("expected IllegalArgumentException with message == null on iab.checkVerticalKey(-123, null)");
            }
        }

        try {
            bimp.checkVerticalKey(123, "abc");
            return failed("expected IllegalArgumentException on iab.checkVerticalKey(123, abc)");
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != "abc") {
                return failed("expected IllegalArgumentException with message == 'abc' on iab.checkVerticalKey(123, 'abc')");
            }
        }

        try {
            bimp.checkVerticalKey(SwingConstants.LEFT, null);
            return failed("expected IllegalArgumentException on iab.checkVerticalKey(SwingConstants.LEFT, abc)");
        } catch (IllegalArgumentException e) {
        }

        try {
            bimp.checkVerticalKey(SwingConstants.RIGHT, null);
            return failed("expected IllegalArgumentException on iab.checkVerticalKey(SwingConstants.RIGHT, abc)");
        } catch (IllegalArgumentException e) {
        }

        return passed();
    }

    public Result testConfigurePropertiesFromAction() {
        ButtonInternalsMadePublic bimp = getButtonWithPublicInternals();
        InstrumentedUILog.clear();

        bimp.configurePropertiesFromAction(null);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "AbstractButton.configurePropertiesFromAction", null },
                { "AbstractButton.setMnemonic", "0" },
                { "AbstractButton.getMnemonic" },
                { "ButtonModel.setMnemonic", "0" },
                { "ButtonModel.fireStateChanged" },
                { "ButtonModel.getMnemonic" },
                { "AbstractButton.setText", null },
                { "JComponent.firePropertyChange", "text", "", null },
                { "AbstractButton.getMnemonic" },
                { "AbstractButton.setDisplayedMnemonicIndex", "-1" },
                { "JComponent.firePropertyChangeInt", "displayedMnemonicIndex",
                        "-1", "-1" },
                { "JComponent.revalidate" },
                { "awt.Component.getParent" },
                { "awt.Component.repaint" },
                { "JComponent.repaint", "0 0 0 0 0" },
                { "JComponent.setToolTipText", null },
                { "JComponent.getToolTipText" },
                { "awt.Component.removeMouseListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                { "awt.Component.removeMouseMotionListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                { "AbstractButton.setIcon", null },
                { "JComponent.firePropertyChange", "icon", null, null },
                { "AbstractButton.setActionCommand", null },
                { "AbstractButton.getModel" },
                { "ButtonModel.setActionCommand", null },
                { "JComponent.setEnabled", "true" },
                { "JComponent.isEnabled" },
                { "awt.Component.enable", "true" },
                { "JComponent.enable" },
                { "JComponent.isEnabled" },
                { "JComponent.firePropertyChangeBoolean", "enabled", "true",
                        "true" }, { "ButtonModel.setEnabled", "true" },
                { "ButtonModel.isEnabled" } })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{
                                "AbstractButton.configurePropertiesFromAction",
                                null },
                        /* 2 */{ "AbstractButton.setMnemonic", "0" },
                        /* 3 */{ "AbstractButton.getMnemonic" },
                        /* 4 */{ "ButtonModel.setMnemonic", "0" },
                        /* 5 */{ "ButtonModel.fireStateChanged" },
                        /* 6 */{ "ButtonModel.getMnemonic" },
                        /* 7 */{ "AbstractButton.fireStateChanged" },
                        /* 8 */{ "awt.Component.repaint" },
                        /* 9 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 10 */{ "ButtonModel.getMnemonic" },
                        /* 11 */{ "AbstractButton.setText", null },
                        /* 12 */{ "JComponent.firePropertyChange", "text", "",
                                null },
                        /* 13 */{ "AbstractButton.getMnemonic" },
                        /* 14 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 15 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 16 */{ "JComponent.revalidate" },
                        /* 17 */{ "awt.Component.getParent" },
                        /* 18 */{ "awt.Component.repaint" },
                        /* 19 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 20 */{ "JComponent.setToolTipText", null },
                        /* 21 */{ "JComponent.getToolTipText" },
                        /* 22 */{ "awt.Component.removeMouseListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 23 */{ "awt.Component.removeMouseMotionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 24 */{ "AbstractButton.setIcon", null },
                        /* 25 */{ "JComponent.firePropertyChange", "icon",
                                null, null },
                        /* 26 */{ "AbstractButton.setActionCommand", null },
                        /* 27 */{ "AbstractButton.getModel" },
                        /* 28 */{ "ButtonModel.setActionCommand", null },
                        /* 29 */{ "JComponent.setEnabled", "true" },
                        /* 30 */{ "JComponent.isEnabled" },
                        /* 31 */{ "awt.Component.enable", "true" },
                        /* 32 */{ "JComponent.enable" },
                        /* 33 */{ "JComponent.isEnabled" },
                        /* 34 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "true" },
                        /* 35 */{ "ButtonModel.setEnabled", "true" },
                        /* 36 */{ "ButtonModel.isEnabled" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected configurePropertiesFromAction(null) to call another sequence of events");
        }

        Action action = new InstrumentedAbstractAction();
        InstrumentedUILog.clear();
        bimp.configurePropertiesFromAction(action);

        Util.waitQueueEventsProcess();
        //InstrumentedUILog.printLogAsArray();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "AbstractButton.configurePropertiesFromAction", action },
                { "AbstractAction", "getValue", "MnemonicKey" },
                {
                        "AbstractButton.setMnemonic",
                        InstrumentedAbstractAction.MNEMONIC_KEY_VALUE
                                .toString() },
                { "AbstractButton.getMnemonic" },
                {
                        "ButtonModel.setMnemonic",
                        InstrumentedAbstractAction.MNEMONIC_KEY_VALUE
                                .toString() },
                { "ButtonModel.fireStateChanged" },
                { "ButtonModel.getMnemonic" },
                {
                        "JComponent.firePropertyChangeInt",
                        "mnemonic",
                        "0",
                        InstrumentedAbstractAction.MNEMONIC_KEY_VALUE
                                .toString() },
                { "AbstractButton.getText" },
                { "AbstractButton.setDisplayedMnemonicIndex", "-1" },
                { "JComponent.firePropertyChangeInt", "displayedMnemonicIndex",
                        "-1", "-1" },
                { "JComponent.revalidate" },
                { "awt.Component.getParent" },
                { "awt.Component.repaint" },
                { "JComponent.repaint", "0 0 0 0 0" },
                { "AbstractAction", "getValue", "Name" },
                { "AbstractButton.setText", InstrumentedAbstractAction.NAME },
                { "JComponent.firePropertyChange", "text", null,
                        InstrumentedAbstractAction.NAME },
                { "AbstractButton.getMnemonic" },
                { "AbstractButton.setDisplayedMnemonicIndex", "0" },
                { "AbstractButton.getText" },
                { "JComponent.firePropertyChangeInt", "displayedMnemonicIndex",
                        "-1", "0" },
                { "JComponent.revalidate" },
                { "awt.Component.getParent" },
                { "awt.Component.repaint" },
                { "JComponent.repaint", "0 0 0 0 0" },
                { "JComponent.revalidate" },
                { "awt.Component.getParent" },
                { "awt.Component.repaint" },
                { "JComponent.repaint", "0 0 0 0 0" },
                { "AbstractAction", "getValue", "ShortDescription" },
                { "JComponent.setToolTipText",
                        InstrumentedAbstractAction.SHORT_DESCRIPTION },
                { "JComponent.getToolTipText" },
                { "JComponent.firePropertyChange", "ToolTipText", null,
                        InstrumentedAbstractAction.SHORT_DESCRIPTION },
                { "awt.Component.removeMouseListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                { "awt.Component.addMouseListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                { "awt.Component.removeMouseMotionListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                { "awt.Component.addMouseMotionListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                { "AbstractAction", "getValue", "SmallIcon" },
                { "AbstractButton.setIcon",
                        InstrumentedAbstractAction.SMALL_ICON },
                { "JComponent.firePropertyChange", "icon", null,
                        InstrumentedAbstractAction.SMALL_ICON },
                { "JComponent.revalidate" },
                { "awt.Component.getParent" },
                { "awt.Component.repaint" },
                { "JComponent.repaint", "0 0 0 0 0" },
                { "AbstractAction", "getValue", "ActionCommandKey" },
                { "AbstractButton.setActionCommand",
                        InstrumentedAbstractAction.ACTION_COMMAND_KEY },
                { "AbstractButton.getModel" },
                { "ButtonModel.setActionCommand",
                        InstrumentedAbstractAction.ACTION_COMMAND_KEY },
                { "AbstractAction", "isEnabled" },
                { "JComponent.setEnabled", "true" },
                { "JComponent.isEnabled" },
                { "awt.Component.enable", "true" },
                { "JComponent.enable" },
                { "JComponent.isEnabled" },
                { "JComponent.firePropertyChangeBoolean", "enabled", "true",
                        "true" }, { "ButtonModel.setEnabled", "true" },
                { "ButtonModel.isEnabled" }, })
                && !InstrumentedUILog
                        .equals(new Object[][] {
                                /* 1 */{
                                        "AbstractButton.configurePropertiesFromAction",
                                        action },
                                /* 2 */{ "AbstractAction", "getValue",
                                        "MnemonicKey" },
                                /* 3 */{
                                        "AbstractButton.setMnemonic",
                                        ""
                                                + InstrumentedAbstractAction.MNEMONIC_KEY_VALUE },
                                /* 4 */{ "AbstractButton.getMnemonic" },
                                /* 5 */{
                                        "ButtonModel.setMnemonic",
                                        ""
                                                + InstrumentedAbstractAction.MNEMONIC_KEY_VALUE },
                                /* 6 */{ "ButtonModel.fireStateChanged" },
                                /* 7 */{ "ButtonModel.getMnemonic" },
                                /* 8 */{
                                        "JComponent.firePropertyChangeInt",
                                        "mnemonic",
                                        "0",
                                        ""
                                                + InstrumentedAbstractAction.MNEMONIC_KEY_VALUE },
                                /* 9 */{ "AbstractButton.getText" },
                                /* 10 */{
                                        "AbstractButton.setDisplayedMnemonicIndex",
                                        "-1" },
                                /* 11 */{ "JComponent.firePropertyChangeInt",
                                        "displayedMnemonicIndex", "-1", "-1" },
                                /* 12 */{ "JComponent.revalidate" },
                                /* 13 */{ "awt.Component.getParent" },
                                /* 14 */{ "awt.Component.repaint" },
                                /* 15 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 16 */{ "AbstractButton.fireStateChanged" },
                                /* 17 */{ "awt.Component.repaint" },
                                /* 18 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 19 */{ "ButtonModel.getMnemonic" },
                                /* 20 */{ "AbstractAction", "getValue", "Name" },
                                /* 21 */{ "AbstractButton.setText",
                                        "AbstractActionName" },
                                /* 22 */{ "JComponent.firePropertyChange",
                                        "text", null, "AbstractActionName" },

                                /* 23 */{ "AbstractButton.getMnemonic" },
                                /* 24 */{
                                        "AbstractButton.setDisplayedMnemonicIndex",
                                        "0" },
                                /* 25 */{ "AbstractButton.getText" },
                                /* 26 */{ "JComponent.firePropertyChangeInt",
                                        "displayedMnemonicIndex", "-1", "0" },
                                /* 27 */{ "JComponent.revalidate" },
                                /* 28 */{ "awt.Component.getParent" },
                                /* 29 */{ "awt.Component.repaint" },
                                /* 30 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 31 */{ "JComponent.revalidate" },
                                /* 32 */{ "awt.Component.getParent" },
                                /* 33 */{ "awt.Component.repaint" },
                                /* 34 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 35 */{ "AbstractAction", "getValue",
                                        "ShortDescription" },
                                /* 36 */{
                                        "JComponent.setToolTipText",
                                        InstrumentedAbstractAction.SHORT_DESCRIPTION },
                                /* 37 */{ "JComponent.getToolTipText" },
                                /* 38 */{
                                        "JComponent.firePropertyChange",
                                        "ToolTipText",
                                        null,
                                        InstrumentedAbstractAction.SHORT_DESCRIPTION },
                                /* 39 */{ "awt.Component.removeMouseListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 40 */{ "awt.Component.addMouseListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 41 */{
                                        "awt.Component.removeMouseMotionListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 42 */{
                                        "awt.Component.addMouseMotionListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 43 */{ "AbstractAction", "getValue",
                                        "SmallIcon" },
                                /* 44 */{ "AbstractButton.setIcon",
                                        InstrumentedAbstractAction.SMALL_ICON },
                                /* 45 */{ "JComponent.firePropertyChange",
                                        "icon", null,
                                        InstrumentedAbstractAction.SMALL_ICON },
                                /* 46 */{ "JComponent.revalidate" },
                                /* 47 */{ "awt.Component.getParent" },
                                /* 48 */{ "awt.Component.repaint" },
                                /* 49 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 50 */{ "AbstractAction", "getValue",
                                        "ActionCommandKey" },
                                /* 51 */{
                                        "AbstractButton.setActionCommand",
                                        InstrumentedAbstractAction.ACTION_COMMAND_KEY },
                                /* 52 */{ "AbstractButton.getModel" },
                                /* 53 */{
                                        "ButtonModel.setActionCommand",
                                        InstrumentedAbstractAction.ACTION_COMMAND_KEY },
                                /* 54 */{ "AbstractAction", "isEnabled" },
                                /* 55 */{ "JComponent.setEnabled", "true" },
                                /* 56 */{ "JComponent.isEnabled" },
                                /* 57 */{ "awt.Component.enable", "true" },
                                /* 58 */{ "JComponent.enable" },
                                /* 59 */{ "JComponent.isEnabled" },
                                /* 60 */{
                                        "JComponent.firePropertyChangeBoolean",
                                        "enabled", "true", "true" },
                                /* 61 */{ "ButtonModel.setEnabled", "true" },
                                /* 62 */{ "ButtonModel.isEnabled" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected configurePropertiesFromAction(Action != null) to call another sequence of events");
        }

        return passed();
    }

    public Result testCreateActionPropertyChangeListener() {
        ButtonInternalsMadePublic bimp = getButtonWithPublicInternals();
        InstrumentedUILog.clear();

        PropertyChangeListener pcl = bimp
                .createActionPropertyChangeListener(null);

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] { {
                "AbstractButton.createActionPropertyChangeListener", null } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected createActionPropertyChangeListener(null) "
                    + "to call another sequence of events");
        }

        if (pcl == null) {
            return failed("expected result of createActionPropertyChangeListener to be non-null");
        }

        Action action = new InstrumentedAbstractAction();
        InstrumentedUILog.clear();
        pcl = bimp.createActionPropertyChangeListener(action);
        if (!InstrumentedUILog
                .equals(new Object[][] { {
                        "AbstractButton.createActionPropertyChangeListener",
                        action } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected createActionPropertyChangeListener(Action != null) to call "
                    + "another sequence of events");
        }
        if (pcl == null) {
            return failed("expected result of createActionPropertyChangeListener to be non-null");
        }

        return passed();
    }

    public Result testCreateChangeListener() {
        ButtonInternalsMadePublic bimp = getButtonWithPublicInternals();
        InstrumentedUILog.clear();

        ChangeListener cl = bimp.createChangeListener();

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.createChangeListener" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected createChangeListener() "
                    + "to call another sequence of events");
        }

        if (cl == null) {
            return failed("expected result of createChangeListener to be non-null");
        }

        return passed();
    }

    public Result testActionCommand() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        String s = iab.getActionCommand();

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                { "AbstractButton.getActionCommand" },
                { "AbstractButton.getModel" },
                { "ButtonModel.getActionCommand" },
                { "AbstractButton.getText" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "AbstractButton.getActionCommand" },
                        { "ButtonModel.getActionCommand" },
                        { "AbstractButton.getText" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getActionCommand() "
                    + "to cause another sequence of events");
        }

        if (!"".equals(s)) {
            return failed("expected getActionCommand() to return '', got '" + s
                    + "'");
        }

        InstrumentedUILog.clear();

        iab.setActionCommand(null);

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                { "AbstractButton.setActionCommand", null },
                { "AbstractButton.getModel" },
                { "ButtonModel.setActionCommand", null } })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "AbstractButton.setActionCommand", null },
                        { "ButtonModel.setActionCommand", null } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setActionCommand(null) "
                    + "to cause another sequence of events");
        }

        s = iab.getActionCommand();

        if (!"".equals(s)) {
            return failed("expected getActionCommand() to return '', got '" + s
                    + "'");
        }
        InstrumentedUILog.clear();

        iab.setActionCommand("qwe");

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                { "AbstractButton.setActionCommand", "qwe" },
                { "AbstractButton.getModel" },
                { "ButtonModel.setActionCommand", "qwe" } })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "AbstractButton.setActionCommand", "qwe" },
                        { "ButtonModel.setActionCommand", "qwe" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setActionCommand('qwe') "
                    + "to cause another sequence of events");
        }

        s = iab.getActionCommand();

        if (!"qwe".equals(s)) {
            return failed("expected getActionCommand() to return 'qwe', got '"
                    + s + "'");
        }

        return passed();
    }

    public Result testAction() {
        AbstractButton iab = getInstrumentedAbstractButton();

        if (iab.getAction() != null) {
            return failed("expected default action to be null");
        }

        InstrumentedUILog.clear();

        iab.setAction(null);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setAction", null },
                        /* 2 */{ "AbstractButton.getAction" },
                        /* 3 */{
                                "AbstractButton.configurePropertiesFromAction",
                                null },
                        /* 4 */{ "AbstractButton.setMnemonic", "0" },
                        /* 5 */{ "AbstractButton.getMnemonic" },
                        /* 6 */{ "ButtonModel.setMnemonic", "0" },
                        /* 7 */{ "ButtonModel.fireStateChanged" },
                        /* 8 */{ "ButtonModel.getMnemonic" },
                        /* 9 */{ "AbstractButton.setText", null },
                        /* 10 */{ "JComponent.firePropertyChange", "text", "",
                                null },
                        /* 11 */{ "AbstractButton.getMnemonic" },
                        /* 12 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 13 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 14 */{ "JComponent.revalidate" },
                        /* 15 */{ "awt.Component.getParent" },
                        /* 16 */{ "awt.Component.repaint" },
                        /* 17 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 18 */{ "JComponent.setToolTipText", null },
                        /* 19 */{ "JComponent.getToolTipText" },
                        /* 20 */{ "awt.Component.removeMouseListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 21 */{ "awt.Component.removeMouseMotionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 22 */{ "AbstractButton.setIcon", null },
                        /* 23 */{ "JComponent.firePropertyChange", "icon",
                                null, null },
                        /* 24 */{ "AbstractButton.setActionCommand", null },
                        /* 25 */{ "AbstractButton.getModel" },
                        /* 26 */{ "ButtonModel.setActionCommand", null },
                        /* 27 */{ "JComponent.setEnabled", "true" },
                        /* 28 */{ "JComponent.isEnabled" },
                        /* 29 */{ "awt.Component.enable", "true" },
                        /* 30 */{ "JComponent.enable" },
                        /* 31 */{ "JComponent.isEnabled" },
                        /* 32 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "true" },
                        /* 33 */{ "ButtonModel.setEnabled", "true" },
                        /* 34 */{ "ButtonModel.isEnabled" },
                        /* 35 */{ "JComponent.firePropertyChange", "action",
                                null, null },
                        /* 36 */{ "JComponent.revalidate" },
                        /* 37 */{ "awt.Component.getParent" },
                        /* 38 */{ "awt.Component.repaint" },
                        /* 39 */{ "JComponent.repaint", "0 0 0 0 0" }, })

                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setAction", null },
                        /* 2 */{ "AbstractButton.getAction" },
                        /* 3 */{
                                "AbstractButton.configurePropertiesFromAction",
                                null },
                        /* 4 */{ "AbstractButton.setMnemonic", "0" },
                        /* 5 */{ "AbstractButton.getMnemonic" },
                        /* 6 */{ "ButtonModel.setMnemonic", "0" },
                        /* 7 */{ "ButtonModel.fireStateChanged" },
                        /* 8 */{ "ButtonModel.getMnemonic" },
                        /* 9 */{ "AbstractButton.fireStateChanged" },
                        /* 10 */{ "awt.Component.repaint" },
                        /* 11 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 12 */{ "ButtonModel.getMnemonic" },
                        /* 13 */{ "AbstractButton.setText", null },
                        /* 14 */{ "JComponent.firePropertyChange", "text", "",
                                null },
                        /* 15 */{ "AbstractButton.getMnemonic" },
                        /* 16 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 17 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 18 */{ "JComponent.revalidate" },
                        /* 19 */{ "awt.Component.getParent" },
                        /* 20 */{ "awt.Component.repaint" },
                        /* 21 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 22 */{ "JComponent.setToolTipText", null },
                        /* 23 */{ "JComponent.getToolTipText" },
                        /* 24 */{ "awt.Component.removeMouseListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 25 */{ "awt.Component.removeMouseMotionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 26 */{ "AbstractButton.setIcon", null },
                        /* 27 */{ "JComponent.firePropertyChange", "icon",
                                null, null },
                        /* 28 */{ "AbstractButton.setActionCommand", null },
                        /* 29 */{ "AbstractButton.getModel" },
                        /* 30 */{ "ButtonModel.setActionCommand", null },
                        /* 31 */{ "JComponent.setEnabled", "true" },
                        /* 32 */{ "JComponent.isEnabled" },
                        /* 33 */{ "awt.Component.enable", "true" },
                        /* 34 */{ "JComponent.enable" },
                        /* 35 */{ "JComponent.isEnabled" },
                        /* 36 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "true" },
                        /* 37 */{ "ButtonModel.setEnabled", "true" },
                        /* 38 */{ "ButtonModel.isEnabled" },
                        /* 39 */{ "JComponent.firePropertyChange", "action",
                                null, null },
                        /* 40 */{ "JComponent.revalidate" },
                        /* 41 */{ "awt.Component.getParent" },
                        /* 42 */{ "awt.Component.repaint" },
                        /* 43 */{ "JComponent.repaint", "0 0 0 0 0" }, })

                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setAction", null },
                        /* 2 */{ "AbstractButton.getAction" },
                        /* 3 */{
                                "AbstractButton.configurePropertiesFromAction",
                                null },
                        /* 4 */{ "AbstractButton.setMnemonic", "0" },
                        /* 5 */{ "AbstractButton.getMnemonic" },
                        /* 6 */{ "ButtonModel.setMnemonic", "0" },
                        /* 7 */{ "ButtonModel.fireStateChanged" },
                        /* 8 */{ "ButtonModel.getMnemonic" },
                        /* 9 */{ "AbstractButton.fireStateChanged" },
                        /* 10 */{ "awt.Component.repaint" },
                        /* 11 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 12 */{ "awt.Component.repaint" },
                        /* 13 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 14 */{ "ButtonModel.getMnemonic" },
                        /* 15 */{ "JComponent.setToolTipText", null },
                        /* 16 */{ "JComponent.getToolTipText" },
                        /* 17 */{ "awt.Component.removeMouseListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 18 */{ "awt.Component.removeMouseMotionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 19 */{ "AbstractButton.setIcon", null },
                        /* 20 */{ "JComponent.firePropertyChange", "icon",
                                null, null },
                        /* 21 */{ "AbstractButton.setActionCommand", null },
                        /* 22 */{ "AbstractButton.getModel" },
                        /* 23 */{ "ButtonModel.setActionCommand", null },
                        /* 24 */{ "JComponent.setEnabled", "true" },
                        /* 25 */{ "JComponent.isEnabled" },
                        /* 26 */{ "awt.Component.enable", "true" },
                        /* 27 */{ "JComponent.enable" },
                        /* 28 */{ "JComponent.isEnabled" },
                        /* 29 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "true" },
                        /* 30 */{ "ButtonModel.setEnabled", "true" },
                        /* 31 */{ "ButtonModel.isEnabled" },
                        /* 32 */{ "AbstractButton.setText", null },
                        /* 33 */{ "JComponent.firePropertyChange", "text", "",
                                null },
                        /* 34 */{ "AbstractButton.getText" },
                        /* 35 */{ "AbstractButton.getMnemonic" },
                        /* 36 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 37 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 38 */{ "JComponent.revalidate" },
                        /* 39 */{ "awt.Component.getParent" },
                        /* 40 */{ "awt.Component.repaint" },
                        /* 41 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 42 */{ "JComponent.firePropertyChange", "action",
                                null, null },
                        /* 43 */{ "JComponent.revalidate" },
                        /* 44 */{ "awt.Component.getParent" },
                        /* 45 */{ "awt.Component.repaint" },
                        /* 46 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setAction(null) to call another sequence of events");
        }

        Action action = new InstrumentedAbstractAction();
        InstrumentedUILog.clear();
        iab.setAction(action);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog
                .equals(new Object[][] {
                        { "AbstractButton.setAction", action },
                        { "AbstractButton.getAction" },
                        //started call
                        // AbstractButton.configurePropertiesFromAction
                        { "AbstractButton.configurePropertiesFromAction",
                                action },
                        { "AbstractAction", "getValue", "MnemonicKey" },
                        {
                                "AbstractButton.setMnemonic",
                                InstrumentedAbstractAction.MNEMONIC_KEY_VALUE
                                        .toString() },
                        { "AbstractButton.getMnemonic" },
                        {
                                "ButtonModel.setMnemonic",
                                InstrumentedAbstractAction.MNEMONIC_KEY_VALUE
                                        .toString() },
                        { "ButtonModel.fireStateChanged" },
                        { "ButtonModel.getMnemonic" },
                        {
                                "JComponent.firePropertyChangeInt",
                                "mnemonic",
                                "0",
                                InstrumentedAbstractAction.MNEMONIC_KEY_VALUE
                                        .toString() },
                        { "AbstractButton.getText" },
                        { "AbstractButton.setDisplayedMnemonicIndex", "-1" },
                        { "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        { "JComponent.revalidate" },
                        { "awt.Component.getParent" },
                        { "awt.Component.repaint" },
                        { "JComponent.repaint", "0 0 0 0 0" },
                        { "AbstractAction", "getValue", "Name" },
                        { "AbstractButton.setText",
                                InstrumentedAbstractAction.NAME },
                        { "JComponent.firePropertyChange", "text", null,
                                InstrumentedAbstractAction.NAME },
                        { "AbstractButton.getMnemonic" },
                        { "AbstractButton.setDisplayedMnemonicIndex", "0" },
                        { "AbstractButton.getText" },
                        { "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "0" },
                        { "JComponent.revalidate" },
                        { "awt.Component.getParent" },
                        { "awt.Component.repaint" },
                        { "JComponent.repaint", "0 0 0 0 0" },
                        { "JComponent.revalidate" },
                        { "awt.Component.getParent" },
                        { "awt.Component.repaint" },
                        { "JComponent.repaint", "0 0 0 0 0" },
                        { "AbstractAction", "getValue", "ShortDescription" },
                        { "JComponent.setToolTipText",
                                InstrumentedAbstractAction.SHORT_DESCRIPTION },
                        { "JComponent.getToolTipText" },
                        { "JComponent.firePropertyChange", "ToolTipText", null,
                                InstrumentedAbstractAction.SHORT_DESCRIPTION },
                        { "awt.Component.removeMouseListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "awt.Component.addMouseListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "awt.Component.removeMouseMotionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "awt.Component.addMouseMotionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "AbstractAction", "getValue", "SmallIcon" },
                        { "AbstractButton.setIcon",
                                InstrumentedAbstractAction.SMALL_ICON },
                        { "JComponent.firePropertyChange", "icon", null,
                                InstrumentedAbstractAction.SMALL_ICON },
                        { "JComponent.revalidate" },
                        { "awt.Component.getParent" },
                        { "awt.Component.repaint" },
                        { "JComponent.repaint", "0 0 0 0 0" },
                        { "AbstractAction", "getValue", "ActionCommandKey" },
                        { "AbstractButton.setActionCommand",
                                InstrumentedAbstractAction.ACTION_COMMAND_KEY },
                        { "AbstractButton.getModel" },
                        { "ButtonModel.setActionCommand",
                                InstrumentedAbstractAction.ACTION_COMMAND_KEY },
                        { "AbstractAction", "isEnabled" },
                        { "JComponent.setEnabled", "true" },
                        { "JComponent.isEnabled" },
                        { "awt.Component.enable", "true" },
                        { "JComponent.enable" },
                        { "JComponent.isEnabled" },
                        { "JComponent.firePropertyChangeBoolean", "enabled",
                                "true", "true" },
                        { "ButtonModel.setEnabled", "true" },
                        { "ButtonModel.isEnabled" },
                        //end of call
                        // AbstractButton.configurePropertiesFromAction
                        { "AbstractButton.addActionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "AbstractButton.createActionPropertyChangeListener",
                                action },
                        { "AbstractAction.addPropertyChangeListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "JComponent.firePropertyChange", "action", null,
                                action }, { "JComponent.revalidate" },
                        { "awt.Component.getParent" },
                        { "awt.Component.repaint" },
                        { "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog
                        .equals(new Object[][] {
                                /* 1 */{ "AbstractButton.setAction", action },
                                /* 2 */{ "AbstractButton.getAction" },
                                /* 3 */{
                                        "AbstractButton.configurePropertiesFromAction",
                                        action },
                                /* 4 */{ "AbstractAction", "getValue",
                                        "MnemonicKey" },
                                /* 5 */{
                                        "AbstractButton.setMnemonic",
                                        ""
                                                + InstrumentedAbstractAction.MNEMONIC_KEY_VALUE },
                                /* 6 */{ "AbstractButton.getMnemonic" },
                                /* 7 */{
                                        "ButtonModel.setMnemonic",
                                        ""
                                                + InstrumentedAbstractAction.MNEMONIC_KEY_VALUE },
                                /* 8 */{ "ButtonModel.fireStateChanged" },
                                /* 9 */{ "ButtonModel.getMnemonic" },
                                /* 10 */{
                                        "JComponent.firePropertyChangeInt",
                                        "mnemonic",
                                        "0",
                                        ""
                                                + InstrumentedAbstractAction.MNEMONIC_KEY_VALUE },
                                /* 11 */{ "AbstractButton.getText" },
                                /* 12 */{
                                        "AbstractButton.setDisplayedMnemonicIndex",
                                        "-1" },
                                /* 13 */{ "JComponent.firePropertyChangeInt",
                                        "displayedMnemonicIndex", "-1", "-1" },
                                /* 14 */{ "JComponent.revalidate" },
                                /* 15 */{ "awt.Component.getParent" },
                                /* 16 */{ "awt.Component.repaint" },
                                /* 17 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 18 */{ "AbstractButton.fireStateChanged" },
                                /* 19 */{ "awt.Component.repaint" },
                                /* 20 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 21 */{ "ButtonModel.getMnemonic" },
                                /* 22 */{ "AbstractAction", "getValue", "Name" },
                                /* 23 */{ "AbstractButton.setText",
                                        InstrumentedAbstractAction.NAME },
                                /* 24 */{ "JComponent.firePropertyChange",
                                        "text", null,
                                        InstrumentedAbstractAction.NAME },
                                /* 25 */{ "AbstractButton.getMnemonic" },
                                /* 26 */{
                                        "AbstractButton.setDisplayedMnemonicIndex",
                                        "0" },
                                /* 27 */{ "AbstractButton.getText" },
                                /* 28 */{ "JComponent.firePropertyChangeInt",
                                        "displayedMnemonicIndex", "-1", "0" },
                                /* 29 */{ "JComponent.revalidate" },
                                /* 30 */{ "awt.Component.getParent" },
                                /* 31 */{ "awt.Component.repaint" },
                                /* 32 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 33 */{ "JComponent.revalidate" },
                                /* 34 */{ "awt.Component.getParent" },
                                /* 35 */{ "awt.Component.repaint" },
                                /* 36 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 37 */{ "AbstractAction", "getValue",
                                        "ShortDescription" },
                                /* 38 */{
                                        "JComponent.setToolTipText",
                                        InstrumentedAbstractAction.SHORT_DESCRIPTION },
                                /* 39 */{ "JComponent.getToolTipText" },
                                /* 40 */{
                                        "JComponent.firePropertyChange",
                                        "ToolTipText",
                                        null,
                                        InstrumentedAbstractAction.SHORT_DESCRIPTION },
                                /* 41 */{ "awt.Component.removeMouseListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 42 */{ "awt.Component.addMouseListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 43 */{
                                        "awt.Component.removeMouseMotionListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 44 */{
                                        "awt.Component.addMouseMotionListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 45 */{ "AbstractAction", "getValue",
                                        "SmallIcon" },
                                /* 46 */{ "AbstractButton.setIcon",
                                        InstrumentedAbstractAction.SMALL_ICON },
                                /* 47 */{ "JComponent.firePropertyChange",
                                        "icon", null,
                                        InstrumentedAbstractAction.SMALL_ICON },
                                /* 48 */{ "JComponent.revalidate" },
                                /* 49 */{ "awt.Component.getParent" },
                                /* 50 */{ "awt.Component.repaint" },
                                /* 51 */{ "JComponent.repaint", "0 0 0 0 0" },
                                /* 52 */{ "AbstractAction", "getValue",
                                        "ActionCommandKey" },
                                /* 53 */{
                                        "AbstractButton.setActionCommand",
                                        InstrumentedAbstractAction.ACTION_COMMAND_KEY },
                                /* 54 */{ "AbstractButton.getModel" },
                                /* 55 */{
                                        "ButtonModel.setActionCommand",
                                        InstrumentedAbstractAction.ACTION_COMMAND_KEY },
                                /* 56 */{ "AbstractAction", "isEnabled" },
                                /* 57 */{ "JComponent.setEnabled", "true" },
                                /* 58 */{ "JComponent.isEnabled" },
                                /* 59 */{ "awt.Component.enable", "true" },
                                /* 60 */{ "JComponent.enable" },
                                /* 61 */{ "JComponent.isEnabled" },
                                /* 62 */{
                                        "JComponent.firePropertyChangeBoolean",
                                        "enabled", "true", "true" },
                                /* 63 */{ "ButtonModel.setEnabled", "true" },
                                /* 64 */{ "ButtonModel.isEnabled" },
                                /* 65 */{ "AbstractButton.addActionListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 66 */{
                                        "AbstractButton.createActionPropertyChangeListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 67 */{
                                        "AbstractAction.addPropertyChangeListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 68 */{ "JComponent.firePropertyChange",
                                        "action", null,
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 69 */{ "JComponent.revalidate" },
                                /* 70 */{ "awt.Component.getParent" },
                                /* 71 */{ "awt.Component.repaint" },
                                /* 72 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setAction(Action != null) to call another sequence of events");
        }

        InstrumentedUILog.clear();
        if (iab.getAction() != action) {
            return failed("expected getAction() to return what was set by setAction(...)");
        }
        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getAction" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getAction() not to call any additional methods");
        }

        return passed();
    }

    public Result testDisplayedMnemonicIndex() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (iab.getDisplayedMnemonicIndex() != -1) {
            return failed("expected getDisplayedMnemonicIndex to return -1 by default");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getDisplayedMnemonicIndex" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getDisplayedMnemonicIndex not to call any more methods");
        }

        InstrumentedUILog.clear();
        try {
            iab.setDisplayedMnemonicIndex(0);
            return failed("expected abstractButton.setDisplayedMnemonicIndex(0) to throw IllegalArgumentException if text is not set");
        } catch (IllegalArgumentException e) {
        }
        iab.setDisplayedMnemonicIndex(-1);

        iab.setText("");
        try {
            iab.setDisplayedMnemonicIndex(0);
            return failed("expected abstractButton.setDisplayedMnemonicIndex(0) to throw IllegalArgumentException aftersetText('')");
        } catch (IllegalArgumentException e) {
        }

        iab.setText("x");
        InstrumentedUILog.clear();
        iab.setDisplayedMnemonicIndex(0);
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setDisplayedMnemonicIndex", "0" },
                /* 2 */{ "AbstractButton.getText" },
                /* 3 */{ "JComponent.firePropertyChangeInt",
                        "displayedMnemonicIndex", "-1", "0" },
                /* 4 */{ "JComponent.revalidate" },
                /* 5 */{ "awt.Component.getParent" },
                /* 6 */{ "awt.Component.repaint" },
                /* 7 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "0" },
                        /* 2 */{ "AbstractButton.getText" },
                        /* 3 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "0" },
                        /* 4 */{ "JComponent.firePropertyChange",
                                "displayedMnemonicIndex", new Integer(-1),
                                new Integer(0) },
                        /* 5 */{ "JComponent.revalidate" },
                        /* 6 */{ "awt.Container.invalidate" },
                        /* 7 */{ "awt.Component.getParent" },
                        /* 8 */{ "JComponent.isValidateRoot" },
                        /* 9 */{ "awt.Component.getParent" },
                        /* 10 */{ "awt.Component.isValid" },
                        /* 11 */{ "awt.Component.isShowing" },
                        /* 12 */{ "awt.Component.isVisible" },
                        /* 13 */{ "awt.Component.isDisplayable" },
                        /* 14 */{ "awt.Component.repaint" },
                        /* 15 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 16 */{ "JComponent.getVisibleRect" },
                        /* 17 */{ "JComponent.getWidth" },
                        /* 18 */{ "JComponent.getHeight" },
                        /* 19 */{ "awt.Component.getParent" }, })
        && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "AbstractButton.setDisplayedMnemonicIndex", "0"}, 
                /* 2 */ { "JComponent.firePropertyChangeInt", "displayedMnemonicIndex", "-1", "0"}, 
                /* 3 */ { "JComponent.revalidate"}, 
                /* 4 */ { "awt.Container.invalidate"}, 
                /* 5 */ { "awt.Component.getParent"}, 
                /* 6 */ { "JComponent.isValidateRoot"}, 
                /* 7 */ { "awt.Component.getParent"}, 
                /* 8 */ { "awt.Component.isValid"}, 
                /* 9 */ { "awt.Component.isShowing"}, 
                /* 10 */ { "awt.Component.isVisible"}, 
                /* 11 */ { "awt.Component.isDisplayable"}, 
                /* 12 */ { "awt.Component.repaint"}, 
                })                
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setDisplayedMnemonicIndex() to call another sequence of events");
        }
        if (iab.getDisplayedMnemonicIndex() != 0) {
            return failed("expected getDisplayedMnemonicIndex() to return 0 after setDisplayedMnemonicIndex(0)");
        }
        return passed();
    }

    public Result testHorizontalAlignment() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();
        //if(iab.getHorizontalAlignment() != SwingConstants.RIGHT) {

        if (iab.getHorizontalAlignment() != SwingConstants.CENTER
                && iab.getHorizontalAlignment() != SwingConstants.RIGHT) {
            return failed("expected getHorizontalAlignment to return  SwingConstants.CENTER or SwingConstants.RIGHT by default");
        }
        //set something to be sure what state we have now
        iab.setHorizontalAlignment(SwingConstants.RIGHT);
        InstrumentedUILog.clear();
        iab.getHorizontalAlignment();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getHorizontalAlignment" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getHorizontalAlignment not to call any more methods");
        }

        InstrumentedUILog.clear();
        iab.setHorizontalAlignment(SwingConstants.LEFT);

        if (!InstrumentedUILog.contains(new Object[][] {
                /* 1 */{ "AbstractButton.setHorizontalAlignment",
                        "" + SwingConstants.LEFT },
                /* 2 */{ "AbstractButton.checkHorizontalKey", "2", "" },
                /* 3 */{ "JComponent.firePropertyChangeInt",
                        "horizontalAlignment", "" + SwingConstants.RIGHT,
                        "" + SwingConstants.LEFT },
                /* 4 */{ "awt.Component.repaint" },
                /* 5 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setHorizontalAlignment(SwingConstants.LEFT) to call another sequence of events");
        }

        //don't test invalid values here because we've tested
        // checkHorizontalKey() explicitly before

        log
                .info("        NOTE: reference VM bug: getHorizontalAlignment() is "
                        + "SwingConstants.CENTER by default but is said to be SwingConstants.RIGHT");
        return passed();
    }

    public Result testHorizontalTextPosition() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (iab.getHorizontalTextPosition() != SwingConstants.TRAILING
                && iab.getHorizontalTextPosition() != SwingConstants.RIGHT) {
            return failed("expected getHorizontalTextPosition to return  SwingConstants.TRAILING or SwingConstants.RIGHT by default");
        }
        //set something to be sure what state we have now
        iab.setHorizontalTextPosition(SwingConstants.RIGHT);
        InstrumentedUILog.clear();
        iab.getHorizontalTextPosition();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getHorizontalTextPosition" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getHorizontalTextPosition not to call any more methods");
        }

        InstrumentedUILog.clear();
        iab.setHorizontalTextPosition(SwingConstants.LEFT);

        if (!InstrumentedUILog.contains(new Object[][] {
                /* 1 */{ "AbstractButton.setHorizontalTextPosition",
                        "" + SwingConstants.LEFT },
                /* 2 */{ "AbstractButton.checkHorizontalKey",
                        "" + SwingConstants.LEFT, "" },
                /* 3 */{ "JComponent.firePropertyChangeInt",
                        "horizontalTextPosition", "" + SwingConstants.RIGHT,
                        "" + SwingConstants.LEFT },
                /* 4 */{ "awt.Component.repaint" },
                /* 5 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setHorizontalTextPosition(SwingConstants.LEFT) to call another sequence of events");
        }

        //don't test invalid values here because we've tested
        // checkHorizontalKey() explicitly before

        log
                .info("        NOTE: reference VM bug: getHorizontalTextPosition() is "
                        + "SwingConstants.TRAILING by default but is said to be SwingConstants.RIGHT");
        return passed();
    }

    public Result testIcon() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (iab.getIcon() != null) {
            return failed("expected getIcon() to return null by default");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getIcon" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getIcon() not to call any more methods");
        }
        Icon i = new ColorIcon(Color.CYAN);
        InstrumentedUILog.clear();
        iab.setIcon(i);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractButton.setIcon", i },
        /* 2 */{ "JComponent.firePropertyChange", "icon", null, i },
        /* 3 */{ "JComponent.revalidate" },
        /* 4 */{ "awt.Component.getParent" },
        /* 5 */{ "awt.Component.repaint" },
        /* 6 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setIcon() to call another sequence of methods");
        }

        if (iab.getIcon() != i) {
            return failed("expected getIcon() to return what was set by setIcon()");
        }

        return passed();
    }

    public Result testIconTextGap() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (iab.getIconTextGap() != 4) {
            return failed("expected getIconTextGap() to return 4 by default, got "
                    + iab.getIconTextGap());
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getIconTextGap" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getIconTextGap() not to call any more methods");
        }

        InstrumentedUILog.clear();

        iab.setIconTextGap(-1);

        if (!InstrumentedUILog.contains(new Object[][] {
                /* 1 */{ "AbstractButton.setIconTextGap", "-1" },
                /* 2 */{ "JComponent.firePropertyChangeInt", "iconTextGap",
                        "4", "-1" },
                /* 3 */{ "JComponent.revalidate" },
                /* 4 */{ "awt.Component.getParent" },
                /* 5 */{ "awt.Component.repaint" },
                /* 6 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setIconTextGap(-1) to call another sequence of events");
        }

        iab.setIconTextGap(0);
        iab.setIconTextGap(Integer.MAX_VALUE);

        return passed();
    }

    public Result testMnemonic() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (iab.getMnemonic() != 0) {
            return failed("extected getMnemonic() to return 0 by default");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getMnemonic" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getMnemonic() not to call any more methods");
        }

        InstrumentedUILog.clear();

        iab.setMnemonic(java.awt.event.KeyEvent.VK_F);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setMnemonic",
                        "" + java.awt.event.KeyEvent.VK_F },
                /* 2 */{ "AbstractButton.getMnemonic" },
                /* 3 */{ "ButtonModel.setMnemonic",
                        "" + java.awt.event.KeyEvent.VK_F },
                /* 4 */{ "ButtonModel.fireStateChanged" },
                /* 5 */{ "ButtonModel.getMnemonic" },
                /* 6 */{ "JComponent.firePropertyChangeInt", "mnemonic", "0",
                        "" + java.awt.event.KeyEvent.VK_F },
                /* 7 */{ "AbstractButton.getText" },
                /* 8 */{ "AbstractButton.setDisplayedMnemonicIndex", "-1" },
                /* 9 */{ "JComponent.firePropertyChangeInt",
                        "displayedMnemonicIndex", "-1", "-1" },
                /* 10 */{ "JComponent.revalidate" },
                /* 11 */{ "awt.Component.getParent" },
                /* 12 */{ "awt.Component.repaint" },
                /* 13 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setMnemonic",
                                "" + java.awt.event.KeyEvent.VK_F },
                        /* 2 */{ "AbstractButton.getMnemonic" },
                        /* 3 */{ "ButtonModel.setMnemonic",
                                "" + java.awt.event.KeyEvent.VK_F },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getMnemonic" },
                        /* 6 */{ "JComponent.firePropertyChangeInt",
                                "mnemonic", "0",
                                "" + java.awt.event.KeyEvent.VK_F },
                        /* 7 */{ "AbstractButton.getText" },
                        /* 8 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 9 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 10 */{ "JComponent.revalidate" },
                        /* 11 */{ "awt.Component.getParent" },
                        /* 12 */{ "awt.Component.repaint" },
                        /* 13 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 14 */{ "AbstractButton.fireStateChanged" },
                        /* 15 */{ "awt.Component.repaint" },
                        /* 16 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 17 */{ "ButtonModel.getMnemonic" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setMnemonic(java.awt.event.KeyEvent.VK_F) to call another sequence of methods");
        }

        InstrumentedUILog.clear();
        iab.setText("Open External File...");
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setText", "Open External File..." },
                /* 2 */{ "JComponent.firePropertyChange", "text", "",
                        "Open External File..." },
                /* 3 */{ "AbstractButton.getMnemonic" },
                /* 4 */{ "AbstractButton.setDisplayedMnemonicIndex", "14" },
                /* 5 */{ "AbstractButton.getText" },
                /* 6 */{ "JComponent.firePropertyChangeInt",
                        "displayedMnemonicIndex", "-1", "14" },
                /* 7 */{ "JComponent.revalidate" },
                /* 8 */{ "awt.Component.getParent" },
                /* 9 */{ "awt.Component.repaint" },
                /* 10 */{ "JComponent.repaint", "0 0 0 0 0" },
                /* 11 */{ "JComponent.revalidate" },
                /* 12 */{ "awt.Component.getParent" },
                /* 13 */{ "awt.Component.repaint" },
                /* 14 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setText(<string>) with string that contains mnemonic key "
                    + "to fire another sequence of events");
        }
        InstrumentedUILog.clear();

        iab.setMnemonic(java.awt.event.KeyEvent.VK_E);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setMnemonic",
                        "" + java.awt.event.KeyEvent.VK_E },
                /* 2 */{ "AbstractButton.getMnemonic" },
                /* 3 */{ "ButtonModel.setMnemonic",
                        "" + java.awt.event.KeyEvent.VK_E },
                /* 4 */{ "ButtonModel.fireStateChanged" },
                /* 5 */{ "ButtonModel.getMnemonic" },
                /* 6 */{ "JComponent.firePropertyChangeInt", "mnemonic",
                        "" + java.awt.event.KeyEvent.VK_F,
                        "" + java.awt.event.KeyEvent.VK_E },
                /* 7 */{ "AbstractButton.getText" },
                /* 8 */{ "AbstractButton.setDisplayedMnemonicIndex", "2" },
                /* 9 */{ "AbstractButton.getText" },
                /* 10 */{ "JComponent.firePropertyChangeInt",
                        "displayedMnemonicIndex", "14", "2" },
                /* 11 */{ "JComponent.revalidate" },
                /* 12 */{ "awt.Component.getParent" },
                /* 13 */{ "awt.Component.repaint" },
                /* 14 */{ "JComponent.repaint", "0 0 0 0 0" },
                /* 15 */{ "JComponent.revalidate" },
                /* 16 */{ "awt.Component.getParent" },
                /* 17 */{ "awt.Component.repaint" },
                /* 18 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setMnemonic",
                                "" + java.awt.event.KeyEvent.VK_E },
                        /* 2 */{ "AbstractButton.getMnemonic" },
                        /* 3 */{ "ButtonModel.setMnemonic",
                                "" + java.awt.event.KeyEvent.VK_E },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getMnemonic" },
                        /* 6 */{ "JComponent.firePropertyChangeInt",
                                "mnemonic", "" + java.awt.event.KeyEvent.VK_F,
                                "" + java.awt.event.KeyEvent.VK_E },
                        /* 7 */{ "AbstractButton.getText" },
                        /* 8 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "2" },
                        /* 9 */{ "AbstractButton.getText" },
                        /* 10 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "14", "2" },
                        /* 11 */{ "JComponent.revalidate" },
                        /* 12 */{ "awt.Component.getParent" },
                        /* 13 */{ "awt.Component.repaint" },
                        /* 14 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 15 */{ "JComponent.revalidate" },
                        /* 16 */{ "awt.Component.getParent" },
                        /* 17 */{ "awt.Component.repaint" },
                        /* 18 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 19 */{ "AbstractButton.fireStateChanged" },
                        /* 20 */{ "awt.Component.repaint" },
                        /* 21 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 22 */{ "ButtonModel.getMnemonic" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setMnemonic(int) with char that exists in button text"
                    + " to fire another sequence of events");
        }

        if (iab.getMnemonic() != java.awt.event.KeyEvent.VK_E) {
            return failed("expected getMnemonic to return what was set by setMnemonic()");
        }
        return passed();
    }

    public Result testModel() {
        AbstractButton ab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        ButtonModel bm1 = ab.getModel();

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getModel" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getModel() not to call any additional methods");
        }

        ButtonModel bm2 = new InstrumentedButtonModel();
        InstrumentedUILog.clear();

        ab.setModel(bm2);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog //RI 1.4
                .equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setModel" },
                        /* 2 */{ "AbstractButton.getModel" },
                        /* 3 */{ "ButtonModel.removeChangeListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 4 */{ "ButtonModel.removeActionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 5 */{ "AbstractButton.createChangeListener" },
                        /* 6 */{ "AbstractButton.createActionListener" },
                        /* 7 */{ "AbstractButton.createItemListener" },
                        /* 8 */{ "ButtonModel.addChangeListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 9 */{ "ButtonModel.addActionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 10 */{ "ButtonModel.addItemListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 11 */{ "ButtonModel.getMnemonic" },
                        /* 12 */{ "AbstractButton.getText" },
                        /* 13 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 14 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 15 */{ "JComponent.firePropertyChange", "model",
                                bm1, bm2 },
                        /* 16 */{ "JComponent.revalidate" },
                        /* 17 */{ "awt.Component.getParent" },
                        /* 18 */{ "awt.Component.repaint" },
                        /* 19 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog
                        //RI 1.5
                        .equals(new Object[][] {
                                /* 1 */{ "AbstractButton.setModel" },
                                /* 2 */{ "AbstractButton.getModel" },
                                /* 3 */{ "ButtonModel.removeChangeListener",
                                        null },
                                /* 4 */{ "ButtonModel.removeActionListener",
                                        null },
                                { "ButtonModel.removeItemListener", null },
                                /* 5 */{ "AbstractButton.createChangeListener" },
                                /* 6 */{ "AbstractButton.createActionListener" },
                                /* 7 */{ "AbstractButton.createItemListener" },
                                /* 8 */{ "ButtonModel.addChangeListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 9 */{ "ButtonModel.addActionListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 10 */{ "ButtonModel.addItemListener",
                                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 11 */{ "ButtonModel.getMnemonic" },
                                /* 12 */{ "AbstractButton.getText" },
                                /* 13 */{
                                        "AbstractButton.setDisplayedMnemonicIndex",
                                        "-1" },
                                /* 14 */{ "JComponent.firePropertyChangeInt",
                                        "displayedMnemonicIndex", "-1", "-1" },
                                /* 15 */{ "JComponent.firePropertyChange",
                                        "model", bm1, bm2 },
                                /* 16 */{ "JComponent.revalidate" },
                                /* 17 */{ "awt.Component.getParent" },
                                /* 18 */{ "awt.Component.repaint" },
                                /* 19 */{ "JComponent.repaint", "0 0 0 0 0" }, })

                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setModel" },
                        /* 2 */{ "AbstractButton.getModel" },
                        /* 3 */{ "ButtonModel.removeChangeListener", null },
                        /* 4 */{ "ButtonModel.removeActionListener", null },
                        { "ButtonModel.removeItemListener", null },
                        /* 5 */{ "AbstractButton.createChangeListener" },
                        /* 6 */{ "AbstractButton.createActionListener" },
                        /* 7 */{ "AbstractButton.createItemListener" },
                        /* 8 */{ "ButtonModel.addChangeListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 9 */{ "ButtonModel.addActionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 10 */{ "ButtonModel.addItemListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 11 */{ "ButtonModel.getMnemonic" },
                        /* 12 */{ "AbstractButton.getText" },
                        /* 13 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 14 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 15 */{ "JComponent.firePropertyChange", "model",
                                bm1, bm2 },
                        /* 16 */{ "JComponent.revalidate" },
                        /* 17 */{ "awt.Component.getParent" },
                        /* 18 */{ "awt.Component.repaint" },
                        /* 19 */{ "JComponent.repaint", "0 0 0 0 0" }, })

                //Linux 1.5
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setModel" },
                        /* 2 */{ "AbstractButton.getModel" },
                        /* 3 */{ "ButtonModel.removeChangeListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 4 */{ "ButtonModel.removeActionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 5 */{ "ButtonModel.removeItemListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 6 */{ "AbstractButton.createChangeListener" },
                        /* 7 */{ "AbstractButton.createActionListener" },
                        /* 8 */{ "AbstractButton.createItemListener" },
                        /* 9 */{ "ButtonModel.addChangeListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 10 */{ "ButtonModel.addActionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 11 */{ "ButtonModel.addItemListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 12 */{ "ButtonModel.getMnemonic" },
                        /* 13 */{ "AbstractButton.getText" },
                        /* 14 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 15 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 16 */{ "JComponent.firePropertyChange", "model",
                                bm1, bm2 },
                        /* 17 */{ "JComponent.revalidate" },
                        /* 18 */{ "awt.Component.getParent" },
                        /* 19 */{ "awt.Component.repaint" },
                        /* 20 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "AbstractButton.setModel" },
                        { "ButtonModel.removeActionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "ButtonModel.removeItemListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "ButtonModel.removeChangeListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },

                        { "ButtonModel.addChangeListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "ButtonModel.addItemListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "ButtonModel.addActionListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "JComponent.firePropertyChange", "model", bm1, bm2 },
                        { "ButtonModel.getMnemonic" },
                        { "AbstractButton.getText" },
                        { "AbstractButton.setDisplayedMnemonicIndex", "-1" },
                        { "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        { "JComponent.firePropertyChange", "model", bm1, bm2 },
                        { "ButtonModel.equals", bm1 }, })

        ) {

            InstrumentedUILog.printLogAsArray();
            return failed("expected setModel() to call another sequence of events");
        }

        if (ab.getModel() != bm2) {
            return failed("expected getModel() to return what was set by setModel(...)");
        }

        return passed();
    }

    public Result testMultiClickThreshhold() {
        AbstractButton ab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (ab.getMultiClickThreshhold() != 0) {
            return failed("expected getMultiClickThreshhold() to return 0 by default, got "
                    + ab.getMultiClickThreshhold());
        }
        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getMultiClickThreshhold" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getMultiClickThreshhold() not to call any additional methods");
        }

        InstrumentedUILog.clear();

        ab.setMultiClickThreshhold(1234);
        if (!InstrumentedUILog.equals(new Object[][] { {
                "AbstractButton.setMultiClickThreshhold", "1234" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setMultiClickThreshhold(1234) not to call any additional methods");
        }
        InstrumentedUILog.clear();
        if (ab.getMultiClickThreshhold() != 1234) {
            return failed("expected getMultiClickThreshhold() to return 1234");
        }

        try {
            ab.setMultiClickThreshhold(-1);
            return failed("expected setMultiClickThreshhold(-1) to throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        if (ab.getMultiClickThreshhold() != 1234) {
            return failed("expected getMultiClickThreshhold() to return 1234 after exception");
        }

        ab.setMultiClickThreshhold(Long.MAX_VALUE);

        if (ab.getMultiClickThreshhold() != Long.MAX_VALUE) {
            return failed("expected getMultiClickThreshhold() to return Long.MAX_VALUE");
        }

        return passed();
    }

    public Result testPressedIcon() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (iab.getPressedIcon() != null) {
            return failed("expected getPressedIcon() to return null by default");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getPressedIcon" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getPressedIcon() not to call any more methods");
        }
        Icon i = new ColorIcon(Color.MAGENTA);
        InstrumentedUILog.clear();
        iab.setPressedIcon(i);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractButton.setPressedIcon", i },
        /* 2 */{ "JComponent.firePropertyChange", "pressedIcon", null, i },
        }))
{
            InstrumentedUILog.printLogAsArray();
            return failed("expected setPressedIcon() to call another sequence of methods");
        }

        ButtonModel bm = new InstrumentedButtonModel() {
            public boolean isPressed() {
                super.isPressed();
                return true;
            }

        };

        iab.setModel(bm);
        InstrumentedUILog.clear();
        //iab.setPressedIcon(null);
        iab.setPressedIcon(i);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractButton.setPressedIcon", i },
        /* 2 */{ "JComponent.firePropertyChange", "pressedIcon", i, i }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setPressedIcon() to call another sequence of methods when button is pressed");
        }

        if (iab.getPressedIcon() != i) {
            return failed("expected getPressedIcon() to return what was set by setPressedIcon()");
        }

        //ok now - third case
        InstrumentedUILog.clear();
        //iab.setPressedIcon(null);
        iab.setPressedIcon(null);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractButton.setPressedIcon", null },
        /* 2 */{ "JComponent.firePropertyChange", "pressedIcon", i, null },
        /* 4 */{ "ButtonModel.isPressed" },
        /* 5 */{ "awt.Component.repaint" },
        /* 6 */{ "JComponent.repaint", "0 0 0 0 0" }, })

        && !InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractButton.setPressedIcon", null },
        /* 2 */{ "JComponent.firePropertyChange", "pressedIcon", i, null },
        /* 4 */{ "JComponent.revalidate" },
        /* 5 */{ "awt.Component.repaint" },
        /* 6 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setPressedIcon(null) to call another sequence of methods when button is pressed");
        }
        if (iab.getPressedIcon() != null) {
            return failed("expected getPressedIcon() to return what was set by setPressedIcon() (null)");
        }

        return passed();
    }

    public Result testText() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (!"".equals(iab.getText())) {
            return failed("expected getText() to return '' by default, got '"
                    + iab.getText() + "'");
        }

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractButton.getText" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getText() not to call any more methods");
        }

        InstrumentedUILog.clear();

        iab.setText("foobar");
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setText", "foobar" },
                        /* 2 */{ "JComponent.firePropertyChange", "text", "",
                                "foobar" },
                        /* 3 */{ "AbstractButton.getMnemonic" },
                        /* 4 */{ "AbstractButton.setDisplayedMnemonicIndex",
                                "-1" },
                        /* 5 */{ "JComponent.firePropertyChangeInt",
                                "displayedMnemonicIndex", "-1", "-1" },
                        /* 6 */{ "JComponent.revalidate" },
                        /* 7 */{ "awt.Component.getParent" },
                        /* 8 */{ "awt.Component.repaint" },
                        /* 9 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setText(String) to call another sequence of events");
        }

        if (!"foobar".equals(iab.getText())) {
            return failed("getText() to return what was set by setText()");
        }
        InstrumentedUILog.clear();

        iab.setText("foobar");
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setText", "foobar" },
                /* 2 */{ "JComponent.firePropertyChange", "text", "foobar",
                        "foobar" },
                /* 3 */{ "AbstractButton.getMnemonic" },
                /* 4 */{ "AbstractButton.setDisplayedMnemonicIndex", "-1" },
                /* 5 */{ "JComponent.firePropertyChangeInt",
                        "displayedMnemonicIndex", "-1", "-1" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setText(<current text>) to call another sequence of events");
        }
        iab.setMnemonic(java.awt.event.KeyEvent.VK_Z);
        InstrumentedUILog.clear();
        iab.setText("foobaz");
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setText", "foobaz" },
                /* 2 */{ "JComponent.firePropertyChange", "text", "foobar",
                        "foobaz" },
                /* 3 */{ "AbstractButton.getMnemonic" },
                /* 4 */{ "AbstractButton.setDisplayedMnemonicIndex", "5" },
                /* 5 */{ "AbstractButton.getText" },
                /* 6 */{ "JComponent.firePropertyChangeInt",
                        "displayedMnemonicIndex", "-1", "5" },
                /* 7 */{ "JComponent.revalidate" },
                /* 8 */{ "awt.Component.getParent" },
                /* 9 */{ "awt.Component.repaint" },
                /* 10 */{ "JComponent.repaint", "0 0 0 0 0" },
                /* 11 */{ "JComponent.revalidate" },
                /* 12 */{ "awt.Component.getParent" },
                /* 13 */{ "awt.Component.repaint" },
                /* 14 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setText(text) to call another sequence of events if text contains "
                    + " mnemonic key");
        }

        InstrumentedUILog.clear();
        iab.setText("foobar");
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setText", "foobar" },
                /* 2 */{ "JComponent.firePropertyChange", "text", "foobaz",
                        "foobar" },
                /* 3 */{ "AbstractButton.getMnemonic" },
                /* 4 */{ "AbstractButton.setDisplayedMnemonicIndex", "-1" },
                /* 5 */{ "JComponent.firePropertyChangeInt",
                        "displayedMnemonicIndex", "5", "-1" },
                /* 6 */{ "JComponent.revalidate" },
                /* 7 */{ "awt.Component.getParent" },
                /* 8 */{ "awt.Component.repaint" },
                /* 9 */{ "JComponent.repaint", "0 0 0 0 0" },
                /* 10 */{ "JComponent.revalidate" },
                /* 11 */{ "awt.Component.getParent" },
                /* 12 */{ "awt.Component.repaint" },
                /* 13 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setText(text) to call another sequence of events if text doesn't contain "
                    + " mnemonic key");
        }

        return passed();
    }

    public Result testUI() {
        AbstractButton ab = new AbstractButton() {
        };
        if (ab.getUI() != null) {
            return failed("expected default abstractbutton UI to be null");
        }

        ab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();
        ab.getUI();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractButton.getUI" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getUI() not to call any more methods");
        }

        ButtonUI bui = new InstrumentedButtonUI();
        InstrumentedUILog.clear();
        ab.setUI(bui);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractButton.setUI", bui },
        /* 2 */{ "ButtonUI.installUI", ab },
        /* 3 */{ "JComponent.firePropertyChange", "UI", null, bui },
        /* 4 */{ "JComponent.revalidate" },
        /* 5 */{ "awt.Component.getParent" },
        /* 6 */{ "awt.Component.repaint" },
        })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setUI(UI) on a button with null ui to call another sequence of events");
        }

        if (ab.getUI() == bui) {
            return failed("expected getUI() not to return what was set by setUI()");
        }

        InstrumentedUILog.clear();
        ab.setUI(bui);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractButton.setUI", bui },
        /* 2 */{ "ButtonUI.uninstallUI", ab },
        /* 3 */{ "ButtonUI.installUI", ab },
        /* 4 */{ "JComponent.firePropertyChange", "UI", bui, bui },
        /* 5 */{ "JComponent.revalidate" },
        /* 6 */{ "awt.Component.getParent" },
        /* 7 */{ "awt.Component.repaint" },
        /* 8 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setUI(UI) on a button with non-null ui to call another sequence of events");
        }

        InstrumentedUILog.clear();
        ab.setUI(null);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractButton.setUI", null },
        /* 2 */{ "ButtonUI.uninstallUI", ab },
        /* 3 */{ "JComponent.firePropertyChange", "UI", bui, null },
        /* 4 */{ "JComponent.revalidate" },
        /* 5 */{ "awt.Component.getParent" },
        /* 6 */{ "awt.Component.repaint" },
        /* 7 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setUI(null) on a button with non-null ui to call another sequence of events");
        }

        return passed();
    }

    public Result testVerticalAlignment() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (iab.getVerticalAlignment() != SwingConstants.CENTER) {
            return failed("expected getVerticalAlignment to return  SwingConstants.CENTER by default");
        }

        InstrumentedUILog.clear();
        iab.getVerticalAlignment();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getVerticalAlignment" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getVerticalAlignment not to call any more methods");
        }

        InstrumentedUILog.clear();
        iab.setVerticalAlignment(SwingConstants.BOTTOM);

        if (!InstrumentedUILog.contains(new Object[][] {
                /* 1 */{ "AbstractButton.setVerticalAlignment",
                        "" + SwingConstants.BOTTOM },
                /* 2 */{ "AbstractButton.checkVerticalKey",
                        "" + SwingConstants.BOTTOM, "verticalAlignment" },
                /* 3 */{ "JComponent.firePropertyChangeInt",
                        "verticalAlignment", "" + SwingConstants.CENTER,
                        "" + SwingConstants.BOTTOM },
                /* 4 */{ "awt.Component.repaint" },
                /* 5 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVerticalAlignment(SwingConstants.BOTTOM) to call another sequence of events");
        }

        //don't test illegal values - they're tested in
        // TestCheckVerticalAlignment

        return passed();
    }

    public Result testVerticalTextPosition() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();

        if (iab.getVerticalTextPosition() != SwingConstants.CENTER) {
            return failed("expected getVerticalTextPosition to return SwingConstants.CENTER by default");
        }

        InstrumentedUILog.clear();
        iab.getVerticalTextPosition();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "AbstractButton.getVerticalTextPosition" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getVerticalTextPosition not to call any more methods");
        }

        InstrumentedUILog.clear();
        iab.setVerticalTextPosition(SwingConstants.BOTTOM);

        if (!InstrumentedUILog.contains(new Object[][] {
                /* 1 */{ "AbstractButton.setVerticalTextPosition",
                        "" + SwingConstants.BOTTOM },
                /* 2 */{ "AbstractButton.checkVerticalKey",
                        "" + SwingConstants.BOTTOM, "verticalTextPosition" },
                /* 3 */{ "JComponent.firePropertyChangeInt",
                        "verticalTextPosition", "" + SwingConstants.CENTER,
                        "" + SwingConstants.BOTTOM },
                /* 4 */{ "awt.Component.repaint" },
                /* 5 */{ "JComponent.repaint", "0 0 0 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVerticalTextPosition(SwingConstants.BOTTOM) to call another sequence of events");
        }

        return passed();
    }

    public Result testConstructor() {
        InstrumentedUILog.clear();
        AbstractButton iab = new InstrumentedAbstractButton();
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "awt.Component.addFocusListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 2 */{ "JComponent.isManagingFocus" },
                /* 3 */{ "awt.Component.setLocale",
                        JComponent.getDefaultLocale() },
                /* 4 */{ "JComponent.firePropertyChange", "locale", null,
                        JComponent.getDefaultLocale() },
                /* 5 */{ "ButtonModel.setEnabled", "true" },
                /* 6 */{ "ButtonModel.isEnabled" },
                /* 7 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "awt.Component.addFocusListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 2 */{ "JComponent.isManagingFocus" },
                        /* 3 *///{ "JComponent.setLocale",
                        // JComponent.getDefaultLocale()},
                        /* 4 */{ "JComponent.firePropertyChange", "locale",
                                null, JComponent.getDefaultLocale() },
                        /* 5 */{ "ButtonModel.setEnabled", "true" },
                        /* 6 */{ "ButtonModel.isEnabled" },
                        /* 7 */{ "ButtonModel.fireStateChanged" }, })

                && !InstrumentedUILog.equals(new Object[][] { // RI 1.5
                                { "JComponent.isManagingFocus" },
                                { "JComponent.firePropertyChange", "locale",
                                        null, JComponent.getDefaultLocale() },
                                { "ButtonModel.setEnabled", "true" },
                                { "ButtonModel.isEnabled" },
                                { "ButtonModel.fireStateChanged" }, }

                        )
                && !InstrumentedUILog.equals(new Object[][] { // Linux RI 1.5
                        /* 1 */ { "JComponent.isManagingFocus"}, 
                        /* 2 */ { "JComponent.firePropertyChange", "locale", null, AbstractButton.getDefaultLocale()}, 
                        /* 3 */ { "ButtonModel.setEnabled", "true"}, 
                        /* 4 */ { "ButtonModel.isEnabled"}, 
                        /* 5 */ { "ButtonModel.fireStateChanged"}, 
                        /* 6 */ { "AbstractButton.setModel"}, 
                        /* 7 */ { "AbstractButton.getModel"}, 
                        /* 8 */ { "AbstractButton.createChangeListener"}, 
                        /* 9 */ { "AbstractButton.createActionListener"}, 
                        /* 10 */ { "AbstractButton.createItemListener"}, 
                        /* 11 */ { "ButtonModel.addChangeListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 12 */ { "ButtonModel.addActionListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 13 */ { "ButtonModel.addItemListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 14 */ { "ButtonModel.getMnemonic"}, 
                        /* 15 */ { "AbstractButton.getText"}, 
                        /* 16 */ { "AbstractButton.setDisplayedMnemonicIndex", "-1"}, 
                        /* 17 */ { "JComponent.firePropertyChangeInt", "displayedMnemonicIndex", "-1", "-1"}, 
                        /* 18 */ { "JComponent.firePropertyChange", "model", null, InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 19 */ { "JComponent.revalidate"}, 
                        /* 20 */ { "awt.Component.getParent"}, 
                        /* 21 */ { "awt.Component.repaint"}, 
                        /* 22 */ { "JComponent.repaint", "0 0 0 0 0"},} 
                )
                        
                && !InstrumentedUILog.contains(new Object[][] {
                         { "JComponent.firePropertyChange", "locale", null, JComponent.getDefaultLocale()}, 
                         { "AbstractButton.createChangeListener"}, 
                         { "AbstractButton.createActionListener"}, 
                         { "AbstractButton.createItemListener"}, 
                         { "AbstractButton.setModel"}, 
                         { "ButtonModel.addChangeListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                         { "ButtonModel.addItemListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                         { "ButtonModel.addActionListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                         { "ButtonModel.getMnemonic"}, 
                         { "JComponent.firePropertyChange", "model", null, InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected new AbstractButton() to call another sequence of events");
        }

        return passed();
    }

    public Result testBorderPainted() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();
        if (!iab.isBorderPainted()) {
            return failed("expected isBorderPainted() to return true by default");
        }
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractButton.isBorderPainted" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.isBorderPainted not to call any additional methods");
        }

        InstrumentedUILog.clear();
        iab.setBorderPainted(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setBorderPainted", "false" },
                /* 2 */{ "JComponent.firePropertyChangeBoolean",
                        "borderPainted", "true", "false" },
                /* 3 */{ "JComponent.revalidate" },
                /* 4 */{ "awt.Component.getParent" },
                /* 5 */{ "awt.Component.repaint" },
                /* 6 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setBorderPainted", "false" },
                        /* 2 */{ "JComponent.firePropertyChangeBoolean",
                                "borderPainted", "true", "false" },
                        /* 3 */{ "JComponent.firePropertyChange",
                                "borderPainted", Boolean.TRUE, Boolean.FALSE },
                        /* 4 */{ "JComponent.revalidate" },
                        /* 5 */{ "awt.Container.invalidate" },
                        /* 6 */{ "awt.Component.getParent" },
                        /* 7 */{ "JComponent.isValidateRoot" },
                        /* 8 */{ "awt.Component.getParent" },
                        /* 9 */{ "awt.Component.isValid" },
                        /* 10 */{ "awt.Component.isShowing" },
                        /* 11 */{ "awt.Component.isVisible" },
                        /* 12 */{ "awt.Component.isDisplayable" },
                        /* 13 */{ "awt.Component.repaint" },
                        /* 14 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 15 */{ "JComponent.getVisibleRect" },
                        /* 16 */{ "JComponent.getWidth" },
                        /* 17 */{ "JComponent.getHeight" },
                        /* 18 */{ "awt.Component.getParent" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.setBorderPainted(false) to call another sequence of events");
        }
        InstrumentedUILog.clear();
        iab.setBorderPainted(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setBorderPainted", "false" },
                /* 2 */{ "JComponent.firePropertyChangeBoolean",
                        "borderPainted", "false", "false" }, })

                && !InstrumentedUILog
                        .equals(new Object[][] {
                                /* 1 */{ "AbstractButton.setBorderPainted",
                                        "false" },
                                /* 2 */{
                                        "JComponent.firePropertyChangeBoolean",
                                        "borderPainted", "false", "false" },
                                /* 3 */{ "JComponent.firePropertyChange",
                                        "borderPainted", Boolean.FALSE,
                                        Boolean.FALSE }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected second AbstractButton.setBorderPainted(false) to call another sequence of events");
        }

        return passed();
    }

    public Result testContentAreaFilled() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();
        if (!iab.isContentAreaFilled()) {
            return failed("expected isContentAreaFilled() to return true by default");
        }

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractButton.isContentAreaFilled" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.isContentAreaFilled not to call any additional methods");
        }

        InstrumentedUILog.clear();
        iab.setContentAreaFilled(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setContentAreaFilled", "false" },
                /* 2 */{ "JComponent.firePropertyChangeBoolean",
                        "contentAreaFilled", "true", "false" },
                /* 3 */{ "awt.Component.repaint" },
                /* 4 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "AbstractButton.setContentAreaFilled", "false" },
                        { "JComponent.setOpaque", "false" },
                        { "JComponent.firePropertyChangeBoolean",
                                "contentAreaFilled", "true", "false" },
                        { "JComponent.firePropertyChange", "contentAreaFilled",
                                Boolean.TRUE, Boolean.FALSE },

                })

        )

        {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.setContentAreaFilled(false) to call another sequence of events");
        }
        InstrumentedUILog.clear();
        iab.setContentAreaFilled(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setContentAreaFilled", "false" },
                /* 2 */{ "JComponent.firePropertyChangeBoolean",
                        "contentAreaFilled", "false", "false" }, })

                && 
                !InstrumentedUILog.equals(new Object[][] {
                        { "AbstractButton.setContentAreaFilled", "false" },
                        { "JComponent.setOpaque", "false" },
                        { "JComponent.firePropertyChangeBoolean",
                                "contentAreaFilled", "false", "false" },
                        { "JComponent.firePropertyChange", "contentAreaFilled",
                                Boolean.FALSE, Boolean.FALSE }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected second AbstractButton.setContentAreaFilled(false) to call another sequence of events");
        }

        return passed();

    }

    public Result testFocusPainted() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();
        if (!iab.isFocusPainted()) {
            return failed("expected isFocusPainted() to return true by default");
        }

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractButton.isFocusPainted" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.isFocusPainted not to call any additional methods");
        }

        InstrumentedUILog.clear();
        iab.setFocusPainted(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setFocusPainted", "false" },
                /* 2 */{ "JComponent.firePropertyChangeBoolean",
                        "focusPainted", "true", "false" },
                /* 3 */{ "awt.Component.isFocusOwner" },
                /* 4 */{ "awt.Component.hasFocus" }, })

                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractButton.setFocusPainted", "false" },
                        /* 2 */{ "JComponent.firePropertyChangeBoolean",
                                "focusPainted", "true", "false" }, })
                //
                && !InstrumentedUILog
                        .equals(new Object[][] {
                                /* 1 */{ "AbstractButton.setFocusPainted",
                                        "false" },
                                /* 2 */{
                                        "JComponent.firePropertyChangeBoolean",
                                        "focusPainted", "true", "false" },
                                /* 2 */{ "JComponent.firePropertyChange",
                                        "focusPainted", Boolean.TRUE,
                                        Boolean.FALSE }, })
                && !InstrumentedUILog.contains(new Object[][] {
                        /* 1 */{ "AbstractButton.setFocusPainted", "false" },
                        /* 2 */{ "JComponent.firePropertyChangeBoolean",
                                "focusPainted", "true", "false" },
                        { "JComponent.revalidate" },
                        { "awt.Component.repaint" },
                        { "JComponent.repaint", "0 0 0 0 0" }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.setFocusPainted(false) to call another sequence of events");
        }
        InstrumentedUILog.clear();
        iab.setFocusPainted(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setFocusPainted", "false" },
                /* 2 */{ "JComponent.firePropertyChangeBoolean",
                        "focusPainted", "false", "false" }, })
                && !InstrumentedUILog
                        .equals(new Object[][] {
                                /* 1 */{ "AbstractButton.setFocusPainted",
                                        "false" },
                                /* 2 */{
                                        "JComponent.firePropertyChangeBoolean",
                                        "focusPainted", "false", "false" },
                                /* 3 */{ "JComponent.firePropertyChange",
                                        "focusPainted", Boolean.FALSE,
                                        Boolean.FALSE }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected second AbstractButton.setFocusPainted(false) to call another sequence of events");
        }

        return passed();

    }

    public Result testRolloverEnabled() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();
        if (iab.isRolloverEnabled()) {
            return failed("expected isRolloverEnabled() to return false by default");
        }

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractButton.isRolloverEnabled" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.isRolloverEnabled not to call any additional methods");
        }

        InstrumentedUILog.clear();
        iab.setRolloverEnabled(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.contains(new Object[][] {
                /* 1 */{ "AbstractButton.setRolloverEnabled", "true" },
                /* 2 */{ "JComponent.firePropertyChangeBoolean",
                        "rolloverEnabled", "false", "true" },
                /* 3 */{ "awt.Component.repaint" },
                /* 4 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog
                        .contains(new Object[][] {
                                /* 1 */{ "AbstractButton.setRolloverEnabled",
                                        "true" },
                                /* 2 */{
                                        "JComponent.firePropertyChangeBoolean",
                                        "rolloverEnabled", "false", "true" },
                                /* 3 */{ "JComponent.firePropertyChange",
                                        "rolloverEnabled", Boolean.FALSE,
                                        Boolean.TRUE }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.setRolloverEnabled(true) to call another sequence of events");
        }
        InstrumentedUILog.clear();

        iab.setRolloverEnabled(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "AbstractButton.setRolloverEnabled", "true" },
                /* 2 */{ "JComponent.firePropertyChangeBoolean",
                        "rolloverEnabled", "true", "true" }, })
                && !InstrumentedUILog
                        .equals(new Object[][] {
                                /* 1 */{ "AbstractButton.setRolloverEnabled",
                                        "true" },
                                /* 2 */{
                                        "JComponent.firePropertyChangeBoolean",
                                        "rolloverEnabled", "true", "true" },
                                /* 3 */{ "JComponent.firePropertyChange",
                                        "rolloverEnabled", Boolean.TRUE,
                                        Boolean.TRUE }, }

                        )

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected second AbstractButton.setRolloverEnabled(true) to call another sequence of events");
        }

        return passed();

    }

    public Result testEnabled() {
        AbstractButton iab = getInstrumentedAbstractButton();
        InstrumentedUILog.clear();
        if (!iab.isEnabled()) {
            return failed("expected isEnabled() to return true by default");
        }

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "JComponent.isEnabled" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.isEnabled not to call any additional methods");
        }

        InstrumentedUILog.clear();
        iab.setEnabled(false);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "JComponent.setEnabled", "false" },
                /* 2 */{ "ButtonModel.isRollover" },
                /* 3 */{ "JComponent.isEnabled" },
                /* 4 */{ "awt.Component.enable", "false" },
                /* 5 */{ "JComponent.disable" },
                /* 6 */{ "JComponent.isEnabled" },
                /* 7 */{ "awt.Component.getParent" },
                /* 8 */{ "awt.Component.isFocusOwner" },
                /* 9 */{ "awt.Component.hasFocus" },
                /* 10 */{ "JComponent.firePropertyChangeBoolean", "enabled",
                        "true", "false" },
                /* 11 */{ "awt.Component.repaint" },
                /* 12 */{ "JComponent.repaint", "0 0 0 0 0" },
                /* 13 */{ "ButtonModel.setEnabled", "false" },
                /* 14 */{ "ButtonModel.isEnabled" },
                /* 15 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "JComponent.setEnabled", "false" },
                        /* 2 */{ "ButtonModel.isRollover" },
                        /* 3 */{ "JComponent.isEnabled" },
                        /* 4 */{ "awt.Component.enable", "false" },
                        /* 5 */{ "JComponent.disable" },
                        /* 6 */{ "JComponent.isEnabled" },
                        /* 7 */// { "awt.Component.getParent"},
                        /* 8 */{ "awt.Component.isFocusOwner" },
                        /* 9 */{ "awt.Component.hasFocus" },
                        /* 10 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "false" },
                        /* 11 */{ "awt.Component.repaint" },
                        /* 12 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 13 */{ "ButtonModel.setEnabled", "false" },
                        /* 14 */{ "ButtonModel.isEnabled" },
                        /* 15 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "JComponent.setEnabled", "false" },
                        /* 2 */{ "ButtonModel.isRollover" },
                        /* 3 */{ "JComponent.isEnabled" },
                        /* 4 */{ "awt.Component.enable", "false" },
                        /* 5 */{ "JComponent.disable" },
                        /* 6 */{ "JComponent.isEnabled" },
                        /* 7 */{ "awt.Component.getParent" },
                        /* 8 */{ "awt.Component.isFocusOwner" },
                        /* 9 */{ "awt.Component.hasFocus" },
                        /* 10 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "false" },
                        /* 11 */{ "awt.Component.repaint" },
                        /* 12 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 13 */{ "ButtonModel.setEnabled", "false" },
                        /* 14 */{ "ButtonModel.isEnabled" },
                        /* 15 */{ "ButtonModel.fireStateChanged" },
                        /* 16 */{ "ButtonModel.getMnemonic" },
                        /* 17 */{ "AbstractButton.fireStateChanged" },
                        /* 18 */{ "awt.Component.repaint" },
                        /* 19 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 20 */{ "awt.Component.repaint" },
                        /* 21 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "JComponent.setEnabled", "false" },
                        /* 2 */{ "ButtonModel.isRollover" },
                        /* 3 */{ "JComponent.isEnabled" },
                        /* 4 */{ "awt.Component.enable", "false" },
                        /* 5 */{ "JComponent.disable" },
                        /* 6 */{ "JComponent.isEnabled" },
                        /* 7 */{ "awt.Component.getParent" },
                        /* 8 */{ "awt.Component.isFocusOwner" },
                        /* 9 */{ "awt.Component.hasFocus" },
                        /* 10 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "false" },
                        /* 11 */{ "awt.Component.repaint" },
                        /* 12 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 13 */{ "ButtonModel.setEnabled", "false" },
                        /* 14 */{ "ButtonModel.isEnabled" },
                        /* 15 */{ "ButtonModel.fireStateChanged" },
                        /* 16 */{ "ButtonModel.getMnemonic" },
                        /* 17 */{ "AbstractButton.fireStateChanged" },
                        /* 18 */{ "awt.Component.repaint" },
                        /* 19 */{ "JComponent.repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "JComponent.setEnabled", "false" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.setEnabled", "false" },
                        /* 4 */{ "ButtonModel.isEnabled" },
                        /* 5 */{ "ButtonModel.setRollover", "false" },
                        /* 6 */{ "ButtonModel.isRollover" },
                        /* 7 */{ "ButtonModel.fireStateChanged" },
                        /* 8 */{ "ButtonModel.getChangeListeners" },
                        /* 9 */{ "JComponent.isEnabled" },
                        /* 10 */{ "awt.Component.enable", "false" },
                        /* 11 */{ "JComponent.disable" },
                        /* 12 */{ "awt.Component.isDisplayable" },
                        /* 13 */{ "awt.Component.isFocusOwner" },
                        /* 14 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "false" },
                        /* 15 */{ "JComponent.firePropertyChange", "enabled",
                                Boolean.TRUE, Boolean.FALSE },
                        /* 16 */{ "JComponent.revalidate" },
                        /* 17 */{ "awt.Container.invalidate" },
                        /* 18 */{ "awt.Component.getParent" },
                        /* 19 */{ "JComponent.isValidateRoot" },
                        /* 20 */{ "awt.Component.getParent" },
                        /* 21 */{ "awt.Component.isValid" },
                        /* 22 */{ "awt.Component.isShowing" },
                        /* 23 */{ "awt.Component.isVisible" },
                        /* 24 */{ "awt.Component.isDisplayable" },
                        /* 25 */{ "awt.Component.repaint" },
                        /* 26 */{ "JComponent.repaint", "0 0 0 0 0" },
                        /* 27 */{ "JComponent.getVisibleRect" },
                        /* 28 */{ "JComponent.getWidth" },
                        /* 29 */{ "JComponent.getHeight" },
                        /* 30 */{ "awt.Component.getParent" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "JComponent.setEnabled", "false" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.setEnabled", "false" },
                        /* 4 */{ "ButtonModel.isEnabled" },
                        /* 5 */{ "ButtonModel.setRollover", "false" },
                        /* 6 */{ "ButtonModel.isRollover" },
                        /* 7 */{ "ButtonModel.fireStateChanged" },
                        /* 8 */{ "ButtonModel.getChangeListeners" },
                        /* 9 */{ "JComponent.isEnabled" },
                        /* 10 */{ "awt.Component.enable", "false" },
                        /* 11 */{ "JComponent.disable" },
                        /* 12 */{ "awt.Component.isDisplayable" },
                        /* 13 */{ "awt.Component.isFocusOwner" },
                        /* 14 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "false" },
                        /* 15 */{ "JComponent.firePropertyChange", "enabled",
                                "true", "false" }, })
                //
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "JComponent.setEnabled", "false" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.setEnabled", "false" },
                        /* 4 */{ "ButtonModel.isEnabled" },
                        /* 5 */{ "ButtonModel.setRollover", "false" },
                        /* 6 */{ "ButtonModel.isRollover" },
                        /* 7 */{ "ButtonModel.fireStateChanged" },
                        /* 8 */{ "ButtonModel.getChangeListeners" },
                        /* 9 */{ "ButtonModel.getMnemonic" },
                        /* 10 */{ "AbstractButton.fireStateChanged" },
                        /* 11 */{ "AbstractButton.getChangeListeners" },
                        /* 12 */{ "JComponent.isEnabled" },
                        /* 13 */{ "awt.Component.enable", "false" },
                        /* 14 */{ "JComponent.disable" },
                        /* 15 */{ "awt.Component.isDisplayable" },
                        /* 16 */{ "awt.Component.isFocusOwner" },
                        /* 17 */{ "JComponent.firePropertyChangeBoolean",
                                "enabled", "true", "false" },
                        /* 18 */{ "JComponent.firePropertyChange", "enabled",
                                Boolean.TRUE, Boolean.FALSE }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected AbstractButton.setEnabled(false) to call another sequence of events");
        }
        InstrumentedUILog.clear();

        iab.setEnabled(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "JComponent.setEnabled", "false" },
                /* 2 */{ "ButtonModel.isRollover" },
                /* 3 */{ "JComponent.isEnabled" },
                /* 4 */{ "awt.Component.enable", "false" },
                /* 5 */{ "JComponent.disable" },
                /* 6 */{ "JComponent.isEnabled" },
                /* 7 */{ "JComponent.firePropertyChangeBoolean", "enabled",
                        "false", "false" },
                /* 8 */{ "ButtonModel.setEnabled", "false" },
                /* 9 */{ "ButtonModel.isEnabled" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "JComponent.setEnabled", "false" },
                /* 2 */{ "ButtonModel.isEnabled" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected second AbstractButton.setEnabled(false) to call another sequence of events");
        }

        return passed();

    }
}

