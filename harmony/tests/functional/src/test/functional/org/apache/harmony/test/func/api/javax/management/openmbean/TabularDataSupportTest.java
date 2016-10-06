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
import java.util.Iterator;
import java.util.List;

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
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.InvalidKeyException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfo;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Test registration of mbean and equals method. Also Test get, equals,
 * containsKey, containsValue methods of TabularDataSupport class.
 * <p>
 * Under test: CompositeDataSupport, OpenMBeanInfoSupport,
 * OpenMBeanOperationInfoSupport, OpenType,
 * SimpleType,TabularDataSupport,TabularType classes.
 * <p>
 * For details see documentation for test cases.
 * 
 */
public class TabularDataSupportTest extends MultiCase implements DynamicMBean {
    private static final String operationName = "operationName";
    private static String[]     indexNames;

    public static void main(String[] args) {
        System.exit(new TabularDataSupportTest().test(args));
    }

    /**
     * <ul>
     * Test registration of mbean and equals method.
     * <li>1. Create CompositeType object which contain several element with
     * different simple types.
     * <li>2. Create TabularType with parameters: above CompositeType and one
     * of name of element used in 1st step as index names.
     * <li>3. Create TabularDataSupport with specified TabularType.
     * <li>4. Create CompositeDataSupport using created CompositeType and put
     * it to TabularDataSupport.
     * <li>5. Create openmbean which has operation which return TabularType.
     * Operation does not have parameters. getMBeanInfo returns OpenMBeanInfo
     * which contain only description for the operation.
     * <li>6. Create instance of openmbean and register it in server.
     * <li>7. Verify that getMBeanInfo of MBeanServer returns OpenMBeanInfo
     * created in 5. Verify that returned type of operation using equals method
     * of it.
     * <li>8. Invoke operation using invoke method of MBeanServer.
     * <li>9. Compare returned TabularDataSupport object with
     * TabularDataSupport object create in 4 step using equals method of it.
     * </ul>
     */
    public Result testRegistration() throws Exception {
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        ObjectName mapName = new ObjectName("domain", "type", "OpenMBean");
        DynamicMBean managedResourse = new TabularDataSupportTest();
        server.registerMBean(managedResourse, mapName);
        assertEquals(managedResourse.getMBeanInfo(), server
            .getMBeanInfo(mapName));
        assertEquals(server.getMBeanInfo(mapName).getOperations()[0],
            managedResourse.getMBeanInfo().getOperations()[0]);
        assertEquals(server.invoke(mapName, operationName, null, null),
            getTabularData());
        return result();
    }

    /**
     * <ul>
     * Test calculateIndex, put, get, equals, remove, containsKey, containsValue
     * methods of TabularDataSupport.
     * <li>1.Create CompositeType object which contain several element with
     * different simple types.
     * <li>2. Create TabularType with parameters: above CompositeType and
     * several (2 or more) of names of elements used in1st step. Denote there
     * names as NAMES.
     * <li>3. Create TabularDataSupport with specified TabularType.
     * <li>4. Create CompositeDataSupport using created CompositeType and put
     * it to TabularDataSupport.
     * <li>5. Create another CompositeDataSupport using created CompositeType
     * and put it to TabularDataSupport.
     * <li>6. Create one another CompositeDataSupport using created
     * CompositeType and put it to TabularDataSupport.
     * <li>7. Verify that calculateIndex method with CompositeDataSupport in
     * 4th step as parameter returns values of elements NAMES in 4th step.
     * <li>8. Verify that containsKey(values of elements NAMES in 5th step)
     * method of CompositeDataSupport returns true.
     * <li>9. Verify that containsKey(some random array) method of
     * CompositeDataSupport returns false.
     * <li>10. Verify that containsValue(CompositeDataSupport used in 6th step)
     * method of CompositeDataSupport returns true.
     * <li>11. Create CompositeDataSupport using created CompositeType with
     * values which differ from values of CompositeDataSupport created in 4,5,6
     * steps. Verify that containsValue(CompositeDataSupport created in this
     * step) method of CompositeDataSupport returns false.
     * <li>12. Verify that get (values of elements NAMES in 4th step) method
     * returns CompositeDataSupport object created in 4th step using equals
     * method.
     * <li>13. Remove CompositeDataSupport created in 4th step using remove
     * (values of elements NAMES in 4th step) method of TabularDataSupport.
     * <li>14. Verify that containsKey(values of elements NAMES in 4th step)
     * method of CompositeDataSupport returns false.
     * </ul>
     */
    public Result testMethodsOfTabularData() throws Exception {
        final CompositeType compositeType = CompositeDataSupportTest
            .getCompositeType();
        TabularData tabularData = getTabularData();
        CompositeData compositeData1 = CompositeDataSupportTest
            .getCompositeData();
        // compositeData1 already added in getTabularType() method.
        CompositeData compositeData2 = new CompositeDataSupport(compositeType,
            CompositeDataSupportTest.namesOfElements, new Object[] {
                "value of string 2", new Integer(1), new Boolean(true),
                new Date(10) });
        tabularData.put(compositeData2);
        CompositeData compositeData3 = new CompositeDataSupport(compositeType,
            CompositeDataSupportTest.namesOfElements, new Object[] {
                "value of string 3", new Integer(1), new Boolean(true),
                new Date(15) });
        tabularData.put(compositeData3);
        {
            Object[] indexOfTabularData = tabularData
                .calculateIndex(compositeData1);
            assertEquals(indexOfTabularData[0], compositeData1
                .get(indexNames[0]));
            assertEquals(indexOfTabularData[1], compositeData1
                .get(indexNames[1]));
        }
        assertTrue(tabularData.containsKey(getKey(compositeData2)));
        assertFalse(tabularData.containsKey(new Object[] {
            compositeData2.get(indexNames[0]), "random value" }));
        assertTrue(tabularData.containsValue(compositeData3));
        {
            CompositeData compositeDataNotExisted = new CompositeDataSupport(
                compositeType, CompositeDataSupportTest.namesOfElements,
                new Object[] { "value of string 4", new Integer(1),
                    new Boolean(true), new Date(15) });
            assertFalse(tabularData.containsValue(compositeDataNotExisted));
        }
        assertEquals(tabularData.get(getKey(compositeData1)), compositeData1);

        // remove compositeData1;
        tabularData.remove(getKey(compositeData1));
        assertFalse(tabularData.containsKey(getKey(compositeData1)));
        tabularData.remove(getKey(compositeData2));
        {
            TabularData tabularData2 = new TabularDataSupport(getTabularType());
            assertNotSame(tabularData, tabularData2);
            tabularData2.put(compositeData3);
            assertEquals(tabularData, tabularData2);
        }
        return result();
    }

