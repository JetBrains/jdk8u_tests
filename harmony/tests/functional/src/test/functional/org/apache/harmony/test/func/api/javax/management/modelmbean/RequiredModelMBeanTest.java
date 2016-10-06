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
//$Id$
package org.apache.harmony.test.func.api.javax.management.modelmbean;

import java.lang.reflect.Method;

import javax.management.Attribute;
import javax.management.Descriptor;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Verify that setAttribute and getAttribute methods sets and returns
 * correct attribute. Verify that all registered listeners for specific
 * attribute were notified about change attribute. Verify that value of
 * attribute write to value of field attribute of descriptor of
 * ModelMBeanAttibuteInfo. Verify that logs write to file whose name is value of
 * descriptor of ModelMBeanAttibuteInfo.
 * <p>
 * Under test: ModelMBeanAttributeInfo, ModelMBeanOperationInfo,
 * ModelMBeanInfoSupport, RequiredModelMBean.
 * 
 */
public class RequiredModelMBeanTest extends MultiCase {
    private Class class1 = ManageResourceAndNotification.class;

    public static void main(String[] args) {
        try {
            System.exit(new RequiredModelMBeanTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify that value of attribute return value which return getter method if
     * currencyTimeLimit < 0.
     * <ul>
     * Step by step:
     * <li>1.Create java class, which is not MBean. This class has getter and
     * setter methods.
     * <li>2.Create ModelMBeanAttibuteInfo object for class in 1st step.
     * <li>3.Extract descriptor from ModelMBeanAttibuteInfo class and set
     * additional fields currencyTimeLimit < 0 and setMethod=nameOfSetterMethod.
     * <li>4.Create ModelMBeanInfoSupport object.
     * <li>5.Create RequiredModelMBean object.
     * <li>6.Create notification listener and register it in RequiredModelMBean
     * object for attribute using addAttributeChangeNotificationListener.
     * <li>7.Instance of java class in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>8.Create objectName.
     * <li>9.Register RequiredModelMBean object in MBeanServer with objectName
     * specified in previous step.
     * <li>10.Set attribute on MBeanServer using setAttribute method.
     * <li>11.Verify that notification listener was notified about change
     * attribute.
     * <li>12.Verify that setter method was invoked.
     * <li>13.Verify that getAttribute of MBeanServer returns correct value of
     * attribute and getter method was invoked.
     * <li>14. Invoke again getAttribute of MBeanServer method and verify
     * returned value. Also verify that getter method was invoked.
     * </ul>
     */
    public Result testNegative() throws Exception {
        Method getter = class1.getMethod("getG", null);
        Method setter = class1.getMethod("setG", new Class[] { String.class });
        ModelMBeanAttributeInfo attributeInfoForG = new ModelMBeanAttributeInfo(
            "name", "description", getter, setter);
        Descriptor descriptor = attributeInfoForG.getDescriptor();
        descriptor.setField("currencyTimeLimit", "-1");
        descriptor.setField("setMethod", setter.getName());
        descriptor.setField("getMethod", getter.getName());
        attributeInfoForG.setDescriptor(descriptor);
        ModelMBeanAttributeInfo[] attributeInfos = new ModelMBeanAttributeInfo[] { attributeInfoForG };
        ModelMBeanOperationInfo[] operationInfos = new ModelMBeanOperationInfo[] {
            new ModelMBeanOperationInfo("description", setter),
            new ModelMBeanOperationInfo("description", getter) };
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", attributeInfos, null,
            operationInfos, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        ManageResourceAndNotification notificationListener = new ManageResourceAndNotification();
        requiredModelMBean.addAttributeChangeNotificationListener(
            notificationListener, attributeInfoForG.getName(),
            ManageResourceAndNotification.handback);
        ManageResourceAndNotification managedResource = new ManageResourceAndNotification();
        requiredModelMBean.setManagedResource(managedResource,
            "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        String newValue = "new value";
        server.setAttribute(objectName, new Attribute(attributeInfoForG
            .getName(), newValue));
        assertTrue(notificationListener.isWasHandleNotificationInvoked());
        assertEquals(managedResource.getG(), newValue);
        managedResource.isGetGWasInvoked();
        assertEquals(server.getAttribute(objectName, attributeInfoForG
            .getName()), newValue);
        assertTrue(managedResource.isGetGWasInvoked());
        assertTrue(server.getMBeanInfo(objectName).getAttributes()[0] == attributeInfoForG);
        assertNull(attributeInfoForG.getDescriptor().getFieldValue("value"));
        return result();
    }

    /**
     * Verify that value of attribute retrieves a value from cache if
     * currencyTimeLimit = 0.
     * <ul>
     * Step by step:
     * <li>1.Create java class, which is not MBean. This class has getter and
     * setter methods.
     * <li>2.Create ModelMBeanAttibuteInfo object for class in 1st step.
     * <li>3.Extract descriptor from ModelMBeanAttibuteInfo class and set
     * additional fields currencyTimeLimit = 0 and setMethod=nameOfSetterMethod.
     * <li>4.Create ModelMBeanInfoSupport object.
     * <li>5.Create RequiredModelMBean object.
     * <li>6.Create notification listener and register it in RequiredModelMBean
     * object for attribute using addAttributeChangeNotificationListener.
     * <li>7.Instance of java class in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>8.Create objectName.
     * <li>9.Register RequiredModelMBean object in MBeanServer with objectName
     * specified in previous step.
     * <li>10.Set attribute on MBeanServer using setAttribute method.
     * <li>11.Verify that notification listener was notified about change
     * attribute.
     * <li>12.Verify that setter method was invoked.
     * <li>13.Verify that getAttribute returns correct value of attribute and
     * getter method was not invoked.
     * <li>14.Verify value of field value of descriptor of
     * ModelMBeanAttibuteInfo.
     */
    public Result testZero() throws Exception {
        Method getter = class1.getMethod("getG", null);
        Method setter = class1.getMethod("setG", new Class[] { String.class });
        ModelMBeanAttributeInfo attributeInfoForG = new ModelMBeanAttributeInfo(
            "name", "description", getter, setter);
        Descriptor descriptor = attributeInfoForG.getDescriptor();
        descriptor.setField("currencyTimeLimit", "0");
        descriptor.setField("setMethod", setter.getName());
        descriptor.setField("getMethod", getter.getName());
        attributeInfoForG.setDescriptor(descriptor);
        ModelMBeanAttributeInfo[] attributeInfos = new ModelMBeanAttributeInfo[] { attributeInfoForG };
        ModelMBeanOperationInfo[] operationInfos = new ModelMBeanOperationInfo[] {
            new ModelMBeanOperationInfo("description", setter),
            new ModelMBeanOperationInfo("description", getter) };
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", attributeInfos, null,
            operationInfos, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        ManageResourceAndNotification notificationListener = new ManageResourceAndNotification();
        requiredModelMBean.addAttributeChangeNotificationListener(
            notificationListener, attributeInfoForG.getName(),
            ManageResourceAndNotification.handback);
        ManageResourceAndNotification managedResource = new ManageResourceAndNotification();
        requiredModelMBean.setManagedResource(managedResource,
            "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        String newValue = "new value";
        server.setAttribute(objectName, new Attribute(attributeInfoForG
            .getName(), newValue));
        assertTrue(notificationListener.isWasHandleNotificationInvoked());
        assertEquals(managedResource.getG(), newValue);
        managedResource.isGetGWasInvoked();
        assertEquals(server.getAttribute(objectName, attributeInfoForG
            .getName()), newValue);
        assertFalse(managedResource.isGetGWasInvoked());
        assertTrue(server.getMBeanInfo(objectName).getAttributes()[0] == attributeInfoForG);
        assertEquals(attributeInfoForG.getDescriptor().getFieldValue("value"),
            newValue);
        return result();
    }

    /**
     * Verify that value of attribute retrieves a value from cache or invoke
     * getter method depends on currencyTimeLimit > 0 and lastUpdatedTimeStamp.
     * <ul>
     * Step by step:
     * <li>1.Create java class, which is not MBean. This class has getter and
     * setter methods.
     * <li>2.Create ModelMBeanAttibuteInfo object for class in 1st step.
     * <li>3.Extract descriptor from ModelMBeanAttibuteInfo class and set
     * additional fields currencyTimeLimit > 0 and setMethod=nameOfSetterMethod.
     * <li>4.Create ModelMBeanInfoSupport object. All ModelMBeanXXXInfo except
     * ModelMBeanAttibuteInfo and ModelMBeanOperationInfo are default.
     * <li>5.Create RequiredModelMBean object.
     * <li>6.Create notification listener and register it in RequiredModelMBean
     * object for attribute using addAttributeChangeNotificationListener.
     * <li>7.Instance of java class in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>8.Create objectName.
     * <li>9.Register RequiredModelMBean object in MBeanServer with objectName
     * specified in previous step.
     * <li>10.Set attribute on MBeanServer using setAttribute method.
     * <li>11.Verify that notification listener was notified about change
     * attribute.
     * <li>12.Verify that setter method was invoked.
     * <li>13.Verify that getAttribute method of MBeanServer returns correct
     * value of attribute and getter method was invoked.
     * <li>14.Verify value of field value of descriptor of
     * ModelMBeanAttibuteInfo.
     * <li>15. Set attribute to another value using setter method in step 1.
     * <li>16. Invoke again getAttribute of MBeanServer method and verify
     * returned value doesn't changed. Also verify that getter method wasn't
     * invoked.
     * <li>17. Wait currencyTimeLimit seconds.
     * <li>18. Invoke again getAttribute of MBeanServer method and verify that
     * returned value is changed. Also verify that getter method was invoked.
     * </ul>
     */
    public Result testPositive() throws Exception {
        Method getter = class1.getMethod("getG", null);
        Method setter = class1.getMethod("setG", new Class[] { String.class });
        ModelMBeanAttributeInfo attributeInfoForG = new ModelMBeanAttributeInfo(
            "name", "description", getter, setter);
        Descriptor descriptor = attributeInfoForG.getDescriptor();
        int currencyTimeLimit = 5;
        descriptor.setField("currencyTimeLimit", currencyTimeLimit + "");
        descriptor.setField("setMethod", setter.getName());
        descriptor.setField("getMethod", getter.getName());
        attributeInfoForG.setDescriptor(descriptor);
        ModelMBeanAttributeInfo[] attributeInfos = new ModelMBeanAttributeInfo[] { attributeInfoForG };
        ModelMBeanOperationInfo[] operationInfos = new ModelMBeanOperationInfo[] {
            new ModelMBeanOperationInfo("description", setter),
            new ModelMBeanOperationInfo("description", getter) };
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", attributeInfos, null,
            operationInfos, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        ManageResourceAndNotification notificationListener = new ManageResourceAndNotification();
        requiredModelMBean.addAttributeChangeNotificationListener(
            notificationListener, attributeInfoForG.getName(),
            ManageResourceAndNotification.handback);
        ManageResourceAndNotification managedResource = new ManageResourceAndNotification();
        requiredModelMBean.setManagedResource(managedResource,
            "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        String newValue = "new value";
        server.setAttribute(objectName, new Attribute(attributeInfoForG
            .getName(), newValue));
        assertTrue(notificationListener.isWasHandleNotificationInvoked());
        assertEquals(managedResource.getG(), newValue);
        managedResource.isGetGWasInvoked();
        assertEquals(server.getAttribute(objectName, attributeInfoForG
            .getName()), newValue);
        assertFalse(managedResource.isGetGWasInvoked());
        assertTrue(server.getMBeanInfo(objectName).getAttributes()[0] == attributeInfoForG);
        assertEquals(attributeInfoForG.getDescriptor().getFieldValue("value"),
            newValue);
        String newValue2 = "new value 2";
        managedResource.setG(newValue2);

        assertEquals(server.getAttribute(objectName, attributeInfoForG
            .getName()), newValue);
        assertFalse(managedResource.isGetGWasInvoked());
        Thread.sleep(currencyTimeLimit * 1000+InvocationTest.latancy);
        assertEquals(server.getAttribute(objectName, attributeInfoForG
            .getName()), newValue2);
        assertTrue(managedResource.isGetGWasInvoked());
        return result();
    }

    /**
     * Verify that value of attribute always retrieves from returned value of
     * getter method if currencyTimeLimit is not defined in descriptor.
     * <p>
     * Instructions are the same as in testNegative.
     */
    public Result testNotPresent() throws Exception {
        Method getter = class1.getMethod("getG", null);
        Method setter = class1.getMethod("setG", new Class[] { String.class });
        ModelMBeanAttributeInfo attributeInfoForG = new ModelMBeanAttributeInfo(
            "name", "description", getter, setter);
        Descriptor descriptor = attributeInfoForG.getDescriptor();
        descriptor.setField("setMethod", setter.getName());
        descriptor.setField("getMethod", getter.getName());
        attributeInfoForG.setDescriptor(descriptor);
        ModelMBeanAttributeInfo[] attributeInfos = new ModelMBeanAttributeInfo[] { attributeInfoForG };
        ModelMBeanOperationInfo[] operationInfos = new ModelMBeanOperationInfo[] {
            new ModelMBeanOperationInfo("description", setter),
            new ModelMBeanOperationInfo("description", getter) };
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", attributeInfos, null,
            operationInfos, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        ManageResourceAndNotification notificationListener = new ManageResourceAndNotification();
        requiredModelMBean.addAttributeChangeNotificationListener(
            notificationListener, attributeInfoForG.getName(),
            ManageResourceAndNotification.handback);
        ManageResourceAndNotification managedResource = new ManageResourceAndNotification();
        requiredModelMBean.setManagedResource(managedResource,
            "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        String newValue = "new value";
        server.setAttribute(objectName, new Attribute(attributeInfoForG
            .getName(), newValue));
        assertTrue(notificationListener.isWasHandleNotificationInvoked());
        assertEquals(managedResource.getG(), newValue);
        managedResource.isGetGWasInvoked();
        assertEquals(server.getAttribute(objectName, attributeInfoForG
            .getName()), newValue);
        assertTrue(managedResource.isGetGWasInvoked());
        assertTrue(server.getMBeanInfo(objectName).getAttributes()[0] == attributeInfoForG);
        assertNull(attributeInfoForG.getDescriptor().getFieldValue("value"));
        return result();
    }
}