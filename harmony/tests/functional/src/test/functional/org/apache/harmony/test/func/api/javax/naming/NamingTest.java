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
package org.apache.harmony.test.func.api.javax.naming;

import java.net.InetAddress;
import java.util.Hashtable;

import javax.naming.Context;

import org.apache.harmony.test.func.api.javax.naming.directory.JNDITest;
import org.apache.harmony.test.func.api.javax.naming.provider.rmi.share.Utils;
import org.apache.harmony.test.func.api.javax.naming.share.Hello;
import org.apache.harmony.test.func.api.javax.naming.share.HelloImpl;
import org.apache.harmony.share.Result;

/**
 */
public class NamingTest extends JNDITest {

    /**
     * Registry port.
     */
    private int    port = 1099;

    /**
     * Provider URL.
     */
    private String url  = "rmi://localhost";

    /**
     * Create registry on the 1199 port.
     * 
     * @throws Exception
     */
    public void init() throws Exception {
        port = Utils.runRegistry(port);
        String hn = InetAddress.getLocalHost().getHostName();
        url = "rmi://" + hn + ":" + port;
        echo("Provider URL: " + url);
    }

    /**
     * Returns environment properties.
     */
    protected Hashtable getEnv() {
        Hashtable env = super.getEnv();
        env.remove(Context.PROVIDER_URL);
        env.put(Context.PROVIDER_URL, url);
        return env;
    }

    /**
     * Bind new object.
     * 
     * @param name
     * @param object
     * @return
     */
    public int testBind(String name, HelloImpl object) {
        try { 
            Context ctx = getInitCtx();
            ctx.bind(name, object);
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Find the named in the registry.
     * 
     * @param name
     * @param object
     * @return
     */
    public int testLookup(String name, HelloImpl object) {
        String str;
        try {
            str = ((Hello) getInitCtx().lookup(name)).sayHello();
            if (!(str).equals(object.sayHello())) {
                return fail("\n    Retrieved: " + str + "\n    Required: "
                    + object.sayHello());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Find the named in the registry.
     * 
     * @param name
     * @param object
     * @return
     */
    public int testLookupLink(String name, HelloImpl object) {
        String str;
        try {
            str = ((Hello) getInitCtx().lookup(name)).sayHello();
            if (!(str).equals(object.sayHello())) {
                return fail("\n    Retrieved: " + str + "\n    Required: "
                    + object.sayHello());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Rebind the specified object.
     * 
     * @param name
     * @param object
     * @return
     */
    public int testRebind(String name, HelloImpl object) {
        try {
            getInitCtx().rebind(name, object);
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Rename the specified object.
     * 
     * @param name1
     * @param name2
     * @return
     */
    public int testRename(String name1, String name2) {
        try {
            getInitCtx().rename(name1, name2);
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Unbind the specified object.
     * 
     * @param name
     * @return
     */
    public int testUnbind(String name) {
        try {
            getInitCtx().unbind(name);
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Close initial context.
     */
    public void destroy() {
        try {
            getInitCtx().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws Exception
     */
    public static void main(String[] args) {
        NamingTest t = new NamingTest();
        t.args = args;
        try {
            t.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(Result.ERROR);
        }
        int ex = t.test(args);
        t.destroy();
        System.exit(ex);
    }
}