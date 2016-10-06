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

package org.apache.harmony.test.reliability.api.kernel.management;

import java.io.File;
import java.io.FileInputStream;
import java.lang.management.*;
import java.util.List;
import java.util.Random;

import org.apache.harmony.test.reliability.share.Test;

/**
 *  Goal: check j.l.management package allows to gather system resource info in MT runtime
 *        The test does:
 *        1. Reads parameters, which are:
 *           param[0] - number of threads
 *           param[1] - number of iterations
 *           param[2] - classFolder where to get loaded class
 *           param[3] - number of classes to load per one test run
 *        2. Creates param[0]-sized array of threads and starts them. 4 types of Threads are created 
 *           1 - MemWorkerThread - creates different size object in a loop
 *           2-  ProcWorkerThread - performs some Math operations
 *           3-  CLassloadingWorkerThread - loads param[2] number of classes with 
 *               UserDefined classloader
 *           4-  SuspendResumeSleepWorkerThread - sleeps in a loop
 *        3. The main thread performs almost all admitted operations from j.l.management 
 *        5. The main thread sets flag to stop running threads and joins them.
 * 
 *        No hang, fail or crash is excpected.
 *    
 */

public class SystemResourceOrientedTest extends Test {
    static final int NUMBER_OF_THREADS = 30;
    static final int NUMBER_OF_ITERATIONS = 20; 
    static int numberOfIterations = NUMBER_OF_ITERATIONS;
    static int numberOfThreads = NUMBER_OF_THREADS;
    static final int NUMBER_OF_LOADED_CLASSES = 1000;
    static volatile int numberOfLoadedClasses = NUMBER_OF_LOADED_CLASSES;    
    static String classFolder = System.getProperty("java.class.path");
    volatile public static boolean failed = false; 
    volatile static boolean stopThreads = false; 

    public static void main(String[] args) {
        System.exit(new SystemResourceOrientedTest().test(args));
    }

    public int test(String[] params) {
        parseParams(params);

        Thread thrds[] = new Thread[numberOfThreads];
        for (int i = 0; i< numberOfThreads; i++){
            thrds[i] = newThread(i);
            thrds[i].start();
        }
        
        // process testing of j.l.management
        
        for (int t=0; t<numberOfIterations; t++){
            // class loading
            ClassLoadingMXBean cmb = ManagementFactory.getClassLoadingMXBean();
            cmb.isVerbose();
            long l2 = cmb.getLoadedClassCount();
            long l3 = cmb.getUnloadedClassCount();
            long l1 = cmb.getTotalLoadedClassCount();
            if ((l2 + l3) > l1){
                failed = true;
                log.add("Number of currently loaded (" + l2 + ") and unloaded (" + l3 +
		    ") classes more than number of totally loaded (" + l1 + ") classes since JVM started.");
                break;
            }
            
            // methods compiled with JIT info 
            CompilationMXBean mmb = ManagementFactory.getCompilationMXBean();
            if (mmb.isCompilationTimeMonitoringSupported()){
                mmb.getName();
                mmb.getTotalCompilationTime();
            }
            
            // GC info
            List<GarbageCollectorMXBean> gcmb = ManagementFactory.getGarbageCollectorMXBeans();
            for (int i = 0; i<gcmb.size();i++){
                GarbageCollectorMXBean gcm = gcmb.get(i);
                gcm.getCollectionCount();
                gcm.getCollectionTime();
            }
            
            // memory pools inside JVM
            List<MemoryManagerMXBean> mmmb = ManagementFactory.getMemoryManagerMXBeans();
            for (int i=0; i< mmmb.size(); i++){
                MemoryManagerMXBean m = mmmb.get(i);
                if (m.isValid()){
                    String names[] = m.getMemoryPoolNames();
                    for (int j=0;j<names.length;j++){
                        // do something
                        names[j].hashCode();
                    }
                    m.getName();
                }
            }
            
            // java heap
            MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
            mb.gc();
            mb.getHeapMemoryUsage();
            mb.getNonHeapMemoryUsage();
            mb.getObjectPendingFinalizationCount();
            
            // OS
            OperatingSystemMXBean omb = ManagementFactory.getOperatingSystemMXBean();
            omb.getArch();
            omb.getAvailableProcessors();
            omb.getName();
            omb.getVersion();
            
            // Runtime
            RuntimeMXBean rmb = ManagementFactory.getRuntimeMXBean();
            rmb.getBootClassPath();
            rmb.getClassPath();
            rmb.getInputArguments();
            rmb.getLibraryPath();
            rmb.getManagementSpecVersion();
            rmb.getName();
            rmb.getSpecName();
            rmb.getSpecVendor();
            rmb.getStartTime();
            rmb.getUptime();
            rmb.getSystemProperties();
            rmb.getVmName();
            rmb.getVmVendor();
    
            // thread
            ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
            tmb.findMonitorDeadlockedThreads();
            long[] ths = tmb.getAllThreadIds();
            tmb.getCurrentThreadCpuTime();
            tmb.getCurrentThreadUserTime();
            tmb.getDaemonThreadCount();
            tmb.getPeakThreadCount();
            tmb.getThreadCount();
            tmb.getThreadInfo(ths);
            tmb.getTotalStartedThreadCount();
            tmb.isCurrentThreadCpuTimeSupported();
            tmb.isThreadContentionMonitoringEnabled();
            tmb.isThreadContentionMonitoringSupported();
            tmb.isThreadCpuTimeEnabled();
            tmb.isThreadCpuTimeSupported();
            tmb.resetPeakThreadCount();
            
        }
        
        // stop threads
        stopThreads = true;

        for (int i = 0; i< numberOfThreads; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                failed = true;
                log.add("Main Thread was interrupted");
            }
        }
        
