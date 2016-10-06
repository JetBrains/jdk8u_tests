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
import java.awt.Color;
import java.awt.Container;
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

import org.apache.harmony.test.func.api.share.InteractiveTest;
import org.apache.harmony.share.Result;

public class DialogInteractiveTest extends InteractiveTest {
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
    // protected final int myColor = 50;

    protected void setUp() {
        frm = new Frame("My Frame");
        dlg = new Dialog(frm, "My Dialog");
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
        dlg.setBackground(Color.WHITE);

    }
    protected void tearDown() {
        dlg.dispose();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) {
        // File f = new File("C:\\temp\\toDelete");
        // DialogInteractiveTest dlgTest = new DialogInteractiveTest();
        // dlgTest.setUp();
        // dlgTest.setTestDir(f);
        // dlgTest.test(args);
        // dlgTest.tearDown();
        // System.exit(0);
        System.exit(new DialogInteractiveTest().test(args));
    }

    public Result testDialog_visibility() {

        dlg.show();
        setDescription("Is the dialog (window) titled " + dlg.getTitle()
                + " visible?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        /* Get displayable property of "btnClose" button */
        boolean isDlgCompDisplayable = ((Container) dlg.getComponents()[0])
                .getComponents()[0].isDisplayable();
        if (dlg.isDisplayable() == false) {
            return failed("The Dialog should be displayable after show()");
        }
        if (isDlgCompDisplayable == false) {
            return failed("The button on the Dialog should be displayable "
                    + " after show() on the Dialog");
        }

        dlg.hide();
        setDescription("The dialog (window) must not be visible right now");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        dlg.show();
        dlg.dispose();

        isDlgCompDisplayable = ((Container) dlg.getComponents()[0])
                .getComponents()[0].isDisplayable();
        if (dlg.isDisplayable() == true) {
            return failed("The Dialog should NOT be displayable after "
                    + " dispose()");
        }
        if (isDlgCompDisplayable == true) {
            return failed("The button on the Dialog should NOT be displayable "
                    + " after dispose() on the Dialog");
        }

        return passed();
    }
}