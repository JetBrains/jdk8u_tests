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
package org.apache.harmony.test.func.api.javax.management.openmbean;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.OpenMBeanAttributeInfo;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfo;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.SimpleType;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: create real openmbean with attribute and operation and verify that
 * set/get Atrribute and invoke methods work correctly.
 * <p>
 * Under test: OpenMBeanAttributeInfoSupport,
 * OpenMBeanInfoSupport,OpenMBeanOperationInfoSupport,
 * OpenMBeanParameterInfoSupport
 * <ul>
 * Step by step:
 * <li>1.Create OpenMBean: create class which implements DynamicMBean.
 * <li>1.1. Create set/get Atrribute methods which can set/return one
 * attribute. The attribute will be SimpleType.
 * <li>1.2. Create set/get Atrributes methods which set/get attributes one by
 * one.
 * <li>1.3. Create invoke method which can invoke one operation method.
 * Operation method has one parameter.
 * <li>1.4. getMBeanInfo method returns OpenMBeanInfoSupport which describes
 * one attribute in 1.1 step. OpenMBeanInfoSupport also describes operation in
 * step 1.3 using OpenMBeanOperationInfo.
 * <li>2.Create instance of openmbean and register it in server
 * <li>3.Verify that getMBeanInfo of MBeanServer returns OpenMBeanInfo created
 * in 1.4.
 * <li>4.Verify that set/get Atrribute methods of server correctly set and
 * return attribute.
 * <li>5.Verify that invoke method of MBeanServer correctly invoke operation
 * method.
 * </ul>
 * This class is test and OpenMBean simultaneously.
 * 
 */
public class RegistringOpenMBeanTest extends MultiCase implements DynamicMBean {
    private static final String attrName              = "attribute1";
    private static final String operationName         = "operation1";
    private boolean             isGetAttributeInvoked = false;
    private boolean             isSetAttributeInvoked = false;
    private String              attributeValue                 = "Default value";

    public static void main (String[] args) {
        System.exit(new RegistringOpenMBeanTest().test(args));
    }

    public Result test01() throws Exception {
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        ObjectName mapName = new ObjectName("domain", "type", "OpenMBean");
        RegistringOpenMBeanTest managedResourse = this;// new
        // RegistringOpenMBeanTest();
        server.registerMBean(managedResourse, mapName);
        assertEquals(server.getMBeanInfo(mapName),
            new RegistringOpenMBeanTest().getMBeanInfo());
        String attributeValue = "value";
        server.setAttribute(mapName, new Attribute(attrName, attributeValue));
        assertEquals(server.getAttribute(mapName, attrName), attributeValue);
        assertTrue(managedResourse.isGetAttributeInvoked);
        assertTrue(managedResourse.isSetAttributeInvoked);
        String parameter = "parameter";
        assertEquals(server
            .invoke(mapName, operationName, new String[] { parameter },
                new String[] { String.class.getName() }), parameter + "1");
        return result();
    }

    public Object getAttribute(String attribute)
        throws AttributeNotFoundException, MBeanException, ReflectionException {
        isGetAttributeInvoked = true;
        if (attribute.equals(attrName)) {
            return attributeValue;
        }
        log.severe("Attribute " + attribute + " not found.");
        throw new AttributeNotFoundException("Attribute " + attribute
            + " not found.");
    }

    public void setAttribute(Attribute attribute)
        throws AttributeNotFoundException, InvalidAttributeValueException,
        MBeanException, ReflectionException {
        isSetAttributeInvoked = true;
        if (attribute.getName().equals(attrName)) {
            attributeValue = (String)attribute.getValue();
        } else {
            log.severe("Attribute " + attribute + " not found.");
            throw new AttributeNotFoundException("Attribute " + attribute
                + " not found.");
        }
    }

    public AttributeList getAttributes(String[] attributes) {
        AttributeList attributeList = new AttributeList();
        for (int i = 0; i < attributes.length; i++) {
            try {
                attributeList.add(new Attribute(attributes[i],
                    getAttribute(attributes[i])));
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue(false);
            }
        }
        return attributeList;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        AttributeList attributeList = new AttributeList();
        for (int i = 0; i < attributes.size(); i++) {
            try {
                Attribute attribute = (Attribute)attributes.get(i);
                setAttribute(attribute);
                attributeList.add(attribute);
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue(false);
            }
        }
        return attributeList;
    }

    public Object invoke(String actionName, Object[] params, String[] signature)
        throws MBeanException, ReflectionException {
        if (operationName.equals(actionName)) {
            assertEquals(signature.length, 1);
            assertEquals(signature[0], String.class.getName());
            assertEquals(params.length, 1);
            return params[0] + "1";
        }
        throw new MBeanException(null, "actionName " + actionName
            + " not found");
    }

    public MBeanInfo getMBeanInfo() {
        OpenMBeanParameterInfoSupport parameterInfoSupport = new OpenMBeanParameterInfoSupport(
            "par name", "descripttiosdf", SimpleType.STRING);
        OpenMBeanOperationInfo infoSupport = new OpenMBeanOperationInfoSupport(
            operationName, "desciption",
            new OpenMBeanParameterInfo[] { parameterInfoSupport },
            SimpleType.STRING, MBeanOperationInfo.ACTION);
        OpenMBeanAttributeInfoSupport attributeInfo = new OpenMBeanAttributeInfoSupport(
            attrName, "description", SimpleType.STRING, true, true, false);
        OpenMBeanInfoSupport beanInfoSupport = new OpenMBeanInfoSupport(this
            .getClass().getName(), "descriptor",
            new OpenMBeanAttributeInfo[] { attributeInfo }, null,
            new OpenMBeanOperationInfo[] { infoSupport }, null);
        return beanInfoSupport;
    }
}