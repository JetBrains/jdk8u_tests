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
import org.apache.harmony.test.func.api.javax.management.monitor.share.ErrorProducer;
import org.apache.harmony.test.func.api.javax.management.monitor.share.NotifListener;
import org.apache.harmony.test.func.api.javax.management.monitor.share.Timer;
import org.apache.harmony.test.func.api.javax.management.monitor.share.Unit;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * This test is intended to check the functionality of GaugeMonitor class when
 * its configuration is not correct: jmx.monitor.error.runtime, threshold, type,
 * attribute and MBean notifications are analyzed. <br>
 * <ul>
 * <li><b>testMBeanUnreg</b><br>
 * Test verifies if observed MBean doesn't registered error.mbean notification
 * is sent
 * <li><b>testMBeanRegUnreg</b><br>
 * Test verifies if observed MBean is unregistered error.mbean notification is
 * sent
 * <li><b>testMBeanUnregOneOf</b><br>
 * Test verifies if one of observed MBeans doesn't registered error.mbean
 * notification is sent.<br>
 * After error notification threshold notification is induced.
 * <li><b>testAttributeAbsent</b><br>
 * Test verifies if the observed attribute doesn't exist in the observed MBean
 * error.attribute notification is sent
 * <li><b>testAttributeAbsentInOne</b><br>
 * Test verifies if the observed attribute doesn't exist in one of the observed
 * MBeans error.attribute notification is sent
 * <li><b>testTypeObject</b><br>
 * Test verifies if the observed attribute type is Object (not supported by
 * monitor) error.type notification is sent
 * <li><b>testTypeNull</b><br>
 * Test verifies if the observed attribute type is null error.type notification
 * is sent
 * <li><b>testRuntimeDontRegMonitor</b><br>
 * Test verifies error.attribute notification if the monitor is not registered
 * <li><b>testRuntimeException</b><br>
 * Test verifies error.attribute notification if the monitor is not registered
 * <li><b>testThresholdError</b><br>
 * This test created to verify error.attribute notifications. Test verifies
 * notification to send if thresholds is of not appropriate type.
 * </ul>
 * 
 */
public class ErrorNotificationsTest extends MultiCase {

    /** Test result */
    public static boolean res = true;

    /** Object for synchronization */
    private static final Object sync = new Object();

    /**
     * Timer to prevent test hangs
     */
    private Timer timer;

    /** Notification Listener */
    private NotifListener nListener = new NotifListener(sync);

    /** Monitor to test */
    private GaugeMonitor monitor;

    private ObjectName monitorName;

    /** MyStingBuffer class represents object to observe */
    private ErrorProducer sensor = new ErrorProducer();

    private ObjectName sensorName;

    private MBeanServer server;

    public static void main(String[] args) {
        System.exit(new ErrorNotificationsTest().test(args));
    }

    /** Actions before every test case (common) */
    public void setUp() {

        res = true;
        sensor.setDouble(new Double(0));
        sensor.setNullFlag(false);
        sensor.setExceptionFlag(false);

        server = MBeanServerFactory.createMBeanServer();
        monitor = new GaugeMonitor();

        try {
            monitorName = new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "gaugemonitor:type=GaugeMonitor,id=1");
            sensorName = new ObjectName(SinusFunction.SENSOR_NAME_TEMPLATE
                    + "1");
        } catch (Throwable e) {
            e.printStackTrace();
            log.add("Test: ERROR: Internal error!");
            System.exit(106);
        }

