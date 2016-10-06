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
package org.apache.harmony.test.func.api.javax.naming.provider.rmi.share;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 */
public class Utils {

    /**
     * Run RMI registry on the specified port. If the port is busy - increase
     * port value by 1 and try again.
     * 
     * @param defaultPort default port.
     * @return port number on which the registry has been created.
     * @throws Exception if the registry could not be created.
     */
    public static int runRegistry(int defaultPort) throws Exception {
        try {
            Registry r = LocateRegistry.createRegistry(defaultPort);
        } catch (Throwable ex) {
            if (defaultPort < 65536) {
                return runRegistry(defaultPort + 1);
            } else {
                throw new Exception("Registry could not be created. "
                    + ex.getMessage());
            }
        }
        return defaultPort;
    }

    /**
     * Returns command line argument.
     * 
     * @param name
     * @return
     */
    public static String getArg(String[] args, String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < args.length - 1; i++) {
            try {
                if (name.equals(args[i])) {
                    return args[i + 1];
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return null;
    }

    /**
     * Returns string representation of the array.
     * 
     * @param array array.
     * @return string representation of the array.
     */
    public static String arrayToString(Object[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str += array[i];
            if (i < array.length - 1) {
                str += ", ";
            }
        }
        return str;
    }
}
