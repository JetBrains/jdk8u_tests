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
 * @author Y.Tokpanov, V.Ivanov
 * @version $Revision: 1.40 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Report;
import org.apache.harmony.harness.Storage;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.Util;

public class StoreRes implements Storage {

    private static final String   classID            = "StoreRes";

    // this constants are for sorting test output to something required by test
    // developer
    public static final String    RECORD_START       = "%ecnfk@";
    public static final String    TIME_DELIMITER     = ":";
    public static final String    MSG_DELIMITER      = "| ";

    public static final String    CMD_MSG_DELIMITER  = "\t";
    public static final String    OUT_MSG_DELIMITER  = "\n";
    public static final String    INFO_MSG_DELIMITER = "\n";

    public static final String[]  DEFAULT_SOURCE_EXT = { ".java", ".j",
        ".ccode", ".cpp", ".c"                      };

    protected volatile FileWriter resStore;
    protected boolean             override           = Main.getCurCore()
                                                         .getConfigIR()
                                                         .getResOver();
    protected Report              rep                = Main.getCurCore()
                                                         .getReporter();
    protected Logging             log                = Main.getCurCore()
                                                         .getInternalLogger();
    protected ConfigIR            cfg                = Main.getCurCore()
                                                         .getConfigIR();
    protected final int[]         pasVal             = cfg.getRepPassed();
    protected final int[]         fldVal             = cfg.getRepFailed();
    protected final int[]         errVal             = cfg.getRepError();
    protected final int[]         merVal             = cfg.getRepModErr();

    protected String              xmlVersion;
    protected String[][]          maps;

    protected String              repSuffix          = Constants.RESULTS_EXT;

