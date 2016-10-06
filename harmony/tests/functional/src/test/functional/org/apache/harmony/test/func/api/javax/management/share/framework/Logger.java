/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.api.javax.management.share.framework;

import java.io.PrintStream;
import java.util.Hashtable;

/**
 */
public class Logger {

    /**
     * SERVE log level.
     */
    public static final int  SERVE        = 0;

    /**
     * DEBUG log level.
     */
    public static final int  DEBUG        = 1;

    /**
     * INFO log level.
     */
    public static final int  INFO         = 2;

    /**
     * WARN log level.
     */
    public static final int  WARN         = 3;

    /**
     * ERROR log level.
     */
    public static final int  ERROR        = 4;

    /**
     * Logger instances.
     */
    private static Hashtable instances    = new Hashtable();

    /**
     * Output stream to use.
     */
    private PrintStream      out          = System.err;

    /**
     * Class name.
     */
    private String           className;

    /**
     * Log level.
     */
    private int              level        = DEBUG;

    /**
     * Default log level.
     */
    private static int       defaultLevel = DEBUG;

    /**
     * Indicates whether details such as method name, source name, line number
     * should be logged.
     */
    private boolean          logDetails   = true;

    /**
     * Construct new Logger instance for the specified class name.
     * 
     * @param className class name.
     */
    private Logger(String className) {
        this.className = className;
        level = defaultLevel;
    }

    /**
     * Retrieves the Logger instance, instantiated for the specified class.
     * 
     * @param c
     * @return
     */
    public static Logger getLogger(Class c) {
        String className = c.getName();
        Logger log = (Logger) instances.get(className);
        if (log == null) {
            log = new Logger(className);
            instances.put(className, log);
        }
        return log;
    }

    /**
     * Log serve message.
     * 
     * @param msg message.
     */
    public void serve(Object msg) {
        if (level == SERVE)
            log(msg, "[SERVE]", 2);
    }

    /**
     * Log debug message.
     * 
     * @param msg message.
     */
    public void debug(Object msg) {
        if (level < INFO)
            log(msg, "[DEBUG]", 2);
    }

    /**
     * Log info message.
     * 
     * @param msg message.
     */
    public void info(Object msg) {
        if (level < WARN)
            log(msg, "[INFO]", 2);
    }

    /**
     * Log warning message.
     * 
     * @param msg message.
     */
    public void warn(Object msg) {
        if (level < ERROR)
            log(msg, "[WARNING]", 2);
    }

    /**
     * Log error message.
     * 
     * @param msg message.
     */
    public void error(Object msg) {
        if (level <= ERROR)
            log(msg, "[ERROR]", 2);
    }

    /**
     * Log message.
     * 
     * @param message message.
     * @param prefix message prefix.
     * @param index method index in the stack trace.
     */
    public void log(Object message, String prefix, int index) {
        String msg = prefix;
        if (logDetails) {
            StackTraceElement ste = new Throwable().getStackTrace()[index];
            String name = ste.getMethodName();
            String fname = "";
            int ln = ste.getLineNumber();
            if (ln >= 0) {
                fname = ste.getFileName();
                if (fname != null) {
                    fname = fname + ":" + ln;
                }
            }
            msg += " [" + name + "] " + ste.getClassName() + "." + name + "("
                + fname + "): ";
        } else {
            msg += " " + className + ":";
        }

        String m = null;
        if (message != null) {
            m = message.toString().replaceAll("\n", "\n" + prefix + "    ");
            m = "\n" + prefix + "    " + m;
        }

        out.println(msg + m);
    }

    /**
     * Log level.
     * 
     * @return Returns the level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set log level.
     * 
     * @param level The level to set.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Set log level.
     * 
     * @param logLevel
     */
    public void setLevel(String logLevel) {
        if (logLevel != null) {
            level = logLevel.equalsIgnoreCase("SERVE") ? SERVE : logLevel
                .equalsIgnoreCase("INFO") ? INFO : logLevel
                .equalsIgnoreCase("WARN") ? WARN : logLevel
                .equalsIgnoreCase("ERROR") ? ERROR : DEBUG;
        }
    }

    /**
     * Set default log level.
     * 
     * @param level The level to set.
     */
    public static void setDefaultLevel(int level) {
        defaultLevel = level;
    }

    /**
     * Set default log level.
     * 
     * @param logLevel
     */
    public static void setDefaultLevel(String logLevel) {
        if (logLevel != null) {
            defaultLevel = logLevel.equalsIgnoreCase("SERVE") ? SERVE
                : logLevel.equalsIgnoreCase("INFO") ? INFO : logLevel
                    .equalsIgnoreCase("WARN") ? WARN : logLevel
                    .equalsIgnoreCase("ERROR") ? ERROR : DEBUG;
        }
    }

    /**
     * Indicates whether details such as method name, source name, line number
     * should be logged.
     * 
     * @param logDetails the logDetails to set.
     */
    public void logDetails(boolean logDetails) {
        this.logDetails = logDetails;
    }

    /**
     * Output stream.
     * 
     * @return output stream.
     */
    public PrintStream getOutput() {
        return out;
    }

    /**
     * Set output stream to use.
     * 
     * @param out output stream to use.
     */
    public void setOutput(PrintStream out) {
        this.out = out;
    }

    /**
     * Release.
     */
    public void release() {
        debug("Releaseing");
        instances.remove(className);
        out = null;
        className = null;
    }
}
