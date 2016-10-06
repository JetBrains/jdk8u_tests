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

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.Test;

/*
 * 24.08.2005
 */
public class ServerSocketTest_02 extends Test {

    private class Client implements Runnable {

        public final int NOT_CONNECTED = 0;

        public final int CONNECTED_OK = 1;

        public final int CONNECTION_REFUSED = 2;

        private int status;

        private int port;

        private String host;

        public synchronized int getStatus() {
            return status;
        }

        private synchronized void setStatus(int status) {
            this.status = status;
        }

        public Client(String host, int port) {
            setStatus(NOT_CONNECTED);
            this.host = host;
            this.port = port;
        }

        public void run() {
            try {
                Socket client = new Socket();
                client.connect(new InetSocketAddress(host, port), 10000);
                setStatus(CONNECTED_OK);
            } catch (Throwable e) {
                //System.err.println(e.getMessage() + " @" + host + ":" + port);
                setStatus(CONNECTION_REFUSED);
            }
        }

    }

    public int test() {

        boolean failed = false;
        for (int i = 0; i < 4; i++) {
            int result = testImpl((i & 1) != 0, (i & 2) != 0);
            failed |= result != Result.PASS;
        }

        if (failed) {
            return fail("One of case have failed");
        } else {
            return pass();
        }
    }

    public int testImpl(boolean useDefault, boolean useLocalhost) {
        try {
            
            System.err.println((useDefault ? "useDefault" : "") + " " + (useLocalhost ? "useLocalhost" : ""));
            
            final int backlog = useDefault ? 50 : 37;
            
            boolean failed = false;
            
            ServerSocket server = new ServerSocket();
            InetSocketAddress addr = null;
            if (useLocalhost) {
                addr = new InetSocketAddress("localhost", 0);
            }
            
            if (server.isBound()) {
                System.err.println("1 ServerSocket.isBound returned wrong value (true)");
                failed = true;
            }
            
            if (server.isClosed()) {
                System.err.println("2 ServerSocket.isClosed() returned wrong value - true");
                failed = true;
            }
            
            if (useDefault) {
                server.bind(addr);
            } else {
                server.bind(addr, backlog);
            }
            
            if (!server.isBound()) {
                System.err.println("3 ServerSocket.isBound returned wrong value (false)");
                failed = true;
            }
            
            if (server.isClosed()) {
                System.err.println("4 ServerSocket.isClosed() returned wrong value - true");
                failed = true;
            }
            
            Client clients[] = new Client[200];
            Thread threads[] = new Thread[clients.length];
            for (int i = 0; i < clients.length; i++) {
                clients[i] = new Client(
                //((InetSocketAddress)server.getLocalSocketAddress()).getHostName(),
                        server.getInetAddress().getHostName(),
                        server.getLocalPort());
                (threads[i] = new Thread(clients[i])).start();
            }
            int status[] = { 0, 0, 0 };
            for (int i = 0; i < clients.length; i++) {
                threads[i].join();
                status[clients[i].getStatus()]++;
            }

            System.err
                    .println(status[0] + ", " + status[1] + ", " + status[2]);

            if (status[1] != backlog) {
                System.err.println("5 ServerSocket default backlog value differs from specified value");
            }
            
            if (status[0] != 0)    {
                System.err.println("other error");
            }
            
            server.close();
            
            if (!server.isBound()) {
                System.err.println("6 ServerSocket.isBound returned wrong value (false)");
                failed = true;
            }
            
            if (!server.isClosed()) {
                System.err.println("7 ServerSocket.isClosed() returned wrong value - false");
                failed = true;
            }
            
            if (failed)
            {
                return fail("error");
            }

        } catch (Throwable e) {
            return fail(e.getMessage());
        }
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new ServerSocketTest_02().test(args));
    }
}