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
 * Created on 04.05.2005
 *  
 */
package org.apache.harmony.test.func.api.javax.swing.DefaultButtonModel;

import javax.swing.DefaultButtonModel;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedButtonModel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.Util;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class DefaultButtonModelTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new DefaultButtonModelTest().test(args));
    }

    public Result testStaticFields() {
        if (DefaultButtonModel.ARMED != 1 ||

        DefaultButtonModel.ENABLED != 8 || DefaultButtonModel.PRESSED != 4
                || DefaultButtonModel.ROLLOVER != 16
                || DefaultButtonModel.SELECTED != 2) {
            return failed("static fields have wrong values");
        }

        return passed();
    }

    public Result testFireStateChanged() {
        InstrumentedButtonModel ibm = new InstrumentedButtonModel();

        InstrumentedUILog.clear();

        ibm.fireStateChanged();

        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog
                .equals(new Object[][] { { "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "ButtonModel.fireStateChanged" },
                        { "ButtonModel.getChangeListeners" } })

        ) {
            InstrumentedUILog.printLog();
            return failed("expected fireStateChanged not to call any additional methods or to call getChangeListeners()");
        }

        return passed();
    }

    public Result testActionCommand() {
        DefaultButtonModel dbm = new InstrumentedButtonModel();

        InstrumentedUILog.clear();

        String s = dbm.getActionCommand();

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "ButtonModel.getActionCommand" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected getActionCommand() "
                    + "not to call any additional methods");
        }

        if (s != null) {
            return failed("expected getActionCommand() to return null, got '"
                    + s + "'");
        }

        InstrumentedUILog.clear();

        dbm.setActionCommand("qwe");

        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] { {
                "ButtonModel.setActionCommand", "qwe" } })) {
            InstrumentedUILog.printLog();
            return failed("expected setActionCommand('qwe') "
                    + "not to call any additional methods");
        }

        s = dbm.getActionCommand();

        if (!"qwe".equals(s)) {
            return failed("expected getActionCommand() to return 'qwe', got '"
                    + s + "'");
        }

        return passed();
    }

    public Result testMnemonic() {
        DefaultButtonModel dbm = new InstrumentedButtonModel();
        InstrumentedUILog.clear();

        if (dbm.getMnemonic() != 0) {
            return failed("extected getMnemonic() to return 0 by default");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "ButtonModel.getMnemonic" } })) {
            InstrumentedUILog.printLog();
            return failed("expected getMnemonic() not to call any more methods");
        }

        InstrumentedUILog.clear();

        dbm.setMnemonic(java.awt.event.KeyEvent.VK_F);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setMnemonic",
                        "" + java.awt.event.KeyEvent.VK_F },
                /* 2 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setMnemonic",
                                "" + java.awt.event.KeyEvent.VK_F },
                        /* 2 */{ "ButtonModel.fireStateChanged" },
                        /* 3 */{ "ButtonModel.getChangeListeners" } })

        ) {
            InstrumentedUILog.printLog();
            return failed("expected setMnemonic(java.awt.event.KeyEvent.VK_F) to call another sequence of methods");
        }

        InstrumentedUILog.clear();

        dbm.setMnemonic(java.awt.event.KeyEvent.VK_E);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setMnemonic",
                        "" + java.awt.event.KeyEvent.VK_E },
                /* 2 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setMnemonic",
                                "" + java.awt.event.KeyEvent.VK_E },
                        /* 2 */{ "ButtonModel.fireStateChanged" },
                        /* 3 */{ "ButtonModel.getChangeListeners" } })) {
            InstrumentedUILog.printLog();
            return failed("expected setMnemonic(java.awt.event.KeyEvent.VK_E) to call another sequence of methods");
        }

        if (dbm.getMnemonic() != java.awt.event.KeyEvent.VK_E) {
            return failed("expected getMnemonic to return what was set by setMnemonic()");
        }
        return passed();
    }

    public Result testConstructor() {
        InstrumentedUILog.clear();
        DefaultButtonModel dbm = new InstrumentedButtonModel();
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setEnabled", "true" },
        /* 2 */{ "ButtonModel.isEnabled" },
        /* 3 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[0][0])) {
            InstrumentedUILog.printLog();
            return failed("expected constructor to call another sequence of methods");
        }

        return passed();
    }

    public Result testArmed() {
        DefaultButtonModel dbm = new InstrumentedButtonModel();
        InstrumentedUILog.clear();

        if (dbm.isArmed()) {
            return failed("expected isArmed() to return false by default");
        }
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "ButtonModel.isArmed" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected isArmed() not to call any additional methods");
        }
        InstrumentedUILog.clear();
        dbm.setArmed(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setArmed", "false" },
        /* 2 */{ "ButtonModel.isArmed" }, 
        })
        &&
