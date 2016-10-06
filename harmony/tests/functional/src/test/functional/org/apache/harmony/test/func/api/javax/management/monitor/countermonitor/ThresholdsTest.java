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
 * This test is intended to check the essential functionality of CounterMonitor.
 * Test verifies threshold notifications. <br>
 * Step by step:<br>
 * <ul>
 * <li>MBean Server is created; CounterMonitor and Counter MBeans are
 * registered.
 * <li>NotificationListener is added to CounterMonitor.
 * <li>Counter is started in a separate thread, CounterMonitor is activated.
 * <li>Notification handler checks that counter threshold is actually exceeded
 * <li>Notification handler reconfigures monitor - removes existing handler and
 * adds another handler (to handle another observed object)
 * <li>Notification handler sets new observed object and new threshold
 * <li>New notification handler expects to receive corresponding notification
 * <li>Monitor is reconfigured to another observed object
 * <li>New notification handler is set to check if the corresponding
 * notification is received
 * </ul>
 * 
 */

public class ThresholdsTest extends Test {

    /** Object for synchronization */
    public static Object sync = new Object();

    private NotifListener nListener = new NotifListener(sync);

    private Timer timer;

    public static void main(String[] args) {
        System.exit(new ThresholdsTest().test(args));
    }

    public int test() {

        boolean res = true;

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        // register monitor and counters
        CounterMonitor monitor = new CounterMonitor();
        Counter counter = new Counter(200);
        Counter counter2 = new Counter(200);
        Counter counter3 = new Counter(300);

        ObjectName cm, counterName, counter2Name, counter3Name;
        try {
            cm = new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "countermonitor:type=CounterMonitor,id=1");
            counterName = new ObjectName(Counter.COUNTER_NAME_TEMPLATE + "1");
            counter2Name = new ObjectName(Counter.COUNTER_NAME_TEMPLATE + "2");
            counter3Name = new ObjectName(Counter.COUNTER_NAME_TEMPLATE + "3");
        } catch (MalformedObjectNameException e) {
            System.err.println("INTERNAL ERROR: Invalid object name");
            e.printStackTrace();
            return fail("FAILED");
        }
        try {
            server.registerMBean(monitor, cm);
            server.registerMBean(counter, counterName);
            server.registerMBean(counter2, counter2Name);
            server.registerMBean(counter3, counter3Name);
        } catch (JMException e) {
            System.err.println("INTERNAL ERROR: MBean registration failed");
            e.printStackTrace();
            return fail("FAILED");
        }

        /* setup monitor */
        monitor.addNotificationListener(nListener, null, "handback");

        monitor.addObservedObject(counterName);
        monitor.addObservedObject(counter2Name);
        monitor.addObservedObject(counter3Name);
        monitor.setInitThreshold(new Integer(Counter.DEFAULT_THRESHOLD));
        monitor.setGranularityPeriod(100);
        monitor.setNotify(true);
        monitor.setDifferenceMode(false);
        monitor.setObservedAttribute("Value");

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

        /*
         * Now threshold is reached and notification is verified. Now add to
         * monitor another observed object and try to reach it's threshold
         */

        nListener.setParameters("Value", counter2Name,
                "jmx.monitor.counter.threshold");

        enableTimer();
        Thread counterThread2 = new Thread(counter2);
        counterThread2.setDaemon(true);
        counterThread2.start();

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

        /* Now remove all observed objects and add new one */

        /*
         * Reset counters and wait 1 secs (if they hasn't been deleted they are
         * reach threshold first)
         */

        nListener.setParameters("Value", counter3Name,
                "jmx.monitor.counter.threshold");

        enableTimer();
        Thread counterThread3 = new Thread(counter3);
        counterThread3.setDaemon(true);
        counterThread3.start();
        monitor.removeObservedObject(counterName);
        monitor.removeObservedObject(counter2Name);
        monitor.setInitThreshold(new Integer((Counter.DEFAULT_THRESHOLD) / 2));
        counter.setIncrement(monitor.getInitThreshold());
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
}
