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
package org.apache.harmony.test.share.jpda.jdwp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.harmony.jpda.tests.framework.DebuggeeRegister;
import org.apache.harmony.jpda.tests.framework.TestErrorException;
import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Field;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;

/**
 * Created on 12.04.2005 
 */
public abstract class JDWPQATestCase extends JDWPQARawTestCase {
    
    protected JDWPQADebuggeeWrapper debuggeeWrapper;
    protected EventPacket vmStartEvent;
    protected DebuggeeRegister debuggeeRegister;
    protected boolean server = true;
    protected boolean showStaticFields = false;
    protected boolean showConstants = false;
    private int recursionDepth = 0;    
    
    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#internalSetUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp(); 
                
//        debuggeeWrapper = new JDWPQADebuggeeWrapper(settings, logWriter, (server) ? TestOptions.LISTENING : Configurator.ATTACHING);
        if (settings.getDebuggeeLaunchKind().equals("manual")) {
            logWriter.println("manual wrapper");
            debuggeeWrapper = new JDWPQAManualDebuggeeWrapper(settings,
                    logWriter, server);
        } else if (settings.getDebuggeeLaunchKind().equals("auto")){
            logWriter.println("auto wrapper");
            debuggeeWrapper = new JDWPQADebuggeeWrapper(settings,
                    logWriter, server);
        }

        debuggeeRegister = new DebuggeeRegister();
        debuggeeRegister.register(debuggeeWrapper);
        
        debuggeeWrapper.start();
        logWriter.println("Launched debuggee VM process and established connection");
        