!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setArmed", "false" },
        /* 2 */{ "ButtonModel.isEnabled" },
        /* 3 */{ "ButtonModel.isArmed" }, 
        })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected setArmed(false) to call isArmed()");
        }

        if (dbm.isArmed()) {
            return failed("expected isArmed() to return what was set by setArmed()");
        }

        InstrumentedUILog.clear();
        dbm.setArmed(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setArmed", "true" },
        /* 2 */{ "ButtonModel.isArmed" },
        /* 3 */{ "ButtonModel.isEnabled" },
        /* 4 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setArmed", "true" },
                /* 2 */{ "ButtonModel.isArmed" },
                /* 3 */{ "ButtonModel.isEnabled" },
                /* 4 */{ "ButtonModel.fireStateChanged" },
                /* 5 */{ "ButtonModel.getChangeListeners" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setArmed", "true" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.isArmed" },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getChangeListeners" }, })
                
        ) {
            InstrumentedUILog.printLog();
            return failed("expected first setArmed(true) to call another sequence of events");
        }
        if (!dbm.isArmed()) {
            return failed("expected isArmed() to return what was set by setArmed()");
        }

        InstrumentedUILog.clear();
        dbm.setArmed(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setArmed", "true" },
        /* 2 */{ "ButtonModel.isArmed" }, })
        && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setArmed", "true" },
                /* 2 */{ "ButtonModel.isEnabled" },
                /* 3 */{ "ButtonModel.isArmed" },
                }
        )
        ) {
            InstrumentedUILog.printLog();
            return failed("expected second setArmed(true) to call another sequence of events");
        }
        if (!dbm.isArmed()) {
            return failed("expected isArmed() to return what was set by setArmed()");
        }

        //dbm.setEnabled(false);
        InstrumentedUILog.clear();
        dbm.setArmed(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setArmed", "false" },
        /* 2 */{ "ButtonModel.isArmed" },
        /* 3 */{ "ButtonModel.isEnabled" },
        /* 4 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setArmed", "false" },
                /* 2 */{ "ButtonModel.isArmed" },
                /* 3 */{ "ButtonModel.isEnabled" },
                /* 4 */{ "ButtonModel.fireStateChanged" },
                /* 5 */{ "ButtonModel.getChangeListeners" }, })
        && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setArmed", "false" },
                /* 2 */{ "ButtonModel.isArmed" },
                /* 3 */{ "ButtonModel.fireStateChanged" },
                /* 4 */{ "ButtonModel.getChangeListeners" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setArmed", "false" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.isArmed" },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getChangeListeners" }, })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected second setArmed(false) to call another sequence of events");
        }
        if (dbm.isArmed()) {
            return failed("expected isArmed() to return what was set by setArmed()");
        }

        dbm.setEnabled(false);
        InstrumentedUILog.clear();
        dbm.setArmed(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setArmed", "true" },
        /* 2 */{ "ButtonModel.isArmed" },
        /* 3 */{ "ButtonModel.isEnabled" }, })
        && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setArmed", "true" },
                /* 2 */{ "ButtonModel.isEnabled" }, })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected setArmed(!setArmed()) to call another sequence of events");
        }
        if (dbm.isArmed()) {
            return failed("expected isArmed() not to be cnahged when button is disabled");
        }

        return passed();
    }

    public Result testEnabled() {
        DefaultButtonModel dbm = new InstrumentedButtonModel();
        InstrumentedUILog.clear();

        if (!dbm.isEnabled()) {
            return failed("expected isEnabled() to return true by default");
        }
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "ButtonModel.isEnabled" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected isEnabled() not to call any additional methods");
        }
        InstrumentedUILog.clear();
        dbm.setEnabled(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setEnabled", "true" },
        /* 2 */{ "ButtonModel.isEnabled" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected setEnabled(true) to call isEnabled()");
        }

        if (!dbm.isEnabled()) {
            return failed("expected isEnabled() to return what was set by setEnabled()");
        }

        InstrumentedUILog.clear();
        dbm.setEnabled(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setEnabled", "false" },
        /* 2 */{ "ButtonModel.isEnabled" },
        /* 3 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setEnabled", "false" },
                /* 2 */{ "ButtonModel.isEnabled" },
                /* 3 */{ "ButtonModel.fireStateChanged" },
                /* 4 */{ "ButtonModel.getChangeListeners" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setEnabled", "false" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.setRollover", "false" },
                        /* 4 */{ "ButtonModel.isRollover" },
                        /* 5 */{ "ButtonModel.fireStateChanged" },
                        /* 6 */{ "ButtonModel.getChangeListeners" }, 
                        })
                        && !InstrumentedUILog.equals(new Object[][] {
                                /* 1 */{ "ButtonModel.setEnabled", "false" },
                                /* 2 */{ "ButtonModel.isEnabled" },
                                /* 3 */{ "ButtonModel.isSelected" },
                                /* 4 */{ "ButtonModel.fireStateChanged" },
                                /* 5 */{ "ButtonModel.getChangeListeners" }, 
                                })
                ) {
            InstrumentedUILog.printLog();
            return failed("expected first setEnabled(false) to call another sequence of events");
        }
        if (dbm.isEnabled()) {
            return failed("expected isEnabled() to return what was set by setEnabled()");
        }

        InstrumentedUILog.clear();
        dbm.setEnabled(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setEnabled", "false" },
        /* 2 */{ "ButtonModel.isEnabled" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected second setEnabled(false) to call another sequence of events");
        }
        if (dbm.isEnabled()) {
            return failed("expected isEnabled() to return what was set by setEnabled()");
        }

        InstrumentedUILog.clear();
        dbm.setEnabled(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setEnabled", "true" },
        /* 2 */{ "ButtonModel.isEnabled" },
        /* 3 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setEnabled", "true" },
                /* 2 */{ "ButtonModel.isEnabled" },
                /* 3 */{ "ButtonModel.fireStateChanged" },
                /* 4 */{ "ButtonModel.getChangeListeners" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setEnabled", "true" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.isSelected" },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getChangeListeners" }, })
                ) {
            InstrumentedUILog.printLog();
            return failed("expected second setEnabled(true) to call another sequence of events");
        }
        if (!dbm.isEnabled()) {
            return failed("expected isEnabled() to return what was set by setEnabled()");
        }

        return passed();
    }

    public Result testPressed() {
        DefaultButtonModel dbm = new InstrumentedButtonModel();
        InstrumentedUILog.clear();

        if (dbm.isPressed()) {
            return failed("expected isPressed() to return false by default");
        }
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "ButtonModel.isPressed" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected isPressed() not to call any additional methods");
        }
        InstrumentedUILog.clear();
        dbm.setPressed(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setPressed", "false" },
        /* 2 */{ "ButtonModel.isPressed" }, })
        && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setPressed", "false" },
                /* 2 */{ "ButtonModel.isEnabled" },
                /* 3 */{ "ButtonModel.isPressed" }, 
                })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected setPressed(false) to call isPressed()");
        }

        if (dbm.isPressed()) {
            return failed("expected isPressed() to return what was set by setPressed()");
        }

        InstrumentedUILog.clear();
        dbm.setPressed(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setPressed", "true" },
        /* 2 */{ "ButtonModel.isPressed" },
        /* 3 */{ "ButtonModel.isEnabled" },
        /* 4 */{ "ButtonModel.isPressed" },
        /* 5 */{ "ButtonModel.fireStateChanged" },

        })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "ButtonModel.setPressed", "true" },
                        { "ButtonModel.isPressed" },
                        { "ButtonModel.isEnabled" },
                        { "ButtonModel.fireStateChanged" },
                        { "ButtonModel.getChangeListeners" }

                })
                
                && !InstrumentedUILog.equals(new Object[][] {
                        { "ButtonModel.setPressed", "true" },
                        { "ButtonModel.isEnabled" },
                        { "ButtonModel.isPressed" },
                        { "ButtonModel.fireStateChanged" },
                        { "ButtonModel.getChangeListeners" }

                })

        ) {
            InstrumentedUILog.printLog();
            return failed("expected first setPressed(true) to call another sequence of events");
        }
        if (!dbm.isPressed()) {
            return failed("expected isPressed() to return what was set by setPressed()");
        }

        InstrumentedUILog.clear();
        dbm.setPressed(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setPressed", "true" },
        /* 2 */{ "ButtonModel.isPressed" }, })
        
        && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setPressed", "true" },
                /* 2 */{ "ButtonModel.isEnabled" },
                /* 3 */{ "ButtonModel.isPressed" },
                })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected second setPressed(true) to call another sequence of events");
        }
        if (!dbm.isPressed()) {
            return failed("expected isPressed() to return what was set by setPressed()");
        }

        InstrumentedUILog.clear();
        dbm.setPressed(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setPressed", "false" },
        /* 2 */{ "ButtonModel.isPressed" },
        /* 3 */{ "ButtonModel.isEnabled" },
        /* 4 */{ "ButtonModel.isPressed" },
        /* 5 */{ "ButtonModel.isArmed" },
        /* 6 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setPressed", "false" },
                /* 2 */{ "ButtonModel.isPressed" },
                /* 3 */{ "ButtonModel.isEnabled" },
                /* 4 */{ "ButtonModel.isPressed" },
                /* 5 */{ "ButtonModel.isArmed" },
                /* 6 */{ "ButtonModel.fireStateChanged" },
                /* 7 */{ "ButtonModel.getChangeListeners" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setPressed", "false" },
                        /* 2 */{ "ButtonModel.isPressed" },
                        /* 3 */{ "ButtonModel.fireStateChanged" },
                        /* 4 */{ "ButtonModel.getChangeListeners" },
                        /* 5 */{ "ButtonModel.isArmed" },
                        })
