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
 * @author Yerlan A. Tokpanov
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.28 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigIR implements Serializable {

    private Map       cfgProperties        = new HashMap(128);
    private HashMap   allPlugins           = new HashMap();

    private int       genTimeout;
    private int       intertestsTimeout    = -1;
    private int       maxTestsinSameCnt    = -1;
    private boolean   needLogLevelOption   = true;
    private Level     logLevel             = DefaultConfigIRValues.DEFAULT_LOG_LEVEL;
    private String    tempStorage;
    private String    testedRuntime;
    private String    testedCompile;
    private String    referenceRuntime;
    private String    referenceCompile;
    private String    testSuiteRoot;
    private String    testResultRoot;
    private ArrayList testedSubSuites      = new ArrayList();
    private ArrayList remoteRunner         = new ArrayList();
    private ArrayList resources            = new ArrayList();
    private ArrayList mapsList             = new ArrayList();

    private String    testSuiteClassRoot;
    private String    testSuiteTestRoot;
    private String    testSuiteConfigRoot;
    private String    testSuiteLibRoot;

    /*
     * this fields are updated from Selector after these list are created
     */
    private HashMap   excludeTestCasesList = new HashMap(256);
    private HashMap   includeTestCasesList = new HashMap(256);

    /**
     * Create the configuration with default values for main parameters.
     */
    public ConfigIR() {
        this(new HashMap(), 0, "", "", "", "", "", new ArrayList(),
            new ArrayList(), new HashMap(), new ArrayList());
    }

    /**
     * Create the configuration with default values for main parameters except
     * timeout. The timeout in second is specified by argument
     * 
     * @param timeout - general timeout in second
     */
    public ConfigIR(int timeout) {
        this(new HashMap(), timeout, "", "", "", "", "", new ArrayList(),
            new ArrayList(), new HashMap(), new ArrayList());
    }

    /**
     * Create the configuration with specified values.
     * 
     * @param properties - configuration property map (any value, not specified
     *        below)
     * @param timeout - general timeout in second
     * @param tempStorage - temporary directory
     * @param runtime - tested runtime
     * @param refRuntime - reference runtime
     * @param suiteRoot - test suite root (directory)
     * @param resultRoot - test result root (directory)
     * @param subSuites - list of additional paths (from suite root) to find
     *        tests or empty to find all
     * @param remoteRunner - list of structures to run mcore
     * @param plugins - plugins property
     */
    public ConfigIR(HashMap properties, int timeout, String tempStorage,
        String runtime, String refRuntime, String suiteRoot, String resultRoot,
        ArrayList subSuites, ArrayList remoteRunner, HashMap plugins,
        ArrayList resources) {
        if (properties != null) {
            cfgProperties = properties;
        }
        genTimeout = timeout;
        this.tempStorage = tempStorage;
        testedRuntime = runtime;
        referenceRuntime = refRuntime;
        testSuiteRoot = suiteRoot;
        testResultRoot = resultRoot;
        if (subSuites != null) {
            testedSubSuites = subSuites;
        }
        if (remoteRunner != null) {
            this.remoteRunner = remoteRunner;
        }
        if (remoteRunner != null) {
            allPlugins = plugins;
        }
        if (remoteRunner != null) {
            this.resources = resources;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Configurator#getProperties()
     */
    public HashMap getProperties() {
        synchronized (cfgProperties) {
            return (HashMap)cfgProperties;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Configurator#getProperty(java.lang.String)
     */
    public String getProperty(String key) {
        if (key == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Get configuration: null key to get a property");
        }
        synchronized (cfgProperties) {
            if (cfgProperties.containsKey(key)) {
                String tmp = cfgProperties.get(key).toString();
                // special handling for defined but empty variables
                if (tmp.length() == 0) {
                    return " ";
                } else {
                    return tmp;
                }
            }
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Configurator#setProperty(java.lang.String,
     *      java.lang.String)
     */
    public String setProperty(String key, String value) {
        Object tmpStore;
        if ((key == null) || (value == null)) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Set configuration: null key or value to set a property");
        }
        synchronized (cfgProperties) {
            tmpStore = cfgProperties.put(key, value);
            if (tmpStore != null) {
                return tmpStore.toString();
            }
            return Constants.NO_VALUE_MSG;
        }
    }

    /**
     * Return the value for property
     * 
     * @param key - property name
     * @return the value of property or string "Unspecified"
     * @throws NullPointerException if key == null
     */
    public Object getProperty(Object key) {
        if (key == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Get configuration: null key to get a property");
        }
        synchronized (cfgProperties) {
            if (cfgProperties.containsKey(key)) {
                return cfgProperties.get(key);
            }
            return Constants.NO_VALUE_MSG;
        }
    }

    /**
     * set the value for property
     * 
     * @param key - property name
     * @param value - property value
     * @return the previous value of this property or string "Unspecified"
     * @throws NullPointerException if key or value == null
     */
    public Object setProperty(String key, Object value) {
        Object tmpStore;
        if ((key == null) || (value == null)) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Set configuration: null key or value to set a property");
        }
        synchronized (cfgProperties) {
            tmpStore = cfgProperties.put(key, value);
            if (tmpStore != null) {
                return tmpStore;
            }
            return Constants.NO_VALUE_MSG;
        }
    }

    /**
     * return the map of all registered plugins
     * 
     * @return map of all registered plugins
     */
    public HashMap getPlugins() {
        synchronized (allPlugins) {
            return allPlugins;
        }
    }

    /**
     * set the map of properties for all plugins
     */
    public HashMap setPlugins(HashMap value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Unexpected null value for arrays of plugin's parameters");
        }
        synchronized (allPlugins) {
            HashMap tmpStore = allPlugins;
            allPlugins = (HashMap)value.clone();
            return tmpStore;
        }
    }

    /**
     * Return the list of all properties which was defined in the configuration
     * files for the specified plugin
     * 
     * @param pluginName - name of plugin
     * @return plugin properties
     */
    public ArrayList getPluginProperties(String pluginName) {
        synchronized (allPlugins) {
            if (allPlugins.containsKey(pluginName)) {
                return (ArrayList)allPlugins.get(pluginName);
            }
            return new ArrayList();
        }
    }

    /**
     * set the list properties which was defined for the pointed plugin
     * 
     * @param pluginName plugin name
     * @param value list of properties
     * @return current plugin properties
     * @throws NullPointerException if pluginName or value is null
     */
    public ArrayList setPluginProperties(String pluginName, ArrayList value) {
        ArrayList tmpStore = new ArrayList();
        if ((pluginName == null) || (value == null)) {
            throw new NullPointerException(
                MessageInfo.MSG_PREFIX
                    + "Set configuration: null plugin's name or value to set a plugin's properties");
        }
        synchronized (allPlugins) {
            if (allPlugins.containsKey(pluginName)) {
                tmpStore = (ArrayList)allPlugins.get(pluginName);
                allPlugins.put(pluginName, value.clone());
            }
            return tmpStore;
        }
    }

    /*
     * return the property TestSuiteRoot which was defined in the configuration
     * file
     */
    public String getTestSuiteRoot() {
        return testSuiteRoot;
    }

    /*
     * set the property TestSuiteRoot which was defined
     */
    public String setTestSuiteRoot(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for test suite root");
        }
        String tmpStore = testSuiteRoot;
        testSuiteRoot = value;//(new File(value)).getPath();
        return tmpStore;
    }

    /*
     * return the property TestSuiteClassRoot which was defined in the
     * configuration file or the directory "TestSuiteRoot/bin/classes"
     */
    public String getTestSuiteClassRoot() {
        if (testSuiteClassRoot == null) {
            return getTestSuiteRoot() + File.separator + "bin" + File.separator
                + "classes";
        }
        return testSuiteClassRoot;
    }

    /*
     * set the property TestSuiteClassRoot
     */
    public String setTestSuiteClassRoot(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for test suite class root");
        }
        String tmpStore = testSuiteClassRoot;
        testSuiteClassRoot = value;//(new File(value)).getPath();
        return tmpStore;
    }

    /*
     * return the property TestSuiteTestRoot which was defined in the
     * configuration file or the directory "TestSuiteRoot/bin/classes"
     */
    public String getTestSuiteTestRoot() {
        if (testSuiteTestRoot == null) {
            return getTestSuiteRoot() + File.separator + "src" + File.separator
                + "test";
        }
        return testSuiteTestRoot;
    }

    /*
     * set the property TestSuiteTestRoot
     */
    public String setTestSuiteTestRoot(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for test suite test root");
        }
        String tmpStore = testSuiteTestRoot;
        testSuiteTestRoot = value;//(new File(value)).getPath();
        return tmpStore;
    }

    public String getTestSuiteConfigRoot() {
        if (testSuiteConfigRoot == null) {
            return getTestSuiteRoot() + File.separator + "config";
        }
        return testSuiteConfigRoot;
    }

    public String setTestSuiteConfigRoot(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for test suite config root");
        }
        String tmpStore = testSuiteConfigRoot;
        testSuiteConfigRoot = value;//(new File(value)).getPath();
        return tmpStore;
    }

    public String getTestSuiteLibRoot() {
        if (testSuiteLibRoot == null) {
            return getTestSuiteRoot() + File.separator + "bin" + File.separator
                + "lib";
        }
        return testSuiteLibRoot;
    }

    public String setTestSuiteLibRoot(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for test suite library root");
        }
        String tmpStore = testSuiteLibRoot;
        testSuiteLibRoot = value;//(new File(value)).getPath();
        return tmpStore;
    }

    public String getTestResultRoot() {
        return testResultRoot;
    }

    public String setTestResultRoot(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for test results root");
        }
        String tmpStore = testResultRoot;
        testResultRoot = value;//(new File(value)).getPath();
        return tmpStore;
    }

    public String getTestedCompile() {
        return testedCompile;
    }

    public String setTestedCompile(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for tested compile");
        }
        String tmpStore = testedCompile;
        testedCompile = value;//(new File(value)).getPath();
        return tmpStore;
    }

    public String getReferenceCompile() {
        return referenceCompile;
    }

    public String setReferenceCompile(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for reference compile");
        }
        String tmpStore = referenceCompile;
        referenceCompile = value;//(new File(value)).getPath();
        return tmpStore;
    }

    public String getTestedRuntime() {
        return testedRuntime;
    }

    public String setTestedRuntime(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for runtime");
        }
        String tmpStore = testedRuntime;
        testedRuntime = value;//(new File(value)).getPath();
        return tmpStore;
    }

    public String getReferenceRuntime() {
        return referenceRuntime;
    }

    public String setReferenceRuntime(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for runtime");
        }
        String tmpStore = referenceRuntime;
        referenceRuntime = value;//(new File(value)).getPath();
        return tmpStore;
    }

    public String getTempStorage() {
        return tempStorage;
    }

    public String setTempStorage(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for temporary storage");
        }
        String tmpStore = tempStorage;
        tempStorage = value;//(new File(value)).getPath();
        return tmpStore;
    }

    /*
     * return current value for general timeout
     */
    public int getGenTimeout() {
        return genTimeout;
    }

    /*
     * set new value for the general timeout @param value - new value for
     * general timeout in seconds return previous value for general timeout
     */
    public int setGenTimeout(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(MessageInfo.MSG_PREFIX
                + "Negative time for timeout!");
        }
        int tmpStore = genTimeout;
        genTimeout = value;
        return tmpStore;
    }

    /*
     * set new value for the general timeout @param value - new value for
     * general timeout in seconds return previous value for general timeout
     */
    public int setGenTimeout(String data) {
        try {
            int value = Integer.parseInt(data);
            return setGenTimeout(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(MessageInfo.MSG_PREFIX
                + "Incorrect value for timeout!");
        }
    }

    public int getIntertestsTimeout() {
        if (intertestsTimeout == -1) {
            setIntertestsTimeout((String)cfgProperties
                .get(Constants.INTER_TESTS_TIMEOUT));
        }
        return intertestsTimeout;
    }

    public int setIntertestsTimeout(int value) {
        if (value < 0) {
            value = 0;
        }
        int tmpStore = intertestsTimeout;
        intertestsTimeout = value;
        return tmpStore;

    }

    public int setIntertestsTimeout(String data) {
        int value;
        try {
            value = Integer.parseInt(data);
        } catch (Exception e) {
            value = 0;
        }
        return setIntertestsTimeout(value);
    }

    public int getMaxTestsinSameCnt() {
        if (maxTestsinSameCnt == -1) {
            setMaxTestsinSameCnt((String)cfgProperties
                .get(Constants.MAX_TESTS_SAME_CNT));
        }
        return maxTestsinSameCnt;
    }

    public int setMaxTestsinSameCnt(int value) {
        if (value < 0) {
            value = 0;
        }
        int tmpStore = maxTestsinSameCnt;
        maxTestsinSameCnt = value;
        return tmpStore;
    }

    public int setMaxTestsinSameCnt(String data) {
        int value;
        try {
            value = Integer.parseInt(data);
        } catch (Exception e) {
            value = 0;
        }
        return setMaxTestsinSameCnt(value);
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public boolean needLogLevelOption() {
        return needLogLevelOption;
    }

    /*
     * set the level of logging for the test. default value is INFO
     */
    public Level setLogLevel(String value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for logger level");
        }
        Level tmpStore = logLevel;
        if (value.equalsIgnoreCase("no")) {
            //predefined value: if 'no' - option is not set for tests
            needLogLevelOption = false;
            return null;
        }
        logLevel = Level.parse(value);
        if (logLevel != Level.ALL && logLevel != Level.CONFIG
            && logLevel != Level.FINE && logLevel != Level.FINER
            && logLevel != Level.FINEST && logLevel != Level.INFO
            && logLevel != Level.OFF && logLevel != Level.SEVERE
            && logLevel != Level.WARNING) {
            logLevel = Level.INFO;
            throw new IllegalArgumentException(MessageInfo.MSG_PREFIX
                + "Incorrect value for logger level");
        }
        return tmpStore;
    }

    /*
     * return current paths to sub-suites
     */
    public ArrayList getTestedSubSuites() {
        synchronized (testedSubSuites) {
            return testedSubSuites;
        }
    }

    /*
     * set current paths to sub-suites
     */
    public ArrayList setTestedSubSuites(ArrayList value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for tested subsuites");
        }
        synchronized (testedSubSuites) {
            ArrayList tmpStore = testedSubSuites;
            testedSubSuites = (ArrayList)value.clone();
            return tmpStore;
        }
    }

    /*
     * return current paths map list
     */
    public ArrayList getMapsList() {
        synchronized (mapsList) {
            return mapsList;
        }
    }

    /*
     * set current paths map list
     */
    public ArrayList setMapsList(ArrayList value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for map list");
        }
        synchronized (mapsList) {
            ArrayList tmpStore = mapsList;
            mapsList = (ArrayList)value.clone();
            return tmpStore;
        }
    }

    /*
     * return current set of remote runners
     */
    public ArrayList getRemoteRunner() {
        synchronized (remoteRunner) {
            return remoteRunner;
        }
    }

    /*
     * set new collection of remote runners @return previous set of remote
     * runners @throws NullPointerException if value == null
     */
    public ArrayList setRemoteRunner(ArrayList value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for remote run configuration");
        }
        synchronized (remoteRunner) {
            ArrayList tmpStore = remoteRunner;
            remoteRunner = (ArrayList)value.clone();
            return tmpStore;
        }
    }

    /*
     * clear current set of remote runners
     */
    public void clearRemoteRunner() {
        synchronized (remoteRunner) {
            remoteRunner = new ArrayList();
        }
    }

    /*
     * return current set of external resources
     */
    public ArrayList getResources() {
        synchronized (resources) {
            return resources;
        }
    }

    /*
     * set new collection of resources @return previous set of resources @throws
     * NullPointerException if value == null
     */
    public ArrayList setResources(ArrayList value) {
        if (value == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Incorrect null value for external resources configuration");
        }
        synchronized (resources) {
            ArrayList tmpStore = resources;
            resources = (ArrayList)value.clone();
            return tmpStore;
        }
    }

    /*
     * clear current set of external resources
     */
    public void clearResources() {
        synchronized (resources) {
            resources = new ArrayList();
        }
    }

    public int[] getRepStatus(String value) {
        int[] result = null;
        try {
            synchronized (allPlugins) {
                if (allPlugins.containsKey("Reporter")) {
                    HashMap param = (HashMap)((HashMap)((ArrayList)allPlugins
                        .get("Reporter")).get(1)).get("status");
                    ArrayList valueState = (ArrayList)param.get(value);
                    if (valueState != null && valueState.size() > 0) {
                        result = new int[valueState.size()];
                        for (int i = 0; i < result.length; i++) {
                            result[i] = Integer.parseInt((String)valueState
                                .get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            //return result;
        }
        return result;
    }

    /*
     * return the int value that should be interpreted as 'test passed status'
     */
    public int[] getRepPassed() {
        int[] result = getRepStatus("passed");
        if (result == null || result.length == 0) {
            result = new int[1];
            result[0] = Integer.parseInt(DefaultConfigIRValues.R_PASSED);
        }
        return result;
    }

    /*
     * return the int value that should be interpreted as 'test failed status'
     */
    public int[] getRepFailed() {
        int[] result = getRepStatus("failed");
        if (result == null || result.length == 0) {
            result = new int[1];
            result[0] = Integer.parseInt(DefaultConfigIRValues.R_FAILED);
        }
        return result;
    }

    /*
     * return the int value that should be interpreted as 'test error status'
     */
    public int[] getRepError() {
        int[] result = getRepStatus("error");
        if (result == null || result.length == 0) {
            result = new int[1];
            result[0] = Integer.parseInt(DefaultConfigIRValues.R_ERROR);
        }
        return result;
    }

    /*
     * return the int value that should be interpreted as 'test skipped status'
     */
    public int[] getRepModErr() {
        int[] result = getRepStatus("moderror");
        if (result == null || result.length == 0) {
            result = new int[1];
            result[0] = Integer.parseInt(DefaultConfigIRValues.R_SKIPPED);
        }
        return result;
    }

    /*
     * return the value that pointed should be previous result files be
     * overwritten or not by default, no
     */
    public boolean getResOver() {
        if (allPlugins.containsKey("Reporter")) {
            try {
                HashMap param = (HashMap)((HashMap)((ArrayList)allPlugins
                    .get("Reporter")).get(1)).get("write");
                if (param != null && ((ArrayList)param.get("over")).size() > 0) {
                    String tmpVal = (String)((ArrayList)param.get("over"))
                        .get(0);
                    if (tmpVal.equalsIgnoreCase("true")) {
                        return true;
                    }
                }
            } catch (Exception e) {
                //return false;
            }
        }
        return false;
    }

    /*
     * return the array of string (key
     */
    public String[] getSelKeywords() {
        if (allPlugins.containsKey("Selector")) {
            try {
                ArrayList param = (ArrayList)((HashMap)((HashMap)((ArrayList)allPlugins
                    .get("Selector")).get(1)).get("include")).get("Keyword");
                if (param != null && param.size() > 0) {
                    String[] tmpStore = new String[param.size()];
                    for (int i = 0; i < tmpStore.length; i++) {
                        tmpStore[i] = (String)param.get(i);
                    }
                    return tmpStore;
                }
            } catch (Exception e) {
                //return null;
            }
        }
        return null;
    }

    public String[] getSelAuthor() {
        if (allPlugins.containsKey("Selector")) {
            try {
                ArrayList param = (ArrayList)((HashMap)((HashMap)((ArrayList)allPlugins
                    .get("Selector")).get(1)).get("include")).get("Author");
                if (param != null && param.size() > 0) {
                    String[] tmpStore = new String[param.size()];
                    for (int i = 0; i < tmpStore.length; i++) {
                        tmpStore[i] = (String)param.get(i);
                    }
                    return tmpStore;
                }
            } catch (NullPointerException e) {
                //return null;
            }
        }
        return null;
    }

    public String[] getSelModifAuthors() {
        if (allPlugins.containsKey("Selector")) {
            try {
                ArrayList param = (ArrayList)((HashMap)((HashMap)((ArrayList)allPlugins
                    .get("Selector")).get(1)).get("include"))
                    .get("modif-author");
                if (param != null && param.size() > 0) {
                    String[] tmpStore = new String[param.size()];
                    for (int i = 0; i < tmpStore.length; i++) {
                        tmpStore[i] = (String)param.get(i);
                    }
                    return tmpStore;
                }
            } catch (NullPointerException e) {
                //return null;
            }
        }
        return null;
    }

    public String[] getSelResources() {
        if (allPlugins.containsKey("Selector")) {
            try {
                ArrayList param = (ArrayList)((HashMap)((HashMap)((ArrayList)allPlugins
                    .get("Selector")).get(1)).get("include")).get("Resource");
                if (param != null && param.size() > 0) {
                    String[] tmpStore = new String[param.size()];
                    for (int i = 0; i < tmpStore.length; i++) {
                        tmpStore[i] = (String)param.get(i);
                    }
                    return tmpStore;
                }
            } catch (NullPointerException e) {
                //return null;
            }
        }
        return null;
    }

    public String[] getSelRunners() {
        if (allPlugins.containsKey("Selector")) {
            try {
                ArrayList param = (ArrayList)((HashMap)((HashMap)((ArrayList)allPlugins
                    .get("Selector")).get(1)).get("include")).get("RunnerID");
                if (param != null && param.size() > 0) {
                    String[] tmpStore = new String[param.size()];
                    for (int i = 0; i < tmpStore.length; i++) {
                        tmpStore[i] = (String)param.get(i);
                    }
                    return tmpStore;
                }
            } catch (NullPointerException e) {
                //return null;
            }
        }
        return null;
    }

    public String[] getSelStatus() {
        if (allPlugins.containsKey("Selector")) {
            try {
                ArrayList param = (ArrayList)((HashMap)((HashMap)((ArrayList)allPlugins
                    .get("Selector")).get(1)).get("include")).get("status");
                if (param != null && param.size() > 0) {
                    String[] tmpStore = new String[param.size()];
                    for (int i = 0; i < tmpStore.length; i++) {
                        tmpStore[i] = (String)param.get(i);
                    }
                    return tmpStore;
                }
            } catch (NullPointerException e) {
                //return null;
            }
        }
        return null;
    }

    public String clearProperty(String key) {
        Object tmpStore;
        if (key == null) {
            throw new NullPointerException(MessageInfo.MSG_PREFIX
                + "Clear property: null key");
        }
        return cfgProperties.remove(key).toString();
    }

    public String getBootClassPath() {
        return (String)cfgProperties.get(Constants.BOOT_PATH);
    }

    public String getPerformanceOption() {
        return (String)cfgProperties.get(Constants.PERF_OPT);
    }

    public String getGenralVMOptAsString() {
        return (String)cfgProperties.get(Constants.GEN_VM_OPT);
    }

    public String[] getGenralVMOptAsArray() {
        String[] tmp = Util.stringToArray(getGenralVMOptAsString());
        return tmp;
    }

    public String getReplayNumber() {
        return (String)cfgProperties.get(Constants.REPLAY_NUMBER);
    }

    public String getGeneralEnvAsString() {
        return (String)cfgProperties.get(Constants.GEN_ENV);
    }

    public boolean getSysInfoFlag() {
        String tmp = (String)cfgProperties.get(Constants.SYSTEM_OPT);
        if (tmp != null && tmp.toLowerCase().indexOf("true") != -1) {
            return true;
        }
        return false;
    }

    public String[] getGeneralEnvAsArray() {
        String envs = getGeneralEnvAsString();
        String[] tmp = Util.stringToArray(envs);
        return tmp;
    }

    public String getInheritEnv() {
        return (String)cfgProperties.get(Constants.INHERIT_ENV);
    }

    public boolean getExecM() {
        String tmpVal = (String)cfgProperties.get("exec");
        if (tmpVal != null) {
            if (tmpVal.equalsIgnoreCase("same") == true) {
                return Main.SAME;
            }
        }
        return DefaultConfigIRValues.DEFAULT_EXEC_MODE;
    }

    public boolean getLocalM() {
        String tmpVal = (String)cfgProperties.get("location");
        if (tmpVal != null) {
            if (tmpVal.equalsIgnoreCase("remote") == true) {
                return Main.REMOTE;
            }
        }
        return DefaultConfigIRValues.DEFAULT_EXEC_LOCATION;
    }

    public int getConcurency() {
        String tmpVal = (String)cfgProperties.get("concurrent");
        try {
            if (tmpVal != null) {
                int tmp = Integer.parseInt(tmpVal);
                if (tmp > 0) {
                    return tmp;
                }
            }
        } catch (Exception e) {
            //return DefaultConfigIRValues.DEFAULT_COMPETITION;
        }
        return DefaultConfigIRValues.DEFAULT_COMPETITION;
    }

    public String getMCMonitorPort() {
        return ((String)cfgProperties.get("mcMonitorPort"));
    }

    public String getSynChannelPort() {
        return ((String)cfgProperties.get("syncChannelPort"));
    }

    public String getSynChannelHost() {
        return ((String)cfgProperties.get("syncChannelHost"));
    }

    public HashMap getExcludeTestCasesList() {
        synchronized (excludeTestCasesList) {
            return excludeTestCasesList;
        }
    }

    public void addTotExcludeTestCasesList(String key, ArrayList value) {
        if (key == null) {
            return;
        }
        synchronized (excludeTestCasesList) {
            if (excludeTestCasesList.containsKey(key)) {
                ArrayList tmp = (ArrayList)excludeTestCasesList.get(key);
                tmp.addAll(value);
                excludeTestCasesList.put(key, tmp);
            } else {
                excludeTestCasesList.put(key, (ArrayList)value.clone());
            }
        }
    }

    public HashMap setExcludeTestCasesList(HashMap newVal) {
        synchronized (excludeTestCasesList) {
            HashMap retVal = excludeTestCasesList;
            excludeTestCasesList = (HashMap)newVal.clone();
            return retVal;
        }
    }

    public HashMap getIncludeTestCasesList() {
        synchronized (includeTestCasesList) {
            return includeTestCasesList;
        }
    }

    public void addTotIncludeTestCasesList(String key, ArrayList value) {
        if (key == null) {
            return;
        }
        synchronized (includeTestCasesList) {
            includeTestCasesList.put(key, (ArrayList)value.clone());
        }
    }

    public HashMap setIncludeTestCasesList(HashMap newVal) {
        synchronized (includeTestCasesList) {
            HashMap retVal = includeTestCasesList;
            includeTestCasesList = (HashMap)newVal.clone();
            return retVal;
        }
    }
}
