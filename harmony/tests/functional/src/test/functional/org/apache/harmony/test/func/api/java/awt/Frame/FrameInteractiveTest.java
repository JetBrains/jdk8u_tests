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

package org.apache.harmony.test.func.api.java.awt.Frame;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import org.apache.harmony.test.func.api.share.InteractiveTest;
import org.apache.harmony.share.Result;

public class FrameInteractiveTest extends InteractiveTest {
    static File         testDir;
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
    protected void setUp() {
        frm = new Frame("My Title");
        btnClose = new Button("MyClose");
        btnDoNothing = new Button("MyLoafer");

        /* WM_DESTROY is posted when button is clicked */
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String action = ae.getActionCommand();
                if (action.equals("Close")) {
                    frm.dispose();
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
        panel.add(btnClose);
        panel.add(btnDoNothing);

        frm.add(panel);

        /* Handler for WM_DESTROY message */
        frm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frm.dispose();
                // System.exit(0);
            }
        });

        frm.setSize(winDimension);
        frm.setLocation(winLocation);

    }
    protected void tearDown() {
        frm.dispose();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) {
        // File f = new File("C:\\temp\\toDelete");
        // FrameInteractiveTest frmTest = new FrameInteractiveTest();
        // frmTest.setUp();
        // frmTest.setTestDir(f);
        // frmTest.test(args);
        // frmTest.tearDown();
        // System.exit(0);
        System.exit(new FrameInteractiveTest().test(args));
    }

    public Result testFrame_getExtendedState() {

        Toolkit tk = frm.getToolkit();
        /*
         * This set of conditions in obligatory as not all states are supported
         * on a given platform
         */
        frm.setVisible(true);
         if (tk.isFrameStateSupported(Frame.NORMAL)) {
            frm.setExtendedState(Frame.NORMAL);
            setDescription("Do you see the frame (window) titled "
                    + frm.getTitle() + "?");
            if (Asserting() == false) {
                return failed(getFailureReason());
            }
        }
        if (tk.isFrameStateSupported(Frame.ICONIFIED)) {
            frm.setExtendedState(Frame.ICONIFIED);
            setDescription("Do the frame (window) titled " + frm.getTitle()
                    + " get Iconified (minimized)?");
            if (Asserting() == false) {
                return failed(getFailureReason());
            }
        }
        if (tk.isFrameStateSupported(Frame.MAXIMIZED_HORIZ)) {
            frm.setExtendedState(Frame.MAXIMIZED_HORIZ);
            setDescription("Do the frame (window) titled " + frm.getTitle()
                    + " get Maximized horizontally?");
            if (Asserting() == false) {
                return failed(getFailureReason());
            }
        }
        if (tk.isFrameStateSupported(Frame.MAXIMIZED_VERT)) {
            frm.setExtendedState(Frame.MAXIMIZED_VERT);
            setDescription("Do the frame (window) titled " + frm.getTitle()
                    + " get Maximized vertically?");
            if (Asserting() == false) {
                return failed(getFailureReason());
            }
        }
        if (tk.isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            frm.setExtendedState(Frame.MAXIMIZED_BOTH);
            setDescription("Do the frame (window) titled " + frm.getTitle()
                    + " get fully Maximized (vertically/horizontally)?");
            setScreenshot("pic");
            if (Asserting() == false) {
                return failed(getFailureReason());
            }
        }
        return passed();
    }
}