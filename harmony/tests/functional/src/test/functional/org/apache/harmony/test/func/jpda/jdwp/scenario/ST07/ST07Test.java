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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST07;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;

/**
 * Created on 16.06.2005 
 */
public class ST07Test extends JDWPQATestCase {
    String[] stepFilters = {
        "java.*",
        "sun.*",
        BREAKPOINT_CLASS_NAME,
        "org.apache.harmony.test.share.jpda.jdwp.JDWPQALogWriter",
        "org.apache.harmony.share.*",
        "jrockit.*",
        "org.apache.harmony.nio.*",
        "org.apache.harmony.niochar.*",
        "org.apache.harmony.misc.*",
        "org.apache.harmony.net.*",
        "org.apache.harmony.vmaccess.*",
        "org.apache.harmony.lang.*",
        "org.apache.harmony.util.*",
        "org.apache.harmony.security.*",          
        "org.apache.harmony.lang.reflect.*",
        "org.apache.harmony.fortress.*",
    };
    
    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.func.jpda.jdwp.scenario.ST07.ST07Debuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    private final static String BREAKPOINT_CLASS_NAME = "org.apache.harmony.test.func.jpda.jdwp.scenario.ST07.ST07Debuggee$TestClass1";
    private final static String BREAKPOINT_CLASS_SIG = "L" + BREAKPOINT_CLASS_NAME.replace('.', '/') + ";";
    
    private String breakpointMethod = "testMethod";
    private int breakpointLine = 50;
    private int breakpointLineInRunMethod = 34;
    


    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
        
    public Result testST07_02() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
                
