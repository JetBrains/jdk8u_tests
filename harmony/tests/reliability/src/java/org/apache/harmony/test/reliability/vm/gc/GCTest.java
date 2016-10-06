/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

package org.apache.harmony.test.reliability.vm.gc;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.nio.*;

import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: Test GC - create chains of various types of references. Try to fragment java heap.
 *  passed parameters:
 *  parameter[0] - number of threads to start
 *  
 *  The test Thread does the following:
 *  * creates numberOfThreads  
 *  * in every thread 3 consequent steps:
 *  1. Array of different objects is constructured - objects are chosen by Random
 *     in emitObject function; after objects were constructed thread starts to exchange 
 *     them to other objects from other threads 
 *  2. The same as 1, but array is WeakHashMap, therefore exchanged refs are weak
 *  3. Every thread constructs 5 kind of cycled object chains - 1 consists of mixed refrences,
 *     1 - of solid, and 3 - of weak, phantom, sof references correspondently
 *  
 *  Additionally test checks synchronization code of bootstrap classloader (the same classes are 
 *  loaded at the same time by different threads)
 *    
 *  */

public class GCTest extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 32;
    static int numberOfThreads = NUMBER_OF_THREADS;
    static CyclicBarrier cb;
    
    public static void main(String[] params){
        System.exit(new GCTest().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);
        cb = new CyclicBarrier(numberOfThreads);
        
        Thread[] thrds = new Thread[numberOfThreads];
        for (int i=0; i< numberOfThreads; i++){
            thrds[i] = new ObjectEmitterThread();
            thrds[i].start();
        }
        
        for (int i=0; i< numberOfThreads; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                failed = true;
            }
        }
        
        // force gc to see results of the test
        System.gc();
        
        if (failed == true){
            return fail("FAILED");
        }
        return pass("OK");
    }
    
    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
    }
}

class ObjectEmitterThread extends Thread{
    static final int ARRAY_SIZE_LIMIT = 777;
    static final int NUMBER_OF_EMMITED_OBJECTS = 1024;
    static Random rm = new Random();
    static Exchanger exc = new Exchanger();
    
    public void run (){
        // 1 step: emit objects of different size and nature
        try {
            GCTest.cb.await();            
        } catch (InterruptedException e) {
            GCTest.log.add("Failed to wait all starting threads, stage 1!");
            GCTest.failed = true;
        } catch (BrokenBarrierException e) {
            GCTest.log.add("Failed to wait all starting threads, stage 1!");
            GCTest.failed = true;
        }
        
        CopyOnWriteArrayList<Object> arr = new CopyOnWriteArrayList<Object>();
            for (int i=0; i<rm.nextInt(NUMBER_OF_EMMITED_OBJECTS)+1; i++){
                try{
                    arr.add(emitObject());
                }catch(Throwable e){
                    GCTest.log.add("Failed to create object");
                    GCTest.failed = true;
                    return;
                }
            }

        // try to exchange with refs with other threads
        for (int i=0; i< arr.size(); i++){
            try {
                Object ret = exc.exchange(arr.get(i), rm.nextInt(50)+1, TimeUnit.MILLISECONDS);
                arr.set(i, ret);
            } catch (InterruptedException e) {
                GCTest.log.add("Failed due thread was interrupted, stage 1!");
                GCTest.failed = true;
            } catch (TimeoutException e) {
                // Excpected
            }
        }
        
        // hint GC to erase every second element from Array
        for (int i=0; i< arr.size(); i++){
            if (i%2 == 0){
                arr.set(i, null);
            }
        }
        
        try {
            GCTest.cb.await();            
        } catch (InterruptedException e) {
            GCTest.log.add("Failed to wait all starting threads, stage 1!");
            GCTest.failed = true;
        } catch (BrokenBarrierException e) {
            GCTest.log.add("Failed to wait all starting threads, stage 1!");
            GCTest.failed = true;
        }
        
        // 2 step: emit objects of different size and nature + store weak refs   
        WeakHashMap<Object, Object> whm = new WeakHashMap<Object, Object>();
        for (int i=0; i<rm.nextInt(rm.nextInt(1024) + 1); i++){
            try{
                whm.put(emitObject(), null);
            }catch(Throwable e){
                GCTest.log.add("Failed to create object!");
                GCTest.failed = true;
                return;
            }
        }
        // try to exchange with refs with other threads
        Iterator it = whm.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry en = (Map.Entry) it.next();
            try {
                Object ret = exc.exchange(en.getKey(), rm.nextInt(50)+1, TimeUnit.MILLISECONDS);
                whm.put(ret, null);
            } catch (InterruptedException e) {
                GCTest.log.add("Failed due thread was interrupted, stage 1!");
                GCTest.failed = true;
            } catch (TimeoutException e) {
                // Excpected
            }
        }
        
