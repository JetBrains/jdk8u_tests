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
 */
public class ManyServersTest extends Test {

    public static void main(String[] args) {
        System.exit(new ManyServersTest().test(args));
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
        // runner = wrapper.getRunner();

        try {
            int numberOfServers = numberOfServers();
            String serverNames[][] = new String[numberOfCreators][numberOfServers];
            ServerInterface servers[][] = new ServerInterface[numberOfCreators][numberOfServers];

            /* create OtherVMTest object in other VM */
            for (int j = 0; j < numberOfCreators; j++) {
                runners[j] = new DistributedRunner("Creator number " + j);
                OtherVMTestInterface creator = (OtherVMTestInterface) runners[j]
                        .init(OtherVMTest.class, null, null);
                for (int i = 0; i < numberOfServers; i++) {
                    serverNames[j][i] = creator.bind(runners[j]
                            .getRemoteSystemHost(), wrapper.getPort(), j, i);
                }
            }

            log.add("All servers have been bound");
            System.setSecurityManager(new RMISecurityManager());

            for (int j = 0; j < numberOfCreators; j++) {
                for (int i = 0; i < numberOfServers; i++) {
                    servers[j][i] = (ServerInterface) Naming
                            .lookup(serverNames[j][i]);

                }
            }

            log.add("All servers have been looked up");

            for (int j = 0; j < numberOfCreators; j++) {
                for (int i = 0; i < numberOfServers; i++) {
                    String s = servers[j][i].remoteMethod("number " + i
                            + ", my creator number is " + j);
                    log.add(s);
                    if (!(("I am server number " + i
                            + ", my creator number is " + j).equals(s))) {
                        return fail("remote method call failed");
                    }
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

    private int numberOfServers() {
        for (int i = 0; i < testArgs.length; i++) {
            if ("-numberOfServers".equals(testArgs[i])) {
                if ((i + 1) < testArgs.length) {
                    int result = Integer.parseInt(testArgs[i + 1]);
                    log.add("number of servers is " + result);
                    return result;
                }
            }
        }
        log.add("number of servers is 1 (default)");
        return 1;
    }
}
