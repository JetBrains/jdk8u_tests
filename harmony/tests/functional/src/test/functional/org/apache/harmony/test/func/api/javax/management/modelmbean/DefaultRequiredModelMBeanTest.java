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

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.MBeanException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ServiceNotFoundException;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Verify that invoke and setAttribute methods of MBeanServer throw
 * exceptions, if RequiredModelMBean is default. Also verify fields of
 * descriptor of two defaults ModelMBeanNotificationInfo.
 * <p>
 * Under test: ModelMBeanNotificationInfo, RequiredModelMBean.
 * 
 */
public class DefaultRequiredModelMBeanTest extends MultiCase {
    /**
     * Used only in testSendNotification test case.
     */
    private boolean ishandleNotificationInvoked = false;

    public static void main(String[] args) {
        try {
            System.exit(new DefaultRequiredModelMBeanTest().test(args));
            // new DefaultRequiredModelMBeanTest().testSendNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify that invoke and setAttribute methods throw exceptions, if
     * RequiredModelMBean is default.
     * <ul>
     * Step by step:
     * <li>Create RequiredModelMBean object using RequiredModelMBean()
     * constructor.
     * <li>Set Integer class as managed resource for RequiredModelMBean object.
     * <li>Create ObjectName object.
     * <li>Register RequiredModelMBean object in MBeanServer with above
     * objectName.
     * <li>Verify that invoke method of MBeanServer throws MBeanException with
     * nested exception is ServiceNotFoundException.
     * <li>Verify that setAttribute method throws AttributeNotFoundException.
     * </ul>
     */
    public Result testException() throws Exception {
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean();
        requiredModelMBean
            .setManagedResource(new Integer(7), "ObjectReference");
        ObjectName objectName = new ObjectName("modelmbean:type=Operation");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        try {
            server.invoke(objectName, "nick", null, null);
        } catch (MBeanException e) {
            assertTrue(e.getTargetException() instanceof ServiceNotFoundException);
        }
        try {
            server.setAttribute(objectName, new Attribute("name", "value"));
            assertTrue(false);
        } catch (AttributeNotFoundException e) {
        }
        server.unregisterMBean(objectName);
        return result();
    }

    /**
     * Verify fields of descriptor of ModelMBeanNotificationInfo for generic
     * notification.
     * <ul>
     * Step by step:
     * <li>Create RequiredModelMBean object using RequiredModelMBean()
     * constructor.
     * <li>Set Integer class as managed resource for RequiredModelMBean object.
     * <li>Get default notifications info using getNotificationInfo() of
     * RequiredModelMBean.
     * <li>Select ModelMBeanNotificationInfo for generic notification by name
     * "GENERIC" from default notifications info.
     * <li>Verify that getNotifTypes() return {"jmx.modelmbean.generic"}.
     * <li>Extract descriptor using getDescriptor().
     * <li>Verify fields: name=GENERIC, descriptorType=notification, log=T,
     * severity=6, displayName=jmx.modelmbean.generic.
     * </ul>
     */
    public Result testGenericNotificationInfo() throws Exception {
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean();
        MBeanNotificationInfo[] notificationInfo = (MBeanNotificationInfo[])requiredModelMBean
            .getNotificationInfo();
        assertEquals(notificationInfo.length, 2);
        ModelMBeanNotificationInfo genericNotification;
        if (notificationInfo[0].getName() == "GENERIC") {
            genericNotification = (ModelMBeanNotificationInfo)notificationInfo[0];
        } else {
            assertEquals(notificationInfo[1].getName(), "GENERIC");
            genericNotification = (ModelMBeanNotificationInfo)notificationInfo[1];
        }
        String[] notifTypes = genericNotification.getNotifTypes();
        assertEquals(notifTypes.length, 1);
        assertEquals(notifTypes[0], "jmx.modelmbean.generic");
        Descriptor descriptor = genericNotification.getDescriptor();
        assertEquals(descriptor.getFieldValue("name"), "GENERIC");
        assertEquals(descriptor.getFieldValue("descriptorType"), "notification");
        assertEquals(descriptor.getFieldValue("log"), "T");
        assertEquals(descriptor.getFieldValue("severity"), "6");
        assertEquals(descriptor.getFieldValue("displayName"),
            "jmx.modelmbean.generic");
        return result();
    }

    /**
     * Verify fields of descriptor of ModelMBeanNotificationInfo for attribute
     * change notification.
     * <ul>
     * Step by step:
     * <li>Create RequiredModelMBean object using RequiredModelMBean()
     * constructor.
     * <li>Set Integer class as managed resource for RequiredModelMBean object.
     * <li>Get default notifications info using getNotificationInfo() of
     * RequiredModelMBean.
     * <li>Select ModelMBeanNotificationInfo for attribute change notification
     * by name "ATTRIBUTE_CHANGE" from default notifications info.
     * <li>Verify that getNotifTypes() return {"jmx.attribute.change"}.
     * <li>Extract descriptor using getDescriptor().
     * <li>Verify fields: name=ATTRIBUTE_CHANGE, descriptorType=notification,
     * log=T, severity=6, displayName=jmx.attribute.change.
     * </ul>
     */
    public Result testAttributeChangeNotificationInfo() throws Exception {
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean();
        MBeanNotificationInfo[] notificationInfo = (MBeanNotificationInfo[])requiredModelMBean
            .getNotificationInfo();
        assertEquals(notificationInfo.length, 2);
        ModelMBeanNotificationInfo genericNotification;
        if (notificationInfo[0].getName() == "ATTRIBUTE_CHANGE") {
            genericNotification = (ModelMBeanNotificationInfo)notificationInfo[0];
        } else {
            assertEquals(notificationInfo[1].getName(), "ATTRIBUTE_CHANGE");
            genericNotification = (ModelMBeanNotificationInfo)notificationInfo[1];
        }
        String[] notifTypes = genericNotification.getNotifTypes();
        assertEquals(notifTypes.length, 1);
        assertEquals(notifTypes[0], "jmx.attribute.change");
        Descriptor descriptor = genericNotification.getDescriptor();
        assertEquals(descriptor.getFieldValue("name"), "ATTRIBUTE_CHANGE");
        assertEquals(descriptor.getFieldValue("descriptorType"), "notification");
        assertEquals(descriptor.getFieldValue("log"), "T");
        assertEquals(descriptor.getFieldValue("severity"), "6");
        assertEquals(descriptor.getFieldValue("displayName"),
            "jmx.attribute.change");
        return result();
    }

    /**
     * Verify that RequiredModelMBean generates correct generic notification.
     * <ul>
     * Step by step:
     * <li>Create RequiredModelMBean object using RequiredModelMBean()
     * constructor.
     * <li>Set Integer class as managed resource for RequiredModelMBean object.
     * <li>Create ObjectName object.
     * <li>Register RequiredModelMBean object in MBeanServer with above
     * objectName.
     * <li> Create notification listener and add this listener in server.
     * <li>Send notification using sendNotification(msg) method of
     * RequiredModelMBean.
     * <li> Verify that handleNotification method of listener was invoked.
     * <li>Verify notification in parameter of handleNotification method: its
     * type=jmx.modelmbean.generic, its message=msg in 4 step and sequence
     * number=1.
     * </ul>
     */
    public Result testSendNotification() throws Exception {
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean();
        requiredModelMBean
            .setManagedResource(new Integer(7), "ObjectReference");
        ObjectName objectName = new ObjectName("modelmbean:type=Operation");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        final String message = "message";
        NotificationListener notificationListener = new NotificationListener() {
            public void handleNotification(Notification arg0, Object arg1) {
                ishandleNotificationInvoked = true;
                assertEquals(arg0.getType(), "jmx.modelmbean.generic");
                assertEquals(arg0.getMessage(), message);
                assertEquals(arg0.getSequenceNumber(), 1);
            }

        };
        server.addNotificationListener(objectName, notificationListener, null,
            null);
        requiredModelMBean.sendNotification(message);
        assertTrue(ishandleNotificationInvoked);
        server.unregisterMBean(objectName);
        return result();
    }
}