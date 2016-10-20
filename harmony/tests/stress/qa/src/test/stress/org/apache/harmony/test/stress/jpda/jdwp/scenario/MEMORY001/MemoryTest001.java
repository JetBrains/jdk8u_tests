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
 * Created on 08.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY001;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;
import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestPrimitives;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Value;


/**
 * This test case runs the JDWP command <code>VirtualMachine.AllClasses</code>
 * when very big number of classes are loaded in debuggee.
 */
public class MemoryTest001 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY001.MemoryDebuggee001";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case runs the JDWP command <code>VirtualMachine.AllClasses</code>
     * when very big number of classes are loaded in debuggee.
     */
    public Result  testMemory001() {
        
        logWriter.println("==> testMemory001: START (" + new Date() + ")...");
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
        String debuggeeSignal =  receiveSignalWithWait();
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        // Prepare classesArray variable for debuggee
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        String classesArrayFieldName = "classesArray";
        long classesArrayFieldID = 
            debuggeeWrapper.vmMirror.getFieldID(debuggeeRefTypeID, classesArrayFieldName);
        if ( classesArrayFieldID == -1 ) {
            logWriter.println("## Can NOT get classesArrayFieldID");
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## Can NOT get classesArrayFieldID");
        }
        printlnForDebug("classesArrayFieldID = " + classesArrayFieldID);
        
        Value classesArrayFieldValue = getClassFieldValue (debuggeeRefTypeID, classesArrayFieldID);
        if ( classesArrayFieldValue == null ) {
            logWriter.println("## Can NOT get classesArrayFieldValue");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get classesArrayFieldValue");
        }
        
        byte tag = classesArrayFieldValue.getTag();
        logWriter.println("=> classesArrayFieldValue tag = " + tag
            + "(" + JDWPConstants.Tag.getName(tag) + ")");
        if ( tag != JDWPConstants.Tag.ARRAY_TAG ) {
            logWriter.println("\n");
            logWriter.println
            ("## FAILURE: Unexpected value tag ");
            logWriter.println
            ("## Expected tag = " + JDWPConstants.Tag.ARRAY_TAG + "(ARRAY_TAG)");
            terminateDebuggee(FAILURE, "MARKER_06");
           return failed("## FAILURE: Unexpected value tag");
        }
        long classesArrayID = classesArrayFieldValue.getLongValue();
        printlnForDebug("classesArrayID = " + classesArrayID);

        logWriter.println("==> Send VirtualMachine::AllClasses command BEFORE loading new classes...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllClassesCommand);
        long commandStartTimeMlsec = System.currentTimeMillis();
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        long commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "VirtualMachine::AllClasses command") ) {
            terminateDebuggee(FAILURE, "MARKER_07");
            return failed("## VirtualMachine::AllClasses command ERROR");
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());

        int classes = reply.getNextValueAsInt();
        logWriter.println("==> Number of reference types BEFORE loading new classes = " + classes);
        
        long[] classesIDs = new long[classes];
        long[] objectIDs = new long[classes];
        int objectIDsCount = 0;
        for (int i = 0; i < classes; i++) {
            byte refTypeTag = reply.getNextValueAsByte();
            long typeID = reply.getNextValueAsReferenceTypeID();
            String signature = reply.getNextValueAsString();
            int status = reply.getNextValueAsInt();
            classesIDs[i] = typeID;
            if ( refTypeTag != JDWPConstants.TypeTag.ARRAY ) {
                objectIDs[objectIDsCount] = typeID;
                objectIDsCount++;
            }
        }
        printlnForDebug("objectIDsCount = " + objectIDsCount);

        logWriter.println("\n");
        logWriter.println("==> Convert ReferenceTypeIDs to ObjectIDs for received classes...");
        for (int i = 0; i < objectIDsCount; i++) {
            packet = new CommandPacket(
                    JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                    JDWPCommands.ReferenceTypeCommandSet.ClassObjectCommand);
            packet.setNextValueAsLong(objectIDs[i]);
            reply = debuggeeWrapper.vmMirror.performCommand(packet);
            if ( checkReplyForError
                    (reply, JDWPConstants.Error.NONE,
                    "ReferenceType::ClassObject command") ) {
                logWriter.println("==> Converted ReferenceTypeID = " + classesIDs[i]);
                terminateDebuggee(FAILURE, "MARKER_08");
                return failed("## ReferenceType::ClassObject command ERROR");
            }
            objectIDs[i] = reply.getNextValueAsLong();
        }
        printlnForDebug("ObjectIDs[" + (objectIDsCount-1) + "] = " + objectIDs[objectIDsCount-1]);
        logWriter.println("\n");
        logWriter.println("==> Convert ReferenceTypeIDs to ObjectIDs for received - DONE");

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
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "ArrayReference::SetValues command") ) {
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed("## ArrayReference::SetValues command ERROR");
        }
        
        logWriter.println("\n");
        logWriter.println("==> Send ArrayReference::SetValues command - DONE WITHOUT ERROR");
        resumeDebuggee("#2");
        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to load very big number of new classes...");
        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal =  receiveSignalWithWait();
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_10");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        logWriter.println("\n");
        logWriter.println("==> Send VirtualMachine::AllClasses command " +
                "AFTER loading by debugge big number of new classes...");
        packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllClassesCommand);
        try {
            commandStartTimeMlsec = System.currentTimeMillis();
            reply = debuggeeWrapper.vmMirror.performCommand(packet, currentTimeout());
            commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
            logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
            
        } catch ( Throwable thrown ) {
            logWriter.println("## FAILURE: Exception while performCommand() = " + thrown);
            resumeDebuggee("#3");
            return failed("## FAILURE: Exception while performCommand()");
        }
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        if ( checkReplyForError(reply, expectedErrors,
                               "VirtualMachine::AllClasses command") ) {
            resumeDebuggee("#4");
            return failed("## VirtualMachine::AllClasses command ERROR");
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());
        int testCaseStatus = SUCCESS;
        if ( ! printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                                  "VirtualMachine::AllClasses command") ) {
            int notFoundClasses = 0;
            int newClasses = reply.getNextValueAsInt();
            logWriter.println
            ("==> Number of reference types AFTER loading by debugge new classes = " + newClasses);

            logWriter.println("\n");
            logWriter.println
            ("==> Check if all expected classes are returned by VirtualMachine::AllClasses command...");
            int sourceClasses = 6;
            int generatedClasses = sourceClasses * MemoryDebuggee001.ARRAY_DIMENSION;
            String[] expectedClassSignatures
                = new String[sourceClasses + generatedClasses];
            expectedClassSignatures[0] = 
                DEBUGGEE_SIGNATURE;
            expectedClassSignatures[1] = 
                "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY001/Memory001_TestClass01;";
            expectedClassSignatures[2] = 
                "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY001/Memory001_TestClass02;";
            expectedClassSignatures[3] = 
                "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY001/Memory001_TestClass03;";
            expectedClassSignatures[4] = 
                "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY001/Memory001_TestClass04;";
            expectedClassSignatures[5] = 
                "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY001/Memory001_TestClass05;";
            StressTestPrimitives.fillArrayOfSignatures
            (expectedClassSignatures, sourceClasses, MemoryDebuggee001.ARRAY_DIMENSION);
            boolean[] foundClassesArray = new boolean[expectedClassSignatures.length];
            for (int i = 0; i < foundClassesArray.length; i++) {
                foundClassesArray[i] = false;
            }
            
            for (int i = 0; i < newClasses; i++) {
                byte refTypeTag = reply.getNextValueAsByte();
                long typeID = reply.getNextValueAsReferenceTypeID();
                String signature = reply.getNextValueAsString();
                int status = reply.getNextValueAsInt();
                for (int j = 0; j < expectedClassSignatures.length; j++) {
                    if ( expectedClassSignatures[j].equals(signature) ) {
                        foundClassesArray[j] = true;
                        break;
                    }
                }
            }
            for (int i = 0; i < foundClassesArray.length; i++) {
                if ( foundClassesArray[i] == false ) {
                    notFoundClasses++;
                    if ( notFoundClasses <= 20 ) {
                        logWriter.println("\n");
                        logWriter.println("## FAILURE: Expected class is NOT found among returned classes");
                        logWriter.println("##          Expected class signature = " + expectedClassSignatures[i]);
                    }
                    if ( notFoundClasses == 21 ) {
                        logWriter.println("\n");
                        logWriter.println("## Very big number of NOT found classes!");
                    }
                    testCaseStatus = FAILURE;
                }
            }
            if ( notFoundClasses > 0 ) {
                logWriter.println("\n");
                logWriter.println("## Not found expected classes number = " + notFoundClasses);
            } else {
                logWriter.println
                ("==> OK - all expected classes are found out!");
            }
        }
        resumeDebuggee("#5");
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testMemory001: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testMemory001: FAILED");
            return failed("testMemory001:");
        } else {
            logWriter.println("==> testMemory001: OK");
            return passed("testMemory001: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testMemory001: FAILED");
    return failed("==> testMemory001: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new MemoryTest001().test(args));
	}
}
