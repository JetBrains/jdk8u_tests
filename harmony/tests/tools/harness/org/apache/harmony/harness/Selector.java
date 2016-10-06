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
 * @version $Revision: 1.24 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;

public class Selector {

    private final String        classID              = "Selector";

    /* constant that point to add test case to include list */
    public static final boolean INCLUDE              = true;

    /* constant that point to add test case to include list */
    public static final boolean EXCLUDE              = false;

    /* the symbol of end of test name in the include file (or the end of line) */
    public static final String  END_TEST_SYMBOL      = " ";

    /* the time expression field delimiter */
    public static final String  TIME_FIELD_DELIMITER = "-";

    /* the time expression field delimiter */
    public static final String  TIME_MODE_DELIMITER  = ":";

    /** the status for test that was not run in previous run */
    public static final int     NEW_TEST             = 101;

    /*
     * the time expression field: 'create' symbols. Use the date pointed in the
     * 'create' field
     */
    public static final String  TIME_CREATE_SYMBOL   = "c";

    /*
     * the time expression field: 'modify' symbols. Use the date pointed in the
     * last 'modification date' field
     */
    public static final String  TIME_MODIFY_SYMBOL   = "m";

    /*
     * the time expression field: 'after' symbols. Select all tests that updated
     * after the pointed date
     */
    public static final String  AFTER_SYMBOL         = "a";

    /*
     * the time expression field: 'before' symbols. Select all tests that
     * updated before the pointed date
     */
    public static final String  BEFORE_SYMBOL        = "b";

    /* the constant to multiply the year field for time selection */
    public static final int     YEAR_MULTIPLIER      = 366;

    /* the constant to multiply the month field for time selection */
    public static final int     MONTH_MULTIPLIER     = 31;

    /* the constant to multiply the day field for time selection */
    public static final int     DAY_MULTIPLIER       = 1;

    protected boolean           excludePredefinedNames = true;
    protected String[]          predefinedNames        = { "/~", "/#", "/.#",
        "/%", "/._", "/CVS/", "/.cvsignore/", "/SCCS/", "/vssver.scc/",
        "/.svn/", "/.DS_Store/"                       };

    /* the list of tests that should be executed in the current run */
    protected Main              curCore              = Main.getCurCore();

    /* the list of tests that should be executed in the current run */
    protected ExecListIR        toRunList            = curCore.getExecList();

    /* the list of test results for current suite */
    protected ExecStatusIR      resList              = curCore.getExecStat();

    /* the list of all tests in the suite to display in gui */
    protected TestSuiteIR       ts                   = curCore.getTstSuite();

    /*
     * the test suite test and result finders. The result finder will work
     * if the selection by status of previous run is configured
     */
    protected Finder            tf                   = curCore.getTestFinder();
    protected Finder            rf                   = curCore.getResFinder();

    /* the current configuration */
    protected ConfigIR          cfg                  = curCore.getConfigIR();

    /* the output stream */
    protected Logging           log                  = curCore
                                                         .getInternalLogger();

    /*
     * the current storage of results: all deselected tests will be reported as
     * skipped with the specified reason
     */
    protected Storage           store                = curCore.getStore();

    //the directory prefix to store result
    protected String            testResRoot          = cfg.getTestResultRoot();
    // the result object for store results
    protected TResIR            result               = new TResIR(
                                                         System
                                                             .getProperty("os.name"),
                                                         System
                                                             .getProperty("os.arch"),
                                                         System
                                                             .getProperty("java.vm.name"),
                                                         null);

    /*
     * the list of tests that should be excluded from current run as single case
     * (whole)
     */
    protected ArrayList         excludeTestNames;

    /*
     * hash codes of strings for selections: list of tests that should be
     * included into run for future optimization
     */
    protected int[][]           selPosAttr;

    /*
     * hash codes of strings for selections: list of tests that should be
     * excluded from run for future optimization
     */
    protected int[][]           selNegAttr;

    /*
     * strings for selections: list of tests that should be included into run
     * String[0] = SelKeywords(); String[1] = SelAuthor(); String[2] =
     * SelModifAuthors(); String[3] = SelResources(); String[4] = SelRunners();
     */
    protected String[][]        includeOpt;

    /*
     * strings for selections: list of tests that should be excluded from run
     * String[0] = SelKeywords(); String[1] = SelAuthor(); String[2] =
     * SelModifAuthors(); String[3] = SelResources(); String[4] = SelRunners();
     */
    protected String[][]        excludeOpt;

    /*
     * internal presentation for date of test's creation/ modification for
     * selection has a 3 field (this value should be updated if format will
     * change) Note, 3 is a minimal value. The IndexOutOfBoundsException will be
     * thrown if this value less than 3
     */
    public static final int     DATE_STRUCTURE_SIZE  = 3;
    /*
     * internal presentation for date of test's creation/ modification for
     * selection each record has a 3 field: [n + 0] = mode 1 - test with date
     * number less than this field will be excluded 2 - test with date number
     * greater than this field will be excluded 3 - test with date number equals
     * to this field will be excluded [n + 1] = time field 1 - work with
     * creation field 2 - work with last modification field [n + 2] = time
     * number for selection
     */
    protected int[]             selDate;

    /*
     * internal presentation for execution status for selection: exclude Note,
     * include means that all other should be excluded
     */
    protected int[]             execStatExcl;

    /*
     * default constructor: parse the config and create the arrays for selection
     */
    public Selector() {
        setDefaultExclude();
        createSortArr();
        createDateNumber();
        createExcStat();
    }

