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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.harmony.test.func.api.javax.naming.provider.rmi.share.Utils;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class EvironmentOwnershipTest extends MultiCase {

    /**
     * RMI registry port.
     */
    private int       regPort = 1099;

    /**
     * Default environment.
     */
    private Hashtable defaultEnv;

    /**
     * Create RegistryContext with some environment. Get environment from the
     * context, make sure it's identical to original one, but is another object.
     * 
     * @return
     */
    public Result testDefaultEnv() {
        try {
            Hashtable env = createCtx().getEnvironment();
            if (!defaultEnv.equals(env)) {
                return failed("The objects are not equals.");
            }

            if (defaultEnv == env) {
                return failed("The objects are the same.");
            }
        } catch (NamingException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
        return passed();
    }

    /**
     * Create context, using some environment object, add property to the
     * context environment, make sure the environment object not changed. Remove
     * property, make sure the method removeFromEnvironment() returns valid
     * object, verify that the property has been removed from the context
     * environment.
     * 
     * @return
     */
    public Result testAddRemove() {
        try {
            Hashtable clone = (Hashtable) defaultEnv.clone();
            Context ctx = createCtx();
            ctx.addToEnvironment("name", "value");
            assertEquals("Default environment has changed!", clone, defaultEnv);
            assertEquals("Wrong property value returned by "
                + "removeFromEnvironment() method", "value", ctx
                .removeFromEnvironment("name"));
            assertEquals("The environment is not equal to initial!", clone, ctx
                .getEnvironment());
        } catch (NamingException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
        return result();
    }

    /**
     * Create RMI registry on any available port.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    private void init(String[] args) throws Exception {
        try {
            regPort = Integer.parseInt(Utils.getArg(args, "port"));
        } catch (Throwable ex) {
        }

        System.setProperty("java.security.policy", "");

        regPort = Utils.runRegistry(regPort);
        log.info("The registry has been startded on port " + regPort);

        final String factory = SecurityManagerTest.provider;
        defaultEnv = new Hashtable();
        defaultEnv.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        defaultEnv.put(Context.PROVIDER_URL, "rmi://:" + regPort);
    }

    /**
     * Create RMI context.
     * 
     * @return initial context.
     * @throws NamingException
     */
    private Context createCtx() throws NamingException {
        return (Context) (new InitialContext(defaultEnv)).lookup("");
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
        try {
            long time = System.currentTimeMillis();
            EvironmentOwnershipTest t = new EvironmentOwnershipTest();
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
