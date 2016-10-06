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
package org.apache.harmony.test.func.api.java.rmi.basicnaming;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import org.apache.harmony.test.func.api.java.rmi.share.Util;
import org.apache.harmony.test.func.api.java.rmi.share.dt.RemoteRegistryDt;
import org.apache.harmony.test.func.api.java.rmi.share.dt.RemoteRegistryDt.RegistryWrapper;
import org.apache.harmony.share.Test;
import org.apache.harmony.test.share.dt.DistributedRunner;

/**
 * <b>Test design:</b>
 * <ul>
 * <li> Server and client are started. Then o1, o2, o3, o4 remote objects at
 * server and n1, n2, n3, n4 strings both at server and at client are created.
 * Then the following actions are tried to perform:
 * <li> Server:<br>
 * 
 * bind(n1,o1); <br>
 * run registry <br>
 * bind(n1,o1); <br>
 * bind(n2,o2); <br>
 * bind(n1,o2); <br>
 * bind(n3,o1); <br>
 * rebind(n1,o3); <br>
 * bind(n4,o4); <br>
 * unbind(n4);
 * 
 * <li> Client (starts working after all those actions at server complete): <br>
 * o1=lookup(n1); <br>
 * o2=lookup(n2); <br>
 * o3=lookup(n3); <br>
 * o4=lookup(n4); <br>
 * call o1.some_method(); <br>
 * call o2.some_method(); <br>
 * call o3.some_method();
 * 
 * <li> It is verified, that result of every action is correct (wrong actions
 * cause proper exception, right actions don't cause any exception and remote
 * method calls return correct result (which accords proper remote object which
 * should be bound to that name))
 * </ul>
 * 
 */
public class BasicNamingTest extends Test {

    public static void main(String[] args) {
        System.exit(new BasicNamingTest().test(args));
    }

    public int test() {

        boolean res = true;

        OtherVMTestInterface testOnOtherVm1 = null;
        OtherVMTestInterface testOnOtherVm2 = null;
        OtherVMTestInterface testOnOtherVm3 = null;
        OtherVMTestInterface testOnOtherVm4 = null;
        final RegistryWrapper wrapper;
        final DistributedRunner runner;
        Util.setJavaPolicy();
        wrapper = RemoteRegistryDt.getRemoteRegistry();
        runner = wrapper.getRunner();

        try {
            ServerInterface obj1;
            ServerInterface obj2;
            ServerInterface obj3;

            testOnOtherVm1 = (OtherVMTestInterface) runner.init(OtherVMTest.class, null,
                    null);
            testOnOtherVm2 = (OtherVMTestInterface) runner.init(OtherVMTest.class, null,
                    null);
            testOnOtherVm3 = (OtherVMTestInterface) runner.init(OtherVMTest.class, null,
                    null);
            testOnOtherVm4 = (OtherVMTestInterface) runner.init(OtherVMTest.class, null,
                    null);

            String name_1 = testOnOtherVm1.bind(runner.getRemoteSystemHost(), wrapper
                    .getPort(), 1);
            String name_2 = testOnOtherVm2.bind(runner.getRemoteSystemHost(), wrapper
                    .getPort(), 2);

            try {
                testOnOtherVm1.bind(runner.getRemoteSystemHost(), wrapper.getPort(), 2);
                log.add("Oh no: double bind successful!");
            } catch (Exception e) {
                if (!(e.getCause() instanceof java.rmi.AlreadyBoundException)) {
                    log.add("Wrong exception was caught "
                            + "when  bind doubled");
                    res = false;
                }
            }

            String name_3 = testOnOtherVm1.bind(runner.getRemoteSystemHost(), wrapper
                    .getPort(), 3);

            testOnOtherVm3.rebind(name_1, 100);

            String name_4 = testOnOtherVm4.bind(runner.getRemoteSystemHost(), wrapper
                    .getPort(), 4);

            testOnOtherVm4.unbind(name_4);

            testOnOtherVm4.enableSecurityManager();
            System.setSecurityManager(new RMISecurityManager());

            obj1 = (ServerInterface) Naming.lookup(name_1);
            obj2 = (ServerInterface) Naming.lookup(name_2);
            obj3 = (ServerInterface) Naming.lookup(name_3);

            try {
                Naming.lookup(name_4);
                log.add("Unbinded object found");
                res = false;
            } catch (Exception e) {
                if (!(e instanceof java.rmi.NotBoundException)) {
                    log.add("Wrong exception was caught when "
                            + "unbinded object lookup");
                    res = false;
                }
            }

            /* Now call remote methods */
            if (obj1.remoteMethod() != 100) {
                log.add("Wrong remote method result: "
                        + "first object, result is " + obj1.remoteMethod());
                res = false;
            }

            if (obj2.remoteMethod() != 2) {
                log.add("Wrong remote method result: "
                        + "first object, result is " + obj2.remoteMethod());
                res = false;
            }

            if (obj3.remoteMethod() != 3) {
                log.add("Wrong remote method result: "
                        + "first object, result is " + obj3.remoteMethod());
                res = false;
            }

            return res ? pass() : fail("FAIL");

        } catch (Throwable ex) {
            log.add(ex);
            return error(ex.toString());
        } finally {
            System.setSecurityManager(null);
            testOnOtherVm1.removeSecurityManager();
            testOnOtherVm2.removeSecurityManager();
            testOnOtherVm3.removeSecurityManager();
            testOnOtherVm4.removeSecurityManager();
            runner.shutdown();
        }
    }
}
