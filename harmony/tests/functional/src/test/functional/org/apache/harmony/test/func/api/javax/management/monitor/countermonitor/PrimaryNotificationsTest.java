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
import org.apache.harmony.share.Test;

/**
 * This test is intended to check the essential functionality of CounterMonitor.
 * It's verifies Counter monitor threshold and one of error notifications. <br>
 * Step by step:<br>
 * <ul>
 * <li>MBean Server is created; CounterMonitor and Counter MBeans are
 * registered.
 * <li>NotificationListener is added to CounterMonitor.
 * <li>Counter is started in a separate thread, CounterMonitor is activated.
 * <li>Notification handler checks that counter threshold is actually reached
 * <li>Notification handler reconfigures monitor removes existing handler and
 * adds another handler (to handle errors)
 * <li>Notification handler sets invalid type threshold value
 * <li>New notification handler expects to receive corresponding error
 * notification
 * </ul>
 * 
 */

public class PrimaryNotificationsTest extends Test {

    /** Test result */
    public static boolean res = true;

    /** Object for synchronization */
    public static final Object sync = new Object();

    /** Tested monitor */
    private CounterMonitor monitor;

    /** Counter */
    private Counter counter;

    /** 
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        // register monitor and counter
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

        // setup monitor
        NotifListener notificationListener = new NotifListener(sync);
        monitor.addNotificationListener(notificationListener, null, "handback");

        monitor.addObservedObject(counterName);
        monitor.setInitThreshold(new Integer(Counter.DEFAULT_THRESHOLD));
        monitor.setGranularityPeriod(100);
        monitor.setNotify(true);
        monitor.setDifferenceMode(false);
        monitor.setObservedAttribute("Value");

        // run counter and monitor
        Thread counterThread = new Thread(counter);
        counterThread.setDaemon(true);
        counterThread.start();

        notificationListener.setParameters("Value", counterName,
                "jmx.monitor.counter.threshold");
        notificationListener.setPredictMode(true, counter);
        counter.setIncrement(monitor.getInitThreshold());

        monitor.start();

        // wait for notification handler's notification on sync and return test
        // result
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return error("THREAD INTERRUPTED");
            }
        }
        res &= notificationListener.getRes();

        notificationListener.setParameters("Float", counterName,
                "jmx.monitor.error.attribute");
        monitor.setObservedAttribute("Float");

        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return error("THREAD INTERRUPTED");
            }
        }
        res &= notificationListener.getRes();

        return res ? pass("PASSED") : fail("FAILED");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.exit(new PrimaryNotificationsTest().test(args));
    }

}
