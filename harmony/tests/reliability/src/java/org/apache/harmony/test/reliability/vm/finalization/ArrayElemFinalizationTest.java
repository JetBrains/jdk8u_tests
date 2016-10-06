/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
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
 * @author Tatyana V. Doubtsova
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.vm.finalization;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;

/*
 * Goal: check that no memory leaks occur and finalization works with objects of various types 
 *       created and then lost for GC-ing being elemnents of static or instance arrays. 
 *
 * The test does:
 *
 *   1. For each of the types: Object, Thread, Classloader
 *
 *      a. Initializes instance and static arrays of the given type.
 *
 *      b. For each of mode: synchronized array, synchronized array's element
 * 
 *        * Runs N_OF_THREADS Threads. Each Thread randomly chooses range of array indexes
 *          to substitute objects and substitutes the objects (the elements of the array), 
 *          either locking the whole array or just element being substituted.
 * 
 *        * Runs Runtime.runFinalization() and System.gc().
 *
 *        * The test is not to crash, hang or throw OOME
 */

public class ArrayElemFinalizationTest extends Test {

    // These are tested object arrays holders:
 
    static Object[] static_object_array = null;
    Object[] instance_object_array = null;

    static Object[] static_thread_array = null;
    Object[] instance_thread_array = null;

    static Object[] static_classloader_array = null;
    Object[] instance_classloader_array = null;


    static final int ARRAY_SIZE = 1000;

    static final int BYTE_ARRAY_SIZE = 1000;

    static final int N_OF_THREADS = 100;

    static final int SYNCED_OBJECT = 1;
    static final int SYNCED_ARRAY = 2;


    public static void main(String[] args) {
        System.exit(new ArrayElemFinalizationTest().test(args));
    }


    public int test(String[] params) {

        boolean passed = true;
        boolean status = false;
        Object[][] arr;

        parseParams(params);

        //------------------ CASE 1:

        // Initialize 2 arrays (static anf instance) of Object objects:

        arr = initializeObjectArrays();

        // Run threads which lock Objects of arrays, substitute objects 

        status = (new TestObjectFinalization(this)).finalizeObjectsInArray(arr, SYNCED_OBJECT);
        passed &= status;

        //log.add("Finalization of Objects in array, Objects are synchronized. Passed? " + status);

        //------------------ CASE 2:

        // Run threads which lock arrays of Objects, substitute objects 

        status = (new TestObjectFinalization(this)).finalizeObjectsInArray(arr, SYNCED_ARRAY);
        passed &= status;

        //log.add("Finalization of Objects, array is synchronized. Passed? " + status);


        //------------------ CASE 3:

        // Initialize 2 arrays (static anf instance) of Thread objects:

        arr = initializeThreadArrays();

        // Run threads which lock Thread objects of arrays, substitute threads in array 

        status = (new TestThreadObjectFinalization(this)).finalizeObjectsInArray(arr, SYNCED_OBJECT);
        passed &= status;

        //log.add("Finalization of Thread objects in array, Thread objects are synchronized. Passed? " + status);


        //------------------ CASE 4:

        // Run threads which lock arrays of Threads, substitute Thread objects 

        status = (new TestThreadObjectFinalization(this)).finalizeObjectsInArray(arr, SYNCED_ARRAY);
        passed &= status;

        //log.add("Finalization of Thread objects, array is synchronized. Passed? " + status);


        //------------------ CASE 5:

        // Initialize 2 arrays (static anf instance) of Classloader objects:

        arr = initializeClArrays();

        // Run threads which lock Classloader objects of arrays, substitute classloaders in array 

        status = (new TestClassLoaderObjectFinalization(this)).finalizeObjectsInArray(arr, SYNCED_OBJECT);
        passed &= status;

        //log.add("Finalization of classloader objects in array, classloader objects are synchronized. Passed? " + status);


        //------------------ CASE 6:

        // Run threads which lock arrays of Classloaders objects, substitute Classloader objects

        status = (new TestClassLoaderObjectFinalization(this)).finalizeObjectsInArray(arr, SYNCED_ARRAY);
        passed &= status;

        //log.add("Finalization of Classloader objects, array is synchronized. Passed? " + status);

        if (!passed) {
            return fail("Failed");
        }

        return pass("OK");
    }


