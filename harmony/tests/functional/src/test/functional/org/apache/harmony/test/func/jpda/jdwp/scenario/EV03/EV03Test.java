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
package org.apache.harmony.test.func.jpda.jdwp.scenario.EV03;

import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.share.Result;
import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;

/**
 * Created on 02.06.2005 
 */
public class EV03Test extends JDWPQATestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    
    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testEV03() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        String method = "run";
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        
        int breakpointLine = 52; // a line with first call to 'ptintln'
        long lineCodeIndex = debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine);
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)lineCodeIndex);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint, JDWPConstants.SuspendPolicy.EVENT_THREAD);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
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
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");

        showStaticFields = true;
        logWriter.println("Show local variables and object fields including static ones");
        printStackFrames(threadID, true /*print local vars*/, true /*print object fields*/);

        reply = debuggeeWrapper.vmMirror.getThreadFrames(threadID, 0, 1);
        if (reply.getNextValueAsInt() == 0) { // no frames were returned
            return failed("Couldn't get current frame of the '" + debuggeeWrapper.vmMirror.getThreadName(threadID) + "' thread");
        }
        
        Frame frame = new Frame();
        frame.setID(reply.getNextValueAsFrameID());
        frame.setThreadID(threadID);
        frame.setLocation(reply.getNextValueAsLocation());
        String qualifiedFrameName = getFrameName(frame);
        long thisObjectID = debuggeeWrapper.vmMirror.getThisObject(threadID, frame.getID());
        if (thisObjectID == 0) {
            return failed("Couldn't get 'this' object ID");
        }
        
        String[] checkedStaticFieldNames = {
            "instance_count", "singletone"
        };
        
        int newIntValue = 5;
        boolean newBooleanValue = true;
        
        long intValueFieldID = debuggeeWrapper.vmMirror.getFieldID(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID), checkedStaticFieldNames[0]);
        long booleanValueFieldID = debuggeeWrapper.vmMirror.getFieldID(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID), checkedStaticFieldNames[1]);
        
        Value[] valuesToSet = {
            new Value(newIntValue),
            new Value(newBooleanValue)
        };
        
        logWriter.println("\nSetting a new value to the field '" + checkedStaticFieldNames[0] + "'. A new value is " + newIntValue);
        logWriter.println("Setting a new value to the field '" + checkedStaticFieldNames[1] + "'. A new value is '" + newBooleanValue + "'");
        
        debuggeeWrapper.vmMirror.setStaticFieldsValues(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID), new long[] {intValueFieldID, booleanValueFieldID}, valuesToSet);
        
        //checking
        logWriter.print("Check whether new values were set properly...");
        Value[] checkedStaticFieldsValues = debuggeeWrapper.vmMirror.getReferenceTypeValues(debuggeeWrapper.vmMirror.getReferenceType(thisObjectID), new long[] {intValueFieldID, booleanValueFieldID});
        for (int i = 0; i < checkedStaticFieldsValues.length; i++) {
            switch (checkedStaticFieldsValues[i].getTag()) {
                case JDWPConstants.Tag.INT_TAG:
                    if (checkedStaticFieldsValues[i].getIntValue() != newIntValue) {
                        return failed("Expected field's '" + checkedStaticFieldNames[i] + "' value is " + newIntValue + " instead of " + checkedStaticFieldsValues[i].getIntValue());
                    }
                    break;
                case JDWPConstants.Tag.BOOLEAN_TAG:
                    if (checkedStaticFieldsValues[i].getBooleanValue() != newBooleanValue) {
                        return failed("Expected field's '" + checkedStaticFieldNames[i] + "' value is '" + newBooleanValue + "' instead of '" + checkedStaticFieldsValues[i].getBooleanValue() + "'");
                    }
                    break;
            }
        }
        
        logWriter.println("OK");
        logWriter.println("\nShow local variables with object fields after new values have been set");
        printStackFrames(threadID, true /*print local vars*/, true /*print object fields*/);        
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.resume();
        
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new EV03Test().test(args));
    }
}
