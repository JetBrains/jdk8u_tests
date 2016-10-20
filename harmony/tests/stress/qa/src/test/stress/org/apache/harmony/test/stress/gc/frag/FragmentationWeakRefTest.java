/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.harmony.test.stress.gc.frag;

import java.lang.ref.WeakReference;

public class FragmentationWeakRefTest extends FragmentationSoftRefTest {

    public static void main(String args[]) {
        runTestClass();
    }

    public void setUpBeforeWatchers() throws Exception {
        super.setUpBeforeWatchers();
        setCompositeObjectClass(WeakRef.class);
    }

    /**
     * The reference to the object.
     */
    public class WeakRef extends WeakReference<Object> {
        protected int _size;

        public WeakRef(Object load, int size) {
            super(load, queue);
            _size = size;
        }

        /**
         * Reuses the method to report that the object is freed.
         */
        public synchronized void clear() {
            super.clear();
            if (_size > 0) {
                reportFreed(_size);
                _size = 0;
            }
        }

        protected void finalize() {
            clear();
        }
    }
}
