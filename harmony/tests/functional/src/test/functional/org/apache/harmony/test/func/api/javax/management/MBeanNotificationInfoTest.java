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
import javax.management.MBeanNotificationInfo;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanNotificationInfo
 * 
 */
public class MBeanNotificationInfoTest extends Test {

    /**
     * Test for the constructor MBeanNotificationInfo(java.lang.String[],
     * java.lang.String, java.lang.String)
     * 
     * @see javax.management.MBeanNotificationInfo#MBeanNotificationInfo(java.lang.String[],
     *      java.lang.String, java.lang.String)
     */
    public final void testMBeanNotificationInfo() {
        MBeanNotificationInfo inf = new MBeanNotificationInfo(
            new String[] { "attribute1.changed, attribute2.changed" },
            AttributeChangeNotification.class.getName(),
            "AttributeChangeNotification");
        assertEquals(AttributeChangeNotification.class.getName(), inf.getName());
        assertEquals("AttributeChangeNotification", inf.getDescription());
    }

    /**
     * Test for the method clone()
     * 
     * @see javax.management.MBeanNotificationInfo#clone()
     */
    public final void testClone() {
        MBeanNotificationInfo inf1 = new MBeanNotificationInfo(
            new String[] { "attribute1.changed, attribute2.changed" },
            AttributeChangeNotification.class.getName(),
            "AttributeChangeNotification");
        MBeanNotificationInfo inf2 = (MBeanNotificationInfo) inf1.clone();

        assertEquals(inf1, inf2);
        assertNotSame(inf1, inf2);
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @see javax.management.MBeanNotificationInfo#equals(java.lang.Object)
     */
    public final void testEquals() {
        MBeanNotificationInfo inf1 = new MBeanNotificationInfo(
            new String[] { "attribute1.changed, attribute2.changed" },
            AttributeChangeNotification.class.getName(),
            "AttributeChangeNotification");
        MBeanNotificationInfo inf2 = new MBeanNotificationInfo(
            new String[] { "attribute1.changed, attribute2.changed" },
            AttributeChangeNotification.class.getName(),
            "AttributeChangeNotification");
        MBeanNotificationInfo inf3 = new MBeanNotificationInfo(
            new String[] { "attribute1.changed, attribute2.changed" },
            AttributeChangeNotification.class.getName(),
            "AttributeChangeNotification3");

        assertTrue("The objects are equal!", inf1.equals(inf1));
        assertTrue("The objects are equal!", inf1.equals(inf2));
        assertFalse("The objects are not equal!", inf1.equals(inf3));
    }

    /**
     * Test for the method getNotifTypes()
     * 
     * @see javax.management.MBeanNotificationInfo#getNotifTypes()
     */
    public final void testGetNotifTypes() {
        String[] types = new String[] { "attribute1.changed, attribute2.changed" };
        MBeanNotificationInfo inf = new MBeanNotificationInfo(types,
            AttributeChangeNotification.class.getName(),
            "AttributeChangeNotification");

        assertEquals(types, inf.getNotifTypes());
    }

    /**
     * Test for the method hashCode()
     * 
     * @see javax.management.MBeanNotificationInfo#hashCode()
     */
    public final void testHashCode() {
        MBeanNotificationInfo inf1 = new MBeanNotificationInfo(
            new String[] { "attribute1.changed, attribute2.changed" },
            AttributeChangeNotification.class.getName(),
            "AttributeChangeNotification");
        MBeanNotificationInfo inf2 = new MBeanNotificationInfo(
            new String[] { "attribute1.changed, attribute2.changed" },
            AttributeChangeNotification.class.getName(),
            "AttributeChangeNotification");

        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", inf1
            .hashCode(), inf1.hashCode());
        assertEquals("If two objects are equal according to the equals(Object)"
            + " method, then hashCode method on each of them must"
            + " produce the same integer. ", inf1.hashCode(), inf2.hashCode());
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(MBeanNotificationInfoTest.class, args));
    }
}
