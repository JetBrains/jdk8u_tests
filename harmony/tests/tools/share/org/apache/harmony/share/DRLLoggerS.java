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
 * @version $Revision: 1.5 $
 */
package org.apache.harmony.share;

import java.io.Serializable;

public class DRLLoggerS implements DRLLogging, Serializable {

    protected int curLevel = INFO;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#init()
     */
    public boolean init() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#initLogging()
     */
    public boolean init(int level) {
        curLevel = level;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#addLog(java.lang.String)
     */
    public boolean add(String logMessage) {
        System.err.println(logMessage);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#add(java.util.logging.Level, java.lang.String)
     */
    public boolean add(int msgLevel, String logMessage) {
        if (curLevel != OFF && msgLevel >= curLevel) {
            System.err.println(logMessage);
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
    public void setLevel(int newLevel) {
        curLevel = newLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#getLevel(java.util.logging.Level)
     */
    public int getLevelInt() {
        return curLevel;
    }

    public Object getLoggerObject() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#setUseParentHandlers(boolean)
     */
    public void setUseParentHandlers(boolean useParentHandlers) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#addHandler(java.lang.Object)
     */
    public void addHandler(Object handler) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#fine(java.lang.String)
     */
    public void fine(String logMessage) {
        add(FINE, logMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#config(java.lang.String)
     */
    public void config(String logMessage) {
        add(CONFIG, logMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#info(java.lang.String)
     */
    public void info(String logMessage) {
        add(INFO, logMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#warning(java.lang.String)
     */
    public void warning(String logMessage) {
        add(WARNING, logMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#severe(java.lang.String)
     */
    public void severe(String logMessage) {
        add(SEVERE, logMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#add(java.lang.Throwable)
     */
    public boolean add(Throwable exception) {
        StackTraceElement[] st = exception.getStackTrace();
        if (st != null) {
            for (int i = 0; i < st.length; i++) {
                add(st[i].toString());
            }
        } else {
            add("stack trace not available for " + exception);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#add(int, java.lang.Throwable)
     */
    public boolean add(int msgLevel, Throwable exception) {
        if (msgLevel >= curLevel) {
            return add(exception);
        }
        return false;
    }

}
