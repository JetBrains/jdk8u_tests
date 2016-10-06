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
package org.apache.harmony.test.func.api.java.rmi.basicexception;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import org.apache.harmony.test.func.api.java.rmi.share.Util;
import org.apache.harmony.test.func.api.java.rmi.share.dt.RemoteRegistryDt;
import org.apache.harmony.test.func.api.java.rmi.share.dt.RemoteRegistryDt.RegistryWrapper;
import org.apache.harmony.share.Test;
import org.apache.harmony.test.share.dt.DistributedRunner;

/**
 * 
 * <b>Name:</b> BasicExceptionTest<br>
 * <b>Purpose:</b> Test basic RMI exceptions<br>
 * <b>Test design:</b>
 * <ul>
 * <li> 1. Server part creates and exports a remote object which has 4 remote
 * methods each of which throws (in the throws clause and/or in the body) an
 * exception.
 * <li> 2. Client part calls the remote methods and checks, that appropriate
 * exception is caught (with the same detail message).
 * </ul>
 * 
 */
public class BasicExceptionTest extends Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
         System.exit(new BasicExceptionTest().test(args));
    }

    /**
     * {@inheritDoc}
     */
    public int test() {
        OtherVMTestInterface testOnOtherVm = null;
        RegistryWrapper wrapper;
        DistributedRunner runner;

        Util.setJavaPolicy();

        wrapper = RemoteRegistryDt.getRemoteRegistry();
        runner = wrapper.getRunner();

        boolean res = true;

        try {
            ServerInterface obj;
            testOnOtherVm = (OtherVMTestInterface) runner.init(OtherVMTest.class, null,
                    null);

            String name = testOnOtherVm.bind(runner.getRemoteSystemHost(), wrapper
                    .getPort());

            System.setSecurityManager(new RMISecurityManager());
            obj = (ServerInterface) Naming.lookup(name);

            /* Check public int run1(int i) throws AccessException */
            try {
                obj.run1();
            } catch (Exception ex) {
                if (!(ex instanceof java.rmi.ServerException)
                        && !(ex instanceof RemoteException)
                        || !(ex.getCause().getMessage().equals("hallo"))) {

                    log.add("Wrong exception was caught:");
                    log.add("java.rmi.ServerException: "
                            + "RemoteException expected");
                    log.add(ex.toString() + " received");
                    res = false;
                }
            }

            /* Check public int run2(int i) throws Exception */
            try {
                obj.run2();
            } catch (Exception ex) {
                if (!(ex instanceof java.rmi.ServerException)
                        && !(ex instanceof RemoteException)
                        || !(ex.getCause().getMessage().equals("hallo"))) {
                    log.add("Wrong exception was caught:");
                    log.add("java.rmi.ServerException: "
                            + "RemoteException expected");
                    log.add(ex.toString() + " received");
                    res = false;
                }
            }

            /* Check public int run3(int i) */
            try {
                obj.run3();
            } catch (Exception ex) {
                if (!(ex instanceof java.lang.RuntimeException)
                        || !(ex.getMessage().equals("hallo"))) {
                    log.add("Wrong exception was caught:");
                    log.add("java.lang.RuntimeException expected");
                    log.add(ex.toString() + " received");
                    res = false;
                }
            }

            try {
                obj.run4();
            } catch (Throwable ex) {
                if (!(ex instanceof java.lang.ClassCastException)) {
                    log.add("Wrong exception was caught:");
                    log.add("java.lang.ClassCastException expected");
                    log.add(ex.toString() + " received");
                    res = false;
                }
            }

            return res ? pass() : fail("FAIL");

        } catch (Throwable ex) {
            log.add(ex);
            return error(ex.toString());
        } finally {
            testOnOtherVm.removeSecurityManager();
            System.setSecurityManager(null);
            runner.shutdown();
        }
    }
}
