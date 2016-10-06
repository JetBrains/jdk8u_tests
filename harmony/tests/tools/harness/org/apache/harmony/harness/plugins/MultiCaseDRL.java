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

import java.util.ArrayList;
import java.util.HashMap;

public class MultiCaseDRL extends RunDRL {

    HashMap tcToExclude = cfg.getExcludeTestCasesList();

    private String[] calcExcludeOpt() {
        String[] tmpStore = new String[0];
        if (tcToExclude == null) {
            return tmpStore;
        }
        if (tcToExclude.containsKey(curTest.getTestID())) {
            ArrayList opt = (ArrayList)tcToExclude.get(curTest.getTestID());
            tmpStore = new String[2];
            tmpStore[0] = "-exclude";
            tmpStore[1] = "";
            for (int i = 0; i < opt.size(); i++) {
                tmpStore[1] = tmpStore[1] + " " + (String)opt.get(i);
            }
        }
        return tmpStore;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.ExecUnit#decode(java.lang.String)
     */
    protected String decode(String name) {
        String tmp = decodePluginProperty(name, "MultiCase");
        if (tmp != null) {
            return tmp;
        }
        return super.decode(name);
    }

    String[] calcMCConfigOptions() {
        String[] retValue = new String[0];
        ArrayList addOptions = new ArrayList();
        HashMap hm = (HashMap)((HashMap)((ArrayList)cfg
            .getPluginProperties("ExecUnit")).get(1)).get("MultiCase");
        Object obj1 = hm.get("createNewInstance");

        ArrayList obj = (ArrayList)hm.get("createNewInstance");
        if (obj != null && ((String)obj.get(0)).equalsIgnoreCase("true")) {
            addOptions.add("-createnewinstance");
        }
        obj = (ArrayList)hm.get("reportZeroTCasError");
        if (obj != null && ((String)obj.get(0)).equalsIgnoreCase("true")) {
            addOptions.add("-zerotcerr");
        }
        retValue = new String[addOptions.size()];
        for (int i = 0; i < retValue.length; i++) {
            retValue[i] = (String)addOptions.get(i);
        }
        return retValue;
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
        String[] excludeOpt;
        String[] cfgOpt;
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
        excludeOpt = calcExcludeOpt();
        cfgOpt = calcMCConfigOptions();

        tmpStore = new String[1/* java */+ testVMParam.size()
            + testParam.size() + excludeOpt.length + cfgOpt.length];
        tmpStore[cnt++] = cfg.getTestedRuntime();
        for (int i = 0; i < testVMParam.size(); i++) {
            tmpStore[cnt++] = (String)testVMParam.get(i);
        }
        for (int i = 0; i < testParam.size(); i++) {
            tmpStore[cnt++] = (String)testParam.get(i);
        }
        for (int i = 0; i < excludeOpt.length; i++) {
            tmpStore[cnt++] = excludeOpt[i];
        }
        for (int i = 0; i < cfgOpt.length; i++) {
            tmpStore[cnt++] = cfgOpt[i];
        }
        return subsEnvToValue(tmpStore);
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
        String[] excludeOpt;
        String[] cfgOpt;
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
        excludeOpt = calcExcludeOpt();
        cfgOpt = calcMCConfigOptions();
        tmpStore = new String[testParam.size() + excludeOpt.length
            + cfgOpt.length];
        for (int i = 0; i < testParam.size(); i++) {
            tmpStore[cnt++] = (String)testParam.get(i);
        }
        for (int i = 0; i < excludeOpt.length; i++) {
            tmpStore[cnt++] = excludeOpt[i];
        }
        for (int i = 0; i < cfgOpt.length; i++) {
            tmpStore[cnt++] = cfgOpt[i];
        }
        return subsEnvToValue(tmpStore);
    }
}