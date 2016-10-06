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

/**
 * This object used for managing of server
 * 
 */

public class OtherVMTest implements OtherVMTestInterface {

    private ServerInterface server;

    private final Client clients[];

    public OtherVMTest() {
        clients = null;
        System.setSecurityManager(new RMISecurityManager());
    }

    public OtherVMTest(Integer numberOfClients) {
        System.setSecurityManager(new RMISecurityManager());
        clients = new Client[numberOfClients.intValue()];        
    }

    public String bind(String host, int port, int creatorNumber,
            int serverNumber) throws Throwable {
        final String name = "rmi://" + host + ":" + port + "/Server_"
                + creatorNumber + "_" + serverNumber;
        server = new ServerImpl();
        Naming.bind(name, server);
        return name;
    }

    public void initializeClients(String serverName, int creatorNumber)
            throws Throwable {
        if (clients == null) {
            throw new Error("Test error: Incorrect OtherVM constructor used");
        }        
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new Client(creatorNumber, i);
            clients[i].lookUpServer(serverName);
        }
    }

    public void removeSecurityManager() {
        System.setSecurityManager(null);
    }

    public boolean performVerifications() throws Throwable {
        for (int i = 0; i < clients.length; i++) {
            if (!clients[i].verifyRemoteMethod()) {
                return false;
            }
        }
        return true;
    }
}