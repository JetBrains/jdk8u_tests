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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST03;

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
public class ST03Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testST03() {
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
            return failed("A breakpoint couldn't be set.");
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
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
        logWriter.print("OK");

        
//        long threadID = -1;
//        Location location;
//        boolean methodMatches = false;
//        int previousLine = 0;
//        while (!methodMatches) {
//            reply = debuggeeWrapper.vmMirror.setMethodEntry(DEBUGGEE_CLASS_NAME);        
//            debuggeeWrapper.vmMirror.resume();
//            event = debuggeeWrapper.vmMirror.receiveEvent();
//            ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
//            for (int i = 0; i < events.length; i++) {
//                if (events[i].getEventKind() != JDWPConstants.EventKind.METHOD_ENTRY) {
//                    continue;
//                }
//                    
//                location = ((ParsedEvent.Event_METHOD_ENTRY)events[i]).getLocation();
//                threadID = ((ParsedEvent.Event_METHOD_ENTRY)events[i]).getThreadID();
//                
//                if (location.classID == debuggeeClassId && location.methodID == methodId) {
//                    previousLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
//                    logWriter.print("Line: " + previousLine);
//                    methodMatches = true;
//                    break;
//                }
//            }
//            
//            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_ENTRY, reply.getNextValueAsInt());
//        }
        
        method = "stepMethod";
        methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("Couldn't obtain line table information of the method '" + method + "'");
        }
        long startLineIndex = reply.getNextValueAsLong();
        int checkedLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, startLineIndex);
        
        int previousLine = stoppedLine;
        int lineAfterMethodInvocation = 53; // line after 'stepMethod' call
        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);        
        logWriter.print("Set several steps by a bytecode instruction and check method '" + method + "' invocation");
        while (stoppedLine != lineAfterMethodInvocation) {
            reply = debuggeeWrapper.vmMirror.setStep(threadName, JDWPConstants.StepSize.MIN, JDWPConstants.StepDepth.INTO);            
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();

            events = ParsedEvent.parseEventPacket(event);
            for (int i = 0; i < events.length; i++) {
                if (events[i].getEventKind() != JDWPConstants.EventKind.SINGLE_STEP) {
                    continue;
                }
                    
                location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
                stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                String stoppedMetod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                if (stoppedLine != previousLine) {
                    previousLine = stoppedLine;
                    logWriter.print("Stepped to the method '" + stoppedMetod + "', line " + stoppedLine);
                }
                
                if (stoppedLine == checkedLine) {
                    logWriter.print("OK");
                    debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
                    logWriter.print("Resume debuggee VM");
                    debuggeeWrapper.vmMirror.resume();
                    return passed();
                }
            }
            
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }        
                
        //printStackFrames(threadID, true, false);
        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        return failed("The method '" + method + "' wasn't invoked");
    }

    public static void main(String[] args) {
        System.exit(new ST03Test().test(args));
    }
}