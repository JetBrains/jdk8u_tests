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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Util;

public class JUExecDRL extends RunDRL {

    private final String       classID    = "JUExecDRL";

    public static final String COMMAND    = "junit";
    public static final String METHODNAME = "start";

    public String[] parseParamOther(String[] args) {
        int cmdOptPos = -1;
        int cnt = 0;
        int index;
        String[] tmpStore;
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if (COMMAND.equalsIgnoreCase(args[i])) {
                cmdOptPos = i;
                break;
            }
        }
        if (cmdOptPos < 0) {
            return null;
        }
        fillParam(args, cmdOptPos);
        tmpStore = new String[1/* java */+ testVMParam.size() + 1 /* start class */
        + 1 /* test class */];
        tmpStore[cnt++] = cfg.getTestedRuntime();
        for (int i = 0; i < testVMParam.size(); i++) {
            tmpStore[cnt++] = (String)testVMParam.get(i);
        }
        tmpStore[cnt++] = "junit.textui.TestRunner";
        tmpStore[cnt++] = (String)testParam.get(0);
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
                return execStatus;
            }
            result.setExecCmd(rName, cmdarray);
            long startTime = Util.getCurrentTime();
            execStatus = runProc(cmdarray);
            result.setExecTime(Util.getCurrentTime() - startTime);
            result.setRealExecStat(execStatus);
            if (execStatus == 0) {
                execStatus = cfg.getRepPassed()[0];
            } else if (execStatus == 1) {
                execStatus = cfg.getRepFailed()[0];
            }
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        }
        return execStatus;
    }

    public String[] parseParamSame(String[] args) {
        int cmdOptPos = -1;
        int cnt = 0;
        String[] tmpStore;
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if (COMMAND.equalsIgnoreCase(args[i])) {
                cmdOptPos = i;
                break;
            }
        }
        if (cmdOptPos < 0) {
            return null;
        }
        tmpStore = new String[2];
        tmpStore[cnt++] = "junit.textui.TestRunner";
        tmpStore[cnt++] = args[cmdOptPos + 1];
        return subsEnvToValue(tmpStore);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#runSame(java.lang.String, java.lang.String[])
     */
    public int runSame(String[] args) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunSame(): ";
        int execStatus = 0;
        String[] cmdarray;
        Class cl;
        Class[] paramTypes;
        Method m;
        byte[] outMsg;
        Object[] p;

        try {
            cmdarray = parseParamSame(args);
            if (cmdarray == null && cmdarray.length < 2) {
                result.setOutMsg(methodLogPrefix
                    + "unrecognized command to run");
                return cfg.getRepModErr()[0];
            }
            result.setExecCmd(rName, cmdarray);
            cl = Class.forName(cmdarray[0]);
            try {
                paramTypes = new Class[1];
                paramTypes[0] = String[].class;
                m = cl.getDeclaredMethod(METHODNAME, paramTypes);
                m.setAccessible(true);
                p = new Object[1];
                String[] argg = new String[1];
                argg[0] = cmdarray[1];
                p[0] = argg;
            } catch (NoSuchMethodException nme) {
                result.setOutMsg(methodLogPrefix + "no method " + METHODNAME
                    + " in the " + cl.getClass().getName()
                    + ", unexpected exception " + nme);
                return cfg.getRepModErr()[0];
            }
            long startTime = Util.getCurrentTime();
            try {
                Object res = m.invoke(cl.newInstance(), p);
                result.setExecTime(Util.getCurrentTime() - startTime);
                int runCnt = ((Integer)(res.getClass().getMethod("runCount",
                    null).invoke(res, null))).intValue();
                int failCnt = ((Integer)(res.getClass().getMethod(
                    "failureCount", null).invoke(res, null))).intValue();
                int errCnt = ((Integer)(res.getClass().getMethod("errorCount",
                    null).invoke(res, null))).intValue();
                if (runCnt != 0 && failCnt == 0 && errCnt == 0) {
                    execStatus = cfg.getRepPassed()[0];
                } else {
                    execStatus = cfg.getRepFailed()[0];
                }
            } catch (InvocationTargetException e) {
                result.setExecTime(Util.getCurrentTime() - startTime);
                result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                    + e + "\nthe cause is " + e.getTargetException());
                execStatus = cfg.getRepFailed()[0];
            } catch (Exception e) {
                result.setExecTime(Util.getCurrentTime() - startTime);
                result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                    + e);
                execStatus = cfg.getRepFailed()[0];
            }
            outMsg = sameModeLog.read();
            if (outMsg != null) {
                try {
                    result.setOutMsg(new String(outMsg, "UTF-8"));
                } catch (UnsupportedEncodingException ue) {
                    result.setOutMsg(methodLogPrefix
                        + "Test output is not UTF-8 compatible");
                    result.setOutMsg(new String(outMsg));
                }
            }
            return execStatus;
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        }
        return cfg.getRepError()[0];
    }
}