        monitor.addNotificationListener(nListener, null, "handback");
    }

    /** Actions after every test case */
    public void tearDown() {

        monitor.stop();
        monitor = null;

    }

    /**
     * Test verifies if observed MBean doesn't registered error.mbean
     * notification is sent
     * 
     * @throws Exception
     */
    public Result testMBeanUnreg() throws Exception {

        server.registerMBean(monitor, monitorName);

        /* Observed object is unregistered */
        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Double");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setGranularityPeriod(100);

        nListener
                .setParameters("Double", sensorName, "jmx.monitor.error.mbean");

        /*
         * wait for notification handler's notification on sync. And enable
         * timer that notifies after 15 secs (to prevent test hang if no
         * notification had been sent)
         */
        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        if (!res) {
            return failed("mbean.error notification if observed object "
                    + "doesn't registered hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * Test verifies if observed MBean is unregistered error.mbean notification
     * is sent
     * 
     * @throws Exception
     */
    public Result testMBeanRegUnreg() throws Exception {

        server.registerMBean(monitor, monitorName);
        server.registerMBean(sensor, sensorName);

        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Double");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setGranularityPeriod(100);

        monitor.start();

        /* Verify the monitor is started */
        Thread.sleep(200);
        if (!monitor.isActive()) {
            return new Result(Result.ERROR,
                    "Monitor doesn't start in 200 milliseconds");
        }

        nListener
                .setParameters("Double", sensorName, "jmx.monitor.error.mbean");

        enableTimer();
        /* Unregister Observed object */
        server.unregisterMBean(sensorName);
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        if (!res) {
            return failed("mbean.error notification if observed object "
                    + "is unregistered hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * Test verifies if one of observed MBeans doesn't registered error.mbean
     * notification is sent.
     * 
     * After error notification threshold notification is induced.
     * 
     * @throws Exception
     */
    public Result testMBeanUnregOneOf() throws Exception {

        server.registerMBean(monitor, monitorName);
        server.registerMBean(sensor, sensorName);

        monitor.addObservedObject(sensorName);
        /* Add an unregistered observed object */
        ObjectName unregName = new ObjectName(
                SinusFunction.SENSOR_NAME_TEMPLATE + "2");
        monitor.addObservedObject(unregName);
        monitor.setObservedAttribute("Double");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setNotifyLow(true);
        monitor.setDifferenceMode(false);
        monitor.setGranularityPeriod(100);

        nListener.setParameters("Double", unregName, "jmx.monitor.error.mbean");

        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        res &= checkThresholdNotification();

        if (!res) {
            return failed("mbean.error notification if one of observed objects "
                    + "doesn't registered hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * Test verifies if the observed attribute doesn't exist in the observed
     * MBean error.attribute notification is sent
     * 
     * @throws Exception
     */
    public Result testAttributeAbsent() throws Exception {

        server.registerMBean(monitor, monitorName);
        server.registerMBean(sensor, sensorName);

        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("This Attribute is absent");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setGranularityPeriod(100);

        nListener.setParameters("This Attribute is absent", sensorName,
                "jmx.monitor.error.attribute");

        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        /* Now try to set incorrect attribute again */
        nListener.setParameters("This Attribute is still absent", sensorName,
                "jmx.monitor.error.attribute");
        monitor.setObservedAttribute("This Attribute is still absent");

        enableTimer();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        if (!res) {
            return failed("error.attribute notification if the observed attribute "
                    + "doesn't exist hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * Test verifies if the observed attribute doesn't exist in one of the
     * observed MBeans error.attribute notification is sent
     * 
     * @throws Exception
     */
    public Result testAttributeAbsentInOne() throws Exception {

        server.registerMBean(new Unit(), Unit.getUnitOblectName());
        server.registerMBean(monitor, monitorName);
        server.registerMBean(sensor, sensorName);

        monitor.addObservedObject(sensorName);
        /* Observed attribute is absent in Unit MBean */
        monitor.addObservedObject(Unit.getUnitOblectName());
        monitor.setObservedAttribute("Double");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setNotifyLow(true);
        monitor.setGranularityPeriod(100);

        nListener.setParameters("Double", Unit.getUnitOblectName(),
                "jmx.monitor.error.attribute");

        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        res &= checkThresholdNotification();

        if (!res) {
            return failed("error.attribute notification if the observed attribute "
                    + "doesn't exist hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * Test verifies if the observed attribute type is Object (not supported by
     * monitor) error.type notification is sent
     * 
     * @throws Exception
     */
    public Result testTypeObject() throws Exception {

        server.registerMBean(monitor, monitorName);
        server.registerMBean(sensor, sensorName);

        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Object");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setNotifyLow(true);
        monitor.setGranularityPeriod(100);

        nListener.setParameters("Object", sensorName, "jmx.monitor.error.type");

        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        monitor.setObservedAttribute("Double");
        res &= checkThresholdNotification();

        if (!res) {
            return failed("error.type notification if the observed attribute "
                    + "type is differs from possible hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * Test verifies if the observed attribute type is null error.type
     * notification is sent
     * 
     * @throws Exception
     */
    public Result testTypeNull() throws Exception {

        server.registerMBean(monitor, monitorName);
        server.registerMBean(sensor, sensorName);

        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Double");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setNotifyLow(true);
        monitor.setGranularityPeriod(100);

        nListener.setParameters("Double", sensorName, "jmx.monitor.error.type");

        enableTimer();
        sensor.setNullFlag(true);
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        sensor.setNullFlag(false);
        res &= checkThresholdNotification();

        if (!res) {
            return failed("error.type notification if the observed attribute "
                    + "type is differs from possible hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * Test verifies error.attribute notification if the monitor is not
     * registered
     */
    public Result testRuntimeDontRegMonitor() throws Exception {

        server.registerMBean(sensor, sensorName);

        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Double");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setNotifyLow(true);
        monitor.setGranularityPeriod(100);

        nListener.setParameters("Double", sensorName,
                "jmx.monitor.error.runtime");

        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        server.registerMBean(monitor, monitorName);
        res &= checkThresholdNotification();

        if (!res) {
            return failed("error.type notification if the observed attribute "
                    + "type is differs from possible hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * Test verifies error.attribute notification if the monitor is not
     * registered
     */
    public Result testRuntimeException() throws Exception {

        server.registerMBean(sensor, sensorName);
        server.registerMBean(monitor, monitorName);

        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Double");
        monitor.setThresholds(new Double(1.5), new Double(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setNotifyLow(true);
        monitor.setGranularityPeriod(100);

        nListener.setParameters("Double", sensorName,
                "jmx.monitor.error.runtime");

        sensor.setExceptionFlag(true);

        enableTimer();
        monitor.start();
        synchronized (sync) {
            sync.wait();
        }
        res &= timer.getRes();
        res &= nListener.getRes();
        timer.interrupt();

        sensor.setExceptionFlag(false);
        res &= checkThresholdNotification();

        // log.add(String.valueOf(monitor.isActive()));

        if (!res) {
            return failed("error.type notification if the observed attribute "
                    + "type is differs from possible hasn't been received or incorrect");
        }
        return passed();
    }

    /**
     * This test created to verify error.attribute notifications. Test verifies
     * notification to send if thresholds is of not appropriate type.
     */
    public Result testThresholdError() throws Throwable {

        server.registerMBean(sensor, sensorName);
        server.registerMBean(monitor, monitorName);

        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Double");
        monitor.setThresholds(new Float(1.5), new Float(-1.5));
        monitor.setDifferenceMode(false);
        monitor.setNotifyLow(true);
        monitor.setGranularityPeriod(100);

        nListener.setParameters("Double", sensorName,
                "jmx.monitor.error.threshold");

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

        monitor.setThresholds(new Double(1.5), new Double(-1.5));

        res &= checkThresholdNotification();

        if (!res)
            return failed("FAILED (See log for more information)");
        return passed();
    }

    /** Tests use this method to verify is the monitor operate correct */
    private boolean checkThresholdNotification() {

        boolean lRes = true;

        nListener.setParameters("Double", sensorName, "jmx.monitor.gauge.low");

        enableTimer();
        sensor.setDouble(new Double(-5000));
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lRes &= timer.getRes();
        lRes &= nListener.getRes();
        timer.interrupt();

        return lRes;
    }

    /**
     * This method used for shorten and more simplicity. It just started new
     * timer thread
     */
    private void enableTimer() {
        timer = new Timer(2000, sync);
        timer.setDaemon(true);
        timer.start();
    }

}
