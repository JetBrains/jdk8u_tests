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
import java.security.Permission;
import java.security.Policy;
import java.security.SecurityPermission;
import java.util.PropertyPermission;
import java.util.Vector;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanPermission;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanTrustPermission;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.loading.MLet;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the interface javax.management.MBeanServer
 * 
 */
public class MbeanServerSecurityPFTest extends Test {

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
     * Directory containing policy files.
     */
    private String            policyDir;

    private void test(final Method preCondition, final Method postCondition,
        final String pref, final Method m, final Object[] params) {
        final String tp = pref + "_true";
        final String fp = pref + "_false";
        String[] list = new File(policyDir).list();
        for (int i = 0; i < list.length; i++) {
            if (!list[i].startsWith(tp) && !list[i].startsWith(fp)) {
                continue;
            }

            if (preCondition != null) {
                try {
                    preCondition.invoke(this, null);
                } catch (Exception ex) {
                    fail("preCondition: " + preCondition, ex);
                }
            }

            System.setProperty("java.security.policy", policyDir + "/"
                + list[i]);
            Policy.getPolicy().refresh();
            System.setSecurityManager(new SecurityManager());
            if (list[i].startsWith(tp)) {
                try {
                    m.invoke(mbs, params);
                } catch (Throwable ex) {
                    fail(list[i] + "\nMethod: " + m, ex);
                }
            } else if (list[i].startsWith(fp)) {
                try {
                    m.invoke(mbs, params);
                    fail("SecurityException wasn't thrown!\nPolicy file: "
                        + list[i] + "\nMethod: " + m);
                } catch (Throwable ex) {
                }
            }

            System.setSecurityManager(null);

            if (postCondition != null) {
                try {
                    postCondition.invoke(this, null);
                } catch (Exception ex) {
                    fail("postCondition: " + postCondition, ex);
                }
            }
        }
    }

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
        test(null, null, "anl", mbs.getClass().getMethod(
            "addNotificationListener",
            new Class[] { ObjectName.class, NotificationListener.class,
                NotificationFilter.class, Object.class }), new Object[] { name,
            mbean, null, null });
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
        test(null, null, "anl", mbs.getClass().getMethod(
            "addNotificationListener",
            new Class[] { ObjectName.class, ObjectName.class,
                NotificationFilter.class, Object.class }), new Object[] { name,
            lName, null, null });
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
        test(null, null, "ga", mbs.getClass().getMethod("getAttribute",
            new Class[] { ObjectName.class, String.class }), new Object[] {
            name, "Attribute1" });
    }

    /**
     * Test for the method getAttributes(javax.management.ObjectName,
     * java.lang.String[])
     * 
     * @see javax.management.MBeanServer#getAttributes(javax.management.ObjectName,
     *      java.lang.String[])
     */
    public final void testGetAttributes() throws Exception {
        test(null, null, "ga", mbs.getClass().getMethod("getAttributes",
            new Class[] { ObjectName.class, String[].class }), new Object[] {
            name, new String[] { "Attribute1", "Attribute2" } });
    }

    /**
     * Test for the method getClassLoader(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#getClassLoader(javax.management.ObjectName)
     */
    public final void testGetClassLoader() throws Exception {
        test(null, null, "gcl", mbs.getClass().getMethod("getClassLoader",
            new Class[] { ObjectName.class }), new Object[] { null });
    }

    /**
     * Test for the method getClassLoaderFor(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#getClassLoaderFor(javax.management.ObjectName)
     */
    public final void testGetClassLoaderFor() throws Exception {
        test(null, null, "gclf", mbs.getClass().getMethod("getClassLoaderFor",
            new Class[] { ObjectName.class }), new Object[] { name });
    }

    /**
     * Test for the method getClassLoaderRepository()
     * 
     * @see javax.management.MBeanServer#getClassLoaderRepository()
     */
    public final void testGetClassLoaderRepository() throws Exception {
        test(null, null, "gclr", mbs.getClass().getMethod(
            "getClassLoaderRepository", null), null);
    }

    /**
     * Test for the method getDomains()
     * 
     * @see javax.management.MBeanServer#getDomains()
     */
    public final void testGetDomains() throws Exception {
        test(null, null, "gd", mbs.getClass().getMethod("getDomains", null),
            null);
    }

    /**
     * Test for the method getMBeanInfo(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#getMBeanInfo(javax.management.ObjectName)
     */
    public final void testGetMBeanInfo() throws Exception {
        test(null, null, "gmbi", mbs.getClass().getMethod("getMBeanInfo",
            new Class[] { ObjectName.class }), new Object[] { name });
    }

    /**
     * Test for the method getObjectInstance(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#getObjectInstance(javax.management.ObjectName)
     */
    public final void testGetObjectInstance() throws Exception {
        test(null, null, "goi", mbs.getClass().getMethod("getObjectInstance",
            new Class[] { ObjectName.class }), new Object[] { name });
    }

    /**
     * Test for the method instantiate(java.lang.String)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String)
     */
    public final void testInstantiateString() throws Exception {
        test(null, null, "i", mbs.getClass().getMethod("instantiate",
            new Class[] { String.class }),
            new Object[] { Hello.class.getName() });
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
        test(null, null, "i", mbs.getClass().getMethod("instantiate",
            new Class[] { String.class, Object[].class, String[].class }),
            new Object[] { Hello.class.getName(), null, null });
    }

    /**
     * Test for the method instantiate(java.lang.String,
     * javax.management.ObjectName)
     * 
     * @throws Exception
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      javax.management.ObjectName)
     */
    public final void testInstantiateStringObjectName() throws Exception {
        test(null, null, "i", mbs.getClass().getMethod("instantiate",
            new Class[] { String.class, ObjectName.class }), new Object[] {
            Hello.class.getName(), loader });
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
        test(null, null, "i", mbs.getClass().getMethod(
            "instantiate",
            new Class[] { String.class, ObjectName.class, Object[].class,
                String[].class }), new Object[] { Hello.class.getName(),
            loader, null, null });
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
        test(null, null, "invoke", mbs.getClass().getMethod(
            "invoke",
            new Class[] { ObjectName.class, String.class, Object[].class,
                String[].class }), new Object[] { name, "sayHello",
            new Object[0], new String[0] });
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
        test(null, null, "iio", mbs.getClass().getMethod("isInstanceOf",
            new Class[] { ObjectName.class, String.class }), new Object[] {
            name, Hello.class.getName() });
    }

    /**
     * Test for the method queryMBeans(javax.management.ObjectName,
     * javax.management.QueryExp)
     * 
     * @see javax.management.MBeanServer#queryMBeans(javax.management.ObjectName,
     *      javax.management.QueryExp)
     */
    public final void testQueryMBeans() throws Exception {
        test(null, null, "qmb", mbs.getClass().getMethod("queryMBeans",
            new Class[] { ObjectName.class, QueryExp.class }), new Object[] {
            name, null });
    }

    /**
     * Test for the method queryNames(javax.management.ObjectName,
     * javax.management.QueryExp)
     * 
     * @see javax.management.MBeanServer#queryNames(javax.management.ObjectName,
     *      javax.management.QueryExp)
     */
    public final void testQueryNames() throws Exception {
        test(null, null, "qn", mbs.getClass().getMethod("queryNames",
            new Class[] { ObjectName.class, QueryExp.class }), new Object[] {
            name, null });
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
        test(null, this.getClass().getMethod("unregMBCondition", null), "rmb",
            mbs.getClass().getMethod("registerMBean",
                new Class[] { Object.class, ObjectName.class }), new Object[] {
                mbean, name });
    }

    public void unregMBCondition() {
        try {
            final ObjectName name = new ObjectName(
                "org.apache.harmony.test.func.api.javax.management:type=Temp");
            mbs.unregisterMBean(name);
        } catch (Exception ex) {
        }
    }

    public void regMBCondition() {
        try {
            final ObjectName name = new ObjectName(
                "org.apache.harmony.test.func.api.javax.management:type=Temp");
            mbs.registerMBean(mbean, name);
        } catch (Exception ex) {
        }
    }

    public void addNLCondition() throws InstanceNotFoundException {
        mbs.addNotificationListener(name, mbean, null, null);
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
        test(this.getClass().getMethod("addNLCondition", null), null, "rnl",
            mbs.getClass().getMethod("removeNotificationListener",
                new Class[] { ObjectName.class, NotificationListener.class }),
            new Object[] { name, mbean });
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
        test(this.getClass().getMethod("addNLCondition", null), null, "rnl",
            mbs.getClass().getMethod(
                "removeNotificationListener",
                new Class[] { ObjectName.class, NotificationListener.class,
                    NotificationFilter.class, Object.class }), new Object[] {
                name, mbean, null, null });
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
        test(this.getClass().getMethod("addNLCondition", null), null, "rnl",
            mbs.getClass().getMethod("removeNotificationListener",
                new Class[] { ObjectName.class, ObjectName.class }),
            new Object[] { name, lName });
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
        test(this.getClass().getMethod("addNLCondition", null), null, "rnl",
            mbs.getClass().getMethod(
                "removeNotificationListener",
                new Class[] { ObjectName.class, ObjectName.class,
                    NotificationFilter.class, Object.class }), new Object[] {
                name, lName, null, null });
    }

    /**
     * Test for the method setAttribute(javax.management.ObjectName,
     * javax.management.Attribute)
     * 
     * @see javax.management.MBeanServer#setAttribute(javax.management.ObjectName,
     *      javax.management.Attribute)
     */
    public final void testSetAttribute() throws Exception {
        final Attribute attr = new Attribute("Attribute1", "value");
        test(null, null, "sa", mbs.getClass().getMethod("setAttribute",
            new Class[] { ObjectName.class, Attribute.class }), new Object[] {
            name, attr });
    }

    /**
     * Test for the method setAttributes(javax.management.ObjectName,
     * javax.management.AttributeList)
     * 
     * @see javax.management.MBeanServer#setAttributes(javax.management.ObjectName,
     *      javax.management.AttributeList)
     */
    public final void testSetAttributes() throws Exception {
        final Attribute attr = new Attribute("Attribute1", "value");
        final AttributeList list = new AttributeList();
        list.add(attr);
        test(null, null, "sa", mbs.getClass().getMethod("setAttributes",
            new Class[] { ObjectName.class, AttributeList.class }),
            new Object[] { name, list });
    }

    /**
     * Test for the method unregisterMBean(javax.management.ObjectName)
     * 
     * @see javax.management.MBeanServer#unregisterMBean(javax.management.ObjectName)
     */
    public final void testUnregisterMBean() throws Exception {
        final ObjectName name = new ObjectName(
            "org.apache.harmony.test.func.api.javax.management:type=Temp");
        test(this.getClass().getMethod("regMBCondition", null), null, "umb",
            mbs.getClass().getMethod("unregisterMBean",
                new Class[] { ObjectName.class }), new Object[] { name });
    }

    /**
     * Create MBean server, register Hello MBean.
     */
    public final void init() throws Exception {
        policyDir = getArg("mbsPolicyDir");
        if ((policyDir == null) || !(new File(policyDir).exists())) {
            fail("policyDir not found: " + policyDir);
            finish();
        }

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
        System.exit(TestRunner.run(MbeanServerSecurityPFTest.class, args));
    }
}
