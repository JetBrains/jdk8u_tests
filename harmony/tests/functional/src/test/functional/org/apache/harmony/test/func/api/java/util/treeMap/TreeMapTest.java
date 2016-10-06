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
 * Created on 07.10.2005
 */
package org.apache.harmony.test.func.api.java.util.treeMap;

import java.util.TreeMap;

import org.apache.harmony.test.func.api.java.util.share.RandomConstruction;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class TreeMapTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new TreeMapTest().test(args));
    }

    public Result testPut() throws Throwable {
        Object[][] a = RandomConstruction.getArrayOfRandomStringAndIntegerKey();
        TreeMap ts = new TreeMap(RandomConstruction.COMPARATOR);
        if (ts.size() != 0) {
            return failed("1");
        }
        for (int i = 0; i < a.length; i++) {
            ts.put(a[i][0], a[i][1]);
            if (!ts.containsKey(a[i][0])) {
                return failed("2");
            }
            if (!ts.containsValue(a[i][1])) {
                return failed("3");
            }
            if (!RandomConstruction.equals(ts.get(a[i][0]), a[i][1])) {
                return failed("4");
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