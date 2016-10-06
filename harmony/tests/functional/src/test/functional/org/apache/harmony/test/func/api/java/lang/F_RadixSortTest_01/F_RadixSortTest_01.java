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
 * Created on 26.11.2004
 * Last modification G.Seryakova
 * Last modified on 29.11.2004
 * 
 * 
 * This test sort arrays per radix sort algorithm.
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_RadixSortTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;
import java.lang.reflect.Array;

/**
 * This test sort array of Integer objects per radix sort algorithm and binary
 * representation.
 * 
 */
public class F_RadixSortTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_RadixSortTest_01().test(args));
    }

    public int test() {

        if (task1(1000) != Result.PASS) {
            return fail("");
        }
        if (task2(100) != Result.PASS) {
            return fail("");
        }

        return pass();
    }

    private int task1(int lenght) {
        Object arr, arr0, arr1;
        int maxCount = 0;
        int n, m;
        Integer it;
        String binStr;
        char chr;

        try {
            arr = Array.newInstance(Class.forName("java.lang.Integer"), lenght);
        } catch (ClassNotFoundException e) {
            return fail("Array.newInstance failed");
        }
        arr0 = Array.newInstance(arr.getClass().getComponentType(), lenght);
        arr1 = Array.newInstance(arr.getClass().getComponentType(), lenght);

        for (int i = 0; i < Array.getLength(arr); i++) {
            int num = Math.round((float) (Math.random() * 10000));
            Array.set(arr, i, new Integer(num));
            num = Integer.toBinaryString(num).length();
            if (num > maxCount) {
                maxCount = num;
            }
        }

        for (int j = 1; j <= maxCount; j++) {
            n = m = 0;

            for (int i = 0; i < Array.getLength(arr); i++) {
                it = (Integer) Array.get(arr, i);
                binStr = Integer.toBinaryString(it.intValue());
                try {
                    chr = binStr.charAt(binStr.length() - j);
                } catch (IndexOutOfBoundsException e) {
                    chr = '0';
                }
                if (chr == '0') {
                    System.arraycopy(arr, i, arr0, n, 1);
                    n++;
                } else {
                    System.arraycopy(arr, i, arr1, m, 1);
                    m++;
                }
            }

            System.arraycopy(arr0, 0, arr, 0, n);
            System.arraycopy(arr1, 0, arr, n, m);
        }

        for (int i = 1; i < Array.getLength(arr); i++) {
            it = (Integer) Array.get(arr, i - 1);

            if (it.compareTo((Integer) Array.get(arr, i)) > 0) {
                return fail("Integer array failed");
            }
        }

        return Result.PASS;
    }

    private int task2(int lenght) {
        int nCount;
        byte arr[] = (byte[]) Array.newInstance(byte.class, lenght);
        byte arrC[][] = (byte[][]) Array.newInstance(byte.class, new int[] {
                10, lenght });
        int num[] = new int[10];

        for (int i = 0; i < Array.getLength(arr); i++) {
            Array.setByte(arr, i, (byte) Math.round((Math.random() * 120)));
        }

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 10; i++)
                num[i] = 0;

            for (int i = 0; i < Array.getLength(arr); i++) {
                int n = (Array.getByte(arr, i) / (byte) Math.pow(10, j)) % 10;
                System.arraycopy(arr, i, arrC[n], num[n]++, 1);
            }

            nCount = 0;
            for (int i = 0; i < 10; i++) {
                System.arraycopy(arrC[i], 0, arr, nCount, num[i]);
                nCount += num[i];
            }
        }

        for (int i = 1; i < Array.getLength(arr); i++) {
            if (Array.getByte(arr, i - 1) > Array.getByte(arr, i)) {
                return fail("Byte array failed");
            }
        }

        return Result.PASS;
    }
}