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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import javax.management.Descriptor;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Verify default descriptor for ModelMBeanConstructorInfo,
 * ModelMBeanAttributeInfo, ModelMBeanNotificationInfo, ModelMBeanOperationInfo,
 * ModelMBeanInfoSupport.
 * <p>
 * Under test: DescriptorSupport, ModelMBeanConstructorInfo,
 * ModelMBeanAttributeInfo, ModelMBeanNotificationInfo, ModelMBeanOperationInfo,
 * ModelMBeanInfoSupport.
 * <ul>
 * Step by step for verify default descriptor ModelMBeanConstructorInfo,
 * ModelMBeanAttributeInfo, ModelMBeanNotificationInfo, ModelMBeanOperationInfo:
 * <li>1. Create ModelMBeanXXXInfo object.
 * <li>2. Extract a descriptor from created object using getDescriptor()
 * method.
 * <li>3. Verify that all default fields of the descriptor exist using
 * getFieldValue(String inFieldName) method.
 * <li>4. There are no other fields.
 * <li>5. Convert the descriptor to xml.
 * <li>6. Create new descriptor from xml using DescriptorSupport(String inStr)
 * constructor.
 * <li>7. Repeat step 1 and 6 for each ModelMBeanXXXInfo object.
 * </ul>
 * Default fields of descriptor can be found in description for test case.
 * 
 */
public class DefaultDescriptorTest extends MultiCase {
    Descriptor                descriptor;
    public static final Class sampleClass = DefaultDescriptorTest.class;

