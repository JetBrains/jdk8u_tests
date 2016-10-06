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
package org.apache.harmony.test.func.jpda.jdwp.scenario.PF01;

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

/**
 * Created on 20.06.2005
 */
public class PF01Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testPF01() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        String method = "run";
        long debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, method);
   
        int breakpointLine = 53;
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassID, methodID, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
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
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != methodID || stoppedLine != breakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
        
        printStackFrames(threadID, false, false);
        
        List frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
        Frame frame = (Frame)frames.toArray()[1];
        String frameName = getFrameName(frame);
        logWriter.print("Drop frame '" + frameName + "'");
        
        debuggeeWrapper.vmMirror.popFrame(frame);
        
        printStackFrames(threadID, false, false);
        logWriter.print("Check stack of frames...");
        frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
        
        String[] checkedFrameNames = {
            //"org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)",
            "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee.main([Ljava.lang.String;)",
        };

        if (frames.size() != checkedFrameNames.length) {
            return failed("Wrong number of frames in the stack. Expected 2 frames instead of " + frames.size());
        }

        for (int i = 0; i < frames.size(); i++) {
            frame = (Frame)frames.toArray()[i];
            frameName = getFrameName(frame);
            if (!getFrameName(frame).equals(checkedFrameNames[i])) {
                return failed("Expected frame's name is '" + checkedFrameNames[i] + "' instead of '" + frameName + "'");
            }
        }
        logWriter.print("OK");        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);        
        events = ParsedEvent.parseEventPacket(event);
        location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != methodID || stoppedLine != breakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
        
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        
        
        return passed();
        
    }

    public static void main(String[] args) {
        System.exit(new PF01Test().test(args));
    }
}
