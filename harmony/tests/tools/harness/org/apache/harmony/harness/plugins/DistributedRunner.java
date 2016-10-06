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
 * @author A.Yantsen
 * @version $Revision: 1.22 $
 */
package org.apache.harmony.harness.plugins;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Positive;
import org.apache.harmony.harness.ProcDestroy;
import org.apache.harmony.harness.Runtime;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.TestIR;
import org.apache.harmony.harness.Util;

public class DistributedRunner extends ExecUnit implements Runtime, Positive {

    private final String       classID            = "DistributedRunner";

    public static final String RUN_CMD            = "torun";
    public static final String EXEC_CMD           = "demon";

    public static final String ALL_IN_ONE_MODE    = "AllInOne";
    public static final String SERVER_MODE        = "Server";
    public static final String CLIENT_MODE        = "Client";
    public static final String MODE_PROPERTY_NAME = "mode";

    public static final String METHODNAME         = "test";

    protected ArrayList        runProcs           = new ArrayList();

    protected String decode(String name) {
        String tmp = decodePluginProperty(name, "DistributedRunner");
        if (tmp != null) {
            return tmp;
        }
        return super.decode(name);
    }

    public String[] parseParam(String[] args, String cmd) {
        int cmdOptPos = -1;
        int cnt = 0;
        int index;
        String[] tmpStore;
        if (args == null || cmd == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if (cmd.equalsIgnoreCase(args[i])) {
                cmdOptPos = i;
                break;
            }
        }
        if (cmdOptPos < 0) {
            return null;
        }
        fillParam(args, cmdOptPos);
        tmpStore = new String[1/* java */+ testVMParam.size()
            + testParam.size()];
        tmpStore[cnt++] = cfg.getTestedRuntime();
        for (int i = 0; i < testVMParam.size(); i++) {
            tmpStore[cnt++] = (String)testVMParam.get(i);
        }
        for (int i = 0; i < testParam.size(); i++) {
            tmpStore[cnt++] = (String)testParam.get(i);
        }
        return subsEnvToValue(tmpStore);
    }

    /**
     * Parse parameters for running demon
     * 
     * @param args - arguments for demon
     * @return parsed parameters
     */
    public String[] parseParamDemon(String[] args) {
        return parseParam(args, EXEC_CMD);
    }

    /**
     * Parse parameters for running application not test
     * 
     * @param args - arguments for application
     * @return parsed parameters
     */
    public String[] parseParamExec(String[] args) {
        return parseParamOther(args);
    }

    /**
     * Parse parameters for running remote test in OtherJVM mode
     * 
     * @param args - arguments for test
     * @return parsed parameters
     */
    public String[] parseParamOther(String[] args) {
        return parseParam(args, RUN_CMD);
    }

