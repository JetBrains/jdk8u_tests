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
 * @version $Revision: 1.4 $
 */
package org.apache.harmony.harness.plugins;

import java.util.ArrayList;

import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Positive;
import org.apache.harmony.harness.Runtime;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.TestIR;
import org.apache.harmony.share.Result;

//execution unit to run all tests from one group in different threads
public class RunGroupInThreads extends ExecUnit implements Runtime, Positive {

    private final static String classID = "RunGroupInThreads";

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#runOther(java.lang.String[])
     */
    public int runOther(String[] args) {
        return Result.ERROR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#parseParamOther(java.lang.String[])
     */
    public String[] parseParamOther(String[] args) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#runSame(java.lang.String[])
     */
    public int runSame(String[] args) {
        return Result.ERROR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#parseParamSame(java.lang.String[])
     */
    public String[] parseParamSame(String[] args) {
        return null;
    }

    public TResIR execTest(TestIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\texecTest(): ";
        result.clearTestInfo();
        result.setTestID(test.getTestID());
        result.setRepFile(calcFileName(test));
        result.setAllProperty(test.getAllProperty());
        result.setProperty("genEnv", getGeneralEnv());
        ArrayList tests = test.getRunnerParam();
        SyncObj synchronizedObj = new SyncObj();
        if (tests != null && tests.size() > 0) {
            ThreadGroup testThGroup = new ThreadGroup(test.getTestID());
            RunTestThread[] rt = new RunTestThread[tests.size()];
            TResIR[] groupRes = new TResIR[tests.size()];
            int[] exitStatuses = new int[tests.size()];
            //group timeout calculated by group test finder
            float groupTimeFactor = test.getTestTimeout();
            //create group
            for (int i = 0; i < tests.size(); i++) {
                TestIR curT = (TestIR)tests.get(i);
                //note, for group runner it is a full class name
                String clas = curT.getRunnerID();
                rt[i] = new RunTestThread(clas, curT, synchronizedObj,
                    testThGroup);
            }
            try {
                //start group and ...
                for (int i = 0; i < rt.length; i++) {
                    rt[i].start();
                }
                //... and wait for execution finish
                calcTimeout(groupTimeFactor);
                int waitfor = 0;
                for (int cnt = 0; cnt < curTimeout; cnt++) {
                    rt[waitfor].join(100);
                    if (!rt[waitfor].isAlive()) {
                        waitfor++;
                    }
                    if (waitfor >= rt.length) {
                        break;
                    }
                }
            } catch (Exception e) {
                result.setExecStat(Result.ERROR);
                result.setOutMsg(MessageInfo.MSG_PREFIX + classID
                    + "\tUnexpected exception: " + e);
            }
            //collect results
            //if some result missed - fill it by Error status
            for (int i = 0; i < rt.length; i++) {
                TResIR res = rt[i].getResult();
                if (res == null) {
                    res = (TResIR)result.clone();
                    res.setTestID(((TestIR)tests.get(i)).getTestID());
                    res.clearRealExecStat();
                    res.setExecStat(Result.ERROR);
                    res.setOutMsg(MessageInfo.MSG_PREFIX + classID
                        + "\tNo result for test: " + res.getTestID());
                }
                result.setRealExecStat(res);
                exitStatuses[i] = res.getExecStat();
                if (rt[i].isAlive()) {
                    rt[i].interrupt();
                    try {
                        rt[i].join(100);
                    } catch (InterruptedException e1) {
                        // do nothing
                    }
                    rt[i].destroy();
                }

            }
            result.setExecStat(calcExecStatus(exitStatuses));
        } else {
            result.setExecStat(Result.ERROR);
            result.setOutMsg(MessageInfo.MSG_PREFIX + classID
                + "\tNo tests to run.");
        }
        return result;
    }
}