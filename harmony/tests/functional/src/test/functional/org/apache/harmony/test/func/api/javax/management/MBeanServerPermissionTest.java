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

import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.PropertyPermission;

import javax.management.MBeanServerPermission;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanServerPermission
 * 
 */
public class MBeanServerPermissionTest extends Test {

    /**
     * Test for the constructor MBeanServerPermission(java.lang.String)
     * 
     * @see javax.management.MBeanServerPermission#MBeanServerPermission(java.lang.String)
     */
    public final void testMBeanServerPermissionString() {
        MBeanServerPermission p = new MBeanServerPermission("createMBeanServer");
        assertEquals("createMBeanServer", p.getName());

        try {
            new MBeanServerPermission(null);
            fail("NullPointerException wasn't thrown!");
        } catch (NullPointerException ex) {
        }

        try {
            new MBeanServerPermission(" ");
            fail("IllegalArgumentException wasn't thrown!");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Test for the constructor MBeanServerPermission(java.lang.String,
     * java.lang.String)
     * 
     * @see javax.management.MBeanServerPermission#MBeanServerPermission(java.lang.String,
     *      java.lang.String)
     */
    public final void testMBeanServerPermissionStringString() {
        MBeanServerPermission p = new MBeanServerPermission(
            "createMBeanServer", null);
        assertEquals("createMBeanServer", p.getName());

        try {
            new MBeanServerPermission(null, null);
            fail("NullPointerException wasn't thrown!");
        } catch (NullPointerException ex) {
        }

        try {
            new MBeanServerPermission(" ", null);
            fail("IllegalArgumentException wasn't thrown!");
        } catch (IllegalArgumentException ex) {
        }

        try {
            new MBeanServerPermission("createMBeanServer", "action");
            fail("IllegalArgumentException wasn't thrown!");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @see javax.management.MBeanServerPermission#equals(java.lang.Object)
     */
    public final void testEquals() {
        assertTrue(new MBeanServerPermission("createMBeanServer")
            .equals(new MBeanServerPermission("createMBeanServer")));

        assertFalse(new MBeanServerPermission("createMBeanServer")
            .equals(new MBeanServerPermission("findMBeanServer")));
    }

    /**
     * Test for the method hashCode()
     * 
     * @see javax.management.MBeanServerPermission#hashCode()
     */
    public final void testHashCode() {
        MBeanServerPermission p1 = new MBeanServerPermission(
            "createMBeanServer");
        MBeanServerPermission p2 = new MBeanServerPermission(
            "createMBeanServer");

        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", p1
            .hashCode(), p1.hashCode());
        assertEquals("If two objects are equal according to the equals(Object)"
            + " method, then hashCode method on each of them must"
            + " produce the same integer. ", p1.hashCode(), p2.hashCode());
    }

    /**
     * Test for the method implies(java.security.Permission)
     * 
     * @see javax.management.MBeanServerPermission#implies(java.security.Permission)
     */
    public final void testImplies() {
        MBeanServerPermission p1 = new MBeanServerPermission(
            "createMBeanServer , findMBeanServer , "
                + "newMBeanServer , releaseMBeanServer");
        MBeanServerPermission p2 = new MBeanServerPermission(
            "createMBeanServer");
        MBeanServerPermission p3 = new MBeanServerPermission("newMBeanServer");
        MBeanServerPermission p4 = new MBeanServerPermission("*");
        MBeanServerPermission p5 = new MBeanServerPermission("findMBeanServer");
        MBeanServerPermission p6 = new MBeanServerPermission("releaseMBeanServer");

        assertTrue(new MBeanServerPermission("*").implies(p2));
        assertTrue(p1.implies(p2));
        assertFalse(p2.implies(p1));
        assertTrue(p2.implies(p3));
        assertFalse(p3.implies(p2));
        assertTrue(p4.implies(p3));
        assertFalse(p3.implies(p4));
        assertTrue(p4.implies(p1));
        assertTrue(p1.implies(p4));
        assertTrue(p1.implies(p5));
        assertTrue(p1.implies(p6));
        assertFalse(p5.implies(p1));
        assertFalse(p6.implies(p1));
        assertFalse(p6.implies(p5));
        assertFalse(p6.implies(p4));
        assertFalse(p6.implies(p3));
        assertFalse(p6.implies(p2));
    }

    /**
     * Test for the method newPermissionCollection()
     * 
     * @see javax.management.MBeanServerPermission#newPermissionCollection()
     */
    public final void testNewPermissionCollection() {
        MBeanServerPermission p = new MBeanServerPermission(
            "createMBeanServer,findMBeanServer");
        MBeanServerPermission p1 = new MBeanServerPermission(
            "createMBeanServer");
        MBeanServerPermission p2 = new MBeanServerPermission("findMBeanServer");
        MBeanServerPermission p3 = new MBeanServerPermission("newMBeanServer");
        PermissionCollection col = new MBeanServerPermission(
            "releaseMBeanServer").newPermissionCollection();

        assertFalse(col.isReadOnly());
        assertFalse(col.implies(p));

        col.add(p1);
        assertTrue(col.implies(p1));
        assertTrue(col.implies(p3));
        assertFalse(col.implies(p2));
        col.add(p2);

        try {
            col.add(new PropertyPermission("*", "read"));
            fail("IllegalArgumentException wasn't thrown!");
        } catch (IllegalArgumentException ex) {
        }

        col.setReadOnly();
        assertTrue(col.isReadOnly());

        try {
            col.add(p);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        Enumeration en = col.elements();
        assertTrue("PermissionCollection should not be empty!", en
            .hasMoreElements());
        while (en.hasMoreElements()) {
            MBeanServerPermission perm = (MBeanServerPermission) en
                .nextElement();
            if (!perm.implies(p1) && !perm.implies(p2)) {
                fail(perm + ": actions should be one of the following: "
                    + "createMBeanServer,findMBeanServer");
            }
        }
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(MBeanServerPermissionTest.class, args));
    }
}
