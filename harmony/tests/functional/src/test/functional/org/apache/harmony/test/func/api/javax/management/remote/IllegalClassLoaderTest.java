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

//Java Core
import java.util.HashMap;
import java.net.URL;

//JMX
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.loading.MLet;

//JMX Remote API
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

//Share library
import org.apache.harmony.test.func.api.javax.management.remote.mbeans.*;

//DRL Harness
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class IllegalClassLoaderTest extends MultiCase {

    MBeanServer mbeanServer;
    public static String PROTOCOL = "rmi";
    
    JMXServiceURL serviceURL;
    
    String prefix = "service:jmx:";
    
    String separator = "://";
    
    JMXConnector connector;
    
    HashMap enviroment;
    
    JMXConnectorServer cs;
    
    public Result testIncorrectClassLoaderKey01() throws Exception {

        MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();

        try {
            
            serviceURL = new JMXServiceURL(prefix + PROTOCOL + separator);
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL, null, mbeanServer);
            cs.start();
            JMXConnector connector = cs.toJMXConnector(null);
            enviroment = new HashMap();
            enviroment.put(JMXConnectorServerFactory.DEFAULT_CLASS_LOADER,"incorrect");
            connector.connect(enviroment);
            return failed("IllegalArgumentException sholud thrown if classLoader object is not an instance of java.lang.ClassLoader");
            
        } catch (IllegalArgumentException e) {
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Thrown wrong exception type");
        } finally {
            MBeanServerFactory.releaseMBeanServer(mbeanServer);
        }

    }

    public Result testIncorrectClassLoaderKey02() throws Exception {

        try {
            MBeanServer mbeanServer = MBeanServerFactory.newMBeanServer();
            serviceURL = new JMXServiceURL(prefix + PROTOCOL + separator);
            enviroment = new HashMap();
            String name = "test:name=TMBClass";
            ObjectName tmb = new ObjectName(name);
            mbeanServer.createMBean("org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBClass",tmb);
            enviroment.put(JMXConnectorServerFactory.DEFAULT_CLASS_LOADER_NAME,
                    tmb);
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL,enviroment, mbeanServer);
            cs.start();
            return failed("IllegalArgumentException sholud thrown if classLoader object is not an instance of java.lang.ClassLoader");
        } catch (IllegalArgumentException e) {
            // e.printStackTrace();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Thrown wrong exception type instead IllegalArgumentException");
        }
    }

    public Result testIncorrectClassLoaderKey03() throws Exception {

        try {
            MBeanServer mbeanServer = MBeanServerFactory.newMBeanServer();
            serviceURL = new JMXServiceURL(prefix + PROTOCOL + separator);
            enviroment = new HashMap();
            enviroment.put(JMXConnectorFactory.DEFAULT_CLASS_LOADER,
                    "incorrect");
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL,
                    enviroment, mbeanServer);
            cs.start();
            return failed("IllegalArgumentException sholud thrown if classLoader object is not an instance of java.lang.ClassLoader");
        } catch (IllegalArgumentException e) {
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Thrown wrong exception type instead IllegalArgumentException");
        }

    }

    public Result testIncorrectClassLoaderKey04() throws Exception {

        try {
            MBeanServer mbeanServer = MBeanServerFactory.newMBeanServer();
            serviceURL = new JMXServiceURL(prefix + PROTOCOL + separator);
            enviroment = new HashMap(2);
            String name = "test:name=TMBClassLoader";
            ObjectName classLoaderName = new ObjectName(name);
            TMBClassLoader classLoader = new TMBClassLoader();
            mbeanServer.createMBean("org.apache.harmony.test.func.api.javax.management.remote.mbeans.TMBClassLoader",classLoaderName);
            enviroment.put(JMXConnectorFactory.DEFAULT_CLASS_LOADER,classLoaderName);
            enviroment.put(JMXConnectorServerFactory.DEFAULT_CLASS_LOADER_NAME, classLoader);
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL,enviroment, mbeanServer);
            cs.start();
            return failed("IllegalArgumentException sholud thrown if classLoader object is not an instance of java.lang.ClassLoader");
        } catch (IllegalArgumentException e) {
           // e.printStackTrace();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Thrown wrong exception type instead IllegalArgumentException");
        }
    }
    public static void main(String[] args) {
        IllegalClassLoaderTest run = new IllegalClassLoaderTest();
        System.exit(run.test(args));
    }
}
