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
/**
 */

package org.apache.harmony.test.func.api.java.net.ServerSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 14, 2006
 */
public class SoTimeoutTest extends MultiCase {

    public Result testSoTimeoutPositive() {
        try {
            final ServerSocket server = new ServerSocket();
            final int timeout = 10000;
            final int clientTimeout = 5000;

            setupServer(server, timeout);
            createClient(server, clientTimeout);

            try {
                server.accept();
                server.close();
                assertTrue(server.isClosed());
            } catch (SocketTimeoutException e) {
                return failed("Unexpected SocketTimeoutException thrown");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return failed("Unexpected exception: " + e.getMessage());
        }
        return result();
    }

    public Result testSoTimeoutNegative() {
        try {
            final ServerSocket server = new ServerSocket();
            final int clientTimeout = 10000;
            final int timeout = 5000;

            setupServer(server, timeout);

            createClient(server, clientTimeout);

            try {
                Socket connected = server.accept();
                return failed("Expected SocketTimeoutException had not thrown");
            } catch (SocketTimeoutException e) {
            }
        } catch (IOException e) {
            e.printStackTrace();
            return failed("Unexpected exception: " + e.getMessage());
        }
        return result();
    }

    public Result testSoTimeoutInfinite() {

        try {
            final ServerSocket server = new ServerSocket();
            final int timeout = 0;
            final int clientTimeout = 15000;

            setupServer(server, timeout);
            createClient(server, clientTimeout);

            try {
                Socket connected = server.accept();
            } catch (SocketTimeoutException e) {
                return failed("Unexpected SocketTimeoutException thrown");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return failed("Unexpected exception: " + e.getMessage());
        }
        return result();
    }

    
    
    private void setupServer(final ServerSocket server, final int timeout)
            throws SocketException, IOException {
        assertFalse(server.isBound());
        assertFalse(server.isClosed());
        server.setSoTimeout(timeout);
        assertEquals(server.getSoTimeout(), timeout);
        server.bind(new InetSocketAddress("localhost", 0));
        assertTrue(server.isBound());
    }

    private void createClient(final ServerSocket server, final int timeout) {
        final Socket client = new Socket();
        assertFalse("client.isBound()", client.isBound());
        assertFalse("client.isConnected()", client.isConnected());
        assertFalse("client.isClosed()", client.isClosed());

        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(timeout);
                    client.connect(server.getLocalSocketAddress(), server
                            .getLocalPort());
                    assertTrue("client.isBound()", client.isBound());
                    assertTrue("client.isConnected()", client.isConnected());
                    client.close();
                    assertTrue("client.isBound()", client.isBound());
                    assertTrue("client.isClosed()", client.isClosed());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public static void main(String[] args) {
        System.exit(new SoTimeoutTest().test(args));
    }

}
