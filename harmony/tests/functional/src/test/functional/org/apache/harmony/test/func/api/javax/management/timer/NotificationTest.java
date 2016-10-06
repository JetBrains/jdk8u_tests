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
package org.apache.harmony.test.func.api.javax.management.timer;

import java.util.ArrayList;
import java.util.Date;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.timer.TimerNotification;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class NotificationTest extends MultiCase implements NotificationListener {

    /**
     * The MBeanServer
     */
    MBeanServer server;

    /**
     * Received notifications
     */
    ArrayList receivedNtf = new ArrayList();

    /**
     * The object name of the timer
     */
    ObjectName timerTest;

    /**
     * Test for single notification.
     */
    public Result testBaseNotification() throws Exception {
        try {
            startTimer();
            Integer id = addNotification(timeCalc(LibTimer.PERIOD), 0, 1);
            waitNotification(1, LibTimer.WAIT);
            if (getType(id) != null) {
                fail("notification should not be registered");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopTimer();
        }
        return result();
    }

    /**
     * Test for not single notification.
     */
    public Result testBaseNotification2() throws Exception {
        try {
            startTimer();
            Integer id = addNotification(timeCalc(LibTimer.PERIOD),
                    LibTimer.PERIOD, 0);
            waitNotification(1, LibTimer.WAIT);
            waitNotification(2, LibTimer.WAIT);
            waitNotification(3, LibTimer.WAIT);

            if (getType(id) == null) {
                fail("notification do not registered");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopTimer();
        }
        return result();
    }


    /**
     * Test for two notification.
     */
    public Result testTwoConcurrentNotification() throws Exception {

        startTimer();

        long firsrtTime = 2000;
        long secondTime = 6000;
        long wait = 12000;
        long start = timeCalc(firsrtTime);

        Integer id1 = addNotification(start + firsrtTime, firsrtTime, 0);
        Integer id2 = addNotification(start + secondTime, secondTime, 0);
        waitNotification(1, wait);
        waitNotification(2, wait);
        TimerNotification ntf1 = (TimerNotification) receivedNtf.get(0);
        TimerNotification ntf2 = (TimerNotification) receivedNtf.get(1);
        if ((!ntf1.getNotificationID().equals(id1)) && (id1 == null)) {
            fail("NotificationID is null or have unexpected value ");
        }
        if ((!ntf2.getNotificationID().equals(id1)) && (id2 == null)) {
            fail("NotificationID is null or have unexpected value ");
        }

        long difference = ntf2.getTimeStamp() - ntf1.getTimeStamp();
        if (difference < (firsrtTime - (firsrtTime / 10))
                || difference > (firsrtTime + (firsrtTime / 10))) {
            fail("Unexpected Time between notification = " + firsrtTime);
        }
        return result();
    }

    /**
     * Waiting notification
     */
    private void waitNotification(int expected, long wait) throws Exception {
        synchronized (receivedNtf) {
            if (receivedNtf.size() < expected) {
                receivedNtf.wait(wait);
            }
            assertEquals(expected, receivedNtf.size());
        }
    }

    private String getType(Integer id) throws Exception {
        Thread.yield();
        return (String) server.invoke(timerTest, "getNotificationType",
                new Object[] { id }, new String[] { "java.lang.Integer" });
    }

    /**
     * Adding notifications to the timer service
     */
    private Integer addNotification(long offset, long period, long nbOccurences)
            throws Exception {
        return (Integer) server.invoke(timerTest, "addNotification",
                new Object[] { LibTimer.TIMER_TYPE, LibTimer.TEST_MESSAGE,
                        LibTimer.TEST_DATA, new Date(offset), new Long(period),
                        new Long(nbOccurences), new Boolean(false) },
                new String[] { "java.lang.String", "java.lang.String",
                        "java.lang.Object", "java.util.Date", "long", "long",
                        "boolean" });
    }

    private long timeCalc(long step) {
        return (System.currentTimeMillis() + step);
    }

    /**
     * Start of the timer service
     */
    private void startTimer() throws Exception {
        server = MBeanServerFactory.createMBeanServer("Timer");
        timerTest = new ObjectName("Timer:type=TimerService");
        server.createMBean("javax.management.timer.Timer", timerTest,
                new Object[0], new String[0]);
        server.invoke(timerTest, "start", new Object[0], new String[0]);
        receivedNtf.clear();
        server.addNotificationListener(timerTest, this, null, null);
    }

    /**
     * Stop of the timer service
     */
    private void stopTimer() {
        try {
            server.invoke(timerTest, "removeAllNotifications", new Object[0],
                    new String[0]);
            server.invoke(timerTest, "stop", new Object[0], new String[0]);
            server.unregisterMBean(timerTest);
            MBeanServerFactory.releaseMBeanServer(server);
        } catch (Exception ignored) {
        }
    }

    /**
     * Adding notification to the collection
     */
    public void handleNotification(Notification notification, Object handback) {
        synchronized (receivedNtf) {
            receivedNtf.add(notification);
            receivedNtf.notifyAll();
        }
    }

    public static void main(String[] args) {
        try {
            System.exit(new NotificationTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}