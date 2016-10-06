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
/*
 * Created on 17.11.2004
 */

package org.apache.harmony.vmtt;

import java.util.logging.*;

/**
 * @author agolubit
 */

public class VMTTLogger extends Logger {

    class VMTTFormatter extends Formatter {
        public String format(LogRecord record) {
            return record.getMessage() + "\n";
        }
    };

    public VMTTLogger() {
        super("VMTTLogger", null);
        setUseParentHandlers(false);
        Handler h = new ConsoleHandler();
        h.setFormatter(new VMTTFormatter());
        addHandler(h);
    }
    
    public void warning(int line, String msg) {
        warning("Error in line #" + line + ". " + msg);
    }
}
