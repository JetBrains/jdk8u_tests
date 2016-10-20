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
 * Created on 14.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY002;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;


/**
 * Checks the JDWP commands VirtualMachine.ClassesBySignature and VirtualMachine.AllClasses
 * with usual number of loaded classes but under memory stressing in debuggee.
 */
public class MemoryTest002 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY002.MemoryDebuggee002";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

	protected String getDebuggeeClassName() {
		return "org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY002.MemoryDebuggee002";
	}

    /**
     * Checks the JDWP commands VirtualMachine.ClassesBySignature and VirtualMachine.AllClasses
     * with usual number of loaded classes but under memory stressing in debuggee.
     */
    public Result  testMemory002() {

        logWriter.println("==> testMemory002: START (" + new Date() + ")...");
        if ( waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE ) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if ( setupSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }
        resumeDebuggee("#1");
try {
        logWriter.println("==> Wait for debuggee to create memory stressing...");
        printlnForDebug("receiving 'SIGNAL_READY_01' Signal from debuggee...");
        String debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        int expectedClasses = 6;
        String[] expectedClassSignatures = new String[expectedClasses];
        expectedClassSignatures[0] = 
            "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY002/MemoryDebuggee002;";
        expectedClassSignatures[1] = 
            "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY002/Memory002_TestClass01;";
        expectedClassSignatures[2] = 
            "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY002/Memory002_TestClass02;";
        expectedClassSignatures[3] = 
            "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY002/Memory002_TestClass03;";
        expectedClassSignatures[4] = 
            "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY002/Memory002_TestClass04;";
        expectedClassSignatures[5] = 
            "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/MEMORY002/Memory002_TestClass05;";

        int testCaseStatus = SUCCESS;
        
        CommandPacket packet = null;
        ReplyPacket reply = null;
        long commandStartTimeMlsec = 0;
        long commandRunTimeMlsec = 0;
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        for (int i=0; i < expectedClasses; i++) {
            logWriter.println("\n");
            logWriter.println
            ("==> Send VirtualMachine::ClassesBySignature command under memory stressing in debuggee...");
            logWriter.println("    Signature = " + expectedClassSignatures[i]);
            packet = new CommandPacket(
                    JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                    JDWPCommands.VirtualMachineCommandSet.ClassesBySignatureCommand);
            packet.setNextValueAsString(expectedClassSignatures[i]);
            try {
                commandStartTimeMlsec = System.currentTimeMillis();
                reply = debuggeeWrapper.vmMirror.performCommand(packet);
                commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
                logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
                
            } catch ( Throwable thrown ) {
                logWriter.println("## FAILURE: Exception while performCommand() = " + thrown);
                testCaseStatus = FAILURE;
                continue;
            }
            if ( checkReplyForError(reply, expectedErrors,
                                   "VirtualMachine::ClassesBySignature command") ) {
                testCaseStatus = FAILURE;
                continue;
            }
            if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                                      "VirtualMachine::ClassesBySignature command") ) {
                continue;
            }
            int returnedClasses = reply.getNextValueAsInt();
            if ( returnedClasses != 1 ) {
                logWriter.println
                ("## FAILURE: Unexpected number of returned classes = " + returnedClasses);
                logWriter.println
                ("##          Expected number of returned classes = 1");
                testCaseStatus = FAILURE;
                continue;
            }
            byte refTypeTag = reply.getNextValueAsByte();
            long typeID = reply.getNextValueAsReferenceTypeID();
            int classStatus = reply.getNextValueAsInt();
            logWriter.println
            ("==> refTypeTag = " + refTypeTag + "(" + JDWPConstants.TypeTag.getName(refTypeTag) + ")" );
            if ( refTypeTag != JDWPConstants.TypeTag.CLASS ) {
                logWriter.println
                ("## FAILURE: Unexpected refTypeTag!");
                logWriter.println
                ("##          Expected refTypeTag = " + JDWPConstants.TypeTag.CLASS +
                        JDWPConstants.TypeTag.getName(JDWPConstants.TypeTag.CLASS));
                testCaseStatus = FAILURE;
                
            }
            logWriter.println
            ("==> classStatus = " + classStatus + "(" + JDWPConstants.ClassStatus.getName(classStatus) + ")" );
            logWriter.println("==> ReferenceTypeID = " + typeID);
            logWriter.println("==> Check signature for this ReferenceTypeID...");
            String classSignature = getClassSignature(typeID);
            logWriter.println("==> classSignature = " + classSignature);
            if ( ! expectedClassSignatures[i].equals(classSignature) ) {
                logWriter.println("## FAILURE: Unexpected classSignature!");
                testCaseStatus = FAILURE;
            } else {
                logWriter.println("==> OK - it is expected signature!");
            }
        }
        
        
        logWriter.println("\n");
        logWriter.println("==> Send VirtualMachine::AllClasses command " +
                "AFTER creating by debuggee memory stressing...");
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
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## FAILURE: Exception while performCommand()");
        }
        if ( checkReplyForError(reply, expectedErrors,
                               "VirtualMachine::AllClasses command") ) {
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## VirtualMachine::AllClasses command ERROR");
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());
        if ( ! printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                                  "VirtualMachine::AllClasses command") ) {
            
            int notFoundClasses = 0;
            int allClasses = reply.getNextValueAsInt();
            logWriter.println
            ("==> Number of reference types AFTER creating by debugge memory stressing = " + allClasses);

            logWriter.println("\n");
            logWriter.println
            ("==> Check if all expected classes are returned by VirtualMachine::AllClasses command...");
            boolean[] foundClassesArray = new boolean[expectedClassSignatures.length];
            for (int i = 0; i < foundClassesArray.length; i++) {
                foundClassesArray[i] = false;
            }
            
            for (int i = 0; i < allClasses; i++) {
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
                    logWriter.println("\n");
                    logWriter.println("## FAILURE: Expected class is NOT found among returned classes");
                    logWriter.println("##          Expected class signature = " + expectedClassSignatures[i]);
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

        resumeDebuggee("#2");
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testMemory002: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testMemory002: FAILED");
            return failed("testMemory002:");
        } else {
            logWriter.println("==> testMemory002: OK");
            return passed("testMemory002: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testMemory002: FAILED");
    return failed("==> testMemory002: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new MemoryTest002().test(args));
	}
}
