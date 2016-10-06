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
package org.apache.harmony.harness;

import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;

public class InternalTHLoggerFile extends InternalTHLogger {
    private Level    curLevel;
    protected String fileName = null;

    public InternalTHLoggerFile(String name) {
        if (name == null) {
            return;
        }
        try {
            File fout = new File(name);
            fout.createNewFile();
            if (fout.isFile()) {
                fileName = fout.getAbsolutePath();
            }
        } catch (Exception e) {
            //do nothing
        }
    }

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
        return super.init(level);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#addLog(java.lang.String)
     */
    public boolean add(String logMessage) {
        writelog(logMessage);
        return super.add(logMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Logging#add(java.util.logging.Level, java.lang.String)
     */
    public boolean add(Level msgLevel, String logMessage) {
        if (curLevel != Level.OFF && msgLevel.intValue() >= curLevel.intValue()) {
            writelog(logMessage);
        }
        return super.add(msgLevel, logMessage);
    }

    protected synchronized void writelog(String data) {
        if (fileName == null) {
            return;
        }
        File fout = new File(fileName);
        FileWriter out;
        try {
            if (fout.isDirectory()) {
                return;
            }
            if (fout.isFile()) {
                out = new FileWriter(fout, true);
            } else {
                out = new FileWriter(fout);
            }
            out.write(data + "\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            //do nothing
        }
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
}
