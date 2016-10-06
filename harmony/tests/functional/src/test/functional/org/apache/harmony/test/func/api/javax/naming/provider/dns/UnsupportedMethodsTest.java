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

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.apache.harmony.test.func.api.javax.naming.share.MultiCaseUtil;
import org.apache.harmony.share.Result;

/**
 * <h3 align="center">Unsupported Operations Test Design</h3>
 * Verify that the following methods throws OperationNotSupportedException.
 * <ul type="1">
 * <li>bind(Name name, Object obj)</li>
 * <li>bind(String name, Object obj)</li>
 * <li>bind(Name name, Object obj, Attributes attrs)</li>
 * <li>bind(String name, Object obj, Attributes attrs)</li>
 * <li>createSubcontext(Name name)</li>
 * <li>createSubcontext(String name)</li>
 * <li>createSubcontext(Name name, Attributes attrs)</li>
 * <li>createSubcontext(String name, Attributes attrs)</li>
 * <li>destroySubcontext(Name name)</li>
 * <li>destroySubcontext(String name)</li>
 * <li>getSchema(Name name)</li>
 * <li>getSchema(String name)</li>
 * <li>getSchemaClassDefinition(Name name)</li>
 * <li>getSchemaClassDefinition(String name)</li>
 * <li>modifyAttributes(Name name, int mod_op, Attributes attrs)</li>
 * <li>modifyAttributes(Name name, ModificationItem[] mods)</li>
 * <li>modifyAttributes(String name, int mod_op, Attributes attrs)</li>
 * <li>modifyAttributes(String name, ModificationItem[] mods)</li>
 * <li>rebind(Name name, Object obj)</li>
 * <li>rebind(String name, Object obj)</li>
 * <li>rebind(Name name, Object obj, Attributes attrs)</li>
 * <li>rebind(String name, Object obj, Attributes attrs)</li>
 * <li>search(Name name, Attributes matchingAttributes)</li>
 * <li>search(Name name, Attributes matchingAttributes, String[]
 * attributesToReturn)</li>
 * <li>search(Name name, String filterExpr, Object[] filterArgs, SearchControls
 * cons)</li>
 * <li>search(Name name, String filter, SearchControls cons)</li>
 * <li>search(String name, Attributes matchingAttributes)</li>
 * <li>search(String name, Attributes matchingAttributes, String[]
 * attributesToReturn)</li>
 * <li>search(String name, String filterExpr, Object[] filterArgs,
 * SearchControls cons)</li>
 * <li>search(String name, String filter, SearchControls cons)</li>
 * <li>rename(Name oldName, Name newName)</li>
 * <li>rename(String oldName, String newName)</li>
 * <li>unbind(Name name)</li>
 * <li>unbind(String name)</li>
 * </ul>
 * 
 */
public class UnsupportedMethodsTest extends MultiCaseUtil {

    private final String      factory = DnsTest1.factory;

    /**
     * Initial context.
     */
    private InitialDirContext ctx;

    /**
     * Context name.
     */
    private Name              name    = null;

    /**
     * Context name.
     */
    private String            strName = "example.com";

    /**
     * Creates InitialDirContext object.
     */
    public void setUp() {
        String host = "default.example.com";
        try {
            String str = getArg("PROVIDER_URL");
            if (str != null && !str.startsWith("$")) {
                host = str.substring(6, str.indexOf("/", 6));
            }
        } catch (Exception e) {
            host = "default.example.com";
        }

        try {
            ctx = new InitialDirContext();
            ctx.addToEnvironment(Context.INITIAL_CONTEXT_FACTORY, factory);
            ctx.addToEnvironment(Context.PROVIDER_URL, "dns://" + host);
            name = new CompositeName(strName);
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(fail(e.toString()));
        }
    }

    /*
     * ------------------------------- Name param ----------------------------
     * The following tests invokes corresponding methods whose name parameter
     * type is Name.
     */

    public Result testBind1() {
        return invoke("bind", new Class[] { Object.class });
    }

    public Result testBind2() {
        return invoke("bind", new Class[] { Object.class, Attributes.class });
    }

    public Result testCreateSubcontext1() {
        return invoke("createSubcontext", new Class[] {});
    }

    public Result testCreateSubcontext2() {
        return invoke("createSubcontext", new Class[] { Attributes.class });
    }

    public Result testDestroySubcontext() {
        return invoke("destroySubcontext", new Class[] {});
    }

    public Result testGetSchema() {
        return invoke("getSchema", new Class[] {});
    }

    public Result testGetSchemaClassDefinition() {
        return invoke("getSchemaClassDefinition", new Class[] {});
    }

    public Result testModifyAttributes1() {
        return invoke("modifyAttributes", new Class[] { int.class,
            Attributes.class });
    }