        if (failed){
            return fail("");
        }else{
            return pass("OK");
        }
            
    }
    
    static Thread newThread(int i){
        Thread ret = null;
        switch(i%4){
            case 0:
                ret = new MemWorkerThread(); 
                break;
            case 1:
                ret = new ProcWorkerThread(); 
                break;
            case 2:
                ret = new SuspendResumeSleepWorkerThread(); 
                break;
            case 3:
                ret = new CLassloadingWorkerThread(); 
                break;
        }
        return ret;
    }
    

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            numberOfIterations = Integer.parseInt(params[1]);
        }
        if (params.length >= 3) {        
            classFolder = params[2];
        }
        if (params.length >= 4) {        
            numberOfLoadedClasses = Integer.parseInt(params[3]);
        }
        
    }

}

class MemWorkerThread extends Thread{
    static final int ARRAY_SIZE_LIMIT = 100;
    static final int TIME_SLEEP_IF_NEEDED = 1;
    public void run(){
        while(!SystemResourceOrientedTest.stopThreads){
            Object obj = emitObject();
            if (obj == null){
                try {
                    Thread.sleep(TIME_SLEEP_IF_NEEDED);
                } catch (InterruptedException e) {
                    SystemResourceOrientedTest.failed = true;
                    SystemResourceOrientedTest.log.add("thread " + this.getId() + " was interrupted!");
                    return;
                }
            }else{
                obj = null;
            }
        }
    }
    
    Object emitObject(){
        Random rm = new Random();
        Object ret = null;
        // generate different objects
        switch(rm.nextInt(10)){
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
                ret = new String("");
                break;
            default:
                ret = null;
        }
        return ret;
    }
    
}

class ProcWorkerThread extends Thread{
    static Random rm = new Random();
    static final int TIME_SLEEP_IF_NEEDED = 10;
    public void run(){
        while(!SystemResourceOrientedTest.stopThreads){
            // make some Math operations
            Math.cos(rm.nextInt());
            Math.log(rm.nextDouble());
            Math.sqrt(rm.nextDouble());
        }
    }
}

class SuspendResumeSleepWorkerThread extends Thread{
    static final int THREAD_SLEEP_TIME = 1000;
    public void run(){
        while(!SystemResourceOrientedTest.stopThreads){
            try {
                Thread.sleep(THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {
                SystemResourceOrientedTest.failed = true;
                SystemResourceOrientedTest.log.add("thread " + this.getId() + " was interrupted!");
                return;
            }
        }
    }
}

class CLassloadingWorkerThread extends Thread{
    static final int THREAD_SLEEP_TIME = 1000;
    public void run(){
        while(!SystemResourceOrientedTest.stopThreads && SystemResourceOrientedTest.numberOfLoadedClasses > 0){
            SystemResourceOrientedTest.numberOfLoadedClasses--;        
            try {
                Thread.sleep(THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {
                SystemResourceOrientedTest.failed = true;
                SystemResourceOrientedTest.log.add("thread " + this.getId() + " was interrupted!");
                return;
            }
            
            String classPath = SystemResourceOrientedTest.classFolder + File.separator + 
                this.getClass().getPackage().getName().replace(".", File.separator) +
                File.separator + "ManTestClass.class";
            ClassLoader cl = new CstmLoader(classPath);
            try {
                cl.loadClass("SOME_NAME");
            } catch (ClassNotFoundException e) {
                SystemResourceOrientedTest.failed = true;
                SystemResourceOrientedTest.log.add("Faile dto load class " + classPath);
                return;
            }
        }
    }
}


class CstmLoader extends ClassLoader {
    String classFile = null;

    CstmLoader(String file){
        classFile = file;
    }

    private byte[] loadBinary(String className){
        byte[] b = null;
        try {
            FileInputStream fis = new FileInputStream(classFile);
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

class ManTestClass{
    int t;
}
