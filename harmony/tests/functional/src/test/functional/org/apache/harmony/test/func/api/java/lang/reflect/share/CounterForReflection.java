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
 * Created on 24.02.2005
 * Last modification G.Seryakova
 * Last modified on 24.02.2005
 * 
 * Simple hash table for reflection tests.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.share;

import java.math.BigInteger;
import java.lang.reflect.*;

/**
 * Simple hash table for reflection tests.
 * 
 */
public class CounterForReflection {
        private static Item DISABLED = new Item(null, 0);
        public int          N        = 0;
        private int         size     = 0;
        private Item        table[];

        static class Item {
            AccessibleObject key;
            int value;

            Item(AccessibleObject key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        public CounterForReflection(int N) {
            this.N = N;
            table = new Item[N];
        }

        public void put(AccessibleObject key) {
            int ind = findIndex(key);
            if (table[ind] == null || table[ind] == DISABLED) {
                table[ind] = new Item(key, 1);
            } else {
                table[ind].value = table[ind].value + 1;
            }
            size++;
        }

        public int get(AccessibleObject key) {
            int ind = findKeyIndex(key);
            if (ind != -1) {
                return table[ind].value;
            } else {
                return 0;
            }
        }

        private int getHashValue(AccessibleObject key, int shift) {
            BigInteger value = BigInteger.valueOf(key.hashCode() + shift);
            value = value.mod(BigInteger.valueOf(N));
            return value.intValue();
        }

        private int findIndex(AccessibleObject key) {
            int ind = findKeyIndex(key);
            if (ind != -1) {
                return ind;
            }
            int shift = 0;
            while (true) {
                ind = getHashValue(key, shift);
                if (table[ind] == null || table[ind] == DISABLED) {
                    return ind;
                } else {
                    shift++;
                }
            }
        }

        private int findKeyIndex(AccessibleObject key) {
            int shift = 0;
            while (true) {
                int ind = getHashValue(key, shift);
                if (table[ind] == null) {
                    return -1;
                } else if (table[ind].key.equals(key)) {
                    return ind;
                } else {
                    shift++;
                }
            }
        }
}
