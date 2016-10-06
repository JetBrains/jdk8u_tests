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

public class ExecStatusIR {

    private HashMap execStatusMap = new HashMap(1024);

    public int getStatus(String testID) {
        Object tmpStore;
        synchronized (execStatusMap) {
            tmpStore = execStatusMap.get(testID);
        }
        if (tmpStore != null) {
            return ((Integer)tmpStore).intValue();
        }
        return -1;
    }

    public int setStatus(String testID, int value) {
        Object tmpStore;
        synchronized (execStatusMap) {
            tmpStore = execStatusMap.put(testID, new Integer(value));
        }
        if (tmpStore != null) {
            return ((Integer)tmpStore).intValue();
        }
        return -1;
    }
}