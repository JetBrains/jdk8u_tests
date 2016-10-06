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
package org.apache.harmony.test.func.api.javax.naming.share;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 */
public class JNDITestUtils extends TestUtils {

    /**
     * Environment variables.
     */
    protected Hashtable env;

    /**
     * ArrayList of bindings, described in the xml file.
     */
    protected ArrayList binds;

    /**
     * ArrayList of contexts, described in the xml file.
     */
    protected ArrayList contexts;

    /**
     * InitialContext object.
     */
    protected Context initCtx;

    /**
     * Sets environment variable DirContext.SECURITY_AUTHENTICATION to <b>auth
     * </b> and initialises new InitialContext object.
     * 
     * @param auth
     * @return
     */
    public int login(String auth) {
        getEnv().put(Context.SECURITY_AUTHENTICATION, auth);
        initCtx = null;
        try {
            getInitCtx();
        } catch (NamingException e) {
            return fail(e.toString());
        }

        return pass();
    }

    /**
     * Returns object, that is described in the xml file in the tag
     * &lt;Context&gt; and has Id = <b>id.
     * 
     * @param ctxId
     * @return
     */
    protected Hashtable getCtxById(String ctxId) {
        ArrayList l = getContexts();
        for (int i = 0; i < l.size(); i++) {
            Hashtable ht = (Hashtable) l.get(i);
            String id = (String) ht.get("Id");

            if (id.equals(ctxId)) {
                return ht;
            }
        }

        return null;
    }

    /**
     * Returns object, that is described in the xml file in the tag &lt;Bind&gt;
     * and has Id = <b>id.
     * 
     * @param bindId
     * @return
     */
    protected Hashtable getBindById(String bindId) {
        ArrayList l = getBinds();
        for (int i = 0; i < l.size(); i++) {
            Hashtable ht = (Hashtable) l.get(i);
            String id = (String) ht.get("Id");

            if (id.equals(bindId)) {
                return ht;
            }
        }

        return null;
    }

    /**
     * Returns Attributes, that are described in the xml file and has Id =
     * <b>id.
     * 
     * @param id
     * @return
     */
    protected Attributes getAttributesById(String id) {
        Hashtable ht = getCtxById(id);
        if (ht == null) {
            ht = getBindById(id);
        }
        if (ht == null) {
            return null;
        }

        return (Attributes) ht.get("Attributes");
    }

    /**
     * Returns object, that is described in the xml file and has Id = <b>id.
     * 
     * @param id
     * @return
     */
    protected Hashtable getObjectById(String id) {
        Hashtable ht = getCtxById(id);
        if (ht == null) {
            ht = getBindById(id);
        }
        return ht;
    }

