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
package org.apache.harmony.test.func.api.javax.naming.provider.rmi.share;

import java.util.Hashtable;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

//import org.apache.harmony.test.func.api.javax.naming.provider.rmi.FederationSupportOFTest;
import org.apache.harmony.test.func.api.javax.naming.share.Hello;

/**
 */
public class SimpleObjectFactory implements ObjectFactory {

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.spi.ObjectFactory#getObjectInstance(java.lang.Object,
     *      javax.naming.Name, javax.naming.Context, java.util.Hashtable)
     */
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
        Hashtable environment) throws Exception {
        final Name bName = new CompositeName("SimpleCtx");
        if (!(obj instanceof Hello)) {
            throw new Exception(
                "Wrong object! The object should be an instance of Hello");
        }
        if (bName.compareTo(name) != 0) {
            throw new Exception("Wrong name parameter!\nExpected: " + bName
                + "\nReceived: " + name);
        }

        return new SimpleCtx(null);
    }
}
