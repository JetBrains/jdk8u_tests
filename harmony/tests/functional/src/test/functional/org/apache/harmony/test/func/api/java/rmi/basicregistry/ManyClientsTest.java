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
package org.apache.harmony.test.func.api.java.rmi.basicregistry;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import org.apache.harmony.test.func.api.java.rmi.share.ServerInterface;
import org.apache.harmony.test.func.api.java.rmi.share.Util;
import org.apache.harmony.test.func.api.java.rmi.share.dt.RemoteRegistryDt;
import org.apache.harmony.test.func.api.java.rmi.share.dt.RemoteRegistryDt.RegistryWrapper;
import org.apache.harmony.share.Test;
import org.apache.harmony.test.share.dt.DistributedRunner;

/**
 * The test verifies that remote clients can successfully call methods on the
 * local VM. Step by Step:
 * <ul>
 * <li>1. The test rises remote VM (Runtime.exec method used)
 * <li>2. The remote VM creates Client objects (the number of objects are given
 * as a parameters) The test creates one Server object.
 * <li>3. All the clients call remote method from the server and verify that
 * data returned is correct.
 * </ul>
 * The test can rise any number of remote VMs and create any number of Client
 * objects and the main purpose of this test is the usage in the stress test
 * suite
 * 
 */
public class ManyClientsTest extends Test {

    public static void main(String[] args) {
        System.exit(new ManyClientsTest().test(args));
    }

    public int test() {

        /* Wrapper synchronizes VMs and registry */
        RegistryWrapper wrapper;
        /* Runner runs other VM */
        int numberOfCreators = numberOfRemoteVms();
        DistributedRunner runners[] = new DistributedRunner[numberOfCreators];

        /* Create the policy */
        Util.setJavaPolicy();

        /* Create the registry */
        wrapper = RemoteRegistryDt.getRemoteRegistry();

        try {
            int numberOfClients = numberOfClients();

            OtherVMTestInterface creators[] = new OtherVMTestInterface[numberOfCreators];

            String serverName = "rmi://"
                    + wrapper.getRunner().getRemoteSystemHost() + ":"
                    + wrapper.getPort() + "/Server";
            ServerInterface server = new ServerImpl();
            Naming.bind(serverName, server);

            /* create OtherVMTest object in other VM */
            for (int j = 0; j < numberOfCreators; j++) {
                runners[j] = new DistributedRunner("Creator number " + j);
                creators[j] = (OtherVMTestInterface) runners[j].init(
                        OtherVMTest.class, new Class[] { Integer.class },
                        new Object[] { new Integer(numberOfClients) });
                creators[j].initializeClients(serverName, j);
            }
            log.add("All clients have been initialized");

            System.setSecurityManager(new RMISecurityManager());

            for (int j = 0; j < numberOfCreators; j++) {
                if (!creators[j].performVerifications()) {
                    return fail("server returns incorrect data");
                }
            }
            return pass();
        } catch (Throwable ex) {
            log.add(ex);
            return error(ex.toString());
        } finally {
            System.setSecurityManager(null);
            for (int i = 0; i < numberOfCreators; i++) {
                runners[i].shutdown();
            }
        }
    }

    private int numberOfRemoteVms() {
        for (int i = 0; i < testArgs.length; i++) {
            if ("-numberOfRemoteVMs".equals(testArgs[i])) {
                if ((i + 1) < testArgs.length) {
                    int result = Integer.parseInt(testArgs[i + 1]);
                    log.add("number of remote virtual java machines is "
                            + result);
                    return result;
                }
            }
        }
        log.add("number of remote virtual java machines is 1 (default)");
        return 1;
    }

    private int numberOfClients() {
        for (int i = 0; i < testArgs.length; i++) {
            if ("-numberOfClients".equals(testArgs[i])) {
                if ((i + 1) < testArgs.length) {
                    int result = Integer.parseInt(testArgs[i + 1]);
                    log.add("number of clients is " + result);
                    return result;
                }
            }
        }
        log.add("number of clients is 1 (default)");
        return 1;
    }
}
