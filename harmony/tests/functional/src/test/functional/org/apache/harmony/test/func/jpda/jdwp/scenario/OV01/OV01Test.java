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
package org.apache.harmony.test.func.jpda.jdwp.scenario.OV01;

import java.util.HashMap;
import java.util.List;

import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Field;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
/**
 * Created on 27.05.2005 
 */
public class OV01Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testOV01() {
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

        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);        
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
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
        
        logWriter.println("\nShow local variables with object fields excluding static fields and constants");
        printStackFrames(threadID, true /*print local vars*/, true /*print object values*/);
        
        //checking
        
        HashMap checkedFieldsMap = new HashMap();        
        checkedFieldsMap.put("logWriter", "org.apache.harmony.test.share.jpda.jdwp.JDWPQALogWriter");
        checkedFieldsMap.put("settings", "org.apache.harmony.jpda.tests.framework.TestOptions");
        checkedFieldsMap.put("int_value", "9");
        checkedFieldsMap.put("long_value", "999999");
        checkedFieldsMap.put("string_value", "Hello, World!");
        checkedFieldsMap.put("array_value", "test");
        
//        String[] checkedObjectFieldNames = {
//            "logWriter", "settings", "int_value", "long_value", "string_value", "array_value"
//        };
//        
//        String[] checkedObjectFieldValues = {
//            "org.apache.harmony.test.share.jpda.jdwp.JDWPQALogWriter", "org.apache.harmony.jpda.tests.framework.TestOptions", "9", "999999", "Hello, World!", "test"
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
        
        Object[] checkedFields = (Object[])checkedFieldsMap.keySet().toArray();
        List thisObjectFields = getInstanceFieldsInClass(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID));
        if (thisObjectFields.size() != checkedFields.length) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Number of fields must be " + checkedFields.length + " instead of " + thisObjectFields.size());
        }
        
        long[] fieldIDs = new long[thisObjectFields.size()];
        for (int i = 0; i < thisObjectFields.size(); i++) {
            Field field = (Field)thisObjectFields.toArray()[i];
            fieldIDs[i] = field.getID();
        }
        
        Value[] thisObjectFieldValues = debuggeeWrapper.vmMirror.getObjectReferenceValues(thisObjectID, fieldIDs);
        
        // check fields' names
//        for (int i = 0; i < thisObjectFieldValues.length; i++) {
        Object[] thisObjectFieldsArray = new Object[thisObjectFields.size()];
        for (int i = 0; i < thisObjectFields.size(); i++) {
            Field field = (Field)thisObjectFields.toArray()[i];
            thisObjectFieldsArray[i] = field.getName();
        }
        
        logWriter.print("Checking fields' names...");
        for (int i = 0; i < checkedFields.length; i++) {
//            logWriter.print("Checking field's '" + field.getName() + "' order and name ... ");
//            if (!field.getName().equals(checkedObjectFieldNames[i])) {
            if (!inArray(thisObjectFieldsArray, checkedFields[i])) {
                debuggeeWrapper.vmMirror.resume();
                //return failed("Expected a field '" + checkedObjectFieldNames[i] + "' instead of '" + field.getName() + "'");
                return failed("A field '" + checkedFields[i] + "' is missing");
            }
        }
        logWriter.println("OK");
            
        logWriter.print("Checking fields' values...");
        for (int i = 0; i < thisObjectFieldValues.length; i++) {            
            Field field = (Field)thisObjectFields.toArray()[i];
            String checkedFieldValue = (String)checkedFieldsMap.get(field.getName());
            logWriter.print("Checking field's '" + field.getName() + "' value ... ");
            Value fieldValue = thisObjectFieldValues[i];
            String stringFieldValue = "";
            switch (fieldValue.getTag()) {
                case JDWPConstants.Tag.CLASS_OBJECT_TAG: {
                    long objectID = fieldValue.getLongValue();
                    ReplyPacket reflectedTypeReply = debuggeeWrapper.vmMirror.getReflectedType(objectID);
                    byte refType = reflectedTypeReply.getNextValueAsByte();
                    long refTypeID = reflectedTypeReply.getNextValueAsReferenceTypeID();                    
                    String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeID);                    
                    stringFieldValue = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/','.');
                    break;
                }
                case JDWPConstants.Tag.CLASS_LOADER_TAG:
                case JDWPConstants.Tag.OBJECT_TAG: {
                    long objectID = fieldValue.getLongValue();
                    long refTypeId = debuggeeWrapper.vmMirror.getReferenceType(objectID);
                    String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeId);
                    stringFieldValue = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/','.');
                    break;
                }
                case JDWPConstants.Tag.INT_TAG: 
                    stringFieldValue = String.valueOf(fieldValue.getIntValue());
                    break;
                case JDWPConstants.Tag.LONG_TAG: 
                    stringFieldValue = String.valueOf(fieldValue.getLongValue());
                    break;
                case JDWPConstants.Tag.STRING_TAG: 
                    stringFieldValue = debuggeeWrapper.vmMirror.getStringValue(fieldValue.getLongValue());
                    break;
                case JDWPConstants.Tag.ARRAY_TAG: 
                    Value[] arrayValues = debuggeeWrapper.vmMirror.getArrayValues(fieldValue.getLongValue());
//                    if (arrayValues.length != checkedObjectFieldValues[i].length()) {
                    if (arrayValues.length != checkedFieldValue.length()) {
                        debuggeeWrapper.vmMirror.resume();
                        //return failed("Expected size of field '" + field.getName() + "' is " + checkedObjectFieldValues[i].length() + "instead of " + arrayValues.length);
                        return failed("Expected size of field '" + field.getName() + "' is " + checkedFieldValue.length() + "instead of " + arrayValues.length);
                    }
                    
                    for (int j = 0; j < arrayValues.length; j++) {
                        stringFieldValue += arrayValues[j].getCharValue();
                    }
                    break;
                default:
                    break;
            }
            
//            if (!stringFieldValue.equals(checkedObjectFieldValues[i])) {
            if (!stringFieldValue.equals(checkedFieldValue)) {
                debuggeeWrapper.vmMirror.resume();
//                return failed("Expected field's '" + checkedObjectFieldNames[i] + "' value is '" + checkedObjectFieldValues[i] + "' instead of '" + stringFieldValue + "'");
                return failed("Expected field's '" + field.getName() + "' value is '" + checkedFieldValue + "' instead of '" + stringFieldValue + "'");
            }
            logWriter.println("OK");
        }
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new OV01Test().test(args));
    }
}
