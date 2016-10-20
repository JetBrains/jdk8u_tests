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
 * Created on 06.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD005;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Value;
import org.apache.harmony.share.framework.jpda.jdwp.TaggedObject;


/**
 * This test case runs concurrently big number of JDWP command <code>ClassType.InvokeMethod</code>
 * in the same thread under threads stressing in debuggee.
 * The test expects that all commands result in expected returned value. 
 */
public class ThreadTest005 extends StressTestCase {

    final int COMMANDS_TO_RUN = THREAD005_COMMANDS_TO_RUN ;
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD005.ThreadDebuggee005";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case runs concurrently big number of JDWP command <code>ClassType.InvokeMethod</code>
     * in the same thread under treads stressing in debuggee.
     * The test expects that all commands result in expected returned value. 
     */
    public Result  testThread005() {
        logWriter.println("==> testThread005: START (" + new Date() + ")...");
        
        if ( waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE ) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if ( setupThreadSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupThreadSignalWithWait.");
        }
        if ( setupSuspendThreadByEvent() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed("## FAILURE while setupSuspendThreadByEvent.");
        }
        resumeDebuggee("#1");
try {
        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create and start thread...");
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_01' Thread Signal from debuggee...");
        String debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for debuggee to start thread = " + measurableCodeRunningTimeMlsec);

        printlnForDebug("waiting for suspend thread by event for ClassType.InvokeMethod command...");
        long toInvokeMethodThreadID =  waitForSuspendThreadByEvent(ANY_THREAD);
        if ( toInvokeMethodThreadID == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed
            ("## FAILURE while waiting for suspend thread by event");
        }
        printlnForDebug("toInvokeMethodThreadID = " + toInvokeMethodThreadID);

        logWriter.println("==> Get debuggeeRefTypeID...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_05");
           return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        long toInvokeMethodID = debuggeeWrapper.vmMirror.getMethodID
            (debuggeeRefTypeID, ThreadDebuggee005.METHOD_TO_INVOKE_NAME);
        if ( toInvokeMethodID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get toInvokeMethodID");
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## Can NOT get toInvokeMethodID");
        }
        printlnForDebug("toInvokeMethodID = " + toInvokeMethodID);
        
        resumeSignalThread("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create threads stress...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_02' Thread Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_07");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for debuggee to create threads stress = " + 
                measurableCodeRunningTimeMlsec);

        CommandPacket[] commands = new CommandPacket[COMMANDS_TO_RUN];
        logWriter.println("\n");
        logWriter.println("==> Send ClassType.InvokeMethod asynchronous commands (" + COMMANDS_TO_RUN +
                ") without waiting for reply...");
        int sentCommands = 0;
        long commandStartTimeMlsec = System.currentTimeMillis();
        int methodToInvokeCallNumber = -1;
        for (int i=0; i < COMMANDS_TO_RUN; i++ ) {
            long trialCount = 0;
            while ( true ) {
                trialCount++;
                long currentTimeout = currentTimeout();
                try {
                    methodToInvokeCallNumber = 
                        getIntValueForStaticField(debuggeeRefTypeID, "methodToInvokeCallNumber");
                } catch ( Throwable thrown ) {
                    logWriter.println
                    ("## FAILURE: Exception in getIntValueForStaticField() method: " + thrown);
                    logWriter.println
                    ("##          command index = " + i);
                    logWriter.println
                    ("##          trialCount = " + trialCount);
                    logWriter.println
                    ("##          methodToInvokeCallNumber = " + methodToInvokeCallNumber);
                    logWriter.println
                    ("##          currentTimeout = " + currentTimeout);
                    printStackTraceToLogWriter(thrown, FOR_DEBUG);
                    terminateDebuggee(FAILURE, "MARKER_05.3");
                    return failed
                    ("## FAILURE Can NOT get value for 'methodToInvokeCallNumber' variable from debuggee! ");
                }
                if ( methodToInvokeCallNumber == BAD_INT_VALUE ) {
                    logWriter.println("## FAILURE: Can NOT get value for 'methodToInvokeCallNumber' variable from debuggee!");
                    terminateDebuggee(FAILURE, "MARKER_07.1");
                    return failed
                    ("## FAILURE Can NOT get value for 'methodToInvokeCallNumber' variable from debuggee! ");
                }
                if ( i == methodToInvokeCallNumber ) {
                    break;  
                }
            }
            if ( waitForThreadSuspendedStatus(toInvokeMethodThreadID) == FAILURE ) {
                logWriter.println(">>> WARNING: Can NOT waitForThreadSuspendedStatus: " +
                        "Command index = " + i);
                break;
            }
            commands[i] = new CommandPacket(
                    JDWPCommands.ClassTypeCommandSet.CommandSetID,
                    JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
            commands[i].setNextValueAsClassID(debuggeeRefTypeID);
            commands[i].setNextValueAsThreadID(toInvokeMethodThreadID);
            commands[i].setNextValueAsMethodID(toInvokeMethodID);
            commands[i].setNextValueAsInt(2);  // args number
            long timeMlsecsToRun = 1;
            commands[i].setNextValueAsValue(new Value(timeMlsecsToRun));
            int valueToReturn = i;
            commands[i].setNextValueAsValue(new Value(valueToReturn));
            commands[i].setNextValueAsInt(JDWPConstants.InvokeOptions.INVOKE_SINGLE_THREADED);
            int commandID = sendCommand(commands[i]);
            if ( commandID == FAILURE ) {
                logWriter.println(">>> WARNING: Can NOT send ClassType.InvokeMethod command: " +
                        "Command index = " + i);
                break;
            }
            sentCommands++;
        }
        if ( sentCommands == 0 ) {
            if ( isOutOfMemory ) {
                logWriter.println(">>> WARNING: OutOfMemory in JDWP Agent - Expected result!");
                terminateDebuggee(SUCCESS, "MARKER_08");
                return passed(">>> OutOfMemory in JDWP Agent - Expected result!");
            } else {
                logWriter.println("## FAILURE: Can NOT send ClassType.InvokeMethod commands");
                terminateDebuggee(FAILURE, "MARKER_08");
                return failed("## Can NOT send ClassType.InvokeMethod commands");
            }
        }
        logWriter.println("==> Send ClassType.InvokeMethod asynchronous commands - Done");
        logWriter.println("==> Sent commands number = " + sentCommands);
        if ( sentCommands != COMMANDS_TO_RUN ) {
            logWriter.println("==> Failures number while sending commands = " + (COMMANDS_TO_RUN - sentCommands));
        }
        long commandsSendingTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> Commands sending time(mlsecs) = " + commandsSendingTimeMlsec);

        logWriter.println("\n");
        logWriter.println("==> Wait for replies for all sent commands and check results...");
        int testCaseStatus = SUCCESS;
        ReplyPacket reply = null;
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        long receiveReplyStartTimeMlsec = System.currentTimeMillis();
        limitedPrintlnInit(20);
        boolean outOfMemory = false;
        int receivedReplies = 0;
        for (int i=0; i < sentCommands; i++ ) {
            reply = receveReply(commands[i].getId(), currentTimeout());
            if ( reply == null ) {
                logWriter.println
                ("## FAILURE: Can NOT receive reply for ClassType.InvokeMethod command: " +
                        "Command index = " + i);
                terminateDebuggee(FAILURE, "MARKER_09");
                return failed
                ("## FAILURE while receiving reply for ClassType.InvokeMethod command");
            }
            receivedReplies++;
            if ( checkReplyForError(reply, expectedErrors,
                    "ClassType.InvokeMethod command (Command index = " + i + ")", LIMITED_PRINT) ) {
                testCaseStatus = FAILURE;
                continue;
            }
            if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "ClassType.InvokeMethod command") ) {
                outOfMemory = true;
                break;
            }
            Value returnValue = reply.getNextValueAsValue();
            if ( returnValue == null ) {
                limitedPrintln
                ("## FAILURE: ClassType.InvokeMethod command results in null returnValue: " +
                        "Command index = " + i);
                testCaseStatus = FAILURE;
            } else {
                int returnIntValue = returnValue.getIntValue();
                if ( returnIntValue != i ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: ClassType.InvokeMethod command results in unexpected int value: " +
                            "Command index = " + i);
                    if ( toPrn ) {
                        logWriter.println("##          Returned int value = " + returnIntValue);
                        logWriter.println("##          Expected int value = " + i);
                    }
                    testCaseStatus = FAILURE;
                }
            }

