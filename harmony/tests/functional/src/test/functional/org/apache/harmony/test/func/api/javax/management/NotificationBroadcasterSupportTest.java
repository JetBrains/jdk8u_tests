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
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.NotificationBroadcasterSupport
 * 
 */
public class NotificationBroadcasterSupportTest extends Test implements
    NotificationListener {

    /**
     * Notification.
     */
    private Notification n;

    /**
     * Handback.
     */
    private final Object handback = "handback";

    /**
     * Received andback.
     */
    private Object       receivedHB;

    /**
     * Test for the constructor NotificationBroadcasterSupport()
     * 
     * @see javax.management.NotificationBroadcasterSupport#NotificationBroadcasterSupport()
     */
    public final void testNotificationBroadcasterSupport() {
        new NotificationBroadcasterSupport();
    }

    /**
     * Test for the method
     * addNotificationListener(javax.management.NotificationListener,
     * javax.management.NotificationFilter, java.lang.Object)
     * 
     * @see javax.management.NotificationBroadcasterSupport#addNotificationListener(javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public final void testAddNotificationListener() {
        Hello h = new Hello();
        // Test exception.
        try {
            h.addNotificationListener(null, null, null);
            fail("IllegalArgumentException not thrown!");
        } catch (Throwable ex) {
            assertTrue("Wrong exception thrown: " + ex,
                (ex instanceof IllegalArgumentException));
        }

        h.addNotificationListener(this, null, handback);
        h.sayHello();
        assertNotNull("Notification has not been received!", n);
        assertEquals("Wrong handback object received!", handback, receivedHB);

        n = null;
        receivedHB = null;
        h = new Hello();
        NotificationFilterSupport f = new NotificationFilterSupport();
        f.enableType(AttributeChangeNotification.ATTRIBUTE_CHANGE);
        h.addNotificationListener(this, f, null);
        h.setName("New name");
        assertNotNull("Notification has not been received!", n);
        assertTrue("Wrong handback object received!", receivedHB == null);
    }

    /**
     * Test for the method getNotificationInfo()
     * 
     * @see javax.management.NotificationBroadcasterSupport#getNotificationInfo()
     */
    public final void testGetNotificationInfo() {
        try {
            NotificationBroadcasterSupport.class.getDeclaredMethod(
                "getNotificationInfo", new Class[0]);
        } catch (Exception e) {
            fail("The method getNotificationInfo is not implemented!");
        }

        NotificationBroadcasterSupport nbs = new NotificationBroadcasterSupport();
        assertEquals(0, nbs.getNotificationInfo().length);
    }

    /**
     * Test for the method
     * handleNotification(javax.management.NotificationListener,
     * javax.management.Notification, java.lang.Object)
     * 
     * @see javax.management.NotificationBroadcasterSupport#handleNotification(javax.management.NotificationListener,
     *      javax.management.Notification, java.lang.Object)
     */
    public final void testHandleNotification() {
        Hello h = new Hello();
        h.addNotificationListener(this, null, handback);
        h.sayHello();
        assertTrue("The handleNotification() method not invoked!",
            h.isHandleNotifInvoked);
    }

    /**
     * Test for the method
     * removeNotificationListener(javax.management.NotificationListener)
     * 
     * @see javax.management.NotificationBroadcasterSupport#removeNotificationListener(javax.management.NotificationListener)
     */
    public final void testRemoveNotificationListenerNotificationListener() {
        Hello h = new Hello();

        // Test exception.
        try {
            h.removeNotificationListener(this);
            fail("ListenerNotFoundException not thrown!");
        } catch (Throwable ex) {
            assertTrue("Wrong exception thrown: " + ex,
                (ex instanceof ListenerNotFoundException));
        }

        h.addNotificationListener(this, null, null);
        h.addNotificationListener(this, null, handback);

        NotificationFilterSupport f = new NotificationFilterSupport();
        f.enableType(AttributeChangeNotification.ATTRIBUTE_CHANGE);
        h.addNotificationListener(this, f, null);
        h.addNotificationListener(this, f, handback);

        f = new NotificationFilterSupport();
        f.enableType(Hello.SAY_HELLO_INVOKED);
        h.addNotificationListener(this, f, null);
        h.addNotificationListener(this, f, handback);
        try {
            h.removeNotificationListener(this);
        } catch (ListenerNotFoundException e) {
            fail(e);
        }

        h.setName("New name");
        h.sayHello();

        assertNull("Notification has been received!", n);
    }

    /**
     * Test for the method
     * removeNotificationListener(javax.management.NotificationListener,
     * javax.management.NotificationFilter, java.lang.Object)
     * 
     * @see javax.management.NotificationBroadcasterSupport#removeNotificationListener(javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public final void testRemoveNotificationListenerNotificationListenerNotificationFilterObject() {
        Hello h = new Hello();
        // Test exception.
        try {
            h.removeNotificationListener(this, null, null);
            fail("ListenerNotFoundException not thrown!");
        } catch (Throwable ex) {
            assertTrue("Wrong exception thrown: " + ex,
                (ex instanceof ListenerNotFoundException));
        }

        h.addNotificationListener(this, null, null);
        h.addNotificationListener(this, null, null);
        removeNL(h, null, null);
        h.sayHello();
        assertNotNull("Notification has not been received!", n);
        removeNL(h, null, null);
        n = null;
        h.sayHello();
        assertNull("Notification has been received!", n);

        AttributeChangeNotificationFilter acf = new AttributeChangeNotificationFilter();
        acf.enableAttribute("Name");
        NotificationFilterSupport f = new NotificationFilterSupport();
        f.enableType(Hello.SAY_HELLO_INVOKED);
        h.addNotificationListener(this, acf, "handback1");
        h.addNotificationListener(this, f, handback);

        removeNL(h, f, handback);
        n = null;
        h.setName("New name");
        assertNotNull("Notification has not been received!", n);
        assertTrue("Wrong notification received: " + n,
            (n instanceof AttributeChangeNotification));
    }

    /**
     * Test for the method sendNotification(javax.management.Notification)
     * 
     * @see javax.management.NotificationBroadcasterSupport#sendNotification(javax.management.Notification)
     */
    public final void testSendNotification() {
        Hello h = new Hello();
        h.addNotificationListener(this, null, null);
        h.sayHello();
        assertNotNull("Notification has not been received!", n);
    }

    public void setUp() {
        n = null;
        receivedHB = null;
    }

    public void handleNotification(Notification n, Object o) {
        this.n = n;
        receivedHB = o;
        proceed();
        log.debug(n);
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(NotificationBroadcasterSupportTest.class,
            args));
    }

    private void removeNL(NotificationEmitter b, NotificationFilter f, Object o) {
        try {
            b.removeNotificationListener(this, f, o);
        } catch (ListenerNotFoundException e) {
            fail(e);
        }
    }
}
