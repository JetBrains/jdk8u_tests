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
package org.apache.harmony.test.func.api.javax.management.remote;

//JMX
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

//JMX Remote API
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;

//RMI registry
import java.rmi.registry.Registry;

//DRL Harness
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class RMIConnectorTest extends MultiCase {

    // MBean Server
    MBeanServer server;

    // Connector Server
    JMXConnectorServer cs;

    public Result testRMIConnectorServer01() throws Exception {
        try {
            server = MBeanServerFactory.newMBeanServer();
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://");
            RMIConnectorServer test = new RMIConnectorServer(url, null);
            ObjectName name = new ObjectName("test:type=rmi");
            server.registerMBean(test, name);
            test.start();
            test.stop();
        } catch (Exception e) {
            return failed("Unexpected exception" + e);
        }

        return result();
    }

    public Result testRMIConnectorServer02() throws Exception {
        try {
            MBeanServer server = MBeanServerFactory.newMBeanServer();
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://");
            RMIConnectorServer test = new RMIConnectorServer(url, null, null);
            ObjectName name = new ObjectName("test:type=rmi");
            server.registerMBean(test, name);
            test.start();
            test.stop();

        } catch (Exception e) {
            return failed("Unexpected exception" + e);
        }
        return result();
    }

    public Result testRMIConnectorBase() throws Exception {
        try {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(9119);
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            JMXServiceURL url = new JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://localhost:9119/server");
            JMXConnectorServer cs = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, server);
            cs.start();
            JMXConnector jmxc = JMXConnectorFactory.connect(url);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            jmxc.close();
            cs.stop();
        } catch (Exception e) {
            return failed("Unexpected exception - " + e);
        }
        return result();
    }

    public Result testIOProblemRMIConnectorStart() throws Exception {
        try {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1199);
            MBeanServer server = MBeanServerFactory.newMBeanServer();
            JMXServiceURL url = new JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://localhost:1199/server");
            JMXConnectorServer cs = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, server);
            cs.start();
            JMXConnector jmxc = JMXConnectorFactory.connect(url);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            jmxc.close();
            mbsc.getDomains();

            return failed("IOException does not thrown");
        } catch (java.io.IOException e) {
            return passed("Got expected exception - " + e);
        } catch (Exception e) {
            return failed("Unexpected exception - " + e);
        }
    }

    public Result testRMIConnectorMakeMBeanOperations() throws Exception {
        try {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(9111);
            MBeanServer server = MBeanServerFactory.createMBeanServer();
            JMXServiceURL url = new JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://localhost:9111/server");
            JMXConnectorServer cs = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, server);
            cs.start();
            JMXConnector jmxc = JMXConnectorFactory.connect(url);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            ObjectName mbeanName = new ObjectName("MBeans:type=Simple");
            mbsc
                    .createMBean(
                            "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple",
                            mbeanName, null, null);
            mbsc.invoke(mbeanName, "echo", null, null);
            jmxc.close();
            cs.stop();
        } catch (Exception e) {
            return failed("Unexpected exception - " + e);
        }
        return result();
    }

    public static void main(String[] args) {
        RMIConnectorTest run = new RMIConnectorTest();
        System.exit(run.test(args));
    }
}
