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

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Test for the class javax.management.loading.MLet
 * 
 */

public class MLetClassLoaderTest extends MultiCase {

    /**
     * URL to mlet conf text file.
     */
    public String MLET_URL;
    
    /**
     * Test for the method findClass()
     * 
     * @see javax.management.loading.mlet#findClass()
     */
    public Result testServerChk() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            ExtMLet mlet = new ExtMLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            mlet.addURL(MLET_URL+ "mbeans.jar");
            Class cls = mlet.findClass("FirstRemoteClass");
            System.out.println("test 3 " + (cls));
        } catch (Exception e) {
            //e.printStackTrace();
            return failed("Unexpected Exception");
        }

        return result();
    }

    /**
     * Test for the method findClass()
     * 
     * @see javax.management.loading.mlet#findClass()
     */
    public Result testLinkageErrorChk() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            ExtMLet mlet = new ExtMLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
            + "/SerChk.conf" }, new String[] { String.class.getName() });
            if (!server.isRegistered(new ObjectName("test:name=AnSerTest")))
                return failed("MBean AnSerTest(ser class) does not registered by the server");
            mlet.findClass("AnSerTest");
            
        } catch (java.lang.LinkageError e) {
            //e.printStackTrace();
            return passed("Passsed Expected exception java.lang.LinkageError ");
        } catch (Exception e) {
            //e.printStackTrace();
            return failed("Unexpected Exception");
        }
        return failed("java.lang.LinkageError exception ia absence");

    }

    /**
     * Test for the method findClass()
     * 
     * @see javax.management.loading.mlet#findClass()
     */
    public Result testLinkageChk() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            ExtMLet mlet = new ExtMLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "/BaseChk.conf" },
                    new String[] { String.class.getName() });

            if (!server.isRegistered(new ObjectName(
                    "test:name=SecondRemoteClass")))
                return failed("MBean SecondRemoteClass does not registered by the server");

            Class cls = mlet.findClass("org.apache.harmony.test.func.api.javax.management.loading.mbeans.SecondRemoteClass");
            System.out.println("test 2 "+ (cls));
        } catch (java.lang.LinkageError e) {
            //e.printStackTrace();
            return passed("Passsed Expected exception java.lang.LinkageError ");
            
        } catch (Exception e) {
            //e.printStackTrace();
            return failed("Unexpected Exception");
        }
        return failed("java.lang.LinkageError exception ia absence");
    }


    public static void main(String[] args) {

        MLetClassLoaderTest run = new MLetClassLoaderTest();
        run.MLET_URL= args[0]+ "/mlet/";
        System.exit(run.test(args));

    }
}
