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
 * Created on 15.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY004;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Value;


/**
 * This test case exercises the JDWP command <code>ArrayReference.GetValues</code>
 * for array with very big number of elements of Object type.
 */
public class MemoryTest004 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY004.MemoryDebuggee004";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case exercises the JDWP command <code>ArrayReference.GetValues</code>
     * for array with very big number of elements of Object type.
     */
    public Result  testMemory004() {

        logWriter.println("==> testMemory004: START (" + new Date() + ")...");
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
        
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_03");
           return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        String smallObjectArrayFieldName = "smallObjectArray";
        long smallObjectArrayID = 
            getArraIDForStaticArrayField(debuggeeRefTypeID, smallObjectArrayFieldName);
        if ( smallObjectArrayID == BAD_ARRAY_ID ) {
            logWriter.println("## FAILURE: Can NOT get smallObjectArrayID");
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## Can NOT get smallObjectArrayID");
        }
        printlnForDebug("smallObjectArrayID = " + smallObjectArrayID);

        logWriter.println("\n");
        logWriter.println("==> Send ArrayReference::GetValues command for smallObjectArray...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.GetValuesCommand);
        packet.setNextValueAsArrayID(smallObjectArrayID);
        packet.setNextValueAsInt(0); // first index
        packet.setNextValueAsInt(MemoryDebuggee004.SMALL_ARRAY_LENGTH); // The number of components to retrieve
        long commandStartTimeMlsec = System.currentTimeMillis();
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        long commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
        printlnForDebug("reply.getLength()= " + reply.getLength());

        int testCaseStatus = SUCCESS;

        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "ArrayReference::GetValues command") ) {
            testCaseStatus = FAILURE;
        } else {
            logWriter.println("\n");
            logWriter.println("==> Check of returned elements of smallObjectArray...");
            byte arrayElementsTag = reply.getNextValueAsByte();
            int valuesNumber = reply.getNextValueAsInt();
            logWriter.println("==> Received array elements tag = " + arrayElementsTag
                    + "(" + JDWPConstants.Tag.getName(arrayElementsTag) + ")");
            if ( arrayElementsTag != JDWPConstants.Tag.OBJECT_TAG ) {
                logWriter.println("## FAILURE: unexpected array elements tag!");
                logWriter.println("##          Expected array elements tag = " +
                        JDWPConstants.Tag.OBJECT_TAG
                        + "(" + JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG) + ")");
                testCaseStatus = FAILURE;
            }
            logWriter.println("==> Received values number = " + valuesNumber);
            if ( valuesNumber != MemoryDebuggee004.SMALL_ARRAY_LENGTH ) {
                logWriter.println("## FAILURE: unexpected values number!");
                logWriter.println("##          Expected values number = " +
                        MemoryDebuggee004.SMALL_ARRAY_LENGTH);
                testCaseStatus = FAILURE;
            } 
            if ( testCaseStatus != FAILURE ) {
                limitedPrintlnInit(20);
                for (int i = 0; i < valuesNumber; i++ ) {
                    Value value = reply.getNextValueAsValue();
                    byte elemTag = value.getTag();
                    if ( elemTag != JDWPConstants.Tag.OBJECT_TAG ) {
                        boolean toPrn = limitedPrintln
                        ("## FAILURE: unexpected tag for array element number: " + i);
                        if ( toPrn ) {
                            logWriter.println("##          Received tag = " +
                                    elemTag
                                    + "(" + JDWPConstants.Tag.getName(elemTag) + ")");
                            logWriter.println("##          Expected tag = " +
                                    JDWPConstants.Tag.OBJECT_TAG
                                    + "(" + JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG) + ")");
                        }
                        testCaseStatus = FAILURE;
                    } else {
                        long objectID = value.getLongValue();
                        String objectSugnature = getObjectSignature(objectID);
                        if ( ! DEBUGGEE_SIGNATURE.equals(objectSugnature) ) {
                            boolean toPrn = limitedPrintln
                            ("## FAILURE: unexpected signature for array element number: " + i);
                            if ( toPrn ) {
                                logWriter.println("##          Received signature = " + objectSugnature);
                                logWriter.println("##          Expected signature = " + DEBUGGEE_SIGNATURE);
                            }
                            testCaseStatus = FAILURE;
                        }
                    }
                }
            }
            if ( testCaseStatus != FAILURE ) {
                logWriter.println("==> Check of returned elements of smallObjectArray - OK.");
            }
        }
        resumeDebuggee("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create array with very big number of objects...");

        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal =  receiveSignalWithWait();
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        String bigObjectArrayFieldName = "bigObjectArray";
        long bigObjectArrayID = 
            getArraIDForStaticArrayField(debuggeeRefTypeID, bigObjectArrayFieldName);
        if ( bigObjectArrayID == BAD_ARRAY_ID ) {
            logWriter.println("## FAILURE: Can NOT get bigObjectArrayID");
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## Can NOT get bigObjectArrayID");
        }
        printlnForDebug("bigObjectArrayID = " + bigObjectArrayID);

        logWriter.println("==> Get bigObjectArray actual length...");
        int bigObjectArrayLength =
            getIntValueForStaticField(debuggeeRefTypeID, "bigObjectArrayActualLength");
        if ( bigObjectArrayLength == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get bigObjectArrayLength");
            terminateDebuggee(FAILURE, "MARKER_07");
            return failed("## Can NOT get bigObjectArrayLength");
        }
        logWriter.println("==> bigObjectArrayLength = " + bigObjectArrayLength);
        
        logWriter.println("\n");
        logWriter.println("==> Send ArrayReference::GetValues command for bigObjectArray...");
        packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.GetValuesCommand);
        packet.setNextValueAsArrayID(bigObjectArrayID);
        packet.setNextValueAsInt(0); // first index
        packet.setNextValueAsInt(bigObjectArrayLength); // The number of components to retrieve
        try {
            commandStartTimeMlsec = System.currentTimeMillis();
            reply = debuggeeWrapper.vmMirror.performCommand(packet, currentTimeout());
            commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
            logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
            
        } catch ( Throwable thrown ) {
            logWriter.println("## FAILURE: Exception while performCommand() = " + thrown);
            terminateDebuggee(FAILURE, "MARKER_08");
            return failed("## FAILURE: Exception while performCommand()");
        }
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        if ( checkReplyForError(reply, expectedErrors,
                               "ArrayReference::GetValues command") ) {
            resumeDebuggee("#3");
            return failed("## ArrayReference::GetValues command ERROR");
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());
        if ( ! printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                                  "ArrayReference::GetValues command") ) {
            logWriter.println("\n");
            logWriter.println("==> Check of returned elements of bigObjectArray...");
            int checkStatus = SUCCESS;
            byte arrayElementsTag = reply.getNextValueAsByte();
            int valuesNumber = reply.getNextValueAsInt();
            logWriter.println("==> Received array elements tag = " + arrayElementsTag
                    + "(" + JDWPConstants.Tag.getName(arrayElementsTag) + ")");
            if ( arrayElementsTag != JDWPConstants.Tag.OBJECT_TAG ) {
                logWriter.println("## FAILURE: unexpected array elements tag!");
                logWriter.println("##          Expected array elements tag = " +
                        JDWPConstants.Tag.OBJECT_TAG
                        + "(" + JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG) + ")");
                checkStatus = FAILURE;
            }
            logWriter.println("==> Received values number = " + valuesNumber);
            if ( valuesNumber != bigObjectArrayLength ) {
                logWriter.println("## FAILURE: unexpected values number!");
                logWriter.println("##          Expected values number = " +
                        bigObjectArrayLength);
                checkStatus = FAILURE;
            } 
            if ( checkStatus != FAILURE ) {
                limitedPrintlnInit(20);
                for (int i = 0; i < valuesNumber; i++ ) {
                    Value value = reply.getNextValueAsValue();
                    byte elemTag = value.getTag();
                    if ( elemTag != JDWPConstants.Tag.OBJECT_TAG ) {
                        boolean toPrn = limitedPrintln
                        ("## FAILURE: unexpected tag for array element number: " + i);
                        if ( toPrn ) {
                            logWriter.println("##          Received tag = " +
                                    elemTag
                                    + "(" + JDWPConstants.Tag.getName(elemTag) + ")");
                            logWriter.println("##          Expected tag = " +
                                    JDWPConstants.Tag.OBJECT_TAG
                                    + "(" + JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG) + ")");
                        }
                        checkStatus = FAILURE;
                    } else {
                        long objectID = value.getLongValue();
                        String objectSugnature = getObjectSignature(objectID);
                        if ( ! DEBUGGEE_SIGNATURE.equals(objectSugnature) ) {
                            boolean toPrn = limitedPrintln
                            ("## FAILURE: unexpected signature for array element number: " + i);
                            if ( toPrn ) {
                                logWriter.println("##          Received signature = " + objectSugnature);
                                logWriter.println("##          Expected signature = " + DEBUGGEE_SIGNATURE);
                            }
                            checkStatus = FAILURE;
                        }
                    }
                }
            }
            if ( checkStatus != FAILURE ) {
                logWriter.println("==> Check of returned elements of bigObjectArray - OK.");
            } else {
                testCaseStatus = FAILURE;
            }
        }

        resumeDebuggee("#4");
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testMemory004: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testMemory004: FAILED");
            return failed("testMemory004:");
        } else {
            logWriter.println("==> testMemory004: OK");
            return passed("testMemory004: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testMemory004: FAILED");
    return failed("==> testMemory004: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new MemoryTest004().test(args));
	}
}
