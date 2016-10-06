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
 * @version $Revision: 1.47 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.apache.harmony.harness.ReportTool.CmpResults;
import org.apache.harmony.harness.ReportTool.ScriptGen;
import org.apache.harmony.harness.plugins.JUFinder;
import org.apache.harmony.harness.plugins.Reporter;
import org.apache.harmony.harness.plugins.ResFinder;
import org.apache.harmony.harness.plugins.StoreRes;
import org.apache.harmony.harness.plugins.THLogger;
import org.apache.harmony.harness.plugins.TestFinder;
import org.apache.harmony.harness.synchannel.SyncChannelServer;

public class Main {

    private final String        classID              = "Main";

    public static final boolean ACTIVE               = true;

    public static final boolean PASSIVE              = false;

    public static final boolean SAME                 = true;

    public static final boolean OTHER                = false;

    public static final boolean LOCAL                = true;

    public static final boolean REMOTE               = false;

    public static final int     MAJOR_VERSION        = 0;

    public static final int     MINOR_VERSION        = 20060516;

    public static final String  VERSION_STRING       = " (May 16, 2006)";

    public static final String  EXCLUDE_FILE_NAME    = "exclude.file";

    protected static Main       curCore;

    protected ConfigIR          cfgM;

    protected Configurator      cfg;

    protected MCPool            clients;

    protected ComServer[]       cs;

    protected Logging           log;

    protected Logging           internalLog;

    protected Report            rep;

    protected Finder            rf;

    protected Finder            tf;

    protected Finder            juf;

    protected Selector          selector;

    protected Dispatcher        dsp;

    protected Storage           store;

    protected ExecListIR        execList;

    protected ExecStatusIR      execStat;

    protected ExecUnitPool      exUnits              = new ExecUnitPool();

    protected String            configName;

    protected String            loggerName;

    boolean                     guiMode              = false;

    boolean                     useSyncChannelServer = true;

    protected TestSuiteIR       tstSuite;

    protected SyncChannelServer ss;

    protected ArrayList         overWriteOptions     = new ArrayList();

    protected ArrayList         overWriteExecOptions = new ArrayList();

    protected String[]          subSuites;

    protected String[]          excludeSuites;

    protected String[]          jarFiles;

    protected String[]          includeTestFiles;

    protected String[]          excludeTestFiles;

    protected ArrayList         extProcs             = new ArrayList();

    protected String            cfgStorePath         = System
                                                         .getProperty("user.dir")
                                                         + File.separator
                                                         + "config";

    protected Object            synObj               = new Object();

    protected boolean           fail_on_error        = false;

    public static Main getCurCore() {
        return curCore;
    }

    public static void main(String[] args) {
        System.exit(new Main().run(args));
    }

