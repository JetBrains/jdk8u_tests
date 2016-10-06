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
/**
 */

package org.apache.harmony.test.func.api.java.net.URLClassLoader;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.IOException;

public class URLClassLoaderTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new URLClassLoaderTest().test(args));
    }

    boolean checkSecurityManager() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {
                sm.checkCreateClassLoader();
            } catch (SecurityException e) {
                return false;
            }
        }
        return true;
    }

    /*
    * java.net.URLClassLoader.findClass(String)
    */
    public Result testFindClass() throws MalformedURLException {
        //convert current classpath into a set of URLs
        //where we could find this class to test findClass()
        String cp = System.getProperty("java.class.path");
        String[] pathElemStrings = cp.split(File.pathSeparator);
        URL[] urls = new URL[pathElemStrings.length];
        for (int i = 0; i < pathElemStrings.length; i++) {
            urls[i] = (new File(pathElemStrings[i])).toURL();
        }

        URLClassLoader cl = null;
        try {
            cl = new MyURLClassLoader(urls);
        } catch (SecurityException e) {
            if (checkSecurityManager()) {
                return failed("Unexpected SecurityException while creating" 
                    + " a ClassLoader");
            }
            return passed("OK. Expected SecurityException has been thrown"
                + "while creating a ClassLoader");
        }
        Class clazz = null;
        //find this class
        try {
            clazz = ((MyURLClassLoader) cl).findClass(getClass().getName());
        } catch (ClassNotFoundException e) {
            return failed("Could not find class: " + clazz.toString());
        }
        //check name of found class
        if (!clazz.getName().equals(getClass().getName())) {
            return failed("Wrong class found: " + clazz.toString()
                + "\n instead of " + getClass().getName());
        }
        //Expect CNFE for nonexisting class
        try {
            clazz = ((MyURLClassLoader) cl).findClass("com.nopackage.NoClass");
            return failed("ClassNotFoundException expected for nonexisting class");
        } catch (ClassNotFoundException e) {
            return passed("OK");
        }
    }

    /*
    * java.net.URLClassLoader.findResource(String)
    */
    public Result testFindResource() throws MalformedURLException {
        File f = new File(System.getProperty("java.io.tmpdir"));
        URL u = f.toURL();
        URL[] urls = {u};
        URLClassLoader cl = null;

        try {
            cl = new URLClassLoader(urls);
        } catch (SecurityException e) {
            if (checkSecurityManager()) {
                return failed("Unexpected SecurityException while creating" 
                    + " a ClassLoader");
            }
            return passed("OK. Expected SecurityException has been thrown"
                + "while creating a ClassLoader");
        } 

        File tmpFile = null;
        String suffixes[] = {".txt", ".jar", ".jpg", ".class"};
        URL res = null;
        for (int i = 0; i < suffixes.length; i++) {
            try {
                tmpFile = File.createTempFile("urlcl", suffixes[i]);
            } catch (IOException e) {
                return new Result(Result.ERROR, "Could not create temporary file");
            }
            res = cl.findResource(tmpFile.getName());
            tmpFile.delete();
            if (res == null) {
                return failed("Could not find an existing resource: " 
                    + u.toString() + tmpFile.getName());
            }
        }
        //Expect null for nonexisting resource
        res = cl.findResource("nonexisting_asdf.resource");
        if (res != null) {
            return failed("Returned non-null value for nonexisting resource: " 
                + u.toString() + "/nonexisting_asdf.resource");
        }
        return passed("OK");
    }
}

/*
* MyURLClassLoader helper
*/
class MyURLClassLoader extends URLClassLoader {
    public MyURLClassLoader(URL[] urls) {
        super(urls);
    }

    //protected method accessor
    protected Class findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
