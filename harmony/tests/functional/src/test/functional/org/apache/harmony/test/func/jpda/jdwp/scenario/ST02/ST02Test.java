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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST02;

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
public class ST02Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.func.jpda.jdwp.scenario.ST02.ST02Debuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testST02() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();
        
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
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        logWriter.print("Check whether BREAKPOINT was received with the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            String failureMessage = "Expected to stop in a method '" + method + "' at line " 
                + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("BREAKPOINT is OK");
        
        method = "stepMethod";
        int stepsBeforeInvokeMethod = 5;
        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
        int expectedStoppedLine = breakpointLine;
        int stoppedLine = 0;
        logWriter.print("Set " + stepsBeforeInvokeMethod 
                + " steps with StepDepth=OVER before stopping at the line with method '" 
                + method + "' invocation");
        for (int i = 0; i < stepsBeforeInvokeMethod; i++) {
            logWriter.println(" ");
            logWriter.println("Set SINGLE_STEP request number # " + (i+1));
            reply = debuggeeWrapper.vmMirror.setStep
                    (threadName, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.OVER);            
            logWriter.println("OK - SINGLE_STEP request is set");
            logWriter.println("Resume debuggee VM");
            debuggeeWrapper.vmMirror.resume();
            logWriter.println("Receiving SINGLE_STEP event...");
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
                        + events[0].getEventKind() + "(" 
                        + JDWPConstants.EventKind.getName(events[0].getEventKind())
                        + ") instead of expected SINGLE_STEP event";
                logWriter.println("## FAILURE: " + failureMessage);
                return failed(failureMessage);
            }

            logWriter.println("OK - SINGLE_STEP event is received. Check stopped line...");
                    
            location = ((ParsedEvent.Event_SINGLE_STEP)events[0]).getLocation();
            stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
            logWriter.print("Stopped line = " + stoppedLine);
            expectedStoppedLine++;
            if ( expectedStoppedLine != stoppedLine ) {
                String failureMessage = "Failed to receive SINGLE_STEP event: Unexpected Stopped line = "
                        + stoppedLine + "; Expected Stopped line = " + expectedStoppedLine;
                logWriter.println("## FAILURE: " + failureMessage);
                return failed(failureMessage);
            }
            logWriter.print("OK - it is expected Stopped line");
            
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }

        logWriter.println(" ");
        logWriter.print("Set final step with StepDepth=OVER and check that " 
            + "Debuggee is NOT stopped inside the method '" + method + "'");
        reply = debuggeeWrapper.vmMirror.setStep(threadName, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.OVER);            
        logWriter.println("OK - SINGLE_STEP request is set");
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        logWriter.println("Receiving SINGLE_STEP event...");
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
                    + events[0].getEventKind() + "(" 
                    + JDWPConstants.EventKind.getName(events[0].getEventKind())
                    + ") instead of expected SINGLE_STEP event";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }

        logWriter.println("OK - SINGLE_STEP event is received. Check stopped class, method and line...");
        method = "run";
        methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        boolean testCaseIsOk = true;
        location = ((ParsedEvent.Event_SINGLE_STEP)events[0]).getLocation();
        long stoppedClassID = location.classID;
        logWriter.println("Stopped ClassID = " + stoppedClassID);
        if ( stoppedClassID != debuggeeClassId ) {
            logWriter.println("## FAILURE: Unexpected Stopped ClassID!");
            logWriter.println("##          Expected Stopped ClassID = " + debuggeeClassId);
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected Stopped ClassID");
        }
        String stoppedClassSignature = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
        logWriter.println("Stopped ClassSignature = '" + stoppedClassSignature + "'");
        if ( ! DEBUGGEE_CLASS_SIG.equals(stoppedClassSignature) ) {
            logWriter.println("## FAILURE: Unexpected Stopped ClassSignature!");
            logWriter.println("##          Expected Stopped ClassSignature = '" + DEBUGGEE_CLASS_SIG + "'");
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected Stopped ClassSignature");
        }
        long stoppedMethodID = location.methodID;
        logWriter.println("Stopped MethodID = " + stoppedMethodID);
        if ( stoppedMethodID != methodId ) {
            logWriter.println("## FAILURE: Unexpected Stopped MethodID!");
            logWriter.println("##          Expected Stopped MethodID = " + methodId);
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected Stopped MethodID");
        }
        String stoppedMethodName = debuggeeWrapper.vmMirror.getMethodName(stoppedClassID, stoppedMethodID);                
        logWriter.println("Stopped MethodName = '" + stoppedMethodName + "'");
        if ( ! method.equals(stoppedMethodName) ) {
            logWriter.println("## FAILURE: Unexpected Stopped MethodName!");
            logWriter.println("##          Expected Stopped MethodName = '" + method + "'");
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected Stopped MethodName");
        }
        expectedStoppedLine++;
        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(stoppedClassID, stoppedMethodID, location.index);
        logWriter.println("Stopped Line = " + stoppedLine);
        if ( stoppedLine != expectedStoppedLine ) {
            logWriter.println("## FAILURE: Unexpected Stopped Line!");
            logWriter.println("##          Expected Stopped Line = " + expectedStoppedLine);
            testCaseIsOk = false;
        } else {
            logWriter.println("OK - it is expected Stopped Line");
        }
        if ( ! testCaseIsOk ) {
            return failed("Failed to receive SINGLE_STEP event: " 
                    + "received SINGLE_STEP event has unexpected attribute(s)!");
        }
        
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();      
    
        logWriter.print("testST02 - OK");
        return passed();
        
    }

    public static void main(String[] args) {
        System.exit(new ST02Test().test(args));
    }
}
