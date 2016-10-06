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

import javax.management.MBeanPermission;
import javax.management.ObjectName;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanPermission
 * 
 */
public class MBeanPermissionTest extends Test {

    /**
     * Test for the constructor MBeanPermission(java.lang.String,
     * java.lang.String)
     * 
     * @see javax.management.MBeanPermission#MBeanPermission(java.lang.String,
     *      java.lang.String)
     */
    public final void testMBeanPermissionStringString() {
        String name = "com.example.Resource#Name[com.example:type=resource]";
        MBeanPermission p = new MBeanPermission(name,
            "addNotificationListener, getAttribute");
        assertEquals(name, p.getName());

        try {
            new MBeanPermission("className#member[objectName", "getAttribute");
            new MBeanPermission("", "getAttribute");
            new MBeanPermission(null, "getAttribute");
            fail("IllegalArgumentException not thrown!");
        } catch (IllegalArgumentException ex) {
        }

        try {
            new MBeanPermission(name, "get Attribute");
            new MBeanPermission(name, "");
            new MBeanPermission(name, null);
            fail("IllegalArgumentException not thrown!");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Test for the constructor MBeanPermission(java.lang.String,
     * java.lang.String, javax.management.ObjectName, java.lang.String)
     * 
     * @throws NullPointerException
     * @throws Exception
     * @see javax.management.MBeanPermission#MBeanPermission(java.lang.String,
     *      java.lang.String, javax.management.ObjectName, java.lang.String)
     */
    public final void testMBeanPermissionStringStringObjectNameString()
        throws Exception {
        String name = "org.apache.harmony.test.func.api.javax.management:type=Hello";
        ObjectName oname = new ObjectName(name);
        MBeanPermission p = new MBeanPermission(name, "Hello", oname,
            "getAttribute");
        assertEquals(name + "#Hello[" + oname.getCanonicalName() + "]", p
            .getName());

        new MBeanPermission("-", "-", oname, "getAttribute");
        new MBeanPermission(null, null, null, "getAttribute");

        try {
            new MBeanPermission(null, null, null, "get Attribute");
            new MBeanPermission(null, null, null, "");
            new MBeanPermission(null, null, null, null);
            fail("IllegalArgumentException not thrown!");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @see javax.management.MBeanPermission#equals(java.lang.Object)
     */
    public final void testEquals() {
        assertTrue(new MBeanPermission("com.example",
            "addNotificationListener, getAttribute")
            .equals(new MBeanPermission("com.example",
                "addNotificationListener, getAttribute")));

        assertFalse(new MBeanPermission("com.example",
            "addNotificationListener, getAttribute")
            .equals(new MBeanPermission("com.example",
                "addNotificationListener")));

        assertFalse(new MBeanPermission("com.example",
            "addNotificationListener, getAttribute")
            .equals(new MBeanPermission("com",
                "addNotificationListener, getAttribute")));
    }

    /**
     * Test for the method getActions()
     * 
     * @see javax.management.MBeanPermission#getActions()
     */
    public final void testGetActions() {
        MBeanPermission p = new MBeanPermission("com.example",
            "getAttribute, addNotificationListener");

        assertEquals("addNotificationListener,getAttribute", p.getActions());
    }

    /**
     * Test for the method hashCode()
     * 
     * @see javax.management.MBeanPermission#hashCode()
     */
    public final void testHashCode() {
        MBeanPermission p1 = new MBeanPermission("com.example",
            "getAttribute, addNotificationListener");
        MBeanPermission p2 = new MBeanPermission("com.example",
            "addNotificationListener,getAttribute");

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
     * @throws NullPointerException
     * @throws MalformedObjectNameException
     * @see javax.management.MBeanPermission#implies(java.security.Permission)
     */
    public final void testImplies() throws Exception {
        MBeanPermission p = new MBeanPermission(
            "com.example.Resource#Name[com.example:type=resource]",
            "getAttribute");

        assertFalse(p.implies(new MBeanPermission(
            "com.example#Name[com.example:type=resource]", "getAttribute")));

        assertTrue((new MBeanPermission("#Name[com.example:type=resource]",
            "getAttribute, addNotificationListener")).implies(p));
        assertTrue((new MBeanPermission("*#Name[com.example:type=resource]",
            "getAttribute, addNotificationListener")).implies(p));
         assertTrue((new MBeanPermission(
            "com.example.Resource[com.example:type=resource]",
            "getAttribute, addNotificationListener")).implies(p));
        assertTrue((new MBeanPermission(
            "com.example.Resource#*[com.example:type=resource]",
            "getAttribute, addNotificationListener")).implies(p));
        assertTrue((new MBeanPermission("[com.example:*]",
            "getAttribute, addNotificationListener")).implies(p));
        assertTrue((new MBeanPermission("com.example.Resource#Name",
            "getAttribute, addNotificationListener")).implies(p));
        assertTrue((new MBeanPermission(
            "com.*#Name[com.example:type=resource]",
            "getAttribute, addNotificationListener")).implies(p));
        assertTrue((new MBeanPermission(
            "com.example.Resource[com.example:type=resource]", "*")).implies(p));

        assertTrue((new MBeanPermission(
            "com.example.Resource[com.example:type=resource]", "*")).implies(p));

         assertTrue((new MBeanPermission("*",
         "getAttribute, addNotificationListener")).implies(p));

        ObjectName oname = new ObjectName("com.example:type=resource");

        assertTrue(new MBeanPermission("com.example.Resource", "Name", oname,
            "getAttribute").implies(p));
        assertTrue(new MBeanPermission("com.*", "Name", oname, "getAttribute")
            .implies(p));
        assertTrue(new MBeanPermission("com.example.Resource", "Name",
            new ObjectName("com.example:*"), "getAttribute").implies(p));

        p = new MBeanPermission(

        "com.example.Resource#Name[com.example:type=resource]",

        "getAttribute");

        assertFalse(p.implies(new MBeanPermission(

        "com.example#Name[com.example:type=resource]", "getAttribute")));

        assertTrue((new MBeanPermission("#Name[com.example:type=resource]",

        "getAttribute, addNotificationListener")).implies(p));

        assertTrue((new MBeanPermission("*#Name[com.example:type=resource]",

        "getAttribute, addNotificationListener")).implies(p));

        assertTrue((new MBeanPermission(

        "com.example.Resource[com.example:type=resource]",

        "getAttribute, addNotificationListener")).implies(p));

        assertTrue((new MBeanPermission(

        "com.example.Resource#*[com.example:type=resource]",

        "getAttribute, addNotificationListener")).implies(p));

        assertTrue((new MBeanPermission("[com.example:*]",

        "getAttribute, addNotificationListener")).implies(p));

        assertTrue((new MBeanPermission("com.example.Resource#Name",

        "getAttribute, addNotificationListener")).implies(p));

        assertTrue((new MBeanPermission(

        "com.*#Name[com.example:type=resource]",

        "getAttribute, addNotificationListener")).implies(p));

        assertTrue((new MBeanPermission("*",

        "getAttribute, addNotificationListener")).implies(p));

        oname = new ObjectName("com.example:type=resource");

        assertTrue(new MBeanPermission("com.example.Resource", "Name", oname,

        "getAttribute").implies(p));

        assertTrue(new MBeanPermission(

        "com.*", "Name", oname, "getAttribute").implies(p));

        assertTrue(new MBeanPermission("com.example.Resource", "Name",

        new ObjectName("com.example:*"), "getAttribute").implies(p));

        MBeanPermission p_3 = new MBeanPermission(

        "com.example.Resource[com.example:type=resource]",

        "getAttribute");

        assertFalse(new MBeanPermission(

        "com.example.Resource#Name[com.example:type=resource]",

        "*").implies(p_3));

    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(MBeanPermissionTest.class, args));
    }
}
