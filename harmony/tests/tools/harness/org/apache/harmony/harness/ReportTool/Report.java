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
 * @version $Revision: 1.19 $
 */
package org.apache.harmony.harness.ReportTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Finder;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.DefaultConfigSetup;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.Util;
import org.apache.harmony.harness.InternalTHLogger;
import org.apache.harmony.harness.plugins.Reporter;

public class Report extends Main {

    public static final String XML_ONLY       = "xml";
    public static final String HTML_ONLY      = "html";
    public static final String SPEC_VIEW      = "specview";

    protected HashMap          excludeTestRes = new HashMap();

    // when reportOnly == true, no test run. Generate report only.
    boolean                    reportOnly     = false;

    String                     reportMode     = XML_ONLY;
    String                     efList         = null;
    String                     exclTestList   = "";

    Finder                     resf;

    protected boolean parseParam(String[] params) {
        String helpMsg = "Create the test run report.\nUsage:\n"
            + "-cfp <path>\t\t\tpath to the configuration files\n"
            + "-exclude \"name1 ...\"\n"
            + "-format \"name\"\t\tname of the format (xml or html plain view or specview formats)\n"
            + "-efl <name>\t\t\tpath to bug data (file, for example). Work for specview format only.\n"
            + "Also options of harness are supported";
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-format".equalsIgnoreCase(params[i])) {
                    reportMode = params[++i];
                } else if ("-cfp".equalsIgnoreCase(params[i])) {
                    cfgStorePath = params[++i];
                } else if ("-exclude".equalsIgnoreCase(params[i])) {
                    exclTestList = params[++i];
                } else if ("-efl".equalsIgnoreCase(params[i])) {
                    efList = params[++i];
                } else if ("-help".equalsIgnoreCase(params[i])
                    || "-h".equalsIgnoreCase(params[i])) {
                    System.out.println(helpMsg);
                    System.exit(0);
                } else {
                    super.parseParam(Util.removeFirstElem(params, i));
                    break;
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            System.out.println("Incorrect parameters. Please check.\n"
                + helpMsg);
            return false;
        }
        return true;
    }

    boolean addOneRecordToExclResList(String record) {
        if (record == null || record.length() < 1) {
            return false;
        }
        String data = record.trim();
        if (!data.startsWith(Constants.START_COMMENT)) {
            int extensPosition = data.indexOf(Constants.START_FILE_EXTENSION);
            int mcDelimPosition = data
                .indexOf(Constants.MULTICASE_TEST_CASE_DELIMITER);
            int min = (extensPosition < mcDelimPosition) ? extensPosition
                : mcDelimPosition;
            if (extensPosition != -1 && mcDelimPosition != -1) {
                data = data.substring(0, min);
            } else if (mcDelimPosition != -1) {
                data = data.substring(0, mcDelimPosition);
            } else if (extensPosition != -1) {
                data = data.substring(0, extensPosition);
            }
            excludeTestRes.put(data.replace(Constants.FILE_SEP_C_TO_SUBS,
                Constants.INTERNAL_FILE_SEP_C), "");
        }
        return true;
    }

    boolean fillExclResList(String record) {
        if (Util.checkExistFile(record)) {
            ArrayList tmpStore = Util.readStringsFromFile(record);
            for (int y = 0; y < tmpStore.size(); y++) {
                addOneRecordToExclResList((String)tmpStore.get(y));
            }
        } else {
            addOneRecordToExclResList(record);
        }
        return true;
    }

    boolean createExclResList(String data) {
        String[] elems = Util.stringToArray(data);
        if (elems == null || elems.length < 1) {
            return false;
        }
        for (int i = 0; i < elems.length; i++) {
            fillExclResList(elems[i]);
        }
        return true;
    }

    void generateReport() {
        try {
            createConfigurator();
            cfg.createConfiguration(cfgStorePath);
            setCfgFromCmdline();
            new DefaultConfigSetup().setDefaultValues();
            checkAndUpdateConfig();
            if (reportMode.equalsIgnoreCase(HTML_ONLY)) {
                rep = new ReporterHTML();
            } else if (reportMode.equalsIgnoreCase(SPEC_VIEW)) {
                rep = new ReporterSpec(efList);
            } else {
                rep = new Reporter();
            }
            createStore();
            createResFinder();
            if (exclTestList != null && exclTestList.length() > 0) {
                createExclResList(exclTestList);
            }
            if (subSuites != null && subSuites.length > 0) {
                for (int i = 0; i < subSuites.length; i++) {
                    addResults(rf.find(subSuites[i]));
                }
            } else {
                addResults(rf.find());
            }
            rep.genReport();
            System.out.println("done. See the run report in the directory\n\t"
                + cfgM.getTestResultRoot());
        } catch (Throwable th) {
            System.out.println("Unexpected exception: " + th);
            th.printStackTrace();
        }
    }

    boolean addResults(int num) {
        boolean retVal = false;
        for (int cnt = 0; cnt < num; cnt++) {
            retVal = addResult((TResIR)rf.getNext());
        }
        return retVal;
    }

    /*
     * add test result to execution status list
     */
    boolean addResult(TResIR tRes) {
        try {
            String testID = tRes.getTestID();
            int testRes = tRes.getExecStat();
            //remove the invalid entry or entry that have no the test ID
            if (testID != null && !excludeTestRes.containsKey(testID)) {
                rep
                    .addResult(testID + getStore().getResultExtension(),
                        testRes);
            }
        } catch (Exception e) {
            System.out.println("addResult. Unexpected exception " + e);
        }
        return false;
    }

    public int run(String[] args) {
        curCore = this;
        internalLog = new InternalTHLogger();
        internalLog.init(Level.SEVERE);
        parseParam(args);
        generateReport();
        return 0;
    }

    public static void main(String[] args) {
        System.exit((new Report().run(args)));
    }
}