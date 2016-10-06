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
package org.apache.harmony.test.func.jpda.jdwp.scenario.LV02;

import java.util.ArrayList;
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
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
/**
 * Created on 20.05.2005 
 */

/**
 * Test stops, when all threads are created. Checks expected frames of threads
 * "worker" and "MainThreadToCheck". Checks number, names and values of local variables
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
public class LV02Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testLV02() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();
        int firstLineNumber = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start);
        
        int breakpointLine = 61; // after worker tread was started
        long lineCodeIndex = debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine);
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)lineCodeIndex);        
        //reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint, JDWPConstants.SuspendPolicy.EVENT_THREAD);
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
//        reply = debuggeeWrapper.vmMirror.getAllThreadID();
//        int threads = reply.getNextValueAsInt();
//        long[] threadIDs = new long[threads];
//        for (int i = 0; i < threads; i++) {
//            threadIDs[i] = reply.getNextValueAsThreadID();
//            if (!debuggeeWrapper.vmMirror.isThreadSuspended(threadIDs[i])) {
//                debuggeeWrapper.vmMirror.suspendThread(threadIDs[i]);
//            }
//        }
 
        HashMap threadsMap = new HashMap(2);
        if (!System.getProperty("java.vendor").equals("Harmony")) {
            threadsMap.put("worker", new String[] {"java.lang.Object.wait()", "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.waitMethod()",
                    "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.wrapperMethod()",
                    "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.run()"});
            threadsMap.put("MainThreadToCheck", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.run()",
                "org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)",
                "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.main([Ljava.lang.String;)"});            
        } else {
            threadsMap.put("worker", new String[] {"java.lang.Object.wait()", "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.waitMethod()", 
                    "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.wrapperMethod()",
                    "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.run()"});
            threadsMap.put("MainThreadToCheck", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.run()",
                "org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)",
                "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.main([Ljava.lang.String;)"});            
        }

        HashMap framesVarsNamesMap = new HashMap();
        framesVarsNamesMap.put("java.lang.Object.wait()", null);
        framesVarsNamesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.waitMethod()", new String[] {"this", "waitMethodVar"});
        framesVarsNamesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.wrapperMethod()", new String[] {"this", "wrapperMethodVar"});
        framesVarsNamesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.run()", new String[] {"this"});
        framesVarsNamesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.run()", new String[] {"this", "local_int_x", "local_long_y", "thread"});
        framesVarsNamesMap.put("org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)", new String[] {"debuggeeClass", "debuggee"});
        framesVarsNamesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.main([Ljava.lang.String;)", new String[] {"args", "i"});
        
        HashMap framesVarsValuesMap = new HashMap();
        framesVarsValuesMap.put("java.lang.Object.wait()", null);
        framesVarsValuesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.waitMethod()", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread", "3.5"});
        framesVarsValuesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.wrapperMethod()", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread", "35"});
        framesVarsValuesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread.run()", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread"});
        framesVarsValuesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.run()", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee", "8", "88888888", "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee$WorkerThread"});
        framesVarsValuesMap.put("org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)", new String[] {"org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee", "org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee"});
        framesVarsValuesMap.put("org.apache.harmony.test.func.jpda.jdwp.scenario.LV02.LV02Debuggee.main([Ljava.lang.String;)", new String[] {"java.lang.String[0]", "5"});
        
        
//        String[][][] frameVarNames = {
//            {null, {"millis", "nanos"}, {"millis"}, {"this"}, {"this"}},
//            {{"this", "thread"}, {"this", "local_int_x", "local_long_y"}, {"debuggeeClass", "debuggee"}, {"args", "i"}},
//        };
//        
//        
//        String[][][] frameVarValues = {
//            {null, {"500", "0"}, {"500"}, {"org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee$WorkerThread"}, {"org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee$WorkerThread"}},
//            {{"org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee", "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee$WorkerThread"}, 
//             {"org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee", "8", "88888888"},
//             {"org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee", "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee"},
//             {"java.lang.String[0]", "5"}},
//        };

        reply = debuggeeWrapper.vmMirror.getAllThreadID();
        int threads = reply.getNextValueAsInt();
        
        logWriter.println("Walking through stack frames of all threads");
        for (int i = 0; i < threads; i++) {
            logWriter.println("---------------------");
            long threadID = reply.getNextValueAsThreadID();
            debuggeeWrapper.vmMirror.suspendThread(threadID);
            printStackFrames(threadID, true /*print local vars*/, false /*print object values*/);
            String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
            if (threadsMap.containsKey(threadName)) {
                logWriter.println("\nChecking the thread '" + threadName + "'");            
                List frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
                ListIterator framesIterator = frames.listIterator();
                logWriter.print("Frames: " + frames.size());
                String[] frameNames = (String[])threadsMap.get(threadName);
                if (frames.size() < frameNames.length) {
                    return failed("Number of frames of thread '" + threadName + "' must be at least " + frameNames.length + " instead of " + frames.size());
                }
            
                for (int j = 0; j < frameNames.length; j++) {
                    //Frame frame = (Frame)frames.toArray()[j];
                    Frame frame = (Frame)framesIterator.next();
                    String currentFrameName = getFrameName(frame);
                    if (!frameNames[j].equals(currentFrameName)) {
                        do {
                            frame = (Frame)framesIterator.next();
                            currentFrameName = getFrameName(frame);
                        } while (!frameNames[j].equals(currentFrameName));
                    }
                    logWriter.println("\nChecking variables of frame '" + frameNames[j] + "'");
                    List vars = debuggeeWrapper.vmMirror.getLocalVars(frame);
                    String[] frameVarsNames = (String[])framesVarsNamesMap.get(frameNames[j]);
                    if (vars == null) {
//                        if (frameVarNames[i][j] != null ) {                
                        if (frameVarsNames != null) {
                            return failed("Couldn't get variables of frame '" + frameNames[j] + "'");
                        }
                        continue;
                    } else if (frameVarsNames == null) {
                    	// ignore unknown variables
                        continue;
                    }
          
//                    if (vars.size() != frameVarNames[i][j].length) {
                    if (vars.size() != frameVarsNames.length) {
//                        return failed("Number of local variables of frame '" + frameNames[j] + "' must be " + frameVarNames[i][j].length + " instead of " + vars.size());
                        return failed("Number of local variables of frame '" + frameNames[j] + "' must be " + frameVarsNames.length + " instead of " + vars.size());
                    }

                    logWriter.print("Checking names and order of variables ... ");
                    for (int k = 0; k < vars.size(); k++) {
                        Frame.Variable var = (Frame.Variable)vars.toArray()[k];
                        String varName = var.getName();
//                        if (!varName.equals(frameVarNames[i][j][k])) {
                        if (!varName.equals(frameVarsNames[k])) {
//                            return failed("Expected variable with name '" + frameVarNames[i][j][k] + "' instead of '" + varName + "'");
                            return failed("Expected variable with name '" + frameVarsNames[k] + "' instead of '" + varName + "'");
                        }
                    }            
                    logWriter.println("OK");
                
                    frame.setVars((ArrayList)vars);
                    Value[] checkedVarsValues = debuggeeWrapper.vmMirror.getFrameValues(frame);
                    String stringFieldValue = "";
                    for (int k = 0; k < checkedVarsValues.length; k++) {
                        Frame.Variable var = (Frame.Variable)vars.toArray()[k];
                        logWriter.print("Checking the value of variable '" + var.getName() + "' ... ");
                        switch (checkedVarsValues[k].getTag()) {
                            case JDWPConstants.Tag.CLASS_OBJECT_TAG: {
                                long objectID = checkedVarsValues[k].getLongValue();
                                ReplyPacket reflectedTypeReply = debuggeeWrapper.vmMirror.getReflectedType(objectID);
                                byte refType = reflectedTypeReply.getNextValueAsByte();
                                long refTypeID = reflectedTypeReply.getNextValueAsReferenceTypeID();                    
                                String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeID);                   
                                stringFieldValue = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/','.');
                                break;
                            }
                            case JDWPConstants.Tag.CLASS_LOADER_TAG:
                            case JDWPConstants.Tag.THREAD_TAG:
                            case JDWPConstants.Tag.OBJECT_TAG: {
                                long objectID = checkedVarsValues[k].getLongValue();
                                long refTypeId = debuggeeWrapper.vmMirror.getReferenceType(objectID);
                                String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeId);
                                stringFieldValue = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/','.');
                                break;
                            }
                            case JDWPConstants.Tag.INT_TAG: 
                                stringFieldValue = String.valueOf(checkedVarsValues[k].getIntValue());
                                break;
                            case JDWPConstants.Tag.LONG_TAG: 
                                stringFieldValue = String.valueOf(checkedVarsValues[k].getLongValue());
                                break;
                            case JDWPConstants.Tag.DOUBLE_TAG:
                                stringFieldValue = String.valueOf(checkedVarsValues[k].getDoubleValue());
                                break;
                            case JDWPConstants.Tag.STRING_TAG: 
                                stringFieldValue = debuggeeWrapper.vmMirror.getStringValue(checkedVarsValues[k].getLongValue());
                                break;
                            case JDWPConstants.Tag.ARRAY_TAG: 
                                Value[] arrayValues = debuggeeWrapper.vmMirror.getArrayValues(checkedVarsValues[k].getLongValue());
                                String type = var.getType().substring(0, var.getType().length() - 2);
                                if (arrayValues == null) {
                                    stringFieldValue = type +  "[0]";
                                } else {
                                    stringFieldValue = type + "[" + arrayValues.length + "]";
                                }
                                break;
                            default:
                                break;
                        }
                    
                        String[] frameVarsValues = (String[])framesVarsValuesMap.get(frameNames[j]);
//                        if (!stringFieldValue.equals(frameVarValues[i][j][k])) {
                        if (!stringFieldValue.equals(frameVarsValues[k])) {
//                            return failed("Expected field's '" + frameVarNames[i][j][k] + "' value is '" + frameVarValues[i][j][k] + "' instead of '" + stringFieldValue + "'");
                            return failed("Expected field's '" + frameVarsNames[k] + "' value is '" + frameVarsValues[k] + "' instead of '" + stringFieldValue + "'");
                            
                        }
                        stringFieldValue = "";
                        logWriter.println("OK");
                    }
                
                }
                
            }
            debuggeeWrapper.vmMirror.resumeThread(threadID);
        }
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }
       
    public static void main(String[] args) {
        System.exit(new LV02Test().test(args));
    }
}
