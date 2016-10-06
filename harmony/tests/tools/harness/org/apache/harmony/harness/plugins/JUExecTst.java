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
 * @version $Revision: 1.17 $
 */
package org.apache.harmony.harness.plugins;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Util;

public class JUExecTst extends RunDRL {

    private final String       classID = "JUExecTst";

    public static final String COMMAND = "junit";

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
            + testParam.size()];
        tmpStore[cnt++] = cfg.getTestedRuntime();
        for (int i = 0; i < testVMParam.size(); i++) {
            tmpStore[cnt++] = (String)testVMParam.get(i);
        }
        tmpStore[cnt++] = "junit.framework.TestCase";
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
                return execStatus;
            }
            result.setExecCmd(rName, cmdarray);
            long startTime = Util.getCurrentTime();
            execStatus = runProc(cmdarray);
            result.setExecTime(Util.getCurrentTime() - startTime);
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        }
        return execStatus;
    }

    public String[] parseParamSame(String[] args) {
        int cmdOptPos = -1;
        int logOptPos = -1;
        int cnt = 0;
        String[] tmpStore;
        String[] tstLog = new String[0];
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
        for (int i = cmdOptPos; i < args.length; i++) {
            if ("logLevel".equalsIgnoreCase(args[i])) {
                logOptPos = i;
            }
        }
        if (logOptPos < 0) {
            tstLog = logOptions;
        }
        tmpStore = new String[args.length - cmdOptPos - 1 + tstLog.length];
        for (int i = (cmdOptPos + 1); i < args.length; i++) {
            tmpStore[cnt++] = args[i];
        }
        for (int i = 0; i < tstLog.length; i++) {
            tmpStore[cnt++] = tstLog[i];
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
        int execStatus;
        String[] testArgs;
        String[] cmdarray;
        Class cl;
        Class[] paramTypes;
        byte[] outMsg;
        Object classToCall;
        Object[] p = new Object[2];

        try {
            cmdarray = parseParamSame(args);
            if (cmdarray == null) {
                return cfg.getRepError()[0];
            }
            result.setExecCmd(rName, cmdarray);
            testArgs = new String[cmdarray.length - 1];
            cl = Class.forName(cmdarray[0]);
            paramTypes = new Class[2];
            paramTypes[0] = String[].class;
            paramTypes[1] = Logger.class;
            Method m = cl.getMethod(METHODNAME, paramTypes);
            for (int i = 0; i < testArgs.length; i++) {
                testArgs[i] = cmdarray[i + 1];
            }
            p[0] = testArgs;
            p[1] = (Logger)sameModeLog;

            try {
                classToCall = cl.newInstance();
            } catch (Exception e) {
                Class[] prm = new Class[1];
                prm[0] = String.class;
                Constructor ctor = cl.getConstructor(prm);
                Object[] objParam = new Object[1];
                objParam[0] = "";
                classToCall = ctor.newInstance(objParam);
            }
            long startTime = Util.getCurrentTime();
            execStatus = ((Integer)m.invoke(classToCall, p)).intValue();
            result.setExecTime(Util.getCurrentTime() - startTime);
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
