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
 * @version $Revision: 1.11 $
 */
package org.apache.harmony.harness.plugins;

import org.apache.harmony.harness.Compile;
import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Positive;
import org.apache.harmony.harness.Util;

public class CompRunDRL extends ExecUnit implements Compile, Positive {

    private final String       classID  = "CompRunDRL";

    public static final String COMMAND1 = "tocompile";
    public static final String COMMAND2 = "torun";

    private int                compileRes;
    private int                runRes;

    protected String decode(String name) {
        String tmp = decodePluginProperty(name, "CompileRuntime");
        if (tmp != null) {
            return tmp;
        }
        return super.decode(name);
    }

    private String[] parseParamOther(String[] args, String cmd) {
        int dirOptPos = -1;
        int cmdOptPos = -1;
        int cpOptPos = -1;
        int cnt = 0;
        String[] tmpStore;
        String[] dirOpt = new String[0];
        String[] cpInfo = new String[0];
        if (args == null || cmd == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if (cmd.equalsIgnoreCase(args[i])) {
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
        if (COMMAND1.equalsIgnoreCase(cmd)) {
            compileRes = -1;
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
        } else if (COMMAND2.equalsIgnoreCase(cmd)) {
            fillParam(args, cmdOptPos);
            tmpStore = new String[1/* java */+ testVMParam.size()
                + testParam.size()];
            tmpStore[cnt++] = cfg.getReferenceRuntime();
            for (int i = 0; i < testVMParam.size(); i++) {
                tmpStore[cnt++] = (String)testVMParam.get(i);
            }
            for (int i = 0; i < testParam.size(); i++) {
                tmpStore[cnt++] = (String)testParam.get(i);
            }
        } else {
            return null;
        }
        return subsEnvToValue(tmpStore);
    }

    public String[] parseParamOther(String[] args) {
        String[] cmd1array = parseParamOther(args, COMMAND1);
        String[] cmd2array = parseParamOther(args, COMMAND2);
        if (cmd1array == null) {
            return cmd2array;
        } else {
            return cmd1array;
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
        int execStatus = cfg.getRepError()[0];
        String[] cmd1array;
        String[] cmd2array;
        try {
            cmd1array = parseParamOther(args, COMMAND1);
            cmd2array = parseParamOther(args, COMMAND2);
            if (cmd1array != null) {
                result.setExecCmd(rName, cmd1array);
                addSysInfo("Before test run: ", result);
                long startTime = Util.getCurrentTime();
                compileRes = runProc(cmd1array);
                result.setExecTime(Util.getCurrentTime() - startTime);
                addSysInfo("After test run: ", result);
                result.setRealExecStat(execStatus);
                if (compileRes == 0) {
                    execStatus = cfg.getRepPassed()[0];
                }
            } else if (cmd2array != null) {
                result.setExecCmd(rName, cmd2array);
                long startTime = Util.getCurrentTime();
                execStatus = runProc(cmd2array);
                result.setExecTime(Util.getCurrentTime() - startTime);
                result.setRealExecStat(execStatus);
            } else {
                result.setOutMsg(methodLogPrefix
                    + "unrecognized command to run");
                return cfg.getRepModErr()[0];
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
