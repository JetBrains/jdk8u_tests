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

//JDK
import java.util.HashMap;

//JMX 
import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;

//JMX Remote API 
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

//Java net security
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

//Test Harness 
import org.apache.harmony.test.func.api.javax.management.remote.TLSEnableProtocolTest.Listener;
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBParameter;
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBParameterExtends;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class TLSClientAuthenticationTest extends MultiCase {

    private void runOperations(JMXConnectorServer server, JMXConnector client,
            MBeanServerConnection mbsc) {

        try {

            log.info("connection id - " + client.getConnectionId());

            // Create Simple MBean
            ObjectName simple = new ObjectName("MBeans:type=Simple");

            log.info("Create MBean");
            String simpleClass = "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple";
            mbsc.createMBean(simpleClass, simple);

            // MBean get attribute
            mbsc.getAttribute(simple, "State");
            mbsc.setAttribute(simple, new Attribute("State", "changed"));
            mbsc.getAttribute(simple, "State");

            // MBean set attribute
            TMBParameterExtends paramExt = new TMBParameterExtends();
            TMBParameter param = new TMBParameter();
            Attribute attr = new Attribute("TMBParameter", param);
            Attribute attrExt = new Attribute("TMBParameter", paramExt);
            mbsc.setAttribute(simple, attrExt);
            mbsc.setAttribute(simple, attr);

            // Invokes MBean operation
            mbsc.invoke(simple, "echo", null, null);
            mbsc.invoke(simple, "returnSomeClass", null, null);

            // Get different parameters
            log.info("MBean count - " + mbsc.getMBeanCount());
            log.info("Default domain name - " + mbsc.getDefaultDomain());
            mbsc.getDomains();
            log.info("Simple MBean info - " + mbsc.getMBeanInfo(simple));
            mbsc.getObjectInstance(simple);
            log
                    .info("Is Simple MBean registred - "
                            + mbsc.isRegistered(simple));
            log.info("Check Simple MBean instance - "
                    + mbsc.isInstanceOf(simple, simpleClass));

            // Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            mbsc.addNotificationListener(simple, listener, null, null);
            mbsc.removeNotificationListener(simple, listener);

            NotificationFilterSupport filter = new NotificationFilterSupport();
            Object obj = new Object();

            mbsc.addNotificationListener(simple, listener, filter, obj);
            mbsc.removeNotificationListener(simple, listener, filter, obj);

            log
                    .info("Add than remove notification listener on MBean from clien and server");
            client.addConnectionNotificationListener(listener, filter, obj);
            client.removeConnectionNotificationListener(listener);
            client.addConnectionNotificationListener(listener, filter, obj);
            client.removeConnectionNotificationListener(listener, filter, obj);

            server.addNotificationListener(listener, filter, obj);
            server.removeNotificationListener(listener, filter, obj);
            server.addNotificationListener(listener, filter, obj);
            server.removeNotificationListener(listener);

            // Log server info
            log.info("Perfrom operation with server");
            log.info("JMX Service address - " + server.getAddress());
            server.getAttributes();
            log.info("Server Connection Ids - " + server.getConnectionIds());
            log.info("Server Notification Info - "
                    + server.getNotificationInfo());

            if (!server.isActive()) {
                fail("server not active");
            }
            log.info("Unregister MBean from the mbean connection server");
            mbsc.unregisterMBean(simple);

        } catch (Exception e) {
            fail("Unexpected exception" + e);
            e.printStackTrace();
        }
    }

    public Result testPositiveTLSNeedClientAuthentication() throws Exception {
        log.info("Create mbean server");
        MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();

        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://");
        log.info("Create JMX Service URL - " + url);
        log.info("Create enviroment");
        HashMap enviroment = new HashMap();
        enviroment.put("jmx.remote.tls.need.client.authentication", "true");
        // Create a JMXConnectorServer
        log.info("Create JMXConnectorServer");
        JMXConnectorServer server = JMXConnectorServerFactory
                .newJMXConnectorServer(url, enviroment, mbeanServer);

        log.info("Start the server");
        server.start();
        JMXConnector client = server.toJMXConnector(enviroment);

        // log.info("Connect client to the server");
        client.connect(enviroment);
        MBeanServerConnection mbsc = client.getMBeanServerConnection();
        runOperations(server, client, mbsc);
        client.close();
        server.stop();

        return result();
    }

    public Result testNegotiveTLSNeedClientAuthentication() throws Exception {
        log.info("Create mbean server");
        MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();

        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://");
        log.info("Create JMX Service URL - " + url);
        log.info("Create enviroment");
        HashMap enviroment = new HashMap();
        enviroment.put("jmx.remote.tls.need.client.authentication", "false");
        // Create a JMXConnectorServer
        log.info("Create JMXConnectorServer");
        JMXConnectorServer server = JMXConnectorServerFactory
                .newJMXConnectorServer(url, enviroment, mbeanServer);

        log.info("Start the server");
        server.start();
        JMXConnector client = server.toJMXConnector(enviroment);

        // log.info("Connect client to the server");
        client.connect(enviroment);
        MBeanServerConnection mbsc = client.getMBeanServerConnection();
        runOperations(server, client, mbsc);
        client.close();
        server.stop();

        return result();
    }

   
        public Result testPositiveTLSWantClientAuthentication() throws Exception {
            log.info("Create mbean server");
            MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();

            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://");
            log.info("Create JMX Service URL - " + url);
            log.info("Create enviroment");
            HashMap enviroment = new HashMap();
            enviroment.put("jmx.remote.tls.want.client.authentication", "true");
            // Create a JMXConnectorServer
            log.info("Create JMXConnectorServer");
            JMXConnectorServer server = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, enviroment, mbeanServer);

            log.info("Start the server");
            server.start();
            JMXConnector client = server.toJMXConnector(enviroment);

            // log.info("Connect client to the server");
            client.connect(enviroment);
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
            runOperations(server, client, mbsc);
            client.close();
            server.stop();

            return result();
        }

        public Result testNegotiveTLSWantClientAuthentication() throws Exception {
            log.info("Create mbean server");
            MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();

            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://");
            log.info("Create JMX Service URL - " + url);
            log.info("Create enviroment");
            HashMap enviroment = new HashMap();
            enviroment.put("jmx.remote.tls.want.client.authentication", "false");
            // Create a JMXConnectorServer
            log.info("Create JMXConnectorServer");
            JMXConnectorServer server = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, enviroment, mbeanServer);

            log.info("Start the server");
            server.start();
            JMXConnector client = server.toJMXConnector(enviroment);

            // log.info("Connect client to the server");
            client.connect(enviroment);
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
            runOperations(server, client, mbsc);
            client.close();
            server.stop();

            return result();
        }
    
    public class Listener implements NotificationListener {
        public void handleNotification(Notification ntf, Object obj) {
        }
    }

    public static void main(String[] args) {
        TLSClientAuthenticationTest run = new TLSClientAuthenticationTest();
        System.exit(run.test(args));
    }
}
