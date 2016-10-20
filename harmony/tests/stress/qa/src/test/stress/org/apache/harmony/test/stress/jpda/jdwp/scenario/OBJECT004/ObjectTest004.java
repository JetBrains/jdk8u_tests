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
 * @author Aleksander V. Budniy
 * @version $Revision: 1.2 $
 */

/**
 * Created on 16.09.2006 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT004;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;
import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;


/**
 * This tests case exercises the JDWP agent under ObjectIDs stressing. First
 * test generates array of Objects with very large length. Then runs
 * <code>ArrayReference.GetValues</code> command for this array, checks and
 * saves result. Runs <code>ArrayReference.GetValues</code> command for the
 * same array, checks if the same ObjectIDs are returned.
 */
public class ObjectTest004 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT004.ObjectDebuggee004";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public long[] allThreadIDs;
    int allThreadsNumber;

    /**
     * This tests case exercises the JDWP agent under ObjectIDs stressing. First
     * test generates array of Objects with very large length. Then runs
     * <code>ArrayReference.GetValues</code> command for this array, checks and
     * saves result. Runs <code>ArrayReference.GetValues</code> command for the
     * same array, checks if the same ObjectIDs are returned.
     */
    public Result  testObject004() {
        logWriter.println("==> testObject004: START (" + new Date() + ")...");

        if ( waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE ) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if ( setupSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }
        resumeDebuggee("#1");
try {
        printlnForDebug("receiving 'SIGNAL_READY_01' Signal from debuggee...");
        String debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        
        int testCaseStatus = SUCCESS;
        resumeDebuggee("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create big array of objects...");

        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        logWriter.println("==> Check array of objects...");
        long[] arrayForCompare1 = new long[StressDebuggee.OBJECT004_ARRAY_LENGTH];
        long[] arrayForCompare2 = new long[StressDebuggee.OBJECT004_ARRAY_LENGTH];
        if (checkArray("classArray", arrayForCompare1, DEBUGGEE_SIGNATURE) == -1) {
            testCaseStatus = FAILURE;
        } else {
            logWriter.println("==> Checking of array is PASSED");

            logWriter.println("==> Check the same array of objects...");
            if (checkArray("classArray", arrayForCompare2, DEBUGGEE_SIGNATURE) == -1) {
                testCaseStatus = FAILURE;
            } else {
                logWriter.println("==> Checking of the same array is PASSED");
                logWriter.println("==> Comparing results of two checking of array of objects...");
                if (compareArrays(arrayForCompare1, arrayForCompare2) != 0) {
                    testCaseStatus = FAILURE;
                } else {
                    logWriter.println("==> Comparing of results is PASSED");
                }
            }
        }
        
        resumeDebuggee("#4");
        
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testObject004: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testObject004: FAILED");
            return failed("testObject004:");
        } else {
            logWriter.println("==> testObject004: OK");
            return passed("testObject004: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testObject004: FAILED");
    return failed("==> testObject004: Unexpected Exception! ");
}
    }
    
    public static void main(String[] args) {
        System.exit(new ObjectTest004().test(args));
    }
}
