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
 * @version $Revision: 1.10 $
 */
package org.apache.harmony.harness.plugins;

import org.apache.harmony.harness.Compile;
import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Negative;
import org.apache.harmony.harness.Util;

public class CompDRL extends ExecUnit implements Compile, Negative {

    private final String       classID = "CompDRL";

    public static final String COMMAND = "tocompile";

    protected int calcExecStatus(int[] statuses) {
        return statuses[statuses.length - 1];
    }

    protected String decode(String name) {
        String tmp = decodePluginProperty(name, "CompileNegative");
        if (tmp != null) {
            return tmp;
        }
        return super.decode(name);
    }

    public String[] parseParamOther(String[] args) {
        int dirOptPos = -1;
        int cmdOptPos = -1;
        int cpOptPos = -1;
        int cnt = 0;
        String[] tmpStore;
        String[] dirOpt = new String[0];
        String[] cpInfo = new String[0];
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if (COMMAND.equalsIgnoreCase(args[i])) {
                cmdOptPos = i;
            } else if ("-d".equalsIgnoreCase(args[i])) {
                dirOptPos = i;
            } else if ("-classpath".equalsIgnoreCase(args[i])) {
                cpOptPos = i;
            }
        }
        if (cmdOptPos < 0) {
            return null;
        }
        if (dirOptPos < 0) {
            dirOpt = new String[2];
            dirOpt[0] = "-d";
            dirOpt[1] = cfg.getTempStorage();
        }
        if (cpOptPos < 0) {
            cpInfo = new String[2];
            cpInfo[0] = "-classpath";
            cpInfo[1] = getCPOptions();
        }
        tmpStore = new String[args.length + dirOpt.length + cpInfo.length];
        tmpStore[cnt++] = cfg.getTestedCompile();
        for (int i = 0; i < dirOpt.length; i++) {
            tmpStore[cnt++] = dirOpt[i];
        }
        for (int i = 0; i < cpInfo.length; i++) {
            tmpStore[cnt++] = cpInfo[i];
        }
        for (int i = 0; i < cmdOptPos; i++) {
            tmpStore[cnt++] = args[i];
        }
        for (int i = (cmdOptPos + 1); i < args.length; i++) {
            tmpStore[cnt++] = args[i];
        }
        return subsEnvToValue(tmpStore);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#runOther(java.lang.String[])
     */
    public int runOther(String[] args) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunOther(): ";
        int execStatus = cfg.getRepError()[0];
        String[] cmdarray;
        try {
            cmdarray = parseParamOther(args);
            if (cmdarray == null) {
                result.setOutMsg(methodLogPrefix
                    + "unrecognized command to run (null)");
                return cfg.getRepModErr()[0];
            }
            result.setExecCmd(rName, cmdarray);
            addSysInfo("Before test run: ", result);
            long startTime = Util.getCurrentTime();
            execStatus = runProc(cmdarray);
            result.setExecTime(Util.getCurrentTime() - startTime);
            addSysInfo("After test run: ", result);
            result.setRealExecStat(execStatus);
            if (execStatus == 0) {
                return cfg.getRepFailed()[0];// negative test failed
            } else {
                int[] retVal = cfg.getRepError();
                for (int i = 0; i < retVal.length; i++) {
                    if (execStatus == retVal[i]) {
                        return execStatus;
                    }
                }
                retVal = cfg.getRepFailed();
                for (int i = 0; i < retVal.length; i++) {
                    if (execStatus == retVal[i]) {
                        return execStatus;
                    }
                }
                return cfg.getRepPassed()[0];//compiler failed, test passed
            }
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        }
        return execStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#runSame(java.lang.String, java.lang.String[])
     */
    public int runSame(String[] args) {
        return cfg.getRepModErr()[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#parseParamSame(java.lang.String[])
     */
    public String[] parseParamSame(String[] args) {
        return null;
    }
}
