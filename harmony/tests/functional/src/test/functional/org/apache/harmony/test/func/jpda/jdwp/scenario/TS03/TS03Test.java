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
package org.apache.harmony.test.func.jpda.jdwp.scenario.TS03;

import java.util.HashMap;
import java.util.List;

import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
/**
 * Created on 19.05.2005 
 */

/**
 * Test stops, when all threads are created. Suspends each created thread.
 * Checks expected frames of threads "worker" and "MainThreadToCheck".
 */
public class TS03Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME 
        = "org.apache.harmony.test.func.jpda.jdwp.scenario.TS03.TS03Debuggee";
    public final static String DEBUGGEE_CLASS_SIG 
        = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }

    public Result testTS03() {        
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        //String method = "run";
        String method = "ts03DebuggeeRunThread";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("Couldn't obtain line table information for the method '" + method + "'");
        }
        long start = reply.getNextValueAsLong();
        int firstLineNumber = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start);
        int breakpointLine = firstLineNumber + TS03Debuggee.RELATIVE_LINE_NUMBER_FOR_BREAKPOINT - 1;
        
        //int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, 0); // the very first line in a 'run' method
        long lineCodeIndex = 
            debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine);
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)lineCodeIndex);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint couldn't be set.");
        }

        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);             
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        //long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");

        HashMap map = new HashMap(2);
        map.put("worker", new String[]
            {
             "java.lang.Object.wait()", 
             "org.apache.harmony.test.func.jpda.jdwp.scenario.TS03.TS03Debuggee$WorkerThread.waitMethod()",
             "org.apache.harmony.test.func.jpda.jdwp.scenario.TS03.TS03Debuggee$WorkerThread.wrapperMethod()",
             "org.apache.harmony.test.func.jpda.jdwp.scenario.TS03.TS03Debuggee$WorkerThread.run()",
            }
        );
        map.put("MainThreadToCheck", new String[] 
            {
             "org.apache.harmony.test.func.jpda.jdwp.scenario.TS03.TS03Debuggee.ts03DebuggeeRunThread()",
             "org.apache.harmony.test.func.jpda.jdwp.scenario.TS03.TS03Debuggee.ts03DebuggeeDummyMethod()",
             "org.apache.harmony.test.func.jpda.jdwp.scenario.TS03.TS03Debuggee.run()",
            }
        );            

        reply = debuggeeWrapper.vmMirror.getAllThreadID();
        int threads = reply.getNextValueAsInt();
        logWriter.println("\nShow tested threads and their stack of frames");
        for (int i = 0; i < threads; i++) {
            long threadID = reply.getNextValueAsThreadID();
            String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
            if ( ! map.containsKey(threadName) ) {
                // Do nothing with unknown threads
                continue;
            }
            if (!debuggeeWrapper.vmMirror.isThreadSuspended(threadID)) {
                debuggeeWrapper.vmMirror.suspendThread(threadID);
            }
            
            printStackFrames(threadID, false /*print local vars*/, false /*print object values*/);

            logWriter.println("Checking frames of the thread '" + threadName + "'...");
            List frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
            String[] frameNames = (String[])map.get(threadName);
            if (frames.size() < frameNames.length) {
                return failed("Number of frames of thread " 
                        + threadName + " must be at least " 
                        + frameNames.length + " instead of " 
                        + frames.size());
            }
            
            int framesNumber = frames.size();
            String  firstExpectedFrameName = frameNames[0];
            boolean firstExpectedFrameFound = false;
            int firstExpectedFrameIndex = 0;
            for (; firstExpectedFrameIndex < framesNumber; firstExpectedFrameIndex++) {
                Frame frame = (Frame)frames.toArray()[firstExpectedFrameIndex];
                String frameName = getFrameName(frame); 
                if (frameName.equals(firstExpectedFrameName)) {
                    firstExpectedFrameFound = true;
                    break;
                }
            }
            if ( ! firstExpectedFrameFound ) {
                return failed("Expected frame with name '" + firstExpectedFrameName + "' is NOT found out! ");
            }
            if (frames.size() - firstExpectedFrameIndex < frameNames.length) {
                return failed("Number of frames of thread " 
                        + threadName + " must be at least " 
                        + frameNames.length + " instead of " + (frames.size()+firstExpectedFrameIndex));
            }
            // check other expected frames
            for (int k = 1; k < frameNames.length; k++) {
                firstExpectedFrameIndex++;
                Frame frame = (Frame)frames.toArray()[firstExpectedFrameIndex];
                String frameName = getFrameName(frame); 
                if (!frameName.equals(frameNames[k])) {
                    return failed("Expected frame's name is '" 
                            + frameNames[k] + "' instead of '" 
                            + frameName + "'");
                }
            }
            logWriter.print("OK");
        }
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }
    
    public static void main(String[] args) {
        System.exit(new TS03Test().test(args));
    }
}
