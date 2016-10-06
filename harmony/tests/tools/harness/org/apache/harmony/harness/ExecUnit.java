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
 * @version $Revision: 1.45 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public abstract class ExecUnit {

    private final String          classID                  = "ExecUnit";

    public static final boolean   ERROR                    = true;
    public static final boolean   INPUT                    = false;
    public static final String    POLICY_FILE_NAME         = "java.policy";
    public static final String    PARAM_CMD                = "vmparam";
    public static final String    SETENV_CMD               = "setenv";
    public static final String    UNSETENV_CMD             = "unsetenv";
    public static final String    SETDIR_CMD               = "rundir";
    public static final String    ENV_MARKER               = "env.";
    protected static final String PERF_OPT_NAME            = "-performance";

    public static final String    PREDEFINED_VM            = "N/A";

    public static String          VM_NAME                  = System
                                                               .getProperty("java.vm.name");
    public static String          OS_NAME                  = System
                                                               .getProperty("os.name")
                                                               + " (version "
                                                               + System
                                                                   .getProperty("os.version")
                                                               + ")";
    public static String          PLATFORM_R_NAME          = System
                                                               .getProperty("os.real.arch");
    public static String          PLATFORM_NAME            = System
                                                               .getProperty("os.arch");

    protected static int          init_buf_size            = 8 * 1024;                                      //8kb
    protected static int          max_buf_size             = 1024 * 1024;                                   //1Mb
    protected static boolean      support_out_file         = true;

    //true if data stored into memory only. set to false when data stored to
    // out file
    protected boolean             create_new_file_out      = true;
    protected boolean             create_new_file_err      = true;

    protected Logging             sameModeLog;

    protected boolean             needSysOptionInit        = false;
    protected boolean             needSysOption            = false;
    protected boolean             needSecInit              = false;
    protected String[]            securityOptions          = {};
    protected boolean             needGeneralVMOptionsInit = false;
    protected String[]            generalVMOptions         = {};
    protected boolean             needGeneralEnvInit       = false;
    protected String[]            generalEnvs              = {};
    protected boolean             needCPInit               = false;
    protected String              cpOption;
    protected boolean             needBCPInit              = false;
    protected String              bcpOption;
    protected int                 replayNumber             = 1;
    protected boolean             isPerformanceRun         = false;
    protected String[]            perfOption;
    protected String[]            logOptions               = { "logLevel",
        "INFO"                                            };

    protected TestIR              curTest;
    protected TResIR              result                   = new TResIR(
                                                               OS_NAME,
                                                               PLATFORM_NAME,
                                                               VM_NAME, null);
    //in the case of more than one execution block in the test
    //this value hold the real (process) exit code of previous
    //block or undefined
    //for next block it is available as $PrevExitCode or ${PrevExitCode}
    protected int                 prevExitCode             = Integer.MIN_VALUE;
    protected String              rName                    = this.getClass()
                                                               .getName();
    protected ConfigIR            cfg;
    protected Logger              log;
    protected EnvProperty         envProvider              = null;
    protected int                 curTimeout;
    protected byte[]              tmpIn                    = new byte[init_buf_size];
    protected byte[]              tmpErr                   = new byte[init_buf_size];
    protected int                 cntIn;
    protected int                 cntErr;
    protected boolean             mode;
    protected ArrayList           testVMParam              = new ArrayList();
    protected ArrayList           testParam                = new ArrayList();

    protected String              sameModeLoggerName       = "org.apache.harmony.harness.MCore.MCConsLogger";

    static {
        String netAdress = "";
        try {
            netAdress = ", host " + InetAddress.getLocalHost().toString();
        } catch (Exception e) {
            //do nothing
        }
        if (PLATFORM_R_NAME != null) {
            PLATFORM_NAME = PLATFORM_NAME + " (" + PLATFORM_R_NAME + ")"
                + netAdress;
        } else {
            PLATFORM_NAME = PLATFORM_NAME + netAdress;
        }
    }

    //initialize internal structures
    protected ExecUnit() {
        if (Main.getCurCore() != null) {
            cfg = Main.getCurCore().getConfigIR();
            log = (Logger)Main.getCurCore().getLogger().getLoggerObject();
        } else {
            cfg = org.apache.harmony.harness.MCore.Main.getCurCore().getConfig();
            log = org.apache.harmony.harness.MCore.Main.getCurCore().getCurOut();
            sameModeLoggerName = org.apache.harmony.harness.MCore.Main
                .getCurCore().getSameModeLoggerName();
        }
        mode = cfg.getExecM();
        if (mode == Main.SAME && sameModeLoggerName != null
            && sameModeLog == null) {
            try {
                sameModeLog = (Logging)Class.forName(sameModeLoggerName)
                    .newInstance();
                sameModeLog.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mode == Main.OTHER) {
            VM_NAME = PREDEFINED_VM;
        }
        if (cfg.needLogLevelOption()) {
            logOptions[1] = cfg.getLogLevel().toString();
        } else {
            logOptions = new String[0];
        }
        ArrayList tmp = cfg.getPluginProperties("Storage");
        if (tmp != null) {
            try {
                HashMap param = (HashMap)((HashMap)tmp.get(1)).get("ex_files");
                if (param != null
                    && ((ArrayList)param.get("supportoutfiles")).size() > 0) {
                    String tmpVal = (String)((ArrayList)param
                        .get("supportoutfiles")).get(0);
                    if (tmpVal.equalsIgnoreCase("false")) {
                        support_out_file = false;
                    }
                }
                if (param != null
                    && ((ArrayList)param.get("maxbufsize")).size() > 0) {
                    String tmpVal = (String)((ArrayList)param.get("maxbufsize"))
                        .get(0);
                    max_buf_size = getSizeinBytes(tmpVal);
                }
            } catch (Exception e) {
                //do nothing
            }
        }
        try {
            replayNumber = Integer.parseInt(cfg.getReplayNumber());
            if (replayNumber < 1) {
                replayNumber = 1;
            }
        } catch (Exception e) {
            replayNumber = 1;
        }
    }

    /*
     * The format is digits( )*(M|m|K|k)(B|b)
     */
    public static int getSizeinBytes(String data) {
        if (data == null || data.length() < 1) {
            return max_buf_size;
        }
        char[] dataArr = data.toCharArray();
        int index = 0;
        int retVal = max_buf_size;
        for (; index < dataArr.length; index++) {
            if (!Character.isDigit(dataArr[index])) {
                break;
            }
        }
        if (index == 0) {
            return max_buf_size;
        }
        try {
            retVal = Integer.parseInt(new String(dataArr, 0, index));
            if (index < dataArr.length) {
                String tmp = new String(dataArr, index, dataArr.length - 1)
                    .toLowerCase();
                if (tmp.indexOf("mb") != -1) {
                    retVal = retVal * 1024 * 1024;
                }
                if (tmp.indexOf("kb") != -1) {
                    retVal = retVal * 1024;
                }
            }
            if (retVal < init_buf_size * 8) {
                retVal = init_buf_size * 8;
            }
            return retVal;
        } catch (Exception e) {
            return max_buf_size;
        }
    }

    /*
     * calculate the file name for use by reporter
     */
    protected String calcFileName(TestIR test) {
        if (test == null) {
            return null;
        }
        return test.getTestID();
    }

    /*
     * return the array of resource name needed for the test
     */
    public synchronized String[] needSpecialResource(TestIR test) {
        try {
            if (test.getResources() != null && test.getResources().length > 0) {
                return test.getResources();
            }
        } catch (Exception e) {
            //return null
        }
        return null;
    }

    /*
     * return the count for simultaneous run part of the test 1 - means that all
     * test components run one-by-one (usual way) more than 1 - some parts
     * should be run simultaneously (for distributed tests, for example) The
     * parts of the test to run simultaneously are defined by test's execution
     * unit (method defineTestPart(TestIR))
     */
    protected synchronized int groupCount(TestIR test) {
        return 1;
    }

    /*
     * return the arrays of tests for simultaneous run by default array length
     * is 1 - means that all test components run one-by-one (usual way) more
     * than 1 - some parts should be run simultaneously (for distributed tests,
     * for example)
     */
    protected synchronized TestIR[] defineTestPart(TestIR test) {
        TestIR[] retVal = new TestIR[1];
        retVal[0] = test;
        return retVal;
    }

    /*
     * return the results of many test run as one test result command, output
     * message and test specific information are merged other fields copied from
     * the first element
     */
    protected synchronized TResIR mergeResults(TResIR[] testRes) {
        if (testRes == null) {
            return null;
        }
        if (testRes.length == 1) {
            return testRes[0];
        }
        for (int i = 0; i < testRes.length; i++) {
            if (testRes[i] == null) {
                return testRes[0];
            }
        }
        TResIR retVal = new TResIR("", "", "", "");
        for (int i = 0; i < testRes.length; i++) {
            retVal.setExecCmd(testRes[i].getExecCmd());
            retVal.setOutMsg(testRes[i].getOutMsg());
            retVal.setTestSpecificInfo(testRes[i].getTestSpecificInfo());
            retVal.setExecTime(testRes[i].getExecTime());
        }
        retVal.setTestID(testRes[0].getTestID());
        retVal.setDate(testRes[0].getDate());
        retVal.setTestedOS(testRes[0].getTestedOS());
        retVal.setTestedVM(testRes[0].getTestedVM());
        retVal.setTestedPlatform(testRes[0].getTestedPlatform());
        retVal.setAllProperty(testRes[0].getAllProperty());
        retVal.setExecStat(mergeStatuses(testRes));
        return retVal;
    }

    public int mergeStatuses(TResIR[] testRes) {
        int[] retCodes = new int[testRes.length];
        for (int i = 0; i < retCodes.length; i++) {
            retCodes[i] = testRes[i].getExecStat();
        }
        return calcExecStatus(retCodes);
    }

    public TestIR updateTimeoutFromEnv(TestIR test) {
        float tFactor = test.getTestTimeout();
        if (test.getProperty("TestTimeoutFactor") != null) {
            String factor = subsEnvToValue(test
                .getProperty("TestTimeoutFactor"));
            try {
                tFactor = Float.parseFloat(factor);
            } catch (Exception ee) {
                //let's stay default: 1.0
            }
            test.setTestTimeout(tFactor);
        }
        return test;
    }

    /**
     * Calculate time for execution in the time slices (100 ms == 1 slice)
     * 
     * @param testTime - tests timeout factor
     */
    protected void calcTimeout(float testTime) {
        int time = (int)(testTime * cfg.getGenTimeout() * 10);
        if (time == 0) {
            curTimeout = cfg.getGenTimeout() * 10; //testTime == 1 by default
        } else {
            curTimeout = time;
        }
    }

    /**
     * The parameters as required by java.lang.Runtime.exec(String[]) to run a
     * process with one extra string for a command code
     * 
     * @param args parameters
     * @return execution status
     */
    public abstract int runOther(String[] args);

    /**
     * The parameters as required by runOther method
     * 
     * @param args parameters
     * @return execution status
     */
    public abstract String[] parseParamOther(String[] args);

    /**
     * @param args parameters
     * @return execution status
     */
    public abstract int runSame(String[] args);

    /**
     * The parameters as required by runOther method
     * 
     * @param args parameters
     * @return execution status
     */
    public abstract String[] parseParamSame(String[] args);

    /*
     * return the security option set to the file from cfg_ts directory if this
     * file existed
     */
    public String[] getSecurityOptions() {
        String fileName = cfg.getTestSuiteConfigRoot() + File.separator
            + POLICY_FILE_NAME;
        if (needSecInit == false && new File(fileName).isFile()) {
            needSecInit = true;
            securityOptions = new String[1];
            securityOptions[0] = "-Djava.security.policy=" + fileName;
        }
        return securityOptions;
    }

    /*
     * return the current set for classpath option as: TestSuiteClassRoot +
     * TempStorage(for compiled classes) + all jar's from TestSuiteLibRoot +
     * system.classpath
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
     * return the current value for the bootclasspath option from the
     * configuration Note, this option is non-standard so do nothing with it
     */
    public String getBCPOptions() {
        if (needBCPInit == false) {
            needBCPInit = true;
            String cp = cfg.getBootClassPath();
            if (cp != null && cp.trim().length() > 0) {
                bcpOption = cp;
            }
        }
        return bcpOption;
    }

    /*
     * return the current value for the bootclasspath option from the
     * configuration Note, this option is non-standard so do nothing with it
     */
    public String[] getPerfOptions() {
        if (isPerformanceRun == false) {
            isPerformanceRun = true;
            String perfOpt = cfg.getPerformanceOption();
            if (perfOpt != null && perfOpt.length() > 0) {
                perfOption = new String[2];
                perfOption[0] = PERF_OPT_NAME;
                perfOption[1] = perfOpt;
            }
        }
        return perfOption;
    }

    /*
     * return the current values for the VM options from the configuration
     */
    public String[] getGeneralVMOptions() {
        if (needGeneralVMOptionsInit == false) {
            needGeneralVMOptionsInit = true;
            String[] tmp = cfg.getGenralVMOptAsArray();
            if (tmp != null && tmp.length > 0) {
                generalVMOptions = tmp;
            }
        }
        return generalVMOptions;
    }

    /*
     * return the additional values for the environment from the configuration.
     * the values should be separated by "," or ";" or "\n" or "\t" or " "
     * (whitespace) the format for variables is "name=value"
     */
    public String[] getGeneralEnv() {
        if (needGeneralEnvInit == false) {
            needGeneralEnvInit = true;
            String envs = cfg.getGeneralEnvAsString();
            String[] tmp = cfg.getGeneralEnvAsArray();
            generalEnvs = subsEnvToValue(tmp);
        }
        return generalEnvs;
    }

    /*
     * return the string with current system information based on SysInfo class
     * or null, if this run does not need into this information
     */
    public void addSysInfo(String comment, TResIR result) {
        if (needSysOptionInit == false) {
            needSysOptionInit = true;
            needSysOption = cfg.getSysInfoFlag();
        }
        if (needSysOption) {
            if (comment != null) {
                result.setOutMsg(comment + "\n" + SysInfo.getMemInfo());
            } else {
                result.setOutMsg(SysInfo.getMemInfo());
            }
        }
    }

    /*
     * Return the directory to run the test or null
     */
    public File getRunDirectory(String[] cmd) {
        for (int i = cmd.length - 1; i >= 0; i--) {
            if (cmd[i].equalsIgnoreCase(SETDIR_CMD)) {
                try {
                    File retVal = new File(cmd[i + 1]);
                    if (retVal.isDirectory()) {
                        return retVal;
                    }
                } catch (Exception e) {
                    //return null;
                }
            }
        }
        return null;
    }

    /*
     * Return environment for this test or null if no special environment were
     * set/unset
     */
    public String[] getEnviroment() {
        return getEnviroment(new String[0]);
    }

    /*
     * Return environment for this test or null if no special environment were
     * set/unset. The special command is selected from the command archive
     */
    public String[] getEnviroment(String[] cmd) {
        String inheritEnv = cfg.getInheritEnv();
        EnvProperty envp = new EnvProperty();
        if (inheritEnv != null
            && (inheritEnv.toLowerCase().equalsIgnoreCase("true") || inheritEnv
                .toLowerCase().equalsIgnoreCase("yes"))) {
            envp.setInheritSystemEnv(true);
        } else {
            envp.setInheritSystemEnv(false);
        }
        String[] commonEnv = getGeneralEnv();
        if (commonEnv != null) {
            for (int i = 0; i < commonEnv.length; i++) {
                envp.addEnv(commonEnv[i]);
            }
        }
        if (cmd != null) {
            for (int i = 0; i < cmd.length; i++) {
                if (cmd[i].equalsIgnoreCase(SETENV_CMD)) {
                    envp.addEnv(cmd[i + 1]);
                    i++;
                } else if (cmd[i].equalsIgnoreCase(UNSETENV_CMD)) {
                    envp.removeEnv(cmd[i + 1]);
                    i++;
                }
            }
        }
        return envp.getEnv();
    }

    /*
     * Remove all service command (like setenv_cmd) from command array
     */
    protected String[] clearServiceCommand(String[] cmd) {
        String[] retVal;
        ArrayList tmpArr = new ArrayList();
        for (int i = 0; i < cmd.length; i++) {
            if (cmd[i].equalsIgnoreCase(SETENV_CMD)
                || cmd[i].equalsIgnoreCase(UNSETENV_CMD)
                || cmd[i].equalsIgnoreCase(SETDIR_CMD)
                || cmd[i].equalsIgnoreCase(PARAM_CMD)) {
                i++; //miss the environment variable to set/unset or dir etc
            } else {
                tmpArr.add(cmd[i]);
            }
        }
        retVal = new String[tmpArr.size()];
        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = (String)tmpArr.get(i);
        }
        return retVal;
    }

    /*
     * replace all occurrence of the string 'from' to string 'to' into string
     * 'data'
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

    /*
     * replace all occurrence of the even strings (0, 2 ...) on the odd string
     * (1, 3...) from mapList into arguments array
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
     * process all params for test to substitute the $value on the value from
     * the configuration. Empty values will be skipped.
     */
    public String[] subsEnvToValue(String[] args) {
        String tmpVal;
        ArrayList tmpArr = new ArrayList();
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            String tmp = args[i];
            while (tmp.indexOf(Constants.PARAM_SEPARATOR) > -1) {
                tmpVal = subsEnvToValue(tmp);
                if (tmp.equals(tmpVal)) {
                    break;
                } else {
                    tmp = tmpVal;
                }
            }
            if (tmp.trim().length() != 0) {
                tmpArr.add(tmp);
            }
        }
        String[] retVal = new String[tmpArr.size()];
        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = (String)tmpArr.get(i);
        }
        tmpArr = cfg.getMapsList();
        if (tmpArr != null && tmpArr.size() > 0 && (tmpArr.size() % 2) == 0) {
            return mapPaths(retVal, tmpArr);
        }
        return retVal;
    }

    /*
     * return the length of the string to decode as property name the end of name
     * is '$' or any symbol that is not a java identifier (except '.')
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

    // return the full name of the tests directory
    public String getTestDirectory() {
        String tmp = calcFileName(curTest);
        if (tmp == null) {
            return cfg.getTestSuiteTestRoot();
        }
        int val = tmp.lastIndexOf(Constants.INTERNAL_FILE_SEP_C);
        if (val > -1) {
            return cfg.getTestSuiteTestRoot() + Constants.INTERNAL_FILE_SEP_C
                + tmp.substring(0, val);
        } else {
            return cfg.getTestSuiteTestRoot();
        }
    }

    // return the value of environment variable 'name' or
    // null if variable undefined
    public String getEnvVariable(String name) {
        if (envProvider == null) {
            envProvider = new EnvProperty();
        }
        return envProvider.getEnv(name);
    }

    // decode property. The decoding way may depend on property name
    protected String decodeProperty(String name) {
        if (name == null) {
            return "";
        }
        if ("TestSuiteRoot".equalsIgnoreCase(name)) {
            return cfg.getTestSuiteRoot();
        } else if ("TestSuiteClassRoot".equalsIgnoreCase(name)) {
            return cfg.getTestSuiteClassRoot();
        } else if ("GenTimeout".equalsIgnoreCase(name)) {
            return "" + cfg.getGenTimeout();
        } else if ("TestTimeout".equalsIgnoreCase(name)) {
            return "" + curTimeout / 10;
        } else if ("TestDirectory".equalsIgnoreCase(name)) {
            return getTestDirectory();
        } else if ("FileSeparator".equalsIgnoreCase(name)) {
            return File.separator;
        } else if ("PathSeparator".equalsIgnoreCase(name)) {
            return File.pathSeparator;
        } else if ("PrevExitCode".equalsIgnoreCase(name)) {
            return "" + prevExitCode;
        } else if ("TestedRuntime".equalsIgnoreCase(name)) {
            return cfg.getTestedRuntime();
        } else if ("TestedCompile".equalsIgnoreCase(name)) {
            return cfg.getTestedCompile();
        } else if ("TempStorage".equalsIgnoreCase(name)) {
            return cfg.getTempStorage();
        } else if ("CP".equalsIgnoreCase(name)) {
            return getCPOptions();
        } else if (name.toLowerCase().startsWith(ENV_MARKER)) {
            String value = getEnvVariable(name.substring(ENV_MARKER.length()));
            if (value == null) {
                return "";
            } else {
                return value;
            }
        } else {
            Method[] methods = cfg.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equalsIgnoreCase("get" + name)) {
                    try {
                        String tmp = (methods[i].invoke(cfg, null)).toString();
                        return tmp;
                    } catch (Exception e) {
                        return "";
                    }
                }
            }
        }
        return "";
    }

    // decode plugin property for plugin 'pluginName'
    protected String decodePluginProperty(String name, String pluginName) {
        HashMap hm = (HashMap)((HashMap)((ArrayList)cfg
            .getPluginProperties("ExecUnit")).get(1)).get(pluginName);
        Object obj = hm.get(name);
        if (obj != null) {
            obj = ((ArrayList)obj).get(0);
            if (obj != null) {
                return obj.toString();
            }
        }
        return null;
    }

    protected String decode(String name) {
        String retVal;
        try {
            retVal = decodeProperty(name);
            if (retVal == null || retVal.length() == 0
                || retVal.equalsIgnoreCase(Constants.NO_VALUE_MSG)) {
                //return zero-length string for undefined variable
                retVal = cfg.getProperty(name);
            }
            if (retVal == null || retVal.length() == 0
                || retVal.equalsIgnoreCase(Constants.NO_VALUE_MSG)) {
                if (name.toLowerCase().startsWith(ENV_MARKER)) {
                    return "";
                }
                return name;
            } else {
                return retVal;
            }
        } catch (Exception e) { //name.length = paramStart.length for example
            return name;
        }
    }

    /*
     * substitute the $value in the input string to value from the configuration
     */
    public String subsEnvToValue(String str) {
        String tmpStore;
        int varLength;
        int cnt = str.indexOf(Constants.PARAM_SEPARATOR);
        boolean used_param_start_end = false;
        if (cnt == -1) {
            return str;
        }
        char[] thisSting = str.toCharArray();
        if (cnt == str.lastIndexOf(Constants.PARAM_SEPARATOR)) {
            //last(one) variable to change
            try {
                if (str.indexOf(Constants.PARAM_START) != -1) {
                    //calc length for ${value} as index of first '}'
                    varLength = str.indexOf(Constants.PARAM_END)
                        + Constants.PARAM_END.length();
                    used_param_start_end = true;
                } else if (str.startsWith(Constants.PARAM_SEPARATOR
                    + "FileSeparator")
                    || str.startsWith(Constants.PARAM_SEPARATOR
                        + "PathSeparator")) { //predefined
                    // names
                    varLength = Constants.PARAM_SEPARATOR.length()
                        + "FileSeparator".length(); //it has equal size
                } else if (str.startsWith(Constants.PARAM_SEPARATOR
                    + "PrevExitCode")) {
                    varLength = Constants.PARAM_SEPARATOR.length()
                        + "PrevExitCode".length();
                } else {
                    // if not special symbols split it by predefined rules
                    varLength = getVarLength(cnt
                        + Constants.PARAM_SEPARATOR.length(), thisSting);
                }
                String tmp;
                if (used_param_start_end == false) {
                    tmp = decode(str.substring(cnt
                        + Constants.PARAM_SEPARATOR.length(), varLength));
                } else {
                    tmp = decode(str.substring(cnt
                        + Constants.PARAM_SEPARATOR.length()
                        + Constants.PARAM_START.length(), varLength
                        - Constants.PARAM_END.length()));
                }
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
                    + subsEnvToValue(Constants.PARAM_SEPARATOR
                        + (String)stT.nextElement());
            }
        }
        return tmpStore;
    }

    protected boolean storeDataToFile(byte[] array, int sizeToWrite,
        String file_name, boolean create_new) {
        if (array == null || sizeToWrite <= 0 || file_name == null) {
            return false;
        }
        FileOutputStream fw = null;
        try {
            if (create_new) {
                new File(file_name).getParentFile().mkdirs();
                fw = new FileOutputStream(file_name);
            } else {
                fw = new FileOutputStream(file_name, true);
            }
            fw.write(array, 0, sizeToWrite);
            return true;
        } catch (Exception e) {
            // just do nothing. Store data into memory buffer
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (Exception e) {
                    //do nothing
                }
            }
        }
    }

    /*
     * resize the byte buffer and copy content of the buffer to the begin of new
     * Store the data to file in case of buf size increase the MAX_BUF_SIZE
     */
    protected void resizeBuf(boolean buf) {
        byte[] tmpBuf;
        if (buf == ERROR) {
            if (tmpErr.length >= max_buf_size && support_out_file) {
                // store process data to file. Note, for remote test execution
                // the TestResultRoot usually is incorrect directory and data
                // will store in the memory buffer
                if (storeDataToFile(tmpErr, cntErr, cfg.getTestResultRoot()
                    + File.separator + curTest.getTestID() + ".err",
                    create_new_file_err)) {
                    create_new_file_err = false;
                    cntErr = 0; //current position in buffer is 0, buffer
                    // length not changed
                    return;
                }
            }
            //increase the buffer size
            tmpBuf = new byte[tmpErr.length * 2];
            for (int i = 0; i < cntErr; i++) {
                tmpBuf[i] = tmpErr[i];
            }
            tmpErr = tmpBuf;
        } else {
            if (tmpIn.length >= max_buf_size && support_out_file) {
                if (storeDataToFile(tmpIn, cntIn, cfg.getTestResultRoot()
                    + File.separator + curTest.getTestID() + ".out",
                    create_new_file_out)) {
                    create_new_file_out = false;
                    cntIn = 0; //current position in buffer is 0, buffer
                    // length not changed
                    return;
                }
            }
            tmpBuf = new byte[tmpIn.length * 2];
            for (int i = 0; i < cntIn; i++) {
                tmpBuf[i] = tmpIn[i];
            }
            tmpIn = tmpBuf;
        }
    }

    /*
     * read the data from input (out or err) to corresponding buffer
     */
    protected int readIn(InputStream in, boolean from) throws IOException {
        int cntTmp;
        if (in == null) {
            return 0;
        }
        cntTmp = in.available();
        if (cntTmp == 0) {
            return 0;
        }
        if (from == ERROR) {
            while ((cntTmp + cntErr) > tmpErr.length) {
                resizeBuf(ERROR);
            }
            in.read(tmpErr, cntErr, cntTmp);
        } else {
            while ((cntTmp + cntIn) > tmpIn.length) {
                resizeBuf(INPUT);
            }
            in.read(tmpIn, cntIn, cntTmp);
        }
        return cntTmp;
    }

    /*
     * clear the buffer contents and counters
     */
    protected void clearBuf() {
        for (int i = 0; i < cntIn; i++) {
            tmpIn[i] = 0;
        }
        cntIn = 0;
        for (int i = 0; i < cntErr; i++) {
            tmpErr[i] = 0;
        }
        cntErr = 0;
    }

    /*
     * add the process as external to the Main to shutdown it when the harness
     * will shutdown
     */
    protected void addExtProcs(Process proc) {
        if (Main.getCurCore() != null) {
            Main.getCurCore().addExtProcs(proc);
        } else {
            org.apache.harmony.harness.MCore.Main.getCurCore().addExtProcs(proc);
        }
    }

    protected boolean fillParam(String[] args, int cmdOptPos) {
        int cnt;
        int index;
        int secInfoPresent = -1;
        String[] tmpStore;
        String[] tmpArr;
        ArrayList tempParam = new ArrayList();
        testVMParam.clear();
        testParam.clear();
        if (args == null || cmdOptPos < 0) {
            return false;
        }
        tmpArr = getGeneralVMOptions();
        for (int i = 0; i < tmpArr.length; i++) {
            tempParam.add(tmpArr[i]);
        }
        for (int i = 0; i < cmdOptPos; i++) {
            tempParam.add(args[i]);
        }
        for (int i = cmdOptPos + 1; i < args.length; i++) {
            if (PARAM_CMD.equalsIgnoreCase(args[i])) {
                try {
                    tempParam.add(args[++i]);
                } catch (Exception e) {
                    log
                        .info(MessageInfo.MSG_PREFIX + classID + "\t"
                            + PARAM_CMD
                            + " expect parameter value. Unexpected exception: "
                            + e);
                }
            } else {
                testParam.add(args[i]);
            }
        }
        if (!tempParam.contains("-cp") && !tempParam.contains("-classpath")) {
            tempParam.add("-cp");
            tempParam.add(getCPOptions());
        }
        for (int i = 0; i < tempParam.size(); i++) {
            if (((String)tempParam.get(i)).startsWith("-Djava.security.policy")) {
                secInfoPresent = 1;
                break;
            }
        }
        if (secInfoPresent == -1) {
            tmpArr = getSecurityOptions();
            for (int i = 0; i < tmpArr.length; i++) {
                tempParam.add(tmpArr[i]);
            }
        }
        if (getBCPOptions() != null) {
            tempParam.add(bcpOption);
        }
        if (!testParam.contains("logLevel")) {
            tmpArr = logOptions;
            for (int i = 0; i < tmpArr.length; i++) {
                testParam.add(tmpArr[i]);
            }
        }
        if (getPerfOptions() != null) {
            tmpArr = getPerfOptions();
            for (int i = 0; i < tmpArr.length; i++) {
                testParam.add(tmpArr[i]);
            }
        }
        //if some VM options is redefined in testVMParam - skip old values
        //old = first
        cnt = tempParam.size();
        for (int i = 0; i < cnt; i++) {
            String tmp = (String)tempParam.remove(0);
            if (tmp.startsWith("-")) { //option name
                if (!testVMParam.contains(tmp)) { //new option
                    if ("-classpath".equalsIgnoreCase(tmp)
                        && testVMParam.contains("-cp")) {
                        i++;
                        index = testVMParam.indexOf("-cp") + 1;
                        tmp = (String)tempParam.remove(0);
                        testVMParam.remove(index);
                        testVMParam.add(index, tmp);
                        break;
                    }
                    if ("-cp".equalsIgnoreCase(tmp)
                        && testVMParam.contains("-classpath")) {
                        i++;
                        index = testVMParam.indexOf("-classpath") + 1;
                        tmp = (String)tempParam.remove(0);
                        testVMParam.remove(index);
                        testVMParam.add(index, tmp);
                        break;
                    }
                    //add option and values to final params
                    testVMParam.add(tmp);
                    int remainOptSize = tempParam.size();
                    for (int y = 0; y < remainOptSize; y++) {
                        tmp = (String)tempParam.get(0);
                        if (tmp.startsWith("-")) { //next option
                            break;
                        } else {
                            i++;
                            testVMParam.add(tempParam.remove(0)); //option
                            // values
                        }
                    }
                } else { //option to redefine values
                    int remainOptSize = tempParam.size();
                    int start = testVMParam.indexOf(tmp) + 1;
                    for (int y = 0; y < remainOptSize; y++) {
                        tmp = (String)tempParam.get(0);
                        if (tmp.startsWith("-")) { //next option
                            break;
                        } else {
                            i++;
                            testVMParam.remove(start + y);
                            testVMParam.add(start + y, tempParam.remove(0)); //option
                            // values
                        }
                    }
                }
            } else { //special or coded option without '-'. Do nothing
                testVMParam.add(tmp);
            }
        }
        return true;
    }

    /*
     * run the process with common environment without waiting for exit
     */
    protected int runDemon(String[] cmdarray) throws Exception {
        return runDemon(cmdarray, getEnviroment());
    }

    /*
     * run the process with specified environment without waiting for exit
     */
    protected int runDemon(String[] cmdarray, String[] envp) throws Exception {
        return runDemon(cmdarray, envp, new File(cfg.getTempStorage()));
    }

    /*
     * add environment to result. In the case of null environment add null (to
     * identify command and env setting
     */
    protected void addEnvToResult(String[] data) {
        try {
            ArrayList tmp = (ArrayList)result.getProperty("genEnv");
            if (tmp == null) {
                tmp = new ArrayList();
            }
            tmp.add(data);
            result.setProperty("genEnv", tmp);
        } catch (Exception e) {
            //no env set to report
        }
    }

    /*
     * run the process with specified environment from the specified directory
     * without waiting for exit
     */
    protected int runDemon(String[] cmdarray, String[] envp, File dir)
        throws Exception {
        Process proc = java.lang.Runtime.getRuntime().exec(cmdarray, envp, dir);
        addEnvToResult(envp);
        addExtProcs(proc);
        return cfg.getRepPassed()[0];
    }

    /*
     * run the pointed process with common environment and wait for result
     */
    public int runProc(String[] args) throws IOException, InterruptedException {
        return runProc(args, getEnviroment());
    }

    /*
     * run the pointed process with common environment and wait for result
     */
    public int runProc(String[] args, String[] envp) throws IOException,
        InterruptedException {
        File workDir = new File(cfg.getTempStorage());
        return runProc(args, envp, workDir);
    }

    /*
     * run the pointed process with common environment from the pointed directory
     * and wait for result Also set variable 'PrevExitCode' for future decoding
     */
    public int runProc(String[] args, String[] envp, File workDir)
        throws IOException, InterruptedException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunProc(): ";
        int execStatus = cfg.getRepError()[0];
        Process proc = null;
        InputStream err = null;
        InputStream in = null;
        addEnvToResult(envp);
        try {
            proc = java.lang.Runtime.getRuntime().exec(args, envp, workDir);
            err = proc.getErrorStream();
            in = proc.getInputStream();
            for (int i = 0; i < curTimeout; i++) {
                try {
                    proc.exitValue();
                    break;
                } catch (IllegalThreadStateException ie) {
                    Thread.sleep(100);
                    cntIn = readIn(in, INPUT) + cntIn;
                    cntErr = readIn(err, ERROR) + cntErr;
                }
            }
            execStatus = proc.exitValue();
            prevExitCode = execStatus; //this variable used for decoding only
            cntIn = readIn(in, INPUT) + cntIn;
            cntErr = readIn(err, ERROR) + cntErr;
        } catch (IllegalThreadStateException e) {
            if (proc != null) {
                cntIn = readIn(in, INPUT) + cntIn;
                cntErr = readIn(err, ERROR) + cntErr;
                //the proc.destroy() can hang up so run it as separated thread
                //give him a chance to work and go
                new ProcDestroy(proc).start();
                Thread.yield();
            }
            result.setOutMsg(methodLogPrefix
                + "Timeout!!!. The test hung (not finished in " + curTimeout
                / 10 + " second).\n");
        } finally {
            if (create_new_file_out && create_new_file_err) {
                try {
                    result.setOutMsg(new String(tmpErr, 0, cntErr, "UTF-8"));
                    result.setTestSpecificInfo(new String(tmpIn, 0, cntIn,
                        "UTF-8"));
                } catch (UnsupportedEncodingException ue) {
                    result.setOutMsg(methodLogPrefix
                        + "Test output is not UTF-8 compatible");
                    result.setOutMsg(new String(tmpErr, 0, cntErr));
                    result.setTestSpecificInfo(new String(tmpIn, 0, cntIn));
                }
            } else {
                if (!create_new_file_out) {
                    storeDataToFile(tmpIn, cntIn, cfg.getTestResultRoot()
                        + File.separator + curTest.getTestID() + ".out",
                        create_new_file_out);
                    result.setTestSpecificInfo(methodLogPrefix
                        + "Test system.out output is available as file: "
                        + cfg.getTestResultRoot() + File.separator
                        + curTest.getTestID() + ".out");
                }
                if (!create_new_file_err) {
                    storeDataToFile(tmpErr, cntErr, cfg.getTestResultRoot()
                        + File.separator + curTest.getTestID() + ".err",
                        create_new_file_err);
                    result.setOutMsg(methodLogPrefix
                        + "Test system.err output is available as file: "
                        + cfg.getTestResultRoot() + File.separator
                        + curTest.getTestID() + ".err");
                }
            }
            clearBuf();
            //force to close descriptors
            if (err != null) {
                try {
                    err.close();
                    err = null;
                } catch (Exception e) {
                    //do nothing
                }
            }
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (Exception e) {
                    //do nothing
                }
            }
            if (proc != null) {
                ProcDestroy pd = new ProcDestroy(proc);
                synchronized (pd) {
                    pd.start();
                    for (int i = 0; i < cfg.getGenTimeout(); i++) {
                        if (pd.started) {
                            break;
                        }
                        pd.wait(100);
                    }
                }
                proc = null;
            }
        }
        return execStatus;
    }

    /*
     * Calculate the execution status. If the test have more than one command to
     * execute the result is passed only ALL command are passed. Algorithm: -
     * checks for passed codes. If all codes is passed - exit with passed status
     * (cfg.passed[0]) - checks for error codes. If at least one error - exit
     * with error status (detected) - checks for failed codes. If at least one
     * failed - exit with failed status (detected) - find unknown code in array
     * and exit with unknown status (detected)
     */
    protected int calcExecStatus(int[] statuses) {
        if (statuses.length == 0) {
            return cfg.getRepError()[0];
        }
        int tmpStat = cfg.getRepPassed()[0];
        int[] codes = cfg.getRepPassed();
        for (int i = 0; i < statuses.length; i++) {
            for (int y = 0; y < codes.length; y++) {
                if (codes[y] != statuses[i]) {
                    return statuses[i];
                }
            }
        }
        return tmpStat;
    }

    /* used by dispatcher or mcore to set the current test for execution */
    public void setCurTest(TestIR test) {
        curTest = test;
    }

    public String[][] getRunParameters(TestIR test) {
        if (((ArrayList)test.getRunnerParam()).size() < 2
            || ((ArrayList)test.getRunnerParam().get(1)) == null
            || ((ArrayList)test.getRunnerParam().get(1)).size() == 0) {
            return null;
        } else {
            String[][] retVal = new String[((ArrayList)test.getRunnerParam())
                .size() - 1][];
            String[] runParam = new String[0];
            if (((ArrayList)test.getRunnerParam().get(0)) != null) {
                runParam = new String[((ArrayList)test.getRunnerParam().get(0))
                    .size()];
                for (int i = 0; i < ((ArrayList)test.getRunnerParam().get(0))
                    .size(); i++) {
                    runParam[i] = (String)((ArrayList)test.getRunnerParam()
                        .get(0)).get(i);
                }
            }
            for (int i = 0; i < retVal.length; i++) {
                retVal[i] = new String[runParam.length
                    + ((ArrayList)test.getRunnerParam().get(i + 1)).size()];
                for (int y = 0; y < runParam.length; y++) {
                    retVal[i][y] = runParam[y];
                }
                for (int y = 0; y < ((ArrayList)test.getRunnerParam()
                    .get(i + 1)).size(); y++) {
                    retVal[i][y + runParam.length] = ((String)((ArrayList)test
                        .getRunnerParam().get(i + 1)).get(y));
                }
            }
            return retVal;
        }
    }

    /**
     * Run test with specified name.
     * 
     * @param test to execute
     * @return execution status
     */
    public TResIR execTest(TestIR test) {
        String[][] runParams;
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\texecTest(): ";
        int[] execStat;
        if (test == null) {
            result = new TResIR(OS_NAME, PLATFORM_NAME, VM_NAME, "");
            result.setOutMsg(methodLogPrefix + "no test to run");
            result.setExecStat(cfg.getRepError()[0]);
            return result;
        }
        result = new TResIR(OS_NAME, PLATFORM_NAME, VM_NAME, test.getTestID());
        result.setRepFile(calcFileName(test));
        result.setAllProperty(test.getAllProperty());
        create_new_file_out = true;
        create_new_file_err = true;
        if (test.getRunnerParam() == null) {
            result.setOutMsg(methodLogPrefix + "no command to run a test");
            result.setExecStat(cfg.getRepError()[0]);
        } else if (((ArrayList)test.getRunnerParam()).size() < 2
            || ((ArrayList)test.getRunnerParam().get(1)) == null
            || ((ArrayList)test.getRunnerParam().get(1)).size() == 0) {
            result.setOutMsg(methodLogPrefix + "no arguments to run a test");
            result.setExecStat(cfg.getRepError()[0]);
        } else {
            if (((mode == Main.OTHER) && (test.getSameVMOnly() == true))
                || ((mode == Main.SAME) && (test.getOtherVMOnly() == true))) {
                result
                    .setOutMsg(methodLogPrefix
                        + "according to the test description it does not support current configuration");
                result.setExecStat(cfg.getRepModErr()[0]);
            } else {
                calcTimeout(test.getTestTimeout());
                runParams = getRunParameters(test);
                execStat = new int[runParams.length * replayNumber];
                for (int rcnt = 0; rcnt < replayNumber; rcnt++) {
                    for (int i = 0; i < runParams.length; i++) {
                        if (mode == Main.OTHER) {
                            execStat[i + (rcnt * runParams.length)] = runOther(runParams[i]);
                        } else {
                            execStat[i + (rcnt * runParams.length)] = runSame(runParams[i]);
                        }
                    }
                }
                result.setExecStat(calcExecStatus(execStat));
            }
        }
        return result;
    }
}