    Object[][] initializeObjectArrays() {

        // The method initializes instance and static arrays of Objects

        static_object_array = new Object[ARRAY_SIZE];
        instance_object_array = new Object[ARRAY_SIZE];

        Object[][] arr = new Object[2][];

        arr[0] = static_object_array;
        arr[1] = instance_object_array;

        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[i].length; ++j) {
                arr[i][j] = new FinalizableObject();  // simple Object
            }
        }

        return arr;
    }

    Object[][] initializeThreadArrays() {

        // The method initializes instance and static arrays of Thread objects

        static_thread_array = new Object[ARRAY_SIZE];
        instance_thread_array = new Object[ARRAY_SIZE];

        Object[][] arr = new Object[2][];

        arr[0] = static_thread_array;
        arr[1] = instance_thread_array;

        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[i].length; ++j) {
                arr[i][j] = new FinalizableThreadObject(); // Thread object
            }
        }
        return arr;
    }


    Object[][] initializeClArrays() {

        // The method initializes instance and static arrays of Classloader objects

        static_classloader_array = new Object[ARRAY_SIZE];
        instance_classloader_array = new Object[ARRAY_SIZE];

        Object[][] arr = new Object[2][];

        arr[0] = static_classloader_array;
        arr[1] = instance_classloader_array;

        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[i].length; ++j) {
                arr[i][j] = new FinalizableClassloaderObject(); // Classloader object
            }
        }
        return arr;
    }


    public void parseParams(String[] params) {

    }

}


class TestObjectFinalization {

    // This is the base class for the other 2 classes (which work with 
    // arrays of Thread and Classloader objects).

    ArrayElemFinalizationTest base;

    public TestObjectFinalization(ArrayElemFinalizationTest base){

        // Why we need the base? - just to use "log" to print output to.

        this.base = base;
    }

    boolean finalizeObjectsInArray(Object[][] arr, int type) {

        // The method just creates N_OF_THREADS Threads each substituting 
        // objects in the "arr" arrays. arr[0] is instance array, arr[1] is
        // static array.

        Thread[] t = new Thread[ArrayElemFinalizationTest.N_OF_THREADS];

        for (int j = 0; j < arr.length; ++j) {

            for (int i = 0; i < t.length ; ++i) {
                t[i] = createThread(arr[j], type);
                t[i].start();
            }

            for (int i = 0; i < t.length ; ++i) {
                try {
                    t[i].join();          
                } catch (InterruptedException ie) {
                    ArrayElemFinalizationTest.log.add("Thread " + t[i] + " was interrupted.");
                    return false;
                }
            }

            Runtime.getRuntime().runFinalization();
            System.gc();
        }

        return true;
    }


    Thread createThread(Object[] arr, int type) {

        // The method is overriden in 2 subclasses which use own Threads
        // for substitution objects in arrays.

        return new TSubstSyncedObectInArray(arr, type);
    }

    long get_num_of_created_objects() {
        return FinalizableObject.num_of_created_objects;
    }

    long get_num_of_finalized_objects() {
        return FinalizableObject.num_of_finalized_objects;
    }

    void null_num_of_finalized_objects() {
        FinalizableObject.num_of_finalized_objects = 0;
    }

    void null_num_of_created_objects() {
        FinalizableObject.num_of_created_objects = 0;
    }
}


class TestThreadObjectFinalization extends TestObjectFinalization {

    public TestThreadObjectFinalization(ArrayElemFinalizationTest base){
        super(base);
    }

    Thread createThread(Object[] arr, int type) {
        return new TSubstSyncedThrObectInArray(arr, type);
    }

    long get_num_of_created_objects() {
        return FinalizableThreadObject.num_of_created_objects;
    }

    long get_num_of_finalized_objects() {
        return FinalizableThreadObject.num_of_finalized_objects;
    }

    void null_num_of_finalized_objects() {
        FinalizableThreadObject.num_of_finalized_objects = 0;
    }

    void null_num_of_created_objects() {
        FinalizableThreadObject.num_of_created_objects = 0;
    }
}


