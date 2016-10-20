/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/**
 * @author Vitaly A. Provodin
 * @version $Revision: 1.2 $
 */

/**
 * Created on 26.01.2005
 */
package org.apache.harmony.share.framework.jpda;

/**
 * This class provides access to options for running JPDA tests.
 * <p>
 * The settings are presented as a set of getters and setters for test options,
 * which can be implemented in different ways. In this implementation test
 * options are implemented via VM system properties, which can be specified
 * using option '-D' in VM command line.
 * <p>
 * The following options are currently recognized:
 * <ul>
 * <li><code>jpda.settings.debuggeeJavaHome</code>
 *   - path to Java bundle to run debuggee on
 * <li><code>jpda.settings.debuggeeJavaExec</code>
 *   - name of Java executable to run debuggee on
 * <li><code>jpda.settings.debuggeeJavaPath</code>
 *   - full path to Java executable to run debuggee on
 * <li><code>jpda.settings.debuggeeAgentName</code>
 *   - name of agent native library
 * <li><code>jpda.settings.debuggeeAgentExtraOptions</code>
 *   - extra options for agent
 * <li><code>jpda.settings.debuggeeClassName</code>
 *   - full name of class to run debuggee with
 * <li><code>jpda.settings.debuggeeVMExtraOptions</code>
 *   - extra options to run debuggee with
 * <li><code>jpda.settings.transportWrapperClass</code>
 *   - class name of TransportWrapper implementation
 * <li><code>jpda.settings.transportAddress</code>
 *   - address for JDWP connection
 * <li><code>jpda.settings.connectorKind</code>
 *   - type of JDWP connection (attach or listen)
 * <li><code>jpda.settings.syncPort</code>
 *   - port number for sync connection
 * <li><code>jpda.settings.timeout</code>
 *   - timeout used in JPDA tests
 * <li><code>jpda.settings.waitingTime</code>
 *   - timeout for waiting events
 * </ul>
 * <li><code>jpda.settings.verbose</code>
 *   - flag that disables (default) or enables writing messages to the log
 * </ul>
 * All options have default values, if they are not specified.
 *  
 */
public class TestOptions {

    public static final int DEFAULT_TIMEOUT = 100 * 1000 * 60;

    public static final int DEFAULT_WAITING_TIME = DEFAULT_TIMEOUT;
    
    public static final String DEFAULT_ATTACHING_ADDRESS = "127.0.0.1:9898";

    //    public static final int DEFAULT_SYNC_PORT = 7777;
    public static final int DEFAULT_SYNC_PORT = 0;

    public static final String DEFAULT_TRANSPORT_WRAPPER = "org.apache.harmony.share.framework.jpda.jdwp.SocketTransportWrapper";

    private long waitingTime = -1;

    private long timeout = -1;

    
    /**
     * Constructs an instance of this class.
     */
    public TestOptions() {
        super();
    }

    /**
     * Returns path to Java bundle to run debuggee on.
     * 
     * @return option "jpda.settings.debuggeeJavaHome" or system property
     *         "java.home" by default.
     */
    public String getDebuggeeJavaHome() {
        return System.getProperty("jpda.settings.debuggeeJavaHome", System
                .getProperty("java.home"));
    }

    /**
     * Returns name of Java exectutable to run debuggee on.
     * 
     * @return option "jpda.settings.debuggeeJavaExec" or "java" by default.
     */
    public String getDebuggeeJavaExec() {
        return System.getProperty("jpda.settings.debuggeeJavaExec", "java");
    }

    /**
     * Returns full path to Java executable to run debuggee on.
     * 
     * @return option "jpda.settings.debuggeeJavaPath" or construct path from
     *         getDebuggeeJavaHome() and getDebuggeeJavaExec() by default.
     */
    public String getDebuggeeJavaPath() {
        return System.getProperty("jpda.settings.debuggeeJavaPath",
                getDebuggeeJavaHome() + "/bin/" + getDebuggeeJavaExec());
    }

