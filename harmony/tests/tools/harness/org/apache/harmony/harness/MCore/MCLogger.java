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
 * @version $Revision: 1.11 $
 */
package org.apache.harmony.harness.MCore;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.plugins.StoreRes;

import org.apache.harmony.share.DRLLogging;

public abstract class MCLogger extends Logger implements Logging, DRLLogging {

    protected Handler     logH = null;
    protected InputStream in;

    public MCLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    public MCLogger() {
        super(null, null);
    }

    public abstract boolean init();

    public abstract boolean close();

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#init(java.util.logging.Level)
     */
    public synchronized boolean init(Level curLevel) {
        boolean tmpVal = init();
        setLevel(curLevel);
        return tmpVal;
    }

    public synchronized byte[] read() {
        logH.flush();
        if (in == null) {
            return null;
        }
        synchronized (in) {
            byte[] tmpBuf = null;
            int toRead;
            try {
                toRead = in.available();
                if (toRead > 0) {
                    tmpBuf = new byte[toRead];
                    in.read(tmpBuf, 0, toRead);
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tmpBuf;
        }
    }

    public synchronized void info(String msg) {
        if (in != null) {
            synchronized (in) {
                super.info(msg);
            }
        } else {
            super.info(msg);
        }
        logH.flush();
    }

    public synchronized void warning(String msg) {
        if (in != null) {
            synchronized (in) {
                super.warning(msg);
            }
        } else {
            super.warning(msg);
        }
        logH.flush();
    }

    public synchronized void severe(String msg) {
        if (in != null) {
            synchronized (in) {
                super.severe(msg);
            }
        } else {
            super.severe(msg);
        }
        logH.flush();
    }

    public synchronized void log(Level lev, String msg) {
        if (in != null) {
            synchronized (in) {
                super.log(lev, msg);
            }
        } else {
            super.log(lev, msg);
        }
        logH.flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#add(java.lang.String)
     */
    public synchronized boolean add(String logMessage) {
        log(Level.SEVERE, logMessage);
        logH.flush();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#add(java.util.logging.Level,
     *      java.lang.String)
     */
    public synchronized boolean add(Level msgLevel, String logMessage) {
        log(msgLevel, logMessage);
        logH.flush();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#add(java.util.logging.Level,
     *      java.lang.String)
     */
    public synchronized boolean add(Level msgLevel, String logMessage,
        Throwable e) {
        log(msgLevel, logMessage);
        if (e != null) {
            StackTraceElement[] st = e.getStackTrace();
            for (int i = 0; i < st.length; i++) {
                log(msgLevel, st[i].toString());
            }
        }
        logH.flush();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#getLoggerObject()
     */
    public synchronized Object getLoggerObject() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#getLevelInt()
     */
    public int getLevelInt() {
        return this.getLevel().intValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#addHandler(java.lang.Object)
     */
    public void addHandler(Object handler) {
        super.addHandler((Handler)handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#add(int, java.lang.String)
     */
    public boolean add(int msgLevel, String logMessage) {
        return add(intToLevel(msgLevel), logMessage);
    }

    Level intToLevel(int data) {
        if (data >= Level.SEVERE.intValue()) {
            return Level.SEVERE;
        } else if (data >= Level.WARNING.intValue()) {
            return Level.WARNING;
        } else if (data >= Level.INFO.intValue()) {
            return Level.INFO;
        } else if (data >= Level.CONFIG.intValue()) {
            return Level.CONFIG;
        } else if (data >= Level.FINE.intValue()) {
            return Level.FINE;
        } else if (data >= Level.FINER.intValue()) {
            return Level.FINER;
        } else if (data >= Level.FINEST.intValue()) {
            return Level.FINEST;
        } else if (data >= Level.ALL.intValue()) {
            return Level.ALL;
        }
        return Level.OFF;
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
     * @see org.apache.harmony.share.DRLLogging#add(java.lang.Throwable)
     */
    public boolean add(Throwable exception) {
        StackTraceElement[] st = exception.getStackTrace();
        for (int i = 0; i < st.length; i++) {
            add(st[i].toString());
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.DRLLogging#add(int, java.lang.Throwable)
     */
    public boolean add(int msgLevel, Throwable exception) {
        if (msgLevel >= this.getLevelInt()) {
            return add(exception);
        }
        return false;
    }
}

class TestLogFormatter extends Formatter {

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public String format(LogRecord record) {
        return StoreRes.RECORD_START + record.getMillis()
            + StoreRes.MSG_DELIMITER + record.getMessage() + "\n";
    }
}