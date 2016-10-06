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
 * <b>Test description</b>
 * <ul>
 * <li> 1. Registry is started
 * <li> 2. Remote object is created and exported
 * <li> 3. It's verified that RemoteObject.toStub(server) returns stub
 * successfully
 * <li> 4. Link to the remote object is dropped
 * <li> 5. Registry is killed
 * <li> 6. GC is called
 * </ul>
 * 
 */
public class BasicDgc3Test extends Test {

    public static void main(String[] args) {
        System.exit(new BasicDgc3Test().test(args));
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
            result = obj.remoteMethod("12345");
            
            if (!result.equals("Remote method called. Result is: 12345")) {
                log.add("Remote method returns: " + result);
                log.add("RMI server not found or wrong remote result");
                return error("remote method is incorrect");
            }
            
            System.setSecurityManager(null);
            registryRunner.shutdown();
            testOnOtherVm.removeRemoteObject();
            System.setSecurityManager(manager);
            result = obj.remoteMethod("12345");
            
            if (!result.equals("Remote method called. Result is: 12345")) {
                log.add("Remote method returns: " + result);
                log.add("RMI server not found or wrong remote result");
                return fail("remote method is incorrect");
            }
            return pass();
        } catch (Throwable ex) {
            // ex.getCause().printStackTrace();
            log.add(ex);
            return error(ex.toString());
        } finally {
            testOnOtherVm.removeSecurityManager();
            System.setSecurityManager(null);
            testVmRunner.shutdown();

        }
    }
}
