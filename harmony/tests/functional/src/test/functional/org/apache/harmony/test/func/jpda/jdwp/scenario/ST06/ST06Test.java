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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST06;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;


/**
 * Created on 14.06.2005 
 */
public class ST06Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    String[] stepFilters = {
        "java.*",
        "sun.*",
        "org.apache.harmony.nio.*",
        "org.apache.harmony.niochar.*",
        "org.apache.harmony.misc.*",
        "org.apache.harmony.net.*",
        "org.apache.harmony.vmaccess.*",
        "org.apache.harmony.lang.*",
        "org.apache.harmony.util.*",
        "org.apache.harmony.security.*",
        "org.apache.harmony.share.*",
        "org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee",
        "org.apache.harmony.test.share.jpda.jdwp.JDWPQADebuggeeWrapper",
        "org.apache.harmony.test.share.jpda.jdwp.JDWPQALogWriter",
        "org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase",
        "org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase",
        "jrockit.*",
        "org.apache.harmony.fortress.*",
    };
    
    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testST06_01() {
        
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        //debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());        
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.CLASS_PREPARE, reply.getNextValueAsInt());
        
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, "main");
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, "main", (int)start);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            debuggeeWrapper.vmMirror.resume();
            return failed("A breakpoint couldn't be set.");
        }
        
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start); 
        logWriter.println("Breakpoint is set in a 'main' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Expected to stop in a method 'main' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        //debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.BREAKPOINT, reply.getNextValueAsInt());
        
        String matchingMethod = "run";
        boolean methodMatches = false;
//        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
        logWriter.print("Set a series of steps into source lines using filters");
        while (!methodMatches) {            
            reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.INTO);            
            debuggeeWrapper.vmMirror.resume();
            //event = debuggeeWrapper.vmMirror.receiveEvent();
            event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.SINGLE_STEP);
            events = ParsedEvent.parseEventPacket(event);
            
            for (int i = 0; i < events.length; i++) {
                //logWriter.print("Event kind: " + JDWPConstants.EventKind.getName(events[i].getEventKind()));
                if (events[i].getEventKind() != JDWPConstants.EventKind.SINGLE_STEP) {
                    continue;
                }
                
                location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
                int steppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                String steppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);                
                String className = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                className = className.substring(1, className.length() - 1).replace('/','.');
                logWriter.print("Step's location: class '" + className + "', method '" + steppedMethod + "', line " + steppedLine);
                if (className.equals(DEBUGGEE_CLASS_NAME) && steppedMethod.equals(matchingMethod)) {
                    methodMatches = true;
                    break;
                }
                
                for (int j = 0; j < stepFilters.length; j++) {
                    if (className.matches(stepFilters[j])) {
                        debuggeeWrapper.vmMirror.resume();
                        return failed("Didn't expect to step into the following location: class '" + className + "', method '" + steppedMethod + "', line " + steppedLine);
                    }
                }
            }
            
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }
        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }
    
    public Result testST06_02() {
        
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.CLASS_PREPARE, reply.getNextValueAsInt());

        
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, "main");
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, "main", (int)start);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            debuggeeWrapper.vmMirror.resume();
            return failed("A breakpoint couldn't be set.");
        }
        
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start); 
        logWriter.println("Breakpoint is set in a 'main' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.BREAKPOINT, reply.getNextValueAsInt());
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Expected to stop in a method 'main' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }

        String matchingMethod = "run";
        boolean methodMatches = false;
        logWriter.print("Set a series of steps into source lines by bytecode instructions using filters");
        int previousLine = 0;
        while (!methodMatches) {            
            reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.MIN, JDWPConstants.StepDepth.INTO);            
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();
            events = ParsedEvent.parseEventPacket(event);
            
            for (int i = 0; i < events.length; i++) {
                if (events[i].getEventKind() != JDWPConstants.EventKind.SINGLE_STEP) {
                    continue;
                }
                
                location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
                int steppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                String steppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);                
                String className = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                className = className.substring(1, className.length() - 1).replace('/','.');
                //logWriter.print("Step's location: class '" + className + "', method '" + steppedMethod + "', line " + steppedLine);                
                if (steppedLine != previousLine) {
                    logWriter.print("Step's location: class '" + className + "', method '" + steppedMethod + "', line " + steppedLine);
                    previousLine = steppedLine;
                }
                
                if (className.equals(DEBUGGEE_CLASS_NAME) && steppedMethod.equals(matchingMethod)) {
                    methodMatches = true;
//                    debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
                    break;
                }
                
                for (int j = 0; j < stepFilters.length; j++) {
                    if (className.matches(stepFilters[j])) {
                        debuggeeWrapper.vmMirror.resume();
                        return failed("Didn't expect to step into the following location: class '" + className + "', method '" + steppedMethod + "', line " + steppedLine);
                    }
                }
            }
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }
        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
        
    }
    
/*    public Result testST06_02() {
        String[] stepFilters = {
            "java.*",
        };
        
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, "main");
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();        
         
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, "main", (int)start);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint couldn't be set.");
        }
        
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start); 
        logWriter.println("Breakpoint is set in a 'main' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            return failed("Expected to stop in a method 'main' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }      
     
        logWriter.print("Set a series of steps over source lines until method 'main' returns using filters");
//        reply = debuggeeWrapper.vmMirror.setMethodExit(DEBUGGEE_CLASS_NAME);
//        int requestID = reply.getNextValueAsInt();
//        debuggeeWrapper.vmMirror.resume();
        
        boolean timeToQuit = false;
        while (!timeToQuit) {
            reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.OVER);
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();
            events = ParsedEvent.parseEventPacket(event);

            for (int i = 0; i < events.length; i++) {
                switch (events[i].getEventKind()) {
//                    case JDWPConstants.EventKind.METHOD_EXIT:
//                        timeToQuit = true;
//                        break;
                    case JDWPConstants.EventKind.SINGLE_STEP:
                        location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
                        int steppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        String steppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);                
                        String className = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        className = className.substring(1, className.length() - 1).replace('/','.');
                        logWriter.print("Step's location: class '" + className + "', method '" + steppedMethod + "', line " + steppedLine);
                        
                        for (int j = 0; j < stepFilters.length; j++) {
                            if (className.matches(stepFilters[j])) {
                                return failed("Didn't expect to step into the following location: class '" + className + "', method '" + steppedMethod + "', line " + steppedLine);
                            }
                        }
                        break;
                }
            }
        }       
        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }*/

    public static void main(String[] args) {
        System.exit(new ST06Test().test(args));
    }
}