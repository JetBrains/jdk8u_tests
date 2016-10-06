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

package org.apache.harmony.test.reliability.vm.finalization;

import org.apache.harmony.test.reliability.share.Test;

import java.util.ArrayList;

/*
 * Goal: check VM finalization subsystem 
 * The test relies that finalization is not garanteed by spec
 * Accepted parameters:
 * param[0] - number of threads to run in test

 * The test consists of 3 parts executed consequently
 * 1 part - ThrowingFinalizerActivities
 *  - the test starts param[0] threads and checks that throwed exceptions in finilizer interrupt it w/o any 
 *    additional exceptions throwing, the check is done fo 7 subclasses of Throwable:
 *    both for j.l.Error and j.l.Exception types
 * 2 part - RestoreObjectsFinalizerActivities
 *  - the test starts param[0] threads and checks that single object finalizer is 
 *    executed only once, check in MT environment.
 * 3 part - SimplethreaddingActivities
 *  - the test starts 1,2 threads in finalizer   
 *  
 */

public class FinalizeThrowRestoreTest extends Test {
    volatile static boolean failed = false;
    public final static int NUMBER_OF_THREADS_OR_INSTANCES = 10;
    public final static int NUMBER_OF_CHAIN_OBJECTS = 100;
    public static int numberOfThreads = NUMBER_OF_THREADS_OR_INSTANCES;
    public static int numberOfObjects = NUMBER_OF_THREADS_OR_INSTANCES;
    public static int numberOfChainObjects = NUMBER_OF_CHAIN_OBJECTS;
    public static int numberOfThreadsLaunchedTotal = 0;
    static int counter = 0;
    static final int NUMBER_ITERATION_CLEAR_FINALIZERS = 5;
    
    public static void setFailed(){
        failed = true;
    }
    
    public static void commenterror(String msg){
        log.add(msg);
    }
    
    public static void main(String[] params){
        System.exit(new FinalizeThrowRestoreTest().test(params));
    }

    public int test(String []params){
        parseParams(params);
        RestoreObjectsFinalizerActivities.finalizeCouner = 0;
        
        counter++;
        if (counter%NUMBER_ITERATION_CLEAR_FINALIZERS == 0){
            RestoreObjectsFinalizerActivities.saveVals.clear();
        }
        
        // Stage 1: launch ThrowingFinalizerActivities test
        ThrowingFinalizerActivities[] thrds2 = new ThrowingFinalizerActivities[numberOfThreads];
        for (int i=0; i<thrds2.length; i++ ){
            thrds2[i] = new ThrowingFinalizerActivities();
            thrds2[i].start();
        }

        System.gc();
        System.runFinalization();
        
        for (int i=0; i<thrds2.length; i++ ){
            try {
                thrds2[i].join();
                thrds2[i] = null;
            } catch (InterruptedException e) {
                failed = true;
                log.add(e);
            }
            System.gc();
            System.runFinalization();
            
        }
        thrds2 = null;
        
        if (failed){
            return fail("rootcause is ThrowingFinalizerActivities");
        }

        // Stage 2: launch RestoreObjectsFinalizerActivities test
        Thread[] thrds3 = new Thread[numberOfThreads];
        for (int i=0; i<thrds3.length; i++ ){
            thrds3[i] = new RestoreObjectsFinalizerActivities();
            thrds3[i].start();
        }
        numberOfThreadsLaunchedTotal += thrds3.length; 

        System.gc();
        System.runFinalization();
        
        for (int i=0; i<thrds3.length; i++ ){
            try {
                thrds3[i].join();
                thrds3[i] = null;
            } catch (InterruptedException e) {
                failed = true;
                log.add(e);
            }
            System.gc();
            System.runFinalization();
            
        }
        thrds3 = null;
        
        // check that number of executed finalizer <= number of threads
        if (RestoreObjectsFinalizerActivities.getFinalizeCounter() > numberOfThreadsLaunchedTotal*RestoreObjectsFinalizerActivities.NUMBER_OF_OBJECTS_TO_CREATE_PER_THREAD){
            return fail("rootcause is RestoreObjectsFinalizerActivities: some finalizers were executed more than once!");
        }
        
        // Stage 3: launch SimplethreaddingActivities test
        for (int i = 0; i < numberOfObjects; i++){
            SimplethreaddingActivities sta = new SimplethreaddingActivities();
            sta = null;
        }
        System.gc();
        System.runFinalization();

        // Final stage: results
        if (failed){
            return fail("rootcause is RestoreObjectsFinalizerActivities");
        }
        
        
        return pass("OK");
    }
    
    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
    }
    
}

    /////////////////// Functional classes section /////////////////////////////


    // Prinitives for throwing exceptions from finilize method

