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
package org.apache.harmony.test.func.api.java.util.jar.Attributes;

import java.util.Set;
import java.util.jar.Attributes;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class AttributesTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new AttributesTest().test(args));
    }

    public Result testInit() {
        Attributes a = new Attributes();
        if (a.size() != 0) {
            return failed("expected size of newly created attributes to be 0");
        }
        return passed();
    }

    public Result testInitInt() {
        Attributes a = new Attributes(5);
        if (a.size() != 0) {
            return failed("expected size of newly created attributes to be 0");
        }
        return passed();
    }

    public Result testEntrySet() {
        Attributes a = new Attributes(5);
        Set s = a.entrySet();

        if (!s.isEmpty()) {
            return failed("expected initial entryset to be empty");
        }

        a.putValue("abcd", "bcde");

        if (s.size() != 1) {
            return failed("expected entryset to have size == 1 after putting one entry to attribute");
        }

        a.putValue("ABCD", "QWER");

        if (s.size() != 1) {
            return failed("expected entryset to have size == 1 after putting second entry to attribute");
        }

        a.clear();

        if (s.size() != 0) {
            return failed("expected entryset to have size == 0 after clearing attributes");
        }

        try {
            s.add("x");
            return failed("expected UnsupportedOperationException addiing data to entryset");
        } catch (UnsupportedOperationException e) {
        }

        return passed();
    }

    public Result testGet() {
        Attributes a = new Attributes();

        if (a.get(new Attributes.Name("abcd")) != null) {
            return failed("expected null on empty attributes");
        }

        a.putValue("abcd", null);

        if (a.get(new Attributes.Name("abcd")) != null) {
            return failed("expected null after putting key -> null");
        }

        a.putValue("abcd", "bcde");

        if (!"bcde".equals(a.get(new Attributes.Name("abcd")))) {
            return failed("expected 'bcde' after putting key -> 'bcde'");
        }

        a.putValue("ABCD", "BCDE");

        if (!"BCDE".equals(a.get(new Attributes.Name("abcd")))) {
            return failed("expected 'BCDE' after putting key -> 'BCDE'");
        }

        if (!"BCDE".equals(a.get((Object) new Attributes.Name("abcd")))) {
            return failed("expected 'BCDE' after putting key -> 'BCDE'");
        }

        if (a.get(new Integer(0)) != null) {
            return failed("expected null on get(Integer(0))");
        }

        return passed();
    }

    public Result testGetValueString() {
        Attributes a = new Attributes();

        if (a.getValue("abcd") != null) {
            return failed("expected null on empty attributes");
        }

        a.putValue("abcd", null);

        if (a.getValue("abcd") != null) {
            return failed("expected null after putting key -> null");
        }

        a.putValue("abcd", "bcde");

        if (!"bcde".equals(a.getValue("abcd"))) {
            return failed("expected 'bcde' after putting key -> 'bcde'");
        }

        a.putValue("ABCD", "BCDE");

        if (!"BCDE".equals(a.getValue("abcd"))) {
            return failed("expected 'BCDE' after putting key -> 'BCDE'");
        }

        try {
            a.getValue((String) null);
            return failed("expected NPE on getValue((String) null)");
        } catch (NullPointerException e) {
        }

        return passed();
    }

    public Result testGetValueAttributesName() {
        Attributes a = new Attributes();

        if (a.getValue(new Attributes.Name("abcd")) != null) {
            return failed("expected null on empty attributes");
        }

        a.putValue("abcd", null);

        if (a.getValue(new Attributes.Name("abcd")) != null) {
            return failed("expected null after putting key -> null");
        }

        a.putValue("abcd", "bcde");

        if (!"bcde".equals(a.getValue(new Attributes.Name("abcd")))) {
            return failed("expected 'bcde' after putting key -> 'bcde'");
        }

        a.putValue("ABCD", "BCDE");

        if (!"BCDE".equals(a.getValue(new Attributes.Name("abcd")))) {
            return failed("expected 'BCDE' after putting key -> 'BCDE'");
        }

        if (a.getValue((Attributes.Name) null) != null) {
            return failed("expected null on get((Attributes.Name) null)");
        }

        return passed();
    }


    public Result testPut() {
        Attributes a = new Attributes();

        if (null != a.put(null, null)) {
            return failed("expected put(null, null) to return null");
        }

        if (null != a.put(null, "abcd")) {
            return failed("expected put(null, 'abcd') to return null");
        }

        if (!"abcd".equals(a.put(null, "ABCD"))) {
            return failed("expected put(null, 'ABCD') to return 'abcd'");
        }

        if (!"ABCD".equals(a.put(null, "ABCD"))) {
            return failed("expected put(null, 'ABCD') to return 'ABCD'");
        }

        try {
            a.put("a", "a");
            return failed("expected ClassCastException on put(String, String)");
        } catch (ClassCastException e) {
        }

        try {
            a.put(new Attributes.Name("a"), new Attributes.Name("a"));
            return failed("expected ClassCastException on put(Attributes.Name, Attributes.Name)");
        } catch (ClassCastException e) {
        }

        try {
            a.put(new Object(), "a");
            return failed("expected ClassCastException on put(Object, String)");
        } catch (ClassCastException e) {
        }

        return passed();
    }

    public Result testPutValue() {
        Attributes a = new Attributes();

        try {
            a.putValue(null, null);
            return failed("expected putValue(null, null) to throw NPE");
        } catch (NullPointerException e) {
        }

        if (null != a.putValue("abcd", null)) {
            return failed("expected putValue('abcd', null) to return null");
        }

        if (null != a.putValue("abcd", "bcde")) {
            return failed("expected putValue('abcd', 'bcde') to return null");
        }

        if (!"bcde".equals(a.putValue("ABCD", "BCDE"))) {
            return failed("expected putValue('ABCD', 'BCDE') to return 'bcde'");
        }

        if (!"BCDE".equals(a.putValue("abcd", "BCDE"))) {
            return failed("expected putValue('abcd', 'BCDE') to return 'bcde'");
        }

        try {
            a.putValue("a", (String) new Object());
            return failed("expected putValue('a', (String) new Object()) to throw ClassCastException");
        } catch (ClassCastException e) {
        }

        return passed();
    }

    public Result testSize() {
        Attributes a = new Attributes();

        if (a.size() != 0) {
            return failed("expected size of new Attributes to be 0");
        }

        a.put(null, null);
        if (a.size() != 1) {
            return failed("expected size of Attributes to be 1 after one put");
        }

        a.put(null, "lalala");
        if (a.size() != 1) {
            return failed("expected size of Attributes to be 1 after overwriting put");
        }

        a.clear();
        if (a.size() != 0) {
            return failed("expected size of Attributes to be 0 after clear()");
        }

        for (int i = 0; i < 5000; ++i) {
            a.putValue("" + i, "");
            if (a.size() != i + 1) {
                return failed("size of Attributes after " + (i + 1)
                        + " puts is " + a.size());
            }
        }

        //        a.putValue("a", "a");
        //        if(a.size() != Integer.MAX_VALUE) {
        //            return failed("expected size to be Integer.MAX_VALUE");
        //        }
        //        a.putValue("b", "b");
        //        if(a.size() != Integer.MAX_VALUE) {
        //            return failed("expected size to be Integer.MAX_VALUE");
        //        }

        return passed();
    }
}