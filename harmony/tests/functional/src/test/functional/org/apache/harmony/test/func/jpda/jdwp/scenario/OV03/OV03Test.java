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
package org.apache.harmony.test.func.jpda.jdwp.scenario.OV03;

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
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
/**
 * Created on 01.06.2005 
 */
public class OV03Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }

    public Result testOV03() {
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
        
        showConstants = true;
        showStaticFields = true;
        logWriter.println("\nShow local variables with object fields including static fields and constants");
        printStackFrames(threadID, true /*print local vars*/, true /*print object values*/);        
        
        HashMap staticFieldsTypesMap = new HashMap();
        staticFieldsTypesMap.put("constant_value", "int");
        staticFieldsTypesMap.put("instance_count", "int");
        staticFieldsTypesMap.put("singletone", "boolean");
                
        HashMap instanceFieldsTypesMap = new HashMap();
        instanceFieldsTypesMap.put("logWriter", "org.apache.harmony.test.share.jpda.jdwp.JDWPQALogWriter");
        instanceFieldsTypesMap.put("settings", "org.apache.harmony.jpda.tests.framework.TestOptions");
        instanceFieldsTypesMap.put("int_value", "int");
        instanceFieldsTypesMap.put("long_value", "long");
        instanceFieldsTypesMap.put("string_value", "java.lang.String");
        instanceFieldsTypesMap.put("array_value", "char[]");
        //checking
//        String[] checkedStaticFieldTypes = {
//            "int", "int", "boolean", "java.lang.Class", 
//        };
//        
//        String[] checkedInstanceFieldTypes = {
//            "org.apache.harmony.test.share.jpda.jdwp.JDWPQALogWriter", "org.apache.harmony.jpda.tests.framework.TestOptions", "int", "long", "java.lang.String", "char[]"
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

        String objectName = debuggeeWrapper.vmMirror.getReferenceTypeSignature(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID));
        objectName = objectName.substring(1, objectName.length() - 1).replace('/','.');
        logWriter.print("Checking types of static fields and constants of object " + objectName + " ...");
        List staticAndConstantFields = getStaticFieldsInClass(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID));
//        if (staticAndConstantFields.size() != checkedStaticFieldTypes.length) {
        if (staticAndConstantFields.size() < staticFieldsTypesMap.size()) {
            debuggeeWrapper.vmMirror.resume();
            //return failed("Wrong number of static fields and constants was encountered. Expected " + checkedStaticFieldTypes.length + " field(s) instead of " + staticAndConstantFields.size());
            return failed("Wrong number of static fields and constants was encountered. Expected at least " + staticFieldsTypesMap.size() + " field(s) instead of " + staticAndConstantFields.size());
        }
        
        for (int i = 0; i < staticAndConstantFields.size(); i++) {
            Field field = (Field) staticAndConstantFields.toArray()[i];
           
            String checkedFieldType = (String) staticFieldsTypesMap.get(field
                    .getName());
            if (checkedFieldType != null) {
                logWriter.print("Checking the field's '" + field.getName()
                        + "' type...");
                // if (!field.getType().equals(checkedStaticFieldTypes[i])) {
                if (!field.getType().equals(checkedFieldType)) {
                    debuggeeWrapper.vmMirror.resume();
                    // return failed("Expected field's '" + field.getName() + "'
                    // type is '" + checkedStaticFieldTypes[i] + "' instead of
                    // '" + field.getType() + "'");
                    return failed("Expected field's '" + field.getName()
                            + "' type is '" + checkedFieldType
                            + "' instead of '" + field.getType() + "'");
                }
                logWriter.println("OK");
            }
        }
         
        logWriter.println("OK");
        logWriter.print("Checking types of instance fields of object " + objectName + " ...");
        List instanceFields = getInstanceFieldsInClass(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID));
//        if (instanceFields.size() != checkedInstanceFieldTypes.length) {
        if (instanceFields.size() < instanceFieldsTypesMap.size()) {
            debuggeeWrapper.vmMirror.resume();
//            return failed("Wrong number of instance fields was encountered. Expected " + checkedInstanceFieldTypes.length + " field(s) instead of " + instanceFields.size());
            return failed("Wrong number of instance fields was encountered. Expected at least " + instanceFieldsTypesMap.size() + " field(s) instead of " + instanceFields.size());
        }
        
        for (int i = 0; i < instanceFields.size(); i++) {
            Field field = (Field) instanceFields.toArray()[i];
            String checkedFieldType = (String) instanceFieldsTypesMap.get(field
                    .getName());
            if (checkedFieldType != null) {
                logWriter.print("Checking the field's '" + field.getName()
                        + "' type...");
                // if (!field.getType().equals(checkedInstanceFieldTypes[i])) {
                if (!field.getType().equals(checkedFieldType)) {
                    debuggeeWrapper.vmMirror.resume();
                    // return failed("Expected field's '" + field.getName() + "'
                    // type is '" + checkedInstanceFieldTypes[i] + "' instead of
                    // '" + field.getType() + "'");
                    return failed("Expected field's '" + field.getName()
                            + "' type is '" + checkedFieldType
                            + "' instead of '" + field.getType() + "'");
                }
                logWriter.println("OK");
            }
        }
            
        logWriter.println("OK");
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();

        return passed();
    }
    
    public static void main(String[] args) {
        System.exit(new OV03Test().test(args));
    }
}