interface Exc{
    public int getRes();
}

class throwArithmeticException extends Exception implements Exc{
    float res = 0;
    public int getRes(){
        return (int)res;
    }
    public void finalize(){
        // devision by zero
        for (int i = 100; true; i--){
            int z = 50;
            float val = z/i;
            res = val;
        }
    }
}

class throwArrayStoreException extends Exception implements Exc{
    float res = 0;
    public int getRes(){
        return (int)res;
    }
    public void finalize(){
        // ArrayStoreException
        Object str[] = new String[10];
        str[0] = new Float(3.53);
        FinalizeThrowRestoreTest.setFailed();
    }
}

class throwRuntimeException extends Exception implements Exc{
    float res = 0;
    public int getRes(){
        return (int)res;
    }
    public void finalize(){
        throw new RuntimeException();
    }
}

class throwInternalError extends Exception implements Exc{
    float res = 0;
    public int getRes(){
        return (int)res;
    }
    public void finalize(){
        throw new InternalError();
    }
}

class throwIndexOutOfBoundsException extends Exception implements Exc{
    float res = 0;
    public int getRes(){
        return (int)res;
    }
    public void finalize(){
        char [] a = new char[100];
        for (int i = 0; i< 1000; i++){
            a[i] = 'a';
        }
        FinalizeThrowRestoreTest.setFailed();
    }
}

class throwNegativeArraySizeException extends Exception implements Exc{
    float res = 0;
    public int getRes(){
        return (int)res;
    }
    public void finalize() {
        int []a = new int[-100];
        a[0] = 100;
        FinalizeThrowRestoreTest.setFailed();
    }
}

class throwError extends Exception implements Exc{
    float res = 0;
    public int getRes(){
        return (int)res;
    }
    public void finalize() {
        throw new Error();
    }
}

    // Create different  - nothing is to be catched 
class ThrowingFinalizerActivities extends Thread{
    Exc xc = null;
    public void run(){
        try {
            throw new throwArithmeticException();
        }catch (Throwable e){
        }
        try {
            throw new throwArrayStoreException();
        }catch (Throwable e){
        } 
        try {
            throw new throwRuntimeException();
        }catch (Throwable e){
        }
        try {
            throw new throwInternalError();
        }catch (Throwable e){
        }
        try {
            throw new throwIndexOutOfBoundsException();
        }catch (Throwable e){
        }
        try {
            throw new throwNegativeArraySizeException();
        }catch (Throwable e){
        }
        try{
            throw new throwError();
        }catch (Throwable e){
        }
        
        System.gc();
        System.runFinalization();
    }
    protected void finalize(){
        // emulate NPE
        xc.getRes();
    }
}

    //restoring objects from finilizer and checking the same finalizer is not called twice
class Finalizator{
    // restorereference to object in finalizer 
    protected void finalize(){
        RestoreObjectsFinalizerActivities.RestoreToSaved(this);
    }
}

class RestoreObjectsFinalizerActivities extends Thread{
    public static final int NUMBER_OF_OBJECTS_TO_CREATE_PER_THREAD = 100;
    // create objects
    RestoreObjectsFinalizerActivities(){
        for (int i=0; i< NUMBER_OF_OBJECTS_TO_CREATE_PER_THREAD; i++){
            new Finalizator();
        }
    }
    static int finalizeCouner = 0;
    public static ArrayList saveVals = new ArrayList();
    public static void RestoreToSaved(Object obj){
        synchronized(saveVals){
            finalizeCouner++;
            saveVals.add(obj);
        }
    }
    public static int getFinalizeCounter(){
        return finalizeCouner;
    }
    public void run(){
        
        // delete previously created objects from savevals
        for (int i=0; i < NUMBER_OF_OBJECTS_TO_CREATE_PER_THREAD; i++){
            Object obj = null;
            synchronized(saveVals) {
                if (saveVals.size() > 0){
                    obj = saveVals.get(0);
                    saveVals.remove(0);
                }else{
                    break;
                }
            }
            obj = null;
        }
        
    }
}


    // launching new threads in finalizer
class SimplethreaddingActivities {
    protected void finalize(){
        // starting threads from existing tests above
        new RestoreObjectsFinalizerActivities().start();
        new ThrowingFinalizerActivities().start();
    }
}
