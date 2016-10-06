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
 * Created on 05.10.2005
 *  
 */

package org.apache.harmony.test.func.api.java.util.AbstractMap;

import java.util.AbstractMap;
import java.util.Set;
import java.util.TreeSet;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

class MyAbstractMap extends AbstractMap {
    private TreeSet set = new TreeSet();

    public MyAbstractMap() {
        super();
    }

    public Set entrySet() {
        return set;
    }

    public Object put(Object o1, Object o2) {
        set.add(o1);
        //set.add(o2);
        return o1;
    }
    public Object remove (Object o) {
        set.remove(o);
        return o;
    }
}

public class AbstractMapTest extends MultiCase {

    public static void main(String[] args) {
        System.exit(new AbstractMapTest().test(args));
    }

    static boolean[] defaultEmpty = { true, false, true, false, true, };

    public Result testIsEmpty() {
        boolean[] isEmpty = { false, true, false, true, false, };
        String key = new String("key");
        String value = new String("value");
        String key1 = new String("key1");
        String value1 = new String("value1");
        
        AbstractMap map = new MyAbstractMap();
        isEmpty[0] = map.isEmpty();

        
        map.put(key, value);
        isEmpty[1] = map.isEmpty();

        map.remove(key);
        isEmpty[2] = map.isEmpty();
        
        map.put(key, value);
        map.put(key1, value1);
        isEmpty[3] = map.isEmpty();
        
        map.clear();
        isEmpty[4] = map.isEmpty();

        for (int i = 0; i < defaultEmpty.length; i++) {
            if (isEmpty[i] != defaultEmpty[i]) {
                return failed("Step " + i + ": isEmpty() returns wrong value: "
                        + isEmpty[i] + ", should be: " + defaultEmpty[i]);
            }
        }
        return passed();
    }

}