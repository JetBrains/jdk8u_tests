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

package org.apache.harmony.test.func.api.java.awt.Window;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.im.InputContext;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class WindowTest extends AutonomousTest {
    static File         testDir;
    protected Window    wnd;
    protected Frame     frm;
    protected boolean   butActionListenerChanged = false;
    Button              btnClose;
    Button              btnDoNothing;
    protected final int WIN_WIDTH                = 320;
    protected final int WIN_HEIGHT               = 240;
    protected final int WIN_X                    = 150;
    protected final int WIN_Y                    = 150;
    protected Dimension winDimension             = new Dimension(WIN_WIDTH,
                                                         WIN_HEIGHT);
    protected Dimension smallWinDimension        = new Dimension(WIN_WIDTH / 4,
                                                         WIN_HEIGHT / 4);
    protected Point     winLocation              = new Point(WIN_X, WIN_Y);
    protected double    testedPropety            = 0.0;

    protected void setUp() {
        frm = new Frame("MyApp");
        wnd = new Window(frm);
        btnClose = new Button("MyClose");
        btnDoNothing = new Button("MyLoafer");

        /* WM_DESTROY is posted when button is clicked */
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String action = ae.getActionCommand();
                if (action.equals("Close")) {
                    frm.dispose();
                    wnd.dispose();
                    butActionListenerChanged = true;
                } else {
                    System.out.println(action);
                }
            }
        };

        btnClose.addActionListener(al);
        btnClose.setBounds(10, 10, 80, 22);
        btnDoNothing.setBounds(70, 20, 100, 22);

        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setSize(winDimension);
        panel.add(btnClose, 0);
        panel.add(btnDoNothing, 1);

        wnd.add(panel);

        /* Handler for WM_DESTROY message */
        frm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frm.dispose();
                // System.exit(0);
            }
        });

        wnd.setSize(winDimension);
        wnd.setBackground(Color.WHITE);

    }
    protected void tearDown() {
        wnd.dispose();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) {
        //
        // File f = new File("C:\\temp\\toDelete");
        // WindowTest winTest = new WindowTest();
        // winTest.setUp(f);
        // winTest.setTestDir(f);
        // winTest.test(args);
        // winTest.tearDown(f);
        // System.exit(0);

        System.exit(new WindowTest().test(args));
    }

    public class MyPropChangeListener implements PropertyChangeListener {
        private int propChangedTimes = 0;

        public int getPropChangedTimes() {
            return propChangedTimes;
        }

        public void propertyChange(PropertyChangeEvent arg0) {
            ++propChangedTimes;
        }
    }

    public Result testWindow_addPropertyChangeListener() {

        MyPropChangeListener myWindowListener = new MyPropChangeListener();

        boolean exceptionThrown = false;
        /* if listener is null no exception is thrown */
        try {
            wnd.addPropertyChangeListener(null);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        if (exceptionThrown == true) {
            return failed("\nAn exception must NOT be thrown when listener is null");
        }

        wnd.addPropertyChangeListener(myWindowListener);

        /* Changing font in window */
        wnd.setFont(new Font("Courier New", Font.BOLD, 12));

        if (myWindowListener.getPropChangedTimes() != 1) {
            return failed("\nExpected that window property had changed 1 time "
                    + "instead it has changed: "
                    + myWindowListener.getPropChangedTimes() + " times");
        }

        /* Changing focus traversal keys in window */
        SortedSet s = new TreeSet();
        wnd.setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS,
                s);
        if (myWindowListener.getPropChangedTimes() != 2) {
            return failed("\nExpected that window property had changed 2 times "
                    + "instead it has changed: "
                    + myWindowListener.getPropChangedTimes() + " times");
        }

        /* Changing background on window */
        wnd.setBackground(Color.BLUE);
        if (myWindowListener.getPropChangedTimes() != 3) {
            return failed("\nExpected that button property had changed 3 times "
                    + "instead it has changed: "
                    + myWindowListener.getPropChangedTimes() + " times");
        }

        /* Changing foreground on window */
        wnd.setForeground(Color.YELLOW);
        if (myWindowListener.getPropChangedTimes() != 4) {
            return failed("\nExpected that button property had changed 4 times "
                    + "instead it has changed: "
                    + myWindowListener.getPropChangedTimes() + " times");
        }

        return passed();
    }

    public Result testWindow_WindowStates() {

        if (wnd.isFocused() || wnd.isShowing() || wnd.isValid()
                || wnd.isActive() || wnd.isVisible() || wnd.isDisplayable()
                || wnd.isFocusCycleRoot() == false
                || wnd.getFocusCycleRootAncestor() != null) {

            return failed("\n Window has incorrect initial state");
        }

        wnd.pack();
        if (wnd.isFocused() == true || wnd.isShowing() == true
                || wnd.isValid() == false || wnd.isActive() == true
                || wnd.isVisible() == true || wnd.isDisplayable() == false
                || wnd.isFocusCycleRoot() == false
                || wnd.getFocusCycleRootAncestor() != null) {
            return failed("\n Window has incorrect state after pack()");
        }
        wnd.setVisible(true);
        if (wnd.isFocused() == true || wnd.isShowing() == false
                || wnd.isValid() == false || wnd.isActive() == true
                || wnd.isVisible() == false || wnd.isDisplayable() == false
                || wnd.isFocusCycleRoot() == false
                || wnd.getFocusCycleRootAncestor() != null) {
            return failed("\n Window has incorrect state after setVisible(true)");
        }

        if (wnd.isFocusableWindow() == true || wnd.isFocusCycleRoot() == false) {
            return failed("\n Window must be focusable");
        }
        wnd.setVisible(false);
        if (wnd.isFocusableWindow() == true || wnd.isFocusCycleRoot() == false) {
            return failed("\n Window must NOT be focusable");
        }
        if (wnd.isFocused() == true || wnd.isShowing() == true
                || wnd.isValid() == false || wnd.isActive() == true
                || wnd.isVisible() == true || wnd.isDisplayable() == false
                || wnd.isFocusCycleRoot() == false
                || wnd.getFocusCycleRootAncestor() != null) {
            return failed("\n Window has incorrect state after setVisible(false)");
        }
        wnd.dispose();
        if (wnd.isFocused() || wnd.isShowing() || wnd.isValid()
                || wnd.isActive() || wnd.isVisible() || wnd.isDisplayable()
                || wnd.isFocusCycleRoot() == false
                || wnd.getFocusCycleRootAncestor() != null) {
            return failed("\n Window has incorrect state after dispose()");
        }

        if (wnd.isDisplayable() == true) {
            return failed("\n Window must NOT be displayable");
        }
        wnd.pack();
        if (wnd.isDisplayable() == false) {
            return failed("\n Window must be displayable");
        }
        /*
         * When getFocusableWindowState() returns false isFocusableWindow() must
         * return false as well
         */
        if (wnd.getFocusableWindowState() == false
                && wnd.isFocusableWindow() == true) {
            return failed("\n Window is NOT focusable");
        }

        return passed();
    }

    public Result testWindow_getGraphicsConfiguration() {
        GraphicsConfiguration grConf = wnd.getGraphicsConfiguration();
        if (grConf.getBounds().width < 0 || grConf.getBounds().height < 0) {
            return failed("\n Window has incorrect GraphicsConfiguration");
        }

        /* Test for an exception */
        boolean exceptionThrown = false;
        try {
            Window wnd_temp = new Window(wnd, grConf);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        if (GraphicsEnvironment.isHeadless() && exceptionThrown == false) {
            return failed("\n Window constructer failed to "
                    + "throw IllegalArgumentException exception");
        }

        return passed();
    }

    public Result testWindow_getInputContext() {
        InputContext inConf = wnd.getInputContext();
        /*
         * "A window always has an input context"-javadoc. This is why the
         * returned value cannot be null
         */
        if (inConf == null) {
            return failed("\n Window's input context can not be null");
        }
        boolean exceptionThrown = false;
        try {
            inConf.reconvert();
        } catch (UnsupportedOperationException e) {
            /*
             * We must get here because "there is no current input method
             * available or the current input method does not support the
             * reconversion operation."
             */
            exceptionThrown = true;
        }
        if (exceptionThrown == false) {
            return failed("\n Window's inConf.reconvert(); failed to "
                    + "throw UnsupportedOperationException exception");
        }

        return passed();
    }

    public Result testWindow_getMostRecentFocusOwner() {

        /* There is only one Panel within this window */
        if (wnd.getComponents().length <= 0 || wnd.getComponents().length > 1) {
            return failed("\n Wrong number of components in window");
        }
        /*
         * In order for Window to become focusable it is required to show Frame
         * see javadoc for Component.requestFocusInWindow() method
         */

        frm.setVisible(true);
        wnd.setVisible(true);
        sleep(getPaintTimeout());
        boolean isWindowFocusable = wnd.isFocusableWindow();
        if (isWindowFocusable == false) {
            return failed("\nFor some reason window can't gain focus");
        }
        sleep(getPaintTimeout());

        /* Gain btnDoNothing instance */
        Button btn = (Button) ((Container) wnd.getComponents()[0])
                .getComponents()[0];

        btn.requestFocus();
        waitEventQueueToClear();

        /* this must be btnClose */
        Button mostRecentFocusOwner = (Button) wnd.getMostRecentFocusOwner();
        sleep(getPaintTimeout());
        if (mostRecentFocusOwner.getLabel().equals(btnClose.getLabel()) == false) {
            return failed("\ngetMostRecentFocusOwner() returned wrong component");
        }

        /* Clears focus from all elements */
        DefaultKeyboardFocusManager dkfm = new DefaultKeyboardFocusManager();
        sleep(getPaintTimeout());
        /*
         * If no child Component has ever requested focus, and this is a
         * non-focusable Window, Window#getMostRecentFocusOwnernull returns
         * null.
         */
        dkfm.clearGlobalFocusOwner();
        wnd.dispose();
        wnd.setVisible(false);
        waitEventQueueToClear();
        sleep(getPaintTimeout());
        if (wnd.getMostRecentFocusOwner() != null) {
            return failed("\ngetMostRecentFocusOwner() did not return null");
        }
        frm.dispose();
        wnd.dispose();

        return passed();
    }
    public Result testWindow_processEvent() {
        class TempWndComEvent extends Window {
            public boolean componentEventFired = false;
            public TempWndComEvent(Frame frm) {
                super(frm);
                super.enableEvents(ComponentEvent.COMPONENT_RESIZED);
            }

            public void processEvent(AWTEvent event) {
                componentEventFired = true;
            }
        }
        class TempWndEvent extends Window {
            public boolean windowEventFired = false;
            public TempWndEvent(Frame frm) {
                super(frm);
                super.enableEvents(WindowEvent.WINDOW_OPENED);
            }

            public void processWindowEvent(WindowEvent event) {
                windowEventFired = true;
            }
        }

        TempWndEvent tempWnd = new TempWndEvent(frm);
        TempWndComEvent tempWndCom = new TempWndComEvent(frm);

        // tempWnd.addComponentListener(new ComponentAdapter() {
        // public void componentResized(ComponentEvent e) {
        // }
        // });
        // tempWnd.addWindowListener(new WindowAdapter() {
        // public void windowClosing(WindowEvent e) {
        // }
        // });

        /* Trigger COMPONENT_RESIZED event */
        tempWndCom.setSize(winDimension);
        /* Trigger WINDOW_OPENED event */
        tempWnd.setVisible(true);

        waitEventQueueToClear();

        // System.out.println("ComponentEventFired: == " +
        // tempWndCom.componentEventFired);
        // System.out.println("WindowEventFired: == " +
        // tempWnd.windowEventFired);

        if (tempWndCom.componentEventFired == false) {
            failed("\n Window's processEvent() was not invoked ");
        }
        if (tempWnd.windowEventFired == false) {
            failed("\n Window's processWindowEvent() was not invoked ");
        }
        tempWnd.dispose();
        return passed();
    }
}