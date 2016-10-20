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
package org.apache.harmony.test.stress.gc.share;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;

import org.apache.harmony.test.share.stress.AbstractTest;
import org.punit.assertion.Assert;
import org.punit.watcher.MinimumWatcher;

public class GcTest extends AbstractTest {
    /**
     * The header contains a reference to a virtual table and a lock word.
     */
    public static final int HEADER_SIZE = 8;

    /**
     * The array header contains an array size in addition to the object header.
     */
    public static final int ARRAY_HEADER_SIZE = 12;

    /**
     * Usually object references contains four bytes even on 64-bit platforms.
     */
    public static final int OBJECT_REF_SIZE = 4;

    private final String HEAP_UTILIZATION = "Heap Utilization"; //$NON-NLS-1$

    public int maxBytes = 2097152;

    public int minBytes = 1;

    /**
     * Remove REMOVE_PART of allocated objects.
     */
    public static final int REMOVE_RATIO = 13;

    MinimumWatcher _heapUtilizationWatcher;

    /**
     * Total memory.
     */
    private long _totalMemory;

    /**
     * Memory used by a framework which shouldn't affect utilization.
     */
    private long _usedMemory;

    /**
     * Stand-alone mode starter.
     */
    public static void main(String args[]) {
        runTestClass();
    }

    public void setUpBeforeWatchers() throws Exception {
        super.setUpBeforeWatchers();
        _heapUtilizationWatcher = new MinimumWatcher(_runner.methodRunner(),
                HEAP_UTILIZATION, "%");
    }

    public void setUpAfterWatchers() throws Exception {
        super.setUpAfterWatchers();
        
        gc();
        long free = freeMemory();
        _totalMemory = Runtime.getRuntime().totalMemory();
        _usedMemory = _totalMemory - free;

        if (free < maxBytes) {
            maxBytes = (int) free;
        }
        //_heapUtilizationWatcher.setValue(100, 1);
        debug("total = " + _totalMemory + ", used = " + _usedMemory
                + ", ratio = " + REMOVE_RATIO + ", sizes = " + minBytes + " - "
                + maxBytes);
    }

    /**
     * It's ok to report heap utilization which is better than the actual one.
     * That is why we make a snapshot of <code>_freedSize</code> before
     * allocation and a snapshot of <code>_allocatedSize</code> after
     * allocation.
     */
    public void test() {
        int size = getObjectSize(r);
        Assert.assertTrue(size > 0);

        long freedSize = _freedSize;
        Object obj;
        try {
            obj = allocateObject(r, size);
        } catch (OutOfMemoryError oome) {
            reportFailedAllocation(freedSize, size);
            return;
        }

        reportAllocated(size);
        freedSize = _freedSize;
        try {
            Reference ref = new Reference(obj, size);
            synchronized (this) {
                ref._next = _first;
                _first = ref;
            }
        } catch (OutOfMemoryError oome) {
            reportFailedAllocation(freedSize, _referenceSize);
            obj = null;
            reportRemoved(size);
        }
    }

    private void reportFailedAllocation(long freedSize, int size) {
        long mem = _allocatedSize + size - freedSize + _usedMemory;
        reclaimHeapPart();
        _heapUtilizationWatcher.setValue(mem * 100, _totalMemory);
    }

    private Random r = new Random(0);

    /**
     * Generates a logarithmically distributed size of the array.
     * 
     * @return an object size
     */
    protected int getArraySize(Random r) {
        double logMin = Math.log(minBytes);
        return Math.round((float) Math.round((float) Math.exp(r.nextDouble()
                * (Math.log(maxBytes + 1) - logMin) + logMin) - 0.5f));
    }

    protected int getObjectSize(Random r) {
        return getArraySize(r) + ARRAY_HEADER_SIZE + getCompositeObjectSize();
    }

    /**
     * Creates an object with the given size.
     * 
     * @return a reference to the created object
     */
    protected Object allocateObject(Random r, int size) {
        Object obj = new byte[size - ARRAY_HEADER_SIZE
                - getCompositeObjectSize()];
        if (_compositeObjectClass != null) {
            try {
                Constructor<?> c = _compositeObjectClass.getConstructors()[0];
                obj = c.newInstance(this, obj, size);
            } catch (Exception e) {
                error(e);
            }
        }
        return obj;
    }

    /**
     * Reclaims a part of the heap in case of out of memory error.
     */
    private void reclaimHeapPart() {
        removePart(REMOVE_RATIO);
        synchronized (this) {
            notifyAll();
        }
    }

    public void tearDownBeforeWatchers() throws Exception {
        removePart(1);
        clear();
        Assert.assertEquals(_allocated, _freed);
        Assert.assertEquals(_allocatedSize, _freedSize);
        super.tearDownBeforeWatchers();
    }

    /**
     * The first object in the list.
     */
    private Reference _first;

    /**
     * The sum of sizes of all objects in the list.
     */
    private int _allocated;

    /**
     * The sum of sizes of all objects in the list.
     */
    private long _allocatedSize;

    /**
     * Number of deallocations.
     */
    private int _freed;

    /**
     * The sum of sizes of freed objects.
     */
    private long _freedSize;

