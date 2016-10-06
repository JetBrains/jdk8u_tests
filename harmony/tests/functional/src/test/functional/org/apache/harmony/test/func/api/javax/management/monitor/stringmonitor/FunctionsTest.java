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
package org.apache.harmony.test.func.api.javax.management.monitor.stringmonitor;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.monitor.StringMonitor;

import org.apache.harmony.test.func.api.javax.management.monitor.share.NotifListener;
import org.apache.harmony.test.func.api.javax.management.monitor.share.Timer;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * This test intended to verify functionality of string monitor<br>
 * <br>
 * testBasicFunctionating:<br>
 * This test is intended to check basic functionality of StringMonitor class in
 * case when both string-to-compare and observed string are not empty and their
 * length is not boundary.
 * <ul>
 * <li>MBeanServer is created; StringMonitor and MyStringBuffer MBeans are
 * registered.
 * <li>StringMonitor is configured: only notifyMatch flag is set to true,
 * string-to-compare is common non-empty string.
 * <li> NotificationListener is added to monitor.
 * <li> NotificationListener checks that string-to-compare is equal to string
 * being observed.
 * <li> StringMonitor is reconfigured then to check the functionality when only
 * notifyDiffer flag is set.
 * <li>NotificationListener waits for next notification.
 * <li>If received notification is correct, StrngMonitor is reconfigured to
 * check the functionality when both notifyMatch and notifyDiffer flags are set.
 * <li> NotificationListener waits for next notification.
 * </ul>
 * 
 * testOnTheFly:<br>
 * This test is intended to check basic functionality of StringMonitor class in
 * case when both string-to-compare and observed string are not empty and their
 * length is not boundary, but observed value changes on-the fly: MyStringBuffer
 * changes it every 10 seconds. Changes corresponds to the pattern
 * "m-d-m-m-d-d-d-d-m" (m is matches with string to compare, d is differs)
 * <ul>
 * <li>MBeanServer is created; StringMonitor and MyStringBuffer MBeans are
 * registered.
 * <li>StringMonitor is configured: only notifyMatch flag is set to true,
 * string-to-compare is common non-empty string.
 * <li> NotificationListener is added to monitor.
 * <li> According to pattern there are 3 notifications to be sent.
 * <li> NotificationListener waits for 3 next notifications.
 * <li> Only notifyDiffer flag is set.
 * <li> According to pattern there are 2 notifications to be sent.
 * <li> NotificationListener waits for 2 next notifications.
 * <li>NotificationListener waits for next notification.
 * <li>If received notification is correct, StrngMonitor is reconfigured to
 * check the functionality when both notifyMatch and notifyDiffer flags are set.
 * <li> According to pattern there are 5 notifications to be sent.
 * <li> NotificationListener waits for 5 next notifications.
 * </ul>
 * 
 * testIrregularValues:<br>
 * This test is intended to check the functionality of StringMonitor in case
 * when string-to-compare and observed string can be assigned with boundary
 * values (null and "")<br>
 * <ul>
 * <li>MBeanServer is created; StringMonitor and MyStringBuffer aMBeans are
 * registered.
 * <li>StringMonitor is configured: only notifyMatch flag is set to true,
 * string-to-compare is common non-empty string.
 * <li>NotificationListener is added to monitor.
 * <li>MyStringBuffer is started in a separate thread, monitor is activated
 * (empty and null strings are possible values for observed string).
 * <li>NotificationListener checks that string-to-compare is equal to string
 * being observed.
 * <li>StringMonitor is reconfigured (string-to-compare is empty).
 * <li>NotificationListener waits for next notification.
 * <li>If correct notification is received test considered to be passed.
 * </ul>
 * 
 */

public class FunctionsTest extends MultiCase {

    /** Object for synchronization */
    private final Object sync = new Object();

    /** Timer used for prevent test from hangs */
    private Timer timer;

    /** Flag to test result */
    boolean res = true;

    private ObjectName mySbName;

    private MyStringBuffer mySb = new MyStringBuffer();

    private ObjectName monitorName;

    private StringMonitor monitor;

    private MBeanServer server;

