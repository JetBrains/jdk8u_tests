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

import java.util.Set;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.loading.MLet;
import java.security.Policy;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Test for the class javax.management.loading.MLet
 * and related classes
 *  
 */

public class SecurityMLetTest extends MultiCase {

    /**
     * URL to mlet conf text file.
     */
    public String MLET_URL ="http://mstmrtd001:8080/mlet/";
    
    /**
     * Path to policy file.
     */
    public String POLICY_FILE;
    
    /**
     * Check that corresponding exception are thrown   
     */
    public Result testBaseSecurityChk1() throws Exception {
        try {
            System.setProperty("java.security.policy", "");
            Policy.getPolicy().refresh();
            System.setSecurityManager(new SecurityManager());
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet1");
            server.registerMBean(mlet, name);
            Object obj = server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "mlet.conf" },
                    new String[] { String.class.getName() });

            boolean res = server.isRegistered(new ObjectName(
                    "test:name=FirstRemoteClass"));
            System.out.println("Is MBean register: " + res);
            if (!res) {
                Set set = (Set) obj;
                ((Exception) (set.iterator().next())).printStackTrace();
                return passed();
            } else {

                return failed("Exception doesn't throw");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return passed();
        }
    }
    
    /**
     * Check that Security Manager work properly with mlet service 
     */
    public Result testBaseSecurityChk2() throws Exception {
        try {
            System.setProperty("java.security.policy", POLICY_FILE);
            Policy.getPolicy().refresh();
            System.setSecurityManager(new SecurityManager());
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            MLet mlet = new MLet();
            ObjectName name = new ObjectName("test:name=mlet1");
            server.registerMBean(mlet, name);
            server.invoke(name, "getMBeansFromURL",
                    new Object[] { MLET_URL + "mlet.conf" },
                    new String[] { String.class.getName() });

            boolean res = server.isRegistered(new ObjectName(
                    "test:name=FirstRemoteClass"));
            System.out.println("Is MBean register: " + res);
            } catch (Exception e) {
            e.printStackTrace();
        }
            return result();
    }

    public static void main(String[] args) {

        SecurityMLetTest run = new SecurityMLetTest();
        run.MLET_URL = args[0] + "/mlet/";
        run.POLICY_FILE = args[1];
        System.exit(run.test(args));
    }
}