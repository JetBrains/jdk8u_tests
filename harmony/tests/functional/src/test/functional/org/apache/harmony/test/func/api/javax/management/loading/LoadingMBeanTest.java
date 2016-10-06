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

import javax.management.loading.MLet;
import javax.management.loading.DefaultLoaderRepository;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Test for the class javax.management.loading.MLet
 * and related classes
 *  
 */

public class LoadingMBeanTest extends MultiCase {

    /**
     * URL to mlet conf text file.
     */
    public String MLET_URL;
    
    /**
     * Test for the constructor MLet()
     * 
     * @see javax.management.loading#MLet()
     */
    public Result testRegister() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);

        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result();
    }

    /**
     * Test for the method getURLs()
     * 
     * @see javax.management.loading.mlet#getURLs()
     */
    public Result testGetURLs() throws Exception {
        MLet mlet = new MLet();
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            mlet.addURL(MLET_URL);
        } catch (Exception e) {
            //e.printStackTrace();
            return failed("Unexpected Exception occur");
        }
        if (mlet.getURLs().length == 1 )
            return passed();
        else
            return failed("Wrong number of URLs returned by getURLs() method");
    }
    
    /**
     * Test for the base mlet loadind functionality via MBeanServer
     * use invoke() method.
     */
    public Result testLoadClass() throws Exception {

        try {
            DefaultLoaderRepository.loadClass("org.apache.harmony.test.func.api.javax.management.loading.mbeans.ThirdRemoteClass");
            failed("class org.apache.harmony.test.func.api.javax.management.loading.mbeans.ThirdRemoteClass was already found in cass loader repository.");
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        try {

            DefaultLoaderRepository.loadClass("org.apache.harmony.test.func.api.javax.management.loading.mbeans.SecondRemoteClass");
            failed("class org.apache.harmony.test.func.api.javax.management.loading.mbeans.ThirdRemoteClass was already found in cass loader repository.");
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        try {
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        MLet mlet = new MLet();
        ObjectName name = new ObjectName("test:name=mlet");
        
        server.registerMBean(mlet, name);
        server.invoke(name, "getMBeansFromURL",new Object[] { MLET_URL+"TrueMlet.conf" },new String[] { String.class.getName() });
        
        if(! server.isRegistered(new ObjectName("test:name=ThirdRemoteClass")))
            return failed("MBean ThirdRemoteClass does not registered by the server");
        if(! server.isRegistered(new ObjectName("test:name=SecondRemoteClass")))
            return failed("MBean SecondRemoteClass does not registered by the server");
            
        server.invoke(new ObjectName("test:name=SecondRemoteClass"), "doOperation",new Object[] { "DrlChk" },new String[] { String.class.getName() });
        server.invoke(new ObjectName("test:name=ThirdRemoteClass"), "doOperation",new Object[] { "DrlChk" },new String[] { String.class.getName() });
        
        }catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected Exception occur");
        }
        return result();
    }

    /**
     * Test for the method getLibraryDirectory()
     * 
     * @see javax.management.loading.mlet#getLibraryDirectory()
     */
    public Result testNativeLib() throws Exception {
        try{
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        MLet mlet = new MLet();
        ObjectName name = new ObjectName("test:name=mlet");
        server.registerMBean(mlet, name);
        mlet.getMBeansFromURL(MLET_URL+"mlet2.conf");
        String libPath = mlet.getLibraryDirectory();
        //System.out.println(libPath);
        if(libPath.equals(null))
            return failed("method MLet.getLibraryDirectory()return wrong value = " + libPath);
    }catch(Exception e){
        //e.printStackTrace();
        return failed("Unexpected Exception occur");
    }
        return result();
    }
    /**
     * Test for the method
     *  getLibraryDirectory()
     *  setLibraryDirectory()
     * 
     * @see javax.management.loading.mlet#setLibraryDirectory()
     */
    
    public Result testSetLibraryDirectory() throws Exception {
        try{
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        MLet mlet = new MLet();
        ObjectName name = new ObjectName("test:name=mlet");
        server.registerMBean(mlet, name);
        mlet.getMBeansFromURL(MLET_URL+"mlet2.conf");
        String libPath = mlet.getLibraryDirectory();
        mlet.setLibraryDirectory(libPath);
        if(libPath.equals(null))
            return failed("method MLet.getLibraryDirectory()return wrong value = " + libPath);
        }catch(Exception e){
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
    public Result testClassCheck() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",new Object[] { MLET_URL+"mlet1.conf" },new String[] { String.class.getName() });
            //mlet.getMBeansFromURL(MLET_URL+"/mlet-1/mlet1.conf");
            Class type = mlet.loadClass("FirstRemoteClass");
            //System.out.println(type);
            if(type.equals(null))
                return failed("method MLet.loadClass return wrong value = " + type);
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected Exception occur");
        }
        return result();
    }
    
    /**
     * Test for the method loadClass()
     * 
     * @see javax.management.loading.mlet#loadClass()
     */
    public Result testExceptionClassCheck() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",new Object[] { MLET_URL+"/mlet-1/mlet1.conf" },new String[] { String.class.getName() });
            Class type = mlet.loadClass("IAmNotAClass");
            System.out.println(type);
            if(type.equals(null))
                return failed("method MLet.loadClass return wrong value = " + type);
        } catch (Exception e) {
            //e.printStackTrace();
            return passed("Expected Exception occur");
        }
        return result();
    }
    public static void main(String[] args) {
        
    
        LoadingMBeanTest run = new LoadingMBeanTest();
        run.MLET_URL= args[0]+ "/mlet/";
        System.exit(run.test(args));
    }
}