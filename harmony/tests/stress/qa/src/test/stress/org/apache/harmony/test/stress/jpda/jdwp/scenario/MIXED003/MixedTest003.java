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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED003;

import java.util.List;
import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Frame;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.Location;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.TaggedObject;
import org.apache.harmony.share.framework.jpda.jdwp.Value;
import org.apache.harmony.share.framework.jpda.jdwp.Frame.Variable;


/**
 * This tests case exercises the JDWP agent under Thread and FrameIDs stressing.
 * First, test creates separate thread and starts it vast number of times. Each
 * created thread runs recursive method some number of times, then it is
 * suspended on proper event, test sends <code>ClassType.InvokeMethod</code>
 * command for <code>methodToInvoke</code> method without waiting a reply.
 * Then test runs <code>ThreadReference.Frames</code> command for each thread
 * with recursive method. Checks if all FrameIDs have unique values, each
 * location contains expected ClassID and MethodID,
 * <code>StackTrace.ThisObject</code> returns proper ObjectID. Then checks
 * replies for each <code>ClassType.InvokeMethod</code> command.
 */
public class MixedTest003 extends StressTestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED003.MixedDebuggee003";

    public final static String DEBUGGEE_SIGNATURE = "L"
            + DEBUGGEE_CLASS_NAME.replace('.', '/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }

    public final static String THREAD_CLASS_NAME = "org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED003.Mixed003_TestThread";

    public final static String THREAD_CLASS_SIGNATURE = "L"
            + THREAD_CLASS_NAME.replace('.', '/') + ";";

   
    public static long[] recursiveThreadsIDs;
    public static String[] recursiveThreadsNames;

    private long[] frameIdArray;

    /**
     * This tests case exercises the JDWP agent under Thread and FrameIDs stressing.
     * First, test creates separate thread and starts it vast number of times. Each
     * created thread runs recursive method some number of times, then it is
     * suspended on proper event, test sends <code>ClassType.InvokeMethod</code>
     * command for <code>methodToInvoke</code> method without waiting a reply.
     * Then test runs <code>ThreadReference.Frames</code> command for each thread
     * with recursive method. Checks if all FrameIDs have unique values, each
     * location contains expected ClassID and MethodID,
     * <code>StackTrace.ThisObject</code> returns proper ObjectID. Then checks
     * replies for each <code>ClassType.InvokeMethod</code> command.
     */
    public Result testMixed003() {
        int testCaseStatus = SUCCESS;
        limitedPrintlnInit(20);

        logWriter.println("==> testMixed003: START (" + new Date() + ")...");

        if (waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if (setupSignalWithWait() == FAILURE) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }
        if (setupThreadSignalWithWait() == FAILURE) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed("## FAILURE while setupThreadSignalWithWait.");
        }
        if (setupSuspendThreadByEvent() == FAILURE) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed("## FAILURE while setupSuspendThreadByEvent.");
        }
        resumeDebuggee("#1");
