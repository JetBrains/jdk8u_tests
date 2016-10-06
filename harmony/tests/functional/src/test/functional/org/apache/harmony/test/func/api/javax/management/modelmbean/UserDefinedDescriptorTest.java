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
import java.util.HashMap;

import javax.management.Descriptor;
import javax.management.MBeanFeatureInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Verify that user's descriptor for ModelMBeanXXXInfo don't lose when
 * RequiredModelMBean registered in server.
 * <p>
 * Under test: ModelMBeanConstructorInfo, ModelMBeanAttributeInfo,
 * ModelMBeanNotificationInfo, ModelMBeanOperationInfo, ModelMBeanInfoSupport,
 * RequiredModelMBean.
 * <ul>
 * Step-by-step:
 * <li>1. Create java class, which is not MBean.
 * <li>2. Create ModelMBeanXXXInfo object for class in 1st step using
 * constructor ModelMBeanXXXInfo with descriptor.
 * <li>3. Extract descriptor for ModelMBeanXXXInfo. Set one specific field for
 * descriptor and set the descriptor for ModelMBeanXXXInfo.
 * <li>4. Repeat steps 1-3 for all ModelMBeanXXXInfo.
 * <li>5. Create descriptor for necessary fields for ModelMBeanInfoSupport plus
 * one specific field.
 * <li>6. Create ModelMBeanInfoSupport object with specified ModelMBeanXXXInfo
 * object and specified descriptor.
 * <li>7. Create RequiredModelMBean object.
 * <li>8. Instance of java class in 1st step sets managed resource for
 * RequiredModelMBean using setManagedResource method.
 * <li>9. Create ObjectName object. Register RequiredModelMBean object in
 * MBeanServer with specified ObjectName.
 * <li>10. Extract ModelMBeanInfo from MBeanServer using getMBeanInfo method.
 * <li>11. Extract descriptor for MBean using getMBeanDescriptor method.
 * <li>12. Verify that all fields in descriptor for ModelMBeanInfoSupport the
 * same as in step 5.
 * <li>13. Extract ModelMBeanXXXInfo from ModelMBeanInfo using getXXX method.
 * <li>14. Verify that all fields in ModelMBeanXXXInfo the same as in step 3.
 * <li>15. Repeat steps 13, 14 for all ModelMBeanXXXInfo.
 * </ul>
 * There is only one test case in this test.
 * 
 */
public class UserDefinedDescriptorTest extends MultiCase {
    /**
     * Getter, setter and operation methods are extracted form this class.
     */
    private static final Class class1 = UserDefinedDescriptorTest.class;
    /**
     * Store Descriptor's for ModelMBeanXXXInfo and ModelMBeanInfoSupport.
     */
    private HashMap            map    = new HashMap();

