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
package org.apache.harmony.test.func.jpda.jdwp.scenario.CB02;

import java.util.ArrayList;
import java.util.List;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;

/**
 * Created on 27.06.2005 
 */
public class CB02Test extends JDWPQATestCase {

    private final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.CB02Debuggee";
    private final static String DEBUGGEE_CLASS_SIG = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    private final static String EXCEPTION_CLASS_NAME = 
        "org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.CB02Debuggee_ClassNotFoundException";
    private final static String EXCEPTION_CLASS_SIG = 
        "L" + EXCEPTION_CLASS_NAME.replace('.','/') + ";";
    private final int OCCURRENCE = 5;

    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;        
    }
    
    public Result testCB02_01() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long startLineCodeIndex = reply.getNextValueAsLong();
        int startLineNumber = debuggeeWrapper.vmMirror.getLineNumber
                                (debuggeeClassId, methodId, startLineCodeIndex);

        logWriter.print("Set countable (Count=" + OCCURRENCE + 
                ") breakpoint on line with 'exceptionMethod()' call in 'run' method of CB02Debuggee...");
        int breakpointLine = startLineNumber + CB02Debuggee.RELATIVE_LINE_NUMBER_WITH_exceptionMethod_CALL - 1;
        Breakpoint breakpoint = 
            new Breakpoint(DEBUGGEE_CLASS_SIG, method, 
                    (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setCountableBreakpoint
            (JDWPConstants.TypeTag.CLASS, breakpoint, JDWPConstants.SuspendPolicy.ALL, OCCURRENCE);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            String failureMessage = "The countable breakpoint couldn't be set.";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("OK - countable (Count=" + OCCURRENCE + ") breakpoint is set!");
        
        debuggeeWrapper.vmMirror.resume();
        logWriter.print("Receiving countable BREAKPOINT event...");
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        if ( events.length != 1 ) {
            String failureMessage = "Failed to receive countable BREAKPOINT event: Number of expected events = 1; " 
                    + "Number of received events = " + events.length;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);      
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        logWriter.println("Check whether a breakpoint was triggered on the " + OCCURRENCE + "th iteration...");
        
        reply = debuggeeWrapper.vmMirror.getThreadFrames(threadID, 0, 1);
        int frames = reply.getNextValueAsInt();
        if (frames != 1) {
            String failureMessage = "Failed to retrieve the current frame of the thread '" 
                + debuggeeWrapper.vmMirror.getThreadName(threadID) + "'.";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }       
        
        Frame frame = new Frame();
        frame.setID(reply.getNextValueAsFrameID());
        frame.setThreadID(threadID);
        frame.setLocation(location);
        
        ArrayList localVars = (ArrayList)debuggeeWrapper.vmMirror.getLocalVars(frame);
        if (localVars == null) {
            String failureMessage = "Couldn't get local variables of frame '" + getFrameName(frame) + "'";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        for (int i = 0; i < localVars.size(); i++) {
            Frame.Variable var = (Frame.Variable)localVars.toArray()[i];
            if (!var.getName().equals("exceptionMethodIteration")) {
                localVars.remove(i);
                --i;
            }
        }
        
        if (localVars.size() == 0) {
            String failureMessage = "Couldn't find required variable 'exceptionMethodIteration' " 
                +"in the list of local variables of the frame '" 
                + getFrameName(frame) + "'.";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        frame.setVars(localVars);
        Value[] localVarsValues = debuggeeWrapper.vmMirror.getFrameValues(frame);
        if (localVarsValues.length != 1) {
            String failureMessage = "Wrong length of an array of variables' values.";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        if (localVarsValues[0].getTag() != JDWPConstants.Tag.INT_TAG) {
            String failureMessage = "Wrong type of required variable. Expected 'int' instead of '" 
                + JDWPConstants.Tag.getName(localVarsValues[0].getTag());
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        int iteration = localVarsValues[0].getIntValue();
        if (iteration != OCCURRENCE) {
            String failureMessage = "Expected to stop at breakpoint on " 
                + OCCURRENCE + "th iteration instead of on " + iteration + "th.";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.println("OK - breakpoint was triggered on the expected " + OCCURRENCE + "th iteration.");
        
        logWriter.print("Resume debuggee VM...");
        debuggeeWrapper.vmMirror.resume();        
        
        logWriter.print("testCB02_01 - OK!");
        return passed();
    }
    
    public Result testCB02_02() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        String method = "run";        
        
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long startLineCodeIndex = reply.getNextValueAsLong();
        int startLineNumber = debuggeeWrapper.vmMirror.getLineNumber
                                (debuggeeClassId, methodId, startLineCodeIndex);

        logWriter.print("Set breakpoint on line with 'loopMethod()' call in 'run' method of CB02Debuggee...");
        int breakpointLine = startLineNumber + CB02Debuggee.RELATIVE_LINE_NUMBER_WITH_loopMethod_CALL - 1;

        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine));        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            String failureMessage = "A breakpoint couldn't be set.";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        logWriter.println("OK - Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        logWriter.print("Receiving BREAKPOINT event...");
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        if ( events.length != 1 ) {
            String failureMessage = "Failed to receive BREAKPOINT event: Number of expected events = 1; " 
                    + "Number of received events = " + events.length;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            String failureMessage = "Expected to stop in a method '" + method 
                + "' at line " + breakpointLine + " instead of method '" 
                + methodStopped + "', line " + lineStopped;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("BREAKPOINT event - OK!");
        debuggeeWrapper.vmMirror.clearBreakpoint(reply.getNextValueAsInt());
        
        method = "loopMethod";
        methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        logWriter.print("Set request on countable method's '" + method 
                + "' entry on " + OCCURRENCE + "th loop iteration...");        
        reply = debuggeeWrapper.vmMirror.setCountableMethodEntry(DEBUGGEE_CLASS_NAME, OCCURRENCE);
        logWriter.print("Set request countable on method's '" + method 
                + "' exit on " + OCCURRENCE + "th loop iteration...");
        reply = debuggeeWrapper.vmMirror.setCountableMethodExit(DEBUGGEE_CLASS_NAME, OCCURRENCE);       

        logWriter.print("Resume debuggee VM...");
        debuggeeWrapper.vmMirror.resume();     
        logWriter.print("Receiving countable METHOD_ENTRY event...");
        event = debuggeeWrapper.vmMirror.receiveEvent();
        events = ParsedEvent.parseEventPacket(event);
        if ( events.length != 1 ) {
            String failureMessage = "Failed to receive countable METHOD_ENTRY event: "
                + "Number of expected events = 1; " 
                + "Number of received events = " + events.length;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        if ( events[0].getEventKind() != JDWPConstants.EventKind.METHOD_ENTRY ) {
            String failureMessage = "Failed to receive countable METHOD_ENTRY event: Received event = "
                    + events[0].getEventKind() + "(" 
                    + JDWPConstants.EventKind.getName(events[0].getEventKind())
                    + ") instead of expected METHOD_ENTRY event";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("OK - countable METHOD_ENTRY event received.");
        logWriter.print("Check location.methodID of received event...");
        threadID = ((ParsedEvent.Event_METHOD_ENTRY)events[0]).getThreadID();
        location = ((ParsedEvent.Event_METHOD_ENTRY)events[0]).getLocation();
        if (location.methodID != methodId) {
            String failureMessage = "Unexpected METHOD_ENTRY event is received: Expected location.methodID = "
                    + methodId + "; Received location.methodID = " + location.methodID;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("OK - expected location.methodID (" + methodId + ") is received.");
        logWriter.print("Check iteration...");
        
        List frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
        boolean expectedFrameFound = false;
        for (int j = 0; j < frames.size(); j++) {
            Frame frame = (Frame)frames.toArray()[j];
            if (getFrameName(frame).equals("org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.CB02Debuggee.run()")) {
                expectedFrameFound = true;
                ArrayList localVars = (ArrayList)debuggeeWrapper.vmMirror.getLocalVars(frame);                                  
                for (int k = 0; k < localVars.size(); k++) {
                    Frame.Variable var = (Frame.Variable)localVars.toArray()[k];
                    if (!var.getName().equals("loopMethodIteration")) {
                        localVars.remove(k);
                        --k;
                    }
                }
                
                if (localVars.size() == 0) {
                    String failureMessage = "Couldn't find required variable 'loopMethodIteration' " 
                        + "in the list of local variables of the frame '" + getFrameName(frame) + "'.";
                    logWriter.println("## FAILURE: " + failureMessage);
                    return failed(failureMessage);
                }

                frame.setVars(localVars);
                Value[] localVarsValues = debuggeeWrapper.vmMirror.getFrameValues(frame);
                
                int iteration = localVarsValues[0].getIntValue();
                if (iteration != OCCURRENCE) {
                    String failureMessage = "Expected to stop at breakpoint on " + OCCURRENCE 
                        + "th iteration instead on " + iteration + "th.";
                    logWriter.println("## FAILURE: " + failureMessage);
                    return failed(failureMessage);
                }
                logWriter.print("OK - Iteration is expecxted = " + iteration);
                break;
            }
        }
        if ( ! expectedFrameFound ) {
            String failureMessage = "Could not find expected frame with name = " 
                    + "'org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.CB02Debuggee.run()'";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }

        logWriter.print("Resume debuggee VM...");
        debuggeeWrapper.vmMirror.resume();     
        logWriter.print("Receiving countable METHOD_EXIT event...");
        event = debuggeeWrapper.vmMirror.receiveEvent();
        events = ParsedEvent.parseEventPacket(event);
        if ( events.length != 1 ) {
            String failureMessage = "Failed to receive countable METHOD_EXIT event: " 
                + "Number of expected events = 1; " 
                + "Number of received events = " + events.length;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        if ( events[0].getEventKind() != JDWPConstants.EventKind.METHOD_EXIT ) {
            String failureMessage = "Failed to receive countable METHOD_EXIT event: Received event = "
                + events[0].getEventKind() + "(" + JDWPConstants.EventKind.getName(events[0].getEventKind())
                + ") instead of expected METHOD_EXIT event";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("OK - countable METHOD_EXIT event received.");
        logWriter.print("Check location.methodID of received event...");
        threadID = ((ParsedEvent.Event_METHOD_EXIT)events[0]).getThreadID();
        location = ((ParsedEvent.Event_METHOD_EXIT)events[0]).getLocation();
        if (location.methodID != methodId) {
            String failureMessage = "Unexpected METHOD_EXIT event is received: Expected location.methodID = "
                    + methodId + "; Received location.methodID = " + location.methodID;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("OK - expected location.methodID (" + methodId + ") is received.");
        logWriter.print("Check iteration...");
        
        frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
        expectedFrameFound = false;
        for (int j = 0; j < frames.size(); j++) {
            Frame frame = (Frame)frames.toArray()[j];
            if (getFrameName(frame).equals("org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.CB02Debuggee.run()")) {
                expectedFrameFound = true;
                ArrayList localVars = (ArrayList)debuggeeWrapper.vmMirror.getLocalVars(frame);                                  
                for (int k = 0; k < localVars.size(); k++) {
                    Frame.Variable var = (Frame.Variable)localVars.toArray()[k];
                    if (!var.getName().equals("loopMethodIteration")) {
                        localVars.remove(k);
                        --k;
                    }
                }
                
                if (localVars.size() == 0) {
                    String failureMessage = "Couldn't find required variable 'loopMethodIteration' " 
                        +"in the list of local variables of the frame '" + getFrameName(frame) + "'.";
                    logWriter.println("## FAILURE: " + failureMessage);
                    return failed(failureMessage);
                }

                frame.setVars(localVars);
                Value[] localVarsValues = debuggeeWrapper.vmMirror.getFrameValues(frame);
                
                int iteration = localVarsValues[0].getIntValue();
                if (iteration != OCCURRENCE) {
                    return failed("Expected to stop at breakpoint on " + OCCURRENCE + "th iteration instead on " + iteration + "th.");
                }
                logWriter.print("OK - Iteration is expecxted = " + iteration);
                break;
            }
        }
        if ( ! expectedFrameFound ) {
            String failureMessage = "Could not find expected frame with name = " 
                    + "'org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.CB02Debuggee.run()'";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        logWriter.print("Resume debuggee VM...");
        debuggeeWrapper.vmMirror.resume();
        
        logWriter.print("testCB02_02 - OK!");
        return passed();
    }
    
    public Result testCB02_03() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        String method = "run";        
        
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long startLineCodeIndex = reply.getNextValueAsLong();
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber
                                (debuggeeClassId, methodId, startLineCodeIndex);

        logWriter.print("Set breakpoint on first line in 'run' method of CB02Debuggee...");

        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)startLineCodeIndex);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            String failureMessage = "A breakpoint couldn't be set.";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        logWriter.println("OK - Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        logWriter.print("Receiving BREAKPOINT event...");
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        if ( events.length != 1 ) {
            String failureMessage = "Failed to receive BREAKPOINT event: Number of expected events = 1; " 
                    + "Number of received events = " + events.length;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            String failureMessage = "Expected to stop in a method '" + method + "' at line " 
                + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        logWriter.print("BREAKPOINT event - OK!");
        debuggeeWrapper.vmMirror.clearBreakpoint(reply.getNextValueAsInt());
        
        logWriter.print("Set request for countable (Count=" + OCCURRENCE + 
            ") EXCEPTION event: Exception class = '" + EXCEPTION_CLASS_NAME + "' ...");
        reply = debuggeeWrapper.vmMirror.setCountableException(EXCEPTION_CLASS_SIG, true, false, OCCURRENCE);
        logWriter.println("Resume debuggee VM...");
        debuggeeWrapper.vmMirror.resume();       

        logWriter.print("Receiving countable EXCEPTION event...");
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.EXCEPTION);
        events = ParsedEvent.parseEventPacket(event);
        if ( events.length != 1 ) {
            String failureMessage = "Failed to receive countable EXCEPTION event: Number of expected events = 1; " 
                    + "Number of received events = " + events.length;
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        threadID = ((ParsedEvent.Event_EXCEPTION)events[0]).getThreadID();
        TaggedObject exception = ((ParsedEvent.Event_EXCEPTION)events[0]).getException();
        String exceptionSignature = debuggeeWrapper.vmMirror.getClassSignature(debuggeeWrapper.vmMirror.getReferenceType(exception.objectID));        
        logWriter.print("OK - the EXCEPTION event '" + exceptionSignature.substring(1, exceptionSignature.length() - 1).replace('/', '.') + "' has been received.");
        logWriter.print("Check iteration...");
        
        boolean expectedFrameFound = false;
        List frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
        for (int i = 0; i < frames.size(); i++) {
            Frame frame = (Frame)frames.toArray()[i];
            if (getFrameName(frame).equals("org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.CB02Debuggee.run()")) {
                expectedFrameFound = true;
                ArrayList localVars = (ArrayList)debuggeeWrapper.vmMirror.getLocalVars(frame);                                    
                for (int k = 0; k < localVars.size(); k++) {
                    Frame.Variable var = (Frame.Variable)localVars.toArray()[k];
                    if (!var.getName().equals("exceptionMethodIteration")) {
                        localVars.remove(k);
                        --k;
                    }
                }
                
                if (localVars.size() == 0) {
                    String failureMessage = "Couldn't find required variable 'exceptionMethodIteration' "
                        + "in the list of local variables of the frame '" + getFrameName(frame) + "'.";
                    logWriter.println("## FAILURE: " + failureMessage);
                    return failed(failureMessage);
                }

                frame.setVars(localVars);
                Value[] localVarsValues = debuggeeWrapper.vmMirror.getFrameValues(frame);
                
                int iteration = localVarsValues[0].getIntValue();
                if (iteration != OCCURRENCE) {
                    String failureMessage = "Expected to stop at EXCEPTION event on " 
                        + OCCURRENCE + "th iteration instead of on " + iteration + "th.";
                    logWriter.println("## FAILURE: " + failureMessage);
                    return failed(failureMessage);
                }
                logWriter.print("OK - Iteration is expecxted = " + iteration);
                break;
            }
        }
        if ( ! expectedFrameFound ) {
            String failureMessage = "Could not find expected frame with name = " 
                    + "'org.apache.harmony.test.func.jpda.jdwp.scenario.CB02.CB02Debuggee.run()'";
            logWriter.println("## FAILURE: " + failureMessage);
            return failed(failureMessage);
        }
        
        logWriter.println("Resume debuggee VM...");
        debuggeeWrapper.vmMirror.resume();       
        
        logWriter.print("testCB02_03 - OK!");
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new CB02Test().test(args));
    }
}
