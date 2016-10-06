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
 * @version $Revision: 1.5 $
 */
package org.apache.harmony.harness.ReportTool;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
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
import org.apache.harmony.harness.InternalTHLogger;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.Util;
import org.apache.harmony.harness.XMLParser;

public class ConvertMavenJUtoTHR extends ConvertAntJUtoTHR {

    String  tcListFileName  = "";
    String  execLogFileName = "";
    HashMap crashedTCMsg    = new HashMap();
    HashMap mapTCtoTest     = new HashMap();

    protected boolean parseParam(String[] params) {
        String helpMsg = "Convert results of unit test run by Maven runner to thr.\nUsage:\n"
            + "-testcaselist <file> - file with list of test cases which results should be reported (one name for line)\n"
            + "-execlog <file> - file with execution log to find possible crash messages\n"
            + "Also options of ConvertAntJUtoTHR script are supported as\n"
            + "ConvertMavenJUtoTHR [-testcaselist,-execlog] [-calctestcaseastest, etc]";
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-testcaselist".equalsIgnoreCase(params[i])) {
                    tcListFileName = params[++i];
                    if (!Util.checkExistFile(tcListFileName)) {
                        log("Incorrect name for 'testcaselist' option: no such file. testcaselist is "
                            + tcListFileName);
                        System.exit(0);
                    }
                } else if ("-execlog".equalsIgnoreCase(params[i])) {
                    execLogFileName = params[++i];
                    if (!Util.checkExistFile(execLogFileName)) {
                        log("Incorrect name for 'execlog' option: no such file. execlog is "
                            + execLogFileName);
                        System.exit(0);
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
            Finder tf = new MavenResFinder(inDirName);
            cfgM.setTestResultRoot(outDirName);
            if (subSuites == null) {
                subSuites = new String[1];
                subSuites[0] = "";
            }
            int cycleCnt = 0;
            TResIR res = null;
            for (int ci = 0; ci < subSuites.length; ci++) {
                if (subSuites[ci] != null && subSuites[ci].length() > 0) {
                    cycleCnt = tf.find(subSuites[ci]);
                } else {
                    cycleCnt = tf.find();
                }
                for (int i = 0; i < cycleCnt; i++) {
                    res = (TResIR)tf.getNext();
                    if (store.init(outDirName + Constants.INTERNAL_FILE_SEP
                        + res.getRepFile() + AntResFinder.OUT_SUFFIX)) {
                        store.add(res);
                        store.close();
                    }
                }
            }
            testCrashedTC(res);
            rep.genReport();
        } catch (Throwable t) {
            log("Unexpected exception: " + t);
            t.printStackTrace();
            return -1;
        }
        return 0;
    }

    private void testCrashedTC(TResIR example) {
        ArrayList expectedTC = readTC(tcListFileName);
        ArrayList crashedTests = readLog(execLogFileName);
        for (int i = 0; i < crashedTests.size(); i++) {
            if (mapTCtoTest.containsKey(crashedTests.get(i))) {
                genCrashedTRes((String)crashedTests.get(i), expectedTC,
                    example);
            }
        }
    }

    //generate results for crashed tests and add it to report
    private void genCrashedTRes(String testName, ArrayList expectedTC,
        TResIR res) {
        if (res == null) {
            res = new TResIR(testName);
        }
        if (mapTCtoTest.containsKey(testName)) {
            ArrayList arr = (ArrayList)mapTCtoTest.get(testName);
            for (int i = 0; i < arr.size(); i++) {
                res.setTestID((String)arr.get(i));
                res.setOutMsg((String)crashedTCMsg.get(testName));
                res.setExecCmd((String)null);
                res.setExecStat(-1);
                if (store.init(outDirName + Constants.INTERNAL_FILE_SEP
                    + res.getRepFile() + AntResFinder.OUT_SUFFIX)) {
                    store.add(res);
                    store.close();
                }
            }
        }
    }

