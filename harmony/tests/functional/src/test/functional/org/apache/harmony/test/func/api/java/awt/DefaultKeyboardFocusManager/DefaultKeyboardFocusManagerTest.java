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

package org.apache.harmony.test.func.api.java.awt.DefaultKeyboardFocusManager;

import java.awt.AWTEvent;
import java.awt.AWTKeyStroke;
import java.awt.Button;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class DefaultKeyboardFocusManagerTest extends AutonomousTest {
    static File         testDir;
    protected Frame     frm;
    protected KeyEvent  ke;

    Button              btnClose;
    Button              btnDoNothing;
    protected final int WIN_WIDTH               = 320;
    protected final int WIN_HEIGHT              = 240;
    protected final int WIN_X                   = 150;
    protected final int WIN_Y                   = 150;
    protected Dimension winDimension            = new Dimension(WIN_WIDTH,
                                                        WIN_HEIGHT);
    protected Dimension smallWinDimension       = new Dimension(WIN_WIDTH / 4,
                                                        WIN_HEIGHT / 4);
    protected Point     winLocation             = new Point(WIN_X, WIN_Y);

    protected boolean   actionPerformed         = false;
    protected boolean   componentEventFocusLost = false;

    /**
     * MyDefaultKeyboardFocusManager class serves as a custom focus manager that
     * can be modified upon the necessity of a particular Test
     */
    class MyDefaultKeyboardFocusManager extends DefaultKeyboardFocusManager {
        protected boolean postProcessKeyEventCalled = false;
        private boolean   postProcessKeyEventResult = false;
        protected boolean dispatchKeyEventCalled    = false;
        private boolean   dispatchKeyEventResult    = false;
        protected boolean dispatchEventCalled       = false;
        private boolean   dispatchEventResult       = false;

        public boolean getDispatchEventResult() {
            return dispatchEventResult;
        }

        public boolean getDispatchKeyEventResult() {
            return dispatchKeyEventResult;
        }

        public boolean getPostProcessKeyEventResult() {
            return postProcessKeyEventResult;
        }

        public boolean dispatchEvent(AWTEvent arg0) {
            dispatchEventCalled = true;
            dispatchEventResult = super.dispatchEvent(arg0);
            return dispatchEventResult;
        }

        public boolean dispatchKeyEvent(KeyEvent arg0) {
            dispatchKeyEventCalled = true;
            dispatchKeyEventResult = super.dispatchKeyEvent(arg0);
            return dispatchKeyEventResult;
        }

        public boolean postProcessKeyEvent(KeyEvent arg0) {
            postProcessKeyEventCalled = true;
            postProcessKeyEventResult = super.postProcessKeyEvent(arg0);
            return postProcessKeyEventResult;
        }
    }

    /**
     * 
     * MyPropChangeListener class has an embedded counter, that is incremented
     * each tiem the bound property is changed
     */

    public class MyPropChangeListener implements PropertyChangeListener {
        private int propChangedTimes = 0;

        public int getPropChangedTimes() {
            return propChangedTimes;
        }

        public void propertyChange(PropertyChangeEvent arg0) {
            ++propChangedTimes;
        }
    }

    protected void setUp() {
        frm = new Frame("My Frame");
        btnClose = new Button("MyClose");
        btnDoNothing = new Button("MyLoafer");

        /* WM_DESTROY is posted when button is clicked */
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                actionPerformed = true;
                String action = ae.getActionCommand();
                if (action.equals("MyClose")) {
                    frm.dispose();
                } else {
                    System.out.println(action);
                }
            }
        };

        btnClose.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                componentEventFocusLost = true;
            }
        });

        btnClose.addActionListener(al);
        btnClose.setBounds(10, 10, 80, 22);
        btnDoNothing.setBounds(70, 20, 100, 22);

        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setSize(winDimension);
        panel.add(btnClose, 0);
        panel.add(btnDoNothing, 1);

        frm.add(panel);

        /* Handler for WM_DESTROY message */
        frm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frm.dispose();
            }
        });

        frm.setSize(winDimension);

    }
    protected void tearDown() {
        frm.dispose();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) throws IOException {
        System.exit(new DefaultKeyboardFocusManagerTest().test(args));
    }

    public KeyEvent provideKeyEvent(int modifiers, int keyCode, char keyChar) {
        return new KeyEvent(frm, KeyEvent.KEY_PRESSED, System
                .currentTimeMillis(), modifiers, keyCode, keyChar);
    }

    public Result testDefaultKeyboardFocusManager_keyEvent() {
        MyDefaultKeyboardFocusManager myKFM = new MyDefaultKeyboardFocusManager();

        /* Install tested DefaultKeyboardFocusManager */
        try {
            KeyboardFocusManager.setCurrentKeyboardFocusManager(myKFM);
        } catch (SecurityException e) {
            return failed("\nFor some reason it is not allowed to call "
                    + "KeyboardFocusManager.setCurrentKeyboardFocusManager()");
        }
        boolean res = myKFM.dispatchKeyEvent(provideKeyEvent(0,
                KeyEvent.VK_SPACE, ' '));
        waitEventQueueToClear();

        if (res == false) {
            return failed("\nMyDefaultKeyboardFocusManager#dispatchEvent has "
                    + "returned false, whereas it should have dispatched the event");
        }

        if (myKFM.getDispatchKeyEventResult() == false) {
            return failed("\nDefaultKeyboardFocusManager#dispatchKeyEvent "
                    + "has returned false instead of true");
        }

        if (myKFM.postProcessKeyEventCalled == false) {
            return failed("\nMyDefaultKeyboardFocusManager#postProcessKeyEvent has "
                    + "not been called");
        }

        if (myKFM.getPostProcessKeyEventResult() == false) {
            return failed("\nDefaultKeyboardFocusManager#postProcessKeyEvent "
                    + "has returned false instead of true");
        }
        return passed();
    }

    public Result testDefaultKeyboardFocusManager_processKeyEvent() {
        MyDefaultKeyboardFocusManager myKFM = new MyDefaultKeyboardFocusManager();

        /* Install tested DefaultKeyboardFocusManager */
        try {
            KeyboardFocusManager.setCurrentKeyboardFocusManager(myKFM);
        } catch (SecurityException e) {
            return failed("\nFor some reason it is not allowed to call "
                    + "KeyboardFocusManager.setCurrentKeyboardFocusManager()");
        }

        frm.setVisible(true);
        Button btn = (Button) ((Container) frm.getComponents()[0])
                .getComponents()[0];
        sleep(getPaintTimeout());
        btn.requestFocus();
        if (btn.getFocusTraversalKeysEnabled()) {
            String firstLabel = btn.getLabel();
            String secondLabel = "";
            Button mostRecentFocusOwner = null;

            /* Get keys (if any) that can travese focus forward */
            Set forwardTravKeys = btn
                    .getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);

            /* In case there are key that can traverse focus forward */
            Iterator it = forwardTravKeys.iterator();

            if (it.hasNext()) {
                /* get 1st supported key that can traverse focus forward */
                AWTKeyStroke fwdTravKey = (AWTKeyStroke) it.next();

                /*
                 * send that key to the component, we assume that this should be
                 * btnDoNothing, because we were able to get forward traversal
                 * keys on btnClose
                 */
                myKFM.processKeyEvent(btn, provideKeyEvent(fwdTravKey
                        .getModifiers(), fwdTravKey.getKeyCode(), fwdTravKey
                        .getKeyChar()));

                /* Wait until the window gets painted, etc. */
                waitEventQueueToClear();
                sleep(getPaintTimeout());
                /*
                 * There are only two buttons in the frame, this is why we
                 * assume that focus could only move from button that was added
                 * first, to the button that wass added second. See setUp() for
                 * details
                 */
                mostRecentFocusOwner = (Button) frm.getMostRecentFocusOwner();

                secondLabel = mostRecentFocusOwner.getLabel();
                sleep(getPaintTimeout());
                if (firstLabel.equals(secondLabel) == true) {
                    return failed("\nDefaultKeyboardFocusManager#processKeyEvent "
                            + "failed to move focus to the next component");
                }
            }
            /* Get keys (if any) that can travese focus backward */
            Set backwardTravKeys = btn
                    .getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);

            /* In case there are key that can traverse focus backward */
            it = backwardTravKeys.iterator();

            /*
             * No sence to traverse focus backward if mostRecentFocusOwner is
             * null
             */
            if (it.hasNext() && mostRecentFocusOwner != null) {
                /* get 1st supported key that can traverse focus backward */
                AWTKeyStroke bwdTravKey = (AWTKeyStroke) it.next();

                /* Move focus to its previous position */
                myKFM.processKeyEvent(mostRecentFocusOwner, provideKeyEvent(
                        bwdTravKey.getModifiers(), bwdTravKey.getKeyCode(),
                        bwdTravKey.getKeyChar()));

                /* Wait until other events get handeled */
                waitEventQueueToClear();
                sleep(getPaintTimeout());
                mostRecentFocusOwner = (Button) frm.getMostRecentFocusOwner();
                String tempLabel = mostRecentFocusOwner.getLabel();
                if (firstLabel.equals(tempLabel) == false) {
                    return failed("\nDefaultKeyboardFocusManager#processKeyEvent "
                            + "failed to maintain the focus cycle");
                }
            }
        }
        return passed();
    }

    public Result testDefaultKeyboardFocusManager_dispatchEvent() {
        MyDefaultKeyboardFocusManager keyEventDispatcher = new MyDefaultKeyboardFocusManager();
        MyDefaultKeyboardFocusManager myKFM = new MyDefaultKeyboardFocusManager();

        /* Install tested DefaultKeyboardFocusManager */
        try {
            KeyboardFocusManager.setCurrentKeyboardFocusManager(myKFM);
        } catch (SecurityException e) {
            return failed("\nFor some reason it is not allowed to call "
                    + "KeyboardFocusManager.setCurrentKeyboardFocusManager()");
        }

        myKFM.addKeyEventDispatcher(keyEventDispatcher);

        Button btn = (Button) ((Container) frm.getComponents()[0])
                .getComponents()[0];

        FocusEvent fe = new FocusEvent(btn, FocusEvent.FOCUS_GAINED);

        if (myKFM.dispatchEvent(fe) == false) {
            return failed("\nDefaultKeyboardFocusManager#dispatchEvent must "
                    + "return true, when processing events from FocusEvent "
                    + "as they are directly involved in managing focus "
                    + "states of component");
        }

        if (myKFM.dispatchEvent(provideKeyEvent(0, KeyEvent.VK_SPACE, ' ')) == false) {
            return failed("\nDefaultKeyboardFocusManager#dispatchEvent must "
                    + "return true, when processing events inherited "
                    + "from KeyEvent or representing KeyEvent object");
        }

        return passed();
    }
}