/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.apache.harmony.harness.ReportTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.Util;
import org.apache.harmony.harness.plugins.StoreRes;

public class StoreResAsJU extends StoreRes {

    public static final int     SHORT_MSG_LEN = 1000;

    private static final String classID       = "StoreResAsJU";

    static final String         DATA_TOKEN    = "@@to_replace@@";

    public StoreResAsJU() {
        super();
        super.repSuffix = ".xml";
    }

    public boolean add(TResIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tadd(): ";
        try {
            // add result to reporter module.
            if (rep != null) {
                String fileName;
                fileName = "TEST-"
                    + test.getTestID().replace(Constants.INTERNAL_FILE_SEP_C,
                        '.');
                rep.addResult(fileName + repSuffix, test.getExecStat());
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
            int errors = 0;
            int failures = 0;
            boolean status_defined = false;
            int tests = 1;
            String resMsg = "";
            for (int i = 0; i < pasVal.length; i++) {
                if (testStatus == pasVal[i]) {
                    resultMsg = " (PASSED)";
                    status_defined = true;
                }
            }
            for (int i = 0; i < fldVal.length; i++) {
                if (testStatus == fldVal[i]) {
                    resultMsg = " (FAILED)";
                    failures++;
                    resMsg = "<failure type='failure'>" + DATA_TOKEN
                        + "</failure>";
                    status_defined = true;
                }
            }
            for (int i = 0; i < errVal.length; i++) {
                if (testStatus == errVal[i]) {
                    resultMsg = " (ERROR)";
                    errors++;
                    resMsg = "<error message='error' type='error'>"
                        + DATA_TOKEN + "</error>";
                    status_defined = true;
                }
            }
            for (int i = 0; i < merVal.length; i++) {
                if (testStatus == merVal[i]) {
                    resultMsg = " (SKIPPED)";
                    status_defined = true;
                }
            }
            if (!status_defined) {
                errors++;
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
            long totalTime = 0;
            if (tmpAL.size() > 0) {
                time = tmpAL.size() + " parts";
                for (int i = 0; i < tmpAL.size(); i++) {
                    time = time + ". Part " + i + ": "
                        + tmpAL.get(i).toString();
                    totalTime = totalTime + ((Long)tmpAL.get(i)).longValue();
                }
                time = "Total time: " + totalTime + ". " + time;
            }
            if (resMsg.contains(DATA_TOKEN)) {
                String shortMsg = msg;
                if (msg.length() > SHORT_MSG_LEN) {
                    shortMsg = msg.substring(0, SHORT_MSG_LEN) + "...]]>";
                }
                resMsg = resMsg.replace(DATA_TOKEN, shortMsg);
            }

            resStore.write("<?xml version=\"" + xmlVersion + "\"?>\n"
                + "<testsuite errors='" + errors + "' failures='" + failures
                + "' name='"
                + test.getTestID().replace(Constants.INTERNAL_FILE_SEP_C, '.')
                + "' tests='" + tests + "' time='" + totalTime
                + "'>\n<properties>\n" + "\t<property name='os' value='"
                + test.getTestedOS() + "'/>\n"
                + "\t<property name='platform' value='"
                + test.getTestedPlatform() + "'/>\n"
                + "\t<property name='runtime' value='" + test.getTestedVM()
                + "'/>\n" + "\t<property name='command'><![CDATA["
                + test.getExecCmdAsString(" ") + "]]></property>\n"
                + "\t<property name='date' value='" + test.getDate() + "'/>\n"
                + "\t<property name='status' value='" + test.getExecStat()
                + resultMsg + "'/>\n" + "</properties>\n"
                + "<testcase classname='"
                + test.getTestID().replace(Constants.INTERNAL_FILE_SEP_C, '.')
                + "' name='test' time='" + totalTime + "'>\n" + resMsg
                + "\n</testcase>\n" + "<system-out>" + msg + "</system-out>\n"
                + "<system-err>" + testInfo + "</system-err>\n"
                + "</testsuite>\n");
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

    public boolean init(String name) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tinit(): ";
        try {
            String resDir = cfg.getTestResultRoot();
            String fileName;
            if (name.length() > (resDir.length() + 1)) {
                fileName = "TEST-"
                    + name.substring(resDir.length() + 1).replace(
                        Constants.INTERNAL_FILE_SEP_C, '.');
            } else {
                fileName = "TEST-"
                    + name.replace(Constants.INTERNAL_FILE_SEP_C, '.');
            }
            new File(resDir).mkdirs();
            File curFile = new File(resDir, fileName);
            if (!override && curFile.exists()) {
                File dest = new File(fileName + ".old");
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

}