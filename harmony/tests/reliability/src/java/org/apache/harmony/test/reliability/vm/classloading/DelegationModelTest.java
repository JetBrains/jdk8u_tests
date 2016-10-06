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
import java.util.Iterator;
import java.util.Map;
import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: Test class loading delegation model
 * The main idea is to make chain of User Defined classloaders where
 * every part of chain could load only definite classes
 * Therefore, covering all classes with appropriate classloaders we
 * can garantee that loading class we'll not meet ClassNotFoundException
 * 
 *  passed parameters:
 *  parameter[0] - number of threads to start
 *  parameter[1] - folder where to scan class files
 *  
 *  The test Thread does the following:
 *  * scans class.path for class folders with class files 
 *  * for every folder found UserDefined classloader is created which can find classes
 *  only in this folder, every previous classloader is parent for a next one 
 *  * in the cycle start loading of found classes with child classloader
 *  * No ClassNotFoundException is to be raised!   
 *  */

public class DelegationModelTest extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 10;
    static int numberOfThreads = NUMBER_OF_THREADS;
    public static String classFolder = System.getProperty("java.class.path");;
    
    public static void main(String[] params){
        System.exit(new DelegationModelTest().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);
        
        Thread[] thrds = new Thread[numberOfThreads];
        for (int i=0; i< numberOfThreads; i++){
            thrds[i] = new Delegation();
            thrds[i].start();
        }
        
        for (int i=0; i< numberOfThreads; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                failed = true;
            }
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
}

class Delegation extends Thread{
    public void run (){
        ClassLoader cCl = null;
        HashMap<String,ArrayList<String> > classFilesFolders = new HashMap<String,ArrayList<String> >();
        String cp = DelegationModelTest.classFolder;
        
        scanClassFilesFolders(cp, classFilesFolders);
        if (classFilesFolders.size() == 0){
            DelegationModelTest.failed = true;
        }
        
        // create classloaders, each previous is parent for next one
        Iterator it = classFilesFolders.entrySet().iterator();
        if (it.hasNext()){
            Map.Entry<String, ArrayList<String>> entry = (Map.Entry)it.next();
            String key = (String)entry.getKey();
            cCl = new CustomELoader(key, ClassLoader.getSystemClassLoader());
        }
        while (it.hasNext()){
            Map.Entry<String, ArrayList<String>> entry = (Map.Entry)it.next();
            String key = (String)entry.getKey();
            cCl = new CustomELoader(key, cCl); 
        }
        
        // trying to load something
        Iterator itr = classFilesFolders.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry<String, ArrayList<String>> entry = (Map.Entry)itr.next();
            String key = (String)entry.getKey();
            ArrayList<String> val = (ArrayList<String>)entry.getValue();
            for (int i = 0; i < val.size(); i++){
                String nm = val.get(i);
                nm = nm.replace(".class", "");
                try{
                    // try to load class with the top classloader in hierarchy
                    Class c = cCl.loadClass(nm);
                }
                catch (ClassNotFoundException e){
                    // garanteed to find class because all folders are covered with classloaders
                    DelegationModelTest.failed = true;
                }
                // Expected: linkage and other errors
                catch (Exception e){

                }catch (Error e){
                
                }
            }            
            
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
                    filesInFolder.add(name);
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

class CustomELoader extends ClassLoader {
    File resourceFolder = null;
    CustomELoader(String folder, ClassLoader parent){
        super(parent);
        resourceFolder = new File(folder);
    }

    private byte[] loadBinary(String className){
        String fullPath = resourceFolder.getAbsolutePath() + "/" + className + ".class";
        byte[] b = null;
        try {
            FileInputStream fis = new FileInputStream(fullPath);
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
