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

// JDK
import java.util.HashMap;

// JMX 
import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;

// JMX Remote API 
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

// Share MBeans
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.*;

// Test Harness 
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class ConnectorServerTest extends MultiCase {

    final MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();
    
    HashMap enviroment = null; 

    /**
     * Test perfom operation with connector server through RMI protocol
     */
    public Result testRMIConnectorServer() throws Exception {
        try {
            
            log.info("Create JMX Service URL");
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://");

            // Create a JMXConnectorServer
            log.info("Create JMXConnectorServer");
            JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(url, enviroment, mbeanServer);

            log.info("Start the server");
            server.start();
            JMXConnector client = server.toJMXConnector(enviroment);
            
            log.info("Connect client to the server");
            client.connect(enviroment);
            client.getClass();
            
            log.info("connection id - " + client.getConnectionId());
            log.info("Create mbean server connection");
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
           
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
            mbsc.getMBeanCount();
            mbsc.getDefaultDomain();
            mbsc.getDomains();
            mbsc.getMBeanInfo(simple);
            mbsc.getObjectInstance(simple);
            mbsc.isRegistered(simple);
            mbsc.isInstanceOf(simple, simpleClass);

            // Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            mbsc.addNotificationListener(simple, listener, null, null);
            mbsc.removeNotificationListener(simple, listener);

            NotificationFilterSupport filter = new NotificationFilterSupport();
            Object obj = new Object();
            
            mbsc.addNotificationListener(simple, listener, filter, obj);
            mbsc.removeNotificationListener(simple, listener, filter, obj);
            
            log.info("Unregister MBean from the connection server");
            mbsc.unregisterMBean(simple);
            
            client.close();
            
            server.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }

        return result();
    }

    /**
     * Test perfom operation with connector server through IIOP protocol
     */
    public Result testIIOPConnectorServer() throws Exception {
        try {
            
            log.info("Create JMX Service URL");
            JMXServiceURL url = new JMXServiceURL("service:jmx:iiop://");

            // Create a JMXConnectorServer
            log.info("Create JMXConnectorServer");
            JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(url, enviroment, mbeanServer);

            log.info("Start the server");
            server.start();
            JMXConnector client = server.toJMXConnector(enviroment);
            
            log.info("Connect client to the server");
            client.connect(enviroment);
            log.info("connection id - " + client.getConnectionId());
            
            log.info("Create mbean server connection");
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
            
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
            mbsc.getMBeanCount();
            mbsc.getDefaultDomain();
            mbsc.getDomains();
            mbsc.getMBeanInfo(simple);
            mbsc.getObjectInstance(simple);
            mbsc.isRegistered(simple);
            mbsc.isInstanceOf(simple, simpleClass);

            // Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            mbsc.addNotificationListener(simple, listener, null, null);
            mbsc.removeNotificationListener(simple, listener);

            NotificationFilterSupport filter = new NotificationFilterSupport();
            Object obj = new Object();
            
            mbsc.addNotificationListener(simple, listener, filter, obj);
            mbsc.removeNotificationListener(simple, listener, filter, obj);
            
            log.info("Unregister MBean from the connection server");
            mbsc.unregisterMBean(simple);
            
            client.close();
            
            server.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }

        return result();

    }
    /**
     * Test perfom operation with connector server through JMXMP protocol
     */
    public Result testJMXMPConnectorServer() throws Exception {
        try {
            
            log.info("Create JMX Service URL");
            JMXServiceURL url = new JMXServiceURL("service:jmx:jmxmp://");

            // Create a JMXConnectorServer
            log.info("Create JMXConnectorServer");
            JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(url, enviroment, mbeanServer);

            log.info("Start the server");
            server.start();
            JMXConnector client = server.toJMXConnector(enviroment);
            
            log.info("Connect client to the server");
            client.connect(enviroment);
            log.info("connection id - " + client.getConnectionId());
            
            log.info("Create mbean server connection");
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
            
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
            mbsc.getMBeanCount();
            mbsc.getDefaultDomain();
            mbsc.getDomains();
            mbsc.getMBeanInfo(simple);
            mbsc.getObjectInstance(simple);
            mbsc.isRegistered(simple);
            mbsc.isInstanceOf(simple, simpleClass);

            // Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            mbsc.addNotificationListener(simple, listener, null, null);
            mbsc.removeNotificationListener(simple, listener);

            NotificationFilterSupport filter = new NotificationFilterSupport();
            Object obj = new Object();
            
            mbsc.addNotificationListener(simple, listener, filter, obj);
            mbsc.removeNotificationListener(simple, listener, filter, obj);
            
            log.info("Unregister MBean from the connection server");
            mbsc.unregisterMBean(simple);
            client.close();
            server.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }
        return result();
    }
    /**
     * Test perfom operation with connector server through HTTP protocol
     */
  
    public Result testHTTPConnectorServer() throws Exception {
        try {
            
            log.info("Create JMX Service URL");
            JMXServiceURL url = new JMXServiceURL("service:jmx:http://");

            // Create a JMXConnectorServer
            log.info("Create JMXConnectorServer");
            JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(url, enviroment, mbeanServer);

            log.info("Start the server");
            server.start();
            JMXConnector client = server.toJMXConnector(enviroment);
            
            log.info("Connect client to the server");
            client.connect(enviroment);
            log.info("connection id - " + client.getConnectionId());
            
            log.info("Create mbean server connection");
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
            
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
            mbsc.getMBeanCount();
            mbsc.getDefaultDomain();
            mbsc.getDomains();
            mbsc.getMBeanInfo(simple);
            mbsc.getObjectInstance(simple);
            mbsc.isRegistered(simple);
            mbsc.isInstanceOf(simple, simpleClass);

            // Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            mbsc.addNotificationListener(simple, listener, null, null);
            mbsc.removeNotificationListener(simple, listener);

            NotificationFilterSupport filter = new NotificationFilterSupport();
            Object obj = new Object();
            
            mbsc.addNotificationListener(simple, listener, filter, obj);
            mbsc.removeNotificationListener(simple, listener, filter, obj);
            
            log.info("Unregister MBean from the connection server");
            mbsc.unregisterMBean(simple);
            client.close();
            server.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }
        return result();
    }

    public class Listener implements NotificationListener {
        public void handleNotification(Notification ntf, Object obj) {
        }
    }
    public static void main(String[] args) {
            ConnectorServerTest run = new ConnectorServerTest();
            System.exit(run.test(args));
     }
}
