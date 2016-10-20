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

import org.apache.harmony.test.stress.gc.share.GcTest;

public class FragmentationFinalizerTest extends GcTest {

    public static void main(String args[]) {
        runTestClass();
    }

    public void setUpBeforeWatchers() throws Exception {
        super.setUpBeforeWatchers();
        setCompositeObjectClass(FinalizableObject.class);
    }

    /**
     * The object with a non-empty finalizer.
     */
    public class FinalizableObject {
        @SuppressWarnings("unused")
        private Object _load;

        protected int _size;

        public FinalizableObject(Object load, int size) {
            _load = load;
            _size = size;
        }

        protected void finalize() {
            reportFreed(_size);
        }
    }

    @Override
    protected synchronized void reportRemoved(int size) {
        // register removal later during finalization
    }
}
