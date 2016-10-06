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
package org.apache.harmony.test.func.api.java.awt.Color;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.apache.harmony.test.func.api.share.InteractiveTest;
import org.apache.harmony.share.Result;

public class ColorInteractiveTest extends InteractiveTest {

    protected Frame frm;

    protected boolean butActionListenerChanged = false;

    protected final int WIN_WIDTH = 420;

    protected final int WIN_HEIGHT = 240;

    protected final int WIN_X = 150;

    protected final int WIN_Y = 150;

    protected Button btn1;

    protected Button btn2;

    protected Dimension winDimension = new Dimension(WIN_WIDTH, WIN_HEIGHT);

    protected Dimension smallWinDimension = new Dimension(WIN_WIDTH / 4,
            WIN_HEIGHT / 4);

    protected Point winLocation = new Point(WIN_X, WIN_Y);

    protected void setUp() {
        frm = new Frame("jawa.awt.Color.init(int,int,int,int) testing window.");
        btn1 = new Button("Button 1");
        btn2 = new Button("Button 2");

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
        btn1.setBounds(10, 10, 80, 22);
        btn2.setBounds(100, 10, 80, 22);

        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setSize(winDimension);
        panel.add(btn1);
        panel.add(btn2);

        frm.add(panel);

        /* Handler for WM_DESTROY message */
        frm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frm.dispose();
                //System.exit(0);
            }
        });

        frm.setSize(winDimension);
        frm.setLocation(winLocation);

    }

    protected void tearDown() {
        frm.dispose();
    }

    public static void main(String[] args) {
        System.exit(new ColorInteractiveTest().test(args));
    }

    public Result testColor_init_iii() {

        Color black = new Color(0, 0, 0);
        Color white = new Color(255, 255, 255);
        Color red = new Color(255, 0, 0);
        Color green = new Color(0, 255, 0);
        Color blue = new Color(0, 0, 255);

        //setUp();

        frm.setBackground(black);
        frm.setVisible(true);
        setDescription("Do you see the window with BLACK background "
                + "and two white bottons?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        frm.setVisible(false);
        frm.setBackground(white);
        frm.setVisible(true);
        setDescription("Do you see the window with WHITE background "
                + "and two white bottons?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        frm.setVisible(false);
        frm.setBackground(red);
        frm.setVisible(true);
        setDescription("Do you see the window with RED background "
                + "and two white bottons?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        frm.setVisible(false);
        frm.setBackground(green);
        frm.setVisible(true);
        setDescription("Do you see the window with GREEN background "
                + "and two white bottons?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        frm.setVisible(false);
        frm.setBackground(blue);
        frm.setVisible(true);
        setDescription("Do you see the window with BLUE background "
                + "and two white bottons?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        frm.setVisible(false);
        frm.setBackground(black);
        btn1.setBackground(red);
        btn2.setBackground(red);
        frm.setVisible(true);
        setDescription("Do you see the window with BLACK background and"
                + " two red bottons?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        frm.setVisible(false);
        frm.setBackground(black);
        btn1.setBackground(red);
        btn2.setBackground(white);
        frm.setVisible(true);
        setDescription("Do you see the window with BLACK background and"
                + " left red botton and right white botton?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        frm.setVisible(false);
        frm.setBackground(white);
        btn1.setBackground(blue);
        btn2.setBackground(green);
        frm.setVisible(true);
        setDescription("Do you see the window with WHITE background and"
                + " left blue botton and right green botton?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        return passed();
    }
}
