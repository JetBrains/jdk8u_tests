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

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBParameter;
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBParameterExtends;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class ForwarderTest extends MultiCase {

    public String protocol = "rmi";
    
    /**
     * Test for the method setMBeanServer(MBeanServer mbs)
     * 
     * @see javax.management.remote.MBeanServerForwarder#setMBeanServer(MBeanServer mbs)
     */
    public Result testSetForwarder() throws Exception {

        MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();

        try {
            
            log.info("Create JMX Service URL");
            JMXServiceURL url = new JMXServiceURL("service:jmx:" + protocol
                    + "://");

            // Create a JMXConnectorServer
            log.info("Create JMXConnectorServer");
            JMXConnectorServer server = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, mbeanServer);

            log.info("Set MBean server forwarder");
            ServerImpl mbsf = new ServerImpl();
            mbsf.setMBeanServer(mbeanServer);
            server.setMBeanServerForwarder(mbsf);

            log.info("Start the server");
            server.start();

            JMXConnector client = server.toJMXConnector(null);
            client.connect(null);
            MBeanServerConnection mbsc = client.getMBeanServerConnection();

            // Create Simple MBean
            ObjectName simple = new ObjectName("MBeans:type=Simple");
            ObjectName simple2 = new ObjectName("MBeans:type=Simple2");
            ObjectName simple3 = new ObjectName("MBeans:type=Simple3");
            ObjectName simple4 = new ObjectName("MBeans:type=Simple4");

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

            log.info("count = " + mbsf.checkCount);
            if (mbsf.checkCount < 35)
                return failed("some method was not forward to appropiate object");

            // Close the connection
            log.info("Close connection");
            client.close();

            // Stop the connector server
            log.info("Stop the sever");
            server.stop();

        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }
        return result();
    }
    
    /**
     * Test for the method getMBeanServer()
     * 
     * @see javax.management.remote.MBeanServerForwarder#getMBeanServer()
     */
     public Result testGetForwarder() throws Exception {
        MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();
        try {
 
            log.info("Create JMX Service URL");
            JMXServiceURL url = new JMXServiceURL("service:jmx:" + protocol + "://");

            // Create JMXConnectorServer
            log.info("Create JMXConnectorServer");

            JMXConnectorServer server = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, mbeanServer);

            // Set MBean server forwarder
            log.info("Set MBean server forwarder");
            ServerImpl mbsf = new ServerImpl();
            mbsf.setMBeanServer(mbeanServer);
            server.setMBeanServerForwarder(mbsf);
            MBeanServer mbeanServerChk = mbsf.getMBeanServer();
            
            //Check MBean server forwarder
            log.info("Check MBean server forwarder");
            if (mbeanServerChk == mbeanServer) {
                return passed();
            } else {
                return failed("Get wrong MBean Server Object");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Unexpected exception");
        }
    }

    public class Listener implements NotificationListener {
        public void handleNotification(Notification ntf, Object obj) {
        }
    }

    public static void main(String[] args) {
        ForwarderTest run = new ForwarderTest();
        System.exit(run.test(args));
    }
}
