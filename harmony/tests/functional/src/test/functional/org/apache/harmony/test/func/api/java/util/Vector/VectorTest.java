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
package org.apache.harmony.test.func.api.java.util.Vector;

import java.util.Vector;

import org.apache.harmony.test.func.api.java.util.share.RandomConstruction;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class VectorTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new VectorTest().test(args));
    }

    public Result testSet() throws Throwable {
        Vector v = new Vector(10);
        try {
            v.set(1, " ");
            return (failed("1"));
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            v.set(-1, " ");
            return (failed("2"));
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        Object[] as = RandomConstruction.getNotSmallArrayOfRandomString();
        for (int i = 0; i < as.length; i++) {
            v.add("");
        }
        int a;
        for (int i = 0; i < as.length; i++) {
            if (i == 0) {
                a = 0;
            } else {
                a = RandomConstruction.RANDOM.nextInt(i);
            }
            v.set(a, as[i]);
            if (v.size() != as.length) {
                return failed("2_1");
            }
            if (!v.contains(as[i])) {
                return failed("3");
            }
            if (!RandomConstruction.equals(v.get(a), as[i])) {
                return failed("4");
            }
        }

        return passed();
    }

    public Result testAdd() throws Throwable {
        Object[] as = RandomConstruction.getNotSmallArrayOfRandomString();
        Vector v = new Vector(as.length / 2, 1);
        if (v.size() != 0) {
            return failed("5");
        }
        if (v.capacity() != as.length / 2) {
            return failed("6");
        }
        Object[] as2 = new Object[as.length];
        for (int i = 0; i < as.length; i++) {
            as2[as.length - i - 1] = as[i];
            v.add(0, as[i]);
            if (i < as.length / 2) {
                if (v.capacity() != as.length / 2) {
                    return failed("7");
                }
            } else {
                if (v.capacity() != i + 1) {
                    return failed("8");
                }
            }
            if (v.size() != i + 1) {
                return failed("9");
            }
            if (!v.contains(as[i])) {
                return failed("10");
            }
            if (v.indexOf(as[i]) != 0) {
                return failed("11");
            }
            if (!RandomConstruction.equals(v.firstElement(), as[i])) {
                return failed("12");
            }
        }
        if (!RandomConstruction.equals(v.toArray(), as2)) {
            return failed("13");
        }

        v.clear();
        if (v.size() != 0) {
            return failed("14");
        }

        as = RandomConstruction.getBigArrayOfRandomString();
        for (int i = 0; i < as.length; i++) {
            v.add(i, as[i]);
            if (v.size() != i + 1) {
                return failed("15");
            }
            if (!v.contains(as[i])) {
                return failed("16");
            }
            if (v.lastIndexOf(as[i]) != i) {
                return failed("17");
            }
            if (!RandomConstruction.equals(v.lastElement(), as[i])) {
                return failed("18");
            }
        }
        if (!RandomConstruction.equals(v.toArray(), as)) {
            return failed("19");
        }

        v.clear();
        if (v.size() != 0) {
            return failed("20");
        }

        as = RandomConstruction.getBigArrayOfRandomString();
        int a;
        for (int i = 0; i < as.length; i++) {
            if (i == 0) {
                a = 0;
            } else {
                a = RandomConstruction.RANDOM.nextInt(i);
            }
            v.add(a, as[i]);
            if (v.size() != i + 1) {
                return failed("21");
            }
            if (!v.contains(as[i])) {
                return failed("22");
            }
            if (!RandomConstruction.equals(v.get(a), as[i])) {
                return failed("23");
            }
        }
        try {
            v.add(v.size() + 1, as[0]);
            return (failed("24"));
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            v.add(-1, as[0]);
            return (failed("25"));
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return passed();
    }

    public Result testToArray() throws Throwable {
        Vector v = new Vector();

        try {
            v.toArray(null);
            return failed("26");
        } catch (NullPointerException e) {
        }

        Object[] as = RandomConstruction.getBigArrayOfRandomString();
        for (int i = 0; i < as.length; i++) {
            v.add(0, as[i]);
        }
        if (!RandomConstruction.equals(v.toArray(as), as)) {
            return failed("27");
        }
        Object[] as2 = new Object[as.length];
        v.copyInto(as2);
        if (!RandomConstruction.equals(as, as2)) {
            return failed("28");
        }

        Object[] ai = new Integer[as.length];
        for (int i = 0; i < as.length; i++) {
            ai[i] = new Integer(i);
        }
        try {
            v.toArray(ai);
            return failed("29");
        } catch (ArrayStoreException e) {
        }

        Object[] ao = new Object[as.length];
        for (int i = 0; i < as.length; i++) {
            ao[i] = new Integer(i);
        }
        if (!RandomConstruction.equals(v.toArray(ao), as)) {
            return failed("30");
        }
        if (!RandomConstruction.equals(ao, as)) {
            return failed("31");
        }

        Object[] ao2 = RandomConstruction.getArrayOfRandomString(as.length - 1);
        Object[] ao3 = RandomConstruction.copyOfArray(ao2);
        if (!RandomConstruction.equals(v.toArray(ao2), as)) {
            return failed("32");
        }
        if (!RandomConstruction.equals(ao2, ao3)) {
            return failed("33");
        }

        int a = RandomConstruction.RANDOM.nextInt(as.length);
        ao2 = RandomConstruction.getArrayOfRandomString(as.length + a);
        ao3 = RandomConstruction.copyOfArray(ao2);
        Object[] ao4 = v.toArray(ao2);

        if (!RandomConstruction.equals(ao4, ao2)) {
            return failed("34");
        }
        for (int i = 0; i < as.length; i++) {
            if (!RandomConstruction.equals(ao2[i], as[i])) {
                return failed("35");
            }
        }
        if (ao2[as.length] != null) {
            return failed("36");
        }
        for (int i = as.length + 1; i < ao2.length; i++) {
            if (!RandomConstruction.equals(ao2[i], ao3[i])) {
                return failed("37");
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