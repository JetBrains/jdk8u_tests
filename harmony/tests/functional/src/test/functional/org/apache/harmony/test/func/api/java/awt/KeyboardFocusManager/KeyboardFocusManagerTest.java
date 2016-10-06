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

package org.apache.harmony.test.func.api.java.awt.KeyboardFocusManager;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.AWTKeyStroke;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class KeyboardFocusManagerTest extends AutonomousTest {
    static File         testDir;
    protected Frame     frm;

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
     * MyKeyboardFocusManager class serves as a custom focus manager that can be
     * modified upon the necessity of a particular Test
     */
    class MyKeyboardFocusManager extends KeyboardFocusManager {
        protected boolean postProcessKeyEventCalled = false;
        protected boolean myProperty                = false;

        public boolean getMyProperty() {
            return myProperty;
        }

        public Window getGlobalActiveWindow() throws SecurityException {
            return super.getGlobalActiveWindow();
        }

        public void setGlobalActiveWindow(Window activeWindow) {
            super.setGlobalActiveWindow(activeWindow);
        }

        public Window getGlobalFocusedWindow() {
            return super.getGlobalFocusedWindow();
        }

        public void setGlobalFocusedWindow(Window window) {
            super.setGlobalFocusedWindow(window);
        }

        public boolean dispatchEvent(AWTEvent arg0) {
            return false;
        }

        public void setGlobalFocusOwner(Component focusOwner) {
            super.setGlobalFocusOwner(focusOwner);
        }

        public Component getGlobalFocusOwner() {
            return super.getGlobalFocusOwner();
        }

        public Component getGlobalPermanentFocusOwner() {
            return super.getGlobalPermanentFocusOwner();
        }

        public void setGlobalPermanentFocusOwner(Component focusOwner) {
            super.setGlobalPermanentFocusOwner(focusOwner);
        }

        protected void dequeueKeyEvents(long arg0, Component arg1) {

        }

        protected void enqueueKeyEvents(long arg0, Component arg1) {

        }

        protected void discardKeyEvents(Component arg0) {

        }

        public void focusNextComponent(Component arg0) {

        }

        public void focusPreviousComponent(Component arg0) {

        }

        public void upFocusCycle(Component arg0) {

        }

        public void downFocusCycle(Container arg0) {

        }

        public boolean dispatchKeyEvent(KeyEvent arg0) {
            return false;
        }

        public boolean postProcessKeyEvent(KeyEvent arg0) {
            postProcessKeyEventCalled = true;
            return false;
        }

        public void processKeyEvent(Component arg0, KeyEvent arg1) {

        }

        public void myFirePropertyChange(String propertyName, Object oldValue,
                Object newValue) {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }

        public void myFireVetoablePropertyChange(String propertyName,
                Object oldValue, Object newValue) throws PropertyVetoException {
            super.fireVetoableChange(propertyName, oldValue, newValue);
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

    public class MyVetoChangeListener implements VetoableChangeListener {
        private int propChangedTimes = 0;

        public int getPropChangedTimes() {
            return propChangedTimes;
        }

        public void vetoableChange(PropertyChangeEvent arg0)
                throws PropertyVetoException {
            ++propChangedTimes;
        }
    }

    public KeyEvent provideKeyEvent(int modifiers, int keyCode, char keyChar) {
        return new KeyEvent(frm, KeyEvent.KEY_PRESSED, System
                .currentTimeMillis(), modifiers, keyCode, keyChar);
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
        panel.add(btnClose);
        panel.add(btnDoNothing);

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

    public static void main(String[] args) {
        // File f = new File("C:\\temp\\toDelete");
        // KeyboardFocusManagerTest kfmTest = new KeyboardFocusManagerTest();
        // kfmTest.setUp();
        // kfmTest.setTestDir(f);
        // kfmTest.test(args);
        // kfmTest.tearDown();
        // System.exit(0);
        System.exit(new KeyboardFocusManagerTest().test(args));
    }

    public Result testKeyboardFocusManager_addKeyEventPostProcessor() {
        MyKeyboardFocusManager keyEvPostProcessor = new MyKeyboardFocusManager();
        DefaultKeyboardFocusManager dkfm = new DefaultKeyboardFocusManager();

        dkfm.addKeyEventPostProcessor(keyEvPostProcessor);

        /* Implicit call of MyKeyboardFocusManager#postProcessKeyEvent */
        dkfm.dispatchKeyEvent(provideKeyEvent(0, KeyEvent.VK_SPACE, ' '));

        if (keyEvPostProcessor.postProcessKeyEventCalled == false) {
            return failed("\nMyKeyboardFocusManager#postProcessKeyEvent has "
                    + "not been called");
        }
        return passed();
    }

    public Result testKeyboardFocusManager_clearGlobalFocusOwner() {

        frm.setVisible(true);

        /* Get instance of btnClose button */
        Button btn = (Button) ((Container) frm.getComponents()[0])
                .getComponents()[0];
        btn.requestFocus();
        MyKeyboardFocusManager keyEvPostProcessor = new MyKeyboardFocusManager();
        DefaultKeyboardFocusManager dkfm = new DefaultKeyboardFocusManager();

        dkfm.addKeyEventPostProcessor(keyEvPostProcessor);

        Robot robo = null;
        try {
            robo = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        /* Move mouse to the btn (btnClose) */
        robo.mouseMove(btn.getX() - 5, btn.getY() - 5);

        componentEventFocusLost = false;

        /*
         * This sleep is required because drawing of the GUI components on Linux
         * PCs can be very slow
         */
        sleep(getPaintTimeout());
        waitEventQueueToClear();
        /* FOCUS_LOST event must be sent to btn (btnClose) */
        dkfm.clearGlobalFocusOwner();
        waitEventQueueToClear();
        sleep(getPaintTimeout());

        if (componentEventFocusLost == false) {
            return failed("\nThe component failed to recieve a FOCUS_LOST event");
        }

        /* reset the actionPerformed flag */
        actionPerformed = false;

        /*
         * Click button, the button must not be clicked and "actionPerformed"
         * flag should not be set
         */
        robo.keyPress(KeyEvent.VK_ENTER);
        robo.keyRelease(KeyEvent.VK_ENTER);
        waitEventQueueToClear();
        // System.out.println("actionPerformed: " + componentEventFocusLost);
        // System.out.println("componentEventFocusLost: "
        // + componentEventFocusLost);
        if (actionPerformed) {
            return failed("\nAfter calling clearGlobalFocusOwner() \"the native "
                    + "windowing system will discard all user-generated "
                    + "KeyEvents\" in our keyPress and keyRelease occured on "
                    + "a button");
        }

        return passed();
    }

    public Result testKeyboardFocusManager_propertyChange() {
        MyKeyboardFocusManager myKFM = new MyKeyboardFocusManager();
        MyPropChangeListener myPropChangeListener = new MyPropChangeListener();
        MyVetoChangeListener myVetoChangeListener = new MyVetoChangeListener();
        // DefaultKeyboardFocusManager dkfm = new DefaultKeyboardFocusManager();

        /* Get instance of btnClose button */
        Button btnOldFocusOwner = (Button) ((Container) frm.getComponents()[0])
                .getComponents()[0];
        /* Get instance of btnDoNothing button */
        Button btnNewFocusOwner = (Button) ((Container) frm.getComponents()[0])
                .getComponents()[1];

        frm.setVisible(true);
        btnOldFocusOwner.requestFocus();
        sleep(getPaintTimeout());
        myKFM.addPropertyChangeListener(myPropChangeListener);
        myKFM.addVetoableChangeListener("myProperty", myVetoChangeListener);
        waitEventQueueToClear();
        /* try to change the property */
        myKFM.myFirePropertyChange("focusOwner", btnOldFocusOwner,
                btnNewFocusOwner);
        waitEventQueueToClear();

        if (myPropChangeListener.getPropChangedTimes() != 1) {
            return failed("\nfirePropertyChange has failed to change "
                    + "focusOwner property");
        }
        /*
         * try again to change the property, according to spec No event will be
         * delivered if oldValue and newValue are the same.
         */
        myKFM.myFirePropertyChange("focusOwner", btnNewFocusOwner,
                btnNewFocusOwner);
        waitEventQueueToClear();
        if (myPropChangeListener.getPropChangedTimes() != 1) {
            return failed("\nNo event must be delivered when "
                    + "oldValue and newValue are the same.\nAs a result the "
                    + "property has changed wrong number of times.\nThe "
                    + "expected number of times is 1, the retrieved number is "
                    + myPropChangeListener.getPropChangedTimes());
        }

        btnOldFocusOwner.requestFocus();
        sleep(getPaintTimeout());
        /* try to change the vetoable property */
        boolean propertyVetoExceptionThrown = false;
        try {
            myKFM.myFireVetoablePropertyChange("myProperty",
                    new Boolean(false), new Boolean(true));
        } catch (PropertyVetoException e) {
            propertyVetoExceptionThrown = true;
        }

        if (propertyVetoExceptionThrown == true) {
            return failed("\nPropertyVetoException must not be thrown");
        }
        waitEventQueueToClear();

        if (myVetoChangeListener.getPropChangedTimes() != 1) {
            return failed("\nfirePropertyChange has failed to change "
                    + "myProperty property");
        }
        /*
         * try again to change the property, according to spec No event will be
         * delivered if oldValue and newValue are the same.
         */
        try {
            myKFM.myFireVetoablePropertyChange("myProperty",
                    new Boolean(false), new Boolean(false));
        } catch (PropertyVetoException e) {
            propertyVetoExceptionThrown = true;
        }
        waitEventQueueToClear();

        if (myVetoChangeListener.getPropChangedTimes() != 1) {
            return failed("\nNo event must be delivered when VetoableProperty "
                    + "oldValue and newValue are the same.\nAs a result the "
                    + "property has changed wrong number of times.\nThe "
                    + "expected number of times is 1, the retrieved number is "
                    + myVetoChangeListener.getPropChangedTimes());
        }
        frm.setVisible(false);
        return passed();
    }

    public Result testKeyboardFocusManager_getProperties() {
        MyKeyboardFocusManager myKFM = new MyKeyboardFocusManager();
        frm.setVisible(true);
        sleep(getPaintTimeout());
        try {
            KeyboardFocusManager.setCurrentKeyboardFocusManager(myKFM);
        } catch (SecurityException e) {
            return failed("\nFor some reason it is not allowed to call "
                    + "KeyboardFocusManager.setCurrentKeyboardFocusManager()");
        }
        if (!(KeyboardFocusManager.getCurrentKeyboardFocusManager() instanceof MyKeyboardFocusManager)) {
            return failed("\n getCurrentKeyboardFocusManager() failed");
        }
        myKFM.clearGlobalFocusOwner();
        frm.setVisible(true);
        /* Wait for a frame to become painted */
        sleep(getPaintTimeout());
        Window activeWindow = myKFM.getActiveWindow();

        if (activeWindow == null) {
            return failed("\nKeyboardFocusManager#getActiveWindow() "
                    + "returned null");
        }
        myKFM.setGlobalActiveWindow(frm);
        Frame globalActiveWindow = (Frame) myKFM.getGlobalActiveWindow();
        if (globalActiveWindow == null
                || globalActiveWindow.getTitle().equals("My Frame") == false) {
            return failed("\nKeyboardFocusManager#getGlobalActiveWindow() "
                    + "returned:" + globalActiveWindow);
        }
        myKFM.setGlobalFocusedWindow(frm);
        Frame globalFocusedWindow = (Frame) myKFM.getGlobalFocusedWindow();
        if (myKFM.getGlobalFocusedWindow() == null
                || !(myKFM.getGlobalFocusedWindow() instanceof Frame)) {
            return failed("\nKeyboardFocusManager#getGlobalFocusedWindow() "
                    + "returned: " + globalFocusedWindow);
        }

        /* Get instance of btnClose button */

        Button btn1 = (Button) ((Container) frm.getComponents()[0])
                .getComponents()[1];

        myKFM.setGlobalFocusOwner(btn1);
        Button globalFocusOwner = (Button) myKFM.getGlobalFocusOwner();
        if (globalFocusOwner == null
                || globalFocusOwner.getLabel().equals("MyLoafer") == false) {
            return failed("\nKeyboardFocusManager#getGlobalFocusOwner() "
                    + "returned the wrong value:" + globalFocusOwner);
        }

        myKFM.setGlobalPermanentFocusOwner(btn1);
        Button globalPermanentFocusOwner = (Button) myKFM
                .getGlobalPermanentFocusOwner();
        if (globalPermanentFocusOwner == null
                || globalPermanentFocusOwner.getLabel().equals("MyLoafer") == false) {
            return failed("\nKeyboardFocusManager#globalPermanentFocusOwner() "
                    + "returned the wrong value:" + globalPermanentFocusOwner);
        }

        sleep(getPaintTimeout());
        Frame focusedWindow = (Frame) myKFM.getFocusedWindow();

        if (focusedWindow == null
                || focusedWindow.getTitle().equals("My Frame") == false) {
            return failed("\nKeyboardFocusManager#getFocusedWindow() "
                    + "returned the wrong value:" + focusedWindow);
        }
        // TODO why does requestFocusInWindow does not help retrieving correct
        // object using getFocusOwner?
        // btn0.requestFocusInWindow();
        waitEventQueueToClear();
        sleep(getPaintTimeout());
        Button focusOwner = (Button) myKFM.getFocusOwner();
        if (focusOwner == null
                || focusOwner.getLabel().equals("MyLoafer") == false) {
            return failed("\nKeyboardFocusManager#getFocusOwner() "
                    + "returned the wrong value:" + focusOwner);
        }
        myKFM.clearGlobalFocusOwner();
        myKFM.setGlobalCurrentFocusCycleRoot(frm);
        Frame currentFocusCycleRoot = (Frame) myKFM.getCurrentFocusCycleRoot();
        if (currentFocusCycleRoot == null
                || currentFocusCycleRoot.getTitle().equals("My Frame") == false) {
            return failed("\nKeyboardFocusManager#getGlobalActiveWindow() "
                    + "returned null");
        }

        return passed();
    }

    public Result testKeyboardFocusManager_getKeyProperties() {
        DefaultKeyboardFocusManager myKFM = new DefaultKeyboardFocusManager();

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
        btn.requestFocus();
        if (btn.getFocusTraversalKeysEnabled()) {
            String firstLabel = btn.getLabel();
            String secondLabel = "";
            Button mostRecentFocusOwner = null;

            /* Get keys (if any) that can travese focus forward */
            Set backwardTravKeys = myKFM
                    .getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);

            boolean exceptionFired = false;
            try {
                myKFM.setDefaultFocusTraversalKeys(
                        KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                        backwardTravKeys);
            } catch (IllegalArgumentException e) {
                exceptionFired = true;
            }

            if (exceptionFired == false) {
                return failed("\nNo IllegalArgumentException exception fired "
                        + "when focus traversal keys are unique for a Component");
            }

            Set travKeys = myKFM
                    .getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
            /* In case there are key that can traverse focus forward */
            Iterator it = travKeys.iterator();

            if (it.hasNext()) {
                /* get 1st supported key that can traverse focus forward */
                AWTKeyStroke travKey = (AWTKeyStroke) it.next();

                /*
                 * send that key to the component, we assume that this should be
                 * btnDoNothing, because we were able to get forward traversal
                 * keys on btnClose
                 */
                myKFM.redispatchEvent(btn, provideKeyEvent(travKey
                        .getModifiers(), travKey.getKeyCode(), travKey
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

                if (firstLabel.equals(secondLabel) == true) {
                    return failed("\nKeyboardFocusManager#redispatchEvent "
                            + "failed to move focus to the next component");
                }
            }
        }
        return passed();
    }

    public Result testKeyboardFocusManager_focusTraversalPolicy() {

        MyKeyboardFocusManager myKFM = new MyKeyboardFocusManager();
        boolean exceptionFired = false;
        try {
            myKFM.setDefaultFocusTraversalPolicy(null);
        } catch (IllegalArgumentException e) {
            exceptionFired = true;
        }

        if (exceptionFired == false) {
            return failed("\nNo IllegalArgumentException exception fired "
                    + "when setDefaultFocusTraversalPolicy gets null");
        }
        FocusTraversalPolicy focTravPol = myKFM
                .getDefaultFocusTraversalPolicy();
        if (((Frame) focTravPol.getInitialComponent(frm)).getTitle().equals(
                "My Frame") == false) {
            return failed("\nKeyboardFocusManager#getDefaultFocusTraversalPolicy()"
                    + "has wrong initial component");
        }
        // TODO perhaps getFirstComponent(frm) should not return null

        return passed();
    }
}