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

/*
 * @author Alexander V. Esin
 * @version $Revision: 1.3 $
 * Created on 29.08.2005
 * 
 * This tests SoftReference, WeakReference and PhantomReference objects. Also it tests WeakHashMap
 * 1.Create Objects in a loop. Put it into Reference.
 * 2.Invoked gc().
 * 3. Check the number of enqueued references.
 * 4. Create WeakHashMap, fill it with key-value pairs.
 * 5. Invoke gc(), check if some of pair is cleared. 
 * 
 * scenario
 */
package org.apache.harmony.test.stress.api.java.lang.ref.S_ReferenceTest_01;

import org.apache.harmony.share.Test;
import java.lang.ref.*;
import java.util.*;
/**
 * This tests SoftReference, WeakReference and PhantomReference objects. Also it tests WeakHashMap
 * 1.Create Objects in a loop. Put it into Reference.
 * 2.Invoked gc().
 * 3. Check the number of enqueued references.
 * 4. Create WeakHashMap, fill it with key-value pairs.
 * 5. Invoke gc(), check if some of pair is cleared. 
 */
public class S_ReferenceTest_01 extends Test {

    public static void main(String[] args) {
        System.exit(new S_ReferenceTest_01().test(args));
    }

    long ref_count;
    ReferenceQueue queue;

    public int test() {
        ref_count = 0;
        queue = new ReferenceQueue();
        Vector v = new Vector();
        for (int i = 0; i < 100; i++) {
            v.add(weak_stress());
            //if (i % 10 == 0) log.info("."); 
        }
        log.info("allocation complete");

        ///check_integrity(v);
//      check the contents of the survivor structs
        Enumeration i = v.elements();
        long count = 0, threshold = 10;
        long ref_count_ = 0, reset_count = 0;
        while (i.hasMoreElements()) {
            Vector inner = (Vector) i.nextElement();
            Enumeration ii = inner.elements();
            while (ii.hasMoreElements()) {
                Object o = ii.nextElement();
                if (null != o && o instanceof Reference) {
                    Reference ref = (Reference) o;
                    ref_count_++;
                    o = ref.get();
                    if (null == o) 
                        reset_count++;
                }
                if (null != o && o instanceof Struct) {
                    Struct stru = (Struct) o;
                    count++;
                    if (count > threshold) {
                        threshold *= 2;
                        //log.info(",");
                    }

                    // check integrity
                    if (!stru.check()) {
                        return fail("corrupted data");
                        ///System.exit(1);
                    }
                }
            }
        }
        log.info("" + ref_count_ + " references total, " + reset_count + " reset");
        
        ///~check_integrity(v);
        log.info("integrity checked");

        //check_ref_count();
        System.gc();
        System.runFinalization();
        System.gc();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        count = 0;
        while (queue.poll() != null) count++;
        if (ref_count - count != 0) {
        	log.info("warning: enqueued reference count differs: "
                + ref_count + " allocated, " + count + " enqueued");
        } 
        if (ref_count - count > 10) {
            return fail("too many unqueued references");
            //System.exit(1);
        }
        //~check_ref_count();
        log.info("reference queues checked");

        //test_weak_hashmap();
        WeakHashMap m = new WeakHashMap();
        try {
            int size = 0;
            int pSize = 0;
            for (int ii = 0; ii < 1000; ii++) {
            	Object k = new int[1024 * 1024];
                m.put(k, null);
            	k=null;
                pSize = size;
                size = m.size();
                if (pSize > size) {
                    break;
                }
            }
            System.gc();
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            if (pSize <= m.size()) {
                return fail("assertion failed: pSize > size");
            }
        } catch (OutOfMemoryError e) {
            return fail("OutOfMemoryError should not be thrown");
        }
        //~test_weak_hashmap();
        log.info("weak hashmaps checked");

        // we need to keep variable v alive up to this point
        log.info("v.size = " + v.size());
        return pass();

    }
    /// structure used to check integrity
    public class Struct {

        final static int dead_beef = 0xDEADBEEF;
        final static int cafe_bebe = 0xCAFEBEBE;

        int i,j;

        public Struct() {
            i = dead_beef;
            j = cafe_bebe;
        }

        public boolean check() {
            return (dead_beef == i && cafe_bebe == j);
        }
    }

    Object weak_stress() {
        int i = 1;
        Vector v = new Vector();
        try {
            while (i < 10000000) {
                Struct stru = new Struct();

                // pseudo-randomize reference distribution
                Reference ref;
                if (i % 53 < 23) { 
                    ref = new WeakReference(stru, queue);
                } else if (i % 97 < 33) {
                    ref = new SoftReference(stru, queue);
                } else {
                    ref = new PhantomReference(stru, queue);
                }

                i *= 3;
                // pseudo-randomly distribute different cases
                if (i % 31 < 15) {
                    // live reference, dead referent
                    v.add(ref);
                    // clear some references
                    if (i % 53 > 25 ) {
                        ref.clear();
                    } else {
                        ref_count++; // we expect this one to be enqueued, 
                                     // if it isn't soft reference
                    }
                } else if (i % 17 < 12) {
                    // dead reference, live referent
                    v.add(stru);
                } else {
                    // live reference, live referent
                    v.add(ref);
                    v.add(stru);
                    if (i % 5 < 2)
                        ref.clear();
                }
                stru = null;
            }
        } catch (OutOfMemoryError e) { 
            // prevent failures
        }
        return v;
    }


    
}