    /**
     * Returns class name of TransportWrapper implementation.
     * 
     * @return option "jpda.settings.transportWrapperClass" or
     *         DEFAULT_TRANSPORT_WRAPPER by default.
     */
    public String getTransportWrapperClassName() {
        return System.getProperty("jpda.settings.transportWrapperClass",
                DEFAULT_TRANSPORT_WRAPPER);
    }

    /**
     * Returns sddress for JDWP connection or null for dynamic address.
     * 
     * @return option "jpda.settings.transportAddress" or null by default.
     */
    public String getTransportAddress() {
        return System.getProperty("jpda.settings.transportAddress", null);
    }
    
    /**
     * Sets address to attach to debuggee.
     * 
     * @param address to attach
     */
    public void setTransportAddress(String address) {
        System.setProperty("jpda.settings.transportAddress", address);
    }

    /**
     * Returns name of JDWP agent library.
     * 
     * @return option "jpda.settings.debuggeeAgentName" or "jdwp" by default
     */
    public String getDebuggeeAgentName() {
        return System.getProperty("jpda.settings.debuggeeAgentName", "jdwp");
    }

    /**
     * Returns string with extra options for agent.
     * 
     * @return option "jpda.settings.debuggeeAgentExtraOptions" or "" by default
     */
    public String getDebuggeeAgentExtraOptions() {
        return System
                .getProperty("jpda.settings.debuggeeAgentExtraOptions", "");
    }

    /**
     * Returns string with all options for agent including specified connection
     * address.
     *  
     * @param address - address to attach
     * @param isDebuggerListen - true if debugger is listening for connection
     * 
     * @return string with all agent options
     */
    public String getDebuggeeAgentOptions(String address, boolean isDebuggerListen) {
        String serv;
        if (isDebuggerListen) {
            serv = "n";
        } else {
            serv = "y";
        }

        return System.getProperty("jpda.settings.debuggeeAgentOptions",
                "transport=dt_socket,address=" + address + ",server=" + serv
                        + getDebuggeeAgentExtraOptions());
    }

    /**
     * Returns string with all options for agent including specified connection
     * address (only for debugger in listening mode). It just calls
     * <ul>
     * <li><code>getDebuggeeAgentOptions(address, true)</code></li>
     * </ul>
     *  
     * @deprecated This method is used as workaround for old tests and will be removed soon. 
     *  
     * @param address - address to attach
     * 
     * @return string with all agent options
     */
    public String getDebuggeeAgentOptions(String address) {
        return getDebuggeeAgentOptions(address, true);
    }
    
    /**
     * Returns VM classpath value to run debuggee with.
     * 
     * @return system property "java.class.path" by default.
     */
    public String getDebuggeeClassPath() {
        return System.getProperty("java.class.path");
    }

    /**
     * Returns full name of the class to start debuggee with.
     * 
     * @return option "jpda.settings.debugeeClassName" or
     *         "jpda.jdwp.share.debuggee.HelloWorld" by default
     */
    public String getDebuggeeClassName() {
        return System.getProperty("jpda.settings.debugeeClassName",
                "jpda.jdwp.share.debuggee.HelloWorld");
    }

    /**
     * Sets full name of the class to start debuggee with.
     * 
     * @param full
     *            class name
     */
    public void setDebuggeeClassName(String className) {
        System.setProperty("jpda.settings.debugeeClassName", className);
    }

    /**
     * Returns string with extra options to start debuggee with.
     * 
     * @return option "jpda.settings.debuggeeVMExtraOptions" or
     *         "-Dvm.assert_dialog=false" by default
     */
    public String getDebuggeeVMExtraOptions() {
    	String extOpts = System.getProperty("jpda.settings.debuggeeVMExtraOptions",
    			"-Dvm.assert_dialog=false");
    	extOpts = extOpts + " -Djpda.settings.verbose=" + isVerbose();
        return extOpts;
    }

    /**
     * Returns string representation of TCP/IP port for synchronization channel.
     * 
     * @return string with port number or null
     */
    public String getSyncPortString() {
        return System.getProperty("jpda.settings.syncPort");
    }

