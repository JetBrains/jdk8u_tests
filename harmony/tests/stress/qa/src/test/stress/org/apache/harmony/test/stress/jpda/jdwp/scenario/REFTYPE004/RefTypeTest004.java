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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.REFTYPE004;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Value;


/**
 * This tests case exercises the JDWP agent under ReferenceTypeIDs stressing.
 * First, test loads very big number of classes on debuggee side, sends
 * <code>VirtualMachine.AllClasses</code> command, saves and checks returned
 * RefTypeIDs. Then, test loads some number of new classes, and creates memory
 * stressing by loading very big array. Then sends
 * <code>VirtualMachine.ClassesBySignature</code> and checks that RefTypeIDs
 * of new classes are unique. Then checks if all RefTypeIDs have expected
 * SourceFile and signatures.
 *  
 */
public class RefTypeTest004 extends StressTestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.stress.jpda.jdwp.scenario.REFTYPE004.RefTypeDebuggee004";

    public final static String DEBUGGEE_SIGNATURE = "L"
            + DEBUGGEE_CLASS_NAME.replace('.', '/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public RefTypeArray refTypesArray = new RefTypeArray();
    public static String[] signaturesArray = new String[200000];
    public static byte[] tagsArray = new byte[200000];

    /**
     * This tests case exercises the JDWP agent under ReferenceTypeIDs stressing.
     * First, test loads very big number of classes on debuggee side, sends
     * <code>VirtualMachine.AllClasses</code> command, saves and checks returned
     * RefTypeIDs. Then, test loads some number of new classes, and creates memory
     * stressing by loading very big array. Then sends
     * <code>VirtualMachine.ClassesBySignature</code> and checks that RefTypeIDs
     * of new classes are unique. Then checks if all RefTypeIDs have expected
     * SourceFile and signatures.
     *  
     */
    public Result testRefType004() {
        int testCaseStatus = SUCCESS;
        logWriter.println("==> testRefType004: START (" + new Date() + ")...");

        if (waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if (setupSignalWithWait() == FAILURE) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }
        resumeDebuggee("#1");
try {
        printlnForDebug("receiving 'SIGNAL_READY_01' Signal from debuggee...");
        String debuggeeSignal = receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_01.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        // Prepare classesArray variable for debuggee
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror
                .getClassID(DEBUGGEE_SIGNATURE);
        if (debuggeeRefTypeID == -1) {
            logWriter.println("## Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        String classesArrayFieldName = "classesArray";
        long classesArrayFieldID = debuggeeWrapper.vmMirror.getFieldID(
                debuggeeRefTypeID, classesArrayFieldName);
        if (classesArrayFieldID == -1) {
            logWriter.println("## Can NOT get classesArrayFieldID");
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## Can NOT get classesArrayFieldID");
        }
        printlnForDebug("classesArrayFieldID = " + classesArrayFieldID);

        Value classesArrayFieldValue = getClassFieldValue(debuggeeRefTypeID,
                classesArrayFieldID);
        if (classesArrayFieldValue == null) {
            logWriter.println("## Can NOT get classesArrayFieldValue");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get classesArrayFieldValue");
        }

        byte tag = classesArrayFieldValue.getTag();
        logWriter.println("=> classesArrayFieldValue tag = " + tag + "("
                + JDWPConstants.Tag.getName(tag) + ")");
        if (tag != JDWPConstants.Tag.ARRAY_TAG) {
            logWriter.println("\n");
            logWriter.println("## FAILURE: Unexpected value tag ");
            logWriter.println("## Expected tag = "
                    + JDWPConstants.Tag.ARRAY_TAG + "(ARRAY_TAG)");
            terminateDebuggee(FAILURE, "MARKER_06");
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
            terminateDebuggee(FAILURE, "MARKER_07");
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
        logWriter.println("==> objectIDsCount = " + objectIDsCount);

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
                terminateDebuggee(FAILURE, "MARKER_08");
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
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed("## ArrayReference::SetValues command ERROR");
        }

        logWriter.println("\n");
        logWriter
                .println("==> Send ArrayReference::SetValues command - DONE WITHOUT ERROR");
        resumeDebuggee("#2");
        logWriter.println("\n");
        logWriter
                .println("==> Wait for debuggee to load very big number of new classes...");
        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal = receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_02.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_10");
            return failed("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        if (!saveRefTypeIDs(refTypesArray, signaturesArray, tagsArray)) {
            logWriter.println("##FAILURE: during saving RefTypesIDs");
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed("## FAILURE during saving RefTypesIDs. ");
        }
        
        resumeDebuggee("#5");
        logWriter.println("==>  receiving 'SIGNAL_READY_03' Signal from debuggee...");
        debuggeeSignal = receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_03.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_12");
            return failed("## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee");
        }
        if (!checkNewClasses(refTypesArray, signaturesArray, tagsArray, 4)) {
            logWriter.println("##FAILURE: during checkNewClasses");
            testCaseStatus = FAILURE;
        }
        
        resumeDebuggee("#6");
        
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testRefType004: running time(mlsecs) = "
                + testRunTimeMlsec);
        if (testCaseStatus == FAILURE) {
            logWriter.println("==> testRefType004: FAILED");
            return failed("testRefType004:");
        } else {
            logWriter.println("==> testRefType004: OK");
            return passed("testRefType004: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testRefType004: FAILED");
    return failed("==> testRefType004: Unexpected Exception! ");
}
    }

    public static void main(String[] args) {
        System.exit(new RefTypeTest004().test(args));
    }
}