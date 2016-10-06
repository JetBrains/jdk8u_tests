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
 * @version $Revision: 1.4 $
 */
package org.apache.harmony.share;

public class Result {

    public static final int PASS       = 104;
    public static final int FAIL       = 105;
    public static final int ERROR      = 106;
    public static final int MODE_ERROR = 107;

    private int             status     = 0;
    private String          msg;

    public Result(int execStat, String repMsg) {
        status = execStat;
        msg = repMsg;
    }

    public int getResult() {
        return status;
    }

    public String getMessage() {
        return msg;
    }
}