    /*
     * setup default exclude option: true/ false and list of substring Section
     * in the configuration file for 'selector' plugin can looks like <parameter
     * name="excludeDefault"> <value name="use">true </value> <value
     * name="substring">/.svn </value> <value name="substring">/CVS </value>
     * </parameter>
     */
    void setDefaultExclude() {
        HashMap params;
        HashMap tmpH;
        ArrayList tmpA;

        tmpA = (ArrayList)cfg.getPlugins().get("Selector");
        if (tmpA == null) {
            return;
        }
        tmpH = (HashMap)(tmpA).get(1);
        if (tmpH == null) {
            return;
        }
        params = (HashMap)tmpH.get("excludeDefault");
        if (params != null) {
            tmpA = (ArrayList)params.get("use");
            if (tmpA != null && tmpA.size() > 0) {
                if ("false".equalsIgnoreCase(tmpA.get(0).toString())) {
                    excludePredefinedNames = false;
                }
            }
            tmpA = (ArrayList)params.get("substring");
            if (tmpA != null && tmpA.size() > 0) {
                predefinedNames = new String[tmpA.size()];
                for (int i = 0; i < tmpA.size(); i++) {
                    predefinedNames[i] = tmpA.get(i).toString();
                }
            }
        }
    }

    /*
     * create general sort array: to include and exclude tests String[0] =
     * SelKeywords(); String[1] = SelAuthor(); String[2] = SelModifAuthors();
     * String[3] = SelResources(); String[4] = SelRunners();
     */
    void createSortArr() {
        String[][] tmpStore = new String[5][];
        int[][] tmpHCode = new int[5][];
        boolean needSelection = false;
        HashMap paramIncl;
        HashMap paramExcl;
        HashMap tmpH;
        ArrayList obj;
        ArrayList tmpA;

        tmpA = (ArrayList)cfg.getPlugins().get("Selector");
        if (tmpA == null) {
            return;
        }
        tmpH = (HashMap)(tmpA).get(1);
        if (tmpH == null) {
            return;
        }
        paramIncl = (HashMap)tmpH.get("include");
        paramExcl = (HashMap)tmpH.get("exclude");

        if (paramIncl != null) {
            obj = (ArrayList)paramIncl.get("Keyword");
            if (obj != null) {
                needSelection = true;
                tmpStore[0] = new String[obj.size()];
                tmpHCode[0] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[0][i] = (String)obj.get(i);
                    tmpHCode[0][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
            obj = (ArrayList)paramIncl.get("Author");
            if (obj != null) {
                needSelection = true;
                tmpStore[1] = new String[obj.size()];
                tmpHCode[1] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[1][i] = (String)obj.get(i);
                    tmpHCode[1][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
            obj = (ArrayList)paramIncl.get("modif-author");
            if (obj != null) {
                needSelection = true;
                tmpStore[2] = new String[obj.size()];
                tmpHCode[2] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[2][i] = (String)obj.get(i);
                    tmpHCode[2][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
            obj = (ArrayList)paramIncl.get("Resource");
            if (obj != null) {
                needSelection = true;
                tmpStore[3] = new String[obj.size()];
                tmpHCode[3] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[3][i] = (String)obj.get(i);
                    tmpHCode[3][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
            obj = (ArrayList)paramIncl.get("RunnerID");
            if (obj != null) {
                needSelection = true;
                tmpStore[4] = new String[obj.size()];
                tmpHCode[4] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[4][i] = (String)obj.get(i);
                    tmpHCode[4][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
        }
        if (needSelection == true) {
            selPosAttr = tmpHCode;
            includeOpt = tmpStore;
        }
        needSelection = false;
        tmpStore = new String[5][];
        tmpHCode = new int[5][];

        if (paramExcl != null) {
            obj = (ArrayList)paramExcl.get("Keyword");
            if (obj != null) {
                needSelection = true;
                tmpStore[0] = new String[obj.size()];
                tmpHCode[0] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[0][i] = (String)obj.get(i);
                    tmpHCode[0][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
            obj = (ArrayList)paramExcl.get("Author");
            if (obj != null) {
                needSelection = true;
                tmpStore[1] = new String[obj.size()];
                tmpHCode[1] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[1][i] = (String)obj.get(i);
                    tmpHCode[1][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
            obj = (ArrayList)paramExcl.get("modif-author");
            if (obj != null) {
                needSelection = true;
                tmpStore[2] = new String[obj.size()];
                tmpHCode[2] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[2][i] = (String)obj.get(i);
                    tmpHCode[2][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
            obj = (ArrayList)paramExcl.get("Resource");
            if (obj != null) {
                needSelection = true;
                tmpStore[3] = new String[obj.size()];
                tmpHCode[3] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[3][i] = (String)obj.get(i);
                    tmpHCode[3][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
            obj = (ArrayList)paramExcl.get("RunnerID");
            if (obj != null) {
                needSelection = true;
                tmpStore[4] = new String[obj.size()];
                tmpHCode[4] = new int[obj.size()];
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore[4][i] = (String)obj.get(i);
                    tmpHCode[4][i] = ((String)obj.get(i)).toLowerCase()
                        .hashCode();
                }
            }
        }
        if (needSelection == true) {
            selNegAttr = tmpHCode;
            excludeOpt = tmpStore;
        }
    }

    /*
     * parse a given string and return the int value for passed date
     */
    int parseTime(String data) {
        StringTokenizer st;
        int iter;
        int retVal;
        if (data == null) {
            return -1;
        }
        st = new StringTokenizer(data, TIME_FIELD_DELIMITER);
        iter = st.countTokens();
        if (iter < DATE_STRUCTURE_SIZE) { //year + month + day
            return -1;
        }
        retVal = Integer.parseInt(st.nextToken()) * YEAR_MULTIPLIER
            + Integer.parseInt(st.nextToken()) * MONTH_MULTIPLIER
            + Integer.parseInt(st.nextToken()) * DAY_MULTIPLIER;
        return retVal;
    }

    /*
     * decode the string from the format pointed for the createDateNumber() to
     * the record for array 'selDate' return null if string can be parsed
     */
    int[] decodeTimeString(String data, boolean include) {
        int[] retData = new int[DATE_STRUCTURE_SIZE];
        int tmp;
        if (data == null) {
            return null;
        }
        if (data.indexOf(AFTER_SYMBOL) != -1) {
            retData[0] = 2; //greater than
        } else {
            retData[0] = 1; //less than
        }
        if (data.indexOf(TIME_MODIFY_SYMBOL) != -1) {
            retData[1] = 2; //calculate value based on the date of last
            // modification
        } else {
            retData[1] = 1; //calculate value based on the date of creation
        }
        tmp = data.indexOf(TIME_MODE_DELIMITER);
        if (tmp != -1) {
            retData[2] = parseTime(data.substring(tmp + 1));
        } else {
            retData[2] = parseTime(data);
        }
        if (retData[2] == -1) {
            return null;
        }
        return retData;
    }

    /*
     * calculate the some number from the date pointed in the configuration. the
     * format of the string is "ca:2004-09-30" - created after <date Y-M-D> -
     * default "cb: <>" - created before ... "ma: <>" - modified after ... "mb:
     * <>" - modified before ... the number is defined as (Y * YEAR_MULTIPLIER +
     * M * MONTH_MULTIPLIER + D * DAY_MULTIPLIER) with restriction 0 < Y, 0 <= M <
     * 12, 0 < D < 32
     */
    void createDateNumber() {
        ArrayList obj;
        ArrayList tmpA;
        ArrayList multiTimeRec = new ArrayList();
        HashMap paramIncl;
        HashMap paramExcl;
        HashMap tmpH;
        String tmpStore = null;
        Calendar rNow = Calendar.getInstance();
        int[] singleTimeRec = new int[DATE_STRUCTURE_SIZE]; //mode + timefield
        // + time
        // data

        int selDateNow = rNow.get(Calendar.YEAR) * YEAR_MULTIPLIER
            + rNow.get(Calendar.MONTH) * MONTH_MULTIPLIER
            + rNow.get(Calendar.DAY_OF_MONTH) * DAY_MULTIPLIER;

        tmpA = (ArrayList)cfg.getPlugins().get("Selector");
        if (tmpA == null) {
            return;
        }
        tmpH = (HashMap)(tmpA).get(1);
        if (tmpH == null) {
            return;
        }
        paramIncl = (HashMap)tmpH.get("include");
        paramExcl = (HashMap)tmpH.get("exclude");

        if (paramIncl != null) {
            obj = (ArrayList)paramIncl.get("date");
            if (obj != null && obj.size() > 0) {
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore = (String)obj.get(i);
                    if (tmpStore != null) {
                        singleTimeRec = decodeTimeString(tmpStore, INCLUDE);
                        if (singleTimeRec != null) {
                            multiTimeRec.add(singleTimeRec);
                        }
                    }
                }
            }
        }
        if (paramExcl != null) {
            obj = (ArrayList)paramExcl.get("date");
            if (obj != null && obj.size() > 0) {
                for (int i = 0; i < obj.size(); i++) {
                    tmpStore = (String)obj.get(i);
                    if (tmpStore != null) {
                        singleTimeRec = decodeTimeString(tmpStore, EXCLUDE);
                        if (singleTimeRec != null) {
                            multiTimeRec.add(singleTimeRec);
                        }
                    }
                }
            }
        }
        if (multiTimeRec.size() == 0) {
            return;
        }
        selDate = new int[multiTimeRec.size() * singleTimeRec.length];
        for (int i = 0; i < multiTimeRec.size(); i++) {
            singleTimeRec = (int[])multiTimeRec.get(i);
            for (int y = 0; y < singleTimeRec.length; y++) {
                selDate[i * singleTimeRec.length + y] = singleTimeRec[y];
            }
        }
    }

    /*
     * create sort array for work with previous execution status. the values for
     * the result passed, failed, error and moderror are taking from the
     * Configuration the value for the result new is reserved (keyword new or
     * value NEW_TEST '101') All other values are unspecified
     */
    void createExcStat() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateExcStat(): ";
        ArrayList obj;
        ArrayList tmpA;
        ArrayList unspecIStat = new ArrayList();
        ArrayList unspecEStat = new ArrayList();
        int[] status = new int[5]; //0 - passed, 1 - failed, 2 - error, 3 -
        // moderror, 4 - new
        int tmpVal;
        HashMap paramIncl;
        HashMap paramExcl;
        HashMap tmpH;

        tmpA = (ArrayList)cfg.getPlugins().get("Selector");
        if (tmpA == null) {
            return;
        }
        tmpH = (HashMap)(tmpA).get(1);
        if (tmpH == null) {
            return;
        }
        paramIncl = (HashMap)tmpH.get("include");
        paramExcl = (HashMap)tmpH.get("exclude");

        for (int i = 0; i < status.length; i++) {
            status[i] = -1;
        }
        //parse the include statuses an update current status
        if (paramIncl != null) {
            obj = (ArrayList)paramIncl.get("status");
            if (obj != null) {
                for (int i = 0; i < obj.size(); i++) {
                    String tmp = (String)obj.get(i);
                    try {
                        //selection for the list of statuses is not supported yet
                        tmpVal = Integer.parseInt(tmp);
                        if (tmpVal == cfg.getRepPassed()[0]) {
                            status[0] = 3;
                        } else if (tmpVal == cfg.getRepFailed()[0]) {
                            status[1] = 3;
                        } else if (tmpVal == cfg.getRepError()[0]) {
                            status[2] = 3;
                        } else if (tmpVal == cfg.getRepModErr()[0]) {
                            status[3] = 3;
                        } else if (tmpVal == NEW_TEST) {
                            status[4] = 3; //new tests
                        } else {
                            unspecIStat.add(new Integer(tmpVal));
                        }
                    } catch (NumberFormatException ne) {
                        if (tmp.equalsIgnoreCase("passed")) {
                            status[0] = 3; //this should be included
                        } else if (tmp.equalsIgnoreCase("failed")) {
                            status[1] = 3;
                        } else if (tmp.equalsIgnoreCase("error")) {
                            status[2] = 3;
                        } else if (tmp.equalsIgnoreCase("moderror")) {
                            status[3] = 3;
                        } else if (tmp.equalsIgnoreCase("new")) {
                            status[4] = 3; //new tests
                        } else {
                            log.add(Level.WARNING, methodLogPrefix
                                + "unspecified name for selecting status: "
                                + tmp);
                        }
                    }
                }
                // work with 5 first statuses
                for (int i = 0; i < status.length; i++) {
                    if (status[i] == 3) {
                        status[i] = -1; // these statuses should be included
                    } else {
                        status[i] = 1; // these statuses should be excluded
                    }
                }
            }
        }
        //parse the exclude statuses an update current status
        if (paramExcl != null) {
            obj = (ArrayList)paramExcl.get("status");
            if (obj != null) {
                for (int i = 0; i < obj.size(); i++) {
                    //selection for the list of statuses is not supported yet
                    String tmp = (String)obj.get(i);
                    try {
                        tmpVal = Integer.parseInt(tmp);
                        if (tmpVal == cfg.getRepPassed()[0]) {
                            status[0] = 1;
                        } else if (tmpVal == cfg.getRepFailed()[0]) {
                            status[1] = 1;
                        } else if (tmpVal == cfg.getRepError()[0]) {
                            status[2] = 1;
                        } else if (tmpVal == cfg.getRepModErr()[0]) {
                            status[3] = 1;
                        } else if (tmpVal == NEW_TEST) {
                            status[4] = 1; //new tests
                        } else {
                            unspecEStat.add(new Integer(tmpVal));
                        }
                    } catch (NumberFormatException ne) {
                        if (tmp.equalsIgnoreCase("passed")) {
                            status[0] = 1;
                        } else if (tmp.equalsIgnoreCase("failed")) {
                            status[1] = 1;
                        } else if (tmp.equalsIgnoreCase("error")) {
                            status[2] = 1;
                        } else if (tmp.equalsIgnoreCase("moderror")) {
                            status[3] = 1;
                        } else if (tmp.equalsIgnoreCase("new")) {
                            status[4] = 1; //new tests
                        }
                    }
                }
            }
        }
        int cnt = 0;
        for (int i = 0; i < status.length; i++) {
            if (status[i] == 1) {
                cnt++;
            }
        }

        //selection for the list of statuses is not supported yet
        //parse the current status to define global status to work
        if (cnt > 0) {
            execStatExcl = new int[cnt];
            cnt = 0;
            for (int i = 0; i < status.length; i++) {
                if (status[i] == 1) {
                    switch (i) {
                    case 0:
                        execStatExcl[cnt++] = cfg.getRepPassed()[0];
                        break;
                    case 1:
                        execStatExcl[cnt++] = cfg.getRepFailed()[0];
                        break;
                    case 2:
                        execStatExcl[cnt++] = cfg.getRepError()[0];
                        break;
                    case 3:
                        execStatExcl[cnt++] = cfg.getRepModErr()[0];
                        break;
                    default:
                        execStatExcl[cnt++] = NEW_TEST; //new test
                        break;
                    }
                }
            }
        }
    }

    /*
     * make the test suite (for presentation in gui): find all tests in the
     * suite
     */
    public boolean mkTSuite() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tmkTSuite(): ";
        try {
            log.add(Level.INFO, methodLogPrefix
                + "find all results for test suite");
            rf.find();
            log.add(Level.INFO, methodLogPrefix
                + "find all tests for test suite");
            int num = tf.find();
            for (int cnt = 0; cnt < num; num++) {
                TestIR tmpStore = (TestIR)tf.getNext();
                ts.add(tmpStore.getTestID(), tmpStore);
            }
        } catch (ConfigurationException ce) {
            //do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //store the results of test (always set the result status to
    // SKIPPED/ModeError)
    protected void stoRes(TestIR test, String msg) {
        result.setTestID(test.getTestID());
        result.setAllProperty(test.getAllProperty());
        result.setOutMsg((String)null); //clear messages
        result.setOutMsg(msg);
        result.setExecStat(cfg.getRepModErr()[0]);
        store.addToReportOnly(result);
    }

    /*
     * read the pointed file as a text file and return the array of test name as
     * ArrayList with String as elements
     */
    ArrayList readFileNamesFromFile(String fileName) {
        ArrayList tmpStore = new ArrayList();
        try {
            FileInputStream in = new FileInputStream(fileName);
            int size = in.available();
            int index = 0;
            byte[] data = new byte[size];
            in.read(data);
            String tmp = new String(data);
            //tmp = tmp.replaceAll("\t", " "); tmp = tmp.replaceAll(" ", " ");
            StringTokenizer st = new StringTokenizer(tmp, System
                .getProperty("line.separator"));
            int iter = st.countTokens();
            for (int cnt = 0; cnt < iter; cnt++) {
                tmpStore.add(st.nextToken().trim());
            }
        } catch (IOException e) {
            //do nothing
        }
        return tmpStore;
    }

    /*
     * parse the input string and define all items after '@' and between ',' for
     * example
     */
    ArrayList defineTestCases(String data) {
        try {
            String testCases = data.substring(data
                .indexOf(Constants.MULTICASE_TEST_CASE_DELIMITER) + 1);
            ArrayList tc = new ArrayList();
            StringTokenizer st = new StringTokenizer(testCases,
                Constants.TEST_CASE_DELIMITER);
            int iter = st.countTokens();
            for (int cnt = 0; cnt < iter; cnt++) {
                tc.add(st.nextToken());
            }
            return tc;
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * parse the input string with test name: skip all comments (string that
     * started from //) and fill in the test cases list for execution for
     * multicase tests the format of strings in file is something like this
     * test_name(xml or class or other file name) //single case test
     * test_name(xml or class or other file name)@testcase1,testcase2
     * //multicase test
     */
    String mkMultiCaseTestCase(String data, boolean exclude) {
        String tmpStore = null;
        int delimPosition;
        int tmp;
        if (data == null && data.startsWith(Constants.START_COMMENT)) {
            return null;
        }
        delimPosition = data.indexOf(Constants.MULTICASE_TEST_CASE_DELIMITER);
        if (delimPosition == -1) {
            delimPosition = data.indexOf(END_TEST_SYMBOL);
            tmp = data.indexOf("\t");
            if (tmp != -1 && delimPosition != -1) {
                if (tmp > delimPosition) {
                    tmp = delimPosition;
                }
            } else if (delimPosition != -1) {
                tmp = delimPosition;
            }
            if (tmp != -1) {
                return data.substring(0, tmp);
            } else {
                return data;
            }
        } else {
            String testName = data.substring(0, delimPosition);
            if (delimPosition + 1 == data.length()) {
                return testName;
            }
            if (testName.indexOf(File.separatorChar) != -1) {
                testName = testName.replace(File.separatorChar,
                    Constants.INTERNAL_FILE_SEP_C);
            }
            if (exclude == EXCLUDE) {
                cfg.addTotExcludeTestCasesList(testName, defineTestCases(data));
            } else {
                cfg.addTotIncludeTestCasesList(testName, defineTestCases(data));
            }
            return testName;
        }
    }

    /*
     * tries to parse the pointed file: if it is impossible than read it as a
     * file with test names list Note, the testcases to include are pointed in
     * the file only
     */
    int includeTest(String inFile) throws ConfigurationException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tincludeTest(): ";
        log.add(Level.FINER, methodLogPrefix + "analyze file " + inFile);
        if (Util.checkExistFile(inFile)) {
            if (tf.find(inFile) == 0) {
                log.add(Level.FINER, methodLogPrefix + inFile
                    + " is a test list.");
                ArrayList tmpStore = readFileNamesFromFile(inFile);
                for (int y = 0; y < tmpStore.size(); y++) {
                    String tmp = mkMultiCaseTestCase((String)tmpStore.get(y),
                        INCLUDE);
                    if (tmp != null) {
                        tf.find(tmp);
                    }
                }
                return tmpStore.size(); // this was a file with test names
            } else {
                return 1; // inFile is a test name
            }
        }
        return 0;
    }

    /*
     * try parse a pointed strings as a test names. If fail - try parse a string
     * in file name which define the set of a test names
     */
    int mkExecTestNameList(String[] includeFiles) throws ConfigurationException {
        int cnt = 0;
        for (int i = 0; i < includeFiles.length; i++) {
            // add test if the name has absolute path: if this test is undefined
            // add 0 tests
            cnt = cnt + includeTest(includeFiles[i]);
            // add test if the name has relative path: if this test is undefined
            // add 0 tests
            cnt = cnt
                + includeTest(cfg.getTestSuiteConfigRoot() + File.separator
                    + includeFiles[i]);
        }
        return cnt;
    }

    /*
     * tries to parse the pointed file: if it is impossible than read it as a
     * file with test names list, exclude all file extension (symbols between '.'
     * and '@' or LF and create a list for exclusion Note, the testcases to
     * exclude are pointed in the file only
     */
    ArrayList excludeTest(String inFile) throws ConfigurationException {
        ArrayList retVal = new ArrayList();
        if (Util.checkExistFile(inFile)) {
            if (!tf.check(inFile)) {
                ArrayList tmpStore = readFileNamesFromFile(inFile);
                for (int y = 0; y < tmpStore.size(); y++) {
                    String data = (String)tmpStore.get(y);
                    if (!data.startsWith(Constants.START_COMMENT)) {
                        int extensPosition = data
                            .indexOf(Constants.START_FILE_EXTENSION);
                        if (extensPosition != -1) {
                            int delimitPosition = data
                                .indexOf(Constants.MULTICASE_TEST_CASE_DELIMITER);
                            if (delimitPosition != -1) {
                                data = data.substring(0, extensPosition)
                                    + data.substring(delimitPosition);
                            } else {
                                data = data.substring(0, extensPosition);
                            }
                        }
                        String tmp = mkMultiCaseTestCase(data, EXCLUDE);
                        if (tmp.equals(data)) {
                            retVal.add(tmp.replace(
                                Constants.FILE_SEP_C_TO_SUBS,
                                Constants.INTERNAL_FILE_SEP_C));
                        }
                    }
                }
            } else {
                if (inFile.indexOf(Constants.START_FILE_EXTENSION) == -1) {
                    retVal.add(inFile.replace(Constants.FILE_SEP_C_TO_SUBS,
                        Constants.INTERNAL_FILE_SEP_C));
                } else {
                    retVal.add(inFile.substring(0,
                        inFile.lastIndexOf(Constants.START_FILE_EXTENSION))
                        .replace(Constants.FILE_SEP_C_TO_SUBS,
                            Constants.INTERNAL_FILE_SEP_C));
                }
            }
        }
        return retVal;
    }

    /*
     * create a test list for exclusion
     */
    ArrayList mkExcludeTestNameList(String[] excludeFiles)
        throws ConfigurationException {
        ArrayList tmpStore = new ArrayList();
        ArrayList res1 = new ArrayList();
        ArrayList res2 = new ArrayList();
        for (int i = 0; i < excludeFiles.length; i++) {
            // add test if the name has absolute path: if this test is undefined
            // add 0 tests
            res1 = excludeTest(excludeFiles[i]);
            // add test if the name has relative path: if this test is undefined
            // add 0 tests
            res2 = excludeTest(cfg.getTestSuiteConfigRoot() + File.separator
                + excludeFiles[i]);
            for (int y = 0; y < res1.size(); y++) {
                tmpStore.add(res1.get(y));
            }
            for (int y = 0; y < res2.size(); y++) {
                tmpStore.add(res2.get(y));
            }
            res1.clear();
            res2.clear();
        }
        return tmpStore;
    }

    /*
     * the start point: this method creates list of tests for execution
     */
    public boolean run(String[] subSuites, String[] excludeSuites,
        String[] includeFiles, String[] excludeFiles) {

        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trun(): ";

        log.add(Level.FINER, methodLogPrefix
            + "Run with parameter subSuites = " + subSuites
            + ", excludeSuites = " + excludeSuites + ", includeFiles = "
            + includeFiles + ", excludeFiles = " + excludeFiles);
        try {
            if (excludeFiles != null) {
                excludeTestNames = mkExcludeTestNameList(excludeFiles);
                log.add(Level.FINER, methodLogPrefix
                    + "Tests to exclude from run " + excludeTestNames.size());
            }
            if (includeFiles != null) {
                addTests(mkExecTestNameList(includeFiles));
                log.add(Level.FINER, methodLogPrefix
                    + "Tests to include to run " + toRunList.size());
                return true;
            }
            if (needExecStatus()) {
                // should find all results of previous run before the make a
                // selection
                if (subSuites != null && subSuites.length > 0) {
                    for (int i = 0; i < subSuites.length; i++) {
                        log.add(Level.INFO, methodLogPrefix
                            + "find results for subsuite " + subSuites[i]);
                        addResults(rf.find(subSuites[i], excludeSuites));
                        log.add(Level.INFO, methodLogPrefix
                            + "find tests for subsuite " + subSuites[i]);
                        addTests(tf.find(subSuites[i], excludeSuites));
                    }
                } else {
                    log.add(Level.CONFIG, methodLogPrefix
                        + "find all results for test suite");
                    addResults(rf.find(null, excludeSuites));
                    log.add(Level.CONFIG, methodLogPrefix
                        + "find all tests for test suite");
                    addTests(tf.find(null, excludeSuites));
                }
            } else {
                if (subSuites != null && subSuites.length > 0) {
                    for (int i = 0; i < subSuites.length; i++) {
                        log.add(Level.CONFIG, methodLogPrefix
                            + "find tests for subsuite " + subSuites[i]);
                        addTests(tf.find(subSuites[i], excludeSuites));
                    }
                } else {
                    log.add(Level.CONFIG, methodLogPrefix
                        + "find all tests for test suite");
                    addTests(tf.find(null, excludeSuites));
                }
            }
        } catch (ConfigurationException ce) {
            //do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    boolean needExecStatus() {
        if (execStatExcl != null) {
            return true;
        }
        return false;
    }

    /*
     * add all test results founded by the result finder to execution status
     * list
     */
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
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\taddResult(): ";
        try {
            String testID = tRes.getTestID();
            int testRes = tRes.getExecStat();
            //remove the invalid entry or entry that have no the test ID and
            // exec status
            log.add(Level.FINE, methodLogPrefix + "status for test " + testID
                + " is " + testRes);
            if (testID != null) {
                resList.setStatus(testID, testRes);
            }
        } catch (Exception e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e, e);
        }
        return false;
    }

    /*
     * add all tests founded by the finder to execution list
     */
    boolean addTests(int num) {
        boolean retVal = false;
        for (int cnt = 0; cnt < num; cnt++) {
            retVal = addTest((TestIR)tf.getNext());
        }
        return true;
    }

    /*
     * analyze the TestIR to the current configuration: check that the Runner is
     * accessible for this test
     */
    boolean addTest(TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\taddTest(): ";
        if (test == null) {
            return false;
        }
        try {
            String testID = test.getTestID();
            //remove the invalid entry or entry that can not be executed
            log.add(Level.FINE, methodLogPrefix + "for test "
                + test.getTestID() + " test RunnerID is " + test.getRunnerID());
            if (test.getRunnerID() != null) {
                log.add(Level.FINE, methodLogPrefix + "for test "
                    + test.getTestID() + " RunnerID class is "
                    + Main.getCurCore().getRunner(test.getRunnerID()));
            }
            if (test.getRunnerID() != null
                && Main.getCurCore().getRunner(test.getRunnerID()) != null) {
                if (runSelect(test)) {
                    toRunList.add(test);
                    log.add(Level.CONFIG, methodLogPrefix
                        + "add to execution list test " + testID);
                    return true;
                }
            }
        } catch (Exception e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e, e);
        }
        return false;
    }

    /*
     * do the test selection according to the current configuration
     */
    boolean runSelect(TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunSelect(): ";

        boolean testAccepted = true;

        //if test name has 'predefined exclude' part - exclude it
        if (excludePredefinedNames) {
            for (int i = 0; i < predefinedNames.length; i++) {
                if (test.getTestID().contains(predefinedNames[i])) {
                    return false;
                }
            }
        }

        //if exclude list is defined and this test is excluded in this list:
        // skip it for run
        if (excludeTestNames != null) {
            if (excludeTestNames.contains(test.getTestID())) {
                stoRes(test, "Test was excluded by exclude list");
                log.add(Level.CONFIG, methodLogPrefix + "The test "
                    + test.getTestID() + " was excluded by exclude list");
                return false;
            }
        }
        //if no other requirement in the configuration:
        if (selPosAttr == null && selNegAttr == null && selDate == null
            && !needExecStatus()) {
            return true;
        }
        //if execution status is defined in the configuration:
        if (needExecStatus()) {
            // note, the getStatus method return -1 for new test
            int value = Main.getCurCore().getExecStat().getStatus(
                test.getTestID());
            if (value == -1) {
                value = NEW_TEST;
            }
            if (execStatExcl != null) {
                for (int i = 0; i < execStatExcl.length; i++) {
                    if (value == execStatExcl[i]) {
                        stoRes(
                            test,
                            methodLogPrefix
                                + "Test was excluded due to 'execution status selection' parameters");
                        log
                            .add(
                                Level.CONFIG,
                                methodLogPrefix
                                    + "The test "
                                    + test.getTestID()
                                    + " was excluded due to 'execution status selection' parameters");
                        return false;
                    }
                }
            }
        }
        // this test should be included
        if (selPosAttr != null) {
            if (!selPAttribute(test)) {
                stoRes(test, methodLogPrefix
                    + "Test was excluded due to 'include selection' parameters");
                log.add(Level.CONFIG, methodLogPrefix + "The test "
                    + test.getTestID()
                    + " was excluded due to 'include selection' parameters");
                return false;
            }
        }
        // this test should be excluded
        if (selNegAttr != null) {
            if (selNAttribute(test)) {
                stoRes(test, methodLogPrefix
                    + "Test was excluded due to 'exclude selection' parameters");
                log.add(Level.CONFIG, methodLogPrefix + "The test "
                    + test.getTestID()
                    + " was excluded due to 'exclude selection' parameters");
                return false;
            }
        }
        if (selDate != null) {
            if (selDateAttribute(test)) {
                stoRes(test, methodLogPrefix
                    + "Test was excluded due to 'date selection' parameters");
                log.add(Level.CONFIG, methodLogPrefix + "The test "
                    + test.getTestID()
                    + " was excluded due to 'date selection' parameters");
                return false;
            }
        }
        return true;
    }

    /*
     * The method return true if the appropriate date of test is OK to run this
     * test format of the selDate. each record has a 3 field: [n + 0] = mode 1 -
     * test with date number less than this field will be excluded 2 - test with
     * date number greater than this field will be excluded 3 - test with date
     * number equals to this field will be excluded [n + 1] = time field 1 -
     * work with creation field 2 - work with last modification field [n + 2] =
     * time number for selection
     */
    boolean selDateAttribute(TestIR test) {
        int testTime;
        String tmp = null;
        boolean retCode = true; //this variable will set to false if test
        // should be excluded
        ArrayList tmpT = test.getModifications();
        if (selDate == null) {
            return true;
        }
        try {
            for (int i = 0; i < selDate.length; i = +DATE_STRUCTURE_SIZE) {
                tmp = test.getProperty("date-of-creation");
                if (selDate[i * DATE_STRUCTURE_SIZE + 1] != 1) {
                    ArrayList tmpA = test.getModifications();
                    if (tmpA != null && tmpA.size() != 0) {
                        tmpA = (ArrayList)tmpA.get(tmpA.size() - 1);
                        if (tmpA != null && tmpA.size() != 0) {
                            tmp = (String)tmpA.get(0);
                        }
                    }
                }
                testTime = parseTime(tmp);
                if (selDate[i * DATE_STRUCTURE_SIZE] == 1) {
                    if (testTime < selDate[i * DATE_STRUCTURE_SIZE + 2]) {
                        retCode = false;
                    }
                }
                if (selDate[i * DATE_STRUCTURE_SIZE] == 2) {
                    if (testTime > selDate[i * DATE_STRUCTURE_SIZE + 2]) {
                        retCode = false;
                    }
                }
            }
        } catch (IndexOutOfBoundsException ie) {
            //the structure is wrong: no special thing to do
        }
        return retCode;
    }

    /*
     * define: this test is OK to include to this run (if any test attribute is
     * defined in the include list) String[0] = SelKeywords(); String[1] =
     * SelAuthor(); String[2] = SelModifAuthors(); String[3] = SelResources();
     * String[4] = SelRunners();
     */
    boolean selPAttribute(TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tselPAttribute(): ";
        int hCode;
        String tmp;
        String[] tmpStore;
        int[] exitStat;
        ArrayList tmpArr;
        if (selPosAttr == null) {
            return true;
        }
        exitStat = new int[5];
        try {
            if (selPosAttr[0] != null && selPosAttr[0].length > 0) {
                exitStat[0] = -1; // this parameters is valuable for calculation
                tmpStore = test.getKeywords();
                if (tmpStore != null && tmpStore.length > 0) {
                    for (int i = 0; i < tmpStore.length; i++) {
                        hCode = tmpStore[i].toLowerCase().hashCode();
                        for (int y = 0; y < selPosAttr[0].length; y++) {
                            if (hCode == selPosAttr[0][y]) {
                                log.add(Level.FINE, methodLogPrefix
                                    + "The test " + test.getTestID()
                                    + " is included due to keywords selection");
                                exitStat[0] = 1;//return true;
                                break;
                            }
                        }
                    }
                }
            }
            if (selPosAttr[1] != null && selPosAttr[1].length > 0) {
                exitStat[1] = -1; // this parameters is valuable for calculation
                tmpArr = test.getAuthors();
                if (tmpArr != null && tmpArr.size() > 0) {
                    for (int i = 0; i < tmpArr.size(); i++) {
                        tmp = (String)tmpArr.get(i);
                        if (tmp != null) {
                            hCode = tmp.toLowerCase().hashCode();
                            for (int y = 0; y < selPosAttr[1].length; y++) {
                                if (hCode == selPosAttr[1][y]) {
                                    log
                                        .add(
                                            Level.FINE,
                                            methodLogPrefix
                                                + "The test "
                                                + test.getTestID()
                                                + " is included due to authors selection");
                                    exitStat[1] = 1;//return true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (selPosAttr[2] != null && selPosAttr[2].length > 0) {
                exitStat[2] = -1; // this parameters is valuable for calculation
                tmpArr = test.getModifications();
                if (tmpArr != null && tmpArr.size() > 0) {
                    for (int i = 0; i < tmpArr.size(); i++) {
                        tmp = (String)((ArrayList)tmpArr.get(i)).get(1);
                        if (tmp != null) {
                            hCode = tmp.toLowerCase().hashCode();
                            for (int y = 0; y < selPosAttr[2].length; y++) {
                                if (hCode == selPosAttr[2][y]) {
                                    log
                                        .add(
                                            Level.FINE,
                                            methodLogPrefix
                                                + "The test "
                                                + test.getTestID()
                                                + " is included due to modification authors selection");
                                    exitStat[2] = 1;//return true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (selPosAttr[3] != null && selPosAttr[3].length > 0) {
                exitStat[3] = -1; // this parameters is valuable for calculation
                tmpStore = test.getResources();
                if (tmpStore != null && tmpStore.length > 0) {
                    for (int i = 0; i < tmpStore.length; i++) {
                        hCode = tmpStore[i].toLowerCase().hashCode();
                        for (int y = 0; y < selPosAttr[3].length; y++) {
                            if (hCode == selPosAttr[3][y]) {
                                log
                                    .add(
                                        Level.FINE,
                                        methodLogPrefix
                                            + "The test "
                                            + test.getTestID()
                                            + " is included due to resources selection");
                                exitStat[3] = 1;//return true;
                                break;
                            }
                        }
                    }
                }
            }
            if (selPosAttr[4] != null && selPosAttr[4].length > 0) {
                exitStat[4] = -1; // this parameters is valuable for calculation
                tmp = test.getRunnerID();
                if (tmp != null) {
                    hCode = tmp.toLowerCase().hashCode();
                    for (int y = 0; y < selPosAttr[4].length; y++) {
                        if (hCode == selPosAttr[4][y]) {
                            log.add(Level.FINE, methodLogPrefix + "The test "
                                + test.getTestID()
                                + " is included due to runner name selection");
                            exitStat[4] = 1;//return true;
                            break;
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException ie) {
            return true;
        }
        // all parameters are taking into account
        for (int i = 0; i < exitStat.length; i++) {
            if (exitStat[i] == -1) {
                return false;
            }
        }
        return true;//false;
    }

    /*
     * define: this test is OK to exclude from this run (if any test attribute
     * is defined in the exclude list) String[0] = SelKeywords(); String[1] =
     * SelAuthor(); String[2] = SelModifAuthors(); String[3] = SelResources();
     * String[4] = SelRunners();
     */
    boolean selNAttribute(TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tselNAttribute(): ";
        int hCode;
        String tmp;
        String[] tmpStore;
        ArrayList tmpArr;
        if (selNegAttr == null) {
            return true;
        }
        try {
            if (selNegAttr[0] != null && selNegAttr[0].length > 0) {
                tmpStore = test.getKeywords();
                if (tmpStore != null && tmpStore.length > 0) {
                    for (int i = 0; i < tmpStore.length; i++) {
                        hCode = tmpStore[i].toLowerCase().hashCode();
                        for (int y = 0; y < selNegAttr[0].length; y++) {
                            if (hCode == selNegAttr[0][y]) {
                                log.add(Level.FINE, methodLogPrefix
                                    + "The test " + test.getTestID()
                                    + " is excluded due to keywords selection");
                                return true;
                            }
                        }
                    }
                }
            }
            if (selNegAttr[1] != null && selNegAttr[1].length > 0) {
                tmpArr = test.getAuthors();
                if (tmpArr != null && tmpArr.size() > 0) {
                    for (int i = 0; i < tmpArr.size(); i++) {
                        hCode = ((String)tmpArr.get(i)).toLowerCase()
                            .hashCode();
                        for (int y = 0; y < selNegAttr[1].length; y++) {
                            if (hCode == selNegAttr[1][y]) {
                                log.add(Level.FINE, methodLogPrefix
                                    + "The test " + test.getTestID()
                                    + " is excluded due to authors selection");
                                return true;
                            }
                        }
                    }
                }
            }
            if (selNegAttr[2] != null && selNegAttr[2].length > 0) {
                tmpArr = test.getModifications();
                if (tmpArr != null && tmpArr.size() > 0) {
                    for (int i = 0; i < tmpArr.size(); i++) {
                        tmp = (String)((ArrayList)tmpArr.get(i)).get(1);
                        if (tmp != null) {
                            hCode = tmp.toLowerCase().hashCode();
                            for (int y = 0; y < selNegAttr[2].length; y++) {
                                if (hCode == selNegAttr[2][y]) {
                                    log
                                        .add(
                                            Level.FINE,
                                            methodLogPrefix
                                                + "The test "
                                                + test.getTestID()
                                                + " is excluded due to modification authors selection");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            if (selNegAttr[3] != null && selNegAttr[3].length > 0) {
                tmpStore = test.getResources();
                if (tmpStore != null && tmpStore.length > 0) {
                    for (int i = 0; i < tmpStore.length; i++) {
                        hCode = tmpStore[i].toLowerCase().hashCode();
                        for (int y = 0; y < selNegAttr[3].length; y++) {
                            if (hCode == selNegAttr[3][y]) {
                                log.add(Level.FINE, methodLogPrefix
                                    + "The test " + test.getTestID()
                                    + " is excluded due to resource selection");
                                return true;
                            }
                        }
                    }
                }
            }
            if (selNegAttr[4] != null && selNegAttr[4].length > 0) {
                tmp = test.getRunnerID();
                if (tmp != null) {
                    hCode = tmp.toLowerCase().hashCode();
                    for (int y = 0; y < selNegAttr[4].length; y++) {
                        if (hCode == selNegAttr[4][y]) {
                            log.add(Level.FINE, methodLogPrefix + "The test "
                                + test.getTestID()
                                + " is excluded due to runner name selection");
                            return true;
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException ie) {
            return true;
        }
        return false;//true;//false;
    }
}