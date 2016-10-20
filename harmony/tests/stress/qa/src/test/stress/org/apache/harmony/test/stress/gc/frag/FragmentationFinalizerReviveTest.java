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

import org.punit.watcher.CustomWatcher;

public class FragmentationFinalizerReviveTest extends
        FragmentationFinalizerTest implements Runnable {

    public static void main(String args[]) {
        runTestClass();
    }

    /**
     * The list contains revived finalizable objects.
     */
    RevivingObject _revived;

    private synchronized void _revive(RevivingObject f) {
        f._next = _revived;
        _revived = f;
    }

    /**
     * These objects delay finalization until {@link #reclaimHeap()} happens.
     */
    public class RevivingObject extends
            FragmentationFinalizerTest.FinalizableObject {

        RevivingObject _next;

        public RevivingObject(Object load, int size) {
            super(load, size);
        }

        protected void finalize() {
            if (CustomWatcher.shouldStop()) {
                super.finalize();
            } else {
                _revive(this);
            }
        }
    }

    public void setUpBeforeWatchers() throws Exception {
        super.setUpBeforeWatchers();
        setCompositeObjectClass(RevivingObject.class);
        (new Thread(this)).start();
    }

    public void tearDownBeforeWatchers() throws Exception {
        reclaimRevived();
        super.tearDownBeforeWatchers();
    }

    public void run() {
        while (!CustomWatcher.shouldStop()) {
            waitThis();
            reclaimRevived();
        }
    }

    private synchronized void reclaimRevived() {
        while (_revived != null) {
            int size = _revived._size;
            _revived = _revived._next;
            reportFreed(size);
        }
    }
}
