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
package org.apache.harmony.test.func.api.javax.management.share;

import java.io.ObjectInputStream;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.loading.ClassLoaderRepository;

/**
 */
public class SimpleMBeanServerImpl implements MBeanServer {

    public String              defaultDomain;

    public MBeanServer         outer;

    public MBeanServerDelegate delegate;

    public SimpleMBeanServerImpl(String defaultDomain, MBeanServer outer,
        MBeanServerDelegate delegate) {
        this.defaultDomain = defaultDomain != null ? defaultDomain
            : "DefaultDomain";
        this.outer = outer;
        this.delegate = delegate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean(java.lang.String,
     *      javax.management.ObjectName)
     */
    public ObjectInstance createMBean(String arg0, ObjectName arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean(java.lang.String,
     *      javax.management.ObjectName, javax.management.ObjectName)
     */
    public ObjectInstance createMBean(String arg0, ObjectName arg1,
        ObjectName arg2) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean(java.lang.String,
     *      javax.management.ObjectName, java.lang.Object[], java.lang.String[])
     */
    public ObjectInstance createMBean(String arg0, ObjectName arg1,
        Object[] arg2, String[] arg3) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean(java.lang.String,
     *      javax.management.ObjectName, javax.management.ObjectName,
     *      java.lang.Object[], java.lang.String[])
     */
    public ObjectInstance createMBean(String arg0, ObjectName arg1,
        ObjectName arg2, Object[] arg3, String[] arg4) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#registerMBean(java.lang.Object,
     *      javax.management.ObjectName)
     */
    public ObjectInstance registerMBean(Object arg0, ObjectName arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#unregisterMBean(javax.management.ObjectName)
     */
    public void unregisterMBean(ObjectName arg0) {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getObjectInstance(javax.management.ObjectName)
     */
    public ObjectInstance getObjectInstance(ObjectName arg0) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#queryMBeans(javax.management.ObjectName,
     *      javax.management.QueryExp)
     */
    public Set queryMBeans(ObjectName arg0, QueryExp arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#queryNames(javax.management.ObjectName,
     *      javax.management.QueryExp)
     */
    public Set queryNames(ObjectName arg0, QueryExp arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#isRegistered(javax.management.ObjectName)
     */
    public boolean isRegistered(ObjectName arg0) {
        // TODO Implement this method
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getMBeanCount()
     */
    public Integer getMBeanCount() {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getAttribute(javax.management.ObjectName,
     *      java.lang.String)
     */
    public Object getAttribute(ObjectName arg0, String arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getAttributes(javax.management.ObjectName,
     *      java.lang.String[])
     */
    public AttributeList getAttributes(ObjectName arg0, String[] arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#setAttribute(javax.management.ObjectName,
     *      javax.management.Attribute)
     */
    public void setAttribute(ObjectName arg0, Attribute arg1) {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#setAttributes(javax.management.ObjectName,
     *      javax.management.AttributeList)
     */
    public AttributeList setAttributes(ObjectName arg0, AttributeList arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#invoke(javax.management.ObjectName,
     *      java.lang.String, java.lang.Object[], java.lang.String[])
     */
    public Object invoke(ObjectName arg0, String arg1, Object[] arg2,
        String[] arg3) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getDefaultDomain()
     */
    public String getDefaultDomain() {
        return defaultDomain;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getDomains()
     */
    public String[] getDomains() {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#addNotificationListener(javax.management.ObjectName,
     *      javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public void addNotificationListener(ObjectName arg0,
        NotificationListener arg1, NotificationFilter arg2, Object arg3) {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#addNotificationListener(javax.management.ObjectName,
     *      javax.management.ObjectName, javax.management.NotificationFilter,
     *      java.lang.Object)
     */
    public void addNotificationListener(ObjectName arg0, ObjectName arg1,
        NotificationFilter arg2, Object arg3) {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.ObjectName)
     */
    public void removeNotificationListener(ObjectName arg0, ObjectName arg1) {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.ObjectName, javax.management.NotificationFilter,
     *      java.lang.Object)
     */
    public void removeNotificationListener(ObjectName arg0, ObjectName arg1,
        NotificationFilter arg2, Object arg3) {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.NotificationListener)
     */
    public void removeNotificationListener(ObjectName arg0,
        NotificationListener arg1) {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public void removeNotificationListener(ObjectName arg0,
        NotificationListener arg1, NotificationFilter arg2, Object arg3) {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getMBeanInfo(javax.management.ObjectName)
     */
    public MBeanInfo getMBeanInfo(ObjectName arg0) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#isInstanceOf(javax.management.ObjectName,
     *      java.lang.String)
     */
    public boolean isInstanceOf(ObjectName arg0, String arg1) {
        // TODO Implement this method
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String)
     */
    public Object instantiate(String arg0) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      javax.management.ObjectName)
     */
    public Object instantiate(String arg0, ObjectName arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      java.lang.Object[], java.lang.String[])
     */
    public Object instantiate(String arg0, Object[] arg1, String[] arg2) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      javax.management.ObjectName, java.lang.Object[], java.lang.String[])
     */
    public Object instantiate(String arg0, ObjectName arg1, Object[] arg2,
        String[] arg3) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize(javax.management.ObjectName,
     *      byte[])
     */
    public ObjectInputStream deserialize(ObjectName arg0, byte[] arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize(java.lang.String, byte[])
     */
    public ObjectInputStream deserialize(String arg0, byte[] arg1) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize(java.lang.String,
     *      javax.management.ObjectName, byte[])
     */
    public ObjectInputStream deserialize(String arg0, ObjectName arg1,
        byte[] arg2) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoaderFor(javax.management.ObjectName)
     */
    public ClassLoader getClassLoaderFor(ObjectName arg0) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoader(javax.management.ObjectName)
     */
    public ClassLoader getClassLoader(ObjectName arg0) {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoaderRepository()
     */
    public ClassLoaderRepository getClassLoaderRepository() {
        // TODO Implement this method
        return null;
    }

}
