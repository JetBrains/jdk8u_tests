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

package org.apache.harmony.test.reliability.vm.stack;

import org.apache.harmony.test.reliability.share.Test;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/*
 * Goal: check that Throwable object saves, kepps and restores (prints) stack trace in case 
 * class unloading happened for methods which have frames saved as stack trace in Throwable 
 * Accepted parameters:
 * param[0] - number Of threads to launch
 * param[1] - folder where loaded class is located

 * The test does:
 * 1. Process Throables (*) every NUMBER_OF_CYCLE_TO_CHECK_THROWABLES Test is called 
 * by reliability suit
 * (*) - call getStackTrace function + read some info about StackTraceElements
 * 2. Starts param[0] number of threads, every thread does:
 *    a. Loads TestClass class with userdefined classloader CustomLoaderSimple
 *    b. Creates instance of the loaded class
 *    c. Calls "testFunction" of TestClass object via Reflection
 *    d. testFunction in its case calls StackFramesRestoreTest.reportThrowable function;
 * 
 * - When GC processed between iterations class unloading is to happen, assuming class 
 *   unloading released class related data the test tries to read info about unloaded
 *   method.
 *   
 * - The test is not to hang, crash.
 */

public class StackFramesRestoreTest extends Test {
    volatile static boolean failed = false;
    final static int NUMBER_OF_THREADS_OR_INSTANCES = 10;
    static int numberOfThreads = NUMBER_OF_THREADS_OR_INSTANCES;
    static String classFolder = System.getProperty("java.class.path");
    static final int NUMBER_OF_CYCLE_TO_CHECK_THROWABLES = 3;
    
    static ArrayList<Throwable> tSaveList = new ArrayList<Throwable>();
    static int internalCycle = 0;
    
    public static void setFailed(){
        failed = true;
    }
    
    public static void commenterror(String msg){
        log.add(msg);
    }
    
    public static void main(String[] params){
        System.exit(new StackFramesRestoreTest().test(params));
    }
    
    public static synchronized void reportThrowable(){
        Throwable t = new Throwable();
        t.fillInStackTrace();
        tSaveList.add(t);
    }
    
    public static void reportThrowableByCatch(){
        try{
            int a = 0;
            int b = 10;
            a = b;
            int t = 10/(b-a);// thrown exception
        }catch(Throwable e){
            e.fillInStackTrace();
            tSaveList.add(e);
        }
    }

    public int test(String []params){
        parseParams(params);

        // increment internalCycle counter to control how often we iterate through 
        // tSaveList collection of saved Throwable
        internalCycle++;
        if (internalCycle%NUMBER_OF_CYCLE_TO_CHECK_THROWABLES == 0){
            for (int i = 0; i < tSaveList.size(); i++){
                Throwable t = tSaveList.get(i);
                StackTraceElement[] els = t.getStackTrace();
                for (int j = 0; j< els.length; j++){
                    els[j].getFileName();
                    els[j].getLineNumber();
                    els[j].getMethodName();
                    els[j].getClassName();
                }
            }
            tSaveList.clear();            
        }
        
        String packageName = this.getClass().getPackage().getName();
        String classPath = File.separator + packageName.replace(".", File.separator) + 
            File.separator + "TestClass.class";
        String filePath = classFolder + classPath;
        
        Thread thrds[] = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++){
            thrds[i] = new ThreadLoader(filePath);
            thrds[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                log.add("Launching thread was interrupted");
                failed = true;
            }
        }        
        if (failed){
            return fail("FAILED");
        }
        
        return pass("OK");
    }
    
    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            classFolder = params[1];
        }
    }
    
}

class ThreadLoader extends Thread{
    String filePath = "";
    ThreadLoader(String file){
        filePath = file;
    }
    public void run(){
        CustomLoaderSimple cl = new CustomLoaderSimple(filePath);
        Class cls = null;
        try {
            cls = cl.loadClass("SOME_NAME");
        } catch (ClassNotFoundException e) {
            StackFramesRestoreTest.commenterror("Failed to load class " + filePath);
            StackFramesRestoreTest.setFailed();
            return;
        }
        
        Object obj = null;
        try {
            new TestClass();
            obj = cls.newInstance();
        } catch (Throwable e1) {
            StackFramesRestoreTest.commenterror("Failed to create object of class " + filePath);
            StackFramesRestoreTest.setFailed();
            return;
        } 
        
        Method m = null;
        try {
            m = cls.getMethod("testFunction", null);
        } catch (Throwable e) {
            StackFramesRestoreTest.commenterror("Failed to invoke testFunction in class " + filePath);
            StackFramesRestoreTest.setFailed();
            return;            
        } 
        
        try {
            m.invoke(obj, null);
        } catch (Throwable e) {
            StackFramesRestoreTest.commenterror("Failed to invoke testFunction function");
            StackFramesRestoreTest.setFailed();
            return;
        } 
    }
}


class CustomLoaderSimple extends ClassLoader {
    String classFilePath = null;

    CustomLoaderSimple(String filePath){
        classFilePath = filePath;
    }

    private byte[] loadBinary(String className){
        byte[] b = null;
        try {
            FileInputStream fis = new FileInputStream(classFilePath);
            b = new byte[fis.available()];
            fis.read(b);
            fis.close();
        } catch (Exception e) {
            return null;
        }
        
        return b;
    }
    
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = loadBinary(name);
        if (b == null){
            throw new ClassNotFoundException();
        }
        return defineClass(b, 0, b.length);
    }
}

