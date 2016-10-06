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

import javax.management.MBeanAttributeInfo;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanAttributeInfo
 * 
 */
public class MBeanAttributeInfoTest extends Test {

    /**
     * Attribute1
     */
    private MBeanAttributeInfo inf1;

    /**
     * Attribute4.
     */
    private MBeanAttributeInfo inf2;

    /**
     * Test for the constructor MBeanAttributeInfo(java.lang.String,
     * java.lang.String, java.lang.reflect.Method, java.lang.reflect.Method)
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#MBeanAttributeInfo(java.lang.String,
     *      java.lang.String, java.lang.reflect.Method,
     *      java.lang.reflect.Method)
     */
    public final void testMBeanAttributeInfoStringStringMethodMethod()
        throws Exception {
        setAttributes();
        assertEquals("Attribute1", inf1.getName());
        assertEquals("attribute1 description", inf1.getDescription());
    }

    /**
     * Test for the constructor MBeanAttributeInfo(java.lang.String,
     * java.lang.String, java.lang.String, boolean, boolean, boolean)
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#MBeanAttributeInfo(java.lang.String,
     *      java.lang.String, java.lang.String, boolean, boolean, boolean)
     */
    public final void testMBeanAttributeInfoStringStringStringbooleanbooleanboolean()
        throws Exception {
        setAttributes2();
        assertEquals("Attribute1", inf1.getName());
        assertEquals("attribute1 description", inf1.getDescription());
    }

    /**
     * Test for the method clone()
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#clone()
     */
    public final void testClone() throws Exception {
        setAttributes();
        MBeanAttributeInfo inf1 = this.inf1;
        MBeanAttributeInfo inf2 = (MBeanAttributeInfo) inf1.clone();

        assertEquals(inf1, inf2);
        assertNotSame(inf1, inf2);
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#equals(java.lang.Object)
     */
    public final void testEquals() throws Exception {
        setAttributes();
        MBeanAttributeInfo inf1 = this.inf1;
        setAttributes();
        MBeanAttributeInfo inf2 = this.inf1;
        assertTrue("The objects are equal!", inf1.equals(inf2));

        setAttributes2();
        inf1 = this.inf1;
        setAttributes2();
        inf2 = this.inf1;
        assertTrue("The objects are equal!", inf1.equals(inf2));

        Class c = Hello.class;
        inf2 = new MBeanAttributeInfo("Attribute11", "attribute1 description",
            c.getMethod("getAttribute1", new Class[0]), c.getMethod(
                "setAttribute1", new Class[] { String.class }));
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanAttributeInfo("Attribute1", "attribute1 description1",
            c.getMethod("getAttribute1", new Class[0]), c.getMethod(
                "setAttribute1", new Class[] { String.class }));
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanAttributeInfo("Attribute1", "attribute1 description",
            null, c.getMethod("setAttribute1", new Class[] { String.class }));
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanAttributeInfo("Attribute1", "attribute1 description", c
            .getMethod("getAttribute1", new Class[0]), null);
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanAttributeInfo("Attribute11", String.class.getName(),
            "attribute1 description", true, true, false);
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanAttributeInfo("Attribute1", "type",
            "attribute1 description", true, true, false);
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanAttributeInfo("Attribute1", String.class.getName(),
            "attribute1 description1", true, true, false);
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanAttributeInfo("Attribute1", String.class.getName(),
            "attribute1 description", false, true, false);
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanAttributeInfo("Attribute1", String.class.getName(),
            "attribute1 description", true, false, false);
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf1 = new MBeanAttributeInfo("Attribute1", boolean.class.getName(),
            "attribute1 description", true, true, false);
        inf2 = new MBeanAttributeInfo("Attribute1", boolean.class.getName(),
            "attribute1 description", true, true, true);
        assertFalse("The objects are not equal!", inf1.equals(inf2));
    }

    /**
     * Test for the method getType()
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#getType()
     */
    public final void testGetType() throws Exception {
        setAttributes();
        assertEquals(String.class.getName(), inf1.getType());
        assertEquals(boolean.class.getName(), inf2.getType());
        setAttributes2();
        assertEquals(String.class.getName(), inf1.getType());
        assertEquals(boolean.class.getName(), inf2.getType());
    }

    /**
     * Test for the method hashCode()
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#hashCode()
     */
    public final void testHashCode() throws Exception {
        setAttributes();
        MBeanAttributeInfo inf1 = this.inf1;
        setAttributes();
        MBeanAttributeInfo inf2 = this.inf1;

        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", inf1
            .hashCode(), inf1.hashCode());
        assertEquals("If two objects are equal according to the equals(Object)"
            + " method, then hashCode method on each of them must"
            + " produce the same integer. ", inf1.hashCode(), inf2.hashCode());

        setAttributes2();
        inf1 = this.inf1;
        setAttributes2();
        inf2 = this.inf1;

        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", inf1
            .hashCode(), inf1.hashCode());
        assertEquals("If two objects are equal according to the equals(Object)"
            + " method, then hashCode method on each of them must"
            + " produce the same integer. ", inf1.hashCode(), inf2.hashCode());
    }

    /**
     * Test for the method isIs()
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#isIs()
     */
    public final void testIsIs() throws Exception {
        setAttributes();
        assertFalse("Attribute1 is not \"is\" getter!", inf1.isIs());
        assertTrue("Attribute4 is \"is\" getter!", inf2.isIs());
        setAttributes2();
        assertFalse("Attribute1 is not \"is\" getter!", inf1.isIs());
        assertTrue("Attribute4 is \"is\" getter!", inf2.isIs());
    }

    /**
     * Test for the method isReadable()
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#isReadable()
     */
    public final void testIsReadable() throws Exception {
        setAttributes();
        assertTrue("Attribute1 is readable!", inf1.isReadable());
        assertTrue("Attribute4 is readable!", inf2.isReadable());
        setAttributes2();
        assertTrue("Attribute1 is readable!", inf1.isReadable());
        assertTrue("Attribute4 is readable!", inf2.isReadable());
    }

    /**
     * Test for the method isWritable()
     * 
     * @throws Exception
     * @see javax.management.MBeanAttributeInfo#isWritable()
     */
    public final void testIsWritable() throws Exception {
        setAttributes();
        assertTrue("Attribute1 is writable!", inf1.isWritable());
        assertFalse("Attribute4 is not writable!", inf2.isWritable());
        setAttributes2();
        assertTrue("Attribute1 is writable!", inf1.isWritable());
        assertFalse("Attribute4 is not writable!", inf2.isWritable());
    }

    public void setUp() {
        inf1 = null;
        inf2 = null;
    }

    /**
     * Create MBeanAttributeInfo objects for attribute1 and attribute2.
     * 
     * @throws Exception
     */
    private void setAttributes() throws Exception {
        Class c = Hello.class;
        inf1 = new MBeanAttributeInfo("Attribute1", "attribute1 description", c
            .getMethod("getAttribute1", new Class[0]), c.getMethod(
            "setAttribute1", new Class[] { String.class }));

        inf2 = new MBeanAttributeInfo("Attribute4", "attribute4 description", c
            .getMethod("isAttribute4", new Class[0]), null);
    }

    /**
     * Create MBeanAttributeInfo objects for attribute1 and attribute2.
     * 
     * @throws Exception
     */
    private void setAttributes2() throws Exception {
        inf1 = new MBeanAttributeInfo("Attribute1", String.class.getName(),
            "attribute1 description", true, true, false);

        inf2 = new MBeanAttributeInfo("Attribute4", boolean.class.getName(),
            "attribute4 description", true, false, true);
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(MBeanAttributeInfoTest.class, args));
    }
}
