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
package org.apache.harmony.share;

import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public abstract class Base {

    private LogFactory       logFactory     = new LogFactory();

    //in the future log should be protected instance variable
    public static DRLLogging log            = new LogFactory().getLogger();
    protected int            curLevel       = DRLLogging.INFO;

    protected String[]       testArgs;

    protected int            logOptPosition = -1;

    protected boolean        performance    = false;
    protected long           duration       = 0;
    protected long           minTestsCount  = 0;

    private OutputStream     userDataOut    = logFactory.getUserDataStream();

    public abstract int test();

    public abstract int test(String[] args);

    public void before() throws Exception {
        //by default, do nothing
    }

    public void after() throws Exception {
        //by default, do nothing
    }

    public int getLogLevel(String[] args) {
        logOptPosition = -1;
        if (args.length > 1) {
            String tmpStore = null;
            String strPerformance = null;
            for (int i = 0; i < args.length; i++) {
                if ("logLevel".equalsIgnoreCase(args[i])) {
                    tmpStore = args[i + 1];
                    logOptPosition = i;
                    break;
                }
            }
            if (tmpStore != null) {
                if (tmpStore.equalsIgnoreCase("info")) {
                    curLevel = DRLLogging.INFO;
                } else if (tmpStore.equalsIgnoreCase("warning")) {
                    curLevel = DRLLogging.WARNING;
                } else if (tmpStore.equalsIgnoreCase("severe")) {
                    curLevel = DRLLogging.SEVERE;
                } else {
                    curLevel = DRLLogging.OFF;
                }
            }
        }
        return curLevel;
    }

    public void removeLogLevel(String[] args) {
        if (logOptPosition >= 0 && logOptPosition < (args.length - 1)) {
            testArgs = new String[args.length - 2];
            int cnt = 0;
            for (int i = 0; i < logOptPosition; i++) {
                testArgs[cnt++] = args[i];
            }
            for (int i = (logOptPosition + 2); i < args.length; i++) {
                testArgs[cnt++] = args[i];
            }
        } else {
            testArgs = args;
        }
    }

    public void setPerformance(String[] args) throws IllegalArgumentException {
        if (args != null && args.length > 1) {
            String paramPerf = "-performance";
            for (int i = 0; i < args.length; i++) {
                if (paramPerf.equalsIgnoreCase(args[i])) {
                    String str = null;
                    try {
                        str = args[i + 1];
                        setPerformanceParameters(str);
                        performance = true;
                    } catch (Exception e) {
                        throw new IllegalArgumentException(str);
                    }
                    break;
                }
            }
        }
    }

    public void setPerformanceParameters(String params)
        throws NoSuchElementException, NumberFormatException {
        if (params == null) {
            throw new NullPointerException();
        }
        final String ppDelimiter = ":";
        StringTokenizer st = new StringTokenizer(params, ppDelimiter);
        duration = Long.valueOf(st.nextToken()).longValue();
        minTestsCount = Long.valueOf(st.nextToken()).longValue();
    }

    public boolean storeUserData(String tagName, String data) {
        try {
            userDataOut
                .write(("<" + tagName + ">\n" + data + "</" + tagName + ">\n")
                    .getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean storeUserData(String data) {
        try {
            userDataOut.write(data.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}