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
package org.apache.harmony.harness.plugins;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.TestIR;

/**
 * This class is an extension of TestFinder that tries to run the JUFinder also.
 * If the JUFinder find the test with the exist ID this test is ignored
 */
public class MTFinder extends TestFinder {

    protected HashMap totalParsedItems = new HashMap(1024);
    protected List    totalParsedTList = new LinkedList();

    public int find(String subsuite, String[] mask)
        throws ConfigurationException {
        super.tsRoot = tsRoot;
        super.find(subsuite, mask);
        mergeTests(parsedItems);
        DirFinder juf = new JUFinder();
        juf.find(subsuite, mask);
        mergeTests(juf.getTestList());
        return totalParsedItems.size();
    }

    public Object getNext() {
        synchronized (totalParsedTList) {
            if (totalParsedTList.isEmpty()) {
                return null;
            }
            return totalParsedTList.remove(0);
        }
    }

    protected void mergeTests(List addList) {
        for (int i = 0; i < addList.size(); i++) {
            try {
                TestIR test = (TestIR)addList.get(i);
                if (!totalParsedItems.containsKey(test.getTestID())) {
                    totalParsedItems.put(test.getTestID(), test);
                    totalParsedTList.add(test);
                }
            } catch (ClassCastException e) {
                log.add(Level.FINE,
                    "testHarness\tMTFinder. Unexpected object into test list "
                        + addList.get(i));
            }
        }
    }
}