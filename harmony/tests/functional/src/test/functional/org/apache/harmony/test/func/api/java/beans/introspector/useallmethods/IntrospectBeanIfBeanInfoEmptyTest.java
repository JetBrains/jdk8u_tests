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
 * Bean has BeanInfo. BeanInfo doesn't redefine any methods, i.e. BeanInfo is
 * empty class. Verifies that values returned by methods is result of
 * introspection of Bean class.
 * 
 */
public class IntrospectBeanIfBeanInfoEmptyTest extends MultiCase {
    private BeanInfo beanInfo;

    public static void main(String[] args) {
        try {
            System.exit(new IntrospectBeanIfBeanInfoEmptyTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws Exception {
        beanInfo = Introspector.getBeanInfo(Bean3.class);
    }

    /**
     * Verify that getBeanDescriptor() method returns Bean class.
     */
    public Result testGetBeanDescriptor() {
        try {
            if (!beanInfo.getBeanDescriptor().getBeanClass()
                .equals(Bean3.class)) {
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
     * Verify that getIcon() method returns null.
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
     * Verify that getMethodDescriptors() method returns method descriptors for
     * public methods of bean.
     */
    public Result testGetMethodDescriptors() {
        try {
            beanInfo.getPropertyDescriptors();
            MethodDescriptor[] methodDescriptors = beanInfo
                .getMethodDescriptors();
            assertTrue(findMethod("getProperty8", methodDescriptors));
            assertTrue(findMethod("setProperty8", methodDescriptors));
            assertTrue(findMethod("removeFredListener", methodDescriptors));
            assertTrue(findMethod("addFredListener", methodDescriptors));
            assertTrue(findMethod("ssetI", methodDescriptors));
            assertTrue(findMethod("ggetI", methodDescriptors));
            assertTrue(findMethod("getClass", methodDescriptors));
            assertTrue(findMethod("equals", methodDescriptors));
            assertTrue(findMethod("hashCode", methodDescriptors));
            assertTrue(findMethod("notify", methodDescriptors));
            assertTrue(findMethod("notifyAll", methodDescriptors));
            assertTrue(findMethod("toString", methodDescriptors));

            // 3 wait methods
            assertTrue(findMethod("wait", methodDescriptors));
            assertEquals(methodDescriptors.length, 15);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getEventSetDescriptors() method returns all introspected
     * events of bean.
     */
    public Result testGetEventSetDescriptors() {
        EventSetDescriptor[] eventSetDescriptors = beanInfo
            .getEventSetDescriptors();
        assertEquals(beanInfo.getEventSetDescriptors()[0]
            .getAddListenerMethod().getName(), "addFredListener");
        assertEquals(eventSetDescriptors.length, 1);
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
     * Verify getDefaultPropertyIndex() method returns -1.
     */
    public Result testGetDefaultPropertyIndex() {
        try {
            if (beanInfo.getDefaultPropertyIndex() != -1) {
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
     * Verify getDefaultEventIndex() method returns -1.
     */
    public Result testGetDefaultEventIndex() {
        try {
            if (beanInfo.getDefaultEventIndex() != -1) {
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
     * Verify that getPropertyDescriptors() method returns all introspected
     * properties of bean.
     */
    public Result testGetPropertyDescriptors() {
        beanInfo.getPropertyDescriptors();
        PropertyDescriptor[] propertyDescriptors = beanInfo
            .getPropertyDescriptors();
        assertTrue(findProperty("property8", propertyDescriptors));
        assertTrue(findProperty("class", propertyDescriptors));
        assertEquals(propertyDescriptors.length, 2);
        return passed();
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
}