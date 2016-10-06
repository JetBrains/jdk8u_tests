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
 * @version $Revision: 1.12 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;

import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Negative;
import org.apache.harmony.harness.Runtime;
import org.apache.harmony.harness.Util;

public class ExecDRL extends ExecUnit implements Runtime, Negative {

    private final String        classID      = "ExecDRL";

    public static final String  COMMAND1     = "torun";
    public static final String  COMMAND2     = "demon";
    public static final boolean RUN          = true;
    public static final boolean DEMON        = false;

    private static boolean      modeRun      = RUN;

    protected File              runDirectory = null;
    protected String[]          envp;

    protected int calcExecStatus(int[] statuses) {
        return statuses[statuses.length - 1];
    }

    protected String decode(String name) {
        String tmp = decodePluginProperty(name, "ExecuteNegative");
        if (tmp != null) {
            return tmp;
        }
        return super.decode(name);
    }

    public String[] parseParamOther(String[] args) {
        int cmdOptPos = -1;
        int cnt = 0;
        String[] tmpStore;
        String[] cmd;
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if (COMMAND1.equalsIgnoreCase(args[i])) {
                modeRun = RUN;
                cmdOptPos = i;
                break;
            }
            if (COMMAND2.equalsIgnoreCase(args[i])) {
                modeRun = DEMON;
                cmdOptPos = i;
                break;
            }
        }
        if (cmdOptPos < 0) {
            return null;
        }
        tmpStore = new String[args.length - 1];
        for (int i = 0; i < cmdOptPos; i++) {
            tmpStore[cnt++] = args[i];
        }
        for (int i = (cmdOptPos + 1); i < args.length; i++) {
            tmpStore[cnt++] = args[i];
        }
        cmd = subsEnvToValue(tmpStore);
        runDirectory = getRunDirectory(cmd);
        envp = getEnviroment(cmd);
        return clearServiceCommand(cmd);
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
                return cfg.getRepError()[0];
            }
            result.setExecCmd(rName, cmdarray);
            if (modeRun == RUN) {
                long startTime = Util.getCurrentTime();
                if (runDirectory != null) {
                    execStatus = runProc(cmdarray, envp, runDirectory);
                } else {
                    execStatus = runProc(cmdarray, envp);
                }
                result.setExecTime(Util.getCurrentTime() - startTime);
                result.setRealExecStat(execStatus);
                if (execStatus == 0) {
                    execStatus = cfg.getRepFailed()[0];
                } else {
                    execStatus = cfg.getRepPassed()[0];
                }
            } else {
                long startTime = Util.getCurrentTime();
                if (runDirectory != null) {
                    execStatus = runDemon(cmdarray, envp, runDirectory);
                } else {
                    execStatus = runDemon(cmdarray, envp);
                }
                result.setExecTime(Util.getCurrentTime() - startTime);
            }
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
