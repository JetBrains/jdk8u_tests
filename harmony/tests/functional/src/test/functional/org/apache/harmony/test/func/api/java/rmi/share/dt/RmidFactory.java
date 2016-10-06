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
package org.apache.harmony.test.func.api.java.rmi.share.dt;

import java.io.File;
import java.io.IOException;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationSystem;
import java.util.LinkedList;
import java.util.List;

import org.apache.harmony.test.func.api.java.rmi.share.Util;
import org.apache.harmony.test.share.dt.DistributedRunner;

/**
 */
public class RmidFactory {

    public static final String[]    RMID_CLASSES = {
        "org.apache.harmony.rmi.activation.Rmid",
        "org.apache.harmony.rmi.activation.Rmid", "sun.rmi.server.Activation" };

    public static final File        LOG_DIR;

    public static final int         DEFAULT_PORT;

    public static final long        TIMEOUT;

    public static final List        VM_ARGS;

    private static Process          proc;

    private static ActivationSystem system;

    private static List             cmd;

    private static Thread           hook;

    private static int              port;

    static {
        final String className = RmidFactory.class.getName();
        String tmp = System.getProperty(className + ".LOG_DIR");

        LOG_DIR = tmp != null ? new File(tmp)
            : new File(System.getProperty("java.io.tmpdir"), "rmid"
                + System.currentTimeMillis());

        tmp = System.getProperty(className + ".DEFAULT_PORT");
        DEFAULT_PORT = tmp != null ? Integer.parseInt(tmp) : 1098;

        tmp = System.getProperty(className + ".TIMEOUT");
        TIMEOUT = tmp != null ? Long.parseLong(tmp) : 30000;

        VM_ARGS = new LinkedList();
        VM_ARGS.add("-Dsun.rmi.server.activation.debugExec=false");

        port = -1;
    }

    /**
     * Starts RMID on any free port.
     * 
     * @return the port on which RMID has been started.
     */
    public static int startRmid() {
        if (port != -1) {
            return port;
        }

        Util.setJavaPolicy();
        Util.removeFile(LOG_DIR);

        final String[] cmdArray;
        final String rmidClassName = getRmidClass();
        final DistributedRunner runner = new DistributedRunner();
        final String classpath = runner.getClasspath();
        final String bootClasspath = runner.getBootClasspath();

        port = Util.getAvailablePort(DEFAULT_PORT);
        cmd = new LinkedList();
        cmd.add(runner.getVmExecutable());

        if ((classpath != null) && (classpath.length() > 0)) {
            cmd.add("-cp");
            cmd.add(classpath);
        }

        if ((bootClasspath != null) && (bootClasspath.length() > 0)) {
            cmd.add("-Xbootclasspath/p:" + bootClasspath);
        }

        cmd.addAll(VM_ARGS);
        cmd.addAll(runner.getVmArgs());
        cmd.add(rmidClassName);
        cmd.add("-port");
        cmd.add(String.valueOf(port));
        cmd.add("-log");
        cmd.add(LOG_DIR.getPath());

        cmdArray = new String[cmd.size()];
        cmd.toArray(cmdArray);

        try {
            proc = Runtime.getRuntime().exec(cmdArray);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Util.redirectStream(proc.getInputStream(), System.err, "RMID");
        Util.redirectStream(proc.getErrorStream(), System.err, "RMID");
        System.setProperty("java.rmi.activation.port", String.valueOf(port));

        for (int i = 0; i < TIMEOUT; i += 100) {
            try {
                Thread.sleep(100);
                system = ActivationGroup.getSystem();
                hook = new ShutdownHook();
                Runtime.getRuntime().addShutdownHook(hook);
                return port;
            } catch (ActivationException ex) {
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        shutdownRmid();
        throw new RuntimeException("RMID was not started in " + TIMEOUT
            + " milliseconds");
    }

    /**
     * Shutdown RMID if stared.
     */
    public static void shutdownRmid() {
        cleanup();

        if (hook != null) {
            Runtime.getRuntime().removeShutdownHook(hook);
        }
    }

    /**
     * Returns a list containing the command used to start RMID.
     * 
     * @return list containing the command used to start RMID.
     */
    public static List getCmd() {
        return cmd;
    }

    private static String getRmidClass() {
        for (int i = 0; i < RMID_CLASSES.length; i++) {
            try {
                Class.forName(RMID_CLASSES[i]);
                return RMID_CLASSES[i];
            } catch (ClassNotFoundException ex) {
            }
        }

        throw new RuntimeException("No RMID classes found");
    }

    static void cleanup() {
        System.setSecurityManager(null);

        if (system == null) {
            return;
        }

        try {
            system.shutdown();
            Thread.sleep(300);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            proc.destroy();
        } catch (Exception ex) {
        }

        cmd.clear();
        cmd = null;
        port = -1;
        system = null;
        proc = null;

        Util.removeFile(LOG_DIR);
    }

    private static class ShutdownHook extends Thread {
        public void run() {
            RmidFactory.cleanup();
        }
    }
}
