/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author  Mikhail A. Markov, Vasily Zakharov
 */
package org.apache.harmony.rmi;

import java.io.EOFException;

import java.rmi.RMISecurityManager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;

import org.apache.harmony.rmi.test.MyRemoteInterface1;
import org.apache.harmony.rmi.test.TestObject;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Unit test for RMI Distributed Garbage Collector.
 *
 * @author  Mikhail A. Markov, Vasily Zakharov
 */
public class DGCTest extends RMITestBase {

    /**
     * String to identify that a registry process must be started.
     */
    private static final String REGISTRY_ID = "registry";

    /**
     * String to identify that a server process for test 0 must be started.
     */
    private static final String SERVER_ID_0 = "server0";

    /**
     * String to identify that a client process for test 0 must be started.
     */
    private static final String CLIENT_ID_0 = "client0";

    /**
     * String to identify that a server process for test 3 must be started.
     */
    private static final String SERVER_ID_3 = "server3";

    /**
     * Garbage collector tick (in milliseconds).
     */
    private static final int GC_TICK = 10000;

    /**
     * No-arg constructor to enable serialization.
     */
    public DGCTest() {
        super();
    }

    /**
     * Constructs this test case with the given name.
     *
     * @param   name
     *          Name for this test case.
     */
    public DGCTest(String name) {
        super(name);
    }

    /**
     * Runs test client process.
     *
     * @param   config
     *          Number of the configuration to run.
     *
     * @throws  Exception
     *          If some error occurs.
     */
    private void runTestClient0(int config) throws Exception {
        System.err.println("Test client started");
        System.setSecurityManager(new RMISecurityManager());
        setEnvironmentForConfig(config);
        Registry reg = LocateRegistry.getRegistry();
        MyRemoteInterface1 mri = (MyRemoteInterface1) reg.lookup(TEST_STRING_1);
        mri.test1();
        System.err.println("Test client completed");
    }

    /**
     * Runs test server process.
     *
     * @param   config
     *          Number of the configuration to run.
     *
     * @throws  Exception
     *          If some error occurs.
     */
    private void runTestServer3(int config) throws Exception {
        System.err.println("Test server started");
        System.setSecurityManager(new RMISecurityManager());
        setEnvironmentForConfig(config);
        Registry reg = LocateRegistry.createRegistry(REGISTRY_PORT);
        TestObject obj = new TestObject();
        UnicastRemoteObject.exportObject(obj, REGISTRY_PORT);
        obj = null;
        System.gc();
        System.err.println("Test server exiting");
    }

    /**
     * Calls system garbage collector ({@link System#gc()}) periodically.
     */
    static class GCThread extends Thread {

        /**
         * Creates this thread and marks it as daemon thread.
         */
        public GCThread() {
            super();
            setDaemon(true);
        }

        /**
         * {@inheritDoc}
         */
        public void run() {
            while (true) {
                try {
                    sleep(GC_TICK);
                } catch (InterruptedException e) {}

                System.out.println("GCThread: Calling GC");
                System.gc();
            }
        }

        /**
         * Creates new GCThread thread.
         */
        public static void create() {
            new GCThread().start();
        }
    }

    /**
     * Returns test suite for this class.
     *
     * @return  Test suite for this class.
     */
    public static Test suite() {
        return new TestSuite(DGCTest.class);
    }

    /**
     * Starts the testing from the command line.
     *
     * @param   args
     *          Command line parameters.
     */
    public static void main(String args[]) {
        switch (args.length) {
        case 0:
            // Run tests normally.
            junit.textui.TestRunner.run(suite());
            break;
        case 2:
            // Run registry, test server or client process.
            int config = new Integer(args[1]).intValue();
            String param = args[0].intern();
            DGCTest dgcTest = new DGCTest();

            try {
                if (param == CLIENT_ID_0) {
                    dgcTest.runTestClient0(config);
                } else if (param == SERVER_ID_3) {
                    dgcTest.runTestServer3(config);
                } else {
                    System.err.println("Bad parameter: " + param);
                    abort();
                }
            } catch (Exception e) {
                System.err.println("Child process ("
                        + param + ", " + config + ") failed: " + e);
                e.printStackTrace();
                abort();
            }
            System.err.println("Child process ("
                    + param + ", " + config + ") is terminating OK");
            break;
        default:
            System.err.println("Bad number of parameters: "
                    + args.length + ", expected: 2.");
            abort();
        }
    }
}
