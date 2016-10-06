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
package org.apache.harmony.test.func.api.javax.management.loading;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.loading.MLet;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Test for the class javax.management.loading.MLet
 * 
 */

public class ExtMLetTest extends MultiCase {

    /**
     * URL to mlet conf text file.
     */
    public String MLET_URL;
    
    /**
     * Test for the method findClass()
     * 
     * @see javax.management.loading.mlet#findClass()
     */
    public Result testFindClass() throws Exception {
        try {
            Class cls;
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            ExtMLet mlet = new ExtMLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            mlet.addURL(MLET_URL+"mbeans.jar");
            cls = mlet.findClass("FirstRemoteClass");
            if (cls == null)
                return failed("method MLet.findClass return wrong value = "
                        + cls);
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected Exception occur");
        }
        return result();
    }
    
    /**
     * Test for the method findClass()
     * 
     * @see javax.management.loading.mlet#findClass()
     */    
    public Result testNotFindClass() throws Exception {
        try {
            Class cls;
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            ExtMLet mlet = new ExtMLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "mlet1.conf" },
                    new String[] { String.class.getName() });

            cls = mlet.findClass("FirstRemoteClass");
            System.out.println(cls);
            if (cls != null)
                return failed("method MLet.findClass return wrong value = "
                        + cls);
        } catch (java.lang.LinkageError e) {
            //e.printStackTrace();
            return passed("Expected Exception occur");
        } catch (Exception e) {
            //e.printStackTrace();
            return failed("Unexpected Exception occur");
        }
        return failed("No Exceptions");
    }
    /**
     * Test for the method findClass()
     * 
     * @see javax.management.loading.mlet#findClass()
     */    
    public Result testNotMLetFindClass() throws Exception {
        try {
            Class cls;
            ExtMLet mlet = new ExtMLet();
            cls = mlet.findClass("");
            if (cls != null)
                return failed("method MLet.findClass return wrong value = ");

        } catch (java.lang.ClassNotFoundException e) {
            //e.printStackTrace();
            return passed("Expected Exception occur");
        } catch (Exception e) {
            //e.printStackTrace();
            return failed("Unexpected Exception occur");
        }
        return failed("No Exceptions");
    }

    /**
     * Test for the method findLibrary()
     * 
     * @see javax.management.loading.mlet#findLibrary()
     */    
/*    public Result testMLetFindDll() throws Exception {

        try {
            ExtMLet mlet = new ExtMLet();
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "dll.conf" },
                    new String[] { String.class.getName() });
            //System.out.println(mlet.findLibrary("test"));
            if (mlet.findLibrary("test") == null)
                return failed("method MLet.findLibrary return wrong value = "
                        + mlet.findLibrary("test"));

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected Exception occur");
        }
        return result();
    }*/
    /**
     * Test for the method findLibrary()
     * 
     * @see javax.management.loading.mlet#findLibrary()
     */
    public Result testMLetNotFindLibrary() throws Exception {
        try {
            ExtMLet mlet = new ExtMLet();
            String libDir = mlet.findLibrary("");
            if (libDir != null)
                return failed("method MLet.findLibrary return wrong value = "
                        + libDir);

        } catch (Exception e) {
            //e.printStackTrace();
            failed("Unexpected Exception occur");
        }
        return result();
    }

    /**
     * Test for the method findLibrary()
     * 
     * @see javax.management.loading.mlet#findLibrary()
     */
    public Result testMLetFindLibraryNull() throws Exception {
        try {
            String str=null;
            ExtMLet mlet = new ExtMLet();
            String libDir = mlet.findLibrary(str);
            if (libDir != null)
                return failed("method MLet.findLibrary return wrong value = "
                        + libDir);

        } catch (NullPointerException e) {
            //e.printStackTrace();
            passed("Expected Exception occur");
        } catch (Exception e) {
            //e.printStackTrace();
            failed("Unexpected Exception occur");
        }
        return result();
    }
    
    /**
     * Test for the method loadClass()
     * 
     * @see javax.management.loading.mlet#loadClass()
     */
    public Result testMLetLoadClass() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "mlet1.conf" },
                    new String[] { String.class.getName() });
            mlet.loadClass("FirstRemoteClass");
        } catch (Exception e) {
            //e.printStackTrace();
            return failed("Unexpected Exception occur");
        }

        return result();
    }
    
    /**
     * Test for the method loadClass()
     * 
     * @see javax.management.loading.mlet#loadClass()
     */
    public Result testMLetLoadNotAClass() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            mlet.loadClass("");
        } catch (java.lang.ClassNotFoundException e) {
            //e.printStackTrace();
            return passed("Expected Exception occur");
        } catch (Exception e) {
            //e.printStackTrace();
            return failed("Unexpected Exception occur");
        }
        return failed("No Exceptions");
    }

    public static void main(String[] args) {
        ExtMLetTest run = new ExtMLetTest();
        run.MLET_URL= args[0]+ "/mlet/";
        System.exit(run.test(args));
        
    }
}