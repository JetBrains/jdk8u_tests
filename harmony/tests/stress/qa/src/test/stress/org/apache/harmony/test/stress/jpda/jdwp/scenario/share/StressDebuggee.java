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
/**
 * @author Anatoly F. Bondarenko
 * @version $Revision: 1.2 $
 */

/**
 * Created on 06.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.share;

import org.apache.harmony.test.stress.jpda.jdwp.share.debuggee.QARawDebuggee;

/**
 * StressDebuggee is super class for all debuggee's classes of
 * JDWP stress tests
 */
public class StressDebuggee extends QARawDebuggee {
    
    static final boolean TRIAL_RUN = false; // true - for trial runs; false - for actual runs
    
    // configurational values
    // Below values for actual runs; values for trial runs - see static initializer
    
    // EVENT tests
    public static long EVENT001_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT001_THREAD_NUMBER_LIMIT = 100; 
    
    public static long EVENT002_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT002_THREAD_NUMBER_LIMIT = 100; 
    
    public static long EVENT003_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT003_THREAD_NUMBER_LIMIT = 100; 
    
    public static long EVENT004_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT004_THREAD_NUMBER_LIMIT = 100; 
    
    public static long EVENT005_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT005_THREAD_NUMBER_LIMIT = 100; 
    public static int EVENT005_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static long EVENT006_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT006_THREAD_NUMBER_LIMIT = 100; 
    public static int EVENT006_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static long EVENT007_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT007_THREAD_NUMBER_LIMIT = 100; 
    public static int EVENT007_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static long EVENT008_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT008_THREAD_NUMBER_LIMIT = 100; 
    public static int EVENT008_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static long EVENT009_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT009_THREAD_NUMBER_LIMIT = 100; 
    
    public static long EVENT010_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT010_THREAD_NUMBER_LIMIT = 100; 
    public static int EVENT010_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
   
    public static long EVENT011_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT011_THREAD_NUMBER_LIMIT = 100; 
    
    public static long EVENT012_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT012_THREAD_NUMBER_LIMIT = 100; 
    public static int EVENT012_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static long EVENT013_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT013_THREAD_NUMBER_LIMIT = 100; 
    
    public static long EVENT014_FREE_MEMORY_LIMIT = 20000000;
    public static int EVENT014_THREAD_NUMBER_LIMIT = 100; 
    public static int EVENT014_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    // FRAME tests
    public static long FRAME001_FREE_MEMORY_LIMIT = 20000000;
    public static int FRAME001_THREAD_NUMBER_LIMIT = 100; 
    public static int FRAME001_RECURSION_NUMBER = 50;
    
    public static long FRAME002_FREE_MEMORY_LIMIT = 20000000;
    public static int FRAME002_THREAD_NUMBER_LIMIT = 100; 
    public static int FRAME002_RECURSION_NUMBER = 50;
    public static int FRAME002_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static long FRAME003_FREE_MEMORY_LIMIT = 20000000;
    public static int FRAME003_THREAD_NUMBER_LIMIT = 100; 
    public static int FRAME003_RECURSION_NUMBER = 50;
    public static int FRAME003_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static long FRAME004_FREE_MEMORY_LIMIT = 20000000;
    public static int FRAME004_THREAD_NUMBER_LIMIT = 100; 
    public static int FRAME004_RECURSION_NUMBER = 50;
    public static int FRAME004_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    // MEMORY tests
    public static int MEMORY001_ARRAY_DIMENSION = 100; 
   
    public static int MEMORY002_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 

    public static int MEMORY003_ARRAY_DIMENSION = 100; 
    public static int MEMORY003_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static int MEMORY004_SMALL_ARRAY_LENGTH = 100; 
    public static int MEMORY004_BIG_ARRAY_LENGTH = 10000; 
    public static long MEMORY004_FREE_MEMORY_LIMIT = 20000000; 
    
