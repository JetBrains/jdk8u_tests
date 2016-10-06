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
package org.apache.harmony.harness;

import org.apache.harmony.harness.Logging;

import java.io.Serializable;
import java.util.logging.Level;

public class InternalTHLogger implements Logging, Serializable {

    private Level curLevel;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#init()
     */
    public boolean init() {
        return init(Level.INFO);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#initLogging()
     */
    public boolean init(Level level) {
        curLevel = level;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#addLog(java.lang.String)
     */
    public boolean add(String logMessage) {
        System.out.println(logMessage);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#add(java.util.logging.Level, java.lang.String)
     */
    public boolean add(Level msgLevel, String logMessage) {
        if (curLevel != Level.OFF && msgLevel.intValue() >= curLevel.intValue()) {
            System.out.println(logMessage);
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#add(java.util.logging.Level,
     *      java.lang.String, java.lang.Throwable)
     */
    public boolean add(Level msgLevel, String logMessage, Throwable e) {
        if (curLevel != Level.OFF && msgLevel.intValue() >= curLevel.intValue()) {
            System.out.println(logMessage);
            e.printStackTrace(System.out);
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#closeLog()
     */
    public boolean close() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#setLevel(java.util.logging.Level)
     */
    public void setLevel(Level newLevel) {
        curLevel = newLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#getLevel(java.util.logging.Level)
     */
    public Level getLevel() {
        return curLevel;
    }

    public Object getLoggerObject() {
        return null;//logger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#read()
     */
    public byte[] read() {
        return null;
    }
}
