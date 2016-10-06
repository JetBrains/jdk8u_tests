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

import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

import org.apache.harmony.test.func.api.javax.naming.share.MultiCaseUtil;
import org.apache.harmony.share.Result;

/**
 * This test can test localhost name server. To do this uncomment methods
 * getCtx1(), testGetAttributes2N();
 * 
 */
public class DnsTest1 extends MultiCaseUtil {

    /**
     * DNS Service Provider class name.
     */
    public static String      factory;

    static {
        try {
            String className = "org.apache.harmony.jndi.provider.dns.DNSContextFactory";
            Class.forName(className);
            factory = className;
        } catch (ClassNotFoundException e) {
            factory = "com.sun.jndi.dns.DnsContextFactory";
        }
    }

    /**
     * DNS server host. This host is uses if command line argument PROVIDER_URL
     * is not specified.
     */
    private String            host = "default.example.com";

    /**
     * Initial dir context.
     */
    private InitialDirContext ctx;

    /**
     * Create initial context.
     */
    public void setUp() {
        try {
            String str = getArg("PROVIDER_URL");
            if (str != null && !str.startsWith("$")) {
                host = str.substring(6, str.indexOf("/", 6));
            }
        } catch (Exception e) {
            e.printStackTrace();
            host = "default.example.com";
        }
        echo("Server host: " + host);
    }

