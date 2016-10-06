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

import java.util.ArrayList;

/**
 * List of test for execution
 */
public class ExecListIR {

    private ArrayList eList = new ArrayList();

    /**
     * Add test to execution list
     * 
     * @param test to add
     */
    public void add(TestIR test) {
        synchronized (eList) {
            eList.add(test);
        }
    }

    /**
     * Get next test from execution list
     * 
     * @return first test from list
     */
    public TestIR get() {
        synchronized (eList) {
            if (eList.size() > 0) {
                return (TestIR)eList.get(0);
            } else {
                return null;
            }
        }
    }

    /**
     * Remove first test from execution list
     * 
     * @return first test from list
     */
    public TestIR remove() {
        synchronized (eList) {
            if (eList.size() > 0) {
                return (TestIR)eList.remove(0);
            } else {
                return null;
            }
        }
    }

    /**
     * Return the current size of execution list (number of tests for execution)
     * 
     * @return current number of tests for execution
     */
    public int size() {
        synchronized (eList) {
            return eList.size();
        }
    }
}