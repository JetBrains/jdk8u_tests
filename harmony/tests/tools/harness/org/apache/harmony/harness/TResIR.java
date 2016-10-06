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
 * @version $Revision: 1.22 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TResIR implements Serializable, Cloneable {

    private String    testedOS;
    private String    testedPlatform;
    private String    testedVM;
    private String    date;
    private String    testID;
    private String    repFile;
    private int       execStat;

    /* test execution time in millisecond */
    private ArrayList execTime         = new ArrayList();
    private ArrayList outMsg           = new ArrayList();
    private ArrayList execCmd          = new ArrayList();
    private ArrayList testSpecificInfo = new ArrayList();
    private HashMap   resMap           = new HashMap(16);
    //in some cases the execution status of the process and test are different
    //so this field hold the process exit code and execStat hold the test exit
    //code for example, the cases are external program that can return 0/1 for
    //Execution model
    private ArrayList realExecStats    = new ArrayList();

    public TResIR(String test) {
        testedOS = "Unspecified";
        testedPlatform = "Unspecified";
        testedVM = "Unspecified";
        testID = setTestID(test);
        date = setDate(null);
    }

    public TResIR(String os, String platform, String vm, String test) {
        testedOS = setTestedOS(os);
        testedPlatform = setTestedPlatform(platform);
        testedVM = setTestedVM(vm);
        testID = setTestID(test);
        date = setDate(null);
    }

    public Object clone() {
        TResIR retVal = new TResIR(testedOS, testedPlatform, testedVM, testID);
        retVal.date = this.date;
        retVal.repFile = this.repFile;
        retVal.execStat = this.execStat;
        retVal.realExecStats = (ArrayList)this.realExecStats.clone();
        retVal.outMsg = (ArrayList)this.outMsg.clone();
        retVal.execCmd = (ArrayList)this.execCmd.clone();
        retVal.testSpecificInfo = (ArrayList)this.testSpecificInfo.clone();
        retVal.resMap = (HashMap)this.resMap.clone();
        retVal.execTime = (ArrayList)this.execTime.clone();
        return retVal;
    }

    public void clear() {
        testedOS = null;
        testedPlatform = null;
        testedVM = null;
        date = null;
        clearTestInfo();
    }

    public void clearTestInfo() {
        testID = null;
        repFile = null;
        execStat = 0;
        realExecStats.clear();
        outMsg.clear();
        execCmd.clear();
        testSpecificInfo.clear();
        execTime.clear();
        clearProperty();
    }

    public String getTestedOS() {
        return testedOS;
    }

    public String setTestedOS(String os) {
        if (os == null) {
            testedOS = "Unspecified";
        } else {
            testedOS = os;
        }
        return testedOS;
    }

    public String getTestedPlatform() {
        return testedPlatform;
    }

    public String setTestedPlatform(String platform) {
        if (platform == null) {
            testedPlatform = "Unspecified";
        } else {
            testedPlatform = platform;
        }
        return testedPlatform;
    }

    public String getTestedVM() {
        return testedVM;
    }

    public String setTestedVM(String vm) {
        if (vm == null) {
            testedVM = "Unspecified";
        } else {
            testedVM = vm;
        }
        return testedVM;
    }

    public String getDate() {
        return date;
    }

    public String setDate(String value) {
        if (value == null) {
            date = new Date().toString();
        } else {
            date = value;
        }
        return date;
    }

    public String getTestID() {
        return testID;
    }

    public String setTestID(String id) {
        if (id == null) {
            testID = "Unspecified";
        } else {
            testID = id;
        }
        return testID;
    }

    public String getOutMsgAsString(String delim) {
        String tmpVal = "";
        if (delim == null) {
            delim = "\n";
        }
        for (int i = 0; i < outMsg.size(); i++) {
            tmpVal = tmpVal + outMsg.get(i) + delim;
        }
        return tmpVal;
    }

    public String[] getOutMsg() {
        String[] tmpVal = new String[outMsg.size()];
        for (int i = 0; i < tmpVal.length; i++) {
            tmpVal[i] = (String)outMsg.get(i);
        }
        return tmpVal;
    }

    public String setOutMsg(String msg) {
        if (msg == null) {
            outMsg.clear();
        } else {
            outMsg.add(msg);
        }
        return msg;
    }

    public String setOutMsg(String[] msg) {
        if (msg == null) {
            outMsg.clear();
        } else {
            if (msg.length == 0) {
                return "";
            }
            for (int i = 0; i < msg.length; i++) {
                outMsg.add(msg[i]);
            }
        }
        return msg[0];
    }

    public String setOutMsg(Throwable t) {
        if (t == null) {
            outMsg.clear();
        } else {
            StackTraceElement[] ste = t.getStackTrace();
            for (int i = 0; i < ste.length; i++) {
                outMsg.add(ste[i].toString());
            }
            return ste[0].toString();
        }
        return "";
    }

    public String getTestSpecificInfoAsString(String delim) {
        String tmpVal = "";
        if (delim == null) {
            delim = "\n";
        }
        for (int i = 0; i < testSpecificInfo.size(); i++) {
            tmpVal = tmpVal + testSpecificInfo.get(i) + delim;
        }
        return tmpVal;
    }

    public String[] getTestSpecificInfo() {
        String[] tmpVal = new String[testSpecificInfo.size()];
        for (int i = 0; i < tmpVal.length; i++) {
            tmpVal[i] = (String)testSpecificInfo.get(i);
        }
        return tmpVal;
    }

    public String setTestSpecificInfo(String msg) {
        if (msg == null) {
            testSpecificInfo.clear();
        } else {
            testSpecificInfo.add(msg);
        }
        return msg;
    }

    public String setTestSpecificInfo(String[] msg) {
        if (msg == null) {
            testSpecificInfo.clear();
        } else {
            if (msg.length == 0) {
                return "";
            }
            for (int i = 0; i < msg.length; i++) {
                testSpecificInfo.add(msg[i]);
            }
        }
        return msg[0];
    }

    public String getRepFile() {
        if (repFile != null) {
            return repFile;
        }
        return testID;
    }

    public String setRepFile(String value) {
        if (value == null) {
            if (testID != null && testID.length() > 0) {
                repFile = System.getProperty("user.dir") + File.separator
                    + testID;
            } else {
                repFile = System.getProperty("user.dir") + File.separator
                    + "testRes";
            }
        } else {
            repFile = value;
        }
        return repFile;
    }

    public String getExecCmdAsString(String delim) {
        String tmpVal = "";
        if (delim == null) {
            delim = "\n";
        }
        for (int i = 0; i < execCmd.size(); i++) {
            tmpVal = tmpVal + execCmd.get(i) + delim;
        }
        return tmpVal;
    }

    public String[] getExecCmd() {
        String[] tmpVal = new String[execCmd.size()];
        for (int i = 0; i < tmpVal.length; i++) {
            tmpVal[i] = (String)execCmd.get(i);
        }
        return tmpVal;
    }

    public void setExecCmd(String cmdLine) {
        if (cmdLine == null) {
            execCmd.clear();
        } else {
            execCmd.add(cmdLine);
        }
    }

    public String setExecCmd(String runner, String[] msg) {
        if (msg == null) {
            execCmd.clear();
            return null;
        } else {
            execCmd.add(runner);
            for (int i = 0; i < msg.length; i++) {
                execCmd.add(msg[i]);
            }
        }
        return runner;
    }

    public String setExecCmd(String[] msg) {
        if (msg == null) {
            execCmd.clear();
            return null;
        } else {
            if (msg.length == 0) {
                return "";
            }
            for (int i = 0; i < msg.length; i++) {
                execCmd.add(msg[i]);
            }
        }
        return msg[0];
    }

    public String setExecCmd(String runner, ArrayList msg) {
        if (msg == null) {
            execCmd.clear();
            return null;
        } else {
            execCmd.add(runner);
            for (int i = 0; i < msg.size(); i++) {
                execCmd.add(msg.get(i));
            }
        }
        return runner;
    }

    public String setExecCmd(ArrayList msg) {
        if (msg == null) {
            execCmd.clear();
            return null;
        } else {
            for (int i = 0; i < msg.size(); i++) {
                execCmd.add(msg.get(i));
            }
        }
        return (String)msg.get(0);
    }

    public int getExecStat() {
        return execStat;
    }

    public int setExecStat(int stat) {
        execStat = stat;
        return execStat;
    }

    public ArrayList getExecTime() {
        return execTime;
    }

    public long setExecTime(long time) {
        if (time < 0) {
            time = 0;
        }
        execTime.add(new Long(time));
        return time;
    }

    public void setExecTime(ArrayList arTime) {
        if (arTime == null) {
            execTime.clear();
        }
        for (int i = 0; i < arTime.size(); i++) {
            execTime.add(arTime.get(i));
        }
    }

    public void clearExecTime() {
        execTime.clear();
    }

    public ArrayList getRealExecStats() {
        return (ArrayList)realExecStats.clone();
    }

    public void setRealExecStat(int stat) {
        realExecStats.add(new Integer(stat));
    }

    public void setRealExecStat(Object obj) {
        realExecStats.add(obj);
    }

    public void clearRealExecStat() {
        realExecStats.clear();
    }

    public Object getProperty(String key) {
        if (key == null) {
            return null;
        }
        synchronized (resMap) {
            return resMap.get(key);
        }
    }

    public Object setProperty(String key, String value) {
        if (key == null) {
            return null;
        }
        synchronized (resMap) {
            return resMap.put(key, value);
        }
    }

    public Object setProperty(String key, Object value) {
        if (key == null) {
            return null;
        }
        synchronized (resMap) {
            return resMap.put(key, value);
        }
    }

    public void clearProperty() {
        synchronized (resMap) {
            resMap.clear();
        }
    }

    public HashMap getAllProperty() {
        synchronized (resMap) {
            return (HashMap)resMap.clone();
        }
    }

    public void setAllProperty(HashMap property) {
        if (property != null) {
            synchronized (resMap) {
                resMap = (HashMap)property.clone();
            }
        }
    }
}