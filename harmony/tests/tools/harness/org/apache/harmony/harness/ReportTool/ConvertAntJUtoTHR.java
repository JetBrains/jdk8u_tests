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
 * @version $Revision: 1.9 $
 */
package org.apache.harmony.harness.ReportTool;

import java.io.CharArrayWriter;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.DefaultConfigSetup;
import org.apache.harmony.harness.Finder;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.Util;
import org.apache.harmony.harness.XMLParser;
import org.apache.harmony.harness.InternalTHLogger;
import org.apache.harmony.harness.plugins.DirFinder;

public class ConvertAntJUtoTHR extends Main {

    static boolean calctestcaseastest = false;
    static int     passed_val         = 104;
    static int     failed_val         = 105;
    static int     error_val          = 106;

    static int     curLogLevel        = 3;

    String         inDirName;
    String         outDirName;

    protected boolean parseParam(String[] params) {
        String helpMsg = "Convert results of unit test run by ant runner to thr.\nUsage:\n"
            + "-subs \"name1 name2...\"\t\tsubsuites to find the tests (from the test suite root)\n"
            + "-indir name\t\t\tpath to the result of unit test into ant-runner format\n"
            + "-outdir name\t\t\tpath to the directory to store results\n"
            + "-logl value\t\t\tlogging level 0-4 (default is 3)\n"
            + "-calctestcaseastest true/false (false by default)";
        cfgStorePath = null;
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-indir".equalsIgnoreCase(params[i])) {
                    inDirName = params[++i];
                    if (!Util.checkExistDir(inDirName)) {
                        log("Incorrect name for 'indir' option: no such directory. indir is "
                            + inDirName);
                        System.exit(0);
                    }
                } else if ("-outdir".equalsIgnoreCase(params[i])) {
                    outDirName = params[++i];
                    if (!Util.checkExistDir(outDirName)) {
                        try {
                            (new File(outDirName)).mkdirs();
                        } catch (Exception e) {
                            log("Incorrect name for 'outdir' option: can not create such directory. outdir is "
                                + outDirName);
                            System.exit(0);
                        }
                    }
                    log("results directory: " + outDirName);
                } else if ("-cfp".equalsIgnoreCase(params[i])) {
                    cfgStorePath = params[++i];
                } else if ("-calctestcaseastest".equalsIgnoreCase(params[i])) {
                    if (params[++i].toLowerCase().indexOf("true") != -1) {
                        calctestcaseastest = true;
                    }
                } else if ("-subs".equalsIgnoreCase(params[i])) {
                    subSuites = Util.stringToArray(params[++i]);
                } else if ("-logl".equalsIgnoreCase(params[i])) {
                    try {
                        curLogLevel = Integer.parseInt(params[++i]);
                        if (curLogLevel < 0 || curLogLevel > 4) {
                            curLogLevel = 2;
                        }
                    } catch (Exception e) {
                        log("Can't parse log level option. Use default value: "
                            + curLogLevel);
                    }
                } else if ("-h".equalsIgnoreCase(params[i])
                    || "-help".equalsIgnoreCase(params[i])) {
                    log(helpMsg);
                    System.exit(0);
                } else {
                    super.parseParam(Util.removeFirstElem(params, i));
                    break;
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            log("Incorrect parameters. Please check.\n" + helpMsg);
            return false;
        }
        return true;
    }

    static void log(String msg) {
        System.out.println(msg);
    }

    static void log(int level, String msg) {
        if (level >= curLogLevel) {
            log(msg);
        }
    }

    public int run(String[] args) {
        try {
            curCore = this;
            parseParam(args);
            internalLog = new InternalTHLogger();
            internalLog.init(Level.SEVERE);
            if (cfgStorePath == null || cfgStorePath.length() == 0) {
                cfgM = new ConfigIR();
            } else {
                createConfigurator();
                cfg.createConfiguration(cfgStorePath);
            }
            setCfgFromCmdline();
            new DefaultConfigSetup().setDefaultValues();
            checkAndUpdateConfig();
            createReporter();
            createStore();
            if (cfgM.getRepPassed() != null && cfgM.getRepPassed().length > 0) {
                passed_val = cfgM.getRepPassed()[0];
            }
            if (cfgM.getRepError() != null && cfgM.getRepError().length > 0) {
                error_val = cfgM.getRepError()[0];
            }
            if (cfgM.getRepFailed() != null && cfgM.getRepFailed().length > 0) {
                failed_val = cfgM.getRepFailed()[0];
            }
            Finder tf = new AntResFinder(inDirName);
            cfgM.setTestResultRoot(outDirName);
            if (subSuites == null) {
                subSuites = new String[1];
                subSuites[0] = "";
            }
            int cycleCnt = 0;
            for (int ci = 0; ci < subSuites.length; ci++) {
                if (subSuites[ci] != null && subSuites[ci].length() > 0) {
                    cycleCnt = tf.find(subSuites[ci]);
                } else {
                    cycleCnt = tf.find();
                }
                for (int i = 0; i < cycleCnt; i++) {
                    TResIR res = (TResIR)tf.getNext();
                    if (store.init(outDirName + Constants.INTERNAL_FILE_SEP
                        + res.getRepFile() + AntResFinder.OUT_SUFFIX)) {
                        store.add(res);
                        store.close();
                    }
                }
            }
            rep.genReport();
        } catch (Throwable t) {
            log("Unexpected exception: " + t);
            t.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static void main(String[] args) {
        System.exit(new ConvertAntJUtoTHR().run(args));
    }
}

class AntResFinder extends DirFinder {

    public static final String IN_SUFFIX  = ".xml";
    public static final String OUT_SUFFIX = ".thr";
    private AntResParser       rp         = new AntResParser();
    protected String           tsRoot     = ".";

    public AntResFinder(String root) {
        tsRoot = root;
    }

    public String setFindRoot(String newValue) {
        String retVal = tsRoot;
        tsRoot = newValue;
        return retVal;
    }

    public int find(String subsuite, String[] mask)
        throws ConfigurationException {
        super.tsRoot = tsRoot;
        return super.find(subsuite, mask);
    }

    protected void findFiles(File root, String[] mask) {
        File[] tmpStore;
        if (root == null) {
            return;
        }
        if (root.isDirectory()) {
            tmpStore = root.listFiles();
            if (tmpStore != null) {
                for (int i = 0; i < tmpStore.length; i++) {
                    findFiles(tmpStore[i], mask);
                }
            } else {
                ConvertAntJUtoTHR.log(3,
                    "AntResFinder. Can not read the directory " + root);
            }
        }
        if (root.getName().endsWith(AntResFinder.IN_SUFFIX)) {
            if (mask != null && mask.length > 0) {
                for (int i = 0; i < mask.length; i++) {
                    if (root.getPath().startsWith(mask[i])) {
                        return;
                    }
                }
            }
            try {
                HashMap hm = rp.parser(root);
                int cnt = hm.size();
                Iterator it = hm.keySet().iterator();
                for (int i = 0; i < cnt; i++) {
                    parsedItems.add(hm.get(it.next()));
                }
                ConvertAntJUtoTHR.log(2, "AntResFinder. Add to results list: "
                    + root.getName() + "\nTotal tests number "
                    + parsedItems.size());
            } catch (ParserException pe) {
                ConvertAntJUtoTHR.log(2,
                    "AntResFinder. The file has invalid format: "
                        + root.getPath());
            }
        } else {
            ConvertAntJUtoTHR.log(2, "AntResFinder. The file " + root.getName()
                + " was ignored due to extension.");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#check(java.lang.String)
     */
    public boolean check(String testName) {
        File fl;
        if (testName == null) {
            return false;
        }
        try {
            if (!testName.endsWith(IN_SUFFIX)) {
                return false;
            }
            fl = new File(testName);
            if (fl.isFile() && rp.parser(fl) != null) {
                return true;
            }
        } catch (Exception e) {
            try {
                fl = new File(tsRoot + File.separator + testName);
                if (fl.isFile() && rp.parser(fl) != null) {
                    return true;
                }
            } catch (Exception e1) {
                System.err.println("unexpected exception: " + e1);
            }
        }
        return false;
    }
}

class AntResParser {

    private XMLParser parser;
    private HashMap   resIRs = new HashMap(300);

    public AntResParser() {
        try {
            parser = new XMLParser(false);
            parser.setErrorHandler(new ThrErrorHandler());
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    protected HashMap parser(File in) throws ParserException {
        AntResHandler handler = new AntResHandler(resIRs);
        try {
            parser.parsefile(handler, in);
        } catch (ConfigurationException e) {
            throw new ParserException("AntResParser.\tCann't parse a data:" + e);
        }
        return resIRs;
    }

    class ThrErrorHandler implements ErrorHandler {
        public void warning(SAXParseException exception) throws SAXException {
            ConvertAntJUtoTHR.log(1,
                "AntResParser.\tThrErrorHandler. Warning: " + exception);
        }

        public void error(SAXParseException exception) throws SAXException {
            ConvertAntJUtoTHR.log(1, "AntResParser.\tThrErrorHandler. Error: "
                + exception);
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            ConvertAntJUtoTHR
                .log("AntResParser.\tThrErrorHandler. fatalError: " + exception);
            //throw exception;
        }
    }
}

class AntResHandler extends DefaultHandler {

    protected HashMap         theResIRs;

    protected String          keyName    = "";
    protected String          testName   = "";
    protected String          keyValue   = "";
    protected String          tc_time    = "";
    protected HashMap         properties = new HashMap();
    protected CharArrayWriter context    = new CharArrayWriter();

    private HashMap           hm_tests   = new HashMap();
    private boolean           tc_failed  = false;
    private boolean           tc_error   = false;
    private String            testedOS;
    private String            testedPlatform;
    private String            testedVM;
    private String            testExecTime;
    private String            testCmd;
    private String            err_msg;
    private String            err_type;

    public void characters(char[] character, int start, int length)
        throws SAXException {
        context.write(character, start, length);
    }

    public AntResHandler(HashMap curResIRs) {
        theResIRs = curResIRs;
    }

    public void startDocument() throws SAXException {
        properties.clear();
        theResIRs.clear();
        hm_tests.clear();
        ConvertAntJUtoTHR.log(1, "startDocument: " + this.getClass().getName());
    }

    public void endDocument() throws SAXException {
        ConvertAntJUtoTHR.log(1, "endDocument: " + this.getClass().getName());
    }

    public void startElement(String uri, String localName, String qName,
        Attributes attrs) throws SAXException {
        context.reset();
        if (localName.equals("testsuites")) {
            properties.clear();
            theResIRs.clear();
            hm_tests.clear();
        }
        if (localName.equals("testsuite")) {
            testedOS = "";
            testedPlatform = "";
            testedVM = "";
            testExecTime = "";
        }
        if (localName.equals("property")) {
            keyName = attrs.getValue("name");
            keyValue = attrs.getValue("value");
        }
        if (localName.equals("testcase")) {
            keyName = attrs.getValue("classname");
            keyValue = attrs.getValue("name");
            tc_time = attrs.getValue("time");
            err_msg = "";
            err_type = "";
            tc_failed = false;
            tc_error = false;
            if (ConvertAntJUtoTHR.calctestcaseastest) {
                testName = keyName + "." + keyValue;
            } else {
                testName = keyName;
            }
            if (!hm_tests.containsKey(testName)) {
                hm_tests.put(testName, new TestData());
            }
        }
        if (localName.equals("error")) {
            err_msg = attrs.getValue("message");
            err_type = attrs.getValue("type");
        }
        if (localName.equals("failure")) {
            err_msg = attrs.getValue("message");
            err_type = attrs.getValue("type");
        }
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        if (localName.equals("property")) {
            if (keyName != null) {
                if (keyName != null) {
                    properties.put(keyName, keyValue);
                    if (keyName.equalsIgnoreCase("os.name")) {
                        testedOS = keyValue;
                    } else if (keyName.equalsIgnoreCase("os.arch")) {
                        if (testedPlatform.length() < 1) {
                            testedPlatform = keyValue;
                        } else {
                            testedPlatform = keyValue + " " + testedPlatform;
                        }
                    } else if (keyName.equalsIgnoreCase("os.real.arch")) {
                        if (testedPlatform.length() < 1) {
                            testedPlatform = keyValue;
                        } else {
                            testedPlatform = testedPlatform + " " + keyValue;
                        }
                    } else if (keyName.equalsIgnoreCase("java.vm.name")) {
                        testedVM = keyValue;
                    } else if (keyName.equalsIgnoreCase("eclipse.commands")) {
                        testCmd = keyValue;
                    } else if (keyName.equalsIgnoreCase("eclipse.startTime")) {
                        try {
                            testExecTime = new Date(Long.parseLong(keyValue))
                                .toString();
                        } catch (Exception e) {
                            ConvertAntJUtoTHR.log(1,
                                "Cann't parse the run's time: " + e);
                        }
                    }
                }
            }
        }
        if (localName.equals("testcase")) {
            TestData td = (TestData)hm_tests.get(testName);
            td.tc_total++;
            try {
                td.timeSt.add(new Long(
                    (long)((new Float(tc_time)).floatValue() * 1000)));
            } catch (Exception e) {
                ConvertAntJUtoTHR.log(1, "Cann't parse the testcase run time: "
                    + e);
            }
            td.tc_out = td.tc_out + "\nTestcase: @" + keyValue
                + "\tresult is: ";
            if (tc_error) {
                td.tc_out = td.tc_out + "ERROR(" + ConvertAntJUtoTHR.error_val
                    + ")\n" + td.tc_err_msg;
            } else if (tc_failed) {
                td.tc_out = td.tc_out + "FAILED("
                    + ConvertAntJUtoTHR.failed_val + ")\n" + td.tc_err_msg;
            } else {
                td.tc_out = td.tc_out + "PASSED("
                    + ConvertAntJUtoTHR.passed_val + ")\n" + td.tc_err_msg;
            }
            td.tc_err_msg = "";
            hm_tests.put(testName, td);
        }
        if (localName.equals("error")) {
            TestData td = (TestData)hm_tests.get(testName);
            td.tc_error++;
            tc_error = true;
            td.tc_err_msg = td.tc_err_msg + "\n" + keyValue + "(" + keyName
                + "): " + context.toString().trim() + "\n";
            hm_tests.put(testName, td);
        }
        if (localName.equals("failure")) {
            TestData td = (TestData)hm_tests.get(testName);
            td.tc_failed++;
            tc_failed = true;
            td.tc_err_msg = td.tc_err_msg + "\n" + keyValue + "(" + keyName
                + "): " + context.toString().trim() + "\n";
            hm_tests.put(testName, td);
        }
        if (localName.equals("testsuites")) {
            int cnt = hm_tests.size();
            Iterator it = hm_tests.keySet().iterator();
            String platform_real_name = System.getProperty("os.real.arch");
            if (testedPlatform != null) {
                if (platform_real_name != null
                    && platform_real_name.length() > 0
                    && testedPlatform.indexOf(platform_real_name) == -1) {
                    testedPlatform = testedPlatform + " (" + platform_real_name
                        + ")";
                }
                String netAdress = "";
                try {
                    netAdress = ", host "
                        + InetAddress.getLocalHost().toString();
                } catch (Exception e) {
                    //do nothing
                }
                testedPlatform = testedPlatform + netAdress;
            }
            for (int i = 0; i < cnt; i++) {
                String testID = it.next().toString();
                TResIR res;
                if (testID.indexOf('.') == -1) {
                    res = new TResIR(testedOS, testedPlatform, testedVM, testID);
                } else {
                    res = new TResIR(testedOS, testedPlatform, testedVM, testID
                        .replace('.', Constants.INTERNAL_FILE_SEP_C));
                }
                TestData td = (TestData)hm_tests.get(testID);
                String addMsg = "";
                String infoMsg = "Test: " + testID + "\n\ttest case(s) to run "
                    + td.tc_total + "\n";
                if (td.tc_error == 0 && td.tc_failed == 0) {
                    addMsg = addMsg + "\nOK (" + td.tc_total + " tests)";
                    res.setExecStat(ConvertAntJUtoTHR.passed_val);
                } else {
                    addMsg = "\n\nFAILURES!!!\nTests run: " + td.tc_total
                        + ",  Failures: " + td.tc_failed + ",  Errors: "
                        + td.tc_error;
                    if (td.tc_error != 0) {
                        res.setExecStat(ConvertAntJUtoTHR.error_val);
                    } else {
                        res.setExecStat(ConvertAntJUtoTHR.failed_val);
                    }
                }
                res.setOutMsg(infoMsg + td.tc_out + addMsg);
                res.setExecCmd(testCmd);
                res.setExecTime(td.timeSt);
                res.setDate(testExecTime);
                theResIRs.put(testID, res);
            }
        }
    }

    class TestData {
        int       tc_total   = 0;
        int       tc_failed  = 0;
        int       tc_error   = 0;
        String    tc_out     = "";
        String    tc_err_msg = "";
        ArrayList timeSt     = new ArrayList();
    }
}