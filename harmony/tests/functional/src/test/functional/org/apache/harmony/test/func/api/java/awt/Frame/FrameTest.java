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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class FrameTest extends AutonomousTest {
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
        frm = new Frame("MyApp");
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

    }
    protected void tearDown() {
        frm.dispose();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) {
        System.exit(new FrameTest().test(args));
    }

    public Result testFrame_appearance() {
        String myFrameTitle = new String("My Frame");
        GraphicsConfiguration grConf = frm.getGraphicsConfiguration();
        Frame tempfrm = new Frame(myFrameTitle, grConf);

        /*
         * javadoc does not say this condition is true by default, but it is a
         * common practice to have Frame resizable
         */
        if (tempfrm.isResizable() == false) {
            return failed("Frame must be resizble by default");
        }
        tempfrm.setResizable(false);
        if (tempfrm.isResizable() == true) {
            return failed("Frame is resizble after setResizable(false)");
        }

        /*
         * javadoc says that all frames are initially decorated
         */
        if (tempfrm.isUndecorated() == true) {
            return failed("Frame must be decorated by default");
        }
        tempfrm.setUndecorated(true);
        if (tempfrm.isUndecorated() == false) {
            return failed("Frame is decorated after setUndecorated(true)");
        }
        
        myFrameTitle = "my$ new@ title^()_!\"';:";
        tempfrm.setTitle(myFrameTitle);

        if (tempfrm.getTitle().equals(myFrameTitle) == false) {
            return failed("getTitle() returned incorrect frame title");
        }

        /* Create image 16x16 pixels */
        BufferedImage bufImage = new BufferedImage(16, 16,
                BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                bufImage.setRGB(i, j, Color.YELLOW.getRGB());

            }
        }
        frm.setIconImage(bufImage);
        BufferedImage retrievedImage = (BufferedImage) frm.getIconImage();

        /* Check retrieved image size */
        if (retrievedImage.getWidth() != bufImage.getWidth()
                || retrievedImage.getHeight() != bufImage.getHeight()
                || retrievedImage.getWidth() != 16
                || retrievedImage.getHeight() != 16) {
            return failed("The retieved image has incorrect");
        }

        /* Check the retrieved image RGB style */
        if (retrievedImage.getRGB(5, 5) != Color.YELLOW.getRGB()) {
            return failed("Pixel in the retieved image has incorrect color");
        }

        /* Check the retrieved image type */
        if (retrievedImage.getType() != BufferedImage.TYPE_INT_ARGB) {
            return failed("The retieved image has incorrect type");
        }

        return passed();
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
            sleep(getPaintTimeout());
            if (frm.getExtendedState() != Frame.NORMAL) {
                return failed("The frame is not in NORMAL state");
            }
        }        
        if (tk.isFrameStateSupported(Frame.ICONIFIED)) {
            frm.setExtendedState(Frame.ICONIFIED);
            sleep(getPaintTimeout());
            if (frm.getExtendedState() != Frame.ICONIFIED) {
                return failed("The frame is not in ICONIFIED state");
            }
        }
        if (tk.isFrameStateSupported(Frame.MAXIMIZED_HORIZ)) {
            frm.setExtendedState(Frame.MAXIMIZED_HORIZ);
            sleep(getPaintTimeout());
            if (frm.getExtendedState() != Frame.MAXIMIZED_HORIZ) {
                return failed("The frame is not in MAXIMIZED_HORIZ state");
            }
        }
        if (tk.isFrameStateSupported(Frame.MAXIMIZED_VERT)) {
            frm.setExtendedState(Frame.MAXIMIZED_VERT);
            sleep(getPaintTimeout());
            if (frm.getExtendedState() != Frame.MAXIMIZED_VERT) {
                return failed("The frame is not in MAXIMIZED_VERT state");
            }
        }
        if (tk.isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            frm.setExtendedState(Frame.MAXIMIZED_BOTH);
            sleep(getPaintTimeout());
            if (frm.getExtendedState() != Frame.MAXIMIZED_BOTH) {
                return failed("The frame is not in MAXIMIZED_BOTH state");
            }
        }
        return passed();
    }
}