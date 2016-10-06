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

import org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor.sensors.Impulse;
import org.apache.harmony.test.func.api.javax.management.monitor.share.NotifListener;
import org.apache.harmony.test.func.api.javax.management.monitor.share.Timer;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * <b>BoundaryTest for gauge monitor:</b>
 * <ul>
 * <li><b>testPrimary:</b><br>
 * Primary test verifies setThresholds getHighThreshold and getLowThreshold
 * methods: the test set boundary values as a thresholds
 * <li><b>testNormal:</b><br>
 * The test sets boundary values to observed MBean or as a thresholds and verify
 * sent notifications
 * <li><b>testDifference:</b><br>
 * The test verifies monitor processes boundary values in difference mode
 * 
 */
public class BoundaryTest extends MultiCase {

    /**
     * Set of parameters for all classes:
     * <ol type=1 start=0>
     * <li>Zero value
     * <li>Maximum value
     * <li>Minimum value
     * <li>Middle positive value
     * <li>Middle negative value
     * <li>The smallest positive step
     * <li>The smallest negative step
     * <li>Negative Maximum value (used for difference mode verification)
     * <li>Maximum value - The smallest positive step (used for difference mode
     * verification)
     * </ol>
     */
    private static final Number[][] classesUnderTest = {
            { new Integer(0), new Integer(Integer.MAX_VALUE),
                    new Integer(Integer.MIN_VALUE), new Integer(100),
                    new Integer(-100), new Integer(1), new Integer(-1),
                    new Integer((-1) * Integer.MAX_VALUE),
                    new Integer(Integer.MAX_VALUE - 1) },
            { new Byte((byte) 0), new Byte(Byte.MAX_VALUE),
                    new Byte(Byte.MIN_VALUE), new Byte((byte) 50),
                    new Byte((byte) -50), new Byte((byte) 1),
                    new Byte((byte) -1),
                    new Byte((byte) ((-1) * Byte.MAX_VALUE)),
                    new Byte((byte) (Byte.MAX_VALUE - 1)) },
            { new Short((short) 0), new Short(Short.MAX_VALUE),
                    new Short(Short.MIN_VALUE), new Short((short) 100),
                    new Short((short) -100), new Short((short) 1),
                    new Short((short) -1),
                    new Short((short) ((-1) * Short.MAX_VALUE)),
                    new Short((short) (Short.MAX_VALUE - 1)) },
            { new Float(0), new Float(Float.MAX_VALUE),
                    new Float((-1) * Float.MAX_VALUE), new Float(100),
                    new Float(-100), new Float(Float.MIN_VALUE),
                    new Float((-1) * Float.MIN_VALUE),
                    new Float((-1) * Float.MAX_VALUE),
                    new Float(Float.MAX_VALUE - Float.MAX_VALUE / 5) },
            { new Double(0), new Double(Double.MAX_VALUE),
                    new Double((-1) * Double.MAX_VALUE), new Double(100),
                    new Double(-100), new Double(Double.MIN_VALUE),
                    new Double((-1) * Double.MIN_VALUE),
                    new Double((-1) * Double.MAX_VALUE),
                    new Double(Double.MAX_VALUE - Double.MAX_VALUE / 5) },
            { new Long(0), new Long(Long.MAX_VALUE), new Long(Long.MIN_VALUE),
                    new Long(100), new Long(-100), new Long(1), new Long(-1),
                    new Long((-1) * Long.MAX_VALUE),
                    new Long(Long.MAX_VALUE - 1) } };

    /**
     * Set of parameters used in current test
     */
    private static Number[] testedClass;

    private static MBeanServer server;

    private static GaugeMonitor monitor;

    private static ObjectName sensorName;

    private static Impulse sensor;

    private static final Object sync = new Object();

    private static NotifListener nListener = new NotifListener(sync);

    /**
     * Timer used for prevent test from hangs
     */
    private Timer timer;

    /**
     * @param args
     */
    public static void main(String[] args) {
        parser(args);

        System.exit(new BoundaryTest().test(args));
    }

