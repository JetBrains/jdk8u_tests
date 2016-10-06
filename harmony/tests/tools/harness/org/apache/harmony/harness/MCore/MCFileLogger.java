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
 * @version $Revision: 1.9 $
 */
package org.apache.harmony.harness.MCore;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class MCFileLogger extends MCLogger {

    protected String fileName;
    protected File   logFile;

    public synchronized boolean init() {
        if (fileName == null) {
            try {
                String tmpDir = "";
                int mcoreID;
                try {
                    mcoreID = Main.getCurCore().getMCoreID();
                    fileName = "fileBuf" + mcoreID + "thlog";
                    tmpDir = Main.getCurCore().getConfig().getTempStorage();
                } catch (Exception e) {
                    // it is for debugging purpose to run tests in the harness
                    // VM this mode should be prohibited in the future
                    fileName = "fileBuf";
                    tmpDir = System.getProperty("user.dir");
                } finally {
                    logFile = File.createTempFile(fileName, null, new File(
                        tmpDir));
                    logFile.deleteOnExit();
                    logH = new FileHandler(logFile.getAbsolutePath());
                    logH.setFormatter(new TestLogFormatter());
                    in = new FileInputStream(logFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            setUseParentHandlers(false);
            addHandler(logH);
            setLevel(Level.INFO);
        }
        return true;
    }

    public synchronized boolean close() {
        try {
            logH.flush();
            logH.close();
            logFile.delete();
        } catch (Exception e) {
            //do nothing
        }
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