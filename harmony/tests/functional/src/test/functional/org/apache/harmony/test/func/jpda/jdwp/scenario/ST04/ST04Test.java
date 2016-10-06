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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST04;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;

/**
 * Created on 10.06.2005 
 */
public class ST04Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.func.jpda.jdwp.scenario.ST04.ST04Debuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testST04() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();
        int runMethodStartLineNumber = debuggeeWrapper.vmMirror.getLineNumber
                                        (debuggeeClassId, methodId, start);

        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)start);
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            String failureMessage = "A breakpoint couldn't be set.";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start); 
        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || stoppedLine != breakpointLine) {
            String failureMessage = "Expected to stop in a method '" 
                + method + "' at line " + breakpointLine + " instead of method '" 
                + methodStopped + "', line " + stoppedLine;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("BREAKPOINT is OK");
        
        method = "stepMethod";
        methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            String failureMessage = "Couldn't obtain line table information of the method '" + method + "'";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        long startLineIndex = reply.getNextValueAsLong();
        int stepMethodStartLineNumber = 
            debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, startLineIndex);
        
        int previousLine = stoppedLine;
        int lineAfterMethodInvocation = runMethodStartLineNumber 
            + ST04Debuggee.RELATIVE_LINE_NUMBER_WITH_stepMethod_CALL; // line after 'stepMethod' call
        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);        
        logWriter.print("Set steps by a bytecode instruction with StepDepth=OVER "
            + "untill line after 'stepMethod' call (" + lineAfterMethodInvocation + ") "
            + "is reached and check that Debuggee is NOT stopped inside the method '" + method + "'");
        while (stoppedLine != lineAfterMethodInvocation) {
            reply = debuggeeWrapper.vmMirror.setStep(threadName, JDWPConstants.StepSize.MIN, JDWPConstants.StepDepth.OVER);            
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();

            events = ParsedEvent.parseEventPacket(event);
            if ( events.length != 1 ) {
                String failureMessage = "Failed to receive SINGLE_STEP event: Number of expected events = 1; " 
                    + "Number of received events = " + events.length;
                logWriter.println("## FAILURE: " + failureMessage);
                return failed(failureMessage);
            }
            if ( events[0].getEventKind() != JDWPConstants.EventKind.SINGLE_STEP ) {
                String failureMessage = "Failed to receive SINGLE_STEP event: Received event = "
                    + events[0].getEventKind() + "(" + JDWPConstants.EventKind.getName(events[0].getEventKind())
                    + ") instead of expected SINGLE_STEP event";
                logWriter.println("## FAILURE: " + failureMessage);
                return failed(failureMessage);
            }
                    
            location = ((ParsedEvent.Event_SINGLE_STEP)events[0]).getLocation();
            stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
            String stoppedMetod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
            if (stoppedLine != previousLine) {
                previousLine = stoppedLine;
                logWriter.print("Stopped in the method '" + stoppedMetod + "', line = " + stoppedLine);
            }

            if (stoppedLine == stepMethodStartLineNumber) {
                String failureMessage = "Debuggee is stopped inside the method '" + method + "'";
                logWriter.println("## FAILURE: " + failureMessage);
                return failed(failureMessage);
            }
            
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }        

        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        logWriter.print("testST04 - OK");
        return passed();
        
    }

    public static void main(String[] args) {
        System.exit(new ST04Test().test(args));
    }
}
