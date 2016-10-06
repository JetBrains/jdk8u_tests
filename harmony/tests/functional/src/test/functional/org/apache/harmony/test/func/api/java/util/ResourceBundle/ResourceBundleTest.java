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
/*
 * Created on 20.10.2005
 *  
 */

package org.apache.harmony.test.func.api.java.util.ResourceBundle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

class MyResourceBundle extends ResourceBundle {

    public Enumeration getKeys() {
        return null;
    }

    protected Object handleGetObject(String arg0) {

        return null;
    }

}

class MyClassLoader extends ClassLoader {
    private boolean[] flags;

    public MyClassLoader() {
        super();
        flags = new boolean[20];
    }

    public boolean[] getFlags() {
        return flags;
    }

    public synchronized void clearAssertionStatus() {
        flags[1] = true;
        super.clearAssertionStatus();
    }

    protected Package definePackage(String arg0, String arg1, String arg2,
            String arg3, String arg4, String arg5, String arg6, URL arg7)
            throws IllegalArgumentException {

        flags[2] = true;
        return super.definePackage(arg0, arg1, arg2, arg3, arg4, arg5, arg6,
                arg7);
    }

    protected Class findClass(String arg0) throws ClassNotFoundException {

        flags[3] = true;
        return super.findClass(arg0);
    }

    protected String findLibrary(String arg0) {

        flags[4] = true;
        return super.findLibrary(arg0);
    }

    protected URL findResource(String arg0) {

        flags[5] = true;
        return super.findResource(arg0);
    }

    protected Enumeration findResources(String arg0) throws IOException {

        flags[6] = true;
        return super.findResources(arg0);
    }

    protected Package getPackage(String arg0) {

        flags[7] = true;
        return super.getPackage(arg0);
    }

    protected Package[] getPackages() {

        flags[8] = true;
        return super.getPackages();
    }

    public URL getResource(String arg0) {

        flags[9] = true;
        return super.getResource(arg0);
    }

    public InputStream getResourceAsStream(String arg0) {

        flags[10] = true;
        return super.getResourceAsStream(arg0);
    }

    protected synchronized Class loadClass(String arg0, boolean arg1)
            throws ClassNotFoundException {

        flags[11] = true;
        return super.loadClass(arg0, arg1);
    }

    public Class loadClass(String arg0) throws ClassNotFoundException {

        flags[12] = true;
        return super.loadClass(arg0);
    }

    public synchronized void setClassAssertionStatus(String arg0, boolean arg1) {

        flags[13] = true;
        super.setClassAssertionStatus(arg0, arg1);
    }

    public synchronized void setDefaultAssertionStatus(boolean arg0) {

        flags[14] = true;
        super.setDefaultAssertionStatus(arg0);
    }

    public synchronized void setPackageAssertionStatus(String arg0, boolean arg1) {

        flags[15] = true;
        super.setPackageAssertionStatus(arg0, arg1);
    }
}

public class ResourceBundleTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new ResourceBundleTest().test(args));
    }

    public Result testGetBundle() {
        ClassLoader l1 = this.getClass().getClassLoader();
        MyClassLoader l2 = new MyClassLoader();
        
        String bundleName = "org.apache.harmony.test.func.api.java.util.ResourceBundle.someResources";

        boolean[] flags_template = { false, false, false, true, false, true,
                false, false, false, true, true, true, true, false, false,
                false, false, false, false, false, };

        ResourceBundle bundle1 = ResourceBundle.getBundle(bundleName,
                Locale.US, l1);
        
        ResourceBundle bundle2 = ResourceBundle.getBundle(bundleName,
                Locale.US, l2);

        boolean[] flags = l2.getFlags();
        for (int i = 0; i < flags.length; i++) {
            //System.out.println (i + " " + flags[i]);
            if (flags[i] != flags_template[i]) {
                return failed("Compatibility error: " + i
                        + "-th method is (not) called.");
            }
        }

        ResourceBundle[] bundle = new ResourceBundle[6];

        Locale.setDefault(Locale.GERMAN);
        bundle[0] = ResourceBundle.getBundle(bundleName, Locale.getDefault(),
                l1);
        bundle[1] = ResourceBundle.getBundle(bundleName,
                new Locale("fr", "CH"), l1);
        bundle[2] = ResourceBundle.getBundle(bundleName,
                new Locale("de", "AAA"), l1);
        bundle[3] = ResourceBundle.getBundle(bundleName,
                new Locale("de", "DE"), l1);
        
        bundle[4] = ResourceBundle.getBundle(bundleName, Locale.FRANCE, l1);
        
        Locale.setDefault(Locale.US);
        bundle[5] = ResourceBundle.getBundle(bundleName, Locale.getDefault(),
                l1);

        String sample[] = { "someResource_de", "someResource_fr_CH",
                "someResource_de", "someResources_de_DE", "someResource_fr",
                "someResource", };
        for (int i = 0; i <= 5; i++) {
            if (!bundle[i].getString("Key").equals(sample[i])) {
                return failed(i + ": value is " + bundle[i].getString("Key")
                        + ", but should be: " + sample[i]);
            }
            if (bundle[i] instanceof ResourceBundle) {
            } else {
                return failed("Wrong object is created: "
                        + bundle[i].getClass().getName());
            }
        }

        return passed();

    }

    public Result testResourceBundle() {

        ResourceBundle bundle = new MyResourceBundle();

        if (bundle instanceof ResourceBundle) {
            return passed();
        } else {
            return failed("Wrong object is created: "
                    + bundle.getClass().getName());
        }

    }
}