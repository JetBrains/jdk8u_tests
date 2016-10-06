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

package org.apache.harmony.test.func.api.java.awt.EventQueue;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.MenuComponent;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class EventQueueTest extends AutonomousTest {
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
        frm = new Frame("My Frame");

        btnClose = new Button("MyClose");
        btnDoNothing = new Button("MyLoafer");

        /* WM_DESTROY is posted when button is clicked */
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String action = ae.getActionCommand();
                if (action.equals("Close")) {
                    frm.dispose();
                    butActionListenerChanged = true;
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
        System.exit(new EventQueueTest().test(args));
    }

    /**
     * MyEventQueue class serves as a custom event queue that can be modified
     * upon the necessity of a particular Test
     */
    class MyEventQueue extends EventQueue {
        private boolean dispatchEventCalled = false;

        public boolean isDispatchEventCalled() {
            return dispatchEventCalled;
        }

        public void dispatchEvent(AWTEvent event) {
            dispatchEventCalled = true;
            super.dispatchEvent(event);
        }
    }

    class MyThread extends Thread {
        private boolean isCurrDispatchThread = false;

        public boolean getIsCurrDispatchThread() {
            return isCurrDispatchThread;
        }

        public void run() {
            isCurrDispatchThread = EventQueue.isDispatchThread();
            AutonomousTest.sleep(5000);
        }
    }

    /**
     * MyComponent extends Component class which is handaled differently by
     * EventQueue#dispatchEvent(AWTEvent) method
     */
    class MyButton extends Button {
        private boolean processEventCalled   = false;
        
        public MyButton(String str) {
            super(str);
            super.enableEvents(ComponentEvent.COMPONENT_RESIZED);
        }

        /* This method is called before dispatchEvent returns */
        public void processEvent(AWTEvent arg0) {
            processEventCalled = true;
            return;
        }  

        public boolean isProcessEventCalled() {
            return processEventCalled;
        }
       
    }

    /**
     * MyMenuComponent extends MenuComponent class which is handaled differently
     * by EventQueue#dispatchEvent(AWTEvent) method
     */
    class MyMenuComponent extends MenuComponent {
        private boolean processEventCalled = false;

        /* This method is called before dispatchEvent returns */
        public void processEvent(AWTEvent arg0) {
            processEventCalled = true;
            return;
        }

        public boolean isProcessEventCalled() {
            return processEventCalled;
        }
    }

    /**
     * MyInvocationEvent implements ActiveEvent interface which is handaled
     * differently by EventQueue#dispatch() method
     */
    class MyInvocationEvent extends InvocationEvent {
        private boolean dispatchCalled = false;
        public MyInvocationEvent(Object source, Runnable runnable) {
            super(source, runnable);
        }

        public void dispatch() {
            dispatchCalled = true;
        }

        public boolean isDispatchCalled() {
            return dispatchCalled;
        }
    }

    public Result testEventQueue_dispatchEvent() {
        MyEventQueue myeq = new MyEventQueue();

        /* Component event */
        MyButton myComp = new MyButton("Temp");
        ComponentEvent ce = new ComponentEvent(myComp,
                ComponentEvent.COMPONENT_SHOWN);
        myeq.dispatchEvent(ce);
        waitEventQueueToClear();
        return passed();
    }

    public Result testEventQueue_postEvent() {

        /* Component event */
        MyButton myBtn = new MyButton("Temp");
        Rectangle r = btnClose.getBounds();
        r.y = r.y + 35;
        myBtn.setBounds(r);
        frm.setVisible(true);

        /* Get panel object */
        Panel pnl = (Panel) (frm.getComponents()[0]);
        pnl.add(myBtn);

        /*
         * For some reason letting other events into the queue
         * (COMPONENT_RESIZED, see next line) pushes previous (in our case
         * COMPONENT_SHOWN) out
         */
        myBtn.setSize(101, 22);
        waitEventQueueToClear();
        sleep(getPaintTimeout());
        if (myBtn.isProcessEventCalled() == false) {
            return failed("\n COMPONENT_SHOWN event has not been handled");
        }
        
        waitEventQueueToClear();
        return passed();
    }

    public Result testEventQueue_isDispatchThread() {
        MyThread myth_1 = new MyThread();
        MyThread myth_2 = new MyThread();
        if (EventQueue.isDispatchThread() == true) {
            return failed("\n Current calling thread is not AWT EventQueue's "
                    + "dispatch thread");
        }
        myth_1.start();
        EventQueue.invokeLater(myth_2);
        sleep();
        if (myth_1.getIsCurrDispatchThread() == true) {
            return failed("\n This custom created thread is not AWT "
                    + "EventQueue's dispatch thread");
        }
        if (myth_2.getIsCurrDispatchThread() == false) {
            return failed("\n This custom created thread is AWT "
                    + "EventQueue's dispatch thread");
        }
        return passed();
    }

    public Result testEventQueue_getPeekEvent() {
        MyEventQueue myeq = new MyEventQueue();
        MyButton myBtn = new MyButton("Temp");

        AWTEvent ae = myeq.peekEvent();
        sleep(getPaintTimeout());
        /* There are no events in the queue to this moment */
        if (ae != null) {
            return failed("\n There must be no events in the queue");
        }
        ComponentEvent ceHidden = new ComponentEvent(myBtn,
                ComponentEvent.COMPONENT_HIDDEN);
        /* Posting one event */
        myeq.postEvent(ceHidden);
        
        sleep(getPaintTimeout());
        /* We must get ceHidden event */
        ComponentEvent ceReceived = (ComponentEvent) myeq.peekEvent();

        if (ceReceived == null) {
            return failed("\n No event has been peeked up from the queue");
        }
        if (ceReceived.getSource().getClass().getName().indexOf("MyButton") < 0
                || ceReceived.getID() != ComponentEvent.COMPONENT_HIDDEN) {
            return failed("\n Wrong event has been peeked up from the queue");
        }
        sleep(getPaintTimeout());
        ComponentEvent ceExtracted = null;
        try {
            ceExtracted = (ComponentEvent) myeq.getNextEvent();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sleep(getPaintTimeout());
        if (ceExtracted.getSource().getClass().getName().indexOf("MyButton") < 0
                || ceReceived.getID() != ComponentEvent.COMPONENT_HIDDEN) {
            return failed("\n Wrong event has been extracted from the queue");
        }

        ae = myeq.peekEvent();

        /* There are no events in the queue to this moment */
        if (ae != null) {
            return failed("\n All events have been extracted. There must be "
                    + "no events in the queue");
        }

        /* unblocking thread */
        myeq
                .postEvent(new ComponentEvent(myBtn,
                        ComponentEvent.COMPONENT_SHOWN));
        return passed();
    }
}