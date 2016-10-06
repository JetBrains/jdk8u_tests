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
import java.beans.Introspector;

import org.apache.harmony.test.func.api.java.beans.introspector.LocationException;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Introspector, SimpleBeanInfo, BeanDescriptor, EventSetDescriptor,
 * IntrospectionException, MethodDescriptor, PropertyDescriptor.
 * <p>
 * Purpose: Verify that methods of BeanInfo returned by Introspector and methods
 * of real BeanInfo, in which all methods redefine, return the same values.
 * 
 */
public class ReturnTheSameAsInRealBeanInfoTest extends MultiCase {
    private BeanInfo beanInfo;

    public static void main(String[] args) {
        try {
            System.exit(new ReturnTheSameAsInRealBeanInfoTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws Exception {
        beanInfo = Introspector.getBeanInfo(Bean1.class);
    }

    /**
     * Verify getBeanDescriptor() method.
     */
    public Result testGetBeanDescriptor() {
        try {
            if (!beanInfo.getBeanDescriptor().getBeanClass().equals(
                ReturnTheSameAsInRealBeanInfoTest.class)) {
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
     * Verify getDefaultEventIndex() method
     */
    public Result testGetDefaultEventIndex() {
        try {
            if (beanInfo.getDefaultEventIndex() != 0) {
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
     * Verify getDefaultPropertyIndex() method.
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
     * Verify, that getIcon(int) method of SimpleBeanInfo returns java.awt.Image
     * object.
     * <p>
     * Step-by-step decoding:
     * <p>
     * Create GIF icon. Redefine getIcon(int) method of SimpleBeanInfo and use
     * loadImage(String) to load icon. Invoke getSource() method on returned
     * image to verify that it is real image.
     */
    public Result testGetIcon() {
        try {

            beanInfo.getIcon(BeanInfo.ICON_MONO_16x16).getSource();
            Bean1BeanInfo.verifyException();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify getMethodDescriptors() method.
     */
    public Result testGetMethodDescriptors() {
        try {
            if (!beanInfo.getMethodDescriptors()[0].getName().equals(
                "getProperty8")) {
                throw new LocationException("mistake MethodDescriptors");
            }
            assertEquals(beanInfo.getMethodDescriptors().length, 1);
            Bean1BeanInfo.verifyException();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify getEventSetDescriptors() method.
     */
    public Result testGetEventSetDescriptors() {
        assertEquals(beanInfo.getEventSetDescriptors()[0]
            .getAddListenerMethod().getName(), "addFredListener");
        assertEquals(beanInfo.getEventSetDescriptors().length, 1);
        return passed();
    }

    /**
     * Verify getPropertyDescriptors() method.
     */
    public Result testGetPropertyDescriptors() {
        assertEquals(beanInfo.getPropertyDescriptors()[0].getName(), "prop1");
        assertEquals(beanInfo.getPropertyDescriptors().length, 1);
        return passed();
    }

}