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
package org.apache.harmony.test.func.api.javax.naming.provider.rmi;

import java.util.Hashtable;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

import org.apache.harmony.test.func.api.javax.naming.provider.rmi.share.Utils;
import org.apache.harmony.test.func.api.javax.naming.share.MultiCaseUtil;
import org.apache.harmony.share.Result;

/**
 * A test for the class
 * org.apache.harmony.jndi.provider.rmi.registry.RegistryContext. This test
 * verifies that all methods that are not supported by RMI service provider
 * throws {@link javax.naming.OperationNotSupportedException}.
 * 
 */
public class UnsupportedMethodsTest extends MultiCaseUtil {

    /**
     * Initial context.
     */
    private InitialContext ctx;

    /**
     * Create RMI registry, create initial context.
     * 
     * @param port
     * @throws NamingException
     */
    private void init(String[] args) throws Exception {
        int port = 1099;
        try {
            port = Integer.parseInt(Utils.getArg(args, "port"));
        } catch (Throwable ex) {
        }

        port = Utils.runRegistry(port);
        log.info("The registry has been startded on port " + port);

        final String factory = SecurityManagerTest.provider;

        String f = Utils.getArg(args, "factory");
        f = f == null ? factory : f;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, f);
        env.put(Context.PROVIDER_URL, "rmi://:" + port);
        ctx = new InitialContext(env);
        log.info("Initial context has been created. "
            + "Initial context factory: " + f);
    }

    /**
     * A test for the method createSubcontext(javax.naming.Name)
     * 
     * @see org.apache.harmony.jndi.provider.rmi.registry#createSubcontext(javax.naming.Name)
     */
    public final Result testCreateSubcontextName() {
        try {
            ctx.createSubcontext(new CompositeName("Test ctx"));
            return failed("OperationNotSupportedException was not thrown!");
        } catch (NamingException e) {
            if (e instanceof OperationNotSupportedException) {
                return passed();
            } else {
                return failed(e.toString());
            }
        }
    }

    /**
     * A test for the method createSubcontext(java.lang.String)
     * 
     * @see org.apache.harmony.jndi.provider.rmi.registry#createSubcontext(java.lang.String)
     */
    public final Result testCreateSubcontextString() {
        try {
            ctx.createSubcontext("Test ctx");
            return failed("OperationNotSupportedException was not thrown!");
        } catch (NamingException e) {
            if (e instanceof OperationNotSupportedException) {
                return passed();
            } else {
                return failed(e.toString());
            }
        }
    }

    /**
     * A test for the method destroySubcontext(javax.naming.Name)
     * 
     * @see org.apache.harmony.jndi.provider.rmi.registry#destroySubcontext(javax.naming.Name)
     */
    public final Result testDestroySubcontextName() {
        try {
            ctx.destroySubcontext(new CompositeName("Test ctx"));
            return failed("OperationNotSupportedException was not thrown!");
        } catch (NamingException e) {
            if (e instanceof OperationNotSupportedException) {
                return passed();
            } else {
                return failed(e.toString());
            }
        }
    }

    /**
     * A test for the method destroySubcontext(java.lang.String)
     * 
     * @see org.apache.harmony.jndi.provider.rmi.registry#destroySubcontext(java.lang.String)
     */
    public final Result testDestroySubcontextString() {
        try {
            ctx.destroySubcontext("Test ctx");
            return failed("OperationNotSupportedException was not thrown!");
        } catch (NamingException e) {
            if (e instanceof OperationNotSupportedException) {
                return passed();
            } else {
                return failed(e.toString());
            }
        }
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) {
        try {
            long time = System.currentTimeMillis();
            UnsupportedMethodsTest t = new UnsupportedMethodsTest();
            t.init(args);
            int res = t.test(args);
            System.out.println("Time: " + (System.currentTimeMillis() - time));
            System.exit(res);
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(Result.ERROR);
        }
    }
}
