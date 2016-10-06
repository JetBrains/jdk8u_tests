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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 */
public class DistributedRunner {

    /**
     * Instances counter.
     */
    private static long counter;

    /**
     * Runner name.
     */
    private String      runnerName;

    /**
     * Path to the VM executable.
     */
    private String      vmExecutable;

    /**
     * Class path.
     */
    private String      classpath;

    /**
     * Boot class path.
     */
    private String      bootClasspath;

    /**
     * Working directory of the VM process.
     */
    private File        workDir;

    /**
     * VM arguments.
     */
    private List        vmArgs;

    /**
     * Environment variables.
     */
    private String[]    environment;

    /**
     * Indicates whether the subprocess's output should be redirected to
     * standard error stream.
     */
    private boolean     redirectOutput;

    /**
     * Remote system timeout.
     */
    private long        systemTimeout;

    /**
     * Shutdown hook.
     */
    protected Thread    shutdownHook;

    /**
     * System host.
     */
    protected String    remoteSystemHost;

    /**
     * System port.
     */
    protected int       remoteSystemPort;

    /**
     * Instantiate new Runner.
     */
    public DistributedRunner() {
        this("System " + counter);
        counter++;
    }

    /**
     * Instantiate new Runner with the specified name.
     * 
     * @param runnerName runner name.
     */
    public DistributedRunner(final String runnerName) {
        final String javaHome = System.getProperty("java.home")
            + File.separatorChar + "bin" + File.separatorChar;
        final String policy = System.getProperty("java.security.policy");
        final String vmStrArgs = System.getProperty(getClass().getName()
            + ".vmArgs");
        final Iterator sysProps = System.getProperties().entrySet().iterator();

        this.runnerName = runnerName;
        classpath = System.getProperty("java.class.path");
        workDir = new File("");
        vmArgs = new LinkedList();
        remoteSystemHost = "localhost";
        remoteSystemPort = -1;
        redirectOutput = true;

        if (new File(javaHome + "java.exe").exists()
            || new File(javaHome + "java").exists()) {
            vmExecutable = javaHome + "java";
        } else {
            vmExecutable = javaHome + "ij";
        }

        if (vmStrArgs != null) {
            final StringTokenizer st = new StringTokenizer(vmStrArgs, ",");

            while (st.hasMoreTokens()) {
                vmArgs.add(st.nextToken());
            }
        }

        if (policy != null) {
            vmArgs.add("-Djava.security.policy=" + policy);
        }

        while (sysProps.hasNext()) {
            final Map.Entry entry = (Map.Entry) sysProps.next();
            final String key = (String) entry.getKey();

            if (key.startsWith("-R")) {
                vmArgs.add("-D" + key.substring(2) + "=" + entry.getValue());
            }
        }
    }

    /**
     * @return Returns the bootClasspath.
     */
    public String getBootClasspath() {
        return bootClasspath;
    }

    /**
     * @param bootClasspath The bootClasspath to set.
     */
    public void setBootClasspath(final String bootClasspath) {
        this.bootClasspath = bootClasspath;
    }

    /**
     * @return Returns the classpath.
     */
    public String getClasspath() {
        return classpath;
    }

    /**
     * @param classpath The classpath to set.
     */
    public void setClasspath(final String classpath) {
        this.classpath = classpath;
    }

    /**
     * @return Returns the vmExecutable.
     */
    public String getVmExecutable() {
        return vmExecutable;
    }

    /**
     * @param vmExecutable The vmExecutable to set.
     */
    public void setVmExecutable(final String vmExecutable) {
        this.vmExecutable = vmExecutable;
    }

    /**
     * @return Returns the workDir.
     */
    public File getWorkDir() {
        return workDir;
    }

    /**
     * @param workDir The workDir to set.
     */
    public void setWorkDir(final File workDir) {
        this.workDir = workDir;
    }

    /**
     * @return Returns the environment.
     */
    public String[] getEnvironment() {
        return environment;
    }

    /**
     * @param environment The environment to set.
     */
    public void setEnvironment(final String[] environment) {
        this.environment = environment;
    }

    /**
     * @return Returns the vmArgs.
     */
    public List getVmArgs() {
        return vmArgs;
    }

    /**
     * @return Returns the redirectOutput.
     */
    public boolean isRedirectOutput() {
        return redirectOutput;
    }

    /**
     * @param redirectOutput The redirectOutput to set.
     */
    public void setRedirectOutput(boolean redirectOutput) {
        this.redirectOutput = redirectOutput;
    }

    /**
     * @return Returns the systemTimeout.
     */
    public long getSystemTimeout() {
        return systemTimeout;
    }

    /**
     * @param systemTimeout The systemTimeout to set.
     */
    public void setSystemTimeout(long systemTimeout) {
        this.systemTimeout = systemTimeout;
    }

    /**
     * @return Returns the remoteSystemHost.
     */
    public String getRemoteSystemHost() {
        return remoteSystemHost;
    }

    /**
     * @param remoteSystemHost The remoteSystemHost to set.
     */
    public void setRemoteSystemHost(String remoteSystemHost) {
        this.remoteSystemHost = remoteSystemHost;
    }

    /**
     * @return Returns the remoteSystemPort.
     */
    public int getRemoteSystemPort() {
        return remoteSystemPort;
    }

    /**
     * @param remoteSystemPort The remoteSystemPort to set.
     */
    public void setRemoteSystemPort(int remoteSystemPort) {
        this.remoteSystemPort = remoteSystemPort;
    }

