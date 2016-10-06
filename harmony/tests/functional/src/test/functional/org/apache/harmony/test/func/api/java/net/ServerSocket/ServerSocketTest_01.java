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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * 23.08.2005
 */
public class ServerSocketTest_01 extends MultiCase {

    String[] request = { "hello", "get data", "bye" };

    String[] response = { "hi", "some data", "bye-bye" };

    int port = 8081;

    String host = "localhost";

    public class InputStreamLogger extends InputStream {

        private InputStream innerStream;

        public InputStreamLogger(InputStream innerStream) {
            this.innerStream = innerStream;
        }

        public int read() throws IOException {
            try {
                int v = innerStream.read();
                System.err.print("'" + (char) v + "'");
                return v;
            } catch (Throwable e) {
                e.printStackTrace();
                if (e instanceof IOException)
                    throw (IOException) e;
            }
            return 0;
        }

        public int available() throws IOException {
            int v = innerStream.available();
            System.err.println(v + " = available()");
            return v;
        }

        public void close() throws IOException {
            innerStream.close();
        }

        public synchronized void mark(int arg0) {
            innerStream.mark(arg0);
        }

        public boolean markSupported() {
            return innerStream.markSupported();
        }

        public int read(byte[] arg0, int arg1, int arg2) throws IOException {
            System.err.println("read(byte[" + arg0.length + "],int " + arg1
                    + ",int " + arg2 + ")");
            return innerStream.read(arg0, arg1, arg2);
        }

        public int read(byte[] arg0) throws IOException {
            System.err.println("read(byte[])");
            return innerStream.read(arg0);
        }

        public synchronized void reset() throws IOException {
            innerStream.reset();
        }

        public long skip(long arg0) throws IOException {
            return innerStream.skip(arg0);
        }
    }

    public class ServerThread implements Runnable {
        public Boolean ready = Boolean.FALSE;

        private boolean passed = true;

        public boolean isPassed() {
            return passed;
        }

        private int serverSocketConstructor = 0;

        private static final int SSConsDefault = 0;

        private static final int SSConsInt = 1;

        private static final int SSConsIntInt = 2;

        private static final int SSConsIntIntIA = 3;

        private int serverSocketBindMode;

        private static final int AutoServerSocket = 0;

        private static final int FixedServerSocket = 1;

        private static final int AutoServerPort = 2;

        int backlog = 15;

        public void run() {
            System.err.println("server: started");
            try {

                ServerSocket socket;
                switch (serverSocketConstructor) {
                default:
                case SSConsDefault:
                    socket = new ServerSocket();
                    switch (serverSocketBindMode) {
                    case AutoServerPort:
                        System.err.println("server: AutoServerPortMode");
                        socket.bind(new InetSocketAddress(host, 0));
                        port = socket.getLocalPort();
                        break;
                    case FixedServerSocket:
                        System.err.println("server: FixedServerSocketMode");
                        while (true) {
                            try {
                                socket.bind(new InetSocketAddress(host, port));
                            } catch (BindException e) {
                                port++;
                                continue;
                            }
                            break;
                        }
                        break;
                    case AutoServerSocket:
                        System.err.println("server: AutoServerSocketMode");
                        socket.bind(null);
                        port = socket.getLocalPort();
                        host = socket.getInetAddress().getHostName();
                        break;
                    }
                    break;
                case SSConsInt:
                    socket = new ServerSocket(port);
                    host = socket.getInetAddress().getHostName();
                    break;
                case SSConsIntInt:
                    socket = new ServerSocket(port, backlog);
                    host = socket.getInetAddress().getHostName();
                    break;
                case SSConsIntIntIA:
                    socket = new ServerSocket(port, backlog,
                            new InetSocketAddress(host, port).getAddress());
                    break;

                }

                synchronized (this) {
                    ready = Boolean.TRUE;
                    this.notifyAll();
                }

                System.err.println("server: wait for connection");

                Socket connection = socket.accept();

                InputStream cis = connection.getInputStream();
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(new InputStreamLogger(cis)));
                PrintStream output = new PrintStream(connection
                        .getOutputStream());

                System.err.println("server: transmitting");
                for (int i = 0; i < request.length && i < response.length; i++) {
                    System.err.println("server: wait for request");
                    String req = input.readLine();
                    System.err.println("server: '" + req + "' requested");
                    if (req.equals(request[i])) {
                        System.err.println("server: ok, sending response '"
                                + response[i] + "'");
                        output.println(response[i]);
                        System.err.println("server: sent");
                    } else {
                        System.err.println("wrong request");
                        passed = false;
                        break;
                    }
                }

                socket.close();

                System.err.println("server: ok");

                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            passed = false;
        }

        public synchronized void waitCreated() {
            System.err.println("server: wait created");
            while (!ready.booleanValue()) {
                try {
                    System.err.println("server: wait loop");
                    this.wait();
                    System.err.println("server: wait ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ClientThread implements Runnable {

        private boolean passed = true;

        public boolean isPassed() {
            return passed;
        }

        public void run() {
            try {
                System.err.println("client: started");
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port));
                System.err.println("client: connected");
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintStream output = new PrintStream(socket.getOutputStream());
                System.err.println("client: transmitting");
                for (int i = 0; i < request.length && i < response.length; i++) {
                    System.err.println("client: sending '" + request[i] + "'");
                    output.println(request[i]);
                    System.err.println("client: wait for response");
                    String resp = input.readLine();
                    System.err.println("client: got '" + resp + "'");
                    if (!resp.equals(response[i])) {
                        System.err.println("wrong response");
                        passed = false;
                        break;
                    }
                }
                System.err.println("client: ok");
                return;
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            passed = false;
        }
    }

    private Result tstImpl(int ssConsType) {
        try {
            int[] serverModes = { ServerThread.AutoServerPort,
                    ServerThread.AutoServerSocket,
                    ServerThread.FixedServerSocket };

            boolean passed = true;

            for (int i = 0; i < serverModes.length; i++) {
                ServerThread server = new ServerThread();
                server.serverSocketBindMode = serverModes[i];
                server.serverSocketConstructor = ssConsType;
                Thread serverThread = new Thread(server);
                System.err.println("ok");
                serverThread.start();
                server.waitCreated();
                System.err.println("started server");
                ClientThread client = new ClientThread();
                Thread clientThread = new Thread(client);
                clientThread.start();
                System.err.println("started client");
                try {
                    serverThread.join();
                    clientThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println("all finished");

                passed &= server.isPassed() & client.isPassed();
            }

            if (passed) {
                return passed();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return failed("error");
    }

    public Result testConsDefault() {
        return tstImpl(ServerThread.SSConsDefault);
    }

    public Result testConsInt() {
        return tstImpl(ServerThread.SSConsInt);
    }

    public Result testConsIntInt() {
        return tstImpl(ServerThread.SSConsIntInt);
    }

    public Result testConsIntIntIA() {
        return tstImpl(ServerThread.SSConsIntIntIA);
    }

    public static void main(String[] args) {
        System.exit(new ServerSocketTest_01().test(args));
    }
}