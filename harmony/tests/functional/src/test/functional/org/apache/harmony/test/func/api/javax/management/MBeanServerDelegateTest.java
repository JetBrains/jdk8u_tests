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

import javax.management.AttributeChangeNotification;
import javax.management.AttributeChangeNotificationFilter;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServerDelegate;
import javax.management.Notification;
import javax.management.NotificationFilter;

import org.apache.harmony.test.func.api.javax.management.share.SimpleNotificationListener;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;
import org.apache.harmony.test.func.api.javax.management.share.framework.Utils;

/**
 * Test for the class javax.management.MBeanServerDelegate
 * 
 */
public class MBeanServerDelegateTest extends Test {

    /**
     * Notification litener.
     */
    private SimpleNotificationListener listener;

    /**
     * Notification filter.
     */
    private NotificationFilter         filter;

    /**
     * MBeanServerDelegate object.
     */
    private MBeanServerDelegate        mbsd;

    /**
     * Handback object.
     */
    private String                     hb = "handback";

    /**
     * Notification object.
     */
    private Notification               n1;

    /**
     * Notification object.
     */
    private Notification               n2;

    /**
     * Test for the constructor MBeanServerDelegate()
     * 
     * @see javax.management.MBeanServerDelegate#MBeanServerDelegate()
     */
    public final void testMBeanServerDelegate() {
        new MBeanServerDelegate();
    }

    /**
     * Test for the method
     * addNotificationListener(javax.management.NotificationListener,
     * javax.management.NotificationFilter, java.lang.Object)
     * 
     * @see javax.management.MBeanServerDelegate#addNotificationListener(javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public final void testAddNotificationListener() {
        mbsd.addNotificationListener(listener, filter, hb);
        mbsd.sendNotification(n1);
        assertEquals(n1, listener.notification);
        assertEquals(hb, listener.handback);

        mbsd.sendNotification(n2);
        assertEquals(n1, listener.notification);
        assertEquals(hb, listener.handback);

        try {
            mbsd.addNotificationListener(null, filter, hb);
            fail("IllegalArgumentException wasn't thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Test for the method getImplementationName()
     * 
     * @see javax.management.MBeanServerDelegate#getImplementationName()
     */
    public final void testGetImplementationName() {
        assertNotNull(mbsd.getImplementationName());
    }

    /**
     * Test for the method getImplementationVendor()
     * 
     * @see javax.management.MBeanServerDelegate#getImplementationVendor()
     */
    public final void testGetImplementationVendor() {
        assertNotNull(mbsd.getImplementationVendor());
    }

    /**
     * Test for the method getImplementationVersion()
     * 
     * @see javax.management.MBeanServerDelegate#getImplementationVersion()
     */
    public final void testGetImplementationVersion() {
        assertNotNull(mbsd.getImplementationVersion());
    }

    /**
     * Test for the method getMBeanServerId()
     * 
     * @see javax.management.MBeanServerDelegate#getMBeanServerId()
     */
    public final void testGetMBeanServerId() {
        assertNotNull(mbsd.getMBeanServerId());
    }

    /**
     * Test for the method getNotificationInfo()
     * 
     * @see javax.management.MBeanServerDelegate#getNotificationInfo()
     */
    public final void testGetNotificationInfo() {
        MBeanNotificationInfo inf = mbsd.getNotificationInfo()[0];
        String[] types = inf.getNotifTypes();
        if (types.length == 2) {
            if (!("JMX.mbean.registered".equals(types[0]) || "JMX.mbean.registered"
                .equals(types[1]))) {
                fail("The notification type JMX.mbean.registered is not "
                    + "contained in the array, \nreturned by the "
                    + "getNotifTypes() method. \nActual: "
                    + Utils.arrayToString(types, ", "));
            }

            if (!("JMX.mbean.registered".equals(types[0]) || "JMX.mbean.registered"
                .equals(types[1]))) {
                fail("The notification type JMX.mbean.unregistered is not "
                    + "contained in the array, \nreturned by the "
                    + "getNotifTypes() method. \nActual: "
                    + Utils.arrayToString(types, ", "));
            }
        }
    }

    /**
     * Test for the method getSpecificationName()
     * 
     * @see javax.management.MBeanServerDelegate#getSpecificationName()
     */
    public final void testGetSpecificationName() {
        assertEquals("Java Management Extensions", mbsd.getSpecificationName());
    }

