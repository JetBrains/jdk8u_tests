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

import java.util.Date;

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
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfo;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Test registration of mbean and equals method. Also Test get, equals,
 * containsKey, containsValue methods of CompositeDataSupport class.
 * <p>
 * Under test: CompositeDataSupport, OpenMBeanInfoSupport,
 * OpenMBeanOperationInfoSupport, OpenType, SimpleType classes.
 * <p>
 * For details see documentation for test cases.
 * 
 */
public class CompositeDataSupportTest extends MultiCase implements DynamicMBean {
    private static final String   operationName       = "operationName";
    static final String[] namesOfElements     = new String[] {
        "Name of String element", "Name of Integer element",
        "Name of Boolean element", "Name of Date element" };
    private static final Object   valueOfFirstElement = "Value of String element";

    public static void main (String[] args) {
        System.exit(new CompositeDataSupportTest().test(args));
    }

    /**
     * <ul>
     * Test registration of mbean and equals method.
     * <li>1.Create CompositeType object which contain several element with
     * different simple types.
     * <li>2.Create CompositeDataSupport using created CompositeType.
     * <li>3.Create openmbean which has operation which return CompositeType.
     * Operation does not have parameters. getMBeanInfo returns OpenMBeanInfo
     * which contain only description for the operation.
     * <li>4.Create instance of openmbean and register it in server
     * <li>5.Verify that getMBeanInfo of MBeanServer returns OpenMBeanInfo
     * created in 3. Verify that returned type of operation using equals method
     * of it.
     * <li>6.Invoke operation using invoke method of MBeanServer.
     * <li>7.Compare returned object with object create in 2 step using equals
     * method of it.
     * </ul>
     */
    public Result testRegistration() throws Exception {
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        ObjectName mapName = new ObjectName("domain", "type", "OpenMBean");
        DynamicMBean managedResourse = new CompositeDataSupportTest();
        server.registerMBean(managedResourse, mapName);
        assertEquals(managedResourse.getMBeanInfo(), server
            .getMBeanInfo(mapName));
        assertEquals(server.getMBeanInfo(mapName).getOperations()[0],
            managedResourse.getMBeanInfo().getOperations()[0]);
        assertEquals(server.invoke(mapName, operationName, null, null),
            getCompositeData());
        return result();
    }

    /**
     * <ul>
     * Test get, equals, containsKey, containsValue methods of
     * CompositeDataSupport class.
     * <li>1.Create CompositeType object which contain several element with
     * different simple types.
     * <li>2.Create CompositeDataSupport using created CompositeType.
     * <li>3.Verify that containsKey(name of element in 1st step) method of
     * CompositeDataSupport returns true. Verify that containsKey(some random
     * name) of CompositeDataSupport returns false.
     * <li>4.Invoke get method of CompositeDataSupport with name of element
     * created in 1st step and verify that it return correct value.
     * <li>5.Verify that containsValue method of CompositeDataSupport returns
     * true if value contains in CompositeDataSupport. Verify that containsValue
     * of CompositeDataSupport returns false if key does not contain in
     * CompositeDataSupport.
     * <li>6.Create CompositeType with different type of one element. Verify
     * that CompositeType in this step and CompositeType in 1st step are not
     * equals using equals method of CompositeType.
     * <li>7.Create CompositeDataSupport with other data then in 2nd step.
     * Verify that CompositeDataSupport in this step and CompositeDataSupport in
     * 2nd step are not equals using equals method of CompositeDataSupport.
     * <li>8.Create CompositeDataSupport with value of one element different
     * from decelerated. Verify that exception was thrown.
     * <li>9. Create CompositeDataSupport which has incorrect type of some
     * element.
     * </ul>
     */
    public Result testMethodsOfCompositeData() throws Exception {
        CompositeDataSupport compositeData = getCompositeData();
        assertTrue(compositeData.containsKey(namesOfElements[0]));
        assertFalse(compositeData.containsKey("random name"));
        assertEquals(compositeData.get(namesOfElements[0]), valueOfFirstElement);
        assertTrue(compositeData.containsValue(valueOfFirstElement));
        CompositeType compositeType = getCompositeType();
        assertFalse(compositeData.containsValue("random object"));
        // anotherCompositeType has different type of second element.
        CompositeType anotherCompositeType = new CompositeType("name",
            "description", namesOfElements, new String[] { "item1 description",
                "item3 description", "item3 description", "date" },
            new OpenType[] { SimpleType.STRING, SimpleType.SHORT,
                SimpleType.BOOLEAN, SimpleType.DATE });
        assertNotSame(anotherCompositeType, compositeType);
        // anotherCompositeType has different value of third element.
        CompositeDataSupport anotherCompositeData = new CompositeDataSupport(
            compositeType, namesOfElements, new Object[] { valueOfFirstElement,
                new Integer(5), new Boolean(false), new Date(10) });
        assertNotSame(anotherCompositeData, compositeData);
        try {
            // Reusing anotherCompositeData variable.
            // No element: item6 name.
            anotherCompositeData = new CompositeDataSupport(compositeType,
                namesOfElements, new Object[] { valueOfFirstElement,
                    new Boolean(false), new Integer(5), new Date(10) });
            assertTrue(false);
        } catch (OpenDataException e) {
        }
        try {
            new CompositeDataSupport(compositeType, namesOfElements,
                new Object[] { valueOfFirstElement, new Boolean(false),
                    new Double(5), new Date(10) });
            assertTrue(false);
        } catch (OpenDataException e) {
        }
        return result();
    }

    static CompositeType getCompositeType() throws OpenDataException {
        CompositeType compositeType = new CompositeType("name", "description",
            namesOfElements, new String[] { "item1 description",
                "item3 description", "item3 description", "date description" },
            new OpenType[] { SimpleType.STRING, SimpleType.INTEGER,
                SimpleType.BOOLEAN, SimpleType.DATE });
        return compositeType;
    }

    static CompositeDataSupport getCompositeData() throws OpenDataException {
        // Reorder sequences of elements.
        String[] localNamesOfElements = (String[])namesOfElements.clone();
        localNamesOfElements[1] = namesOfElements[2];
        localNamesOfElements[2] = namesOfElements[1];
        CompositeDataSupport compositeDataSupport = new CompositeDataSupport(
            getCompositeType(), localNamesOfElements, new Object[] {
                valueOfFirstElement, new Boolean(false), new Integer(4),
                new Date(10) });
        return compositeDataSupport;
    }

    public Object getAttribute(String attribute)
        throws AttributeNotFoundException, MBeanException, ReflectionException {
        return null;
    }

    public void setAttribute(Attribute attribute)
        throws AttributeNotFoundException, InvalidAttributeValueException,
        MBeanException, ReflectionException {
    }

    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    public Object invoke(String actionName, Object[] params, String[] signature)
        throws MBeanException, ReflectionException {
        if (actionName.equals(operationName)) {
            try {
                return getCompositeData();
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue(false);
                throw new MBeanException(e);
            }
        }
        throw new MBeanException(null, "actionName " + actionName
            + " can't be found.");
    }

    public MBeanInfo getMBeanInfo() {
        try {
            OpenMBeanOperationInfo infoSupport = new OpenMBeanOperationInfoSupport(
                operationName, "desciption", null, getCompositeType(),
                MBeanOperationInfo.ACTION);
            OpenMBeanInfoSupport beanInfoSupport = new OpenMBeanInfoSupport(
                this.getClass().getName(), "descriptor", null, null,
                new OpenMBeanOperationInfo[] { infoSupport }, null);
            return beanInfoSupport;
        } catch (OpenDataException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        return null;
    }
}