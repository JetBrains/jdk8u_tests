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

import java.util.Hashtable;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.harmony.test.func.api.javax.management.share.framework.NotImplementedException;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.ObjectName
 * 
 */
public class ObjectNameTest extends Test {

    /**
     * Test for the constructor ObjectName(java.lang.String)
     * 
     * @see javax.management.ObjectName#ObjectName(java.lang.String)
     */
    public final void testObjectNameString() {
        // Correct names.
        String[] correctNames = new String[] { "com.example:key=value", ":*",
            "*:*", ":key=value", "*:key=value", "com.example:*",
            "??com.example??:*", "*com.example*:*", "com.example:key=value,*",
            ":key=value,*", "*:key=value,*", "??com.example:key=value,*",
            "com*?\"\'=Domain:p1=v1,p2=v2,p3=v3", "?*:*", "**:*", "*?:*",
            "?f?f?:*", "*G*g*:*", "*d?d*:*", "?f*f?:*", "awe?*:*", "*?qwe:*",
            "qwe**:*", "**er:*" };

        for (int i = 0; i < correctNames.length; i++) {
            try {
                assertEquals(correctNames[i], new ObjectName(correctNames[i])
                    .getCanonicalName());
            } catch (MalformedObjectNameException ex) {
                fail("ObjectName: " + correctNames[i] + " is valid");
            } catch (Exception ex) {
                fail("ObjectName: " + correctNames[i], ex);
            }
        }

        // Incorrect names.
        String[] incorrectNames = new String[] { "*:", "??com.example*:", "com:",
            "*com.example??:", "com:key=value=12,,", "com:key=*v", "com:key=?v",
            "com:key1=value, key2=*", "*com.example??:", "com:key=value=12,,",
            "com:key=*v", "com:key=?v", "com:key1=value, key2=*",
            "com:key=v\nv", "com:ke\ny=value", "co\nm:key=value", "*", "?",
            "?:?", "?:?=*" };

        for (int i = 0; i < incorrectNames.length; i++) {
            try {
                new ObjectName(incorrectNames[i]);
                fail("ObjectName: " + incorrectNames[i] + " is invalid");
            } catch (MalformedObjectNameException ex) {
            }
        }

        try {
            new ObjectName("My:Domain:p1=v1,p2=v2,p3=v3");
            fail("MalformedObjectNameException wasn't thrown!");
        } catch (MalformedObjectNameException e) {
        }

        try {
            new ObjectName("My\nDomain:p1=v1,p2=v2,p3=v3");
            fail("MalformedObjectNameException wasn't thrown!");
        } catch (MalformedObjectNameException e) {
        }

        try {
            new ObjectName("MyDomain");
            fail("MalformedObjectNameException wasn't thrown!");
        } catch (MalformedObjectNameException e) {
        }

        try {
            new ObjectName(null);
            fail("NullPointerException wasn't thrown!");
        } catch (NullPointerException e) {
        } catch (MalformedObjectNameException e) {
            fail(e);
        }
    }

    /**
     * Test for the constructor ObjectName(java.lang.String,
     * java.util.Hashtable)
     * 
     * @see javax.management.ObjectName#ObjectName(java.lang.String,
     *      java.util.Hashtable)
     */
    public final void testObjectNameStringHashtable() {
        Hashtable ht = new Hashtable();
        ht.put("p2", "v2");
        ht.put("p3", "v3");
        ht.put("p1", "v1");
        try {
            ObjectName on = new ObjectName("My*?\"\'=Domain", ht);
            assertEquals("My*?\"\'=Domain:p1=v1,p2=v2,p3=v3", on
                .getCanonicalName());
        } catch (Exception ex) {
            fail(ex);
        }

        try {
            new ObjectName("My:Domain", ht);
            fail("MalformedObjectNameException wasn't thrown!");
        } catch (MalformedObjectNameException e) {
        }

        try {
            ht.put("p4", "v4:");
            new ObjectName("MyDomain", ht);
            fail("MalformedObjectNameException wasn't thrown!");
        } catch (MalformedObjectNameException e) {
        }

        try {
            new ObjectName(null, ht);
            fail("NullPointerException wasn't thrown!");
        } catch (NullPointerException e) {
        } catch (MalformedObjectNameException e) {
            fail(e);
        }
    }

