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

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.AttributeChangeNotification
 * 
 */
public class AttributeChangeNotificationTest extends Test implements
    NotificationListener {

    /**
     * MBean server.
     */
    private MBeanServer                 mbs;

    /**
     * ObjectName for the Hello MBean.
     */
    private ObjectName                  name;

    /**
     * Received notification.
     */
    private AttributeChangeNotification n;

    /**
     * Test for the constructor AttributeChangeNotification(java.lang.Object,
     * long, long, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.Object, java.lang.Object)
     * 
     * @throws NotImplementedException
     * @see javax.management.AttributeChangeNotification#AttributeChangeNotification(java.lang.Object,
     *      long, long, java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.Object, java.lang.Object)
     */
    public final void testAttributeChangeNotification() {
        long t = System.currentTimeMillis();
        AttributeChangeNotification n = new AttributeChangeNotification(this,
            1, t, "Name changed", "Name", "java.lang.String", "HelloMBean",
            "New HelloMBean");

        assertSame(this, n.getSource());
        assertEquals(n.getSequenceNumber(), 1);
        assertEquals(n.getTimeStamp(), t);
        assertEquals(n.getMessage(), "Name changed");
        assertEquals(n.getAttributeName(), "Name");
        assertEquals(n.getAttributeType(), "java.lang.String");
        assertEquals(n.getOldValue(), "HelloMBean");
        assertEquals(n.getNewValue(), "New HelloMBean");
    }

    /**
     * Test for the method getAttributeName()
     * 
     * @throws NotImplementedException
     * @throws NotImplementedException
     * @throws
     * @see javax.management.AttributeChangeNotification#getAttributeName()
     */
    public final void testGetAttributeName() {
        assertEquals("Name", n.getAttributeName());
    }

    /**
     * Test for the method getAttributeType()
     * 
     * @see javax.management.AttributeChangeNotification#getAttributeType()
     */
    public final void testGetAttributeType() {
        assertEquals("java.lang.String", n.getAttributeType());
    }

    /**
     * Test for the method getNewValue()
     * 
     * @see javax.management.AttributeChangeNotification#getNewValue()
     */
    public final void testGetNewValue() {
        assertEquals("New name", n.getNewValue());
    }

    /**
     * Test for the method getOldValue()
     * 
     * @see javax.management.AttributeChangeNotification#getOldValue()
     */
    public final void testGetOldValue() {
        assertEquals("HelloMBean", n.getOldValue());
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
        Hello mbean = new Hello();

        // Register the Hello MBean
        mbs.registerMBean(mbean, name);

        // Add notification listener
        mbs.addNotificationListener(name, this, null, "handback");

        // Invoke sayHello method
        // mbs.invoke(name, "sayHello", new Object[0], new String[0]);

        // Wait until the notification is received.
        freeze(3000);

        // Change attribute
        mbs.setAttribute(name, new Attribute("Name", "New name"));
    }

    /**
     * Check if the notification is received.
     */
    public final void setUp() {
        if (n == null) {
            fail("The AttributeChangeNotification "
                + "has not been received in 3 seconds.");
            finish();
        }
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
        this.n = (AttributeChangeNotification) n;
        assertEquals("Invalid handback object!", "handback", o);
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
        System
            .exit(TestRunner.run(AttributeChangeNotificationTest.class, args));
    }
}