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
 * Created on 10.12.2004 
 * Last modification G.Seryakova 
 * Last modified on 10.12.2004 
 * 
 * Tests hashCode() for Integer, Long, String, Object. 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_HashCodeTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.math.BigInteger;

class SimpleHashTable {
    private static Item DISABLED = new Item(null, null);
    public int          N        = 0;
    private int         size     = 0;
    private Item        table[];

    static class Item {
        Object key;
        Object value;

        Item(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    SimpleHashTable(int N) {
        this.N = N;
        table = new Item[N];
    }

    void put(Object key, Object value) {
        int ind = findIndex(key);
        table[ind] = new Item(key, value);
        size++;
    }

    Object get(Object key) {
        int ind = findKeyIndex(key);
        if (ind != -1) {
            return table[ind].value;
        } else {
            return null;
        }
    }

    //        void remove(Object key) {
    //        int ind = findKeyIndex(key);
    //        if (ind != -1) table[ind] = DISABLED;
    //        size--;
    //    }
    //    
    void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    private int getHashValue(Object key, int shift) {
        BigInteger value = BigInteger.valueOf(key.hashCode() + shift);
        value = value.mod(BigInteger.valueOf(N));
        return value.intValue();
    }

    private int findIndex(Object key) {
        int ind = findKeyIndex(key);
        if (ind != -1) {
            return ind;
        }
        int shift = 0;
        while (true) {
            ind = getHashValue(key, shift);
            if (table[ind] == null || table[ind] == DISABLED) {
                return ind;
            } else {
                shift++;
            }
        }
    }

    private int findKeyIndex(Object key) {
        int shift = 0;
        while (true) {
            int ind = getHashValue(key, shift);
            if (table[ind] == null) {
                return -1;
            } else if (table[ind].key.equals(key)) {
                return ind;
            } else {
                shift++;
            }
        }
    }
}

/**
 * Tests hashCode() for Integer, Long, String, Object.
 * 
 */

public class F_HashCodeTest_01 extends ScenarioTest {
    private SimpleHashTable table;

    class TestObject {
        long i;

        TestObject(long I) {
            i = I;
        }
    }
    
    class MyObject implements Cloneable {
        int sn;
        
        MyObject(int n) {
            sn = n;
        }
        
        public int hashCode() {
            return System.identityHashCode(this);
        }
    }

    public static void main(String[] args) {
        System.exit(new F_HashCodeTest_01().test(args));
    }

    public int test() {
        table = new SimpleHashTable(2000);

        if (!testObject()) {
            return fail("Fail for Object");
        }
        if (!testMyObject()) {
            return fail("Fail for MyObject");
        }
        if (!testInteger()) {
            return fail("Fail for Integer");
        }
        if (!testLong()) {
            return fail("Fail for Long");
        }
        if (!testString()) {
            return fail("Fail for String");
        }
        if (!testByte()) {
            return fail("Fail for Byte");
        }
        if (!testShort()) {
            return fail("Fail for Short");
        }
        if (!testFloat()) {
            return fail("Fail for Float");
        }
        if (!testDouble()) {
            return fail("Fail for Double");
        }

        return pass();
    }

    private boolean testObject() {
        TestObject arr[] = new TestObject[1000];
        String strs[] = new String[1000];
        int num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = (int)Math.round(Math.random() * 100000);
            arr[i] = new TestObject(num);
            strs[i] = Integer.toString(num);
            table.put(arr[i], strs[i]);
        }

        for (int i = 0; i < arr.length; i++) {
            if (!strs[i].equals(table.get(arr[i]))) {
                return false;
            }
        }

        return true;
    }
    private boolean testMyObject() {
        MyObject arr[] = new MyObject[1000];
        String strs[] = new String[1000];
        int num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = (int)Math.round(Math.random() * 100000);
            arr[i] = new MyObject(num);
            strs[i] = Integer.toString(num);
            table.put(arr[i], strs[i]);
        }

        for (int i = 0; i < arr.length; i++) {
            if (!strs[i].equals(table.get(arr[i]))) {
                return false;
            }
        }

        return true;
    }

    private boolean testInteger() {
        int arr[] = new int[1000];
        int num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = Math.round((float)Math.random() * 100000);
            arr[i] = num;
            table.put(new Integer(num), String.valueOf(num));
        }

        for (int i = 0; i < arr.length; i++) {
            if (!String.valueOf(arr[i]).equals(table.get(new Integer(arr[i])))) {
                return false;
            }
        }

        return true;
    }
    
    private boolean testByte() {
        byte arr[] = new byte[1000];
        byte num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = (byte)Math.round((float)Math.random() * 100);
            arr[i] = num;
            table.put(new Byte(num), String.valueOf(num));
        }

        for (int i = 0; i < arr.length; i++) {
            if (!String.valueOf(arr[i]).equals(table.get(new Byte(arr[i])))) {
                return false;
            }
        }

        return true;
    }
    
    private boolean testShort() {
        short arr[] = new short[1000];
        short num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = (short)Math.round((float)Math.random() * 100);
            arr[i] = num;
            table.put(new Short(num), String.valueOf(num));
        }

        for (int i = 0; i < arr.length; i++) {
            if (!String.valueOf(arr[i]).equals(table.get(new Short(arr[i])))) {
                return false;
            }
        }

        return true;
    }
    
    private boolean testDouble() {
        double arr[] = new double[1000];
        double num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = Math.random() * 10000;
            arr[i] = num;
            table.put(new Double(num), String.valueOf(num));
        }

        for (int i = 0; i < arr.length; i++) {
            if (!String.valueOf(arr[i]).equals(table.get(new Double(arr[i])))) {
                return false;
            }
        }

        return true;
    }
    
    private boolean testFloat() {
        float arr[] = new float[1000];
        float num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = (float)Math.random() * 10000;
            arr[i] = num;
            table.put(new Float(num), String.valueOf(num));
        }

        for (int i = 0; i < arr.length; i++) {
            if (!String.valueOf(arr[i]).equals(table.get(new Float(arr[i])))) {
                return false;
            }
        }

        return true;
    }

    private boolean testLong() {
        long arr[] = new long[1000];
        long num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = Math.round(Math.random() * 100000);
            arr[i] = num;
            table.put(new Long(num), String.valueOf(num));
        }

        for (int i = 0; i < arr.length; i++) {
            if (!String.valueOf(arr[i]).equals(table.get(new Long(arr[i])))) {
                return false;
            }
        }

        return true;
    }

    private boolean testString() {
        long arr[] = new long[1000];
        long num;

        table.clear();

        for (int i = 0; i < arr.length; i++) {
            num = Math.round(Math.random() * 100000);
            arr[i] = num;
            table.put(String.valueOf(num), "-" + String.valueOf(num) + "-");
        }

        for (int i = 0; i < arr.length; i++) {
            if (!("-" + String.valueOf(arr[i]) + "-").equals(table.get(String
                .valueOf(arr[i])))) {
                return false;
            }
        }

        return true;
    }
}