    /**
     * Test for the constructor ObjectName(java.lang.String, java.lang.String,
     * java.lang.String)
     * 
     * @see javax.management.ObjectName#ObjectName(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public final void testObjectNameStringStringString() {
        try {
            ObjectName on = new ObjectName("My*?\"\'=Domain", "p1", "v1");
            assertEquals("My*?\"\'=Domain:p1=v1", on.getCanonicalName());
        } catch (Exception ex) {
            fail(ex);
        }

        try {
            new ObjectName("My:Domain", "p1", "v1");
            fail("MalformedObjectNameException wasn't thrown!");
        } catch (MalformedObjectNameException e) {
        }

        try {
            new ObjectName("MyDomain", "p1:", "v1");
            fail("MalformedObjectNameException wasn't thrown!");
        } catch (MalformedObjectNameException e) {
        }

        try {
            new ObjectName("MyDomain", "p1", "v1:");
            fail("MalformedObjectNameException wasn't thrown!");
        } catch (MalformedObjectNameException e) {
        }

        try {
            new ObjectName(null, "p1", "v1");
            fail("NullPointerException wasn't thrown!");
        } catch (NullPointerException e) {
        } catch (MalformedObjectNameException e) {
            fail(e);
        }
    }

    /**
     * Test for the method apply(javax.management.ObjectName)
     * 
     * @see javax.management.ObjectName#apply(javax.management.ObjectName)
     */
    public final void testApply() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");

        assertTrue(new ObjectName("*:*").apply(on));
        assertTrue(new ObjectName("*:p1=v1,p2=v2,p3=v3").apply(on));
        assertTrue(new ObjectName("MyDomain:p1=v1,*").apply(on));
        assertTrue(new ObjectName("My*:p1=v1,p2=v2,p3=v3").apply(on));
        assertTrue(new ObjectName("*Domain*:p1=v1,p2=v2,p3=v3").apply(on));
        assertTrue(new ObjectName("??Domai?:p1=v1,p2=v2,p3=v3").apply(on));
        assertFalse(new ObjectName("??Domai??:p1=v1,p2=v2,p3=v3").apply(on));
        assertFalse(new ObjectName("myDomain:p1=v1,p2=v2,p3=v3").apply(on));
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @see javax.management.ObjectName#equals(java.lang.Object)
     */
    public final void testEquals() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");
        Hashtable ht = new Hashtable();
        ht.put("p2", "v2");
        ht.put("p3", "v3");
        ht.put("p1", "v1");

        assertNotEquals(on, new ObjectName("MyDomain:p1=v1,p2=v2,p3=v4"));
        assertEquals(on, new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3"));
        assertEquals(on, new ObjectName("MyDomain", ht));
    }

    /**
     * Test for the method getCanonicalKeyPropertyListString()
     * 
     * @see javax.management.ObjectName#getCanonicalKeyPropertyListString()
     */
    public final void testGetCanonicalKeyPropertyListString() throws Exception {
        Hashtable ht = new Hashtable();
        ht.put("p2", "v2");
        ht.put("p3", "v3");
        ht.put("p1", "v1");
        ObjectName on1 = new ObjectName("MyDomain:p2=v2,p1=v1,p3=v3");
        ObjectName on2 = new ObjectName("MyDomain", ht);
        final String props = "p1=v1,p2=v2,p3=v3";

        assertEquals(props, on1.getCanonicalKeyPropertyListString());
        assertEquals(props, on2.getCanonicalKeyPropertyListString());
    }

    /**
     * Test for the method getCanonicalName()
     * 
     * @see javax.management.ObjectName#getCanonicalName()
     */
    public final void testGetCanonicalName() throws Exception {
        Hashtable ht = new Hashtable();
        ht.put("p2", "v2");
        ht.put("p3", "v3");
        ht.put("p1", "v1");
        ObjectName on1 = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");
        ObjectName on2 = new ObjectName("MyDomain", ht);
        final String cname = "MyDomain:p1=v1,p2=v2,p3=v3";

        assertEquals(cname, on1.getCanonicalName());
        assertEquals(cname, on2.getCanonicalName());
    }

    /**
     * Test for the method getDomain()
     * 
     * @see javax.management.ObjectName#getDomain()
     */
    public final void testGetDomain() throws Exception {
        assertEquals("MyDomain", new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3")
            .getDomain());
        assertEquals("", new ObjectName(":p1=v1,p2=v2,p3=v3").getDomain());
        assertEquals("MyDoma*??", new ObjectName("MyDoma*??:p1=v1,p2=v2,p3=v3")
            .getDomain());
    }

