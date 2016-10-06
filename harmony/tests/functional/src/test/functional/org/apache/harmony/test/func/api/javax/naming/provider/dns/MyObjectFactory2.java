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
package org.apache.harmony.test.func.api.javax.naming.provider.dns;

import java.util.Hashtable;

import javax.naming.CannotProceedException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.spi.DirObjectFactory;

/**
 */
public class MyObjectFactory2 implements DirObjectFactory {

    /**
     * Returns new instance of the
     * org.apache.harmony.test.func.api.javax.naming.directory.provider.dns.TestCtxFactory
     * 
     * @param obj
     * @param name
     * @param nameCtx
     * @param environment
     * @param attrs
     * @return
     * @throws Exception
     */
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
        Hashtable environment, Attributes attrs) throws Exception {
        Object o = environment.get("java.naming.spi.CannotProceedException");
        if ((o instanceof CannotProceedException)
            && (nameCtx instanceof DirContext)) {

            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.harmony.test.func.api.javax."
                    + "naming.provider.dns.TestCtxFactory");

            return new InitialDirContext(env);
        }
        return null;
    }

    /**
     * Returns new instance of the
     * org.apache.harmony.test.func.api.javax.naming.directory.provider.dns.TestCtxFactory
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @return
     * @throws Exception
     */
    public Object getObjectInstance(Object arg0, Name arg1, Context arg2,
        Hashtable arg3) throws Exception {
        return getObjectInstance(arg0, arg1, arg2, arg3, null);
    }
}