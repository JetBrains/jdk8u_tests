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
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

//JMX Remote API
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;

//DRL Harness
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class RemoteRegistrationTest extends MultiCase {

    public Result testConnectorRegistration01() throws Exception {

        MBeanServer first = MBeanServerFactory.createMBeanServer();
        MBeanServer second = MBeanServerFactory.createMBeanServer();
        ConServPreReg cs = new ConServPreReg(second);

        try {   
            ObjectName name = new ObjectName("test:name=prereg");
            cs.preRegister(first, name);
            if (!second.equals(cs.getMBeanServer())) {
                return failed("preRegister method unworkable "
                        + cs.getMBeanServer() + " instead of " + second);
            } else {
                passed("passed");
            }
        } catch (Exception e) {
            return failed("Unexpected exception");
        }
        return result();
    }

    public Result testConnectorRegistration02() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();
        ConServPreReg cs = new ConServPreReg();

        try {
            ObjectName name = new ObjectName("test:name=prereg");
            cs.preRegister(server, name);
            if (!server.equals(cs.getMBeanServer())) {
                return failed("JMXConnectorServer is not in the MBeanServer -"
                        + cs.getMBeanServer());
            } else {
                return passed();
            }
        } catch (Exception e) {
            return failed("Unexpected exception");
        }
    }

    public static void main(String[] args) {
        RemoteRegistrationTest run = new RemoteRegistrationTest();
        System.exit(run.test(args));
    }
}