    NotifListener nListener = new NotifListener(sync);

    public static void main(String[] args) {
        System.exit(new FunctionsTest().test(args));
    }

    protected void tearDown() {
        monitor.stop();
    }

    /** Actions before every test case (common) */
    protected void setUp() {

        res = true;
        mySb.setString("match");

        server = MBeanServerFactory.createMBeanServer();
        monitor = new StringMonitor();

        try {
            monitorName = new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "stringMonitor:type=StringMonitor,id=1");
            mySbName = new ObjectName(MyStringBuffer.MSB_NAME_TEMPLATE + "1");

            server.registerMBean(monitor, monitorName);
            server.registerMBean(mySb, mySbName);

        } catch (Throwable e) {
            e.printStackTrace();
            log.add("Test: ERROR: Internal error!");
            System.exit(106);
        }

        monitor.addNotificationListener(nListener, null, "handback");

        monitor.addObservedObject(mySbName);
        monitor.setObservedAttribute("String");
        monitor.setNotifyMatch(true);
        monitor.setNotifyDiffer(true);
        monitor.setGranularityPeriod(100);

    }

    public Result testIrregularValues() throws Exception {

        /* Monitor is configured in setUp method */
        monitor.setStringToCompare("");

        /*
         * Now test begins. String to compare matches to observed attribute, so
         * "matches" notification should be sent. Due to NotifyDiffer
         * flag=false, "differs" notifications shouldn't be sent
         */
        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.differs");

        log.add("Test: Waiting for 2 notifications " + "for empty string");

        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.matches");

        enableTimer();
        mySb.setString("");
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        log.add("Test: Waiting for error notification for null string");

        monitor.setStringToCompare("null");

        nListener.setParameters("String", mySbName, "jmx.monitor.error.type");

        enableTimer();
        mySb.setString(null);
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        return res ? passed() : failed("");
    }

    /**
     * This test is intended to check basic functionality of StringMonitor class
     * in case when both string-to-compare and observed string are not empty and
     * their length is not boundary.
     * <ul>
     * <li>MBeanServer is created; StringMonitor and MyStringBuffer MBeans are
     * registered.
     * <li>StringMonitor is configured: only notifyMatch flag is set to true,
     * string-to-compare is common non-empty string.
     * <li> NotificationListener is added to monitor.
     * <li> NotificationListener checks that string-to-compare is equal to
     * string being observed.
     * <li> StringMonitor is reconfigured then to check the functionality when
     * only notifyDiffer flag is set.
     * <li>NotificationListener waits for next notification.
     * <li>If received notification is correct, StrngMonitor is reconfigured to
     * check the functionality when both notifyMatch and notifyDiffer flags are
     * set.
     * <li> NotificationListener waits for next notification.
     * </ul>
     */
    public Result testBasicFunctionating() throws Throwable {

        monitor.setStringToCompare("match");

        /*
         * Now test begins. String to compare matches to observed attribute, so
         * "matches" notification should be sent. Due to NotifyDiffer
         * flag=false, "differs" notifications shouldn't be sent
         */
        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.matches");

        log.add("Test: Waiting for \"matches\" notification");

        monitor.setNotifyDiffer(false);

        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        log.add("Test: NotifyDiffer flag = false,"
                + " so \"differs\" notifications shouldn't be sent");
        mySb.setString("differ");
        enableTimer();
        synchronized (sync) {
            sync.wait();
        }
        res &= !(timer.getRes());
        timer.interrupt();

        /*
         * Now change value of observed attribute and try to receive "differs"
         * notification
         */
        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.differs");

        log.add("Test: Waiting for \"differs\" notification");

        monitor.setNotifyMatch(false);
        monitor.setNotifyDiffer(true);

        enableTimer();
        monitor.setStringToCompare("match");
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        log.add("Test: Second \"differs\" notification"
                + " after setStringToCompare() should be sent");

        enableTimer();
        monitor.setStringToCompare("match");
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        timer.interrupt();

        log.add("Test: NotifyDiffer flag = false,"
                + " so \"matches\" notifications shouldn't be sent");

        enableTimer();
        mySb.setString("match");
        synchronized (sync) {
            sync.wait();
        }
        res &= !(timer.getRes());
        timer.interrupt();

        /* Now set both NotifyMatch and NotifyDiffer flags */
        monitor.setNotifyMatch(true);
        monitor.setNotifyDiffer(true);

        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.differs");

        log.add("Test: Waiting for \"differs\" notification");

        enableTimer();
        mySb.setString("differ");
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.matches");

        log.add("Test: Waiting for \"matches\" notification");

        enableTimer();
        mySb.setString("match");
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        return res ? passed() : failed("");
    }

    /**
     * This test is intended to check basic functionality of StringMonitor class
     * in case when both string-to-compare and observed string are not empty and
     * their length is not boundary, but observed value changes on-the fly:
     * MyStringBuffer changes it every 10 seconds. Changes corresponds to the
     * pattern "m-d-m-m-d-d-d-d-m" (m is matches with string to compare, d is
     * differs)
     * <ul>
     * <li>MBeanServer is created; StringMonitor and MyStringBuffer MBeans are
     * registered.
     * <li>StringMonitor is configured: only notifyMatch flag is set to true,
     * string-to-compare is common non-empty string.
     * <li> NotificationListener is added to monitor.
     * <li> According to pattern there are 3 notifications to be sent.
     * <li> NotificationListener waits for 3 next notifications.
     * <li> Only notifyDiffer flag is set.
     * <li> According to pattern there are 2 notifications to be sent.
     * <li> NotificationListener waits for 2 next notifications.
     * <li>NotificationListener waits for next notification.
     * <li>If received notification is correct, StrngMonitor is reconfigured to
     * check the functionality when both notifyMatch and notifyDiffer flags are
     * set.
     * <li> According to pattern there are 5 notifications to be sent.
     * <li> NotificationListener waits for 5 next notifications.
     * </ul>
     */
    public Result testOnTheFly() throws Throwable {

        monitor.setStringToCompare("match");
        mySb.setString("match");

        /*
         * Now test begins. String to compare matches to observed attribute, so
         * "matches" notification should be sent. Due to NotifyDiffer
         * flag=false, "differs" notifications shouldn't be sent
         */
        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.matches");

        System.err.println("Test: Waiting for \"matches\" notification");

        monitor.setNotifyMatch(true);
        monitor.setNotifyDiffer(false);
        monitor.start();

        Thread mySBThread1 = new Thread(mySb);
        mySBThread1.setDaemon(true);
        mySBThread1.start();

        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();
        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();
        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();

        /*
         * Now change value of observed attribute and try to receive "differs"
         * notification
         */

        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.differs");

        monitor.setStringToCompare("match");
        System.err.println("Test: Waiting for \"differs\" notification");

        monitor.setNotifyMatch(false);
        monitor.setNotifyDiffer(true);

        Thread mySBThread2 = new Thread(mySb);
        mySBThread2.setDaemon(true);
        mySBThread2.start();

        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();
        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();

        /* Now set both NotifyMatch and NotifyDiffer flags */

        monitor.setNotifyMatch(true);
        monitor.setNotifyDiffer(true);

        System.err.println("Test: Waiting for notifications of both types");

        monitor.removeObservedObject(mySbName);
        server.unregisterMBean(mySbName);
        mySb = new MyStringBuffer();
        server.registerMBean(mySb, mySbName);
        monitor.addObservedObject(mySbName);
        Thread mySBThread3 = new Thread(mySb);
        mySBThread3.setDaemon(true);
        mySBThread3.start();

        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.matches");
        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();
        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.differs");
        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();
        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.matches");
        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();
        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.differs");
        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();
        nListener.setParameters("String", mySbName,
                "jmx.monitor.string.matches");
        enableTimer();
        synchronized (sync) {
            sync.wait();
            res &= timer.getRes();
            res &= nListener.getRes();
        }
        timer.interrupt();

        return res ? passed() : failed("");

    }

    /**
     * This method used for shorten and more simplicity. It just started timer
     * thread
     */
    private void enableTimer() {
        timer = new Timer(5000, sync);
        timer.setDaemon(true);
        timer.start();
    }

}
