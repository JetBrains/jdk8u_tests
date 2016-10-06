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

import java.lang.reflect.Method;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.Hashtable;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.naming.Name;

import org.apache.harmony.test.func.api.javax.naming.provider.rmi.share.SimpleCtx;
import org.apache.harmony.test.func.api.javax.naming.provider.rmi.share.Utils;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Federation is not supported in Harmony and RI.
 * 
 */
public class FederationSupportTest extends MultiCase {

    /**
     * Initial context.
     */
    private InitialContext ctx;

    /**
     * The name of the binding, bound in the registry.
     */
    private final String   bindingName = "SimpleCtx";

    /**
     * Second part of the composite name.
     */
    private final String   name        = "test";

    /**
     * Composite name.
     */
    private final String   compNameStr = bindingName + "/" + name;

    /**
     * Composite name.
     */
    private Name           compName;

    public FederationSupportTest() throws InvalidNameException {
        compName = new CompositeName(compNameStr);
    }

    /**
     * Test for the method bind(javax.naming.Name, java.lang.Object)
     * 
     * @see javax.naming#bind(javax.naming.Name, java.lang.Object)
     */
    public Result testBindNameObject() {
        return verify("bind", new Class[] { Name.class, Object.class },
            new Object[] { compName, "Obj" }, null);
    }

    /**
     * Test for the method bind(java.lang.String, java.lang.Object)
     * 
     * @see javax.naming#bind(java.lang.String, java.lang.Object)
     */
    public Result testBindStringObject() {
        return verify("bind", new Class[] { String.class, Object.class },
            new Object[] { compNameStr, "Obj" }, null);
    }

    /**
     * Test for the method createSubcontext(javax.naming.Name)
     * 
     * @see javax.naming#createSubcontext(javax.naming.Name)
     */
    public Result testCreateSubcontextName() {
        return verify("createSubcontext", new Class[] { Name.class },
            new Object[] { compName }, null);
    }

    /**
     * Test for the method createSubcontext(java.lang.String)
     * 
     * @see javax.naming#createSubcontext(java.lang.String)
     */
    public Result testCreateSubcontextString() {
        return verify("createSubcontext", new Class[] { String.class },
            new Object[] { compNameStr }, null);
    }

    /**
     * Test for the method destroySubcontext(javax.naming.Name)
     * 
     * @see javax.naming#destroySubcontext(javax.naming.Name)
     */
    public Result testDestroySubcontextName() {
        return verify("destroySubcontext", new Class[] { Name.class },
            new Object[] { compName }, null);
    }

    /**
     * Test for the method destroySubcontext(java.lang.String)
     * 
     * @see javax.naming#destroySubcontext(java.lang.String)
     */
    public Result testDestroySubcontextString() {
        return verify("destroySubcontext", new Class[] { String.class },
            new Object[] { compNameStr }, null);
    }

    /**
     * Test for the method getNameParser(javax.naming.Name)
     * 
     * @see javax.naming#getNameParser(javax.naming.Name)
     */
    public Result testGetNameParserName() {
        return verify("getNameParser", new Class[] { Name.class },
            new Object[] { compName }, null);
    }

    /**
     * Test for the method getNameParser(java.lang.String)
     * 
     * @see javax.naming#getNameParser(java.lang.String)
     */
    public Result testGetNameParserString() {
        return verify("getNameParser", new Class[] { String.class },
            new Object[] { compNameStr }, null);
    }

    /**
     * Test for the method listBindings(javax.naming.Name)
     * 
     * @see javax.naming#listBindings(javax.naming.Name)
     */
    public Result testListBindingsName() {
        return verify("listBindings", new Class[] { Name.class },
            new Object[] { compName }, null);
    }

    /**
     * Test for the method listBindings(java.lang.String)
     * 
     * @see javax.naming#listBindings(java.lang.String)
     */
    public Result testListBindingsString() {
        return verify("listBindings", new Class[] { String.class },
            new Object[] { compNameStr }, null);
    }

    /**
     * Test for the method list(javax.naming.Name)
     * 
     * @see javax.naming#list(javax.naming.Name)
     */
    public Result testListName() {
        return verify("list", new Class[] { Name.class },
            new Object[] { compName }, null);
    }

    /**
     * Test for the method list(java.lang.String)
     * 
     * @see javax.naming#list(java.lang.String)
     */
    public Result testListString() {
        return verify("list", new Class[] { String.class },
            new Object[] { compNameStr }, null);
    }

    /**
     * Test for the method lookupLink(javax.naming.Name)
     * 
     * @see javax.naming#lookupLink(javax.naming.Name)
     */
    public Result testLookupLinkName() {
        return verify("lookupLink", new Class[] { Name.class },
            new Object[] { compName }, "Ok");
    }

    /**
     * Test for the method lookupLink(java.lang.String)
     * 
     * @see javax.naming#lookupLink(java.lang.String)
     */
    public Result testLookupLinkString() {
        return verify("lookupLink", new Class[] { String.class },
            new Object[] { compNameStr }, "Ok");
    }

