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

/**
 * GOAL: the test is intendent to process different operations on classes loaded 
 * from jar files in JRE folder
 * 
 *  passed parameters:
 *  parameter[0] - number of threads to launch for parallel loading and processing Classes 
 */

package org.apache.harmony.test.reliability.share;

import java.util.ArrayList;
import org.apache.harmony.test.reliability.share.Test;
import org.apache.harmony.test.reliability.share.JarFilesScanner;


public abstract class ClassMultiTestBase extends Test implements Runnable{
    volatile boolean failed = false;
    final static String classFilesExt = ".class";
    final static char slashCharDelimiter1 = '/';
    final static char slashCharDelimiter2 = '\\';
    final static char dotCharDelimiter = '.';
    final static int NUMBER_OF_THREADS = 3;
    int numberOfThreads = NUMBER_OF_THREADS;
    int classCounter = 0;
    ArrayList<String> jarFiles;
    
    public abstract void initCycle();
    public abstract void releaseCycle();
    public abstract void testContent(Class cls);
    
    public int test(String []args){
        parseParams(args);        
        jarFiles = new JarFilesScanner().getClassFilesInJRE();
        initCycle();
        
        Thread[] thrds = new Thread[numberOfThreads];
        for (int i = 0; i< thrds.length; i++){
            thrds[i] = new Thread(this);
            thrds[i].start();
        }
        
        for (int i = 0; i< thrds.length; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                failed = true;
                log.add("Failed to join thread " + e);
            }
        }

        releaseCycle();
        
        if (failed){
            return fail("FAILED");
        }
        //System.out.println("Number of classes tested "+ classCounter);
        return pass("OK");
    }
    
    
    public void run(){
        for (int i=0; i<jarFiles.size(); i++){
            Class jlClass = null;
            String classPureName = jarFiles.get(i).substring(0, jarFiles.get(i).length()-classFilesExt.length());
            classPureName = classPureName.replace(slashCharDelimiter1, dotCharDelimiter);
            classPureName = classPureName.replace(slashCharDelimiter2, dotCharDelimiter);
            try {
                // do not initialize loaded classes
                jlClass = Class.forName(classPureName, false, this.getClass().getClassLoader());
            } catch (Throwable e) {
                // DO NOT CONSIDER failed to load classes - the test GOAL is different
                // see HARMONY-2615 for issues
                continue;
            }
            if (jlClass != null){
                classCounter++;
                try{
                    testContent(jlClass);
                } catch (Throwable e){
                    if (e.getClass().getName() == "java.lang.InternalError") continue; // enables 100% pass on RI
                    log.add("Failed to test class: " + classPureName + " Issue:" + e);
                    e.printStackTrace();
                    failed = true;
                }
            }
        }
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
    }

}
