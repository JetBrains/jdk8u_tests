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
 * @version $Revision: 1.21 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TestIR;
import org.apache.harmony.harness.Util;

public class TestFinder extends DirFinder {

    private final String classID    = "TestFinder";

    //suffix for the test files
    static final String  TST_SUFFIX = ".xml";

    protected TestParser tp         = new TestParser();
    protected String     tsRoot     = Main.getCurCore().getConfigIR()
                                        .getTestSuiteTestRoot();

    public String setFindRoot(String newValue) {
        String retVal = tsRoot;
        tsRoot = newValue;
        return retVal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#find(java.lang.String, java.lang.String)
     */
    public int find(String subsuite, String[] mask)
        throws ConfigurationException {
        super.tsRoot = tsRoot;
        return super.find(subsuite, mask);
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
        if (root.getName().endsWith(TST_SUFFIX)) {
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
                printTestInfo(testIR);
                parsedItems.add(testIR);
                log.add(Level.CONFIG, methodLogPrefix + "Add to list: "
                    + root.getName());
            } catch (ParserException pe) {
                log.add(Level.WARNING, methodLogPrefix + "The file "
                    + root.getPath() + " has invalid format: " + pe);
            }
        } else {
            log.add(Level.FINEST, methodLogPrefix + "The file "
                + root.getName() + " was ignored due to extension.");
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
            if (!testName.endsWith(TST_SUFFIX)) {
                return false;
            }
            fl = new File(testName);
            if (fl.isFile() && tp.parser(fl) != null) {
                return true;
            }
        } catch (Exception e) {
            try {
                fl = new File(tsRoot + File.separator + testName);
                if (fl.isFile() && tp.parser(fl) != null) {
                    return true;
                }
            } catch (Exception e1) {
                //do nothing
            }
        }
        return false;
    }

    private void printTestInfo(TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tprintTestInfo(): ";
        if (log.getLevel().intValue() <= Level.FINER.intValue()) {
            try {
                if (test != null && test.getTestID() != null
                    && test.getTestID().endsWith("null")) {
                    log.add(methodLogPrefix + "Test candidate: "
                        + test.getTestID());
                    if (test.getRunnerID() != null) {
                        log.add(methodLogPrefix + "\ttest runner: "
                            + test.getRunnerID());
                        ArrayList tmp = test.getAuthors();
                        if (tmp != null) {
                            for (int i = 0; i < tmp.size(); i++) {
                                log.add(methodLogPrefix + "\ttest author: "
                                    + tmp.get(i));
                            }
                        }
                        try {
                            tmp = (ArrayList)test
                                .getProperty((Object)"sources");
                            if (tmp != null && tmp.size() > 0) {
                                for (int i = 0; i < tmp.size(); i++) {
                                    String srcbase = ".";
                                    if (test.getTestID().indexOf(
                                        Constants.INTERNAL_FILE_SEP_C) != -1) {
                                        srcbase = test.getTestID().substring(
                                            0,
                                            test.getTestID().lastIndexOf(
                                                Constants.INTERNAL_FILE_SEP_C));
                                    }
                                    if (!Util.checkExistFile(Main.getCurCore()
                                        .getConfigIR().getTestSuiteTestRoot()
                                        + Constants.INTERNAL_FILE_SEP_C
                                        + srcbase
                                        + Constants.INTERNAL_FILE_SEP_C
                                        + tmp.get(i))) {
                                        log
                                            .add("\tinccorect link to the test source for "
                                                + test.getTestID()
                                                + ": "
                                                + tmp.get(i));
                                    } else {
                                        log.add("\ttest sources OK: "
                                            + tmp.get(i));
                                    }
                                }
                            } else {
                                log.add("\ttest sources is undefined for "
                                    + test.getTestID());
                            }
                        } catch (Exception se) {
                            log.add(Level.FINER, methodLogPrefix
                                + MessageInfo.UNEX_EXCEPTION
                                + "while check sources: " + se, se);
                        }
                    }
                }
            } catch (Exception e) {
                log.add(Level.FINER, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION
                    + "while print the test information: " + e, e);
            }
        }
    }
}
