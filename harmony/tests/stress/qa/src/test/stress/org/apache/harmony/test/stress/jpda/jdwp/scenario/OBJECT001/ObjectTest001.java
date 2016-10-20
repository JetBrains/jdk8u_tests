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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT001;

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
 * running threads. For each thread test checks ReferenceType, Name, and Status.
 */
public class ObjectTest001 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT001.ObjectDebuggee001";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    public final static String THREAD_CLASS_SIGNATURE = 
        "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/OBJECT001/ObjectDebuggee001_Thread;";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }

    /**
     * This tests case exercises the JDWP agent under ObjectIDs stressing. First
     * test runs <code>VirtualMachine.AllThreads</code> command, creates separate
     * thread and run it vast number of time, runs
     * <code>VirtualMachine.AllThreads</code> command again, checks number of
     * running threads. For each thread test checks ReferenceType, Name, and Status.
     */
    public Result  testObject001() {
        logWriter.println("==> testObject001: START (" + new Date() + ")...");
        if ( waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE ) {
            return failed("## FAILURE while DebuggeeClassLoad.");
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
        logWriter.println("==> Send VirtualMachine.AllThreads just after debuggee started...");
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
                "VirtualMachine::AllThreads command") ) {
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
                logWriter.println("==> VirtualMachine.AllThreads just after debuggee started - OK");
            }
        }
        resumeSignalThread("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create and start very big number of threads...");

        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
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
        
        logWriter.println("==> Get threadRefTypeID...");
        long threadRefTypeID = debuggeeWrapper.vmMirror.getClassID(THREAD_CLASS_SIGNATURE);
        if ( threadRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get threadRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_07");
           return failed("## Can NOT get threadRefTypeID!");
        }
        logWriter.println("==> threadRefTypeID = " + threadRefTypeID);

        // Create array of expected threads names which are started
        String[] startedThreadsNames = new String[startedThreadsNumber];
        boolean[] startedThreadsFound = new boolean[startedThreadsNumber];
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsNames[i] = ObjectDebuggee001.THREAD_NAME_PATTERN + i;
            startedThreadsFound[i] = false;
        }
        
        logWriter.println("\n");
        logWriter.println("==> Send VirtualMachine.AllThreads after creating and starting " + 
                "very big number of threads in debuggee...");
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
            return failed("## FAILURE: Exception while performCommand()! ");
        }
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        if ( checkReplyForError(reply, expectedErrors,
                               "VirtualMachine::AllThreads command") ) {
            terminateDebuggee(FAILURE, "MARKER_06.1");
            return failed("## VirtualMachine::AllThreads command ERROR! ");
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());
        boolean isOutOfMemnory = false;
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        boolean toMeasure = false;
        if ( ! printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                                  "VirtualMachine::AllThreads command") ) {
            toMeasure = true;
            logWriter.println("\n");
            logWriter.println("==> Check if all expected threads are returned...");
            int allThreadsNumber = reply.getNextValueAsInt();
            limitedPrintlnInit(30);
            int foundErrors = 0;
            for (int i = 0; i < allThreadsNumber; i++ ) {
                if ( (foundErrors > 10) || (isOutOfMemnory) ) {
                    break;   
                }
                long threadID = reply.getNextValueAsThreadID();
                                
                long refType = 0;
                try {
                    refType = debuggeeWrapper.vmMirror.getReferenceType(threadID);
                } catch ( Throwable thrown ) {
                    foundErrors++;
                    boolean toPrn = limitedPrintln
                        ("## FAILURE: Exception while getting ReferenceType for checked thread = " + thrown);
                    if ( toPrn ) {
                        logWriter.println("##     threadID = " + threadID);
                    } else {
                        break;   
                    }
                }
                
                String threadName = getThreadName(threadID);
                if ( threadName == null ) {
                    foundErrors++;
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Can not get name for checked thread!");
                    if ( toPrn ) {
                        logWriter.println("##     threadID = " + threadID);
                    } else {
                        break;   
                    }
                }
                for (int j = 0; j < startedThreadsNumber; j++ ) {
                    if ( startedThreadsNames[j].equals(threadName) ) {
                        startedThreadsFound[j] = true;
                        if ( refType != threadRefTypeID ) {
                            logWriter.println("##FAILURE: Unexpected ReferenceTypeID for checked thread:");
                            logWriter.println("##         Thread name = " + threadName);
                            logWriter.println("##         Received ReferenceTypeID = " + refType);
                            logWriter.println("##         Expected ReferenceTypeID = " + threadRefTypeID);
                            foundErrors++;
                        }
                        String source = getSource(refType);
                        if (!source.equals("ObjectDebuggee001.java")) {
                            limitedPrintln("##FAILURE: received source of thread class = "+source+" instead of ObjectDebuggee001.java");
                            foundErrors++;
                        }
                        
                        String signature = null;
                        try {
                            signature = debuggeeWrapper.vmMirror.getClassSignature(refType);
                        } catch ( Throwable thrown ) {
                            // ignore
                        }
                        if (!signature.equals("Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/OBJECT001/ObjectDebuggee001_Thread;")) {
                            limitedPrintln("##FAILURE: received signature of thread class = "+signature+" instead of Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/OBJECT001/ObjectDebuggee001_Thread;");
                            foundErrors++;
                        }
                        
                        CommandPacket statusPacket = new CommandPacket(
                                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                                JDWPCommands.ThreadReferenceCommandSet.StatusCommand);
                        statusPacket.setNextValueAsThreadID(threadID);
                        ReplyPacket statusReply = performCommand(statusPacket);
                        if ( isException != null ) {
                            logWriter.println("## Command - ThreadReference.Status");
                            terminateDebuggee(FAILURE, "MARKER_02.0");
                            return failed("## FAILURE in ThreadReference.Status command! ");
                        }
                        if (checkReplyForError(statusReply, expectedErrors,
                                "ThreadReference.Status command", LIMITED_PRINT)) {
                            foundErrors++;
                            break;
                        }
                        if ( printExpectedError(statusReply, JDWPConstants.Error.OUT_OF_MEMORY,
                            "ThreadReference.Status command") ) {
                            isOutOfMemnory = true;
                            break;
                        }
                        int threadStatus = statusReply.getNextValueAsInt();
                        if ( threadStatus != JDWPConstants.ThreadStatus.RUNNING ) {
                            if ( threadStatus != JDWPConstants.ThreadStatus.WAIT ) {
                                logWriter.println("##FAILURE: Unexpected status for checked thread!");
                                logWriter.println("##     Thread name = " + startedThreadsNames[j]);
                                logWriter.println("##     Received status = " + threadStatus +
                                " (" + JDWPConstants.ThreadStatus.getName(threadStatus) + ")");
                                logWriter.println("##     Expecteed status: " + 
                                        JDWPConstants.ThreadStatus.RUNNING +
                                        " (" + JDWPConstants.ThreadStatus.getName(JDWPConstants.ThreadStatus.RUNNING) + "); " +
                                        JDWPConstants.ThreadStatus.WAIT +
                                        " (" + JDWPConstants.ThreadStatus.getName(JDWPConstants.ThreadStatus.WAIT) + "); "
                                );
                                foundErrors++;
                            }
                        }
                        int suspendStatus = statusReply.getNextValueAsInt();
                        if ( suspendStatus == JDWPConstants.SuspendStatus.SUSPEND_STATUS_SUSPENDED ) {
                            logWriter.println("##FAILURE: Unexpected suspend status for checked thread!");
                            logWriter.println("##     Thread name = " + startedThreadsNames[j]);
                            logWriter.println("##     Received suspend status = STATUS_SUSPENDED");
                            logWriter.println("##     Expected suspend status = NOT_SUSPENDED");
                            foundErrors++;
                        }
                    }
                }
            }
            if ( foundErrors > 0 ) {
                testCaseStatus = FAILURE;
            }
            foundErrors = 0;
            for (int i = 0; i < startedThreadsNumber; i++ ) {
                if ( foundErrors > 10 ) {
                    break;   
                }
                if ( !startedThreadsFound[i] ) {
                    logWriter.println("## FAILURE: Expected thread is NOT found out");
                    logWriter.println("##          Thread name  = " + startedThreadsNames[i]);
                    foundErrors++;
                }
            }
            if ( foundErrors > 0 ) {
                testCaseStatus = FAILURE;
            }
            if ( testCaseStatus != FAILURE ) {
                logWriter.println("==> OK - all expected threads are found out!");
            }
        }
        if ( toMeasure ) {
            long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
            logWriter.println
            ("==> Time(mlsecs) of checking for all expected threads = " + measurableCodeRunningTimeMlsec);
        }

        resumeSignalThread("#4");

        logWriter.println("\n");
        logWriter.println("==> Wait for finish of all threads in debuggee...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();

        printlnForDebug("receiving 'SIGNAL_READY_03' Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for finish of all threads in debuggee = " + 
                measurableCodeRunningTimeMlsec);

        resumeSignalThread("#5");

        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testObject001: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testObject001: FAILED");
            return failed("testObject001:");
        } else {
            logWriter.println("==> testObject001: OK");
            return passed("testObject001: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testObject001: FAILED");
    return failed("==> testObject001: Unexpected Exception! ");
}
    }

    public static void main(String[] args) {
        System.exit(new ObjectTest001().test(args));
    }
}
