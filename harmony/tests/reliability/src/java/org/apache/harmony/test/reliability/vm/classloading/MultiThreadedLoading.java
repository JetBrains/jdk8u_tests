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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: Test class loading in MT environment
 * The main idea is to test loading of the same class in many threads
 * All synchronizations are to be done on classloader level in VM
 * The main members of the test are: initiating, defining classloaders, 
 * delegation model
 * 
 *  passed parameters:
 *  parameter[0] - number of threads to start
 *  
 *  The test Thread does the following:
 *  * scans class.path for class folders with class files 
 *  * for every folder found 10 UserDefined classloaders are created which can find classes
 *  only in this folder
 *  * start threads which load the same class with classloaders chosen by Random (but related to the folder where class is located) 
 *  * Check that classes loaded w different classloaders are different types!   
 *  */

public class MultiThreadedLoading extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 30;
    static int numberOfThreads = NUMBER_OF_THREADS;
    static final int NUMBER_OF_CLASSLOADERS = 10;
    static volatile Entry sharedEntry = null;
    static ArrayList<Class> reportedClasses = new ArrayList<Class>();
    public static String classFolder = System.getProperty("java.class.path"); 
    
    static void reportClass(Class c){
        synchronized(reportedClasses){
            reportedClasses.add(c);
        }
    }

    public static void main(String[] params){
        System.exit(new MultiThreadedLoading().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);

        ClassLoader[] cCl = null;
        String cp = classFolder;
        
        HashMap<String, ArrayList<String> > classFilesFolders = new HashMap<String, ArrayList<String> >(); 
        scanClassFilesFolders(cp, classFilesFolders);
        if (classFilesFolders.size() == 0){
            log.add("Did not found any golden file!");
            return fail("FAILED");
        }

        // create classloaders, each previous is parent for next one
        ArrayList<Entry> classFilesLoaders = new ArrayList<Entry>();
        Iterator it = classFilesFolders.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, ArrayList<String>> entry = (Map.Entry)it.next();
            
            // create classloaders - number is randomized, not more than 10
            String key = (String)entry.getKey();
            cCl = new ClassLoader[NUMBER_OF_CLASSLOADERS];
            for (int i=0; i< cCl.length; i++){
                cCl[i] = new MultiClassloader(key);
            }

            // create relations between class and classloader
            
            ArrayList<String> classFiles = (ArrayList<String>)entry.getValue();
            for (int i=0; i < classFiles.size(); i++){
                classFilesLoaders.add(new Entry(classFiles.get(i), cCl));
            }
        }
        
        if (classFilesLoaders.size() == 0){
            log.add("Unknown error happent!");
            return fail("FAILED");
        }
        
        for (int j = 0; j < classFilesLoaders.size(); j++){
            // choose entry to process
            reportedClasses.clear();
            sharedEntry = classFilesLoaders.get(j);
            
            // start processing
            Thread[] thrds = new Thread[numberOfThreads];
            for (int i=0; i<thrds.length; i++){
                thrds[i] = new ThreadLoader();
                thrds[i].start();
            }
        
            for (int i=0; i<thrds.length; i++){
                try {
                    thrds[i].join();
                } catch (InterruptedException e) {
                    return fail("FAILED3");
                }
            }
            
            // check that all loaded classes are loaded w different classloaders
            // + try to create new instance
            Object objToCompare = null;
            HashSet<Class> hs = new HashSet<Class>();
            if (reportedClasses.size() > 0){
                for (int i=1; i<reportedClasses.size();i++){
                    Class c = reportedClasses.get(i);
                    boolean res = hs.add(c);
                    Object obj = null;
                    if (res = false)
                    {
                        return fail("FAILED");
                    }
                    try {
                        obj = c.newInstance();
                    } catch(Throwable e){
                        // Expected
                    }
                    objToCompare = obj;
                }
            }
            reportedClasses.clear();
            hs = null;
            
        }        
        
        if (failed == true){
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
    
    //     scan class files in test folder
    void scanClassFilesFolders(String dir, HashMap<String,ArrayList<String> > classFoldersFiles){
        File root = new File(dir);
        File[] files = root.listFiles();
        if (files.length > 0){
            ArrayList<String> filesInFolder = new ArrayList<String>();
            classFoldersFiles.put(dir, filesInFolder);
            for (int i=0; i< files.length; i++){
                String name = files[i].getName();
                if (name.endsWith(".class") && !name.contains("$")){
                    filesInFolder.add(name.replace(".class", ""));
                }
                if (files[i].isDirectory()){
                    scanClassFilesFolders(files[i].getAbsolutePath(), classFoldersFiles);
                }
            }
        }
        files = null;
        root = null;
    }
    
}

class ThreadLoader extends Thread{
    public void run (){
        Random rm  = new Random(); 
        String className = MultiThreadedLoading.sharedEntry.className;
        int len = MultiThreadedLoading.sharedEntry.cls.length;
        int ind = rm.nextInt(len);
        ClassLoader cl = MultiThreadedLoading.sharedEntry.cls[ind];
        
        Class c = null;
        try{
            c = cl.loadClass(className);
            MultiThreadedLoading.reportClass(c);
        }catch (LinkageError e){
            // Expected
        }catch(ClassNotFoundException e){
            // Expected
        }catch (Throwable t){
            // Expected something else
        }
    }
}
    
class MultiClassloader extends ClassLoader {
    String classFileFolder = null;

    MultiClassloader(String fileFolderPath){
        classFileFolder = fileFolderPath;
    }

    private byte[] loadBinary(String className){
        byte[] b = null;
        try {
            String classFileFullPath = classFileFolder + "/" + className + ".class";
            FileInputStream fis = new FileInputStream(classFileFullPath);
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

class Entry{
    Entry(String nm, ClassLoader[] c_l){
        className = nm;
        cls = c_l;
    }
    public String className;
    public ClassLoader cls[];
}