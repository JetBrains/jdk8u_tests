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

package org.apache.harmony.test.reliability.vm.classloading;

import org.apache.harmony.test.reliability.share.Test;
import java.lang.reflect.Method;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Goal: check that class loading and unloading work without OOME or other unexpected 
 *       problems when classes are loaded by user-defined classloader from binary file
 *       in numerous threads in parallel. 
 * 
 * The test does:
 *    1. Reads parameters, which are:
 *       param[0] - path to classes/ directory, relatively to which auxilliary 
 *                  class TestUnloadingClass.class can be found.
 *       param[1] - number of classes to load via custom class loader in each thread
 *       param[2] - number of classloading threads
 *       
 *    2. Starts param[2] of classloading threads. Each thread:
 *       a. Runs param[1] iterations. On each iteration:
 *       b. New instance of custom classloader (class CustomLoader) is created.
 *       c. Loading of some class (represented by org.apache....TestUnloadingClass.class)
 *          by the new instance of the loader is initiated.
 *       d. After the class is loaded, it is checked that classes' loader (as returned
 *          by Class.getClassLoader()) is definitely instance of the used custom loder.
 *       e. Then, some method of the loaded class is invoked.
 *       f. Finally, reference to the class, custom classloader which loaded the class 
 *          are null-ed, expecting that it will allow to unload the class and custom loader.
 */

public class ClassUnloadingTest extends Test {
    
    static int NUMBER_OF_CLASSES_TO_LOAD = 100;
    
    static int NUMBER_OF_THREADS_TO_START = 100;

    static String CLASS_DIR = "";
    
    static String pckgName = "org.apache.harmony.test.reliability.vm.classloading";
    
    static String testedClassName = "TestUnloadingClass";
        
    volatile Boolean failed = false; 

    public static void main(String[] args){
        System.exit(new ClassUnloadingTest().test(args));
    }
        
    public int test(String[] args){
    
        parseParams(args);
        failed = false;
        
        Thread thrds[] = new Thread[NUMBER_OF_THREADS_TO_START];
        for (int i = 0; i < thrds.length; i++){
            thrds[i] = new LoadingThread(this);
            thrds[i].run();
        }
        
        for (int i = 0; i < thrds.length; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                failed = true;
                log.add("InterruptedException was thrown: " + e.getMessage());
            }
        }
        if (failed){
            return fail("");
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {        
        if (params.length >= 1) {
            CLASS_DIR = params[0];
        }
        if (params.length >= 2) {
            NUMBER_OF_CLASSES_TO_LOAD = Integer.parseInt(params[1]);
        }
        if (params.length >= 3) {
            NUMBER_OF_THREADS_TO_START = Integer.parseInt(params[2]);
        }
    }
    
    String getTestedClassURI() {
        String s = pckgName + "." + testedClassName;
        return (s.replace('.', '/') + ".class");
    }
    
}


class LoadingThread extends Thread {
    
    ClassUnloadingTest base;
    
    LoadingThread (ClassUnloadingTest base) {
        this.base = base;
    }
    
    public void run() {
        
        for (int i = 0; i < base.NUMBER_OF_CLASSES_TO_LOAD; i++) {
            CustomLoader cl = new CustomLoader(base);
            try {
                String className = base.pckgName + "." + base.testedClassName;
                String classFileName = base.testedClassName + ".class";
                Class cls = cl.loadClass(className, classFileName);
                
                if (cls == null){
                    base.failed = true;
                    continue;
                }
                if (cls.getClassLoader() != cl) {
                    base.log.add("getClassLoader() returned not the custom classloader");
                    base.failed = true;
                    continue;
                }
                
                Object obj = cls.newInstance();
                Method meth = cls.getMethod("test", new Class[0]);
                meth.invoke(obj, new Object[0]);
                
                // Force unloading
                meth = null;
                obj   = null;
                cls = null;
                cl = null;
            } catch (Throwable t) {
                base.log.add("Thread " + this.getId() + ": unexpected exception was thrown: " + t.getMessage());
                base.failed = true;
            }
        }
    }
}

class CustomLoader extends ClassLoader {
    
    ClassUnloadingTest base;
    
    CustomLoader(ClassUnloadingTest base) {
        this.base = base;
    }

    protected URL findResource(String name){
        URL ret = null;
        try {
            File workDir = new File(base.CLASS_DIR);
            ret = new URL(workDir.toURI().toString() + "/" + base.getTestedClassURI());
        } catch (MalformedURLException e) {
            throw new Error(e);
        }
        return ret;
    }
    
    public Class loadClass(String cname, String fname) {
        
        InputStream is = getResourceAsStream(fname);
        
        if (is == null) {
            throw new RuntimeException("Couldn't find resource: " + fname);
        }
        
        byte[] data = new byte[0];
        byte[] piece = new byte[512];
        int len;
        try {
            while ((len = is.read(piece)) != -1) {
                byte[] tmp = data;
                data = new byte[tmp.length + len];
                System.arraycopy(tmp, 0, data, 0, tmp.length);
                System.arraycopy(piece, 0, data, tmp.length, len);
                tmp = null;
            }
        } catch (IOException ex) {
            throw new Error(ex);
        }
        
        try {
            is.close();
        } catch (IOException ex) {
            throw new Error(ex);
        }
        return defineClass(cname, data, 0, data.length);
    }
}