    public static int MEMORY005_SMALL_ARRAY_LENGTH = 100; 
    public static int MEMORY005_BIG_ARRAY_LENGTH = 10000; 
    public static long MEMORY005_FREE_MEMORY_LIMIT = 20000000; 

    public static long MEMORY006_FREE_MEMORY_LIMIT = 20000000; 
    public static int MEMORY006_THREAD_NUMBER_LIMIT = 100; 
    
    public static long MEMORY007_FREE_MEMORY_LIMIT = 20000000; 
    public static int MEMORY007_THREAD_NUMBER_LIMIT = 100; 
    public static int MEMORY007_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    // THREAD tests
    public static long THREAD002_FREE_MEMORY_LIMIT = 20000000; 
    public static int THREAD002_THREAD_NUMBER_LIMIT = 100; 
    
    public static int THREAD003_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 

    public static long THREAD004_FREE_MEMORY_LIMIT = 20000000; 
    public static int THREAD004_THREAD_NUMBER_LIMIT = 100; 
    public static int THREAD004_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static int THREAD005_STRESS_THREAD_NUMBER_LIMIT = 100;
    public static int THREAD005_ARRAY_SIZE_FOR_THREAD_STRESS = 1000000; 
    
    public static long THREAD006_FREE_MEMORY_LIMIT = 20000000; 
    public static int THREAD006_THREAD_NUMBER_LIMIT = 100; 
    public static int THREAD006_STRESS_THREAD_NUMBER_LIMIT = 100;
    public static int THREAD006_ARRAY_SIZE_FOR_THREAD_STRESS = 1000000; 
    
    public static long THREAD008_FREE_MEMORY_LIMIT = 20000000; 
    public static int THREAD008_THREAD_NUMBER_LIMIT = 100; 
    
    public static int THREAD009_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 

    public static long THREAD010_FREE_MEMORY_LIMIT = 20000000; 
    public static int THREAD010_THREAD_NUMBER_LIMIT = 100; 
    public static int THREAD010_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
    
    public static int THREAD011_STRESS_THREAD_NUMBER_LIMIT = 100;
    public static int THREAD011_ARRAY_SIZE_FOR_THREAD_STRESS = 1000000; 

    public static long THREAD012_FREE_MEMORY_LIMIT = 20000000; 
    public static int THREAD012_THREAD_NUMBER_LIMIT = 100; 
    public static int THREAD012_STRESS_THREAD_NUMBER_LIMIT = 100;
    public static int THREAD012_ARRAY_SIZE_FOR_THREAD_STRESS = 1000000; 

    // MIXED tests
    public static int MIXED001_ARRAY_DIMENSION = 50;
    public static int MIXED001_ARRAY_DIMENSION_FOR_MORE_CLASSES = 50;
    public static int MIXED001_THREADS_NUMBER = 100;
    public static long MIXED001_FREE_MEMORY_LIMIT = 20000000;
    
    
    public static int MIXED002_THREADS_NUMBER = 100;
    public static long MIXED002_FREE_MEMORY_LIMIT = 20000000;
    
    
    public static int MIXED003_THREADS_NUMBER = 100;
    public static int MIXED003_RECURSIVE_THREADS_NUMBER = 100;
    
    // REFTYPE tests
    public static int REFTYPE001_ARRAY_DIMENSION = 50;
    public static int REFTYPE001_ARRAY_DIMENSION_FOR_CLASSES = 50;
    public static int REFTYPE001_CLASSES_ARRAY_LENGTH = 1024;
    
    public static int REFTYPE002_ARRAY_DIMENSION = 50; 
    public static int REFTYPE002_ARRAY_DIMENSION_FOR_CLASSES = 50;
    public static int REFTYPE002_CLASSES_ARRAY_LENGTH = 1024;
    
    public static int REFTYPE003_ARRAY_DIMENSION = 50;
    public static int REFTYPE003_CLASSES_ARRAY_LENGTH = 1024;
    