    /**
     * Create the instance of the configuration and store the link into internal
     * field
     * 
     * @throws NullPointerException if the logger not initialized
     */
    protected void createConfigurator() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateConfigurator(): ";
        if (configName != null) {
            try {
                Object obj = Class.forName(configName).newInstance();
                if (!(obj instanceof Configurator)) {
                    internalLog
                        .add(
                            Level.WARNING,
                            methodLogPrefix
                                + "Incorrect name for configuration plug-in. Use default.");
                    cfg = null;
                } else {
                    cfg = (Configurator)obj;
                    internalLog.add(Level.CONFIG, methodLogPrefix
                        + "Configurator class is " + configName);
                }
            } catch (Exception e) {
                internalLog
                    .add(
                        Level.WARNING,
                        methodLogPrefix
                            + "unexpected exception "
                            + e
                            + "\nIncorrect name for configuration plug-in. Use default.");
                cfg = null;
            }
        }
        if (cfg == null) {
            cfg = new Config();
        }
    }

    /**
     * Set the instance of the ConfigIR
     */
    public void setConfigIR(ConfigIR value) {
        if (value != null) {
            cfgM = value;
        } else {
            cfgM = new ConfigIR();
        }
    }

    /**
     * Create the instance of logger and store the link into internal field
     * 
     * @return true if logger is founded and initialized, otherwise false
     */
    protected boolean createLog() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateLog(): ";
        Level curLevel = Level.INFO;
        if (loggerName != null) { // was set by command line
            try {
                Object obj = Class.forName(loggerName).newInstance();
                if (!(obj instanceof Logging)) {
                    internalLog
                        .add(methodLogPrefix
                            + "Incorrect name for logger (pointed class is not instanceof Logging). Use default.");
                    loggerName = null;
                } else {
                    log = (Logging)obj;
                }
            } catch (Exception e) {
                internalLog.add(methodLogPrefix + "unexpected exception: " + e
                    + "\nIncorrect name for logger. Use default.");
                loggerName = null;
            }
        } else if (cfgM != null) {
            ArrayList logCfg = (ArrayList)cfgM.getPluginProperties("Logger");
            if (logCfg.size() > 0) {
                loggerName = (String)logCfg.get(0);
                if (loggerName != null) {
                    try {
                        Object obj = Class.forName(loggerName).newInstance();
                        if (!(obj instanceof Logging)) {
                            internalLog.add(Level.WARNING, methodLogPrefix
                                + "Incorrect name for logger. Use default.");
                            loggerName = null;
                        } else {
                            log = (Logging)obj;
                        }
                    } catch (Exception e) {
                        internalLog.add(Level.WARNING, methodLogPrefix
                            + "Incorrect name for logger. Use default.");
                        loggerName = null;
                    }
                }
            }
            if (logCfg.size() > 1) {
                if (logCfg.get(1) != null
                    && ((HashMap)logCfg.get(1)).size() > 0) {
                    try {
                        cfgM
                            .setLogLevel((String)((ArrayList)((HashMap)((HashMap)logCfg
                                .get(1)).get("Level")).get("level")).get(0));
                    } catch (IllegalArgumentException e) {
                        internalLog
                            .add(
                                Level.WARNING,
                                methodLogPrefix
                                    + "Incorrect value for level of logging. Use default (info).");
                        cfgM.setLogLevel("INFO");
                    }
                }
            }
            curLevel = cfgM.getLogLevel();
        }
        if (log == null) {
            log = new THLogger();
        }
        log.init(curLevel);
        return true;
    }

    /**
     * Create the instance of reporter and store the link into internal field
     */
    protected void createReporter() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateReporter(): ";
        ArrayList repCfg = (ArrayList)cfgM.getPluginProperties("Reporter");
        if (repCfg.size() > 0) {
            String className = (String)repCfg.get(0);
            if (className != null) {
                try {
                    Object obj = Class.forName(className).newInstance();
                    if (!(obj instanceof Report)) {
                        internalLog.add(Level.WARNING, methodLogPrefix
                            + "Incorrect name for reporter. Use default.");
                        rep = null;
                    } else {
                        rep = (Report)obj;
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Reporter class is " + className);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Incorrect name for reporter. Use default.");
                    rep = null;
                }
            }
        }
        if (rep == null) {
            rep = new Reporter();
        }
    }

    /**
     * Create the instance of result finder and store the link into internal
     * field
     */
    protected void createResFinder() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateResFinder(): ";
        ArrayList rfCfg = (ArrayList)cfgM.getPluginProperties("ResFinder");
        if (rfCfg.size() > 0) {
            String className = (String)rfCfg.get(0);
            if (className != null) {
                try {
                    Object obj = Class.forName(className).newInstance();
                    if (!(obj instanceof Finder)) {
                        internalLog.add(Level.WARNING, methodLogPrefix
                            + "Incorrect name for finder. Use default.");
                        rf = null;
                    } else {
                        rf = (Finder)obj;
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Finder class is " + className);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Incorrect name for finder. Use default.");
                    rf = null;
                }
            }
        }
        if (rf == null) {
            rf = new ResFinder();
        }
    }

    /**
     * Create the instances of runners and store the links into internal fields
     */
    protected void createRunners() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateRunners(): ";
        ExecUnit runner;
        if (((ArrayList)cfgM.getPluginProperties("ExecUnit")).size() < 2) {
            internalLog.add(Level.SEVERE, methodLogPrefix
                + "Incorrect configuration for the execution units");
        }
        HashMap runnerCfg = (HashMap)((ArrayList)cfgM
            .getPluginProperties("ExecUnit")).get(1);
        internalLog
            .add(Level.CONFIG, "runnerCfg.size() is " + runnerCfg.size());
        if (runnerCfg.size() > 0) {
            for (Iterator it = runnerCfg.keySet().iterator(); it.hasNext();) {
                Object key = it.next();
                String className = (String)((HashMap)runnerCfg.get(key))
                    .get("map-class");
                internalLog.add(Level.FINE, "plugins name is " + className);
                if (className != null) {
                    try {
                        Object obj = Class.forName(className).newInstance();
                        internalLog.add(Level.FINER, methodLogPrefix + "Class "
                            + className + " was instantiate OK");
                        if (!(obj instanceof ExecUnit)) {
                            internalLog.add(Level.WARNING, methodLogPrefix
                                + "Incorrect name for runner. Use default.");
                            runner = null;
                        } else {
                            runner = (ExecUnit)obj;
                            internalLog.add(Level.CONFIG, methodLogPrefix
                                + "Runner class is " + className);
                        }
                    } catch (Exception e) {
                        internalLog.add(Level.WARNING, methodLogPrefix
                            + "Incorrect name for runner. Use default.");
                        runner = null;
                    }
                    if (runner != null) {
                        exUnits.add(key, runner);
                    }
                } else {
                    internalLog.add(Level.SEVERE, methodLogPrefix
                        + "Invalid class name for ExecUnit " + key.toString());
                }
            }
        }
    }

    public ExecUnit getRunner(String name) {
        return exUnits.get(name);
    }

    public ExecUnit[] getRunnerSet(String name) {
        return exUnits.getSet(name);
    }

    ExecUnitPool getExecUnitPool() {
        return exUnits;
    }

    /**
     * Create the instance of selector and store the link into internal field
     */
    protected void createSelector() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateSelector(): ";
        ArrayList selectorCfg = (ArrayList)cfgM.getPluginProperties("Selector");
        if (selectorCfg.size() > 0) {
            String className = (String)selectorCfg.get(0);
            if (className != null && className.length() > 0) {
                try {
                    Object obj = Class.forName(className).newInstance();
                    if (!(obj instanceof Selector)) {
                        internalLog.add(Level.WARNING, methodLogPrefix
                            + "Incorrect name for selector. Use default.");
                        selector = null;
                    } else {
                        selector = (Selector)obj;
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Selector class is " + className);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Incorrect name for selector. Use default.");
                    selector = null;
                }
            }
        }
        if (selector == null) {
            selector = new Selector();
        }
    }

    /**
     * Create the instance of store result and store the link into internal
     * field
     */
    protected void createStore() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateSelector(): ";
        ArrayList storeCfg = (ArrayList)cfgM.getPluginProperties("Storage");
        if (storeCfg.size() > 0) {
            String className = (String)storeCfg.get(0);
            if (className != null && className.length() > 0) {
                try {
                    Object obj = Class.forName(className).newInstance();
                    if (!(obj instanceof Storage)) {
                        internalLog.add(Level.WARNING, methodLogPrefix
                            + "Incorrect name for store. Use default.");
                        store = null;
                    } else {
                        store = (Storage)obj;
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Store class is " + className);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Incorrect name for store. Use default.");
                    store = null;
                }
            }
        }
        if (store == null) {
            store = new StoreRes();
        }
    }

    /**
     * Create the instance of test finder and store the link into internal field
     */
    protected void createTestFinder() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateTestFinder(): ";
        ArrayList tfCfg = (ArrayList)cfgM.getPluginProperties("TestFinder");
        String className = "";
        if (tfCfg.size() > 0) {
            className = (String)tfCfg.get(0);
            if (className != null && className.length() > 0) {
                try {
                    Object obj = Class.forName(className).newInstance();
                    if (!(obj instanceof Finder)) {
                        internalLog.add(Level.WARNING, methodLogPrefix
                            + "Incorrect name for test finder. Use default.");
                        tf = null;
                    } else {
                        tf = (Finder)obj;
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Test finder class is " + className);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Incorrect name for test finder. Use default.");
                    tf = null;
                }
            }
        }
        if (tf == null) {
            if (className.indexOf("JUFinder") != -1) {
                tf = new JUFinder();
            } else {
                tf = new TestFinder();
            }
        }
    }

    /**
     * Return the array of currently available communication servers
     * 
     * @return array of communication servers or null
     */
    public ComServer[] getComServer() {
        return cs;
    }

    /**
     * Set the new array of currently available communication servers
     */
    public void setComServers(ComServer[] val) {
        cs = val;
    }

    public Configurator getConfigurator() {
        return cfg;
    }

    public ConfigIR getConfigIR() {
        return cfgM;
    }

    public ExecListIR getExecList() {
        return execList;
    }

    public ExecStatusIR getExecStat() {
        return execStat;
    }

    public Logging getLogger() {
        return log;
    }

    public Logging getInternalLogger() {
        return internalLog;
    }

    public MCPool getMCPoll() {
        return clients;
    }

    public Report getReporter() {
        return rep;
    }

    public Finder getResFinder() {
        return rf;
    }

    public OutputStream getCurOut() {
        return System.out;
    }

    public Storage getStore() {
        return store;
    }

    public Object getSynObj() {
        return synObj;
    }

    public Dispatcher getDispatcher() {
        return dsp;
    }

    public Finder getTestFinder() {
        return tf;
    }

    public TestSuiteIR getTstSuite() {
        return tstSuite;
    }

    public void addExtProcs(Process proc) {
        extProcs.add(proc);
    }

    public String getCfgPath() {
        return cfgStorePath;
    }

    public String getCfgDir() {
        if (Util.checkExistFile(cfgStorePath)) {
            return (new File(cfgStorePath)).getParent();
        }
        return cfgStorePath;
    }

    /*
     * return the current version of the harness
     */
    public String getVersion() {
        return MAJOR_VERSION + "." + MINOR_VERSION + VERSION_STRING;
    }

    /*
     * into this method the command lines options are redefined the options from
     * config files Note, if the new TestSuiteConfig location is defined than
     * the configuration should be reloaded.
     */
    protected boolean setCfgFromCmdline() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetCfgFromCmdline(): ";
        if ((overWriteOptions.size() % 2) != 0) {
            internalLog.add(Level.INFO, methodLogPrefix
                + "Incorrect size for array of options");
            return false;
        }
        String variable;
        Method[] methods = cfgM.getClass().getMethods();
        String[] value = new String[1];
        for (int i = 0; i < overWriteOptions.size(); i++) {
            for (int y = 0; y < methods.length; y++) {
                variable = (String)overWriteOptions.get(i);
                if (methods[y].getName().equalsIgnoreCase("set" + variable)
                    && methods[y].getParameterTypes().length > 0
                    && methods[y].getParameterTypes()[0].getName().indexOf(
                        "String") != -1) {
                    try {
                        value[0] = (String)overWriteOptions.get(++i);
                        methods[y].invoke(cfgM, value);
                        if ("TestSuiteConfigRoot".equals(variable)
                            || "TestSuiteRoot".equals(variable)) {
                            cfg.loadTSConfig();
                        }
                        break;
                    } catch (Exception e) {
                        internalLog.add(Level.FINE, methodLogPrefix
                            + "Can't set variable: " + variable
                            + ", the reason is " + e);
                    }
                }
            }
        }
        if ((overWriteExecOptions.size() % 2) != 0) {
            internalLog.add(Level.INFO, methodLogPrefix
                + "Incorrect size for array of execution options");
            return false;
        }
        for (int i = 0; i < overWriteExecOptions.size(); i++) {
            //redefine parameters for plugin
            if (((String)overWriteExecOptions.get(i))
                .indexOf(Constants.PLUGIN_CMD_LINE_PARAM_SEPARATOR) != -1) {
                String[] data = Util.stringToArray((String)overWriteExecOptions
                    .get(i), Constants.PLUGIN_CMD_LINE_PARAM_SEPARATOR);
                String keyValue = (String)overWriteExecOptions.get(++i);
                if (data.length == 3) {
                    //data[0] - plugin name
                    //data[1] - parameter name
                    //data[2] - option name
                    ArrayList tmp = cfgM.getPluginProperties(data[0]);
                    if (tmp != null) { //plugin defined
                        try {
                            if (tmp.size() > 1) {
                                //some plugin options already defined. First
                                //element is plugin class name, second
                                //parameters HashMap. Values updated directly
                                //in the plugin lists
                                HashMap params = (HashMap)tmp.get(1);
                                if (params != null) {
                                    //some parameters already defined for
                                    // plugin
                                    HashMap curValue = (HashMap)(params
                                        .get(data[1]));
                                    if (curValue != null) {
                                        //some parameters already defined with
                                        //same parameter name
                                        ArrayList tmpVal = (ArrayList)curValue
                                            .get(data[2]);
                                        if (!tmpVal.contains(keyValue)) {
                                            tmpVal.add(keyValue);
                                        }
                                    } else {
                                        ArrayList tmpVal = new ArrayList();
                                        tmpVal.add(keyValue);
                                        curValue = new HashMap();
                                        curValue.put(data[2], tmpVal);
                                        params.put(data[1], curValue);
                                    }
                                } else {
                                    HashMap tmpHM = new HashMap();
                                    ArrayList tmpVal = new ArrayList();
                                    tmpVal.add(keyValue);
                                    tmpHM.put(data[2], tmpVal);
                                    tmp.add(tmpHM);
                                }
                            } else {
                                tmp.add("");
                                HashMap tmpHM = new HashMap();
                                HashMap params = new HashMap();
                                ArrayList tmpVal = new ArrayList();
                                tmpVal.add(keyValue);
                                tmpHM.put(data[2], tmpVal);
                                params.put(data[1], tmpHM);
                                tmp.add(params);
                                cfgM.getPlugins().put(data[0], tmp);
                            }
                        } catch (Exception e) {
                            internalLog.add(Level.CONFIG, methodLogPrefix
                                + "Can't parse plugin option: " + data[0]);
                        }
                    } else {
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Can't parse param as plugin option: " + data[0]);
                        //set it as simple param
                        cfgM.setProperty((String)overWriteExecOptions.get(i),
                            (String)overWriteExecOptions.get(++i));
                    }
                }
            } else {
                //redefine common parameters (exclude plugin parameters)
                String optionName = (String)overWriteExecOptions.get(i);
                if (optionName.startsWith(Constants.ADDOPTIONVAL)) {
                    optionName = optionName.substring(Constants.ADDOPTIONVAL
                        .length());
                    String tmp = cfgM.getProperty(optionName);
                    cfgM.setProperty(optionName, tmp + " "
                        + (String)overWriteExecOptions.get(++i));
                } else {
                    cfgM.setProperty(optionName, (String)overWriteExecOptions
                        .get(++i));
                }
            }
        }
        return true;
    }

    /*
     * the function to process params
     */
    protected boolean parseParam(String[] params) {
        Level tmpLevel = null;
        final String helpMsg = "Usage:\n"
            + "-ln <name>\t\t\tlogger name\n"
            + "-cn <name>\t\t\tconfigurator name\n"
            + "-cfp <path>\t\t\tpath to the configuration files\n"
            + "-logl <0/1-7>\t\t\tharness log level: 0 - off, 1 - finest, 2 - finer, ... 7 - severe\n"
            + "-nosyncchan\t\t\tdo not start the server for the synchronization channel\n"
            + "-subs \"name1 name2...\"\t\tsubsuites to find the tests (from the test suite root)\n"
            + "-excludesubs \"name1 name2...\"\tsubsuites to exclude from finding process (from the test suite root)\n"
            + "-jars \"name1 name2...\"\t\tjar archives to find the unit tests (from the test suite class root)\n"
            + "-include \"name1 name2...\"\ttest or file names with the test list to include in this run\n"
            + "-exclude \"name1 name2...\"\ttest or file names with the test list to exclude from this run\n"
            + "-property <name> <value>\tdefine new value for the configuration property\n"
            + "-execopt <name> <value>\t\tdefine new value or add value to the execution options\n"
            + "-logfile <name> \t\tstore the log into file too. By default, harness write log to 'out' only\n"
            + "-version\t\t\tprint the harness version and run tests\n"
            + "-versiononly\t\t\tprint the harness version only\n"
            + "-failonerror\t\t\texit with non-zero (-1) code if any test non-passed\n"
            + "-gui\t\t\t\tGUI mode";
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-ln".equalsIgnoreCase(params[i])) {
                    loggerName = params[++i];
                } else if ("-cn".equalsIgnoreCase(params[i])) {
                    configName = params[++i];
                } else if ("-cfp".equalsIgnoreCase(params[i])) {
                    cfgStorePath = params[++i];
                } else if ("-batch".equalsIgnoreCase(params[i])) {
                    guiMode = false;
                } else if ("-nosyncchan".equalsIgnoreCase(params[i])) {
                    useSyncChannelServer = false;
                } else if ("-logl".equalsIgnoreCase(params[i])) {
                    int llevel = 5; //default is 'info'
                    try {
                        llevel = Integer.parseInt(params[++i]);
                    } catch (Exception ne) {
                        System.out
                            .println("Incorrect value for log level. Use default.");
                    }
                    switch (llevel) {
                    case 1:
                        tmpLevel = Level.FINEST;
                        break;
                    case 2:
                        tmpLevel = Level.FINER;
                        break;
                    case 3:
                        tmpLevel = Level.FINE;
                        break;
                    case 4:
                        tmpLevel = Level.CONFIG;
                        break;
                    case 5:
                        tmpLevel = Level.INFO;
                        break;
                    case 6:
                        tmpLevel = Level.WARNING;
                        break;
                    case 7:
                        tmpLevel = Level.SEVERE;
                        break;
                    default:
                        tmpLevel = Level.OFF;
                    }
                    internalLog.init(tmpLevel);
                } else if ("-version".equalsIgnoreCase(params[i])) {
                    System.out.println("The test harness version is "
                        + getVersion());
                } else if ("-versiononly".equalsIgnoreCase(params[i])) {
                    System.out.println("The test harness version is "
                        + getVersion());
                    System.exit(0);
                } else if ("-subs".equalsIgnoreCase(params[i])) {
                    subSuites = Util.stringToArray(params[++i]);
                    subSuites = Util.mergeArrays(subSuites, jarFiles);
                } else if ("-excludesubs".equalsIgnoreCase(params[i])) {
                    excludeSuites = Util.stringToArray(params[++i]);
                    if (excludeSuites != null && excludeSuites.length > 0) {
                        for (int y = 0; y < excludeSuites.length; y++) {
                            if (excludeSuites[y].indexOf(File.separator) != -1) {
                                excludeSuites[y].replace(File.separatorChar,
                                    Constants.INTERNAL_FILE_SEP_C);
                            }
                        }
                    }
                } else if ("-jars".equalsIgnoreCase(params[i])) {
                    jarFiles = Util.stringToArray(params[++i]);
                    jarFiles = Util.mergeArrays(subSuites, jarFiles);
                } else if ("-include".equalsIgnoreCase(params[i])) {
                    includeTestFiles = Util.stringToArray(params[++i]);
                } else if ("-exclude".equalsIgnoreCase(params[i])) {
                    excludeTestFiles = Util.stringToArray(params[++i]);
                } else if ("-property".equalsIgnoreCase(params[i])) {
                    overWriteOptions.add(params[++i]);
                    overWriteOptions.add(params[++i]);
                } else if ("-execopt".equalsIgnoreCase(params[i])) {
                    overWriteExecOptions.add(params[++i]);
                    overWriteExecOptions.add(params[++i]);
                } else if ("-failonerror".equalsIgnoreCase(params[i])) {
                    fail_on_error = true;
                } else if ("-logfile".equalsIgnoreCase(params[i])) {
                    internalLog = new InternalTHLoggerFile(params[++i]);
                    if (tmpLevel != null) {
                        internalLog.init(tmpLevel);
                    } else {
                        internalLog.init();
                    }
                } else if ("report".equalsIgnoreCase(params[i])) {
                    System
                        .exit((new org.apache.harmony.harness.ReportTool.Report()
                            .run(Util.removeFirstElem(params, ++i))));
                } else if ("scriptgen".equalsIgnoreCase(params[i])) {
                    System.exit((new ScriptGen().run(Util.removeFirstElem(
                        params, ++i))));
                } else if ("cmpresults".equalsIgnoreCase(params[i])) {
                    System.exit((new CmpResults().run(Util.removeFirstElem(
                        params, ++i))));
                } else if ("-h".equalsIgnoreCase(params[i])
                    || "-help".equalsIgnoreCase(params[i])) {
                    System.out.println(helpMsg);
                    System.exit(0);
                } else {
                    System.out.println("Incorrect option: " + params[i]);
                    System.out.println(helpMsg);
                    System.exit(0);
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            System.out.println("Incorrect parameters. Please check.\n"
                + helpMsg);
            return false;
        }
        return true;
    }

    /*
     * into this methods the runs property should be checked and updated from
     * the DefaultConfigIRValues class if needed
     */
    protected void checkAndUpdateConfig() throws ConfigurationException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcheckAndUpdateConfig(): ";
        String tmpVal;
        if (cfgM == null) {
            throw new ConfigurationException(methodLogPrefix
                + "Configuration is undefined");
        }
        tmpVal = cfgM.getTestSuiteClassRoot();
        if (tmpVal == null) {
            throw new ConfigurationException(methodLogPrefix
                + "Test suite class path is undefined");
        } else {
            if (cfgM.getLocalM() == Main.LOCAL) {
                if (!Util.checkExistDir(tmpVal)) {
                    if (!Util.checkExistDir(cfgM.getTestSuiteRoot()
                        + File.separator + tmpVal)) {
                        internalLog
                            .add(
                                Level.WARNING,
                                methodLogPrefix
                                    + "Possible incorrect value for test suite class root");
                    } else {
                        cfgM.setTestSuiteClassRoot(cfgM.getTestSuiteRoot()
                            + Constants.INTERNAL_FILE_SEP + tmpVal);
                    }
                }
            }
        }
        tmpVal = cfgM.getTestSuiteTestRoot();
        if (tmpVal == null) {
            throw new ConfigurationException(methodLogPrefix
                + "Test suite test path is undefined");
        } else {
            if (cfgM.getLocalM() == Main.LOCAL) {
                if (!Util.checkExistDir(tmpVal)) {
                    if (!Util.checkExistDir(cfgM.getTestSuiteRoot()
                        + File.separator + tmpVal)) {
                        internalLog
                            .add(
                                Level.WARNING,
                                methodLogPrefix
                                    + "Possible incorrect value for test suite test root");
                    } else {
                        cfgM.setTestSuiteTestRoot(cfgM.getTestSuiteRoot()
                            + Constants.INTERNAL_FILE_SEP + tmpVal);
                    }
                }
            }
        }
        tmpVal = cfgM.getTestSuiteLibRoot();
        if (tmpVal == null) {
            throw new ConfigurationException(methodLogPrefix
                + "Test suite library path is undefined");
        } else {
            if (cfgM.getLocalM() == Main.LOCAL) {
                if (!Util.checkExistDir(tmpVal)) {
                    if (!Util.checkExistDir(cfgM.getTestSuiteRoot()
                        + File.separator + tmpVal)) {
                        internalLog
                            .add(
                                Level.WARNING,
                                methodLogPrefix
                                    + "Possible incorrect value for test suite library root");
                    } else {
                        cfgM.setTestSuiteLibRoot(cfgM.getTestSuiteRoot()
                            + Constants.INTERNAL_FILE_SEP + tmpVal);
                    }
                }
            }
        }
        tmpVal = cfgM.getTestSuiteConfigRoot();
        if (tmpVal == null) {
            throw new ConfigurationException(methodLogPrefix
                + "Test suite configuration path is undefined");
        } else {
            if (cfgM.getLocalM() == Main.LOCAL) {
                if (!Util.checkExistDir(tmpVal)) {
                    if (!Util.checkExistDir(cfgM.getTestSuiteRoot()
                        + File.separator + tmpVal)) {
                        internalLog
                            .add(
                                Level.WARNING,
                                methodLogPrefix
                                    + "Possible incorrect value for test suite configuration root");
                    } else {
                        cfgM.setTestSuiteConfigRoot(cfgM.getTestSuiteRoot()
                            + Constants.INTERNAL_FILE_SEP + tmpVal);
                    }
                }
            }
        }
        tmpVal = cfgM.getTestResultRoot();
        if (tmpVal == null) {
            throw new ConfigurationException(methodLogPrefix
                + "Place for results of test suite run is undefined");
        } else {
            if (!Util.checkExistDir(tmpVal) && cfgM.getLocalM() == Main.LOCAL) {
                if (!(new File(tmpVal)).mkdirs()) {
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Incorrect value for test results root");
                }
            }
        }
        tmpVal = cfgM.getTempStorage();
        if (tmpVal == null) {
            throw new ConfigurationException(methodLogPrefix
                + "Place for temporary storage directory is undefined");
        } else {
            if (!Util.checkExistDir(tmpVal) && cfgM.getLocalM() == Main.LOCAL) {
                if (!(new File(tmpVal)).mkdirs()) {
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Incorrect value for temporary storage directory");
                }
            }
        }
        tmpVal = cfgM.getTestedCompile();
        if (!Util.checkExistFileWithExt(tmpVal)
            && cfgM.getLocalM() == Main.LOCAL) {
            internalLog.add(Level.INFO, methodLogPrefix
                + "Possible incorrect value for link to tested compile");
        }
        tmpVal = cfgM.getReferenceCompile();
        if (!Util.checkExistFileWithExt(tmpVal)
            && cfgM.getLocalM() == Main.LOCAL) {
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Possible incorrect value for link to reference compile");
        }
        tmpVal = cfgM.getTestedRuntime();
        if (!Util.checkExistFileWithExt(tmpVal)
            && cfgM.getLocalM() == Main.LOCAL) {
            internalLog.add(Level.WARNING, methodLogPrefix
                + "Possible incorrect value for link to tested runtime");
        }
        tmpVal = cfgM.getReferenceRuntime();
        if (!Util.checkExistFileWithExt(tmpVal)
            && cfgM.getLocalM() == Main.LOCAL) {
            internalLog.add(Level.INFO, methodLogPrefix
                + "Possible incorrect value for link to reference runtime");
        }
        //for local mode correct all paths to TestedRuntime and so on
        //for remote mode do nothing
        if (cfgM.getLocalM() == Main.LOCAL) {
            cfgM.setTestSuiteRoot((new File(cfgM.getTestSuiteRoot())).getPath()
                .replace(File.separatorChar, Constants.INTERNAL_FILE_SEP_C));
            cfgM.setTestSuiteClassRoot((new File(cfgM.getTestSuiteClassRoot()))
                .getPath().replace(File.separatorChar,
                    Constants.INTERNAL_FILE_SEP_C));
            cfgM.setTestSuiteTestRoot((new File(cfgM.getTestSuiteTestRoot()))
                .getPath().replace(File.separatorChar,
                    Constants.INTERNAL_FILE_SEP_C));
            cfgM
                .setTestSuiteConfigRoot((new File(cfgM.getTestSuiteConfigRoot()))
                    .getPath().replace(File.separatorChar,
                        Constants.INTERNAL_FILE_SEP_C));
            cfgM.setTestSuiteLibRoot((new File(cfgM.getTestSuiteLibRoot()))
                .getPath().replace(File.separatorChar,
                    Constants.INTERNAL_FILE_SEP_C));
            cfgM.setTestResultRoot((new File(cfgM.getTestResultRoot()))
                .getPath().replace(File.separatorChar,
                    Constants.INTERNAL_FILE_SEP_C));
            cfgM.setTestedCompile((new File(cfgM.getTestedCompile())).getPath()
                .replace(File.separatorChar, Constants.INTERNAL_FILE_SEP_C));
            cfgM.setReferenceCompile((new File(cfgM.getReferenceCompile()))
                .getPath().replace(File.separatorChar,
                    Constants.INTERNAL_FILE_SEP_C));
            cfgM.setTestedRuntime((new File(cfgM.getTestedRuntime())).getPath()
                .replace(File.separatorChar, Constants.INTERNAL_FILE_SEP_C));
            cfgM.setReferenceRuntime((new File(cfgM.getReferenceRuntime()))
                .getPath().replace(File.separatorChar,
                    Constants.INTERNAL_FILE_SEP_C));
            cfgM.setTempStorage((new File(cfgM.getTempStorage())).getPath()
                .replace(File.separatorChar, Constants.INTERNAL_FILE_SEP_C));
        }
        //by default: check the TestSuiteConfigRoot for file 'exclude.file'
        //if it's present and no exclude list were difined - use it as list of
        // tests for exclude
        if (excludeTestFiles == null
            && Util.checkExistFile(getCfgDir() + File.separator
                + EXCLUDE_FILE_NAME) == true) {
            excludeTestFiles = new String[1];
            excludeTestFiles[0] = getCfgDir() + File.separator
                + EXCLUDE_FILE_NAME;
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Use the default exclude file " + excludeTestFiles[0]);
        } else if (excludeTestFiles != null) {
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Use the exclude file");
            for (int i = 0; i < excludeTestFiles.length; i++) {
                internalLog.add(Level.FINE, methodLogPrefix
                    + "Exclude files/tests " + excludeTestFiles[i]);
            }
        } else {
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "The default exclude file is unavailable");
        }
    }

    /*
     * run the server that provides the work of the synchronized channels on the
     * port pointed in the configuration
     */
    protected void startSynChannel() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tstartSynChannel(): ";
        if (useSyncChannelServer == false) {
            internalLog.add(Level.INFO, methodLogPrefix
                + "synchronized channel's server disabled");
            return;
        }
        synchronized (getSynObj()) {
            String[] strArr = { cfgM.getSynChannelPort() };
            ss = new SyncChannelServer(strArr);
            try {
                ss.start();
                getSynObj().wait();
                cfgM.setProperty("syncChannelPort", "" + ss.getPort());
                if (ss.getGenCnt() > 0) {
                    internalLog.add(Level.INFO, methodLogPrefix
                        + "Start synchronize channel on port " + ss.getPort());
                } else {
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Syn channel is unavailable.");
                }
            } catch (Exception e) {
                internalLog.add(Level.WARNING, methodLogPrefix
                    + "Syn channel may be unavailable: " + e.getMessage());
            }
        }
    }

    /*
     * To generate report based on the information stored into result directory.
     * No test run
     */

    /**
     * The main methods. The program start here.
     * 
     * @param args input args
     * @return execution status
     */
    public int run(String[] args) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trun(): ";
        curCore = this;
        Date startTime = new Date();
        Date testStartTime = startTime;
        internalLog = new InternalTHLogger();
        internalLog.init(Level.INFO);
        if (!parseParam(args)) {
            return 2000;
        }
        try {
            log = new THLogger();
            log.init(Level.ALL);
            createConfigurator();
            if (!cfg.createConfiguration(cfgStorePath)) {
                internalLog.add(Level.WARNING, methodLogPrefix
                    + "Invalid configuration in config files");
            }
            if (setCfgFromCmdline()) {
                internalLog.add(Level.CONFIG, methodLogPrefix
                    + "Config options were redefined: OK");
            } else {
                internalLog.add(Level.INFO, methodLogPrefix
                    + "Some errors in the command line options");
            }
            new DefaultConfigSetup().setDefaultValues();
            checkAndUpdateConfig();
            internalLog.add(Level.WARNING, methodLogPrefix
                + "Create configuration: OK");
            log.close();

            if (!createLog()) {
                return 2001;
            }
            createReporter();
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Create reporter: OK");
            createStore();
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Create store result module: OK");
            createResFinder();
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Create result finder: OK");
            createTestFinder();
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Create test finder: OK");

            dsp = new Dispatcher();
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Run dispatcher: OK");
            createRunners();
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Create runners: OK");
            execList = new ExecListIR();
            execStat = new ExecStatusIR();
            tstSuite = new TestSuiteIR();
            createSelector();
            internalLog.add(Level.CONFIG, methodLogPrefix
                + "Create Selector: OK");
            startSynChannel();

            //preload classes
            new ProcDestroy(null);
            new ThreadDestroy(null);
            
            if (cfgM.getLocalM() == REMOTE) { // || getExecM() == SAME) {
                clients = new MCPool();
                (new RRunner()).start();
                //wait for MCore not more than 10 timeout's
                for (int i = 0; i < cfgM.getGenTimeout() * 10; i++) {
                    try {
                        Thread.sleep(Constants.WAIT_TIME);
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                    if ((i % cfgM.getGenTimeout()) == 0) {
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Waiting for MCore connection ...");
                    }
                    if (clients.size() > 0) {
                        break;
                    }
                }
                if (clients.size() > 0) {
                    internalLog.add(Level.CONFIG, methodLogPrefix
                        + "Run remote runner: OK");
                } else {
                    internalLog.add(Level.CONFIG, methodLogPrefix
                        + "Can not receive response from the remote runners");
                }
            }

            if (guiMode) {
                selector.mkTSuite();
                // call gui and wait for user
            } else {
                if (subSuites != null && subSuites.length > 0) {
                    selector.run(subSuites, excludeSuites, includeTestFiles,
                        excludeTestFiles);
                } else {
                    if (cfgM.getTestedSubSuites() != null
                        && cfgM.getTestedSubSuites().size() > 0) {
                        String[] tmp = new String[cfgM.getTestedSubSuites()
                            .size()];
                        for (int i = 0; i < cfgM.getTestedSubSuites().size(); i++) {
                            tmp[i] = cfgM.getTestedSubSuites().get(i)
                                .toString();
                        }
                        selector.run(tmp, excludeSuites, includeTestFiles,
                            excludeTestFiles);
                    } else {
                        selector.run(null, excludeSuites, includeTestFiles,
                            excludeTestFiles);
                    }
                }
                internalLog.add(Level.WARNING, methodLogPrefix
                    + "Tests were selected: OK (total "
                    + Main.getCurCore().getExecList().size() + ")");
                testStartTime = new Date();
                dsp.run();
                internalLog.add(Level.INFO, methodLogPrefix
                    + "Tests were run: OK");
                rep.genReport();
            }
            internalLog.add("Done. See the run report in the directory\n\t"
                + cfgM.getTestResultRoot());
            Date endTime = new Date();
            internalLog.add("Tests execution time:\t"
                + Util.getTime(endTime.getTime() - testStartTime.getTime()));
            internalLog.add(Util.getMinutesTime(endTime.getTime()
                - startTime.getTime()));
        } catch (ConfigurationException th) {
            internalLog.add(methodLogPrefix
                + "unexpected ConfigurationException: " + th.getMessage());
            return 1;
        } catch (Throwable th) {
            internalLog.add(methodLogPrefix + "unexpected exception: " + th);
            internalLog
                .add("Please, send the code and this message to developer");
            th.printStackTrace();
            return 2;
        } finally {
            if (cs != null) {
                for (int i = 0; i < cs.length; i++) {
                    try {
                        cs[i].close();
                        cs[i].interrupt();
                        cs[i].join(Constants.WAIT_TIME);
                        if (cs[i].isAlive()) {
                            new ThreadDestroy(cs[i]).start();
                            Thread.yield();
                        }
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Close communication server: OK " + cs[i]);
                    } catch (Exception e) {
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Can't close communication server: " + cs[i]
                            + " " + e);
                    } catch (Error e) {
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Can't close communication server: " + cs[i]
                            + " " + e, e);
                    }
                }
                internalLog.add(Level.CONFIG, methodLogPrefix
                    + "Close communication servers: OK ");
            }
            if (ss != null) {
                try {
                    ss.close();
                    ss.interrupt();
                    ss.join(Constants.WAIT_TIME);
                    if (ss.isAlive()) {
                        new ThreadDestroy(ss).start();
                        Thread.yield();
                    }
                    internalLog.add(Level.CONFIG, methodLogPrefix
                        + "Close synchronization server: OK");
                } catch (Exception e) {
                    internalLog.add(Level.CONFIG, methodLogPrefix
                        + "Can't close synchronization server: " + e);
                } catch (Error e) {
                    internalLog.add(Level.WARNING, methodLogPrefix
                        + "Can't close synchronization server: " + e, e);
                }
                internalLog.add(Level.CONFIG, methodLogPrefix
                    + "Close sync channel: OK ");
            }
            if (clients != null) {
                for (int i = 0; i < clients.size(); i++) {
                    try {
                        ((MCoreIR)clients.get(i)).close();
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Close mcore: OK " + clients.get(i));
                    } catch (Exception e) {
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Can't close mcore: " + clients.get(i) + " " + e);
                    } catch (Error e) {
                        internalLog.add(Level.CONFIG, methodLogPrefix
                            + "Can't close mcore: " + clients.get(i) + " " + e, e);
                    }
                }
                internalLog.add(Level.CONFIG, methodLogPrefix
                    + "Close all mcore: OK ");
            }
            for (int i = 0; i < extProcs.size(); i++) {
                Process proc = (Process)(extProcs.get(i));
                InputStream err = proc.getErrorStream();
                InputStream in = proc.getInputStream();
                try {
                    byte[] tmpIn = new byte[in.available()];
                    in.read(tmpIn, 0, tmpIn.length);
                    byte[] tmpErr = new byte[err.available()];
                    err.read(tmpErr, 0, tmpErr.length);
                    proc.exitValue();
                } catch (Exception ior) {
                    internalLog.add(Level.SEVERE, methodLogPrefix
                        + "Process to destroy: " + proc
                        + "\nNote, some processes can hang up");
                    // Note, on some implementation the destroy() hang up when
                    // called for improperly work procs
                    Thread th = new ProcDestroy(proc);
                    th.start();
                    try {
                        th.join(Constants.WAIT_TIME);
                    } catch (InterruptedException ie) {
                        //do noting
                    }
                } catch (Error ior) {
                    internalLog.add(Level.SEVERE, methodLogPrefix
                        + "Process to destroy: " + proc
                        + "\nNote, some processes can hang up");
                }
                internalLog.add(Level.CONFIG, methodLogPrefix
                    + "Process was destroyed : OK " + proc);
            }
            log.close();
            internalLog.close();
        }
        if (fail_on_error
            && ((rep.errorTestCnt() + rep.failedTestCnt() + rep
                .unspecifiedTestCnt()) != 0)) {
            return -1;
        }
        return 0;
    }
}