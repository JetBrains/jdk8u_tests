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

// Test Harness 
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class ConnectorClientTest extends MultiCase {

    final MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();
    
    HashMap enviroment = null; 

    /**
     * Test perfom operation with connector client through RMI protocol
     */
    public Result testRMIConnectorClient() throws Exception {
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
            log.info("Connection id - " + client.getConnectionId());
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
           
             //Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            Object obj = new Object();
            NotificationFilterSupport filter = new NotificationFilterSupport();
            client.addConnectionNotificationListener(listener,filter,obj );
            client.removeConnectionNotificationListener(listener);
            client.addConnectionNotificationListener(listener,filter,obj );
            client.removeConnectionNotificationListener(listener,filter,obj );
            
            log.info("Close client connection");
            client.close();
            log.info("Stop the server");
            server.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }

        return result();
    }

    /**
     * Test perfom operation with connector client through IIOP protocol
     */
    public Result testIIOPConnectorClient() throws Exception {
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
            client.getClass();
            log.info("Connection id - " + client.getConnectionId());
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
           
             //Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            Object obj = new Object();
            NotificationFilterSupport filter = new NotificationFilterSupport();
            client.addConnectionNotificationListener(listener,filter,obj );
            client.removeConnectionNotificationListener(listener);
            client.addConnectionNotificationListener(listener,filter,obj );
            client.removeConnectionNotificationListener(listener,filter,obj );
            
            log.info("Close client connection");
            client.close();
            log.info("Stop the server");
            server.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }

        return result();
    }
    /**
     * Test perfom operation with connector client through JMXMP protocol
     */
    public Result testJMXMPConnectorClient() throws Exception {
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
            client.getClass();
            log.info("Connection id - " + client.getConnectionId());
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
           
             //Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            Object obj = new Object();
            NotificationFilterSupport filter = new NotificationFilterSupport();
            client.addConnectionNotificationListener(listener,filter,obj );
            client.removeConnectionNotificationListener(listener);
            client.addConnectionNotificationListener(listener,filter,obj );
            client.removeConnectionNotificationListener(listener,filter,obj );
            
            log.info("Close client connection");
            client.close();
            log.info("Stop the server");
            server.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }

        return result();
    }
    /**
     * Test perfom operation with connector client through HTTP protocol
     */
  
    public Result testHTTPConnectorClient() throws Exception {
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
            client.getClass();
            log.info("Connection id - " + client.getConnectionId());
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
           
             //Add than remove notification listener on MBean
            log.info("Add than remove notification listener on MBean");
            Listener listener = new Listener();
            Object obj = new Object();
            NotificationFilterSupport filter = new NotificationFilterSupport();
            client.addConnectionNotificationListener(listener,filter,obj );
            client.removeConnectionNotificationListener(listener);
            client.addConnectionNotificationListener(listener,filter,obj );
            client.removeConnectionNotificationListener(listener,filter,obj );
            
            log.info("Close client connection");
            client.close();
            log.info("Stop the server");
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
            ConnectorClientTest run = new ConnectorClientTest();
            System.exit(run.test(args));
     }
}