try {
        logWriter
                .println("==> Wait for debuggee to run very big number of threads...");
        resumeDebuggee("#2");

        String debuggeeSignal = receiveThreadSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_01.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## FAILURE while receiving 'SIGNAL_READY_01' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);

        long debuggeeRefTypeID = debuggeeWrapper.vmMirror
                .getClassID(DEBUGGEE_SIGNATURE);
        if (debuggeeRefTypeID == -1) {
            logWriter.println("## Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        long toInvokeMethodID = debuggeeWrapper.vmMirror.getMethodID(
                debuggeeRefTypeID, MixedDebuggee003.METHOD_TO_INVOKE_NAME);
        if (toInvokeMethodID == -1) {
            logWriter.println("## FAILURE: Can NOT get toInvokeMethodID");
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## Can NOT get toInvokeMethodID");
        }
        printlnForDebug("toInvokeMethodID = " + toInvokeMethodID);

        logWriter.println("==> Get startedThreadsNumber value from debuggee...");
        int startedThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "createdThreadsNumber");
        if ( startedThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_07");
            return failed("## Can NOT get startedThreadsNumber");
        }
        logWriter.println("==> startedThreadsNumber = " + startedThreadsNumber);
        
        logWriter.println("==> Get startedRecursiveThreadsNumber value from debuggee...");
        int startedRecursiveThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "createdRecursiveThreadsNumber");
        if ( startedRecursiveThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedRecursiveThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_08");
            return failed("## Can NOT get startedRecursiveThreadsNumber");
        }
        logWriter.println("==> startedRecursiveThreadsNumber = " + startedRecursiveThreadsNumber);
        
        logWriter.println("==> Waiting for suspending by event of all started threads...");
        String[] startedThreadsNames = new String[startedThreadsNumber];
        long[] startedThreadsIDs = new long[startedThreadsNumber];
        recursiveThreadsIDs = new long[startedRecursiveThreadsNumber];
        recursiveThreadsNames = new String[startedRecursiveThreadsNumber];
        frameIdArray = new long[startedRecursiveThreadsNumber * 20];
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        int threadIndex = 0;
        int recursiveThreadIndex = 0;
        for (int i = 0; i < startedThreadsNumber + startedRecursiveThreadsNumber; i++) {
            long threadsID = waitForSuspendThreadByEvent(ANY_THREAD);
            if (threadsID == FAILURE) {
                logWriter.println("## FAILURE while waiting for suspending of all started threads!");
                logWriter.println("##         Thread index = " + i);
                terminateDebuggee(FAILURE, "MARKER_09");
                return failed("## FAILURE while waiting for suspending of all started threads! ");
            }
            String threadName = null;
            try {
                threadName = debuggeeWrapper.vmMirror.getThreadName(threadsID);
            } catch (Throwable thrown) {
                logWriter.println("## FAILURE while getting name for started thread!");
                logWriter.println("##         Thread index = " + i);
                terminateDebuggee(FAILURE, "MARKER_10");
                return failed("## FAILURE while getting name for started thread! ");
            }
            if ( threadName.startsWith("recursiveThread") ) {
                recursiveThreadsIDs[recursiveThreadIndex] = threadsID;
                recursiveThreadsNames[recursiveThreadIndex] = threadName;
                recursiveThreadIndex++;
            } else {
                startedThreadsIDs[threadIndex] = threadsID;
                startedThreadsNames[threadIndex] = threadName;
                threadIndex++;
            }
        }
        long measurableCodeRunningTimeMlsec = System.currentTimeMillis()
                - measurableCodeStartTimeMlsec;
        logWriter
                .println("==> Time(mlsecs) of waiting for suspending of all started threads= "
                        + measurableCodeRunningTimeMlsec);

        int commandsToRun = startedThreadsNumber;
        CommandPacket[] commands = new CommandPacket[commandsToRun];
        logWriter.println("\n");
        logWriter
                .println("==> Send ClassType.InvokeMethod asynchronous commands ("
                        + commandsToRun + ") without waiting for reply...");
        int sentCommands = 0;
        long commandStartTimeMlsec = System.currentTimeMillis();
        for (int i = 0; i < commandsToRun; i++) {
            commands[i] = new CommandPacket(
                    JDWPCommands.ClassTypeCommandSet.CommandSetID,
                    JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
            commands[i].setNextValueAsClassID(debuggeeRefTypeID);
            commands[i].setNextValueAsThreadID(startedThreadsIDs[i]);
            commands[i].setNextValueAsMethodID(toInvokeMethodID);
            commands[i].setNextValueAsInt(2); // args number
            long timeMlsecsToRun = 100;
            commands[i].setNextValueAsValue(new Value(timeMlsecsToRun));
            int valueToReturn = i;
            commands[i].setNextValueAsValue(new Value(valueToReturn));
            commands[i]
                    .setNextValueAsInt(JDWPConstants.InvokeOptions.INVOKE_SINGLE_THREADED);
            int commandID = sendCommand(commands[i]);
            if (commandID == FAILURE) {
                logWriter
                        .println(">>> WARNING: Can NOT send ClassType.InvokeMethod command: "
                                + "Command index = " + i);
                break;
            }
            sentCommands++;
        }
        if (sentCommands == 0) {
            logWriter
                    .println("## FAILURE: Can NOT send ClassType.InvokeMethod commands");
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed("## Can NOT send ClassType.InvokeMethod commands");
        }
        logWriter
                .println("==> Send ClassType.InvokeMethod asynchronous commands - Done");
        logWriter.println("==> Sent commands number = " + sentCommands);
        if (sentCommands != commandsToRun) {
            logWriter.println("==> WARNING: Failures number while sending commands = "
                    + (commandsToRun - sentCommands));
        }
        long commandsSendingTimeMlsec = System.currentTimeMillis()
                - commandStartTimeMlsec;
        logWriter.println("==> Commands sending time(mlsecs) = "
                + commandsSendingTimeMlsec);

        logWriter.println("==> Checking frames.. ");
        if (checkFrames(startedRecursiveThreadsNumber) == FAILURE ) {
            testCaseStatus = FAILURE;
            logWriter.println("## FAILURE: frames checking FAILED!");
        } else {
            logWriter.println("==> Checking frames PASSED ");
        }

        logWriter.println("\n");
        logWriter.println("==> Wait for replies for all sent commands and check results...");
        ReplyPacket reply = null;
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        long receiveReplyStartTimeMlsec = System.currentTimeMillis();
        int receiveReplyStatus = SUCCESS;
        int receivedReplies = 0;
        for (int i = 0; i < sentCommands; i++) {
            reply = receveReply(commands[i].getId(), currentTimeout());
            if (reply == null) {
                logWriter
                        .println("## FAILURE: Can NOT receive reply for ClassType.InvokeMethod command: "
                                + "Command index = " + i);
                terminateDebuggee(FAILURE, "MARKER_12`");
                return failed
                ("## FAILURE while receiving reply for ClassType.InvokeMethod command");
            }
            receivedReplies++;
            if (checkReplyForError(reply, expectedErrors,
                    "ClassType.InvokeMethod command (Command index = " + i
                            + ")")) {
                receiveReplyStatus = FAILURE;
                continue;
            }
            if (printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "ClassType.InvokeMethod command")) {
                continue;
            }
            Value returnValue = reply.getNextValueAsValue();
            if (returnValue == null) {
                logWriter
                        .println("## FAILURE: ClassType.InvokeMethod command results in null returnValue: "
                                + "Command index = " + i);
                receiveReplyStatus = FAILURE;
            } else {
                int returnIntValue = returnValue.getIntValue();
                if (returnIntValue != i) {
                    logWriter
                            .println("## FAILURE: ClassType.InvokeMethod command results in unexpected int value: "
                                    + "Command index = " + i);
                    logWriter.println("##          Returned int value = "
                            + returnIntValue);
                    logWriter.println("##          Expected int value = " + i);
                    receiveReplyStatus = FAILURE;
                }
            }
            TaggedObject exception = reply.getNextValueAsTaggedObject();
            if (exception == null) {
                logWriter
                        .println("## FAILURE: Can NOT get tagged-objectID for ClassType.InvokeMethod command: "
                                + "Command index = " + i);
                receiveReplyStatus = FAILURE;
            } else {
                long exceptionObjectID = exception.objectID;
                if (exceptionObjectID != 0) {
                    logWriter
                            .println("## FAILURE: ClassType.InvokeMethod command results in unexpected exception: "
                                    + "Command index = " + i);
                    logWriter.println("##          exceptionObjectID = "
                            + exceptionObjectID);
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
        long receiveReplyTimeMlsec = System.currentTimeMillis()
                - receiveReplyStartTimeMlsec;
        logWriter.println("==> Receiving replies time(mlsecs) = "
                + receiveReplyTimeMlsec);

        logWriter.println("==> Resuming debuggee..");
        resumeDebuggee("#5");

        logWriter.println("==> Wait for all threads finish in debuggee...");
        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal = receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_02.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_13");
            return failed("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        logWriter.println("==> Resuming debuggee..");
        resumeDebuggee("#5");

        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testMixed003: running time(mlsecs) = "
                + testRunTimeMlsec);
        if (testCaseStatus == FAILURE) {
            logWriter.println("==> testMixed003: FAILED");
            return failed("testMixed003:");
        } else {
            logWriter.println("==> testMixed003: OK");
            return passed("testMixed003: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testMixed003: FAILED");
    return failed("==> testMixed003: Unexpected Exception! ");
}
    }

    public int checkFrames(int startedRecursiveThreadsNumber) {
        int success = SUCCESS;
        int allFrames = 0;
        limitedPrintlnInit(20);
        for (int i = 0; i < startedRecursiveThreadsNumber; i++) {
            
            CommandPacket packet = new CommandPacket(
                    JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                    JDWPCommands.ThreadReferenceCommandSet.FramesCommand);
            packet.setNextValueAsThreadID(recursiveThreadsIDs[i]);
            packet.setNextValueAsInt(0);
            packet.setNextValueAsInt(-1);

            ReplyPacket reply = performCommand(packet, currentTimeout());
            if ( reply == null ) {
                return FAILURE;
            }

            int[] expectedErrors = { JDWPConstants.Error.NONE,
                    JDWPConstants.Error.OUT_OF_MEMORY };
            if (checkReplyForError(reply, expectedErrors,
                    "ThreadReference::Frames command")) {
                return FAILURE;
            }
            if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "ThreadReference::Frames command")) {
                return success;
            }

            int frames = reply.getNextValueAsInt();
            for (int j = 0; j < frames; j++) {
                allFrames++;
                frameIdArray[allFrames] = reply.getNextValueAsFrameID();
                Location location = reply.getNextValueAsLocation();

                String methName = debuggeeWrapper.vmMirror.getMethodName(
                        location.classID, location.methodID);
                if (methName.equals("testRecursiveMethod")) {
                    ReplyPacket receivedReply = debuggeeWrapper.vmMirror
                            .getClassBySignature("Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MIXED003/Mixed03_RecursiveThread;");
                    receivedReply.getNextValueAsInt();
                    receivedReply.getNextValueAsByte();
                    long threadClassID = receivedReply
                            .getNextValueAsReferenceTypeID();
                            
                    long methodID = debuggeeWrapper.vmMirror.getMethodID(
                            threadClassID, "testRecursiveMethod");
                    if (location.methodID != methodID) {
                        boolean toPrint = limitedPrintln("##FAILURE: methodID from frame's location = "
                                        + location.methodID
                                        + " instead of "
                                        + methodID);
                        if (toPrint) {
                            String receivedMethodName = debuggeeWrapper.vmMirror.getMethodName(threadClassID, methodID);
                            String receivedClassSignature = debuggeeWrapper.vmMirror.getClassSignature(threadClassID);
                            String fromLocationMethodName = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                            String fromLocationClassSignature = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                            
                            limitedPrintln("##Info received using location.classID and location.methodID: methodName = " + fromLocationMethodName + " classSignature = " + fromLocationClassSignature);
                            limitedPrintln("##                               methodName = " + fromLocationMethodName );
                            limitedPrintln("##                               classSignature = " + fromLocationClassSignature);
                            limitedPrintln("##Info received using thread class signature: methodName = " + receivedMethodName + " classSignature = " + receivedClassSignature);
                            limitedPrintln("##                               methodName = " + receivedMethodName);
                            limitedPrintln("##                               classSignature = " + receivedClassSignature);
                        } else {
                            return FAILURE;
                        }
                        success = FAILURE;
                    }
                    if (location.classID != threadClassID) {
                        boolean toPrint = limitedPrintln("##FAILURE: classID from frame's location = "
                                        + location.classID
                                        + " instead of "
                                        + threadClassID);
                        
                        if (toPrint) {
                            String receivedMethodName = debuggeeWrapper.vmMirror.getMethodName(threadClassID, methodID);
                            String receivedClassSignature = debuggeeWrapper.vmMirror.getClassSignature(threadClassID);
                            String fromLocationMethodName = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                            String fromLocationClassSignature = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                            
                            limitedPrintln("##Info received using location.classID and location.methodID: methodName = " + fromLocationMethodName + " classSignature = " + fromLocationClassSignature);
                            limitedPrintln("##                               methodName = " + fromLocationMethodName );
                            limitedPrintln("##                               classSignature = " + fromLocationClassSignature);
                            limitedPrintln("##Info received using thread class signature: methodName = " + receivedMethodName + " classSignature = " + receivedClassSignature);
                            limitedPrintln("##                               methodName = " + receivedMethodName);
                            limitedPrintln("##                               classSignature = " + receivedClassSignature);
                        } else {
                            return FAILURE;
                        }
                        success = FAILURE;
                    }

                    Frame frame = new Frame();
                    frame.setThreadID(recursiveThreadsIDs[i]);
                    frame.setID(frameIdArray[allFrames]);
                    frame.setLocation(location);

                    List vars = debuggeeWrapper.vmMirror.getLocalVars(frame);

                    for (int g = 0; g < vars.size(); g++) {
                        Variable v = (Variable) vars.get(g);
                        if (v.getName().equals("localValue")) {
                            if (!v.getSignature().equals("I")) {
                                limitedPrintln("##FAILURE: local variable "
                                        + v.getName()
                                        + " has unexpected signature "
                                        + v.getSignature() + " instead of I");
                                success = FAILURE;
                            }
                        }
                    }
                }

                
                CommandPacket stackFrameCommand = new CommandPacket(
                        JDWPCommands.StackFrameCommandSet.CommandSetID,
                        JDWPCommands.StackFrameCommandSet.ThisObjectCommand);
                stackFrameCommand.setNextValueAsThreadID(recursiveThreadsIDs[i]);
                stackFrameCommand
                        .setNextValueAsFrameID(frameIdArray[allFrames]);
                ReplyPacket stackFrameReply = debuggeeWrapper.vmMirror
                        .performCommand(stackFrameCommand);
                
                if (checkReplyForError(stackFrameReply, expectedErrors,
                        "StackFrame::ThisObject command")) {
                    success = FAILURE;
                    break;
                }
                TaggedObject object = stackFrameReply
                        .getNextValueAsTaggedObject();
                long objectID = object.objectID;
                if (methName.equals("testMethod")) {
                    if (object.objectID == 0) {
                        limitedPrintln("##FAILURE: returned frame's objectID = null, but method is not static");
                        success = FAILURE;
                    }
                }
            }
        }
       /*
        if (checkFrameIdUnique(allFrames) == -1) {
            success = -1;
        }
        */
        return success;
    }

    private int checkFrameIdUnique(int allFrames) {
        int success = 0;
        limitedPrintlnInit(20);
        for (int i = 0; i < allFrames; i++) {
            long frameID = frameIdArray[i];
            for (int j = i + 1; j < allFrames; j++) {
                if (frameID == frameIdArray[j]) {
                    limitedPrintln("##FAILURE: frameID is not unique: "
                            + frameID+" i = "+i+" j = "+j);
                    
                                        
                    
                    success = -1;
                }
            }
        }
        if (success != 0) {
            logWriter.println("");
            logWriter.println("##FAILURE: frameID are not unique");
        } else {
            logWriter.println("");
            logWriter.println("==> frameIDs are unique");
        }
        return success;
    }

    public static void main(String[] args) {
        System.exit(new MixedTest003().test(args));
    }
}