    public static int REFTYPE004_ARRAY_DIMENSION = 50; 
    public static int REFTYPE004_CLASSES_ARRAY_LENGTH = 1024;
    
    // OBJECT tests
    public static long OBJECT001_FREE_MEMORY_LIMIT = 20000000; 
    public static int OBJECT001_THREAD_NUMBER_LIMIT = 100; 
    
    public static long OBJECT002_FREE_MEMORY_LIMIT = 20000000; 
    public static int OBJECT002_THREAD_NUMBER_LIMIT = 100; 
    
    public static long OBJECT003_FREE_MEMORY_LIMIT = 20000000; 
    public static int OBJECT003_THREAD_NUMBER_LIMIT = 100; 
    
    public static long OBJECT004_FREE_MEMORY_LIMIT = 20000000;
    public static int OBJECT004_ARRAY_LENGTH = 10000;
    
    public static long OBJECT005_FREE_MEMORY_LIMIT = 20000000;
    public static int OBJECT005_ARRAY_LENGTH = 10000;
    
    public static long OBJECT006_FREE_MEMORY_LIMIT = 20000000;
    public static int OBJECT006_ARRAY_LENGTH = 10000;
    
    public static long OBJECT007_FREE_MEMORY_LIMIT = 20000000;
    public static int OBJECT007_ARRAY_LENGTH = 10000;
    
    public static long OBJECT008_FREE_MEMORY_LIMIT = 20000000;
    public static int OBJECT008_ARRAY_LENGTH = 10000;
    
    public static long OBJECT009_FREE_MEMORY_LIMIT = 20000000;
    public static int OBJECT009_ARRAY_LENGTH = 10000;

    // END of configurational values

