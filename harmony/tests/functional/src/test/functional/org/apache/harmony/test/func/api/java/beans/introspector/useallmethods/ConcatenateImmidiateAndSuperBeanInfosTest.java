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
package org.apache.harmony.test.func.api.java.beans.introspector.useallmethods;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

import org.apache.harmony.test.func.api.java.beans.introspector.LocationException;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Introspector, SimpleBeanInfo, BeanDescriptor, EventSetDescriptor,
 * IntrospectionException, MethodDescriptor, PropertyDescriptor.
 * <p>
 * Bean2 extents Bean1. Bean2 doesn't have real BeanInfo, but Bean1 has
 * Bean1BeanInfo. Verify that Introspector correctly concatenates Bean1BeanInfo
 * and beanInfo which is result of introspection only Bean2. Bean1BeanInfo
 * redefines all methods in SimpleBeanInfo class, except loadImage(String).
 * 
 */
public class ConcatenateImmidiateAndSuperBeanInfosTest extends MultiCase {
    private BeanInfo beanInfo;

    public static void main(String[] args) {
        try {
            System.exit(new ConcatenateImmidiateAndSuperBeanInfosTest()
                .test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify that getBeanDescriptor() method returns Bean2.
     */
    public Result testGetBeanDescriptor() {
        try {
            if (!beanInfo.getBeanDescriptor().getBeanClass()
                .equals(Bean2.class)) {
                throw new LocationException("mistake BeanDescriptor");
            }
            Bean1BeanInfo.verifyException();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }

    }

    /**
     * This is compatibility test. Verify that getDefaultEventIndex() return 1.
     */

    public Result testGetDefaultEventIndex() {
        try {
            if (beanInfo.getDefaultEventIndex() != 1) {
                throw new LocationException("mistake DefaultEventIndex");
            }
            Bean1BeanInfo.verifyException();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * This is compatibility test. Verify that getDefaultEventIndex() return 1.
     */

    public Result testGetDefaultPropertyIndex() {
        try {
            if (beanInfo.getDefaultPropertyIndex() != 0) {
                throw new LocationException("mistake DefaultPropertyIndex");
            }
            Bean1BeanInfo.verifyException();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getIcon(int) method returns null.
     */
    public Result testGetIcon() {
        try {
            assertNull(beanInfo.getIcon(BeanInfo.ICON_MONO_16x16));
            Bean1BeanInfo.verifyException();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getMethodDescriptors() method returns method descriptors
     * which consists of method descriptors returned by getMethodDescriptors()
     * of Bean1BeanInfo and method descriptors introspected in bean2.
     */
    public Result testGetMethodDescriptors() {
        try {
            beanInfo.getPropertyDescriptors();
            MethodDescriptor[] methodDescriptors = beanInfo
                .getMethodDescriptors();
            assertTrue(findMethod("getProperty8", methodDescriptors));
            assertTrue(findMethod("addColorListener", methodDescriptors));
            assertTrue(findMethod("removeColorListener", methodDescriptors));
            assertTrue(findMethod("getProperty9", methodDescriptors));
            assertTrue(findMethod("setProperty9", methodDescriptors));
            assertEquals(methodDescriptors.length, 5);
            Bean1BeanInfo.verifyException();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getPropertyDescriptors() method returns property descriptors
     * which consist of property descriptors returned by
     * getPropertyDescriptors() of Bean1BeanInfo and a property descriptor
     * introspected in bean2.
     */
    public Result testGetPropertyDescriptors() {
        try {
            beanInfo.getPropertyDescriptors();
            PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
            assertTrue(findProperty("prop1", propertyDescriptors));
            assertTrue(findProperty("property9", propertyDescriptors));
            assertEquals(propertyDescriptors.length, 2);
            Bean1BeanInfo.verifyException();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getEventSetDescriptors() method returns event descriptors
     * which consist of event descriptors returned by getEventSetDescriptors()
     * of Bean1BeanInfo and event descriptor introspected in bean2.
     */
    public Result testGetEventSetDescriptors() {
        EventSetDescriptor[] eventSetDescriptors = beanInfo
            .getEventSetDescriptors();
        if (eventSetDescriptors[0].getAddListenerMethod().getName().equals(
            "addFredListener")) {
            if (!eventSetDescriptors[1].getAddListenerMethod().getName()
                .equals("addColorListener")) {
                return failed("not correct eventSetDescriptors. place 1");
            }
        }
        if (eventSetDescriptors[0].getAddListenerMethod().getName().equals(
            "addColorListener")) {
            if (!eventSetDescriptors[1].getAddListenerMethod().getName()
                .equals("addFredListener")) {
                return failed("not correct eventSetDescriptors. place2");
            }
        }
        assertEquals(eventSetDescriptors.length, 2);
        return passed();
    }

    /**
     * Find method by name from methodDescriptors.
     */
    private boolean findMethod(String name, MethodDescriptor[] methodDescriptors) {
        for (int i = 0; i < methodDescriptors.length; i++) {
            if (methodDescriptors[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find property by name from propertyDescriptors.
     */
    private boolean findProperty(String name,
        PropertyDescriptor[] propertyDescriptors) {
        for (int i = 0; i < propertyDescriptors.length; i++) {
            if (propertyDescriptors[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    protected void setUp() throws Exception {
        beanInfo = Introspector.getBeanInfo(Bean2.class);
    }
}