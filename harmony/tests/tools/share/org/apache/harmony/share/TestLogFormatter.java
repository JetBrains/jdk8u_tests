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
 * @version $Revision: 1.3 $
 */
package org.apache.harmony.share;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TestLogFormatter extends Formatter {

    //this constants for sorting test output to something required by test
    // developer
    protected static final String RECORD_START  = "%ecnfk@";
    protected static final String MSG_DELIMITER = "| ";

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public String format(LogRecord record) {
        return RECORD_START + record.getMillis() + MSG_DELIMITER
            + record.getMessage() + "\n";
    }
}