    /**
     * Create an instance of the specified class in the remote system.
     * 
     * @param testClass instance class.
     * @param types constructor parameter types.
     * @param args constructor arguments.
     * @return proxy object representing the remote instance.
     * @throws Exception
     */
    public Object init(final Class c, final Class[] types, final Object[] args)
        throws Exception {
        final Long instanceId;

        if (remoteSystemPort == -1) {
            runSystem();
        }

        instanceId = (Long) sendRequest(new RemoteRequest.Register(c.getName(),
            types, args));

        return Proxy.newProxyInstance(c.getClassLoader(), c.getInterfaces(),
            new DTInvocationHandler(c, instanceId.longValue(),
                remoteSystemHost, remoteSystemPort));
    }

    /**
     * Release the remote object represented by the specified proxy.
     * 
     * @param proxy proxy object representing the remote object.
     * @throws Exception
     */
    public void release(final Object proxy) throws Exception {
        final DTInvocationHandler handler = (DTInvocationHandler) Proxy
            .getInvocationHandler(proxy);

        sendRequest(new RemoteRequest.Unregister(handler.instanceId));
    }

    /**
     * Shutdown the remote system.
     */
    public void shutdown() {
        if (shutdownHook != null) {
            shutdownHook.run();
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
        }

        remoteSystemPort = -1;
    }

    /**
     * This method creates new VM and runs RemoteSystem in the created VM.
     * Subclasses can override this method to run the system on a remote
     * computer.
     * 
     * @throws Exception
     */
    protected void runSystem() throws Exception {
        final String[] cmdarray;
        final LineNumberReader reader;
        final Runtime runtime;
        final List cmd;
        final Process proc;

        cmd = new LinkedList();

        cmd.add(vmExecutable);
        cmd.addAll(vmArgs);

        if ((classpath != null) && (classpath.length() > 0)) {
            cmd.add("-cp");
            cmd.add(classpath);
        }

        if ((bootClasspath != null) && (bootClasspath.length() > 0)) {
            cmd.add("-Xbootclasspath/p:" + bootClasspath);
        }

        cmd.add(RemoteSystem.class.getName());

        if (redirectOutput) {
            cmd.add("-n");
            cmd.add(runnerName);
        }

        if (systemTimeout > 0) {
            cmd.add("-t");
            cmd.add(String.valueOf(systemTimeout));
        }

        cmdarray = new String[cmd.size()];
        cmd.toArray(cmdarray);

        runtime = Runtime.getRuntime();
        proc = runtime.exec(cmdarray, environment);
        reader = new LineNumberReader(new InputStreamReader(proc
            .getInputStream()));
        remoteSystemPort = Integer.parseInt(reader.readLine());

        if (redirectOutput) {
            new RedirectStream(proc.getErrorStream(), System.err).start();
        } else {
        }

        shutdownHook = new ShutdownHook(remoteSystemHost, remoteSystemPort,
            proc);

        runtime.addShutdownHook(shutdownHook);

        sendRequest(new RemoteRequest.CheckConnection());
    }

    /**
     * Finalize the remote system.
     */
    protected void finalize() {
        shutdown();

        runnerName = null;
        vmArgs.clear();
        vmExecutable = null;
        classpath = null;
        bootClasspath = null;
        workDir = null;
        vmArgs = null;
        environment = null;
        shutdownHook = null;
        remoteSystemHost = null;
    }

    /**
     * Send request to the remote system.
     * 
     * @param request the request to be sent.
     * @return response on the specified request.
     * @throws Exception
     */
    Object sendRequest(final RemoteRequest.Request request) throws Exception {
        return RemoteRequest.sendRequest(request, remoteSystemHost,
            remoteSystemPort);
    }

    private static class DTInvocationHandler implements InvocationHandler {
        private final RemoteRequest.ObjectWrapper wrapper;
        final long                                instanceId;
        private final String                      remoteSystemHost;
        private final int                         remoteSystemPort;

        private DTInvocationHandler(final Class c, final long instanceId,
            final String remoteSystemHost, final int remoteSystemPort) {
            this.instanceId = instanceId;
            this.remoteSystemHost = remoteSystemHost;
            this.remoteSystemPort = remoteSystemPort;
            wrapper = new RemoteRequest.ObjectWrapper(c, null);
        }

        public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
            return RemoteRequest.sendRequest(new RemoteRequest.InvokeMethod(
                instanceId, wrapper.getMethodId(method), args),
                remoteSystemHost, remoteSystemPort);
        }
    }

    private static class RedirectStream extends Thread {
        private final InputStream in;
        private final PrintStream out;

        private RedirectStream(final InputStream in, final PrintStream out) {
            this.in = in;
            this.out = out;
            setDaemon(true);
        }

        public void run() {
            try {
                int i = -1;
                while ((i = in.read()) != -1) {
                    out.write(i);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class ShutdownHook extends Thread {
        private final String  remoteSystemHost;
        private final int     remoteSystemPort;
        private final Process proc;
        private boolean       isFinalized;

        private ShutdownHook(final String remoteSystemHost,
            final int remoteSystemPort, final Process proc) {
            this.remoteSystemHost = remoteSystemHost;
            this.remoteSystemPort = remoteSystemPort;
            this.proc = proc;
        }

        public void run() {
            if (isFinalized) {
                return;
            }

            try {
                RemoteRequest
                    .shutDownSystem(remoteSystemHost, remoteSystemPort);
            } catch (Exception ex) {
                // ex.printStackTrace();
            }

            if (proc != null) {
                proc.destroy();
            }

            isFinalized = true;
        }
    }
}