        try {
            GCTest.cb.await();            
        } catch (Throwable e) {
            GCTest.log.add("Failed to wait all starting threads, stage 1!");
            GCTest.failed = true;
        } 
 
        // 3 step: construct various chains of objects 
        Entry first = null;
        // start 5 cases: 1 mixed refs chain, 4 - set: solid, weak, phantom, 
        for (int j = -1; j<4; j++){
            Entry e = first = newEntry(first, j);
            for (int i=0; i<rm.nextInt(rm.nextInt(1024) + 1); i++){
                first = newEntry(first, j);
            }
            e.ref = first;
        }

    }

    class Entry{
        Object ref = null;
        Object obj = null;
    }
    
    static ReferenceQueue rQue = new ReferenceQueue();
    Entry newEntry(Object next, int caseForce){
        Entry ret = new Entry();
        ret.obj = emitObject();
        int choice = 0;
        if (caseForce < 0){
            choice = rm.nextInt(4);
        }else{
            choice = caseForce;
        }
        
        switch(choice){
            case 0:
                ret.ref = new WeakReference(next);
                break;
            case 1:
                ret.ref = new SoftReference(next);
                break;
            case 2:
                ret.ref = new PhantomReference(next, rQue);
                break;
            case 3:
                ret.ref = next;
                break;
            
        }
        return ret;
    }
    
    Object emitObject() throws OutOfMemoryError{
        Object ret = null;
        switch(rm.nextInt(32)){
                // array types
            case 0:
                ret = new int[rm.nextInt(ARRAY_SIZE_LIMIT)];
                break;
            case 1:
                ret = new char[rm.nextInt(ARRAY_SIZE_LIMIT)];
                break;
            case 2:
                ret = new byte[rm.nextInt(ARRAY_SIZE_LIMIT)];
                break;
            case 3:
                ret = new long[rm.nextInt(ARRAY_SIZE_LIMIT)];
                break;
            case 4:
                ret = new float[rm.nextInt(ARRAY_SIZE_LIMIT)];
                break;
            case 5:
                ret = new double[rm.nextInt(ARRAY_SIZE_LIMIT)];
                break;
            case 6:
                ret = new boolean[rm.nextInt(ARRAY_SIZE_LIMIT)];
                break;
            case 7:
                ret = new String[rm.nextInt(ARRAY_SIZE_LIMIT)];
                break;
            case 8:
                ret = new char[rm.nextInt(ARRAY_SIZE_LIMIT)].toString();
                break;
                // simple types
            case 9: 
                ret = new Boolean(true);
                break;
            case 10:
                ret = new Byte((byte) rm.nextInt(32));
                break;
            case 11:
                ret = new Character('a');
                break;
            case 12:
                ret = new Double("3.54");
                break;
            case 13:
                ret = new Character('a');
                break;
            case 14:
                ret = new Float("3.54");
                break;
            case 15:
                ret = new Integer(rm.nextInt());
                break;
            case 16:
                ret = new Long(rm.nextLong());
                break;
                // big  chunks of memory, upto: 32K, 64K, 128K, 256K, 512K
            case 17:
                ret = new byte[rm.nextInt(32*1024)];
                break;
            case 18:
                ret = new byte[rm.nextInt(64*1024)];
                break;
            case 19:
                ret = new byte[rm.nextInt(128*1024)];
                break;
            case 20:
                ret = new byte[rm.nextInt(256*1024)];
                break;
            case 21:
                ret = new byte[rm.nextInt(512*1024)];
                break;
                // Objects which could relate to native memory allocations
                // we do not make too big chunks for direct allocations
                // to avoid issues with native heap fragmentation
            case 22:
                ret = ByteBuffer.allocateDirect(rm.nextInt(64));
                break;
            case 23:
                ret = ByteBuffer.allocateDirect(rm.nextInt(128));
                break;
            case 24:
                ret = ByteBuffer.allocate(rm.nextInt(256));
                break;
            case 25:
                ret = ByteBuffer.allocate(rm.nextInt(512));
                break;
            case 26:
                ret = CharBuffer.allocate(rm.nextInt(64));
                break;
            case 27:
                ret = CharBuffer.allocate(rm.nextInt(128));
                break;
            case 28:
                ret = CharBuffer.allocate(rm.nextInt(256));
                break;
            case 29:
                ret = CharBuffer.allocate(rm.nextInt(512));
                break;
            case 30:
                ret = IntBuffer.allocate(rm.nextInt(64));
                break;
            case 31:
                ret = IntBuffer.allocate(rm.nextInt(128));
                break;
            default:
                ret = new byte[1];
        }
        return null;
    }
    
}

