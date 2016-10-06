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
 * @version $Revision: 1.3 $
 */
package org.apache.harmony.harness.plugins;

import java.lang.reflect.Method;

import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.TestIR;

public class RunTestThread extends Thread {

    private String  className;
    private TestIR  curTest;
    private TResIR  curRes;
    private SyncObj synObj;

    public void run() {
        if (synObj != null) {
            synchronized (synObj) {
                synObj.status = SyncObj.WORK;
                synObj.notifyAll();
            }
            try {
                Class cl = Class.forName(className);
                Class[] param = new Class[1];
                param[0] = TestIR.class;
                Method m = cl.getMethod("execTest", param);
                Method sm = cl.getMethod("setCurTest", param);
                Object[] p = new Object[1];
                p[0] = curTest;
                Object wInstance = cl.newInstance();
                sm.invoke(wInstance, p);
                curRes = (TResIR)m.invoke(wInstance, p);
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (synObj) {
                synObj.status = SyncObj.FINISH;
                synObj.notifyAll();
            }
        }
    }

    /*
     * return result by request
     */
    public TResIR getResult() {
        synchronized (synObj) {
            return curRes;
        }
    }

    /*
     * set up the test to run
     */
    public RunTestThread(String clas, TestIR test, SyncObj synchronizedObj,
        ThreadGroup tgrp) {
        super(tgrp, test.getTestID());
        className = clas;
        curRes = null;
        synObj = synchronizedObj;
        curTest = test;
    }
}