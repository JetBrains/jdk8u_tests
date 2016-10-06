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
 * @version $Revision: 1.6 $
 */
package org.apache.harmony.harness;

import java.util.logging.Level;

public interface Logging {

    /**
     * Initialize the logging process with default logging level
     * 
     * @return true if successful false otherwise
     */
    public boolean init();

    /**
     * Initialize the logging process
     * 
     * @return true if successful false otherwise
     */
    public boolean init(Level curLevel);

    /**
     * Set the logging level
     * 
     * @return true if successful false otherwise
     */
    public void setLevel(Level curLevel);

    /**
     * Get the logging level
     * 
     * @return true if successful false otherwise
     */
    public Level getLevel();

    /**
     * Add message to the flow of logging data
     * 
     * @return true if successful false otherwise
     */
    public boolean add(String logMessage);

    /**
     * Add message to the flow of logging data
     * 
     * @return true if successful false otherwise
     */
    public boolean add(Level msgLevel, String logMessage);

    /**
     * Add message to the flow of logging data and stack trace for exception
     * 
     * @return true if successful false otherwise
     */
    public boolean add(Level msgLevel, String logMessage, Throwable e);

    /**
     * Return the logger object which is used to log the logging data
     * 
     * @return true if successful false otherwise
     */
    public Object getLoggerObject();

    /**
     * Return the messages wrote to log or null if this data not available.
     * 
     * @return true if successful false otherwise
     */
    public byte[] read();

    /**
     * Close the logging process
     * 
     * @return true if successful false otherwise
     */
    public boolean close();
}