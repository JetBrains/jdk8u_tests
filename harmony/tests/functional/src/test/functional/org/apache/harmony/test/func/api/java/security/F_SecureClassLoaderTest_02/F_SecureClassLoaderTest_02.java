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
package org.apache.harmony.test.func.api.java.security.F_SecureClassLoaderTest_02;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.util.PropertyPermission;

import org.apache.harmony.test.func.share.ScenarioTest;
/**
 * Created on 25.11.2004
 */
public class F_SecureClassLoaderTest_02 extends ScenarioTest 
{
    public int test() 
    {
        System.setSecurityManager(new SecurityManager());
        testArgs[0] = testArgs[0].replace('\\', '/');
        int index = testArgs[0].indexOf(".");
        if (index != -1) {
//            testArgs[0] = testArgs[0].substring(0, index) + testArgs[0].substring(index + 2);
        }
        
        testArgs[3] = testArgs[3].replace('\\', '/');
        index = testArgs[3].indexOf(".");
        if (index != -1) {
//            testArgs[3] = testArgs[3].substring(0, index) + testArgs[3].substring(index + 2);
        }
        
        URL url;
        // An attempt to create an URL for use in construction of the MySecureClassLoader class 
        try
        {
            url = new URL(testArgs[0]);
        }
        catch (MalformedURLException ex)
        {
            ex.printStackTrace();
            return error(ex.getMessage());
        }

        MySecureClassLoader scl = new MySecureClassLoader(url, ClassLoader.getSystemClassLoader());   
        if (scl == null)
        {
            return fail("Couldn't create class loader. tTest failed.");
        }
        
        Class HelloWorldClass = null;
        Class MyPrivilegedActionClass = null;
        try
        {
            MyPrivilegedActionClass = scl.loadClass(testArgs[2]);
            HelloWorldClass = scl.loadClass(testArgs[1]);           
            //MyPrivilegedActionClass = ClassLoader.getSystemClassLoader().loadClass("org.apache.harmony.test.func.api.java.security.F_SecureClassLoaderTest_02.auxiliary.MyPrivilegedAction");
            //HelloWorldClass = ClassLoader.getSystemClassLoader().loadClass("org.apache.harmony.test.func.api.java.security.F_SecureClassLoaderTest_02.auxiliary.HelloWorld");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
            return error(ex.getMessage());
        }        
        
        if (HelloWorldClass == null || MyPrivilegedActionClass == null)
        {
            return fail("Couldn't load specified classes. Test failed.");
        }      
        
        
        // Check whether a class being loaded has all the permissions which are 
        // specified in a policy file.    
        if (!HelloWorldClass.getProtectionDomain().getPermissions().implies(new PropertyPermission("os.name", "read")) ||
            !HelloWorldClass.getProtectionDomain().getPermissions().implies(new PropertyPermission("os.version", "read")) ||
            !HelloWorldClass.getProtectionDomain().getPermissions().implies(new PropertyPermission("os.arch", "read")))
        {
            return fail("Loaded classes' protection domain doesn't have required permissions. Test failed.");
        }        
        
        try 
        {                  
            String[] args = new String[] {};
            Method method = HelloWorldClass.getMethod("main", new Class[] {args.getClass()});            
            method.invoke(null, new Object[] {args});
        }
        catch (Throwable e)
        {
            e.printStackTrace();             
        }
        
        return pass("Test passed");

    }

    public static void main(String[] args) 
    {
        System.exit(new F_SecureClassLoaderTest_02().test(args));
    }
    
    class MySecureClassLoader extends SecureClassLoader
    {
        private CodeSource codeSource = null;
        private PermissionCollection perms = null;
        public MySecureClassLoader()
        {
            this(null, null);
        }
        
        public MySecureClassLoader(URL url)
        {
            this(url, null);
        }
        
        public MySecureClassLoader(URL url, ClassLoader parent)
        {
            super(parent);
            if (url != null)
            {
                codeSource = new CodeSource(url, new Certificate[] {});                
            }
        }        
        
        protected Class findClass(String name) throws ClassNotFoundException 
        {
            byte[] classBytes = null;
            try 
            {
                classBytes = loadClassBytes(name);                
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
                throw new ClassNotFoundException(name); 
            }

            if (classBytes == null) {
                return null;
            }
            
            Class cl;
            try 
            {                
                log.info("Defining class " + name + "...");
                cl = defineClass("auxiliary." + name, classBytes, 0, classBytes.length, codeSource);                
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
        
        private byte[] loadClassBytes(String name) throws IOException {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(testArgs[3] + name.replace('.', '/') + ".class");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int ch;
                while ((ch = fis.read()) != -1) {
                    out.write(ch);
                }                
                
                return out.toByteArray();
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }          
            
        }       
        
/*        private byte[] loadClassBytes(String name) throws IOException 
        {
            InputStream is = null;
            log.info("Loading class " + name);
            try
            {
                //is  = F_SecureClassLoaderTest_02.class.getResourceAsStream("/org/apache/harmony/test/func/api/java/security/F_SecureClassLoaderTest_02/auxiliary/" + name + ".class");
                is  = ClassLoader.getSystemResourceAsStream("org/apache/harmony/test/func/api/java/security/F_SecureClassLoaderTest_02/auxiliary/" + name + ".class");
            }
            catch (Exception e)
            {
                e.printStackTrace();                
            }
            
            if (is == null) {
                return null;
            }                
            
            log.info(is.available());
            byte[] classBytes = null; 
            try
            {
                classBytes = new byte[is.available()];
                is.read(classBytes);
            }
            catch (IOException e)
            {            
               e.printStackTrace();
               return null;
            }           
            finally {
                if (is != null) {
                    is.close();
                }
            }

            FileOutputStream fos = new FileOutputStream("C:\\temp\\" + name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.write(classBytes);
            oos.flush();
            oos.close();
            fos.close();           
            
            return classBytes;            
        }*/      
        
        protected PermissionCollection getPermissions(CodeSource cs) 
        {
            PermissionCollection perms = super.getPermissions(cs);            
            if (perms != null && !perms.isReadOnly())
            {                
                perms.add(new PropertyPermission("os.name", "read"));
                perms.add(new PropertyPermission("os.arch", "read"));
                perms.add(new PropertyPermission("os.version", "read"));
            }
            
            return perms;
        }
    }    
}