    static {
        if ( TRIAL_RUN ) {
            // configurational values
            // Below values for trial runs; values for actual runs - see declarations above
            
            // EVENT tests
            EVENT001_FREE_MEMORY_LIMIT = 20000000;
            EVENT001_THREAD_NUMBER_LIMIT = 4; 
            
            EVENT002_FREE_MEMORY_LIMIT = 20000000;
            EVENT002_THREAD_NUMBER_LIMIT = 4; 
            
            EVENT003_FREE_MEMORY_LIMIT = 20000000;
            EVENT003_THREAD_NUMBER_LIMIT = 4; 
            
            EVENT004_FREE_MEMORY_LIMIT = 20000000;
            EVENT004_THREAD_NUMBER_LIMIT = 4; 
            
            EVENT005_FREE_MEMORY_LIMIT = 20000000;
            EVENT005_THREAD_NUMBER_LIMIT = 4; 
            EVENT005_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            EVENT006_FREE_MEMORY_LIMIT = 20000000;
            EVENT006_THREAD_NUMBER_LIMIT = 4; 
            EVENT006_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            EVENT007_FREE_MEMORY_LIMIT = 20000000;
            EVENT007_THREAD_NUMBER_LIMIT = 4; 
            EVENT007_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            EVENT008_FREE_MEMORY_LIMIT = 20000000;
            EVENT008_THREAD_NUMBER_LIMIT = 4; 
            EVENT008_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            EVENT009_FREE_MEMORY_LIMIT = 20000000;
            EVENT009_THREAD_NUMBER_LIMIT = 4; 
            
            EVENT010_FREE_MEMORY_LIMIT = 20000000;
            EVENT010_THREAD_NUMBER_LIMIT = 4; 
            EVENT010_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
           
            EVENT011_FREE_MEMORY_LIMIT = 20000000;
            EVENT011_THREAD_NUMBER_LIMIT = 4; 
            
            EVENT012_FREE_MEMORY_LIMIT = 20000000;
            EVENT012_THREAD_NUMBER_LIMIT = 4; 
            EVENT012_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            EVENT013_FREE_MEMORY_LIMIT = 20000000;
            EVENT013_THREAD_NUMBER_LIMIT = 4; 
            
            EVENT014_FREE_MEMORY_LIMIT = 20000000;
            EVENT014_THREAD_NUMBER_LIMIT = 4; 
            EVENT014_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            // FRAME tests
            FRAME001_FREE_MEMORY_LIMIT = 20000000;
            FRAME001_THREAD_NUMBER_LIMIT = 4; 
            FRAME001_RECURSION_NUMBER = 10;
            
            FRAME002_FREE_MEMORY_LIMIT = 20000000;
            FRAME002_THREAD_NUMBER_LIMIT = 4; 
            FRAME002_RECURSION_NUMBER = 10;
            FRAME002_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            FRAME003_FREE_MEMORY_LIMIT = 20000000;
            FRAME003_THREAD_NUMBER_LIMIT = 4; 
            FRAME003_RECURSION_NUMBER = 10;
            FRAME003_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            FRAME004_FREE_MEMORY_LIMIT = 20000000;
            FRAME004_THREAD_NUMBER_LIMIT = 4; 
            FRAME004_RECURSION_NUMBER = 10;
            FRAME004_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            // MEMORY tests
            MEMORY001_ARRAY_DIMENSION = 2; 
           
            MEMORY002_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 

            MEMORY003_ARRAY_DIMENSION = 2; 
            MEMORY003_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            MEMORY004_SMALL_ARRAY_LENGTH = 100; 
            MEMORY004_BIG_ARRAY_LENGTH = 1000; 
            MEMORY004_FREE_MEMORY_LIMIT = 20000000; 
            
            MEMORY005_SMALL_ARRAY_LENGTH = 100; 
            MEMORY005_BIG_ARRAY_LENGTH = 1000; 
            MEMORY005_FREE_MEMORY_LIMIT = 20000000; 

            MEMORY006_FREE_MEMORY_LIMIT = 20000000; 
            MEMORY006_THREAD_NUMBER_LIMIT = 4; 
            
            MEMORY007_FREE_MEMORY_LIMIT = 20000000; 
            MEMORY007_THREAD_NUMBER_LIMIT = 4; 
            MEMORY007_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            // THREAD tests
            THREAD002_FREE_MEMORY_LIMIT = 20000000; 
            THREAD002_THREAD_NUMBER_LIMIT = 4; 
            
            THREAD003_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 

            THREAD004_FREE_MEMORY_LIMIT = 20000000; 
            THREAD004_THREAD_NUMBER_LIMIT = 4; 
            THREAD004_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            THREAD005_STRESS_THREAD_NUMBER_LIMIT = 4;
            THREAD005_ARRAY_SIZE_FOR_THREAD_STRESS = 1000000; 
            
            THREAD006_FREE_MEMORY_LIMIT = 20000000; 
            THREAD006_THREAD_NUMBER_LIMIT = 4; 
            THREAD006_STRESS_THREAD_NUMBER_LIMIT = 4;
            THREAD006_ARRAY_SIZE_FOR_THREAD_STRESS = 1000000; 
            
            THREAD008_FREE_MEMORY_LIMIT = 20000000; 
            THREAD008_THREAD_NUMBER_LIMIT = 4; 
            
            THREAD009_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 

            THREAD010_FREE_MEMORY_LIMIT = 20000000; 
            THREAD010_THREAD_NUMBER_LIMIT = 4; 
            THREAD010_ARRAY_SIZE_FOR_MEMORY_STRESS = 1000000; 
            
            THREAD011_STRESS_THREAD_NUMBER_LIMIT = 4;
            THREAD011_ARRAY_SIZE_FOR_THREAD_STRESS = 1000000; 

            THREAD012_FREE_MEMORY_LIMIT = 20000000; 
            THREAD012_THREAD_NUMBER_LIMIT = 4; 
            THREAD012_STRESS_THREAD_NUMBER_LIMIT = 4;
            THREAD012_ARRAY_SIZE_FOR_THREAD_STRESS = 1000000; 

            // MIXED tests
            MIXED001_ARRAY_DIMENSION = 2;
            MIXED001_ARRAY_DIMENSION_FOR_MORE_CLASSES = 2;
            MIXED001_THREADS_NUMBER = 4;
            MIXED001_FREE_MEMORY_LIMIT = 20000000;
                        
            MIXED002_THREADS_NUMBER = 4;
            MIXED002_FREE_MEMORY_LIMIT = 20000000;
                        
            MIXED003_THREADS_NUMBER = 4;
            MIXED003_RECURSIVE_THREADS_NUMBER = 4;
            
            // REFTYPE tests
            REFTYPE001_ARRAY_DIMENSION = 2;
            REFTYPE001_ARRAY_DIMENSION_FOR_CLASSES = 2;
            REFTYPE001_CLASSES_ARRAY_LENGTH = 1024;
            
            REFTYPE002_ARRAY_DIMENSION = 2; 
            REFTYPE002_ARRAY_DIMENSION_FOR_CLASSES = 2;
            REFTYPE002_CLASSES_ARRAY_LENGTH = 1024;
            
            REFTYPE003_ARRAY_DIMENSION = 2;
            REFTYPE003_CLASSES_ARRAY_LENGTH = 1024;
            
            REFTYPE004_ARRAY_DIMENSION = 2; 
            REFTYPE004_CLASSES_ARRAY_LENGTH = 1024;
            
            // OBJECT tests
            OBJECT001_FREE_MEMORY_LIMIT = 20000000; 
            OBJECT001_THREAD_NUMBER_LIMIT = 4; 
            
            OBJECT002_FREE_MEMORY_LIMIT = 20000000; 
            OBJECT002_THREAD_NUMBER_LIMIT = 4; 
            
            OBJECT003_FREE_MEMORY_LIMIT = 20000000; 
            OBJECT003_THREAD_NUMBER_LIMIT = 4; 
            
            OBJECT004_FREE_MEMORY_LIMIT = 20000000;
            OBJECT004_ARRAY_LENGTH = 1000;
            
            OBJECT005_FREE_MEMORY_LIMIT = 20000000;
            OBJECT005_ARRAY_LENGTH = 4;
            
            OBJECT006_FREE_MEMORY_LIMIT = 20000000;
            OBJECT006_ARRAY_LENGTH = 1000;
            
            OBJECT007_FREE_MEMORY_LIMIT = 20000000;
            OBJECT007_ARRAY_LENGTH = 1000;
            
            OBJECT008_FREE_MEMORY_LIMIT = 20000000;
            OBJECT008_ARRAY_LENGTH = 4;
            
            OBJECT009_FREE_MEMORY_LIMIT = 20000000;
            OBJECT009_ARRAY_LENGTH = 1000;
            
        }
    }

