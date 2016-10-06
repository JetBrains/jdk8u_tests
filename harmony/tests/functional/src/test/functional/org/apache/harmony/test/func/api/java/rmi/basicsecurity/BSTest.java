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
package org.apache.harmony.test.func.api.java.rmi.basicsecurity;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.security.Policy;

import org.apache.harmony.test.func.api.java.rmi.share.ServerInterface;
import org.apache.harmony.share.Result;

/**
 * This object used for managing of server
 * 
 */

public class BSTest implements BSTestInterface {

    public void removeSecurityManager() {
        System.setSecurityManager(null);
    }

    public int test(final String host, final int port) {

        System.setProperty("java.security.policy", "NoPolicy!");
        Policy.getPolicy().refresh();
        System.setSecurityManager(new RMISecurityManager());
        try {
            ServerInterface obj = (ServerInterface) Naming.lookup("rmi://"
                    + host + ":" + port + "/Server");
            obj.remoteMethod("1 v pole ne voin");
            System.out.println("SecurityException wasn't thrown");

            return Result.FAIL;
        } catch (Throwable ex) {
            Throwable th = ex;
            while (th != null) {
                if (th instanceof SecurityException) {
                    return Result.PASS;
                }
                th = th.getCause();
            }
            ex.printStackTrace();
            return Result.FAIL;
        }
    }
}
