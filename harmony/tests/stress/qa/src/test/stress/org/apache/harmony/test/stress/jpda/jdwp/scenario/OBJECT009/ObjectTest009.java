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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT009;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;
import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.ArrayRegion;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Value;


/**
 * This tests case exercises the JDWP agent under ObjectIDs stressing. First
 * test generates array of Objects with very large length. Then runs
 * <code>ArrayReference.GetValues</code> command for this array, checks and
 * saves result. Generates another array of Objects with the same length, but
 * with null values, creates memory stressing by loading in memory very big
 * array. Runs <code>ArrayReference.SetValues</code> command and fills the
 * second array with values from the first array, returned by
 * <code>ArrayReference.GetValues</code> command. Checks if both arrays
 * contain the same objects.
 */
public class ObjectTest009 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT009.ObjectDebuggee009";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Value[] arrayForCompare1 = new Value[StressDebuggee.OBJECT009_ARRAY_LENGTH];
    public Value[] arrayForCompare2 = new Value[StressDebuggee.OBJECT009_ARRAY_LENGTH];
    
    public long[] allThreadIDs;
    int allThreadsNumber;

    /**
     * This tests case exercises the JDWP agent under ObjectIDs stressing. First
     * test generates array of Objects with very large length. Then runs
     * <code>ArrayReference.GetValues</code> command for this array, checks and
     * saves result. Generates another array of Objects with the same length, but
     * with null values, creates memory stressing by loading in memory very big
     * array. Runs <code>ArrayReference.SetValues</code> command and fills the
     * second array with values from the first array, returned by
     * <code>ArrayReference.GetValues</code> command. Checks if both arrays
     * contain the same objects.
     */
    public Result  testObject009() {
        logWriter.println("==> testObject009: START (" + new Date() + ")...");

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
        String debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        
        int testCaseStatus = SUCCESS;
        resumeDebuggee("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create big array of objects...");

        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        if(checkArray("classArray", arrayForCompare1) == -1) {
            testCaseStatus = FAILURE;
        }
        resumeDebuggee("#4");
        
        printlnForDebug("receiving 'SIGNAL_READY_03' Signal from debuggee...");
        debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_03.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }        
        logWriter.println("==> Check array second time..");
        
        if (setArray("anotherClassArray", arrayForCompare1) == -1) {
            testCaseStatus = FAILURE;
        } else {
            if (checkArray("anotherClassArray", arrayForCompare2) == -1) {
                testCaseStatus = FAILURE;
            }       
        }
        
        if ( testCaseStatus != FAILURE ) {
            logWriter.println("==> Compare objects in both arrays..");
            testCaseStatus = compareArrays();
        }
        
        resumeDebuggee("#5");
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testObject009: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testObject009: FAILED");
            return failed("testObject009:");
        } else {
            logWriter.println("==> testObject009: OK");
            return passed("testObject009: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testObject009: FAILED");
    return failed("==> testObject009: Unexpected Exception! ");
}
    }
    
    public int checkArray(String arrayName, Value[] arrayForCompare) {
        int success = 0;
        logWriter.println("==> Get values from debuggee's object array...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_04");
            success = -1;
        }
        long arrayID = getArraIDForStaticArrayField(debuggeeRefTypeID, "classArray");
        
        logWriter.println("==> ArrayID = "+arrayID);
        
        long commandStartTimeMlsec = System.currentTimeMillis();
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.GetValuesCommand);
        packet.setNextValueAsArrayID(arrayID);
        packet.setNextValueAsInt(0);
        packet.setNextValueAsInt(StressDebuggee.OBJECT009_ARRAY_LENGTH);
        
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if(!(reply.getErrorCode() == JDWPConstants.Error.NONE)){
            logWriter.println("## FAILURE: ArrayReference::GetValues command");
            logWriter.println("ErrorCODE: "+reply.getErrorCode());
            success = -1;
        }
        ArrayRegion region = reply.getNextValueAsArrayRegion();
        byte arrayTag = region.getTag();
        logWriter.println("==> arrayTag =  " + arrayTag
                + "(" + JDWPConstants.Tag.getName(arrayTag) + ")");
        logWriter.println("==> arrayLength =  "+region.getLength());
        Value value_0 = region.getValue(0);
        byte elementTag = value_0.getTag();
        logWriter.println("==> elementTag =  " + elementTag
                + "(" + JDWPConstants.Tag.getName(elementTag) + ")");
        for (int i=0; i<region.getLength(); i++) {
            Value value = region.getValue(i);
            arrayForCompare[i] = value;
        }
        long commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
        return success;
    }
    
    public int setArray(String arrayName, Value[] objectArray) {
        int success = 0;
        logWriter.println("==> Get values from debuggee's object array...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_04");
            success = -1;
        }
        logWriter.println("debuggeeRefTypeID = " + debuggeeRefTypeID);
        
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.FieldsCommand);
        packet.setNextValueAsReferenceTypeID(debuggeeRefTypeID);
        
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        long fieldID = 0;
        if(!(reply.getErrorCode() == JDWPConstants.Error.NONE)){
            logWriter.println("## FAILURE: FieldsCommand");
            logWriter.println("ErrorCODE: "+reply.getErrorCode());
            return -1;
        }
        int declared = reply.getNextValueAsInt();
        for (int i = 0; i < declared; i++) {
            fieldID = reply.getNextValueAsFieldID();
            String name = reply.getNextValueAsString();
            reply.getNextValueAsString();
            reply.getNextValueAsInt();
            if (name.equals(arrayName))
                break;
        }
        logWriter.println("==> FieldID = " + fieldID);
        
        packet = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.GetValuesCommand);
        // set referenceTypeID
        packet.setNextValueAsReferenceTypeID(debuggeeRefTypeID);
        // repeat 1 time
        packet.setNextValueAsInt(1);
        // set fieldID
        packet.setNextValueAsFieldID(fieldID);
        
        reply = debuggeeWrapper.vmMirror.performCommand(packet);
        
        if(!(reply.getErrorCode() == JDWPConstants.Error.NONE)){
            logWriter.println("## FAILURE: GetValuesCommand");
            logWriter.println("ErrorCODE: "+reply.getErrorCode()+" ("+JDWPConstants.Error.getName(reply.getErrorCode()) + ") ");
            success = -1;
        }
        
        int numberOfValues = reply.getNextValueAsInt();
        if (numberOfValues != 1) {
            logWriter.println("##FAILURE: number of returned values after ReferenceType::GetValues command = " + numberOfValues + " instead of 1");
        }
        Value value = reply.getNextValueAsValue();
        long arrayID = value.getLongValue();
        
        logWriter.println("==> ArrayID = "+arrayID);
        
        long commandStartTimeMlsec = System.currentTimeMillis();
        packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.SetValuesCommand);
        packet.setNextValueAsArrayID(arrayID);
        packet.setNextValueAsInt(0);
        packet.setNextValueAsInt(StressDebuggee.OBJECT009_ARRAY_LENGTH);
        
        for (int i=0; i < StressDebuggee.OBJECT009_ARRAY_LENGTH; i++) {
            packet.setNextValueAsObjectID(objectArray[i].getLongValue());
        }
        
        reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if(!(reply.getErrorCode() == JDWPConstants.Error.NONE)){
            logWriter.println("## FAILURE: ArrayReference::GetValues command");
            logWriter.println("ErrorCODE: "+reply.getErrorCode());
            success = -1;
        }
        
        long commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
        return success;
    }
    
    public int compareArrays() {
        int success = 0;
        int numberOfEquals = 0;
        for (int i=0; i < StressDebuggee.OBJECT009_ARRAY_LENGTH; i++) {
          long value = arrayForCompare1[i].getLongValue();
          for (int j=0; j < StressDebuggee.OBJECT009_ARRAY_LENGTH; j++) {  
            if (value == arrayForCompare2[j].getLongValue()) {
                numberOfEquals++;
            }
          }
        }
        if (numberOfEquals != StressDebuggee.OBJECT009_ARRAY_LENGTH) {
            logWriter.println("##FAILURE: old values are not equals to new values");
            success = -1;
        }
        return success;
    }
    
    public static void main(String[] args) {
        System.exit(new ObjectTest009().test(args));
    }
}
