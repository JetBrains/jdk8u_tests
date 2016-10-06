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
/*
 * Created on 22.02.2005
 * Last modification G.Seryakova
 * Last modified on 22.02.2005
 * 
 * 
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ArrayTest_01;

import java.lang.reflect.Array;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_ArrayTest_01 extends ScenarioTest {
    String resString = "";

    public static void main(String[] args) {
        System.exit(new F_ArrayTest_01().test(args));
    }
    
    class MyObject {
        long num;
        
        MyObject(long n) {
            num = n;
        }
    }

    public int test() {
        if (!checkBoolean()) {
            return fail("Fail for boolean: " + resString);
        }
        if (!checkByte()) {
            return fail("Fail for byte: " + resString);
        }
        if (!checkChar()) {
            return fail("Fail for char: " + resString);
        }
        if (!checkDouble()) {
            return fail("Fail for double: " + resString);
        }
        if (!checkFloat()) {
            return fail("Fail for float: " + resString);
        }
        if (!checkInt()) {
            return fail("Fail for int: " + resString);
        }
        if (!checkLong()) {
            return fail("Fail for long: " + resString);
        }
        if (!checkShort()) {
            return fail("Fail for short: " + resString);
        }
        if (!checkObject()) {
            return fail("Fail for MyObject: " + resString);
        }
                
        return pass();
    }
    
    private boolean checkBoolean() {
        boolean arr[] = new boolean[10];
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (Math.round(Math.random()) == 0) {
                arr[i] = false;
            } else {
                arr[i] = true;
            }
        }
        
        boolean arrCopy[] = (boolean[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i] != arrCopy[i]) {
                log.info(arr[i] + " and " + arrCopy[i]);
                resString = resString + "Array element not equal for boolean(" + arr[i] + " and " + arrCopy[i] + ");";
                return false;
            } 
        }
        
        return true;
    }
    
    private boolean checkByte() {
        byte arr[] = new byte[100];
        for (int i = 0; i < Array.getLength(arr); i++) {
            arr[i] = (byte) Math.round((Math.random() * Byte.MAX_VALUE));
        }
        
        byte arrCopy[] = (byte[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i] != arrCopy[i]) {
                resString = resString + "Array element not equal for byte(" + arr[i] + " and " + arrCopy[i] + ");";
                return false;
            } 
        }
        
        return true;
    }
    
    private boolean checkChar() {
        char arr[] = new char[100];
        for (int i = 0; i < Array.getLength(arr); i++) {
            arr[i] = (char)Math.round((Math.random() * 256));
        }
        
        char arrCopy[] = (char[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i] != arrCopy[i]) {
                resString = resString + "Array element not equal for char(" + arr[i] + " and " + arrCopy[i] + ");";
                return false;
            } 
        }
        
        return true;
    }
    
    private boolean checkDouble() {
        double arr[] = new double[100];
        for (int i = 0; i < Array.getLength(arr); i++) {
            arr[i] = (Math.random() * 1000);
        }
        
        double arrCopy[] = (double[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i] != arrCopy[i]) {
                resString = resString + "Array element not equal for double(" + arr[i] + " and " + arrCopy[i] + ");";
                return false;
            } 
        }        
        
        return true;
    }
    
    private boolean checkFloat() {
        float arr[] = new float[100];
        for (int i = 0; i < Array.getLength(arr); i++) {
            arr[i] = (float)(Math.random() * 1000);
        }
        
        float arrCopy[] = (float[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i] != arrCopy[i]) {
                resString = resString + "Array element not equal for float(" + arr[i] + " and " + arrCopy[i] + ");";
                return false;
            } 
        }
        
        return true;
    }
    
    private boolean checkInt() {
        int arr[] = new int[100];
        for (int i = 0; i < Array.getLength(arr); i++) {
            arr[i] = (int)Math.round((Math.random() * Integer.MAX_VALUE));
        }
        
        int arrCopy[] = (int[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i] != arrCopy[i]) {
                resString = resString + "Array element not equal for int(" + arr[i] + " and " + arrCopy[i] + ");";
                return false;
            } 
        }
                
        return true;
    }
    
    private boolean checkLong() {
        long arr[] = new long[100];
        for (int i = 0; i < Array.getLength(arr); i++) {
            arr[i] = Math.round((Math.random() * Long.MAX_VALUE));
        }
        
        long arrCopy[] = (long[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i] != arrCopy[i]) {
                resString = resString + "Array element not equal for long(" + arr[i] + " and " + arrCopy[i] + ");";
                return false;
            } 
        }
        
        return true;
    }
    
    private boolean checkShort() {
        short arr[] = new short[100];
        for (int i = 0; i < Array.getLength(arr); i++) {
            arr[i] = (short)Math.round((Math.random() * Short.MAX_VALUE));
        }
        
        short arrCopy[] = (short[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i] != arrCopy[i]) {
                resString = resString + "Array element not equal for short(" + arr[i] + " and " + arrCopy[i] + ");";
                return false;
            } 
        }
        
        return true;
    }
    
    private boolean checkObject() {
        MyObject arr[] = new MyObject[100];
        for (int i = 0; i < Array.getLength(arr); i++) {
            arr[i] = new MyObject(Math.round((Math.random() * Long.MAX_VALUE)));
        }
        
        MyObject arrCopy[] = (MyObject[])getCopy(arr);
        
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (arr[i].num != arrCopy[i].num) {
                resString = resString + "Array element not equal for MyObject(" + arr[i].num + " and " + arrCopy[i].num + ");";
                return false;
            } 
        }
        
        return true;
    }
    
    private Object getCopy(Object arr) {
        Object res = null;
        Class type = arr.getClass().getComponentType();
        res = Array.newInstance(type, Array.getLength(arr));
        
        if (type.isPrimitive()) {
            if (type.getName().equals("boolean")) {
                    for (int i = 0; i < Array.getLength(arr); i++) {
                        Array.setBoolean(res, i, Array.getBoolean(arr, i));
                    }
            } else if (type.getName().equals("byte")) {
                for (int i = 0; i < Array.getLength(arr); i++) {
                    Array.setByte(res, i, Array.getByte(arr, i));
                }
            } else if (type.getName().equals("char")) {
                for (int i = 0; i < Array.getLength(arr); i++) {
                    Array.setChar(res, i, Array.getChar(arr, i));
                }
            } else if (type.getName().equals("double")) {
                for (int i = 0; i < Array.getLength(arr); i++) {
                    Array.setDouble(res, i, Array.getDouble(arr, i));
                }
            } else if (type.getName().equals("float")) {
                for (int i = 0; i < Array.getLength(arr); i++) {
                    Array.setFloat(res, i, Array.getFloat(arr, i));
                }
            } else if (type.getName().equals("int")) {
                for (int i = 0; i < Array.getLength(arr); i++) {
                    Array.setInt(res, i, Array.getInt(arr, i));
                }
            } else if (type.getName().equals("long")) {
                for (int i = 0; i < Array.getLength(arr); i++) {
                    Array.setLong(res, i, Array.getLong(arr, i));
                }
            } else if (type.getName().equals("short")) {
                for (int i = 0; i < Array.getLength(arr); i++) {
                    Array.setShort(res, i, Array.getShort(arr, i));
                }
            } 
        } else {
            for (int i = 0; i < Array.getLength(arr); i++) {
                Array.set(res, i, Array.get(arr, i));
            }
        }
        
        return res;
    }
}
