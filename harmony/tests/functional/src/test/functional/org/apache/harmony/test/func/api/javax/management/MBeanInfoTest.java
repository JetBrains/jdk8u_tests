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
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanInfo
 * 
 */
public class MBeanInfoTest extends Test {

    /**
     * MBeanInfo object.
     */
    private MBeanInfo               inf;

    /**
     * MBeanInfo object.
     */
    private MBeanInfo               inf2;

    /**
     * Attributes.
     */
    private MBeanAttributeInfo[]    attributes;

    /**
     * Constructors.
     */
    private MBeanConstructorInfo[]  constructors;

    /**
     * Operations.
     */
    private MBeanOperationInfo[]    operations;

    /**
     * Notifications.
     */
    private MBeanNotificationInfo[] notifications;

    /**
     * Test for the constructor MBeanInfo(java.lang.String, java.lang.String,
     * javax.management.MBeanAttributeInfo[],
     * javax.management.MBeanConstructorInfo[],
     * javax.management.MBeanOperationInfo[],
     * javax.management.MBeanNotificationInfo[])
     * 
     * @see javax.management.MBeanInfo#MBeanInfo(java.lang.String,
     *      java.lang.String, javax.management.MBeanAttributeInfo[],
     *      javax.management.MBeanConstructorInfo[],
     *      javax.management.MBeanOperationInfo[],
     *      javax.management.MBeanNotificationInfo[])
     */
    public final void testMBeanInfo() {
        // Tested below.
    }

    /**
     * Test for the method clone()
     * 
     * @see javax.management.MBeanInfo#clone()
     */
    public final void testClone() {
        MBeanInfo inf1 = (MBeanInfo) inf.clone();
        assertEquals(inf1, inf);
        assertNotSame(inf1, inf);
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @throws Exception
     * @see javax.management.MBeanInfo#equals(java.lang.Object)
     */
    public final void testEquals() throws Exception {
        MBeanInfo inf1 = inf;
        init();
        assertTrue("The objects are equal!", inf.equals(inf));
        assertTrue("The objects are equal!", inf.equals(inf1));
    }

    /**
     * Test for the method getAttributes()
     * 
     * @see javax.management.MBeanInfo#getAttributes()
     */
    public final void testGetAttributes() {
        assertEquals(attributes, inf.getAttributes());
    }

    /**
     * Test for the method getClassName()
     * 
     * @see javax.management.MBeanInfo#getClassName()
     */
    public final void testGetClassName() {
        assertEquals(Hello.class.getName(), inf.getClassName());
    }

    /**
     * Test for the method getConstructors()
     * 
     * @see javax.management.MBeanInfo#getConstructors()
     */
    public final void testGetConstructors() {
        assertEquals(constructors, inf.getConstructors());
    }

    /**
     * Test for the method getDescription()
     * 
     * @see javax.management.MBeanInfo#getDescription()
     */
    public final void testGetDescription() {
        assertEquals("HelloMBean description", inf.getDescription());
    }

    /**
     * Test for the method getNotifications()
     * 
     * @see javax.management.MBeanInfo#getNotifications()
     */
    public final void testGetNotifications() {
        assertEquals(notifications, inf.getNotifications());
    }

    /**
     * Test for the method getOperations()
     * 
     * @see javax.management.MBeanInfo#getOperations()
     */
    public final void testGetOperations() {
        assertEquals(operations, inf.getOperations());
    }

    /**
     * Test for the method hashCode()
     * 
     * @throws Exception
     * @see javax.management.MBeanInfo#hashCode()
     */
    public final void testHashCode() throws Exception {
        MBeanInfo inf1 = inf;
        init();
        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", inf1
            .hashCode(), inf1.hashCode());
        assertEquals("If two objects are equal according to the equals(Object)"
            + " method, then hashCode method on each of them must"
            + " produce the same integer. ", inf.hashCode(), inf1.hashCode());

        inf1 = inf2;
        init();
        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", inf1
            .hashCode(), inf1.hashCode());
        assertEquals("If two objects are equal according to the equals(Object)"
            + " method, then hashCode method on each of them must"
            + " produce the same integer. ", inf1.hashCode(), inf2.hashCode());
    }

    /**
     * Construct MBeanInfo object.
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IntrospectionException
     */
    public void init() throws Exception {
        final Class c = Hello.class;
        attributes = new MBeanAttributeInfo[] {
            new MBeanAttributeInfo("attribute1", "attribute1 desc", c
                .getMethod("getAttribute1", new Class[0]), c.getMethod(
                "setAttribute1", new Class[] { String.class })),
            new MBeanAttributeInfo("attribute4", "attribute4 desc", c
                .getMethod("isAttribute4", new Class[0]), null) };
        constructors = new MBeanConstructorInfo[] {
            new MBeanConstructorInfo("constructor1", c
                .getConstructor(new Class[0])),
            new MBeanConstructorInfo("constructor2", c
                .getConstructor(new Class[] { String.class, String.class,
                    String.class, boolean.class, Object.class })) };
        operations = new MBeanOperationInfo[] {
            new MBeanOperationInfo("sayHello", c.getMethod("sayHello",
                new Class[0])),
            new MBeanOperationInfo("sayHello", c.getMethod("operation",
                new Class[] { Object.class })) };
        notifications = new MBeanNotificationInfo[] {
            new MBeanNotificationInfo(
                new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
                AttributeChangeNotification.class.getName(), "desc"),
            new MBeanNotificationInfo(new String[] { Hello.SAY_HELLO_INVOKED },
                Hello.class.getName(), "sayHello desc") };
        inf = new MBeanInfo(Hello.class.getName(), "HelloMBean description",
            attributes, constructors, operations, notifications);
        inf2 = new MBeanInfo(Hello.class.getName(), null, null, null, null,
            null);
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(MBeanInfoTest.class, args));
    }
}
