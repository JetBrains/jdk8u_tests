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

import java.util.Vector;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeChangeNotificationFilter;
import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.AttributeChangeNotificationFilter
 * 
 */
public class AttributeChangeNotificationFilterTest extends Test implements
    NotificationListener {

    /**
     * MBean server.
     */
    private MBeanServer                       mbs;

    /**
     * ObjectName for the Hello MBean.
     */
    private ObjectName                        name;

    /**
     * Received notification.
     */
    private AttributeChangeNotification       n;

    /**
     * Notification fileter.
     */
    private AttributeChangeNotificationFilter f;

    /**
     * Test for the constructor AttributeChangeNotificationFilter()
     * 
     * @see javax.management.AttributeChangeNotificationFilter#AttributeChangeNotificationFilter()
     */
    public final void testAttributeChangeNotificationFilter() {
        AttributeChangeNotificationFilter f = new AttributeChangeNotificationFilter();
        assertEquals(0, f.getEnabledAttributes().size());
    }

    public final void setUpDisableAllAttributes() throws Exception {
        f = new AttributeChangeNotificationFilter();
        f.enableAttribute("Name");
        f.disableAllAttributes();
        mbs.addNotificationListener(name, this, f, "handback");
        freeze(100);
        mbs.setAttribute(name, new Attribute("Name", "New name"));
    }

    /**
     * Test for the method disableAllAttributes()
     * 
     * @see javax.management.AttributeChangeNotificationFilter#disableAllAttributes()
     */
    public final void testDisableAllAttributes() {
        assertNull("Notification has been received!", n);
    }

    public final void setUpDisableAttribute() throws Exception {
        f = new AttributeChangeNotificationFilter();
        f.enableAttribute("Name");
        f.disableAttribute("Name");
        mbs.addNotificationListener(name, this, f, "handback");
        freeze(100);
        mbs.setAttribute(name, new Attribute("Name", "New name123"));
    }

    /**
     * Test for the method disableAttribute(java.lang.String)
     * 
     * @see javax.management.AttributeChangeNotificationFilter#disableAttribute(java.lang.String)
     */
    public final void testDisableAttribute() {
        assertNull("Notification has been received!", n);
    }

    public final void setUpEnableAttribute() throws Exception {
        f = new AttributeChangeNotificationFilter();
        f.enableAttribute("Name");
        mbs.addNotificationListener(name, this, f, "handback");
        freeze(100);
        mbs.setAttribute(name, new Attribute("Name", "New name"));
    }

    /**
     * Test for the method enableAttribute(java.lang.String)
     * 
     * @see javax.management.AttributeChangeNotificationFilter#enableAttribute(java.lang.String)
     */
    public final void testEnableAttribute() {
        assertNotNull("Notification has not been received!", n);

        // Test exception.
        f = new AttributeChangeNotificationFilter();
        try {
            f.enableAttribute(null);
            fail("IllegalArgumentException not thrown!");
        } catch (Throwable ex) {
            assertTrue("Wrong exception thrown: " + ex,
                (ex instanceof IllegalArgumentException));
        }
    }

    public final void setUpGetEnabledAttributes() throws Exception {
        f = new AttributeChangeNotificationFilter();
        f.enableAttribute("Name");
        f.enableAttribute("Attribute1");
        f.enableAttribute("Attribute2");
        f.disableAttribute("Attribute2");
        mbs.addNotificationListener(name, this, f, "handback");
        freeze(100);
        mbs.setAttribute(name, new Attribute("Name", "New name"));
    }

    /**
     * Test for the method getEnabledAttributes()
     * 
     * @see javax.management.AttributeChangeNotificationFilter#getEnabledAttributes()
     */
    public final void testGetEnabledAttributes() {
        Vector v = f.getEnabledAttributes();
        assertNotNull("Notification has not been received!", n);
        assertTrue("The attribute Name should be enabled!", v.contains("Name"));
        assertTrue("The attribute Attribute1 should be enabled!", v
            .contains("Attribute1"));
        assertFalse("The attribute Attribute2 should not be enabled!", v
            .contains("Attribute2"));
    }

    public final void setUpIsNotificationEnabled() throws Exception {
        f = new AttributeChangeNotificationFilter();
        f.enableAttribute("Name");
        mbs.addNotificationListener(name, this, f, "handback");
        freeze(100);
        mbs.setAttribute(name, new Attribute("Name", "New name"));
    }

    /**
     * Test for the method isNotificationEnabled(javax.management.Notification)
     * 
     * @see javax.management.AttributeChangeNotificationFilter#isNotificationEnabled(javax.management.Notification)
     */
    public final void testIsNotificationEnabled() {
        assertTrue("Notification should be enabled for the attribute Name!", f
            .isNotificationEnabled(n));
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
            try {
                mbs.setAttribute(name, new Attribute("Attribute1", "New name"));
            } catch (Exception e1) {
                printStackTrace(e1);
            }
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
        System.exit(TestRunner.run(AttributeChangeNotificationFilterTest.class,
            args));
    }
}