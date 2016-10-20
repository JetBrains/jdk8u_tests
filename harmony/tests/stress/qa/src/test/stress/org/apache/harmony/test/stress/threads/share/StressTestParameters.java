/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
package org.apache.harmony.test.stress.threads.share;

import org.apache.harmony.share.DRLLogging;
import java.util.List;
import java.util.LinkedList;

/**
 * Class with parameters.
 */

public class StressTestParameters {

    /**
     * Array of big padding objects.
     */
    public static List arrayOfObjects;

    /**
     * Array of small padding objects.
     */
    public static List arrayOfSmallObjects;
    
    /* Logger */
    DRLLogging log;

    public StressTestParameters(DRLLogging log) {
        this.log = log;
    }

    public int numThreads = 10;

    public int smallObjSize = 500;

    public int freeMem = 250;

    public int sleepTime = 3000;

    public int iterations = 500;

    /**
     * OutOfMemoryError (low memory conditions). 0 == off, 1 == on.
     */
    public int oOMEr = 1;

    /**
     * StackOverflowError (dedicated thread throws SOE). 0 == off, 1 == on.
     */
    public int sOEr = 1;

    /**
     * Synchronized or unsynchronized access. 0 == synch, 1 == unsynch, 2 ==
     * both.
     */
    public int synchUnsynch = 2;

    /**
     * Join method (0 == every thread joins to previous, 1 == all threads join
     * to one)
     */
    public int joinMthd = 0;

    /* Parse parameters. */
    public boolean parse(String params[]) {
        if (params.length > 0) {
            String parameter;

            try {
                parameter = getArg(params, "-numThreads");
                if (parameter != null) {
                    numThreads = Integer.parseInt(parameter);
                    if (numThreads < 1) {
                        return error("Parameter -numThreads should be larger"
                                + " than 1");
                    }
                    if ((numThreads % 2) != 0) {
                        numThreads++;
                    }
                }
            } catch (Throwable t) {
                return error("Error during -numThreads param parsing: "
                        + t.toString());
            }

            try {
                parameter = getArg(params, "-smallObjSize");
                if (parameter != null) {
                    smallObjSize = Integer.parseInt(parameter);
                    if (smallObjSize < 1) {
                        return error("Parameter -smallObjSize should be larger"
                                + " than 1");
                    }
                }
            } catch (Throwable t) {
                return error("Error during -smallObjSize param parsing: "
                        + t.toString());
            }

            try {
                parameter = getArg(params, "-freeMem");
                if (parameter != null) {
                    freeMem = Integer.parseInt(parameter);
                    if (freeMem < 1) {
                        return error("Parameter -freeMem should be larger"
                                + " than 1");
                    }
                }
            } catch (Throwable t) {
                return error("Error during -freeMem param parsing: "
                        + t.toString());
            }

            try {
                parameter = getArg(params, "-sleepTime");
                if (parameter != null) {
                    sleepTime = Integer.parseInt(parameter);
                    if (sleepTime < 1) {
                        return error("Parameter -sleepTime should be larger"
                                + " than 1");
                    }
                }
            } catch (Throwable t) {
                return error("Error during -sleepTime param parsing: "
                        + t.toString());
            }

            try {
                parameter = getArg(params, "-iterations");
                if (parameter != null) {
                    iterations = Integer.parseInt(parameter);
                    if (iterations < 1) {
                        return error("Parameter -iterations should be larger"
                                + " than 1");
                    }
                }
            } catch (Throwable t) {
                return error("Error during -iterations param parsing: "
                        + t.toString());
            }

            try {
                parameter = getArg(params, "-oOMEr");
                if (parameter != null) {
                    if (parameter.equals("off")) {
                        oOMEr = 0;
                    } else if (parameter.equals("on")) {
                        oOMEr = 1;
                    } else {
                        return error("Bad -oOMEr param");
                    }
                }
            } catch (Throwable t) {
                return error("Error during -oOMEr param parsing: "
                        + t.toString());
            }

            try {
                parameter = getArg(params, "-sOEr");
                if (parameter != null) {
                    if (parameter.equals("off")) {
                        sOEr = 0;
                    } else if (parameter.equals("on")) {
                        sOEr = 1;
                    } else {
                        return error("Bad -sOEr param");
                    }
                }
            } catch (Throwable t) {
                return error("Error during -sOEr param parsing: "
                        + t.toString());
            }

            try {
                parameter = getArg(params, "-synchUnsynch");
                if (parameter != null) {
                    if (parameter.equals("synch")) {
                        synchUnsynch = 0;
                    } else if (parameter.equals("unsynch")) {
                        synchUnsynch = 1;
                    } else if (parameter.equals("both")) {
                        synchUnsynch = 2;
                    } else {
                        return error("Bad -synchUnsynch param");
                    }
                }
            } catch (Throwable t) {
                return error("Error during -synchUnsynch param parsing: "
                        + t.toString());
            }

            try {
                parameter = getArg(params, "-joinMthd");
                if (parameter != null) {
                    if (parameter.equals("chain")) {
                        joinMthd = 0;
                    } else if (parameter.equals("leader")) {
                        joinMthd = 1;
                    } else {
                        return error("Bad -joinMthd param");
                    }
                }
            } catch (Throwable t) {
                return error("Error during -joinMthd param parsing: "
                        + t.toString());
            }
        }
        return true;
    }

    private boolean error(String failInfo) {
        log.info(failInfo);
        return false;
    }

    /**
     * Method to parse parameters. Gives parameter by name.
     */
    public static String getArg(String[] args, String name) {

        if (name == null) {
            return null;
        }

        for (int i = 0; i < args.length - 1; i++) {
            try {
                if (name.equals(args[i])) {
                    return args[i + 1];
                }
            } catch (Exception ex) {
                break;
            }
        }

        return null;
    }
    
    /**
     * Method pads memory.
     */
    public static void padMemory(int smallObjSize, int freeMem) {

        arrayOfObjects = new LinkedList();

        arrayOfSmallObjects = new LinkedList();

        Object ballastObj1 = new int[100][100][100];

        Object ballastObj2 = new int[smallObjSize];

        /* Initiate OutOfMemory, */
        try {
            while (true) {
                /* Pad memory by big objects. */
                arrayOfObjects.add(new int[100][100][100]);
            }
        } catch (OutOfMemoryError oome) {
            ballastObj1 = null;

            /* Initiate OutOfMemory again. Now by small objects. */
            try {
                while (true) {
                    /* Pad memory by small objects. */
                    arrayOfSmallObjects.add(new int[smallObjSize]);
                }
            } catch (OutOfMemoryError oome2) {
                ballastObj2 = null;

                /* Check size of list. */
                if (freeMem > arrayOfSmallObjects.size()) {
                    freeMem = arrayOfSmallObjects.size();
                }
                
                /* Release some memory. */
                for (int i = 0; i < freeMem; i++) {
                    arrayOfSmallObjects.remove(0);
                }
            }
        }

        /**
         * For now we have filled memory heap with a small peace of free memory
         * (freeMem*smallObject)
         */
    }

    /**
     * Method releases memory.
     */
    public static void releaseMemory() {
        arrayOfObjects = null;
        arrayOfSmallObjects = null;
        return;
    }
    
    /**
     * Method checks verification array.
     */
    public static boolean checkVerifArray(int verificationArray[],
            int numThreads) {
        boolean passage = true;
        
        /* Check register array. It should contain all 1. */
        for (int j = 0; j < numThreads; j++) {
            if (verificationArray[j] != 1) {
                if (passage) {
                    passage = false;
                }
            }
        }

        return passage;
    }
}
