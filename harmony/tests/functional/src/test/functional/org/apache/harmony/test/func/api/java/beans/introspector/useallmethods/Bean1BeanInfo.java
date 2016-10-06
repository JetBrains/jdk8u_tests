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

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * BeanInfo for Bean1 class.
 * 
 */
public class Bean1BeanInfo extends SimpleBeanInfo {

    public Bean1BeanInfo() {
        System.out.println("point 3");
    }
    /**
     * If a exception is thrown in any methods of this class, this exception
     * have to be written to this variables.
     */
    private static Exception exception;

    /**
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _property1 = new PropertyDescriptor("prop1",
                Bean1.class, "ggetI", "ssetI");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _property1 };
            return pds;
        } catch (IntrospectionException exception) {
            exception.printStackTrace();
            this.exception = exception;
            return null;
        }
    }

    /**
     * @see java.beans.BeanInfo#getBeanDescriptor()
     */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(ReturnTheSameAsInRealBeanInfoTest.class);
    }

    /**
     * @see java.beans.BeanInfo#getDefaultEventIndex()
     */
    public int getDefaultEventIndex() {
        return 0;
    }

    /**
     * @see java.beans.BeanInfo#getDefaultPropertyIndex()
     */
    public int getDefaultPropertyIndex() {
        return 0;
    }

    /**
     * @see java.beans.BeanInfo#getEventSetDescriptors()
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
        EventSetDescriptor event;
        try {
            event = new EventSetDescriptor(Bean1.class, "fred",
                FredListener.class, "fireFredEvent");
            return new EventSetDescriptor[] { event };
        } catch (IntrospectionException e) {
            exception = e;
            e.printStackTrace();
        }
        return null;
    }

    public Image getIcon(int i) {
        if (i == BeanInfo.ICON_MONO_16x16) {
            String fileName ="3.gif";
            System.out.println("path to image: " + fileName);
            return loadImage(fileName);
        }
        return null;
    }

    /**
     * @see java.beans.BeanInfo#getMethodDescriptors()
     */
    public MethodDescriptor[] getMethodDescriptors() {
        try {
            return new MethodDescriptor[] { new MethodDescriptor(Bean1.class
                .getDeclaredMethod("getProperty8", null)) };
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verify that an exception wasn't threw in any methods of this class.
     * 
     * @throws Exception
     */
    public static void verifyException() throws Exception {
        if (exception != null) {
            throw exception;
        }
    }
}