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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT006;

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
 * saves result. Generates another array of Objects with very large length. Runs
 * <code>ArrayReference.GetValues</code> command for the second array of
 * Objects, checks returned ObjectIDs. For each ObjectID returned by both
 * <code>ArrayReference.GetValues</code> command runs
 * <code>ObjectReference.ReferenceType</code> command and checks results.
 */
public class ObjectTest006 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.OBJECT006.ObjectDebuggee006";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public long[] arrayForCompare1 = new long[StressDebuggee.OBJECT006_ARRAY_LENGTH];
    public long[] arrayForCompare2 = new long[StressDebuggee.OBJECT006_ARRAY_LENGTH];
    
    public long[] allThreadIDs;
    int allThreadsNumber;

    /**
     * This tests case exercises the JDWP agent under ObjectIDs stressing. First
     * test generates array of Objects with very large length. Then runs
     * <code>ArrayReference.GetValues</code> command for this array, checks
     * and saves result. Generates another array of Objects with very large
     * length. Runs <code>ArrayReference.GetValues</code> command for the
     * second array of Objects, checks returned ObjectIDs. For each ObjectID
     * returned by both <code>ArrayReference.GetValues</code> command runs
     * <code>ObjectReference.ReferenceType</code> command and checks results.
     */
    public Result  testObject006() {
        logWriter.println("==> testObject006: START (" + new Date() + ")...");

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
            return failed("## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee");
        }
        logWriter.println("==> Check array second time..");
        
        if(checkArray("anotherClassArray", arrayForCompare2) == -1) {
            testCaseStatus = FAILURE;
        }
        
        logWriter.println("==> Get refTypes for objects in both arrays..");
        getRefTypes(arrayForCompare1);
        getRefTypes(arrayForCompare2);
        
        resumeDebuggee("#5");
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testObject006: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testObject006: FAILED");
            return failed("testObject006:");
        } else {
            logWriter.println("==> testObject006: OK");
            return passed("testObject006: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testObject006: FAILED");
    return failed("==> testObject006: Unexpected Exception! ");
}
    }
    
    public int checkArray(String arrayName, long[] arrayForCompare) {
        int success = 0;
        logWriter.println("==> Get values from debuggee's object array...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_04");
            return -1;
        }
        
        long arrayID = getArraIDForStaticArrayField(debuggeeRefTypeID, "classArray");
        logWriter.println("==> ArrayID = "+arrayID);
        
        long commandStartTimeMlsec = System.currentTimeMillis();
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.GetValuesCommand);
        packet.setNextValueAsArrayID(arrayID);
        packet.setNextValueAsInt(0);
        packet.setNextValueAsInt(StressDebuggee.OBJECT006_ARRAY_LENGTH);
        
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if(!(reply.getErrorCode() == JDWPConstants.Error.NONE)){
            logWriter.println("## FAILURE: ArrayReference::GetValues command");
            logWriter.println("ErrorCODE: "+reply.getErrorCode());
            return -1;
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
            arrayForCompare[i] = value.getLongValue();
        }
        long commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
        return success;
    }
    
    public void getRefTypes(long[] objectArray) {
        for (int i=0; i < objectArray.length; i++) {
            long refTypeID = debuggeeWrapper.vmMirror.getReferenceType(objectArray[i]);
                      
        }
    }
    
    public static void main(String[] args) {
        System.exit(new ObjectTest006().test(args));
    }
}
