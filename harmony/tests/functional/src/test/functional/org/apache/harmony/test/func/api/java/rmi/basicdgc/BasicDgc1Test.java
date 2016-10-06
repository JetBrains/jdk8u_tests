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

import java.rmi.NoSuchObjectException;
import java.rmi.RMISecurityManager;

import org.apache.harmony.test.func.api.java.rmi.share.ServerInterface;
import org.apache.harmony.test.func.api.java.rmi.share.Util;
import org.apache.harmony.share.Test;
import org.apache.harmony.test.share.dt.DistributedRunner;

/**
 * 
 * <b>Test design:</b>
 * <ul>
 * <li> 1. Server creates and exports remote object "server" and registers it
 * <li> 2. It's verified that RemoteObject.toStub(server) returns stub
 * successfully
 * <li> 3. The links to server and to the located registry are dropped
 * <li> 4. GC is called
 * </ul>
 * 
 */

public class BasicDgc1Test extends Test {

    private int testCycles = 30;

    public static void main(String[] args) {

        BasicDgc1Test tst = new BasicDgc1Test();
        tst.stateTestCyclesParameter();

        System.exit(tst.test(args));
    }

    /**
     * Server managed by OtherVMTest object(initialized in new VM(raised by
     * DistributedRunner))
     */

    public int test() {
        OtherVMTestInterface testOnOtherVm = null;
        Util.setJavaPolicy();
        DistributedRunner runner = new DistributedRunner();

        try {
            String result;
            testOnOtherVm = (OtherVMTestInterface) runner.init(OtherVMTest.class, null,
                    null);

            ServerInterface obj = (ServerInterface) testOnOtherVm.getStub();
            testOnOtherVm.enableSecurityManager();
            System.setSecurityManager(new RMISecurityManager());

            result = obj.remoteMethod("._0");
            if (!result.equals("Remote method called. Result is: ._0")) {
                log.add("Remote method returns: " + result);
                log.add("RMI server not found or wrong remote result");
                return error("remote method is incorrect");
            }

            testOnOtherVm.unexportServer();
            testOnOtherVm.removeRemoteObject();

            // Thread.sleep(10000);

            /* Wait for DGC to remove object */
            for (int counter = 0; counter < testCycles; counter++) {
                Thread.sleep(500);
                try {
                    result = obj.remoteMethod("0_.");
                    if (!result.equals("Remote method called. Result is: 0_.")) {
                        log.add("Remote method returns: " + result);
                        log.add("RMI server not found or wrong remote result");
                        return error("remote method is incorrect");
                    }
                } catch (NoSuchObjectException e) {
                    return pass();
                }
            }
            return fail("NoSuchObjectException had to be thrown");

        } catch (Throwable ex) {
            log.add(ex);
            return error(ex.toString());
        } finally {
            testOnOtherVm.removeSecurityManager();
            System.setSecurityManager(null);
            runner.shutdown();
        }
    }

    private void stateTestCyclesParameter() {
        try {
            for (int i = 0; i < testArgs.length; i++) {
                if ("-TCycles".equals(testArgs[i])) {
                    this.testCycles = Integer.parseInt(testArgs[i + 1]);
                }
            }
        } catch (Throwable t) {
            this.testCycles = 30;
        }
    }
}
