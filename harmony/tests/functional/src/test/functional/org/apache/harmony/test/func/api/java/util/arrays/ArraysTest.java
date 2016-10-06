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
package org.apache.harmony.test.func.api.java.util.arrays;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.harmony.test.func.api.java.util.share.RandomConstruction;
import org.apache.harmony.test.func.api.java.util.share.StringComparator;
import org.apache.harmony.test.func.api.java.util.share.Consts;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class ArraysTest extends MultiCase {

    public static void main(String[] args) {
        System.exit(new ArraysTest().test(args));
    }

    Comparator c = new StringComparator();

    public Result testBinarySearch() throws Throwable {

        Result res = checkSearch(RandomConstruction
                .copyOfArray(Consts.STRING_ARRAY), Consts.STRING, c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        res = checkSearch(RandomConstruction.copyOfArray(Consts.STRING_ARRAY),
                Consts.STRING2, c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        Object[][] testArray = RandomConstruction
                .getArrayOfArrayOfRandomString();
        res = checkSearch(testArray, RandomConstruction.getArrayOfRandomString(
                testArray.length, 1), c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        testArray = RandomConstruction.getArrayOfArrayOfRandomString();
        res = checkSearch(testArray, RandomConstruction.getArrayOfRandomString(
                testArray.length, 2), c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        return passed();
    }

    public Result checkSearch(Object[][] ar, Object[] a, Comparator c)
            throws Throwable {
        for (int i = 0; i < ar.length; i++) {
            Object[] testArray = ar[i];
            Arrays.sort(testArray, c);
            int bs = Arrays.binarySearch(testArray, a[i], c);
            if (bs < 0) {
                if (RandomConstruction.mySearch(testArray, a[i], c) > 0) {
                    return failed("Arrays.binarySearch(...) not found element in array");
                }
            } else {
                if (!RandomConstruction.equals(testArray[bs], a[i], c)) {
                    return failed("Arrays.binarySearch(...) found element in array, but not that we want");
                }
            }

        }

        return passed();
    }

    public Result checkSorted(Object[][] ar1, Object[][] ar2, Comparator c)
            throws Throwable {
        for (int i = 0; i < ar1.length; i++) {
            Object[] testArray = ar1[i];
            Arrays.sort(testArray, c);
            if (!RandomConstruction.equals(testArray, ar2[i], c)) {
                return failed("Arrays.sort(...) returned not sorted array");
            }
        }

        return passed();
    }

    public Result testSort() throws Throwable {
        Result res = checkSorted(RandomConstruction
                .copyOfArray(Consts.STRING_ARRAY), RandomConstruction.mySort(
                Consts.STRING_ARRAY, c), c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        res = checkSorted(RandomConstruction
                .copyOfArray(Consts.STRING_TWO_DIMENSIONAL_ARRAY),
                RandomConstruction.mySort(Consts.STRING_TWO_DIMENSIONAL_ARRAY,
                        c), c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        Object[][] testArray = RandomConstruction
                .getArrayOfArrayOfRandomString();
        res = checkSorted(testArray, RandomConstruction.mySort(testArray, c), c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        return passed();
    }

}