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

import javax.management.MBeanFeatureInfo;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanFeatureInfo
 * 
 */
public class MBeanFeatureInfoTest extends Test {

    /**
     * Test for the constructor MBeanFeatureInfo(java.lang.String,
     * java.lang.String)
     * 
     * @see javax.management.MBeanFeatureInfo#MBeanFeatureInfo(java.lang.String,
     *      java.lang.String)
     */
    public final void testMBeanFeatureInfo() {
        new MBeanFeatureInfo("name", "description");
        new MBeanFeatureInfo("name", null);
    }

    /**
     * Test for the method equals(java.lang.Object)
     * 
     * @see javax.management.MBeanFeatureInfo#equals(java.lang.Object)
     */
    public final void testEquals() {
        MBeanFeatureInfo inf1 = new MBeanFeatureInfo("name", "description");
        MBeanFeatureInfo inf2 = new MBeanFeatureInfo("name", "description");
        assertTrue("The objects are equal!", inf1.equals(inf2));

        inf2 = new MBeanFeatureInfo("name1", "description");
        assertFalse("The objects are not equal!", inf1.equals(inf2));

        inf2 = new MBeanFeatureInfo("name", "description2");
        assertFalse("The objects are not equal!", inf1.equals(inf2));
    }

    /**
     * Test for the method getDescription()
     * 
     * @see javax.management.MBeanFeatureInfo#getDescription()
     */
    public final void testGetDescription() {
        MBeanFeatureInfo inf = new MBeanFeatureInfo("name", "description");
        assertEquals("description", inf.getDescription());
    }

    /**
     * Test for the method getName()
     * 
     * @see javax.management.MBeanFeatureInfo#getName()
     */
    public final void testGetName() {
        MBeanFeatureInfo inf = new MBeanFeatureInfo("name", "description");
        assertEquals("name", inf.getName());
    }

    /**
     * Test for the method hashCode()
     * 
     * @see javax.management.MBeanFeatureInfo#hashCode();
     */
    public final void testHashCode() {
        MBeanFeatureInfo inf1 = new MBeanFeatureInfo("name", "description");
        MBeanFeatureInfo inf2 = new MBeanFeatureInfo("name", "description");

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
        System.exit(TestRunner.run(MBeanFeatureInfoTest.class, args));
    }
}
