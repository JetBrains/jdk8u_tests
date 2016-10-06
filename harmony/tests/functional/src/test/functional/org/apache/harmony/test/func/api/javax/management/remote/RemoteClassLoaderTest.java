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

import java.net.URL;
import java.util.HashMap;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.harmony.test.func.api.javax.management.remote.mbeans.*;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class RemoteClassLoaderTest extends MultiCase {

    MBeanServer mbeanServer;

    public static String PROTOCOL = "rmi";

    JMXServiceURL serviceURL;

    final String prefix = "service:jmx:";

    final String separator = "://";

    JMXConnector connector;

    HashMap enviroment;

    JMXConnectorServer cs;

    TMBClassLoaderURL loader;

    public Result testBaseClassLoader() throws Exception {
        
        //Create MBean server 
        mbeanServer = MBeanServerFactory.createMBeanServer();

        try {

            String simpleClass = "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple";
            String simpleName = "test:name=TMBClass";
            
            final ObjectName simple = new ObjectName(simpleName);
            mbeanServer.createMBean(simpleClass, simple);
            
            serviceURL = new JMXServiceURL(prefix + PROTOCOL + separator);
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL,
                    null, mbeanServer);
            
            cs.start();
            JMXConnector client = null;
            
            client = JMXConnectorFactory.newJMXConnector(cs.getAddress(), null);
            client.connect(null);
            
            MBeanServerConnection mbsc = client.getMBeanServerConnection();
            mbsc.invoke(simple, "echo", null, null);

            TMBParameterExtends paramExt = new TMBParameterExtends();
            TMBParameter param = new TMBParameter();

            Attribute attr = new Attribute("TMBParameter", param);
            Attribute attrExt = new Attribute("TMBParameter", paramExt);

            mbsc.setAttribute(simple, attrExt);
            mbsc.setAttribute(simple, attr);

            mbsc.invoke(simple, "returnSomeClass", null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result();
    }

    public Result testDefultClassLoader() throws Exception {

        mbeanServer = MBeanServerFactory.createMBeanServer();

        try {

            TMBParameterExtends paramExt = new TMBParameterExtends();
            TMBParameter param = new TMBParameter();
            serviceURL = new JMXServiceURL(prefix + PROTOCOL + separator);
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL,
                    null, mbeanServer);
            cs.start();
            URL location = param.getClass().getProtectionDomain()
                    .getCodeSource().getLocation();
            loader = new TMBClassLoaderURL(location, Thread.currentThread()
                    .getContextClassLoader());
            HashMap env = new HashMap(1);
            env.put(JMXConnectorFactory.DEFAULT_CLASS_LOADER, loader);
            JMXConnector client = null;
            client = JMXConnectorFactory.newJMXConnector(cs.getAddress(), null);
            client.connect(env);

            MBeanServerConnection mbsc = client.getMBeanServerConnection();

            String simpleClass = "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple";
            String simpleName = "test:name=TMBClass";
            ObjectName simple = new ObjectName(simpleName);
            mbsc.createMBean(simpleClass, simple);

            Attribute attr = new Attribute("TMBParameter", param);
            Attribute attrExt = new Attribute("TMBParameter", paramExt);
            mbsc.invoke(simple, "echo", null, null);
            mbsc.setAttribute(simple, attr);
            mbsc.setAttribute(simple, attrExt);

        } catch (Exception e) {
            log.info("Unexpected exception");
            e.printStackTrace();
        }
        return result();
    }

    public Result testServerDefultClassLoaderName() throws Exception {

        // Create MBean server
        mbeanServer = MBeanServerFactory.createMBeanServer();

        // Parameters for tested MBeans
        TMBParameterExtends paramExt = new TMBParameterExtends();
        TMBParameter param = new TMBParameter();

        // Tested MBeans location
        URL location = param.getClass().getProtectionDomain().getCodeSource()
                .getLocation();
        try {

            serviceURL = new JMXServiceURL(prefix + PROTOCOL + separator);
            HashMap env = new HashMap();

            // Set classloader for protocol provider
            env.put(JMXConnectorServerFactory.PROTOCOL_PROVIDER_CLASS_LOADER,
                    Thread.currentThread().getContextClassLoader());

            TMBClassLoaderURL loader = new TMBClassLoaderURL(location, Thread
                    .currentThread().getContextClassLoader());

            // Create Object Name for class loader
            ObjectName loaderName = new ObjectName(
                    "test:name=TMBClassLoaderURL");
            mbeanServer.registerMBean(loader, loaderName);
            env.put(JMXConnectorServerFactory.DEFAULT_CLASS_LOADER_NAME,
                    loaderName);

            // Create and start connector server
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL,
                    env, mbeanServer);
            cs.start();

            JMXConnector client = null;
            client = JMXConnectorFactory.newJMXConnector(cs.getAddress(), null);
            client.connect();

            MBeanServerConnection mbsc = client.getMBeanServerConnection();

            // Create simple MBean
            String simpleClass = "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple";
            String simpleName = "test:name=TMBClass";
            ObjectName simple = new ObjectName(simpleName);
            mbsc.createMBean(simpleClass, simple);

            // Make operations with MBean
            Attribute attr = new Attribute("TMBParameter", param);
            Attribute attrExt = new Attribute("TMBParameter", paramExt);
            mbsc.invoke(simple, "echo", null, null);
            mbsc.setAttribute(simple, attr);
            mbsc.setAttribute(simple, attrExt);

        } catch (Exception e) {
            log.info("Unexpected exception");
            e.printStackTrace();

        }
        return result();
    }

    public Result testServerDefultClassLoader() throws Exception {

        // Create MBean server
        mbeanServer = MBeanServerFactory.createMBeanServer();

        // Parameters for tested MBeans
        TMBParameterExtends paramExt = new TMBParameterExtends();
        TMBParameter param = new TMBParameter();

        // Tested MBeans location
        URL location = param.getClass().getProtectionDomain().getCodeSource()
                .getLocation();
        try {

            serviceURL = new JMXServiceURL(prefix + PROTOCOL + separator);
            HashMap env = new HashMap();

            // Set classloader for protocol provider
            env.put(JMXConnectorServerFactory.PROTOCOL_PROVIDER_CLASS_LOADER,
                    Thread.currentThread().getContextClassLoader());

            TMBClassLoaderURL loader = new TMBClassLoaderURL(location, Thread
                    .currentThread().getContextClassLoader());

            // Create Object Name for class loader
            
            env.put(JMXConnectorServerFactory.DEFAULT_CLASS_LOADER,
                    loader);

            // Create and start connector server
            cs = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL,
                    env, mbeanServer);
            cs.start();

            JMXConnector client = null;
            client = JMXConnectorFactory.newJMXConnector(cs.getAddress(), null);
            client.connect();

            MBeanServerConnection mbsc = client.getMBeanServerConnection();

            // Create simple MBean
            String simpleClass = "org.apache.harmony.test.func.api.javax.management.remote.mbeans.Simple";
            String simpleName = "test:name=TMBClass";
            ObjectName simple = new ObjectName(simpleName);
            mbsc.createMBean(simpleClass, simple);

            // Make operations with MBean
            Attribute attr = new Attribute("TMBParameter", param);
            Attribute attrExt = new Attribute("TMBParameter", paramExt);
            mbsc.invoke(simple, "echo", null, null);
            mbsc.setAttribute(simple, attr);
            mbsc.setAttribute(simple, attrExt);

        } catch (Exception e) {
            log.info("Unexpected exception");
            
            e.printStackTrace();
        }
        return result();
    }
    
    public static void main(String[] args) {
        RemoteClassLoaderTest run = new RemoteClassLoaderTest();
        System.exit(run.test(args));
    }
}
