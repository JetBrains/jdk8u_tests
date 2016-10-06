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
 * @version $Revision: 1.7 $
 */
package org.apache.harmony.harness.MCore;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class MCConsLogger extends MCLogger {

    protected boolean initialized = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#init()
     */
    public boolean init() {
        if (initialized == false) {
            try {
                logH = new ConsoleHandler();
                logH.setFormatter(new TestLogFormatter());
                setUseParentHandlers(false);
                addHandler(logH);
                setLevel(Level.INFO);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#close()
     */
    public boolean close() {
        logH.close();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#init(int)
     */
    public boolean init(int level) {
        return init();
    }
}