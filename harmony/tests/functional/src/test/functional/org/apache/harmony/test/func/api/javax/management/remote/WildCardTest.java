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

//Share mbeans
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBParameter;
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBParameterExtends;

//Test Harness 
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class WildCardTest extends MultiCase {

    // By default firstIP = secondIP = localhost

    public static String firstIP = "localhost";

    public static String secondIP = "localhost";

    private Result runTest(boolean positive) {
        try {
            log.info("Create mbean server");
            MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();

            JMXServiceURL url1;
            JMXServiceURL url2;
            
            if (firstIP.equals(secondIP)) {
                url1 = new JMXServiceURL("jmxmp", firstIP, 1111);
                url2 = new JMXServiceURL("jmxmp", secondIP, 3333);

            } else {
                url1 = new JMXServiceURL("jmxmp", firstIP, 1111);
                url2 = new JMXServiceURL("jmxmp", secondIP, 1111);
            }

            log.info("First JMX Service URL - " + url1);
            log.info("Sirst JMX Service URL - " + url2);
            log.info("Create enviroment for wildcard");
            HashMap enviroment = new HashMap();
            if (!positive)
                enviroment.put("jmx.remote.server.address.wildcard", "false");

            // Create a JMXConnectorServer
            log.info("Create JMXConnectorServer");
            JMXConnectorServer server1 = JMXConnectorServerFactory
                    .newJMXConnectorServer(url1, enviroment, mbeanServer);
            JMXConnectorServer server2 = JMXConnectorServerFactory
                    .newJMXConnectorServer(url2, enviroment, mbeanServer);

            log.info("Start the server");
            server1.start();
            server2.start();
            JMXConnector client1 = server1.toJMXConnector(enviroment);
            JMXConnector client2 = server2.toJMXConnector(enviroment);
            client1.connect(enviroment);
            client2.connect(enviroment);
            MBeanServerConnection mbsc1 = client1.getMBeanServerConnection();
            MBeanServerConnection mbsc2 = client2.getMBeanServerConnection();
            runOperations(server1, client1, mbsc1);
            runOperations(server2, client2, mbsc2);
            client1.close();
            client2.close();
            server1.stop();
            server2.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception - " + e.getMessage());
        }
        
        return result();
    }

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

    public Result testPositiveWildCardTest() throws Exception {
        log.info("            ");
        log.info("start test PositiveWildCardTest");
        
        return runTest(true);
    }

    public Result testNegotiveWildCardTest() throws Exception {
        log.info("            ");
        log.info("start test NegotiveWildCardTest");
       
        return  runTest(false);
    }

    public class Listener implements NotificationListener {
        public void handleNotification(Notification ntf, Object obj) {
        }
    }

    public static void main(String[] args) {
        WildCardTest run = new WildCardTest();
        System.exit(run.test(args));
    }
}
