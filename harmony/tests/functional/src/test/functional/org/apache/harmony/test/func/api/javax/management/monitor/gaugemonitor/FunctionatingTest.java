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
package org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.monitor.GaugeMonitor;

import org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor.sensors.MonotonousFunction;
import org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor.sensors.RandomFunction;
import org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor.sensors.RandomThree;
import org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor.sensors.SinusFunction;
import org.apache.harmony.test.func.api.javax.management.monitor.share.NotifListener;
import org.apache.harmony.test.func.api.javax.management.monitor.share.Timer;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * This test is intended to check the basic functionality of GaugeMonitor class.<br>
 * <br>
 * testLowHigh:<br>
 * The Sensor class represents monotonous function (of type double), which
 * does't reach boundary values.<br>
 * <br>
 * testIntPeriodic():<br>
 * The sensor class represents a periodic function (of type int to provide
 * accurate comparisons)<br>
 * <br>
 * testRandomThree:<br>
 * The sensor class represents a random function which has three possible
 * values.<br>
 * <br>
 * testRandomFunction:<br>
 * The sensor class represents a random function with a specified range of
 * values (F[t] is continuous)<br>
 * <br>
 */

public class FunctionatingTest extends MultiCase {

    /** Timer used for prevent test from hangs */
    private Timer timer;

    /** Object for synchronization */
    public static final Object sync = new Object();

    /** Test result */
    private static boolean res = true;

    /** Tested monitor */
    private GaugeMonitor monitor;

    private NotifListener notificationListener = new NotifListener(sync);

    /**
     * This method used for shorten and more simplicity. It just started timer
     * thread
     */
    private void enableTimer() {
        timer = new Timer(5000, sync);
        timer.setDaemon(true);
        timer.start();
    }

