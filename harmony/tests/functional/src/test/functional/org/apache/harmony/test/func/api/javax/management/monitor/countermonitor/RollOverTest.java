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
 * This test is intended to check if CounterMonitor class processes counter
 * rollover correctly. CounterMonitor is configured with default modulus and
 * offset values (zero); F[t] rolls over. <br>
 * Step by step:
 * <ul>
 * <li>MBean server is started; CounterMonitor and Counter MBeans are
 * registered.
 * <li>CounterMonitor is configured in default mode.
 * <li>NotificationListener is added to CounterMonitor.
 * <li>Counter is started in a separate thread, CounterMonitor is activated.
 * <li>NotificationListener checks that proper notification is received and
 * threshold is actually reached.
 * <li>NotificationListener waits for next notification (counter should
 * rollover to zero and reach threshold value again).
 * <li>New notification listener checks that proper notification is received
 * and threshold is actually reached.
 * </ul>
 * 
 */

public class RollOverTest extends Test {

    /** Object for synchronization */
    public static Object sync = new Object();

    private NotifListener nListener = new NotifListener(sync);

    private Timer timer;

    public int test() {
        MBeanServer server = MBeanServerFactory.createMBeanServer();

        /* register monitor and counter */
        CounterMonitor monitor = new CounterMonitor();

        ObjectName cm, counterName;

        Counter counter = new Counter(200);

        counter.setRollOverValue(Counter.DEFAULT_THRESHOLD, 0);

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

        /* run counter and monitor */
        Thread counterThread = new Thread(counter);
        counterThread.setDaemon(true);
        counterThread.start();
        monitor.start();

        nListener.setPredictMode(true, counter);
        nListener.setParameters("Value", counterName,
                "jmx.monitor.counter.threshold");
        counter.setIncrement(monitor.getInitThreshold());

        for (int i = 0; i < 3; i++) {
            enableTimer();
            synchronized (sync) {
                try {
                    sync.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!(timer.getRes() && nListener.getRes())) {
                return fail("FAILED");
            }
            timer.interrupt();
        }
        return pass("PASSED");

    }

    public static void main(String[] args) {
        int res = new RollOverTest().test(args);
        System.exit(res);
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
