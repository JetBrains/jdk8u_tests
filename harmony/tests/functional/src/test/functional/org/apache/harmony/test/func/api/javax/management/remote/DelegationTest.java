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

import java.security.Policy;
import java.util.Collections;
import java.util.HashMap;
import javax.security.auth.Subject;
import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXPrincipal;
import javax.management.remote.JMXServiceURL;
import javax.management.ObjectName;
import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBParameter;
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBParameterExtends;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class DelegationTest extends MultiCase {

    public static String protocol = "rmi";

    public Result testPositiveDelegation() throws Exception {

        final MBeanServer mbs = MBeanServerFactory.createMBeanServer();

        try {

            final JMXServiceURL url = new JMXServiceURL("service:jmx:"
                    + protocol + "://");

            Subject sbj = new Subject();
            SecureAuthenticator auth = new SecureAuthenticator(sbj);
            HashMap enviroment = new HashMap();
            enviroment.put(JMXConnectorServer.AUTHENTICATOR, auth);
            JMXConnectorServer srv = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, enviroment, mbs);

            srv.start();
            final JMXConnector client = srv.toJMXConnector(enviroment);
            client.connect(enviroment);
            final MBeanServerConnection mbsc = client
                    .getMBeanServerConnection();
            final SecurePolicy newPolicy = new SecurePolicy();
            newPolicy.delegate = true;
            // Save current and set new policy
            Policy savePolicy = null;
            savePolicy = Policy.getPolicy();
            Policy.setPolicy(newPolicy);

            // Make operation via connection
            ObjectName simple = new ObjectName("MBeans:type=Simple");
            log.info("Create MBean");
            mbsc
                    .createMBean(
                            "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple",
                            simple, null, null);

            log.info("MBean numbers = " + mbsc.getMBeanCount());
            log.info("State = " + mbsc.getAttribute(simple, "State"));
            mbsc.setAttribute(simple, new Attribute("State", "changed"));
            log.info("State = " + mbsc.getAttribute(simple, "State"));
            mbsc.invoke(simple, "echo", null, null);
            TMBParameterExtends paramExt = new TMBParameterExtends();
            TMBParameter param = new TMBParameter();
            Attribute attr = new Attribute("TMBParameter", param);
            Attribute attrExt = new Attribute("TMBParameter", paramExt);
            mbsc.setAttribute(simple, attrExt);
            mbsc.setAttribute(simple, attr);
            mbsc.invoke(simple, "returnSomeClass", null, null);
            // Add notification listener on MBean
            ClientListener listener = new ClientListener();
            log.info("Add and remove notification listener");
            mbsc.addNotificationListener(simple, listener, null, null);
            mbsc.removeNotificationListener(simple, listener);
            mbsc.unregisterMBean(simple);
            // Set policy back
            Policy.setPolicy(savePolicy);
            // Close connection
            client.close();
            // Stop connector server
            srv.stop();

        } catch (Exception e) {

            e.printStackTrace();
            return failed("Unexpected exception - " + e);
        }
        return result();

    }

    public Result testNegativeDelegation() throws Exception {

        final MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        JMXConnector client = null;
        JMXConnectorServer srv = null;
        try {

            final JMXServiceURL url = new JMXServiceURL("service:jmx:"
                    + protocol + "://");
            Subject sbj = null;
            SecureAuthenticator auth = new SecureAuthenticator(sbj);
            HashMap enviroment = new HashMap();

            enviroment.put(JMXConnectorServer.AUTHENTICATOR, auth);

            srv = JMXConnectorServerFactory.newJMXConnectorServer(url,
                    enviroment, mbs);
            srv.start();
            client = srv.toJMXConnector(enviroment);
            client.connect(enviroment);
            Subject subject = new Subject(true, Collections
                    .singleton(new JMXPrincipal("test")),
                    Collections.EMPTY_SET, Collections.EMPTY_SET);

            final MBeanServerConnection mbsc = client
                    .getMBeanServerConnection(subject);

            ObjectName mbaen = new ObjectName("MBeans:type=Simple");
            mbsc
                    .createMBean(
                            "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple",
                            mbaen, null, null);

        } catch (SecurityException e) {
            client.close();
            srv.stop();
            return passed("Expected exception - " + e);
        } catch (Exception e) {
            client.close();
            srv.stop();
            e.printStackTrace();
            return failed("Unexpected exception - " + e);
        }
        return result();
    }

    public Result testDelegateSubjectBadConnection() throws Exception {
        final MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        JMXConnector client = null;
        JMXConnectorServer srv = null;
        Policy savePolicy = null;
        try {

            final JMXServiceURL url = new JMXServiceURL("service:jmx:"
                    + protocol + "://");

            Subject sbj = new Subject(true, Collections
                    .singleton(SecurePolicy.badPrincipal),
                    Collections.EMPTY_SET, Collections.EMPTY_SET);
            final SecureAuthenticator auth = new SecureAuthenticator(sbj);

            HashMap enviroment = new HashMap();

            enviroment.put(JMXConnectorServer.AUTHENTICATOR, auth);

            srv = JMXConnectorServerFactory.newJMXConnectorServer(url,
                    enviroment, mbs);

            srv.start();

            client = srv.toJMXConnector(enviroment);

            client.connect(enviroment);

            final Subject subject = new Subject(true, Collections
                    .singleton(SecurePolicy.badPrincipal),
                    Collections.EMPTY_SET, Collections.EMPTY_SET);

            final MBeanServerConnection mbsc = client
                    .getMBeanServerConnection(subject);

            final SecurePolicy newPolicy = new SecurePolicy();
            newPolicy.delegate = true;

            // Save current and set new policy
            savePolicy = Policy.getPolicy();

            Policy.setPolicy(newPolicy);

            ObjectName mbaen = new ObjectName("MBeans:type=Simple");
            mbsc
                    .createMBean(
                            "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple",
                            mbaen, null, null);

        } catch (SecurityException e) {
            Policy.setPolicy(savePolicy);
            client.close();
            srv.stop();
            return passed("Expected exception - " + e);

        } catch (Exception e) {
            Policy.setPolicy(savePolicy);
            client.close();
            srv.stop();
            e.printStackTrace();
            return failed("Unexpected exception - " + e);
        }
        return result();
    }

    public Result testDelegateSubjectCorrectConnection() throws Exception {
        final MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        JMXConnector client = null;
        JMXConnectorServer srv = null;
        Policy savePolicy = null;
        try {

            final JMXServiceURL url = new JMXServiceURL("service:jmx:"
                    + protocol + "://");

            Subject sbj = new Subject(true, Collections
                    .singleton(SecurePolicy.correctPrincipal),
                    Collections.EMPTY_SET, Collections.EMPTY_SET);
            final SecureAuthenticator auth = new SecureAuthenticator(sbj);

            HashMap enviroment = new HashMap();

            enviroment.put(JMXConnectorServer.AUTHENTICATOR, auth);

            srv = JMXConnectorServerFactory.newJMXConnectorServer(url,

            enviroment, mbs);

            srv.start();

            client = srv.toJMXConnector(enviroment);

            client.connect(enviroment);

            final Subject subject = new Subject(true, Collections
                    .singleton(SecurePolicy.correctPrincipal),
                    Collections.EMPTY_SET, Collections.EMPTY_SET);

            final MBeanServerConnection mbsc = client
                    .getMBeanServerConnection(subject);

            final SecurePolicy newPolicy = new SecurePolicy();
            newPolicy.delegate = true;

            // Save current and set new policy

            savePolicy = Policy.getPolicy();

            Policy.setPolicy(newPolicy);

            ObjectName simple = new ObjectName("MBeans:type=Simple");
            mbsc
                    .createMBean(
                            "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple",
                            simple, null, null);

            log.info("MBean numbers = " + mbsc.getMBeanCount());

            log.info("State = " + mbsc.getAttribute(simple, "State"));

            mbsc.setAttribute(simple, new Attribute("State", "changed"));

            log.info("State = " + mbsc.getAttribute(simple, "State"));

            mbsc.invoke(simple, "echo", null, null);

            TMBParameterExtends paramExt = new TMBParameterExtends();
            TMBParameter param = new TMBParameter();

            Attribute attr = new Attribute("TMBParameter", param);
            Attribute attrExt = new Attribute("TMBParameter", paramExt);

            mbsc.setAttribute(simple, attrExt);
            mbsc.setAttribute(simple, attr);
            mbsc.invoke(simple, "returnSomeClass", null, null);

            // Add notification listener on MBean
            ClientListener listener = new ClientListener();

            log.info("Add and remove notification listener");
            mbsc.addNotificationListener(simple, listener, null, null);
            mbsc.removeNotificationListener(simple, listener);
            mbsc.unregisterMBean(simple);

            Policy.setPolicy(savePolicy);
            client.close();
            srv.stop();

        } catch (Exception e) {
            Policy.setPolicy(savePolicy);
            client.close();
            srv.stop();
            e.printStackTrace();
            return failed("Unexpected exception - " + e);
        }
        return result();
    }

    public class ClientListener implements NotificationListener {
        public void handleNotification(Notification ntf, Object obj) {
            log.info("\nReceived notification: " + ntf);
        }
    }

    public static void main(String[] args) {
        DelegationTest run = new DelegationTest();
        System.exit(run.test(args));
    }
}
