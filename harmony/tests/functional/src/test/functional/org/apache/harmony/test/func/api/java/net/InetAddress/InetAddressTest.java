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
package org.apache.harmony.test.func.api.java.net.InetAddress;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: test some methods and constructors of java.net.InetSocketAddress and
 * java.net.InetAddress classes: InetAddress.getByAddress(String, byte[]),
 * InetAddress.getHostName(), InetSocketAddress(int), InetSocketAddress(String,
 * int), InetSocketAddress(InetAddress, int), InetSocketAddress.getAddress(),
 * InetSocketAddress.getPort(), InetSocketAddress.isUnresolved().
 * 
 */
public class InetAddressTest extends MultiCase {
    private static String hostName;
    private static int    port;

    public static void main(String[] args) {
        hostName = args[0];
        System.out.println("HostName: "+hostName);
        port = Integer.parseInt(args[1]);
        System.out.println("Port: "+port);
        System.exit(new InetAddressTest().test(args));
    }

    /**
     * Test unicast InetSocketAddress(java.lang.String, int) constructor.
     */
    public Result testUnicastAddress() {
        InetSocketAddress address = new InetSocketAddress(hostName, port);
        assertEquals(address.getPort(), port);
        assertEquals(address.getHostName(), hostName);
        assertEquals(address.isUnresolved(), false);
        return result();
    }

    /**
     * Test wildcard InetSocketAddress#InetSocketAddress(int) constructor.
     * Verify that InetSocketAddress.getHostName() returns "0.0.0.0".
     */
    public Result testWildcardAddress() {
        InetSocketAddress address = new InetSocketAddress(port);
        assertEquals(address.getPort(), port);
        assertEquals(address.getHostName(), "0.0.0.0");
        return result();
    }

    /**
     * Negative test: incorrect host.
     */
    public Result testIncorrectHostName() {
        InetSocketAddress address = new InetSocketAddress("gugugu.example.com",
            port);
        assertEquals(address.isUnresolved(), true);
        return result();
    }

    /**
     * Test InetSocketAddress(InetAddress, int) constructor.
     */
    public Result testInetAddressConstructor() throws UnknownHostException {
        InetSocketAddress address = new InetSocketAddress(InetAddress
            .getByName(hostName), port);
        assertEquals(address.getAddress().getHostName(), hostName);
        return result();
    }

    /**
     * Test InetAddress#getByAddress(java.lang.String, byte[]) method and
     * InetAddress#getHostName() method.
     */
    public Result testGetByAddress() throws UnknownHostException {
        byte[] ipAddr = new byte[] { 4, 6, 2, 6 };
        InetAddress inetAddress = InetAddress.getByAddress(hostName, ipAddr);
        assertEquals(inetAddress.getHostName(), hostName);
        assertTrue(Arrays.equals(inetAddress.getAddress(), ipAddr));
        return result();
    }
}