    /**
     * Get attributes, associated with the host example.com. getAttributes()
     * method should retrieve a=A: 198.175.96.33
     * 
     * @return
     */
    public Result testGetAttributes11() {
        try {
            getCtx1();
        } catch (NamingException e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return checkAttributes1();
    }

    /**
     * Get attributes, associated with the host example.com. getAttributes()
     * method should retrieve A, NS and SOA attributes.
     * 
     * @return
     */
    public Result testGetAttributes12() {
        try {
            getCtx1();
        } catch (NamingException e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return checkAttributes2();
    }

    /**
     * Get attributes, associated with the host1.example.com. getAttributes()
     * method should retrieve txt=TXT: "test", a=A: 10.0.0.1, hinfo=HINFO: OS
     * Windows XP, OS Linux SuSe 9.2, CPU Pentium 3.0 GHz, mx=MX: 10
     * host1.example.com.
     * 
     * @return
     */
    public Result testGetAttributes13() {
        try {
            getCtx1();
        } catch (NamingException e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return checkAttributes3();
    }

    /**
     * Get attributes, associated with the host example.com. getAttributes()
     * method should retrieve a=A: 198.175.96.33
     * 
     * @return
     */
    // public Result testGetAttributes21() {
    // try {
    // getCtx2();
    // } catch (NamingException e) {
    // e.printStackTrace();
    // return failed(e.toString());
    // }
    // return checkAttributes1();
    // }
    /**
     * Get attributes, associated with the host example.com. getAttributes()
     * method should retrieve A, NS and SOA attributes.
     * 
     * @return
     */
    // public Result testGetAttributes22() {
    // try {
    // getCtx2();
    // } catch (NamingException e) {
    // e.printStackTrace();
    // return failed(e.toString());
    // }
    // return checkAttributes2();
    // }
    /**
     * Get attributes, associated with the host1.example.com. getAttributes()
     * method should retrieve txt=TXT: "test", a=A: 10.0.0.1, hinfo=HINFO: OS
     * Windows XP, OS Linux SuSe 9.2, CPU Pentium 3.0 GHz, mx=MX: 10
     * host1.example.com.
     * 
     * @return
     */
    // public Result testGetAttributes23() {
    // try {
    // getCtx2();
    // } catch (NamingException e) {
    // e.printStackTrace();
    // return failed(e.toString());
    // }
    // return checkAttributes3();
    // }
    /**
     * Create initial context with faulty the configuration:
     * (DirContext.PROVIDER_URL, "dns://127.0.0.1:5353
     * dns://127.0.0.1:53/example.com"). In this case ConfigurationException
     * should be thrown.
     * 
     * @return
     */
    public Result testFaultyConfiguration() {
        try {
            ctx = new InitialDirContext();
            ctx.addToEnvironment(Context.INITIAL_CONTEXT_FACTORY, factory);
            ctx.addToEnvironment(Context.PROVIDER_URL, "dns://" + host
                + ":5353 dns://" + host + "/example.com");
        } catch (Exception e) {
            if (e instanceof ConfigurationException) {
                return passed();
            }
        }
        return failed("ConfigurationException should be thwrown");
    }

    /**
     * Get attributes, associated with the host example.com. getAttributes()
     * method should retrieve a=A: 198.175.96.33
     * 
     * @return
     */
    private Result checkAttributes1() {
        try {
            Attributes attrs = ctx.getAttributes("example.com");
            if (!compareAttrs(attrs, "A", "198.175.96.33")) {
                return failed("FAILED\n    Command: "
                    + "getAttributes(\"example.com\")\n    Retrieved: " + attrs
                    + "\n    Required:  {a=A: 198.175.96.33}\n");
            }
        } catch (NamingException e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Get attributes, associated with the host example.com. getAttributes()
     * method should retrieve A, NS and SOA attributes.
     * 
     * @return
     */
    private Result checkAttributes2() {
        try {
            Attributes attrs = ctx.getAttributes("example.com");
            if (!(compareAttrs(attrs, "A", "127.0.0.1")
                && compareAttrs(attrs, "NS", "example.com.") && compareAttrs(
                attrs, "SOA", "host1.example.com. hostmaster.example.com."
                    + " 200412105 36000 3600 604800 86400"))) {
                return failed("FAILED\n    Command: "
                    + "getAttributes(\"example.com\")\n    Retrieved: " + attrs
                    + "\n    Required:  {a=A: 127.0.0.1, ns=NS: example.com.,"
                    + " soa=SOA: host1.example.com. hostmaster.example.com. "
                    + "200412105 36000 3600 604800 86400} \n");
            }
        } catch (NamingException e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Get attributes, associated with host1.example.com. getAttributes() method
     * should retrieve txt=TXT: "test", a=A: 10.0.0.1, hinfo=HINFO: OS Windows
     * XP, OS Linux SuSe 9.2, CPU Pentium 3.0 GHz, mx=MX: 10 host1.example.com.
     * 
     * @return
     */
    private Result checkAttributes3() {
        try {
            Attributes attrs = ctx.getAttributes("host1.example.com");
            if (!(compareAttrs(attrs, "txt", "test host1")
                && compareAttrs(attrs, "MX", "10 host1.example.com.") && compareAttrs(
                attrs, "HINFO",
                "CPU Pentium 3.0 GHz, OS Linux SuSe 9.2, OS Windows XP"))) {
                return failed("FAILED\n    Command: "
                    + "getAttributes(\"host1.example.com\")\n    Retrieved: "
                    + attrs
                    + "\n    Required:  {txt=TXT: test host1, a=A: 10.0.0.1, "
                    + "hinfo=HINFO: OS Linux SuSe 9.2, CPU Pentium 3.0 GHz, "
                    + "OS Windows XP, mx=MX: 10 host1.example.com.}\n" + "\n");
            }
        } catch (NamingException e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Compares the Attributes object with its string representation.
     * 
     * @param attrs Attributes object
     * @param name comma separated attribute names
     * @param values comma separated attribute values.
     * @return
     */
    private boolean compareAttrs(Attributes attrs, String name, String values) {
        String[] val = getCommaSeparatedTokens(values);
        Attribute attr = attrs.get(name);
        if (attr == null || attr.size() != val.length) {
            return false;
        }
        try {
            for (int i = 0; i < attr.size(); i++) {
                String attrVal = (String)attr.get(i);
                boolean b = true;
                for (int n = 0; n < val.length; n++) {
                    if (val[n].equals(attrVal)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    return false;
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Create new initial context.
     * 
     * @return
     * @throws NamingException
     */
    private InitialDirContext getCtx1() throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(Context.PROVIDER_URL, "dns://" + host + ":5353 dns://" + host);
        ctx = new InitialDirContext(env);
        return ctx;
    }

    /**
     * @return
     * @throws NamingException
     */
    // private InitialDirContext getCtx2() throws NamingException {
    // Hashtable env = new Hashtable();
    // env.put(DirContext.INITIAL_CONTEXT_FACTORY, factory);
    // env.put(DirContext.PROVIDER_URL, "dns://:5353 dns://:53");
    // ctx = new InitialDirContext(env);
    // return ctx;
    // }
    /**
     * Close initial context.
     */
    public void tearDown() {
        try {
            if (ctx != null) {
                ctx.close();
            }
        } catch (NamingException e) {
            // e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.exit(new DnsTest1().test(args));
    }
}