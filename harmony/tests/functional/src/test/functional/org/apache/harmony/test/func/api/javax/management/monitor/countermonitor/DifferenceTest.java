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
 * In this test randomizer generates a random sequence of numbers, which is
 * monotonous and difference between consequent values if from 1/2*THRESHOLD to
 * 3/2*THRESHOLD.<br>
 * Step by step:
 * <ul>
 * <li>MBean server is started; CounterMonitor and Counter MBeans are
 * registered.
 * <li>CounterMonitor is configured to difference mode.
 * <li>NotificationListener is added to CounterMonitor.
 * <li>Counter is started in a separate thread, CounterMonitor is activated.
 * <li>NotificationListener checks that proper notification is received and
 * threshold is actually reached.
 * </ul>
 * 
 */

public class DifferenceTest extends Test {

    /** Object for synchronization */
    private static final Object sync = new Object();

    /** Flag states randomizer mode to be used */
    private static boolean isMonotonous = false;

    /** Flag states to vary randomizer or not */
    private static boolean isVary = true;

    /** MBean server */
    private MBeanServer server;

    /** Tested monitor */
    private CounterMonitor monitor;

    /** Randomizer (Used as counter: produces seed of int) */
    private Randomizer randomizer;

    /** Randomizer thread */
    private Thread counterThread;

    private ObjectName randomizerName;

    private NotifListener notificationListener = new NotifListener(sync);

    public static void main(String[] args) {
        parseArgs(args);
        System.exit(new DifferenceTest().test());
    }

    public int test() {

        boolean res = true;

        server = MBeanServerFactory.createMBeanServer();

        /* register monitor and counter */
        monitor = new CounterMonitor();

        ObjectName cm;

        randomizer = new Randomizer(isMonotonous, 200);

        monitor.addNotificationListener(notificationListener, null, "handback");

        try {
            cm = new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management.monitor."
                            + "countermonitor:type=CounterMonitor,id=1");
            randomizerName = new ObjectName(Randomizer.RANDOMIZER_NAME
                    + "default");
        } catch (MalformedObjectNameException e) {
            log.info(e.toString());
            return error("INTERNAL ERROR: Invalid object name");
        }

        try {
            server.registerMBean(monitor, cm);
            server.registerMBean(randomizer, randomizerName);
        } catch (JMException e) {
            log.info(e.toString());
            return error("INTERNAL ERROR: MBean registration failed");
        }

        /* setup monitor */
        monitor.addObservedObject(randomizerName);
        monitor.setInitThreshold(new Integer(Randomizer.DEFAULT_THRESHOLD));
        monitor.setGranularityPeriod(100);
        monitor.setNotify(true);
        monitor.setDifferenceMode(true);
        monitor.setObservedAttribute("Value");

        /* run counter and monitor */
        counterThread = new Thread(randomizer);
        counterThread.setDaemon(true);
        counterThread.start();

        monitor.start();

        /* Repeat the action 3 times */
        for (int i = 0; i < 3; i++) {
            notificationListener.setParameters("Value", randomizerName,
                    "jmx.monitor.counter.threshold");
            notificationListener.setPredictMode(true, randomizer);
            randomizer.setIncrement(monitor.getInitThreshold());
            /*
             * Wait for notification handler's notification on sync. And enable
             * a timer that notifies after 5 secs (to prevent test hang if no
             * notification had been sent)
             */
            synchronized (sync) {
                try {
                    sync.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            res &= notificationListener.getRes();

            if (isVary) {
                reconfigure(i);
            }
        }

        return res ? pass("PASSED") : fail("FAILED");
    }

    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("Monotonous")) {
                isMonotonous = true;
            }
            if (args[i].equals("notMonotonous")) {
                isMonotonous = false;
            }
            if (args[i].equals("Vary")) {
                isVary = true;
            }
            if (args[i].equals("dontVary")) {
                isVary = false;
            }
            if (args[i].equals("-isMonotonous")) {
                try {
                    i++;
                    if (args[i].equals("true")) {
                        isMonotonous = true;
                    }
                    if (args[i].equals("false")) {
                        isMonotonous = false;
                    }
                } catch (Exception e) {
                    /* Default value used */
                }
            }
            if (args[i].equals("-isVary")) {
                try {
                    i++;
                    if (args[i].equals("true")) {
                        isVary = true;
                    }
                    if (args[i].equals("false")) {
                        isVary = false;
                    }
                } catch (Exception e) {
                    /* Default value used */
                }
            }
        }
        log.info("Test: Test started. isMonotonous=" + isMonotonous
                + "; isVary=" + isVary);
    }

    /**
     * This method reconfigures monitor to new observed object (with new
     * properties): removes previous object name (delivered as a parameter),
     * registers new MBean, configures monitor to new MBean and new threshold
     * @param cycleNumber 
     */
    private void reconfigure(int cycleNumber) {

        counterThread.stop();

        int newThreshold = Math.abs(500 - (150 * cycleNumber));
        log.info("Test: Monitor reconfigured. New Threshold = " + newThreshold);
        randomizer = new Randomizer(newThreshold, isMonotonous, 300);

        try {
            /* Remove previous object */
            monitor.removeObservedObject(randomizerName);
            server.unregisterMBean(randomizerName);
            /* Register new Object */
            randomizerName = new ObjectName(Randomizer.RANDOMIZER_NAME
                    + cycleNumber);
            server.registerMBean(randomizer, randomizerName);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        /* Reconfigure monitor */
        monitor.addObservedObject(randomizerName);
        monitor.setInitThreshold(new Integer(newThreshold));
        monitor.setGranularityPeriod(100);
        monitor.setObservedAttribute("Value");
                
        notificationListener.setPredictMode(true, randomizer);
        randomizer.setIncrement(monitor.getInitThreshold());
        // monitor.start();

        /* Start new object's thread */
        counterThread = new Thread(randomizer);
        counterThread.setDaemon(true);
        counterThread.start();
    }
}
