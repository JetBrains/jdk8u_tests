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
package org.apache.harmony.test.func.jpda.jdwp.scenario.OV02;

import java.util.HashMap;
import java.util.List;

import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Field;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
/**
 * Created on 31.05.2005 
 */
public class OV02Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testOV02() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        
        int breakpointLine = 60; // before running a new thread
        long lineCodeIndex = debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine);
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)lineCodeIndex);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint, JDWPConstants.SuspendPolicy.EVENT_THREAD);
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
        
        logWriter.println("\nShow only local variables");
        printStackFrames(threadID, true /*print local vars*/, false /*print object values*/);
        
        showConstants = true;
        showStaticFields = true;
        logWriter.println("\nShow local variables with object fields including static fields and constants");
        printStackFrames(threadID, true /*print local vars*/, true /*print object values*/);
        
        //checking
        
        HashMap fieldsMap = new HashMap();
        fieldsMap.put("constant_value", "777");
        fieldsMap.put("instance_count", "1");
        fieldsMap.put("singletone", "false");
                
//        String[] checkedFieldsNames = {
//            "constant_value", "instance_count", "singletone", "class$0"
//        };
//        
//        String[] checkedFieldsValues = {
//            "777", "1", "false", "java.lang.Class (" + DEBUGGEE_CLASS_NAME + ")"
//        };
        
        reply = debuggeeWrapper.vmMirror.getThreadFrames(threadID, 0, 1);
        if (reply.getNextValueAsInt() == 0) { // no frames were returned
            debuggeeWrapper.vmMirror.resume();
            return failed("Couldn't get current frame of the '" + debuggeeWrapper.vmMirror.getThreadName(threadID) + "' thread");
        }
        
        Frame frame = new Frame();
        frame.setID(reply.getNextValueAsFrameID());
        frame.setThreadID(threadID);
        frame.setLocation(reply.getNextValueAsLocation());
        String qualifiedFrameName = getFrameName(frame);
        long thisObjectID = debuggeeWrapper.vmMirror.getThisObject(threadID, frame.getID());
        if (thisObjectID == 0) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Couldn't get 'this' object ID");
        }
        
        List staticAndConstantFields = getStaticFieldsInClass(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID));
        if (staticAndConstantFields.size() == 0) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Zero number of static field and constants was encountered.");
        }
        
        if (staticAndConstantFields.size() < fieldsMap.size()) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Number of static fields and constants must be at least " + fieldsMap.size() + " instead of " + staticAndConstantFields.size());
        }
        
        Object[] checkedFields = fieldsMap.keySet().toArray();
        Object[] staticAndConstFieldsArray = new Object[staticAndConstantFields.size()];
        for (int i = 0; i < staticAndConstantFields.size(); i++) {
            Field field = (Field)staticAndConstantFields.toArray()[i];
            staticAndConstFieldsArray[i] = field.getName();
        }
        
        // check fields' names
        logWriter.print("Checking fields' names...");
//        for (int i = 0; i < staticAndConstantFields.size(); i++) {        
        for (int i = 0; i < checkedFields.length; i++) {
//            if (!field.getName().equals(checkedFieldsNames[i])) {
//                return failed("Expected a field '" + checkedFieldsNames[i] + "' instead of '" + field.getName() + "'");
//            }
            if (!inArray(staticAndConstFieldsArray, checkedFields[i])) {
                debuggeeWrapper.vmMirror.resume();
                return failed("A field '" + checkedFields[i] + "' is missing");
            }
        }
        logWriter.println("OK");
            
        logWriter.print("Checking fields' values...");
        for (int i = 0; i < staticAndConstantFields.size(); i++) {
            Field field = (Field) staticAndConstantFields.toArray()[i];
            if (inArray(checkedFields, field)) {
                String checkedFieldValue = (String) fieldsMap.get(field
                        .getName());
                logWriter.print("Checking the field's '" + field.getName()
                        + "' value...");
                Value fieldValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValues(field.getClassID(),
                                new long[] { field.getID() })[0];
                String stringFieldValue = "";
                switch (fieldValue.getTag()) {
                case JDWPConstants.Tag.CLASS_OBJECT_TAG: {
                    String fieldSignature = field.getSignature();
                    fieldSignature = fieldSignature.substring(1,
                            fieldSignature.length() - 1 /* skip ending ';' */)
                            .replace('/', '.');
                    long objectID = fieldValue.getLongValue();
                    ReplyPacket reflectedTypeReply = debuggeeWrapper.vmMirror
                            .getReflectedType(objectID);
                    byte refType = reflectedTypeReply.getNextValueAsByte();
                    long refTypeID = reflectedTypeReply
                            .getNextValueAsReferenceTypeID();
                    String typeSignature = debuggeeWrapper.vmMirror
                            .getReferenceTypeSignature(refTypeID);
                    stringFieldValue = fieldSignature
                            + " ("
                            + typeSignature
                                    .substring(1, typeSignature.length() - 1 /*
                                                                                 * skip
                                                                                 * ending
                                                                                 * ';'
                                                                                 */)
                                    .replace('/', '.') + ")";
                    break;
                }
                case JDWPConstants.Tag.INT_TAG:
                    stringFieldValue = String.valueOf(fieldValue.getIntValue());
                    break;
                case JDWPConstants.Tag.BOOLEAN_TAG:
                    stringFieldValue = String.valueOf(fieldValue
                            .getBooleanValue());
                    break;
                default:
                    break;
                }

                // if (!stringFieldValue.equals(checkedFieldsValues[i])) {
                //                return failed("Expected field's '" + checkedFieldsNames[i] + "' value is '" + checkedFieldsValues[i] + "' instead of '" + stringFieldValue + "'");
                //            }
                if (!stringFieldValue.equals(checkedFieldValue)) {
                    debuggeeWrapper.vmMirror.resume();
                    return failed("Expected field's '" + field.getName()
                            + "' value is '" + checkedFieldValue
                            + "' instead of '" + stringFieldValue + "'");
                }
                logWriter.println("OK");
            }
        }
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();

        return passed();
    }

    public static void main(String[] args) {
        System.exit(new OV02Test().test(args));
    }
}
