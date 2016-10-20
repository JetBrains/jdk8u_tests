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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.harmony.test.stress.gc.share.GcTest;
import org.punit.util.MemoryUtil;

public class FragmentationSoftRefTest extends GcTest implements Runnable {

    public static void main(String args[]) {
        runTestClass();
    }

    protected ReferenceQueue<Object> queue;

    private boolean _shouldStop = false;

    public void setUpBeforeWatchers() throws Exception {
        super.setUpBeforeWatchers();
        queue = new ReferenceQueue<Object>();
        setCompositeObjectClass(SoftRef.class);
        (new Thread(this)).start();
    }

    public void run() {
        try {
            while (!_shouldStop) {
                Reference<?> ref = queue.remove();
                ref.clear(); // report deallocation
            }
        } catch (InterruptedException ie) {
            error(ie);
        }
    }

    /**
     * Creates an object with the given size, either array or composite object.
     * 
     * @return a reference to the created object
     */
    protected Object allocateObject(Random r, int size) {
        Object obj;
        if (isArraySize(size)) {
            obj = new byte[size - ARRAY_HEADER_SIZE];
        } else {
            obj = super.allocateObject(r, size);
        }
        return obj;
    }

    /**
     * The reference to the object.
     */
    public class SoftRef extends SoftReference<Object> {
        protected int _size;

        public SoftRef(Object load, int size) {
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

    @Override
    protected synchronized void reportRemoved(int size) {
        if (isArraySize(size)) {
            super.reportRemoved(size);
        } else {
            // else register removal later during reference cleanup
        }
    }

    private boolean isArraySize(int size) {
        return size % 2 == 0;
    }

    public void tearDownBeforeWatchers() throws Exception {
        clear();
        super.tearDownBeforeWatchers();
        _shouldStop = true;
    }
}
