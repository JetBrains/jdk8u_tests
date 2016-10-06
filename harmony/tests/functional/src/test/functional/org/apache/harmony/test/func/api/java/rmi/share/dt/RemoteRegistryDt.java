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
package org.apache.harmony.test.func.api.java.rmi.share.dt;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.harmony.test.share.dt.DistributedRunner;

/**
 */
public class RemoteRegistryDt implements RemoteRegistry {

    private Registry registry;

    private int port;

    /**
     * {@inheritDoc}
     */

    public Registry getRegistry() {
        if (registry == null) {
            createRegistry(1098);
        }
        return registry;
    }

    public int getPort() {
        return port;
    }

    public int createRegistry(final int port) {
        if (this.port != 0) {
            return this.port;
        }

        try {
            registry = LocateRegistry.createRegistry(port);
            System.out.println("Registry created. Port: " + port);
            this.port = port;
            return port;
        } catch (Exception ex) {
            if (port < 65536) {
                return createRegistry(port + 1);
            }
            throw new RuntimeException("No available ports found.");
        }
    }

    public static RegistryWrapper getRemoteRegistry() {
        return new RegistryWrapper();
    }

    /**
     * {@inheritDoc}
     */
    protected void finalize() {
        if (registry == null) {
            return;
        }

        try {
            final String[] names = registry.list();
            for (int i = 0; i < names.length; i++) {
                registry.unbind(names[i]);
            }

            registry = null;
            System.gc();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Registry closed. Port: " + port);
    }

    public static class RegistryWrapper {
        private final DistributedRunner runner;

        private RemoteRegistry registryRunner;

        private Registry registry;

        private int port;

        RegistryWrapper() {
            runner = new DistributedRunner("Registry");
        }

        /**
         * @return Returns the registry.
         * @throws Exception
         */
        public Registry getRegistry() throws Exception {
            if (registry == null) {
                port = getRegistryRunner().createRegistry(1099);
                registry = LocateRegistry.getRegistry(port);
            }

            return registry;
        }

        /**
         * @return Returns the registryRunner.
         * @throws Exception
         */
        public RemoteRegistry getRegistryRunner() throws Exception {
            if (registryRunner == null) {
                registryRunner = (RemoteRegistry) runner.init(
                        RemoteRegistryDt.class, null, null);
            }

            return registryRunner;
        }

        /**
         * @return Returns the runner.
         */
        public DistributedRunner getRunner() {
            return runner;
        }

        /**
         * @return Returns the port.
         * @throws Exception
         */
        public int getPort() throws Exception {
            if (port == 0) {
                getRegistry();
            }

            return port;
        }

        protected void finalize() {
            runner.shutdown();
        }
    }
}