    /**
     * Test for the method getSpecificationVendor()
     * 
     * @see javax.management.MBeanServerDelegate#getSpecificationVendor()
     */
    public final void testGetSpecificationVendor() {
        assertEquals("Sun Microsystems", mbsd.getSpecificationVendor());
    }

    /**
     * Test for the method getSpecificationVersion()
     * 
     * @see javax.management.MBeanServerDelegate#getSpecificationVersion()
     */
    public final void testGetSpecificationVersion() {
        assertEquals("1.2 Maintenance Release", mbsd.getSpecificationVersion());
    }

    /**
     * Test for the method
     * removeNotificationListener(javax.management.NotificationListener)
     * 
     * @see javax.management.MBeanServerDelegate#removeNotificationListener(javax.management.NotificationListener)
     */
    public final void testRemoveNotificationListenerNotificationListener() {
        try {
            mbsd.removeNotificationListener(listener);
            fail("ListenerNotFoundException wasn't thrown!");
        } catch (ListenerNotFoundException e) {
        }

        mbsd.addNotificationListener(listener, filter, hb);
        mbsd.addNotificationListener(listener, filter, hb + hb);
        listener.handback = null;
        listener.notification = null;
        try {
            mbsd.removeNotificationListener(listener);
            mbsd.sendNotification(n1);
            assertNull(listener.notification);
            assertNull(listener.handback);
        } catch (ListenerNotFoundException e) {
            fail(e);
        }
    }

    /**
     * Test for the method
     * removeNotificationListener(javax.management.NotificationListener,
     * javax.management.NotificationFilter, java.lang.Object)
     * 
     * @see javax.management.MBeanServerDelegate#removeNotificationListener(javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public final void testRemoveNotificationListenerNotificationListenerNotificationFilterObject() {
        try {
            mbsd.removeNotificationListener(listener, filter, hb);
            fail("ListenerNotFoundException wasn't thrown!");
        } catch (ListenerNotFoundException e) {
        }

        mbsd.addNotificationListener(listener, filter, hb);
        mbsd.addNotificationListener(listener, filter, hb);
        listener.handback = null;
        listener.notification = null;
        try {
            mbsd.removeNotificationListener(listener, filter, hb);
            mbsd.sendNotification(n1);
            assertEquals(n1, listener.notification);
            assertEquals(hb, listener.handback);
        } catch (ListenerNotFoundException e) {
            fail(e);
        }

        listener.handback = null;
        listener.notification = null;
        try {
            mbsd.removeNotificationListener(listener, filter, hb);
            mbsd.sendNotification(n1);
            assertNull(listener.notification);
            assertNull(listener.handback);
        } catch (ListenerNotFoundException e) {
            fail(e);
        }
    }

    /**
     * Test for the method sendNotification(javax.management.Notification)
     * 
     * @see javax.management.MBeanServerDelegate#sendNotification(javax.management.Notification)
     */
    public final void testSendNotification() {
        mbsd.addNotificationListener(listener, filter, hb);
        mbsd.sendNotification(n1);
        assertEquals(n1, listener.notification);
        assertEquals(hb, listener.handback);
    }

    public void tearDown() {
        listener.handback = null;
        listener.notification = null;
        try {
            mbsd.removeNotificationListener(listener);
        } catch (ListenerNotFoundException e) {
        }
    }

    /**
     * Initialize the notofocation listener and filter.
     */
    public void init() {
        listener = new SimpleNotificationListener();
        AttributeChangeNotificationFilter filter = new AttributeChangeNotificationFilter();
        filter.enableAttribute("Attribute1");
        filter.disableAttribute("Attribute2");
        this.filter = filter;
        mbsd = new MBeanServerDelegate();
        n1 = new AttributeChangeNotification(this, 1, System
            .currentTimeMillis(), "Attribute1 changed", "Attribute1",
            "java.lang.String", "oldVal1", "newVal1");
        n2 = new AttributeChangeNotification(this, 2, System
            .currentTimeMillis(), "Attribute2 changed", "Attribute2",
            "java.lang.String", "oldVal2", "newVal2");
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(MBeanServerDelegateTest.class, args));
    }
}
