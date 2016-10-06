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

import java.security.Permission;
import java.security.SecurityPermission;
import java.util.PropertyPermission;
import java.util.Vector;
import java.util.logging.LoggingPermission;

import javax.management.MBeanPermission;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerPermission;
import javax.management.MBeanTrustPermission;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.MBeanServerFactory
 * 
 */
public class MBeanServerFactorySecurityTest extends Test {

    /**
     * Tested class name.
     */
    public final String       testedClass = MBeanServerFactory.class.getName();

    /**
     * Security manager.
     */
    private MySecurityManager mgr;

    /**
     * Test for the method createMBeanServer()
     * 
     * @see javax.management.MBeanServerFactory#createMBeanServer()
     */
    public final void testCreateMBeanServer() {
        MBeanServerFactory.createMBeanServer();
        System.setSecurityManager(mgr);

        try {
            MBeanServerFactory.createMBeanServer();
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr.setPermission("createMBeanServer");
        MBeanServerFactory.createMBeanServer();
    }

    /**
     * Test for the method createMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#createMBeanServer(java.lang.String)
     */
    public final void testCreateMBeanServerString() {
        MBeanServerFactory.createMBeanServer("domain");
        System.setSecurityManager(mgr);
        try {
            MBeanServerFactory.createMBeanServer("domain");
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr.setPermission("createMBeanServer");
        MBeanServerFactory.createMBeanServer("domain");
    }

    /**
     * Test for the method findMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#findMBeanServer(java.lang.String)
     */
    public final void testFindMBeanServer() {
        MBeanServerFactory.findMBeanServer(null);
        System.setSecurityManager(mgr);
        try {
            MBeanServerFactory.findMBeanServer(null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr.setPermission("findMBeanServer");
        MBeanServerFactory.findMBeanServer(null);
    }

    /**
     * Test for the method
     * getClassLoaderRepository(javax.management.MBeanServer)
     * 
     * @see javax.management.MBeanServerFactory#getClassLoaderRepository(javax.management.MBeanServer)
     */
    public final void testGetClassLoaderRepository() {
        MBeanServer srv = MBeanServerFactory.createMBeanServer();
        MBeanServerFactory.getClassLoaderRepository(srv);
        System.setSecurityManager(mgr);
        try {
            MBeanServerFactory.getClassLoaderRepository(srv);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr.col
            .add(new MBeanPermission("*#*[*:*]", "getClassLoaderRepository"));
        MBeanServerFactory.getClassLoaderRepository(srv);
    }

    /**
     * Test for the method newMBeanServer()
     * 
     * @see javax.management.MBeanServerFactory#newMBeanServer()
     */
    public final void testNewMBeanServer() {
        MBeanServerFactory.newMBeanServer();
        System.setSecurityManager(mgr);
        try {
            MBeanServerFactory.newMBeanServer();
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr.setPermission("newMBeanServer");
        MBeanServerFactory.newMBeanServer();

        mgr.col.clear();
        mgr.setPermissions();
        mgr.setPermission("createMBeanServer");
        MBeanServerFactory.newMBeanServer();
    }

    /**
     * Test for the method newMBeanServer(java.lang.String)
     * 
     * @see javax.management.MBeanServerFactory#newMBeanServer(java.lang.String)
     */
    public final void testNewMBeanServerString() {
        MBeanServerFactory.newMBeanServer("domain");
        System.setSecurityManager(mgr);
        try {
            MBeanServerFactory.newMBeanServer("domain");
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr.setPermission("newMBeanServer");
        MBeanServerFactory.newMBeanServer("domain");
        
        mgr.col.clear();
        mgr.setPermissions();
        mgr.setPermission("createMBeanServer");
        MBeanServerFactory.newMBeanServer("domain");
    }

    /**
     * Test for the method releaseMBeanServer(javax.management.MBeanServer)
     * 
     * @see javax.management.MBeanServerFactory#releaseMBeanServer(javax.management.MBeanServer)
     */
    public final void testReleaseMBeanServer() {
        MBeanServer srv = MBeanServerFactory.createMBeanServer();
        MBeanServerFactory.releaseMBeanServer(srv);
        srv = MBeanServerFactory.createMBeanServer();
        System.setSecurityManager(mgr);
        try {
            MBeanServerFactory.releaseMBeanServer(srv);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr.setPermission("releaseMBeanServer");
        MBeanServerFactory.releaseMBeanServer(srv);
        mgr.setPermission("createMBeanServer");
        srv = MBeanServerFactory.createMBeanServer();
    }

    /**
     * Instantiate the security manager.
     */
    public final void init() throws Exception {
        mgr = new MySecurityManager();
    }

    /**
     * Uninstall the security manager.
     */
    public void tearDown() {
        mgr.col.clear();
        mgr.setPermissions();
        System.setSecurityManager(null);
    }

    /**
     * Simple security manager.
     * 
     */
    private class MySecurityManager extends SecurityManager {

        public Vector col = new Vector();

        public MySecurityManager() {
            super();
            setPermissions();
        }

        public void checkPermission(Permission perm) {
            for (int i = 0; i < col.size(); i++) {
                if (((Permission) col.get(i)).implies(perm)) {
                    return;
                }
            }

            throw new SecurityException(perm + " is not permitted");
        }

        public void checkPermission(Permission perm, Object context) {
            checkPermission(perm);
        }

        public void setPermission(String act) {
            col.add(new MBeanServerPermission(act));
        }

        public void setPermissions() {
            col.add(new MBeanPermission("*#*[*:*]", "registerMBean"));
            col.add(new LoggingPermission("control", null));
            col.add(new SecurityPermission("*"));
            col.add(new PropertyPermission("*", "read,write"));
            col.add(new RuntimePermission("*"));
            col.add(new MBeanTrustPermission("*"));
        }
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(MBeanServerFactorySecurityTest.class, args));
    }
}
