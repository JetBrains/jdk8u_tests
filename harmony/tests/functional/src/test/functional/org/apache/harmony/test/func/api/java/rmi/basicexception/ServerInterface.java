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
package org.apache.harmony.test.func.api.java.rmi.basicexception;

import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The server can produce exceptional situations
 * 
 */

public interface ServerInterface extends Remote {
    void run1() throws RemoteException, AccessException;

    void run2() throws RemoteException, Exception;

    void run3() throws RemoteException;

    void run4() throws RemoteException;
}