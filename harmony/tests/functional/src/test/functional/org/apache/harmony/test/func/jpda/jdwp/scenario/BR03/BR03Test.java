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
package org.apache.harmony.test.func.jpda.jdwp.scenario.BR03;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;

/**
 * Created on 17.05.2005 
 */
public class BR03Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testBR03() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        
        String method = "main";        
        
        long debuggeeClassId = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassId, methodId);
        long start = reply.getNextValueAsLong();
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)start);        
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint couldn't be set.");
        }
        
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, methodId, start); 
        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();       
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        event.getNextValueAsByte(); // suspend policy, is not used
        event.getNextValueAsInt(); // events in set, is not used
        event.getNextValueAsByte(); // event kind, is not used
        event.getNextValueAsInt(); // requestID, is not used;
        long threadID = event.getNextValueAsThreadID();
        Location location = event.getNextValueAsLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + lineStopped);
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");
        

        showConstants = true;
        showStaticFields = true;
        printStackFrames(threadID, true /*print local vars*/, false /*print object values*/);
        
        logWriter.println("Setting a breakpoint at a line with a first call to 'println' in a run method.");
        method = "run";        
        methodId = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassId, method);
        breakpointLine = 52; //
        
        breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)debuggeeWrapper.vmMirror.getLineCodeIndex(debuggeeClassId, methodId, breakpointLine));
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        
        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        event.getNextValueAsByte(); // suspend policy, is not used
        event.getNextValueAsInt(); // events in set, is not used
        event.getNextValueAsByte(); // event kind, is not used
        event.getNextValueAsInt(); // requestID, is not used;
        threadID = event.getNextValueAsThreadID();
        location = event.getNextValueAsLocation();
        methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        lineStopped = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassId, location.methodID, location.index);      
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + " at line " + lineStopped);
        logWriter.print("Check whether breakpoint was set at the expected location...");
        if (location.methodID != methodId || lineStopped != breakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + lineStopped);
        }
        logWriter.print("OK");
        
        showConstants = true;
        showStaticFields = true;
        printStackFrames(threadID, true /*print local vars*/, false /*print object values*/);
        
        logWriter.println("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        
    
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new BR03Test().test(args));
    }
}
