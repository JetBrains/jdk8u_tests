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

import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.Exchanger;
import java.nio.*;
import java.util.*;

import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: Test GC weak refs support - how often GC collects weak refs when it is close to OOME.
 * The test will show if weak refs support in GC is balanced.
 *  passed parameters:
 *  parameter[0] - number of threads to start
 *  
 *  The test Thread does the following:
 *  * creates numberOfThreads  
 *  * in every thread: different objects are created by Random. 
 *  
 *  No hangs, crashes or fails are expected.
 *  */

public class WeakRefsTest extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 32;
    static int numberOfThreads = NUMBER_OF_THREADS;
    static final int TIME_LIMIT_FOR_ONE_ITERATION = 1; // minutes
    volatile static boolean stopThreads = false;
    
    public static void main(String[] params){
        System.exit(new WeakRefsTest().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);
        
        Thread[] thrds = new Thread[numberOfThreads];
        for (int i=0; i< numberOfThreads; i++){
            thrds[i] = new ObjectEmitterThreadEx();
            thrds[i].start();
        }
        
        try {
            Thread.currentThread().sleep(TIME_LIMIT_FOR_ONE_ITERATION * 60 * 1000);// converted minutes to milliseconds
        } catch (InterruptedException e1) {
            return fail("Main thread was interrupted.");
        } 
        
        stopThreads = true;
        
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

class ObjectEmitterThreadEx extends Thread{
    static final int ARRAY_SIZE_LIMIT = 777;
    static final int NUMBER_OF_EMMITED_OBJECTS = 1024;
    static Random rm = new Random();
    static Exchanger exc = new Exchanger();
    
    public void run (){
        WeakHashMap<Object, Object> whm = new WeakHashMap<Object, Object>();
        while(!WeakRefsTest.stopThreads){
            try{
                whm.put(emitObject(), null);
            }catch(Throwable e){
                GCTest.log.add("Failed to create object!");
                GCTest.failed = true;
                return;
            }
        }

    }

    Object emitObject() throws OutOfMemoryError{
        Object ret = null;
        switch(rm.nextInt(51)){
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
            case 32:
                int z = 3;
                ret = z;
                break;
            case 33:
                char z1 = 'a';
                ret = z1;
                break;
            case 34:
                float z2 = (float) 3.14;
                ret = z2;
                break;
            case 35:
                double z3 = 3.14;
                ret = z3;
                break;
            case 36:
                long z4 = 314;
                ret = z4;
                break;
            case 37:
                ret = new Thread();
                break;
            case 38:
                ret = new StringBuffer();
                break;
            case 39:
                ret = new Exception();
                break;
            case 40:
                ret = new Error();
                break;
            case 41:
                ret = new Formatter();
                break;
            case 42:
                ret = new HashMap();
                break;
            case 43:
                ret = new HashSet();
                break;
            case 44:
                ret = new Hashtable();
                break;
            case 45:
                ret = new LinkedList();
                break;
            case 46:
                ret = new Properties();
                break;
            case 47:
                ret = new Random();
                break;
            case 48:
                ret = new Stack();
                break;
            case 49:
                ret = new Vector();
                break;
            case 50:
                ret = new WeakHashMap();
                break;
            default:
                ret = new byte[1];
        }
        return null;
    }
    
}

