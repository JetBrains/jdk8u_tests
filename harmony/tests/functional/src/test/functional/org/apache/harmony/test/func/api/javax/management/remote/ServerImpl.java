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
package org.apache.harmony.test.func.api.javax.management.remote;

import java.io.ObjectInputStream;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;
import javax.management.remote.MBeanServerForwarder;
import org.apache.harmony.share.Test;

/**
 */
public class ServerImpl implements MBeanServerForwarder {

    MBeanServer mbeanServer;

    public static int checkCount = 0;

    public ServerImpl() {
        checkCount++;

    }

    public MBeanServer getMBeanServer() {
        checkCount++;
        return mbeanServer;
    }

    public void setMBeanServer(MBeanServer mbeanServer) {
        checkCount++;

        this.mbeanServer = mbeanServer;
    }

    public Object getAttribute(ObjectName objectName, String attr)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException {
        checkCount++;
        Test.log
                .info("getAttribute(ObjectName objectName, String attr) method forward to the appropriate object");
        return getMBeanServer().getAttribute(objectName, attr);
    }

    public AttributeList getAttributes(ObjectName objectName, String[] attrs)
            throws InstanceNotFoundException, ReflectionException {
        checkCount++;
        Test.log
                .info("getAttributes(ObjectName name, String[] attrs) method forward to the appropriate object");
        return getMBeanServer().getAttributes(objectName, attrs);
    }

