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

import javax.management.MBeanParameterInfo;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanParameterInfo
 * 
 */
public class MBeanParameterInfoTest extends Test {

    /**
     * Test for the constructor MBeanParameterInfo(java.lang.String,
     * java.lang.String, java.lang.String)
     * 
     * @see javax.management.MBeanParameterInfo#MBeanParameterInfo(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public final void testMBeanParameterInfo() {
        MBeanParameterInfo inf = new MBeanParameterInfo("parameter",
            String.class.getName(), "parameter description");
        assertEquals("parameter", inf.getName());
        assertEquals(String.class.getName(), inf.getType());
        assertEquals("parameter description", inf.getDescription());
    }

    /**
     * Test for the method clone()
     * 
     * @see javax.management.MBeanParameterInfo#clone()
     */
    public final void testClone() {
        MBeanParameterInfo inf1 = new MBeanParameterInfo("parameter",
            String.class.getName(), "parameter description");
        MBeanParameterInfo inf2 = (MBeanParameterInfo) inf1.clone();

        assertEquals(inf1, inf2);
        assertNotSame(inf1, inf2);
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @see javax.management.MBeanParameterInfo#equals(java.lang.Object)
     */
    public final void testEquals() {
        MBeanParameterInfo inf1 = new MBeanParameterInfo("parameter",
            String.class.getName(), "parameter description");
        MBeanParameterInfo inf2 = new MBeanParameterInfo("parameter",
            String.class.getName(), "parameter description");
        assertTrue("The objects are equal!", inf1.equals(inf2));

        inf2 = new MBeanParameterInfo("parameter1", String.class.getName(),
            "parameter description");
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanParameterInfo("parameter", String.class.getName(),
            "parameter description1");
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanParameterInfo("parameter", "type",
            "parameter description");
        assertFalse("The objects are not equal!", inf1.equals(inf2));
    }

    /**
     * Test for the method getType()
     * 
     * @see javax.management.MBeanParameterInfo#getType()
     */
    public final void testGetType() {
        MBeanParameterInfo inf = new MBeanParameterInfo("parameter",
            String.class.getName(), "parameter description");
        assertEquals(String.class.getName(), inf.getType());
    }

    /**
     * Test for the method hashCode()
     * 
     * @see javax.management.MBeanParameterInfo#hashCode()
     */
    public final void testHashCode() {
        MBeanParameterInfo inf1 = new MBeanParameterInfo("parameter",
            String.class.getName(), "parameter description");
        MBeanParameterInfo inf2 = new MBeanParameterInfo("parameter",
            String.class.getName(), "parameter description");

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
        System.exit(TestRunner.run(MBeanParameterInfoTest.class, args));
    }
}