    /**
     * Compares two objects. If object type is DirContext - also compares
     * attributes, associated with this objects.
     * 
     * @param id
     *            Object, that is described in the xml file and has Id= <b>id
     *            </b>.
     * @param obj
     *            Object, that should be compared.
     * @return true if objects are equals.
     */
    protected boolean compareObjects(String id, Object obj) {
        Hashtable res = null;
        try {
            if (obj instanceof Context) {
                res = getObjectById(id);
                Context ctx = ((Context) obj);
                if ((ctx.getNameInNamespace().indexOf(
                        ctx.composeName((String) res.get("name"), (String) res
                                .get("parent"))) == -1)
                        || ((obj instanceof DirContext) && !compareAttrs(
                                (Attributes) res.get("Attributes"),
                                ((DirContext) obj).getAttributes("")))) {
                    return false;
                }
            } else {
                res = getBindById(id);
                if (!obj.equals(res.get("Object"))) {
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
     * Compares two Attributes objects.
     * 
     * @param attrs1
     * @param attrs2
     * @return
     * @throws NamingException
     */
    protected boolean compareAttrs(Attributes attrs1, Attributes attrs2)
            throws NamingException {
        if (attrs1 == null || attrs2 == null
                || (attrs1.size() != attrs2.size())) {
            return false;
        }
        NamingEnumeration ne = attrs1.getAll();
        while (ne.hasMore()) {
            Attribute attr1 = (Attribute) ne.next();
            Attribute attr2 = attrs2.get(attr1.getID());
            if (!compareAttrValues(attr1, attr2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares values of two Attribute objects.
     * 
     * @param attr1
     * @param attr2
     * @return
     */
    protected boolean compareAttrValues(Attribute attr1, Attribute attr2) {
        if (attr1 == null || attr2 == null || (attr1.size() != attr2.size())) {
            return false;
        }
        for (int i = 0; i < attr1.size(); i++) {
            try {
                return attr2.contains(attr1.get(i));
            } catch (NamingException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * String representation of the Context object.
     * 
     * @param ctx
     * @return
     */
    protected String printCtx(Context ctx) {
        String msg = "\n";
        try {
            msg += "    Context name: '" + ctx.getNameInNamespace() + "'";

            if (ctx instanceof DirContext) {
                msg += "\n        Attributes: "
                        + ((DirContext) ctx).getAttributes("") + "\n";
            }

        } catch (NamingException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * Set environment properties, described in the xml file.
     * 
     * @return The environment of this context
     */
    protected Hashtable getEnv() {
        if (env != null) {
            return env;
        }

        env = new Hashtable();
        NodeList nl = getTest().getElementsByTagName("SysEnv");
        for (int i = 0; i < nl.getLength(); i++) {
            NodeList params = nl.item(i).getChildNodes();
            for (int k = 0; k < params.getLength(); k++) {
                Node n = params.item(k);
                if (!(n instanceof Element)) {
                    continue;
                }
                Element e = (Element) n;
                String name = e.getAttribute("name");
                String value = e.getAttribute("value");
                if ("INITIAL_CONTEXT_FACTORY".equals(name)) {
                    value = getInitialContextFactoryValue(e);
                    env.put(Context.INITIAL_CONTEXT_FACTORY, value);
                } else if ("PROVIDER_URL".equals(name)) {
                    env.put(Context.PROVIDER_URL, value);
                } else if ("SECURITY_AUTHENTICATION".equals(name)) {
                    env.put(Context.SECURITY_AUTHENTICATION, value);
                } else if ("SECURITY_PRINCIPAL".equals(name)) {
                    env.put(Context.SECURITY_PRINCIPAL, value);
                } else if ("SECURITY_CREDENTIALS".equals(name)) {
                    env.put(Context.SECURITY_CREDENTIALS, value);
                } else {
                    env.put(name, value);
                }
            }
        }

        setEnv(Context.INITIAL_CONTEXT_FACTORY,
                getArg("INITIAL_CONTEXT_FACTORY"));

        setEnv(Context.PROVIDER_URL, getArg("PROVIDER_URL"));
        setEnv(Context.SECURITY_PRINCIPAL, getArg("SECURITY_PRINCIPAL"));
        setEnv(Context.SECURITY_CREDENTIALS, getArg("SECURITY_CREDENTIALS"));

        return env;
    }

    private void setEnv(String key, String value) {
        if (value != null && !value.startsWith("$")) {
            env.put(key, value);
        }
    }

    /**
     * The method to define initial context factory class. It looks all the
     * children of INITIAL_CONTEXT_FACTORY node in the xml text description file
     * and defines if the child's <b>value</b> attribute fit
     * 
     * @param initialContextFactoryElement
     *            INITIAL_CONTEXT_FACTORY node
     * @return String class name of initial factory
     */
    protected String getInitialContextFactoryValue(
            Element initialContextFactoryElement) {
        String valueFit = null;
        NodeList contextFactoryVariants = initialContextFactoryElement
                .getChildNodes();
        for (int i = 0; i < contextFactoryVariants.getLength(); i++) {
            Node observedNode = contextFactoryVariants.item(i);
            if (!(observedNode instanceof Element)) {
                continue;
            }
            String variant = ((Element) observedNode).getAttribute("value");
            try {
                Class.forName(variant);
                log.add(variant + " initial context class is fit");
                valueFit = defineInitialContextPreference(valueFit, variant);
            } catch (ClassNotFoundException cnf) {
                // Can't find a class specified. Try again
            }
        }
        // log.add("Can't find initial context");
        return valueFit;
    }

    /**
     * The method describes preferences in initial context. If the defined value
     * is null or contains "sun" in class name, value is changed otherwise
     * value is remains
     * 
     * @param target -
     *            previous value
     * @param candidat -
     *            candidate value
     * @return preferred value
     */
    protected String defineInitialContextPreference(String target,
            String candidat) {
        if (target == null) {
            return candidat;
        }
        if (isSunClass(target)) {
            log.add(candidat + " is preferred");
            return candidat;
        }
        return target;

    }

    /**
     * Method verifies the class name contains "sun". <br>It is specific
     * case of contains() method (it's written due to contains() method absent in
     * 1.4)
     * 
     * @param className
     * @return is the class name contains "sun"
     */
    protected boolean isSunClass(String className) {
        for (int i = 0; i < className.length() - 3; i++) {
            boolean isFound = (className.charAt(i) == '.')
                    && (className.charAt(i) == 's')
                    && (className.charAt(i) == 'u')
                    && (className.charAt(i) == 'n')
                    && (className.charAt(i) == '.');
            if (isFound) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns InitialContext object.
     * 
     * @return InitialContext object
     * @throws NamingException
     */
    protected Context getInitCtx() throws NamingException {
        if (initCtx == null) {
            initCtx = new InitialContext(getEnv());
        }

        return initCtx;
    }

    /**
     * Returns ArrayList of bindings, described in the xml file.
     * 
     * @return
     */
    protected ArrayList getBinds() {
        if (binds != null) {
            return binds;
        }

        binds = new ArrayList();
        NodeList nl = getTest().getElementsByTagName("Bind");
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (!(n instanceof Element)) {
                continue;
            }

            Element bind = (Element) n;
            Hashtable ht = new Hashtable();
            Attributes attrs = getAttributes((Element) bind
                    .getElementsByTagName("Attributes").item(0));

            ht.put("type", "Bind");
            ht.put("name", bind.getAttribute("name"));
            ht.put("ctx", bind.getAttribute("ctx"));
            ht.put("Id", bind.getAttribute("Id"));
            ht.put("Attributes", attrs);
            Object obj = getObj(bind.getElementsByTagName("Object"));
            if (obj != null) {
                ht.put("Object", obj);
            }
            binds.add(ht);
        }

        return binds;
    }

    /**
     * Returns ArrayList of contexts, described in the xml file.
     * 
     * @return
     */
    protected ArrayList getContexts() {
        if (contexts != null) {
            return contexts;
        }

        contexts = new ArrayList();
        NodeList nl = getTest().getElementsByTagName("Context");
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (!(n instanceof Element)) {
                continue;
            }

            Element bind = (Element) n;
            Hashtable ht = new Hashtable();
            Attributes attrs = getAttributes((Element) bind
                    .getElementsByTagName("Attributes").item(0));

            ht.put("type", "Context");
            ht.put("name", bind.getAttribute("name"));
            ht.put("parent", bind.getAttribute("parent"));
            ht.put("Id", bind.getAttribute("Id"));
            ht.put("Attributes", attrs);

            contexts.add(ht);
        }

        return contexts;
    }

    /**
     * Returns Attributes, described in the <b>attributes </b> object.
     * 
     * @param attributes
     *            Element object whose tag name is &lt;Attribute&gt;. This
     *            element should contain information about attributes.
     * @return
     */
    protected Attributes getAttributes(Element attributes) {
        Attributes attrs = null;
        if ("false".equalsIgnoreCase(attributes.getAttribute("ignoreCase"))) {
            attrs = new BasicAttributes(false);
        } else {
            attrs = new BasicAttributes(true);
        }

        NodeList nl = attributes.getElementsByTagName("Attribute");
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (!(n instanceof Element)) {
                continue;
            }
            attrs.put(getAttribute((Element) n));
        }
        return attrs;
    }

    /**
     * Returns Attribute, described in the <b>attribute </b> object.
     * 
     * @param attribute
     * @return
     */
    protected Attribute getAttribute(Element attribute) {
        Attribute attr = null;
        if ("true".equalsIgnoreCase(attribute.getAttribute("ordered"))) {
            attr = new BasicAttribute(attribute.getAttribute("name"), true);
        } else {
            attr = new BasicAttribute(attribute.getAttribute("name"), false);
        }

        NodeList nl = attribute.getElementsByTagName("Value");
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (!(n instanceof Element)) {
                continue;
            }

            Element v = (Element) n;
            String index = v.getAttribute("index");
            String value = v.getChildNodes().item(0).getNodeValue();
            if (!"".equals(index)) {
                try {
                    attr.add(Integer.parseInt(index), value);
                    continue;
                } catch (Exception e) {
                    log.info(e.toString());
                }
            }

            attr.add(value);
        }

        return attr;
    }

    /**
     * Returns Object, described in the <b>nl </b> object.
     * 
     * @param nl
     * @return
     */
    protected Object getObj(NodeList nl) {
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (!(n instanceof Element)) {
                continue;
            }

            Element object = (Element) n;
            NodeList nll = object.getElementsByTagName("Param");
            Class[] cc = new Class[nll.getLength()];
            Object[] co = new Object[nll.getLength()];
            for (int ii = 0; ii < nll.getLength(); ii++) {
                Node nn = nll.item(ii);
                if (!(nn instanceof Element)) {
                    continue;
                }

                Element param = (Element) nn;
                String type = param.getAttribute("type");
                String value = param.getChildNodes().item(0).getNodeValue();
                try {
                    Class c = Class.forName(type);
                    Constructor constr = c.getConstructor(new Class[] { Class
                            .forName("java.lang.String") });

                    cc[ii] = c;
                    co[ii] = constr.newInstance(new Object[] { value });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                return Class.forName(object.getAttribute("type"))
                        .getConstructor(cc).newInstance(co);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}