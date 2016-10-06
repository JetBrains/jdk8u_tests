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

import java.util.logging.Level;

public interface DRLLogging {

    public static final int SEVERE  = Level.SEVERE.intValue();  //1000;
    public static final int WARNING = Level.WARNING.intValue(); //800;
    public static final int INFO    = Level.INFO.intValue();    //700;
    public static final int CONFIG  = Level.CONFIG.intValue();  //500;
    public static final int FINE    = Level.FINE.intValue();    //300;
    public static final int FINER   = Level.FINER.intValue();   //200;
    public static final int FINEST  = Level.FINEST.intValue();  //100;
    public static final int ALL     = Level.ALL.intValue();     //50;
    public static final int OFF     = Level.OFF.intValue();     //0;

    /*
     * initialize the current logger with the default level
     */
    public boolean init();

    /*
     * initialize the current logger with the pointed level
     */
    public boolean init(int level);

    /*
     * add message to the log if the current level not OFF
     */
    public boolean add(String logMessage);

    /*
     * add message to the log
     */
    public boolean add(int msgLevel, String logMessage);

    /*
     * add stackTrace to the log
     */
    public boolean add(Throwable exception);

    /*
     * add stackTrace to the log
     */
    public boolean add(int msgLevel, Throwable exception);

    /*
     * add message to the log
     */
    public void fine(String logMessage);

    /*
     * add message to the log
     */
    public void config(String logMessage);

    /*
     * add message to the log
     */
    public void info(String logMessage);

    /*
     * add message to the log
     */
    public void warning(String logMessage);

    /*
     * add message to the log
     */
    public void severe(String logMessage);

    /*
     * close the current logger
     */
    public boolean close();

    /*
     * set the current log level
     */
    public void setLevel(int newLevel);

    /*
     * Return the current log level as integer value
     */
    public int getLevelInt();

    public Object getLoggerObject();

    void setUseParentHandlers(boolean useParentHandlers);

    void addHandler(Object handler);
}