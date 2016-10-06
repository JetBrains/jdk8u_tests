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
 * @version $Revision: 1.13 $
 */
package org.apache.harmony.harness.plugins;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Positive;
import org.apache.harmony.harness.Runtime;
import org.apache.harmony.harness.Util;
import org.apache.harmony.share.DRLLogging;

public class RunDRL extends ExecUnit implements Runtime, Positive {

    private static final String classID    = "RunDRL";

    public static final String  METHODNAME = "test";
    public static final String  COMMAND    = "torun";

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#decode(java.lang.String)
     */
    protected String decode(String name) {
        String tmp = decodePluginProperty(name, "Runtime");
        if (tmp != null) {
            return tmp;
        }
        return super.decode(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#parseParamOther(java.lang.String[])
     */
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
                    + "unrecognized command to run");
                return cfg.getRepModErr()[0];
            }
            result.setExecCmd(rName, cmdarray);
            addSysInfo("Before test run: ", result);
            long startTime = Util.getCurrentTime();
            execStatus = runProc(cmdarray);
            result.setExecTime(Util.getCurrentTime() - startTime);
            addSysInfo("After test run: ", result);
            result.setRealExecStat(execStatus);
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while execute test in the same mode: " + e);
            result.setOutMsg(e);
        }
        return execStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#parseParamSame(java.lang.String[])
     */
    public String[] parseParamSame(String[] args) {
        int cmdOptPos = -1;
        int logOptPos = -1;
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
        fillParam(args, cmdOptPos);
        tmpStore = new String[testParam.size()];
        for (int i = 0; i < testParam.size(); i++) {
            tmpStore[cnt++] = (String)testParam.get(i);
        }
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
        String[] testArgs;
        String[] cmdarray;
        Class cl;
        Class[] paramTypes;
        Method m;
        byte[] outMsg;
        Object[] p;

        try {
            cmdarray = parseParamSame(args);
            if (cmdarray == null) {
                result.setOutMsg(methodLogPrefix
                    + "unrecognized command to run");
                return cfg.getRepModErr()[0];
            }
            result.setExecCmd(rName, cmdarray);
            /*
             * check that it's not a recursion from test (for example, if
             * somebody call "Thread.currentThread().run();" from test for
             * current thread. Thread.currentThread().getStackTrace(); it will
             * be available only for 1.5 So use the empty throw to check the
             * stack trace
             */
            try {
                throw new Exception();
            } catch (Exception te) {
                StackTraceElement[] curST = te.getStackTrace();
                String thisClassName = this.getClass().getName();
                for (int i = 1; i < curST.length; i++) { //miss the top (zero)
                    // element - current
                    // call.
                    if (thisClassName.equals(curST[i].getClassName())) {
                        if ("runSame".equals(curST[i].getMethodName())) {
                            result
                                .setOutMsg(methodLogPrefix
                                    + "Prohibited recursion call. Please check you test for incorrect call like 'Thread.currentThread().run()' or something like that");
                            return cfg.getRepError()[0];
                        }
                    }
                }
            }
            cl = Class.forName(cmdarray[0]);
            try {
                paramTypes = new Class[2];
                paramTypes[0] = String[].class;
                paramTypes[1] = DRLLogging.class;
                m = cl.getMethod(METHODNAME, paramTypes);
                testArgs = new String[cmdarray.length - 1];
                for (int i = 0; i < testArgs.length; i++) {
                    testArgs[i] = cmdarray[i + 1];
                }
                p = new Object[2];
                p[0] = testArgs;
                p[1] = (DRLLogging)sameModeLog;
            } catch (NoSuchMethodException nme) {
                paramTypes = new Class[1];
                paramTypes[0] = String[].class;
                m = cl.getMethod(METHODNAME, paramTypes);
                testArgs = new String[cmdarray.length - 3]; //drop out the
                // parameters for Logger and class name
                for (int i = 0; i < testArgs.length; i++) {
                    testArgs[i] = cmdarray[i + 1];
                }
                p = new Object[1];
                p[0] = testArgs;
            }
            addSysInfo("Before test run: ", result);
            long startTime = Util.getCurrentTime();
            execStatus = ((Integer)m.invoke(cl.newInstance(), p)).intValue();
            result.setExecTime(Util.getCurrentTime() - startTime);
            addSysInfo("After test run: ", result);
            result.setRealExecStat(execStatus);
            prevExitCode = execStatus;
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
        } catch (InvocationTargetException e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e
                + "\nthe cause is " + e.getTargetException());
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        }
        return cfg.getRepError()[0];
    }
}
