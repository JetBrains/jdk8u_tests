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

package org.apache.harmony.test.func.api.java.net.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketTimeoutException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 16, 2006
 */
public class SocketTest extends MultiCase {

    public Result testConnectTimeout() {
        try {

            final Socket client = new Socket();
            ServerSocket server = new ServerSocket(0);

            assertFalse(client.isBound());
            client.bind(new InetSocketAddress("localhost", 0));
            assertTrue(client.isBound());

            int port = server.getLocalPort();
            server.close();
            
            SocketAddress addr = new InetSocketAddress("localhost", port);
            assertFalse(client.isConnected());
            try {
                client.connect(addr, 1);
                return failed("Expected SocketTimeoutException had not been thrown");
            } catch (SocketTimeoutException e) {
                assertFalse(client.isConnected());
            } catch (ConnectException ex) {
              // Sometimes the above code throws "Connection refused" for localhost.
              assertFalse(client.isConnected());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return failed("Unexpected exception: " + e.getMessage());
        }
        return result();
    }

    public Result testSetTimeout() {

        try {
            final ServerSocket server = new ServerSocket();
            final int timeout = 0;
            final int clientTimeout = 5000;

            server.bind(new InetSocketAddress("localhost", 0));

            final Socket client = new Socket();
            assertFalse(client.isConnected());
            long start = System.currentTimeMillis();
            try {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            server.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                client.connect(new InetSocketAddress("localhost", server
                        .getLocalPort()));

                client.setSoTimeout(clientTimeout);
                assertEquals(client.getSoTimeout(), clientTimeout);
                client.getInputStream().read();
                return failed("Expected SocketTimeoutException had not been thrown");

            } catch (SocketTimeoutException e) {
                long finish = System.currentTimeMillis();
                assertTrue(finish - start > clientTimeout - 1000);
                assertTrue(finish - start < clientTimeout + 1000);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result();
    }

    public Result testInputShutdown() {
        try {
            final ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress("localhost", 0));

            new Thread(new Runnable() {
                public void run() {
                    try {
                        server.accept().getOutputStream().write(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            Socket client = new Socket();
            client.connect(new InetSocketAddress("localhost", server
                    .getLocalPort()));

            assertFalse(client.isInputShutdown());
            assertFalse(client.isOutputShutdown());
            assertEquals(client.getInputStream().read(), 1);

            client.shutdownInput();

            // assertEquals(client.getInputStream().read(), -1);
            assertTrue(client.isInputShutdown());
            assertFalse(client.isOutputShutdown());

            client.shutdownOutput();

            // assertEquals(client.getInputStream().read(), -1);
            assertTrue(client.isInputShutdown());
            assertTrue(client.isOutputShutdown());

            assertFalse(client.isClosed());

            client.close();

            assertTrue(client.isClosed());

            try {
                client.getOutputStream().write(1);
                return failed("Output stream should be closed");
            } catch (IOException e) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result();

    }

    public Result testSetTcpNodelay() throws SocketException {
        Socket socket = new Socket();

        socket.setTcpNoDelay(true);
        assertTrue(socket.getTcpNoDelay());

        socket.setTcpNoDelay(false);
        assertFalse(socket.getTcpNoDelay());

        return result();
    }

    public Result testConnectConstructor() throws IOException {
        final ServerSocket server = new ServerSocket(0);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = server.accept();
                    OutputStream output = socket.getOutputStream();
                    output.write(1);
                    output.write(2);
                    output.write(3);
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Socket client = new Socket("localhost", server.getLocalPort());
        InputStream input = client.getInputStream();

        assertEquals(input.read(), 1);
        assertEquals(input.read(), 2);
        assertEquals(input.read(), 3);

        return result();
    }

    private class MySocket extends Socket {
        public MySocket(SocketImpl impl) throws SocketException {
            super(impl);
        }
    }

    public Result testSocketImplConstructor() throws IOException {
        Impl impl = new Impl();
        Socket socket = new MySocket(impl);
        SocketAddress addr = new InetSocketAddress("www.google.com", 80);
        socket.connect(addr);
        assertTrue(impl.connectCalled);
        return result();
    }

    public static void main(String[] args) {
        System.exit(new SocketTest().test(args));
    }

}
