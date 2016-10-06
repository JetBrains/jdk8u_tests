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
package org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.harmony.test.func.share.ScenarioTest;
//import auxiliary.AuxiliaryClass;

/**
 * Created on 25.08.2005
 */
public class F_CertificateFactoryTest_01 extends ScenarioTest {

    private String m_JARFile;
    private String m_CertificateFile;
    private String m_auxClass;
    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {
        m_JARFile = testArgs[0];
        m_CertificateFile = testArgs[1];
        m_auxClass = testArgs[2];
        System.setSecurityManager(new SecurityManager());
        
        try {
            FileInputStream fis = new FileInputStream(m_CertificateFile);
            log.info("Acquire an instance of CertificateFactory object");
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            log.info("Generate certificate from the file");
            Certificate cert = certFactory.generateCertificate(fis);
            SecureClassLoader scl = new MySecureClassLoader(ClassLoader.getSystemClassLoader(), m_JARFile);
            log.info("Loading class " + m_auxClass);
            Class cl = scl.loadClass("auxiliary.AuxiliaryClass");
            log.info("Acquiring certificates from the loaded class");
            Certificate[] certs = cl.getProtectionDomain().getCodeSource().getCertificates();
            /*log.info("Compare certificates");
            if (certs[0].equals(cert)) {            
                return pass("Test passed");
            }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } catch (CertificateException e) {
            e.printStackTrace();
            return fail("FAILED: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return error("Error: " + e.getMessage());
        } 

        return pass("Test passed");
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
                cs = new CodeSource(new URL("file:/" + this.jarfile), certificate);
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
    
    public static void main(String[] args) {
        System.exit(new F_CertificateFactoryTest_01().test(args));
    }
}
