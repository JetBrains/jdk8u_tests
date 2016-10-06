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

package org.apache.harmony.test.reliability.api.zip;

import org.apache.harmony.test.reliability.share.Test;

import java.io.File;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;


/**
 * Goal: check that runtime works correctly when multiple threads are reading
 *       and receiving all entries of all/each jar file from java.home directory
 *       and subdirectories.
 *       Also, check that GC closes ZipFiles resources when they are not needed 
 *       and objects used them can be collected.
 *
 * The test does:
 *
 *    1. Reads parameters, which are:
 *          param[0] - number of threads which receive entries of all/each jar
 *          file in parallel.
 *
 *    2. Finds all jar files in java.home and its subdirectories.
 *     
 *    3. Creates param[0] threads and starts them:
 *
 *       Each running thread: 
 *          - for each found jar file receives all its entries via 
 *            new ZipFile(<jar filename>).entries();
 *
 *          - closes every second created ZipFile.
 *
 *    5. Joins all threads
 *
 */


public class ZipEntryIterateThreadingTest extends Test{

    static final String JAR_FILE_EXTENSION = ".jar";
    
    static int NUMBER_OF_THREADS = 600;

    ArrayList<String> jarFiles; // contains names of found jar files

    volatile boolean failed; // flag signals whether there was an error during test execution


    public static void main(String[] args) {
        System.exit(new ZipEntryIterateThreadingTest().test(args));
    }


    public int test(String[] params) {

        cleanUp();

        // System.out.println("ZipEntryIterateThreaddingTest started..."); 

        parseParams(params);

        // System.out.println("Number of threads to start " + NUMBER_OF_THREADS);

        String rootDir = getDirToScanForJarFiles();

        scanDirForJarFiles(rootDir);

        /* needed for test debugging only:

         System.out.println("Jar files found in " + rootDir + ":");

         for (int i = 0; i < jarFiles.size(); i++){
         System.out.println(jarFiles.get(i));
         }

         */

        Thread thrds[] = new Thread[NUMBER_OF_THREADS];

        // System.out.println("Starting threads");

        for (int i = 0; i < thrds.length; i++) {
            thrds[i] = new Zipper(this);
            thrds[i].start();
        }

        // System.out.println("\n Joining threads...");

        for (int j = 0; j < thrds.length; j++) {

            try {

                thrds[j].join();
                // System.out.print(".");

            } catch (InterruptedException e) {
                log.add("" + e.getMessage() + " exception while joining " + j + "-th thread");
                failed = true;
            }

        }

        if (failed) {
            return fail("");
        }

        return pass("OK");
    }

    void cleanUp() {
        jarFiles = new ArrayList<String>();
            failed = false;
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_THREADS = Integer.parseInt(params[0]);
        }
    }


    String getDirToScanForJarFiles() {
        return System.getProperty("java.home");
    }


    void scanDirForJarFiles(String rootDir) {

        try {

            File fl = new File (rootDir);
            File[] files = fl.listFiles();

            for (int i = 0; i < files.length; i++) {

                if (files[i].isDirectory()){
                    scanDirForJarFiles(files[i].getAbsolutePath());
                    continue;
                }

                if (files[i].getName().endsWith(JAR_FILE_EXTENSION)){
                    jarFiles.add(files[i].getAbsolutePath());
                }
            }

            // cleanup
            files = null;
            fl = null;

        } catch(Exception ex) {
            log.add("Failed to scan directory for jar files, thrown exception: " + ex.getMessage());
            failed = true;
        }
    }
    
}



class Zipper extends Thread {

    ZipEntryIterateThreadingTest base;

    public Zipper(ZipEntryIterateThreadingTest base) {
        this.base = base;
    }

    public void run() {

        try {

            // System.out.print(".");

            for (int i = 0; i < base.jarFiles.size(); i++){

                ZipFile xf = new ZipFile(base.jarFiles.get(i));

                Enumeration en = xf.entries();

                while(en.hasMoreElements()){
                    ZipEntry ze = (ZipEntry) en.nextElement();
                }

                if (i % 2 == 0) {
                    xf.close();
                    xf = null;
                } else {
                    xf = null;
                }
            }
        } catch (Exception e) {
            base.log.add(this.getName() + " thrown exception: " + e.getMessage());
            base.failed = true;
        }
    }
}
