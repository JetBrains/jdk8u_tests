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

import org.apache.harmony.test.func.api.java.rmi.share.ServerInterface;

public class Client {

    private ServerInterface server;

    private final int clientNumber;

    private final int creatorNumber;

    public Client(int givenCreatorNumber, int givenClientNumber) {
        this.clientNumber = givenClientNumber;
        this.creatorNumber = givenCreatorNumber;
    }

    public void lookUpServer(String serverName) throws Throwable {
        server = (ServerInterface) Naming.lookup(serverName);
    }

    public boolean verifyRemoteMethod() throws Throwable {
        String s = server.remoteMethod("number " + clientNumber
                + ", my creator number is " + creatorNumber);
        System.err.println(s);
        return ("I am server number " + clientNumber
                + ", my creator number is " + creatorNumber).equals(s);
    }
}
