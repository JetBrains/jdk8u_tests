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
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.monitor.GaugeMonitor;

import org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor.sensors.MonotonousFunction;
import org.apache.harmony.share.Test;

/**
 * This test is intended to check the functionality of GaugeMonitor class getter
 * and setter methods. The test calls consequently each setter method and then
 * calls corresponding getter method and checks if the returned value is
 * correct. There are three categories of values passed to each setter method:
 * common intermediate values, boundary values and invalid values (which are
 * expected to cause an exception).
 * 
 */

public class GetterSetterTest extends Test implements NotificationListener {

    /** Test result */
    private boolean res = true;

    /** Tested monitor */
    GaugeMonitor monitor;

    /** Notification Counter */
    private int notificationsCount = 0;

    public void handleNotification(Notification n, Object o) {
        notificationsCount++;
        switch (notificationsCount) {
        case 1:
            if (!n.getType().equals("jmx.monitor.gauge.high")) {
                res = false;
            }
            break;
        case 2:
            if (!n.getType().equals("jmx.monitor.gauge.low")) {
                res = false;
            }
            break;
        default:
            res = false;
            log.add("NotificationListener: "
                    + "Unexpected notification received.");
            log.add("Notification type = " + n.getType());
        }
    }

    public int test() {

        primitiveNotificationTest();

        monitor = new GaugeMonitor();

        /* Checking basic functionality of setter/getter methods */

        /* setting difference mode flag */
        monitor.setDifferenceMode(false);

        /* setting granularity period */
        if (monitor.getGranularityPeriod() != 10000) {
            res = false;
        }

        try {
            monitor.setGranularityPeriod(-1684629);
            log.add("FAIL: Exception expected when passing "
                    + "negative value to setGranularityPeriod");
            res = false;
        } catch (IllegalArgumentException e) {
            if (monitor.getGranularityPeriod() != 10000) {
                log.add("FAIL: Granularity value changed"
                        + " when exception was raised");
                res = false;
            }
        }
        try {
            monitor.setGranularityPeriod(0);
            log.add("FAIL: Exception expected when passing "
                    + "zero value to setGranularityPeriod");
            res = false;
        } catch (IllegalArgumentException e) {
            if (monitor.getGranularityPeriod() != 10000) {
                log.add("FAIL: Granularity value changed when "
                        + "exception was raised");
                res = false;
            }
        }
        try {
            monitor.setGranularityPeriod(2459743);
        } catch (Throwable e) {
            log.add("FAIL: Unexpected exception caught when "
                    + "setting granularity period");
            e.printStackTrace();
            return fail("FAILED");
        }

        /* setting init threshold */
        try {
            monitor.setThresholds(null, null);
            log.add("FAIL: Exception expected when passing both "
                    + "nulls value to setThresholds");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            monitor.setThresholds(null, new Integer(5));
            log.add("FAIL: Exception expected when passing null "
                    + "low threshold value to setThresholds");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            monitor.setThresholds(new Integer(5), null);
            log.add("FAIL: Exception expected when passing null "
                    + "high threshold value to setThresholds");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            monitor.setThresholds(new Integer(3), new Integer(5));
            log.add("FAIL: Exception expected when passing "
                    + "low value higher than high value to setInitThreshold");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            monitor.setThresholds(new Integer(1), new Float(-1.5));
            log.add("FAIL: Exception expected when passing values"
                    + " of different types to setInitThreshold");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            monitor.setThresholds(new Float(1.5), new Float(1.5));
        } catch (Throwable e) {
            log.add("FAIL: Unexpected exception caught when "
                    + "setting thresholds");
            e.printStackTrace();
            res = false;
        }

        try {
            monitor.setThresholds(new Float(1.5), new Float(-1.5));
        } catch (Throwable e) {
            log.add("FAIL: Unexpected exception caught when "
                    + "setting init threshold");
            e.printStackTrace();
            return fail("FAILED");
        }

        /* setting notify flag */
        monitor.setNotifyHigh(true);
        monitor.setNotifyLow(false);

        /* setting observed objects */
        try {
            monitor.addObservedObject(null);
            log.add("FAIL: Null observed object added successfully");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        MonotonousFunction object1 = new MonotonousFunction();
        MonotonousFunction object2 = new MonotonousFunction();
        ObjectName object1Name = null;
        ObjectName object2Name = null;
        MBeanServer mBeanServer;

        try {
            mBeanServer = MBeanServerFactory.createMBeanServer();

            object1Name = new ObjectName(MonotonousFunction.SENSOR_NAME_TEMPLATE
                    + "1");
            mBeanServer.registerMBean(object1, object1Name);

            object2Name = new ObjectName(MonotonousFunction.SENSOR_NAME_TEMPLATE
                    + "2");
            mBeanServer.registerMBean(object2, object2Name);
        } catch (Throwable t) {
            log.add("FAIL: Unexpected exception caught "
                    + "when registering MBeans");
            t.printStackTrace();
        }

        try {
            monitor.addObservedObject(object1Name);
        } catch (Throwable t) {
            log.add("FAIL: Unexpected exception caught "
                    + "when adding observed object");
            t.printStackTrace();
            res = false;
        }

        if (!monitor.containsObservedObject(object1Name)) {
            log.add("FAIL: Observed object hadn't been added");
            res = false;
        }

        monitor.removeObservedObject(object1Name);

        if (monitor.containsObservedObject(object1Name)) {
            log.add("FAIL: Observed object hadn't been removed");
            res = false;
        }

        try {
            monitor.addObservedObject(object2Name);
        } catch (Throwable t) {
            log.add("FAIL: Unexpected exception caught "
                    + "when adding observed object");
            t.printStackTrace();
            res = false;
        }

        if ((monitor.getObservedObjects().length != 1)
                && (!monitor.getObservedObjects()[0].equals(object2Name))) {
            log.add("FAIL: getObservedObjects method is incorrect");
            res = false;
        }

        /* setting Observed attribute */
        try {
            monitor.setObservedAttribute(null);
            log.add("FAIL: " + "Null observed attribute added successfully");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            monitor.setObservedAttribute("AnyString");
        } catch (Throwable t) {
            log.add("FAIL: Unexpected exception caught "
                    + "when adding observed attribute");
            t.printStackTrace();
            res = false;
        }

        if (!monitor.getObservedAttribute().equals("AnyString")) {
            log.add("FAIL: Observed attribute hadn't been added");
            res = false;
        }

        try {
            monitor.setObservedAttribute("DoubleAttributeHigh");
        } catch (Throwable t) {
            log.add("FAIL: Unexpected exception caught "
                    + "when adding observed attribute");
            t.printStackTrace();
            res = false;
        }

        if (!monitor.getObservedAttribute().equals("DoubleAttributeHigh")) {
            log.add("FAIL: Observed attribute hadn't been added");
            res = false;
        }

        /* Verifying isActive method */
        if (monitor.isActive()) {
            log.add("FAIL: monitor active before it starts");
            res = false;
        }

        monitor.start();

        if (!monitor.isActive()) {
            log.add("FAIL: isActive method incorrect");
            res = false;
        }

        monitor.stop();

        if (monitor.isActive()) {
            log.add("FAIL: monitor active before it stops");
            res = false;
        }

        /* verifying getters */
        if (monitor.getHighThreshold().floatValue() != 1.5) {
            log.info("FAIL: InitThreshold value changed");
            res = false;
        }
        if (monitor.getLowThreshold().floatValue() != -1.5) {
            log.info("FAIL: InitThreshold value changed");
            res = false;
        }

        if (monitor.getDifferenceMode()) {
            log.info("FAIL: Difference mode flag changed");
            res = false;
        }
        if (monitor.getGranularityPeriod() != 2459743) {
            log.info("FAIL: Granularity period value changed");
            res = false;
        }
        if (!monitor.getNotifyHigh()) {
            log.info("FAIL: Notify flag changed");
            res = false;
        }
        if (monitor.getNotifyLow()) {
            log.info("FAIL: Notify flag changed");
            res = false;
        }

        return res ? pass("PASSED") : fail("FAILED");
    }

    /**
     * This test intended to verify Gauge monitor notifications are sent. Test
     * varies notifyHigh and notifyLow flags and sets attributes that values are
     * higher or lower than thresholds.
     */

    private void primitiveNotificationTest() {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        /* Create sensor and monitor MBeans */
        MonotonousFunction sensor = new MonotonousFunction();
        ObjectName sensorName = null;
        ObjectName monitorName = null;

        monitor = new GaugeMonitor();

        try {
            mBeanServer = MBeanServerFactory.createMBeanServer();

            sensorName = new ObjectName(MonotonousFunction.SENSOR_NAME_TEMPLATE
                    + "1");
            mBeanServer.registerMBean(sensor, sensorName);

            monitorName = new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "gaugemonitor:type=GaugeMonitor,id=1");
            mBeanServer.registerMBean(monitor, monitorName);

        } catch (Throwable t) {
            log.add("FAIL: Unexpected exception caught "
                    + "when registering MBeans");
            t.printStackTrace();
        }

        /* Configure and activate monitor */
        monitor.addNotificationListener(this, null, "handback");
        monitor.setGranularityPeriod(500);
        monitor.setThresholds(new Double(0), new Double(0));
        sensor.setValue(10);
        monitor.setNotifyHigh(true);
        monitor.setNotifyLow(false);
        monitor.setDifferenceMode(false);
        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Value");        
        monitor.start();

        /*
         * DoubleAttributeHigh value is higher than high threshold, NotifyHigh =
         * true, so Notification is to be sent.
         */

        monitor.setThresholds(new Double(-100), new Double(-1000));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
         * DoubleAttributeLow value is lower than low threshold, but NotifyLow =
         * false, so Notification isn't to be sent.
         */
        monitor.setThresholds(new Double(1000), new Double(100));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
         * DoubleAttributeHigh value is higher than high threshold, but
         * NotifyHigh = false, so Notification isn't to be sent.
         */
        monitor.setNotifyHigh(false);
        monitor.setThresholds(new Double(-12341), new Double(-1434321));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
         * DoubleAttributeLow value is lower than low threshold, NotifyLow =
         * true, so Notification is to be sent.
         */
        monitor.setNotifyLow(true);
        monitor.setThresholds(new Double(12341325), new Double(1434321));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int res = new GetterSetterTest().test(args);
        System.exit(res);
    }

}