    /**
     * Test for the method getInstance(javax.management.ObjectName)
     * 
     * @see javax.management.ObjectName#getInstance(javax.management.ObjectName)
     */
    public final void testGetInstanceObjectName() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");
        assertEquals(on, ObjectName.getInstance(on));

        try {
            ObjectName.getInstance((ObjectName) null);
            fail("NullPointerException was not thrown");
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Test for the method getInstance(java.lang.String)
     * 
     * @see javax.management.ObjectName#getInstance(java.lang.String)
     */
    public final void testGetInstanceString() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");
        assertEquals(on, ObjectName.getInstance("MyDomain:p1=v1,p2=v2,p3=v3"));

        try {
            ObjectName.getInstance("My:Domain:p1=v1,p2=v2,p3=v");
            fail("MalformedObjectNameException was not thrown");
        } catch (MalformedObjectNameException ex) {
        }

        try {
            ObjectName.getInstance((String) null);
            fail("NullPointerException was not thrown");
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Test for the method getInstance(java.lang.String, java.util.Hashtable)
     * 
     * @see javax.management.ObjectName#getInstance(java.lang.String,
     *      java.util.Hashtable)
     */
    public final void testGetInstanceStringHashtable() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");
        Hashtable ht = new Hashtable();
        ht.put("p2", "v2");
        ht.put("p3", "v3");
        ht.put("p1", "v1");
        assertEquals(on, ObjectName.getInstance("MyDomain", ht));

        try {
            ObjectName.getInstance("My:Domain", ht);
            fail("MalformedObjectNameException was not thrown");
        } catch (MalformedObjectNameException ex) {
        }

        try {
            ht.put("p4:", "v4=");
            ObjectName.getInstance("MyDomain", ht);
            fail("MalformedObjectNameException was not thrown");
        } catch (MalformedObjectNameException ex) {
        }

        try {
            ObjectName.getInstance((String) null);
            fail("NullPointerException was not thrown");
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Test for the method getInstance(java.lang.String, java.lang.String,
     * java.lang.String)
     * 
     * @see javax.management.ObjectName#getInstance(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public final void testGetInstanceStringStringString() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p1=v1");
        assertEquals(on, ObjectName.getInstance("MyDomain", "p1", "v1"));

        try {
            ObjectName.getInstance("MyDomain:", "p1", "v1");
            fail("MalformedObjectNameException was not thrown");
        } catch (MalformedObjectNameException ex) {
        }

        try {
            ObjectName.getInstance("MyDomain", "p1:", "v1");
            fail("MalformedObjectNameException was not thrown");
        } catch (MalformedObjectNameException ex) {
        }

        try {
            ObjectName.getInstance("MyDomain", "p1", "v1=");
            fail("MalformedObjectNameException was not thrown");
        } catch (MalformedObjectNameException ex) {
        }

        try {
            ObjectName.getInstance(null, null, null);
            fail("NullPointerException was not thrown");
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Test for the method getKeyProperty(java.lang.String)
     * 
     * @see javax.management.ObjectName#getKeyProperty(java.lang.String)
     */
    public final void testGetKeyProperty() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");

        assertEquals("v1", on.getKeyProperty("p1"));
        assertEquals("v2", on.getKeyProperty("p2"));

        try {
            on.getKeyProperty(null);
            fail("NullPointerException was not thrown");
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Test for the method getKeyPropertyList()
     * 
     * @see javax.management.ObjectName#getKeyPropertyList()
     */
    public final void testGetKeyPropertyList() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");
        Hashtable ht = new Hashtable();
        ht.put("p2", "v2");
        ht.put("p3", "v3");
        ht.put("p1", "v1");
        assertEquals(ht, on.getKeyPropertyList());
    }

    /**
     * Test for the method getKeyPropertyListString()
     * 
     * @see javax.management.ObjectName#getKeyPropertyListString()
     */
    public final void testGetKeyPropertyListString() throws Exception {
        ObjectName on = new ObjectName("MyDomain:p2=v2,p1=v1,p3=v3");
        assertEquals("p2=v2,p1=v1,p3=v3", on.getKeyPropertyListString());
    }

    /**
     * Test for the method hashCode()
     * 
     * @see javax.management.ObjectName#hashCode()
     */
    public final void testHashCode() throws Exception {
        Hashtable ht = new Hashtable();
        ht.put("p2", "v2");
        ht.put("p3", "v3");
        ht.put("p1", "v1");
        ObjectName on1 = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");
        ObjectName on2 = new ObjectName("MyDomain", ht);

        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", on1
            .hashCode(), on1.hashCode());
        assertEquals("If two objects are equal according to the equals(Object)"
            + " method, then hashCode method on each of them must"
            + " produce the same integer. ", on1.hashCode(), on2.hashCode());
        assertNotEquals(on1.hashCode(), new ObjectName(
            "MyDomain:p1=v1,p2=v2,p4=v4").hashCode());
    }

    /**
     * Test for the method isDomainPattern()
     * 
     * @see javax.management.ObjectName#isDomainPattern()
     */
    public final void testIsDomainPattern() throws Exception {
        assertFalse(new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3")
            .isDomainPattern());
        assertFalse(new ObjectName("MyDomain:p1=v1,p2=v2,*").isDomainPattern());
        assertTrue(new ObjectName("?yDomain:p1=v1,p2=v2,p3=v3")
            .isDomainPattern());
        assertTrue(new ObjectName("*Domain:p1=v1,p2=v2,p3=v3")
            .isDomainPattern());
    }

    /**
     * Test for the method isPattern()
     * 
     * @see javax.management.ObjectName#isPattern()
     */
    public final void testIsPattern() throws Exception {
        assertFalse(new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3").isPattern());
        assertTrue(new ObjectName("?yDomain:p1=v1,p2=v2,p3=v3").isPattern());
        assertTrue(new ObjectName("MyDomain:p1=v1,p2=v2,*").isPattern());
    }

    /**
     * Test for the method isPropertyPattern()
     * 
     * @see javax.management.ObjectName#isPropertyPattern()
     */
    public final void testIsPropertyPattern() throws Exception {
        assertFalse(new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3")
            .isPropertyPattern());
        assertFalse(new ObjectName("*Domain:p1=v1,p2=v2").isPropertyPattern());
        assertTrue(new ObjectName("MyDomain:p1=v1,p2=v2,*").isPropertyPattern());
        assertTrue(new ObjectName("*Domain:*").isPropertyPattern());
    }

    /**
     * Test for the method quote(java.lang.String)
     * 
     * @see javax.management.ObjectName#quote(java.lang.String)
     */
    public final void testQuote() {
        assertEquals("\"111\\\"111\\*111\\?111\\\\111\\n\"", ObjectName
            .quote("111\"111*111?111\\111\n"));

        try {
            ObjectName.quote(null);
            fail("NullPointerException was not thrown");
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Test for the method setMBeanServer(javax.management.MBeanServer)
     * 
     * @see javax.management.ObjectName#setMBeanServer(javax.management.MBeanServer)
     */
    public final void testSetMBeanServer() {
        // TODO Implement this method
        throw new NotImplementedException("Not implemented.");
    }

    /**
     * Test for the method toString()
     * 
     * @see javax.management.ObjectName#toString()
     */
    public final void testToString() throws Exception {
        ObjectName on1 = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");
        ObjectName on2 = new ObjectName("MyDomain:p1=v1,p2=v2,p3=v3");

        assertEquals(on1.toString(), on2.toString());
    }

    /**
     * Test for the method unquote(java.lang.String)
     * 
     * @see javax.management.ObjectName#unquote(java.lang.String)
     */
    public final void testUnquote() {
        assertEquals("111\"111*111?111\\111\n", ObjectName
            .unquote("\"111\\\"111\\*111\\?111\\\\111\\n\""));

        try {
            ObjectName.unquote("\"111\\\"111\\*111\\?111\\\\111\n\"");
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }

        try {
            ObjectName.unquote("\"111\\\"111\\*111\\?111\\111\\n\"");
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }

        try {
            ObjectName.unquote("\"111\\\"111\\*111?111\\\\111\\n\"");
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }

        try {
            ObjectName.unquote("\"111\\\"111*111\\?111\\\\111\\n\"");
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }

        try {
            ObjectName.unquote("\"111\"111\\*111\\?111\\\\111\\n\"");
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }

        try {
            ObjectName.unquote("111\\\"111\\*111\\?111\\\\111\\n\"");
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
        }

        try {
            ObjectName.unquote(null);
            fail("NullPointerException was not thrown");
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(ObjectNameTest.class, args));
    }
}