    public static void main(String[] args) {
        try {
            System.exit(new DefaultDescriptorTest().test(args));
            // new DefaultDescriptortTest().testModelMBeanInfoSupport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify default fields of descriptor from ModelMBeanOperationInfo:
     * name=nameOfMethod, displayName=nameOfMethod, role=operation and
     * descriptorType=operation.
     */
    public Result testModelMBeanOperationInfo() throws Exception {
        Method method = sampleClass.getMethod("sayOk", null);
        ModelMBeanOperationInfo operationInfo = new ModelMBeanOperationInfo(
            "description", method);
        descriptor = operationInfo.getDescriptor();
        String name = (String)descriptor.getFieldValue("name");
        assertEquals(name, descriptor.getFieldValue("Name"));
        assertEquals(name, method.getName());
        assertEquals(descriptor.getFieldValue("displayName"), method.getName());
        assertEquals(descriptor.getFieldValue("role"), "operation");
        assertEquals(descriptor.getFieldValue("descriptorType"), "operation");
        assertEquals(descriptor.getFields().length, 4);
        commonCheck();
        return result();
    }

    /**
     * Verify default fields of descriptor from ModelMBeanConstructorInfo:
     * name=nameOfConstructor, displayName=nameOfConstructor, role=constructor
     * and descriptorType=operation.
     */
    public Result testModelMBeanConstructorInfo() throws Exception {
        Constructor constructor = sampleClass.getConstructor(null);
        ModelMBeanConstructorInfo constructorInfo = new ModelMBeanConstructorInfo(
            "description", constructor);
        descriptor = constructorInfo.getDescriptor();
        assertEquals(descriptor.getFieldValue("name"), constructorInfo
            .getName());
        assertEquals(descriptor.getFieldValue("displayName"), constructor
            .getName());
        assertEquals(descriptor.getFieldValue("role"), "constructor");
        assertEquals(descriptor.getFieldValue("descriptorType"), "operation");
        assertEquals(descriptor.getFields().length, 4);
        commonCheck();
        return result();
    }

    /**
     * Verify default fields of descriptor from ModelMBeanAttributeInfo:
     * name=nameUsedInConstructor, displayName=nameUsedInConstructor and
     * descriptorType=attribute.
     */
    public Result testModelMBeanAttributeInfo() throws Exception {
        Method getter = sampleClass.getMethod("getH", null);
        Method setter = sampleClass.getMethod("setH",
            new Class[] { String.class });
        final String name = "attribute name";
        ModelMBeanAttributeInfo attributeInfo = new ModelMBeanAttributeInfo(
            name, "description", getter, setter);
        descriptor = attributeInfo.getDescriptor();
        //descriptor.getFieldNames();
        assertEquals(descriptor.getFieldValue("name"), name);
        assertEquals(descriptor.getFieldValue("displayName"), name);
        assertEquals(descriptor.getFieldValue("descriptorType"), "attribute");
        assertEquals(descriptor.getFields().length, 3);
        commonCheck();
        return result();
    }

    /**
     * Verify default fields of descriptor from ModelMBeanNotificationInfo:
     * name=nameUsedInConstructor, displayName=nameUsedInConstructor, severity=6
     * and descriptorType=notification.
     */
    public Result testModelMBeanNotificationInfo() throws Exception {
        final String name = "notification name";
        ModelMBeanNotificationInfo notificationInfo = new ModelMBeanNotificationInfo(
            null, name, "description");
        descriptor = notificationInfo.getDescriptor();
        assertEquals(descriptor.getFieldValue("name"), name);
        assertEquals(descriptor.getFieldValue("displayName"), name);
        assertEquals(descriptor.getFieldValue("severity"), "6");
        assertEquals(descriptor.getFieldValue("descriptorType"), "notification");
        assertEquals(descriptor.getFields().length, 4);
        commonCheck();
        return result();
    }

    /**
     * Verify default fields of descriptor from ModelMBeanInfoSupport:
     * name=nameofClassUsedInConstructor, descriptorType=mbean,
     * displayName=nameofClassUsedInConstructor, persistPolicy=never, log=F,
     * visibility=1.
     * <ul>
     * Step by step:
     * <li>Create ModelMBeanInfoSupport object using ModelMBeanInfoSupport.
     * <li>Extract a descriptor from created object using getMBeanDescriptor()
     * method.
     * <li>Verify that all default fields of the descriptor exist using
     * getFieldValue(String inFieldName) method.
     * <li>There are no other fields.
     * <li>Convert the descriptor to xml.
     * <li>Create new descriptor from xml using DescriptorSupport(String inStr)
     * constructor.
     * </ul>
     */
    public Result testModelMBeanInfoSupport() throws Exception {
        ModelMBeanInfoSupport modelMBeanInfoSupport = new ModelMBeanInfoSupport(
            sampleClass.getName(), "description", null, null, null, null);
        descriptor = modelMBeanInfoSupport.getMBeanDescriptor();
        assertEquals(descriptor.getFieldValue("name"), sampleClass.getName());
        assertEquals(descriptor.getFieldValue("descriptorType"), "mbean");
        assertEquals(descriptor.getFieldValue("displayName"), sampleClass
            .getName());
        assertEquals(descriptor.getFieldValue("persistPolicy"), "never");
        assertEquals(descriptor.getFieldValue("log"), "F");
        assertEquals(descriptor.getFieldValue("visibility"), "1");
        assertEquals(descriptor.getFieldValue("export"), "F");
        assertEquals(descriptor.getFields().length, 7);
        commonCheck();
        return result();
    }

    /**
     * TODO: add comments.
     * 
     * @throws Exception
     */
    private void testFieldValue() throws Exception {
        String fieldName = "simple field";
        ArrayList arrayList = new ArrayList();
        String valueOfElement = "value";
        arrayList.add(valueOfElement);
        DescriptorSupport descriptorSupport = new DescriptorSupport(
            new String[] { fieldName }, new Object[] { arrayList });
        assertEquals(((ArrayList)descriptorSupport.getFieldValue(fieldName))
            .get(0), valueOfElement);
        descriptor = descriptorSupport;
        // TODO: following line throws exception
        // descriptorSupport.toXMLString();
        // commonCheck();
    }

    /**
     * Test xml conversions, isValid() and toString() methods of
     * DescriptorSupport at end of each test case.
     */
    public void commonCheck() throws Exception {
        assertTrue(descriptor.isValid());
        DescriptorSupport descriptorSupport = new DescriptorSupport(descriptor
            .getFields());
        // verify DescriptorSupport(String[]) constructor.
        compareArrays(descriptor.getFields(), descriptorSupport.getFields());
        // verify DescriptorSupport(String xmlString) constructor.
        // System.out.println(descriptorSupport.toXMLString());
        DescriptorSupport descriptorSupport2 = new DescriptorSupport(
            descriptorSupport.toXMLString());
        assertTrue(compareArrays(descriptor.getFields(), descriptorSupport2
            .getFields()));
        // verify toString()
        String[] fieldAndValues = trimForStringArrays(descriptorSupport
            .toString().split(","));
        String[] fieldAndValues2 = trimForStringArrays(descriptorSupport
            .getFields());
        assertTrue(compareArrays(fieldAndValues, fieldAndValues2));
    }

    private String[] trimForStringArrays(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].trim();
        }
        return array;
    }

    public static boolean compareDescriptors(Descriptor descriptor,
        Descriptor descriptor2) {
        return compareArrays(descriptor.getFields(), descriptor2.getFields());
    }

    public static boolean compareArrays(Object[] array1, Object[] array2) {
        Arrays.sort(array1);
        Arrays.sort(array2);
        return Arrays.equals(array1, array2);
    }

    public static void printArray(String message, String[] stringArray) {
        System.out.print(message);
        for (int i = 0; i < stringArray.length; i++) {
            System.out.print(stringArray[i] + " ");
        }
        System.out.println();
    }

    // To the end of class: methods are needed for this test.
    public void sayOk() {
        System.out.println("ok");
    }

    public String getH() {
        return "gugu";
    }

    public void setH(String h) {
    }
}