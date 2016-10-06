/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.jpda.jdwp.scenario.EB01;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;

/**
 * Created on 20.06.2005
 */
public class EB01Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.func.jpda.jdwp.scenario.EB01.EB01Debuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    private final static String LOADED_CLASS_NAME = 
        "org.apache.harmony.test.func.jpda.jdwp.scenario.EB01.EB01Debuggee_Tested_Class";
    private final static String LOADED_CLASS_SIG = "L" + LOADED_CLASS_NAME.replace('.', '/') + ";";    

    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testEB01() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        logWriter.print("Set request for CLASS_PREPARE event for '" + LOADED_CLASS_NAME + "' class...");        
        reply = debuggeeWrapper.vmMirror.setClassPrepared(LOADED_CLASS_NAME);
        int requestID = reply.getNextValueAsInt();
        logWriter.print("OK - request for CLASS_PREPARE event is set: RequestID = " + requestID);        

        logWriter.println("Resume debuggee VM...");
        debuggeeWrapper.vmMirror.resume();
        logWriter.println("Receiving CLASS_PREPARE event...");
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event); 
        if ( events.length != 1 ) {
            String failureMessage = "Failed to receive CLASS_PREPARE event: Number of expected events = 1; " 
                    + "Number of received events = " + events.length;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.println("OK - CLASS_PREPARE event is received: Check it....");
        
        boolean testCaseIsOk = true;
        int eventRequestID = events[0].getRequestID();
        logWriter.println("Received event RequestID = " + eventRequestID);
        if ( requestID != eventRequestID ) {
            logWriter.println("## FAILURE: Unexpected Received event RequestID!");
            logWriter.println("##          Expected RequestID = " + requestID);
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected RequestID");
        }
        
        byte refTypeTag = ((ParsedEvent.Event_CLASS_PREPARE)events[0]).getRefTypeTag();
        logWriter.println("refTypeTag = " + refTypeTag 
            + "(" + JDWPConstants.TypeTag.getName(refTypeTag) + ")");
        if ( refTypeTag != JDWPConstants.TypeTag.CLASS ) {
            logWriter.println("## FAILURE: Unexpected refTypeTag!");
            logWriter.println("##          Expected refTypeTag = " + JDWPConstants.TypeTag.CLASS
                + "(" + JDWPConstants.TypeTag.getName(JDWPConstants.TypeTag.CLASS) + ")");
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected refTypeTag");
        }
        
        String preparedClassSignature = ((ParsedEvent.Event_CLASS_PREPARE)events[0]).getSignature();
        logWriter.println("preparedClassSignature = " + preparedClassSignature);
        if (!LOADED_CLASS_SIG.equals(preparedClassSignature) ) {
            logWriter.println("## FAILURE: Unexpected preparedClassSignature!");
            logWriter.println("##          Expected preparedClassSignature = '" + LOADED_CLASS_SIG + "'");
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected preparedClassSignature");
        }
        
        int referenceTypeStatus = ((ParsedEvent.Event_CLASS_PREPARE)events[0]).getStatus();
        logWriter.println("referenceTypeStatus = 0x" + Integer.toHexString(referenceTypeStatus)
            + "(" + JDWPConstants.ClassStatus.getName(referenceTypeStatus) + ")");
        if ( (referenceTypeStatus & JDWPConstants.ClassStatus.PREPARED) != JDWPConstants.ClassStatus.PREPARED ) {
            logWriter.println("## FAILURE: Unexpected referenceTypeStatus!");
            logWriter.println("##          Expected referenceTypeStatus has to contain " 
                + "'PREPARED' bit = 0x" + Integer.toHexString(JDWPConstants.ClassStatus.PREPARED));
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected referenceTypeStatus");
        }

        if ( ! testCaseIsOk ) {
            return failed("Failed to receive CLASS_PREPARE event: " 
                    + "received CLASS_PREPARE event has unexpected attribute(s)!");
        }
        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();      
    
        logWriter.print("testEB01 - OK");
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new EB01Test().test(args));
    }
}
