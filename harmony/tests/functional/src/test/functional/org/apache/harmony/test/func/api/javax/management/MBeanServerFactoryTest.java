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

import java.util.ArrayList;

import javax.management.JMRuntimeException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.apache.harmony.test.func.api.javax.management.share.SimpleMBeanServerBuilder;
import org.apache.harmony.test.func.api.javax.management.share.SimpleMBeanServerImpl;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanServerFactory
 * 
 */
public class MBeanServerFactoryTest extends Test {

    /**
     * Test for the method createMBeanServer()
     * 
     * @see javax.management.MBeanServerFactory#createMBeanServer()
     */
    public final void testCreateMBeanServer() {
        MBeanServer srv = MBeanServerFactory.createMBeanServer();
        assertEquals("DefaultDomain", srv.getDefaultDomain());
        MBeanServerFactory.releaseMBeanServer(srv);

        // Check reaction on the javax.management.builder.initial system
        // property.
        System.setProperty("javax.management.builder.initial",
            SimpleMBeanServerBuilder.class.getName());
        srv = MBeanServerFactory.createMBeanServer();
        assertEquals(SimpleMBeanServerImpl.class, srv.getClass());
        assertEquals("DefaultDomain", srv.getDefaultDomain());
        MBeanServerFactory.releaseMBeanServer(srv);

        SimpleMBeanServerBuilder.returnNull = true;
        try {
            srv = MBeanServerFactory.createMBeanServer();
            fail("JMRuntimeException wasn't thrown.");
        } catch (JMRuntimeException ex) {
        }

        System.setProperty("javax.management.builder.initial", "builder");
        SimpleMBeanServerBuilder.returnNull = true;
        try {
            srv = MBeanServerFactory.createMBeanServer();
            fail("JMRuntimeException wasn't thrown.");
        } catch (JMRuntimeException ex) {
        }

        System.setProperty("javax.management.builder.initial", String.class
            .getName());
        try {
            srv = MBeanServerFactory.createMBeanServer();
            fail("ClassCastException wasn't thrown.");
        } catch (ClassCastException ex) {
        }
    }

    /**
     * Test for the method createMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#createMBeanServer(java.lang.String)
     */
    public final void testCreateMBeanServerString() {
        MBeanServer srv = MBeanServerFactory
            .createMBeanServer("Default Domain");
        assertEquals("Default Domain", srv.getDefaultDomain());
        MBeanServerFactory.releaseMBeanServer(srv);

        // Check reaction on the javax.management.builder.initial system
        // property.
        System.setProperty("javax.management.builder.initial",
            SimpleMBeanServerBuilder.class.getName());
        srv = MBeanServerFactory.createMBeanServer("Default Domain");
        assertEquals(SimpleMBeanServerImpl.class, srv.getClass());
        assertEquals("Default Domain", srv.getDefaultDomain());
        MBeanServerFactory.releaseMBeanServer(srv);

        SimpleMBeanServerBuilder.returnNull = true;
        try {
            srv = MBeanServerFactory.createMBeanServer("Default Domain");
            fail("JMRuntimeException wasn't thrown.");
        } catch (JMRuntimeException ex) {
        }

        System.setProperty("javax.management.builder.initial", "builder");
        SimpleMBeanServerBuilder.returnNull = false;
        try {
            srv = MBeanServerFactory.createMBeanServer("Default Domain");
            fail("JMRuntimeException wasn't thrown.");
        } catch (JMRuntimeException ex) {
        }

        System.setProperty("javax.management.builder.initial", String.class
            .getName());
        try {
            srv = MBeanServerFactory.createMBeanServer("Default Domain");
            fail("ClassCastException wasn't thrown.");
        } catch (ClassCastException ex) {
        }
    }

    /**
     * Test for the method findMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#findMBeanServer(java.lang.String)
     */
    public final void testFindMBeanServer() {
        MBeanServer srv = MBeanServerFactory.createMBeanServer();
        MBeanServer srv1 = MBeanServerFactory.createMBeanServer("Server1");
        MBeanServer srv2 = MBeanServerFactory.createMBeanServer("Server2");

        ArrayList l = MBeanServerFactory.findMBeanServer(null);
        assertTrue(l.contains(srv));
        assertTrue(l.contains(srv1));
        assertTrue(l.contains(srv2));
        try {
            l = MBeanServerFactory.findMBeanServer((String) srv2.getAttribute(
                new ObjectName("JMImplementation:type=MBeanServerDelegate"),
                "MBeanServerId"));
            if (l.size() == 1) {
                assertEquals(srv2, l.get(0));
            } else {
                fail("The ArrayList object should contain the only one "
                    + "MBeanServer object - srv2. Actual: " + l);
            }
        } catch (Exception ex) {
            fail(ex);
        }
        MBeanServerFactory.releaseMBeanServer(srv);
        MBeanServerFactory.releaseMBeanServer(srv1);
        MBeanServerFactory.releaseMBeanServer(srv2);
    }

    /**
     * Test for the method
     * getClassLoaderRepository(javax.management.MBeanServer)
     * 
     * @see javax.management.MBeanServerFactory#getClassLoaderRepository(javax.management.MBeanServer)
     */
    public final void testGetClassLoaderRepository() {
        MBeanServer srv = MBeanServerFactory.createMBeanServer("Server1");
        assertEquals(srv.getClassLoaderRepository(), MBeanServerFactory
            .getClassLoaderRepository(srv));

        try {
            MBeanServerFactory.getClassLoaderRepository(null);
            fail("NullPointerException wasn't thrown.");
        } catch (NullPointerException ex) {
        }
        MBeanServerFactory.releaseMBeanServer(srv);
    }

