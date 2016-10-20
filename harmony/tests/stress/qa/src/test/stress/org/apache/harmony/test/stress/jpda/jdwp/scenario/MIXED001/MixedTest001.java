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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED001;

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
 * This tests case exercises the JDWP agent under Thread and
 * ReferenceTypeIDs stressing. First, test loads very big number of classes
 * on debuggee side, creates separate thread and starts it vast number of
 * time. Each created thread is suspended on proper event, test sends
 * <code>ClassType.InvokeMethod</code> command for
 * <code>methodToInvoke</code> method without waiting a reply. Then test
 * runs <code>VirtualMachine.AllClasses</code> command and saves and
 * checks returned RefTypeIDs. Loads vast number of new classes, sends
 * <code>VirtualMachine.AllClasses</code> and compares returned by the
 * second AllClasses command RefTypeIDs with RefTypeIDs saved after the
 * first AllClasses command. Then checks if all RefTypeIDs have expected
 * SourceFile and signatures. Then checks replies for each
 * <code>ClassType.InvokeMethod</code> command.
 */
public class MixedTest001 extends StressTestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.stress.jpda.jdwp.scenario.MIXED001.MixedDebuggee001";

    public final static String DEBUGGEE_SIGNATURE = "L"
            + DEBUGGEE_CLASS_NAME.replace('.', '/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }

    public RefTypeArray refTypesArray = new RefTypeArray();

    public static long[] threadIdArray = new long[StressDebuggee.MIXED001_THREADS_NUMBER];

    public static String[] signaturesArray = new String[200000];

    public static byte[] tagsArray = new byte[200000];

    public static long allClasses = 0;

    public long[] threadObjectIDArray = new long[StressDebuggee.MIXED001_THREADS_NUMBER];

    /**
     * This tests case exercises the JDWP agent under Thread and
     * ReferenceTypeIDs stressing. First, test loads very big number of classes
     * on debuggee side, creates separate thread and starts it vast number of
     * time. Each created thread is suspended on proper event, test sends
     * <code>ClassType.InvokeMethod</code> command for
     * <code>methodToInvoke</code> method without waiting a reply. Then test
     * runs <code>VirtualMachine.AllClasses</code> command and saves and
     * checks returned RefTypeIDs. Loads vast number of new classes, sends
     * <code>VirtualMachine.AllClasses</code> and compares returned by the
     * second AllClasses command RefTypeIDs with RefTypeIDs saved after the
     * first AllClasses command. Then checks if all RefTypeIDs have expected
     * SourceFile and signatures. Then checks replies for each
     * <code>ClassType.InvokeMethod</code> command.
     */
    public Result testMixed001() {
        int testCaseStatus = SUCCESS;

        logWriter.println("==> testMixed001: START (" + new Date() + ")...");
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
        printlnForDebug("receiving 'SIGNAL_READY_01' Signal from debuggee...");
        String debuggeeSignal = receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_01.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        // Prepare classesArray variable for debuggee
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror
                .getClassID(DEBUGGEE_SIGNATURE);
        if (debuggeeRefTypeID == -1) {
            logWriter.println("## Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        String classesArrayFieldName = "classesArray";
        long classesArrayFieldID = debuggeeWrapper.vmMirror.getFieldID(
                debuggeeRefTypeID, classesArrayFieldName);
        if (classesArrayFieldID == -1) {
            logWriter.println("## Can NOT get classesArrayFieldID");
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## Can NOT get classesArrayFieldID");
        }
        printlnForDebug("classesArrayFieldID = " + classesArrayFieldID);

        Value classesArrayFieldValue = getClassFieldValue(debuggeeRefTypeID,
                classesArrayFieldID);
        if (classesArrayFieldValue == null) {
            logWriter.println("## Can NOT get classesArrayFieldValue");
            terminateDebuggee(FAILURE, "MARKER_07");
            return failed("## Can NOT get classesArrayFieldValue");
        }

        byte tag = classesArrayFieldValue.getTag();
        logWriter.println("==> classesArrayFieldValue tag = " + tag + "("
                + JDWPConstants.Tag.getName(tag) + ")");
        if (tag != JDWPConstants.Tag.ARRAY_TAG) {
            logWriter.println("\n");
            logWriter.println("## FAILURE: Unexpected value tag ");
            logWriter.println("## Expected tag = "
                    + JDWPConstants.Tag.ARRAY_TAG + "(ARRAY_TAG)");
            terminateDebuggee(FAILURE, "MARKER_08");
            return failed("## FAILURE: Unexpected value tag");
        }
        long classesArrayID = classesArrayFieldValue.getLongValue();
        printlnForDebug("classesArrayID = " + classesArrayID);

        logWriter
                .println("==> Send VirtualMachine::AllClasses command BEFORE loading new classes...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllClassesCommand);
        long commandStartTimeMlsec = System.currentTimeMillis();
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        long commandRunTimeMlsec = System.currentTimeMillis()
                - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = "
                + commandRunTimeMlsec);
        if (checkReplyForError(reply, JDWPConstants.Error.NONE,
                "VirtualMachine::AllClasses command")) {
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed("## VirtualMachine::AllClasses command ERROR");
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());

        int classes = reply.getNextValueAsInt();
        logWriter
                .println("==> Number of reference types BEFORE loading new classes = "
                        + classes);

        long[] classesIDs = new long[classes];
        long[] objectIDs = new long[classes];
        int objectIDsCount = 0;
        for (int i = 0; i < classes; i++) {
            byte refTypeTag = reply.getNextValueAsByte();
            long typeID = reply.getNextValueAsReferenceTypeID();
            String signature = reply.getNextValueAsString();
            int status = reply.getNextValueAsInt();
            classesIDs[i] = typeID;
            if (refTypeTag != JDWPConstants.TypeTag.ARRAY) {
                objectIDs[objectIDsCount] = typeID;
                objectIDsCount++;
            }
        }
        printlnForDebug("objectIDsCount = " + objectIDsCount);

        logWriter.println("\n");
        logWriter
                .println("==> Convert ReferenceTypeIDs to ObjectIDs for received classes...");
        for (int i = 0; i < objectIDsCount; i++) {
            packet = new CommandPacket(
                    JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                    JDWPCommands.ReferenceTypeCommandSet.ClassObjectCommand);
            packet.setNextValueAsLong(objectIDs[i]);
            reply = debuggeeWrapper.vmMirror.performCommand(packet);
            if (checkReplyForError(reply, JDWPConstants.Error.NONE,
                    "ReferenceType::ClassObject command")) {
                logWriter.println("==> Converted ReferenceTypeID = "
                        + classesIDs[i]);
                terminateDebuggee(FAILURE, "MARKER_10");
                return failed("## ReferenceType::ClassObject command ERROR");
            }
            objectIDs[i] = reply.getNextValueAsLong();
        }
        printlnForDebug("ObjectIDs[" + (objectIDsCount - 1) + "] = "
                + objectIDs[objectIDsCount - 1]);
        logWriter.println("\n");
        logWriter
                .println("==> Convert ReferenceTypeIDs to ObjectIDs for received - DONE");

        logWriter.println("\n");
        logWriter.println("==> Send ArrayReference::SetValues command...");
        packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.SetValuesCommand);
        packet.setNextValueAsArrayID(classesArrayID);
        packet.setNextValueAsInt(0); // firstIndex
        packet.setNextValueAsInt(objectIDsCount); // The number of values to set
        for (int i = 0; i < objectIDsCount; i++) {
            packet.setNextValueAsObjectID(objectIDs[i]);
        }
        reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if (checkReplyForError(reply, JDWPConstants.Error.NONE,
                "ArrayReference::SetValues command")) {
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed("## ArrayReference::SetValues command ERROR");
        }

        logWriter.println("\n");
        logWriter
                .println("==> Send ArrayReference::SetValues command - DONE WITHOUT ERROR");
        resumeDebuggee("#2");
        logWriter.println("\n");
        logWriter
                .println("==> Wait for debuggee to load very big number of new classes...");
        debuggeeSignal = receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_02.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_12");
            return failed("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }

        logWriter.println("\n");
        logWriter
                .println("==> Wait for debuggee to run very big number of threads...");
        resumeDebuggee("#3");

        debuggeeSignal = receiveThreadSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_03.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_13");
            return failed("## FAILURE while receiving 'SIGNAL_READY_03' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);

        long toInvokeMethodID = debuggeeWrapper.vmMirror.getMethodID(
                debuggeeRefTypeID, MixedDebuggee001.METHOD_TO_INVOKE_NAME);
        if (toInvokeMethodID == -1) {
            logWriter.println("## FAILURE: Can NOT get toInvokeMethodID");
            terminateDebuggee(FAILURE, "MARKER_14");
            return failed("## Can NOT get toInvokeMethodID");
        }
        printlnForDebug("toInvokeMethodID = " + toInvokeMethodID);

        logWriter.println("==> Get startedThreadsNumber value from debuggee...");
        int startedThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "createdThreadsNumber");
        if ( startedThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_15");
            return failed("## Can NOT get startedThreadsNumber");
        }
        logWriter.println("==> startedThreadsNumber = " + startedThreadsNumber);
        
        logWriter
                .println("==> Waiting for suspending by event of all started threads...");
        String[] startedThreadsNames = new String[startedThreadsNumber];
        long[] startedThreadsIDs = new long[startedThreadsNumber];
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        for (int i = 0; i < startedThreadsNumber; i++) {
            startedThreadsIDs[i] = waitForSuspendThreadByEvent(ANY_THREAD);
            if (startedThreadsIDs[i] == FAILURE) {
                logWriter
                        .println("## FAILURE while waiting for suspending of all started threads!");
                terminateDebuggee(FAILURE, "MARKER_16");
                return failed("## FAILURE while waiting for suspending of all started threads! ");
            }
            startedThreadsNames[i] = "Unknown Thread Name";
            try {
                startedThreadsNames[i] = debuggeeWrapper.vmMirror
                        .getThreadName(startedThreadsIDs[i]);
            } catch (Throwable thrown) {
                // ignore
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
        commandStartTimeMlsec = System.currentTimeMillis();
        for (int i = 0; i < commandsToRun; i++) {
            commands[i] = new CommandPacket(
                    JDWPCommands.ClassTypeCommandSet.CommandSetID,
                    JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
            commands[i].setNextValueAsClassID(debuggeeRefTypeID);
            commands[i].setNextValueAsThreadID(startedThreadsIDs[i]);
            commands[i].setNextValueAsMethodID(toInvokeMethodID);
            commands[i].setNextValueAsInt(2); // args number
            long timeMlsecsToRun = 10;
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
            terminateDebuggee(FAILURE, "MARKER_17");
            return failed("## Can NOT send ClassType.InvokeMethod commands");
        }
        logWriter
                .println("==> Send ClassType.InvokeMethod asynchronous commands - Done");
        logWriter.println("==> Sent commands number = " + sentCommands);
        if (sentCommands != commandsToRun) {
            logWriter.println("==> Failures number while sending commands = "
                    + (commandsToRun - sentCommands));
        }
        long commandsSendingTimeMlsec = System.currentTimeMillis()
                - commandStartTimeMlsec;
        logWriter.println("==> Commands sending time(mlsecs) = "
                + commandsSendingTimeMlsec);

        boolean saveRefTypeIDsStatus = 
            saveRefTypeIDs(refTypesArray, signaturesArray, tagsArray);
        if (! saveRefTypeIDsStatus ) {
            logWriter.println("##FAILURE: during saving RefTypesIDs");
        } else {
            logWriter.println("==> Saving classes PASSED");
        }

        logWriter
                .println("==> Resuming debuggee main Thread and waiting for loading classes second time...");
        resumeSignalThread("#5");
        logWriter.println("==> receiving 'SIGNAL_READY_04' Thread Signal from debuggee...");
        debuggeeSignal = receiveThreadSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_04.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_18");
            return failed("## FAILURE while receiving 'SIGNAL_READY_04' Thread Signal from debuggee");
        }
        
        boolean checkClassesStatus = true; 
        if ( saveRefTypeIDsStatus ) {
            allClasses = refTypesArray.getLength();
            checkClassesStatus = 
                checkClasses(refTypesArray, signaturesArray, tagsArray, allClasses);
            if ( ! checkClassesStatus ) {
                logWriter.println
                ("==> ## FAILURE while checking classes returned by VirtualMachine::AllClasses...");
            }
        }

        logWriter.println("\n");
        logWriter
                .println("==> Wait for replies for all sent ClassType.InvokeMethod commands and check results...");
        testCaseStatus = SUCCESS;
        reply = null;
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        long receiveReplyStartTimeMlsec = System.currentTimeMillis();
        int receivedReplies = 0;
        for (int i = 0; i < sentCommands; i++) {
            reply = receveReply(commands[i].getId(), currentTimeout());
            if (reply == null) {
                logWriter
                        .println("## FAILURE: Can NOT receive reply for ClassType.InvokeMethod command: "
                                + "Command index = " + i);
                terminateDebuggee(FAILURE, "MARKER_19`");
                return failed
                ("## FAILURE while receiving reply for ClassType.InvokeMethod command");
            }
            receivedReplies++;
            if (checkReplyForError(reply, expectedErrors,
                    "ClassType.InvokeMethod command (Command index = " + i
                            + ")")) {
                testCaseStatus = FAILURE;
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
                testCaseStatus = FAILURE;
            } else {
                int returnIntValue = returnValue.getIntValue();
                if (returnIntValue != i) {
                    logWriter
                            .println("## FAILURE: ClassType.InvokeMethod command results in unexpected int value: "
                                    + "Command index = " + i);
                    logWriter.println("##          Returned int value = "
                            + returnIntValue);
                    logWriter.println("##          Expected int value = " + i);
                    testCaseStatus = FAILURE;
                }
            }
            TaggedObject exception = reply.getNextValueAsTaggedObject();
            if (exception == null) {
                logWriter
                        .println("## FAILURE: Can NOT get tagged-objectID for ClassType.InvokeMethod command: "
                                + "Command index = " + i);
                testCaseStatus = FAILURE;
            } else {
                long exceptionObjectID = exception.objectID;
                if (exceptionObjectID != 0) {
                    logWriter
                            .println("## FAILURE: ClassType.InvokeMethod command results in unexpected exception: "
                                    + "Command index = " + i);
                    logWriter.println("##          exceptionObjectID = "
                            + exceptionObjectID);
                    testCaseStatus = FAILURE;
                }
            }
        }
        logWriter.println
        ("==> received Replies number for ClassType.InvokeMethod = " + receivedReplies);
        if (testCaseStatus == SUCCESS) {
            logWriter.println
            ("==> OK - all ClassType.InvokeMethod asynchronous commands returned expected results");
        } else {
            if ( (!saveRefTypeIDsStatus) || (!checkClassesStatus) ) {
                testCaseStatus = FAILURE;
            }
        }
        long receiveReplyTimeMlsec = System.currentTimeMillis()
                - receiveReplyStartTimeMlsec;
        logWriter.println("==> Receiving replies time(mlsecs) = "
                + receiveReplyTimeMlsec);

        logWriter.println("==> Resuming debuggee with all threads...");
        resumeDebuggee("#5");

        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testMixed001: running time(mlsecs) = "
                + testRunTimeMlsec);
        if (testCaseStatus == FAILURE) {
            logWriter.println("==> testMixed001: FAILED");
            return failed("testMixed001: ");
        } else {
            logWriter.println("==> testMixed001: OK");
            return passed("testMixed001: OK");
        }

} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testMixed001: FAILED");
    return failed("==> testMixed001: Unexpected Exception! ");
}
    }

    public static void main(String[] args) {
        System.exit(new MixedTest001().test(args));
    }
}