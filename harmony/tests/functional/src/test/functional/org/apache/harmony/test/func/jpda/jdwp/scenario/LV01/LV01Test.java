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
package org.apache.harmony.test.func.jpda.jdwp.scenario.LV01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
/**
 * Created on 19.05.2005 
 */
public class LV01Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }

    public Result testLV01() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        
        int breakpointLine = 55; // after all local vars are printed
        long lineCodeIndex = debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine);
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)lineCodeIndex);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            debuggeeWrapper.vmMirror.resume();
            return failed("A breakpoint couldn't be set.");
        }

        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);             

        event.getNextValueAsByte(); // suspend policy, is not used
        event.getNextValueAsInt(); // events in set, is not used
        event.getNextValueAsByte(); // event kind, is not used
        event.getNextValueAsInt(); // requestID, is not used;
        long threadID = event.getNextValueAsThreadID(); // threadID, is not used
        Location location = event.getNextValueAsLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");

        printStackFrames(threadID, true /*print local vars*/, false /*print object values*/);
        
        // checking
        List frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
        if (frames.size() != 3) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Number of frames of thread '" + debuggeeWrapper.vmMirror.getThreadName(threadID) + "' must be 3 instead of " + frames.size());
        }

        HashMap[] varsMaps = new HashMap[3];
        varsMaps[0] = new HashMap();
        varsMaps[0].put("this", DEBUGGEE_CLASS_NAME);
        varsMaps[0].put("local_int_x", "8");
        varsMaps[0].put("local_long_y", "88888888");
        
        varsMaps[1] = new HashMap();
        varsMaps[1].put("debuggeeClass", DEBUGGEE_CLASS_NAME);
        varsMaps[1].put("debuggee", DEBUGGEE_CLASS_NAME);
        
        varsMaps[2] = new HashMap();
        varsMaps[2].put("args", "java.lang.String[0]");
        varsMaps[2].put("i", "5");

        HashMap framesMap = new HashMap();
        framesMap.put("org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee.run()", varsMaps[0]);
        framesMap.put("org.apache.harmony.test.share.jpda.jdwp.debuggee.QARawDebuggee.runDebuggee(Ljava.lang.Class;)", varsMaps[1]);
        framesMap.put("org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee.main([Ljava.lang.String;)", varsMaps[2]);
        
//        String[][] frameVarNames = {
//            {"this", "local_int_x", "local_long_y"},
//            {"debuggeeClass", "debuggee"},
//            {"args", "i"}
//        };
//        
//        String[][] frameVarValues = {
//            {DEBUGGEE_CLASS_NAME, "8", "88888888"},
//            {DEBUGGEE_CLASS_NAME, DEBUGGEE_CLASS_NAME},
//            {"java.lang.String[0]", "5"}
//        };
        
        for (int i = 0; i < frames.size(); i++) {
            Frame frame = (Frame)frames.toArray()[i];
            String qualifiedFrameName = getFrameName(frame);
            logWriter.println("\nChecking variables of frame '" + qualifiedFrameName + "'");
            List vars = debuggeeWrapper.vmMirror.getLocalVars(frame);
            if (vars == null) {
                debuggeeWrapper.vmMirror.resume();
                return failed("Couldn't get variables of frame '" + qualifiedFrameName + "'");                  
            }

            HashMap varsMap = (HashMap)framesMap.get(qualifiedFrameName);
//            if (vars.size() != frameVarNames[i].length) {
            if (vars.size() != varsMap.size()) {
                debuggeeWrapper.vmMirror.resume();
//                return failed("Number of local variables of frame '" + qualifiedFrameName + "' must be " + frameVarNames[i].length + " instead of " + vars.size());
                return failed("Number of local variables of frame '" + qualifiedFrameName + "' must be " + varsMap.size() + " instead of " + vars.size());
            }

            logWriter.print("Checking names and order of variables ... ");
            Object[] frameVarNames = varsMap.keySet().toArray();
//            for (int j = 0; j < vars.size(); j++) {
            for (int j = 0; j < frameVarNames.length; j++) {
                Frame.Variable var = (Frame.Variable)vars.toArray()[j];
                String varName = var.getName();
//                if (!varName.equals(frameVarNames[i][j])) {
//                    return failed("Expected variable with name '" + frameVarNames[i][j] + "' instead of '" + varName + "'");
//                }
                
                if (!inArray(frameVarNames, varName)) {
                    debuggeeWrapper.vmMirror.resume();
                    return failed("Expected variable with name '" + frameVarNames[j] + "' instead of '" + varName + "'");
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
                        stringFieldValue = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/', '.');
                        break;
                    }
                    case JDWPConstants.Tag.CLASS_LOADER_TAG:
                    case JDWPConstants.Tag.OBJECT_TAG: {
                        long objectID = checkedVarsValues[k].getLongValue();
                        long refTypeId = debuggeeWrapper.vmMirror.getReferenceType(objectID);
                        String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeId);
                        stringFieldValue = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/', '.');
                        break;
                    }
                    case JDWPConstants.Tag.INT_TAG: 
                        stringFieldValue = String.valueOf(checkedVarsValues[k].getIntValue());
                        break;
                    case JDWPConstants.Tag.LONG_TAG: 
                        stringFieldValue = String.valueOf(checkedVarsValues[k].getLongValue());
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

//                if (!stringFieldValue.equals(frameVarValues[i][k])) {
//                    return failed("Expected field's '" + frameVarNames[i][k] + "' value is '" + frameVarValues[i][k] + "' instead of '" + stringFieldValue + "'");
//                }
                
                String frameVarValue = (String)varsMap.get(var.getName());
                if (!stringFieldValue.equals(frameVarValue)) {
                    debuggeeWrapper.vmMirror.resume();
                    return failed("Expected variable's '" + var.getName() + "' value is '" + frameVarValue + "' instead of '" + stringFieldValue + "'");
                }                
                logWriter.println("OK");
            }
        }
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }
    
    public static void main(String[] args) {
        System.exit(new LV01Test().test(args));
    }
}
