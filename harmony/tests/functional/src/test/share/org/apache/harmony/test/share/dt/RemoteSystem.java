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
package org.apache.harmony.test.share.dt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 */
public class RemoteSystem {

    /**
     * Server socket.
     */
    final ServerSocket   serverSocket;

    /**
     * Instance of the current system.
     */
    final RemoteSystem   system;

    /**
     * Socket listener.
     */
    final SocketListener listener;

    /**
     * Map containing instances of the registered objects.
     */
    final Map            instances;

    /**
     * Registered objects counter.
     */
    long                 counter;

    /**
     * The flag indicating whether the system is alive.
     */
    private boolean      isAlive;

    /**
     * Object used for threads synchronization.
     */
    final Object         lock;

    /**
     * Create a remote system on any free port.
     * 
     * @param port
     * @throws IOException
     */
    public RemoteSystem() throws IOException {
        this(0, 0);
    }

    /**
     * Create a remote system on the specified port.
     * 
     * @param port the number of port on which the system have to be created.
     * @throws IOException
     */
    public RemoteSystem(final int port, final int soTimeout) throws IOException {
        serverSocket = new ServerSocket(port);
        if ( soTimeout==0 ) {
            // FIXME: timeout should be passed by framework as parameter
            // 
            // Don't set intinite timeout - set 15min as default timeout
            // Otherwise in case of tests failure the suite run may hang
            serverSocket.setSoTimeout(900000);
        } else {
            serverSocket.setSoTimeout(soTimeout);
        }
        serverSocket.setReuseAddress(true);
        system = this;
        instances = new Hashtable();
        listener = new SocketListener();
        lock = new Object();

        listener.setPriority(Thread.NORM_PRIORITY);
        listener.start();
        isAlive = true;
    }

    /**
     * Shutdown the system.
     */
    void shutdown() {
        finalize();
        System.exit(0);
    }

    protected void finalize() {
        if (!isAlive) {
            return;
        }

        final Iterator it = instances.values().iterator();
        while (it.hasNext()) {
            try {
                ((RemoteRequest.ObjectWrapper) it.next()).release();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        instances.clear();
    }

    /**
     * Start the remote system on any free port.
     * 
     * @param argsm command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final RemoteSystem system;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream out = new PrintStream(baos);
        final PrintStream defaultOut = System.out;
        final PrintStream defaultErr = System.err;
        final int port;
        String name = null;
        int timeout = 0;

        for (int i = 0; i < (args.length - 1); i++) {
            if (args[i].equals("-n")) {
                i++;
                name = args[i];
            } else if (args[i].equals("-t")) {
                i++;
                timeout = Integer.parseInt(args[i]);
            }
        }

        System.setOut(out);
        System.setErr(out);
        system = new RemoteSystem(0, timeout);
        port = system.serverSocket.getLocalPort();
        defaultOut.println(port);
        defaultOut.flush();

        if (name != null) {
            final PrintStream output = new PrintStream(new Output(defaultErr,
                name));
            System.setOut(output);
            System.setErr(output);
        } else {
            System.setOut(defaultOut);
            System.setErr(defaultErr);
        }

        if (baos.size() > 0) {
            System.out.write(baos.toByteArray());
        }

        baos.close();
    }

    private class SocketListener extends Thread {

        public void run() {
            while (true) {
                try {
                    final ConnectionHandler ch = new ConnectionHandler(
                        serverSocket.accept());
                    ch.setPriority(Thread.NORM_PRIORITY);
                    ch.setDaemon(true);
                    ch.start();
                } catch (SocketException ex) {
                    return;
                } catch (SocketTimeoutException ex) {
                    shutdown();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class ConnectionHandler extends Thread {

        private final Socket socket;

        private ConnectionHandler(final Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                final ObjectInputStream in = new ObjectInputStream(socket
                    .getInputStream());
                final ObjectOutputStream out = new ObjectOutputStream(socket
                    .getOutputStream());
                final RemoteRequest.Request request = (RemoteRequest.Request) in
                    .readObject();

                if (request instanceof RemoteRequest.Shutdown) {
                    system.finalize();
                    out.writeObject(new Boolean(true));
                    out.flush();
                    in.close();
                    out.close();
                    socket.close();
                    System.exit(0);
                }

                out.writeObject(getResult(request));
                out.flush();
                in.close();
                out.close();
                socket.close();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        public Object getResult(final RemoteRequest.Request request) {
            try {
                return request.perform(system);
            } catch (Throwable ex) {
                return new RemoteRequest.RemoteException(ex);
            }
        }
    }

    private static class Output extends OutputStream {

        private final static char NEW_LINE_CHAR = '\n';

        private final PrintStream out;

        private final String      name;

        private boolean           isNewLine     = true;

        public Output(final PrintStream out, final String name) {
            this.out = out;
            this.name = "    [" + name + "]    ";
        }

        public void write(final int b) {
            if (isNewLine) {
                out.print(name);
            }

            if (b == NEW_LINE_CHAR) {
                isNewLine = true;
            } else {
                isNewLine = false;
            }

            out.write(b);
        }
    }
}
