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

import javax.management.MBeanConstructorInfo;
import javax.management.MBeanParameterInfo;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanConstructorInfo
 * 
 */
public class MBeanConstructorInfoTest extends Test {

    /**
     * Constructor info.
     */
    private MBeanConstructorInfo       inf1;

    /**
     * Constructor info.
     */
    private MBeanConstructorInfo       inf2;

    /**
     * Constructor info.
     */
    private MBeanConstructorInfo       inf3;

    /**
     * Constructor info.
     */
    private MBeanConstructorInfo       inf4;

    /**
     * Constructor parameters.
     */
    private final MBeanParameterInfo[] sig1 = new MBeanParameterInfo[0];

    /**
     * Constructor parameters.
     */
    private final MBeanParameterInfo[] sig2 = new MBeanParameterInfo[] {
        new MBeanParameterInfo("attribute1", String.class.getName(),
            "attribute1"),
        new MBeanParameterInfo("attribute2", String.class.getName(),
            "attribute2"),
        new MBeanParameterInfo("attribute3", String.class.getName(),
            "attribute3"),
        new MBeanParameterInfo("attribute4", boolean.class.getName(),
            "attribute4"),
        new MBeanParameterInfo("userData", String.class.getName(), "userData"), };

    /**
     * Test for the constructor MBeanConstructorInfo(java.lang.String,
     * java.lang.reflect.Constructor)
     * 
     * @see javax.management.MBeanConstructorInfo#MBeanConstructorInfo(java.lang.String,
     *      java.lang.reflect.Constructor)
     */
    public final void testMBeanConstructorInfoStringConstructor() {
        assertEquals("org.apache.harmony.test.func.api.javax."
            + "management.share.Hello", inf1.getName());
        assertEquals("org.apache.harmony.test.func.api.javax."
            + "management.share.Hello", inf3.getName());

        assertEquals("Constructor with parameters", inf2.getDescription());
        assertEquals("Constructor without parameters", inf4.getDescription());
    }

    /**
     * Test for the constructor MBeanConstructorInfo(java.lang.String,
     * java.lang.String, [Ljavax.management.MBeanParameterInfo;)
     * 
     * @see javax.management.MBeanConstructorInfo#MBeanConstructorInfo(java.lang.String,
     *      java.lang.String, [Ljavax.management.MBeanParameterInfo;)
     */
    public final void testMBeanConstructorInfoStringStringMBeanParameterInfoArray() {
        assertEquals("org.apache.harmony.test.func.api.javax."
            + "management.share.Hello", inf2.getName());
        assertEquals("org.apache.harmony.test.func.api.javax."
            + "management.share.Hello", inf4.getName());

        assertEquals("Constructor with parameters", inf2.getDescription());
        assertEquals("Constructor without parameters", inf4.getDescription());
    }

    /**
     * Test for the method clone()
     * 
     * @see javax.management.MBeanConstructorInfo#clone()
     */
    public final void testClone() {
        MBeanConstructorInfo inf1 = this.inf1;
        MBeanConstructorInfo inf2 = (MBeanConstructorInfo) inf1.clone();
        assertEquals(inf1, inf2);
        assertNotSame(inf1, inf2);
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @see javax.management.MBeanConstructorInfo#equals(java.lang.Object)
     */
    public final void testEquals() {
        assertTrue("The objects are equal!", inf3.equals(inf4));
        assertFalse("The objects are not equal!", inf1.equals(inf3));
    }

    /**
     * Test for the method getSignature()
     * 
     * @see javax.management.MBeanConstructorInfo#getSignature()
     */
    public final void testGetSignature() {
        assertEquals(sig1, inf3.getSignature());
        assertEquals(sig1, inf4.getSignature());
        assertEquals(sig2.length, inf1.getSignature().length);
        assertEquals(sig2, inf2.getSignature());
    }

    /**
     * Test for the method hashCode()
     * 
     * @see javax.management.MBeanConstructorInfo#hashCode()
     */
    public final void testHashCode() {
        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", inf1
            .hashCode(), inf1.hashCode());
    }

    /**
     * Create MBeanConstructorInfo objects.
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public void setUp() throws Exception {
        final Class c = Hello.class;

        inf1 = new MBeanConstructorInfo("Constructor with parameters", c
            .getConstructor(new Class[] { String.class, String.class,
                String.class, boolean.class, Object.class }));
        inf2 = new MBeanConstructorInfo(
            "org.apache.harmony.test.func.api.javax.management.share.Hello",
            "Constructor with parameters", sig2);

        inf3 = new MBeanConstructorInfo("Constructor without parameters", c
            .getConstructor(new Class[0]));
        inf4 = new MBeanConstructorInfo(
            "org.apache.harmony.test.func.api.javax.management.share.Hello",
            "Constructor without parameters", sig1);
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(MBeanConstructorInfoTest.class, args));
    }
}
