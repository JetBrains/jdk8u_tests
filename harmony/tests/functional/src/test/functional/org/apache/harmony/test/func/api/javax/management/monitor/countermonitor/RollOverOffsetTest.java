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
 * This test is intended to check basic CounterMonitor threshold rollover
 * functionality in case when modulus value is not zero. <br>
 * Step by step:
 * <ul>
 * <li>MBean server is started; CounterMonitor and Counter MBeans are
 * registered.
 * <li>CounterMonitor is configured with non-zero offset and modulus values.
 * <li>NotificationListener is added to CounterMonitor.
 * <li>Counter is started in a separate thread, CounterMonitor is activated.
 * <li>NotificationListener checks that proper notification is received and
 * threshold is actually reached.
 * <li>Listener updates its threshold value according to offset value.
 * <li>Listener waits for next notification.
 * <li>Listener updates its threshold according to offset and modulus values.
 * Counter is also updated.
 * <li>Listener waits for next notification.
 * <li>If proper notification is received test considered to be passed.
 * </ul>
 * 
 */

public class RollOverOffsetTest extends Test {

    /** Object for synchronization */
    public static Object sync = new Object();

    /** Counter roll overs to this value */
    private static int rollOverValue = 5;

    /** Test result */
    private static boolean res = true;

    /** Default offset value */
    public static final int DEFAULT_OFFSET = 5;

    private NotifListener nListener = new NotifListener(sync);

    public int test() {
        MBeanServer server = MBeanServerFactory.createMBeanServer();

        /* register monitor and counter */
        CounterMonitor monitor = new CounterMonitor();

        ObjectName cm, counterName;

        Counter counter = new Counter(200);
        counter.setRollOverValue(
                Counter.DEFAULT_THRESHOLD + 2 * DEFAULT_OFFSET, rollOverValue);

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

        /* Enable NotificationListener */
        monitor.addNotificationListener(nListener, null, "handback");

        /* setup monitor */
        monitor.addObservedObject(counterName);
        monitor.setInitThreshold(new Integer(Counter.DEFAULT_THRESHOLD));
        monitor.setGranularityPeriod(100);
        monitor.setNotify(true);
        monitor.setDifferenceMode(false);
        monitor.setObservedAttribute("Value");
        monitor.setOffset(new Integer(DEFAULT_OFFSET));
        monitor.setModulus(new Integer(Counter.DEFAULT_THRESHOLD
                + (DEFAULT_OFFSET * 2)));

        /* run counter and monitor */
        Thread counterThread = new Thread(counter);
        counterThread.setDaemon(true);
        counterThread.start();

        nListener.setPredictMode(true, counter);
        nListener.setParameters("Value", counterName,
                "jmx.monitor.counter.threshold");

        counter.setIncrement(monitor.getInitThreshold());

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

        counter.setIncrement(monitor.getModulus());

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

        counter.setIncrement(monitor.getInitThreshold());

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
        timer = new Timer(30000, sync);
        timer.setDaemon(true);
        timer.start();
    }

    private Timer timer;

    public static void main(String[] args) {
        System.exit(new RollOverOffsetTest().test(args));
    }

}
