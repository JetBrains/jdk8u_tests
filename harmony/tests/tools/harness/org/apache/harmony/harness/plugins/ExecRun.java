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
 * @version $Revision: 1.14 $
 */
package org.apache.harmony.harness.plugins;

import org.apache.harmony.harness.MessageInfo;

public class ExecRun extends ExecRunDRL {

    private final String classID = "ExecRun";

    public int runOther(String[] args) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunOther(): ";
        int execStatus = super.runOther(args);
        try {
            //need to copy the system.err to outMsg
            //No agreement for native tests about of system.out and system.err
            //So, default behavior do copy
            result.setOutMsg(result.getTestSpecificInfoAsString(null));
            result.setTestSpecificInfo((String)null);
        } catch (Exception e) {
            result.setOutMsg(methodLogPrefix + MessageInfo.UNEX_EXCEPTION + e);
        }
        return execStatus;
    }

}