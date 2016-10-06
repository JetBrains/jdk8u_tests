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
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.Notification
 * 
 */
public class NotificationTest extends Test implements NotificationListener {

    /**
     * MBean server.
     */
    private MBeanServer  mbs;

    /**
     * ObjectName for the Hello MBean.
     */
    private ObjectName   name;

    /**
     * MBean instance.
     */
    private Hello        hello;

    /**
     * Received notification.
     */
    private Notification n;

    /**
     * Test for the constructor Notification(java.lang.String, java.lang.Object,
     * long)
     * 
     * @see javax.management.Notification#Notification(java.lang.String, java.lang.Object,
     *      long)
     */
    public final void testNotificationStringObjectlong() {
        Notification n = new Notification("type", "src", 1);
        assertEquals("type", n.getType());
        assertEquals("src", n.getSource());
        assertEquals(1, n.getSequenceNumber());
    }

    /**
     * Test for the constructor Notification(java.lang.String, java.lang.Object,
     * long, java.lang.String)
     * 
     * @see javax.management.Notification#Notification(java.lang.String, java.lang.Object,
     *      long, java.lang.String)
     */
    public final void testNotificationStringObjectlongString() {
        Notification n = new Notification("type", "src", 1, "msg");
        assertEquals("type", n.getType());
        assertEquals("src", n.getSource());
        assertEquals(1, n.getSequenceNumber());
        assertEquals("msg", n.getMessage());
    }

    /**
     * Test for the constructor Notification(java.lang.String, java.lang.Object,
     * long, long)
     * 
     * @see javax.management.Notification#Notification(java.lang.String, java.lang.Object,
     *      long, long)
     */
    public final void testNotificationStringObjectlonglong() {
        Notification n = new Notification("type", "src", 1, 2);
        assertEquals("type", n.getType());
        assertEquals("src", n.getSource());
        assertEquals(1, n.getSequenceNumber());
        assertEquals(2, n.getTimeStamp());
    }

    /**
     * Test for the constructor Notification(java.lang.String, java.lang.Object,
     * long, long, java.lang.String)
     * 
     * @see javax.management.Notification#Notification(java.lang.String, java.lang.Object,
     *      long, long, java.lang.String)
     */
    public final void testNotificationStringObjectlonglongString() {
        Notification n = new Notification("type", "src", 1, 2, "msg");
        assertEquals("type", n.getType());
        assertEquals("src", n.getSource());
        assertEquals(1, n.getSequenceNumber());
        assertEquals(2, n.getTimeStamp());
        assertEquals("msg", n.getMessage());
    }

    /**
     * Test for the method getMessage()
     * 
     * @see javax.management.Notification#getMessage()
     */
    public final void testGetMessage() {
        assertEquals(Hello.SAY_HELLO_INVOKED_MSG, n.getMessage());
    }

    /**
     * Test for the method getSequenceNumber()
     * 
     * @see javax.management.Notification#getSequenceNumber()
     */
    public final void testGetSequenceNumber() {
        assertEquals(hello.sequenceNumber, n.getSequenceNumber());
    }

    /**
     * Test for the method getTimeStamp()
     * 
     * @see javax.management.Notification#getTimeStamp()
     */
    public final void testGetTimeStamp() {
        assertEquals(hello.timeStamp, n.getTimeStamp());
    }

    /**
     * Test for the method getType()
     * 
     * @see javax.management.Notification#getType()
     */
    public final void testGetType() {
        assertEquals(Hello.SAY_HELLO_INVOKED, n.getType());
    }

    /**
     * Test for the method getUserData()
     * 
     * @see javax.management.Notification#getUserData()
     */
    public final void testGetUserData() {
        assertEquals(hello.userData, n.getUserData());
    }

    /**
     * Test for the method setSequenceNumber(long)
     * 
     * @see javax.management.Notification#setSequenceNumber(long)
     */
    public final void testSetSequenceNumber() {
        Notification n = new Notification("type", "src", 1);
        assertEquals(1, n.getSequenceNumber());
        n.setSequenceNumber(123);
        assertEquals(123, n.getSequenceNumber());
    }

    /**
     * Test for the method setSource(java.lang.Object)
     * 
     * @see javax.management.Notification#setSource(java.lang.Object)
     */
    public final void testSetSource() {
        Notification n = new Notification("type", "src", 1);
        assertEquals("src", n.getSource());
        n.setSource("new src");
        assertEquals("new src", n.getSource());
    }

    /**
     * Test for the method setTimeStamp(long)
     * 
     * @see javax.management.Notification#setTimeStamp(long)
     */
    public final void testSetTimeStamp() {
        Notification n = new Notification("type", "src", 1);
        n.setTimeStamp(123);
        assertEquals(123, n.getTimeStamp());
    }

    /**
     * Test for the method setUserData(java.lang.Object)
     * 
     * @see javax.management.Notification#setUserData(java.lang.Object)
     */
    public final void testSetUserData() {
        Notification n = new Notification("type", "src", 1);
        assertNull(n.getUserData());
        n.setUserData("User data");
        assertEquals("User data", n.getUserData());
    }

    /**
     * Test for the method toString()
     * 
     * @see javax.management.Notification#toString()
     */
    public final void testToString() {
        try {
            Notification.class.getDeclaredMethod("toString", new Class[0]);
        } catch (SecurityException e) {
            printStackTrace(e);
        } catch (NoSuchMethodException e) {
            fail("The method toString() is not overridden");
        }
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

        // Add notification listener
        mbs.addNotificationListener(name, this, null, "handback");

        // Wait until the notification is received.
        freeze(3000);

        // Invoke sayHello method
        mbs.invoke(name, "sayHello", new Object[0], new String[0]);
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
        this.n = n;
        log.debug(n);
        assertEquals(name, n.getSource());
        assertEquals("handback", o);
        proceed();
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(NotificationTest.class, args));
    }
}