    public void setUp() {
        try {
            server = MBeanServerFactory.createMBeanServer();
            monitor = new GaugeMonitor();
            sensor = new Impulse(100, 100, testedClass[0], testedClass[1]);

            server.registerMBean(monitor, new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "countermonitor:type=CounterMonitor,id=1"));
            sensorName = new ObjectName(Impulse.SENSOR_NAME_TEMPLATE + "1");
            server.registerMBean(sensor, sensorName);

            monitor.addNotificationListener(nListener, null, "handback");

            /* setup monitor */
            monitor.addObservedObject(sensorName);
            monitor.setObservedAttribute("Value");
            monitor.setGranularityPeriod(50);
            monitor.setNotifyHigh(true);
            monitor.setNotifyLow(true);

            Thread sensorThread = new Thread(sensor);
            sensorThread.setDaemon(true);
            sensorThread.start();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void tearDown() {
        try {
            server.unregisterMBean(new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "countermonitor:type=CounterMonitor,id=1"));
            server.unregisterMBean(sensorName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Primary test verifies setThresholds getHighThreshold and getLowThreshold
     * methods: the test set boundary values as a thresholds
     */
    public Result testPrimary() {
        boolean res = true;

        /* Verifying threshold boundary values */

        try {
            monitor.setThresholds(testedClass[1], testedClass[3]);
            verifyThresholds(testedClass[1], testedClass[3]);
        } catch (Throwable e) {
            e.printStackTrace();
            log.add("FAIL: Unexpected exception caught when "
                    + "setting High Threshold as MAX");
            res = false;
        }

        try {
            monitor.setThresholds(testedClass[3], testedClass[2]);
            verifyThresholds(testedClass[3], testedClass[2]);
        } catch (Throwable e) {
            log.add("FAIL: Unexpected exception caught when setting"
                    + " LowThreshold as MIN");
            e.printStackTrace();
            res = false;
        }

        try {
            monitor.setThresholds(testedClass[2], testedClass[2]);
            verifyThresholds(testedClass[2], testedClass[2]);
        } catch (Throwable e) {
            log.add("FAIL:" + " Unexpected exception caught when setting "
                    + "High Threshold and LowThreshold as MIN");
            e.printStackTrace();
            res = false;
        }

        try {
            monitor.setThresholds(testedClass[1], testedClass[1]);
            verifyThresholds(testedClass[1], testedClass[1]);
        } catch (Throwable e) {
            log.add("FAIL: " + "Unexpected exception caught when setting "
                    + "High Threshold and LowThreshold as MAX");
            e.printStackTrace();
            res = false;
        }

        try {
            monitor.setThresholds(testedClass[2], testedClass[1]);
            log.add("FAIL: Exception expected when passing "
                    + "low value higher than high value to setInitThreshold");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            verifyThresholds(testedClass[1], testedClass[1]);
        } catch (Exception e) {
            log.add("FAIL: Exception expected when passing "
                    + "low value higher than high value to setInitThreshold:"
                    + "incorrect values has been set!");
            res = false;
        }

        try {
            monitor.setThresholds(testedClass[1], testedClass[2]);
            verifyThresholds(testedClass[1], testedClass[2]);
        } catch (Throwable e) {
            log.add("FAIL: " + "Unexpected exception caught when setting "
                    + "High Threshold as MAX and and LowThreshold as MIN");
            e.printStackTrace();
            res = false;
        }
        return (res ? passed() : failed("Test failed"));
    }

    /**
     * The test sets boundary values to observed MBean or as a thresholds and
     * verify sent notifications
     */
    public Result testNormal() {

        monitor.setDifferenceMode(false);

        boolean res = true;

        sensor.setLeapValue(testedClass[1]);
        sensor.setSleepValue(testedClass[0]);

        monitor.setThresholds(testedClass[1], testedClass[2]);
        nListener.setParameters("Value", sensorName, "jmx.monitor.gauge.high");

        log.add("Test: set high threshold as maximum value, "
                + "(counter can achieve it) "
                + "and wait the gauge.high notification");

        enableTimer();
        monitor.start();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        log.add("Test: set low threshold as minimum value "
                + "(counter can achieve it)"
                + "and wait the gauge.low notification");

        nListener.setParameters("Value", sensorName, "jmx.monitor.gauge.low");
        sensor.setSleepValue(testedClass[2]);

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        sensor.setLeapValue(testedClass[3]);
        sensor.setSleepValue(testedClass[4]);

        log.add("Test: Now counter can't achieve thresholds");

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= !(timer.getRes());
        timer.interrupt();

        log.add("Test: Now counter can't achieve "
                + "both minimum and maximum values: "
                + "try to receive three corresponding notifications");

        nListener
                .setParameters("Value", sensorName, NotifListener.UNKNOWN_TYPE);
        monitor.setThresholds(testedClass[5], testedClass[6]);

        enableTimer();
        sensor.setLeapValue(testedClass[3]);
        sensor.setSleepValue(testedClass[4]);
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        if (nListener.getType().equals("jmx.monitor.gauge.high")) {
            nListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.low");
        } else {
            nListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.high");
        }

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        if (nListener.getType().equals("jmx.monitor.gauge.high")) {
            nListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.low");
        } else {
            nListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.high");
        }

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        log.add("Test: Now set as thresholds the nearest to zero values"
                + " and counter set to zero");

        sensor.setLeapValue(testedClass[0]);
        sensor.setSleepValue(testedClass[0]);

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= !(timer.getRes());
        timer.interrupt();

        monitor.stop();
        return (res ? passed() : failed("Test failed"));
    }

    /**
     * The test verifies monitor processes boundary values in difference mode
     */
    public Result testDifference() {

        boolean res = true;

        monitor.setDifferenceMode(true);

        nListener.setParameters("Value", sensorName, "jmx.monitor.gauge.low");

        monitor.setThresholds(testedClass[5], testedClass[6]);

        sensor.setLeapValue(testedClass[1]);
        sensor.setSleepValue(testedClass[1]);
        sensor.setValue(testedClass[1]);

        log.add("Test: Now set counter to maximum state "
                + "(but the counter cannot modify it)"
                + " and set as thresholds nearest to zero values");

        enableTimer();
        monitor.start();

        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= !(timer.getRes());
        timer.interrupt();

        log.add("Test: Now let the counter falls down a little "
                + "(the thresholds remain the same)");

        enableTimer();
        sensor.setLeapValue(testedClass[8]);
        sensor.setSleepValue(testedClass[8]);
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        log.add("Test: Modify the sensor: Now reconfigure counter: "
                + "it can vary more freely "
                + "(the thresholds remain the same)");

        nListener.setParameters("Value", sensorName, "jmx.monitor.gauge.high");
        sensor.setSleepValue(testedClass[4]);
        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        nListener.setParameters("Value", sensorName, "jmx.monitor.gauge.low");

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        log.add("Test: Now counter can rise and fall to maximum value "
                + "(thresholds are maximum and -maximum values)");

        nListener
                .setParameters("Value", sensorName, NotifListener.UNKNOWN_TYPE);
        monitor.setThresholds(testedClass[1], testedClass[7]);

        enableTimer();
        sensor.setLeapValue(testedClass[1]);
        sensor.setSleepValue(testedClass[0]);
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        if (nListener.getType().equals("jmx.monitor.gauge.high")) {
            nListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.low");
        } else {
            nListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.high");
        }

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        if (nListener.getType().equals("jmx.monitor.gauge.high")) {
            nListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.low");
        } else {
            nListener.setParameters("Value", sensorName,
                    "jmx.monitor.gauge.high");
        }

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        res &= nListener.getRes();
        res &= timer.getRes();
        timer.interrupt();

        log.add("Test: Now counter can't rise and fall to maximum value "
                + "(thresholds are the same)");

        sensor.setLeapValue(testedClass[3]);
        sensor.setSleepValue(testedClass[4]);
        /* Notification skipped due to sensor's value changed from maximum value to middle value*/
        nListener.skipNext();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        enableTimer();
        sensor.setPause(false);
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                res = false;
            }
        }
        sensor.setPause(true);
        res &= !(timer.getRes());
        timer.interrupt();

        monitor.stop();
        return (res ? passed() : failed("Test failed"));
    }

    private static void parser(String[] parameters) {
        if (parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                for (int j = 0; j < 6; j++) {
                    if (("java.lang." + parameters[i])
                            .equals(classesUnderTest[j][0].getClass().getName())) {
                        log.add("Parser: " + parameters[i] + " class tested");
                        testedClass = classesUnderTest[j];
                        return;
                    }
                }
            }
        }
        log.add("Parser: Default (Float) parameters used. "
                + "To change tested class, type class name "
                + "(Integer, Byte, Double, Float, Long or Short)");
        testedClass = classesUnderTest[3];
    }

    /**
     * This method used for shorten and more simplicity. It just started timer
     * thread
     */
    private void enableTimer() {
        timer = new Timer(1000, sync);
        timer.setDaemon(true);
        timer.start();
    }

    /**
     * This method used for shorten and more simplicity. It just check if given
     * values is monitor thresholds
     * 
     * @throws Exception
     */
    private void verifyThresholds(Number highToVerify, Number lowToVerify)
            throws Exception {
        if (!((monitor.getHighThreshold().equals(highToVerify)) && (monitor
                .getLowThreshold().equals(lowToVerify)))) {
            throw new Exception("Thresholds hasn't set to the monitor! high="
                    + highToVerify + " low=" + lowToVerify);
        }
    }
}
