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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.Vector;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.Test;

/**
 */
public class RMITestExecutorTH extends Test {

    /**
     * Vector containing processess with executed tests.
     */
    Vector               testProcessess = new Vector();

    /**
     * Vector containing created processess.
     */
    Vector               processess     = new Vector();

    /**
     * Test result.
     */
    private int          result         = Result.PASS;

    /**
     * RMI registry port.
     */
    private int          registryPort   = 1099;

    /**
     * String that should be replaced with registryPort.
     */
    private final String registryStr    = "REGISTRY_PORT";

    /**
     * RMI registry port.
     */
    private int          rmidPort       = 1098;

    /**
     * String that should be replaced with registryPort.
     */
    private final String rmidStr        = "RMID_PORT";

    /**
     * RMI registry port.
     */
    private int          syncPort       = 20111;

    /**
     * RMI registry port.
     */
    private int          proxyPort      = 31288;

    /**
     * String that should be replaced with registryPort.
     */
    private final String syncStr        = "SYNCHRONIZER_PORT";

    /**
     * This method parses command line arguments and performs instructions,
     * contained in the arguments (execute test, execute cmd, timeout etc.).
     */
    public int test() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                destroyProc(processess);
                destroyProc(testProcessess);
            }
        });
        selectPorts();
        final Class c = this.getClass();
        final Class[] types = new Class[] { String.class };
        for (int i = 0; i < (testArgs.length - 1); i++) {
            String method = testArgs[i];
            if (!method.startsWith("do")) {
                continue;
            }

            i++;
            String params = testArgs[i];
            try {
                Method m = c.getMethod(method, types);
                m.invoke(this, new Object[] { params });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result != Result.PASS) {
            destroyProc(processess);
            destroyProc(testProcessess);
            TestSynchronizer.stopSynchronizer();
            return result;
        }

        int size = testProcessess.size();
        for (int i = 0; i < size; i++) {
            if (result != Result.PASS) {
                destroyProc(processess);
                destroyProc(testProcessess);
                TestSynchronizer.stopSynchronizer();
                return result;
            }

            Object o = testProcessess.get(i);
            if (!(o instanceof Process)) {
                continue;
            }

            Process p = (Process) o;
            try {
                p.waitFor();
                result = p.exitValue();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            try {
                p.destroy();
            } catch (Throwable ex) {
            }
        }

        destroyProc(processess);
        TestSynchronizer.stopSynchronizer();

        return result;
    }

    /**
     * Run test.
     * 
     * @param cmd command line.
     */
    public void doRunTest(String cmd) {
        cmd = parseCmd(cmd);
        log.info("Executing test. Command: " + cmd);
        new ExecTestThread(cmd).run();
    }

    /**
     * Run test in new thread.
     * 
     * @param cmd command line.
     */
    public void doRunTest_NewThread(String cmd) {
        cmd = parseCmd(cmd);
        log.info("Executing test in new thread. Command: " + cmd);
        new ExecTestThread(cmd).start();
    }

    /**
     * Executes specified system command.
     * 
     * @param cmd
     */
    public void doRunCmd(String cmd) {
        cmd = parseCmd(cmd);
        log.info("Executing command: " + cmd);
        new ExecCmdThread(cmd).run();
    }

    /**
     * Executes specified system command in new thread.
     * 
     * @param cmd
     */
    public void doRunCmd_NewThread(String cmd) {
        cmd = parseCmd(cmd);
        log.info("Executing command in new thread: " + cmd);
        new ExecCmdThread(cmd).start();
    }

    /**
     * Kill the programm.
     * 
     * @param prog
     */
    public void doKill(String prog) {
        String kill = "killall ";
        String s = "";
        String os = System.getProperty("os.name");
        if (os != null && (os.toLowerCase().indexOf("windows") != -1)) {
            kill = "taskkill /F /IM ";
            s = ".exe";
        }

        doRunCmd(kill + prog + s);
    }

    /**
     * Sleep current thread for the specified number of milliseconds.
     * 
     * @param cmd number of milliseconds.
     */
    public void doSleep(String time) {
        log.info("Sleep: " + time);
        try {
            Thread.sleep(Long.parseLong(time.trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove file.
     * 
     * @param path path to the file.
     */
    public void doRemoveFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            String[] f = file.list();
            for (int i = 0; i < f.length; i++) {
                doRemoveFile(path + File.separator + f[i]);
            }
        }

        log.info("Removing file: " + file);
        file.delete();
    }

    /**
     * Start the test synchronizer on the specified port.
     * 
     * @param port
     */
    public void doStartSynchronizer(String port) {
        int p = 20111;
        try {
            p = Integer.parseInt(parseCmd(port));
        } catch (Throwable ex) {
        }
        log.info("Starting test synchronizer on port " + p);
        final int fp = p;
        new Thread() {
            public void run() {
                TestSynchronizer.runSynchronizer(fp);
            }
        }.start();
    }

    public void doRunRMID(String cmd) {
        final String rmidClass = "org.apache.harmony.rmi.activation.Rmid";
        final String RMID_CMD = "-Ddrl.rmi.transport.tcp.logLevel=OFF "
            + "-Dsun.rmi.server.activation.debugExec=false "
            + "-Ddrl.rmi.loader.logLevel=OFF -Djava.rmi.server.logCalls=false "
            + "-Ddrl.rmi.transport.connectionTimeout=60000 "
            + "-Ddrl.rmi.activation.logLevel=VERBOSE " + rmidClass
            + " -port RMID_PORT";

        String cmd1 = null;
        String cmd2 = null;
        int ind = cmd.indexOf("||");
        if (ind != -1) {
            cmd1 = cmd.substring(0, ind);
            cmd2 = cmd.substring(ind + 2);
        } else {
            cmd2 = cmd;
        }

        try {
            Class.forName(rmidClass);
            doRunCmd_NewThread(cmd1.replaceAll("RMID_CMD", RMID_CMD));
        } catch (Throwable e) {
            doRunCmd_NewThread(cmd2);
        }
    }

    public void doStopRMID(String runtime) {
        final String rmidClass = "org.apache.harmony.rmi.activation.Rmid";
        try {
            Class.forName(rmidClass);
            doRunCmd(runtime + " " + rmidClass + " -stop -port " + rmidPort);
        } catch (Throwable ex) {
            doRunCmd("rmid -stop -port " + rmidPort);
            doKill("rmid");
        }
    }

    /**
     * Destroy all of the processes contained in the list.
     * 
     * @param processes the list containing the processes that have to be
     *            destroyed.
     */
    void destroyProc(Vector processes) {
        if (processes == null) {
            return;
        }
        synchronized (processes) {
            for (int i = 0; i < processes.size(); i++) {
                try {
                    Object o = processes.get(i);
                    if (!(o instanceof Process)) {
                        continue;
                    }

                    Process p = (Process) o;
                    try {
                        p.destroy();
                    } catch (Throwable ex) {
                    }
                } catch (Throwable ex) {
                }
            }
        }
    }

    /**
     * Select ports for RMI registry and RMID.
     */
    private void selectPorts() {
        registryPort = findPort(registryPort);
        log.info("Registry port: " + registryPort);
        rmidPort = findPort(rmidPort);
        if (rmidPort == registryPort) {
            rmidPort = findPort(rmidPort + 1);
        }
        log.info("RMID port: " + rmidPort);
        syncPort = findPort(syncPort);
        proxyPort = findPort(proxyPort);
        log.info("Synchronizer port: " + syncPort);
    }

    /**
     * Find available port.
     * 
     * @param port default port.
     * @return found port.
     */
    private int findPort(int port) {
        try {
            ServerSocket s = new ServerSocket(port);
            s.setReuseAddress(true);
            s.close();
            return port;
        } catch (Throwable ex) {
            if (port < 65536) {
                return findPort(port + 1);
            }
            throw new RuntimeException("There was no available ports found.");
        }
    }

    /**
     * Parses the command and replaces all strings such as RMID_PORT,
     * REGISTRY_PORT etc. with its values.
     * 
     * @param cmd command.
     * @return
     */
    private String parseCmd(String cmd) {
        cmd = cmd.replaceAll(registryStr, registryPort + "");
        cmd = cmd.replaceAll(rmidStr, rmidPort + "");
        cmd = cmd.replaceAll(syncStr, syncPort + "");
        return cmd;
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        RMITestExecutorTH t = new RMITestExecutorTH();
        long time = System.currentTimeMillis();
        int ex = t.test(args);
        System.err.println("Time: " + (System.currentTimeMillis() - time));
        System.exit(ex);
    }

    /**
     * Executes tests in new thread.
     */
    private class ExecTestThread extends Thread {

        /**
         * System command.
         */
        private String cmd;

        private ExecTestThread(String cmd) {
            this.cmd = cmd;
        }

        /**
         * Run the thread.
         */
        public void run() {
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                testProcessess.add(p);
                new RedirectStream(p.getInputStream(), System.out, getName())
                    .start();
                new RedirectStream(p.getErrorStream(), System.err, getName())
                    .start();
                p.waitFor();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Executes system command in new thread.
     */
    private class ExecCmdThread extends Thread {

        /**
         * System command.
         */
        private String cmd;

        private ExecCmdThread(String cmd) {
            this.cmd = cmd;
        }

        /**
         * Run the thread.
         */
        public void run() {
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                processess.add(p);
                new RedirectStream(p.getInputStream(), System.out, getName())
                    .start();
                new RedirectStream(p.getErrorStream(), System.err, getName())
                    .start();
                p.waitFor();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Redirects the input stream to the output.
     */
    private class RedirectStream extends Thread {
        public InputStream is;
        public PrintStream out;
        private String     id;

        public RedirectStream(InputStream is, PrintStream out, String id) {
            this.is = is;
            this.out = out;
            this.id = id;
        }

        public void run() {
            if (is == null) {
                return;
            }

            byte[] b = new byte[1];
            final byte[] eol = "\n".getBytes();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                while (is.read(b) != -1) {
                    if (b[0] == eol[0]) {
                        baos.flush();
                        String str = baos.toString();
                        if (str != null) {
                            str = str.trim();
                            if (str.length() != 0) {
                                out.println("[" + id + "] " + str);
                                out.flush();
                            }
                        }
                        baos = new ByteArrayOutputStream();
                    } else {
                        baos.write(b);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
