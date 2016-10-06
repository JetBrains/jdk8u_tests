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

import java.io.Serializable;
import java.util.Vector;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.NotificationFilterSupport
 * 
 */
public class NotificationFilterSupportTest extends Test implements
    NotificationListener {

    /**
     * MBean server.
     */
    private MBeanServer  mbs;

    /**
     * ObjectName for the Hello MBean.
     */
    private ObjectName   name;

    /**
     * Received notification.
     */
    private Notification n;

    /**
     * MBean instance.
     */
    private Hello        hello;

    /**
     * Test for the constructor NotificationFilterSupport()
     * 
     * @see javax.management.NotificationFilterSupport#NotificationFilterSupport()
     */
    public final void testNotificationFilterSupport() {
        NotificationFilterSupport f = new NotificationFilterSupport();
        assertEquals("No one notification type should be enabled!", 0, f
            .getEnabledTypes().size());
        assertTrue("The instance should be serializable!",
            (Serializable.class
            .isAssignableFrom(f.getClass())));
    }

    /**
     * Disable all types, change attribute1, invoke sayHello.
     */
    public final void setUpDisableAllTypes()
        throws InstanceNotFoundException, AttributeNotFoundException,
        InvalidAttributeValueException, MBeanException, ReflectionException {
        NotificationFilterSupport f = new NotificationFilterSupport();
        f.enableType(AttributeChangeNotification.ATTRIBUTE_CHANGE);
        f.enableType(Hello.SAY_HELLO_INVOKED);
        f.disableAllTypes();
        mbs.addNotificationListener(name, this, f, null);
        freeze(300);
        mbs.setAttribute(name, new Attribute("Attribute1", "New value"));
        mbs.invoke(name, "sayHello", new Object[0], new String[0]);
    }

    /**
     * Test for the method disableAllTypes()
     * 
     * @see javax.management.NotificationFilterSupport#disableAllTypes()
     */
    public final void testDisableAllTypes() {
        assertNull("Notification received!", n);
    }

    /**
     * Disable ATTRIBUTE_CHANGE type, change attribute1.
     */
    public final void setUpDisableType() throws InstanceNotFoundException,
        AttributeNotFoundException, InvalidAttributeValueException,
        MBeanException, ReflectionException {
        NotificationFilterSupport f = new NotificationFilterSupport();
        f.enableType(AttributeChangeNotification.ATTRIBUTE_CHANGE);
        f.disableType(AttributeChangeNotification.ATTRIBUTE_CHANGE);
        mbs.addNotificationListener(name, this, f, null);
        freeze(300);
        mbs.setAttribute(name, new Attribute("Attribute1", "New value"));
    }

    /**
     * Test for the method disableType(java.lang.String)
     * 
     * @see javax.management.NotificationFilterSupport#disableType(java.lang.String)
     */
    public final void testDisableType() {
        assertNull("Notification received!", n);
    }

    /**
     * Enable ATTRIBUTE_CHANGE type, change attribute1.
     */
    public final void setUpEnableType() throws InstanceNotFoundException,
        MBeanException, ReflectionException {
        NotificationFilterSupport f = new NotificationFilterSupport();
        mbs.addNotificationListener(name, this, f, null);
        f = new NotificationFilterSupport();
        f.enableType("say");
        mbs.addNotificationListener(name, this, f, null);
        freeze(300);
        mbs.invoke(name, "sayHello", new Object[0], new String[0]);
    }

    /**
     * Test for the method enableType(java.lang.String)
     * 
     * @see javax.management.NotificationFilterSupport#enableType(java.lang.String)
     */
    public final void testEnableType() {
        assertNotNull("Notification has not been received!", n);

        // Test exception.
        NotificationFilterSupport f = new NotificationFilterSupport();
        try {
            f.enableType(null);
            fail("IllegalArgumentException not thrown!");
        } catch (Throwable ex) {
            assertTrue("Wrong exception thrown: " + ex,
                (ex instanceof IllegalArgumentException));
        }
    }

    /**
     * Test for the method getEnabledTypes()
     * 
     * @see javax.management.NotificationFilterSupport#getEnabledTypes()
     */
    public final void testGetEnabledTypes() {
        NotificationFilterSupport f = new NotificationFilterSupport();
        f.enableType(AttributeChangeNotification.ATTRIBUTE_CHANGE);
        f.enableType(Hello.SAY_HELLO_INVOKED);
        Vector v = f.getEnabledTypes();

        assertTrue("The ATTRIBUTE_CHANGE notification should be enabled!", v
            .contains(AttributeChangeNotification.ATTRIBUTE_CHANGE));
        assertTrue("The SAY_HELLO_INVOKED notification should be enabled!", v
            .contains(Hello.SAY_HELLO_INVOKED));
    }

    /**
     * Test for the method
     * isNotificationEnabled(javax.management.Notification)
     * 
     * @see javax.management.NotificationFilterSupport#isNotificationEnabled(javax.management.Notification)
     */
    public final void testIsNotificationEnabled() {
        Notification n1 = new AttributeChangeNotification(hello, 1, 1, null,
            null, null, null, null);
        Notification n2 = new Notification(Hello.SAY_HELLO_INVOKED, "src", 1);
        NotificationFilterSupport f = new NotificationFilterSupport();
        f.enableType(Hello.SAY_HELLO_INVOKED);

        assertFalse("The AttributeChangeNotification should be disabled!", f
            .isNotificationEnabled(n1));
        assertTrue("The SAY_HELLO_INVOKED notification should be enabled!", f
            .isNotificationEnabled(n2));
    }

    /**
     * Create MBean server, register Hello MBean.
     */
    public final void init() throws Exception {
        // Get the Platform MBean Server
        mbs = MBeanServerFactory.createMBeanServer();

        // Construct the ObjectName for the Hello MBean
        name = new ObjectName(
            "org.apache.harmony.test.func.api.javax.management:type=Hello");

        // Create the Hello MBean
        hello = new Hello();

        // Register the Hello MBean
        mbs.registerMBean(hello, name);
    }

    /**
     * Deregister Hello MBean, shut down the server.
     */
    public final void release() throws Exception {
        mbs.unregisterMBean(name);
        MBeanServerFactory.releaseMBeanServer(mbs);
    }

    /**
     * Handle AttributeChangeNotification notification.
     */
    public void handleNotification(Notification n, Object o) {
        this.n = n;
        log.debug(n);
        proceed();
    }

    /**
     * @throws InstanceNotFoundException
     */
    public final void tearDown() throws InstanceNotFoundException {
        try {
            mbs.removeNotificationListener(name, this);
        } catch (ListenerNotFoundException e) {
        }
        n = null;
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(NotificationFilterSupportTest.class, args));
    }
}