    public Result testModifyAttributes2() {
        return invoke("modifyAttributes",
            new Class[] { ModificationItem[].class });
    }

    public Result testReBind1() {
        return invoke("rebind", new Class[] { Object.class });
    }

    public Result testReBind2() {
        return invoke("rebind", new Class[] { Object.class, Attributes.class });
    }

    public Result testSearch1() {
        return invoke("search", new Class[] { Attributes.class });
    }

    public Result testSearch2() {
        return invoke("search",
            new Class[] { Attributes.class, String[].class });
    }

    public Result testSearch3() {
        return invoke("search", new Class[] { String.class, Object[].class,
            SearchControls.class });
    }

    public Result testRename() {
        return invokeMethod("rename", new Class[] { Name.class, Name.class },
            new Object[] { name, name });
    }

    public Result testUnbind() {
        return invoke("unbind", new Class[] {});
    }

    /*
     * ------------------------------- String param --------------------------
     * The following tests invokes corresponding methods whose name parameter
     * type is String.
     */
    public Result testBindStr1() {
        return invokeStr("bind", new Class[] { Object.class });
    }

    public Result testBindStr2() {
        return invokeStr("bind", new Class[] { Object.class, Attributes.class });
    }

    public Result testCreateSubcontextStr1() {
        return invokeStr("createSubcontext", new Class[] {});
    }

    public Result testCreateSubcontextStr2() {
        return invokeStr("createSubcontext", new Class[] { Attributes.class });
    }

    public Result testDestroySubcontextStr() {
        return invokeStr("destroySubcontext", new Class[] {});
    }

    public Result testGetSchemaStr() {
        return invokeStr("getSchema", new Class[] {});
    }

    public Result testGetSchemaClassDefinitionStr() {
        return invokeStr("getSchemaClassDefinition", new Class[] {});
    }

    public Result testModifyAttributesStr1() {
        return invokeStr("modifyAttributes", new Class[] { int.class,
            Attributes.class });
    }

    public Result testModifyAttributesStr2() {
        return invokeStr("modifyAttributes",
            new Class[] { ModificationItem[].class });
    }

    public Result testReBindStr1() {
        return invokeStr("rebind", new Class[] { Object.class });
    }

    public Result testReBindStr2() {
        return invokeStr("rebind",
            new Class[] { Object.class, Attributes.class });
    }

    public Result testSearchStr1() {
        return invokeStr("search", new Class[] { Attributes.class });
    }

    public Result testSearchStr2() {
        return invokeStr("search", new Class[] { Attributes.class,
            String[].class });
    }

    public Result testSearchStr3() {
        return invokeStr("search", new Class[] { String.class, Object[].class,
            SearchControls.class });
    }

    public Result testRenameStr() {
        return invokeMethod("rename",
            new Class[] { String.class, String.class }, new Object[] { strName,
                strName });
    }

    public Result testUnbindStr() {
        return invokeStr("unbind", new Class[] {});
    }

    /**
     * Invokes the specified method whose name parameter type is String.
     * 
     * @param mName method name.
     * @param types method parameter types.
     * @return
     */
    private Result invokeStr(String mName, Class[] types) {
        Class[] c = new Class[types.length + 1];
        Object[] o = new Object[types.length + 1];
        c[0] = String.class;
        o[0] = strName;
        for (int i = 1; i < c.length; i++) {
            c[i] = types[i - 1];
            if (c[i].equals(int.class)) {
                o[i] = new Integer(0);
            }
        }
        return invokeMethod(mName, c, o);
    }

    /**
     * Invokes the specified method whose name parameter type is Name.
     * 
     * @param mName method name.
     * @param types method parameter types.
     * @return
     */
    private Result invoke(String mName, Class[] types) {
        Class[] c = new Class[types.length + 1];
        Object[] o = new Object[types.length + 1];
        c[0] = Name.class;
        o[0] = name;
        for (int i = 1; i < c.length; i++) {
            c[i] = types[i - 1];
            if (c[i].equals(int.class)) {
                o[i] = new Integer(0);
            }
        }
        return invokeMethod(mName, c, o);
    }

    /**
     * Invokes the specified method with the specified parameters.
     * 
     * @param mName method name.
     * @param types method parameter types.
     * @param o parameter values.
     * @return
     */
    private Result invokeMethod(String mName, Class[] types, Object[] o) {
        try {
            ctx.getClass().getMethod(mName, types).invoke(ctx, o);
        } catch (Exception e) {
            if (e.getCause() instanceof OperationNotSupportedException) {
                return passed();
            }

            e.printStackTrace();
            return (failed(e.toString()));
        }
        return failed("OperationNotSupportedException should be thrown.");
    }

    /**
     * Close initial context.
     */
    public void tearDown() {
        try {
            if (ctx != null) {
                ctx.close();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.exit(new UnsupportedMethodsTest().test(args));
    }
}