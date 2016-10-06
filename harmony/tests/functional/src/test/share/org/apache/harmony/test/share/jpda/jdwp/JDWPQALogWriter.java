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
package org.apache.harmony.test.share.jpda.jdwp;

import org.apache.harmony.jpda.tests.framework.LogWriter;
import org.apache.harmony.share.DRLLogging;

/**
 * This class provides logging messages to underlying DRLLogging object.
 * There are can be several JPDALogWriter objects writing to the same 
 * underlying log with different prefixes. 
 *
 */
public class JDWPQALogWriter extends LogWriter {

    protected DRLLogging log;
    
    /**
     * Constructs an instance of the class for given output stream.
     * 
     * @param outputStream stream for output
     * @param prefix prefix for messages or null
     */
    public JDWPQALogWriter(DRLLogging log, String prefix) {
        super(prefix);
        this.log = log;
    }
    
    /**
     * Prints error message.
     *  
     * @param error message to be printed
     */
    public synchronized void printError(String message) {
        log.add(getErrorPrefix() + message);
    }

    /**
     * Prints exception information with explaining message.
     * 
     * @param explaining message to be printed
     * @param exception to be printed
     */
    public synchronized void printError(String message, Throwable throwable) {
        log.add(getErrorPrefix() + message);
        log.add(throwable);
    }

    /**
     * Prints exception information w/o explaining message.
     * 
     * @param exception to be printed
     */
    public synchronized void printError(Throwable throwable) {
        log.add(throwable);
    }

    /**
     * Prints message to the output stream w/o line feed.
     * 
     * @param message to be printed
     */
    public synchronized void print(String message) {
        log.add(prefix + message);
    }

    /**
     * Prints message to the output stream with line feed.
     * 
     * @param message to be printed
     */
    public synchronized void println(String message) {
        log.add(prefix + message);
    }
    
    ///////////////////////////////////////////////////////////////////////////////
    
    /**
     * Get prefix for error messages.
     */
    protected String getErrorPrefix() {
        return "# ERROR: " + prefix;
    }
}
