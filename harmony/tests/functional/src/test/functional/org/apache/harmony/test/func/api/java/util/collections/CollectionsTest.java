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
 * Created on 04.10.2005
 */
package org.apache.harmony.test.func.api.java.util.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.harmony.test.func.api.java.util.share.Consts;
import org.apache.harmony.test.func.api.java.util.share.RandomConstruction;
import org.apache.harmony.test.func.api.java.util.share.StringComparator;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class CollectionsTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new CollectionsTest().test(args));
    }

    Comparator c = new StringComparator();

    public Result checkSearch(List[] ar, Object[] a, Comparator c)
            throws Throwable {
        for (int i = 0; i < ar.length; i++) {
            List testList = ar[i];
            Collections.sort(testList, c);
            int bs = Collections.binarySearch(testList, a[i], c);
            if (bs < 0) {
                if (RandomConstruction.mySearch(testList, a[i], c) > 0) {
                    return failed("1");
                }
            } else {
                if (!RandomConstruction.equals(testList.get(bs), a[i], c)) {
                    return failed("2");
                }
            }
        }
        return passed();
    }

    public Result testBinarySearch() throws Throwable {

        List[] testedList1 = RandomConstruction
                .arrayOfArrayToArrayOfList(Consts.STRING_ARRAY);
        Result res = checkSearch(testedList1, Consts.STRING, c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        testedList1 = RandomConstruction
                .arrayOfArrayToArrayOfList(Consts.STRING_ARRAY);
        res = checkSearch(testedList1, Consts.STRING2, c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        List[] testedList2 = RandomConstruction.getArrayOfListOfRandomString();
        res = checkSearch(testedList2, RandomConstruction
                .getArrayOfRandomString(testedList2.length, 1), c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        testedList2 = RandomConstruction.getArrayOfListOfRandomString();
        res = checkSearch(testedList2, RandomConstruction
                .getArrayOfRandomString(testedList2.length, 2), c);
        if (res.getResult() != passed().getResult()) {
            return res;
        }

        return passed();
    }

    public Result testSingleton() throws Throwable {
        Object o = RandomConstruction.getRandomString();
        Set s = Collections.singleton(o);
        if (s.size() != 1) {
            return failed("3");
        }
        if (!s.contains(o)) {
            return failed("4");
        }
        if (!RandomConstruction.equals(s.toArray()[0], o, c)) {
            return failed("5");
        }
        return passed();
    }

    public Result testSingletonList() throws Throwable {
        Object o = RandomConstruction.getRandomString();
        List s = Collections.singletonList(o);
        if (s.size() != 1) {
            return failed("6");
        }
        if (s.indexOf(o) != 0) {
            return failed("7");
        }
        if (!s.contains(o)) {
            return failed("8");
        }
        if (!RandomConstruction.equals(s.toArray()[0], o, c)) {
            return failed("9");
        }
        return passed();
    }

    void wr(Object a) {
        System.out.println(a);
    }

    void wr(int a) {
        wr("" + a);
    }
}