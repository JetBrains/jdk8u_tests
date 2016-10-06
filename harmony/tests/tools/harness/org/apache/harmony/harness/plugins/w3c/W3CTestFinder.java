/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Vladimir A. Ivanov
 * @author Serguei I. Katkov
 * @version $Revision: 1.9 $
 */
package org.apache.harmony.harness.plugins.w3c;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Finder;
import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.TestIR;

public class W3CTestFinder implements Finder {

    public static final String PLUGIN_NAME              = "TestFinder";
    public static final String SUBS_OPT                 = "subsuite";
    public static final String PATH_OPT                 = "path";
    public static final String NIST_OPT                 = "nist_xmlschema";
    public static final String XML_OPT                  = "xml_test";
    public static final String XPATH_OPT                = "xslt_xpath";
    public static final String MSXSD_OPT                = "msxsdtest";

    protected boolean          nist_XMLSchema_tests_def = true;
    protected boolean          xml_test_suite_def       = true;
    protected boolean          xslt_XPath_tests_def     = true;
    protected boolean          msxsdtest_Def            = true;

    String                     nist_XMLSchema_run_class = "org.apache.harmony.harness.plugins.w3c.W3CJAXPSchemaRun";
    String                     xml_test_run_class       = "org.apache.harmony.harness.plugins.w3c.W3CJAXPxml_testRun";
    String                     xslt_XPath_run_class     = "org.apache.harmony.harness.plugins.w3c.W3CXersesXPathRun";
    String                     msxsdtest_Run_Class      = "org.apache.harmony.harness.plugins.w3c.W3CJAXPSchemaRun";

    protected Logging          log                      = Main
                                                            .getCurCore()
                                                            .getInternalLogger();
    ArrayList                  nistSchemaOpt            = new ArrayList();
    ArrayList                  nistXmlOpt               = new ArrayList();
    ArrayList                  xpathOpt                 = new ArrayList();
    ArrayList                  msxsdtestOpt             = new ArrayList();

    ConfigIR                   cfg                      = Main.getCurCore()
                                                            .getConfigIR();

    ArrayList                  listToExec               = new ArrayList();

