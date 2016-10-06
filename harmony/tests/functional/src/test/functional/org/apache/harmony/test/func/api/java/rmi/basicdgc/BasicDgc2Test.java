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
package org.apache.harmony.test.func.api.java.rmi.basicdgc;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import org.apache.harmony.test.func.api.java.rmi.share.ServerInterface;
import org.apache.harmony.test.func.api.java.rmi.share.Util;
import org.apache.harmony.test.func.api.java.rmi.share.dt.RemoteRegistryDt;
import org.apache.harmony.test.func.api.java.rmi.share.dt.RemoteRegistryDt.RegistryWrapper;
import org.apache.harmony.share.Test;
import org.apache.harmony.test.share.dt.DistributedRunner;

/**
 * <b>Test description </b>
 * <ul>
 * <li> 1. Server creates and exports remote object (o1)
 * <li> 2. Registry is started
 * <li> 3. Server registers o1 in the registry
 * <li> 4. Client find o1 in the registry using lookup() method
 * <li> 5. Registry is killed
 * <li> 6. Server calls GC
 * <li> 7. It's verified that client can successfully call remote methods of o1
 * after that
 * <ul>
 * 
 */
public class BasicDgc2Test extends Test {

    private int testCycles = 30;

    public static void main(String[] args) {
        
        BasicDgc2Test tst = new BasicDgc2Test();
        tst.stateTestCyclesParameter();

        System.exit(tst.test(args));
    }

    /**
     * Server managed by OtherVMTest object(initialized in new VM(raised by
     * DistributedRunner))
     */
    public int test() {
        OtherVMTestInterface testOnOtherVm = null;
        RegistryWrapper wrapper;
        DistributedRunner registryRunner;

        Util.setJavaPolicy();

        DistributedRunner testVmRunner = new DistributedRunner();
        wrapper = RemoteRegistryDt.getRemoteRegistry();
        registryRunner = wrapper.getRunner();

        try {
            String result;
            ServerInterface obj;
            SecurityManager manager = new RMISecurityManager();

            testOnOtherVm = (OtherVMTestInterface) testVmRunner.init(OtherVMTest.class,
                    null, null);

            String name = testOnOtherVm.bind(registryRunner.getRemoteSystemHost(), wrapper
                    .getPort());

            testOnOtherVm.enableSecurityManager();
            System.setSecurityManager(manager);

            obj = (ServerInterface) Naming.lookup(name);
            result = obj.remoteMethod("*#-->>");
            
            if (!result.equals("Remote method called. Result is: *#-->>")) {
                log.add("Remote method returns: " + result);
                log.add("RMI server not found or wrong remote result");
                return error("remote method is incorrect");
            }

            System.setSecurityManager(null);
            registryRunner.shutdown();
            testOnOtherVm.removeRemoteObject();
            System.setSecurityManager(manager);

            // Thread.sleep(5000);

            /* Wait for DGC to remove object */
            for (int counter = 0; counter < testCycles; counter++) {
                Thread.sleep(500);
                result = obj.remoteMethod("@-%--");
                if (!result.equals("Remote method called. Result is: @-%--")) {
                    log.add("Remote method returns: " + result);
                    log.add("RMI server not found or wrong remote result");
                    return fail("remote method is incorrect");
                }
            }
            return pass();

        } catch (Throwable ex) {
            log.add(ex);
            return error(ex.toString());
        } finally {
            testOnOtherVm.removeSecurityManager();
            System.setSecurityManager(null);
            testVmRunner.shutdown();
        }
    }

    public void stateTestCyclesParameter() {

        try {
            for (int i = 0; i < testArgs.length; i++) {
                if ("-TCycles".equals(testArgs[i])) {
                    this.testCycles = Integer.parseInt(testArgs[i + 1]);
                }
                log.add("Test cycles number is " + testCycles);
            }
        } catch (Throwable t) {
            this.testCycles = 30;
        }
    }
}
