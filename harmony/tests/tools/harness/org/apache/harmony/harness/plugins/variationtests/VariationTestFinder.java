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
 * @author Kirill Rodionov
 * @version $Revision: 1.11 $
 */
package org.apache.harmony.harness.plugins.variationtests;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TestIR;
import org.apache.harmony.harness.plugins.DirFinder;
import org.apache.harmony.harness.plugins.TestParser;

/**
 * This class handles a special kind of tests that contain 'variations'. It
 * splits the original test into a number of tests with all possible
 * combinations of variation parameters substituted. It is a 'TestFinder'
 * plugin.
 */
public class VariationTestFinder extends DirFinder implements VConstants {

    private static final String    classID     = "VariationTestFinder";

    //suffix for report files
    static final String            TST_SUFFIX  = ".xml";

    protected static DecimalFormat decFormat   = new DecimalFormat("00000",
                                                   new DecimalFormatSymbols(
                                                       Locale.US));

    protected boolean              needCPInit  = false;
    protected String               cpOption;

    protected int                  warnings;

    private TestParser             tp          = new TestParser();
    private String                 tsRoot      = Main.getCurCore()
                                                   .getConfigIR()
                                                   .getTestSuiteTestRoot();
    private ConfigIR               cfg         = Main.getCurCore()
                                                   .getConfigIR();
    protected TestIR               curTest     = null;
    boolean                        resolveProd = false;
    Product                        prod        = null;

    /*
     * @see org.apache.harmony.harness.plugins.TestFinder#setFindRoot(java.lang.String)
     */
    public String setFindRoot(String newValue) {
        String retVal = tsRoot;
        tsRoot = newValue;
        return retVal;
    }

    /*
     * @see org.apache.harmony.harness.Finder#find(java.lang.String,
     *      java.lang.String)
     */
    public int find(String subsuite, String[] mask)
        throws ConfigurationException {
        super.tsRoot = tsRoot;
        return super.find(subsuite, mask);
    }

