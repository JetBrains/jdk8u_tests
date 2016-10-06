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
import java.rmi.NoSuchObjectException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.harmony.test.func.api.java.rmi.share.ServerInterface;

/**
 * This object used for managing of server
 * 
 */

public class OtherVMTest implements OtherVMTestInterface {

    private ServerInterface server;

    public void enableSecurityManager() {
        System.setSecurityManager(new RMISecurityManager());
    }

    public String bind(final String host, final int port) throws Exception {
        final String name = "rmi://" + host + ":" + port + "/Server";
        server = new ServerImpl();
        Naming.bind(name, UnicastRemoteObject.exportObject(server));
        return name;
    }

    public void unexportServer() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(server, true);
    }

    public void removeRemoteObject() {
        server = null;
        System.gc();
    }

    public Remote getStub() {
        try {
            server = new ServerImpl();
            return UnicastRemoteObject.exportObject(server);
        } catch (RemoteException e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public void removeSecurityManager() {
        System.setSecurityManager(null);
    }
}