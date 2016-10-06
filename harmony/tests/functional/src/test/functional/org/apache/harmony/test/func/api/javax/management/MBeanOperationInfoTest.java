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

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanOperationInfo
 * 
 */
public class MBeanOperationInfoTest extends Test {

    /**
     * Operation info.
     */
    private MBeanOperationInfo inf1;

    /**
     * Operation info.
     */
    private MBeanOperationInfo inf2;

    /**
     * Operation info.
     */
    private MBeanOperationInfo inf3;

    /**
     * Operation info.
     */
    private MBeanOperationInfo inf4;

    /**
     * Operation info.
     */
    private MBeanOperationInfo inf5;

    /**
     * Test for the constructor MBeanOperationInfo(java.lang.String,
     * java.lang.reflect.Method)
     * 
     * @see javax.management.MBeanOperationInfo#MBeanOperationInfo(java.lang.String,
     *      java.lang.reflect.Method)
     */
    public final void testMBeanOperationInfoStringMethod() {
        assertEquals("sayHello", inf5.getName());
        assertEquals("sayHello description", inf5.getDescription());
    }

    /**
     * Test for the constructor MBeanOperationInfo(java.lang.String,
     * java.lang.String, javax.management.MBeanParameterInfo[],
     * java.lang.String, int)
     * 
     * @see javax.management.MBeanOperationInfo#MBeanOperationInfo(java.lang.String,
     *      java.lang.String, javax.management.MBeanParameterInfo[],
     *      java.lang.String, int)
     */
    public final void testMBeanOperationInfoStringStringMBeanParameterInfoArrayStringint() {
        assertEquals("getAttribute1", inf1.getName());
        assertEquals("getAttribute1 description", inf1.getDescription());
    }

    /**
     * Test for the method clone()
     * 
     * @see javax.management.MBeanOperationInfo#clone()
     */
    public final void testClone() {
        MBeanOperationInfo inf1 = this.inf1;
        MBeanOperationInfo inf2 = (MBeanOperationInfo) inf1.clone();
        assertEquals(inf1, inf2);
        assertNotSame(inf1, inf2);
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @throws Exception
     * @see javax.management.MBeanOperationInfo#equals(java.lang.Object)
     */
    public final void testEquals() throws Exception {
        MBeanOperationInfo inf2 = this.inf1;
        setUp();
        assertTrue("The objects are equal!", inf1.equals(inf2));
        assertTrue("The objects are equal!", inf1.equals(inf1));
        assertFalse("The objects are not equal!", inf1.equals(inf3));
    }

    /**
     * Test for the method getImpact()
     * 
     * @see javax.management.MBeanOperationInfo#getImpact()
     */
    public final void testGetImpact() {
        assertEquals(MBeanOperationInfo.INFO, inf1.getImpact());
        assertEquals(MBeanOperationInfo.ACTION, inf2.getImpact());
        assertEquals(MBeanOperationInfo.ACTION_INFO, inf3.getImpact());
        assertEquals(MBeanOperationInfo.UNKNOWN, inf4.getImpact());
        assertEquals(MBeanOperationInfo.UNKNOWN, inf5.getImpact());
    }

    /**
     * Test for the method getReturnType()
     * 
     * @see javax.management.MBeanOperationInfo#getReturnType()
     */
    public final void testGetReturnType() {
        assertEquals(String.class.getName(), inf1.getReturnType());
        assertEquals(void.class.getName(), inf2.getReturnType());
        assertEquals(Object.class.getName(), inf3.getReturnType());
        assertEquals(void.class.getName(), inf4.getReturnType());
        assertEquals(void.class.getName(), inf5.getReturnType());
    }

    /**
     * Test for the method getSignature()
     * 
     * @see javax.management.MBeanOperationInfo#getSignature()
     */
    public final void testGetSignature() {
        assertEquals(new MBeanParameterInfo[0], inf1.getSignature());
        assertEquals(new MBeanParameterInfo[] { new MBeanParameterInfo(
            "attribute1", String.class.getName(), "attribute1") }, inf2
            .getSignature());
        assertEquals(new MBeanParameterInfo[] { new MBeanParameterInfo(
            "operationParam", Object.class.getName(), "operationParam") }, inf3
            .getSignature());
        assertEquals(new MBeanParameterInfo[0], inf4.getSignature());
        assertEquals(new MBeanParameterInfo[0], inf5.getSignature());
    }

    /**
     * Test for the method hashCode()
     * 
     * @throws Exception
     * @see javax.management.MBeanOperationInfo#hashCode()
     */
    public final void testHashCode() throws Exception {
        MBeanOperationInfo inf2 = this.inf1;
        setUp();
        assertEquals("The hashCode() method must return the same integer "
            + "when it is invoked on the same object more than once!", inf1
            .hashCode(), inf1.hashCode());
        assertEquals("If two objects are equal according to the equals(Object)"
            + " method, then hashCode method on each of them must"
            + " produce the same integer. ", inf1.hashCode(), inf2.hashCode());
    }

    /**
     * Construct MBeanOperationInfo objects.
     * 
     * @throws Exception
     */
    public void setUp() throws Exception {
        final Class c = Hello.class;
        inf1 = new MBeanOperationInfo("getAttribute1",
            "getAttribute1 description", new MBeanParameterInfo[0],
            String.class.getName(), MBeanOperationInfo.INFO);
        inf2 = new MBeanOperationInfo("setAttribute1", "setAttribute1",
            new MBeanParameterInfo[] { new MBeanParameterInfo("attribute1",
                String.class.getName(), "attribute1") }, void.class.getName(),
            MBeanOperationInfo.ACTION);
        inf3 = new MBeanOperationInfo("operation", "operation",
            new MBeanParameterInfo[] { new MBeanParameterInfo("operationParam",
                Object.class.getName(), "operationParam") }, Object.class
                .getName(), MBeanOperationInfo.ACTION_INFO);
        inf4 = new MBeanOperationInfo("sayHello", "sayHello",
            new MBeanParameterInfo[0], void.class.getName(),
            MBeanOperationInfo.UNKNOWN);
        inf5 = new MBeanOperationInfo("sayHello description", c.getMethod(
            "sayHello", new Class[0]));
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        System.exit(TestRunner.run(MBeanOperationInfoTest.class, args));
    }
}
