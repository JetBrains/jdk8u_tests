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
/*
 * Created on 21.12.2004
 * Last modification G.Seryakova
 * Last modified on 21.12.2004
 * 
 * 
 * Class loader for testing ClassLoader class.
 */
package org.apache.harmony.test.func.api.java.lang.share;

import java.io.*;
import java.net.URL;

/**
 * 
 * Class loader for testing ClassLoader class.
 */
public class TestClassLoader extends ClassLoader {
    public int loadTracker = 0;
    public ClassLoader parent;
    
    public TestClassLoader() {
        super();
        parent = getParent();
    }
    
    public TestClassLoader(ClassLoader clsLoader) {
        super(clsLoader);
        parent = getParent();
    }
     
    public Class loadClass(String name) throws ClassNotFoundException {
        Class cls;
        if ((cls = findLoadedClass(name)) != null) {
            return cls;
        }
        return super.loadClass(name, false);
    }
    
    public Class find0(String name) {
        return findLoadedClass(name);
    }
    
    public Class findClass(String name) throws ClassNotFoundException {
        loadTracker += 1;
        String fileName = convertName(name);
        Class cls = null;
        try {
            System.err.println("org/apache/harmony/test/func/api/java/lang/" + fileName);
            InputStream source = getResourceAsStream("org/apache/harmony/test/func/api/java/lang/" + fileName);
            DataInputStream dataStream = new DataInputStream(source);
            int size = dataStream.available(); 
            byte[] classData = new byte[size];
            dataStream.readFully(classData);
            String packageName = "org.apache.harmony.test.func.api.java.lang." + extractPackage(name);
            if (getPackage(packageName) == null) {
                definePackage(packageName, "", "", "", "", "", "", null);
            }
            cls = defineClass("org.apache.harmony.test.func.api.java.lang." + name, classData, 0, size, this.getClass().getProtectionDomain());
            if (getPackage(packageName).isSealed()) {
                System.err.println("package mustn't be sealed.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassFormatError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        
        if (cls == null) {
            cls = super.findClass(name);
        }
        return cls;
    }
    
    private String convertName(String name) {
        String result;
        if (name == null) {
            result = name;
        } else {
            result = name.replace('.', '/') + ".class";
        }
        return result;
    }
    
    private String extractPackage(String name) {
        String result;
        if (name == null) {
            result = name;
        } else {
            result = name.substring(0, name.lastIndexOf("."));
        }
        return result;
        
    }
    
    protected  URL findResource(String name) {
        loadTracker += 2;
        return super.findResource(name);
    }
}
