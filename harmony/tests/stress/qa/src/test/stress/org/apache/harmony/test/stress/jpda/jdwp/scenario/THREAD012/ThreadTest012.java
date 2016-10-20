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
 * Created on 24.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD012;

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
  * This test case runs concurrently big number of JDWP command
  * ObjectReference.InvokeMethod - each command in the separate thread
  * and under threads stressing in debuggee.
  * The test expects that all commands result in expected returned value. 
 */
public class ThreadTest012 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.THREAD012.ThreadDebuggee012";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
      * This test case runs concurrently big number of JDWP command
      * ObjectReference.InvokeMethod - each command in the separate thread
      * and under threads stressing in debuggee.
      * The test expects that all commands result in expected returned value. 
     */
    public Result  testThread012() {
        logWriter.println("==> testThread012: START (" + new Date() + ")...");
        
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
        logWriter.println("==> Get debuggeeRefTypeID...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_03");
           return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        long toInvokeMethodID = debuggeeWrapper.vmMirror.getMethodID
            (debuggeeRefTypeID, ThreadDebuggee012.METHOD_TO_INVOKE_NAME);
        if ( toInvokeMethodID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get toInvokeMethodID");
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## Can NOT get toInvokeMethodID");
        }
        printlnForDebug("toInvokeMethodID = " + toInvokeMethodID);
        
        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create and start big number of threads...");
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_01' Thread Signal from debuggee...");
        String debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for debuggee to start all threads = " + measurableCodeRunningTimeMlsec);

        logWriter.println("==> Get startedThreadsNumber value from debuggee...");
        int startedThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "startedThreadsNumber");
        if ( startedThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## Can NOT get startedThreadsNumber");
        }
        logWriter.println("==> startedThreadsNumber = " + startedThreadsNumber);
        
        logWriter.println("==> Waiting for suspending by event of all started threads...");
        String[] startedThreadsNames = new String[startedThreadsNumber];
        long[] startedThreadsIDs = new long[startedThreadsNumber];
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsIDs[i] = waitForSuspendThreadByEvent(ANY_THREAD);
            if ( startedThreadsIDs[i] == FAILURE ) {
                logWriter.println("## FAILURE while waiting for suspending of all started threads!");
                terminateDebuggee(FAILURE, "MARKER_07");
                return failed("## FAILURE while waiting for suspending of all started threads!");
            }
            startedThreadsNames[i] = "Unknown Thread Name";
            try {
                startedThreadsNames[i] = debuggeeWrapper.vmMirror.getThreadName(startedThreadsIDs[i]);
            } catch (Throwable thrown ) {
                // ignore   
            }
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println("==> Time(mlsecs) of waiting for suspending of all started threads= " + 
                measurableCodeRunningTimeMlsec);
        
        resumeSignalThread("#2");
        
        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create threads stress...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_02' Thread Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_08");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for debuggee to create threads stress = " + measurableCodeRunningTimeMlsec);

        int commandsToRun = startedThreadsNumber;
        CommandPacket[] commands = new CommandPacket[commandsToRun];
        logWriter.println("\n");
        logWriter.println("==> Send ObjectReference.InvokeMethod asynchronous commands (" + commandsToRun +
                ") without waiting for reply...");
        String ObjectIDFieldName = "threadDebuggee012This";
        long methodObjectID = getObjectIDValueForStaticField(debuggeeRefTypeID, ObjectIDFieldName);
        if ( methodObjectID == BAD_OBJECT_ID ) {
            logWriter.println("## FAILURE: Can NOT get ObjectID of method to invoke");
            terminateDebuggee(FAILURE, "MARKER_08.1");
            return failed("## Can NOT get ObjectID of method to invoke!");
        }
        printlnForDebug("ObjectID of method to invoke = " + methodObjectID);
        int sentCommands = 0;
        long commandStartTimeMlsec = System.currentTimeMillis();
        for (int i=0; i < commandsToRun; i++ ) {
            commands[i] = new CommandPacket(
                    JDWPCommands.ObjectReferenceCommandSet.CommandSetID,
                    JDWPCommands.ObjectReferenceCommandSet.InvokeMethodCommand);
            commands[i].setNextValueAsObjectID(methodObjectID);
            commands[i].setNextValueAsThreadID(startedThreadsIDs[i]);
            commands[i].setNextValueAsClassID(debuggeeRefTypeID);
            commands[i].setNextValueAsMethodID(toInvokeMethodID);
            commands[i].setNextValueAsInt(2);  // args number
            long timeMlsecsToRun = 10;
            commands[i].setNextValueAsValue(new Value(timeMlsecsToRun));
            int valueToReturn = i;
            commands[i].setNextValueAsValue(new Value(valueToReturn));
            commands[i].setNextValueAsInt(JDWPConstants.InvokeOptions.INVOKE_SINGLE_THREADED);
            int commandID = sendCommand(commands[i]);
            if ( commandID == FAILURE ) {
                logWriter.println(">>> WARNING: Can NOT send ObjectReference.InvokeMethod command: " +
                        "Command index = " + i);
                break;
            }
            sentCommands++;
        }
        if ( sentCommands == 0 ) {
            logWriter.println("## FAILURE: Can NOT send ObjectReference.InvokeMethod commands");
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed("## Can NOT send ObjectReference.InvokeMethod commands");
        }
        logWriter.println("==> Send ObjectReference.InvokeMethod asynchronous commands - Done");
        logWriter.println("==> Sent commands number = " + sentCommands);
        if ( sentCommands != commandsToRun ) {
            logWriter.println("==> Failures number while sending commands = " + (commandsToRun - sentCommands));
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
                ("## FAILURE: Can NOT receive reply for ObjectReference.InvokeMethod command: " +
                        "Command index = " + i);
                terminateDebuggee(FAILURE, "MARKER_09.0.1`");
                return failed
                ("## FAILURE while receiving reply for ObjectReference.InvokeMethod command");
            }
            receivedReplies++;
            if ( checkReplyForError(reply, expectedErrors,
                    "ObjectReference.InvokeMethod command (Command index = " + i + ")", LIMITED_PRINT) ) {
                testCaseStatus = FAILURE;
                continue;
            }
            if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "ObjectReference.InvokeMethod command") ) {
                outOfMemory = true;
                break;
            }
            Value returnValue = reply.getNextValueAsValue();
            if ( returnValue == null ) {
                limitedPrintln
                ("## FAILURE: ObjectReference.InvokeMethod command results in null returnValue: " +
                        "Command index = " + i);
                testCaseStatus = FAILURE;
            } else {
                int returnIntValue = returnValue.getIntValue();
                if ( returnIntValue != i ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: ObjectReference.InvokeMethod command results in unexpected int value: " +
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
                ("## FAILURE: Can NOT get tagged-objectID for ObjectReference.InvokeMethod command: " +
                        "Command index = " + i);
                testCaseStatus = FAILURE;
            } else {
                long exceptionObjectID = exception.objectID;
                if ( exceptionObjectID != 0 ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: ObjectReference.InvokeMethod command results in unexpected exception: " +
                            "Command index = " + i);
                    if ( toPrn ) {
                        logWriter.println("##          exceptionObjectID = " + exceptionObjectID);
                    }
                    testCaseStatus = FAILURE;
                }
            }
        }
        logWriter.println
        ("==> received Replies number for ObjectReference.InvokeMethod = " + receivedReplies);
        if ( outOfMemory ) {
            if ( testCaseStatus == FAILURE ) {
                terminateDebuggee(FAILURE, "MARKER_09.1");
                return failed
                ("## FAILURE while receiving replies for ObjectReference.InvokeMethod commands! ");
            } else {
                terminateDebuggee(SUCCESS, "MARKER_9.2");
                return passed
                ("==> OutOfMemory while receiving replies for ObjectReference.InvokeMethod commands - Expected result!");
            }
        }
        if ( testCaseStatus == SUCCESS ) {
            logWriter.println
            ("==> OK - all ObjectReference.InvokeMethod asynchronous commands returned expected results");
        }
        long receiveReplyTimeMlsec = System.currentTimeMillis() - receiveReplyStartTimeMlsec;
        logWriter.println("==> Receiving replies time(mlsecs) = " + receiveReplyTimeMlsec);
        
        resumeDebuggee("#3");

        logWriter.println("\n");
        logWriter.println("==> Wait for finish of all started threads in debuggee...");

        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_03' Thread Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_10");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println("==> Time(mlsecs) of waiting for finish of all started threads in debuggee= " + 
                measurableCodeRunningTimeMlsec);

        resumeDebuggee("#4");

        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testThread012: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testThread012: FAILED");
            return failed("testThread012:");
        } else {
            logWriter.println("==> testThread012: OK");
            return passed("testThread012: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testThread012: FAILED");
    return failed("==> testThread012: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new ThreadTest012().test(args));
	}
}
