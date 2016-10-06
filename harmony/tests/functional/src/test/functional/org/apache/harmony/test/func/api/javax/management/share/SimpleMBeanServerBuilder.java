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
package org.apache.harmony.test.func.api.javax.management.share;

import javax.management.MBeanServer;
import javax.management.MBeanServerBuilder;
import javax.management.MBeanServerDelegate;

/**
 */
public class SimpleMBeanServerBuilder extends MBeanServerBuilder {

    /**
     * Indicates whether all of the methods have to return null.
     */
    public static boolean returnNull;

    public MBeanServer newMBeanServer(String defaultDomain, MBeanServer outer,
        MBeanServerDelegate delegate) {

        return returnNull ? null : new SimpleMBeanServerImpl(defaultDomain,
            outer, delegate);
    }
}
