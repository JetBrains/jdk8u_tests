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
package org.apache.harmony.test.func.api.javax.naming.directory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.harmony.test.func.api.javax.naming.share.JNDITestUtils;
import org.apache.harmony.share.Result;

/**
 * This test parses the xml file, specified as command line argument and invokes
 * all methods specified in the xml with the specified parameters.
 * 
 */
public class JNDITest extends JNDITestUtils {

    /**
     * Sets environment properties and creates InitialContext object.
     * 
     * @return fail if NamingExceptopn occurs.
     */
    public int testCreateInitCtx() {
        env = null;
        initCtx = null;

        echo("DYNAMIC_KEY=" + DYNAMIC_KEY);
        try {
            getInitCtx();
            echo(getEnv());
        } catch (NamingException e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Creates context described in the xml file.
     * 
     * @param ids comma separated contexts Id.
     * @return fail if Naming exception is thrown or created context and
     *         described in the xml context aren't equals.
     */
    public int testCreateCtx(String ids) {
        int r = Result.PASS;
        StringTokenizer st = new StringTokenizer(ids, ",");
        while (st.hasMoreTokens()) {
            String id = st.nextToken().trim();
            Hashtable ht = getCtxById(id);

            if (ht == null) {
                fail("Context '" + id + "' not found");
                r = Result.FAIL;
                continue;
            }

            String name = (String) ht.get("name");
            String parent = (String) ht.get("parent");
            Attributes attrs = (Attributes) ht.get("Attributes");

            try {
                Context ctx = (Context) getInitCtx().lookup(parent);
                Context newCtx = null;

                if (attrs == null) {
                    newCtx = ctx.createSubcontext(name);
                } else {
                    newCtx = ((DirContext) ctx).createSubcontext(name, attrs);
                }

                if (compareObjects((String) ht.get("Id"), newCtx)) {
                    pass();
                } else {
                    return fail("Created context:" + printCtx(newCtx)
                        + "    not equals to:\n        " + ht);
                }
            } catch (NamingException e) {
                e.printStackTrace();
                fail(e);
                r = Result.FAIL;
            }
        }

        return r;
    }

    /**
     * Binds objects described in the xml file.
     * 
     * @param ids comma separated binds Id.
     * @return fail if Naming exception is thrown.
     */
    public int testBindObjects(String ids) {
        int r = Result.PASS;
        StringTokenizer st = new StringTokenizer(ids, ",");
        while (st.hasMoreTokens()) {
            String id = st.nextToken().trim();
            Hashtable ht = getBindById(id);

            if (ht == null) {
                fail("Bind '" + id + "' not found");
                r = Result.FAIL;
                continue;
            }

            String name = (String) ht.get("name");
            String parent = (String) ht.get("ctx");
            Attributes attrs = (Attributes) ht.get("Attributes");
            Object object = ht.get("Object");

            try {
                Context ctx = (Context) getInitCtx().lookup(parent);
                if (attrs == null) {
                    ctx.bind(name, object);
                } else {
                    ((DirContext) ctx).bind(name, object, attrs);
                }
                pass();
            } catch (NamingException e) {
                fail(e);
                r = Result.FAIL;
            }
        }
        return r;
    }

    /**
     * Retrieves all visible bindings in the context <b>name </b> and compares
     * retrieved binding names with names from <b>ids <b>.
     * 
     * @param name Context name.
     * @param ids Comma separated bindings Id, whose names should be retrieved
     *            by list() method.
     * @return fail if retrieved list of names is not equal to required one.
     */
    public int testList(String name, String ids) {
        try {
            String[] str = getCommaSeparatedTokens(ids);
            String[] names = new String[str.length];
            for (int i = 0; i < str.length; i++) {
                Hashtable ht = getObjectById(str[i]);
                if (ht != null) {
                    names[i] = (String) ht.get("name");
                } else {
                    names[i] = str[i];
                }
            }
            NamingEnumeration en = getInitCtx().list(name);
            while (en.hasMore()) {
                String n = ((NameClassPair) en.next()).getName();
                if (!containsValue(names, n)) {
                    return fail("Name '" + n + "' doesn't exist in '{"
                        + printArray(names) + "}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
            
        }
        return pass();
    }

    /**
     * Retrieves all visible bindings in the context <b>name </b> and compares
     * retrieved binding names with names from <b>ids <b>.
     * 
     * @param name Context name.
     * @param ids Comma separated bindings Id, whose names should be retrieved
     *            by list() method.
     * @return test result
     */
    public int testListBindings(String name, String ids) {
        try {
            String[] str = getCommaSeparatedTokens(ids);
            String[] names = new String[str.length];
            for (int i = 0; i < str.length; i++) {
                Hashtable ht = getObjectById(str[i]);
                if (ht != null) {
                    names[i] = (String) ht.get("name");
                } else {
                    names[i] = str[i];
                }
            }
            NamingEnumeration en = getInitCtx().listBindings(name);
            while (en.hasMore()) {
                String n = ((NameClassPair) en.next()).getName();
                if (!containsValue(names, n)) {
                    return fail("Name '" + n + "' doesn't exist in '{"
                        + printArray(names) + "}");
                }
            }
        } catch (Exception e) {
            return fail(e);
        }
        return pass();
    }

    /**
     * Retrieves the named object and compares it with the object, described in
     * the xml file.
     * 
     * @param name The name of the object to look up.
     * @param xmlResult Object Id
     * @return fail if Naming exception is thrown or objects aren't equals.
     */
    public int testLookup(String name, String xmlResult) {
        try {
            Object o = getInitCtx().lookup(name);
            if (!compareObjects(xmlResult, o)) {
                return fail("Command: lookup(\"" + name + "\")\n    Retrieved:"
                    + printCtx((Context) o) + "\n    Required: "
                    + getObjectById(xmlResult).get("Attributes"));
            }
        } catch (NamingException e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Retrieves selected attributes associated with a named object and compares
     * them with attributes, described in the xml file.
     * 
     * @param attrIds comma separated identifiers of the attributes to retrieve.
     *            null indicates that all attributes should be retrieved; an
     *            empty String indicates that none should be retrieved.
     * @param bId binding id, whose attributes should be retrieved.
     * @return fail if Naming exception is thrown or attributes aren't equals.
     */
    public int testGetAttributes(String attrIds, String bId) {
        Hashtable obj = getObjectById(bId);
        Attributes attrs1 = (Attributes) obj.get("Attributes");
        if (attrs1 == null) {
            return fail("Couldn't find attributes associated with object Id = '"
                + bId + "'");
        }

        try {
            String[] aIds = getCommaSeparatedTokens(attrIds);
            if (aIds.length != 0 && "NULL".equalsIgnoreCase(aIds[0])) {
                aIds = null;
            }
            String name = (String) obj.get("name");
            String parent = (String) obj.get("parent");
            DirContext ctx = (DirContext) getInitCtx().lookup(parent);
            Attributes attrs2 = ctx.getAttributes(name, aIds);
            NamingEnumeration en = attrs2.getAll();

            while (en.hasMoreElements()) {
                Attribute attr2 = (Attribute) en.next();
                Attribute attr1 = attrs1.get(attr2.getID());
                if (attr1 == null || !attr1.equals(attr2)) {
                    return fail("Command: getAttributes(\"" + name + "\", {"
                        + attrIds + "})\n    "
                        + "Retrieved attributes:\n        " + attrs2
                        + "\n    required:\n        " + attrs1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * @param ctxName
     * @param propName
     * @param propValue
     * @return test result
     */
    public int testAddProperty(String ctxName, String propName, String propValue) {
        try {
            Context ctx = (Context) getInitCtx().lookup(ctxName);
            ctx.addToEnvironment(propName, propValue);
            String retr = (String) ctx.getEnvironment().get(propName);
            if (!propValue.equals(retr)) {
                return fail("\n  Added to environment: " + propValue
                    + "\n  Retrieved: " + retr);
            }
        } catch (NamingException e) {
            return fail(e);
        }
        return pass();
    }

    /**
     * @param ctxName
     * @param propName
     * @param propValue
     * @return test result
     */
    public int testRemoveFromEnvironment(String ctxName, String propName,
        String propValue) {
        try {
            Context ctx = (Context) getInitCtx().lookup(ctxName);
            ctx.addToEnvironment(propName, propValue);
            String str = (String) ctx.removeFromEnvironment(propName);
            if (!propValue.equals(str)) {
                return fail("\n  removeFromEnvironment() returns: " + str
                    + "\n  Required: " + propValue);
            }

            str = (String) ctx.getEnvironment().get(propName);
            if (str != null) {
                return fail("\n  Property has not been removed from context "
                    + ctxName);
            }

            str = (String) ctx.removeFromEnvironment(propName);
            if (str != null)
                return fail("\n  removeFromEnvironment() returns: " + str
                    + "\n  Required: " + null);
        } catch (NamingException e) {
            return fail(e);
        }
        return pass();
    }

    /**
     * Federation test.
     * 
     * @param cname composite name, for example: www.example.com/index.html
     * @param res path to index.html file, for example:
     *            org/apache/harmony/test/func/api/javax/naming/share/index.html.
     *            This file should be in the classpath.
     * @return pass if files are equal.
     */
    public int testFederation(String cname, String res) {
        try {
            Context ctx = getInitCtx();
            int i = cname.indexOf("http://");
            if (i != -1)
                cname = cname.substring(i + 7);
            i = cname.indexOf(':');
            if (i != -1) {
                String port = null;
                int i2 = cname.indexOf('/', i);
                if (i2 != -1) {
                    port = cname.substring(i + 1, i2);
                    cname = cname.substring(0, i) + cname.substring(i2);
                } else {
                    port = cname.substring(i + 1);
                    cname = cname.substring(0, i) + "/";
                }
                ctx.addToEnvironment("netContext.resource.port", port);
            }

            String retrieved = (String) ctx.lookup(cname);
            if (retrieved == null) {
                return fail("null String retrieved");
            }

            InputStream is = getSystemResourceAsStream(res);
            String str = "";
            byte[] b = new byte[512];
            i = -1;
            while ((i = is.read(b)) >= 0) {
                str += new String(b, 0, i);
            }
            is.close();

            ByteArrayInputStream rqbais = new ByteArrayInputStream(str
                .getBytes());
            ByteArrayInputStream rtbais = new ByteArrayInputStream(retrieved
                .getBytes());

            if (!htmlToString(rtbais).equals(htmlToString(rqbais))) {
                return fail("Invalid String object retrieved:\n" + retrieved
                    + "\n required:\n" + str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }
        return pass();
    }

    /**
     * Tests DirContext.search(Name, Attributes, String []) method.
     * 
     * @param name the name of the context to search.
     * @param attrsId comma separated attributes Id to search for. Attributes
     *            should be described in the xml file. If empty or null, all
     *            objects in the target context are returned.
     * @param attributesToReturn the attributes to return. null indicates that
     *            all attributes are to be returned; an empty string indicates
     *            that none are to be returned.
     * @param res comma separated object Id, which should be retrieved. Objects
     *            should be described in the xml file.
     * @return fail if retrieved objects are not equal to the objects,
     *         enumerated in res variable.
     */
    public int testBasicSearch(String name, String attrsId,
        String attributesToReturn, String res) {

        Attributes mattrs = null;
        if (attrsId.equals("")) {
            mattrs = new BasicAttributes();
        } else if (!"NULL".equalsIgnoreCase(attrsId)) {
            mattrs = getAttributes(getElementById(getTest(), "Attributes",
                "Id", attrsId));
        }

        String[] astr = null;
        if (attributesToReturn.equals("")) {
            astr = new String[0];
        } else if (!"NULL".equalsIgnoreCase(attributesToReturn)) {
            astr = getCommaSeparatedTokens(attributesToReturn);
        }

        try {
            DirContext ctx = (DirContext) getInitCtx().lookup("");
            NamingEnumeration en = ctx.search(name, mattrs, astr);

            ArrayList reqObjs = new ArrayList();
            Hashtable sRes = new Hashtable();
            String[] s = getCommaSeparatedTokens(res);
            String err = "\n    search('" + name + "', '" + mattrs + "', '"
                + attributesToReturn.toLowerCase() + "') retrieved:";

            while (en.hasMore()) {
                SearchResult sr = (SearchResult) en.next();
                String n = sr.getName();
                Attributes as = sr.getAttributes();
                sRes.put(n, as);
                err += "\n        ctx:'" + n + "' attributes: " + as;
            }

            err += "\n\n    required:";
            for (int i = 0; i < s.length; i++) {
                Hashtable ht = getCtxById(s[i]);
                Hashtable nht = null;
                if (ht == null) {
                    ht = getBindById(s[i]);
                }
                if (ht == null) {
                    throw new Exception("Object " + s[i] + " not found");
                }

                String oname = (String) ht.get("name");
                if (astr == null) {
                    nht = ht;
                } else if (astr.length == 0) {
                    nht = new Hashtable();
                    nht.put("name", oname);
                    nht.put("Attributes", new BasicAttributes());
                } else {
                    nht = new Hashtable();
                    Attributes as = (Attributes) ht.get("Attributes");
                    Attributes nas = new BasicAttributes();
                    for (int n = 0; n < astr.length; n++) {
                        Attribute na = as.get(astr[n]);
                        if (na != null)
                            nas.put(na);
                    }
                    nht.put("name", oname);
                    nht.put("Attributes", nas);
                }
                reqObjs.add(nht);
                err += "\n        ctx:'" + nht.get("name") + "' attributes: "
                    + nht.get("Attributes");
            }

            if (reqObjs.size() != sRes.size()) {
                return fail(err);
            }

            for (int i = 0; i < reqObjs.size(); i++) {
                Hashtable ht = (Hashtable) reqObjs.get(i);
                Attributes as = (Attributes) sRes.get(ht.get("name"));
                if (as == null
                    || !compareAttrs(as, (Attributes) ht.get("Attributes")))
                    return fail(err);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e);
        }

        return pass();
    }

    /**
     * Tests DirContext.search(Name, String, Object[], SearchControls) method.
     * 
     * @param name the name of the context to search.
     * @param filterExpr the filter expression to use for the search. The
     *            expression may contain variables of the form "{i}" where i is
     *            a 0 or positive integer. May not be null.
     * @param filterArgs comma separated arguments to substitute for the
     *            variables in filterExpr. The value of filterArgs[i] will
     *            replace each occurrence of "{i}". If null, equivalent to an
     *            empty array.
     * @param scope
     * @param countLimit
     * @param res
     * @return test result
     */
    public int testAdvSearch(String name, String filterExpr, String filterArgs,
        String scope, String countLimit, String res) {
        Object[] o = getCommaSeparatedTokens(filterArgs);
        int ss = SearchControls.ONELEVEL_SCOPE;
        if ("OBJECT_SCOPE".equalsIgnoreCase(scope)) {
            ss = SearchControls.OBJECT_SCOPE;
        } else if ("SUBTREE_SCOPE".equalsIgnoreCase(scope)) {
            ss = SearchControls.SUBTREE_SCOPE;
        }

        try {
            DirContext ctx = (DirContext) getInitCtx().lookup("");
            SearchControls sc = new SearchControls();
            sc.setSearchScope(ss);
            sc.setCountLimit(Long.parseLong(countLimit));
            NamingEnumeration en = ctx.search(name, filterExpr, o, sc);

            ArrayList reqObjs = new ArrayList();
            Hashtable sRes = new Hashtable();
            String[] s = getCommaSeparatedTokens(res);
            String err = "\n    search('" + name + "', '" + filterExpr + "', {"
                + filterArgs + "}, SearchControls." + scope + ") retrieved:";
            while (en.hasMore()) {
                SearchResult sr = (SearchResult) en.next();
                String n = sr.getName();
                Attributes as = sr.getAttributes();
                sRes.put(n, as);
                err += "\n        ctx:'" + n + "' attributes: " + as;
            }

            err += "\n\n    required:";
            for (int i = 0; i < s.length; i++) {
                Hashtable ht = getCtxById(s[i]);
                if (ht == null) {
                    ht = getBindById(s[i]);
                }
                if (ht == null) {
                    throw new Exception("Object " + s[i] + " not found");
                }

                err += "\n        ctx:'" + ht.get("name") + "' attributes: "
                    + ht.get("Attributes");
                reqObjs.add(ht);
            }

            if (reqObjs.size() != sRes.size()) {
                return fail(err);
            }

            for (int i = 0; i < reqObjs.size(); i++) {
                Hashtable ht = (Hashtable) reqObjs.get(i);
                Attributes as = (Attributes) sRes.get(ht.get("name"));
                if (as == null
                    || !compareAttrs(as, (Attributes) ht.get("Attributes"))) {
                    return fail(err);
                }
            }
        } catch (Exception e) {
            return fail(e);
        }
        return pass();
    }

    /**
     * @param name
     * @param newAttrs
     * @param reqAttrs
     * @param mod_op
     * @return test result
     */
    public int testModifyAttributes(String name, String newAttrs,
        String reqAttrs, String mod_op) {
        int mod = DirContext.ADD_ATTRIBUTE;
        if ("REPLACE_ATTRIBUTE".equalsIgnoreCase(mod_op)) {
            mod = DirContext.REPLACE_ATTRIBUTE;
        } else if ("REMOVE_ATTRIBUTE".equalsIgnoreCase(mod_op)) {
            mod = DirContext.REMOVE_ATTRIBUTE;
        }

        try {
            Attributes na = getAttributes(getElementById(getTest(),
                "Attributes", "Id", newAttrs));
            Attributes ra = getAttributes(getElementById(getTest(),
                "Attributes", "Id", reqAttrs));

            DirContext ctx = (DirContext) getInitCtx().lookup("");
            ctx.modifyAttributes(name, mod, na);

            Attributes retas = ctx.getAttributes(name);
            if (!compareAttrs(ra, retas)) {
                String err = "\n    command: modifyAttributes('" + name
                    + "', DirContext." + mod_op + ", {" + na
                    + "} \n        retrieved: " + retas
                    + "\n        required: " + ra;
                return fail(err);
            }
        } catch (NamingException e) {
            return fail(e);
        }

        return pass();
    }

    /**
     * Unbinds the named objects.
     * 
     * @param ids comma separated binds Id.
     * @return fail if Naming exception is thrown.
     */
    public int testUnbindObjects(String ids) {
        int r = Result.PASS;
        StringTokenizer st = new StringTokenizer(ids, ",");
        while (st.hasMoreTokens()) {
            String id = st.nextToken().trim();
            Hashtable ht = getBindById(id);

            if (ht == null) {
                fail("Bind '" + id + "' not found");
                r = Result.FAIL;
                continue;
            }

            String name = (String) ht.get("name");
            String parent = (String) ht.get("ctx");

            try {
                DirContext ctx = (DirContext) getInitCtx().lookup(parent);
                ctx.unbind(name);
                pass();
            } catch (NamingException e) {
                fail(e);
                r = Result.FAIL;
            }
        }
        return r;
    }

    /**
     * Destroys the named contexts and removes them from the namespace.
     * 
     * @param ids comma separated contexts Id.
     * @return fail if Naming exception is thrown.
     */
    public int testDestroyCtx(String ids) {
        int r = Result.PASS;
        StringTokenizer st = new StringTokenizer(ids, ",");
        while (st.hasMoreTokens()) {
            String id = st.nextToken().trim();
            Hashtable ht = getCtxById(id);

            if (ht == null) {
                fail("Context '" + id + "' not found");
                r = Result.FAIL;
                continue;
            }

            String name = (String) ht.get("name");
            String parent = (String) ht.get("parent");

            try {
                DirContext ctx = (DirContext) getInitCtx().lookup(parent);
                ctx.destroySubcontext(name);
                pass();
            } catch (NamingException e) {
                fail(e);
                r = Result.FAIL;
            }
        }

        return r;
    }

    /**
     * Creates new JNDITest() instance and invokes test(String [] args) method.
     * 
     * @param args
     */
    public static void main(final String[] args) {
        // new Thread() {
        // public void run() {
        // int res = new JNDITest().test(args);
        //
        // if (res != Result.PASS) {
        // System.out.println("FAILED!!! " + res);
        // System.exit(res);
        // }
        // }
        // }.start();

        System.exit(new JNDITest().test(args));
    }

    /**
     * Retrieves text data from the HTML document.
     * 
     * @param is input stream
     * @return text data from the HTML document.
     * @throws IOException
     */
    private String htmlToString(InputStream is) throws IOException {
        final byte lt = "<".getBytes()[0];
        final byte gt = ">".getBytes()[0];
        String text = "";

        byte[] buf = new byte[1];
        boolean isText = true;
        String str = "";
        while (is.read(buf) != -1) {
            if (buf[0] == lt) {
                isText = false;
                text += str.trim();
                str = "";
                continue;
            } else if (buf[0] == gt) {
                isText = true;
                continue;
            }

            if (isText) {
                str += new String(buf);
            }
        }

        return text;
    }

}