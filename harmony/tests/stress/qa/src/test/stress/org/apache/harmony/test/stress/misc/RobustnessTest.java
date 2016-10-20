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

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import java.util.*;

public class RobustnessTest extends Thread {

    private static final int MIN_SIZE = 100; // minimal allocated object size

    private static final int MAX_SIZE = 100000; // maximal allocated object size

    private static int THREADS_COUNT = 40; // number of threads

    private static final long DELAY_TIME = 10000; // time gap between
                                                    // measurement reports
                                                    // (milliseconds)

    private static final float PERCENTAGE = 0.8f;

    private static Random rnd = new Random();

    public static void main(String[] args) {
        String th = System.getProperty("threads");
        if (th != null) {
            try {
                THREADS_COUNT = Integer.parseInt(th);
            } catch (NumberFormatException e) {
                System.out
                        .println("Warning: Invalid value of threads property ignored. Using default");
            }
        }
        System.out.println("Number of threads: " + THREADS_COUNT);

        // fill memory to expand heap to maximal size to make freeMemory and
        // totalMemory to be actual data
        List tmp = new ArrayList();
        try {
            tmp.add(new int[100000]);
        } catch (OutOfMemoryError e) {
            System.out.println("OOME catched");
            tmp = null;
            System.gc();
        }

        for (int i = 0; i < THREADS_COUNT; i++) {
            new RobustnessTest().start();
        }

        try {
            while (true) {
                sleep(DELAY_TIME);
                Statistics.report();
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted");
            e.printStackTrace();
        }
    }

    private static Object allocObject(Int size) {
        // arrays size is evaluated as 12+4*Length for object arrays
        // and 12+sizeof(<ElementType>)*Length for primitive type arrays
        int type = rnd.nextInt(4);
        int len;
        Object res = null;
        switch (type) {
        case 0:
            len = (size.getValue() - 12) / 1;
            res = new byte[len];
            size.setValue(12 + len * 1);
            break;
        case 1:
            len = (size.getValue() - 12) / 4;
            res = new int[len];
            size.setValue(12 + len * 4);
            break;
        case 2:
            len = (size.getValue() - 12) / 8;
            res = new double[len];
            size.setValue(12 + len * 8);
            break;
        case 3:
            len = (size.getValue() - 12) / 4;
            res = new Object[len];
            size.setValue(12 + 4 * len);
            break;
        default:
            break;
        }
        return res;
    }

    private static int nextSize() {
        return rnd.nextInt(MAX_SIZE - MIN_SIZE + 1) + MIN_SIZE;
    }

    public void run() {
        Statistics stats = new Statistics();

        List list = new ObjList(); // TOFIX: sizes of these objects are ignored
                                    // fo now
        List sizes = new ObjList();

        while (true) {
            if (Statistics.getHeapOccupancy() < PERCENTAGE) {
                int size = nextSize();
                Int sz = new Int(size);
                long allocTime = System.currentTimeMillis();
                Object o = allocObject(sz);
                allocTime = System.currentTimeMillis() - allocTime;
                list.add(o);
                sizes.add(sz);
                stats.newAllocation(size, allocTime);
            } else {
                Int sz;
                if (list.size() <= 0) {
                    continue;
                }
                int index = rnd.nextInt(list.size());
                list.remove(index);
                sz = (Int) sizes.remove(index);
                stats.removeObject(sz.getValue());
            }
        }
    }

    private static class Int {
        private int value;

        public Int(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private static class Statistics {
        private static Runtime runtime = Runtime.getRuntime();

        private static long freeMemory = 0;

        private static long totalMemory = 0;

        private static List statsList = new ObjList();

        private long maxPauseTotal = 0;

        private long maxPauseCurrent = 0;

        private long maxPauseTotalGC = 0;

        private long maxPauseCurrentGC = 0;

        private long minPauseTotalGC = 0;

        private long minPauseCurrentGC = 0;

        private long sizeTotal = 0;

        private long sizeCurrent = 0;

        private long allocTimeTotal = 0;

        private long allocTimeCurrent = 0;

        private long allocCountTotal = 0;

        private long allocCountCurrent = 0;

        private transient double avAllocTime = 0;

        private transient double avAllocTimeGC = 0;

        private long GCCountCurrent = 0;

        private static int cnt = 0;

        public Statistics() {
            synchronized (Statistics.class) {
                if (totalMemory == 0) {
                    totalMemory = runtime.totalMemory();
                    freeMemory = runtime.freeMemory();
                }
                statsList.add(this);
                cnt++;
            }
        }

        public void newAllocation(int size, long allocTime) {
            allocCountTotal++;
            allocCountCurrent++;
            sizeCurrent += size;
            sizeTotal += size;
            allocTimeCurrent += allocTime;
            allocTimeTotal += allocTime;
            avAllocTime = (double) allocTimeCurrent / allocCountCurrent;
            if (allocTime >= 5 * avAllocTime) {
                if (maxPauseTotalGC < allocTime) {
                    maxPauseTotalGC = maxPauseCurrentGC = allocTime;
                } else if (maxPauseCurrentGC < allocTime) {
                    maxPauseCurrentGC = allocTime;
                } else if (minPauseTotalGC > allocTime) {
                    minPauseTotalGC = minPauseCurrentGC = allocTime;
                } else if (minPauseCurrentGC > allocTime) {
                    minPauseCurrentGC = allocTime;
                }
                avAllocTimeGC = (double) allocTimeCurrent / GCCountCurrent;
            }
            if (maxPauseTotal < allocTime) {
                maxPauseTotal = maxPauseCurrent = allocTime;
            } else if (maxPauseCurrent < allocTime) {
                maxPauseCurrent = allocTime;
            }
            synchronized (Statistics.class) {
                freeMemory -= size + ObjList.ELEMENT_SIZE;
            }
        }

        public void removeObject(int size) {
            synchronized (Statistics.class) {
                freeMemory += size + ObjList.ELEMENT_SIZE;
            }
        }

        private static synchronized void report() {
            System.out.println("Current Statistics from " + statsList.size()
                    + " threads:");
            Iterator i = statsList.iterator();
            long maxPauseTotal = 0;
            long maxPauseCurrent = 0;
            long maxPauseTotalGC = 0;
            long maxPauseCurrentGC = 0;
            long minPauseTotalGC = 0;
            long minPauseCurrentGC = 0;
            double avAllocTime = 0.0;
            double avAllocTimeGC = 0.0;
            int count = 0;
            while (i.hasNext()) {
                Statistics s = (Statistics) i.next();
                synchronized (s) {
                    if (s.maxPauseTotal > maxPauseTotal)
                        maxPauseTotal = s.maxPauseTotal;
                    if (s.maxPauseCurrent > maxPauseCurrent)
                        maxPauseCurrent = s.maxPauseCurrent;
                    if (s.maxPauseTotalGC > maxPauseTotalGC)
                        maxPauseTotalGC = s.maxPauseTotalGC;
                    if (s.maxPauseCurrentGC > maxPauseCurrentGC)
                        maxPauseCurrentGC = s.maxPauseCurrentGC;
                    avAllocTime = (avAllocTime * count + s.avAllocTime)
                            / (count + 1);
                    count++;

                    s.maxPauseCurrent = 0;
                    s.sizeCurrent = 0;
                    s.allocTimeCurrent = 0;
                }

            }
            System.out.println("Max. allocation time (total)             : "
                    + (maxPauseTotal * (1e-6)) + "ms");
            System.out.println("Max. allocation time (current)           : "
                    + (maxPauseCurrent * (1e-6)) + "ms");
            System.out.println("Average allocation time (all allocations): "
                    + (avAllocTime * 1e-6) + "ms");
            System.out.println();
            System.out.println("Max. garbage collection time (total)     : "
                    + (maxPauseTotalGC * (1e-6)) + "ms");
            System.out.println("Max. garbage collection time (current)   : "
                    + (maxPauseCurrentGC * (1e-6)) + "ms");
            System.out.println("Min. garbage collection time (total)     : "
                    + (minPauseTotalGC * (1e-6)) + "ms");
            System.out.println("Min. garbage collection time (current)   : "
                    + (minPauseCurrentGC * (1e-6)) + "ms");
            System.out.println("Average garbage collection time          : "
                    + (avAllocTimeGC * 1e-6) + "ms");
        }

        private static synchronized float getHeapOccupancy() {
            return (totalMemory - freeMemory) / (float) totalMemory;
        }
    }

    private static class ObjList implements List {
        public static final int ELEMENT_SIZE = 8 + 4 + 4; // 8 bytes per object
                                                            // + 2 references of
                                                            // 4 bytes

        private Element list;

        private int size;

        private static class Element {
            private Object data;

            private Element next;

            public Element(Object o, Element n) {
                data = o;
                next = n;
            }
        }

        public ObjList() {
            list = null;
            size = 0;
        }

        public synchronized boolean add(Object o) {
            Element newElem = new Element(o, null);
            if (list != null) {
                Element p = list;
                while (p.next != null)
                    p = p.next;
                p.next = newElem;
            } else
                list = newElem;
            size++;
            return true;
        }

        public synchronized int size() {
            return size;
        }

        public synchronized Object remove(int index) {
            if (index < 0 || index >= size)
                throw new IndexOutOfBoundsException(
                        "Index must be non-negative and less than list size (index="
                                + index + ", size=" + size + ")");
            Object o;
            if (index == 0) {
                o = list.data;
                list = list.next;
                size--;
                return o;
            }
            Element p = list;
            while (index > 1) {
                p = p.next;
                index--;
            }
            o = p.next.data;
            p.next = p.next.next;
            size--;
            return o;
        }

        public Iterator iterator() {
            return new ObjListIterator();
        }

        public List subList(int from, int to) {
            throw new UnsupportedOperationException();
        }

        public ListIterator listIterator() {
            throw new UnsupportedOperationException();
        }

        public ListIterator listIterator(int index) {
            throw new UnsupportedOperationException();
        }

        public int lastIndexOf(Object o) {
            throw new UnsupportedOperationException();
        }

        public int indexOf(Object o) {
            throw new UnsupportedOperationException();
        }

        public void add(int index, Object o) {
            throw new UnsupportedOperationException();
        }

        public Object set(int index, Object o) {
            throw new UnsupportedOperationException();
        }

        public Object get(int index) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        public void removeAll(int index, Collection c) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(int index, Collection c) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        public boolean containsAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray(Object[] array) {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }

        private class ObjListIterator implements Iterator {
            Element currElem;

            public ObjListIterator() {
                currElem = ObjList.this.list;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public Object next() {
                if (currElem == null)
                    throw new NoSuchElementException(
                            "No more elements in the list");
                Object o = currElem.data;
                currElem = currElem.next;
                return o;
            }

            public boolean hasNext() {
                return currElem != null;
            }
        }

    }

}