    public Result testRandomThree() throws Exception {

        /* Create object for synchronisation and MBean server */
        MBeanServer server = MBeanServerFactory.createMBeanServer();

        /* register monitor and sensor */
        monitor = new GaugeMonitor();
        server.registerMBean(monitor, new ObjectName(
                "org.apache.harmony.test.func.api.javax.management.monitor."
                        + "countermonitor:type=CounterMonitor,id=1"));

        RandomThree sensor = new RandomThree(sync);
        ObjectName sensorName = new ObjectName(RandomThree.SENSOR_NAME_TEMPLATE
                + "1");
        server.registerMBean(sensor, sensorName);

        monitor.addNotificationListener(notificationListener, null, "handback");

        /* setup monitor */
        monitor.addObservedObject(sensorName);
        monitor.setThresholds(new Double(5.000000000000001), new Double(
                4.999999999999999));
        monitor.setGranularityPeriod(100);
        monitor.setNotifyHigh(true);
        monitor.setNotifyLow(true);
        monitor.setObservedAttribute("Value");

        /* run counter and monitor */
        Thread sensorThread = new Thread(sensor);
        sensorThread.setDaemon(true);
        sensorThread.start();
        monitor.start();

        /*
         * wait for notification handler's notification on sync and return test
         * result
         */
        notificationListener.setParameters("Value", sensorName,
                NotifListener.UNKNOWN_TYPE);
        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("THREAD INTERRUPTED");
            }
        }
        res &= timer.getRes();
        res &= notificationListener.getRes();
        timer.interrupt();

        /*
         * reconfigure monitor to another sensor and trying to reach low
         * threshold
         */
        if (notificationListener.getType().equals("jmx.monitor.gauge.high")) {
            notificationListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.low");
            RandomThree.isHigh = false;
        } else {
            notificationListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.high");
            RandomThree.isHigh = true;
        }

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("THREAD INTERRUPTED");
            }
        }
        res &= timer.getRes();
        res &= notificationListener.getRes();
        timer.interrupt();

        if (notificationListener.getType().equals("jmx.monitor.gauge.high")) {
            notificationListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.low");
            RandomThree.isHigh = false;
        } else {
            notificationListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.high");
            RandomThree.isHigh = true;
        }

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("THREAD INTERRUPTED");
            }
        }
        res &= timer.getRes();
        res &= notificationListener.getRes();
        timer.interrupt();

        monitor.stop();
        RandomThree.stop = true;

        return res ? passed() : failed("FAILED");
    }

    public Result testIntPeriodic() throws Exception {

        /* Create object for synchronisation and MBean server */
        MBeanServer server = MBeanServerFactory.createMBeanServer();

        /* register monitor and sensor */
        monitor = new GaugeMonitor();
        server.registerMBean(monitor, new ObjectName(
                "org.apache.harmony.test.func.api.javax.management.monitor."
                        + "countermonitor:type=CounterMonitor,id=1"));

        SinusFunction sensor = new SinusFunction(sync, 200);
        ObjectName sensorName = new ObjectName(
                SinusFunction.SENSOR_NAME_TEMPLATE + "1");
        server.registerMBean(sensor, sensorName);

        /* Enable NotificationListener */
        monitor.addNotificationListener(notificationListener, null, "handback");

        /* setup monitor */
        monitor.addObservedObject(sensorName);
        monitor.setThresholds(new Integer(100), new Integer(-100));
        monitor.setGranularityPeriod(100);
        monitor.setNotifyHigh(true);
        monitor.setNotifyLow(true);
        monitor.setObservedAttribute("Integer");

        /* run counter and monitor */
        Thread sensorThread = new Thread(sensor);
        sensorThread.setDaemon(true);
        sensorThread.start();
        monitor.start();

        /*
         * wait for notification handler's notification on sync and return test
         * result
         */
        notificationListener.setParameters("Integer", sensorName,
                "jmx.monitor.gauge.high");
        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("THREAD INTERRUPTED");
            }
        }
        res &= timer.getRes();
        res &= notificationListener.getRes();
        timer.interrupt();

        /*
         * reconfigure monitor to another sensor and trying to reach low
         * threshold
         */
        notificationListener.setParameters("Integer", sensorName,
                "jmx.monitor.gauge.low");

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("THREAD INTERRUPTED");
            }
        }
        res &= timer.getRes();
        res &= notificationListener.getRes();

        timer.interrupt();

        monitor.stop();

        return res ? passed() : failed("FAILED");
    }

    public Result testRandomFunction() throws Exception {
    
        /* Create object for synchronisation and MBean server */
        MBeanServer server = MBeanServerFactory.createMBeanServer();
    
        /* register monitor and sensor */
        monitor = new GaugeMonitor();
        server.registerMBean(monitor, new ObjectName(
                "org.apache.harmony.test.func.api.javax.management.monitor."
                        + "countermonitor:type=CounterMonitor,id=1"));
    
        RandomFunction sensor = new RandomFunction(sync);
        ObjectName sensorName = new ObjectName(
                RandomFunction.SENSOR_NAME_TEMPLATE + "1");
        server.registerMBean(sensor, sensorName);
    
        /* Enable NotificationListener */
        monitor.addNotificationListener(notificationListener, null, "handback");
    
        /* setup monitor */
        monitor.addObservedObject(sensorName);
        monitor.setThresholds(new Double(25), new Double(-25));
        monitor.setGranularityPeriod(100);
        monitor.setNotifyHigh(true);
        monitor.setNotifyLow(true);
        monitor.setObservedAttribute("Value");
    
        /* run counter and monitor */
        Thread sensorThread = new Thread(sensor);
        sensorThread.setDaemon(true);
        sensorThread.start();
        monitor.start();
    
        /*
         * wait for notification handler's notification on sync and return test
         * result
         */
        notificationListener.setParameters("Value", sensorName,
                NotifListener.UNKNOWN_TYPE);
        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("THREAD INTERRUPTED");
            }
        }
        res &= timer.getRes();
        res &= notificationListener.getRes();
        timer.interrupt();
    
        for (int i = 0; i < 2; i++) {
    
            /*
             * reconfigure monitor to another sensor and trying to reach low
             * threshold
             */
            RandomFunction.isNotify = true;
    
            if (notificationListener.getType().equals("jmx.monitor.gauge.high")) {
                notificationListener.setParameters("Value", sensorName,
                        "jmx.monitor.gauge.low");
                RandomFunction.isHigh = false;
            } else {
                notificationListener.setParameters("Value", sensorName,
                        "jmx.monitor.gauge.high");
                RandomFunction.isHigh = true;
            }
    
            enableTimer();
            synchronized (sync) {
                try {
                    sync.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("THREAD INTERRUPTED");
                }
            }
            res &= timer.getRes();
            res &= notificationListener.getRes();
            timer.interrupt();
    
            if (notificationListener.getType().equals("jmx.monitor.gauge.high")) {
                notificationListener.setParameters("Value", sensorName,
                        "jmx.monitor.gauge.low");
                RandomFunction.isHigh = false;
            } else {
                notificationListener.setParameters("Value", sensorName,
                        "jmx.monitor.gauge.high");
                RandomFunction.isHigh = true;
            }
    
            enableTimer();
            synchronized (sync) {
                try {
                    sync.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return failed("THREAD INTERRUPTED");
                }
            }
            res &= timer.getRes();
            res &= notificationListener.getRes();
            timer.interrupt();
        }
    
        RandomFunction.stop = true;
        monitor.stop();
    
        return res ? passed() : failed("FAILED");
    }

    public Result testLowHigh() throws Exception {

        /* Create object for synchronisation and MBean server */
        MBeanServer server = MBeanServerFactory.createMBeanServer();

        /* register monitor and sensor */
        monitor = new GaugeMonitor();
        server.registerMBean(monitor, new ObjectName(
                "org.apache.harmony.test.func.api.javax.management.monitor."
                        + "countermonitor:type=CounterMonitor,id=1"));

        MonotonousFunction sensor = new MonotonousFunction(sync, 10);
        ObjectName sensorName = new ObjectName(
                MonotonousFunction.SENSOR_NAME_TEMPLATE + "1");
        server.registerMBean(sensor, sensorName);

        /* Enable NotificationListener */
        monitor.addNotificationListener(notificationListener, null, "handback");

        /* setup monitor */
        monitor.addObservedObject(sensorName);
        monitor.setThresholds(new Double(10), new Double(-10));
        monitor.setGranularityPeriod(100);
        monitor.setNotifyHigh(true);
        monitor.setNotifyLow(false);
        monitor.setObservedAttribute("Value");

        /* run counter and monitor */
        Thread sensorThread = new Thread(sensor);
        sensorThread.setDaemon(true);
        sensorThread.start();

        monitor.start();

        /*
         * wait for notification handler's notification on sync and return test
         * result
         */
        notificationListener.setParameters("Value", sensorName,
                "jmx.monitor.gauge.high");

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("THREAD INTERRUPTED");
            }
        }
        res &= timer.getRes();
        res &= notificationListener.getRes();
        timer.interrupt();

        /*
         * reconfigure monitor to another sensor and trying to reach low
         * threshold
         */

        sensor = new MonotonousFunction(sync, 200);
        sensor.setValue(-20);

        sensorName = new ObjectName(MonotonousFunction.SENSOR_NAME_TEMPLATE
                + "2");
        server.registerMBean(sensor, sensorName);

        monitor.addObservedObject(sensorName);
        monitor.setNotifyHigh(false);
        monitor.setNotifyLow(true);

        sensorThread = new Thread(sensor);
        sensorThread.setDaemon(true);
        sensorThread.start();

        notificationListener.setParameters("Value", sensorName,
                "jmx.monitor.gauge.low");
        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return failed("THREAD INTERRUPTED");
            }
        }
        res &= timer.getRes();
        res &= notificationListener.getRes();

        timer.interrupt();
        monitor.stop();

        return res ? passed() : failed("FAILED");
    }

    public static void main(String[] args) {
        System.exit(new FunctionatingTest().test(args));
    }
}