    public W3CTestFinder() {
        if (cfg != null) {
            try {
                ArrayList prop = cfg.getPluginProperties(PLUGIN_NAME);
                if (prop.size() >= 2) {
                    HashMap property = (HashMap)prop.get(1);
                    if (property.containsKey(SUBS_OPT)) {
                        HashMap subs = (HashMap)property.get(SUBS_OPT);
                        log
                            .add(Level.CONFIG,
                                "TestHarness.\tTry to find tests according to config file");
                        String tmp;
                        if (subs.containsKey(NIST_OPT)) {
                            nistSchemaOpt = (ArrayList)subs.get(NIST_OPT);
                            tmp = (String)nistSchemaOpt.get(0);
                            if ("false".equalsIgnoreCase(tmp)) {
                                nist_XMLSchema_tests_def = false;
                                log
                                    .add(Level.CONFIG,
                                        "TestHarness.\tNIST_XMLSchema suite deselected according to config");
                            }
                        } else {
                            log
                                .add(Level.CONFIG,
                                    "TestHarness.\tNIST_XMLSchema suite selected by default");
                        }
                        if (subs.containsKey(XML_OPT)) {
                            nistXmlOpt = (ArrayList)subs.get(XML_OPT);
                            tmp = (String)nistXmlOpt.get(0);
                            if ("false".equalsIgnoreCase(tmp)) {
                                xml_test_suite_def = false;
                                log
                                    .add(Level.CONFIG,
                                        "TestHarness.\tNIST_XML suite deselected according to config");
                            }
                        } else {
                            log
                                .add(Level.CONFIG,
                                    "TestHarness.\tNIST_XML suite selected by default");
                        }
                        if (subs.containsKey(XPATH_OPT)) {
                            xpathOpt = (ArrayList)subs.get(XPATH_OPT);
                            tmp = (String)xpathOpt.get(0);
                            if ("false".equalsIgnoreCase(tmp)) {
                                xslt_XPath_tests_def = false;
                                log
                                    .add(Level.CONFIG,
                                        "TestHarness.\tXSLT_XPath suite deselected according to config");
                            }
                        } else {
                            log
                                .add(Level.CONFIG,
                                    "TestHarness.\tXSLT_XPath suite selected by default");
                        }
                        if (subs.containsKey(MSXSD_OPT)) {
                            msxsdtestOpt = (ArrayList)subs.get(MSXSD_OPT);
                            tmp = (String)msxsdtestOpt.get(0);
                            if ("false".equalsIgnoreCase(tmp)) {
                                msxsdtest_Def = false;
                                log
                                    .add(Level.CONFIG,
                                        "TestHarness.\tmsxsdtest suite deselected according to config");
                            }
                        } else {
                            log
                                .add(Level.CONFIG,
                                    "TestHarness.\tmsxsdtest suite selected by default");
                        }
                    }
                    prop = cfg.getPluginProperties("nist_xmlschemaRun");
                    if (prop.size() >= 2) {
                        nist_XMLSchema_run_class = (String)prop.get(0);
                        property = (HashMap)prop.get(1);
                        if (property.containsKey(PATH_OPT)) {
                            HashMap subs = (HashMap)property.get(PATH_OPT);
                            if (subs.containsKey(NIST_OPT)) {
                                nistSchemaOpt = (ArrayList)subs.get(NIST_OPT);
                            }
                        }
                    }
                    prop = cfg.getPluginProperties("xml_testRun");
                    if (prop.size() >= 2) {
                        xml_test_run_class = (String)prop.get(0);
                        property = (HashMap)prop.get(1);
                        if (property.containsKey(PATH_OPT)) {
                            HashMap subs = (HashMap)property.get(PATH_OPT);
                            if (subs.containsKey(XML_OPT)) {
                                nistXmlOpt = (ArrayList)subs.get(XML_OPT);
                            }
                        }
                    }
                    prop = cfg.getPluginProperties("xslt_xpathRun");
                    if (prop.size() >= 2) {
                        xslt_XPath_run_class = (String)prop.get(0);
                        property = (HashMap)prop.get(1);
                        if (property.containsKey(PATH_OPT)) {
                            HashMap subs = (HashMap)property.get(PATH_OPT);
                            if (subs.containsKey(XPATH_OPT)) {
                                xpathOpt = (ArrayList)subs.get(XPATH_OPT);
                            }
                        }
                    }
                    prop = cfg.getPluginProperties("msxsdtestRun");
                    if (prop.size() >= 2) {
                        msxsdtest_Run_Class = (String)prop.get(0);
                        property = (HashMap)prop.get(1);
                        if (property.containsKey(PATH_OPT)) {
                            HashMap subs = (HashMap)property.get(PATH_OPT);
                            if (subs.containsKey(MSXSD_OPT)) {
                                msxsdtestOpt = (ArrayList)subs.get(MSXSD_OPT);
                            }
                        }
                    }
                } else {
                    log.add(Level.INFO,
                        "TestHarness.\tTry to find tests in 4 w3c testsuites");
                }
            } catch (Exception e) {
                log.add(Level.INFO,
                    "TestHarness.\tUnexpected exception while parse the config for W3CTestFinder "
                        + e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.plugins.DirFinder#findFiles(java.io.File,
     *      java.lang.String[])
     */
    protected void findFiles() {
        if (nist_XMLSchema_tests_def) {
            TestFinderNIST_XMLSchema_Tests tfn;
            if (nistSchemaOpt.size() < 2) {
                tfn = new TestFinderNIST_XMLSchema_Tests(cfg.getTestSuiteRoot()
                    + "/nisttest/NISTTestsAll", "NISTXMLSchemaTestSuite.xml",
                    this);
                log.add(Level.INFO, "TestHarness.\tFind NIST_XMLSchema_Tests "
                    + cfg.getTestSuiteRoot()
                    + "/nisttest/NISTTestsAll/NISTXMLSchemaTestSuite.xml");
            } else {
                tfn = new TestFinderNIST_XMLSchema_Tests(cfg.getTestSuiteRoot()
                    + "/" + nistSchemaOpt.get(0), (String)nistSchemaOpt.get(1),
                    this);
                log.add(Level.INFO, "TestHarness.\tFind NIST_XMLSchema_Tests "
                    + cfg.getTestSuiteRoot() + "/" + nistSchemaOpt.get(0) + "/"
                    + (String)nistSchemaOpt.get(1));
            }
            try {
                tfn.run();
            } catch (ConfigurationException e) {
                log.add(Level.WARNING,
                    "TestHarness.\tUnexpected exception (NIST_XMLSchema_Tests) "
                        + e);
            }
        }
        if (xml_test_suite_def) {
            TestFinderXML_Test_Suite tfn;
            if (nistXmlOpt.size() < 2) {
                tfn = new TestFinderXML_Test_Suite(cfg.getTestSuiteRoot()
                    + "/nisttest/NISTTestsAll", "xmlconf.xml", this);
            } else {
                tfn = new TestFinderXML_Test_Suite(cfg.getTestSuiteRoot() + "/"
                    + nistXmlOpt.get(0), (String)nistXmlOpt.get(1), this);
            }
            try {
                tfn.run();
            } catch (ConfigurationException e) {
                log.add(Level.WARNING,
                    "TestHarness.\tUnexpected exception (XML core) " + e);
            }
        }
        if (msxsdtest_Def) {
            TestFindermsxsdtest tfn;
            if (msxsdtestOpt.size() < 2) {
                try {
                    tfn = new TestFindermsxsdtest(cfg.getTestSuiteRoot(),
                        "result-ms-Particles.htm", this);
                    tfn.run();
                } catch (ConfigurationException e) {
                    log.add(Level.WARNING,
                        "TestHarness.\tUnexpected exception (msxsdtest) " + e);
                }
            } else {
                String tmp = (String)msxsdtestOpt.get(0);
                if (tmp.length() > 0) {
                    tmp = "/" + tmp;
                }
                for (int i = 1; i < msxsdtestOpt.size(); i++) {
                    try {
                        tfn = new TestFindermsxsdtest(cfg.getTestSuiteRoot()
                            + tmp, (String)msxsdtestOpt.get(i), this);
                        tfn.run();
                        log.add(Level.INFO, "TestHarness.\tFind msxsdtest: "
                            + msxsdtestOpt.get(i));
                    } catch (ConfigurationException e) {
                        log.add(Level.WARNING,
                            "TestHarness.\tUnexpected exception (msxsdtest: "
                                + msxsdtestOpt.get(i) + ") " + e);
                    }
                }
            }
        }
        if (xslt_XPath_tests_def) {
            TestFinderXSLT_XPath_Tests tfn;
            if (xpathOpt.size() < 2) {
                tfn = new TestFinderXSLT_XPath_Tests(cfg.getTestSuiteRoot()
                    + "/nist", "nist.xml", this);
            } else {
                tfn = new TestFinderXSLT_XPath_Tests(cfg.getTestSuiteRoot()
                    + "/" + xpathOpt.get(0), (String)xpathOpt.get(1), this);
            }
            try {
                tfn.run();
            } catch (ConfigurationException e) {
                log.add(Level.WARNING,
                    "TestHarness.\tUnexpected exception (XSLT_XPath_Tests_Def) "
                        + e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#check(java.lang.String)
     */
    public boolean check(String testName) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#find()
     */
    public int find() throws ConfigurationException {
        findFiles();
        return getCurSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#find(java.lang.String)
     */
    public int find(String subsuite) throws ConfigurationException {
        return find();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#find(java.lang.String,
     *      java.lang.String[])
     */
    public int find(String subsuite, String[] mask)
        throws ConfigurationException {
        return find();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#getNext()
     */
    public Object getNext() {
        synchronized (listToExec) {
            if (!listToExec.isEmpty()) {
                return listToExec.remove(0);
            }
        }
        return null;
    }

    public void addTest(TestIR test) {
        synchronized (listToExec) {
            listToExec.add(test);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#getCurSize()
     */
    public int getCurSize() {
        synchronized (listToExec) {
            return listToExec.size();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#clear()
     */
    public void clear() {
        synchronized (listToExec) {
            listToExec.clear();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#setFindRoot(java.lang.String)
     */
    public String setFindRoot(String newValue) {
        return newValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#isActive()
     */
    public boolean isActive() {
        // while always report as active
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#stop()
     */
    public void stop() {
        // while do nothing
    }
}

/**
 * TestFinder for the NIST Testsuite. url parameter for start method should
 * point to NISTXMLSchemaTestSuite.xml file.
 * (\nisttest\NISTTestsAll\NISTXMLSchemaTestSuite.xml)
 */

class TestFinderNIST_XMLSchema_Tests {

    private String          url;
    private String          tbase;
    private File            base;
    private DocumentBuilder db;
    private final String    testSuiteId = W3CTestFinder.NIST_OPT;
    private String          link;
    private String          facet;
    private String          schema;
    private String          schemaFile;
    private String          docFile;
    private String          notes;
    private W3CTestFinder   toAddTests;

    public TestFinderNIST_XMLSchema_Tests(String testBase, String fileName,
        W3CTestFinder toAdd) {
        base = new File(testBase);
        tbase = testBase;
        url = fileName;
        toAddTests = toAdd;
    }

    public void run() throws ConfigurationException {
        // parsing head content file
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(tbase + File.separator + url);
            NodeList nodes = doc.getElementsByTagName("Link");
            int count = nodes.getLength();
            for (int i = 0; i < count; i++) {
                parseLink(nodes.item(i));
            }
        } catch (Throwable e) {
            new ConfigurationException("Parser Error:" + e);
        }
    }

    private void parseLink(Node node) throws Exception {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm == null)
            return;
        link = nnm.getNamedItem("name").getNodeValue();
        File href = new File(base, nnm.getNamedItem("href").getNodeValue());
        Document doc = db.parse(href);
        doc.normalize();
        NodeList nodes = doc.getElementsByTagName("Facet");
        int count = nodes.getLength();
        for (int i = 0; i < count; i++) {
            parseFacet(nodes.item(i));
        }
    }

    private void parseFacet(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm == null)
            return;
        facet = nnm.getNamedItem("Name").getNodeValue();
        NodeList nodes = node.getChildNodes();
        int count = nodes.getLength();
        for (int i = 0; i < count; i++) {
            parseSchema(nodes.item(i));
        }
    }

    private void parseSchema(Node node) {
        if (!node.getNodeName().equals("Schema"))
            return;
        NamedNodeMap nnm = node.getAttributes();
        if (nnm == null)
            return;
        schema = nnm.getNamedItem("name").getNodeValue();
        schemaFile = nnm.getNamedItem("href").getNodeValue();
        NodeList nodes = node.getChildNodes();
        int count = nodes.getLength();
        for (int i = 0; i < count; i++) {
            parseInstance(nodes.item(i));
        }
    }

    private void parseInstance(Node node) {
        if (node.getNodeName().equals("Notes")) {
            notes = node.getFirstChild().getNodeValue();
        } else if (node.getNodeName().equals("Instance")) {
            NamedNodeMap nnm = node.getAttributes();
            if (nnm == null)
                return;
            String instance = nnm.getNamedItem("name").getNodeValue();
            docFile = nnm.getNamedItem("href").getNodeValue();
            TestIR test = new TestIR("Runtime", testSuiteId + "/"
                + docFile.substring(0, docFile.length() - ".xml".length())); //
            ArrayList paramVM = new ArrayList();
            ArrayList paramTst = new ArrayList();
            paramTst.add("toRun");
            paramTst.add(toAddTests.nist_XMLSchema_run_class);
            paramTst.add("valid");
            paramTst.add(tbase + "/" + schemaFile);
            paramTst.add(tbase + "/" + docFile);
            ArrayList param = new ArrayList();
            param.add(paramVM);
            param.add(paramTst);
            test.setRunnerParam(param);
            toAddTests.addTest(test);
        }
    }
}

/**
 * TestFinder for the XML-Test-Suite Testsuite. url parameter for start method
 * should point to xmlconf.xml file in this testsuite.
 */

class TestFinderXML_Test_Suite {

    private final String  testSuiteId = W3CTestFinder.XML_OPT;
    private String        url;
    private String        tbase;

    private W3CTestFinder toAddTests;

    public TestFinderXML_Test_Suite(String testBase, String fileName,
        W3CTestFinder toAdd) {
        tbase = testBase;
        url = fileName;
        toAddTests = toAdd;
    }

    public void run() throws ConfigurationException {
        try {
            parseSuite(DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(url));
        } catch (Throwable e) {
            new ConfigurationException("Parser Error:" + e);
        }
    }

    private void parseSuite(Document doc) {
        doc.normalize();
        Element root = doc.getDocumentElement();
        if (!root.getTagName().equals("TESTSUITE")) {
            throw new RuntimeException("Root Element is not TESTSUITE:"
                + root.getTagName());
        }
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE
                && node.getNodeName().equals("TESTCASES")) {
                parseTestCase((Element)node, "");
            }
        }
    }

    private void parseTestCase(Element tc, String base) {
        String str = tc.getAttribute("xml:base");
        if (str.indexOf("file:///") == -1) {
            base += str;
        }
        NodeList nodes = tc.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE
                && node.getNodeName().equals("TESTCASES")) {
                parseTestCase((Element)node, base);
            } else if (node.getNodeType() == Node.ELEMENT_NODE
                && node.getNodeName().equals("TEST")) {
                parseTest((Element)node, base);
            }
        }
    }

    private void parseTest(Element test, String base) {
        String type = test.getAttribute("TYPE");
        if (type.equals("not-wf"))
            type = "non-well-formed";
        String id = test.getAttribute("ID");
        String uri = test.getAttribute("URI");
        String notes = test.getFirstChild().getNodeValue();
        String namespace = test.getAttribute("NAMESPACE");
        TestIR tst = new TestIR("Runtime", id);
        ArrayList paramVM = new ArrayList();
        ArrayList paramTst = new ArrayList();
        paramTst.add("toRun");
        paramTst.add(toAddTests.xml_test_run_class);
        paramTst.add(base + "/" + uri);
        paramTst.add(type);
        ArrayList param = new ArrayList();
        param.add(paramVM);
        param.add(paramTst);
        tst.setRunnerParam(param);
        toAddTests.addTest(tst);
    }
}
/**
 * msxsdtest (PathToSuiteXMLFile: result-ms-*.htm)
 */

class TestFindermsxsdtest {

    private final String  testSuiteId  = W3CTestFinder.MSXSD_OPT;
    private String        url;
    private String        base;
    private String        subsuiteName;

    private W3CTestFinder toAddTests;

    private Pattern       patternTable = Pattern
                                           .compile("<table[^>]*>(.*?)</table>");
    private Pattern       patternTr    = Pattern.compile("<tr[^>]*>(.*?)</tr>");
    private Pattern       patternTd    = Pattern.compile("<td[^>]*>(.*?)</td>");
    private Pattern       patternHref  = Pattern
                                           .compile("<a\\s*href=\"(.*?)\"\\s*>");
    private Pattern       patternS     = Pattern.compile("S:\\s*(\\d)");
    private Pattern       patternI     = Pattern.compile("I:\\s*(\\d)");

    public TestFindermsxsdtest(String testBase, String fileName,
        W3CTestFinder toAdd) {
        base = testBase;
        url = fileName;
        toAddTests = toAdd;
    }

    public void run() throws ConfigurationException {
        try {
            String text = load(base + "/" + url);
            parseSuite(removeBU(text));
        } catch (Throwable e) {
            new ConfigurationException("Parser Error:" + e);
        }
    }

    private void parseHead(String cs) throws ConfigurationException {
        Matcher matcher = patternTable.matcher(cs);
        if (!matcher.find()) {
            throw new ConfigurationException(
                "TestFindermsxsdtest. Could not find table!");
        }
        parseHeadTable(matcher.group(1));
    }

    private void parseHeadTable(String table) throws ConfigurationException {
        Matcher matcher = patternTr.matcher(table);
        while (matcher.find()) {
            parseHeadTr(matcher.group(1));
        }
    }

    private void parseHeadTr(String tr) throws ConfigurationException {
        Matcher matcher = patternTd.matcher(tr);
        String[] res = new String[3];
        int index = 0;
        while (matcher.find()) {
            res[index++] = matcher.group(1);
        }
        subsuiteName = res[0];
        matcher = patternHref.matcher(res[2]);
        if (!matcher.find())
            return;
        String htmlfile = matcher.group(1);
        String text = load(base + File.separator + htmlfile);
        parseSuite(removeBU(text));
    }

    private void parseSuite(String text) throws ConfigurationException {
        Matcher matcher = patternTable.matcher(text);
        if (!matcher.find()) {
            throw new ConfigurationException(
                "TestFindermsxsdtest. Could not find table 1!");
        }
        if (!matcher.find()) {
            throw new ConfigurationException(
                "TestFindermsxsdtest. Could not find table 2!");
        }
        parseSuiteTable(matcher.group(1));
    }

    private void parseSuiteTable(String table) throws ConfigurationException {
        Matcher matcher = patternTr.matcher(table);
        if (!matcher.find()) {
            throw new ConfigurationException(
                "TestFindermsxsdtest. Could not find first TR!");
        }
        if (!matcher.find()) {
            throw new ConfigurationException(
                "TestFindermsxsdtest. Could not find second Tr!");
        }
        while (matcher.find()) {
            parseSuiteTr(matcher.group(1));
        }
    }

    private void parseSuiteTr(String tr) throws ConfigurationException {
        Matcher matcher = patternTd.matcher(tr);
        String[] res = new String[5];
        for (int index = 0; index < 5; index++) {
            if (!matcher.find()) {
                throw new ConfigurationException(
                    "TestFindermsxsdtest. Could not find column:" + (index + 1));
            }
            res[index] = matcher.group(1);
        }
        String testId = res[0];
        String notes = res[2];
        String schema = null;
        String doc = null;
        String S = null;
        String I = null;
        matcher = patternHref.matcher(res[3]);
        if (!matcher.find()) {
            throw new ConfigurationException(
                "TestFindermsxsdtest. Schema not found");
        }
        testId = matcher.group(1).substring(0,
            matcher.group(1).length() - ".xsd".length());
        schema = base + "/" + matcher.group(1);
        if (matcher.find()) {
            doc = base + "/" + matcher.group(1);
        } else {
            doc = "";
        }
        matcher = patternS.matcher(res[4]);
        if (!matcher.find()) {
            throw new ConfigurationException(
                "TestFindermsxsdtest. Result for schema not found");
        }
        S = matcher.group(1);
        matcher = patternI.matcher(res[4]);
        if (matcher.find()) {
            I = matcher.group(1);
        }
        TestIR test = new TestIR("Runtime", testId); //
        ArrayList paramVM = new ArrayList();
        ArrayList paramTst = new ArrayList();
        paramTst.add("toRun");
        paramTst.add(toAddTests.msxsdtest_Run_Class);
        if (S.equals("0")) {
            paramTst.add("invalid");
            paramTst.add(schema);
            paramTst.add(doc);
        } else if (I == null) {
            paramTst.add("valid");
            paramTst.add(schema);
            paramTst.add(doc);
        } else if (I.equals("0")) {
            TestIR test1 = new TestIR("Runtime", testId + "_schema");
            ArrayList paramVM1 = new ArrayList();
            ArrayList paramTst1 = new ArrayList();
            paramTst1.add("toRun");
            paramTst1.add(toAddTests.msxsdtest_Run_Class);
            paramTst1.add("valid");
            paramTst1.add(schema);
            //paramTst1.add(null);
            ArrayList param1 = new ArrayList();
            param1.add(paramVM1);
            param1.add(paramTst1);
            test1.setRunnerParam(param1);
            toAddTests.addTest(test1);

            paramTst.add("invalid");
            paramTst.add(schema);
            paramTst.add(doc);
        } else if (I.equals("1")) {
            TestIR test1 = new TestIR("Runtime", testId + "_schema");
            ArrayList paramVM1 = new ArrayList();
            ArrayList paramTst1 = new ArrayList();
            paramTst1.add("toRun");
            paramTst1.add(toAddTests.msxsdtest_Run_Class);
            paramTst1.add("valid");
            paramTst1.add(schema);
            //paramTst1.add(null);
            ArrayList param1 = new ArrayList();
            param1.add(paramVM1);
            param1.add(paramTst1);
            test1.setRunnerParam(param1);
            toAddTests.addTest(test1);

            paramTst.add("valid");
            paramTst.add(schema);
            paramTst.add(doc);
        } else {
            throw new ConfigurationException(
                "TestFindermsxsdtest. Unsupported combination of parameters: "
                    + tr);
        }

        ArrayList param = new ArrayList();
        param.add(paramVM);
        param.add(paramTst);
        test.setRunnerParam(param);
        toAddTests.addTest(test);
    }

    private String removeBU(String src) {
        return src.replaceAll("</?b>", "").replaceAll("</?u>", "");
    }

    private String load(String url) throws ConfigurationException {
        StringBuffer text = new StringBuffer(1024);
        try {
            BufferedReader br = new BufferedReader(new FileReader(url));
            String line = null;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append(" ");
            }
            br.close();
        } catch (Exception e) {
            throw new ConfigurationException(e.toString());
        }
        return text.toString();
    }
}

/**
 * TestFinder for the XSLT-XPath-Tests Testsuite. url parameter for start method
 * should point to nist.xml file in this testsuite.
 */

class TestFinderXSLT_XPath_Tests {

    private final String  testSuiteId = W3CTestFinder.XPATH_OPT;
    private String        url;
    private String        tbase;
    private File          base;

    private W3CTestFinder toAddTests;

    public TestFinderXSLT_XPath_Tests(String testBase, String fileName,
        W3CTestFinder toAdd) {
        base = new File(testBase);
        tbase = testBase;
        url = fileName;
        toAddTests = toAdd;
    }

    public void run() throws ConfigurationException {
        try {
            parseSuite(DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(url));
        } catch (Throwable e) {
            throw new ConfigurationException(
                "TestFinderXSLT_XPath_Tests. Exception: " + e);
        }
    }

    private void parseSuite(Document doc) {
        doc.normalize();
        NodeList nodes = doc.getElementsByTagName("TestCase");
        for (int i = 0; i < nodes.getLength(); i++) {
            parseTestCase((Element)nodes.item(i));
        }
    }

    private void parseTestCase(Element tc) {
        String category = tc.getAttribute("category").replaceAll("-", "") + "/";
        Element purpose = (Element)tc.getElementsByTagName("Purpose").item(0);
        String notes = "";
        if (purpose.getFirstChild() != null) {
            notes = purpose.getFirstChild().getNodeValue();
        }
        Element sourceElem = (Element)tc.getElementsByTagName("Source").item(0);
        String source = sourceElem.getFirstChild().getNodeValue();
        Element outputElem = (Element)tc.getElementsByTagName("output-file")
            .item(0);
        String output = outputElem.getFirstChild().getNodeValue();

        //Bug fixing!!!!!
        if (source.equals("expression/expression016"))
            source = "expression016";
        // END

        String sourceXLS = category + source + ".xsl";
        if (!new File(base, sourceXLS).exists()) {
            throw new RuntimeException("File Not Found:"
                + new File(base, category + sourceXLS));
        }
        String sourceDOC = category + source + ".xml";
        if (!new File(base, sourceDOC).exists()) {
            sourceDOC = category + "stdxmlFile.xml";
            if (!new File(base, sourceDOC).exists()) {
                throw new RuntimeException("File Not Found:"
                    + new File(base, category + sourceDOC));
            }
        }
        String resultDOC = category + "output/" + source + ".out";
        if (!new File(base, resultDOC).exists()) {
            resultDOC = null;
        }
        TestIR tst = new TestIR("Runtime", testSuiteId + "/"
            + sourceDOC.substring(0, sourceDOC.length() - ".xml".length()));
        ArrayList paramVM = new ArrayList();
        ArrayList paramTst = new ArrayList();
        paramTst.add("toRun");
        paramTst.add(toAddTests.xml_test_run_class);
        paramTst.add(tbase + "/" + sourceXLS);
        paramTst.add(tbase + "/" + sourceDOC);
        paramTst.add(resultDOC == null ? "invalidStylesheet" : "valid");
        paramTst.add(tbase + "/" + resultDOC);
        ArrayList param = new ArrayList();
        param.add(paramVM);
        param.add(paramTst);
        tst.setRunnerParam(param);
        toAddTests.addTest(tst);
    }
}