    /**
     * Verify that get (values of elements which is not index names) method of
     * CompositeDataSupport throws InvalidKeyException.
     */
    public Result testInvalidKey() throws Exception {
        TabularData tabularData = getTabularData();
        try {
            tabularData.get(new Object[] { CompositeDataSupportTest
                .getCompositeData().get(indexNames[0]) });
            assertTrue(false);
        } catch (InvalidKeyException e) {
        }
        return result();
    }

    /**
     * Verify that TabularType verifies that index names exist in CompositeType.
     */
    public Result testIncorrectIndexNames() {
        try {
            CompositeType compositeType = CompositeDataSupportTest
                .getCompositeType();
            new TabularType("specific type", "description", compositeType,
                new String[] { "random value" });
            assertTrue(false);
        } catch (OpenDataException e) {
        }
        return result();
    }

    /**
     * Verify that TabularData does not accept the same CompositeData.
     */
    public Result testTheSameCompositeData() throws Exception {
        try {
            TabularData tabularData = getTabularData();
            tabularData.put(CompositeDataSupportTest.getCompositeData());
            assertTrue(false);
        } catch (KeyAlreadyExistsException e) {
        }
        return result();
    }

    /**
     * Verify that equals method of TabularType return false if compared
     * TabularType's are different.
     */
    public Result testNotEqualsOfTabularType() throws Exception {
        CompositeType compositeType = CompositeDataSupportTest
            .getCompositeType();
        Iterator names = compositeType.keySet().iterator();
        names.next();
        String[] indexNamesTemp = new String[] { (String)names.next(),
            (String)names.next() };
        TabularType tabularTypeTemp = new TabularType("specific type",
            "description", compositeType, indexNamesTemp);
        assertNotSame(tabularTypeTemp, getTabularType());
        return result();
    }

    public Result testGetIndexNames() throws Exception {
        TabularType tabularType = getTabularType();
        List indexNamesToVerify = tabularType.getIndexNames();
        if (!(indexNamesToVerify.get(0).equals(indexNames[0]) && indexNamesToVerify
            .get(1).equals(indexNames[1]))) {
            throw new Exception("tabularType.getIndexNames() works incorrectly");

        }
        return result();
    }

    private static Object[] getKey(CompositeData compositeData) {
        return new Object[] { compositeData.get(indexNames[0]),
            compositeData.get(indexNames[1]) };
    }

    /**
     * Construct Tabular Type using CompositeType created in
     * CompositeDataSupportTest.
     */
    private static TabularType getTabularType() throws Exception {
        CompositeType compositeType = CompositeDataSupportTest
            .getCompositeType();
        Iterator names = compositeType.keySet().iterator();
        indexNames = new String[] { (String)names.next(), (String)names.next() };
        TabularType tabularType = new TabularType("specific type",
            "description", compositeType, indexNames);
        return tabularType;
    }

    private static TabularData getTabularData() throws Exception {
        TabularData tabularData = new TabularDataSupport(getTabularType());
        tabularData.put(CompositeDataSupportTest.getCompositeData());
        return tabularData;
    }

    // All below methods implements methods of DynamicMBean
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
                return getTabularData();
            } catch (Exception e) {
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
                operationName, "desciption", null, getTabularType(),
                MBeanOperationInfo.ACTION);
            OpenMBeanInfoSupport beanInfoSupport = new OpenMBeanInfoSupport(
                this.getClass().getName(), "descriptor", null, null,
                new OpenMBeanOperationInfo[] { infoSupport }, null);
            return beanInfoSupport;
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        return null;
    }
}