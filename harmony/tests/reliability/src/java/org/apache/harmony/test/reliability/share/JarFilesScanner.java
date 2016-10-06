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
 * The GOAL: to provide developer with working data:
 * number of jar files in JRE 
 */

package org.apache.harmony.test.reliability.share;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.File;
import java.io.IOException;

public class JarFilesScanner{
    static final String JAR_FILE_EXTENSION = ".jar";    
    static final String CLASS_FILE_EXTENSION = ".class";
    static ArrayList<String> jarFiles = new ArrayList<String>(); // contains names of found jar files
    static ArrayList<String> classFiles = new ArrayList<String>(); // contains names of found class files
    static Boolean cache = false;

    public static ArrayList<String> getJarFilesInJRE(){
        if (cache == false) {
            String rootDir = getDirToScanForJarFiles();
            scanDirForJarFiles(rootDir);
            cache = true;
        }
        return jarFiles;
    }

    public static ArrayList<String> getClassFilesInJRE(){
        if (cache == false)
        {
            getJarFilesInJRE();
            for (int i=0; i<jarFiles.size(); i++ ){
                ZipFile xf;
                try {
                    xf = new ZipFile(jarFiles.get(i));
                    Enumeration en = xf.entries();
                    while(en.hasMoreElements()){
                        ZipEntry ze = (ZipEntry) en.nextElement();
                        String zeName = ze.getName();
                        if (zeName.endsWith(CLASS_FILE_EXTENSION)){ 
                            classFiles.add(ze.getName());
                        }
                    }
                    xf.close();
                } catch (IOException e) {
                }
            }           
        }
        return classFiles;
    }

    static String getDirToScanForJarFiles() {
        return System.getProperty("java.home");
    }

    static void scanDirForJarFiles(String rootDir) {

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
            System.gc();
        } catch(Exception ex) {
            throw new RuntimeException("Failed to scan JAR files " +  ex.getMessage());
        }
    }

}