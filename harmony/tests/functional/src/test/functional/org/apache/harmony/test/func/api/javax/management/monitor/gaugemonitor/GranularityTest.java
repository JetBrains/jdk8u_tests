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
import org.apache.harmony.share.Test;

/**
 * <b>GranularityTest</b><br>
 * This test verifies setGranularityPeriod method.<br>
 * Step by step:
 * <ul>
 * <li>1. Create the monitor and observed MBean objects. The Monitor and
 * Observed MBean has the same granularity period(GP), but the shifted to GP/2
 * value
 * <li>2. After waiting for a reasonable time(~5*GP), change monitors
 * granularity period to GP/10 and receive the corresponding notification
 * </ul>
 */
public class GranularityTest extends Test {

    private static final Object sync = new Object();

    private static NotifListener nListener = new NotifListener(sync);

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.exit(new GranularityTest().test(args));
    }

    public int test() {
        /* Initialization */
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        GaugeMonitor monitor = new GaugeMonitor();
        Impulse sensor = new Impulse(1000, 50, new Integer(3), new Integer(103));
        sensor.setPause(true);

        ObjectName sensorName;

        try {
            server.registerMBean(monitor, new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "gaugemonitor:type=GaugeMonitor,id=1"));
            sensorName = new ObjectName(Impulse.SENSOR_NAME_TEMPLATE + "1");
            server.registerMBean(sensor, sensorName);
        } catch (Throwable t) {
            t.printStackTrace();
            return error(t.toString());
        }
        monitor.addNotificationListener(nListener, null, "handback");
        nListener.setParameters("Value", sensorName, "jmx.monitor.gauge.high");

        /* setup monitor */
        monitor.addObservedObject(sensorName);
        monitor.setObservedAttribute("Value");
        monitor.setGranularityPeriod(1000);
        monitor.setNotifyHigh(true);
        monitor.setNotifyLow(true);
        monitor.setThresholds(new Integer(100), new Integer(0));

        Thread sensorThread = new Thread(sensor);
        sensorThread.setDaemon(true);
        sensorThread.start();

        monitor.start();
        sensor.setPause(false);

        /* Wait 10 seconds (~10 periods) */
        log.add("Test: Observed MBean counter and granularity periods "
                + "can't overlap: wait 5 seconds to verify");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.add("Test:Now check if granularity period has been changed,"
                + " notification sends");
        monitor.setGranularityPeriod(50);

        Timer timer = new Timer(2000, sync);
        timer.setDaemon(true);
        timer.start();
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return error(e.toString());
            }
        }
        if (!(nListener.getRes() && timer.getRes())) {
            return fail("Either notification has been sent "
                    + "when periods doesn't overlap "
                    + "or hasn't been sent when periods should overlap");
        }
        return pass();
    }

}
