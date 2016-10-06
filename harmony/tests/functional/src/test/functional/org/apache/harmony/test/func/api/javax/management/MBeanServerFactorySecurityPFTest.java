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

import java.io.File;
import java.lang.reflect.Method;
import java.security.Policy;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanServerFactory
 * 
 */
public class MBeanServerFactorySecurityPFTest extends Test {

    /**
     * Tested class name.
     */
    public final String testedClass = MBeanServerFactory.class.getName();

    /**
     * Directory containing policy files.
     */
    private String      policyDir;

    private void test(final String pref, final Method m, final Object[] params) {
        final String tp = pref + "_true";
        final String fp = pref + "_false";
        String[] list = new File(policyDir).list();
        for (int i = 0; i < list.length; i++) {
            if (!list[i].startsWith(tp) && !list[i].startsWith(fp)) {
                continue;
            }

            System.setProperty("java.security.policy", policyDir + "/"
                + list[i]);
            Policy.getPolicy().refresh();
            System.setSecurityManager(new SecurityManager());
            if (list[i].startsWith(tp)) {
                try {
                    m.invoke(null, params);
                } catch (Throwable ex) {
                    fail(list[i] + "\nMethod: " + m, ex);
                }
            } else if (list[i].startsWith(fp)) {
                try {
                    m.invoke(null, params);
                    fail("SecurityException wasn't thrown! Policy file: "
                        + list[i] + ", Method: " + m);
                } catch (Throwable ex) {
                }
            }
        }
    }

    /**
     * Test for the method createMBeanServer()
     * 
     * @throws Exception
     * @see javax.management.MBeanServerFactory#createMBeanServer()
     */
    public final void testCreateMBeanServer() throws Exception {
        test("cmbs", MBeanServerFactory.class.getMethod("createMBeanServer",
            null), null);
    }

    /**
     * Test for the method createMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#createMBeanServer(java.lang.String)
     */
    public final void testCreateMBeanServerString() throws Exception {
        test("cmbs", MBeanServerFactory.class.getMethod("createMBeanServer",
            new Class[] { String.class }), new Object[] { "domain" });
    }

    /**
     * Test for the method findMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#findMBeanServer(java.lang.String)
     */
    public final void testFindMBeanServer() throws Exception {
        test("fmbs", MBeanServerFactory.class.getMethod("findMBeanServer",
            new Class[] { String.class }), new Object[] { null });
    }

    /**
     * Test for the method
     * getClassLoaderRepository(javax.management.MBeanServer)
     * 
     * @see javax.management.MBeanServerFactory#getClassLoaderRepository(javax.management.MBeanServer)
     */
    public final void testGetClassLoaderRepository() throws Exception {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        test("gclr", MBeanServerFactory.class.getMethod(
            "getClassLoaderRepository", new Class[] { MBeanServer.class }),
            new Object[] { mbs });
    }

    /**
     * Test for the method newMBeanServer()
     * 
     * @see javax.management.MBeanServerFactory#newMBeanServer()
     */
    public final void testNewMBeanServer() throws Exception {
        test("nmbs",
            MBeanServerFactory.class.getMethod("newMBeanServer", null), null);
    }

    /**
     * Test for the method newMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#newMBeanServer(java.lang.String)
     */
    public final void testNewMBeanServerString() throws Exception {
        test("nmbs", MBeanServerFactory.class.getMethod("newMBeanServer",
            new Class[] { String.class }), new Object[] { "domain" });
    }

    /**
     * Test for the method releaseMBeanServer(javax.management.MBeanServer)
     * 
     * @see javax.management.MBeanServerFactory#releaseMBeanServer(javax.management.MBeanServer)
     */
    public final void testReleaseMBeanServer() throws Exception {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        test("rmbs", MBeanServerFactory.class.getMethod("releaseMBeanServer",
            new Class[] { MBeanServer.class }), new Object[] { mbs });
    }

    /**
     * Instantiate the security manager.
     */
    public final void init() throws Exception {
        policyDir = getArg("mbsfPolicyDir");
        if ((policyDir == null) || !(new File(policyDir).exists())) {
            fail("policyDir not found: " + policyDir);
            finish();
        }
    }

    /**
     * Uninstall the security manager.
     */
    public void tearDown() {
        System.setSecurityManager(null);
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner
            .run(MBeanServerFactorySecurityPFTest.class, args));
    }
}
