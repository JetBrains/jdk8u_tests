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
/** 
 */
/*
 * Created on 13.04.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.share;

import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *  
 */
public class InstrumentedUILog {
    static final List log = Collections.synchronizedList(new LinkedList());

    public static final Object ANY_NON_NULL_VALUE = new Object();

    private static boolean active = true; //this is a hook that allows

    // collecting new data while printing
    // log

    public static void clear() {
        Util.waitQueueEventsProcess();
        getLog().clear();
    }

    public static void printLog() {
        Util.waitQueueEventsProcess();

        boolean saveActive = active;
        active = false;

        try {
            List lst = log;
            PrintStream logStream = System.err;

            logStream.println("log size is " + lst.size());
            for (int i = 0; i < lst.size(); ++i) {
                logStream.print("" + (i + 1) + "-th log line is ");
                if (lst.get(i) instanceof Object[]) {
                    Object[] oarr = (Object[]) lst.get(i);
                    for (int j = 0; j < oarr.length; ++j) {
                        if (j != 0) {
                            logStream.print(", ");
                        }
                        try {
                            logStream.print(oarr[j]);
                        } catch (Throwable e) {
                            logStream.print(oarr[j].getClass().getName()
                                    + ".toString() thrown exception");
                        }
                    }
                } else {
                    logStream.print(lst.get(i));
                }
                logStream.println();
            }
        } finally {
            active = saveActive;
        }
    }

    public static void printLogAsArray() {
        Util.waitQueueEventsProcess();

        boolean saveActive = active;
        active = false;

        try {
            List lst = log;
            PrintStream logStream = System.err;

            for (int i = 0; i < lst.size(); ++i) {
                logStream.print("/* " + (i + 1) + " */ { ");
                if (lst.get(i) instanceof Object[]) {
                    Object[] oarr = (Object[]) lst.get(i);
                    for (int j = 0; j < oarr.length; ++j) {
                        if (j != 0) {
                            logStream.print(", ");
                        }
                        if (oarr[j] != null) {
                            logStream.print('"');
                            try {
                                logStream.print(oarr[j]);
                            } catch (Throwable e) {
                                logStream.print(oarr[j].getClass().getName()
                                        + ".toString() thrown exception");
                            }
                            logStream.print('"');
                        } else {
                            logStream.print("null");
                        }
                    }
                } else {
                    logStream.print('"');
                    logStream.print(lst.get(i));
                    logStream.print('"');
                }
                logStream.print("}, ");
                logStream.println();
            }
        } finally {
            active = saveActive;
        }
    }

    public static List getLog() {
        return log;
    }

    public static void add(Object o) {
        if (active) {
            getLog().add(o);
        }
    }

    public static boolean equals(Object[][] supposedLog) {
        Util.waitQueueEventsProcess();

        boolean saveActive = active;
        active = false;

        try {

            if (supposedLog == null) {
                return false;
            }
            if (log.size() != supposedLog.length) {
                return false;
            }
            for (int i = 0; i < supposedLog.length; ++i) {
                if (!(log.get(i) instanceof Object[])) {
                    System.err.println("log line " + (i + 1) + " not an array");
                    return false;
                }
                if (!arraysEquals(supposedLog[i], (Object[]) log.get(i))) {
                    System.err.print("Supposed: ");

                    for (int j = 0; j < supposedLog[i].length; ++j) {
                        if (j != 0) {
                            System.err.print(", ");
                        }
                        System.err.print(supposedLog[i][j]);
                    }
                    System.err.println();

                    System.err.print("Got: ");
                    Object[] oarr = (Object[]) log.get(i);

                    for (int j = 0; j < oarr.length; ++j) {
                        if (j != 0) {
                            System.err.print(", ");
                        }
                        System.err.print(oarr[j]);
                    }
                    System.err.println();

                    return false;
                }
            }
        } finally {
            active = saveActive;
        }

        return true;
    }

    public static boolean contains(Object[][] supposedLog) {
        Util.waitQueueEventsProcess();

        boolean saveActive = active;
        active = false;

        try {
            if (supposedLog == null) {
                return false;
            }
            for (int i = 0; i < supposedLog.length; ++i) {
                if (!contains(supposedLog[i])) {
                    System.err.println("log line " + (i + 1) + " ("
                            + supposedLog[i][0] + ") not found in log");
                    return false;
                }
            }
        } finally {
            active = saveActive;
        }

        return true;
    }

    private static boolean contains(Object[] supposedLogEntry) {
        for (int i = 0; i < log.size(); ++i) {
            if (!(log.get(i) instanceof Object[])) {
                continue;
            }
            if (arraysEquals(supposedLogEntry, (Object[]) log.get(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean arraysEquals(Object[] oarr1, Object[] oarr2) {
        if (oarr1.length != oarr2.length) {
            //System.err.println("length 1 : " + oarr1.length + "length 2: " +
            // oarr2.length);
            return false;
        }
        for (int i = 0; i < oarr1.length; ++i) {
            if (!objectsEquals(oarr1[i], oarr2[i])) {
                //System.err.println("failing on line " + (i + 1));
                return false;
            }
        }
        return true;
    }

    private static boolean objectsEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1 == ANY_NON_NULL_VALUE || o2 == ANY_NON_NULL_VALUE) {
            return true;
        }
        if (o1 instanceof Boolean && o2 instanceof String) {
            return o1.equals(Boolean.valueOf((String) o2));
        }
        if (o2 instanceof Boolean && o1 instanceof String) {
            return o2.equals(Boolean.valueOf((String) o1));
        }

        return o1.equals(o2);
    }
}