    public StoreRes() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tStoreRes(): ";
        try {
            //seems that IE does not support xml 1.1 yet
            xmlVersion = "1.0";//new XMLParser().getXMLVersion();
        } catch (Exception e) {
            xmlVersion = "1.0";
        }
        ArrayList tmp = cfg.getPluginProperties("Storage");
        if (tmp != null) {
            try {
                HashMap param = (HashMap)((HashMap)tmp.get(1)).get("maps");
                if (param != null
                    && (((ArrayList)param.get("from")).size() > 0)
                    && (((ArrayList)param.get("to")).size() > 0)) {
                    int from_size = ((ArrayList)param.get("from")).size();
                    int to_size = ((ArrayList)param.get("to")).size();
                    int cnt = (from_size <= to_size) ? from_size : to_size;
                    maps = new String[cnt][2];
                    for (int i = 0; i < cnt; i++) {
                        maps[i][0] = (String)((ArrayList)param.get("from"))
                            .get(i);
                        maps[i][1] = (String)((ArrayList)param.get("to"))
                            .get(i);
                    }
                }
            } catch (Exception e) {
                log
                    .add(Level.CONFIG, methodLogPrefix
                        + MessageInfo.UNEX_EXCEPTION
                        + "while parse the 'map' configuration parameters: "
                        + e, e);
            }
            try {
                HashMap param = (HashMap)((HashMap)tmp.get(1)).get("out_files");
                if (param != null
                    && ((ArrayList)param.get("extension")).size() > 0) {
                    repSuffix = "."
                        + (String)(((ArrayList)param.get("extension")).get(0));
                }
            } catch (Exception e) {
                log.add(Level.CONFIG, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION
                    + "while parse the config parameters: " + e, e);
            }
        }
    }

    public String getResultExtension() {
        return repSuffix;
    }

    /*
     * sort the messages in the log by time stamp the expected log records
     * format is startOfRecord + time_in_millisecond_starting_from_1970 +
     * msgDelimiter + message
     */
    protected String sortByTimestamp(String data) {
        String retVal;
        if (data.indexOf(RECORD_START) == -1) {
            return data;
        }
        String[] st = data.split(RECORD_START);
        String[] sortedSt;
        boolean tmp = data.startsWith(RECORD_START);
        retVal = st[0];
        sortedSt = new String[st.length - 1];
        for (int i = 0; i < sortedSt.length; i++) {
            sortedSt[i] = st[i + 1];
        }
        sortedSt = sortStringArr(sortedSt);
        for (int i = 0; i < sortedSt.length; i++) {
            retVal = retVal + parseTime(sortedSt[i]);
        }
        return retVal;
    }

    /*
     * sort the string in input array and return the sorted array the elements
     * format of input array is time_in_millisecond_starting_from_1970 +
     * msgDelimiter + message this method use only the millisecond to sort
     */
    protected String[] sortStringArr(String[] data) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsortStringArr(): ";
        String[] retValue = new String[data.length];
        try {
            long[][] toSort = new long[data.length][2];
            for (int i = 0; i < toSort.length; i++) {
                toSort[i][0] = Long.parseLong(data[i].substring(0, data[i]
                    .indexOf(MSG_DELIMITER)));
                toSort[i][1] = i;
            }
            toSort = sortLongArrByFirst(toSort);
            for (int i = 0; i < data.length; i++) {
                retValue[i] = data[(int)toSort[i][1]];
            }
            return retValue;
        } catch (Exception e) {
            log.add(Level.CONFIG, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while sort a log messages: " + e, e);
            return data;
        }
    }

    /*
     * sort the array of long according to the first dimension
     */
    protected long[][] sortLongArrByFirst(long[][] data) {
        int cnt = 0;
        long[] tmp;
        for (int i = 1; i < data.length; i++) {
            if (data[i - 1][0] > data[i][0]) {
                cnt++;
                tmp = data[i - 1];
                data[i - 1] = data[i];
                data[i] = tmp;
            }
        }
        if (cnt > 0) {
            sortLongArrByFirst(data);
        }
        return data;
    }

    /*
     * parse the time from millisecond to hh:mm:ss format the expected input
     * string format is time_in_millisecond_starting_from_1970 + msgDelimiter +
     * message
     */
    protected String parseTime(String data) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tparseTime(): ";
        String tmp = "";
        int index = data.indexOf(MSG_DELIMITER);
        if (index == -1) {
            return data;
        }
        try {
            tmp = data.substring(0, index);
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(Long.parseLong(tmp)));
            int h = c.get(Calendar.HOUR_OF_DAY);
            int minut = c.get(Calendar.MINUTE);
            int sec = c.get(Calendar.SECOND);
            int msec = c.get(Calendar.MILLISECOND);
            String retValue = "";
            if (h < 10) {
                retValue = retValue + "0" + h;
            } else {
                retValue = retValue + h;
            }
            if (minut < 10) {
                retValue = retValue + TIME_DELIMITER + "0" + minut;
            } else {
                retValue = retValue + TIME_DELIMITER + minut;
            }
            if (sec < 10) {
                retValue = retValue + TIME_DELIMITER + "0" + sec;
            } else {
                retValue = retValue + TIME_DELIMITER + sec;
            }
            /*
             * if (msec < 10) { retValue = retValue + timeDelimiter + "00" +
             * msec; } else if (msec < 100) { retValue = retValue +
             * timeDelimiter + "0" + msec; } else { retValue = retValue +
             * timeDelimiter + msec; }
             */
            return retValue + data.substring(index);
        } catch (Exception e) {
            log.add(Level.CONFIG, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while parse a log: " + e, e);
            return data;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Storage#initStorage()
     */
    public boolean init(String name) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tinit(): ";
        try {
            int Pos = name.lastIndexOf(Constants.INTERNAL_FILE_SEP_C);
            String fileName = name.substring(Pos + 1);
            String dirName = name.substring(0, Pos);
            File curFile = new File(dirName);
            curFile.mkdirs();
            curFile = new File(name);
            if (!override && curFile.exists()) {
                File dest = new File(name + ".old");
                if (dest.exists()) {
                    dest.delete();
                }
                curFile.renameTo(dest);
            }
            resStore = new FileWriter(curFile);
            return true;
        } catch (IOException e) {
            resStore = null;
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while creating the report file(s).\n" + e, e);
        }
        return false;
    }

    //harnes prepare the test report and responsible for it.
    //so check for allowed symbols into CDATA section and substitute
    //incorrect to ' ' sign (whitespace)
    //allowed in the CDATA chars is
    //#x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
    protected String correctDataToCD(String data) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcorrectDataToCD(): ";
        if (data == null) {
            return data;
        }
        char[] data_arr = data.toCharArray();
        for (int i = 0; i < data_arr.length; i++) {
            if (!(data_arr[i] == '\u0009' || data_arr[i] == '\r'
                || data_arr[i] == '\n'
                || (data_arr[i] >= '\u0020' && data_arr[i] <= '\uD7ff') || (data_arr[i] >= '\uE000' && data_arr[i] <= '\uFFFD'))) {
                log.add(Level.CONFIG, methodLogPrefix
                    + "Incorrect symbol for xml 1.0 ("
                    + Integer.toHexString(new Integer(data_arr[i]).intValue())
                    + ") changed to whitespace");
                data_arr[i] = ' ';
            }
        }
        return new String(data_arr);
    }

    /*
     * convert the log message to specific format (add some tags etc) by
     * default, do nothing
     */
    protected String convertOutToSpecificFormat(String[] data) {
        String tmpVal = "<![CDATA[";
        for (int i = 0; i < data.length; i++) {
            tmpVal = tmpVal + correctDataToCD(data[i]) + OUT_MSG_DELIMITER;
        }
        tmpVal = tmpVal + "]]>";
        return tmpVal;
    }

    /*
     * convert the command to run to specific format (add some tags etc) by
     * default, do nothing.
     */
    protected String convertCmdToSpecificFormat(String[] data) {
        String tmpVal = "<![CDATA[";
        for (int i = 0; i < data.length; i++) {
            tmpVal = tmpVal + correctDataToCD(data[i]) + CMD_MSG_DELIMITER;
        }
        tmpVal = tmpVal + "]]>";
        return tmpVal;
    }

    /*
     * convert the test specific information to specific format (add some tags
     * etc) by default, do nothing. Note, by convention it is a framework output
     */
    protected String convertInfoToSpecificFormat(String[] data) {
        String tmpVal = "";
        for (int i = 0; i < data.length; i++) {
            tmpVal = tmpVal + data[i] + INFO_MSG_DELIMITER;
        }
        return tmpVal;
    }

    /*
     * calculate difference between 2 string array. Elements compared with case.
     * if element exist in current only remember it as "set element" if element
     * exist in previous only remember it as "unset element"
     */
    protected String[] getDifference(String[] previous, String[] current) {
        if (previous == null && current == null) {
            return null;
        }
        String[] retVal;
        if (previous == null) {
            retVal = new String[current.length];
            for (int i = 0; i < current.length; i++) {
                retVal[i] = "set " + current[i];
            }
            return retVal;
        }
        if (current == null) {
            retVal = new String[previous.length];
            for (int i = 0; i < previous.length; i++) {
                retVal[i] = "unset " + previous[i];
            }
            return retVal;
        }
        ArrayList tmpVal = new ArrayList();
        int[] activPrevIndex = new int[previous.length];
        for (int i = 0; i < current.length; i++) {
            boolean exist = false;
            for (int y = 0; y < previous.length; y++) {
                if (previous[y].equals(current[i])) {
                    exist = true;
                    activPrevIndex[y] = 1;
                    break;
                }
            }
            if (!exist) {
                tmpVal.add("set " + current[i]);
            }
        }
        for (int i = 0; i < activPrevIndex.length; i++) {
            if (activPrevIndex[i] != 1) {
                tmpVal.add("unset " + previous[i]);
            }
        }
        retVal = new String[tmpVal.size()];
        for (int i = 0; i < tmpVal.size(); i++) {
            retVal[i] = (String)tmpVal.get(i);
        }
        return retVal;
    }

    /*
     * return the environment for this test or empty string
     */
    protected String getEnvMsg(TResIR test) {
        try {
            String retVal = "";
            String[] prevArr = null;
            String[] data;
            //env is a ArrayList of string arrays with environment settings
            ArrayList tmp = (ArrayList)test.getProperty("genEnv");
            //case when tmp is null end other possible handled by catch section
            for (int i = 0; i < tmp.size(); i++) {
                data = (String[])tmp.get(i);
                if (data != null) {
                    Arrays.sort(data);
                    retVal = retVal
                        + "<property-item name=\"env\"><![CDATA[Environment for part N"
                        + i + "\n";
                    if (prevArr == null) {//first time just print and save for
                        // future use
                        prevArr = new String[data.length];
                        for (int y = 0; y < data.length; y++) {
                            retVal = retVal + data[y] + "\n";
                            prevArr[y] = data[y];
                        }
                    } else {
                        //calculate the difference with previous array
                        String[] diff = getDifference(prevArr, data);
                        //print difference
                        if (diff == null || diff.length == 0) {
                            retVal = retVal + "the same as previous\n";
                        } else {
                            retVal = retVal + "previous and\n";
                        }
                        for (int y = 0; y < diff.length; y++) {
                            retVal = retVal + diff[y] + "\n";
                        }
                        //refresh array of previous values
                        prevArr = new String[data.length];
                        for (int y = 0; y < data.length; y++) {
                            prevArr[y] = data[y];
                        }
                    }
                    retVal = retVal + "]]></property-item>\n";
                }
            }
            return retVal;
        } catch (Exception e) {
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Storage#addStorage(TestIR)
     */
    public boolean add(TResIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tadd(): ";
        try {
            // add result to reporter module.
            if (rep != null) {
                if (test.getRepFile() != null) {
                    rep.addResult(test.getRepFile() + repSuffix, test
                        .getExecStat());
                } else { //use the default rules for report file name
                    rep.addResult(test.getTestID() + repSuffix, test
                        .getExecStat());
                }
            }
            if (resStore == null) {
                log.add(Level.INFO, methodLogPrefix
                    + "Can not store the result. Please, check your settings.");
                return false;
            }
            //some service information
            String path = "";
            String srcPath = "";
            String msg = convertOutToSpecificFormat(test.getOutMsg());
            String testInfo = convertInfoToSpecificFormat(test
                .getTestSpecificInfo());
            String resultMsg = " (UNKNOWN)";
            String tmpVal;
            String cmd = convertCmdToSpecificFormat(test.getExecCmd());
            String time = " ";
            String[] srcNames = null;
            int testStatus = test.getExecStat();
            if (msg == null) {
                msg = "";
            } else {
                msg = sortByTimestamp(msg);
            }
            if (testInfo == null) {
                testInfo = "";
            }
            // note: this result is parsed by result parser which expect the
            // digit before the whitespace
            for (int i = 0; i < pasVal.length; i++) {
                if (testStatus == pasVal[i]) {
                    resultMsg = " (PASSED)";
                }
            }
            for (int i = 0; i < fldVal.length; i++) {
                if (testStatus == fldVal[i]) {
                    resultMsg = " (FAILED)";
                }
            }
            for (int i = 0; i < errVal.length; i++) {
                if (testStatus == errVal[i]) {
                    resultMsg = " (ERROR)";
                }
            }
            for (int i = 0; i < merVal.length; i++) {
                if (testStatus == merVal[i]) {
                    resultMsg = " (SKIPPED)";
                }
            }
            char[] tmp = test.getTestID().toCharArray();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i] == Constants.INTERNAL_FILE_SEP_C) {
                    if (i > 0 && tmp[i - 1] == '.') { //look for /./
                        // construction
                        if (i - 1 > 0
                            && tmp[i - 2] != Constants.INTERNAL_FILE_SEP_C) {
                            path = path + ".." + File.separator;
                        }
                    } else {
                        path = path + ".." + File.separator;
                    }
                }
            }
            srcPath = cfg.getTestSuiteTestRoot()
                + File.separator
                + test.getTestID().replace(Constants.INTERNAL_FILE_SEP_C,
                    File.separatorChar);
            int index = srcPath.lastIndexOf(File.separator);
            srcPath = srcPath.substring(0, index) + File.separator;
            ArrayList tmpArr = (ArrayList)test.getProperty("sources");
            if (tmpArr != null) {
                int realCnt;
                int iter = tmpArr.size();
                srcNames = new String[iter];
                for (int cnt = 0; cnt < iter; cnt++) {
                    srcNames[cnt] = srcPath + tmpArr.get(cnt);
                    if (Util.checkExistFile(srcNames[cnt])) {
                        srcNames[cnt] = (new File(srcNames[cnt]))
                            .getAbsolutePath();
                    } else {
                        srcNames[cnt] = null;
                    }
                }
                realCnt = 0;
                for (int i = 0; i < srcNames.length; i++) {
                    if (srcNames[i] != null) {
                        realCnt++;
                    }
                }
                if (realCnt != srcNames.length && realCnt >= 1) {
                    String[] tmpValArr = new String[realCnt];
                    for (int i = 0; i < srcNames.length; i++) {
                        if (srcNames[i] != null) {
                            tmpValArr[--realCnt] = srcNames[i];
                        }
                    }
                    srcNames = tmpValArr;
                }
                if (realCnt == 0) {
                    srcNames = null;
                }
            }
            //if exist xml-file for test - add it to the head of list
            if (Util.checkExistFile(cfg.getTestSuiteTestRoot() + File.separator
                + test.getTestID() + ".xml")) {
                srcNames = Util.addElemToArray(srcNames, cfg
                    .getTestSuiteTestRoot()
                    + File.separator + test.getTestID() + ".xml", true);
            }
            //if nothing found try to check some predefined extension
            if (srcNames == null) {
                srcNames = new String[1];
                for (int i = 0; i < DEFAULT_SOURCE_EXT.length; i++) {
                    srcNames[0] = cfg.getTestSuiteTestRoot() + File.separator
                        + test.getTestID() + DEFAULT_SOURCE_EXT[i];
                    if (Util.checkExistFile(srcNames[0])) {
                        break;
                    } else {
                        srcNames[0] = null;
                    }
                }
            }
            if (srcNames[0] == null) {
                srcNames[0] = "";
            }
            if (maps != null) {
                for (int i = 0; i < maps.length; i++) {
                    for (int y = 0; y < srcNames.length; y++) {
                        if (srcNames[y].startsWith(maps[i][0])) {
                            srcNames[y] = maps[i][1]
                                + srcNames[y].substring(maps[i][0].length());
                        }
                    }
                }
            }
            ArrayList tmpAL = test.getExecTime();
            if (tmpAL.size() > 0) {
                long totalTime = 0;
                time = tmpAL.size() + " parts";
                for (int i = 0; i < tmpAL.size(); i++) {
                    time = time + ". Part " + i + ": "
                        + tmpAL.get(i).toString();
                    totalTime = totalTime + ((Long)tmpAL.get(i)).longValue();
                }
                time = "Total time: " + totalTime + ". " + time;
            }
            resStore
                .write("<?xml version=\""
                    + xmlVersion
                    + "\" encoding=\"UTF-8\"?>\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + path
                    + "test.xsl\"?>\n"
                    + "<TestRes>\n"
                    + "<property-list>\n\t<property-item name=\"OS\">"
                    + test.getTestedOS()
                    + "</property-item>\n\t<property-item name=\"Platform\">"
                    + test.getTestedPlatform()
                    + "</property-item>\n\t<property-item name=\"Run VM\">"
                    + test.getTestedVM()
                    + "</property-item>\n\t<property-item name=\"DATE\">"
                    + test.getDate()
                    + "</property-item>\n\t<property-item name=\"TestID\"><![CDATA["
                    + test.getTestID()
                    + "]]></property-item>\n\t"
                    + getEnvMsg(test)
                    + "<property-item name=\"cmd\">"
                    + cmd
                    + "</property-item>\n\t<property-item name=\"exectime\">"
                    + time
                    + "</property-item>\n\t<property-item name=\"Status\">"
                    + test.getExecStat()
                    + resultMsg
                    + "</property-item>\n\t<property-item name=\"outMsg\">"
                    + msg
                    + "</property-item>\n\t<property-item name=\"testSpecificInfo\">"
                    + testInfo + "</property-item>\n</property-list>\n"
                    + "<info-list>\n"
                    + tagStrings(srcNames, "info-item", "source file", true)
                    + "</info-list>\n" + "</TestRes>");
            resStore.flush();
            log.add(Level.WARNING, methodLogPrefix + "Test was run "
                + test.getTestID() + " " + resultMsg);
            return true;
        } catch (Exception e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while writing the report file(s) for test: "
                + test.getTestID() + "\n" + e, e);
            return false;
        }
    }

    /*
     * mark all strings from 'data' array by 'tag' with attribute name and value
     * 'name' (xml notation)
     */
    protected String tagStrings(String[] data, String tag, String name,
        boolean needCdata) {
        String retVal = "";
        String startCData = "";
        String endCData = "";
        if (needCdata == true) {
            startCData = "<![CDATA[";
            endCData = "]]>";
        }
        if (data != null && data.length > 0) {
            if (tag == null) {
                for (int i = 0; i < data.length; i++) {
                    retVal = retVal + data[i] + "\n";
                }
                return retVal;
            }
            if (name != null) {
                for (int i = 0; i < data.length; i++) {
                    retVal = retVal + "<" + tag + " name=\"" + name + "\">"
                        + startCData + data[i] + endCData + "</" + tag + ">\n";
                }
            } else {
                for (int i = 0; i < data.length; i++) {
                    retVal = retVal + "<" + tag + ">" + startCData + data[i]
                        + endCData + "</" + tag + ">\n";
                }
            }
        }
        return retVal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Storage#closeStorage()
     */
    public boolean close() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tclose(): ";
        try {
            if (resStore != null) {
                resStore.close();
                resStore = null;
                return true;
            }
        } catch (IOException e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while closing the report file(s).\n" + e, e);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Storage#addToReportOnly(org.apache.harmony.harness.TResIR)
     */
    public boolean addToReportOnly(TResIR test) {
        // add result to reporter module.
        if (rep != null) {
            if (test.getRepFile() != null) {
                rep
                    .addResult(test.getRepFile() + repSuffix, test
                        .getExecStat());
            } else { //use the default rules for report file name
                rep.addResult(test.getTestID() + repSuffix, test.getExecStat());
            }
            return true;
        }
        return false;
    }
}
