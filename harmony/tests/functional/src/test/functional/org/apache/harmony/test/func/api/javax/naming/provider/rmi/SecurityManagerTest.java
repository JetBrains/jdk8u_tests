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

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Hashtable;
import java.util.PropertyPermission;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.harmony.test.func.api.javax.naming.provider.rmi.share.Utils;
import org.apache.harmony.test.func.api.javax.naming.share.Hello;
import org.apache.harmony.test.func.api.javax.naming.share.HelloImpl;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class SecurityManagerTest extends MultiCase {
    public static String        provider;
    private static final String javaPolicy = System
                                               .getProperty("java.security.policy");

    static {
        try {
            final String className = "org.apache.harmony.jndi.provider.rmi.registry.RegistryContextFactory";
            Class.forName(className);
            provider = className;
        } catch (final ClassNotFoundException e) {
            provider = "com.sun.jndi.rmi.registry.RegistryContextFactory";
        }
    }
    /**
     * RMI registry port.
     */
    private int                 regPort    = 1099;

    /**
     * RMI registry.
     */
    private Registry            reg;

    /**
     * Remote object instance.
     */
    private final Hello         remObj     = new HelloImpl();

    /**
     * The name of the remote object.
     */
    private final String        objName    = "Remote Object";

    /**
     * Try to bind remote object without installing security manager. The object
     * should be successfully bound.
     * 
     * @return
     */
    public Result testBindWithoutSM() {
        try {
            final InitialContext ctx = createCtx(false);
            ctx.bind(objName, remObj);
        } catch (final Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        } finally {
            try {
                reg.unbind(objName);
            } catch (final Exception e) {
            }
        }
        return passed();
    }

    /**
     * Set java.naming.rmi.security.manager environment property and try to bind
     * remote object. It's expected that SecurityException should be thrown.
     * 
     * @return
     */
    public Result testBindWithSM() {
        try {
            final InitialContext ctx = createCtx(true);
            ctx.bind(objName, remObj);
            return failed("SecurityException was not thrown!");
        } catch (final SecurityException e) {
            return passed();
        } catch (final Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        } finally {
            try {
                reg.unbind(objName);
            } catch (final Exception e) {
            }
        }
    }

    /**
     * Create InitialContext with environment containing the property. Add the
     * same property to environment, make sure no fail occurs.
     * 
     * @return
     */
    public Result testAddSM() {
        try {
            final InitialContext ctx = createCtx(true);
            ctx.addToEnvironment("java.naming.rmi.security.manager",
                "some value");
        } catch (final Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }

        return passed();
    }

    /**
     * Create initial context without java.naming.rmi.security.manager
     * environment property. Set java.naming.rmi.security.manager environment
     * property and try to bind remote object. It's expected that
     * SecurityException should be thrown. Set the same property again, make
     * sure no fail occurs.
     * 
     * @return
     */
    public Result testAddSMTwice() {
        try {
            final InitialContext ctx = createCtx(false);
            ctx.addToEnvironment("java.naming.rmi.security.manager",
                "some value");
            try {
                ctx.bind(objName, remObj);
                return failed("SecurityException was not thrown!");
            } catch (final SecurityException e) {
            }

            ctx.addToEnvironment("java.naming.rmi.security.manager",
                "some value");
        } catch (final Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        } finally {
            try {
                reg.unbind(objName);
            } catch (final Exception e) {
            }
        }

        return passed();
    }

    /**
     * Create RMI registry on any available port.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    private void init(final String[] args) throws Exception {
        try {
            regPort = Integer.parseInt(Utils.getArg(args, "port"));
        } catch (final Throwable ex) {
        }

        System.setProperty("java.security.policy", "");

        regPort = Utils.runRegistry(regPort);
        reg = LocateRegistry.getRegistry(regPort);
        log.info("The registry has been startded on port " + regPort);
    }

    /**
     * Create initial context.
     * 
     * @param install indicates whether java.naming.rmi.security.manager
     *        environment property should be set.
     * @return initial context.
     * @throws NamingException
     */
    private InitialContext createCtx(final boolean install)
        throws NamingException {
        final String factory = SecurityManagerTest.provider;
        // final String factory =
        // "com.sun.jndi.rmi.registry.RegistryContextFactory";

        final Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(Context.PROVIDER_URL, "rmi://:" + regPort);
        if (install) {
            env.put("java.naming.rmi.security.manager", "some value");
        }
        return new InitialContext(env);
    }

    protected void setUp() throws Exception {

        System.setProperty("java.security.policy", javaPolicy);
        System.setSecurityManager(null);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) {
        try {
            new SecurityManager().checkPermission(new PropertyPermission(
                "java.security.policy", "write"));
            final long time = System.currentTimeMillis();
            final SecurityManagerTest t = new SecurityManagerTest();
            t.init(args);
            final int res = t.test(args);
            System.out.println("Time: " + (System.currentTimeMillis() - time));
            System.exit(res);
        } catch (final Throwable ex) {
            ex.printStackTrace();
            System.exit(Result.ERROR);
        }
    }

}
