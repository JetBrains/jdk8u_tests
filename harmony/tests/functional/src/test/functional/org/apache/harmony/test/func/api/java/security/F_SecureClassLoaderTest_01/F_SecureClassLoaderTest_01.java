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
package org.apache.harmony.test.func.api.java.security.F_SecureClassLoaderTest_01;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.CodeSource;
import java.security.GeneralSecurityException;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.security.SecurityPermission;
import java.security.SignatureException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.InflaterInputStream;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 *    Usage: java F_SecureClassLoaderTest_01 <path> <classname>
 *           <path> - path to helloworld.cert and testkey.cert files.
 *           <classname> - name of loading class.
 * 
 * 
 * Test loads class from jar file which signed with some certificate and check 
 * 
 */

public class F_SecureClassLoaderTest_01 extends ScenarioTest {
    
    private String path;
    private String classname;
    
    public int task(Class c) {
        
        System.setSecurityManager(new SecurityManager());        
        ProtectionDomain pd = c.getProtectionDomain();   
        log.info(""+pd);
        log.info("codeBase=\"" + c.getProtectionDomain().getCodeSource().getLocation() + "\"");
        Policy policy = Policy.getPolicy();
        for (Enumeration e = policy.getPermissions(pd).elements(); e.hasMoreElements() ;) {
            log.info(""+e.nextElement());
        }
        
        // check permissions set
        if (!policy.implies(pd, new SecurityPermission("getPolicy"))) {
            return fail("Error: java.security.SecurityPermission 'getPolicy' is absent, but must be here");
        }
        
        if (policy.implies(pd, new SecurityPermission("getDomainCombiner"))) {
            return fail("Error: java.security.SecurityPermission 'getDomainCombiner' is present, but must not be here");
        }
        
        if (!policy.implies(pd, new RuntimePermission("accessClassInPackage.COM.TEST.security.Something"))) {
            return fail("Error: java.lang.RuntimePermission 'accessClassInPackage.COM.TEST.security.Something' is absent, but must be here");
        }
        if (!policy.implies(pd, new FilePermission("D:/root/next/-", "read, write"))) {
            return fail("Error: java.io.FilePermission 'D:/root/next/-' is absent, but must be here");
        }
        
        //check certificates set
        /*Certificate certfromfile;
        Certificate certfromfile2;
        try {
            FileInputStream fis = new FileInputStream(path+"helloworld.cert");
            FileInputStream fis2 = new FileInputStream(path+"testkey.cert");
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                certfromfile = cf.generateCertificate(fis);
                certfromfile2 = cf.generateCertificate(fis2); 
            } catch (CertificateException e) {
                return error(e.toString());
            }
        } catch (IOException e) {
            return error(e.toString());
        }*/
        
        Certificate[] cert1 = pd.getCodeSource().getCertificates();
        if (cert1 != null) {
        
        cert1 = null;
        Certificate[] cert2 = pd.getCodeSource().getCertificates();
        if (cert2 == null) {
            return fail("There is a security breach in CodeSource.getCertificates() implementation. Test failed.");
        }
        
        log.info("Number of certificates: " + cert2.length);
        for (int i = 0; i < cert2.length; i++) {
            log.info("Certificate " + (i + 1) + ": " + cert2[i]);
        }
        /*if (certfromfile != null && certfromfile2 != null) {
            log.info("number of certificates = " + cl.length);
            for(int i=0; i<cl.length; i++) {
                try {
                    Certificate cert = cl[i];
                    if (cert.getEncoded().length != certfromfile.getEncoded().length) {
                        return fail("error in getEncoded().length");
                    }
                    try {
                        cert.verify(certfromfile.getPublicKey());
                    } catch (GeneralSecurityException e) {
                        return error(e.toString());
                    }                
                    try {
                        cert.verify(certfromfile2.getPublicKey());
                    } catch (SignatureException e) {
                        //it's right way
                    } catch (GeneralSecurityException e) {
                        return error(e.toString());
                    }                
                } catch (CertificateEncodingException e) {
                    return error(e.toString());
                }
            }
        }*/

        }
        //(c.newInstance()).main();
        return pass("Test passed");
    }
    
    public int test() {
        testArgs[1] = testArgs[1].replace('\\', '/');
        log.info(testArgs[1]);
        SCL scl = new SCL(testArgs[1]);
        classname = testArgs[0]; 
        try {
            log.info("Load class: " + classname + " ...");
            Class c = scl.loadClass(classname);
            if (c == null) {
                return error("Couldn't load class " + classname); 
            }
            log.info("OK");
            return task(c);
            
        } catch (ClassNotFoundException e) {
            return error(e.toString());
        }
    }    

    public static void main(String[] args) {
        System.exit(new F_SecureClassLoaderTest_01().test(args));        
    }
    
    public class SCL extends SecureClassLoader {        
        CodeSource cs = null;
        String jarfile = null;
        Certificate[] certificate = null;
        
        public SCL(String jarfile) {
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
        
/*        private byte[] loadClassBytes(String name) throws IOException {            
            log.info("Loading class " + name);
            try {
                is  = F_SecureClassLoaderTest_01.class.getResourceAsStream("/helloworld/" + name.replace('.', '/') + ".class");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }            
            
            if (is == null) {
                log.info("Couldn't find the requested resource.");
                return null;
            }              
            iis = new InflaterInputStream(is);
            log.info(iis);
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] classBytes = null; 
            try {                
                int ch;
                while (iis.available() > 0) {
                    ch = iis.read();
                    log.info(ch);
                    out.write(ch);
                }
            } catch (IOException e) {            
               e.printStackTrace();
               return null;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            
            return out.toByteArray();            
            
        }*/       
    }
}
