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
 * @author Y.Tokpanov
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.16 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TestIR;

public class JUFinder extends DirFinder {

    private final String       classID          = "JUFinder";

    public static final String TST_SUFFIX       = ".class";
    public static final String ARC_SUFFIX       = ".jar";
    public static final String PLUGIN_NAME      = "TestFinder";
    public static final String PARAMETR_NAME    = "InnerClass";
    public static final String PROPERTY_NAME    = "ignore";
    public static final char   INER_CLASS_SIGN  = '$';

    protected JUParser         jup              = new JUParser();
    protected ConfigIR         cfg              = Main.getCurCore()
                                                    .getConfigIR();
    protected String           tsRoot           = cfg.getTestSuiteClassRoot();

    protected boolean          ignoreInnerClass = false;

    public String setFindRoot(String newValue) {
        String retVal = tsRoot;
        tsRoot = newValue;
        return retVal;
    }

    public JUFinder() {
        super();
        ArrayList finderProp = (ArrayList)cfg.getPlugins().get(PLUGIN_NAME);
        if (finderProp.size() > 1) {
            HashMap finderHM = (HashMap)finderProp.get(1);
            if (finderHM.containsKey(PARAMETR_NAME)) {
                HashMap tmp = (HashMap)finderHM.get(PARAMETR_NAME);
                if (tmp.containsKey(PROPERTY_NAME)) {
                    finderProp = (ArrayList)tmp.get(PROPERTY_NAME);
                    if (finderProp.size() >= 1
                        && ((String)finderProp.get(0)).startsWith("true")) {
                        ignoreInnerClass = true;
                    }
                }
            }
        }
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
        log.add(Level.FINEST, methodLogPrefix + "path to search is " + root);
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
                    + "can not read the directory " + root);
            }
        }
        if (root.getName().indexOf(TST_SUFFIX) != -1) {
            log.add(Level.FINEST, methodLogPrefix + "try to find test into "
                + root);
            if (mask != null && mask.length > 0) {
                for (int i = 0; i < mask.length; i++) {
                    String tmp = root.getPath();
                    if (tmp.indexOf(File.separatorChar) != -1) {
                        tmp = tmp.replace(File.separatorChar,
                            Constants.INTERNAL_FILE_SEP_C);
                    }
                    if (tmp.startsWith(mask[i])) {
                        return;
                    }
                }
            }
            try {
                if (ignoreInnerClass == true
                    && root.getName().indexOf(INER_CLASS_SIGN) != -1) {
                    log.add(Level.CONFIG, methodLogPrefix
                        + "was ignored as inner class: " + root.getName());
                } else {
                    log.add(Level.FINEST, methodLogPrefix + "try to parse: "
                        + root.getName());
                    testIR = jup.parser(root);
                    if (testIR != null) {
                        parsedItems.add(testIR);
                        log.add(Level.INFO, methodLogPrefix + "add to list: "
                            + root.getName());
                    }
                }
            } catch (ParserException pe) {
                log.add(Level.INFO, methodLogPrefix + "the file "
                    + root.getPath() + " has invalid format: " + pe);
            }
        } else if (root.getName().indexOf(ARC_SUFFIX) != -1) {
            log.add(Level.FINEST, methodLogPrefix
                + "try to find tests into archive " + root);
            try {
                JarFile tstArch = new JarFile(root);
                Enumeration tstEntries = tstArch.entries();
                log.add(Level.FINE, methodLogPrefix + "archive name: "
                    + root.getName());
                int cnt = 0;
                while (tstEntries.hasMoreElements()) {
                    cnt++;
                    JarEntry tstObj = (JarEntry)tstEntries.nextElement();
                    String name = tstObj.getName();
                    String className;
                    log.add(Level.FINEST, methodLogPrefix + "analyze: " + name);
                    if (name.endsWith(TST_SUFFIX)) {
                        //according to the Info-ZIP Application Note 970311
                        //(package description for the java.util.zip)
                        //filename: (Variable)
                        //The name of the file, with optional relative path.
                        //The path stored should not contain a drive or
                        //device letter, or a leading slash. All slashes
                        //should be forward slashes '/' as opposed to
                        //backwards slashes '\' for compatibility with Amiga
                        //and Unix file systems etc. If input came from
                        // standard
                        //input, the file name is set to "-" (without the
                        // quotes).
                        // So this replace is correct
                        className = (name.substring(0, name.length()
                            - TST_SUFFIX.length())).replace('/', '.');
                        log.add(Level.FINEST, methodLogPrefix + "analyze: "
                            + className);
                        try {
                            if (ignoreInnerClass == true
                                && className.indexOf(INER_CLASS_SIGN) != -1) {
                                log.add(Level.CONFIG, methodLogPrefix
                                    + "was ignored as inner class: "
                                    + className);
                            } else {
                                log.add(Level.FINEST, methodLogPrefix
                                    + "try to parse: " + className);
                                testIR = jup.parser(className);
                                if (testIR != null) {
                                    parsedItems.add(testIR);
                                    log.add(Level.INFO, methodLogPrefix
                                        + "add to list: " + className);
                                } else {
                                    log.add(Level.FINEST, methodLogPrefix
                                        + "parser can not parse: " + className);
                                }
                            }
                        } catch (ParserException pe) {
                            log.add(Level.INFO, methodLogPrefix + "the file "
                                + name + " has invalid format: " + pe);
                        }
                    }
                }
                log.add(Level.FINE, methodLogPrefix + "archive : "
                    + root.getName() + " has " + cnt + " entry");
            } catch (Exception e) {
                log.add(Level.WARNING, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + "while work with file "
                    + root.getPath() + ": " + e);
            }
        } else {
            log.add(Level.FINEST, methodLogPrefix + "the file "
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
            if (testName.indexOf(ARC_SUFFIX) != -1) {
                return true;
            }
            if (testName.indexOf(TST_SUFFIX) == -1) {
                return false;
            }
            fl = new File(testName);
            if (fl.isFile() && jup.parser(fl) != null) {
                return true;
            }
        } catch (Exception e) {
            try {
                fl = new File(tsRoot + File.separator + testName);
                if (fl.isFile() && jup.parser(fl) != null) {
                    return true;
                }
            } catch (Exception e1) {
                //return false;
            }
        }
        return false;
    }
}