    /*
     * This method does all the variation-specific work
     * 
     * @see org.apache.harmony.harness.plugins.DirFinder#findFiles(java.io.File,
     *      java.lang.String[])
     */
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
                curTest = testIR;
                // the test is placed not in the root directory
                if (tsRoot.length() < root.getParent().length()) {
                    testIR
                        .setTestID((root.getParent()
                            .substring(tsRoot.length() + 1))
                            .replace(
                                File.separatorChar,
                                org.apache.harmony.harness.Constants.INTERNAL_FILE_SEP_C)
                            + org.apache.harmony.harness.Constants.INTERNAL_FILE_SEP_C
                            + testIR.getTestID());
                }
                if (testIR.getRunnerID() != null
                    && Main.getCurCore().getRunner(testIR.getRunnerID()) != null) {

                    //variation stuff
                    warnings = 0;
                    String[] parms;

                    // Product handles the combining of variation parameters
                    // it's used to retrieve all possible combinations
                    prod = new Product();
                    Environment env = new Environment();
                    ArrayList variations = new ArrayList();
                    String[] cmd = null;
                    ArrayList[] arr = Util.toArrayListArr(testIR
                        .getRunnerParam());
                    ArrayList newRunnerParam = new ArrayList();
                    try {
                        //parsing specific, non-standard test parameters
                        for (int i = 1; i < arr.length; i++) {
                            parms = Util.toStringArr(arr[i]);
                            String parmName = parms[0];
                            String parmSurName = parms[1];
                            parms = Util.chop(parms, 2);

                            // substitute harness' standard variables and those
                            // passed with -execopt
                            processParams(parms);

                            if (parmName.equalsIgnoreCase("env")) {
                                // parse environment variables block
                                // (not used at this point)
                                boolean match = false;
                                if (parmSurName == null
                                    || parmSurName.length() == 0) {
                                    match = true;
                                } else {
                                    match = Pattern.matches(parmSurName, System
                                        .getProperty("os.name"));
                                }
                                if (match) {
                                    for (int j = 0; j < parms.length; j += 2) {
                                        env.add(parms[j], parms[j + 1]);
                                    }
                                }
                            } else if (parmName.equalsIgnoreCase("variation")) {
                                //just add the variation information to Product
                                // at
                                // this point,
                                //combinations will be retrieved later
                                IVariation var = (IVariation)getPlugin(parmSurName);
                                if (var != null) {
                                    parms = var.setup(parms);
                                    if (parms.length != 0) {
                                        return;
                                    }
                                    prod.add(var);
                                }
                            } else if (parmName.equalsIgnoreCase("parameter")) {
                                // parse static (as opposed to variations)
                                // parameters
                                // they are stored in TestIR and Storage plugin
                                // can
                                // retrieve
                                // them from the result later for its own needs
                                if ("config".equalsIgnoreCase(parmSurName)) {
                                    int j;
                                    for (j = 0; j < parms.length; j++) {
                                        String cfgname = parms[j];
                                        if (++j >= parms.length) {
                                            throw new SetupException(
                                                "Parameter name " + cfgname
                                                    + " without value");
                                        }
                                        testIR.setProperty(
                                            CFG_PREFIX + cfgname, parms[j]);
                                    }
                                }
                            } else if (parmName.equalsIgnoreCase("execution")) {
                                // parse execution block (see the method body)
                                ArrayList al = setupExecutionParams(testIR,
                                    parms);
                                newRunnerParam.add(al);
                            } else if (parmName.equalsIgnoreCase("torun")) {
                                // parse torun block (see the method body)
                                ArrayList al = setupRunParams(testIR,
                                    parmSurName, parms);
                                newRunnerParam.add(al);
                            } else {
                                log.add(Level.CONFIG, methodLogPrefix
                                    + "Unknown param: " + parmName);
                                return;
                            }
                        }
                    } catch (SetupException se) {
                        warning(se.getMessage());
                        return;
                    }

                    resolveProd = true;
                    try {
                        prod.init();
                    } catch (SetupException e) {
                        warning(e.getMessage());
                        return;
                    }

                    // the main cycle which is used to produce tests with unique
                    // combinations of variation parameters
                    // initially, new tests are cloned from the one that was
                    // returned by TestParser
                    for (int iterID = 0;; iterID++) {
                        curTest = (TestIR)testIR.clone();
                        curTest.setTestID(curTest.getTestID()
                            + decFormat.format(iterID));
                        for (int iv = 0; iv < prod.getSize(); iv++) {
                            ArrayList valist = new ArrayList();
                            valist.add(Util.toXML(prod.getVarTitle(iv)));
                            valist.add(Util.toXML(((IValue)prod.get(iv))
                                .getValue(prod.getVarName(iv)).toString()));
                            curTest.setProperty(VARIANT_PREFIX
                                + Util.toXML(prod.getVarName(iv)), valist
                                .clone());
                        }

                        //evaluate all variations
                        ArrayList processedParams = new ArrayList();
                        //stick an empty item in the beginning - required
                        processedParams.add(new ArrayList());
                        ArrayList[] nparams = Util
                            .toArrayListArr(newRunnerParam);

                        // al is the array that is added TestIR's runnerParam
                        // and al2 contains command and arguments which can be
                        // retrieved
                        // and used in another plugin (FilterStoreRes uses them,
                        // for
                        // example)
                        ArrayList al = new ArrayList();
                        ArrayList al2 = new ArrayList();
                        for (int npcount = 0; npcount < nparams.length; npcount++) {
                            al.clear();
                            al2.clear();
                            String[] exec_params = Util
                                .toStringArr(nparams[npcount]);

                            // substitute variation parameters
                            processParams(exec_params);
                            for (int pcount = 0; pcount < exec_params.length; pcount++) {
                                al.add(exec_params[pcount]);
                                al2.add(exec_params[pcount]);
                            }
                            //replace execution block title with a "toRun"
                            // instruction
                            al.set(0, "toRun");
                            processedParams.add(al.clone());
                            curTest.setProperty(RESOLVED_CMD_PREFIX
                                + decFormat.format(npcount), al2.clone());
                            al.clear();
                        }
                        curTest.setRunnerParam(processedParams);
                        parsedItems.add(curTest);
                        // check if there're any possible variation parameter
                        // combinations left
                        if (!prod.next())
                            break;
                    }
                    resolveProd = false;
                    prod = null;
                    log.add(Level.CONFIG, methodLogPrefix + "Add to list: "
                        + root.getName());
                } else {
                    log.add(Level.CONFIG, methodLogPrefix
                        + "File was ignored: " + root.getName());
                }
            } catch (ParserException pe) {
                log.add(Level.CONFIG, methodLogPrefix + "The file "
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

    // this method is used to obtain plugin class by name
    // (as specified by mapping in cfg.env.xml)
    private IPluggable getPlugin(String pluginName) {
        Class cls = null;
        IPluggable pi = null;
        String className = null;
        try {
            className = pluginName;
            cls = Class.forName(className);
        } catch (ClassNotFoundException e1) {
            try {
                className = decode(pluginName);
                cls = Class.forName(className);
            } catch (ClassNotFoundException e2) {
                return null;
            }
        }
        try {
            pi = (IPluggable)cls.newInstance();
        } catch (InstantiationException e1) {
            return null;
        } catch (IllegalAccessException e1) {
            return null;
        }
        return pi;
    }

    // this method resolves variation parameters specific for current iteration
    public String decode(String name) {
        String tmp = null;
        // Resolve product slot names
        if (prod != null && resolveProd) {
            IValue o = (IValue)prod.get(name);
            if (o != null) {
                return o.getValue(name).toString();
            }
        }
        tmp = decodePluginProperty(name, "VariationTestFinder");
        if (tmp != null) {
            return tmp;
        }

        String tmpStore;
        String retVal;
        try {
            if (name.startsWith(Constants.PARAM_START)) {
                tmpStore = name.substring(Constants.PARAM_START.length(), name
                    .length()
                    - Constants.PARAM_END.length());
            } else {
                tmpStore = name;
            }
            retVal = decodeProperty(tmpStore);
            if (retVal == null) {
                //return empty string for undefined variable
                retVal = cfg.getProperty(tmpStore);
            }
            if (retVal == null || retVal.length() == 0) {
                return name;
            } else {
                return retVal;
            }
        } catch (Exception e) { //name.length = paramStart.length for example
            return name;
        }
    }

    /*
     * This method decodes properties. The decoding way may depend on property
     * name.
     * 
     * @see org.apache.harmony.harness.ExecUnit#decodeProperty(java.lang.String)
     */
    protected String decodeProperty(String name) {
        if ("TestSuiteRoot".equalsIgnoreCase(name)) {
            return cfg.getTestSuiteRoot();
        } else if ("TestSuiteClassRoot".equalsIgnoreCase(name)) {
            return cfg.getTestSuiteClassRoot();
        } else if ("GenTimeout".equalsIgnoreCase(name)) {
            return "" + cfg.getGenTimeout();
        } else if ("TestDirectory".equalsIgnoreCase(name)) {
            return getTestDirectory();
        } else if ("FileSeparator".equalsIgnoreCase(name)) {
            return File.separator;
        } else if ("PathSeparator".equalsIgnoreCase(name)) {
            return File.pathSeparator;
        } else if ("TestedRuntime".equalsIgnoreCase(name)) {
            return cfg.getTestedRuntime();
        } else if ("TestedCompile".equalsIgnoreCase(name)) {
            return cfg.getTestedCompile();
        } else if ("TempStorage".equalsIgnoreCase(name)) {
            return cfg.getTempStorage();
        } else if ("CP".equalsIgnoreCase(name)) {
            return getCPOptions();
        } else {
            Method[] methods = cfg.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equalsIgnoreCase("get" + name)) {
                    try {
                        String tmp = (methods[i].invoke(cfg, null)).toString();
                        return tmp;
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /*
     * decode plugin property for plugin 'pluginName'
     * 
     * @see org.apache.harmony.harness.ExecUnit#decodePluginProperty(java.lang.String,
     *      java.lang.String)
     */
    protected String decodePluginProperty(String name, String pluginName) {
        HashMap hm = (HashMap)((HashMap)((ArrayList)cfg
            .getPluginProperties("TestFinder")).get(1)).get(pluginName);
        if (hm == null) {
            return null;
        }
        Object obj = hm.get(name);
        if (obj != null) {
            obj = ((ArrayList)obj).get(0);
            if (obj != null) {
                return obj.toString();
            }
        }
        return null;
    }

    /*
     * return the full name of the tests directory
     * 
     * @see org.apache.harmony.harness.ExecUnit#getTestDirectory()
     */
    protected String getTestDirectory() {
        String tmp = curTest == null ? null : curTest.getTestID();
        if (tmp == null) {
            return cfg.getTestSuiteTestRoot();
        }
        int val = tmp
            .lastIndexOf(org.apache.harmony.harness.Constants.INTERNAL_FILE_SEP_C);
        return cfg.getTestSuiteTestRoot()
            + org.apache.harmony.harness.Constants.INTERNAL_FILE_SEP_C
            + tmp.substring(0, val);
    }

    /*
     * return the current set for classpath option as: TestSuiteClassRoot +
     * TempStorage(for compiled classes) + all jar's from TestSuiteLibRoot +
     * system.classpath
     * 
     * @see org.apache.harmony.harness.ExecUnit#getCPOptions()
     */
    public String getCPOptions() {
        if (needCPInit == false) {
            needCPInit = true;
            cpOption = cfg.getTestSuiteClassRoot() + File.pathSeparator
                + cfg.getTempStorage();
            String libRootName = cfg.getTestSuiteLibRoot();
            File libRoot = new File(libRootName);
            if (libRoot.isDirectory()) {
                String[] files = libRoot.list();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].endsWith(".jar")) {
                        cpOption = cpOption + File.pathSeparator + libRootName
                            + File.separator + files[i];
                    }
                }
            }
            cpOption = cpOption + File.pathSeparator
                + System.getProperty("java.class.path");
        }
        return cpOption;
    }

    /*
     * This method parses execution block parameters The execution block can
     * contain output filters and must contain a title and command to execute
     * with optional arguments
     */
    public ArrayList setupExecutionParams(TestIR test, String[] args)
        throws SetupException {
        int unnamed_filter_count = 0;
        ArrayList tail = new ArrayList();
        String title = null;
        for (int i = 0; i < args.length; i++) {
            // filters are stored as TestIR's properties and can be used
            // by another plugin (FilterStoreRes is one)
            if (args[i].equalsIgnoreCase("-filter")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -filter without value");
                }
                String val = args[i];
                if (val == null || val.length() == 0) {
                    throw new SetupException("Empty value of -filter option");
                }
                int pos = val.indexOf('|');
                if (pos == -1) {
                    test.setProperty(FILTER_PREFIX
                        + decFormat.format(unnamed_filter_count++), val);
                } else {
                    if (pos == 0) {
                        throw new SetupException(
                            "Empty string from the left of '|' in -filter option");
                    }
                    if (pos == val.length() - 1) {
                        throw new SetupException(
                            "Empty string from the right of '|' in -filter option");
                    }
                    test.setProperty(FILTER_PREFIX + val.substring(0, pos), val
                        .substring(pos + 1));
                }
            } else if (args[i].equalsIgnoreCase("-title")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -title without value");
                }
                title = args[i];
            } else {
                tail.add(args[i]);
            }
        }
        if (title == null) {
            throw new SetupException("-title option is not specified");
        }
        tail.add(0, title);

        return tail;
    }

    /*
     * This method parses parameters to run no restriction for this block
     */
    public ArrayList setupRunParams(TestIR test, String classToRun,
        String[] args) throws SetupException {
        ArrayList tail = new ArrayList();
        for (int i = 0; i < args.length; i++) {
            tail.add(args[i]);
        }
        //set class name as run object
        tail.add(0, classToRun);
        //set 'java' as title
        tail.add(0, "java program");
        return tail;
    }

    public void warning(String msg) {
        log.add(Level.WARNING, classID + " WARNING:" + msg);
        warnings++;
    }

    /*
     * process all test parameters and substitute variables where necessary
     * 
     * @see org.apache.harmony.harness.ExecUnit#processParams
     */
    protected String[] processParams(String[] args) {
        String tmpVal;
        ArrayList tmpArr;
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            while (args[i].indexOf(Constants.PARAM_SEPARATOR) > -1) {
                tmpVal = subsValue(args[i]);
                if (args[i].equals(tmpVal)) {
                    break;
                } else {
                    args[i] = tmpVal;
                }
            }
        }
        tmpArr = cfg.getMapsList();
        if (tmpArr != null && tmpArr.size() > 0 && (tmpArr.size() % 2) == 0) {
            return mapPaths(args, tmpArr);
        }
        return args;
    }

    /*
     * Process a single string and substitute variables where necessary decode()
     * is used to resolve variables
     * 
     * @see org.apache.harmony.harness.ExecUnit#subsValue(java.lang.String)
     */
    protected String subsValue(String str) {
        String tmpStore;
        int varLength;
        int cnt = str.indexOf(Constants.PARAM_SEPARATOR);
        if (cnt == -1) {
            return str;
        }
        char[] thisSting = str.toCharArray();
        if (cnt == str.lastIndexOf(Constants.PARAM_SEPARATOR)) {
            //last(one) variable to change
            try {
                if (str.indexOf(Constants.PARAM_START) != -1) {
                    //calculate length for ${value} as index of first '}'
                    varLength = str.indexOf(Constants.PARAM_END)
                        + Constants.PARAM_END.length();
                } else if (str.startsWith(Constants.PARAM_SEPARATOR
                    + "FileSeparator")
                    || str.startsWith(Constants.PARAM_SEPARATOR
                        + "PathSeparator")) { //predefined
                    // names
                    varLength = Constants.PARAM_SEPARATOR.length()
                        + "FileSeparator".length(); //it has equal size
                } else {
                    //if no special symbols split it by predefined rules
                    varLength = getVarLength(cnt
                        + Constants.PARAM_SEPARATOR.length(), thisSting);
                }
                String tmp = decode(str.substring(cnt
                    + Constants.PARAM_SEPARATOR.length(), varLength));
                if (tmp != null
                    && !tmp.equals(str.substring(cnt
                        + Constants.PARAM_SEPARATOR.length(), varLength))) {
                    tmpStore = str.substring(0, cnt) + tmp
                        + str.substring(varLength);
                } else {
                    return str;
                }
            } catch (Exception e) { //missed '}' for example
                return str;
            }
        } else {
            tmpStore = str.substring(0, cnt);
            StringTokenizer stT = new StringTokenizer(str.substring(cnt
                + Constants.PARAM_SEPARATOR.length()),
                Constants.PARAM_SEPARATOR);
            while (stT.hasMoreElements()) {
                tmpStore = tmpStore
                    + subsValue(Constants.PARAM_SEPARATOR
                        + (String)stT.nextElement());
            }
        }
        return tmpStore;
    }

    /*
     * return the length of the string to decode as property name the end of
     * name is '$' or any symbol that is not a java identifier (except '.')
     * 
     * @see org.apache.harmony.harness.ExecUnit#getVarLength
     */
    protected int getVarLength(int start, char[] arr) {
        if (arr == null) {
            return 0;
        }
        for (int i = start; i < arr.length; i++) {
            if ((!Character.isJavaIdentifierPart(arr[i]) && arr[i] != '.')
                || arr[i] == '$') {
                return i;
            }
        }
        return arr.length;
    }

    /*
     * replace all occurrences of the even strings (0, 2 ...) on the odd string
     * (1, 3...) from mapList into arguments array
     * 
     * @see org.apache.harmony.harness.ExecUnit#mapPaths
     */
    protected String[] mapPaths(String[] args, ArrayList mapList) {
        String from;
        String to;
        for (int i = 0; i < args.length; i++) {
            for (int cnt = 0; cnt < mapList.size(); cnt = cnt + 2) {
                from = (String)mapList.get(cnt);
                if (args[i].indexOf(from) != -1) {
                    to = (String)mapList.get(cnt + 1);
                    args[i] = myReplaceAll(args[i], from, to);
                }
            }
        }
        return args;
    }

    /*
     * replace all occurrences of string 'from' in 'data' with string 'to'
     * 
     * @see org.apache.harmony.harness.ExecUnit#myReplaceAll
     */
    protected String myReplaceAll(String data, String from, String to) {
        String retVal = "";
        int index;
        if (data == null) {
            return data;
        }
        while (data.indexOf(from) != -1) {
            index = data.indexOf(from);
            retVal = retVal + data.substring(0, index) + to;
            data = data.substring(index + from.length());
        }
        retVal = retVal + data;
        return retVal;
    }
}

/*
 * This class stores environment variables specified in 'env' block (not used at
 * this point)
 */

class Environment {
    ArrayList arr = new ArrayList();

    public void add(String name, String value) {
        arr.add(name + "=" + value);
    }

    String[] getEnv() {
        return (String[])arr.toArray(new String[0]);
    }
}