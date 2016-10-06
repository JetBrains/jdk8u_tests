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

import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.loading.MLet;
import javax.management.MBeanException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Test for the MLet Parser 
 * 
 */
public class TheMLetTagTest extends MultiCase {

    /**
     * URL to mlet conf text file.
     */
    public String MLET_URL;
    
    /**
     * Test for mandatory: Tags CODE, ARCHIVE, NAME
     */
    public Result testBaseChk() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "BaseChk.conf" },
                    new String[] { String.class.getName() });
            if (!server.isRegistered(new ObjectName(
                    "test:name=SecondRemoteClass")))
                return failed("MBean SecondRemoteClass does not registered by the server");
        } catch (Exception e) {
            return failed("Unexpected Exception");
        }
        return result();
    }

    /**
     * Test for mandatory: Tags OBJECT, ARCHIVE, NAME 
     */
    public Result testSerChk() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");
            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "SerChk.conf" },
                    new String[] { String.class.getName() });
            if (!server.isRegistered(new ObjectName("test:name=AnSerTest")))
                return failed("MBean AnSerTest(ser class) does not registered by the server");

            server.getMBeanInfo(new ObjectName("test:name=AnSerTest")).getDescription();
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected Exception");
        }
        return result();
    }

    /**
     * Test for mandatory: Tags CODE, ARCHIVE, NAME, CODEBASE and arglist 
     */

/*    public Result testAllTags() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                            + "AllTags.conf" }, new String[] { String.class
                            .getName() });

            if (!server.isRegistered(new ObjectName(
                    "test:name=ThirdRemoteClass")))
                return failed("MBean ThirdRemoteClass does not registered by the server");
        } catch (Exception e) {
            return failed("Unexpected Exception");
        }
        return result();
    }
*/
    /**
     * Test for mandatory: Tags CODE, OBJECT.
     * Exception should be thrown if it is exist in conf mlet text file  
     */

    public Result testCODEAndOBJECT() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "CODEAndOBJECT.conf" }, new String[] { String.class
                    .getName() });

        } catch (Exception e) {
            return failed("Unexpected Exception");
        }
        return result();
    }

    /**
     * Exception should be thrown if in conf mlet text file exist unexpected end of file 
     */

    public Result testUnexpCfg() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "unexp.conf" }, new String[] { String.class.getName() });
        } catch (MBeanException e) {
            //e.printStackTrace();
            return passed();
        } catch (Exception exp) {
            //exp.printStackTrace();
            return failed("Unexpected Exception");

        }
        return failed("javax.management.MBeanException is absence");
    }

    /**
     * Exception should be thrown if in conf mlet text file absence mandatory ARCHIVE tag 
     */
    public Result testMissArc() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                            + "MissArc.conf" }, new String[] { String.class
                            .getName() });
        } catch (MBeanException e) {
            //e.printStackTrace();
            return passed();
        } catch (Exception exp) {
            //exp.printStackTrace();
            return failed("Unexpected Exception");

        }
        return failed("javax.management.MBeanException is absence");
    }

    /**
     * Exception should be thrown if in conf mlet text file out of end 
     */
    public Result testOutOfEnd() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "OutOfEnd.conf" },
                    new String[] { String.class.getName() });
        } catch (MBeanException e) {
            //e.printStackTrace();
            return passed();
        } catch (Exception exp) {
            //exp.printStackTrace();
            return failed("Unexpected Exception");

        }
        return failed("javax.management.MBeanException is absence");
    }

    
    /**
     * Exception should be thrown if in conf mlet text is empty
     */
    public Result testEmptyConf() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "empty.conf" }, new String[] { String.class.getName() });
        } catch (MBeanException e) {
            //e.printStackTrace();
            return passed();
        } catch (Exception exp) {
            //exp.printStackTrace();
            return failed("Unexpected Exception");
        }
        return failed("javax.management.MBeanException is absence");
    }

    /**
     * Test for mandatory: Tags CODE, ARCHIVE, NAME, CODEBASE and arglist 
     * Check that mlet service work properly with list of remote classes
     */
    public Result testListOfMlets() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "ListMLet.conf" },
                    new String[] { String.class.getName() });
        } catch (Exception exp) {
            exp.printStackTrace();
            return failed("Unexpected Exception");
        }
        return result();
    }
    
    /**
     * Test for mandatory: Tags CODE, ARCHIVE, NAME
     * Check that mlet service work properly with comments in mlet text conf file 
     */
    public Result testMletsComments() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "ComMLet.conf" },
                    new String[] { String.class.getName() });
        } catch (Exception exp) {
            exp.printStackTrace();
            return failed("Unexpected Exception");
        }
        return result();
    }

    /**
     * Test for mandatory: Tags CODE, OBJECT, ARCHIVE, NAME, CODEBASE and arglist
     * Exception should be thrown if in conf mlet text file list of mlets
     */
    public Result testWrongListOfMlets() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            Object obj = server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "WrongListMlet.conf" },
                    new String[] { String.class.getName() });
            Set set = (Set) obj;
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Object obj2 = i.next();
                if (obj2 instanceof javax.management.InstanceAlreadyExistsException) {

                    return passed("Expected exception");
                } if(obj2 instanceof Exception) {

                    return failed("Unexpected exception");
                }
            }
        } catch (Exception exp) {
            //exp.printStackTrace();
            return failed("Unexpected Exception");
        }
        return result();
    }

    /**
     * Test for mandatory: Tags CODE, OBJECT, ARCHIVE, NAME, CODEBASE and arglist
     * Exception should be thrown if in conf mlet text file list of remote classes
     */
    public Result testWrongListOfCalsses() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "DiffNameChk.conf" }, new String[] { String.class
                    .getName() });
            if (!server.isRegistered(new ObjectName(
                    "test:name=SecondRemoteClass")))
                return failed("MBean SecondRemoteClass does not registered by the server");
            if (!server.isRegistered(new ObjectName(
                    "test:name=FirstRemoteClass")))
                return failed("MBean SecondRemoteClass does not registered by the server");
        } catch (Exception exp) {
            //exp.printStackTrace();
            return failed("Unexpected Exception");
        }
        return result();
    }

    /**
     * Exception should be thrown if in conf mlet text file absence mandatory CODE tag 
     */
    public Result testMissCode() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "MissCode.conf" },
                    new String[] { String.class.getName() });
        } catch (MBeanException e) {
            //e.printStackTrace();
            return passed();
        } catch (Exception exp) {
            //exp.printStackTrace();
            return failed("Unexpected Exception");

        }
        return failed("javax.management.MBeanException is absence");
    }

    /**
     * Exception should be thrown if conf mlet text file not a conf mlet file
     */
    public Result testNoconf() throws Exception {

        try {
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet");

            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL", new Object[] { MLET_URL
                    + "not.conf" }, new String[] { String.class.getName() });
        } catch (MBeanException e) {
            //e.printStackTrace();
            return passed();
        } catch (Exception exp) {
            //exp.printStackTrace();
            return failed("Unexpected Exception");

        }
        return failed("javax.management.MBeanException is absence");
    }

    public static void main(String[] args) {
        
        TheMLetTagTest run = new TheMLetTagTest();
        run.MLET_URL=args[0]+ "/mlet/";
        System.out.println(run.MLET_URL);
        System.exit(run.test(args));

    }
}