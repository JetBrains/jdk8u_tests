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
 * @version $Revision: 1.13 $
 */
package org.apache.harmony.harness.ReportTool;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;

public class BugNumberProviderFile implements BugNumberProvider {

    private static final String classID       = "BugNumberProviderFile";

    public static final String  BUG_ID_START  = "<a href=\"http://bugtrack/show_bug.cgi?id=";
    public static final String  BUG_ID_MIDDLE = "\">";
    public static final String  BUG_ID_END    = "</a>";

    protected Logging           log           = Main.getCurCore()
                                                  .getInternalLogger();

    HashMap                     knownBugs     = new HashMap(120);

    private String              eflFileName   = null;

    /*
     * read the information about bugs from a pointed file. The file format is:
     * one string for one bug with fields separated by 'tab' char testID + OS +
     * architecture + execution_mode + bugID + reason + comment
     */
    public BugNumberProviderFile(String fileName) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tBugNumberProviderFile(): ";
        BufferedReader in = null;
        if (fileName == null || fileName.length() < 1) {
            return;
        }
        try {
            File file = new File(fileName);
            if (file.isFile()) {
                in = new BufferedReader(new FileReader(file));
                while (in.ready()) {
                    String bugRecord = in.readLine();
                    StringTokenizer st = new StringTokenizer(bugRecord, "\t");
                    String testID = st.nextToken();
                    BugInfo bInfo = null;
                    try {
                        bInfo = new BugInfo(st.nextToken(), st.nextToken(), st
                            .nextToken(), st.nextToken());
                        bInfo.reason = st.nextToken();
                        bInfo.comment = st.nextToken();
                    } catch (NoSuchElementException ne) {
                        //do nothing
                    }
                    if (bInfo != null && bInfo.isValid()) {
                        addBug(testID, bInfo);
                    }
                }
            }
            eflFileName = fileName;
        } catch (Exception e) {
            log.add(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    log.add(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e1);
                }
            }
        }
    }

    protected void addBug(String id, BugInfo bInfo) {
        if (id == null || bInfo == null) {
            return;
        }
        ArrayList bList;
        String testID = id.replace('\\', Constants.INTERNAL_FILE_SEP_C);
        if (knownBugs.containsKey(testID)) {
            bList = (ArrayList)knownBugs.get(testID);
        } else {
            bList = new ArrayList();
        }
        bList.add(bInfo);
        knownBugs.put(testID, bList);
    }

    public String wrapBug(String bugID) {
        return BUG_ID_START + bugID + BUG_ID_MIDDLE + bugID + BUG_ID_END;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ReportTool.BugNumberProvider#getBugNumber(java.lang.String)
     */
    public String getAllBugNumbers(String testName) {
        if (knownBugs.containsKey(testName)) {
            ArrayList bList = (ArrayList)knownBugs.get(testName);
            String retVal = wrapBug(((BugInfo)bList.get(0)).bugID);
            int i = 1;
            for (; i < bList.size(); i++) {
                BugInfo bInfo = (BugInfo)bList.get(i);
                if (retVal.indexOf(bInfo.bugID) == -1) {
                    retVal = retVal + "," + wrapBug(bInfo.bugID);
                }
            }
            return retVal;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ReportTool.BugNumberProvider#getBugNumber(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public String getBugNumber(String testName, String os, String arch,
        String execMode) {
        return null;
    }

    class BugInfo {
        String bugID;
        String os;
        String arch;
        String executionMode;
        String reason;
        String comment;

        BugInfo(String os, String arch, String mode, String bug) {
            bugID = bug;
            this.os = os;
            this.arch = arch;
            executionMode = mode;
        }

        boolean isValid() {
            if (bugID != null && os != null && arch != null
                && executionMode != null) {
                return true;
            }
            return false;
        }
    }
}