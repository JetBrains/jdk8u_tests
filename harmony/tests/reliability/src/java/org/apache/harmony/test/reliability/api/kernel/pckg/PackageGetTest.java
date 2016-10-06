/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
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
 * @author Nikolay Bannikov
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.kernel.pckg;

import org.apache.harmony.test.reliability.share.Test;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;


/**
 * Goal: find resource leaks or intermittent failures caused by Package.getName() / getImplementationTitle() / 
 * getImplementationVendor() / getImplementationVersion() / isSealed operations.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize in a cycle
 *    2. Excutes a cycle of param[0] iterations, on each iteration:
 *          calls 'get' methods of an package
 *          check result 
 */

public class PackageGetTest extends Test {

    public int NUMBER_OF_ITERATIONS = 1000000;

    static String pkgName = "auxiliary";    

    static String name = "auxiliary.Test";    

    static String urlstr = "jar:file:test.jar!/";

    public static void main(String[] args) {
        System.exit(new PackageGetTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        URL url = null;

        try {
            url = new URL(urlstr);
        } catch(MalformedURLException e) {
            log.add(e.toString());
            return fail("Failed");
        }

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            if (!checkGetPackage(url, pkgName, name)) {
                return fail("Failed.");
            }
            if (i % 10000 == 0) {
                System.gc();
                // log.add("Iteration: " + i);
            }
        }
        
        return pass("OK");
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }
    }

    public boolean checkGetPackage(URL url, String pkg, String className)  {

        boolean result = true;

        URL[] urls = new URL[] {url};

        URLClassLoader classLoader = new URLClassLoader(urls);

        Class loadClass = null;
        try {
            loadClass = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        Package p = loadClass.getPackage();

        if(!p.getName().equals(pkg)) {
            log.add("Failed:package name = " + p.getName());        
        }

        if(p.getImplementationTitle() != null) {
            log.add("implementation Title should be equals null ");
            result = false;
        }
        if(p.getImplementationVendor() != null) {
            log.add("implementation Vendor should be equals null ");
            result = false;
        }
        if(p.getImplementationVersion() != null) {
            log.add("implementation Version should be equals null ");
            result = false;
        }
        if(p.getSpecificationTitle() != null) {
            log.add("Specification Title should be equals null ");
            result = false;
        }
        if(p.getSpecificationVendor() != null) {
            log.add("Specification Vendor should be equals null ");
            result = false;
        }
        if(p.getSpecificationVersion() != null) {
            log.add("Specification Version should be equals null ");
            result = false;
        }

        if(p.isSealed() != p.isSealed(url)) {
            log.add("Failed: isSealed shold be equals isSealed_url");        
            result = false;
        }
        return result;
    }


}

