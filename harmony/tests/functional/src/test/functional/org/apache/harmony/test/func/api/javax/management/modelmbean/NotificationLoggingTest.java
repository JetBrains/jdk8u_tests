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

import java.io.File;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.Descriptor;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Verify that logs of notification, file name is value of descriptor
 * of ModelMBeanNotificationInfo. Test addAttributeChangeNotificationListener
 * and removeAttributeChangeNotificationListener methods, also test that log of
 * notifications writes to filename if there are fields: log=true,
 * logfile=filename.
 * <p>
 * Under test: ModelMBeanAttributeInfo, ModelMBeanNotificationInfo,
 * ModelMBeanInfoSupport, RequiredModelMBean.
 * 
 */
public class NotificationLoggingTest extends MultiCase {
    private Class class1 = ManageResourceAndNotification.class;

    public static void main (String[] args) {
        try {
            System.exit(new NotificationLoggingTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <ul>
     * Verify that logs of new notifications, when sendNotification is invoked,
     * write to file. File name is value of descriptor of
     * ModelMBeanNotificationInfo.
     * <li>Create java class, which is not MBean. MBean has 1 getter and 1
     * setter methods.
     * <li>Create ModelMBeanNotificationInfo object for my type with descriptor
     * with logging.
     * <li>Create ModelMBeanInfoSupport object. All ModelMBeanXXXInfo except
     * ModelMBeanNotificationInfo are default.
     * <li>Create RequiredModelMBean object.
     * <li>Instance of java class in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>Send my notification using sendNotification method.
     * <li>Verify that logfile was created and size of file > 0.
     * </ul>
     */
    public Result testLogging() throws Exception {
        ModelMBeanAttributeInfo attributeInfoForG = new ModelMBeanAttributeInfo(
            "g", "descr", class1.getMethod("getG", null), class1.getMethod(
                "setG", new Class[] { String.class }));
        ModelMBeanAttributeInfo[] attributeInfos = new ModelMBeanAttributeInfo[] { attributeInfoForG };
        ModelMBeanNotificationInfo notificationInfo = new ModelMBeanNotificationInfo(
            new String[] { SimpleNotification.notificationType },
            SimpleNotification.notificationType, "description");
        File file = File.createTempFile("log", ".txt");
        file.deleteOnExit();
        Descriptor descriptor = notificationInfo.getDescriptor();
        descriptor.setField("log", "true");
        descriptor.setField("logfile", file.getAbsolutePath());
        log.info("file name: " + file.getAbsolutePath());
        notificationInfo.setDescriptor(descriptor);
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", attributeInfos, null, null,
            new ModelMBeanNotificationInfo[] { notificationInfo });
        beanInfoSupport.getNotification(new SimpleNotification("src", 1)
            .getType());
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        beanInfoSupport.getDescriptor(new SimpleNotification("src", 1)
            .getType(), "notification");
        requiredModelMBean.sendNotification(new SimpleNotification("src", 1));
        assertTrue(file.length() > 0);
        file.delete();
        return result();
    }

    /**
     * Test addAttributeChangeNotificationListener and
     * removeAttributeChangeNotificationListener methods, also test that log of
     * notifications writes to filename if there are fields: log=true,
     * logfile=filename.
     * <ul>
     * Step by step:
     * <li>1.Create java class, which is not MBean. This class has 2 getter and
     * 2 setter methods.
     * <li>2.Create 2 ModelMBeanAttibuteInfo object for class in 1st step.
     * <li>4.Create ModelMBeanInfoSupport object. All ModelMBeanXXXInfo except
     * ModelMBeanAttibuteInfo's and ModelMBeanOperationInfo are default.
     * <li>5.Extract descriptor from ModelMBeanInfoSupport class and set
     * additional field log=true, logfile=filename.
     * <li>6.Create RequiredModelMBean object.
     * <li>7.Create first notification listener and register it in
     * RequiredModelMBean object for first attribute using
     * addAttributeChangeNotificationListener.
     * <li>8.Create second notification listener and register it in
     * RequiredModelMBean object for first attribute using
     * addAttributeChangeNotificationListener.
     * <li>9.Create third notification listener and register it in
     * RequiredModelMBean object for second attribute using
     * addAttributeChangeNotificationListener.
     * <li>10.Instance of java class in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>11.Create objectName.
     * <li>12.Register RequiredModelMBean object in MBeanServer with objectName
     * specified in previous step.
     * <li>13.Set first attribute using setAttribute method of MBeanServer.
     * <li>14.Verify that first and second notification listener(in 7 and 8
     * steps) were notified about change attribute. And third wasn't notified.
     * <li>15.Remove first notification listener from RequiredModelMBean using
     * removeAttributeChangeNotificationListener.
     * <li>16.Set first attribute using setAttribute method of MBeanServer.
     * <li>17.Verify that only second notification listener(in 7 step) were
     * notified about change attribute. And third wasn't notified.
     * <li>17.Verify that logfile with name in step 5 was created and size of
     * file > 0.
     * </ul>
     */
    public Result testNotification() throws Exception {
        ModelMBeanAttributeInfo attributeInfoForG = new ModelMBeanAttributeInfo(
            "g", "descr", class1.getMethod("getG", null), class1.getMethod(
                "setG", new Class[] { String.class }));
        ModelMBeanAttributeInfo attributeInfoForH = new ModelMBeanAttributeInfo(
            "h", "descr", class1.getMethod("getH", null), class1.getMethod(
                "setH", new Class[] { String.class }));
        ModelMBeanAttributeInfo[] attributeInfos = new ModelMBeanAttributeInfo[] {
            attributeInfoForG, attributeInfoForH };
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", attributeInfos, null, null, null);
        Descriptor descriptor = beanInfoSupport.getMBeanDescriptor();
        File file = File.createTempFile("log", "txt");
        file.deleteOnExit();
        descriptor.setField("log", "true");
        descriptor.setField("logfile", file.getAbsolutePath());
        log.info("file name: " + file.getAbsolutePath());
        beanInfoSupport.setMBeanDescriptor(descriptor);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        ManageResourceAndNotification notificationListener1 = new ManageResourceAndNotification();
        requiredModelMBean.addAttributeChangeNotificationListener(
            notificationListener1, attributeInfoForG.getName(),
            ManageResourceAndNotification.handback);
        ManageResourceAndNotification notificationListener2 = new ManageResourceAndNotification();
        requiredModelMBean.addAttributeChangeNotificationListener(
            notificationListener2, attributeInfoForG.getName(),
            ManageResourceAndNotification.handback);
        ManageResourceAndNotification notificationListener3 = new ManageResourceAndNotification();
        requiredModelMBean.addAttributeChangeNotificationListener(
            notificationListener3, attributeInfoForH.getName(),
            ManageResourceAndNotification.handback);
        requiredModelMBean.setManagedResource(
            new ManageResourceAndNotification(), "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        String newValue = "new value";
        server.setAttribute(objectName, new Attribute(attributeInfoForG
            .getName(), newValue));
        verifyNotifivation((AttributeChangeNotification)notificationListener1
            .getNotification(), attributeInfoForG);
        assertTrue(notificationListener2.isWasHandleNotificationInvoked());
        assertFalse(notificationListener3.isWasHandleNotificationInvoked());
        requiredModelMBean.removeAttributeChangeNotificationListener(
            notificationListener1, attributeInfoForG.getName());

        server.setAttribute(objectName, new Attribute(attributeInfoForG
            .getName(), newValue));
        assertFalse(notificationListener1.isWasHandleNotificationInvoked());
        assertTrue(notificationListener2.isWasHandleNotificationInvoked());
        assertFalse(notificationListener3.isWasHandleNotificationInvoked());
        assertTrue(file.length() > 0);
        file.delete();
        return result();
    }

    /**
     * Verify that notification which RequiredModelMBean is emitted is correct.
     */
    private void verifyNotifivation(
        AttributeChangeNotification attributeChangeNotification,
        ModelMBeanAttributeInfo attributeInfo) {
        assertEquals(attributeChangeNotification.getAttributeName(),
            attributeInfo.getName());
        assertEquals(attributeChangeNotification.getAttributeType(),
            attributeInfo.getType());
        assertEquals(attributeChangeNotification.getNewValue(), "new value");
        assertNull(attributeChangeNotification.getOldValue());
        assertEquals(attributeChangeNotification.getType(),
            "jmx.attribute.change");
        assertEquals(attributeChangeNotification.getMessage(),
            "AttributeChangeDetected");
        assertEquals(attributeChangeNotification.getSequenceNumber(), 1);
    }
}