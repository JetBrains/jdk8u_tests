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
 * @version $Revision: 1.4 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TestIR;
import org.apache.harmony.harness.Util;

public class GroupTestFinder extends TestFinder {

    private final String       classID              = "GroupTestFinder";

    public static final String GROUP_NAME           = "TestGroup";
    public static final String GROUP_RUNNER_NAME    = "GroupRunner";
    public static final String PLUGIN_NAME          = "TestFinder";
    public static final String PARAMETR_NAME        = "TestsInGroup";

    public static final String PROPERTY_NUMBER_NAME = "number";
    public static final String PROPERTY_FILE_NAME   = "file";

    protected int              testGroupCnt         = 0;
    protected TestIR           groupTest            = new TestIR(
                                                        GROUP_RUNNER_NAME,
                                                        GROUP_NAME
                                                            + testGroupCnt);

    protected ConfigIR         cfg                  = Main.getCurCore()
                                                        .getConfigIR();

    //number of tests in each group
    //testInGroupCnt=1 - each test in the proper group
    protected int              testInGroupCnt       = 3;

    //number of the tests in current group
    protected int              curTestInGroupCnt    = -1;

    //number of the total tests in all groups
    protected int              testCnt              = 0;

    //file name with test list
    protected String           fileName;

    public GroupTestFinder() {
        super();
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tGroupTestFinder(): ";
        ArrayList finderProp = (ArrayList)cfg.getPlugins().get(PLUGIN_NAME);
        if (finderProp.size() > 1) {
            HashMap finderHM = (HashMap)finderProp.get(1);
            if (finderHM.containsKey(PARAMETR_NAME)) {
                HashMap tmp = (HashMap)finderHM.get(PARAMETR_NAME);
                if (tmp.containsKey(PROPERTY_NUMBER_NAME)) {
                    finderProp = (ArrayList)tmp.get(PROPERTY_NUMBER_NAME);
                    if (finderProp.size() >= 1) {
                        try {
                            testInGroupCnt = Integer
                                .parseInt((String)finderProp.get(0));
                        } catch (NumberFormatException nfe) {
                            log
                                .add(
                                    Level.FINE,
                                    methodLogPrefix
                                        + "unexpected exception while parse configuration: "
                                        + nfe);
                        }
                    }
                }
                if (tmp.containsKey(PROPERTY_FILE_NAME)) {
                    finderProp = (ArrayList)tmp.get(PROPERTY_FILE_NAME);
                    if (finderProp.size() >= 1) {
                        try {
                            fileName = (String)finderProp.get(0);
                            if (!Util.checkExistFile(fileName)) {
                                if (Util.checkExistFile(cfg
                                    .getTestSuiteConfigRoot()
                                    + File.separator + fileName)) {
                                    fileName = cfg.getTestSuiteConfigRoot()
                                        + Constants.INTERNAL_FILE_SEP
                                        + fileName;
                                } else if (Util.checkExistFile(cfg
                                    .getTestSuiteRoot()
                                    + File.separator + fileName)) {
                                    fileName = cfg.getTestSuiteRoot()
                                        + Constants.INTERNAL_FILE_SEP
                                        + fileName;
                                }
                            }
                            if (!Util.checkExistFile(fileName)) {
                                fileName = null;
                                log.add(Level.FINE, methodLogPrefix
                                    + "Can't read file: " + fileName);
                            } else {
                                log.add(Level.FINE, methodLogPrefix
                                    + "file property is: " + fileName);
                            }
                        } catch (Exception e) {
                            log
                                .add(
                                    Level.FINE,
                                    methodLogPrefix
                                        + "unexpected exception while parse file name: "
                                        + e);
                        }
                    }
                }
            }
        }
    }

    public int find(String subsuite, String[] mask)
        throws ConfigurationException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tfind(): ";
        int retCode = super.find(subsuite, mask);
        //add last group if it contains tests
        if (groupTest.getRunnerParam().size() > 0) {
            parsedItems.add(groupTest);
            retCode++;
        }
        log.add(Level.INFO, methodLogPrefix + "Group number: " + retCode
            + "\tTests number: " + testCnt);
        return retCode;
    }

    protected void findFiles(File root, String[] mask) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tfindFiles(): ";
        File[] tmpStore;
        TestIR testIR;
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
                log.add(Level.CONFIG, methodLogPrefix
                    + "Can not read the directory " + root);
            }
        }
        if (root.getName().indexOf(TST_SUFFIX) != -1) {
            if (mask != null && mask.length > 0) {
                for (int i = 0; i < mask.length; i++) {
                    String tmp = root.getPath();
                    if (tmp.indexOf(File.separator) != -1) {
                        tmp = tmp.replace(File.separatorChar,
                            Constants.INTERNAL_FILE_SEP_C);
                    }
                    if (tmp.startsWith(mask[i])) {
                        return;
                    }
                }
            }
            try {
                testIR = tp.parser(root);
                // the test is placed not in the root directory
                if (tsRoot.length() < root.getParent().length()) {
                    testIR.setTestID((root.getParent().substring(tsRoot
                        .length() + 1)).replace(File.separatorChar,
                        Constants.INTERNAL_FILE_SEP_C)
                        + Constants.INTERNAL_FILE_SEP_C + testIR.getTestID());
                }
                if (testIR.getRunnerID() != null
                    && Main.getCurCore().getRunner(testIR.getRunnerID()) != null) {
                    updateTestGroup(testIR);
                    log.add(Level.CONFIG, methodLogPrefix + "Add to list: "
                        + root.getName());
                } else {
                    log.add(Level.CONFIG, methodLogPrefix
                        + "File was ignored: " + root.getName());
                }
            } catch (ParserException pe) {
                log.add(Level.WARNING, methodLogPrefix + "The file "
                    + root.getPath() + " has invalid format: " + pe);
            }
        } else {
            log.add(Level.FINEST, methodLogPrefix + "The file "
                + root.getName() + " was ignored due to extension.");
        }
    }

    protected boolean currentGroupFinish() {
        if (++curTestInGroupCnt == testInGroupCnt) {
            // assign initial value '-1' + 1 -> 0
            // because the first test added to group in the moment of creation
            // except first group
            curTestInGroupCnt = 0;
            return true;
        }
        return false;
    }

    protected void updateTestGroup(TestIR testIR) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tupdateTestGroup(): ";
        testCnt++;
        groupTest.setTestTimeout(groupTest.getTestTimeout()
            + testIR.getTestTimeout());
        testIR.setRunnerID(Main.getCurCore().getRunner(testIR.getRunnerID())
            .getClass().getName());
        if (currentGroupFinish()) {
            if (groupTest.getRunnerParam().size() > 0) {
                parsedItems.add(groupTest);
            }
            testGroupCnt++;
            groupTest = new TestIR(GROUP_RUNNER_NAME, GROUP_NAME + testGroupCnt);
            log.add(Level.CONFIG, methodLogPrefix + "New tests group created "
                + GROUP_NAME + testGroupCnt);
            ArrayList tmp = new ArrayList();
            tmp.add(testIR);
            groupTest.setRunnerParam(tmp);
            groupTest.setTestTimeout(testIR.getTestTimeout());
        } else {
            ArrayList tmp = groupTest.getRunnerParam();
            tmp.add(testIR);
            groupTest.setRunnerParam(tmp);
        }
        log.add(Level.CONFIG, methodLogPrefix + "Tests " + testIR.getTestID()
            + " added to group " + GROUP_NAME + testGroupCnt);
    }
}