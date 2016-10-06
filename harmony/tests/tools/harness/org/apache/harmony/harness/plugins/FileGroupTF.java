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
 * @version $Revision: 1.3 $
 */
package org.apache.harmony.harness.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TestIR;
import org.apache.harmony.harness.Util;

public class FileGroupTF extends GroupTestFinder {

    private final String       classID          = "FileGroupTF";

    public static final String NEW_GROUP_MARKER = "NewGroup:";

    private boolean            groupFinished    = false;

    protected void findFiles(File root, String[] mask) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tfindFiles(): ";
        if (fileName == null || !Util.checkExistFile(fileName)) {
            super.findFiles(root, mask);
            return;
        }
        TestIR testIR;
        BufferedReader fr = null;
        try {
            fr = new BufferedReader(new FileReader(fileName));
            while (fr.ready()) {
                String data = fr.readLine().trim();
                if (data != null) {
                    if (data.indexOf(Constants.START_COMMENT) != -1) {
                        data = data.substring(0,
                            data.indexOf(Constants.START_COMMENT)).trim();
                    }
                    if (data.indexOf(TST_SUFFIX) != -1) {
                        try {
                            if (Util.checkExistFile(data)) {
                                root = new File(data);
                            } else if (Util.checkExistFile(cfg
                                .getTestSuiteTestRoot()
                                + Constants.INTERNAL_FILE_SEP + data)) {
                                root = new File(cfg.getTestSuiteTestRoot()
                                    + Constants.INTERNAL_FILE_SEP + data);
                            }
                            testIR = tp.parser(root);
                            if (data.indexOf(File.separatorChar) != -1) {
                                data.replace(File.separatorChar,
                                    Constants.INTERNAL_FILE_SEP_C);
                            }
                            testIR.setTestID(data);
                            if (testIR.getRunnerID() != null
                                && Main.getCurCore().getRunner(
                                    testIR.getRunnerID()) != null) {
                                updateTestGroup(testIR);
                                log.add(Level.CONFIG, methodLogPrefix
                                    + "Add to list: " + data);
                            } else {
                                log.add(Level.CONFIG, methodLogPrefix
                                    + "No runner for test: " + data);
                            }
                        } catch (ParserException pe) {
                            log.add(Level.WARNING, methodLogPrefix
                                + "The file " + root.getPath()
                                + " has invalid format: " + pe);
                        } catch (Exception e) {
                            log.add(Level.WARNING, methodLogPrefix
                                + "Can't read file: " + data + "\texception: "
                                + e);
                        }
                    } else if (data.startsWith(NEW_GROUP_MARKER)) {
                        groupFinished = true;
                    } else {
                        log.add(Level.FINEST, methodLogPrefix + "The record "
                            + data + " was ignored due to extension.");
                    }
                }
            }
        } catch (Exception e) {
            log.add(Level.CONFIG, methodLogPrefix
                + "Can't read file with group descriptions: " + e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    log
                        .add(Level.CONFIG, methodLogPrefix
                            + "Can't close file: " + fileName + "\texception: "
                            + e);
                }
            }
        }
    }

    protected boolean currentGroupFinish() {
        if (groupFinished) {
            groupFinished = false;
            return true;
        }
        return false;
    }
}