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
 * @version $Revision: 1.8 $
 */

package org.apache.harmony.share;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class LogFactory {

    private static boolean initialize = false;
    private DRLLogging     log        = new DRLLoggerS();

    public DRLLogging getLogger() {
        if (initialize == false) {
            try {
                Handler h = new ConsoleHandler();
                Object obj = Logger.getAnonymousLogger();
                DRLLogging tmp = new DRLLoggerL(null, null);
                tmp.init();
                log = tmp;
            } catch (Throwable e) {
                //do nothing. The DRLLoggerS will be used in this case
            }
        }
        return log;
    }
    //note, the error stream is default test logger stream. It should
    //be divided from frame works out stream.
    //ConsoleHandler use the error stream by default
    OutputStream getLogStream() {
        return System.err;
    }
    //default output for user data stream. By default, it is interpreted
    //as xml data
    OutputStream getUserDataStream() {
        return System.out;
    }
}