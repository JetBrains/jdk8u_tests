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
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Finder;
import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;

public abstract class DirFinder implements Finder {

    private final String classID     = "DirFinder";

    protected List       parsedItems = new LinkedList();
    protected Logging    log         = Main.getCurCore().getInternalLogger();
    protected String     tsRoot;
    protected boolean    activeFlag  = false;

    public String setFindRoot(String newValue) {
        String retVal = tsRoot;
        tsRoot = newValue;
        return retVal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#find()
     */
    public int find() throws ConfigurationException {
        return find(null, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#find(java.lang.String)
     */
    public int find(String subsuite) throws ConfigurationException {
        return find(subsuite, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#find(java.lang.String, java.lang.String)
     */
    public int find(String subsuite, String[] mask)
        throws ConfigurationException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tfind(): ";
        String fullPath = tsRoot;
        if (fullPath == null) {
            throw new ConfigurationException(methodLogPrefix
                + "test root undefined");
        }
        if (fullPath.indexOf(File.separator) != -1) {
            fullPath = fullPath.replace(File.separatorChar,
                Constants.INTERNAL_FILE_SEP_C);
        }
        log.add(Level.CONFIG, methodLogPrefix + "test root is " + fullPath);
        String[] updatedMask = null;
        if (mask != null) {
            updatedMask = new String[mask.length];
            for (int i = 0; i < mask.length; i++) {
                if (fullPath.endsWith(Constants.INTERNAL_FILE_SEP)) {
                    updatedMask[i] = fullPath + mask[i];
                } else {
                    updatedMask[i] = fullPath + Constants.INTERNAL_FILE_SEP_C
                        + mask[i];
                }
            }
        }
        if (subsuite != null) {
            fullPath = fullPath + Constants.INTERNAL_FILE_SEP_C + subsuite;
        }
        log.add(Level.INFO, methodLogPrefix + "path to search tests is "
            + fullPath);
        activeFlag = true;
        findFiles(new File(fullPath), updatedMask);
        activeFlag = false;
        return parsedItems.size();
    }

    protected abstract void findFiles(File root, String[] mask);

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#getNext()
     */
    public Object getNext() {
        synchronized (parsedItems) {
            if (parsedItems.isEmpty()) {
                return null;
            }
            return parsedItems.remove(0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#getCurSize()
     */
    public int getCurSize() {
        if (!parsedItems.isEmpty()) {
            return parsedItems.size();
        }
        return 0;
    }

    public void clear() {
        synchronized (parsedItems) {
            parsedItems.clear();
        }
    }

    public List getTestList() {
        return parsedItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#isActive()
     */
    public boolean isActive() {
        return activeFlag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Finder#stop()
     */
    public void stop() {
        // while do nothing
    }
}
