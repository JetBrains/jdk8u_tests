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
package org.apache.harmony.test.func.api.java.beans.introspector.surveymethods.property;

import java.beans.IndexedPropertyDescriptor;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: IndexedPropertyDescriptor, Introspector, PropertyDescriptor,
 * FeatureDescriptor.
 * <p>
 * Purpose: verify that Introspector finds properties and indexed properties in
 * a bean.
 * <p>
 * 
 * @see java.beans.BeanInfo#getPropertyDescriptors()
 * @see java.beans.PropertyDescriptor
 * @see java.beans.IndexedPropertyDescriptor
 */
public class PropertyDescriptorTest extends MultiCase {
    private PropertyDescriptor[] propertyDescriptors;

    public static void main(String[] args) {
        try {
            System.exit(new PropertyDescriptorTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws Exception {
        propertyDescriptors = Introspector.getBeanInfo(Bean1.class)
            .getPropertyDescriptors();
    }

    /**
     * Verify class type of property.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter and getter methods of string property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that type of property is string.
     */
    public Result testComplexTypeOfProperty() {
        try {
            int i = findProperty("prop7");
            if (!String.class.equals(propertyDescriptors[i].getPropertyType())) {
                throw new Exception("mistake in type of propperty");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify primitive type of property.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter and getter methods of boolean property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that type of property is boolean.
     */
    public Result testPrimitiveTypeOfProperty() {
        try {
            int i = findProperty("prop1");
            if (!boolean.class.equals(propertyDescriptors[i].getPropertyType())) {
                throw new Exception("mistake in type of propperty");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify, that getReadMethod() method returns first read method of boolean
     * property, which begins with "is".
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has two getter method of boolean property, which
     * begins with "is" and with "get".
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that getReadMethod() of PropertyDescriptor returns read
     * method of boolean property which begins with "is".
     */
    public Result testReadMethodOfBooleanPropertyBeginsWithIs() {
        try {
            int i = findProperty("prop1");
            if (!propertyDescriptors[i].getReadMethod().getName().equals(
                "isProp1")) {
                throw new Exception("PropertyDescriptor.getReadMethod");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify, that Introspector sets read and write methods of one
     * PropertyDescriptors.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has getter and setter methods of property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that getWriteMethod() of PropertyDescriptor returns write
     * method of property.
     * <li>Verify that getReadMethod() of PropertyDescriptor returns read
     * method of property.
     */
    public Result testCombineReadAndWriteMethods() {
        try {
            int i = findProperty("prop1");
            if (!propertyDescriptors[i].getWriteMethod().getName().equals(
                "setProp1")) {
                throw new Exception("PropertyDescriptor.setWriteMethod");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify, that Introspector method returns a read method of boolean
     * property, which begins with "get".
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has getter method of boolean property, which
     * begins with "get".
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that getReadMethod() of PropertyDescriptor returns read
     * method of boolean property.
     */
    public Result testReadMethodOfBooleanPropertyBeginsWithGet() {
        try {
            int i = findProperty("prop10");
            assertEquals(propertyDescriptors[i].getReadMethod().getName(),
                "getProp10");
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify, that Introspector finds property, which name consists of
     * uppercase letters.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has getter method of property, which name is
     * "get"+ uppercase letters.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that getReadMethod() of PropertyDescriptor returns read
     * method of property.
     */
    public Result testUppercaseProperty() {
        try {
            int i = findProperty("URL");
            assertEquals(propertyDescriptors[i].getReadMethod().getName(),
                "getURL");
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that PropertyDescriptor.equals(..) method returns true if 2
     * PropertyDescriptor's are the same.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter and getter method of property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for given property by name.
     * <li>Invoke equals method from PropertyDescriptor, where the same
     * PropertyDescriptor is parameter of equals method.
     * <li>Verify that returned value is true.
     */
    public Result testEquals() {
        try {
            int i = findProperty("prop1");
            if (!propertyDescriptors[i].equals(propertyDescriptors[i])) {
                throw new Exception("PropertyDescriptor.equals");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that Introspector attaches indexed read method to
     * PropertyDescriptor.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter, getter, indexed setter, indexed getter
     * methods of property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that found PropertyDescriptor is IndexedPropertyDescriptor.
     * <li>Verify that getIndexedReadMethod() of IndexedPropertyDescriptor
     * returns indexed read method of the indexed property.
     */
    public Result testGetIndexedReadMethod() {
        try {
            int i = findProperty("prop2");
            IndexedPropertyDescriptor indexProp = (IndexedPropertyDescriptor)propertyDescriptors[i];
            assertEquals(indexProp.getIndexedReadMethod(), Bean1.class
                .getMethod("getProp2", new Class[] { int.class }));
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that Introspector attaches indexed write method to
     * PropertyDescriptor.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter, getter, indexed setter, indexed getter
     * methods of property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that found PropertyDescriptor is IndexedPropertyDescriptor.
     * <li>Verify that getIndexedWriteMethod() of IndexedPropertyDescriptor
     * returns indexed write method of indexed property.
     */
    public Result testGetIndexedWriteMethod() {
        try {
            int i = findProperty("prop2");
            IndexedPropertyDescriptor indexProp = (IndexedPropertyDescriptor)propertyDescriptors[i];
            assertEquals(indexProp.getIndexedWriteMethod(), Bean1.class
                .getMethod("setProp2", new Class[] { int.class, String.class }));
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getIndexedWriteMethod() of IndexedPropertyDescriptor returns
     * indexed write method of indexed property, if there is only indexed write
     * method.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has only indexed setter methods of property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that found PropertyDescriptor is IndexedPropertyDescriptor.
     * <li>Verify that getIndexedWriteMethod() of IndexedPropertyDescriptor
     * returns indexed write method of indexed property.
     */
    public Result testOnlyIndexedSetter() {
        try {
            int i = findProperty("prop9");
            IndexedPropertyDescriptor indexProp = (IndexedPropertyDescriptor)propertyDescriptors[i];
            assertEquals(indexProp.getIndexedWriteMethod(), Bean1.class
                .getMethod("setProp9", new Class[] { int.class, String.class }));
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getIndexedReadMethod() of IndexedPropertyDescriptor returns
     * indexed read method of indexed property, if there is only indexed read
     * method.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has only indexed getter method of property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that the PropertyDescriptor is IndexedPropertyDescriptor.
     * <li>Verify that getIndexedReadMethod() of IndexedPropertyDescriptor
     * returns indexed read method of indexed property.
     */
    public Result testOnlyIndexedGetter() {
        try {
            int i = findProperty("prop8");
            IndexedPropertyDescriptor indexProp = (IndexedPropertyDescriptor)propertyDescriptors[i];
            assertEquals(indexProp.getIndexedReadMethod(), Bean1.class
                .getMethod("getProp8", new Class[] { int.class }));
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Disregard this test case.
     * <p>
     * Verify type of indexed property.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter, getter, indexed setter, indexed getter
     * methods of string property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that found PropertyDescriptor is IndexedPropertyDescriptor.
     * <li>Verify that type of indexed property is string.
     */
    public Result testTypeOfIndexedProperty() {
        try {
            int i = findProperty("prop2");
            IndexedPropertyDescriptor indexProp = (IndexedPropertyDescriptor)propertyDescriptors[i];
            if (!indexProp.getIndexedPropertyType().equals(String.class)) {
                throw new Exception(
                    "IndexedPropertyDescriptor.getIndexedPropertyType()");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify quantity of found properties.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter and getter methods of property#1;
     * setter, getter, indexed setter, indexed getter methods of indexed
     * property#2; only getter methods of property#3; only setter method of
     * property#4.
     * <li>Introspect bean.
     * <li>Verify that quantity of found properties is 5. (One property from
     * Object class).
     */
    public Result testQuantityOfProperties() {
        if (propertyDescriptors.length != 10) {
            return failed("Quantity of properties is not 10.");
        }
        return passed();
    }

    /**
     * Verify, that Introspector finds a property with only getter method.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has only getter method of a property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that getReadMethod() of PropertyDescriptor returns read
     * method of the property.
     */
    public Result testOnlyGetterMethod() {
        int i = findProperty("prop3");
        assertEquals(propertyDescriptors[i].getReadMethod().getName(),
            "getProp3");
        return passed();
    }

    /**
     * Verify that getReadMethod() returns read method of indexed property.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter, getter, indexed setter, indexed getter
     * methods of property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that getReadMethod() of IndexedPropertyDescriptor returns
     * indexed read method of the indexed property.
     */
    public Result testGetReadMethodOfIndexedPropertyDescriptor()
        throws Exception {
        int i = findProperty("prop2");
        assertEquals(propertyDescriptors[i].getReadMethod(), Bean1.class
            .getMethod("getProp2", null));
        return passed();
    }

    /**
     * Verify that getWriteMethod() returns write method of indexed property.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has setter, getter, indexed setter, indexed getter
     * methods of property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that getWriteMethod() of IndexedPropertyDescriptor returns
     * indexed write method of the indexed property.
     */
    public Result testGetWriteMethodOfIndexedPropertyDescriptor()
        throws Exception {
        int i = findProperty("prop2");
        assertEquals(propertyDescriptors[i].getWriteMethod(), Bean1.class
            .getMethod("setProp2", new Class[] { String[].class }));
        return passed();
    }

    /**
     * Verify, that Introspector finds a property with only setter method.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has only setter method of a property.
     * <li>Introspect bean.
     * <li>Find PropertyDescriptor for the property by name of property.
     * <li>Verify that getWriteMethod() of PropertyDescriptor returns write
     * method of the property.
     */
    public Result testOnlySetterMethod() {
        int i = findProperty("prop4");
        assertEquals(propertyDescriptors[i].getWriteMethod().getName(),
            "setProp4");
        return passed();
    }

    /**
     * Verify, that Introspector doesn't find property which setter method
     * returns a value.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has public setter method which returns value.
     * <li>Introspect bean.
     * <li>Verify that there isn't given property in list of found properties.
     */
    public Result testWrongSetterMethod() {
        int i = findProperty("prop5");
        assertEquals(i, -1);
        return passed();
    }

    /**
     * Verify, that Introspector doesn't find property which getter method has a
     * parameter.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create bean which has public getter method, which has a parameter.
     * <li>Introspect bean.
     * <li>Verify that there isn't given property in list of found properties.
     */
    public Result testWrongGetterMethod() {
        int i = findProperty("prop6");
        assertEquals(i, -1);
        return passed();
    }

    /**
     * Find property with certain name.
     * 
     * @param name is name of property.
     * @return index of property in array propertyDescriptors.
     */
    private int findProperty(String name) {
        for (int i = 0; i < propertyDescriptors.length; i++) {
            if (propertyDescriptors[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}