    protected final static String SIGNAL_FAILURE = "FAILURE";
    protected final static String SIGNAL_READY_01 = "READY_01";
    protected final static String SIGNAL_READY_02 = "READY_02";
    protected final static String SIGNAL_READY_03 = "READY_03";
    protected final static String SIGNAL_READY_04 = "READY_04";
    protected final static String SIGNAL_READY_05 = "READY_05";
    protected final static String SIGNAL_READY_06 = "READY_06";
    protected final static String SIGNAL_READY_07 = "READY_07";
    protected final static String SIGNAL_READY_08 = "READY_08";
    protected final static String SIGNAL_READY_09 = "READY_09";
    protected final static String SIGNAL_READY_10 = "READY_10";

    protected final int SUCCESS = 0;
    protected final int FAILURE = -1;

    private static String signalWithSuspendDebuggee = null;
    private static String signalWithSuspendThread = null;
    private static String signalWithoutSuspend = null;
    
    public static volatile int suspendedThreadsNumber = 0;
    
    protected final int YES = 0;
    protected final int NO = -1;

    // DEBUG_FLAG - turn on/off extra tests logging which may be
    // useful for tests runs evaluations
    protected int DEBUG_FLAG = YES;

    protected void printlnForDebug(String message) {
        if ( DEBUG_FLAG == YES ) {
            logWriter.println("---------> DEBUG info: " + message);
        }
    }
    
