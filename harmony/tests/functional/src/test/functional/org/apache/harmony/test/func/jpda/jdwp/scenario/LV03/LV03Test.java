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
package org.apache.harmony.test.func.jpda.jdwp.scenario.LV03;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

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
 * Created on 24.05.2005 
 */

/**
 * Test stops, when all threads are created. Checks expected frames of threads
 * "worker" and "MainThreadToCheck". Checks number and types of local variables
 * in this threads.
 * 
 * Expected frames of "worker" thread:
 * "java.lang.Object.wait()", "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.waitMethod()",
 * "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.wrapperMethod()",
 * "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.run()"
 * 
 * Expected frames of "MainThreadToCheck" thread:
 * "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.run()",
 * "org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)",
 * "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.main([Ljava.lang.String;)"
 */
public class LV03Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testLV03() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();
        int firstLineNumber = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start);
        
        int breakpointLine = 61; // just before notification of thread
        long lineCodeIndex = debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine);
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)lineCodeIndex);        
//        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint, JDWPConstants.SuspendPolicy.EVENT_THREAD);
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint, JDWPConstants.SuspendPolicy.EVENT_THREAD);

        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint couldn't be set.");
        }

        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);             

        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();        
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");

//        logWriter.println("Suspending all running threads");
        reply = debuggeeWrapper.vmMirror.getAllThreadID();
        int threads = reply.getNextValueAsInt();
//        long[] threadIDs = new long[threads];
//        for (int i = 0; i < threads; i++) {
//            threadIDs[i] = reply.getNextValueAsThreadID();
//            if (!debuggeeWrapper.vmMirror.isThreadSuspended(threadIDs[i])) {
//                debuggeeWrapper.vmMirror.suspendThread(threadIDs[i]);
//            }
//        }
//
//        int[] framesCount = {3, 0, 0, 0, 0, 1, 3};
//        
//        String[][][] frameVarNames = {
//            {{"this", "local_int_x", "local_long_y"}, {"debuggeeClass", "debuggee"}, {"args", "i"}},
//            {null},
//            {null},
//            {null},
//            {null},
//            {null},
//            {null, null, null}
//        };
//        
//        String[][][] frameVarTypes = {
//            {{DEBUGGEE_CLASS_NAME, "int", "long"}, {"java.lang.Class", "org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee"}, {"java.lang.String[]", "int"}},
//            {null},
//            {null},
//            {null},
//            {null},
//            {null},
//            {null, null, null}
//        };

        HashMap threadsMap = new HashMap(2);
        if (!System.getProperty("java.vendor").equals("Harmony")) {
            threadsMap.put("worker", new String[] {"java.lang.Object.wait()", "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.waitMethod()",
                    "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.wrapperMethod()",
                    "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.run()"});
            threadsMap.put("MainThreadToCheck", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee.run()",
                "org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)",
                "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee.main([Ljava.lang.String;)"});            
        } else {
            threadsMap.put("worker", new String[] {"java.lang.Object.wait()", "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.waitMethod()", 
                    "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.wrapperMethod()",
                    "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.run()"});
            threadsMap.put("MainThreadToCheck", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee.run()",
                "org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)",
                "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee.main([Ljava.lang.String;)"});            
        }
        
        
        HashMap framesVarsTypesMap = new HashMap();
        framesVarsTypesMap.put("java.lang.Object.wait()", null);
        framesVarsTypesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.waitMethod()", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread", "double"});
        framesVarsTypesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.wrapperMethod()", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread", "long"});
        framesVarsTypesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread.run()", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread"});
        framesVarsTypesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee.run()", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee", "int", "long", "org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee$WorkerThread"});
        framesVarsTypesMap.put("org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)", new String[] {"java.lang.Class", "org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee"});
        framesVarsTypesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV03.LV03Debuggee.main([Ljava.lang.String;)", new String[] {"java.lang.String[]", "int"});
        
        logWriter.println("Walking through stack frames of all threads");
        for (int i = 0; i < threads; i++) {
            long threadID = reply.getNextValueAsThreadID();
            debuggeeWrapper.vmMirror.suspendThread(threadID);
            printStackFrames(threadID, true /*print local vars*/, false /*print object values*/);
            String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
            if (threadsMap.containsKey(threadName)) {            
                List frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
                ListIterator framesIterator = frames.listIterator();
                String[] frameNames = (String[])threadsMap.get(threadName);
                if (frames.size() < frameNames.length) {
                    return failed("Number of frames of thread '" + threadName + "' must be at least " + frameNames.length + " instead of " + frames.size());
                }
            
                for (int j = 0; j < frameNames.length; j++) {
                    Frame frame = (Frame)framesIterator.next();
                    String currentFrameName = getFrameName(frame);
                    if (!frameNames[j].equals(currentFrameName)) {
                        do {
                            frame = (Frame)framesIterator.next();
                            currentFrameName = getFrameName(frame);
                        } while (!frameNames[j].equals(currentFrameName));
                    }
                    //Frame frame = (Frame)frames.toArray()[j];
                    List vars = debuggeeWrapper.vmMirror.getLocalVars(frame);
                    String[] frameVarsTypes = (String[])framesVarsTypesMap.get(frameNames[j]);                    
                    if (vars == null) {
                        if (frameVarsTypes != null ) {                
                            return failed("Couldn't get variables of frame '" + frameNames[j] + "'");
                        }
                        continue;
                    } else if (frameVarsTypes == null) {
                    	// ignore unknown variables
                        continue;
                    }
          
                    if (vars.size() != frameVarsTypes.length) {
                        return failed("Number of local variables of frame '" + frameNames[j] + "' must be " + frameVarsTypes.length + " instead of " + vars.size());
                    }
                
                    for (int k = 0; k < vars.size(); k++) {
                        Frame.Variable var = (Frame.Variable)vars.toArray()[k];
                        String type = var.getType();
                        logWriter.print("Checking a type of variable '" + var.getName() + "' of frame '" + frameNames[j] + "' ... ");
                        if (!type.equals(frameVarsTypes[k])) {
                            return failed("Variable '" + var.getName() + "' must be of type '" + frameVarsTypes[k] + "' instead of '" + type + "'");
                        }
                        logWriter.println("OK");
                    }            
                }
            }
            debuggeeWrapper.vmMirror.suspendThread(threadID);
        }
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new LV03Test().test(args));
    }
}