    /**
     * Test for the method newMBeanServer()
     * 
     * @see javax.management.MBeanServerFactory#newMBeanServer()
     */
    public final void testNewMBeanServer() {
        MBeanServer srv = MBeanServerFactory.newMBeanServer();
        assertEquals("DefaultDomain", srv.getDefaultDomain());
        try {
            ArrayList l = MBeanServerFactory.findMBeanServer((String) srv
                .getAttribute(new ObjectName(
                    "JMImplementation:type=MBeanServerDelegate"),
                    "MBeanServerId"));
            if (l.size() != 0) {
                fail("The ArrayList object should be empty. Actual: " + l);
            }
        } catch (Exception ex) {
            fail(ex);
        }

        // Check reaction on the javax.management.builder.initial system
        // property.
        System.setProperty("javax.management.builder.initial",
            SimpleMBeanServerBuilder.class.getName());
        srv = MBeanServerFactory.newMBeanServer();
        assertEquals(SimpleMBeanServerImpl.class, srv.getClass());
        assertEquals("DefaultDomain", srv.getDefaultDomain());

        SimpleMBeanServerBuilder.returnNull = true;
        try {
            srv = MBeanServerFactory.newMBeanServer();
            fail("JMRuntimeException wasn't thrown.");
        } catch (JMRuntimeException ex) {
        }

        System.setProperty("javax.management.builder.initial", "builder");
        SimpleMBeanServerBuilder.returnNull = true;
        try {
            srv = MBeanServerFactory.newMBeanServer();
            fail("JMRuntimeException wasn't thrown.");
        } catch (JMRuntimeException ex) {
        }

        System.setProperty("javax.management.builder.initial", String.class
            .getName());
        try {
            srv = MBeanServerFactory.newMBeanServer();
            fail("ClassCastException wasn't thrown.");
        } catch (ClassCastException ex) {
        }
    }

    /**
     * Test for the method newMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#newMBeanServer(java.lang.String)
     */
    public final void testNewMBeanServerString() {
        MBeanServer srv = MBeanServerFactory.newMBeanServer("Default Domain");
        assertEquals("Default Domain", srv.getDefaultDomain());
        try {
            ArrayList l = MBeanServerFactory.findMBeanServer((String) srv
                .getAttribute(new ObjectName(
                    "JMImplementation:type=MBeanServerDelegate"),
                    "MBeanServerId"));
            if (l.size() != 0) {
                fail("The ArrayList object should be empty. Actual: " + l);
            }
        } catch (Exception ex) {
            fail(ex);
        }

        // Check reaction on the javax.management.builder.initial system
        // property.
        System.setProperty("javax.management.builder.initial",
            SimpleMBeanServerBuilder.class.getName());
        srv = MBeanServerFactory.newMBeanServer("Default Domain");
        assertEquals(SimpleMBeanServerImpl.class, srv.getClass());
        assertEquals("Default Domain", srv.getDefaultDomain());

        SimpleMBeanServerBuilder.returnNull = true;
        try {
            srv = MBeanServerFactory.newMBeanServer("Default Domain");
            fail("JMRuntimeException wasn't thrown.");
        } catch (JMRuntimeException ex) {
        }

        System.setProperty("javax.management.builder.initial", "builder");
        SimpleMBeanServerBuilder.returnNull = true;
        try {
            srv = MBeanServerFactory.newMBeanServer("Default Domain");
            fail("JMRuntimeException wasn't thrown.");
        } catch (JMRuntimeException ex) {
        }

        System.setProperty("javax.management.builder.initial", String.class
            .getName());
        try {
            srv = MBeanServerFactory.newMBeanServer("Default Domain");
            fail("ClassCastException wasn't thrown.");
        } catch (ClassCastException ex) {
        }
    }

    /**
     * Test for the method releaseMBeanServer(javax.management.MBeanServer)
     * 
     * @see javax.management.MBeanServerFactory#releaseMBeanServer(javax.management.MBeanServer)
     */
    public final void testReleaseMBeanServer() {
        MBeanServer srv = MBeanServerFactory.createMBeanServer();
        MBeanServerFactory.releaseMBeanServer(srv);
        try {
            ArrayList l = MBeanServerFactory.findMBeanServer((String) srv
                .getAttribute(new ObjectName(
                    "JMImplementation:type=MBeanServerDelegate"),
                    "MBeanServerId"));
            if (l.size() != 0) {
                fail("The ArrayList object should be empty. Actual: " + l);
            }
        } catch (Exception ex) {
            fail(ex);
        }

        try {
            MBeanServerFactory.releaseMBeanServer(srv);
            fail("IllegalArgumentException wasn't thrown.");
        } catch (IllegalArgumentException ex) {
        }

        try {
            MBeanServerFactory.releaseMBeanServer(MBeanServerFactory
                .newMBeanServer());
            fail("IllegalArgumentException wasn't thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Remove the system property javax.management.builder.initial.
     */
    public void tearDown() {
        SimpleMBeanServerBuilder.returnNull = false;
        System.getProperties().remove("javax.management.builder.initial");
    }

    /**
     * Release all MBean servers.
     */
    public void release() {
        ArrayList list = MBeanServerFactory.findMBeanServer(null);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            MBeanServerFactory.releaseMBeanServer((MBeanServer) list.get(i));
        }
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(MBeanServerFactoryTest.class, args));
    }
}