    static Object waitTimeObject = new Object();
    static public void waitMlsecsTime(long mlsecsTime) { 
        synchronized(waitTimeObject) {
            try {
                waitTimeObject.wait(mlsecsTime);
            } catch (Throwable throwable) {
                 // ignore
            }
        }
    }
    
    static public void waitMlsecsTime(long mlsecsTime, Object objectToWAit) { 
        synchronized(objectToWAit) {
            try {
                objectToWAit.wait(mlsecsTime);
            } catch (Throwable throwable) {
                 // ignore
            }
        }
    }
    
    private void signalWithSuspendDebuggeeMethod() {}

    protected void sendSignalAndWait(String message) {
        signalWithSuspendDebuggee = message;
        signalWithSuspendDebuggeeMethod();
    }

    private void signalWithSuspendThreadMethod() {}

    protected void sendThreadSignalAndWait(String message) {
        signalWithSuspendThread = message;
        signalWithSuspendThreadMethod();
    }

    private void signalWithoutSuspendMethod() {}

    protected void sendSignalAndContinue(String message) {
        signalWithoutSuspend = message;
        signalWithoutSuspendMethod();
    }
   
    private void suspendThreadByEventMethod() {};

    public void suspendThreadByEvent() {
        synchronized (this) {
            suspendedThreadsNumber = suspendedThreadsNumber + 1;
        }
        suspendThreadByEventMethod();
    }
   
    protected long[][] longArrayForCreatingMemoryStress = null;
    protected boolean isOutOfMemory = false;
    protected void createMemoryStress(int arrayLength_0, int arrayLength_1) {
        Runtime currentRuntime = Runtime.getRuntime();
        long freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> Debuggee: createMemoryStress: freeMemory (bytes) before memory stress = " + freeMemory);

        isOutOfMemory = false;
        int i = 0;
        long startTimeMlsec = System.currentTimeMillis();
        try {
            longArrayForCreatingMemoryStress = new long[arrayLength_0][];
            for (; i < longArrayForCreatingMemoryStress.length; i++) {
                longArrayForCreatingMemoryStress[i] = new long[arrayLength_1];
            }
            logWriter.println("--> Debuggee: createMemoryStress: NO OutOfMemoryError!!!");
        } catch ( OutOfMemoryError outOfMem ) {
            isOutOfMemory = true;
            logWriter.println("--> Debuggee: createMemoryStress: OutOfMemoryError!!!");
            printlnForDebug("createMemoryStress: OutOfMemoryError on loop step = " + i);
        }
        long createTimeMlsec = System.currentTimeMillis() - startTimeMlsec;
        printlnForDebug("createMemoryStress: expended time(mlsecs) = " + createTimeMlsec);
        freeMemory = currentRuntime.freeMemory();
        logWriter.println
        ("--> Debuggee: createMemoryStress: freeMemory after creating memory stress = " + freeMemory);
    }

    protected void printStackTraceToLogWriter(Throwable thrown, boolean forDebug) {
        StackTraceElement[] traceElems = thrown.getStackTrace();
        if ( forDebug ) {
            printlnForDebug("## StackTrace for " +  thrown);
        } else {
            logWriter.println("## StackTrace for " +  thrown);
        }
        for ( int i=0; i < traceElems.length; i++) {
            String currentElem = traceElems[i].toString();
            if ( forDebug ) {
                printlnForDebug("   " +  currentElem);
            } else {
                logWriter.println("   " +  currentElem);
            }
        }
    }

    protected void printStackTraceToLogWriter(Throwable thrown) {
        printStackTraceToLogWriter(thrown, false /* for debug parameter */);
    }

    /* (non-Javadoc)
     * @see jpda.share.Debuggee#run()
     */
    public void run() {        
    }    
    
}

