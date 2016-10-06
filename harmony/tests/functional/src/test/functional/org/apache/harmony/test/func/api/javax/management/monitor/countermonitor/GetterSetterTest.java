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
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.monitor.CounterMonitor;

import org.apache.harmony.share.Test;

/**
 * This test is intended to test the functionality of CounterMonitor (and
 * supers) getter and setter methods. It calls consequently each setter method
 * and then checks that the value returned by getter method is the same. There
 * are three categories of values passed to each setter method during test:
 * common intermediate values, boundary values (such as Long.MAX_VALUE) and
 * invalid value (nulls or negatives). Invalid values are passed to method as
 * parameters to check that corresponding exception is raised.
 * 
 */

public class GetterSetterTest extends Test implements NotificationListener {

    /** Flag for notification differs from jmx.monitor.counter.threshold */
    private static boolean isNotification = false;

    /** Test result */
    private boolean res;

    /**
     * @param args
     */
    public static void main(String[] args) {
        int res = new GetterSetterTest().test(args);
        System.exit(res);
    }

    /**
     * Test verifies if the notification jmx.monitor.counter.threshold type
     * 
     * @param notification
     * @param handback
     */
    public void handleNotification(Notification notification, Object handback) {
        log.info(notification.getType() + " notification received");
        if (notification.getType().equals("jmx.monitor.counter.threshold")) {
            isNotification = true;
        } else {
            isNotification = false;
        }
        // log.add(String.valueOf(isNotification));
    }

    /**
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {

        res = thresholdTest();

        CounterMonitor monitor = new CounterMonitor();

        /* Checking basic functionality of setter/getter methods */

        /* setting difference mode flag */
        monitor.setDifferenceMode(false);

