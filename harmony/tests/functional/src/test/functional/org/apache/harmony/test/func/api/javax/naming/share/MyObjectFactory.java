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
/*
 *
 */  
package org.apache.harmony.test.func.api.javax.naming.share;

import java.util.Hashtable;

import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.spi.DirObjectFactory;

/**
 */
public class MyObjectFactory implements DirObjectFactory {

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.spi.DirObjectFactory#getObjectInstance(java.lang.Object,
     *      javax.naming.Name, javax.naming.Context, java.util.Hashtable,
     *      javax.naming.directory.Attributes)
     */
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
        Hashtable environment, Attributes attrs) throws Exception {
        Object o = environment.get("java.naming.spi.CannotProceedException");
        if ((o instanceof CannotProceedException)
            && (nameCtx instanceof DirContext)) {
            CannotProceedException e = (CannotProceedException) o;
            DirContext dns = (DirContext) nameCtx; 
            if(name == null || name.equals(new CompositeName("/"))) {
                name = new CompositeName("");
            }
            Attributes as = dns.getAttributes(name);
            Attribute a = as.get("A");
            if (a == null) {
                return null;
            }

            String port = (String) environment.get("netContext.resource.port");
            if (port != null) {
                port = ":" + port;
            }

            NamingEnumeration en2 = a.getAll();
            while (en2.hasMore()) {
                String ip = (String) en2.next();
                try {
                    Hashtable env = new Hashtable(environment);
                    env.put(DirContext.INITIAL_CONTEXT_FACTORY,
                        "org.apache.harmony.test.func.api.javax.naming."
                            + "share.NetContextFactory");
                    env.put(NetContext.RESOURCE_IP, ip + port);
                    return new InitialDirContext(env);
                } catch (Exception ex) {
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.spi.ObjectFactory#getObjectInstance(java.lang.Object,
     *      javax.naming.Name, javax.naming.Context, java.util.Hashtable)
     */
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
        Hashtable environment) throws Exception {
        return getObjectInstance(obj, name, nameCtx, environment, null);
    }
}