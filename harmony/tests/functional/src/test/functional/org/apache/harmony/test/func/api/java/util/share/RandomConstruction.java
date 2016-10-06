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
/**
 */
/*
 * Created on 27.09.2005
 */
package org.apache.harmony.test.func.api.java.util.share;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class RandomConstruction {

    public final static Random RANDOM = new Random();

    public final static Comparator COMPARATOR = new StringComparator();

    public static byte[] getArrayOfRandomPositiveByte(int len) {
        byte[] b = new byte[len];
        RANDOM.nextBytes(b);
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (Math.abs(b[i]) + 1);
        }
        return b;
    }

    public static int returnIntInInterval(int begin, int end) {
        return begin + RANDOM.nextInt(end - begin);
    }

    public static int getStringSize() {
        return returnIntInInterval(Consts.MIN_STRING_SIZE,
                Consts.MAX_STRING_SIZE);
    }

    public static int getArraySize() {
        return getArraySize(Consts.MIN_ARRAY_SIZE);
    }

    public static int getArraySize(int min) {
        return getArraySize(min, Consts.MAX_ARRAY_SIZE);
    }

    public static int getArraySize(int min, int max) {
        return returnIntInInterval(min, max);
    }

    public static int getNotSmallArraySize() {
        return getArraySize(Consts.MIN_OF_BIG_ARRAY_SIZE,
                Consts.MAX_OF_BIG_ARRAY_SIZE);
    }

    public static int getBigArraySize() {
        return getArraySize(Consts.MIN_ARRAY_SIZE, Consts.MAX_OF_BIG_ARRAY_SIZE);
    }

    public static Object[] getArrayOfRandomInteger(int number) {
        Object[] b = new Object[number];
        for (int i = 0; i < number; i++) {
            b[i] = new Integer(RANDOM.nextInt());
        }
        return b;
    }

    public static Object[][] rotateArray(Object[][] a) {
        Object[][] a2 = new Object[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                a2[j][i] = a[i][j];
            }
        }
        return a2;
    }

    public static Object[][] getArrayOfRandomStringAndIntegerKey(int number) {
        Object[][] a = new Object[2][number];
        a[0] = RandomConstruction.getArrayOfRandomInteger(number);
        a[1] = RandomConstruction.getArrayOfRandomString(number);
        return rotateArray(a);
    }

    public static Object[][] getArrayOfRandomStringAndIntegerKey() {
        return getArrayOfRandomStringAndIntegerKey(getBigArraySize());
    }

    public static Object[] getBigArrayOfRandomInteger() {
        return getArrayOfRandomInteger(getBigArraySize());
    }

    public static Object[] getNotSmallArrayOfRandomInteger() {
        return getArrayOfRandomInteger(getNotSmallArraySize());
    }

    public static Object getRandomString() {
        return getRandomString(getStringSize());
    }

    public static Object getRandomString(int len) {
        return new String(getArrayOfRandomPositiveByte(len));
    }

    public static Object[] getArrayOfRandomString() {
        return getArrayOfRandomString(getArraySize());
    }

    public static Object[] getNotSmallArrayOfRandomString() {
        return getArrayOfRandomString(getNotSmallArraySize());
    }

    public static Object[] getBigArrayOfRandomString() {
        return getArrayOfRandomString(getBigArraySize());
    }

    public static Object[] getArrayOfRandomString(int sizeOfArray) {
        Object[] as = new Object[sizeOfArray];
        for (int i = 0; i < as.length; i++) {
            as[i] = getRandomString();
        }
        return as;
    }

    public static Object[] getArrayOfRandomString(int sizeOfArray,
            int sizeOfString) {
        Object[] as = new Object[sizeOfArray];
        for (int i = 0; i < as.length; i++) {
            as[i] = getRandomString(sizeOfString);
        }
        return as;
    }

    public static Object[][] getArrayOfArrayOfRandomString() {
        Object[][] as = new Object[Consts.NUMBER_OF_TESTS][];
        for (int i = 0; i < as.length; i++) {
            as[i] = getArrayOfRandomString();
        }
        return as;
    }

    public static void write(Object[] as) {
        for (int j = 0; j < as.length; j++) {
            System.out.print("\"" + as[j] + "\" ");
        }
        System.out.println();
    }

    public static void write(List l) {
        write(l.toArray());
    }

    public static void write(List[] al) {
        for (int j = 0; j < al.length; j++) {
            write(al[j]);
        }
        System.out.println();
    }

    public static void write(Object[][] as) {
        for (int j = 0; j < as.length; j++) {
            write(as[j]);
        }
        System.out.println();
    }

    public static boolean equals(Object o1, Object o2, Comparator c) {
        return c.compare(o1, o2) == 0;
    }

    public static boolean equals(Object o1, Object o2) {
        return equals(o1, o2, COMPARATOR);
    }

    public static boolean equals(Object[] ar1, Object[] ar2) {
        return equals(ar1, ar2, COMPARATOR);
    }

    public static boolean equals(Object[] ar1, Object[] ar2, Comparator c) {
        if (ar1.length != ar2.length) {
            return false;
        } else {
            for (int i = 0; i < ar1.length; i++) {
                if (!equals(ar1[i], ar2[i], c)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean equals(Object[][] ar1, Object[][] ar2) {
        return equals(ar1, ar2, COMPARATOR);
    }

    public static boolean equals(Object[][] ar1, Object[][] ar2, Comparator c) {
        if (ar1.length != ar2.length) {
            return false;
        } else {
            for (int i = 0; i < ar1.length; i++) {
                if (!equals(ar1[i], ar2[i], c)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Object[][] copyOfArray(Object[][] ar) {
        Object[][] ar1 = new Object[ar.length][];
        for (int i = 0; i < ar.length; i++) {
            ar1[i] = (Object[]) ar[i].clone();
        }
        return ar1;
    }

    public static Object[] copyOfArray(Object[] ar) {
        return copyOfArray(ar, ar.length);
    }

    public static Object[] copyOfArray(Object[] ar, int l) {
        Object[] ar1 = new Object[l];
        System.arraycopy(ar, 0, ar1, 0, l);
        return ar1;
    }

    public static Object[][] mySort(Object[][] ar2) {
        return mySort(ar2, COMPARATOR);
    }

    public static Object[][] mySort(Object[][] ar2, Comparator c) {
        Object[][] ar1 = copyOfArray(ar2);
        for (int k = 0; k < ar1.length; k++) {
            Object[] ar = ar1[k];
            for (int i = 0; i < ar.length; i++) {
                for (int j = ar.length - 1; j > i; j--) {
                    if (c.compare(ar[j - 1], ar[j]) > 0) {
                        Object x = ar[j - 1];
                        ar[j - 1] = ar[j];
                        ar[j] = x;
                    }
                }
            }
            ar1[k] = ar;
        }
        return ar1;
    }

    public static int mySearch(Object[] ar, Object a) {
        return mySearch(ar, a, COMPARATOR);
    }

    public static int mySearch(Object[] ar, Object a, Comparator c) {
        for (int i = 0; i < ar.length; i++) {
            if (equals(ar[i], a, c)) {
                return i;
            }
        }
        return -1;
    }

    public static int mySearch(List l, Object a) {
        return mySearch(l, a, COMPARATOR);
    }

    public static int mySearch(List l, Object a, Comparator c) {
        return mySearch(l.toArray(), a, c);
    }

    public static List[] arrayOfArrayToArrayOfList(Object[][] ao) {
        List[] al = new List[ao.length];
        for (int i = 0; i < al.length; i++) {
            al[i] = Arrays.asList(ao[i]);
        }
        return al;
    }

    public static List[] getArrayOfListOfRandomString() {
        return RandomConstruction.arrayOfArrayToArrayOfList(RandomConstruction
                .getArrayOfArrayOfRandomString());
    }

    public static Object[] deleteRepeatFromSortedArray(Object[] ao) {
        Vector v = new Vector();
        for (int i = 0; i < ao.length - 1; i++) {
            if (!equals(ao[i], ao[i + 1])) {
                v.add(ao[i]);
            }
        }
        v.add(ao[ao.length - 1]);
        return v.toArray();
    }

    public static Object[] getSetFromArray(Object[] ao) {
        Object[] ao2 = copyOfArray(ao);
        Arrays.sort(ao2);
        return deleteRepeatFromSortedArray(ao2);
    }

    public static Object[] getSetFromArray(Object[] ao, int l) {
        return getSetFromArray(copyOfArray(ao, l));
    }
}