    // return list of crashed tests and fill the crashedTCMsg map
    private ArrayList readLog(String logFileName) {
        ArrayList toRet = new ArrayList();
        File f = new File(logFileName);
        if (f != null && f.exists() && f.isFile()) {
            BufferedReader r = null;
            try {
                r = new BufferedReader(new FileReader(f));
                String tmp = "";
                while (r.ready()) {
                    String prev = tmp;
                    tmp = r.readLine().trim();
                    if (tmp.indexOf("crash") != -1
                        || tmp.indexOf("Crash") != -1) {
                        if (prev.indexOf("Running") != -1) {
                            String testName = prev.substring(
                                prev.indexOf("Running") + "Running".length())
                                .trim();
                            if (testName.indexOf('.') != -1) {
                                testName = testName.replace('.', Constants.INTERNAL_FILE_SEP_C);
                            }
                            toRet.add(testName);
                            String msg = tmp;
                            while (r.ready()) {
                                tmp = r.readLine().trim();
                                if (tmp.indexOf("Running") != -1) {
                                    crashedTCMsg.put(testName, msg);
                                    break;
                                } else {
                                    msg = msg + "\n" + tmp;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log("Unexpected exception while parse input log file: " + e);
            } finally {
                if (r != null) {
                    try {
                        r.close();
                    } catch (Exception e) {
                        log("Unexpected exception while close input log file: "
                            + e);
                    }
                }
            }
        }
        return toRet;
    }

    // return list of expected test cases and fill the mapTCtoTest map
    private ArrayList readTC(String listFileName) {
        ArrayList toRet = new ArrayList();
        File f = new File(listFileName);
        if (f != null && f.exists() && f.isFile()) {
            BufferedReader r = null;
            try {
                r = new BufferedReader(new FileReader(f));
                while (r.ready()) {
                    String tmp = r.readLine().trim();
                    if (tmp.indexOf(File.separatorChar) != -1) {
                        tmp = tmp.replace(File.separatorChar,
                            Constants.INTERNAL_FILE_SEP_C);
                    }
                    toRet.add(tmp);
                    if (tmp.indexOf(Constants.INTERNAL_FILE_SEP) != -1) {
                        String testName = tmp.substring(0, tmp
                            .lastIndexOf(Constants.INTERNAL_FILE_SEP));
                        ArrayList arr;
                        if (mapTCtoTest.containsKey(testName)) {
                            arr = (ArrayList)mapTCtoTest.get(testName);
                        } else {
                            arr = new ArrayList();
                        }
                        arr.add(tmp);
                        mapTCtoTest.put(testName, arr);
                    }
                }
            } catch (Exception e) {
                log("Unexpected exception while parse input file: " + e);
            } finally {
                if (r != null) {
                    try {
                        r.close();
                    } catch (Exception e) {
                        log("Unexpected exception while close input file: " + e);
                    }
                }
            }
        }
        return toRet;
    }

    public static void main(String[] args) {
        System.exit(new ConvertMavenJUtoTHR().run(args));
    }
}

class MavenResFinder extends AntResFinder {

    public static final String IN_SUFFIX  = ".xml";
    public static final String OUT_SUFFIX = ".thr";
    private MavenResParser     rp         = new MavenResParser();
    protected String           tsRoot     = ".";

    public MavenResFinder(String root) {
        super(root);
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
                    "MavenResFinder. Can not read the directory " + root);
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
                ConvertAntJUtoTHR.log(2,
                    "MavenResFinder. Add to results list: " + root.getName()
                        + "\nTotal tests number " + parsedItems.size());
            } catch (ParserException pe) {
                ConvertAntJUtoTHR.log(2,
                    "MavenResFinder. The file has invalid format: "
                        + root.getPath());
            }
        } else {
            ConvertAntJUtoTHR.log(2, "MavenResFinder. The file "
                + root.getName() + " was ignored due to extension.");
        }
    }
}

class MavenResParser {

    private XMLParser parser;
    private HashMap   resIRs = new HashMap(300);

    public MavenResParser() {
        try {
            parser = new XMLParser(false);
            parser.setErrorHandler(new ThrErrorHandler());
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    protected HashMap parser(File in) throws ParserException {
        MavenResHandler handler = new MavenResHandler(resIRs);
        try {
            parser.parsefile(handler, in);
        } catch (ConfigurationException e) {
            throw new ParserException("MaventResParser.\tCann't parse a data:"
                + e);
        }
        return resIRs;
    }

    class ThrErrorHandler implements ErrorHandler {
        public void warning(SAXParseException exception) throws SAXException {
            ConvertAntJUtoTHR.log(1,
                "MaventResParser.\tThrErrorHandler. Warning: " + exception);
        }

        public void error(SAXParseException exception) throws SAXException {
            ConvertAntJUtoTHR.log(1,
                "MaventResParser.\tThrErrorHandler. Error: " + exception);
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            ConvertAntJUtoTHR
                .log("MaventResParser.\tThrErrorHandler. fatalError: "
                    + exception);
            //throw exception;
        }
    }
}

class MavenResHandler extends DefaultHandler {

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
    private String            testSuite  = "";
    private String            testedOS;
    private String            testedPlatform;
    private String            testedVM;
    private String            testExecTime;
    private String            testCmd;
    private String            err_msg;
    private String            err_type;

    private String            tstDir;
    private String            projDir;
    private String            tstPrefix  = "";

    public void characters(char[] character, int start, int length)
        throws SAXException {
        context.write(character, start, length);
    }

    public MavenResHandler(HashMap curResIRs) {
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
        if (localName.equals("testsuite")) {
            testedOS = "";
            testedPlatform = "";
            testedVM = "";
            testExecTime = "";
            testSuite = attrs.getValue("name");
        }
        if (localName.equals("property")) {
            keyName = attrs.getValue("name");
            keyValue = attrs.getValue("value");
        }
        if (localName.equals("testcase")) {
            keyName = testSuite;
            keyValue = attrs.getValue("name");
            tc_time = attrs.getValue("time");
            err_msg = "";
            err_type = "";
            tc_failed = false;
            tc_error = false;
            if (tstDir != null && projDir != null
                && projDir.length() < tstDir.length()) {
                tstPrefix = tstDir.substring(projDir.length()) + ".";
            }
            if (ConvertAntJUtoTHR.calctestcaseastest) {
                testName = tstPrefix + keyName + "." + keyValue;
                if (hm_tests.containsKey(testName)) {
                    int index = 1;
                    String tmp = testName + "_" + index;
                    while (hm_tests.containsKey(tmp)) {
                        tmp = testName + "_" + (++index);
                    }
                    testName = tmp;
                }
            } else {
                testName = tstPrefix + keyName;
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
            err_msg = "";
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
                    } else if (keyName.equalsIgnoreCase("user.dir")) {
                        tstDir = keyValue;
                    } else if (keyName.equalsIgnoreCase("project.root")) {
                        projDir = keyValue;
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
        if (localName.equals("testsuite")) {
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