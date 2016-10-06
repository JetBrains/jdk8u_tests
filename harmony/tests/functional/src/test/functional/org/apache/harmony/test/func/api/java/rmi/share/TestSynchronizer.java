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
package org.apache.harmony.test.func.api.java.rmi.share;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class is used to synchronise tests executed in different VM.
 * 
 */
public class TestSynchronizer extends Thread {

    public static final String  getMessageReq = "GET MESSAGE";

    /**
     * Socket to use.
     */
    private Socket              s;

    /**
     * Received message;
     */
    private static String       message;

    /**
     * The synchronizer works until the value of this field is true.
     */
    private static boolean      listen        = true;

    /**
     * Server socket.
     */
    private static ServerSocket server;

    public TestSynchronizer(Socket s) {
        this.s = s;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    /**
     * Process connection.
     */
    public void run() {
        try {
            InputStream is = s.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            PrintWriter w = new PrintWriter(s.getOutputStream());
            String str = r.readLine();
            if (getMessageReq.equals(str)) {
                w.println(message);
                w.flush();
                // System.out.println("Sending message: " + message);
            } else {
                message = str;
                // System.out.println("Message received: " + message);
            }
            is.close();
            r.close();
            w.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the message to the specified port.
     * 
     * @param message
     * @param port
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void sendMessage(String message, int port)
        throws UnknownHostException, IOException {
        sendMessage(message, "localhost", port);
    }

    /**
     * Send the message to the specified host and port.
     * 
     * @param message
     * @param host
     * @param port
     * @throws IOException
     * @throws UnknownHostException
     */
    public static void sendMessage(String message, String host, int port)
        throws UnknownHostException, IOException {
        Socket s = new Socket(host, port);
        PrintWriter w = new PrintWriter(s.getOutputStream());
        w.println(message);
        w.flush();
        w.close();
        s.close();
    }

    /**
     * Read message from the specified port.
     * 
     * @param port
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public static String getMessage(int port) throws UnknownHostException,
        IOException {
        return getMessage("localhost", port);
    }

    /**
     * Read message from the specified host/port.
     * 
     * @param host
     * @param port
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public static String getMessage(String host, int port)
        throws UnknownHostException, IOException {
        Socket s = new Socket(host, port);
        PrintWriter w = new PrintWriter(s.getOutputStream());
        w.println(getMessageReq);
        w.flush();
        InputStream is = s.getInputStream();
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String msg = r.readLine();
        w.close();
        r.close();
        is.close();
        s.close();
        return msg;
    }

    /**
     * Read message from the specified port until the expected messaged is
     * retrieved.
     * 
     * @param message
     * @param port
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public static String getExpectedMessage(String message, int port)
        throws Exception {
        return getExpectedMessage(message, "localhost", port);
    }

    /**
     * Read message from the specified host/port until the expected messaged is
     * retrieved.
     * 
     * @param message
     * @param host
     * @param port
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public static String getExpectedMessage(String message, String host,
        int port) throws Exception {
        final int max = 60000;
        String msg = getMessage(host, port);
        int i = 0;
        while (!message.equals(msg)) {
            msg = getMessage(host, port);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            i += 100;
            if (i >= max) {
                throw new Exception("The message \"" + message
                    + "\" not received in " + max + " milliseconds!");
            }
        }
        return msg;
    }

    /**
     * Run the synchronizer.
     * 
     * @param port
     */
    public static void runSynchronizer(int port) {
        try {
            server = new ServerSocket(port, 0, InetAddress
                .getByName("localhost"));

            System.out.println("Test synchronizer has been started on port: "
                + port);
            while (listen) {
                synchronized (server) {
                    new TestSynchronizer(server.accept());
                }
            }
        } catch (Exception e) {
            if (e instanceof SocketException) {
                System.out.println("Server socket closed.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public static void stopSynchronizer() {
        listen = false;
        if (server != null) {
            try {
                server.close();
                server = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Run the synchronizer.
     * 
     * @param args
     */
    public static void main(String args[]) {
        int port = 20111;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Throwable ex) {
        }

        runSynchronizer(port);
    }
}