        /* setting granularity period */
        if (monitor.getGranularityPeriod() != 10000) {
            res = false;
        }
        try {
            monitor.setGranularityPeriod(-1684629);
            log.info("FAIL: Exception expected when passing"
                    + " negative value to setGranularityPeriod");
            res = false;
        } catch (IllegalArgumentException e) {
            if (monitor.getGranularityPeriod() != 10000) {
                log.info("FAIL: Granularity value "
                        + "changed when exception was raised");
                res = false;
            }
        }
        try {
            monitor.setGranularityPeriod(0);
            log.info("FAIL: Exception expected when passing "
                    + "zero value to setGranularityPeriod");
            res = false;
        } catch (IllegalArgumentException e) {
            if (monitor.getGranularityPeriod() != 10000) {
                log.info("FAIL: Granularity value changed "
                        + "when exception was raised");
                res = false;
            }
        }
        try {
            monitor.setGranularityPeriod(2459743);
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception caught "
                    + "when setting granularity period");
            e.printStackTrace();
            return fail("FAILED");
        }

        /* setting init threshold */
        try {
            monitor.setInitThreshold(null);
            log.info("FAIL: Exception expected when "
                    + "passing null value to setInitThreshold");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct condition */
        }

        try {
            Number value = new Integer(-5);
            monitor.setInitThreshold(value);
            log.info("FAIL: Exception expected when passing"
                    + " negative value to setInitThreshold");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct condition */
        }

        try {
            Number value = new Float(234.0);
            monitor.setInitThreshold(value);
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception caught"
                    + " when setting init threshold");
            e.printStackTrace();
            return fail("FAILED");
        }
        try {
            Number value = new Float(-235.5);
            monitor.setInitThreshold(value);
            log.info("FAIL: Exception expected when "
                    + "passing negative value to setInitThreshold");
            res = false;
        } catch (IllegalArgumentException e) {
            Number value = monitor.getInitThreshold();
            if (value.intValue() != 234) {
                log.info("FAIL: InitThreshold value "
                        + "changed when exception raised");
                res = false;
            }
        }
        try {
            Number value = new Integer(436);
            monitor.setInitThreshold(value);
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception caught "
                    + "when setting init threshold");
            e.printStackTrace();
            return fail("FAILED");
        }

        /* setting modulus */
        try {
            monitor.setModulus(null);
            log.info("FAIL: Exception expected when "
                    + "passing null value to setModulus");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct condition */
        }

        try {
            Number value = new Integer(-5);
            monitor.setModulus(value);
            log.info("FAIL: Exception expected when"
                    + " passing negative value to setModulus");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct condition */
        }

        try {
            Number value = new Float(234.0);
            monitor.setModulus(value);
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception caught "
                    + "when setting modulus");
            e.printStackTrace();
            return fail("FAILED");
        }
        try {
            Number value = new Float(-235.5);
            monitor.setModulus(value);
            log.info("FAIL: Exception expected when "
                    + "passing negative value to setModulus");
            res = false;
        } catch (IllegalArgumentException e) {
            Number value = monitor.getModulus();
            if (value.intValue() != 234) {
                log.info("FAIL: Modulus value "
                        + "changed when exception raised");
                res = false;
            }
        }
        try {
            Number value = new Integer(436);
            monitor.setModulus(value);
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception "
                    + "caught when setting modulus");
            e.printStackTrace();
            return fail("FAILED");
        }

        /* setting notify flag */
        monitor.setNotify(true);

        /* setting offset */
        try {
            monitor.setOffset(null);
            log.info("FAIL: Exception expected "
                    + "when passing null value to setOffset");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct condition */
        }

        try {
            Number value = new Integer(-5);
            monitor.setOffset(value);
            log.info("FAIL: Exception expected when "
                    + "passing negative value to setOffset");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct condition */
        }

        try {
            Number value = new Float(234.0);
            monitor.setOffset(value);
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception "
                    + "caught when setting offset");
            e.printStackTrace();
            return fail("FAILED");
        }
        try {
            Number value = new Float(-235.5);
            monitor.setOffset(value);
            log.info("FAIL: Exception expected when "
                    + "passing negative value to setOffset");
            res = false;
        } catch (IllegalArgumentException e) {
            Number value = monitor.getOffset();
            if (value.intValue() != 234) {
                log.info("FAIL: InitThreshold value"
                        + " changed when exception raised");
                res = false;
            }
        }
        try {
            Number value = new Integer(436);
            monitor.setOffset(value);
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception "
                    + "caught when setting offset");
            e.printStackTrace();
            return fail("FAILED");
        }

        if (monitor.getModulus().intValue() != 436) {
            log.info("FAIL: Modulus value changed");
            res = false;
        }
        if (monitor.getInitThreshold().intValue() != 436) {
            log.info("FAIL: InitThreshold value changed");
            res = false;
        }
        if (monitor.getOffset().intValue() != 436) {
            log.info("FAIL: Offset value changed");
            res = false;
        }
        if (monitor.getDifferenceMode() != false) {
            log.info("FAIL: Difference mode flag changed");
            res = false;
        }
        if (monitor.getGranularityPeriod() != 2459743) {
            log.info("FAIL: Granularity period value changed");
            res = false;
        }
        if (monitor.getNotify() != true) {
            log.info("FAIL: Notify flag changed");
            res = false;
        }

        /* checking functionality on boundary values */
        Number value = null;
        long val;
        try {
            monitor.setInitThreshold(new Long(Long.MAX_VALUE));
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception caught "
                    + "when setting InitThreshold as MAX_LONG");
            e.printStackTrace();
            res = false;
        }
        value = monitor.getInitThreshold();
        val = value.longValue();
        if (val != Long.MAX_VALUE) {
            log.info("FAIL: Invalid value returned by getInitThreshold: " + val
                    + " instead of Long.MAX_VALUE");
            res = false;
        }

        try {
            monitor.setOffset(new Long(Long.MAX_VALUE));
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception "
                    + "caught when setting Offset as MAX_LONG");
            e.printStackTrace();
            res = false;
        }
        value = monitor.getOffset();
        val = value.longValue();
        if (val != Long.MAX_VALUE) {
            log.info("FAIL: Invalid value returned by getOffset: " + val
                    + " instead of Long.MAX_VALUE");
            res = false;
        }

        try {
            monitor.setModulus(new Long(Long.MAX_VALUE));
        } catch (Throwable e) {
            log.info("FAIL: Unexpected exception "
                    + "caught when setting Modulus as MAX_LONG");
            e.printStackTrace();
            res = false;
        }
        value = monitor.getModulus();
        val = value.longValue();
        if (val != Long.MAX_VALUE) {
            log.info("FAIL: Invalid value returned by getModulus: " + val
                    + " instead of Long.MAX_VALUE");
            res = false;
        }

        return res ? pass("PASSED") : fail("FAILED");
    }

    /**
     * @return result of the subTest
     */
    public boolean thresholdTest() {
        isNotification = false;
        CounterMonitor monitor = new CounterMonitor();
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        ObjectName obj1, obj2, cm;
        Counter c1, c2;
        try {
            obj1 = new ObjectName(Counter.COUNTER_NAME_TEMPLATE + "1");
            obj2 = new ObjectName(Counter.COUNTER_NAME_TEMPLATE + "2");
            cm = new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "countermonitor:type=CounterMonitor,id=1");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
            log.info("INTERNAL ERROR: Invalid object name");
            return false;
        }
        try {
            c1 = new Counter(0, 100);
            c2 = new Counter(0, 100);
            server.registerMBean(c1, obj1);
            server.registerMBean(c2, obj2);
            server.registerMBean(monitor, cm);
        } catch (JMException e) {
            e.printStackTrace();
            log.info("INTERNAL ERROR: MBean registration failed");
            return false;
        }
        monitor.addObservedObject(obj1);
        monitor.addObservedObject(obj2);
        monitor.setObservedAttribute("Value");
        monitor.setGranularityPeriod(10);
        monitor.setOffset(new Integer(1));
        monitor.setInitThreshold(new Integer(10));
        monitor.setNotify(true);
        monitor.addNotificationListener(this, null, null);
        monitor.setDifferenceMode(false);
        monitor.start();
        /* new Thread(c1).start(); */
        c1.setValue(5);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("Thread interrupted");
            return false;
        }
        Number th1 = monitor.getThreshold(obj1);
        Number th2 = monitor.getThreshold(obj2);
        log.info("Obj1: Attribute value is " + c1.getValue());
        log.info("Threshold: Number class is " + th1.getClass().getName()
                + ", value is " + th1.intValue());
        log.info("Obj2: Attribute value is " + c2.getValue());
        log.info("Threshold: Number class is " + th2.getClass().getName()
                + ", value is " + th2.intValue());
        monitor.setInitThreshold(new Integer(5));
        th1 = monitor.getThreshold(obj1);
        log.info("Init threshold changed");
        if (th1.intValue() != 5) {
            log.info("FAIL: Unexpected value of threshold for obj1");
            return false;
        }
        th2 = monitor.getThreshold(obj2);
        if (th2.intValue() != 5) {
            log.info("FAIL: Unexpected value of threshold for obj2");
            return false;
        }

        if (isNotification) {
            log.info("FAIL:Error notification had been sent");
            return false;
        }
        return true;
    }
}
