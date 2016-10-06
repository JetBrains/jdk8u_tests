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

package org.apache.harmony.test.reliability.api.net;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.harmony.test.reliability.share.JarFilesScanner;

import org.apache.harmony.test.reliability.share.Test;

/*
 * Goal: Test JavaUrlConnection functionality
 * the idea is to create JavaUrlConnection for existing jar file in JRE
 * + scan jar files and create JavaUrlConnection on jar entries
 * and read some information about scanned jar files and entries   
 *  passed parameters:
 *  parameter[0] - number of threads to start
 *  
 *  The test Thread does the following:
 *    * scans jar files in JRE 
 *    * creates parameter[0] threads for every jar files found 
 *    Every thread:
 *    * creates JavaUrlConnection for every jar file
 *    * reads some info about jar file
 *    * scans read jar files for entries and reads info about these entries
 *  
 *  */

public class JarUrlConnectionTest extends Test{
    static volatile boolean failed = false;
    static final int NUMBER_OF_THREADS = 100;
    static int numberOfThreads = NUMBER_OF_THREADS;
    static ArrayList<String> jarFiles = null; 

    public static void main(String[] params){
        System.exit(new JarUrlConnectionTest().test(params));
    }
    
    public int test(String[] params){
        parseParams(params);
        jarFiles = JarFilesScanner.getJarFilesInJRE();
        
        if (jarFiles == null){
            return fail("Failed to scan jar files in JRE.");
        }
        
        if (numberOfThreads > jarFiles.size()){
            numberOfThreads = jarFiles.size();
        }

        Thread[] thrds = new Thread[numberOfThreads];
        for (int i = 0; i<thrds.length; i++){
            File file = new File(jarFiles.get(i));
            try {
                thrds[i] = new scanRunner(file.toURL());
            } catch (MalformedURLException e) {
                return fail("Failed to get URL for " + jarFiles.get(i));
            }
            thrds[i].start();
        }

        for (int i = 0; i<thrds.length; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                return fail("FAILED");
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
    }
}

class scanRunner extends Thread{
    Random rm = new Random();
    URL url = null;
    
    scanRunner(URL u){
        url = u;
    }
    
    public void run (){
        JarURLConnection juc = null;

        String jarUrlString = "jar:" + url + "!/";
        //System.out.println(jarUrlString);
        URL jurl = null;
        try {
            jurl = new URL(jarUrlString);
        } catch (MalformedURLException e1) {
            JarUrlConnectionTest.log.add("Failed to convert URI name to jar URL for " + url);
            JarUrlConnectionTest.failed = true;
            return;
        }
        
        try {
            juc = (JarURLConnection)jurl.openConnection();
        } catch (IOException e) {
            JarUrlConnectionTest.log.add("Failed to open JarURLConnection for " + url);
            JarUrlConnectionTest.failed = true;
            return;
        }
        
        Attributes atts = null;
        try {
            atts = juc.getAttributes();
        } catch (IOException e) {
            JarUrlConnectionTest.log.add("Failed to get Attributes for " + url);
            JarUrlConnectionTest.failed = true;
            return;
        }
        
        Attributes mAtts = null;
        try {
            mAtts = juc.getMainAttributes();
        } catch (IOException e) {
            JarUrlConnectionTest.log.add("Failed to get Attributes for " + url);
            JarUrlConnectionTest.failed = true;
            return;
        }
        
        Certificate[] ct = null;
        try {
            ct = juc.getCertificates();
        } catch (IOException e) {
            JarUrlConnectionTest.log.add("Failed to get certificates for " + url);
            JarUrlConnectionTest.failed = true;
            return;
        }
        
        Manifest msft = null;
        try {
            msft = juc.getManifest();
        } catch (IOException e) {
            JarUrlConnectionTest.log.add("Failed to get manifest for " + url);
            JarUrlConnectionTest.failed = true;
            return;
        }

        JarFile jf = null;
        try {
            jf = juc.getJarFile();
        } catch (IOException e) {
            JarUrlConnectionTest.log.add("Failed to get manifest for " + url);
            JarUrlConnectionTest.failed = true;
            return;
        }
        
        Enumeration<JarEntry> en = jf.entries();
        while (en.hasMoreElements()){
            JarEntry je = en.nextElement();
            je.getName();
            //System.out.println(je.getName());
            je.getCertificates();
            je.getExtra();
            je.getCrc();
            je.getTime();
            je.getComment();
            je.getCodeSigners();
            
            // try to create JarURLConnection for JarEntries
            String eUrlString = jarUrlString + je.getName();
            URL eUrl;
            try {
                eUrl = new URL(eUrlString);
            } catch (MalformedURLException e) {
                JarUrlConnectionTest.log.add("Failed to Create URL to jar entry " + eUrlString);
                JarUrlConnectionTest.failed = true;
                return;
            }
            JarURLConnection juce = null;
            try {
                juce = (JarURLConnection)eUrl.openConnection();
            } catch (IOException e) {
                JarUrlConnectionTest.log.add("Failed to Create JarURLConnection to jar entry " + eUrlString);
                JarUrlConnectionTest.failed = true;
                return;
            }
            try {
                juce.getContent();
            } catch (IOException e) {
                // ignore
            }
            try {
                juce.getAttributes();
            } catch (IOException e) {
                JarUrlConnectionTest.log.add("Failed to getAttributes for " + eUrlString);
                JarUrlConnectionTest.failed = true;
                return;
            }

            juce.getAllowUserInteraction();
            try {
                juce.getCertificates();
            } catch (IOException e) {
                JarUrlConnectionTest.log.add("Failed to get certificates for " + eUrlString);
                JarUrlConnectionTest.failed = true;
                return;
            }
            
            try {
                juce.getManifest();
            } catch (IOException e) {
                JarUrlConnectionTest.log.add("Failed to get manifest for " + eUrlString);
                JarUrlConnectionTest.failed = true;
                return;
            }
            
        }
        
    }
    
}
    