    /**
     * Size of this object is sum of field sizes: <code>Object _next</code>,
     * <code>Object _obj</code>, <code>int _size</code>.
     */
    protected final int _referenceSize = getClassSize(Reference.class);

    /**
     * Collection of all living objects with size estimates.
     */
    class Reference {

        /**
         * Default constructor sets default sizes.
         */
        Reference(Object obj, int size) {
            reportAllocated(_referenceSize);
            _size = size;
            _obj = obj;
        }

        /**
         * The reference to the next object.
         */
        Reference _next;

        /**
         * The reference to maintain an object alive.
         */
        Object _obj;

        /**
         * The estimate of the size for this object.
         */
        int _size;
    }

    /**
     * Removes references to objects, then decreases memory consumption
     * counters.
     * 
     * @param part
     *            a part to remove
     */
    private synchronized void removePart(int part) {
        if (_first == null) {
            return;
        }

        int size;
        Reference previous = _first;

        for (int i = 1; previous != null; i++) {
            Reference next = previous._next;
            if (i % part == 0 && next != null) {
                size = next._size;
                next = next._next;
                // delete a reference to the next object
                previous._next = next;

                reportFreed(_referenceSize);
                reportRemoved(size);
            } else {
                previous = next;
            }
        }

        // remove the first element
        size = _first._size;
        _first = _first._next;

        reportFreed(_referenceSize);
        reportRemoved(size);

    }
    
    protected synchronized void reportAllocated(int size) {
        Assert.assertTrue(size > 0);
        // debug("allocated = " + size);
        _allocatedSize += size;
        _allocated++;
    }

    protected synchronized void reportFreed(int size) {
        Assert.assertTrue(size > 0);
        // debug("freed = " + size);
        _freedSize += size;
        _freed++;
    }

    /**
     * Some objects stay alive after they are removed from the registry. For
     * such objects {@link #reportFreed(int)} method should be called
     * explicitly.
     * 
     * @param size
     *            a size of freed object
     */
    protected synchronized void reportRemoved(int size) {
        reportFreed(size);
    }

    private Class<?> _compositeObjectClass = null;

    private int _compositeObjectSize;

    /**
     * @return a size of a composite class.
     */
    private int getCompositeObjectSize() {
        return _compositeObjectSize;
    }

    protected void setCompositeObjectClass(Class<?> clazz) {
        _compositeObjectClass = clazz;
        _compositeObjectSize = getClassSize(clazz);
    }

    /**
     * Estimates an object size in the heap. Note, a size of objects of the same
     * class is implementation dependent and may differ even for different
     * object instances.
     * 
     * @param clazz
     *            a class of the object
     * @return a size estimate
     */
    protected int getClassSize(Class<?> clazz) {
        int size;

        Assert.assertTrue(!clazz.isArray());
        Assert.assertTrue(!clazz.isPrimitive());

        size = clazz.equals(Object.class) ? HEADER_SIZE : getClassSize(clazz
                .getSuperclass());
        for (Field f : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                size += getFieldSize(f);
            }
        }
        return size;
    }

    protected int getFieldSize(Field f) {
        Class<?> type = f.getType();
        if (type.isPrimitive()) {
            return (type.equals(long.class) || type.equals(double.class)) ? 8
                    : 4;
        } else {
            return OBJECT_REF_SIZE;
        }
    }

    protected synchronized void waitThis() {
        try {
            wait();
        } catch (InterruptedException ie) {
            error(ie);
        }
    }

    public long clear() {
        final long TIMEOUT = 500;

        long free = 0;
        long oldFree;
        
        long bytes = 0;
        long oldBytes;
        
        int max = 0;
        int oldMax;

        
        while (true) {
            gc();
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException ie) {
            }
            oldFree = free;
            free = Runtime.getRuntime().freeMemory();
            //debug("free = " + free); 
            if (free > oldFree) {
                continue;
            }

            gc();
            oldMax = max;
            max = maxChunk();
            //debug("max = " + max);
            if (max != oldMax) {
                continue;
            }

            gc();
            oldBytes = bytes;
            bytes = freeMemory();
            //debug("bytes = " + bytes); 
            
            if (bytes == oldBytes) {
                return bytes;
            }
        }
    }
    
    private void gc() {
        System.gc();
        System.runFinalization();
    }

    private long freeMemory() {
        int size = Integer.MAX_VALUE;
        long free = 0;

        Object[] list = new Object[1];

        // decrease the chunk size
        while (size > 0) {
            try {
                while (true) {
                    Object[] newElement = new Object[size];
                    free += size;
                    newElement[0] = list;
                    list = newElement;
                }
            } catch (OutOfMemoryError oome) {
            }
            size /= 2;
        }
        list = null;
        System.gc();
        return free * OBJECT_REF_SIZE;
    }

    private int maxChunk() {
        int size = Integer.MAX_VALUE;
        int step = size / 2;
        int max = 0;

        @SuppressWarnings("unused")
        Object obj;

        // decrease the step size
        while (step > 0) {
            try {
                obj = new byte[size];
                max = size;
                size += step;
            } catch (OutOfMemoryError oome) {
                size -= step;
            }
            step /= 2;
        }
        obj = null;
        System.gc();
        return max;
    }
}
