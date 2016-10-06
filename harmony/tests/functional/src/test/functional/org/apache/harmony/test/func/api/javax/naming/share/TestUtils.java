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

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.Test;

/**
 */
public class TestUtils extends Test {

    /**
     * XML Document object.
     */
    protected Document doc;

    /**
     * Test element in the XML document.
     */
    protected Element test;

    /**
     * Command line arguments.
     */
    protected String[] args;

    /**
     * Test result.
     */
    protected int result = Result.PASS;

    /**
     * List methods which should be invoked before the test.
     */
    protected ArrayList beforeTestMethods;

    /**
     * Test methods.
     */
    protected ArrayList testMethods;

    /**
     * List methods which should be invoked after the test.
     */
    protected ArrayList afterTestMethods;

    /**
     * Current method.
     */
    protected String currentMethod;

    /**
     * Indicates whether the messages should be logged.
     */
    protected boolean logMsg = true;

    /**
     * Logger.
     */
    protected Logger localLog = Logger.getAnonymousLogger();

    /**
     * Dynamic key. All properties ${dynamic.key} in the XML will be replaced
     * with the value of this field.
     */
    protected final String DYNAMIC_KEY = String.valueOf(System
            .currentTimeMillis()
            + hashCode());

    /**
     * Run the test
     * 
     * @param takenArguments
     *            command line arguments.
     * @return test result
     */
    public int test(String[] takenArguments) {
        this.args = takenArguments;
        return test();
    }