    /**
     * Returns type of connection with debuggee.
     * 
     * @return system property "jpda.settings.connectorKind" or "listen" by default.
     */
    public String getConnectorKind() {
        return System.getProperty("jpda.settings.connectorKind", "listen");
    }

    /**
     * Checks if attach connection with debuggee.
     * 
     * @return true, if attach connection, false otherwise.
     */
    public boolean isAttachConnectorKind() {
        return ((getConnectorKind()).equals("attach"));

    }

    /**
     * Checks if listen connection with debuggee.
     * 
     * @return true, if listen connection, false otherwise.
     */
    public boolean isListenConnectorKind() {
        return (getConnectorKind().equals("listen"));
    }

    /**
     * Sets connectorKind to attach to debuggee.
     */
    public void setAttachConnectorKind() {
        setConnectorKind("attach");
    }

    /**
     * Sets connectorKind to listen connection from debuggee.
     */
    public void setListenConnectorKind() {
        setConnectorKind("listen");
    }
    
    /**
     * Sets kind of connector (attach or listen).
     */
    public void setConnectorKind(String kind) {
        System.setProperty("jpda.settings.connectorKind", kind);
    }

    /**
     * Returns kind of launching debuggee VM, which can be "auto" or "manual".
     * 
     * @return option "jpda.settings.debuggeeLaunchKind" or "auto" by default.
     */
    public String getDebuggeeLaunchKind() {
        return System.getProperty("jpda.settings.debuggeeLaunchKind", "auto");
    }

    /**
     * Returns TCP/IP port for synchronization channel.
     * 
     * @return string with port number or null
     */
    public int getSyncPortNumber() {
        String buf = getSyncPortString();
        if (buf == null) {
            return DEFAULT_SYNC_PORT;
        }

        try {
            return Integer.parseInt(buf);
        } catch (NumberFormatException e) {
            throw new TestErrorException(e);
        }
    }

    /**
     * Returns timeout for JPDA tests in milliseconds.
     * 
     * @return option "jpda.settings.timeout" or DEAFULT_TIMEOUT by default.
     */
    public long getTimeout() {
        if (timeout < 0) {
            timeout = DEFAULT_TIMEOUT;
            String buf = System.getProperty("jpda.settings.timeout");
            if (buf != null) {
                try {
                    timeout = Long.parseLong(buf);
                } catch (NumberFormatException e) {
                    throw new TestErrorException(e);
                }
            }
        }
        return timeout;
    }

    /**
     * Sets timeout for JPDA tests in milliseconds.
     * 
     * @param timeout
     *            timeout to be set
     */
    public void setTimeout(long timeout) {
        if (timeout < 0) {
            throw new TestErrorException("Cannot set negative timeout value: "
                    + timeout);
        }
        this.timeout = timeout;
    }

    /**
     * Returns waiting time for events in milliseconds.
     * 
     * @return waiting time
     */
    public long getWaitingTime() {
        if (waitingTime < 0) {
            waitingTime = DEFAULT_WAITING_TIME;
            String buf = System.getProperty("jpda.settings.waitingTime");
            if (buf != null) {
                try {
                    waitingTime = Long.parseLong(buf);
                } catch (NumberFormatException e) {
                    throw new TestErrorException(e);
                }
            }
        }
        return waitingTime;
    }

    /**
     * Sets waiting time for events in milliseconds.
     * 
     * @param waitingTime
     *            waiting time to be set
     */
    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }
    
    /**
     * Returns whether print to log is enabled.
     * 
     * @return false (default) if log is disabled or true otherwise.
     */
    public boolean isVerbose() {
    	return isTrue(System.getProperty("jpda.settings.verbose", "true"));
    }

    /**
     * Converts text to boolean.
     * 
     * @return false or true.
     */
    static public boolean isTrue(String str) {
    	return str != null && (
    		str.equalsIgnoreCase("true") ||
    		str.equalsIgnoreCase("yes") ||
    		str.equalsIgnoreCase("on") ||
    		str.equals("1"));
    }
}