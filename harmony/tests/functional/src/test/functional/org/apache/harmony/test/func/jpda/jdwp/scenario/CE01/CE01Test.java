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
package org.apache.harmony.test.func.jpda.jdwp.scenario.CE01;

import java.util.ArrayList;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;

/**
 * Created on 28.06.2005 
 */
public class CE01Test extends JDWPQATestCase {

    private final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    private final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testCE01_01() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
 
        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long startLineCodeIndex = reply.getNextValueAsLong();
        int startLineNumber = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, startLineCodeIndex);
        
        int breakpointLine = startLineNumber + 30; // after expresionVar variable has been calculated and printed
        long breakpointLineCodeIndex = debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine);
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)breakpointLineCodeIndex);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            debuggeeWrapper.vmMirror.resume();
            return failed("A breakpoint couldn't be set.");
        }
        

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
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");
        
        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
        reply = debuggeeWrapper.vmMirror.getThreadFrames(threadID, 0, 1);
        int frames = reply.getNextValueAsInt();
        if (frames == 0) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Couldn't get a current frame of the thread '" + threadName + "'");
        }
        
        Frame frame = new Frame();
        frame.setID(reply.getNextValueAsFrameID());
        frame.setLocation(reply.getNextValueAsLocation());
        frame.setThreadID(threadID);
        
        ArrayList localVars = (ArrayList)debuggeeWrapper.vmMirror.getLocalVars(frame);
        if (localVars == null || localVars.size() == 0) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Couldn't get local variables of the frame '" + getFrameName(frame) + "'");
        }
        
        for (int i = 0; i < localVars.size(); i++) {
            Frame.Variable var = (Frame.Variable)localVars.toArray()[i];
            if (!var.getName().equals("expressionVar")) {
                localVars.remove(i);
                --i;
            }
        }
        
        if (localVars.size() == 0) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Couldn't find variable 'expression_var' among local variables of the frame '" + getFrameName(frame) + "'");
        }
        
        frame.setVars(localVars);
        Value[] localVarValue = debuggeeWrapper.vmMirror.getFrameValues(frame);
        
        int expressionVar = localVarValue[0].getIntValue();
        logWriter.print("A variable which was calculated through method's 'invokeMethod' call in the target VM has a value: " + expressionVar);
        logWriter.print("Invoking the same method in a debuggee VM and check whether the returned value is equal to the calculated variable's value.");
        long classObjectID = debuggeeWrapper.vmMirror.getThisObject(threadID, frame.getID());
        reply = debuggeeWrapper.vmMirror.invokeInstanceMethod(classObjectID, threadID, "invokeMethod", new Value[] {new Value(15)}, 0);
        Value returnedValue = reply.getNextValueAsValue();
        if (returnedValue.getTag() != JDWPConstants.Tag.INT_TAG) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Expected type of the returned value is 'int' instead of '" + JDWPConstants.Tag.getName(returnedValue.getTag()));
        }
        
        int returnedIntValue = returnedValue.getIntValue();
        if (returnedIntValue != expressionVar) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Values are not equal");
        }
        
        logWriter.print("A returned value is: " + returnedIntValue);
        logWriter.print("OK");
//        TaggedObject exception = reply.getNextValueAsTaggedObject();
//        String exceptionSignature = debuggeeWrapper.vmMirror.getClassSignature(debuggeeWrapper.vmMirror.getReferenceType(exception.objectID));
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }
    
    public Result testCE01_02() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
 
        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long startLineCodeIndex = reply.getNextValueAsLong();
        int startLineNumber = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, startLineCodeIndex);
        
        int breakpointLine = startLineNumber + 32; // after isSingletone variable has been calculated and printed
        long breakpointLineCodeIndex = debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine);
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)breakpointLineCodeIndex);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            debuggeeWrapper.vmMirror.resume();
            return failed("A breakpoint couldn't be set.");
        }

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
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");
        
        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
        reply = debuggeeWrapper.vmMirror.getThreadFrames(threadID, 0, 1);
        int frames = reply.getNextValueAsInt();
        if (frames == 0) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Couldn't get a current frame of the thread '" + threadName + "'");
        }
        
        Frame frame = new Frame();
        frame.setID(reply.getNextValueAsFrameID());
        frame.setLocation(reply.getNextValueAsLocation());
        frame.setThreadID(threadID);
        
        ArrayList localVars = (ArrayList)debuggeeWrapper.vmMirror.getLocalVars(frame);
        if (localVars == null || localVars.size() == 0) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Couldn't get local variables of the frame '" + getFrameName(frame) + "'");
        }
        
        for (int i = 0; i < localVars.size(); i++) {
            Frame.Variable var = (Frame.Variable)localVars.toArray()[i];
            if (!var.getName().equals("isSingletone")) {
                localVars.remove(i);
                --i;
            }
        }
        
        if (localVars.size() == 0) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Couldn't find variable 'siSingletone' among local variables of the frame '" + getFrameName(frame) + "'");
        }
        
        frame.setVars(localVars);
        Value[] localVarValue = debuggeeWrapper.vmMirror.getFrameValues(frame);
        
        boolean expressionVar = localVarValue[0].getBooleanValue();
        logWriter.print("A variable which was calculated through static method's 'getSingletone' call in the target VM has a value: " + expressionVar);
        logWriter.print("Invoking the same static method in a debuggee VM and check whether the returned value is equal to the calculated variable's value.");
        reply = debuggeeWrapper.vmMirror.invokeStaticMethod(debuggeeClassId, threadID, "getSingletone", new Value[0], 0);
        Value returnedValue = reply.getNextValueAsValue();
        if (returnedValue.getTag() != JDWPConstants.Tag.BOOLEAN_TAG) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Expected type of the returned value is 'boolean' instead of '" + JDWPConstants.Tag.getName(returnedValue.getTag()));
        }
        
        boolean returnedBooleanValue = returnedValue.getBooleanValue();
        if (returnedBooleanValue != expressionVar) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Values are not equal");
        }
        
        logWriter.print("A returned value is: " + returnedBooleanValue);
        logWriter.print("OK");
//        TaggedObject exception = reply.getNextValueAsTaggedObject();
//        String exceptionSignature = debuggeeWrapper.vmMirror.getClassSignature(debuggeeWrapper.vmMirror.getReferenceType(exception.objectID));

        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new CE01Test().test(args));
    }
}