    /**
     * Run the test.
     * 
     * @return test result
     */
    public int test() {
        try {
            long time = new Date().getTime();
            invoke();
            echo("Time = " + (new Date().getTime() - time));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.toString());
        }
        return result;
    }

    /**
     * Parses the content of the XML file and returns new Document object. Path
     * to the XML file is the value of <b>xmlPath </b> argument.
     * 
     * @return Document object, taken from xmlPath test parameter
     */
    protected Document getDoc() {
        if (doc != null)
            return doc;

        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }

        try {
            InputStream is = getSystemResourceAsStream(getArg("xmlPath"));
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(is);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(fail(e.toString()));
        }

        addDynamicKey(doc.getDocumentElement());

        return doc;
    }

    private void addDynamicKey(final Element el) {
        final NodeList nl;
        final NamedNodeMap nnm = el.getAttributes();
        int len = nnm.getLength();
        for (int i = 0; i < len; i++) {
            Attr attr = (Attr) nnm.item(i);
            attr.setValue(attr.getValue().replaceAll("\\$\\{dynamic.key\\}",
                    DYNAMIC_KEY));
        }

        nl = el.getChildNodes();
        len = nl.getLength();
        for (int i = 0; i < len; i++) {
            final Node n = nl.item(i);
            if (n instanceof Element) {
                addDynamicKey((Element) n);
            } else if ((n instanceof Text) && !(n instanceof Comment)) {
                n.setNodeValue(n.getNodeValue().replaceAll(
                        "\\$\\{dynamic.key\\}", DYNAMIC_KEY));
            }
        }
    }

    /**
     * Retrieves the XML document as input stream.
     * 
     * @param res
     * @return the stream
     */
    protected InputStream getSystemResourceAsStream(String res) {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }

        return classLoader.getResourceAsStream(res);
    }

    /**
     * Parses child nodes of the root element of the document and returns the
     * element whose tag name is <b>tagName </b> and value of the attribute
     * <b>attrName </b> is <b>attrValue </b>.
     * 
     * @param tagName
     * @param attrName
     * @param attrValue
     * @return Document object element, with tagName, atterName and attrValue
     *         given
     */
    protected Element getElementById(String tagName, String attrName,
            String attrValue) {
        return getElementById(getDoc().getDocumentElement(), tagName, attrName,
                attrValue);
    }

    /**
     * Parses child nodes of the element <b>el </b> and returns the element
     * whose tag name is <b>tagName </b> and value of the attribute <b>attrName
     * </b> is <b>attrValue </b>.
     * 
     * @param el
     * @param tagName
     * @param attrName
     * @param attrValue
     * @return Document object element el child, with tagName, atterName and
     *         attrValue given
     */
    protected Element getElementById(Element el, String tagName,
            String attrName, String attrValue) {
        if (el == null || tagName == null || attrName == null
                || attrValue == null)
            return null;
        NodeList nl = el.getElementsByTagName(tagName);
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            if (attrValue.equals(e.getAttribute(attrName)))
                return e;
        }
        return null;
    }
   
    /**
     * @return the <b>Test </b> element whose name attribute equals to the value
     *         of <b>testName </b> argument
     */
    protected Element getTest() {
        if (test != null)
            return test;

        test = getTest(getElementById("Test", "name", getArg("testName")));
        return test;
    }

    /**
     * Extends all child nodes of the <b>Test </b> element whose name attribute
     * is equal to the value of <b>extends </b> attribute of the given element
     * <b>e </b> and returns obtained element.
     * 
     * @param e
     * @return element whose name attribute is equal to the value of <b>extends
     *         </b> attribute of the given element
     */
    protected Element getTest(Element e) {
        Element ext = getElementById("Test", "name", e.getAttribute("extends"));

        if (ext == null
                || "false".equalsIgnoreCase(ext.getAttribute("heritable")))
            return e;

        ext = removeUnheritable((Element) ext.cloneNode(true));

        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i).cloneNode(true);
            ext.appendChild(n);
        }

        ext.setAttribute("name", e.getAttribute("name"));

        String s = ext.getAttribute("extends");
        if (s != null && !"".equals(s)) {
            ext.setAttribute("extends", s);
            return getTest(ext);
        }

        return ext;
    }

    /**
     * Removes all child nodes of the element <b>e </b> whose <b>heritable </b>
     * attribute value is <b>false </b>
     * 
     * @param e
     * @return Modified element
     */
    protected Element removeUnheritable(Element e) {
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (!(n instanceof Element))
                continue;

            String heritable = ((Element) n).getAttribute("heritable");
            if (heritable != null && "false".equalsIgnoreCase(heritable)) {
                e.removeChild(n);
                continue;
            }

            removeUnheritable((Element) n);
        }

        return e;
    }

    /**
     * @return methods, which should be invoked before starting the test. Those
     *         methods should be described int the <b>&lt;BeforeTestMethods&gt;
     *         </b> tag.
     */
    protected ArrayList getBeforeTestMethods() {
        return getMethods("BeforeTestMethods", beforeTestMethods);
    }

    /**
     * @return test methods, which should be invoked. Those methods should be
     *         described int the <b>&lt;AfterTestMethods&gt; </b> tag.
     */
    protected ArrayList getTestMethods() {
        return getMethods("TestMethods", testMethods);
    }

    /**
     * @return methods, which should be invoked after test methods. Those
     *         methods should be described int the <b>&lt;AfterTestMethods&gt;
     *         </b> tag.
     */
    protected ArrayList getAfterTestMethods() {
        return getMethods("AfterTestMethods", afterTestMethods);
    }

    /**
     * Puts all methods, described in <b>tagName </b> tag, into the ArrayList
     * <b>methods </b>. Each method should be described in the <b>Method </b>
     * tag.
     * 
     * @param tagName
     * @param methods
     * @return List of methods
     */
    protected ArrayList getMethods(String tagName, ArrayList methods) {
        if (methods != null)
            return methods;

        methods = new ArrayList();
        NodeList nl = getTest().getElementsByTagName(tagName);
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (!(n instanceof Element))
                continue;

            Element ms = (Element) n;
            NodeList nll = ms.getElementsByTagName("Method");
            for (int ii = 0; ii < nll.getLength(); ii++) {
                Node nn = nll.item(ii);
                if (!(nn instanceof Element))
                    continue;

                Element method = (Element) nn;
                Hashtable ht = new Hashtable();
                ArrayList paramTypes = new ArrayList();
                ArrayList paramValues = new ArrayList();
                NodeList nlll = method.getElementsByTagName("Param");
                for (int iii = 0; iii < nlll.getLength(); iii++) {
                    Node nnn = nlll.item(iii);
                    if (!(nnn instanceof Element))
                        continue;

                    Element param = (Element) nnn;
                    try {
                        String paramV = null;
                        try {
                            paramV = param.getChildNodes().item(0)
                                    .getNodeValue();
                        } catch (NullPointerException e) {
                            paramV = "";
                        }
                        Class c = Class.forName(param.getAttribute("type"));
                        Constructor cs = c.getConstructor(new Class[] { Class
                                .forName("java.lang.String") });
                        paramTypes.add(c);
                        paramValues.add(cs.newInstance(new Object[] { paramV
                                .trim() }));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                ht.put("name", (method.getAttribute("name")).trim());
                ht.put("logMsg", (method.getAttribute("logMsg")).trim());
                ht.put("testName", (method.getAttribute("testName")).trim());
                ht.put("position", (method.getAttribute("position")).trim());
                ht.put("paramTypes", paramTypes.toArray(new Class[paramTypes
                        .size()]));
                ht.put("paramValues", paramValues.toArray());
                methods.add(ht);
            }
        }

        return sortMethods(methods);
    }

    /**
     * @param methods
     * @return sorted methods
     */
    protected ArrayList sortMethods(ArrayList methods) {
        ArrayList sm = new ArrayList(methods);
        for (int i = 0; i < methods.size(); i++) {
            Hashtable ht = (Hashtable) methods.get(i);
            String position = (String) ht.get("position");
            if (position != null && !position.equals("")) {
                try {
                    sm.remove(i);
                    sm.add(Integer.parseInt(position), ht);
                } catch (Exception e) {
                    log.add("sortMethods method: Warning: Exception is thrown");
                }
            }
        }
        return sm;
    }

    /**
     * Invokes all methods, stored in the beforeTestMethosd, testMethods and
     * afterTestMethods ArrayList variables.
     * 
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected void invoke() throws SecurityException, IllegalArgumentException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        logMsg = false;
        invoke(getBeforeTestMethods());
        logMsg = true;
        invoke(getTestMethods());
        logMsg = false;
        invoke(getAfterTestMethods());
    }

    /**
     * Invokes all methods, stored in the ArrayList <b>l </b> variable.
     * 
     * @param l
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected void invoke(ArrayList l) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        for (int i = 0; i < l.size(); i++) {
            Hashtable ht = (Hashtable) l.get(i);
            currentMethod = (String) ht.get("name");
            Method m = this.getClass().getMethod(currentMethod,
                    (Class[]) ht.get("paramTypes"));
            m.invoke(this, (Object[]) ht.get("paramValues"));

        }
    }

    /**
     * @param name
     * @return argument value, that corresponds argument name
     */
    protected String getArg(String name) {
        if (name == null)
            return null;
        for (int i = 0; i < args.length - 1; i++) {
            try {
                if (name.equals(args[i]))
                    return args[i + 1];
            } catch (Exception e) {
                localLog.info(e.toString());
                break;
            }
        }
        return null;
    }

    /**
     * @param str
     * @return tokenized str
     */
    public String[] getCommaSeparatedTokens(String str) {
        if (str == null)
            return new String[0];
        StringTokenizer st = new StringTokenizer(str, ",");
        ArrayList list = new ArrayList();
        while (st.hasMoreTokens()) {
            String t = st.nextToken().trim();
            if (t.length() > 0)
                list.add(t);
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * @param str
     * @param value
     * @return true if str contains value given, 
     */
    public boolean containsValue(String[] str, String value) {
        for (int i = 0; i < str.length; i++) {
            if (value.equals(str[i]))
                return true;
        }
        return false;
    }

    /**
     * @param o
     * @return message
     */
    public String printArray(Object[] o) {
        String msg = "{";
        for (int i = 0; i < o.length; i++) {
            msg += o[i];
            if (i != o.length - 1)
                msg += ", ";
        }
        msg += "}";
        return msg;
    }

    /**
     * @return "PASSED" result
     */
    public int pass() {
        return pass("PASSED");
    }

    /**
     * @param msg
     * @return "PASSED" result
     */
    public int pass(String msg) {
        if (logMsg) {
            localLog.logp(Level.parse("INFO"), this.getClass().getName(),
                    currentMethod + ": PASSED", msg);
        }

        return Result.PASS;
    }

    /**
     * @param o
     * @return "FAILED" result
     */
    public int fail(Object o) {
        return fail(o.toString());
    }

    /**
     * @param msg
     * @return "FAILED" result
     */
    public int fail(String msg) {
        if (logMsg) {
            localLog.logp(Level.parse("INFO"), this.getClass().getName(),
                    currentMethod + ": FAILED", msg);
        }

        result = Result.FAIL;
        return Result.FAIL;
    }

    /**
     * @param msg
     * @return "ERROR" result
     */
    public int error(String msg) {
        if (logMsg) {
            localLog.logp(Level.parse("INFO"), this.getClass().getName(),
                    currentMethod, msg);
        }

        result = Result.ERROR;
        return Result.ERROR;
    }

    /**
     * @param o
     */
    public static void echo(Object o) {
        System.out.println(o);
    }

    /**
     * @param str
     */
    public static void echo(String str) {
        System.out.println(str);
    }
}