    public ClassLoader getClassLoader(ObjectName classLoader)
            throws InstanceNotFoundException {
        Test.log
                .info("getClassLoader(ObjectName classLoader) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().getClassLoader(classLoader);
    }

    public ClassLoader getClassLoaderFor(ObjectName mbeanObjectName)
            throws InstanceNotFoundException {
        Test.log
                .info("getClassLoaderFor(ObjectName mbeanObjectName) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().getClassLoaderFor(mbeanObjectName);
    }

    public ClassLoaderRepository getClassLoaderRepository() {
        Test.log
                .info("getClassLoaderRepository() method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().getClassLoaderRepository();
    }

    public String getDefaultDomain() {
        Test.log
                .info("getDefaultDomain() method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().getDefaultDomain();
    }

    public String[] getDomains() {
        checkCount++;
        Test.log.info("getDomains() method forward to the appropriate object");
        return getMBeanServer().getDomains();
    }

    public Integer getMBeanCount() {
        checkCount++;
        Test.log
                .info("getMBeanCount() method forward to the appropriate object");
        return getMBeanServer().getMBeanCount();
    }

    public MBeanInfo getMBeanInfo(ObjectName objectName)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException {
        checkCount++;
        Test.log
                .info("getMBeanCount() method forward to the appropriate object");
        return getMBeanServer().getMBeanInfo(objectName);
    }

    public ObjectInstance getObjectInstance(ObjectName objectName)
            throws InstanceNotFoundException {
        Test.log
                .info("getObjectInstance(ObjectName objectName) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().getObjectInstance(objectName);
    }

    public void addNotificationListener(ObjectName objectName,
            NotificationListener listenerName, NotificationFilter notificationFilter,
            Object obj) throws InstanceNotFoundException {
        checkCount++;
        Test.log
                .info("addNotificationListener(ObjectName objectName, NotificationListener listenerName, NotificationFilter notificationFilter, Object obj) method forward to the appropriate object");
        getMBeanServer().addNotificationListener(objectName, listenerName, notificationFilter,
                obj);
    }

    public void addNotificationListener(ObjectName objectName, ObjectName listenerName,
            NotificationFilter notificationFilter, Object obj)
            throws InstanceNotFoundException {

        checkCount++;
        Test.log
                .info("addNotificationListener(ObjectName objectName, ObjectName listenerName, NotificationFilter notificationFilter, Object obj) method forward to the appropriate object");
        getMBeanServer().addNotificationListener(objectName, listenerName, notificationFilter,
                obj);
    }

    public ObjectInstance createMBean(String className, ObjectName objectName)
            throws ReflectionException, InstanceAlreadyExistsException,
            MBeanRegistrationException, MBeanException,
            NotCompliantMBeanException {
        Test.log
                .info("createMBean(String className, ObjectName objectName) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().createMBean(className, objectName);
    }

    public ObjectInstance createMBean(String className, ObjectName objectName,
            Object params[], String signature[]) throws ReflectionException,
            InstanceAlreadyExistsException, MBeanRegistrationException,
            MBeanException, NotCompliantMBeanException {
        Test.log
                .info("createMBean(String className, ObjectName objectName, Object params[], String signature[]) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().createMBean(className, objectName, params, signature);
    }

    public ObjectInstance createMBean(String className, ObjectName objectName,
            ObjectName classLoader)

    throws ReflectionException, InstanceAlreadyExistsException,
            MBeanRegistrationException, MBeanException,
            NotCompliantMBeanException, InstanceNotFoundException {
        Test.log
                .info("createMBean(String className, ObjectName objectName, ObjectName classLoader) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().createMBean(className, objectName, classLoader);
    }

    public ObjectInstance createMBean(String className, ObjectName objectName,
            ObjectName classLoader, Object parameters[], String signature[])
            throws ReflectionException, InstanceAlreadyExistsException,
            MBeanRegistrationException, MBeanException,
            NotCompliantMBeanException, InstanceNotFoundException {
        checkCount++;
        Test.log
                .info("createMBean(String className, ObjectName objectName, ObjectName classLoader, Object parameters[], String signature[]) method forward to the appropriate object");
        return getMBeanServer().createMBean(className, objectName, classLoader,
                parameters, signature);
    }

    public ObjectInputStream deserialize(ObjectName objectName, byte[] dataArray)
            throws InstanceNotFoundException, OperationsException {
        Test.log
                .info("deserialize(ObjectName objectName, byte[] dataArray) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().deserialize(objectName, dataArray);
    }

    public ObjectInputStream deserialize(String className, byte[] dataArray)
            throws OperationsException, ReflectionException {
        checkCount++;
        Test.log
                .info("deserialize(String className, byte[] dataArray) method forward to the appropriate object");
        return getMBeanServer().deserialize(className, dataArray);
    }

    public ObjectInputStream deserialize(String className,
            ObjectName classLoader, byte[] dataArray)
            throws InstanceNotFoundException, OperationsException,
            ReflectionException {
        checkCount++;
        Test.log
                .info("deserialize(String className, ObjectName classLoader, byte[] dataArray) method forward to the appropriate object");
        return getMBeanServer().deserialize(className, classLoader, dataArray);
    }

    public Object instantiate(String className) throws ReflectionException,
            MBeanException {
        Test.log
                .info("instantiate(String className) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().instantiate(className);
    }

    public Object instantiate(String className, Object parameters[],
            String signature[]) throws ReflectionException, MBeanException {
        Test.log
                .info("instantiate(String className, Object parameters[], String signature[]) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().instantiate(className, parameters, signature);
    }

    public Object instantiate(String className, ObjectName classLoader)
            throws ReflectionException, MBeanException,
            InstanceNotFoundException {
        Test.log
                .info("instantiate(String className, ObjectName classLoader) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().instantiate(className, classLoader);
    }

    public Object instantiate(String className, ObjectName classLoader,
            Object parameters[], String signature[]) throws ReflectionException,
            MBeanException, InstanceNotFoundException {
        Test.log
                .info("instantiate(String className, ObjectName classLoader, Object parameters[], String signature[]) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().instantiate(className, classLoader, parameters,
                signature);
    }

    public Object invoke(ObjectName objectName, String operationName,
            Object parameters[], String signature[])
            throws InstanceNotFoundException, MBeanException,
            ReflectionException {
        Test.log
                .info("invoke(ObjectName objectName, String operationName, Object parameters[], String signature[]) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().invoke(objectName, operationName, parameters, signature);
    }

    public boolean isInstanceOf(ObjectName objectName, String className)
            throws InstanceNotFoundException {
        Test.log
                .info("isInstanceOf(ObjectName objectName, String className) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().isInstanceOf(objectName, className);
    }

    public boolean isRegistered(ObjectName objectName) {
        Test.log
                .info("isRegistered(ObjectName objectName) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().isRegistered(objectName);
    }

    public Set queryMBeans(ObjectName objectName, QueryExp queryExp) {
        Test.log
                .info(" queryMBeans(ObjectName objectName, QueryExp query) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().queryMBeans(objectName, queryExp);
    }

    public Set queryNames(ObjectName objectName, QueryExp queryExp) {
        Test.log
                .info("queryNames(ObjectName objectName, QueryExp query) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().queryNames(objectName, queryExp);
    }

    public ObjectInstance registerMBean(Object object, ObjectName objectName)
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException {
        checkCount++;
        Test.log
                .info("registerMBean(Object object, ObjectName objectName) method forward to the appropriate object");
        return getMBeanServer().registerMBean(object, objectName);
    }

    public void removeNotificationListener(ObjectName objectName,
            NotificationListener listenerName) throws InstanceNotFoundException,
            ListenerNotFoundException {
        Test.log
                .info("removeNotificationListener(ObjectName objectName,            NotificationListener listenerName) method forward to the appropriate object");
        checkCount++;
        getMBeanServer().removeNotificationListener(objectName, listenerName);
    }

    public void removeNotificationListener(ObjectName objectName,
            NotificationListener listenerNotification, NotificationFilter notificationFilter,
            Object obj) throws InstanceNotFoundException,
            ListenerNotFoundException {
        checkCount++;
        Test.log
                .info("getObjectInstance(ObjectName objectName) method forward to the appropriate object");
        getMBeanServer().removeNotificationListener(objectName, listenerNotification, notificationFilter,
                obj);
    }

    public void removeNotificationListener(ObjectName objectName, ObjectName listenerName)
            throws InstanceNotFoundException, ListenerNotFoundException {
        Test.log
                .info("getObjectInstance(ObjectName objectName) method forward to the appropriate object");
        checkCount++;
        getMBeanServer().removeNotificationListener(objectName, listenerName);

    }

    public void removeNotificationListener(ObjectName objectName,
            ObjectName listenerName, NotificationFilter notificationFilter, Object obj)
            throws InstanceNotFoundException, ListenerNotFoundException {
        Test.log
                .info("getObjectInstance(ObjectName objectName) method forward to the appropriate object");
        checkCount++;
        getMBeanServer().removeNotificationListener(objectName, listenerName, notificationFilter,
                obj);
    }

    public void setAttribute(ObjectName objectName, Attribute attr)
            throws InstanceNotFoundException, AttributeNotFoundException,
            InvalidAttributeValueException, MBeanException, ReflectionException {
        Test.log
                .info("getObjectInstance(ObjectName objectName) method forward to the appropriate object");
        checkCount++;
        getMBeanServer().setAttribute(objectName, attr);
    }

    public AttributeList setAttributes(ObjectName objectName, AttributeList attrs)
            throws InstanceNotFoundException, ReflectionException {
        Test.log
                .info("getObjectInstance(ObjectName objectName) method forward to the appropriate object");
        checkCount++;
        return getMBeanServer().setAttributes(objectName, attrs);
    }

    public void unregisterMBean(ObjectName objectName)
            throws InstanceNotFoundException, MBeanRegistrationException {
        Test.log
                .info("getObjectInstance(ObjectName objectName) method forward to the appropriate object");
        checkCount++;
        getMBeanServer().unregisterMBean(objectName);
    }
}
