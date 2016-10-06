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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.security.Policy;

/**
 */
public class Util {

    public static void setJavaPolicy() {
        if (System.getProperty("java.security.policy") == null) {
            try {
                final File tmp = File.createTempFile("java", ".policy");
                final PrintStream out;

                tmp.deleteOnExit();
                out = new PrintStream(new FileOutputStream(tmp));

                out.println("grant{");

                // Common permissions.
                out.println("permission java.net.SocketPermission "
                    + "\"*:1024-65535\", \"connect,accept,resolve,listen\";");
                out.println("permission java.lang.RuntimePermission "
                    + "\"setFactory\";");
                out.println("permission java.lang.RuntimePermission "
                    + "\"setSecurityManager\";");

                // Permissions required for RI's RMID.
                out.println("permission com.sun.rmi.rmid.ExecPermission "
                    + "\"*\";");
                out.println("permission com.sun.rmi.rmid.ExecOptionPermission"
                    + "\"*\";");

                // Permissions required for DRL VM.
                out.println("permission java.io.FilePermission "
                    + "\"/-\", \"read\";");
                out.println("permission java.io.SerializablePermission "
                    + "\"enableSubclassImplementation\";");
                out.println("permission java.lang.RuntimePermission "
                    + "\"accessDeclaredMembers\";");
                out.println("permission java.lang.reflect.ReflectPermission "
                    + "\"suppressAccessChecks\";");
                out.println("permission java.util.logging.LoggingPermission "
                    + "\"control\";");

                out.println("};");

                out.flush();
                out.close();

                System.setProperty("java.security.policy", tmp.getPath());
                Policy.getPolicy().refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void redirectStream(final InputStream in,
        final PrintStream out, final String prefix) {

        new RedirectStream(in, out, prefix).start();
    }

    /**
     * Remove file or directory.
     * 
     * @param path path to the file.
     */
    public static void removeFile(final File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            final File[] f = file.listFiles();
            for (int i = 0; i < f.length; i++) {
                removeFile(f[i]);
            }
        }

        file.delete();
    }

    /**
     * Check whether the specified port is free. If the port is busy this method
     * attempts to find any free port.
     * 
     * @param port initial port number.
     * @return number of port which is not busy.
     */
    public static int getAvailablePort(final int port) {
        if (port == 0) {
            try {
                ServerSocket s = new ServerSocket(0);
                s.setReuseAddress(true);
                s.close();
                return s.getLocalPort();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            final ServerSocket s = new ServerSocket(port);
            s.setReuseAddress(true);
            s.close();
            return port;
        } catch (IOException ex) {
            if (port < 65536) {
                return getAvailablePort(port + 1);
            }
            throw new RuntimeException("No available ports found.");
        }
    }

    private static class RedirectStream extends Thread {
        private static final int  NL        = '\n';

        private final InputStream in;
        private final PrintStream out;
        private final String      prefix;

        private boolean           isNewLine = true;

        private RedirectStream(final InputStream in, final PrintStream out,
            final String prefix) {
            this.in = in;
            this.out = out;
            this.prefix = (prefix != null) ? "    [" + prefix + "]    " : null;
            setDaemon(true);
        }

        public void run() {
            try {
                for (int i = -1; (i = in.read()) != -1;) {
                    if (prefix != null) {
                        if (isNewLine) {
                            out.print(prefix);
                        }

                        if (i == NL) {
                            isNewLine = true;
                        } else {
                            isNewLine = false;
                        }
                    }

                    out.write(i);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
