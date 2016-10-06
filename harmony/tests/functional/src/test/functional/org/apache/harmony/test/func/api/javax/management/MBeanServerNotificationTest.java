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
package org.apache.harmony.test.func.api.javax.management;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanServerNotification
 * 
 */
public class MBeanServerNotificationTest extends Test implements
    NotificationListener {

    /**
     * MBean server.
     */
    private MBeanServer             mbs;

    /**
     * ObjectName for the Hello MBean.
     */
    private ObjectName              name;

    /**
     * ObjectName for the MBeanServerDelegate MBean.
     */
    private ObjectName              dname;

    /**
     * MBean instance.
     */
    private Hello                   hello;

    /**
     * Received notification.
     */
    private MBeanServerNotification n;

    /**
     * Test for the constructor MBeanServerNotification(java.lang.String,
     * java.lang.Object, long, javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServerNotification#MBeanServerNotification(java.lang.String,
     *      java.lang.Object, long, javax.management.ObjectName)
     */
    public final void testMBeanServerNotification() {
        n = new MBeanServerNotification(
            MBeanServerNotification.REGISTRATION_NOTIFICATION, hello, 0, name);
        verifyNotifacation(MBeanServerNotification.REGISTRATION_NOTIFICATION,
            hello);

        n = new MBeanServerNotification(
            MBeanServerNotification.UNREGISTRATION_NOTIFICATION, hello, 0, name);
        verifyNotifacation(MBeanServerNotification.UNREGISTRATION_NOTIFICATION,
            hello);
    }

    private void verifyNotifacation(String expectedType, Object expectedSource) {
        assertEquals(expectedType, n.getType());
        assertEquals(expectedSource, n.getSource());
        testGetMBeanName();
    }

    /**
     * Test for the method getMBeanName()
     * 
     * @see javax.management.MBeanServerNotification#getMBeanName()
     */
    public final void testGetMBeanName() {
        if (n != null) {
            assertEquals(name, n.getMBeanName());
        }
    }

    /**
     * Register HelloMBean.
     * 
     * @throws Exception
     */
    public final void setUpRegistration() throws Exception {
        n = null;
        // Wait until the notification is received.
        freeze(3000);

        // Register the Hello MBean
        mbs.registerMBean(hello, name);
    }

    /**
     * Verify that REGISTRATION_NOTIFICATION is received.
     */
    public final void testRegistration() {
        // Verify that the notification is received.
        if (n == null) {
            fail("The REGISTRATION_NOTIFICATION "
                + "has not been received in 3 seconds.");
            return;
        }

        assertEquals(MBeanServerNotification.REGISTRATION_NOTIFICATION, n
            .getType());
        verifyNotifacation(MBeanServerNotification.REGISTRATION_NOTIFICATION,
            dname);
    }

    /**
     * Unregister HelloMBean.
     * 
     * @throws Exception
     */
    public final void setUpUnregistration() throws Exception {
        try {
            mbs.registerMBean(hello, name);
        } catch (Throwable ex) {
        }
        n = null;
        // Wait until the notification is received.
        freeze(3000);

        // Unregister the Hello MBean
        mbs.unregisterMBean(name);
    }

    /**
     * Verify that UNREGISTRATION_NOTIFICATION is received.
     */
    public final void testUnregistration() {
        // Verify that the notification is received.
        if (n == null) {
            fail("The UNREGISTRATION_NOTIFICATION "
                + "has not been received in 3 seconds.");
            return;
        }

        assertEquals(MBeanServerNotification.UNREGISTRATION_NOTIFICATION, n
            .getType());
        verifyNotifacation(MBeanServerNotification.UNREGISTRATION_NOTIFICATION,
            dname);
    }

    /**
     * Create MBean server, add notification listener to receive
     * MBeanServerNotifications.
     */
    public final void init() throws Exception {
        // Get the Platform MBean Server
        mbs = MBeanServerFactory.createMBeanServer();

        // Construct ObjectName for the Hello MBean
        name = new ObjectName(
            "org.apache.harmony.test.func.api.javax.management:type=Hello");

        // Construct ObjectName for the MBeanServerDelegate MBean
        dname = new ObjectName("JMImplementation:type=MBeanServerDelegate");

        // Create Hello MBean
        hello = new Hello();

        // Add notification listener
        mbs.addNotificationListener(dname, this, null, "handback");
    }

    /**
     * Handle notification.
     */
    public void handleNotification(Notification n, Object o) {
        this.n = (MBeanServerNotification) n;
        log.debug(n);
        proceed();
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(MBeanServerNotificationTest.class, args));
    }
}