        long debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long runMethodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, "run");
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, "run", (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassID, runMethodID, breakpointLineInRunMethod));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + DEBUGGEE_CLASS_SIG + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + DEBUGGEE_CLASS_SIG + "', method '" + "run" + "', line " + breakpointLineInRunMethod);
                
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);        
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != runMethodID || stoppedLine != breakpointLineInRunMethod) {
            return failed("Expected to stop in a method 'run' at line " + breakpointLineInRunMethod + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
        
        long breakpointClassID = debuggeeWrapper.vmMirror.getClassID(BREAKPOINT_CLASS_SIG);
        long breakpointMethodID = debuggeeWrapper.vmMirror.getMethodID(breakpointClassID, breakpointMethod);
                
        breakpoint = new Breakpoint(BREAKPOINT_CLASS_SIG, breakpointMethod, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(breakpointClassID, breakpointMethodID, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + BREAKPOINT_CLASS_NAME + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + BREAKPOINT_CLASS_NAME + "', method '" + breakpointMethod + "', line " + breakpointLine);
        
        int previousline = stoppedLine;
        boolean done = false;
        boolean success = false;
        logWriter.print("Set a series of steps into source line by a bytecode instruction\nand check whether the execution of the filtered out class '" + BREAKPOINT_CLASS_NAME + "' is interrupted only on the breakpoint line " + breakpointLine);
        while (!done) {
            reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.MIN, JDWPConstants.StepDepth.INTO);
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();
            events = ParsedEvent.parseEventPacket(event);
         
            for (int i = 0; i < events.length; i++) {
                switch (events[i].getEventKind()) {
                    case JDWPConstants.EventKind.BREAKPOINT:
                        location = ((ParsedEvent.Event_BREAKPOINT)events[i]).getLocation();
                        String stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                        String stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        logWriter.print("Stopped at breakpoint at the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                        if (stoppedClass.equals(BREAKPOINT_CLASS_NAME) && stoppedMethod.equals(breakpointMethod) && stoppedLine == breakpointLine) {
                            success = true;
                        } else {
                            return failed("Unexpected breakpoint at the location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                        }
                        break;
                    case JDWPConstants.EventKind.SINGLE_STEP:
                        location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
                        stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                        stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        if (stoppedLine != previousline) {
                            logWriter.print("Stepped into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                            previousline = stoppedLine;
                        } else {
                            continue;
                        }
                        
                        for (int j = 0; j < stepFilters.length; j++) {
                            if (stoppedClass.matches(stepFilters[j])) {
                                logWriter.println("Iteration #" + j);
                                printStackFrames(threadID, true, false);
                                debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
                                logWriter.print("Resume debuggee VM");
                                debuggeeWrapper.vmMirror.resume();
                                return failed("Unexpected step into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine + " has been made");
                            }
                        }
                        if (success) {
                            done = true;
                        }
                        break;
                }
            }
            
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }
        
        if (!success) {
            return failed("The breakpoint wasn't triggered at required location.");
        }
        
        logWriter.print("OK");
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        

        return passed();
    }
    
    public Result testST07_01() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
                      
        long debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long runMethodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, "run");
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, "run", (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassID, runMethodID, breakpointLineInRunMethod));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + DEBUGGEE_CLASS_SIG + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + DEBUGGEE_CLASS_SIG + "', method '" + "run" + "', line " + breakpointLineInRunMethod);
                
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);        
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != runMethodID || stoppedLine != breakpointLineInRunMethod) {
            return failed("Expected to stop in a method 'run' at line " + breakpointLineInRunMethod + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
        
        long breakpointClassID = debuggeeWrapper.vmMirror.getClassID(BREAKPOINT_CLASS_SIG);
        long breakpointMethodID = debuggeeWrapper.vmMirror.getMethodID(breakpointClassID, breakpointMethod);
        
        breakpoint = new Breakpoint(BREAKPOINT_CLASS_SIG, breakpointMethod, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(breakpointClassID, breakpointMethodID, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + BREAKPOINT_CLASS_NAME + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + BREAKPOINT_CLASS_NAME + "', method '" + breakpointMethod + "', line " + breakpointLine);
        
        boolean done = false;
        boolean success = false;
        logWriter.print("Set a series of steps into source line and check whether the execution of the filtered out class '" + BREAKPOINT_CLASS_NAME + "' is interrupted only on the breakpoint line " + breakpointLine);
        while (!done) {
            reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.INTO);
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();
            events = ParsedEvent.parseEventPacket(event);
         
            for (int i = 0; i < events.length; i++) {
                switch (events[i].getEventKind()) {
                    case JDWPConstants.EventKind.BREAKPOINT:
                        location = ((ParsedEvent.Event_BREAKPOINT)events[i]).getLocation();
                        String stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                        String stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        logWriter.print("Stopped at breakpoint at the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                        if (stoppedClass.equals(BREAKPOINT_CLASS_NAME) && stoppedMethod.equals(breakpointMethod) && stoppedLine == breakpointLine) {
                            success = true;
                        } else {
                            return failed("Unexpected breakpoint at the location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                        }
                        break;
                    case JDWPConstants.EventKind.SINGLE_STEP:
                        location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
                        stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                        stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        logWriter.print("Stepped into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);

                        if (stoppedClass.equals(DEBUGGEE_CLASS_NAME)) {
                            if (stoppedMethod.equals("run")) {
                                done = true;
                            }
                        } else {
                            for (int j = 0; j < stepFilters.length; j++) {
                                if (stoppedClass.matches(stepFilters[j])) {
                                    printStackFrames(threadID, true, false);
                                    debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
                                    logWriter.print("Resume debuggee VM");
                                    debuggeeWrapper.vmMirror.resume();
                                    return failed("Unexpected step into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine + " has been made");                           
                                }
                            }
                        }
                        break;
                }
            }
            
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }
        
        if (!success) {
            return failed("The breakpoint wasn't triggered at required location.");
        }
        
        logWriter.print("OK");
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        

        return passed();
    }
        
    public Result testST07_03() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        long debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long runMethodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, "run");
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassID, runMethodID);
        long start = reply.getNextValueAsLong();
        long end = reply.getNextValueAsLong();
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, "run", (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassID, runMethodID, breakpointLineInRunMethod));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + DEBUGGEE_CLASS_SIG + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + DEBUGGEE_CLASS_SIG + "', method '" + "run" + "', line " + breakpointLineInRunMethod);
              
        int runStartLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassID, runMethodID, start);
        int runEndLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassID, runMethodID, end);
        logWriter.println("Breakpoint is set in a 'run' method at line " + runStartLine);
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);        
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != runMethodID || stoppedLine != breakpointLineInRunMethod) {
            return failed("Expected to stop in a method 'run' at line " + breakpointLineInRunMethod + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
      
        long breakpointClassID = debuggeeWrapper.vmMirror.getClassID(BREAKPOINT_CLASS_SIG);
        long breakpointMethodID = debuggeeWrapper.vmMirror.getMethodID(breakpointClassID, breakpointMethod);
        
        breakpoint = new Breakpoint(BREAKPOINT_CLASS_SIG, breakpointMethod, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(breakpointClassID, breakpointMethodID, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + BREAKPOINT_CLASS_NAME + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + BREAKPOINT_CLASS_NAME + "', method '" + breakpointMethod + "', line " + breakpointLine);
        
     
        
        boolean done = false;
        boolean success = false;
        logWriter.print("Set a series of steps over source line and check whether the execution\nof the filtered out class '" + BREAKPOINT_CLASS_NAME + "' is interrupted only on the breakpoint line " + breakpointLine);
        while (!done) {
            reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.OVER);
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();
            events = ParsedEvent.parseEventPacket(event);
         
            for (int i = 0; i < events.length; i++) {
                switch (events[i].getEventKind()) {
                    case JDWPConstants.EventKind.BREAKPOINT:
                        location = ((ParsedEvent.Event_BREAKPOINT)events[i]).getLocation();
                        String stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                        String stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        logWriter.print("Stopped at breakpoint at the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                        if (stoppedClass.equals(BREAKPOINT_CLASS_NAME) && stoppedMethod.equals(breakpointMethod) && stoppedLine == breakpointLine) {
                            success = true;
                        } else {
                            return failed("Unexpected breakpoint at the location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                        }
                        break;
                    case JDWPConstants.EventKind.SINGLE_STEP:
                        location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
                        stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                        stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        logWriter.print("Stepped into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);

                        if (stoppedClass.equals(DEBUGGEE_CLASS_NAME) && stoppedMethod.equals("run")) {
                            done = stoppedLine == runEndLine;
                        } else {
                            for (int j = 0; j < stepFilters.length; j++) {
                                if (stoppedClass.matches(stepFilters[j])) {
                                    printStackFrames(threadID, true, false);
                                    logWriter.print("Resume debuggee VM");
                                    debuggeeWrapper.vmMirror.resume();
                                    return failed("Unexpected step into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine + " has been made");                           
                                }
                            }
                        }
                        
                }
            }
            logWriter.println("=> Clear step event request");
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
            
        }
        
        if (!success) {
            return failed("The breakpoint wasn't triggered at required location.");
        }
        
        logWriter.print("OK");
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        

        return passed();
    }
       
    public Result testST07_04() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
                
        long debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long runMethodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, "run");
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassID, runMethodID);
        long start = reply.getNextValueAsLong();
        long end = reply.getNextValueAsLong();
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, "run", (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassID, runMethodID, breakpointLineInRunMethod));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + DEBUGGEE_CLASS_SIG + "' couldn't be set.");
        }
        
        int runStartLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassID, runMethodID, start);
        int runEndLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassID, runMethodID, end);
        logWriter.println("Breakpoint is set in a 'run' method at line " + runStartLine);
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);        
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != runMethodID || stoppedLine != breakpointLineInRunMethod) {
            return failed("Expected to stop in a method 'run' at line " + breakpointLineInRunMethod + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
   
        long breakpointClassID = debuggeeWrapper.vmMirror.getClassID(BREAKPOINT_CLASS_SIG);
        long breakpointMethodID = debuggeeWrapper.vmMirror.getMethodID(breakpointClassID, breakpointMethod);
        
        breakpoint = new Breakpoint(BREAKPOINT_CLASS_SIG, breakpointMethod, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(breakpointClassID, breakpointMethodID, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + BREAKPOINT_CLASS_NAME + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + BREAKPOINT_CLASS_NAME + "', method '" + breakpointMethod + "', line " + breakpointLine);
        

        int previousLine = stoppedLine;
        boolean done = false;
        boolean success = false;
        logWriter.print("Set a series of steps over source lines by a bytecode instruction and check whether the execution\nof the filtered out class '" + BREAKPOINT_CLASS_NAME + "' is interrupted only on the breakpoint line " + breakpointLine);
        while (!done) {
            reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.MIN, JDWPConstants.StepDepth.OVER);
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();
            events = ParsedEvent.parseEventPacket(event);
         
            for (int i = 0; i < events.length; i++) {
                switch (events[i].getEventKind()) {
                    case JDWPConstants.EventKind.BREAKPOINT:
                        location = ((ParsedEvent.Event_BREAKPOINT)events[i]).getLocation();
                        String stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                        String stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        logWriter.print("Stopped at breakpoint at the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                        if (stoppedClass.equals(BREAKPOINT_CLASS_NAME) && stoppedMethod.equals(breakpointMethod) && stoppedLine == breakpointLine) {
                            success = true;
                        } else {
                            return failed("Unexpected breakpoint at the location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                        }
                        break;
                    case JDWPConstants.EventKind.SINGLE_STEP:
                        location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
                        stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                        stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                        if (previousLine != stoppedLine) {
                            logWriter.print("Stepped into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                            previousLine = stoppedLine;
                        }

                        if (stoppedClass.equals(DEBUGGEE_CLASS_NAME) && stoppedMethod.equals("run")) {
                            done = stoppedLine == runEndLine;
                        } else {
                            for (int j = 0; j < stepFilters.length; j++) {
                                if (stoppedClass.matches(stepFilters[j])) {
                                    printStackFrames(threadID, true, false);
                                    logWriter.print("Resume debuggee VM");
                                    debuggeeWrapper.vmMirror.resume();
                                    return failed("Unexpected step into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine + " has been made");                           
                                }
                            }
                        }
                        
                }
            }
            
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }
        
        if (!success) {
            return failed("The breakpoint wasn't triggered at required location.");
        }
        
        logWriter.print("OK");
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        

        return passed();
        
    }
        
    public Result testST07_05() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        
        long debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long runMethodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, "run");
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassID, runMethodID);
        long start = reply.getNextValueAsLong();
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, "run", (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassID, runMethodID, breakpointLineInRunMethod));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + DEBUGGEE_CLASS_SIG + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + DEBUGGEE_CLASS_SIG + "', method '" + "run" + "', line " + breakpointLineInRunMethod);
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);        
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != runMethodID || stoppedLine != breakpointLineInRunMethod) {
            return failed("Expected to stop in a method 'run' at line " + breakpointLineInRunMethod + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
        
        long breakpointClassID = debuggeeWrapper.vmMirror.getClassID(BREAKPOINT_CLASS_SIG);
        long breakpointMethodID = debuggeeWrapper.vmMirror.getMethodID(breakpointClassID, breakpointMethod);
        
        breakpoint = new Breakpoint(BREAKPOINT_CLASS_SIG, breakpointMethod, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(breakpointClassID, breakpointMethodID, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + BREAKPOINT_CLASS_NAME + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + BREAKPOINT_CLASS_NAME + "', method '" + breakpointMethod + "', line " + breakpointLine);
        
        logWriter.println("Set second breakpoint inside " + breakpointMethod);
        breakpointLine = 52;
        
        breakpointClassID = debuggeeWrapper.vmMirror.getClassID(BREAKPOINT_CLASS_SIG);
        breakpointMethodID = debuggeeWrapper.vmMirror.getMethodID(breakpointClassID, breakpointMethod);
        
        breakpoint = new Breakpoint(BREAKPOINT_CLASS_SIG, breakpointMethod, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(breakpointClassID, breakpointMethodID, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint at the class '" + BREAKPOINT_CLASS_NAME + "' couldn't be set.");
        }
        
        logWriter.println("Breakpoint is set at the class '" + BREAKPOINT_CLASS_NAME + "', method '" + breakpointMethod + "', line " + breakpointLine);
        
        
        boolean done = false;
        boolean success = false;
        logWriter.print("Make a step into source line code");
//              
        int stepsToBeDone = 1;
        for (int i = 0; i < stepsToBeDone; i++) {
            reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.INTO);
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();
            events = ParsedEvent.parseEventPacket(event);
            for (int j = 0; j < events.length; j++) {
                if (events[j].getEventKind() != JDWPConstants.EventKind.SINGLE_STEP) {
                    continue;
                }
                
                location = ((ParsedEvent.Event_SINGLE_STEP)events[j]).getLocation();
                String stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
                String stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                logWriter.print("Stepped into the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
            }
            
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        }
        
        logWriter.print("Make a step out of the current method and check check whether the execution of the filtered out class '" + BREAKPOINT_CLASS_NAME + "' is interrupted only on the breakpoint line " + breakpointLine);
        reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.OUT);
        debuggeeWrapper.vmMirror.resume();
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        events = ParsedEvent.parseEventPacket(event);
        location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
        stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
        String stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
        logWriter.print("Stopped at breakpoint at the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
        if (!stoppedClass.equals(BREAKPOINT_CLASS_NAME) || !stoppedMethod.equals(breakpointMethod) || stoppedLine != breakpointLine) {
            return failed("Unexpected breakpoint at the location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
        }
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.BREAKPOINT, reply.getNextValueAsInt());
        
        logWriter.print("Make a final step and check its location");
        reply = debuggeeWrapper.vmMirror.setStep(stepFilters, threadID, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.OUT);
        debuggeeWrapper.vmMirror.resume();
        //event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.SINGLE_STEP);
        event = debuggeeWrapper.vmMirror.receiveEvent();
        events = ParsedEvent.parseEventPacket(event);
        
        for (int i = 0; i < events.length; i++) {
            if (events[i].getEventKind() != JDWPConstants.EventKind.SINGLE_STEP) {
                continue;
            }
            
            location = ((ParsedEvent.Event_SINGLE_STEP)events[i]).getLocation();
            stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
            stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/','.');
            stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
            stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
            logWriter.print("Stepped to the following location: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
            if (!stoppedClass.equals(DEBUGGEE_CLASS_NAME) || !stoppedMethod.equals("run")) {
                debuggeeWrapper.vmMirror.resume();
                return failed("Unexpected loacation: class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
            }
            
        }
        
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
        logWriter.print("OK");
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        

        return passed();
    }
    
    

            
    public static void main(String[] args) {
        System.exit(new ST07Test().test(args));
    }
}
