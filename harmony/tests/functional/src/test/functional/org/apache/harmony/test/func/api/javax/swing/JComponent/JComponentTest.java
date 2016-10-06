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
package org.apache.harmony.test.func.api.javax.swing.JComponent;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedJComponent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.JComponentReal;
import org.apache.harmony.test.func.api.javax.swing.share.Util;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class JComponentTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new JComponentTest().test(args));
    }

    public Result testStaticFields() {
        if (!"ToolTipText".equals(JComponent.TOOL_TIP_TEXT_KEY)) {
            return failed("expected JComponent.TOOL_TIP_TEXT_KEY to be 'ToolTipText' but got '"
                    + JComponent.TOOL_TIP_TEXT_KEY + "'");
        }
        int i = JComponent.UNDEFINED_CONDITION;
        if (JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT == JComponent.WHEN_FOCUSED) {
            return failed("JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT == JComponent.WHEN_FOCUSED");
        }
        if (JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT == JComponent.WHEN_IN_FOCUSED_WINDOW) {
            return failed("JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT  == JComponent.WHEN_IN_FOCUSED_WINDOW ");
        }
        if (JComponent.WHEN_FOCUSED == JComponent.WHEN_IN_FOCUSED_WINDOW) {
            return failed("JComponent.WHEN_FOCUSED == JComponent.WHEN_IN_FOCUSED_WINDOW ");
        }
        return passed();
    }

    public Result testConstructorSequence() {
        InstrumentedUILog.getLog().clear();

        InstrumentedJComponent ijc = new InstrumentedJComponent();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "awt.Component.addFocusListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                { "isManagingFocus" },
                { "firePropertyChange", "locale", null,
                        JComponent.getDefaultLocale() }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "awt.Component.addFocusListener",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "isManagingFocus" },
                        { "awt.Component.setLocale",
                                JComponent.getDefaultLocale() },
                        { "firePropertyChange", "locale", null,
                                JComponent.getDefaultLocale() }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "isManagingFocus" },
                        { "firePropertyChange", "locale", null,
                                JComponent.getDefaultLocale() }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "awt.Component.isVisible" },
                        { "awt.Component.setLocale",
                                JComponent.getDefaultLocale() },
                        { "firePropertyChange", "locale", null,
                                JComponent.getDefaultLocale() }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected JComponent constructor to call  "
                    + "isManagingFocus and firePropertyChange");
        }

        return passed();
    }

    public Result testFields() {
        JComponentReal jcr = new JComponentReal();

        if (jcr.getAccessibleContext() != null) {
            return failed("accessibleContext != null");
        }
        if (jcr.getListenerList() == null) {
            return failed("listenerList is null");
        }

        if (jcr.getUI() != null) {
            return failed("ui != null");
        }
        return passed();
    }

    public Result testAddNotify() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        JWindow window = new JWindow();

        InstrumentedUILog.clear();
        window.getContentPane().add(jc);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.getLog().isEmpty()) {
            return failed("adding component to content pane should cause no sequantial calls");
        }

        InstrumentedUILog.clear();
        window.addNotify();

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "addNotify" },
                { "awt.Component.getToolkit" },
                { "awt.Container.invalidate" },
                { "awt.Component.getFont" },
                { "awt.Component.getParent" },
                { "firePropertyChange", "ancestor", null,
                        window.getContentPane() },
                { "getNextFocusableComponent" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "addNotify" },
                        { "awt.Component.getToolkit" },
                        { "awt.Container.invalidate" },
                        { "isPreferredSizeSet" },
                        { "isMinimumSizeSet" },
                        { "isMaximumSizeSet" },
                        { "awt.Component.getFont" },
                        { "awt.Component.getParent" },
                        { "firePropertyChange", "ancestor", null,
                                window.getContentPane() },
                        { "getNextFocusableComponent" },
                        { "awt.Component.isLightweight" },
                        { "awt.Component.getPeer" },
                        { "awt.Component.isLightweight" },
                        { "awt.Component.getPeer" },
                        { "awt.Container.getComponentCount" },
                        { "awt.Container.countComponents" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "awt.Component.getPeer" },
                        /* 2 */{ "awt.Container.getComponentCount" },
                        /* 3 */{ "awt.Container.countComponents" },
                        /* 4 */{ "addNotify" },
                        /* 5 */{ "awt.Component.getToolkit" },
                        /* 6 */{ "awt.Container.invalidate" },
                        /* 7 */{ "isPreferredSizeSet" },
                        /* 8 */{ "isMinimumSizeSet" },
                        /* 9 */{ "isMaximumSizeSet" },
                        /* 10 */{ "awt.Component.getFont" },
                        /* 11 */{ "awt.Component.getParent" },
                        /* 12 */{ "firePropertyChange", "ancestor", null,
                                window.getContentPane() },
                        /* 13 */{ "getNextFocusableComponent" },
                        /* 14 */{ "awt.Component.getPeer" },
                        /* 15 */{ "awt.Container.getComponentCount" },
                        /* 16 */{ "awt.Container.countComponents" }, })
                && !InstrumentedUILog.contains(new Object[][] {
                        { "addNotify" },
                        { "firePropertyChange", "ancestor", null,
                                window.getContentPane() }, { "getListeners" },
                        { "awt.Component.getHierarchyListeners" }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected addNotify to call another sequence of events");
        }

        return passed();
    }

    public Result testRemoveNotify() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        JWindow window = new JWindow();

        InstrumentedUILog.clear();

        window.getContentPane().add(jc);

        InstrumentedUILog.clear();
        window.addNotify();

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "addNotify" },
                { "awt.Component.getToolkit" },
                { "awt.Container.invalidate" },
                { "awt.Component.getFont" },
                { "awt.Component.getParent" },
                { "firePropertyChange", "ancestor", null,
                        window.getContentPane() },
                { "getNextFocusableComponent" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "addNotify" },
                        { "awt.Component.getToolkit" },
                        { "awt.Container.invalidate" },
                        { "isPreferredSizeSet" },
                        { "isMinimumSizeSet" },
                        { "isMaximumSizeSet" },
                        { "awt.Component.getFont" },
                        { "awt.Component.getParent" },
                        { "firePropertyChange", "ancestor", null,
                                window.getContentPane() },
                        { "getNextFocusableComponent" },
                        { "awt.Component.isLightweight" },
                        { "awt.Component.getPeer" },
                        { "awt.Component.isLightweight" },
                        { "awt.Component.getPeer" },
                        { "awt.Container.getComponentCount" },
                        { "awt.Container.countComponents" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "awt.Component.getPeer" },
                        /* 2 */{ "awt.Container.getComponentCount" },
                        /* 3 */{ "awt.Container.countComponents" },
                        /* 4 */{ "addNotify" },
                        /* 5 */{ "awt.Component.getToolkit" },
                        /* 6 */{ "awt.Container.invalidate" },
                        /* 7 */{ "isPreferredSizeSet" },
                        /* 8 */{ "isMinimumSizeSet" },
                        /* 9 */{ "isMaximumSizeSet" },
                        /* 10 */{ "awt.Component.getFont" },
                        /* 11 */{ "awt.Component.getParent" },
                        /* 12 */{ "firePropertyChange", "ancestor", null,
                                window.getContentPane() },
                        /* 13 */{ "getNextFocusableComponent" },
                        /* 14 */{ "awt.Component.getPeer" },
                        /* 15 */{ "awt.Container.getComponentCount" },
                        /* 16 */{ "awt.Container.countComponents" }, }

                )

                && !InstrumentedUILog.contains(new Object[][] {
                        { "addNotify" },
                        { "firePropertyChange", "ancestor", null,
                                window.getContentPane() }, { "getListeners" },
                        { "awt.Component.getHierarchyListeners" }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected addNotify to call another sequence of events");
        }

        InstrumentedUILog.getLog().clear();

        window.getContentPane().remove(jc);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "removeNotify" },
                /* 2 */{ "awt.Component.getParent" },
                /* 3 */{ "awt.Component.isFocusOwner" },
                /* 4 */{ "awt.Component.hasFocus" },
                /* 5 */{ "awt.Component.getInputContext" },
                /* 6 */{ "awt.Component.getParent" },
                /* 7 */{ "firePropertyChange", "ancestor",
                        window.getContentPane(), null },
                /* 8 */{ "getNextFocusableComponent" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "removeNotify" },
                        /* 2 */// { "awt.Component.getParent" },
                        /* 3 */{ "awt.Component.isFocusOwner" },
                        /* 4 */{ "awt.Component.hasFocus" },
                        /* 5 */{ "awt.Component.getInputContext" },
                        /* 6 */{ "awt.Component.getParent" },
                        /* 7 */{ "firePropertyChange", "ancestor",
                                window.getContentPane(), null },
                        /* 8 */{ "getNextFocusableComponent" }, })
                && !InstrumentedUILog.equals(new Object[][] {

                        /* 1 */{ "awt.Component.isShowing" },
                        /* 2 */{ "awt.Component.isVisible" },
                        /* 3 */{ "awt.Component.isDisplayable" },
                        /* 4 */{ "awt.Component.isDisplayable" },
                        /* 5 */{ "awt.Component.isDisplayable" },
                        /* 6 */{ "removeNotify" },
                        /* 7 */{ "awt.Component.getParent" },
                        /* 8 */{ "firePropertyChange", "ancestor",
                                window.getContentPane(), null },
                        /* 9 */{ "awt.Component.isFocusOwner" },
                        /* 10 */{ "awt.Component.getParent" },
                        /* 11 */{ "awt.Component.getBounds" },
                        /* 12 */{ "awt.Component.bounds" },
                        /* 13 */{ "awt.Component.getInputContext" },
                        /* 14 */{ "awt.Component.getParent" },
                        /* 15 */{ "lang.object equals" },
                        /* 16 */{ "awt.Component.isShowing" },
                        /* 17 */{ "awt.Component.isVisible" },
                        /* 18 */{ "awt.Component.isDisplayable" },
                        /* 19 */{ "awt.Component.isDisplayable" },
                        /* 20 */{ "awt.Component.getToolkit" },
                        /* 21 */{ "getListeners" },
                        /* 22 */{ "awt.Component.getHierarchyListeners" },

                })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected window.getContentPane().remove(jc) to another sequence of events");
        }

        return passed();
    }

    public Result testContains() {
        JComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        if (jc.contains(0, 0)) {
            return failed("expected newly created JComponent not to contain (0, 0)");
        }

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {

        /* 1 */{ "contains", "0 0" },
        /* 2 */{ "awt.Component.inside", "0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {

                /* 1 */{ "contains", "0 0" },
                /* 2 */{ "awt.Component.inside", "0 0" },
                /* 3 */{ "getWidth" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected contains(int, int) to call awt.Component.inside(int, int)");
        }

        if (jc.contains(1, 1)) {
            return failed("expected newly created JComponent not to contain (1, 1)");
        }

        InstrumentedUILog.getLog().clear();

        jc.setLocation(-1, -1);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                { "setLocation", "-1 -1" }, { "awt.Component.move", "-1 -1" },
                { "awt.Component.setBounds", "-1 -1 0 0" },
                { "reshape", "-1 -1 0 0" }, { "awt.Component.isShowing" }, })
                && !InstrumentedUILog.contains(new Object[][] {
                        { "setLocation", "-1 -1" },
                        { "awt.Component.move", "-1 -1" },
                        { "awt.Component.setBounds", "-1 -1 0 0" },
                        { "reshape", "-1 -1 0 0" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setLocation() to fire awt.Component.move, "
                    + "awt.Component.setBounds, reshape, + optionally awt.Component.isShowing");
        }

        if (jc.contains(-1, -1)) {
            return failed("JComponent cannot contain (-1, -1)");
        }

        return passed();
    }

    public Result testEnabled() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        if (!jc.isEnabled()) {
            return failed("expected default isEnabled to be true");
        }

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected isEnabled() not to call any more public/private properties");
        }

        InstrumentedUILog.getLog().clear();
        jc.setEnabled(false);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "setEnabled", "false" },
        /* 2 */{ "isEnabled" },
        /* 3 */{ "awt.Component.enable", "false" },
        /* 4 */{ "disable" },
        /* 5 */{ "isEnabled" },
        /* 6 */{ "awt.Component.getParent" },
        /* 7 */{ "awt.Component.isFocusOwner" },
        /* 8 */{ "awt.Component.hasFocus" },
        /* 9 */{ "firePropertyChangeBoolean", "enabled", "true", "false" },
        /* 10 */{ "awt.Component.repaint" },
        /* 11 */{ "repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "setEnabled", "false" },
                        /* 2 */{ "isEnabled" },
                        /* 3 */{ "awt.Component.enable", "false" },
                        /* 4 */{ "disable" },
                        /* 5 */{ "isEnabled" },
                        /* 6 *///{ "awt.Component.getParent" },
                        /* 7 */{ "awt.Component.isFocusOwner" },
                        /* 8 */{ "awt.Component.hasFocus" },
                        /* 9 */{ "firePropertyChangeBoolean", "enabled",
                                "true", "false" },
                        /* 10 */{ "awt.Component.repaint" },
                        /* 11 */{ "repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "setEnabled", "false" },
                        /* 2 */{ "isEnabled" },
                        /* 3 */{ "awt.Component.enable", "false" },
                        /* 4 */{ "disable" },
                        /* 5 */{ "awt.Component.isDisplayable" },
                        /* 6 */{ "awt.Component.isFocusOwner" },
                        /* 7 */{ "firePropertyChangeBoolean", "enabled",
                                "true", "false" },
                        /* 8 */{ "firePropertyChange", "enabled",
                                Boolean.TRUE, Boolean.FALSE }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setEnabled(false) to call another sequence of events");
        }

        if (jc.isEnabled()) {
            return failed("expected isEnabled to be false");
        }

        InstrumentedUILog.getLog().clear();

        jc.setEnabled(true);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "setEnabled", "true" }, { "isEnabled" },
                { "awt.Component.enable", "true" }, { "enable" },
                { "isEnabled" },
                { "firePropertyChangeBoolean", "enabled", "false", "true" },
                { "awt.Component.repaint" }, { "repaint", "0 0 0 0 0" },

        })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "setEnabled", "true" },
                        /* 2 */{ "isEnabled" },
                        /* 3 */{ "awt.Component.enable", "true" },
                        /* 4 */{ "enable" },
                        /* 5 */{ "awt.Component.isDisplayable" },
                        /* 6 */{ "firePropertyChangeBoolean", "enabled",
                                "false", "true" },
                        /* 7 */{ "firePropertyChange", "enabled", "false",
                                "true" },

                })

        )

        {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setEnabled(true) to call isEnabled,  "
                    + "awt.Component.enable, disable, isEnabled "
                    + "firePropertyChangeBoolean, awt.Component.repaint, repaint");
        }

        if (!jc.isEnabled()) {
            return failed("expected isEnabled to be true");
        }

        InstrumentedUILog.getLog().clear();
        jc.disable();
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "disable" },
        /* 2 */{ "isEnabled" },
        /* 3 */{ "awt.Component.getParent" },
        /* 4 */{ "awt.Component.isFocusOwner" },
        /* 5 */{ "awt.Component.hasFocus" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "disable" },
                /* 2 */{ "isEnabled" },
                /* 3 */// { "awt.Component.getParent" },
                        /* 4 */{ "awt.Component.isFocusOwner" },
                        /* 5 */{ "awt.Component.hasFocus" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "disable" },
                /* 2 */{ "awt.Component.isDisplayable" },
                /* 3 */{ "awt.Component.isFocusOwner" }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected disable() to call another sequence of events");
        }

        if (jc.isEnabled()) {
            return failed("expected isEnabled to be false");
        }

        InstrumentedUILog.getLog().clear();
        jc.enable();
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "enable" },
        /* 2 */{ "isEnabled" }, }) &&

        !InstrumentedUILog.equals(new Object[][] {

        /* 1 */{ "enable" },
        /* 2 */{ "awt.Component.isDisplayable" },

        })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected enable() to call isEnabled");
        }

        if (!jc.isEnabled()) {
            return failed("expected isEnabled to be true");
        }

        return passed();
    }

    public Result testActionMap() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        InstrumentedUILog.getLog().clear();

        class InnerActionMap extends ActionMap {
            int code;

            InnerActionMap(int c) {
                code = c;
            }

            public boolean equals(Object arg0) {
                if (arg0 == null) {
                    return false;
                }
                if (!(arg0 instanceof InnerActionMap)) {
                    return false;
                }
                return code == ((InnerActionMap) arg0).code;
            }
        }

        if (jc.getActionMap() == null) {
            return failed("expected basic action map to be not null");
        }

        ActionMap am = new InnerActionMap(1);

        jc.setActionMap(am);

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.getLog().isEmpty()) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setActionMap not to call any more public/protected methods");
        }

        if (jc.getActionMap() != am) {
            return failed("expected getActionMap() return what was set using setActionMap(...)");
        }

        ActionMap am1 = new InnerActionMap(1);

        jc.setActionMap(am1);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.getLog().isEmpty()) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setActionMap not to call any more public/protected methods");
        }

        if (jc.getActionMap() != am1) {
            return failed("expected getActionMap() return what was set using setActionMap(...)");
        }

        jc.setActionMap(null);
        if (!InstrumentedUILog.getLog().isEmpty()) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setActionMap not to call any more public/protected methods");
        }

        if (jc.getActionMap() != null) {
            return failed("expected getActionMap() return null after setActionMap(null)");
        }

        return passed();
    }

    public Result testInputMap() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        InstrumentedUILog.getLog().clear();

        class InnerInputMap extends InputMap {
            int code;

            InnerInputMap(int c) {
                code = c;
            }

            public boolean equals(Object arg0) {
                if (arg0 == null) {
                    return false;
                }
                if (!(arg0 instanceof InnerInputMap)) {
                    return false;
                }
                return code == ((InnerInputMap) arg0).code;
            }
        }

        if (jc.getInputMap() == null) {
            return failed("expected basic action map to be not null");
        }

        InputMap im = new InnerInputMap(1);

        try {
            jc.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, im);
            return failed("expected IllegalArgumentException in setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, ...)");
        } catch (IllegalArgumentException e) {
        }

        jc.setInputMap(JComponent.WHEN_FOCUSED, im);

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.getLog().isEmpty()) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setInputMap not to call any more public/protected methods");
        }

        if (jc.getInputMap() != im) {
            return failed("expected getInputMap() return what was set using setInputMap(...)");
        }

        InputMap im1 = new InnerInputMap(1);

        jc.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, im1);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.getLog().isEmpty()) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setInputMap not to call any more public/protected methods");
        }

        if (jc.getInputMap() != im) {
            return failed("expected getInputMap() return what was set using setActionMap(JComponent.WHEN_FOCUSED, ...)");
        }

        jc.setInputMap(JComponent.WHEN_FOCUSED, im1);
        if (jc.getInputMap() != im1) {
            return failed("expected getInputMap() return what was set using setActionMap(JComponent.WHEN_FOCUSED, ...)");
        }

        jc.setInputMap(JComponent.WHEN_FOCUSED, null);
        if (!InstrumentedUILog.getLog().isEmpty()) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setInputMap not to call any more public/protected methods");
        }

        if (jc.getInputMap() != null) {
            return failed("expected getInputMap() return null after setInputMap(JComponent.WHEN_FOCUSED, null)");
        }

        return passed();
    }

    public Result testInputVerifier() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        InstrumentedUILog.getLog().clear();

        class InnerInputVerifier extends InputVerifier {
            int code;

            InnerInputVerifier(int c) {
                code = c;
            }

            public boolean equals(Object arg0) {
                if (arg0 == null) {
                    return false;
                }
                if (!(arg0 instanceof InnerInputVerifier)) {
                    return false;
                }
                return code == ((InnerInputVerifier) arg0).code;
            }

            public boolean verify(JComponent arg0) {
                return false;
            }
        }

        if (jc.getInputVerifier() != null) {
            return failed("expected basic verifier to be null");
        }

        InputVerifier iv = new InnerInputVerifier(1);
        InstrumentedUILog.getLog().clear();

        jc.setInputVerifier(iv);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "setInputVerifier", iv },
                { "firePropertyChange", "InputVerifier", null, iv }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "setInputVerifier", iv },
                        { "firePropertyChange", "InputVerifier", null, iv },
                        { "firePropertyChange", "inputVerifier", null, iv }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "setInputVerifier", iv },
                        { "firePropertyChange", "inputVerifier", null, iv }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setInputVerifier 1 to call firePropertyChange");
        }

        if (jc.getInputVerifier() != iv) {
            return failed("expected getInputVerifier() return what was set using setInputVerifier(...)");
        }

        InputVerifier iv1 = new InnerInputVerifier(1);

        InstrumentedUILog.getLog().clear();

        jc.setInputVerifier(iv1);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "setInputVerifier", iv1 },
                { "firePropertyChange", "InputVerifier", iv, iv1 }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "setInputVerifier", iv1 },
                        { "firePropertyChange", "InputVerifier", iv, iv1 },
                        { "firePropertyChange", "inputVerifier", iv, iv1 }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "setInputVerifier", iv1 },
                        { "firePropertyChange", "inputVerifier", iv, iv1 }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setInputVerifier 2 to call firePropertyChange");
        }

        if (jc.getInputVerifier() != iv1) {
            return failed("expected getInputVerifier() return what was set using setInputVerifier(...)");
        }

        InstrumentedUILog.getLog().clear();
        jc.setInputVerifier(null);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "setInputVerifier", null },
                { "firePropertyChange", "InputVerifier", iv1, null }, })
                && !InstrumentedUILog
                        .equals(new Object[][] {
                                { "setInputVerifier", null },
                                { "firePropertyChange", "InputVerifier", iv1,
                                        null },
                                { "firePropertyChange", "inputVerifier", iv1,
                                        null }, })
                && !InstrumentedUILog
                        .equals(new Object[][] {
                                { "setInputVerifier", null },
                                { "firePropertyChange", "inputVerifier", iv1,
                                        null }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setInputVerifier (null) to call firePropertyChange");
        }

        if (jc.getInputVerifier() != null) {
            return failed("expected getInputVerifier() return null after setInputVerifier(null)");
        }

        return passed();
    }

    public Result testVerifyInputWhenFocusTarget() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        InstrumentedUILog.getLog().clear();

        if (!jc.getVerifyInputWhenFocusTarget()) {
            return failed("expected default getVerifyInputWhenFocusTarget to return true");
        }

        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 1) {
            return failed("expected getVerifyInputWhenFocusTarget not to call any additional methods");
        }
        InstrumentedUILog.getLog().clear();

        jc.setVerifyInputWhenFocusTarget(false);

        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 2) {
            return failed("expected setVerifyInputWhenFocusTarget to call firePropertyChange(String, boolean, boolean)");
        }
        Object[] oarr = (Object[]) InstrumentedUILog.getLog().get(0);
        if (!oarr[0].equals("setVerifyInputWhenFocusTarget")
                || !oarr[1].equals("false")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVerifyInputWhenFocusTarget to call firePropertyChange(String, boolean, boolean)");
        }
        oarr = (Object[]) InstrumentedUILog.getLog().get(1);
        if (!oarr[0].equals("firePropertyChangeBoolean")
                || !oarr[1].equals("verifyInputWhenFocusTarget")
                || !oarr[2].equals("true") || !oarr[3].equals("false")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVerifyInputWhenFocusTarget to call firePropertyChange(String, boolean, boolean) 3");
        }
        InstrumentedUILog.getLog().clear();

        jc.setVerifyInputWhenFocusTarget(false);
        Util.waitQueueEventsProcess();
        oarr = (Object[]) InstrumentedUILog.getLog().get(0);
        if (!oarr[0].equals("setVerifyInputWhenFocusTarget")
                || !oarr[1].equals("false")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVerifyInputWhenFocusTarget to call firePropertyChange(String, boolean, boolean)");
        }
        oarr = (Object[]) InstrumentedUILog.getLog().get(1);
        if (!oarr[0].equals("firePropertyChangeBoolean")
                || !oarr[1].equals("verifyInputWhenFocusTarget")
                || !oarr[2].equals("false") || !oarr[3].equals("false")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVerifyInputWhenFocusTarget to call firePropertyChange(String, boolean, boolean)");
        }
        InstrumentedUILog.getLog().clear();

        jc.setVerifyInputWhenFocusTarget(true);
        Util.waitQueueEventsProcess();
        oarr = (Object[]) InstrumentedUILog.getLog().get(0);
        if (!oarr[0].equals("setVerifyInputWhenFocusTarget")
                || !oarr[1].equals("true")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVerifyInputWhenFocusTarget to call firePropertyChange(String, boolean, boolean)");
        }
        oarr = (Object[]) InstrumentedUILog.getLog().get(1);
        if (!oarr[0].equals("firePropertyChangeBoolean")
                || !oarr[1].equals("verifyInputWhenFocusTarget")
                || !oarr[2].equals("false") || !oarr[3].equals("true")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVerifyInputWhenFocusTarget to call firePropertyChange(String, boolean, boolean)");
        }
        InstrumentedUILog.getLog().clear();

        return passed();
    }

    public Result testDoubleBuffered() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        InstrumentedUILog.getLog().clear();

        if (jc.isDoubleBuffered()) {
            return failed("expected default isDoubleBuffered to return false");
        }

        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 1) {
            return failed("expected isDoubleBuffered not to call any additional methods");
        }

        InstrumentedUILog.getLog().clear();
        jc.setDoubleBuffered(false);
        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setDoubleBuffered not to call any other public/protected methods");
        }

        InstrumentedUILog.getLog().clear();
        jc.setDoubleBuffered(true);
        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setDoubleBuffered not to call any other public/protected methods");
        }

        return passed();
    }

    public Result testToolTipText() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        InstrumentedUILog.getLog().clear();

        if (jc.getToolTipText() != null) {
            return failed("expected basic tooltiptext to be null");
        }

        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getToolTipText not to call any public/protected methods");
        }

        InstrumentedUILog.getLog().clear();
        jc.setToolTipText("hello world");
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                { "setToolTipText", "hello world" },
                { "getToolTipText" },
                { "firePropertyChange", JComponent.TOOL_TIP_TEXT_KEY, null,
                        "hello world" },
                { "awt.Component.removeMouseListener",
                        javax.swing.ToolTipManager.sharedInstance() },
                { "awt.Component.addMouseListener",
                        javax.swing.ToolTipManager.sharedInstance() },
                { "awt.Component.removeMouseMotionListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                { "awt.Component.addMouseMotionListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },

        })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "setToolTipText", "hello world" },
                        /* 2 */{ "firePropertyChange", "ToolTipText", null,
                                "hello world" },
                        /* 3 */{ "awt.Component.addMouseMotionListener",
                                javax.swing.ToolTipManager.sharedInstance() },
                        /* 4 */{ "awt.Component.addMouseListener",
                                javax.swing.ToolTipManager.sharedInstance() },

                }))

        {
            InstrumentedUILog.printLogAsArray();
            return failed("expected first setToolTipText to call another sequence of events");

        }

        InstrumentedUILog.getLog().clear();
        jc.setToolTipText("hello world");
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                { "setToolTipText", "hello world" },
                { "getToolTipText" },
                { "firePropertyChange", JComponent.TOOL_TIP_TEXT_KEY, "hello world",
                        "hello world" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected second setToolTipText to call getToolTipText, firePropertyChange ");

        }

        InstrumentedUILog.getLog().clear();
        jc.setToolTipText(null);
        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 5
                || !Arrays.equals(new Object[] { "setToolTipText", null },
                        (Object[]) InstrumentedUILog.getLog().get(0))
                || !Arrays.equals(new Object[] { "getToolTipText" },
                        (Object[]) InstrumentedUILog.getLog().get(1))
                || !Arrays.equals(new Object[] { "firePropertyChange",
                        JComponent.TOOL_TIP_TEXT_KEY, "hello world", null },
                        (Object[]) InstrumentedUILog.getLog().get(2))
                || !Arrays.equals(new Object[] {
                        "awt.Component.removeMouseListener",
                        javax.swing.ToolTipManager.sharedInstance() },
                        (Object[]) InstrumentedUILog.getLog().get(3))
                || !"awt.Component.removeMouseMotionListener"
                        .equals(((Object[]) InstrumentedUILog.getLog().get(4))[0])) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setToolTipText to call getToolTipText, firePropertyChange, "
                    + "awt.Component.removeMouseListener, awt.Component.removeMouseMotionListener");

        }

        return passed();
    }

    public Result testAlignmentX() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        if (jc.getAlignmentX() != Component.CENTER_ALIGNMENT) {
            return failed("expected default X alignment to be Component.CENTER_ALIGNMENT but it is "
                    + jc.getAlignmentX());
        }

        InstrumentedUILog.getLog().clear();

        jc.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setAlignmentX not to fire any additional events");
        }

        if (jc.getAlignmentX() != Component.LEFT_ALIGNMENT) {
            return failed("expected alignment after set to be Component.LEFT_ALIGNMENT");
        }

        jc.setAlignmentX(Component.RIGHT_ALIGNMENT);
        if (jc.getAlignmentX() != Component.RIGHT_ALIGNMENT) {
            return failed("expected alignment after set to be Component.RIGHT_ALIGNMENT");
        }

        jc.setAlignmentX((float) 0.75);
        if (jc.getAlignmentX() != (float) 0.75) {
            return failed("expected alignment after set 0.75 to be 0.75");
        }

        jc.setAlignmentX((float) -1);
        if (jc.getAlignmentX() != 0) {
            return failed("expected alignment after set -1 to be 0 but it is "
                    + jc.getAlignmentX());
        }

        jc.setAlignmentX((float) 2);
        if (jc.getAlignmentX() != 1) {
            return failed("expected alignment after set 2 to be 1");
        }

        jc.setAlignmentX(Float.NaN);
        if (!Float.isNaN(jc.getAlignmentX()) && jc.getAlignmentX() != 0) {
            return failed("expected alignment after set NaN to be NaN or 0 but it is "
                    + jc.getAlignmentX());
        }

        jc.setAlignmentX(Float.POSITIVE_INFINITY);
        if (jc.getAlignmentX() != 1) {
            return failed("expected alignment after setting Float.POSITIVE_INFINITY to be 1");
        }

        return passed();
    }

    public Result testAlignmentY() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        if (jc.getAlignmentY() != Component.CENTER_ALIGNMENT) {
            return failed("expected default Y alignment to be Component.CENTER_ALIGNMENT but it is "
                    + jc.getAlignmentY());
        }

        InstrumentedUILog.getLog().clear();
        jc.setAlignmentY(Component.TOP_ALIGNMENT);
        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setAlignmentY not to fire any additional events");
        }

        if (jc.getAlignmentY() != Component.TOP_ALIGNMENT) {
            return failed("expected alignment after set to be Component.TOP_ALIGNMENT");
        }

        jc.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        if (jc.getAlignmentY() != Component.BOTTOM_ALIGNMENT) {
            return failed("expected alignment after set to be Component.BOTTOM_ALIGNMENT");
        }

        jc.setAlignmentY((float) 0.75);
        if (jc.getAlignmentY() != (float) 0.75) {
            return failed("expected alignment after set 0.75 to be 0.75");
        }

        jc.setAlignmentY((float) -1);
        if (jc.getAlignmentY() != 0) {
            return failed("expected alignment after set -1 to be 0 but it is "
                    + jc.getAlignmentY());
        }

        jc.setAlignmentY((float) 2);
        if (jc.getAlignmentY() != 1) {
            return failed("expected alignment after set 2 to be 1");
        }

        jc.setAlignmentY(Float.NaN);
        if (!Float.isNaN(jc.getAlignmentY()) && jc.getAlignmentY() != 0) {
            return failed("expected alignment after set NaN to be NaN or 0 but it is "
                    + jc.getAlignmentY());
        }

        jc.setAlignmentY(Float.POSITIVE_INFINITY);
        if (jc.getAlignmentY() != 1) {
            return failed("expected alignment after setting Float.POSITIVE_INFINITY to be 1");
        }

        return passed();
    }

    public Result testBorder() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        if (jc.getBorder() != null) {
            return failed("expected default border to be null");
        }

        Border border = BorderFactory.createLineBorder(Color.MAGENTA);

        InstrumentedUILog.clear();
        jc.setBorder(border);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.contains(new Object[][] {
                { "setBorder", border },
                { "firePropertyChange", "border", null, border }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setBorder to call firePropertyChange ");
        }

        if (jc.getBorder() != border) {
            return failed("expected getBorder() to return border previously set by setBorder()");
        }

        return passed();
    }

    public Result testClientProperty() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        jc.putClientProperty("abcd", "def");

        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected putClientProperty to call firePropertyChange");
        }

        Object[] oarr = (Object[]) InstrumentedUILog.getLog().get(0);
        if (!"firePropertyChange".equals(oarr[0])) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected putClientProperty to call firePropertyChange");
        }
        if (!"abcd".equals(oarr[1]) || oarr[2] != null
                || !"def".equals(oarr[3])) {
            InstrumentedUILog.printLogAsArray();
            return failed("improper call of firePropertyChange on putClientProperty");
        }

        InstrumentedUILog.getLog().clear();

        if (!"def".equals(jc.getClientProperty("abcd"))) {
            return failed("getClientProperty to return previously set value('def') but got "
                    + jc.getClientProperty("abcd"));
        }
        if (jc.getClientProperty("ABCD") != null) {
            return failed("expected getClientProperty(not previously set value) to return null  but got "
                    + jc.getClientProperty("ABCD"));
        }

        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 0) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getClientProperty not to call any public/protected methods");
        }

        jc.putClientProperty("abcd", null);
        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected putClientProperty to call firePropertyChange");
        }

        oarr = (Object[]) InstrumentedUILog.getLog().get(0);
        if (!"firePropertyChange".equals(oarr[0])) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected putClientProperty to call firePropertyChange");
        }
        if (!"abcd".equals(oarr[1]) || !"def".equals(oarr[2])
                || oarr[3] != null) {
            InstrumentedUILog.printLogAsArray();
            return failed("improper call of firePropertyChange on putClientProperty");
        }

        if (jc.getClientProperty("abcd") != null) {
            return failed("expected getClientProperty(...) to return null after setClientProperty(..., null)  but got "
                    + jc.getClientProperty("abcd"));
        }

        return passed();
    }

    public Result testLocale() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        JComponent.getDefaultLocale();

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.getLog().isEmpty()) {
            return failed("expected no events by now");
        }

        if (jc.getLocale() != JComponent.getDefaultLocale()) {
            return failed("expected JComponent locale to be default locale");
        }

        Locale locale = new Locale("es");

        InstrumentedUILog.getLog().clear();

        jc.setLocale(locale);

        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 2
                || !Arrays.equals(new Object[] { "awt.Component.setLocale",
                        locale }, (Object[]) InstrumentedUILog.getLog().get(0))
                || !Arrays.equals(new Object[] { "firePropertyChange",
                        "locale", JComponent.getDefaultLocale(), locale },
                        (Object[]) InstrumentedUILog.getLog().get(1))

        ) {
            return failed("expected setLocale to call firePropertyChange");
        }

        if (jc.getLocale() != locale) {
            return failed("expected getLocale() to return what was set by setLocale(..)");
        }

        JComponent.setDefaultLocale(locale);

        if (new JComponent() {
        }.getLocale() != locale) {
            return failed("expected getLocale() of a newly created JComponent returnw what was set by "
                    + " JComponent.setDefaultLocale()");
        }

        return passed();
    }

    public Result testHeightWidth() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        if (jc.getHeight() != 0) {
            return failed("expected  getHeight() of an empty component to return 0");
        }

        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected  getHeight() of an empty component not to call any additional methods");
        }

        InstrumentedUILog.getLog().clear();

        if (jc.getWidth() != 0) {
            return failed("expected  getWidth() of an empty component to return 0");
        }

        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected  getWidth() of an empty component not to call any additional methods");
        }

        return passed();
    }

    public Result testXY() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        if (jc.getX() != 0) {
            return failed("expected getX() of an empty component to return 0");
        }

        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getX() of an empty component not to call any additional methods");
        }

        InstrumentedUILog.getLog().clear();

        if (jc.getY() != 0) {
            return failed("expected getY() of an empty component to return 0");
        }

        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getY() of an empty component not to call any additional methods");
        }

        return passed();
    }

    public Result testManagingFocus() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();
        if (jc.isManagingFocus()) {
            return failed("expected default isManagingFocus to be false");
        }

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected isManagingFocus not to call any other public/protected methods");
        }

        return passed();
    }

    public Result testOptimizedDrawing() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();
        if (!jc.isOptimizedDrawingEnabled()) {
            return failed("expected default isOptimizedDrawingEnabled to return true");
        }

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected isOptimizedDrawingEnabled not to call any other public/protected methods");
        }

        return passed();
    }

    public Result testOpaque() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();
        if (jc.isOpaque()) {
            return failed("expected default isOpaque to be false");
        }

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected isOpaque not to call any other public/protected methods");
        }

        InstrumentedUILog.getLog().clear();

        jc.setOpaque(false);
        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 2) {
            return failed("expected setOpaque to call firePropertyChange(String, boolean, boolean)");
        }
        Object[] oarr = (Object[]) InstrumentedUILog.getLog().get(0);
        if (!oarr[0].equals("setOpaque") || !oarr[1].equals("false")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setOpaque to call firePropertyChange(String, boolean, boolean)");
        }
        oarr = (Object[]) InstrumentedUILog.getLog().get(1);
        if (!oarr[0].equals("firePropertyChangeBoolean")
                || !oarr[1].equals("opaque") || !oarr[2].equals("false")
                || !oarr[3].equals("false")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setOpaque to call firePropertyChange(String, boolean, boolean)");
        }
        InstrumentedUILog.getLog().clear();

        jc.setOpaque(true);
        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 2) {
            return failed("expected setOpaque to call firePropertyChange(String, boolean, boolean)");
        }
        oarr = (Object[]) InstrumentedUILog.getLog().get(0);
        if (!oarr[0].equals("setOpaque") || !oarr[1].equals("true")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setOpaque to call firePropertyChange(String, boolean, boolean)");
        }
        oarr = (Object[]) InstrumentedUILog.getLog().get(1);
        if (!oarr[0].equals("firePropertyChangeBoolean")
                || !oarr[1].equals("opaque") || !oarr[2].equals("false")
                || !oarr[3].equals("true")) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setOpaque to call firePropertyChange(String, boolean, boolean)");
        }
        InstrumentedUILog.getLog().clear();

        return passed();
    }

    public Result testRequestFocusEnabled() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();
        if (!jc.isRequestFocusEnabled()) {
            return failed("expected default isRequestFocusEnabled to be true");
        }

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected isRequestFocusEnabled not to call any other public/protected methods");
        }

        InstrumentedUILog.getLog().clear();

        jc.setRequestFocusEnabled(true);
        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setRequestFocusEnabled not to call any other public/protected methods");
        }
        InstrumentedUILog.getLog().clear();

        jc.setRequestFocusEnabled(false);
        Util.waitQueueEventsProcess();
        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setRequestFocusEnabled not to call any other public/protected methods");
        }
        InstrumentedUILog.getLog().clear();

        return passed();
    }

    public Result testIsValidateRoot() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        if (jc.isValidateRoot()) {
            return failed("expected default isValidateRoot to be false");
        }
        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 1) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected isValidateRoot not to call any other public/protected methods");
        }

        return passed();
    }

    public Result testPaintImmediately() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.clear();

        jc.paintImmediately(0, 0, 1, 1);
        Util.waitQueueEventsProcess();

        //TODO: test case when isShowing() returns true
        if (!InstrumentedUILog.equals(new Object[][] {
                { "paintImmediately", "0 0 1 1" },
                { "awt.Component.isShowing" } })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "paintImmediately", "0 0 1 1" },
                /* 2 */{ "getVisibleRect" },
                /* 3 */{ "getWidth" },
                /* 4 */{ "getHeight" },
                /* 5 */{ "awt.Component.getParent" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected paintImmediately() to call awt.Component.isShowing");
        }

        return passed();
    }

    public Result testRepaint() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        jc.repaint(0, 0, 0, 1, 1);

        Util.waitQueueEventsProcess();

        if (

        //                InstrumentedUILog.getLog().size() != 2
        //                || !Arrays.equals(new Object[] { "repaint", "0 0 0 1 1" },
        //                        (Object[]) InstrumentedUILog.getLog().get(0))
        //                || !Arrays.equals(new Object[] { "getWidth" },
        //                        (Object[]) InstrumentedUILog.getLog().get(1))

        !InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "repaint", "0 0 0 1 1" },
        /* 2 */{ "getVisibleRect" },
        /* 3 */{ "getWidth" },
        /* 4 */{ "getHeight" },
        /* 5 */{ "awt.Component.getParent" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "repaint", "0 0 0 1 1" },
                /* 2 */{ "getWidth" }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected repaint() to call getWidth only when getWidth returns 0");
        }

        //if getWidth() returns 0, there will be no further calls
        //otherwise getHeight() will be called
        jc = new InstrumentedJComponent() {
            public int getWidth() {
                super.getWidth();
                return 1;
            }

            public int getHeight() {
                super.getHeight();
                return 1;
            }
        };

        InstrumentedUILog.getLog().clear();

        jc.repaint(0, 0, 0, 1, 1);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "repaint", "0 0 0 1 1" },
        /* 2 */{ "getVisibleRect" },
        /* 3 */{ "getWidth" },
        /* 4 */{ "getHeight" },
        /* 5 */{ "awt.Component.getPeer" },

        })

        && !InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "repaint", "0 0 0 1 1" },
        /* 2 */{ "getVisibleRect" },
        /* 3 */{ "getWidth" },
        /* 4 */{ "getHeight" },
        /* 5 */{ "awt.Component.getParent" }, })

        && !InstrumentedUILog.equals(new Object[][] {
                { "repaint", "0 0 0 1 1" },
                { "getWidth" },
                { "getHeight" },
                { "awt.Component.isVisible" },
                { "awt.Component.getPeer" }, 
                })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected repaint() to another sequence of events when getWidth() != 0");
        }

        return passed();
    }

    public Result testRequestFocusInWindow() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        if (jc.requestFocusInWindow()) {
            return failed("expected requestFocusInWindow to return false");
        }
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "requestFocusInWindow" },
        /* 2 */{ "getVerifyInputWhenFocusTarget" },
        /* 3 */{ "awt.Component.isFocusable" },
        /* 4 */{ "awt.Component.isFocusTraversable" },
        /* 5 */{ "awt.Component.isVisible" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "requestFocusInWindow" },
                /* 2 */{ "getVerifyInputWhenFocusTarget" },
                /* 3 */{ "requestFocusInWindow", "false" },
                /* 4 */{ "getVerifyInputWhenFocusTarget" },
                /* 5 */{ "awt.Component.getParent" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected requestFocusInWindow() to call getVerifyInputWhenFocusTarget, "
                    + "awt.Component.isFocusable, awt.Component.isFocusTraversable, awt.Component.isVisible");
        }

        return passed();
    }

    public Result testRequestFocus() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();
        jc.requestFocus();
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] { { "requestFocus" },
                { "getVerifyInputWhenFocusTarget" },
                { "awt.Component.isFocusable" },
                { "awt.Component.isFocusTraversable" },
                { "awt.Component.isVisible" }, })

                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "requestFocus" },
                /* 2 */{ "getVerifyInputWhenFocusTarget" },
                /* 3 */{ "requestFocus", "false" },
                /* 4 */{ "getVerifyInputWhenFocusTarget" },
                /* 5 */{ "awt.Component.isFocusOwner" },
                /* 6 */{ "awt.Component.getParent" },
                /* 7 */{ "awt.Component.isShowing" },
                /* 8 */{ "awt.Component.isVisible" },
                /* 9 */{ "awt.Component.isDisplayable" },

                })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected requestFocus() to call another sequence of events");
        }

        InstrumentedUILog.getLog().clear();
        if (jc.requestFocus(false)) {
            return failed("expected requestFocus(false) to return false");
        }
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                { "requestFocus", "false" },
                { "getVerifyInputWhenFocusTarget" },
                { "awt.Component.isFocusable" },
                { "awt.Component.isFocusTraversable" },
                { "awt.Component.isVisible" }, }

        )
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "requestFocus", "false" },
                /* 2 */{ "getVerifyInputWhenFocusTarget" },
                /* 3 */{ "awt.Component.isFocusOwner" },
                /* 4 */{ "awt.Component.getParent" },
                /* 5 */{ "awt.Component.isShowing" },
                /* 6 */{ "awt.Component.isVisible" },
                /* 7 */{ "awt.Component.isDisplayable" }, }

                )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected requestFocus() to call getVerifyInputWhenFocusTarget, "
                    + "awt.Component.isFocusable, awt.Component.isFocusTraversable, awt.Component.isVisible");
        }

        return passed();
    }

    public Result testReshape() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        jc.reshape(0, 0, 2, 2);

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
                { "reshape", "0 0 2 2" }, { "awt.Component.isShowing" }, })
                && !InstrumentedUILog.equals(new Object[][] { { "reshape",
                        "0 0 2 2" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "reshape", "0 0 2 2" },
                /* 2 */{ "awt.Container.invalidate" },
                /* 3 */{ "awt.Component.getParent" },
                /* 4 */{ "awt.Component.getToolkit" },
                /* 5 */{ "awt.Component.getParent" },
                /* 6 */{ "awt.Container.getComponentCount" },
                /* 7 */{ "awt.Container.countComponents" },
                /* 8 */{ "getListeners" },
                /* 9 */{ "awt.Component.getComponentListeners" },

                })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected reshape() to call another sequence of events");
        }

        return passed();
    }

    public Result testRevalidate() {
        InstrumentedJComponent jc = new InstrumentedJComponent();
        InstrumentedJComponent jcValidateRoot = new InstrumentedJComponent() {
            public boolean isValidateRoot() {
                return true;
            }
        };

        InstrumentedUILog.getLog().clear();

        jc.revalidate();

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog
                .contains(new Object[][] { { "awt.Component.getParent" } })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected revalidate() with null parent to call awt.Component.getParent");
        }

        jcValidateRoot.add(jc);
        InstrumentedUILog.getLog().clear();
        jc.revalidate();
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "revalidate" },
        /* 2 */{ "awt.Component.getParent" },
        /* 3 */{ "revalidate" },
        /* 4 */{ "awt.Component.getParent" },
        /* 5 */{ "awt.Container.invalidate" },
        /* 6 */{ "awt.Component.getPeer" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "revalidate" },
                /* 2 */{ "awt.Component.getParent" },
                /* 3 */{ "revalidate" },
                /* 4 */{ "awt.Component.getParent" },
                /* 5 */{ "awt.Container.invalidate" },
                /* 6 */{ "isPreferredSizeSet" },
                /* 7 */{ "isMinimumSizeSet" },
                /* 8 */{ "isMaximumSizeSet" },
                /* 9 */{ "awt.Component.getPeer" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "revalidate" },
                /* 2 */{ "awt.Container.invalidate" },
                /* 3 */{ "getListeners" },
                /* 4 */{ "awt.Component.getHierarchyListeners" },
                /* 5 */{ "getListeners" },
                /* 6 */{ "awt.Container.getContainerListeners" },
                /* 7 */{ "awt.Component.getParent" },
                /* 8 */{ "isValidateRoot" },
                /* 9 */{ "awt.Component.getParent" },
                /* 10 */{ "awt.Component.getParent" },
                /* 11 */{ "awt.Component.isValid" },
                /* 12 */{ "awt.Component.isShowing" },
                /* 13 */{ "awt.Component.isVisible" },
                /* 14 */{ "awt.Component.isDisplayable" }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected revalidate() with non-null parent to call another sequence of events");
        }

        return passed();
    }

    public Result testSetUI() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();

        if (UIManager.getUI(jc) != null) {
            Util.waitQueueEventsProcess();
            InstrumentedUILog.printLogAsArray();
            return failed("expected UIManager.getUI(JComponent) to return null");
        }

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "getUIClassID" },
        /* 2 */{ "awt.Component.toString" },
        /* 3 */{ "paramString" },
        /* 4 */{ "awt.Component.getName" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "getUIClassID" },
                /* 2 */{ "awt.Component.toString" },
                /* 3 */{ "paramString" },
                /* 4 */{ "isPreferredSizeSet" },
                /* 5 */{ "isMinimumSizeSet" },
                /* 6 */{ "isMaximumSizeSet" },
                /* 7 */{ "awt.Component.getName" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "getUIClassID" }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected UIManager.getUI(JComponent jc) to call another sequence of events");
        }
        InstrumentedUILog.clear();

        jc.setUI(null);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] { { "setUI", null },
                { "firePropertyChange", "UI", null, null }, { "revalidate" },
                { "awt.Component.getParent" }, { "awt.Component.repaint" },
                { "repaint", "0 0 0 0 0" }, })
                && !InstrumentedUILog
                        .equals(new Object[][] { { "setUI", null }, })) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected JComponent.setUI(UI) to call another sequence of events");
        }

        return passed();
    }

    public Result testSetVisible() {
        InstrumentedJComponent jc = new InstrumentedJComponent();

        InstrumentedUILog.getLog().clear();
        jc.setVisible(true);
        Util.waitQueueEventsProcess();

        if (InstrumentedUILog.getLog().size() != 2
                || !Arrays.equals(new Object[] { "setVisible", "true" },
                        (Object[]) InstrumentedUILog.getLog().get(0))
                || !Arrays.equals(new Object[] { "awt.Component.isVisible" },
                        (Object[]) InstrumentedUILog.getLog().get(1))) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVisible(true) to call awt.Component.isVisible");
        }

        InstrumentedUILog.getLog().clear();
        jc.setVisible(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "setVisible", "false" },
        /* 2 */{ "awt.Component.isVisible" },
        /* 3 */{ "awt.Component.show", "false" },
        /* 4 */{ "awt.Component.hide" },
        /* 5 */{ "awt.Component.getParent" },
        /* 6 */{ "awt.Component.getParent" },
        /* 7 */{ "revalidate" },
        /* 8 */{ "awt.Component.getParent" }, })
                && !InstrumentedUILog.contains(new Object[][] {
                /* 1 */{ "setVisible", "false" },
                /* 2 */{ "awt.Component.isVisible" },
                /* 3 */{ "awt.Component.show", "false" },
                /* 4 */{ "awt.Component.hide" },
                /* 5 */{ "awt.Component.isFocusOwner" },
                /* 7 */{ "awt.Component.getParent" },
                /* 8 */{ "revalidate" },
                /* 9 */{ "awt.Component.getParent" }, })

        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setVisible(false) to call another sequence of events");
        }

        return passed();
    }
}