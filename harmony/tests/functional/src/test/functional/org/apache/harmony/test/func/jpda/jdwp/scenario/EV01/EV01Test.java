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
package org.apache.harmony.test.func.jpda.jdwp.scenario.EV01;

import java.util.ArrayList;
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
 * Created on 01.06.2005 
 */
public class EV01Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testEV01() {
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

        logWriter.println("Show only local variables");
        printStackFrames(threadID, true /*print local vars*/, false /*print object fields*/);
            
        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
        logWriter.print("\n------------------------------");
        logWriter.print("Acquiring the current (topmost) frame of the '" + threadName + "' thread.");
        reply = debuggeeWrapper.vmMirror.getThreadFrames(threadID, 0, 1);
        if (reply.getNextValueAsInt() == 0) { // no frames were returned
            return failed("Couldn't get current frame of the '" + debuggeeWrapper.vmMirror.getThreadName(threadID) + "' thread");
        }
        logWriter.print("OK");        
        
        Frame frame = new Frame();
        frame.setID(reply.getNextValueAsFrameID());
        frame.setThreadID(threadID);
        frame.setLocation(reply.getNextValueAsLocation());
        String qualifiedFrameName = getFrameName(frame);
        logWriter.print("The current frame is '" + qualifiedFrameName + "'");
        logWriter.print("------------------------------\n");
        
        logWriter.print("Acquiring local variables of frame '" + qualifiedFrameName + "'");
        List localVars = debuggeeWrapper.vmMirror.getLocalVars(frame);
        if (localVars == null) {
            return failed("Couldn't get local variables of frame " + qualifiedFrameName);
        }
        
        if (localVars.size() == 0) {
            return failed("Zero number of local variables of frame " + qualifiedFrameName + " was encountered");
        }
        logWriter.print("OK");
        
        String[] localVarsNames = {
            "", "local_int_x", "local_long_y", 
        };
        
        logWriter.println("Start checking...");
        
        Frame.Variable[] checkedVarsArray = new Frame.Variable[2];
        ArrayList checkedVarsList = new ArrayList(0);
        for (int i = 0; i < localVars.size(); i++) {
            Frame.Variable var = (Frame.Variable)localVars.toArray()[i];
            if (!var.getName().equals(localVarsNames[i])) {
                continue;
            }
            checkedVarsList.add(var);
        }
        
        for (int i = 0; i < checkedVarsList.size(); i++) {
            checkedVarsArray[i] = (Frame.Variable)checkedVarsList.toArray()[i];
        }

        int newLocalIntValue = 10;
        long newLocalLongValue = 100000000L;
        Value[] valuesToSet = {
            new Value(newLocalIntValue),
            new Value(newLocalLongValue),
        };

        logWriter.println("\nSetting a new value to a local variable 'local_int_x'. A new value is: " + newLocalIntValue);
        logWriter.println("Setting a new value to a local variable 'local_long_y'. A new value is: " + newLocalLongValue);
        debuggeeWrapper.vmMirror.setLocalVars(frame, checkedVarsArray, valuesToSet);
        
        logWriter.print("Check whether new values were set properly...");
        frame.setVars(checkedVarsList);
        Value[] checkedVarsValues = debuggeeWrapper.vmMirror.getFrameValues(frame);
        for (int i = 0; i < checkedVarsValues.length; i++) {
            Value varValue = checkedVarsValues[i];
            switch (varValue.getTag()) {
                case JDWPConstants.Tag.INT_TAG:
                    if (varValue.getIntValue() != newLocalIntValue) {
                        String varName = ((Frame.Variable)checkedVarsList.toArray()[i]).getName();
                        return failed("Expected variable '" + varName + "' value is: " + newLocalIntValue + " instead of " + varValue.getIntValue());
                    }
                    break;
                case JDWPConstants.Tag.LONG_TAG:
                    if (varValue.getLongValue() != newLocalLongValue) {
                        String varName = ((Frame.Variable)checkedVarsList.toArray()[i]).getName();
                        return failed("Expected variable '" + varName + "' value is: " + newLocalLongValue + " instead of " + varValue.getLongValue());
                    }
                    break;
                default:
                    break;
            }
        }
        
        logWriter.println("OK");
        logWriter.println("\nShow local variables after new values have been set.");
        printStackFrames(threadID, true /*print local vars*/, false /*print object fields*/);
        
        logWriter.println("\nResume debuggee VM");
        debuggeeWrapper.vmMirror.resume();
        
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new EV01Test().test(args));
    }
}