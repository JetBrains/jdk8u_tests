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
package org.apache.harmony.test.func.api.java.beans.persistence.beans;

/*In this class different objects are generated, which then can be encoded.*/

/**
 * Methods in this class generate different beans, which then are transferred to
 * XML.
 * 
 */
public class GeneratingBeans {

    // Beginning testing simple types
    /**
     * Return Boolean object.
     */
    public static Boolean boolean_() {
        return new Boolean(true);
    }

    /**
     * Return Byte object.
     */
    public static Byte byte_() {
        return new Byte((byte)12);
    }

    /**
     * Return Short object.
     */
    public static Short short_() {
        return new Short((short)23);
    }

    /**
     * Return Character object.
     */
    public static Character character_() {
        return new Character('d');
    }

    /**
     * Return Integer object.
     */
    public static Integer integer_() {
        return new Integer(3);
    }

    /**
     * Return Long object.
     */
    public static Long long_() {
        return new Long(3);
    }

    /**
     * Return Float object.
     */
    public static Float float_() {
        return new Float(3.57);
    }

    /**
     * Return Double object.
     */
    public static Double double_() {
        return new Double(3.575);
    }

    /**
     * Return String object.
     */
    public static String string_() {
        return "Abc";
    }

    /**
     * Return Class object.
     */
    public static Class class_() {
        return String.class;
    }

    /**
     * Return null.
     */
    public static Object null_() {
        return null;
    }

    // End testing simple types

    /*
     * Bred. Return empty array of primitive type. Detail: return String array,
     * all 10 elements of which are null.
     */
    static String[] array_string_empty() {
        return new String[10];
    }

    /**
     * Return empty array of object.
     */
    public static Object[] array_object_empty() {
        return new Object[10];
    }

    /*
     * Bred. Return array of primitive type, all elements of the array are
     * defined. Detail: return String array. All 2 elements are set.
     */
    static String[] array_string_allDefined() {
        return new String[] { "3", "aab" };
    }

    /**
     * Return array of primitive type, all elements of the array are defined.
     * Detail: return String array. All 2 elements are set.
     */
    public static int[] array_primitive_allDefined() {
        return new int[] { 2, 4 };
    }

    /**
     * Return array of string type, some elements of the array are defined.
     * Detail: return String array. 2 element are set, other 8 elements are
     * null.
     */
    public static String[] array_string_someDefined() {
        String[] string = new String[10];
        string[3] = "dsg";
        string[4] = "dsf";
        return string;
    }

    /*
     * Bred. Return array of Integer class, some elements of the array are
     * defined. Detail: return Integer array. 2 element are set, other 8
     * elements are null.
     */
    static Integer[] array_interger() {
        Integer[] integers = new Integer[10];
        integers[0] = new Integer(3);
        integers[9] = new Integer(9);
        return integers;
    }

    /*
     * Bean1 object, title property is set.
     */
    static Bean1 object_property_2() {
        Bean1 bean1 = new Bean1();
        bean1.setTitle("Abcdef");
        return bean1;
    }

    /**
     * Return object, 2 of 4 properties of the object are set. Detail: return
     * Bean1 object, title and subTitle properties of the Bean1 object are set.
     */
    public static Bean1 object_properties_noreference() {
        Bean1 bean1 = new Bean1();
        String string = "Abcdef";
        bean1.setTitle(string);
        bean1.setSubTitle(string);
        return bean1;
    }

    /*
     * Object return media mfd Detail:
     */
    static Bean1 object_property_int_array() {
        Bean1 bean1 = new Bean1();
        bean1.setInts(new int[] { 3, 7 });
        return bean1;
    }

    /*
     * Object Detail:
     */
    static Bean1 object_property_integer_array() {
        Bean1 bean1 = new Bean1();
        bean1.setIntegers(new Integer[] { new Integer(7) });
        return bean1;
    }

    /**
     * Return object, values of 2 properties of this object refer to one object.
     * Detail: return Bean2 object so that Bean3 object is set as value title
     * and subTitle properties of Bean2.
     */
    public static Bean2 object_properties_reference() {
        Bean2 bean2 = new Bean2();
        Bean3 type1 = new Bean3();
        bean2.setTitle(type1);
        bean2.setSubTitle(type1);
        return bean2;
    }

    /**
     * Return object, which has cross-reference. Detail: return Bean2 object so
     * that object of Bean3 has property which refers to Bean2 object, which has
     * property which refers to the same former Bean3 object.
     */
    public static Bean4 object_properties_cross_reference() {
        Bean4 bean4 = new Bean4();
        Bean5 bean5 = new Bean5();
        bean5.setBean4(bean4);
        bean4.setBean5(bean5);
        return bean4;
    }

    /**
     * Return object, which has property, value of which is returned object.
     * Detail: return Bean3 object so that value of bean3 property is returned
     * bean.
     */
    public static Bean3 object_properties_reference_to_returnedObject() {
        Bean3 bean3 = new Bean3();
        bean3.setBean3(bean3);
        return bean3;
    }

    /*
     * public static Bean3 object_properties_indexed(){ Bean3 bean3=new Bean3();
     * bean3.setIndexedProperty(4, 1000); return bean3; }
     */

}