    public static void main (String[] args) {
        try {
            System.exit(new UserDefinedDescriptorTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Test consists of 3 parts: 1-6 implemented in
     * constractModelMBeanInfoSupport method, 7-10 in current method, 11-15
     * implemented in verifyModelMBeanInfo method.
     */
    public Result test01() throws Exception {
        ModelMBeanInfoSupport beanInfoSupport = constractModelMBeanInfoSupport();
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        requiredModelMBean.setManagedResource(new UserDefinedDescriptorTest(),
            "ObjectReference");
        ObjectName objectName = new ObjectName("modelmbean:type=Operation");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        verifyModelMBeanInfo((ModelMBeanInfoSupport)server
            .getMBeanInfo(objectName));
        return passed();
    }

    /**
     * Do 11-15 steps.
     */
    private void verifyModelMBeanInfo(ModelMBeanInfoSupport modelMBeanInfo)
        throws Exception {
        Descriptor descriptor = modelMBeanInfo.getMBeanDescriptor();
        Descriptor descriptor2 = (Descriptor)map.get(modelMBeanInfo.getClass()
            .getName());
        assertTrue(DefaultDescriptorTest.compareDescriptors(descriptor,
            descriptor2));
        verifyDescriptor(modelMBeanInfo.getAttribute("name"));
        verifyDescriptor(modelMBeanInfo.getAttributes());
        verifyDescriptor(modelMBeanInfo.getConstructors());
        verifyDescriptor(modelMBeanInfo.getNotification("name"));
        verifyDescriptor(modelMBeanInfo.getNotifications());
        verifyDescriptor(modelMBeanInfo.getOperations());
    }

    private void verifyDescriptor(MBeanFeatureInfo[] featureInfos)
        throws Exception {
        assertEquals(featureInfos.length, 1);
        verifyDescriptor(featureInfos[0]);
    }

    private void verifyDescriptor(MBeanFeatureInfo featureInfo)
        throws Exception {
        Method getDescriptorMethod = featureInfo.getClass().getMethod(
            "getDescriptor", null);
        Descriptor descriptor = (Descriptor)getDescriptorMethod.invoke(
            featureInfo, null);
        Descriptor descriptor2 = (Descriptor)map.get(featureInfo.getClass()
            .getName());
        assertTrue(DefaultDescriptorTest.compareDescriptors(descriptor,
            descriptor2));
    }

    /**
     * Do 1-6 steps.
     */
    private ModelMBeanInfoSupport constractModelMBeanInfoSupport()
        throws Exception {
        ModelMBeanOperationInfo operationInfo = new ModelMBeanOperationInfo(
            "description", class1.getMethod("simpleOperartion", null));
        setDescriptor(operationInfo);
        ModelMBeanConstructorInfo constructorInfo = new ModelMBeanConstructorInfo(
            "description", class1.getConstructor(null));
        setDescriptor(constructorInfo);
        ModelMBeanAttributeInfo attributeInfo = new ModelMBeanAttributeInfo(
            "name", "description", class1.getMethod("getH", null), class1
                .getMethod("setH", new Class[] { int.class }));
        setDescriptor(attributeInfo);
        ModelMBeanNotificationInfo notificationInfo = new ModelMBeanNotificationInfo(
            new String[] { "specific notification tepes" }, "name",
            "description");
        setDescriptor(notificationInfo);
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description",
            new ModelMBeanAttributeInfo[] { attributeInfo },
            new ModelMBeanConstructorInfo[] { constructorInfo },
            new ModelMBeanOperationInfo[] { operationInfo },
            new ModelMBeanNotificationInfo[] { notificationInfo });
        Descriptor descriptor = beanInfoSupport.getMBeanDescriptor();
        String[] strings = getSpesific(beanInfoSupport.getClass());
        descriptor.setField(strings[0], strings[1]);
        map.put(beanInfoSupport.getClass().getName(), descriptor);
        beanInfoSupport.setMBeanDescriptor(descriptor);
        return beanInfoSupport;
    }

    private void setDescriptor(MBeanFeatureInfo featureInfo) throws Exception {
        Method getDescriptorMethod = featureInfo.getClass().getMethod(
            "getDescriptor", null);
        Descriptor descriptor = (Descriptor)getDescriptorMethod.invoke(
            featureInfo, null);
        String[] strings = getSpesific(featureInfo.getClass());
        descriptor.setField(strings[0], strings[1]);
        map.put(featureInfo.getClass().getName(), descriptor);
        Method setDescriptorMethod = featureInfo.getClass().getMethod(
            "setDescriptor", new Class[] { Descriptor.class });
        setDescriptorMethod.invoke(featureInfo, new Object[] { descriptor });
    }

    /**
     * Return specific field name and value for class1.
     */
    private String[] getSpesific(Class class1) {
        return new String[] { class1.getName() + " name",
            class1.getName() + " value" };
    }

    // Getter, setter and operation methods
    public void simpleOperartion() {

    }

    public int getH() {
        return 7;
    }

    public void setH(int h) {

    }
}