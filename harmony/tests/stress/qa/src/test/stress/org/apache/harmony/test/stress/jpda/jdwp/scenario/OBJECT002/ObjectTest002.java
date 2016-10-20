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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT002;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;


/**
 * This tests case exercises the JDWP agent under ObjectIDs stressing. First
 * test runs <code>VirtualMachine.AllThreads</code> command, creates separate
 * thread and run it vast number of time, runs
 * <code>VirtualMachine.AllThreads</code> command again, checks number of
 * running threads. Then it runs <code>VirtualMachine.DisposeObjects</code>
 * and disposes some number of ThreadIDs. For each thread test checks
 * ReferenceType, Name, and Status, and expects proper number of
 * INVALID_OBJECTID errors.
 */
public class ObjectTest002 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT002.ObjectDebuggee002";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public final static int TEST_NUMBER = 2;
    
    public long[] allThreadIDs;
    int allThreadsNumber;

    /**
     * This tests case exercises the JDWP agent under ObjectIDs stressing. First
     * test runs <code>VirtualMachine.AllThreads</code> command, creates separate
     * thread and run it vast number of time, runs
     * <code>VirtualMachine.AllThreads</code> command again, checks number of
     * running threads. Then it runs <code>VirtualMachine.DisposeObjects</code>
     * and disposes some number of ThreadIDs. For each thread test checks
     * ReferenceType, Name, and Status, and expects proper number of
     * INVALID_OBJECTID errors.
     */
    public Result  testObject002() {
        logWriter.println("==> testObject002: START (" + new Date() + ")...");

        if ( waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE ) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if ( setupSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }
        if ( setupThreadSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }
        resumeDebuggee("#1");
try {
        printlnForDebug("receiving 'SIGNAL_READY_01' Signal from debuggee...");
        String debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        logWriter.println("\n");
        logWriter.println("==> Send VirtualMachine.AllThreads at once after debuggee started...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllThreadsCommand);
        long commandStartTimeMlsec = System.currentTimeMillis();
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        long commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
        printlnForDebug("reply.getLength()= " + reply.getLength());

        int testCaseStatus = SUCCESS;

        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "VirtualMachine.AllThreads command") ) {
            testCaseStatus = FAILURE;
        } else {
            logWriter.println("\n");
            int threadsNumber = reply.getNextValueAsInt();
            logWriter.println("==> Received threads number = " + threadsNumber);
            try {
                for (int i = 0; i < threadsNumber; i++ ) {
                    long threadID = reply.getNextValueAsThreadID();
                }
            } catch ( Throwable thrown ) {
                logWriter.println("## FAILURE: Exception while reading thread IDs " + thrown);
                testCaseStatus = FAILURE;
            }
            if ( testCaseStatus != FAILURE ) {
                logWriter.println("==> VirtualMachine.AllThreads at once after debuggee started - OK");
            }
        }
        if ( testCaseStatus == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_02.001");
            return failed
            ("## FAILURE while performing VirtualMachine.AllThreads at once after debuggee started");
        }
        resumeDebuggee("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create and start very big number of threads...");

        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            String failureMessage = "## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee! ";
            if ( SIGNAL_FAILURE.equals(debuggeeSignal) ) {
                failureMessage = "## SIGNAL_FAILURE received from debuggee! ";
            }
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed(failureMessage);
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        logWriter.println("==> Get startedThreadsNumber value from debuggee...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_04");
           return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        int startedThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "startedThreadsNumber");
        if ( startedThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get startedThreadsNumber");
        }
        logWriter.println("==> startedThreadsNumber = " + startedThreadsNumber);
        if ( startedThreadsNumber == 0 ) {
            logWriter.println("==> WARNING: Debuggee could not start tested threads - test is cut off!");
            terminateDebuggee(FAILURE, "MARKER_05.001");
            return passed("Test is cut off: Debuggee could not start tested threads! ");
        }
        
        // Create array of expected threads names which are started
        String[] startedThreadsNames = new String[startedThreadsNumber];
        boolean[] startedThreadsFound = new boolean[startedThreadsNumber];
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsNames[i] = ObjectDebuggee002.THREAD_NAME_PATTERN + i;
            startedThreadsFound[i] = false;
        }
        
        logWriter.println("\n");
        logWriter.println("==> Send VirtualMachine.AllThreads after creating and starting " + 
            "very big number of threads in debuggee and check returned threads...");
        packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllThreadsCommand);
        try {
            commandStartTimeMlsec = System.currentTimeMillis();
            reply = debuggeeWrapper.vmMirror.performCommand(packet, currentTimeout());
            commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
            logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
            
        } catch ( Throwable thrown ) {
            logWriter.println("## FAILURE: Exception while performCommand() = " + thrown);
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## FAILURE: Exception while performCommand()");
        }
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        if ( checkReplyForError(reply, expectedErrors,
                               "VirtualMachine.AllThreads command") ) {
            terminateDebuggee(FAILURE, "MARKER_06.01");
            return failed("## VirtualMachine.AllThreads command ERROR");
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());
        if ( ! printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                                  "VirtualMachine.AllThreads command") ) {
            logWriter.println("\n");
            logWriter.println("==> Check if all expected threads are returned...");
            int checkStatus = SUCCESS;
            allThreadsNumber = reply.getNextValueAsInt();
            allThreadIDs = new long[startedThreadsNumber];
            logWriter.println("==> Received threads number = " + allThreadsNumber);
            logWriter.println("==> Started threads number = " + startedThreadsNumber);
            for (int i = 0; i < allThreadsNumber; i++ ) {
                long threadID = reply.getNextValueAsThreadID();
                long refType = debuggeeWrapper.vmMirror.getReferenceType(threadID);
                
                           
                int status = getStatus(threadID);
                if (status == -1) {
                    logWriter.println("## FAILURE: Status of thread is NOT returned");
                    checkStatus = FAILURE;
                }
                
                String threadName = getThreadName(threadID);
                for (int j = 0; j < startedThreadsNumber; j++ ) {
                    if ( startedThreadsNames[j].equals(threadName) ) {
                        startedThreadsFound[j] = true;
                        allThreadIDs[j] = threadID;

                        String source = getSource(refType);
                        if (!source.equals("ObjectDebuggee002.java")) {
                            logWriter.println("##FAILURE: received source of thread class = "+source+" instead of ObjectDebuggee002.java");
                            testCaseStatus = FAILURE;
                        }
                        
                        String signature = debuggeeWrapper.vmMirror.getClassSignature(refType);
                        if (!signature.equals("Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/OBJECT002/ObjectDebuggee002_Thread;")) {
                            logWriter.println("##FAILURE: received signature of thread class = "+signature+" instead of Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/OBJECT002/ObjectDebuggee002_Thread;");
                            testCaseStatus = FAILURE;
                        }
                    }
                }
            }
            for (int i = 0; i < startedThreadsNumber; i++ ) {
                if ( ! startedThreadsFound[i] ) {
                    logWriter.println("## FAILURE: Expected thread is NOT found out");
                    logWriter.println("##          Thread name  = " + startedThreadsNames[i]);
                    checkStatus = FAILURE;
                }
            }
            if ( checkStatus != FAILURE ) {
                logWriter.println("==> Check if all expected threads are returned - OK.");
            } else {
                testCaseStatus = FAILURE;
            }
        } else {
            logWriter.println
            ("==> OutOfMemory while running VirtualMachine.AllThreads command - Expected result!");
            terminateDebuggee(FAILURE, "MARKER_06.001");
            return passed
            ("==> OutOfMemory while running VirtualMachine.AllThreads command - Expected result!");
        }
        if ( testCaseStatus == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_06.002");
            return failed
            ("## FAILURE while cheking debuggee threads after creating and starting " + 
                "very big number of threads!");
        }
        
        int disposeObjects = startedThreadsNumber/2;
        if ( disposeObjects == 0 ) {
            disposeObjects = 1;
        }
        int result = disposeObj(disposeObjects, allThreadIDs);
        if (result == -1) {
            logWriter.println("## FAILURE: disposing objects - FAILED");
            testCaseStatus = FAILURE;
        } else {
            logWriter.println("==> Disposing objects - PASSED");
            result = checkThreads(allThreadIDs, startedThreadsNames, TEST_NUMBER, disposeObjects);
            if (result != 1) {
               logWriter.println("##FAILURE: during checking threads");
               testCaseStatus = FAILURE;
            } else {
                logWriter.println("==> Checking threads - PASSED");
            }
        }
        
        resumeDebuggee("#4");

        logWriter.println("\n");
        logWriter.println("==> Wait for finish of all threads in debuggee...");

        printlnForDebug("receiving 'SIGNAL_READY_03' Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        resumeDebuggee("#5");

        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testObject002: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testObject002: FAILED");
            return failed("testObject002:");
        } else {
            logWriter.println("==> testObject002: OK");
            return passed("testObject002: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testObject002: FAILED");
    return failed("==> testObject002: Unexpected Exception! ");
}
    }
    
    public static void main(String[] args) {
        System.exit(new ObjectTest002().test(args));
    }
}
