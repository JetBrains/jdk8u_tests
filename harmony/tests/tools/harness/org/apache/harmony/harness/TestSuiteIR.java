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
 * @version $Revision: 1.6 $
 */
package org.apache.harmony.harness;

import java.util.HashMap;

public class TestSuiteIR {

    private HashMap tsMap = new HashMap(1024);

    public TestIR get(String testID) {
        Object tmpStore;
        synchronized (tsMap) {
            tmpStore = tsMap.get(testID);
        }
        if (tmpStore != null) {
            return (TestIR)tmpStore;
        }
        return null;
    }

    public TestIR add(String testID, TestIR value) {
        Object tmpStore;
        synchronized (tsMap) {
            tmpStore = tsMap.put(testID, value);
        }
        if (tmpStore != null) {
            return (TestIR)tmpStore;
        }
        return null;
    }

    public TestIR remove(String testID) {
        return (TestIR)tsMap.remove(testID);
    }

    public void removeAll() {
        tsMap.clear();
    }

    public int size() {
        return tsMap.size();
    }
}