class TestClassLoaderObjectFinalization extends TestObjectFinalization {

    public TestClassLoaderObjectFinalization(ArrayElemFinalizationTest base){
        super(base);
    }

    Thread createThread(Object[] arr, int type) {
        return new TSubstSyncedClObectInArray(arr, type);
    }

    long get_num_of_created_objects() {
        return FinalizableClassloaderObject.num_of_created_objects;
    }

    long get_num_of_finalized_objects() {
        return FinalizableClassloaderObject.num_of_finalized_objects;
    }

    void null_num_of_finalized_objects() {
        FinalizableClassloaderObject.num_of_finalized_objects = 0;
    }

    void null_num_of_created_objects() {
        FinalizableClassloaderObject.num_of_created_objects = 0;
    }
}


class TSubstSyncedObectInArray extends Thread {

    static Random r = new Random(10);
    int type = ArrayElemFinalizationTest.SYNCED_OBJECT;

    Object[] obj_array;

    static final int SLEEP_TIMEOUT = 20;

    public TSubstSyncedObectInArray(Object[] obj_array, int type) {
        this.obj_array = obj_array;
        this.type = type;
    }

    public void run() {

        // When thread starts it sleeps for some arbitrary time:

        try {
            Thread.sleep(r.nextInt(SLEEP_TIMEOUT));
        } catch (InterruptedException ie){
        }

        // Then, we select index from which substitute objects in array
        // and index up to which substitute objects:

        int i = r.nextInt(obj_array.length);
        int j = r.nextInt(obj_array.length);

        int from = i < j ? i : j;
        int to = i >= j ? i : j;

        // Finally, substitute objects either locking the whole array or locking 
        // just beings substituted object.

        if (type == ArrayElemFinalizationTest.SYNCED_OBJECT) {

            for (int x = from; x < to; ++x) {
                synchronized(obj_array[x]){
                    obj_array[x] = createObject();
                }
            }

        } else if (type == ArrayElemFinalizationTest.SYNCED_ARRAY) {

            synchronized(obj_array){
                for (int x = from; x < to; ++x) {
                    obj_array[x] = createObject();
                }
            }
        }

        // System.out.println(" " + from + ", " + to);
    }


    Object createObject() {
        return new FinalizableObject();
    }
}


class TSubstSyncedThrObectInArray extends TSubstSyncedObectInArray {

    public TSubstSyncedThrObectInArray(Object[] obj_array, int type) {
        super(obj_array, type);
    }

    Object createObject() {
        return new FinalizableThreadObject();
    }

}


class TSubstSyncedClObectInArray extends TSubstSyncedObectInArray {
 
    public TSubstSyncedClObectInArray(Object[] obj_array, int type) {
        super(obj_array, type);
    }

    Object createObject() {
        return new FinalizableClassloaderObject();
    }

}


class FinalizableObject {

    volatile public static long num_of_created_objects = 0;

    volatile public static long num_of_finalized_objects = 0;

    byte[] b;

    public FinalizableObject() {
        num_of_created_objects++;
        b = new byte[ArrayElemFinalizationTest.BYTE_ARRAY_SIZE];
    }

    protected void finalize() {
        synchronized(this) { // just in case
            num_of_finalized_objects++;
        }
    }

}


class FinalizableThreadObject extends Thread {

    volatile public static long num_of_created_objects = 0;

    volatile public static long num_of_finalized_objects = 0;

    byte[] b;

    public FinalizableThreadObject() {
        num_of_created_objects++;
        b = new byte[ArrayElemFinalizationTest.BYTE_ARRAY_SIZE];
    }

    protected void finalize() {
        synchronized(this) { // just in case
            num_of_finalized_objects++;
        }
    }

    public void run() {
    }
    
}


class FinalizableClassloaderObject extends ClassLoader {

    volatile public static long num_of_created_objects = 0;

    volatile public static long num_of_finalized_objects = 0;

    byte[] b;

    public FinalizableClassloaderObject() {
        num_of_created_objects++;
        b = new byte[ArrayElemFinalizationTest.BYTE_ARRAY_SIZE];
    }

    protected void finalize() {
        synchronized(this) { // just in case
            num_of_finalized_objects++;
        }
    }


}






