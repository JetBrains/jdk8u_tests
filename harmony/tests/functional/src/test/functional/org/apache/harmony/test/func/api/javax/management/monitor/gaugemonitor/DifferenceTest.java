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

import org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor.sensors.SinusFunction;
import org.apache.harmony.test.func.api.javax.management.monitor.share.NotifListener;
import org.apache.harmony.test.func.api.javax.management.monitor.share.Timer;
import org.apache.harmony.share.Test;

/**
 * This test is indented to check basic functionality of GaugeMonitor class when
 * difference mode flag is set to true.
 * 
 * Test verifies unreachable thresholds, and various positive and negative
 * threshold values
 */

public class DifferenceTest extends Test {

    /** Timer used for prevent test from hangs */
    private Timer timer;

    /** Object for synchronization */
    private static final Object sync = new Object();

    /**
     * This method used for shorten and more simplicity. It just started timer
     * thread
     */
    private void enableTimer() {
        timer = new Timer(3000, sync);
        timer.setDaemon(true);
        timer.start();
    }

    private static Number[] thresholds = { new Integer(0), new Integer(150),
            new Integer(-402), new Integer(20), new Integer(-20) };

    private static String attr = "Integer";

    public int test() {

        /* Test result */
        boolean res = true;

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        /* register monitor and sensor */
        GaugeMonitor monitor = new GaugeMonitor();
        SinusFunction sensor = new SinusFunction(sync, 200);

        ObjectName sensorName = null;

        try {
            server.registerMBean(monitor, new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "countermonitor:type=CounterMonitor,id=1"));
            sensorName = new ObjectName(SinusFunction.SENSOR_NAME_TEMPLATE
                    + "1");
            server.registerMBean(sensor, sensorName);
        } catch (Throwable t) {
            t.printStackTrace();
            return error("Test: Unresolved test problem");
        }

        /* Enable NotificationListener */
        NotifListener nListener = new NotifListener(sync);
        monitor.addNotificationListener(nListener, null, "handback");

        /* Setup monitor */
        monitor.addObservedObject(sensorName);
        monitor.setThresholds(thresholds[1], thresholds[2]);
        monitor.setGranularityPeriod(100);
        monitor.setNotifyHigh(true);
        monitor.setNotifyLow(true);
        monitor.setDifferenceMode(true);
        monitor.setObservedAttribute(attr);

        /*
         * Sensor represents sinus function that increase its argument to P1/12
         * every 10 milliseconds
         */
        Thread sensorThreadDouble = new Thread(sensor);
        sensorThreadDouble.setDaemon(true);
        sensorThreadDouble.start();

        /* Boundaries is out of range */
        log.add("Test: Boundaries is out of range. "
                + "So notifications shouldn't be sent");

        monitor.start();

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res &= false;
            }
        }
        res &= !(timer.getRes());
        timer.interrupt();

        /* Now boundaries set to zero: so sensor can easily achieve it */
        log.add("Test: Boundaries set to zero");

        /*
         * Type of notification is unknown. Verifies observed attribute and
         * object
         */
        nListener.setParameters(attr, sensorName, NotifListener.UNKNOWN_TYPE);

        monitor.setThresholds(thresholds[0], thresholds[0]);

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        /* Setup notification according to type of previous notification */
        if (nListener.getType().equals("jmx.monitor.gauge.high")) {
            nListener.setParameters(attr, sensorName, "jmx.monitor.gauge.low");
        } else {
            nListener.setParameters(attr, sensorName, "jmx.monitor.gauge.high");
        }

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        if (nListener.getType().equals("jmx.monitor.gauge.high")) {
            nListener.setParameters(attr, sensorName, "jmx.monitor.gauge.low");
        } else {
            nListener.setParameters(attr, sensorName, "jmx.monitor.gauge.high");
        }

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        res &= (timer.getRes());
        res &= nListener.getRes();
        timer.interrupt();

        /* Now test non-zero values */
        log.add("Test: Boundaries set to non-zero value "
                + "(Function can achieve it)");

        monitor.setThresholds(thresholds[3], thresholds[2]);
        // System.out.println(monitor.getHighThreshold());
        // System.out.println(monitor.getLowThreshold());

        nListener.setParameters(attr, sensorName, "jmx.monitor.gauge.high");

        enableTimer();
        synchronized (sync) {

            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res &= false;
            }
        }

        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        monitor.setThresholds(thresholds[1], thresholds[4]);

        nListener.setParameters(attr, sensorName, "jmx.monitor.gauge.low");

        enableTimer();
        synchronized (sync) {

            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res &= false;
            }
        }

        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        /*
         * Verify monitor when high threshold is positive and low threshold is
         * negative
         */
        monitor.setThresholds(thresholds[3], thresholds[4]);

        log.add("Test: Set high threshold as positive value "
                + "and low threshold as negative value "
                + "(Function can achieve it)");

        nListener.setParameters(attr, sensorName, NotifListener.UNKNOWN_TYPE);

        enableTimer();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res &= false;
            }
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        monitor.stop();

        /* Verification of getters */
        res &= monitor.getNotifyHigh();
        res &= monitor.getNotifyLow();

        return res ? pass("PASSED") : fail("FAILED");
    }

    /** Parsing parameters */
    private static void parseParameters(String args[]) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("Integer")) {
                log.add("Parser: Integer values used");
                return;
            }
            if (args[i].equals("Double")) {
                thresholds[0] = new Double(0);
                thresholds[1] = new Double(1.3);
                thresholds[2] = new Double(-5.5);
                thresholds[3] = new Double(0.2);
                thresholds[4] = new Double(-0.2);
                attr = "Double";
                log.add("Parser: Double values used");
                return;
            }
            if (args[i].equals("Float")) {
                thresholds[0] = new Float(0);
                thresholds[1] = new Float(1.9);
                thresholds[2] = new Float(-2.3);
                thresholds[3] = new Float(0.2);
                thresholds[4] = new Float(-0.2);
                attr = "Float";
                log.add("Parser: Float values used");
                return;
            }
            if (args[i].equals("Long")) {
                thresholds[0] = new Long(0);
                thresholds[1] = new Long(150);
                thresholds[2] = new Long(-402);
                thresholds[3] = new Long(20);
                thresholds[4] = new Long(-20);
                attr = "Long";
                log.add("Parser: Long values used");
                return;
            }
            if (args[i].equals("Short")) {
                thresholds[0] = new Short((short) 0);
                thresholds[1] = new Short((short) 150);
                thresholds[2] = new Short((short) -202);
                thresholds[3] = new Short((short) 20);
                thresholds[4] = new Short((short) -20);
                attr = "Short";
                log.add("Parser: Short values used");
                return;
            }
            if (args[i].equals("Byte")) {
                thresholds[0] = new Byte((byte) 0);
                thresholds[1] = new Byte((byte) 110);
                thresholds[2] = new Byte((byte) -120);
                thresholds[3] = new Byte((byte) 19);
                thresholds[4] = new Byte((byte) -19);
                attr = "Byte";
                log.add("Parser: Byte values used");
                return;
            }
        }

        log.add("Parser: Incorrect parameters. "
                + "Integer (default) boundaries verified.");
    }

    public static void main(String[] args) {
        parseParameters(args);
        System.exit(new DifferenceTest().test(args));
    }
}
