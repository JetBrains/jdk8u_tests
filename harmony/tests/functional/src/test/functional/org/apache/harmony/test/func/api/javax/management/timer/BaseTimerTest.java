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
import javax.management.timer.Timer;
import java.util.Date;
import javax.management.*;
import javax.management.timer.TimerNotification;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class BaseTimerTest extends MultiCase implements NotificationListener {

    /**
     * The object name of the timer
     */
    ObjectName timerName;

    /**
     * The MBeanServer
     */
    MBeanServer server;

    /**
     * The Timer
     */
    Timer timer;

    int notifNbs = 0;

    /**
     * The timer notification identifier
     */
    Integer id;

    /**
     * Starting the timer service adding 100 notifications at 1 millsecond
     * intervals
     */
    public Result testBaseChk() throws Exception {

        startTimer();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++){
            addNotification(1000, 1, LibTimer.REPEATS);
        }
        LibTimer.sleep(1000);
        long end = System.currentTimeMillis();
        stopTimer();
        log.info("Timer counter millis - " + (end - start));
        return result();
    }

    /**
     * Adding and removing notifications in the timer service
     */
    public Result testRemoveNotifications() throws Exception {

        startTimer();
        timer.addNotification(LibTimer.TIMER_TYPE, LibTimer.TEST_MESSAGE,
                LibTimer.TEST_DATA,  timeCalc(1000), LibTimer.PERIOD,
                LibTimer.REPEATS);
        if (timer.isEmpty())
            return failed("notification do not added");
        timer.removeNotifications(LibTimer.TIMER_TYPE);
        if (!timer.isEmpty())
            return failed("notification do not removed");
        stopTimer();
        return result();
    }

    /**
     * Adding and removing All notifications in the timer service
     */
    public Result testRemoveAllNotifications() throws Exception {

        startTimer();
        for (int i = 0; i < 10; i++){
            addNotification(1000, 1, LibTimer.REPEATS);
        }
        LibTimer.sleep(1000);
        if (timer.isEmpty())
            return failed("notification do not added");
        timer.removeAllNotifications();
        if (!timer.isEmpty())
            return failed("notification do not removed");
        stopTimer();
        return result();
    }

    /**
     * Removing notifications from the timer service if timer notification type String = null
     */
    public Result testRemoveNullStringNotifications() throws Exception {
        String nullNotification = new String();
        try {
            startTimer();
            timer.removeNotifications(nullNotification);
        } catch (InstanceNotFoundException e) {
            return passed();
        } catch (Exception e) {
            return failed("Thrown wrong type of exception");
        } finally {
            stopTimer();
        }

        return failed("No exeptions");
    }

    /**
     * Adding notifications to the timer service with timer notification type String type =
     * null
     */
    public Result testaddNotificationParam1() throws Exception {
        try {
            startTimer();
            timer.addNotification(null, LibTimer.TEST_MESSAGE,
                    LibTimer.TEST_DATA, timeCalc(1000), LibTimer.PERIOD,
                    LibTimer.REPEATS);
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        } finally {
            stopTimer();
        }
        return result();
    }

    /**
     * Adding notifications to the timer service with parameter String message =
     * null
     */
    public Result testaddNotificationParam2() throws Exception {
        startTimer();
        timer.addNotification(LibTimer.TIMER_TYPE, null, LibTimer.TEST_DATA,
                timeCalc(1000), LibTimer.PERIOD, LibTimer.REPEATS);
        stopTimer();
        return result();
    }
    /**
     * Adding notifications to the timer service with parameter Object userData =
     * null
     */
    public Result testaddNotificationParam3() throws Exception {

        startTimer();
        timer.addNotification(LibTimer.TIMER_TYPE, LibTimer.TEST_MESSAGE, null,
                timeCalc(1000), LibTimer.PERIOD, LibTimer.REPEATS);
        stopTimer();
        return result();
    }

    /**
     * Adding notifications to the timer service with parameter Date date = null
     */
    public Result testaddNotificationParam4() throws Exception {
        try {
            startTimer();
            timer.addNotification(LibTimer.TIMER_TYPE,
                            LibTimer.TEST_MESSAGE, LibTimer.TEST_DATA, null,
                            LibTimer.PERIOD, LibTimer.REPEATS);
        } catch (IllegalArgumentException e) {
            return passed();
            
        } catch (Exception e) {
            return failed("Thorwn wrong type of exception");

        } finally {
            stopTimer();
        }
        return failed("No exeptions");
    }

    /**
     * Test for timer notification identifier
     */
    public Result testTimerNotificationIdentifier() throws Exception {

        startTimer();
        Date currentDate = timeCalc(1000);
        id = timer.addNotification(LibTimer.TIMER_TYPE, LibTimer.TEST_MESSAGE,
                LibTimer.TEST_DATA, currentDate, LibTimer.PERIOD,
                LibTimer.REPEATS, true);
        
        assertEquals(true,timer.isActive());
        assertEquals(currentDate,timer.getDate(id));
        assertEquals(LibTimer.REPEATS,timer.getNbOccurences(id).longValue());
        assertEquals(true,timer.getFixedRate(id).booleanValue());
        assertEquals(LibTimer.TEST_MESSAGE ,timer.getNotificationMessage(id));
        assertEquals(LibTimer.TIMER_TYPE,timer.getNotificationType(id)); 
        assertEquals(LibTimer.TEST_DATA,timer.getNotificationUserData(id));
        assertEquals(LibTimer.PERIOD,timer.getPeriod(id).longValue());
        timer.removeNotification(id);
        assertEquals(true,timer.isEmpty());
        stopTimer();
        assertEquals(false,timer.isActive());
        
        return result();
    }

    /**
     * Test for timer notification identifier = null
     */
    public Result testTimerNotificationIdentifier2() throws Exception {

        startTimer();

        try {
            timer.getDate(null);
        } catch (NullPointerException e) {
            pass();
        } catch (Exception e) {
            fail("Thorwn wrong type of exception");
        }
        try {
            startTimer();
            timer.getNbOccurences(null);
        } catch (NullPointerException e) {
            pass();
        } catch (Exception e) {
            fail("Thorwn wrong type of exception");
        }
        try {
            startTimer();
            timer.getFixedRate(null);
        } catch (NullPointerException e) {
            pass();
        } catch (Exception e) {
            fail("Thorwn wrong type of exception");
        }
        try {
            startTimer();
            timer.getNotificationMessage(null);
        } catch (NullPointerException e) {
            pass();
        } catch (Exception e) {
            fail("Thorwn wrong type of exception");
        }
        try {
            startTimer();
            timer.getNotificationType(null);
        } catch (NullPointerException e) {
            pass();
        } catch (Exception e) {
            fail("Thorwn wrong type of exception");
        }
        try {
            startTimer();
            timer.getNotificationUserData(null);
        } catch (NullPointerException e) {
            pass();
        } catch (Exception e) {
            fail("Thorwn wrong type of exception");
        }
        try {
            startTimer();
            timer.getPeriod(null);
        } catch (NullPointerException e) {
            pass();
        } catch (Exception e) {
            fail("Thorwn wrong type of exception");
        }
        try {
            startTimer();
            timer.removeNotification(null);
        } catch (NullPointerException e) {
            pass();
        } catch (Exception e) {
            fail("Thorwn wrong type of exception");
        }

        finally {

            stopTimer();

        }
        return result();
    }

    /**
     * Start of the timer service
     */
    private void startTimer() {
        try {
            server = MBeanServerFactory.createMBeanServer();
            timer = new Timer();
            timerName = new ObjectName("test:type=timer");
            server.registerMBean(timer, timerName);
            server.addNotificationListener(timerName, this, null, null);
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop the timer service
     */
    private void stopTimer() {
        try {
            timer.removeAllNotifications();
            timer.stop();
            server.unregisterMBean(timerName);
            MBeanServerFactory.releaseMBeanServer(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle Notification
     */
    public void handleNotification(Notification notif, Object handback) {
        notifNbs++;
        TimerNotification timerNotif = (TimerNotification) notif;
        if (timer.getNbOccurences(timerNotif.getNotificationID()).longValue() == 1)
            synchronized (timerName) {
                timerName.notifyAll();
            }
    }

    /**
     * Adding notifications to the timer service
     */
    private void addNotification(long offset, long period, long occurs) {
        id = timer.addNotification(LibTimer.TIMER_TYPE, LibTimer.TEST_MESSAGE,
                LibTimer.TEST_DATA, timeCalc(offset), period, occurs);

    }
    
    

    /**
     * Geting current time and offset
     */
    private Date timeCalc(long step) {
        return new Date(System.currentTimeMillis() + step);
    }
    public static void main(String[] args) {
        try {
            System.exit(new BaseTimerTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}