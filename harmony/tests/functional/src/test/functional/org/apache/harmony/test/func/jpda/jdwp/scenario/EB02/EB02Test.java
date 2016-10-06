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
package org.apache.harmony.test.func.jpda.jdwp.scenario.EB02;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;

/**
 * Created on 23.06.2005 
 */
public class EB02Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    
    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testEB02() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        String method = "main";
        long debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassID, methodID);
        long start = reply.getNextValueAsLong();
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)start);
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint couldn't be set.");
        }        
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassID, methodID, start);
        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();    
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);        
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != methodID || stoppedLine != breakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }

        method = "run";
        methodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, method);
        
        logWriter.print("Set request to stop on method '" + method + "' entry...");
        reply = debuggeeWrapper.vmMirror.setMethodEntry(DEBUGGEE_CLASS_NAME);
        logWriter.print("OK");
        logWriter.print("Set request to stop on method '" + method + "' exit...");
        reply = debuggeeWrapper.vmMirror.setMethodExit(DEBUGGEE_CLASS_NAME);
        logWriter.print("OK");
        logWriter.print("Resume debuggee VM and check whether it stops at requested locations.");
        
        boolean done = false;
        boolean stoppedAtMethodEntry = false, stoppedAtMethodExit = false;
        while (!done) {
            debuggeeWrapper.vmMirror.resume();
            event = debuggeeWrapper.vmMirror.receiveEvent();
            events = ParsedEvent.parseEventPacket(event);
            for (int i = 0; i < events.length; i++) {
                switch (events[i].getEventKind()) {
                    case JDWPConstants.EventKind.METHOD_ENTRY:
                        location = ((ParsedEvent.Event_METHOD_ENTRY)events[i]).getLocation();
                        if (location.classID == debuggeeClassID && location.methodID == methodID) {
                            stoppedAtMethodEntry = true;
                            logWriter.print("Stopped at method '" + method + "' entry.");
                        } /*else {
                            String stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                            stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/', '.');
                            String stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                            logWriter.print("Stopped at class's '" + stoppedClass + "' method '" + stoppedMethod + "' entry");
                        }*/
                        break;
                    case JDWPConstants.EventKind.METHOD_EXIT:
                        location = ((ParsedEvent.Event_METHOD_EXIT)events[i]).getLocation();
                        if (location.classID == debuggeeClassID && location.methodID == methodID) {
                            stoppedAtMethodExit = true;
                            done = true;
                            logWriter.print("Stopped at method '" + method + "' exit.");
                        } /*else {
                            String stoppedClass = debuggeeWrapper.vmMirror.getClassSignature(location.classID);
                            stoppedClass = stoppedClass.substring(1, stoppedClass.length() - 1).replace('/', '.');
                            String stoppedMethod = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
                            logWriter.print("Stopped at class's '" + stoppedClass + "' method '" + stoppedMethod + "' exit");
                            
                        }*/
                        break;
                }
            }
        }
        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        if (!stoppedAtMethodEntry) {
            return failed("Failed to stop at method '" + method + "' entry.");
        }
        
        if (!stoppedAtMethodExit) {
            return failed("Failed to stop at method '" + method + "' exit.");
        }
        
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new EB02Test().test(args));
    }
}
