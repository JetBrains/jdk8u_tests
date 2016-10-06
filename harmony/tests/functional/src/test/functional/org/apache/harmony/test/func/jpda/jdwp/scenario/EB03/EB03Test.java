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
package org.apache.harmony.test.func.jpda.jdwp.scenario.EB03;

import org.apache.harmony.test.share.jpda.jdwp.JDWPQATestCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;

/**
 * Created on 23.06.2005 
 */
public class EB03Test extends JDWPQATestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.share.jpda.jdwp.debuggee.QADebuggee";
    public final static String DEBUGGEE_CLASS_SIG = "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    private final static String EXCEPTION_CLASS_SIG = "Ljava/lang/ClassNotFoundException;";
    
    /* (non-Javadoc)
     * @see org.apache.harmony.test.share.jpda.jdwp.JDWPQARawTestCase#getDebuggeeClassName()
     */
    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }
    
    public Result testEB03() {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(DEBUGGEE_CLASS_NAME);
        debuggeeWrapper.vmMirror.resume();
        EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);

        String method = "main";
        long debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_CLASS_SIG);
        long methodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID, method);
        reply = debuggeeWrapper.vmMirror.getLineTable(debuggeeClassID, methodID);
        long start = reply.getNextValueAsLong();
        
        Breakpoint breakpoint = new Breakpoint(DEBUGGEE_CLASS_SIG, method, (int)start);
        reply = debuggeeWrapper.vmMirror.setBreakpoint(JDWPConstants.TypeTag.CLASS, breakpoint);
        if (reply.getErrorCode() != JDWPConstants.Error.NONE) {
            return failed("A breakpoint couldn't be set.");
        }        
        int breakpointLine = debuggeeWrapper.vmMirror.getLineNumber(debuggeeClassID, methodID, start);
        logWriter.println("Breakpoint is set in a '" + method + "' method at line " + breakpointLine);
        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();    
        
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);        
        ParsedEvent[] events = ParsedEvent.parseEventPacket(event);
        long threadID = ((ParsedEvent.Event_BREAKPOINT)events[0]).getThreadID();
        Location location = ((ParsedEvent.Event_BREAKPOINT)events[0]).getLocation();
        String methodStopped = debuggeeWrapper.vmMirror.getMethodName(location.classID, location.methodID);
        int stoppedLine = debuggeeWrapper.vmMirror.getLineNumber(location.classID, location.methodID, location.index);        
        logWriter.println("Stopped at breakpoint in a method '" + methodStopped + "' at line " + stoppedLine);
        if (location.methodID != methodID || stoppedLine != breakpointLine) {
            return failed("Expected to stop in a method '" + method + "' at line " + breakpointLine + " instead of method '" + methodStopped + "', line " + stoppedLine);
        }
        
//        HashMap classes = (HashMap)getAllClasses();
//        Set keys = classes.keySet();
//        for (int i = 0; i < keys.size(); i++) {
//            String message = "";
//            Long classID = (Long)keys.toArray()[i];
//            message += "ID: " + classID + ", class: " + classes.get(classID);
//            logWriter.print(message);
//        }

        logWriter.print("Set request to stop when 'ClassNotFoundException' is caught");
        reply = debuggeeWrapper.vmMirror.setException(EXCEPTION_CLASS_SIG, true, false);
        logWriter.print("Resume debuggee VM and check whether an exception was caught and breakpoint was triggered");
        debuggeeWrapper.vmMirror.resume();
        event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.EXCEPTION);
        events = ParsedEvent.parseEventPacket(event);
        threadID = ((ParsedEvent.Event_EXCEPTION)events[0]).getThreadID();
//        Location throwLocation = ((ParsedEvent.Event_EXCEPTION)events[0]).getLocation();
//        Location catchLocation = ((ParsedEvent.Event_EXCEPTION)events[0]).getCatchLocation();
        TaggedObject exception = ((ParsedEvent.Event_EXCEPTION)events[0]).getException();
        String exceptionSignature = debuggeeWrapper.vmMirror.getClassSignature(debuggeeWrapper.vmMirror.getReferenceType(exception.objectID));        
        logWriter.print("An exception '" + exceptionSignature.substring(1, exceptionSignature.length() - 1).replace('/', '.') + "' has been caught.");
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.EXCEPTION, reply.getNextValueAsInt());
        
//        String threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
//        String throwClass = debuggeeWrapper.vmMirror.getClassSignature(throwLocation.classID);
//        throwClass = throwClass.substring(1, throwClass.length() - 1).replace('/', '.');
//        String throwMethod = debuggeeWrapper.vmMirror.getMethodName(throwLocation.classID, throwLocation.methodID);
//        int throwLine = debuggeeWrapper.vmMirror.getLineNumber(throwLocation.classID, throwLocation.methodID, throwLocation.index);
//        
//        String catchClass = debuggeeWrapper.vmMirror.getClassSignature(catchLocation.classID);
//        catchClass = catchClass.substring(1, catchClass.length() - 1).replace('/', '.');
//        String catchMethod = debuggeeWrapper.vmMirror.getMethodName(catchLocation.classID, catchLocation.methodID);
//        int catchLine = debuggeeWrapper.vmMirror.getLineNumber(catchLocation.classID, catchLocation.methodID, catchLocation.index);
        if (!exceptionSignature.equals(EXCEPTION_CLASS_SIG)) {
            debuggeeWrapper.vmMirror.resume();
            return failed("Expected to stop when an '" + EXCEPTION_CLASS_SIG + "' exception has been caught.");
        }
        

        logWriter.print("Resume debuggee VM");
        debuggeeWrapper.vmMirror.resume();        
        
        logWriter.print("OK");
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new EB03Test().test(args));
    }
}