        vmStartEvent = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.VM_INIT);
        logWriter.println("Received VM_START event");

        debuggeeWrapper.vmMirror.adjustTypeLength();
        logWriter.println("Adjusted VM-dependent type lengths");
    }    
    
    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#internalTearDown()
     */
    protected void tearDown() throws Exception
    {      
        if (debuggeeRegister != null) {
            debuggeeRegister.stopAllRegistered();
            logWriter.println("Finished debuggee VM process and closed connection");
        }
        
        super.tearDown();        
    }
    
    protected List getStaticFieldsInClass(long classID) {
        List allFields = debuggeeWrapper.vmMirror.getAllFields(classID);
        List staticFields = new ArrayList(0);
        for (int i = 0; i < allFields.size(); i++) {
            Field field = (Field)allFields.toArray()[i];
            if (field.isStatic()) {
                // if showConstants option is enabled then this field is put into the list regardless of the fact that
                // showStaticFields option is disabled
                if (field.isFinal()) {
                    if (showConstants) {
                        staticFields.add(field);
                    }
                } else if (showStaticFields) {
                    staticFields.add(field);
                }
            }
        }

        return staticFields;
    }
    
    protected List getInstanceFieldsInClass(long classID) {
        List allFields = debuggeeWrapper.vmMirror.getAllFields(classID);
        List instanceFields = new ArrayList(0);
        for (int i = 0; i < allFields.size(); i++) {
            Field field = (Field)allFields.toArray()[i];
            if (!field.isStatic()) {
                instanceFields.add(field);
            }
        }

        return instanceFields;
    }
    
    protected Map getAllClasses() {
        CommandPacket command = new CommandPacket(
            JDWPCommands.VirtualMachineCommandSet.CommandSetID,
            JDWPCommands.VirtualMachineCommandSet.AllClassesCommand);
        ReplyPacket reply = debuggeeWrapper.vmMirror.checkReply(debuggeeWrapper.vmMirror.performCommand(command));
        int classes = reply.getNextValueAsInt();
        Map map = new HashMap(classes);
        for (int i = 0; i < classes; i++) {
            byte refTypeTag = reply.getNextValueAsByte();
            long reftTypeID = reply.getNextValueAsReferenceTypeID();
            String signature = reply.getNextValueAsString();
            int status = reply.getNextValueAsInt();
            map.put(new Long(reftTypeID), signature);
        }
        
        return map;
    }
    
    
    private void printStaticFields(long classID) {
        List staticFields = getStaticFieldsInClass(classID);
        for (int i = 0; i < staticFields.size(); i++) {
            Field field = (Field)staticFields.toArray()[i];
            Value staticFieldValue = debuggeeWrapper.vmMirror.getReferenceTypeValues(field.getClassID(), new long[] {field.getID()})[0];
            
            byte valueTag = staticFieldValue.getTag(); 
            if (valueTag == JDWPConstants.Tag.OBJECT_TAG || valueTag == JDWPConstants.Tag.CLASS_LOADER_TAG) {
                long objectID = staticFieldValue.getLongValue();
                if (objectID != 0 && classID == debuggeeWrapper.vmMirror.getReferenceType(objectID)) {
                    String fieldSignature = field.getSignature();
                    fieldSignature = fieldSignature.substring(1, fieldSignature.length() - 1 /*skip ending ';'*/).replace('/','.');
                    String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(classID);
                    typeSignature = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/','.');
                    logWriter.println(fieldSignature + " " + field.getName() + " = " + typeSignature);
                    continue;
                }
            } 
            
            printFieldValue(staticFieldValue, field);
        }
    }
    
    private void printInstanceFields(long objectID) {
        List instanceFields = getInstanceFieldsInClass(debuggeeWrapper.vmMirror.getReferenceType(objectID));
        if (instanceFields.size() == 0) {
            return;
        }
        
        long[] fieldIDs = new long[instanceFields.size()];
        for (int i = 0; i < instanceFields.size(); i++) {
            Field field = (Field)instanceFields.toArray()[i];
            fieldIDs[i] = field.getID();
        }        
        
        Value[] instanceValues = debuggeeWrapper.vmMirror.getObjectReferenceValues(objectID, fieldIDs); 
        for (int i = 0; i < instanceValues.length; i++) {
            Field field = (Field)instanceFields.toArray()[i];
            Value value = instanceValues[i];
            printFieldValue(value, field);
        }
    }

    private void printFieldValue(Value value, Field field) {
        String message = "";
        String indentation = "";
        for (int j = 0; j < recursionDepth; j++) {
            indentation += "  ";
        }
        
        if ((field.getModBits() & JDWPConstants.FieldAccess.ACC_STATIC) == JDWPConstants.FieldAccess.ACC_STATIC) {
            indentation += "(S) ";
        }
        
        if ((field.getModBits() & JDWPConstants.FieldAccess.ACC_FINAL) == JDWPConstants.FieldAccess.ACC_FINAL) {
            indentation += "(F) ";        
        }
        
        switch (value.getTag()) {
            case JDWPConstants.Tag.BOOLEAN_TAG:
                message = indentation + "boolean " + field.getName() + " = " + value.getBooleanValue();
                break;
            case JDWPConstants.Tag.BYTE_TAG:
                message = indentation + "byte " + field.getName() + " = " + value.getByteValue();
                break;
            case JDWPConstants.Tag.CHAR_TAG:
                message = indentation + "char " + field.getName() + " = " + value.getCharValue();
                break;
            case JDWPConstants.Tag.DOUBLE_TAG:
                message = indentation + "double " + field.getName() + " = " + value.getDoubleValue();
                break;
            case JDWPConstants.Tag.FLOAT_TAG:
                message = indentation + "float " + field.getName() + " = " + value.getFloatValue();
                break;
            case JDWPConstants.Tag.INT_TAG:
                message = indentation + "int " + field.getName() + " = " + value.getIntValue();
                break;
            case JDWPConstants.Tag.LONG_TAG:
                message = indentation + "long " + field.getName() + " = " + value.getLongValue();
                break;
            case JDWPConstants.Tag.SHORT_TAG:
                message = indentation + "short " + field.getName() + " = " + value.getShortValue();
                break;
            case JDWPConstants.Tag.STRING_TAG:
                String stringValue = debuggeeWrapper.vmMirror.getStringValue(value.getLongValue());             
                stringValue = "\"" + stringValue + "\"";
                message = indentation + "java.lang.String " + field.getName() + " = " + stringValue;
                break;               
            case JDWPConstants.Tag.CLASS_OBJECT_TAG: {
                String fieldType = field.getType(); 
                message = indentation + fieldType + " " + field.getName();
                long objectID = value.getLongValue();
                if (objectID == 0) {
                    message += " = null";
                } else {
                    ReplyPacket reply = debuggeeWrapper.vmMirror.getReflectedType(objectID);
                    byte refType = reply.getNextValueAsByte();
                    long refTypeID = reply.getNextValueAsReferenceTypeID();                 
                    String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeID);
                    message += " = " + fieldType + " (" + typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/','.') + ")";
                    //getObjectValues(objectID);
                }   
                break;                  
            }
            case JDWPConstants.Tag.CLASS_LOADER_TAG:
            case JDWPConstants.Tag.OBJECT_TAG: {
                String fieldType = field.getType();
                message = indentation + fieldType + " " + field.getName();
                long objectID = value.getLongValue();
                if (objectID == 0) {
                    message += " = null";
                } else {
                    long refTypeId = debuggeeWrapper.vmMirror.getReferenceType(objectID);
                    String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeId);
                    typeSignature = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/','.');
                    message += " = " + typeSignature;
                    //getObjectValues(objectID);                        
                }
                break;              
            }
            case JDWPConstants.Tag.ARRAY_TAG: {
                Value[] arrayValues = debuggeeWrapper.vmMirror.getArrayValues(value.getLongValue());                     
                String fieldType = field.getType();
                if (arrayValues == null) {
                    logWriter.print(indentation + fieldType + " " + field.getName() + " = " + fieldType.substring(0, fieldType.length() - 2) + "[0]");
                } else {                    
                    logWriter.print(indentation + fieldType + " " + field.getName() + " = " + fieldType.substring(0, fieldType.length() - 2) + "[" + arrayValues.length + "]");
                    for (int i = 0; i < arrayValues.length; i++) {
                        message += indentation + "    " + fieldType.substring(0, fieldType.length() - 2) + "[" + i + "] = ";
                        switch (arrayValues[i].getTag()) {
                            case JDWPConstants.Tag.BOOLEAN_TAG:
                                message += arrayValues[i].getBooleanValue() + "\n";
                                break;
                            case JDWPConstants.Tag.BYTE_TAG:
                                message += arrayValues[i].getByteValue() + "\n";
                                break;
                            case JDWPConstants.Tag.CHAR_TAG:
                                message += arrayValues[i].getCharValue() + "\n";
                                break;
                            case JDWPConstants.Tag.DOUBLE_TAG:
                                message += arrayValues[i].getDoubleValue() + "\n";
                                break;
                            case JDWPConstants.Tag.FLOAT_TAG:
                                message += arrayValues[i].getFloatValue() + "\n";
                                break;
                            case JDWPConstants.Tag.INT_TAG:
                                message += arrayValues[i].getIntValue() + "\n";
                                break;
                            case JDWPConstants.Tag.LONG_TAG:
                                message += arrayValues[i].getLongValue() + "\n";
                                break;
                            case JDWPConstants.Tag.SHORT_TAG:
                                message += arrayValues[i].getShortValue() + "\n";
                                break;
                            case JDWPConstants.Tag.STRING_TAG:
                                message += debuggeeWrapper.vmMirror.getStringValue(arrayValues[i].getLongValue()) + "\n";
                                break;
                        }
                    }
                }               
                break;
            }
            default: {
                break;
            }       
        }    
        
        logWriter.print(message);
    }
    
    private void printFrameVarsValues(Frame frame, boolean printObjectValues) {
        logWriter.println("  [VARIABLES]");
//        logWriter.print("Acquiring 'this' object for the frame '" + getFrameName(frame) + "' of the thread '" + debuggeeWrapper.vmMirror.getThreadName(frame.getThreadID()) + "' ... ");
        long thisObjectID = debuggeeWrapper.vmMirror.getThisObject(frame.getThreadID(), frame.getID());
        
//        logWriter.println("OK");
//        logWriter.println("'this' object ID is : " + thisObjectID);
        // static fields must be printed independently in case there is no 'this' object yet 
        if (thisObjectID == 0 /*|| !printObjectValues*/) {
            printStaticFields(frame.getLocation().classID);
        }

        List vars = debuggeeWrapper.vmMirror.getLocalVars(frame);
        if (vars == null) {
            logWriter.println("local variables unavailable");
            return;
        }
        
        if (vars.size() == 0) {
            logWriter.println("No local variables");
            return;
        }
            
        frame.setVars((ArrayList)vars);           
        Value[] frameValues = debuggeeWrapper.vmMirror.getFrameValues(frame);
        for (int i = 0; i < frameValues.length; i++) {
            Value frameValue = frameValues[i];
            String msg = "";
            Frame.Variable var = (Frame.Variable)frame.getVars().toArray()[i];
            switch (frameValue.getTag()) {              
                case JDWPConstants.Tag.ARRAY_TAG:
                    Value[] values = debuggeeWrapper.vmMirror.getArrayValues(frameValue.getLongValue());                
                    msg = var.getType() + " " + var.getName() + " = ";
                    if (values == null) {                       
                        msg += var.getType().substring(0, var.getType().length() - 2) + "[0]";                      
                    }
                    else {
                        msg += var.getType() + "[" + values.length + "]";
                        byte tag = values[0].getTag();
                        for (int j = 0; i < values.length; i++) {
                            switch (tag) {
                                case JDWPConstants.Tag.STRING_TAG:
                                    msg += "-->java.lang.String[" + j + "] = \"" + debuggeeWrapper.vmMirror.getStringValue(values[j].getLongValue()) + "\"";
                                    break;
                                case JDWPConstants.Tag.OBJECT_TAG:
                                    msg += "-->" + var.getType() + "[" + j + "] = " + var.getType(); 
                                    printObjectValues(values[j].getLongValue());
                                    break;
                                case JDWPConstants.Tag.BOOLEAN_TAG:
                                    msg += "-->boolean[" + j + "] = \"" + values[j].getBooleanValue() + "\"";
                                    break;
                                case JDWPConstants.Tag.BYTE_TAG:
                                    msg += "-->byte[" + j + "] = \"" + values[j].getByteValue() + "\"";
                                    break;
                                case JDWPConstants.Tag.CHAR_TAG:
                                    msg += "-->char[" + j + "] = \"" + values[j].getCharValue() + "\"";
                                    break;
                                case JDWPConstants.Tag.DOUBLE_TAG:
                                    msg += "-->double[" + j + "] = \"" + values[j].getDoubleValue() + "\"";
                                    break;
                                case JDWPConstants.Tag.FLOAT_TAG:
                                    msg += "-->float[" + j + "] = \"" + values[j].getFloatValue() + "\"";
                                    break;
                                case JDWPConstants.Tag.INT_TAG:
                                    msg += "-->int[" + j + "] = \"" + values[j].getIntValue() + "\"";
                                    break;
                                case JDWPConstants.Tag.LONG_TAG:
                                    msg += "-->long[" + j + "] = \"" + values[j].getLongValue() + "\"";
                                    break;
                                case JDWPConstants.Tag.SHORT_TAG:
                                    msg += "-->short[" + j + "] = \"" + values[j].getShortValue() + "\"";
                                    break;
                                default:
                                    msg += "-->" + var.getType() + "[" + j + "] = \"\"";                                
                            }                                       
                        }
                    }
                    logWriter.println(msg);
                    break;
                case JDWPConstants.Tag.STRING_TAG:
                    msg = "String " + var.getName() + " = \"" + debuggeeWrapper.vmMirror.getStringValue(frameValue.getLongValue()) + "\"";
                    logWriter.println(msg);
                    break;
                case JDWPConstants.Tag.CLASS_OBJECT_TAG: {
                    ReplyPacket reply = debuggeeWrapper.vmMirror.getReflectedType(frameValue.getLongValue());
                    byte refType = reply.getNextValueAsByte();
                    long refTypeID = reply.getNextValueAsReferenceTypeID();
                    String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeID); 
                    logWriter.println(var.getType() + " " + var.getName() + " = " + typeSignature.substring(1, typeSignature.length() - 1).replace('/','.'));
                    if (printObjectValues) {
                        printObjectValues(frameValue.getLongValue());
                    }
                    break;
                }
                case JDWPConstants.Tag.CLASS_LOADER_TAG:
                case JDWPConstants.Tag.THREAD_TAG:
                case JDWPConstants.Tag.OBJECT_TAG: {
                    long refTypeId = debuggeeWrapper.vmMirror.getReferenceType(frameValue.getLongValue());
                    String typeSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(refTypeId);
                    typeSignature = typeSignature.substring(1, typeSignature.length() - 1 /*skip ending ';'*/).replace('/','.');                    
                    logWriter.println(var.getType() + " " + var.getName() + " = " + typeSignature);
                    if (printObjectValues) {
                        printObjectValues(frameValue.getLongValue());
                    }
                    continue;
                }
                case JDWPConstants.Tag.BYTE_TAG:
                    msg = "byte " + var.getName() + " = " + String.valueOf(frameValue.getByteValue());
                    logWriter.println(msg);
                    break;
                case JDWPConstants.Tag.BOOLEAN_TAG:
                    msg = "boolean " + var.getName() + " = " + String.valueOf(frameValue.getBooleanValue());
                    logWriter.println(msg);
                    break;
                case JDWPConstants.Tag.CHAR_TAG:
                    msg = "char " + var.getName() + " = " + String.valueOf(frameValue.getCharValue());
                    logWriter.println(msg);
                    break;                  
                case JDWPConstants.Tag.FLOAT_TAG:
                    msg = "float " + var.getName() + " = " + String.valueOf(frameValue.getFloatValue());
                    logWriter.println(msg);
                    break;                  
                case JDWPConstants.Tag.DOUBLE_TAG:
                    msg = "double " + var.getName() + " = " + String.valueOf(frameValue.getDoubleValue());
                    logWriter.println(msg);
                    break;                  
                case JDWPConstants.Tag.INT_TAG:
                    msg = "int " + var.getName() + " = " + String.valueOf(frameValue.getIntValue());
                    logWriter.println(msg);
                    break;                  
                case JDWPConstants.Tag.LONG_TAG:
                    msg = "long " + var.getName() + " = " + String.valueOf(frameValue.getLongValue());
                    logWriter.println(msg);
                    break;              
                case JDWPConstants.Tag.SHORT_TAG:
                    msg = "short " + var.getName() + " = " + String.valueOf(frameValue.getShortValue());
                    logWriter.println(msg);
                    break;
                default:
                    break;
            }
        }
    }
    
    
    private void printObjectValues(long objectID) {
        recursionDepth++;        
        long refTypeID = debuggeeWrapper.vmMirror.getReferenceType(objectID);
        printStaticFields(refTypeID);
        printInstanceFields(objectID);
        recursionDepth--;
        
    }    
    

    protected int printThreads(boolean showSystem) {
        logWriter.println("\n--- Threads ---");
        ReplyPacket reply = debuggeeWrapper.vmMirror.getAllThreadID();
        int threads = reply.getNextValueAsInt();
        if (threads == 0) {
            throw new TestErrorException("Zero number of threads was encountered.");
        }
        
        int threadsProcessed = 0;
        for (int i = 0; i < threads; i++) {
            long threadID = reply.getNextValueAsThreadID();
            long threadGroupID = debuggeeWrapper.vmMirror.getThreadGroupID(threadID);
            String threadGroupName = debuggeeWrapper.vmMirror.getThreadGroupName(threadGroupID);
            if (threadGroupName.indexOf("system") != -1) {
                if (!showSystem) {
                    continue;
                }
            }
            ++threadsProcessed;
            printThread(threadID);
        }
        logWriter.println("--- Threads ---\n");
        
        return threadsProcessed;
    }
    
    private void printThread(long threadID) {
        long threadGroupID = debuggeeWrapper.vmMirror.getThreadGroupID(threadID);
        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
        String threadGroupName = debuggeeWrapper.vmMirror.getThreadGroupName(threadGroupID);
        int threadStatus = debuggeeWrapper.vmMirror.getThreadStatus(threadID);
        String threadStatusString = JDWPConstants.ThreadStatus.getName(threadStatus);
        String message = "";
        if (threadGroupName.indexOf("system") != -1) {
            message += "System thread ";
        } else {
            message += "Thread ";
        }
        
        message += "[" + threadName + "]";
        if (debuggeeWrapper.vmMirror.isThreadSuspended(threadID)) {
            message += "(Suspended)";
        } else {
            message += "(" + threadStatusString + ")";
        }
        
        logWriter.print(message);
    }
    
    
    protected int printStackFrames(long threadID, boolean printVariables, boolean printObjectValues) {
        printThread(threadID);
        if (!debuggeeWrapper.vmMirror.isThreadSuspended(threadID)) {
            return 0;
        }
        
        int framesProcessed = 0;
        List frames = debuggeeWrapper.vmMirror.getAllThreadFrames(threadID);
        if (frames.size() > 0) {
            Iterator it = frames.iterator();
            while (it.hasNext()) {
                Frame frame = (Frame)it.next();
                ++framesProcessed;
                String qualifiedFrameName = getFrameName(frame);
                int stoppedLine = getFrameLine(frame);
                if (stoppedLine != -1) {
                    qualifiedFrameName += ", line: " + stoppedLine;
                } else {
                     qualifiedFrameName += ", line: unavailable";
                }
                logWriter.println("  [FRAME] " + qualifiedFrameName);
                if (printVariables) {
                    printFrameVarsValues(frame, printObjectValues);
                }
            }
        }
        
        return framesProcessed;
    }
    
    protected String getFrameName(Frame frame) {
        Location location = frame.getLocation();
        String className = debuggeeWrapper.vmMirror.getReferenceTypeSignature(location.classID);
        className = className.substring(1, className.length() - 1).replace('/','.');
        String methodName = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        String methodSignature = debuggeeWrapper.vmMirror.getMethodSignature(location.classID, location.methodID);
        //int lineNumber = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);
        //String frameName = className + "." + methodName + methodSignature + " line: ";
        String frameName = className + "." + methodName + methodSignature;
//        if (lineNumber != -1) {
//            frameName += String.valueOf(lineNumber);
//        } else {
//            frameName += "unavailable";
//        }
        
        return frameName;
    }
    
    protected int getFrameLine(Frame frame) {
        Location location = frame.getLocation();
        return debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
    }
    
    protected boolean inArray(Object[] hayStack, Object needle) {
        for (int i = 0; i < hayStack.length; i++) {            
            if (hayStack[i].equals(needle)) {
                return true;
            }
        }
        
        return false;
    }
}    
