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
 * @version $Revision: 1.19 $
 */
package org.apache.harmony.harness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TestIR implements Serializable {

    private final String classID     = "TestIR";

    private HashMap      propertyMap = new HashMap(16);

    /** The runner's ID */
    private String       runnerID;

    /** The runner's parameters */
    private ArrayList    runnerParam = new ArrayList();

    /** The test's ID */
    private String       testID;

    /**
     * The test's timeout. If this value is not specified the GenTimeout from
     * configuration is used
     */
    private float        testTimeoutFactor;

    /** The test's keywords. */
    private String[]     keywords;

    /** The test's resources. */
    private String[]     resources;

    /**
     * Restriction: if this flag set to 'true' the test can be run in the same
     * VM mode only
     */
    private boolean      sameVMOnly;

    /**
     * Restriction: if this flag set to 'true' the test can be run in the other
     * VM mode only
     */
    private boolean      otherVMOnly;

    /**
     * Restriction: if this flag set to 'true' the test can be run in the same
     * VM mode one time only (this test use the static context, for example)
     */
    private boolean      sameVMOne;

    /**
     * Create the empty TestIR object
     */
    public TestIR() {
        //do nothing
    }

    /**
     * Create the TestIR object with specified testID
     * 
     * @param test the TestID
     */
    public TestIR(String test) {
        this("Runtime", null, test, 0, null, null, false, false, false);
    }

    /**
     * Create the TestIR object with specified testID and RunnerID
     * 
     * @param test the TestID
     */
    public TestIR(String runner, String test) {
        this(runner, null, test, 0, null, null, false, false, false);
    }

    public TestIR(String runer, ArrayList rParam, String test, float time,
        String[] keywords, String[] resources, boolean same, boolean other,
        boolean sameOne) {
        String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tTestIR(): ";
        if (runer == null || test == null) {
            throw new NullPointerException(methodLogPrefix
                + "Incorrect null value for constructor parameters");
        }
        runnerID = runer;
        if (rParam == null) {
            runnerParam = new ArrayList();
        } else {
            runnerParam = rParam;
        }
        testID = test;
        if (keywords == null) {
            keywords = new String[0];
        } else {
            this.keywords = keywords;
        }
        if (resources == null) {
            resources = new String[0];
        } else {
            this.resources = resources;
        }
        sameVMOnly = same;
        otherVMOnly = other;
        sameVMOne = sameOne;
        if (time == 0) {
            testTimeoutFactor = 1;
        } else {
            testTimeoutFactor = time;
        }
    }

    public void clear() {
        propertyMap.clear();
        runnerID = null;
        runnerParam.clear();
        testID = null;
        testTimeoutFactor = 1;
        keywords = null;
        resources = null;
        sameVMOnly = false;
        otherVMOnly = false;
        sameVMOne = false;
    }

    public Object clone() {
        TestIR retVal = new TestIR(this.runnerID, (ArrayList)this.runnerParam
            .clone(), this.testID, this.testTimeoutFactor,
            (String[])this.keywords.clone(), (String[])this.resources.clone(),
            this.sameVMOnly, this.otherVMOnly, this.sameVMOne);
        retVal.setAllProperty(this.getAllProperty());
        return retVal;
    }

    public String getRunnerID() {
        return runnerID;
    }

    public String setRunnerID(String value) {
        if (value == null) {
            value = "Unspecified";
        }
        String tmpStore = runnerID;
        runnerID = value;
        return tmpStore;
    }

    public ArrayList getRunnerParam() {
        return runnerParam;
    }

    public ArrayList setRunnerParam(ArrayList value) {
        String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetRunnerParam(): ";
        if (value == null) {
            throw new NullPointerException(
                methodLogPrefix
                    + "Unexpected null value for array of execution units parameters");
        }
        ArrayList tmpStore = runnerParam;
        runnerParam = (ArrayList)value.clone();
        return tmpStore;
    }

    public void clearRunnerParam() {
        runnerParam.clear();
    }

    public ArrayList getModifications() {
        return (ArrayList)propertyMap.get("modificationDate");
    }

    public ArrayList setModifications(ArrayList value) {
        String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetModifications(): ";
        if (value == null) {
            throw new NullPointerException(methodLogPrefix
                + "Unexpected null value for modifications date");
        }
        ArrayList tmpStore = (ArrayList)propertyMap.put("modificationDate",
            value);
        return tmpStore;
    }

    public ArrayList getAuthors() {
        return (ArrayList)propertyMap.get("authors");
    }

    public ArrayList setAuthors(ArrayList value) {
        String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetAuthors(): ";
        if (value == null) {
            throw new NullPointerException(methodLogPrefix
                + "Unexpected null value for authors");
        }
        ArrayList tmpStore = (ArrayList)propertyMap.put("authors", value);
        return tmpStore;
    }

    public String getTestID() {
        return testID;
    }

    public String setTestID(String value) {
        if (value == null) {
            value = "Unspecified";
        }
        String tmpStore = testID;
        testID = value;
        return tmpStore;
    }

    public float getTestTimeout() {
        return testTimeoutFactor;
    }

    public float setTestTimeout(float value) {
        String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetTestTimeout(): ";
        if (value < 0) {
            throw new IllegalArgumentException(methodLogPrefix
                + "Negative value for test timeout");
        }
        float tmpStore = testTimeoutFactor;
        if (value == 0) {
            testTimeoutFactor = Main.getCurCore().getConfigIR().getGenTimeout();
        }
        testTimeoutFactor = value;
        return tmpStore;
    }

    public float setTestTimeout(String value) {
        String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetTestTimeout(String): ";
        if (value == null || !value.startsWith(Constants.PARAM_SEPARATOR)) {
            throw new IllegalArgumentException(methodLogPrefix
                + "Incorrect value for test timeout");
        }
        propertyMap.put("TestTimeoutFactor", value);
        setTestTimeout(1.0F);
        return 1.0F;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String[] setKeywords(String[] value) {
        if (value == null) {
            value = new String[0];
        }
        String[] tmpStore = keywords;
        keywords = value;
        return tmpStore;
    }

    public String[] getResources() {
        return resources;
    }

    public String[] setResources(String[] value) {
        if (value == null) {
            value = new String[0];
        }
        String[] tmpStore = resources;
        resources = value;
        return tmpStore;
    }

    public boolean getSameVMOnly() {
        return sameVMOnly;
    }

    public boolean setSameVMOnly(boolean value) {
        boolean tmpStore = sameVMOnly;
        sameVMOnly = value;
        return tmpStore;
    }

    public boolean getOtherVMOnly() {
        return otherVMOnly;
    }

    public boolean setOtherVMOnly(boolean value) {
        boolean tmpStore = otherVMOnly;
        otherVMOnly = value;
        return tmpStore;
    }

    public boolean getSameVMOne() {
        return sameVMOne;
    }

    public boolean setSameVMOne(boolean value) {
        boolean tmpStore = sameVMOne;
        sameVMOne = value;
        return tmpStore;
    }

    public String getProperty(String key) {
        Object tmpStore;
        synchronized (propertyMap) {
            tmpStore = propertyMap.get(key);
        }
        if (tmpStore != null) {
            return tmpStore.toString();
        }
        return null;
    }

    public String setProperty(String key, String value) {
        Object tmpStore;
        synchronized (propertyMap) {
            tmpStore = propertyMap.put(key, value);
        }
        if (tmpStore != null) {
            return tmpStore.toString();
        }
        return null;
    }

    public Object getProperty(Object key) {
        Object tmpStore;
        synchronized (propertyMap) {
            tmpStore = propertyMap.get(key);
        }
        if (tmpStore != null) {
            return tmpStore;
        }
        return null;
    }

    public Object setProperty(Object key, Object value) {
        Object tmpStore;
        synchronized (propertyMap) {
            tmpStore = propertyMap.put(key, value);
        }
        if (tmpStore != null) {
            return tmpStore;
        }
        return null;
    }

    public HashMap getAllProperty() {
        synchronized (propertyMap) {
            return (HashMap)propertyMap.clone();
        }
    }

    public void setAllProperty(HashMap newVal) {
        if (newVal != null) {
            synchronized (propertyMap) {
                propertyMap = (HashMap)newVal.clone();
            }
        }
    }
}