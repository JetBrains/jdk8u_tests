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
 * @version $Revision: 1.17 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;

public class ResFinder extends DirFinder {

    private final String classID      = "ResFinder";

    private ResParser    rp           = new ResParser();

    private String       tsRoot       = Main.getCurCore().getConfigIR()
                                          .getTestResultRoot();

    private String       resExtension = Constants.RESULTS_EXT;

    public ResFinder() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tResFinder(): ";
        try {
            resExtension = Main.getCurCore().getStore().getResultExtension();
        } catch (Exception e) {
            log.add(Level.CONFIG, methodLogPrefix + "unexpected exception: "
                + e);
        }
    }

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
        if (root.getName().endsWith(resExtension)) {
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
                parsedItems.add(rp.parser(root));
                log.add(Level.CONFIG, methodLogPrefix + "Add to results list: "
                    + root.getName());
            } catch (ParserException pe) {
                log.add(Level.CONFIG, methodLogPrefix
                    + "The file has invalid format: " + root.getPath());
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
            if (!testName.endsWith(resExtension)) {
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
                //do nothing
            }
        }
        return false;
    }
}
