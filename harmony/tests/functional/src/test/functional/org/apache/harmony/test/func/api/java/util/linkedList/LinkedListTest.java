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
package org.apache.harmony.test.func.api.java.util.linkedList;

import java.util.Comparator;
import java.util.LinkedList;

import org.apache.harmony.test.func.api.java.util.share.RandomConstruction;
import org.apache.harmony.test.func.api.java.util.share.StringComparator;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class LinkedListTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new LinkedListTest().test(args));
    }

    Comparator c = new StringComparator();

    public Result testAdd() throws Throwable {
        LinkedList ll = new LinkedList();
        LinkedList ll2 = new LinkedList();
        LinkedList ll3 = new LinkedList();
        if (ll.size() != 0) {
            return failed("1");
        }
        Object[] as = RandomConstruction.getBigArrayOfRandomString();
        Object[] as2 = new Object[as.length];
        for (int i = 0; i < as.length; i++) {
            as2[as.length - i - 1] = as[i];
            ll.add(0, as[i]);
            if (ll.size() != i + 1) {
                return failed("2");
            }
            if (!ll.contains(as[i])) {
                return failed("3");
            }
            if (ll.indexOf(as[i]) != 0) {
                return failed("4");
            }
            if (c.compare(ll.getFirst(), as[i]) != 0) {
                return failed("5");
            }
            ll2.addFirst(as[i]);
            if (!ll2.equals(ll)) {
                return failed("6");
            }
        }
        if (!RandomConstruction.equals(ll2.toArray(), as2, c)) {
            return failed("7");
        }

        ll.clear();
        if (ll.size() != 0) {
            return failed("8");
        }
        ll2.clear();
        if (ll2.size() != 0) {
            return failed("9");
        }

        as = RandomConstruction.getBigArrayOfRandomString();
        for (int i = 0; i < as.length; i++) {
            ll.add(i, as[i]);
            if (ll.size() != i + 1) {
                return failed("10");
            }
            if (!ll.contains(as[i])) {
                return failed("11");
            }
            if (ll.lastIndexOf(as[i]) != i) {
                return failed("12");
            }
            if (new StringComparator().compare(ll.getLast(), as[i]) != 0) {
                return failed("13");
            }
            ll2.addLast(as[i]);
            if (!ll2.equals(ll)) {
                return failed("14");
            }
            ll3.add(as[i]);
            if (!ll3.equals(ll)) {
                return failed("15");
            }
        }
        if (!RandomConstruction.equals(ll.toArray(), as, c)) {
            return failed("16");
        }

        ll.clear();
        if (ll.size() != 0) {
            return failed("17");
        }

        as = RandomConstruction.getBigArrayOfRandomString();
        int a;
        for (int i = 0; i < as.length; i++) {
            if (i == 0) {
                a = 0;
            } else {
                a = RandomConstruction.RANDOM.nextInt(i);
            }
            ll.add(a, as[i]);
            if (ll.size() != i + 1) {
                return failed("18");
            }
            if (!ll.contains(as[i])) {
                return failed("19");
            }
            if (new StringComparator().compare(ll.get(a), as[i]) != 0) {
                return failed("20");
            }
        }
        try {
            ll.add(ll.size() + 1, as[0]);
            return (failed("21"));
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            ll.add(-1, as[0]);
            return (failed("22"));
        } catch (IndexOutOfBoundsException e) {
        }

        return passed();
    }

    public Result testToArray() throws Throwable {
        LinkedList ll = new LinkedList();

        try {
            ll.toArray(null);
            return failed("23");
        } catch (NullPointerException e) {
        }

        Object[] as = RandomConstruction.getBigArrayOfRandomString();
        for (int i = 0; i < as.length; i++) {
            ll.add(0, as[i]);
        }
        if (!RandomConstruction.equals(ll.toArray(as), as, c)) {
            return failed("24");
        }

        Object[] ai = new Integer[as.length];
        for (int i = 0; i < as.length; i++) {
            ai[i] = new Integer(i);
        }
        try {
            ll.toArray(ai);
            return failed("25");
        } catch (ArrayStoreException e) {
        }

        Object[] ao = new Object[as.length];
        for (int i = 0; i < as.length; i++) {
            ao[i] = new Integer(i);
        }
        if (!RandomConstruction.equals(ll.toArray(ao), as, c)) {
            return failed("26");
        }
        if (!RandomConstruction.equals(ao, as, c)) {
            return failed("27");
        }

        Object[] ao2 = RandomConstruction.getArrayOfRandomString(as.length - 1);
        Object[] ao3 = RandomConstruction.copyOfArray(ao2);
        if (!RandomConstruction.equals(ll.toArray(ao2), as, c)) {
            return failed("28");
        }
        if (!RandomConstruction.equals(ao2, ao3, c)) {
            return failed("29");
        }

        int a = RandomConstruction.RANDOM.nextInt(as.length) + 1;
        ao2 = RandomConstruction.getArrayOfRandomString(as.length + a);
        ao3 = RandomConstruction.copyOfArray(ao2);
        Object[] ao4 = ll.toArray(ao2);

        if (!RandomConstruction.equals(ao4, ao2, c)) {
            return failed("30");
        }
        for (int i = 0; i < as.length; i++) {
            if (!RandomConstruction.equals(ao2[i], as[i], c)) {
                return failed("31");
            }
        }
        if (ao2[as.length] != null) {
            return failed("32");
        }
        for (int i = as.length + 1; i < ao2.length; i++) {
            if (!RandomConstruction.equals(ao2[i], ao3[i], c)) {
                return failed("33");
            }
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