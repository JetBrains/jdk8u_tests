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

package org.apache.harmony.test.func.api.java.util.Hashtable.functional;

import java.util.Hashtable;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 14, 2006
 */
public class RehashTest extends MultiCase {

    private class MyHash extends Hashtable {
        public int callCount = 0;

        public MyHash(int capacity) {
            super(capacity);
        }

        public MyHash(int capacity, float loadFactor) {
            super(capacity, loadFactor);
        }

        public void rehash() {
            super.rehash();
            callCount++;
        }
    }

    public Result testEverInvoked() {

        MyHash hash = new MyHash(10);
        int cnt = 0;
        long time = System.currentTimeMillis();
        while (hash.callCount == 0) {
            hash.put(new Integer(cnt), Integer.toString(cnt));
            cnt++;
            if (System.currentTimeMillis() - time > 1000) {
                return failed("Did not invoked");
            }
        }

        return checkNotDamaged(hash, cnt) ? passed()
                : failed("Hashtable was corrupted");
    }

    public Result testNotInvoked() {
        MyHash hash = new MyHash(100, 1.0f);

        for (int count = 0; count < 99; count++) {
            hash.put(new Integer(count), Integer.toString(count));
            count++;
        }

        if (!checkNotDamaged(hash, 99)) {
            return hash.callCount == 0 ? passed() : failed("Invoked on small table");
        } else {
            return failed("Hashtable was corrupted");
        }

    }

    private boolean checkNotDamaged(MyHash hash, int size) {
        for (int i = 0; i < size; i++) {
            if (!hash.containsKey(new Integer(i))
                    || !hash.containsValue(Integer.toString(i))
                    || !hash.get(new Integer(i)).equals(Integer.toString(i))) {
                return false;
            }
        }
        return hash.size() == size;
    }

    public static void main(String[] args) {
        System.exit(new RehashTest().test(args));
    }

}
