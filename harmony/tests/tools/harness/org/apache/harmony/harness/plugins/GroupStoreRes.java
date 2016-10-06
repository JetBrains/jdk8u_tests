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

import java.util.ArrayList;

import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.TResIR;

public class GroupStoreRes extends StoreRes {

    private static final String classID      = "GroupStoreRes";

    private String              testResRoot  = cfg.getTestResultRoot();
    private String              resExtension = Main.getCurCore().getStore()
                                                 .getResultExtension();

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Storage#initStorage()
     */
    public boolean init(String name) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Storage#addStorage(TestIR)
     */
    public boolean add(TResIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tadd(): ";
        ArrayList results = test.getRealExecStats();
        if (results != null) {
            for (int i = 0; i < results.size(); i++) {
                if (super.init(testResRoot + Constants.INTERNAL_FILE_SEP
                    + ((TResIR)results.get(i)).getRepFile() + resExtension)) {
                    super.add((TResIR)results.get(i));
                    super.close();
                }
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Storage#closeStorage()
     */
    public boolean close() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Storage#addToReportOnly(org.apache.harmony.harness.TResIR)
     */
    public boolean addToReportOnly(TResIR test) {
        return true;
    }
}