&& !InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setPressed", "false" },
        /* 2 */{ "ButtonModel.isEnabled" },
        /* 2 */{ "ButtonModel.isPressed" },
        /* 3 */{ "ButtonModel.fireStateChanged" },
        /* 4 */{ "ButtonModel.getChangeListeners" },
        /* 5 */{ "ButtonModel.isArmed" },
        
}
                        
        )) {
            InstrumentedUILog.printLog();
            return failed("expected second setPressed(false) to call another sequence of events");
        }
        if (dbm.isPressed()) {
            return failed("expected isPressed() to return what was set by setPressed()");
        }

        dbm.setArmed(true);
        dbm.setPressed(true);
        InstrumentedUILog.clear();
        dbm.setPressed(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setPressed", "false" },
                /* 2 */{ "ButtonModel.isPressed" },
                /* 3 */{ "ButtonModel.isEnabled" },
                /* 4 */{ "ButtonModel.isPressed" },
                /* 5 */{ "ButtonModel.isArmed" },
                /* 6 */{ "ButtonModel.getActionCommand" },
                /* 7 */{ "ButtonModel.fireActionPerformed",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 8 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setPressed", "false" },
                        /* 2 */{ "ButtonModel.isPressed" },
                        /* 3 */{ "ButtonModel.isEnabled" },
                        /* 4 */{ "ButtonModel.isPressed" },
                        /* 5 */{ "ButtonModel.isArmed" },
                        /* 6 */{ "ButtonModel.getActionCommand" },
                        /* 7 */{ "ButtonModel.fireActionPerformed",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 8 */{ "ButtonModel.fireStateChanged" },
                        /* 9 */{ "ButtonModel.getChangeListeners" }, })
                        && !InstrumentedUILog.equals(new Object[][] { //some implementations don't call
                                //getActionCommand() - they just take 'action' property directly
                                /* 1 */{ "ButtonModel.setPressed", "false" },
                                /* 2 */{ "ButtonModel.isPressed" },
                                /* 3 */{ "ButtonModel.fireStateChanged" },
                                /* 4 */{ "ButtonModel.getChangeListeners" },
                                /* 5 */{ "ButtonModel.isArmed" },
                                /* 6 */{ "ButtonModel.fireActionPerformed",
                                    InstrumentedUILog.ANY_NON_NULL_VALUE },
                                /* 7 */{ "ButtonModel.getActionListeners" },
                                })
                                && !InstrumentedUILog.equals(new Object[][] { 
                                        /* 1 */{ "ButtonModel.setPressed", "false" },
                                        /* 2 */{ "ButtonModel.isEnabled" },
                                        /* 3 */{ "ButtonModel.isPressed" },
                                        /* 4 */{ "ButtonModel.fireStateChanged" },
                                        /* 5 */{ "ButtonModel.getChangeListeners" },
                                        /* 6 */{ "ButtonModel.isArmed" },
                                        /* 7 */{ "ButtonModel.fireActionPerformed",
                                            InstrumentedUILog.ANY_NON_NULL_VALUE },
                                        /* 8 */{ "ButtonModel.getActionListeners" },
                                        })
                                
                        
        ) {
            InstrumentedUILog.printLog();
            return failed("expected third setPressed(false) to call another sequence of events");
        }

        return passed();
    }

    public Result testRollover() {
        DefaultButtonModel dbm = new InstrumentedButtonModel();
        InstrumentedUILog.clear();

        if (dbm.isRollover()) {
            return failed("expected isRollover() to return false by default");
        }
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "ButtonModel.isRollover" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected isRollover() not to call any additional methods");
        }
        InstrumentedUILog.clear();
        dbm.setRollover(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "ButtonModel.setRollover", "false" },
        /* 2 */{ "ButtonModel.isRollover" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected setRollover(false) to call isRollover()");
        }

        if (dbm.isRollover()) {
            return failed("expected isRollover() to return what was set by setRollover()");
        }

        InstrumentedUILog.clear();
        dbm.setRollover(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setRollover", "true" },
        /* 2 */{ "ButtonModel.isRollover" },
        /* 3 */{ "ButtonModel.isEnabled" },
        /* 4 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setRollover", "true" },
                /* 2 */{ "ButtonModel.isRollover" },
                /* 3 */{ "ButtonModel.isEnabled" },
                /* 4 */{ "ButtonModel.fireStateChanged" },
                /* 5 */{ "ButtonModel.getChangeListeners" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setRollover", "true" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.isRollover" },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getChangeListeners" }, })
                ) {
            InstrumentedUILog.printLog();
            return failed("expected first setRollover(true) to call another sequence of events");
        }
        if (!dbm.isRollover()) {
            return failed("expected isRollover() to return what was set by setRollover()");
        }

        InstrumentedUILog.clear();
        dbm.setRollover(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setRollover", "true" },
        /* 2 */{ "ButtonModel.isRollover" }, })
        && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setRollover", "true" },
                /* 2 */{ "ButtonModel.isEnabled" },
                /* 3 */{ "ButtonModel.isRollover" },
                }
        )
        ) {
            InstrumentedUILog.printLog();
            return failed("expected second setRollover(true) to call another sequence of events");
        }
        if (!dbm.isRollover()) {
            return failed("expected isRollover() to return what was set by setRollover()");
        }

        InstrumentedUILog.clear();
        dbm.setRollover(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setRollover", "false" },
        /* 2 */{ "ButtonModel.isRollover" },
        /* 3 */{ "ButtonModel.isEnabled" },
        /* 4 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setRollover", "false" },
                /* 2 */{ "ButtonModel.isRollover" },
                /* 3 */{ "ButtonModel.isEnabled" },
                /* 4 */{ "ButtonModel.fireStateChanged" },
                /* 5 */{ "ButtonModel.getChangeListeners" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setRollover", "false" },
                        /* 2 */{ "ButtonModel.isEnabled" },
                        /* 3 */{ "ButtonModel.isRollover" },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getChangeListeners" }, })
                ) {
            InstrumentedUILog.printLog();
            return failed("expected second setRollover(false) to call another sequence of events");
        }
        if (dbm.isRollover()) {
            return failed("expected isRollover() to return what was set by setRollover()");
        }

        return passed();
    }

    public Result testSelected() {
        DefaultButtonModel dbm = new InstrumentedButtonModel();
        InstrumentedUILog.clear();

        if (dbm.isSelected()) {
            return failed("expected isSelected() to return false by default");
        }
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "ButtonModel.isSelected" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected isSelected() not to call any additional methods");
        }
        InstrumentedUILog.clear();
        dbm.setSelected(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setSelected", "false" },
        /* 2 */{ "ButtonModel.isSelected" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected setSelected(false) to call isSelected()");
        }

        if (dbm.isSelected()) {
            return failed("expected isSelected() to return what was set by setSelected()");
        }

        InstrumentedUILog.clear();
        dbm.setSelected(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setSelected", "true" },
                /* 2 */{ "ButtonModel.isSelected" },
                /* 3 */{ "ButtonModel.fireItemStateChanged",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 4 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setSelected", "true" },
                        /* 2 */{ "ButtonModel.isSelected" },
                        /* 3 */{ "ButtonModel.fireItemStateChanged",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getChangeListeners" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "ButtonModel.setSelected", "true" },
                        { "ButtonModel.isSelected" },
                        { "ButtonModel.fireStateChanged" },
                        { "ButtonModel.getChangeListeners" },
                        { "ButtonModel.fireItemStateChanged",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        { "ButtonModel.getItemListeners" },

                })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected first setSelected(true) to call another sequence of events");
        }
        if (!dbm.isSelected()) {
            return failed("expected isSelected() to return what was set by setSelected()");
        }

        InstrumentedUILog.clear();
        dbm.setSelected(true);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "ButtonModel.setSelected", "true" },
        /* 2 */{ "ButtonModel.isSelected" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected second setSelected(true) to call another sequence of events");
        }
        if (!dbm.isSelected()) {
            return failed("expected isSelected() to return what was set by setSelected()");
        }

        InstrumentedUILog.clear();
        dbm.setSelected(false);
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "ButtonModel.setSelected", "false" },
                /* 2 */{ "ButtonModel.isSelected" },
                /* 3 */{ "ButtonModel.fireItemStateChanged",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 4 */{ "ButtonModel.fireStateChanged" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "ButtonModel.setSelected", "false" },
                        /* 2 */{ "ButtonModel.isSelected" },
                        /* 3 */{ "ButtonModel.fireItemStateChanged",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 4 */{ "ButtonModel.fireStateChanged" },
                        /* 5 */{ "ButtonModel.getChangeListeners" }, })
         && !InstrumentedUILog.equals(new Object[][] {
                 /* 1 */{ "ButtonModel.setSelected", "false" },
                 /* 2 */{ "ButtonModel.isSelected" },
                 /* 3 */{ "ButtonModel.fireStateChanged" },
                 /* 4 */{ "ButtonModel.getChangeListeners" },
                 /* 5 */{ "ButtonModel.fireItemStateChanged",
                         InstrumentedUILog.ANY_NON_NULL_VALUE },
                 /* 6 */{ "ButtonModel.getItemListeners" },
         })
         
        ) {
            InstrumentedUILog.printLog();
            return failed("expected second setSelected(false) to call another sequence of events");
        }
        if (dbm.isSelected()) {
            return failed("expected isSelected() to return what was set by setSelected()");
        }

        return passed();
    }

}