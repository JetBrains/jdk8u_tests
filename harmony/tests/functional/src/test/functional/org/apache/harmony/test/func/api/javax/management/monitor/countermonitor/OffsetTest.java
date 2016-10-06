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

package org.apache.harmony.test.func.api.javax.management.monitor.countermonitor;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.monitor.CounterMonitor;

import org.apache.harmony.test.func.api.javax.management.monitor.share.NotifListener;
import org.apache.harmony.test.func.api.javax.management.monitor.share.Timer;
import org.apache.harmony.share.Test;

/**
 * This test is intended to check if CounterMonitor class processes threshold
 * increment when counter reaches threshold. <br>
 * Step by step:
 * <ul>
 * <li>MBean server is started; CounterMonitor and Counter MBeans are
 * registered.
 * <li>CounterMonitor is configured with non-zero offset value.
 * <li>NotificationListener is added to CounterMonitor.
 * <li>Counter is started in a separate thread, CounterMonitor is activated.
 * <li>NotificationListener checks that proper notification is received and
 * threshold is reached.
 * <li>NotificationListener updates its threshold value and waits for next
 * notification.
 * <li>When second notification received, NotificationListener updates its
 * threshold value again and waits for next notification.
 * <li>If all three notifications are received correctly then test is
 * considered to be passed.
 * </ul>
 * 
 */

public class OffsetTest extends Test {

    /** Object for synchronization */
    public static Object sync = new Object();

    /** Test result */
    private static boolean res = true;

    private Timer timer;

    /** Default offset value */
    public static final int DEFAULT_OFFSET = 5;

    /** Tested monitor */
    private CounterMonitor monitor;

    /** Counter */
    private Counter counter;

    private NotifListener nListener = new NotifListener(sync);

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.exit(new OffsetTest().test(args));
    }

    /**
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        /* register monitor and counter */
        monitor = new CounterMonitor();
        counter = new Counter(200);

        ObjectName cm, counterName;
        try {
            cm = new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "countermonitor:type=CounterMonitor,id=1");
            counterName = new ObjectName(Counter.COUNTER_NAME_TEMPLATE + "1");
        } catch (MalformedObjectNameException e) {
            System.err.println("INTERNAL ERROR: Invalid object name");
            e.printStackTrace();
            return fail("FAILED");
        }
        try {
            server.registerMBean(monitor, cm);
            server.registerMBean(counter, counterName);
        } catch (JMException e) {
            System.err.println("INTERNAL ERROR: MBean registration failed");
            e.printStackTrace();
            return fail("FAILED");
        }

        monitor.addNotificationListener(nListener, null, "handback");

        monitor.addObservedObject(counterName);
        monitor.setInitThreshold(new Integer(Counter.DEFAULT_THRESHOLD));
        monitor.setGranularityPeriod(100);
        monitor.setNotify(true);
        monitor.setDifferenceMode(false);
        monitor.setObservedAttribute("Value");
        monitor.setOffset(new Integer(DEFAULT_OFFSET));

        /* run counter and monitor */
        Thread counterThread = new Thread(counter);
        counterThread.setDaemon(true);
        counterThread.start();

        nListener.setPredictMode(true, counter);
        nListener.setParameters("Value", counterName,
                "jmx.monitor.counter.threshold");
        counter.setIncrement(monitor.getInitThreshold());

        /*
         * wait for notification handler's notification on sync and return test
         * result
         */
        enableTimer();
        monitor.start();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        counter.setIncrement(new Integer(Counter.DEFAULT_THRESHOLD
                + DEFAULT_OFFSET));

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        counter.setIncrement(new Integer(Counter.DEFAULT_THRESHOLD
                + DEFAULT_OFFSET * 2));

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        counter.setIncrement(new Integer(Counter.DEFAULT_THRESHOLD
                + DEFAULT_OFFSET * 3));

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        return res ? pass("PASSED") : fail("FAILED");
    }

    /**
     * This method used for shorten and more simplicity. It just started new
     * timer thread
     */
    private void enableTimer() {
        timer = new Timer(5000, sync);
        timer.setDaemon(true);
        timer.start();
    }
}
