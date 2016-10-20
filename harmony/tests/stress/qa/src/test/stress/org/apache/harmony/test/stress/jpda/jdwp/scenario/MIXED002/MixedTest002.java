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
 * Created on 08.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED002;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;
import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.TaggedObject;
import org.apache.harmony.share.framework.jpda.jdwp.Value;


/**
 * This tests case exercises the JDWP agent under Thread and ObjectIDs
 * stressing. First, test generates array of Objects with very large length,
 * creates separate thread and starts it vast number of times. Each created
 * thread is suspended on proper event, test sends
 * <code>ClassType.InvokeMethod</code> command for <code>methodToInvoke</code>
 * method without waiting a reply. Then test runs
 * <code>ArrayReference.GetValues</code> command for created array of Objects,
 * saves and checks returned values. Then generates another array with the same
 * length but with null values, runs <code>ArrayReference.SetValues</code> and
 * fills the second array with values from the first array. Checks if both
 * arrays contains the same objects. Then checks replies for each
 * <code>ClassType.InvokeMethod</code> command.
 */
public class MixedTest002 extends StressTestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED002.MixedDebuggee002";

    public final static String DEBUGGEE_SIGNATURE = "L"
            + DEBUGGEE_CLASS_NAME.replace('.', '/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public static long[] threadIdArray = new long[StressDebuggee.MIXED002_THREADS_NUMBER];
    
    public long[] arrayForCompare1 = new long[MixedDebuggee002.MIXED002_ARRAY_LENGTH];
    public long[] arrayForCompare2 = new long[MixedDebuggee002.MIXED002_ARRAY_LENGTH];

    /**
     * This tests case exercises the JDWP agent under Thread and ObjectIDs
     * stressing. First, test generates array of Objects with very large length,
     * creates separate thread and starts it vast number of times. Each created
     * thread is suspended on proper event, test sends
     * <code>ClassType.InvokeMethod</code> command for
     * <code>methodToInvoke</code> method without waiting a reply. Then test
     * runs <code>ArrayReference.GetValues</code> command for created array of
     * Objects, saves and checks returned values. Then generates another array
     * with the same length but with null values, runs
     * <code>ArrayReference.SetValues</code> and fills the second array with
     * values from the first array. Checks if both arrays contains the same
     * objects. Then checks replies for each <code>ClassType.InvokeMethod</code>
     * command.
     */
    public Result testMixed002() {
        int testCaseStatus = SUCCESS;
        limitedPrintlnInit(20);

        logWriter.println("==> testMixed002: START (" + new Date() + ")...");
        try {
            wait(1000);
        } catch (Exception e) {
        }
        if (waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE) {
           return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if (setupSignalWithWait() == FAILURE) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }
        if ( setupThreadSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed("## FAILURE while setupThreadSignalWithWait.");
        }
        if ( setupSuspendThreadByEvent() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed("## FAILURE while setupSuspendThreadByEvent.");
        }
        resumeDebuggee("#1");
try {
        printlnForDebug("receiving 'SIGNAL_READY_01' Signal from debuggee...");
        String debuggeeSignal = receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_01.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

       
        resumeDebuggee("#2");
        logWriter.println("\n");
        logWriter
                .println("==> Wait for debuggee to create big array of objects...");
        debuggeeSignal = receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_02.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        
        logWriter.println("==> Resuming debuggee..");
                
        logWriter.println("\n");
        logWriter
                .println("==> Wait for debuggee to run very big number of threads...");
        resumeDebuggee("#3");
                
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        
        
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror
                .getClassID(DEBUGGEE_SIGNATURE);
        if (debuggeeRefTypeID == -1) {
            logWriter.println("## Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_07");
            return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID); 
        
        long toInvokeMethodID = debuggeeWrapper.vmMirror.getMethodID
        (debuggeeRefTypeID, MixedDebuggee002.METHOD_TO_INVOKE_NAME);
        if ( toInvokeMethodID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get toInvokeMethodID");
            terminateDebuggee(FAILURE, "MARKER_08");
            return failed("## Can NOT get toInvokeMethodID");
        }
        printlnForDebug("toInvokeMethodID = " + toInvokeMethodID);
        
        logWriter.println("==> Get startedThreadsNumber value from debuggee...");
        int startedThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "createdThreadsNumber");
        if ( startedThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed("## Can NOT get startedThreadsNumber");
        }
        logWriter.println("==> startedThreadsNumber = " + startedThreadsNumber);
        
        logWriter.println("==> Waiting for suspending by event of all started threads...");
        String[] startedThreadsNames = new String[startedThreadsNumber];
        long[] startedThreadsIDs = new long[startedThreadsNumber];
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsIDs[i] = waitForSuspendThreadByEvent(ANY_THREAD);
            if ( startedThreadsIDs[i] == FAILURE ) {
                logWriter.println("## FAILURE while waiting for suspending of all started threads!");
                terminateDebuggee(FAILURE, "MARKER_10");
                return failed("## FAILURE while waiting for suspending of all started threads!");
            }
            startedThreadsNames[i] = "Unknown Thread Name";
            try {
                startedThreadsNames[i] = debuggeeWrapper.vmMirror.getThreadName(startedThreadsIDs[i]);
            } catch (Throwable thrown ) {
                // ignore   
            }
        }
        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println("==> Time(mlsecs) of waiting for suspending of all started threads= " + 
                measurableCodeRunningTimeMlsec);
        
        int commandsToRun = startedThreadsNumber;
        CommandPacket[] commands = new CommandPacket[commandsToRun];
        logWriter.println("\n");
        logWriter.println("==> Send ClassType.InvokeMethod asynchronous commands (" + commandsToRun +
                ") without waiting for reply...");
        int sentCommands = 0;
        long commandStartTimeMlsec = System.currentTimeMillis();
        for (int i=0; i < commandsToRun; i++ ) {
            commands[i] = new CommandPacket(
                    JDWPCommands.ClassTypeCommandSet.CommandSetID,
                    JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
            commands[i].setNextValueAsClassID(debuggeeRefTypeID);
            commands[i].setNextValueAsThreadID(startedThreadsIDs[i]);
            commands[i].setNextValueAsMethodID(toInvokeMethodID);
            commands[i].setNextValueAsInt(2);  // args number
            long timeMlsecsToRun = 10;
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
            logWriter.println("## FAILURE: Can NOT send ClassType.InvokeMethod commands");
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed("## Can NOT send ClassType.InvokeMethod commands");
        }
        logWriter.println("==> Send ClassType.InvokeMethod asynchronous commands - Done");
        logWriter.println("==> Sent commands number = " + sentCommands);
        if ( sentCommands != commandsToRun ) {
            logWriter.println("==> Failures number while sending commands = " + (commandsToRun - sentCommands));
        }
        long commandsSendingTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> Commands sending time(mlsecs) = " + commandsSendingTimeMlsec);
        
        if(checkArray("classArray", arrayForCompare1, DEBUGGEE_SIGNATURE) == -1) {
            logWriter.println("## FAILURE while checkArray(...) #1");
            testCaseStatus = FAILURE;
        }

        logWriter.println("==> Resuming debuggee and waiting for creating array of objects with null values...");
        resumeSignalThread("#5");
        logWriter.println("receiving 'SIGNAL_READY_04' Signal from debuggee...");
        debuggeeSignal = receiveThreadSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_04.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_12");
            return failed("## FAILURE while receiving 'SIGNAL_READY_04' Thread Signal from debuggee");
        }

        logWriter.println("==> Set new array..");
        if ( setArray("anotherClassArray", arrayForCompare1, DEBUGGEE_SIGNATURE) == -1 ) {
            logWriter.println("## FAILURE while setArray(...)");
            testCaseStatus = FAILURE;
        } else {
            if (checkArray("anotherClassArray", arrayForCompare2, DEBUGGEE_SIGNATURE) == -1) {
                logWriter.println("## FAILURE while checkArray(...) #2");
                testCaseStatus = FAILURE;
            } else {
                logWriter.println("==> Compare objects in both arrays..");
                long startOfCompare = System.currentTimeMillis();
                if ( compareArrays(arrayForCompare1, arrayForCompare2) == -1 ) {
                    logWriter.println("## FAILURE while compareArrays(...)");
                    testCaseStatus = FAILURE;
                }
                long endOfCompare = System.currentTimeMillis();
                logWriter.println("==> Duration of comparing: " + (endOfCompare - startOfCompare));
            }
        }

        logWriter.println("\n");
        logWriter.println("==> Wait for replies for all sent commands and check results...");
        int receiveReplyStatus = SUCCESS;
        ReplyPacket reply = null;
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        long receiveReplyStartTimeMlsec = System.currentTimeMillis();
        int receivedReplies = 0;
        for (int i = 0; i < sentCommands ; i++ ) {
            reply = receveReply(commands[i].getId(), currentTimeout());
            
            if ( reply == null ) {
                limitedPrintln("## FAILURE: Can NOT receive reply for ClassType.InvokeMethod command: " +
                        "Command index = " + i);
                terminateDebuggee(FAILURE, "MARKER_13`");
                return failed
                ("## FAILURE while receiving reply for ClassType.InvokeMethod command");
            }
            receivedReplies++;
            if ( checkReplyForError(reply, expectedErrors,
                    "ClassType.InvokeMethod command (Command index = " + i + ")") ) {
                receiveReplyStatus = FAILURE;
                continue;
            }
            if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "ClassType.InvokeMethod command") ) {
                continue;   
            }
            Value returnValue = reply.getNextValueAsValue();
            if ( returnValue == null ) {
                limitedPrintln
                ("## FAILURE: ClassType.InvokeMethod command results in null returnValue: " +
                        "Command index = " + i);
                receiveReplyStatus = FAILURE;
            } else {
                int returnIntValue = returnValue.getIntValue();
                if ( returnIntValue != i ) {
                    limitedPrintln
                    ("## FAILURE: ClassType.InvokeMethod command results in unexpected int value: " +
                            "Command index = " + i);
                    limitedPrintln("##          Returned int value = " + returnIntValue);
                    limitedPrintln("##          Expected int value = " + i);
                    receiveReplyStatus = FAILURE;
                }
            }
            TaggedObject exception = reply.getNextValueAsTaggedObject();
            if ( exception == null ) {
                limitedPrintln
                ("## FAILURE: Can NOT get tagged-objectID for ClassType.InvokeMethod command: " +
                        "Command index = " + i);
                receiveReplyStatus = FAILURE;
            } else {
                long exceptionObjectID = exception.objectID;
                if ( exceptionObjectID != 0 ) {
                    limitedPrintln
                    ("## FAILURE: ClassType.InvokeMethod command results in unexpected exception: " +
                            "Command index = " + i);
                    limitedPrintln("##          exceptionObjectID = " + exceptionObjectID);
                    receiveReplyStatus = FAILURE;
                }
            }
        }
        logWriter.println
        ("==> received Replies number for ClassType.InvokeMethod = " + receivedReplies);
        if ( receiveReplyStatus == SUCCESS ) {
            logWriter.println
            ("==> OK - all ClassType.InvokeMethod asynchronous commands returned expected results");
        } else {
            testCaseStatus = FAILURE;   
        }
        long receiveReplyTimeMlsec = System.currentTimeMillis() - receiveReplyStartTimeMlsec;
        logWriter.println("==> Receiving replies time(mlsecs) = " + receiveReplyTimeMlsec);
        
        logWriter.println("==> Resuming debuggee..");
        resumeDebuggee("#5");
        
        
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testMixed002: running time(mlsecs) = "
                + testRunTimeMlsec);
        if (testCaseStatus == FAILURE) {
            logWriter.println("==> testMixed002: FAILED");
            return failed("testMixed002:");
        } else {
            logWriter.println("==> testMixed002: OK");
            return passed("testMixed002: OK");
        }
        
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testMixed002: FAILED");
    return failed("==> testMixed002: Unexpected Exception! ");
}
    }
           

    public static void main(String[] args) {
        System.exit(new MixedTest002().test(args));
    }
}