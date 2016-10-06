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

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DRLLoggerL extends Logger implements DRLLogging {

    protected Level   curLevel = Level.INFO;
    protected Handler handler;

    /**
     * @param name
     * @param bundlName
     */
    protected DRLLoggerL(String name, String bundlName) {
        super(name, bundlName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#init()
     */
    public boolean init() {
        return init(Level.INFO.intValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#init(int)
     */
    public boolean init(int level) {
        this.setLevel(level);
        handler = new ConsoleHandler();
        handler.setFormatter(new TestLogFormatter());
        handler.setLevel(Level.ALL);
        addHandler(handler);
        setUseParentHandlers(false);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#add(java.lang.String)
     */
    public boolean add(String logMessage) {
        return add(curLevel.intValue(), logMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#add(int, java.lang.String)
     */
    public boolean add(int msgLevel, String logMessage) {
        log(intToLevel(msgLevel), logMessage);
        handler.flush();
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#close()
     */
    public boolean close() {
        return true;
    }

    // convert int value to Level
    Level intToLevel(int data) {
        return Level.INFO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#setLevel(int)
     */
    public void setLevel(int newLevel) {
        setLevel(intToLevel(newLevel));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#getLevelInt()
     */
    public int getLevelInt() {
        return curLevel.intValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#getLoggerObject()
     */
    public Object getLoggerObject() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#addHandler(java.lang.Object)
     */
    public void addHandler(Object handler) {
        addHandler((Handler)handler);
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
        if (msgLevel >= curLevel.intValue()) {
            return add(exception);
        }
        return false;
    }
}