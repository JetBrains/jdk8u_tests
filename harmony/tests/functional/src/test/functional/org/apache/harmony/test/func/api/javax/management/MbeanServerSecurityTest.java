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

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanPermission;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanTrustPermission;
import javax.management.ObjectName;
import javax.management.loading.MLet;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the interface javax.management.MBeanServer
 * 
 */
public class MbeanServerSecurityTest extends Test {

    /**
     * Tested class name.
     */
    public final String       testedClass = MBeanServer.class.getName();

    /**
     * MBean server.
     */
    private MBeanServer       mbs;

    /**
     * MBean instance.
     */
    private Hello             mbean;

    /**
     * ObjectName for the Hello MBean.
     */
    private ObjectName        name;

    /**
     * ClassLoader name.
     */
    private ObjectName        loader;

    /**
     * ObjectName for the notification listener.
     */
    private ObjectName        lName;

    /**
     * Security manager.
     */
    private MySecurityManager mgr;

    /**
     * Test for the method addNotificationListener(javax.management.ObjectName,
     * javax.management.NotificationListener,
     * javax.management.NotificationFilter, java.lang.Object)
     * 
     * @throws Exception
     * @see javax.management.MBeanServer#addNotificationListener(javax.management.ObjectName,
     *      javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public final void testAddNotificationListenerObjectNameNotificationListenerNotificationFilterObject()
        throws Exception {
        mbs.addNotificationListener(name, mbean, null, null);
        System.setSecurityManager(mgr);
        try {
            mbs.addNotificationListener(name, mbean, null, null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        mgr.setPermission("addNotificationListener");
        mbs.addNotificationListener(name, mbean, null, null);
    }

    /**
     * Test for the method addNotificationListener(javax.management.ObjectName,
     * javax.management.ObjectName, javax.management.NotificationFilter,
     * java.lang.Object)
     * 
     * @see javax.management.MBeanServer#addNotificationListener(javax.management.ObjectName,
     *      javax.management.ObjectName, javax.management.NotificationFilter,
     *      java.lang.Object)
     */
    public final void testAddNotificationListenerObjectNameObjectNameNotificationFilterObject()
        throws Exception {
        mbs.addNotificationListener(name, lName, null, null);
        System.setSecurityManager(mgr);
        try {
            mbs.addNotificationListener(name, lName, null, null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        mgr.setPermission("addNotificationListener");
        mbs.addNotificationListener(name, lName, null, null);
    }

    /**
     * Test for the method getAttribute(javax.management.ObjectName,
     * java.lang.String)
     * 
     * @throws ReflectionException
     * @throws MBeanException
     * @throws InstanceNotFoundException
     * @throws AttributeNotFoundException
     * @see javax.management.MBeanServer#getAttribute(javax.management.ObjectName,
     *      java.lang.String)
     */
    public final void testGetAttribute() throws Exception {
        mbs.getAttribute(name, "Attribute1");
        System.setSecurityManager(mgr);
        try {
            mbs.getAttribute(name, "Attribute1");
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("getAttribute ");
        mbs.getAttribute(name, "Attribute1");
    }

    /**
     * Test for the method getAttributes(javax.management.ObjectName,
     * java.lang.String[])
     * 
     * @see javax.management.MBeanServer#getAttributes(javax.management.ObjectName,
     *      java.lang.String[])
     */
    public final void testGetAttributes() throws Exception {
        mbs.getAttributes(name, new String[] { "Attribute1", "Attribute2" });
        System.setSecurityManager(mgr);
        try {
            mbs
                .getAttributes(name,
                    new String[] { "Attribute1", "Attribute2" });
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("getAttribute ");
        mbs.getAttributes(name, new String[] { "Attribute1", "Attribute2" });
    }

    /**
     * Test for the method getClassLoader(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#getClassLoader(javax.management.ObjectName)
     */
    public final void testGetClassLoader() throws Exception {
        mbs.getClassLoader(null);
        System.setSecurityManager(mgr);
        try {
            mbs.getClassLoader(null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("getClassLoader");
        mbs.getClassLoader(null);
    }

    /**
     * Test for the method getClassLoaderFor(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#getClassLoaderFor(javax.management.ObjectName)
     */
    public final void testGetClassLoaderFor() throws Exception {
        mbs.getClassLoaderFor(name);
        System.setSecurityManager(mgr);
        try {
            mbs.getClassLoaderFor(name);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("getClassLoaderFor");
        mbs.getClassLoaderFor(name);
    }

    /**
     * Test for the method getClassLoaderRepository()
     * 
     * @see javax.management.MBeanServer#getClassLoaderRepository()
     */
    public final void testGetClassLoaderRepository() {
        mbs.getClassLoaderRepository();
        System.setSecurityManager(mgr);
        try {
            mbs.getClassLoaderRepository();
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("getClassLoaderRepository");
        mbs.getClassLoaderRepository();
    }

    /**
     * Test for the method getDomains()
     * 
     * @see javax.management.MBeanServer#getDomains()
     */
    public final void testGetDomains() {
        mbs.getDomains();
        System.setSecurityManager(mgr);
        try {
            mbs.getDomains();
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("getDomains");
        mbs.getDomains();
    }

    /**
     * Test for the method getMBeanInfo(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#getMBeanInfo(javax.management.ObjectName)
     */
    public final void testGetMBeanInfo() throws Exception {
        mbs.getMBeanInfo(name);
        System.setSecurityManager(mgr);
        try {
            mbs.getMBeanInfo(name);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("getMBeanInfo");
        mbs.getMBeanInfo(name);
    }

    /**
     * Test for the method getObjectInstance(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#getObjectInstance(javax.management.ObjectName)
     */
    public final void testGetObjectInstance() throws Exception {
        mbs.getObjectInstance(name);
        System.setSecurityManager(mgr);
        try {
            mbs.getObjectInstance(name);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("getObjectInstance");
        mbs.getObjectInstance(name);
    }

    /**
     * Test for the method instantiate(java.lang.String)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String)
     */
    public final void testInstantiateString() throws Exception {
        mbs.instantiate(Hello.class.getName());
        System.setSecurityManager(mgr);
        try {
            mbs.instantiate(Hello.class.getName());
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("instantiate");
        mbs.instantiate(Hello.class.getName());
    }

    /**
     * Test for the method instantiate(java.lang.String, java.lang.Object[],
     * java.lang.String[])
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      java.lang.Object[], java.lang.String[])
     */
    public final void testInstantiateStringObjectArrayStringArray()
        throws Exception {
        mbs
            .instantiate(Hello.class.getName(), (Object[]) null,
                (String[]) null);
        System.setSecurityManager(mgr);
        try {
            mbs.instantiate(Hello.class.getName(), (Object[]) null,
                (String[]) null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("instantiate");
        mbs
            .instantiate(Hello.class.getName(), (Object[]) null,
                (String[]) null);
    }

    /**
     * Test for the method instantiate(java.lang.String,
     * javax.management.ObjectName)
     * 
     * @throws Exception
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      javax.management.ObjectName)
     */
    public final void testInstantiateStringObjectName() {
        try {
            mbs.instantiate(Hello.class.getName(), loader);
            System.setSecurityManager(mgr);
            try {
                mbs.instantiate(Hello.class.getName(), loader);
                fail("SecurityException wasn't thrown!");
            } catch (SecurityException ex) {
            } catch (Throwable ex) {
                fail(ex);
            }
            mgr.setPermission("instantiate");
            mbs.instantiate(Hello.class.getName(), loader);
        } catch (Throwable ex) {
            fail(ex);
        }
    }

    /**
     * Test for the method instantiate(java.lang.String,
     * javax.management.ObjectName, java.lang.Object[], java.lang.String[])
     * 
     * @throws Exception
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      javax.management.ObjectName, java.lang.Object[], java.lang.String[])
     */
    public final void testInstantiateStringObjectNameObjectArrayStringArray()
        throws Exception {
        try {
            mbs.instantiate(Hello.class.getName(), loader, null, null);
            System.setSecurityManager(mgr);
            try {
                mbs.instantiate(Hello.class.getName(), loader, null, null);
                fail("SecurityException wasn't thrown!");
            } catch (SecurityException ex) {
            } catch (Throwable ex) {
                fail(ex);
            }
            mgr.setPermission("instantiate");
            mbs.instantiate(Hello.class.getName(), loader, null, null);
        } catch (Throwable ex) {
            fail(ex);
        }
    }

    /**
     * Test for the method invoke(javax.management.ObjectName, java.lang.String,
     * java.lang.Object[], java.lang.String[])
     * 
     * @throws Exception
     * @see javax.management.MBeanServer#invoke(javax.management.ObjectName,
     *      java.lang.String, java.lang.Object[], java.lang.String[])
     */
    public final void testInvoke() throws Exception {
        mbs.invoke(name, "sayHello", new Object[0], new String[0]);
        System.setSecurityManager(mgr);
        try {
            mbs.invoke(name, "sayHello", new Object[0], new String[0]);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("invoke");
        mbs.invoke(name, "sayHello", new Object[0], new String[0]);
    }

    /**
     * Test for the method isInstanceOf(javax.management.ObjectName,
     * java.lang.String)
     * 
     * @throws Exception
     * @see javax.management.MBeanServer#isInstanceOf(javax.management.ObjectName,
     *      java.lang.String)
     */
    public final void testIsInstanceOf() throws Exception {
        mbs.isInstanceOf(name, Hello.class.getName());
        System.setSecurityManager(mgr);
        try {
            mbs.isInstanceOf(name, Hello.class.getName());
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("isInstanceOf");
        mbs.isInstanceOf(name, Hello.class.getName());
    }

    /**
     * Test for the method queryMBeans(javax.management.ObjectName,
     * javax.management.QueryExp)
     * 
     * @see javax.management.MBeanServer#queryMBeans(javax.management.ObjectName,
     *      javax.management.QueryExp)
     */
    public final void testQueryMBeans() {
        mbs.queryMBeans(name, null);
        System.setSecurityManager(mgr);
        try {
            mbs.queryMBeans(name, null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("queryMBeans");
        mbs.queryMBeans(name, null);
    }

    /**
     * Test for the method queryNames(javax.management.ObjectName,
     * javax.management.QueryExp)
     * 
     * @see javax.management.MBeanServer#queryNames(javax.management.ObjectName,
     *      javax.management.QueryExp)
     */
    public final void testQueryNames() {
        mbs.queryNames(name, null);
        System.setSecurityManager(mgr);
        try {
            mbs.queryNames(name, null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("queryNames");
        mbs.queryNames(name, null);
    }

    /**
     * Test for the method registerMBean(java.lang.Object,
     * javax.management.ObjectName)
     * 
     * @throws NullPointerException
     * @throws MalformedObjectNameException
     * @throws NullPointerException
     * @throws MalformedObjectNameException
     * @see javax.management.MBeanServer#registerMBean(java.lang.Object,
     *      javax.management.ObjectName)
     */
    public final void testRegisterMBean() throws Exception {
        final ObjectName name = new ObjectName(
            "org.apache.harmony.test.func.api.javax.management:type=Temp");
        try {
            mbs.registerMBean(mbean, name);
            mbs.unregisterMBean(name);

            System.setSecurityManager(mgr);
            try {
                mbs.registerMBean(mbean, name);
                fail("SecurityException wasn't thrown!");
            } catch (SecurityException ex) {
            }

            mgr.setPermission("registerMBean");
            mbs.registerMBean(mbean, name);
        } catch (Throwable ex) {
            fail(ex);
        } finally {
            try {
                mbs.unregisterMBean(name);
            } catch (Throwable ex) {
            }
        }
    }

    /**
     * Test for the method
     * removeNotificationListener(javax.management.ObjectName,
     * javax.management.NotificationListener)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.NotificationListener)
     */
    public final void testRemoveNotificationListenerObjectNameNotificationListener()
        throws Exception {
        mbs.addNotificationListener(name, mbean, null, null);
        mbs.removeNotificationListener(name, mbean);

        mbs.addNotificationListener(name, mbean, null, null);
        System.setSecurityManager(mgr);
        try {
            mbs.removeNotificationListener(name, mbean);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr
            .setPermission("addNotificationListener, removeNotificationListener");
        mbs.addNotificationListener(name, mbean, null, null);
        mbs.removeNotificationListener(name, mbean);
    }

    /**
     * Test for the method
     * removeNotificationListener(javax.management.ObjectName,
     * javax.management.NotificationListener,
     * javax.management.NotificationFilter, java.lang.Object)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public final void testRemoveNotificationListenerObjectNameNotificationListenerNotificationFilterObject()
        throws Exception {
        mbs.addNotificationListener(name, mbean, null, null);
        mbs.removeNotificationListener(name, mbean, null, null);

        mbs.addNotificationListener(name, mbean, null, null);
        System.setSecurityManager(mgr);
        try {
            mbs.removeNotificationListener(name, mbean, null, null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr
            .setPermission("addNotificationListener, removeNotificationListener");
        mbs.addNotificationListener(name, mbean, null, null);
        mbs.removeNotificationListener(name, mbean, null, null);
    }

    /**
     * Test for the method
     * removeNotificationListener(javax.management.ObjectName,
     * javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.ObjectName)
     */
    public final void testRemoveNotificationListenerObjectNameObjectName()
        throws Exception {
        mbs.addNotificationListener(name, mbean, null, null);
        mbs.removeNotificationListener(name, lName);

        mbs.addNotificationListener(name, mbean, null, null);
        System.setSecurityManager(mgr);
        try {
            mbs.removeNotificationListener(name, lName);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr
            .setPermission("addNotificationListener, removeNotificationListener");
        mbs.addNotificationListener(name, mbean, null, null);
        mbs.removeNotificationListener(name, lName);
    }

    /**
     * Test for the method
     * removeNotificationListener(javax.management.ObjectName,
     * javax.management.ObjectName, javax.management.NotificationFilter,
     * java.lang.Object)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.ObjectName, javax.management.NotificationFilter,
     *      java.lang.Object)
     */
    public final void testRemoveNotificationListenerObjectNameObjectNameNotificationFilterObject()
        throws Exception {
        mbs.addNotificationListener(name, mbean, null, null);
        mbs.removeNotificationListener(name, lName, null, null);

        mbs.addNotificationListener(name, mbean, null, null);
        System.setSecurityManager(mgr);
        try {
            mbs.removeNotificationListener(name, lName, null, null);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr
            .setPermission("addNotificationListener, removeNotificationListener");
        mbs.addNotificationListener(name, mbean, null, null);
        mbs.removeNotificationListener(name, lName, null, null);
    }

    /**
     * Test for the method setAttribute(javax.management.ObjectName,
     * javax.management.Attribute)
     * 
     * @see javax.management.MBeanServer#setAttribute(javax.management.ObjectName,
     *      javax.management.Attribute)
     */
    public final void testSetAttribute() throws Exception {
        Attribute attr = new Attribute("Attribute1", "value");
        mbs.setAttribute(name, attr);
        System.setSecurityManager(mgr);
        try {
            mbs.setAttribute(name, attr);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }
        mgr.setPermission("setAttribute");
        mbs.setAttribute(name, attr);
    }

    /**
     * Test for the method setAttributes(javax.management.ObjectName,
     * javax.management.AttributeList)
     * 
     * @see javax.management.MBeanServer#setAttributes(javax.management.ObjectName,
     *      javax.management.AttributeList)
     */
    public final void testSetAttributes() throws Exception {
        Attribute attr = new Attribute("Attribute1", "value");
        AttributeList list = new AttributeList();
        list.add(attr);
        mbs.setAttributes(name, list);

        System.setSecurityManager(mgr);
        try {
            mbs.setAttributes(name, list);
            fail("SecurityException wasn't thrown!");
        } catch (SecurityException ex) {
        }

        mgr.setPermission("setAttribute");
        mbs.setAttributes(name, list);
    }

    /**
     * Test for the method unregisterMBean(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#unregisterMBean(javax.management.ObjectName)
     */
    public final void testUnregisterMBean() throws Exception {
        try {
            mbs.unregisterMBean(name);
            mbs.registerMBean(mbean, name);
            System.setSecurityManager(mgr);
            try {
                mbs.unregisterMBean(lName);
                fail("SecurityException wasn't thrown!");
            } catch (SecurityException ex) {
            }
            mgr.setPermission("unregisterMBean");
            mbs.unregisterMBean(name);
        } catch (Throwable ex) {
            fail(ex);
        } finally {
            try {
                mgr.setPermission("registerMBean");
                mbs.registerMBean(mbean, name);
                mbs.registerMBean(mbean, lName);
            } catch (Throwable ex) {
            }
        }
    }

    /**
     * Create MBean server, register Hello MBean.
     */
    public final void init() throws Exception {
        // Get the Platform MBean Server
        mbs = MBeanServerFactory.createMBeanServer();

        // Construct the ObjectName for the Hello MBean
        name = new ObjectName(
            "org.apache.harmony.test.func.api.javax.management:type=Hello");

        // Create the Hello MBean
        mbean = new Hello();

        // Register the Hello MBean
        mbs.registerMBean(mbean, name);

        // Construct the ObjectName for the notification listener
        lName = new ObjectName(
            "org.apache.harmony.test.func.api.javax.management:type=Listener");

        // Register the notification listener
        mbs.registerMBean(mbean, lName);

        // Register class loader.
        loader = new ObjectName("test.loader:type=Loader");
        mbs.registerMBean(new MLet(), loader);

        // Instantiate the security manager
        mgr = new MySecurityManager();
    }

    /**
     * Uninstall the security manager.
     */
    public void tearDown() {
        // mgr.p = null;
        mgr.col.clear();
        mgr.setPermissions();
        System.setSecurityManager(null);
    }

    /**
     * Deregister Hello MBean, shut down the server.
     */
    public final void release() {
        try {
            System.setSecurityManager(null);
            mbs.unregisterMBean(name);
            mbs.unregisterMBean(lName);
            MBeanServerFactory.releaseMBeanServer(mbs);
        } catch (Throwable e) {
        }
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
            col.add(new MBeanPermission("*#*[*:*]", act));
        }

        public void setPermissions() {
            col.add(new SecurityPermission("*"));
            col.add(new PropertyPermission("*", "read"));
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
        System.exit(TestRunner.run(MbeanServerSecurityTest.class, args));
    }
}
