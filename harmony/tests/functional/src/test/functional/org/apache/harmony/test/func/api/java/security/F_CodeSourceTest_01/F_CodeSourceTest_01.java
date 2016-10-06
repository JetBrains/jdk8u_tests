/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.api.java.security.F_CodeSourceTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.Policy;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created on 30.11.2004
 */

public class F_CodeSourceTest_01 extends ScenarioTest 
{
    public int test() 
    {        
        CodeSource cs = null;
        FileInputStream fis = null;
        
        try {
            for (Enumeration en = Policy.getPolicy().getPermissions(this.getClass().getProtectionDomain()).elements(); en.hasMoreElements();) {
                log.info(""+en.nextElement());
            }            
            
            if (System.getProperty("os.name").indexOf("Win") != -1) {
                testArgs[0] = "file:/" + testArgs[0];
            } else {
                testArgs[0] = "file:" + testArgs[0];
            }
            //fis = new FileInputStream(testArgs[1]);
            //CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            //Certificate cert = certFactory.generateCertificate(fis);
            //cs = new CodeSource(new URL(testArgs[0]), new Certificate[] {cert});
            cs = new CodeSource(new URL(testArgs[0]), new Certificate[] {});        
                        
            SecureClassLoader scl = new MySecureClassLoader(ClassLoader.getSystemClassLoader(), testArgs[3]);
            log.info("Loading class " + testArgs[2] + " ...");
            Class c = scl.loadClass(testArgs[2]);            
            log.info("OK");
            URL location = cs.getLocation();
            location = null;
            if (cs.getLocation() == null) {
                return fail("There is a security breach in CodeSource.getLocation(). Test failed");
            }
/*            log.info("Constructed codesource's location: " + cs.getLocation());
            log.info("A protection domain of the loaded class: "); 
            log.info(c.getProtectionDomain());
            log.info("A codesource location of the loaded class: " + c.getProtectionDomain().getCodeSource().getLocation());*/
            
            log.info(""+cs);
            log.info(""+c.getProtectionDomain().getCodeSource());
            if (cs.equals(c.getProtectionDomain().getCodeSource()))
            {
                return fail("Code sources are equal. Test failed.");
            }
            
            int hashCode1 = cs.hashCode();
            int hashCode2 = cs.hashCode();
            log.info("Computing a hash code of the same object.");
            log.info("Hashcode1: " + hashCode1);
            log.info("Hashcode2: " + hashCode2);
            
            if (hashCode1 != hashCode2)
            {
                return fail("Hash codes of the same object aren't equal. Test failed.");
            }
            
            if (c.getProtectionDomain().getCodeSource() != null) {
                //hashCode2 = 0;
                //log.info("Computing a hash code of two different objects.");
                //hashCode2 = c.getProtectionDomain().getCodeSource().hashCode();
                //log.info("Hashcode1: " + hashCode1);
                //log.info("Hashcode2: " + hashCode2);
                //if (hashCode1 == hashCode2)
                //{
                //    return fail("Hash codes are equal. Test failed.");
                //}
                
//                if (c.getProtectionDomain().getCodeSource().implies(cs))
//                {
//                    return fail("The code source of the loaded class implies the specified code source whereas it must not. Test failed");
//                }
                if (!cs.implies(c.getProtectionDomain().getCodeSource())) {
                    return fail("The specified code source doesn't imply the code source of the loaded class. Test failed");
                }
            }
            
            /*if (!c.getProtectionDomain().getCodeSource().implies(cs))
            {
                return fail("The code source of the loaded class doesn't imply the specified code source. Test failed");
            }*/            
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return error("An error has occured while creating URL.");
        } /*catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("An error has occured while opening certificate file.");
        } catch (CertificateException e) {
            e.printStackTrace();
            return error("An error has occured while generating certificate.");
        }*/ catch (ClassNotFoundException e) {            
            e.printStackTrace();
            return error("An error has occured while loading a class.");
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }                    
            } catch (IOException e) {
                e.printStackTrace();                
            }            
        }
        
        return pass("Test passed.");
    }
    
    public class MySecureClassLoader extends SecureClassLoader {        
        CodeSource cs = null;
        String jarfile = null;
        Certificate[] certificate = null;
        
        public MySecureClassLoader(ClassLoader parent, String jarfile) {
            super(parent);
            this.jarfile = jarfile;
        }
        
        protected Class findClass(String name) throws ClassNotFoundException 
        {            
            byte[] classBytes = null;
            try {
                classBytes = loadClassBytes(jarfile, name);                
            } catch (IOException e) {
                e.printStackTrace();
                throw new ClassNotFoundException(name); 
            }
            
            if (classBytes == null) {
                return null;
            }
            
            try {
                if (System.getProperty("os.name").indexOf("Win") != -1) {                
                    cs = new CodeSource(new URL("file:/" + this.jarfile), new Certificate[] {});
                } else {
                    cs = new CodeSource(new URL("file:" + this.jarfile), new Certificate[] {});
                }
                //log.info("Constructed codesource:" + cs);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            
            Class cl;
            try 
            {                
                log.info("Defining class " + name + "...");
                cl = defineClass(name, classBytes, 0, classBytes.length, cs);                
                log.info("OK");
            }
            catch (ClassFormatError cfe)
            {
                cfe.printStackTrace();
                return null;
            }
            
            catch (SecurityException se)
            {
                se.printStackTrace();
                return null;
            }
            
            if (cl == null)
                throw new ClassNotFoundException(name);
            
            return cl;
        }  
        
        private byte[] loadClassBytes(final String jarfile, String classname) throws IOException {            
            byte[] classbytes = null;
            try {
                JarFile jarFile = new JarFile(jarfile);
                try {
                    JarEntry jarEntry = jarFile.getJarEntry(classname.replace('.', '/') + ".class");                    
                    if (jarEntry != null) {
                        //log.info(jarEntry.getAttributes());
                        int size = (int)jarEntry.getSize();                        
                        classbytes = new byte[size];
                        InputStream fis = jarFile.getInputStream(jarEntry);
                        try {
                            fis.read(classbytes);
                            certificate = jarEntry.getCertificates();
                        } finally {
                            if (fis != null) {
                                fis.close();
                            }
                        }                    
                    }                    
                } finally {
                    jarFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            
            return classbytes;
        }        
    }

    public static void main(String[] args) 
    {
        System.exit(new F_CodeSourceTest_01().test(args));
    }
}
