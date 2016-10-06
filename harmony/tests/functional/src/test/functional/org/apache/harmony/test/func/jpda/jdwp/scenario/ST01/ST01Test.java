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
package org.apache.harmony.test.func.jpda.jdwp.scenario.ST01;

import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.share.Result;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;

/**
 * Created on 06.06.2005
 */
public class ST01Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.func.jpda.jdwp.scenario.ST01.ST01Debuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;       
    }
    
    public Result testST01() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();
        
        int runBreakpointLine = 44;
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, runBreakpointLine));
        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint couldn't be set.");
        }

        //int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start); 
        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + runBreakpointLine);
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
        if (location.methodID != methodId || lineStopped != runBreakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + runBreakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");

        method = "stepMethod";
        methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("Couldn't obtain line table information of the method '" + method + "'");
        }
        long startLineIndex = reply.getNextValueAsLong();
        int checkedMethodBreakpointLine = 53;
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, startLineIndex);
        
        int stepsToEnterMethod = 10;
        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
        logWriter.print("Set steps to enter method '" + method + "' in thread=" + threadName);
        boolean achievedMethod = false;         
        boolean success = false;
        for (int i = 0; i < stepsToEnterMethod; i++) {
            reply = debuggeeWrapper.vmMirror.setStep(threadName, JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.INTO);
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
                int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
                logWriter.print("Stepped into class '" + stoppedClass + "', method '" + stoppedMethod + "', line " + stoppedLine);
                if (location.classID == debuggeeClassId && location.methodID == methodId && stoppedLine == checkedMethodBreakpointLine) {
                    achievedMethod = true;
                    break;
                }
            }
      
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, reply.getNextValueAsInt());
            if (achievedMethod) {
                if (i != 3) {
                    return failed("Test made unexpected number of steps to achieve checkpoint inside checked method: " + i + " instead of 3");
                }
                success = true;
                break;
            }
        }
        
        if (!success) {
            return failed("Test didn't step to expected class: '" + DEBUGGEE_CLASS_NAME + "', method: '" + method + "', line: " + breakpointLine);
        }

        logWriter.println("Test successfully entered checked method");
        logWriter.print("OK");
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new ST01Test().test(args));
    }
}