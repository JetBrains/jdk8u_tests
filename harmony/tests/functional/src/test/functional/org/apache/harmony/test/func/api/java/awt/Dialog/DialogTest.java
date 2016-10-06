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

package org.apache.harmony.test.func.api.java.awt.Dialog;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class DialogTest extends AutonomousTest {
    static File         testDir;
    protected Frame     frm;
    protected Dialog    dlg;
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
        frm = new Frame("My Frame");
        dlg = new Dialog(frm);
        btnClose = new Button("MyClose");
        btnDoNothing = new Button("MyLoafer");

        /* WM_DESTROY is posted when button is clicked */
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String action = ae.getActionCommand();
                if (action.equals("Close")) {
                    dlg.dispose();
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

        dlg.add(panel);

        /* Handler for WM_DESTROY message */
        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dlg.dispose();
            }
        });

        dlg.setSize(winDimension);

    }
    
    protected void tearDown() {
        dlg.dispose();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) {
        System.exit(new DialogTest().test(args));
    }

    public Result testDialog_appearance() {

        String myDialogTitle = new String("My Dialog");
        /* Create modal dialog */
        Dialog tempDlg = new Dialog(frm, myDialogTitle, true, frm
                .getGraphicsConfiguration());

        /*
         * javadoc does not say this condition is true by default, but it is a
         * common practice to have Frame resizable
         */
        if (tempDlg.isResizable() == false) {
            return failed("\nFrame must be resizble by default");
        }
        tempDlg.setResizable(false);
        if (tempDlg.isResizable() == true) {
            return failed("\nFrame is resizble after setResizable(false)");
        }

        /*
         * javadoc does not say that by default the frame is initially decorated
         */
        if (tempDlg.isUndecorated() == true) {
            return failed("\nFrame must be decorated by default");
        }
        tempDlg.setUndecorated(true);
        if (tempDlg.isUndecorated() == false) {
            return failed("\nFrame is decorated after setUndecorated(true)");
        }

        if (tempDlg.getTitle().equals(myDialogTitle) == false) {
            return failed("\ngetTitle() returned incorrect frame title");
        }
        if (tempDlg.isModal() == false) {
            return failed("\nisModal() returned false on modal dialog");
        }
        tempDlg.setModal(false);
        if (tempDlg.isModal() == true) {
            return failed("\nisModal() returned true on NON-modal dialog");
        }
        return passed();
    }

    public Result testDialog_setTitle() {
        
        String myDialogTitle = new String("My Dialog");
        /* Create modal dialog */
        Dialog tempDlg = new Dialog(frm, myDialogTitle, true);
        Dialog testedTempDlg = new Dialog(tempDlg, myDialogTitle, false,
                tempDlg.getGraphicsConfiguration());

        if (testedTempDlg.isModal() == true) {
            return failed("\nisModal() returned true on modal dialog");
        }

        if (testedTempDlg.getTitle().equals(myDialogTitle) == false) {
            return failed("\ngetTitle() returned incorrect frame title");
        }

        myDialogTitle = "my$ new@ title^()_!\"';:";
        testedTempDlg.setTitle(myDialogTitle);

        if (testedTempDlg.getTitle().equals(myDialogTitle) == false) {
            return failed("\ngetTitle() returned incorrect frame title after "
                    + "setTitle() was called");
        }
        return passed();
    }
}