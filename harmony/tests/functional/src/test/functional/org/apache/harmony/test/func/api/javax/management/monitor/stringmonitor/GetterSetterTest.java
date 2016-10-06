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
package org.apache.harmony.test.func.api.javax.management.monitor.stringmonitor;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.monitor.StringMonitor;

import org.apache.harmony.share.Test;

/**
 * This test is intended to check the functionality of StringMonitor class
 * getter and setter methods. The test calls consequently each setter methods,
 * then corresponding getter method is called and its value is compared to that
 * passed as a parameter to setter method. As for CounterMonitor and
 * GaugeMonitor there are three categories of values passed to setter methods:
 * common intermediate values, boundary values, invalid values (they are
 * expected to cause an exception).
 * 
 */

public class GetterSetterTest extends Test {

    public int test() {

        Object verifyObject = new Object();

        boolean res = true;

        StringMonitor monitor = new StringMonitor();

        /* Checking basic functionality of setter/getter methods */

        /* setting notify flag */
        monitor.setNotifyDiffer(true);
        monitor.setNotifyMatch(false);

        /* setting granularity period */
        if (monitor.getGranularityPeriod() != 10000) {
            res = false;
        }

        try {
            monitor.setGranularityPeriod(-1684629);
            System.err.println("FAIL: Exception expected when passing "
                    + "negative value to setGranularityPeriod");
            res = false;
        } catch (IllegalArgumentException e) {
            if (monitor.getGranularityPeriod() != 10000) {
                System.err.println("FAIL: Granularity value changed"
                        + " when exception was raised");
                res = false;
            }
        }

        try {
            monitor.setGranularityPeriod(0);
            System.err.println("FAIL: Exception expected when passing "
                    + "zero value to setGranularityPeriod");
            res = false;
        } catch (IllegalArgumentException e) {
            if (monitor.getGranularityPeriod() != 10000) {
                System.err.println("FAIL: Granularity value changed when "
                        + "exception was raised");
                res = false;
            }
        }

        try {
            monitor.setGranularityPeriod(2459743);
        } catch (Throwable e) {
            System.err.println("FAIL: Unexpected exception caught when "
                    + "setting granularity period");
            e.printStackTrace();
            return fail("FAILED");
        }

        /* Setting String for compare */
        try {
            monitor.setStringToCompare(null);
            System.err.println("FAIL: Null compare string added successfully");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            monitor.setStringToCompare("");
        } catch (Throwable t) {
            System.err.println("FAIL: Unexpected exception caught "
                    + "when adding compare string");
            t.printStackTrace();
            res = false;
        }

        try {
            monitor.setStringToCompare(verifyObject.toString());
        } catch (Throwable t) {
            System.err.println("FAIL: Unexpected exception caught "
                    + "when adding compare string");
            t.printStackTrace();
            res = false;
        }

        /* setting observed objects */
        try {
            monitor.addObservedObject(null);
            System.err.println("FAIL: Null observed object added successfully");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        MyStringBuffer object1 = new MyStringBuffer();
        MyStringBuffer object2 = new MyStringBuffer();
        ObjectName object1Name = null;
        ObjectName object2Name = null;
        MBeanServer mBeanServer;

        try {
            mBeanServer = MBeanServerFactory.createMBeanServer();

            object1Name = new ObjectName(MyStringBuffer.MSB_NAME_TEMPLATE + "1");
            mBeanServer.registerMBean(object1, object1Name);

            object2Name = new ObjectName(MyStringBuffer.MSB_NAME_TEMPLATE + "2");
            mBeanServer.registerMBean(object2, object2Name);
        } catch (Throwable t) {
            System.err.println("FAIL: Unexpected exception caught "
                    + "when registering MBeans");
            t.printStackTrace();
        }

        try {
            monitor.addObservedObject(object1Name);
        } catch (Throwable t) {
            System.err.println("FAIL: Unexpected exception caught "
                    + "when adding observed object");
            t.printStackTrace();
            res = false;
        }

        if (!monitor.containsObservedObject(object1Name)) {
            System.err.println("FAIL: Observed object hadn't been added");
            res = false;
        }

        monitor.removeObservedObject(object1Name);

        if (monitor.containsObservedObject(object1Name)) {
            System.err.println("FAIL: Observed object hadn't been removed");
            res = false;
        }

        try {
            monitor.addObservedObject(object2Name);
        } catch (Throwable t) {
            System.err.println("FAIL: Unexpected exception caught "
                    + "when adding observed object");
            t.printStackTrace();
            res = false;
        }

        if ((monitor.getObservedObjects().length != 1)
                && (!monitor.getObservedObjects()[0].equals(object2Name))) {
            System.err.println("FAIL: getObservedObjects method is incorrect");
            res = false;
        }

        /* setting Observed attribute */
        try {
            monitor.setObservedAttribute(null);
            System.err.println("FAIL: "
                    + "Null observed attribute added successfully");
            res = false;
        } catch (IllegalArgumentException e) {
            /* Correct state */
        }

        try {
            monitor.setObservedAttribute("AnyString");
        } catch (Throwable t) {
            System.err.println("FAIL: Unexpected exception caught "
                    + "when adding observed attribute");
            t.printStackTrace();
            res = false;
        }

        if (!monitor.getObservedAttribute().equals("AnyString")) {
            System.err.println("FAIL: Observed attribute hadn't been added");
            res = false;
        }

        try {
            monitor.setObservedAttribute("DoubleAttributeHigh");
        } catch (Throwable t) {
            System.err.println("FAIL: Unexpected exception caught "
                    + "when adding observed attribute");
            t.printStackTrace();
            res = false;
        }

        if (!monitor.getObservedAttribute().equals("DoubleAttributeHigh")) {
            System.err.println("FAIL: Observed attribute hadn't been added");
            res = false;
        }

        /* Verifying isActive method */
        if (monitor.isActive()) {
            System.err.println("FAIL: monitor active before it starts");
            res = false;
        }

        monitor.start();

        if (!monitor.isActive()) {
            System.err.println("FAIL: isActive method incorrect");
            res = false;
        }

        monitor.stop();

        if (monitor.isActive()) {
            System.err.println("FAIL: monitor active before it stops");
            res = false;
        }

        /* Verifying getter */

        if (!monitor.getStringToCompare().equals(verifyObject.toString())) {
            log.info("FAIL: String to compare changed");
            res = false;
        }

        if (monitor.getGranularityPeriod() != 2459743) {
            log.info("FAIL: Granularity period value changed");
            res = false;
        }
        if (monitor.getNotifyMatch()) {
            log.info("FAIL: Notify flag changed");
            res = false;
        }
        if (!monitor.getNotifyDiffer()) {
            log.info("FAIL: Notify flag changed");
            res = false;
        }

        return res ? pass("PASSED") : fail("FAILED");
    }

    public static void main(String[] args) {
        System.exit(new GetterSetterTest().test(args));
    }
}