    /**
     * Parse parameters for running remote test in SameJVM mode
     * 
     * @param args - arguments for test
     * @return parsed parameters
     */
    public String[] parseParamSame(String[] args) {
        int cmdOptPos = -1;
        int cnt = 0;
        String[] tmpStore;
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if (RUN_CMD.equalsIgnoreCase(args[i])) {
                cmdOptPos = i;
                break;
            }
        }
        if (cmdOptPos < 0) {
            return null;
        }
        fillParam(args, cmdOptPos);
        tmpStore = new String[testParam.size()];
        for (int i = 0; i < testParam.size(); i++) {
            tmpStore[cnt++] = (String)testParam.get(i);
        }
        return subsEnvToValue(tmpStore);
    }

    protected void addDemonProcs(Process proc) {
        runProcs.add(proc);
    }

    protected void closeDemonProcs() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcloseDemonProcs(): ";
        int cntDemons = runProcs.size();
        Process proc;
        if (cntDemons < 1) {
            return;
        }
        for (int i = 0; i < cntDemons; i++) {
            proc = (Process)runProcs.remove(0);
            if (proc != null) {
                try {
                    cntIn = cntIn + readIn(proc.getInputStream(), INPUT);
                    cntErr = cntErr + readIn(proc.getErrorStream(), ERROR);
                    new ProcDestroy(proc).start();
                } catch (IOException e) {
                    result.setOutMsg(methodLogPrefix
                        + MessageInfo.UNEX_EXCEPTION + e);
                }
            }
        }
    }

    protected void readDemonProcsOut() throws IOException {
        int cntDemons = runProcs.size();
        Process proc;
        if (cntDemons < 1) {
            return;
        }
        for (int i = 0; i < cntDemons; i++) {
            proc = (Process)runProcs.get(i);
            if (proc != null) {
                cntIn = cntIn + readIn(proc.getInputStream(), INPUT);
                cntErr = cntErr + readIn(proc.getErrorStream(), ERROR);
            }
        }
    }

    protected Process runDRDemon(String[] cmdarray) throws Exception {
        result.setExecCmd(rName, cmdarray);
        Process proc = java.lang.Runtime.getRuntime().exec(cmdarray);
        addDemonProcs(proc);
        // reschedule all process
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            //do nothing
        }
        return proc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#groupCount(org.apache.harmony.harness.TestIR)
     *      by default this method remove the vm option and calculate each param
     *      as separated test
     */
    public synchronized int groupCount(TestIR test) {
        String cfgMode = decode(MODE_PROPERTY_NAME);
        if (ALL_IN_ONE_MODE.equalsIgnoreCase(cfgMode)) {
            return 1;
        } else {
            return test.getRunnerParam().size() - 1;
        }
    }

    public synchronized TestIR[] defineTestPart(TestIR test) {
        TestIR[] retVal;
        String cfgMode = decode(MODE_PROPERTY_NAME);
        if (ALL_IN_ONE_MODE.equalsIgnoreCase(cfgMode)) {
            retVal = new TestIR[1];
            retVal[0] = test;
            return retVal;
        } else {
            retVal = new TestIR[test.getRunnerParam().size() - 1];
            TestIR template = (TestIR)test.clone();
            ArrayList param = new ArrayList();
            param.add(test.getRunnerParam().get(0));
            for (int i = 0; i < retVal.length; i++) {
                template.clearRunnerParam();
                param.add(test.getRunnerParam().get(i + 1));
                template.setRunnerParam((ArrayList)param.clone());
                retVal[i] = (TestIR)template.clone();
                param.remove(1);
            }
            return retVal;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#runOther(java.lang.String[])
     */
    public int runOther(String[] args) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunOther(): ";
        // Only for AllInOne mode
        int execStatus = cfg.getRepError()[0];
        String[] cmdarray;

        try {
            cmdarray = parseParamOther(args);
            if (cmdarray == null) {
                return execStatus;
            }
            result.setExecCmd(rName, cmdarray);
            execStatus = runProc(cmdarray);
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        }
        return execStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#runSame(java.lang.String[])
     */
    public int runSame(String[] args) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunSame(): ";
        //Only for AllInOne mode
        int execStatus = cfg.getRepError()[0];
        String[] testArgs;
        String[] cmdarray;
        Class cl;
        Method m;
        Class[] paramTypes;
        Object[] p = new Object[1];

        try {
            cmdarray = parseParamSame(args);
            if (cmdarray == null && cmdarray.length < 1) {
                return cfg.getRepError()[0];
            }
            result.setExecCmd(rName, cmdarray);
            testArgs = new String[cmdarray.length - 1];
            cl = Class.forName(cmdarray[0]);
            paramTypes = new Class[1];
            paramTypes[0] = String[].class;
            m = cl.getMethod(METHODNAME, paramTypes);
            for (int i = 0; i < testArgs.length; i++) {
                testArgs[i] = cmdarray[i + 1];
            }
            p[0] = testArgs;
            execStatus = ((Integer)m.invoke(cl.newInstance(), p)).intValue();
            return execStatus;
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        }
        return cfg.getRepError()[0];
    }

    /**
     * Run test in Server mode and in OtherJVM test mode. First start test,
     * which is acts as server, then start all applications
     * 
     * @param toExecs - array of toRun's
     * @param demons - number of toRun's
     * @return passed of failed status
     */
    // direct order to test run
    protected int runServerMode(String[][] toExecs, String[][] demons) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunServerMode(): ";
        int execStat = cfg.getRepError()[0];
        int cntExecs;
        int cntDemons = 0;
        int[] status = null;
        Process[] execs = null;
        Process[] demons_procs = null;
        try {
            String[] cmdarray;
            // Start demons
            if (demons != null) {
                cntDemons = demons.length;
                demons_procs = new Process[cntDemons];
                for (int i = 0; i < cntDemons; i++) {
                    cmdarray = parseParamDemon(demons[i]);
                    if (cmdarray == null) {
                        result.setOutMsg(methodLogPrefix
                            + "No command array to run a demon");
                        return execStat;
                    }
                    // demons always start in other VM mode
                    demons_procs[i] = runDRDemon(cmdarray);
                }
            }
            // Start all applications then test
            if (toExecs != null) {
                cntExecs = toExecs.length;
                status = new int[cntExecs];
                execs = new Process[cntExecs];
                if (mode == Main.SAME) {
                    /*
                     * place for code to start distributed tests in SameJVM harness mode
                     */
                } else {
                    execs[0] = runDRDemon(this.parseParamOther(toExecs[0]));
                    //application other than the test always start in other VM
                    for (int i = 1; i < cntExecs; i++) {
                        cmdarray = parseParamExec(toExecs[i]);
                        if (cmdarray == null) {
                            result.setOutMsg(methodLogPrefix
                                + "No command array to run an application");
                            return execStat;
                        }
                        execs[i] = runDRDemon(cmdarray);
                    }
                }

                for (int i = 0; i < curTimeout; i++) {
                    try {
                        for (int j = 0; j < cntExecs; j++) {
                            execs[j].exitValue();
                        }
                        break;
                    } catch (IllegalThreadStateException ie) {
                        Thread.sleep(100);
                        readDemonProcsOut();
                    }
                }
                for (int i = 0; i < cntExecs; i++) {
                    status[i] = execs[i].exitValue();
                }
                execStat = calcExecStatus(status);
            }
        } catch (IllegalThreadStateException e) {
            result.setOutMsg(methodLogPrefix
                + "Timeout!!!. The test is hung (not finished in " + curTimeout
                / 10 + " second).\n");
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        } finally {
            if (status == null && demons_procs != null) {
                //no application to start. run demons only.
                //wait a test timeout or process end.
                int[] statusD = new int[demons_procs.length];
                for (int i = 0; i < curTimeout; i++) {
                    try {
                        for (int j = 0; j < demons_procs.length; j++) {
                            statusD[j] = demons_procs[j].exitValue();
                            //interpret the 0 code for demon as 'passed'
                            //because sometimes it is a external program
                            if (statusD[j] == 0) {
                                statusD[j] = cfg.getRepPassed()[0];
                            }
                        }
                        break;
                    } catch (IllegalThreadStateException ie) {
                        try {
                            Thread.sleep(100);
                            readDemonProcsOut();
                        } catch (Exception e) {
                            result.setOutMsg(methodLogPrefix
                                + MessageInfo.UNEX_EXCEPTION
                                + "while wait for a command execution: " + e);
                        }
                    }
                }
                execStat = cfg.getRepPassed()[0];
            } else if (status == null && demons_procs == null) {
                //no test to start and no demons to start
                execStat = cfg.getRepError()[0];
            }
            closeDemonProcs();
            try {
                result.setOutMsg(new String(tmpErr, 0, cntErr, "UTF-8"));
                result
                    .setTestSpecificInfo(new String(tmpIn, 0, cntIn, "UTF-8"));
            } catch (UnsupportedEncodingException ue) {
                result.setOutMsg(methodLogPrefix
                    + "Test output is not UTF-8 compatible");
                result.setOutMsg(new String(tmpErr, 0, cntErr));
                result.setTestSpecificInfo(new String(tmpIn, 0, cntIn));
            }
            clearBuf();
        }
        return execStat;
    }

    /**
     * Run tests in Client mode. First start application(s), which is(are) acts
     * as server(s), then start test
     * 
     * @param toExecs - array of toRun's
     * @param demons - array of Demons
     * @return passed of failed status
     */
    // back order to run
    protected int runClientMode(String[][] toExecs, String[][] demons) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunClientMode(): ";
        int execStat = cfg.getRepError()[0];
        int cntExecs;
        int cntDemons = 0;
        int[] status = null;
        Process[] execs = null;
        Process[] demons_procs = null;
        try {
            String[] cmdarray;
            // Start demons
            if (demons != null) {
                cntDemons = demons.length;
                demons_procs = new Process[cntDemons];
                for (int i = (cntDemons - 1); i >= 0; i--) {
                    cmdarray = parseParamDemon(demons[i]);
                    if (cmdarray == null) {
                        result.setOutMsg(methodLogPrefix
                            + "No command array to run a demon");
                        return execStat;
                    }
                    // demons always start in other VM mode
                    demons_procs[i] = runDRDemon(cmdarray);
                }
            }
            // Start all applications then test
            if (toExecs != null) {
                cntExecs = toExecs.length;
                status = new int[cntExecs];
                execs = new Process[cntExecs];
                for (int i = cntExecs - 1; i > 0; i--) {
                    cmdarray = parseParamExec(toExecs[i]);
                    if (cmdarray == null) {
                        result.setOutMsg(methodLogPrefix
                            + "No command array to run an application");
                        return execStat;
                    }
                    //application other than the test always start in other VM
                    execs[i] = runDRDemon(cmdarray);
                }
                if (mode == Main.SAME) {
                    /*
                     * place for code to start distributed tests in SameJVM harness mode
                     */
                } else {
                    execs[0] = runDRDemon(this.parseParamOther(toExecs[0]));
                }

                for (int i = 0; i < curTimeout; i++) {
                    try {
                        for (int j = 0; j < cntExecs; j++) {
                            execs[j].exitValue();
                        }
                        break;
                    } catch (IllegalThreadStateException ie) {
                        Thread.sleep(100);
                        readDemonProcsOut();
                    }
                }
                for (int i = 0; i < cntExecs; i++) {
                    status[i] = execs[i].exitValue();
                }
                execStat = calcExecStatus(status);
            }
        } catch (IllegalThreadStateException e) {
            result.setOutMsg(methodLogPrefix
                + "Timeout!!!. The test is hung (not finished in " + curTimeout
                / 10 + " second).\n");
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        } finally {
            if (status == null && demons_procs != null) {
                //no application to start. run demons only.
                //wait a common timeout.
                int[] statusD = new int[demons_procs.length];
                for (int i = 0; i < curTimeout; i++) {
                    try {
                        for (int j = 0; j < demons_procs.length; j++) {
                            statusD[j] = demons_procs[j].exitValue();
                            //interpret the 0 code for demon as 'passed'
                            //because sometimes it is a external program
                            if (statusD[j] == 0) {
                                statusD[j] = cfg.getRepPassed()[0];
                            }
                        }
                        break;
                    } catch (IllegalThreadStateException ie) {
                        try {
                            Thread.sleep(100);
                            readDemonProcsOut();
                        } catch (Exception e) {
                            result.setOutMsg(methodLogPrefix
                                + MessageInfo.UNEX_EXCEPTION
                                + "while wait for a command execution: " + e);
                        }
                    }
                }
                execStat = cfg.getRepPassed()[0];
            } else if (status == null && demons_procs == null) {
                //no test to start and no demons to start
                execStat = cfg.getRepError()[0];
            }
            closeDemonProcs();
            try {
                result.setOutMsg(new String(tmpErr, 0, cntErr, "UTF-8"));
                result
                    .setTestSpecificInfo(new String(tmpIn, 0, cntIn, "UTF-8"));
            } catch (UnsupportedEncodingException ue) {
                result.setOutMsg(methodLogPrefix
                    + "Test output is not UTF-8 compatible");
                result.setOutMsg(new String(tmpErr, 0, cntErr));
                result.setTestSpecificInfo(new String(tmpIn, 0, cntIn));
            }
            clearBuf();
        }
        return execStat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#execTest(TestIR)
     */
    public TResIR execTest(TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\texecTest(): ";
        String[][] toRun;
        String[][] demon;
        String[][] temp;
        String[] genRunParam;
        String[] testKeywords = test.getKeywords();
        String cfgMode = decode(MODE_PROPERTY_NAME);
        ArrayList testParam = test.getRunnerParam();
        int execStat = cfg.getRepError()[0];
        int cnt = 0;
        int cntDemon = 0;
        int cntToRun = 0;
        int paramSize = 0;
        boolean skipTest = true;

        result.clearTestInfo();
        result.setTestID(test.getTestID());
        result.setRepFile(calcFileName(test));
        result.setAllProperty(test.getAllProperty());
        result.setProperty("genEnv", getGeneralEnv());
        if (testParam.size() < 2 || testParam.get(1) == null
            || ((ArrayList)testParam.get(1)).size() == 0) {
            result.setExecStat(execStat);
        } else {
            /*
             * 1. Get all toRun's (String [][] toRun's) 2. First toRun assumed to
             * be test other toRun's assumed to be remote ran applications 3.
             * Depending on type of connection method (relative to test itself):
             * AllInOne, Server, Client run toRun's in following sequence: - for
             * AllInOne method: run only tests toRun providing it with all other
             * toRun's info and parameters - for Server method: run tests toRun
             * first then run applications toRun's - for Client method: run
             * applications toRun's first, then tests toRun
             */
            // Check if test executed in corresponding mode
            if (testKeywords == null || testKeywords.length == 0) {
                // Test can be run in any mode
                skipTest = false;
            } else {
                for (int i = 0; i < testKeywords.length; i++) {
                    if (cfgMode.equalsIgnoreCase(testKeywords[i])) {
                        skipTest = false;
                    }
                }
            }
            if (skipTest) {
                execStat = cfg.getRepModErr()[0];
                result.setOutMsg(methodLogPrefix + "Test cannot be run in "
                    + cfgMode + " mode. Test skipped.");
                result.setExecStat(execStat);
                return result;
            }
            if (mode == Main.SAME) {
                execStat = cfg.getRepModErr()[0];
                result
                    .setOutMsg(methodLogPrefix
                        + "SameJVM mode is not supported for distributed tests. Test skipped.");
                result.setExecStat(execStat);
                return result;
            }
            calcTimeout(test.getTestTimeout());
            genRunParam = new String[0];
            //First populate VM options
            if ((testParam.get(0)) != null) {
                paramSize = ((ArrayList)testParam.get(0)).size();
                genRunParam = new String[paramSize];
                for (int i = 0; i < paramSize; i++) {
                    genRunParam[i] = (String)((ArrayList)testParam.get(0))
                        .get(i);
                }
            }
            // Populate toRun and demon
            cnt = testParam.size() - 1;
            temp = new String[cnt][];
            for (int i = 0; i < cnt; i++) {
                temp[i] = new String[genRunParam.length
                    + ((ArrayList)testParam.get(i + 1)).size()];
                for (int y = 0; y < genRunParam.length; y++) {
                    temp[i][y] = genRunParam[y];
                }
                for (int y = 0; y < ((ArrayList)testParam.get(i + 1)).size(); y++) {
                    temp[i][y + genRunParam.length] = ((String)((ArrayList)testParam
                        .get(i + 1)).get(y));
                }
            }

            for (int i = 0; i < cnt; i++) {
                for (int y = 0; y < temp[i].length; y++) {
                    if (RUN_CMD.equalsIgnoreCase(temp[i][y])) {
                        cntToRun++;
                        break;
                    } else if (EXEC_CMD.equalsIgnoreCase(temp[i][y])) {
                        cntDemon++;
                        break;
                    }
                }
            }
            if (cntToRun > 0) {
                toRun = new String[cntToRun][];
            } else {
                toRun = null;
            }
            if (cntDemon > 0) {
                demon = new String[cntDemon][];
            } else {
                demon = null;
            }
            if (cntToRun == 0 && cntDemon == 0) {
                result.setExecStat(execStat);
                return result;
            }

            cntToRun = 0;
            cntDemon = 0;
            for (int i = 0; i < cnt; i++) {
                for (int y = 0; y < temp[i].length; y++) {
                    if (temp[i][y].equalsIgnoreCase(RUN_CMD)) {
                        toRun[cntToRun] = temp[i];
                        cntToRun++;
                        break;
                    } else if (temp[i][y].equalsIgnoreCase(EXEC_CMD)) {
                        demon[cntDemon] = temp[i];
                        cntDemon++;
                        break;
                    }
                }
            }
            // Execute test depending on test mode
            if (ALL_IN_ONE_MODE.equalsIgnoreCase(cfgMode)) {
                // AllInOne
                // transform String[][] to String[]
                String[] oneToRun;
                cnt = 0;
                for (int i = 0; i < temp.length; i++) {
                    cnt = cnt + temp[i].length;
                }
                oneToRun = new String[cnt];
                cnt = 0;
                // Test starts all applications
                // Here we assume that test knows how to start them.
                for (int i = 0; i < toRun.length; i++) {
                    for (int j = 0; j < toRun[i].length; j++) {
                        oneToRun[cnt + j] = toRun[i][j];
                    }
                    cnt = cnt + toRun[i].length;
                }
                for (int i = 0; i < demon.length; i++) {
                    for (int j = 0; j < demon[i].length; j++) {
                        oneToRun[cnt + j] = demon[i][j];
                    }
                    cnt = cnt + demon[i].length;
                }
                if (mode == Main.OTHER) {
                    long startTime = Util.getCurrentTime();
                    int[] execRes = new int[replayNumber];
                    for (int i = 0; i < replayNumber; i++) {
                        execRes[i] = runOther(oneToRun);
                    }
                    execStat = calcExecStatus(execRes);
                    result.setExecTime(Util.getCurrentTime() - startTime);
                } else {
                    // execStat = runSame(oneToRun);
                    execStat = cfg.getRepModErr()[0];
                }
            } else if (SERVER_MODE.equalsIgnoreCase(cfgMode)) {
                // Server(test listen for connection) Direct order to start
                addSysInfo("Before test run: ", result);
                long startTime = Util.getCurrentTime();
                int[] execRes = new int[replayNumber];
                for (int i = 0; i < replayNumber; i++) {
                    execRes[i] = runServerMode(toRun, demon);
                }
                result.setExecTime(Util.getCurrentTime() - startTime);
                addSysInfo("After test run: ", result);
                execStat = calcExecStatus(execRes);
            } else if (CLIENT_MODE.equalsIgnoreCase(cfgMode)) {
                // Client(test attach to application(s)) Back order to start
                addSysInfo("Before test run: ", result);
                long startTime = Util.getCurrentTime();
                int[] execRes = new int[replayNumber];
                for (int i = 0; i < replayNumber; i++) {
                    execRes[i] = runClientMode(toRun, demon);
                }
                result.setExecTime(Util.getCurrentTime() - startTime);
                addSysInfo("After test run: ", result);
                execStat = calcExecStatus(execRes);
            }
        }
        result.setExecStat(execStat);
        return result;
    }
}
