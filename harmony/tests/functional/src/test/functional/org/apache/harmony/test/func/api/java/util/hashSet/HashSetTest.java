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
package org.apache.harmony.test.func.api.java.util.hashSet;

import java.util.HashSet;

import org.apache.harmony.test.func.api.java.util.share.RandomConstruction;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class HashSetTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new HashSetTest().test(args));
    }

    public Result testAdd() throws Throwable {
        Object[] as = RandomConstruction.getNotSmallArrayOfRandomString();
        HashSet ts = new HashSet();
        if (ts.size() != 0) {
            return failed("1");
        }
        Object[] as2;
        for (int i = 0; i < as.length; i++) {
            ts.add(as[i]);
            if (!ts.contains(as[i])) {
                return failed("2");
            }
        }

        ts.clear();
        as2 = RandomConstruction.getSetFromArray(as);

        for (int i = 0; i < as2.length; i++) {
            if (!ts.add(as2[i])){
                return failed("2_2");
            }
            if (ts.size() != i + 1) {
                return failed("3");
            }
            if (!ts.contains(as2[i])) {
                return failed("4");
            }
        }

        ts.clear();

        for (int i = as2.length - 1; i >= 0; i--) {
            if (!ts.add(as2[i])){
                return failed("4_2");
            }
            if (ts.size() != as2.length - i) {
                return failed("5");
            }
            if (!ts.contains(as2[i])) {
                return failed("6");
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