    /**
     * Test for the method lookup(javax.naming.Name)
     * 
     * @see javax.naming#lookup(javax.naming.Name)
     */
    public Result testLookupName() throws InvalidNameException {
        return verify("lookup", new Class[] { Name.class },
            new Object[] { compName }, "Ok");
    }

    /**
     * Test for the method lookup(java.lang.String)
     * 
     * @see javax.naming#lookup(java.lang.String)
     */
    public Result testLookupString() throws InvalidNameException {
        return verify("lookup", new Class[] { String.class },
            new Object[] { compNameStr }, "Ok");
    }

    /**
     * Test for the method rebind(javax.naming.Name, java.lang.Object)
     * 
     * @see javax.naming#rebind(javax.naming.Name, java.lang.Object)
     */
    public Result testRebindNameObject() {
        return verify("rebind", new Class[] { Name.class, Object.class },
            new Object[] { compName, "Obj" }, null);
    }

    /**
     * Test for the method rebind(java.lang.String, java.lang.Object)
     * 
     * @see javax.naming#rebind(java.lang.String, java.lang.Object)
     */
    public Result testRebindStringObject() {
        return verify("rebind", new Class[] { String.class, Object.class },
            new Object[] { compNameStr, "Obj" }, null);
    }

    /**
     * Test for the method rename(javax.naming.Name, javax.naming.Name)
     * 
     * @see javax.naming#rename(javax.naming.Name, javax.naming.Name)
     */
    public Result testRenameNameName() {
        return verify("rename", new Class[] { Name.class, Name.class },
            new Object[] { compName, compName }, null);
    }

    /**
     * Test for the method rename(java.lang.String, java.lang.String)
     * 
     * @see javax.naming#rename(java.lang.String, java.lang.String)
     */
    public Result testRenameStringString() {
        return verify("rename", new Class[] { String.class, String.class },
            new Object[] { compNameStr, compNameStr }, null);
    }

    /**
     * Test for the method unbind(javax.naming.Name)
     * 
     * @see javax.naming#unbind(javax.naming.Name)
     */
    public Result testUnbindName() {
        return verify("unbind", new Class[] { Name.class },
            new Object[] { compName }, null);
    }

    /**
     * Test for the method unbind(java.lang.String)
     * 
     * @see javax.naming#unbind(java.lang.String)
     */
    public Result testUnbindString() {
        return verify("unbind", new Class[] { String.class },
            new Object[] { compNameStr }, null);
    }

    /**
     * Create registry, construct initial context object, bind SimpleCtx object
     * to the registry.
     * 
     * @param args command line arguments.
     * @throws Exception
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
        ctx.bind(bindingName, new SimpleCtx(null));
        log.info("Ctx has been successfully bound: "
            + LocateRegistry.getRegistry(port).lookup(bindingName));
    }

    /**
     * Invoke the method, verify that the method has been invoked, compare
     * parameters passed to the method and returned value with expected.
     * 
     * @param mName method name.
     * @param c parameter types.
     * @param o parameter values.
     * @param retVal expected value, that should be returned by the method.
     * @return
     */
    private Result verify(String mName, Class[] c, Object[] o, Object retVal) {
        try {
            SimpleCtx.lastMethodInvoked = null;
            SimpleCtx.lastMethodParams = null;
            Method m = ctx.getClass().getMethod(mName, c);
            Object obj = m.invoke(ctx, o);
            Object[] exp = getExpectedParams(o);
            if (SimpleCtx.lastMethodInvoked == null) {
                return failed("The method " + m.getName() + " not invoked!");
            }
            if (!m.getName().equals(SimpleCtx.lastMethodInvoked.getName())) {
                return failed("Wrong method invoked!\nExpected: " + m.getName()
                    + "\nRetrieved: " + SimpleCtx.lastMethodInvoked);
            }
            if (!Arrays.equals(exp, SimpleCtx.lastMethodParams)) {
                return failed("Wrong paramerets passed to the method!\nExpected: "
                    + Utils.arrayToString(exp)
                    + "\nRetrieved: "
                    + Utils.arrayToString(SimpleCtx.lastMethodParams));
            }
        } catch (Throwable e) {
            if (e.getCause() != null) {
                e = e.getCause();
            }
            e.printStackTrace();
            return new Result(Result.ERROR, "FAILED\n" + e.toString());
        }
        return passed();
    }

    /**
     * Replaces composite names with simple name names.
     * 
     * @param o
     * @return
     */
    private Object[] getExpectedParams(Object[] o) {
        Object[] no = new Object[o.length];
        for (int i = 0; i < o.length; i++) {
            Object obj = o[i];
            if (compName.equals(obj)) {
                try {
                    obj = new CompositeName(name);
                } catch (InvalidNameException e) {
                    e.printStackTrace();
                }
            } else if (compNameStr.equals(obj)) {
                obj = name;
            }

            no[i] = obj;
        }

        return no;
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
            FederationSupportTest t = new FederationSupportTest();
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