            TaggedObject exception = reply.getNextValueAsTaggedObject();
            if ( exception == null ) {
                limitedPrintln
                ("## FAILURE: Can NOT get tagged-objectID for ClassType.InvokeMethod command: " +
                        "Command index = " + i);
                testCaseStatus = FAILURE;
            } else {
                long exceptionObjectID = exception.objectID;
                if ( exceptionObjectID != 0 ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: ClassType.InvokeMethod command results in unexpected exception: " +
                            "Command index = " + i);
                    if ( toPrn ) {
                        logWriter.println("##          exceptionObjectID = " + exceptionObjectID);
                    }
                    testCaseStatus = FAILURE;
                }
            }
        }
        logWriter.println
        ("==> received Replies number for ClassType.InvokeMethod = " + receivedReplies);
        if ( outOfMemory ) {
            if ( testCaseStatus == FAILURE ) {
                terminateDebuggee(FAILURE, "MARKER_09.1");
                return failed
                ("## FAILURE while receiving replies for ClassType.InvokeMethod commands! ");
            } else {
                terminateDebuggee(SUCCESS, "MARKER_9.2");
                return passed
                ("==> OutOfMemory while receiving replies for ClassType.InvokeMethod commands - Expected result!");
            }
        }
        if ( testCaseStatus == SUCCESS ) {
            logWriter.println
            ("==> OK - all ClassType.InvokeMethod asynchronous commands returned expected results");
        }
        long receiveReplyTimeMlsec = System.currentTimeMillis() - receiveReplyStartTimeMlsec;
        logWriter.println("==> Receiving replies time(mlsecs) = " + receiveReplyTimeMlsec);
        
        resumeDebuggee("#3");

        logWriter.println("\n");
        logWriter.println("==> Wait for finish of thread in debuggee...");

        printlnForDebug("receiving 'SIGNAL_READY_03' Thread Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait();
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_10");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);

        resumeDebuggee("#4");

        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testThread005: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testThread005: FAILED");
            return failed("testThread005:");
        } else {
            logWriter.println("==> testThread005: OK");
            return passed("testThread005: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testThread005: FAILED");
    return failed("==> testThread005: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new ThreadTest005().test(args));
	}
}
