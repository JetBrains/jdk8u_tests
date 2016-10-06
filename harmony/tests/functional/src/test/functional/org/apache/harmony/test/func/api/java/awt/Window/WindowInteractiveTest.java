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

import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import org.apache.harmony.test.func.api.share.InteractiveTest;
import org.apache.harmony.share.Result;

public class WindowInteractiveTest extends InteractiveTest {
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

    private boolean     eventRecieved            = false;

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
        btnClose.setBounds(10, 10, 250, 25);
        btnDoNothing.setBounds(10, 80, 250, 25);

        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setSize(winDimension);
        panel.add(btnClose);
        panel.add(btnDoNothing);

        wnd.add(panel);

        /* Handler for WM_DESTROY message */
        frm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frm.dispose();
                // System.exit(0);
            }
        });

        wnd.setSize(winDimension);
        wnd.setLocation(winLocation);
        wnd.setBackground(Color.WHITE);

    }
    protected void tearDown() {
        wnd.dispose();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) {
        System.exit(new WindowInteractiveTest().test(args));
    }

    public Result testWindow_windowListener() {

        class TempWnd extends Window implements Runnable {
            private int waitingTime;
            public TempWnd(Frame frm, int time) {
                super(frm);
                waitingTime = time;
                setBackground(Color.BLUE);
                super.enableEvents(WindowEvent.WINDOW_CLOSING);
            }

            public void run() {
                setSize(smallWinDimension);
                setVisible(true);
                sleep(waitingTime);
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

                /* Make sure our event has been processed */
                waitEventQueueToClear();
                sleep(getPaintTimeout());
            }
        }

        TempWnd tempWnd1 = new TempWnd(frm, 5000);
        tempWnd1.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                eventRecieved = true;
                /* Close our temp window */
                e.getWindow().dispose();
            }
        });

        /* WindowListener is attached to this window */
        Thread tempWndThread1 = new Thread(tempWnd1);
        tempWndThread1.start();
        setDescription("The little blue window must become closed within 5 seconds");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        /*
         * Additional check. If you click "Yes" before the moment when
         * WINDOW_CLOSING event has been processed this check will fail
         */
        if (eventRecieved == false) {
            return failed("Event has NOT been processed by the WindowListener");
        }

        WindowAdapter myAdapter = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                /* Close our tem window */
                e.getWindow().dispose();
            }
        };

        TempWnd tempWnd2 = new TempWnd(frm, 0);
        tempWnd2.setBackground(Color.YELLOW);
        tempWnd2.addWindowListener(myAdapter);

        /* Right after this line no events are processed by WindowListener */
        tempWnd2.removeWindowListener(myAdapter);

        /* Clear eventRecieved flag */
        eventRecieved = false;

        /*
         * WindowListener has been deattached to this window this is why
         * eventRecieved must NOT be set to true anymore, events are no longer
         * processed by WindowListener
         */
        Thread tempWndThread2 = new Thread(tempWnd2);
        tempWndThread2.start();
        waitEventQueueToClear();
        sleep(getPaintTimeout());

        setDescription("Do you (still) see the little yellow window?");

        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        /* Additional check */
        if (eventRecieved == true) {
            return failed("Event has been processed by the removed WindowListener");
        }
        /* Clean up */
        tempWnd1.dispose();
        tempWnd2.dispose();

        return passed();
    }

    public Result testWindow_setCursor() {
        Cursor curHourGlass = new Cursor(Cursor.WAIT_CURSOR);
        Cursor curCrossHair = new Cursor(Cursor.CROSSHAIR_CURSOR);
        Cursor curTextCursor = new Cursor(Cursor.TEXT_CURSOR);
        wnd.setCursor(curHourGlass);
        setDescription("Place cursor within the window, it should turn "
                + "into hour glass.");
        wnd.setVisible(true);

        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        wnd.setCursor(curCrossHair);
        setDescription("Place cursor within the window, it should turn "
                + "into crosshair.");
        wnd.setVisible(true);
        wnd.validate();

        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        wnd.setCursor(curTextCursor);
        setDescription("Place cursor within the window, it should turn "
                + "into Text cursor.");
        wnd.setVisible(true);
        wnd.validate();

        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        wnd.dispose();
        return passed();
    }

    public Result testWindow_setLocationRelativeTo() {
        class TempWnd extends Window {
            public TempWnd(Frame frm) {
                super(frm);
                setBackground(Color.BLUE);
            }
        }

        wnd.setVisible(true);
        TempWnd tempWnd = new TempWnd(frm);
        tempWnd.setSize(smallWinDimension);
        tempWnd.setLocationRelativeTo(btnDoNothing);
        tempWnd.setVisible(true);
        setDescription("The location of the blue window is set relative to the "
                + "\"" + btnDoNothing.getLabel() + "\" button");

        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        tempWnd.setLocationRelativeTo(btnClose);
        setDescription("The location of the blue window is set relative to the "
                + "\"" + btnClose.getLabel() + "\" button");
        wnd.toFront();
        tempWnd.toFront();
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        tempWnd.dispose();
        wnd.dispose();
        return passed();
    }

    public Result testWindow_toFront() {
        class TempWnd extends Window {

            public TempWnd(Frame frm) {
                super(frm);
                setBackground(Color.BLUE);
            }
        }

        TempWnd tempWnd = new TempWnd(frm);
        tempWnd.setSize(winDimension);
        tempWnd.setLocation(WIN_X + 100, WIN_Y + 100);
        wnd.setBackground(Color.WHITE);
        wnd.setVisible(true);
        tempWnd.setVisible(true);
        wnd.toFront();

        setDescription("The window with WHITE background is displayed "
                + "in front of the window with BLUE background");

        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        tempWnd.setVisible(true);
        wnd.setVisible(true);
        tempWnd.toFront();

        setDescription("The window with BLUE background is displayed "
                + "in front of the window with WHITE background");

        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        tempWnd.dispose();
        wnd.dispose();
        return passed();
    }

}