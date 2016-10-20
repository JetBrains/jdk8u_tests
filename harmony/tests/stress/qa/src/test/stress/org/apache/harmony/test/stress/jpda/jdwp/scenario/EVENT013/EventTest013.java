/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/**
 * @author Anatoly F. Bondarenko
 * @version $Revision: 1.2 $
 */

/**
 * Created on 20.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT013;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ParsedEvent;
import org.apache.harmony.share.framework.jpda.jdwp.ParsedEvent.*;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Location;


/**
 * This test case exercises the JDWP agent when very big number of event
 * requests for 'METHOD_ENTRY' are sent with 
 * suspendPolicy = EVENT_THREAD and with 'ThreadOnly' modifier, 
 * which cause very big number of asynchronous events in many
 * threads in debuggee.
 * After all 'METHOD_ENTRY' events are received the test sets requests for 
 * 'BREAKPOINT' and 'SINGLE_STEP' events for each thread.
 * After all 'BREAKPOINT' and 'SINGLE_STEP' events are received all threads
 * are continued and repeated 'SINGLE_STEP' event should be for each thread.
 * The tests expects that all expected 'METHOD_ENTRY', 'BREAKPOINT' and 
 * 'SINGLE_STEP' events are received.
 */
public class EventTest013 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT013.EventDebuggee013";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    public final static String THREAD_CLASS_SIGNATURE = 
        "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/EVENT013/EventDebuggee013_Thread;";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case exercises the JDWP agent when very big number of event
     * requests for 'METHOD_ENTRY' are sent with 
     * suspendPolicy = EVENT_THREAD and with 'ThreadOnly' modifier, 
     * which cause very big number of asynchronous events in many
     * threads in debuggee.
     * After all 'METHOD_ENTRY' events are received the test sets requests for 
     * 'BREAKPOINT' and 'SINGLE_STEP' events for each thread.
     * After all 'BREAKPOINT' and 'SINGLE_STEP' events are received all threads
     * are continued and repeated 'SINGLE_STEP' event should be for each thread.
     * The tests expects that all expected 'METHOD_ENTRY', 'BREAKPOINT' and 
     * 'SINGLE_STEP' events are received.
     */
    public Result  testEvent013() {

        logWriter.println("==> testEvent013: START (" + new Date() + ")...");
        if ( waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE ) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }

        if ( setupSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }

        if ( setupThreadSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed("## FAILURE while setupThreadSignalWithWait.");
        }

        resumeDebuggee("#1");
try {
        logWriter.println("==> Wait for debuggee to start big number of treads ('SIGNAL_READY_01')...");

        String debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        logWriter.println("\n");
        logWriter.println
        ("==> Send VirtualMachine.AllThreads after big number of threads started in debuggee...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllThreadsCommand);
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Command running time(mlsecs) = " + measurableCodeRunningTimeMlsec);

        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        if ( checkReplyForError(reply, expectedErrors,
                "VirtualMachine.AllThreads command") ) {
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed
            ("## FAILURE while running VirtualMachine.AllThreads command");
        }
        if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                "VirtualMachine.AllThreads command") ) {
            terminateDebuggee(SUCCESS, "MARKER_05");
            return passed
            ("==> OutOfMemory while running VirtualMachine.AllThreads command - Expected result!");
        }

        logWriter.println("\n");
        logWriter.println
        ("==> Prepare and send requests for 'METHOD_ENTRY' " +
                "for each thread started by debuggee...");

        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        logWriter.println("==> Get debuggeeRefTypeID...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## Can NOT get debuggeeRefTypeID!");
        }
        logWriter.println("==> debuggeeRefTypeID = " + debuggeeRefTypeID);

        logWriter.println("==> Get threadRefTypeID...");
        long threadRefTypeID = debuggeeWrapper.vmMirror.getClassID(THREAD_CLASS_SIGNATURE);
        if ( threadRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get threadRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_07");
           return failed("## Can NOT get threadRefTypeID!");
        }
        logWriter.println("==> threadRefTypeID = " + threadRefTypeID);

        logWriter.println("==> Get startedThreadsNumber value from debuggee...");
        int startedThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "startedThreadsNumber");
        if ( startedThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_08");
            return failed("## Can NOT get startedThreadsNumber");
        }
        logWriter.println("==> startedThreadsNumber = " + startedThreadsNumber);
        
        String[] startedThreadsNames = new String[startedThreadsNumber];
        long[] startedThreadsIDs = new long[startedThreadsNumber];
        int[] methodEntryRequestIDs = new int[startedThreadsNumber];
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsNames[i] = EventDebuggee013.THREAD_NAME_PATTERN + i;
            startedThreadsIDs[i] = NO_THREAD_ID;
            methodEntryRequestIDs[i] = NO_REQUEST_ID;
        }
        int methodEntryRequestsNumber = 0;
        int allThreads = reply.getNextValueAsInt();
        int testCaseStatus = SUCCESS;
        boolean outOfMemory = false;
        limitedPrintlnInit(20);
        for (int i=0; i < allThreads; i++) {
            long threadID = reply.getNextValueAsThreadID();
            String threadName = null;
            try {
                threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
            } catch (Throwable thrown ) {
                logWriter.println("## FAILURE: Exception in vmMirror.getThreadName():");
                logWriter.println("##          Thread index = " + i);
                logWriter.println("##          ThreadID = " + threadID);
                printStackTraceToLogWriter(thrown);
                testCaseStatus = FAILURE;
                break;
            }
            int threadIndex = 0;
            for (; threadIndex < startedThreadsNumber; threadIndex++) {
                if ( startedThreadsNames[threadIndex].equals(threadName) ) {
                    startedThreadsIDs[threadIndex] = threadID;
                    break;
                }
            }
            if ( threadIndex == startedThreadsNumber ) {
                continue;   
            }
            ReplyPacket setEventReply = null;

            setEventReply = setEventRequest(JDWPConstants.EventKind.METHOD_ENTRY,
                    JDWPConstants.SuspendPolicy.EVENT_THREAD, threadID, threadRefTypeID, 0);
            if ( setEventReply == null ) {
                logWriter.println("## FAILURE: Can NOT set event request:");
                logWriter.println("##          Event kind = " + JDWPConstants.EventKind.METHOD_ENTRY +
                        "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.METHOD_ENTRY));
                logWriter.println("##          Thread name = " + threadName);
                testCaseStatus = FAILURE;
                break;
            } else {
                if ( checkReplyForError(setEventReply, expectedErrors,
                    "EventRequest.set command for METHOD_ENTRY", LIMITED_PRINT) ) {
                    testCaseStatus = FAILURE;
                    break;
                }
                if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                        "EventRequest.set command for METHOD_ENTRY") ) {
                    outOfMemory = true;
                    break;
                }
                methodEntryRequestIDs[threadIndex] = setEventReply.getNextValueAsInt();
                methodEntryRequestsNumber++;
            }

        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time (mlsecs) of preparing requests for 'METHOD_ENTRY' events " +
        "for each thread started by debuggee = " + measurableCodeRunningTimeMlsec);
        logWriter.println("==> Successful requests for 'METHOD_ENTRY' = " + methodEntryRequestsNumber);
        if ( methodEntryRequestsNumber != startedThreadsNumber ) {
            logWriter.println(">>> WARNING: Expected requests for 'METHOD_ENTRY' = " + startedThreadsNumber);
        }
        if ( outOfMemory ) {
            if ( testCaseStatus == FAILURE ) {
                terminateDebuggee(FAILURE, "MARKER_09");
                return failed
                ("## FAILURE while sending requests for 'METHOD_ENTRY' events");
            } else {
                terminateDebuggee(SUCCESS, "MARKER_10");
                return passed
                ("==> OutOfMemory while sending requests for 'METHOD_ENTRY' events - Expected result!");
            }
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println
            ("## FAILURE: Can NOT set all requests for 'METHOD_ENTRY'events!");
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed
            ("## FAILURE while sending requests for 'METHOD_ENTRY' events");
        }
        
        logWriter.println
        ("==> Resume debuggee to allow all threads to enter in test method and cause " +
                "'METHOD_ENTRY' events...");
        resumeDebuggee("#2");

        logWriter.println("\n");
        logWriter.println
        ("==> Check that all expected 'METHOD_ENTRY' events are generated...");
        String testMethodName = "testMethod";
        printMethodLineTable(threadRefTypeID, null, testMethodName);
        long methodStartCodeIndex = getMethodStartCodeIndex(threadRefTypeID, testMethodName);
        long methodEndCodeIndex = getMethodEndCodeIndex(threadRefTypeID, testMethodName);
        if ( (methodStartCodeIndex == BAD_LONG_VALUE) || (methodEndCodeIndex == BAD_LONG_VALUE) ) {
            logWriter.println
            ("## FAILURE: Can NOT get start or enf code index for 'testMethod'!");
            terminateDebuggee(FAILURE, "MARKER_11_009");
            return failed
            ("## FAILURE: Can NOT get start or enf code index for 'testMethod'! - ");
        }
        int receivingEventsStatus = SUCCESS;
        int receivedMethodEntryEvents = 0;
        boolean[] isMethodEntryReceivedSuccessfully = new boolean[startedThreadsNumber];
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        limitedPrintlnInit(20);
        for (int i=0; i < startedThreadsNumber; i++) {
            isMethodEntryReceivedSuccessfully[i] = false;
            if ( methodEntryRequestIDs[i] != NO_REQUEST_ID ) {
                ParsedEvent event = receiveEvent(NO_REQUEST_ID, JDWPConstants.EventKind.METHOD_ENTRY, 
                        startedThreadsIDs[i], currentTimeout());
                if ( event == null ) {
                    receivingEventsStatus = FAILURE;
                    logWriter.println
                    ("##FAILURE: Can NOT receive expected 'METHOD_ENTRY' event for thread name = " +
                            startedThreadsNames[i]);
                    logWriter.println("##         Event index = " + i);
                    break;
                }
                receivedMethodEntryEvents++;
                int requestID = event.getRequestID();
                if ( requestID != methodEntryRequestIDs[i] ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected requestID for received event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received requestID = " + requestID);
                        logWriter.println("##          Expected requestID = " + methodEntryRequestIDs[i]);
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                Location location = ((Event_METHOD_ENTRY)event).getLocation();
                long methodID = location.methodID;
                String methodName = debuggeeWrapper.vmMirror.getMethodName(threadRefTypeID, methodID);
                if ( ! testMethodName.equals(methodName) ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected method name for 'METHOD_ENTRY' event");
                    if ( toPrn ) {
                        logWriter.println("##          Received method name = " + methodName);
                        logWriter.println("##          Expected method name = " + testMethodName);
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                long codeIndex = location.index; 
                if ( (codeIndex < methodStartCodeIndex) || (codeIndex > methodEndCodeIndex) ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected location's code index for 'METHOD_ENTRY' event");
                    if ( toPrn ) {
                        logWriter.println("##          Thread name = " + startedThreadsNames[i]);
                        logWriter.println("##          Method name = " + methodName);
                        logWriter.println("##          Received code index = " + codeIndex);
                        logWriter.println("##          Expected scope for code index = (" 
                                + methodStartCodeIndex + " , " + methodEndCodeIndex + ")");
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                isMethodEntryReceivedSuccessfully[i] = true;
            }
        }
        logWriter.println
        ("==> Number of received 'METHOD_ENTRY' events = " + receivedMethodEntryEvents );
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of receiving all expected 'METHOD_ENTRY' events  = " + 
                measurableCodeRunningTimeMlsec);
        if ( receivingEventsStatus == FAILURE ) {
            logWriter.println
            ("## FAILURE while receiving 'METHOD_ENTRY' events!");
            terminateDebuggee(FAILURE, "MARKER_11.1");
            return failed("## FAILURE while receiving 'METHOD_ENTRY' events! ");
        } else {
            logWriter.println
            ("==> OK - all expected 'METHOD_ENTRY' events are received!");
        }

        int clearingRequestsStatus = SUCCESS;
        logWriter.println("\n");
        logWriter.println
        ("==> Clear all requests for 'METHOD_ENTRY' " +
                "for each thread started by debuggee...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        limitedPrintlnInit(20);
        for (int i=0; i < startedThreadsNumber; i++) {
            if ( methodEntryRequestIDs[i] != NO_REQUEST_ID ) {
                reply = clearEventRequest
                (JDWPConstants.EventKind.METHOD_ENTRY, methodEntryRequestIDs[i]);
                if ( checkReplyForError
                        (reply, JDWPConstants.Error.NONE,
                        "EventRequest.Clear command", LIMITED_PRINT) ) {
                    limitedPrintln
                    ("## FAILURE: Can NOT clear event request for 'METHOD_ENTRY' event" +
                            " for thread name = " + startedThreadsNames[i]);
                    clearingRequestsStatus = FAILURE;
                }
            }
        }
        if ( clearingRequestsStatus == FAILURE ) {
            logWriter.println("## FAILURE while clear event request for 'METHOD_ENTRY' event");
            terminateDebuggee(FAILURE, "MARKER_11.02");
            return failed("## FAILURE while clear event request for 'METHOD_ENTRY' event! ");
        } else {
            logWriter.println
            ("==> OK -  all requests for 'METHOD_ENTRY' events are cleared!");
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of clearing  all requests for 'METHOD_ENTRY' events = " + 
                measurableCodeRunningTimeMlsec);

        logWriter.println("\n");
        logWriter.println
        ("==> Prepare and send requests for 'BREAKPOINT' and 'SINGLE_STEP' events ...");

        logWriter.println("==> Get breakpoint location...");
        Location breakpointLocation = 
            getMethodLocationForLine(threadRefTypeID, testMethodName, 2 /* line number in method*/);
        if ( breakpointLocation == null ) {
            logWriter.println("## FAILURE: Can NOT get breakpoint location");
            terminateDebuggee(FAILURE, "MARKER_12");
            return failed("## Can NOT get breakpoint location!");
        }
        logWriter.println("==> OK - breakpoint location is got");
        
        int[] breakpointRequestIDs = new int[startedThreadsNumber];
        int[] singleStepRequestIDs = new int[startedThreadsNumber];
        for (int i=0; i < startedThreadsNumber; i++) {
            breakpointRequestIDs[i] = NO_REQUEST_ID;
            singleStepRequestIDs[i] = NO_REQUEST_ID;
        }
        int breakpointRequestsNumber = 0;
        int singleStepRequestsNumber = 0;
        int setEventRequestsStatus = SUCCESS;
        outOfMemory = false;
        limitedPrintlnInit(20);
        for (int i=0; i < startedThreadsNumber; i++) {
            if ( ! isMethodEntryReceivedSuccessfully[i] ) {
                continue;   
            }
            ReplyPacket setEventReply = null;

            setEventReply = setBreakpointRequest(JDWPConstants.SuspendPolicy.EVENT_THREAD, 
                    startedThreadsIDs[i], breakpointLocation, 0);
            if ( setEventReply == null ) {
                logWriter.println("## FAILURE: Can NOT set breakpoint request:");
                logWriter.println("##          Thread name = " + startedThreadsNames[i]);
                setEventRequestsStatus = FAILURE;
                break;
            } else {
                if ( checkReplyForError(setEventReply, expectedErrors,
                    "EventRequest.set command for BREAKPOINT", LIMITED_PRINT) ) {
                    setEventRequestsStatus = FAILURE;
                    break;
                }
                if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                        "EventRequest.set command for BREAKPOINT") ) {
                    outOfMemory = true;
                    break;
                }
                breakpointRequestIDs[i] = setEventReply.getNextValueAsInt();
                breakpointRequestsNumber++;
            }

            setEventReply = setSingleStepRequest(JDWPConstants.SuspendPolicy.EVENT_THREAD,
                startedThreadsIDs[i], JDWPConstants.StepSize.LINE, JDWPConstants.StepDepth.OVER, 0);
            if ( setEventReply == null ) {
                logWriter.println("## FAILURE: Can NOT set single step request:");
                logWriter.println("##          Thread name = " + startedThreadsNames[i]);
                setEventRequestsStatus = FAILURE;
                break;
            } else {
                if ( checkReplyForError(setEventReply, expectedErrors,
                    "EventRequest.set command for SINGLE_STEP", LIMITED_PRINT) ) {
                    setEventRequestsStatus = FAILURE;
                    break;
                }
                if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                        "EventRequest.set command for SINGLE_STEP") ) {
                    outOfMemory = true;
                    break;
                }
                singleStepRequestIDs[i] = setEventReply.getNextValueAsInt();
                singleStepRequestsNumber++;
            }
            
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time (mlsecs) of preparing requests for 'BREAKPOINT' and 'SINGLE_STEP' events " +
        "for each thread started by debuggee = " + measurableCodeRunningTimeMlsec);
        if ( setEventRequestsStatus == FAILURE ) {
            testCaseStatus = FAILURE;
        }
        logWriter.println("==> Successful requests for 'BREAKPOINT' = " + breakpointRequestsNumber);
        if ( breakpointRequestsNumber != startedThreadsNumber ) {
            logWriter.println(">>> WARNING: Expected requests for 'BREAKPOINT' = " + 
                    receivedMethodEntryEvents);
        }
        logWriter.println("==> Successful requests for 'SINGLE_STEP' = " + singleStepRequestsNumber);
        if ( singleStepRequestsNumber != startedThreadsNumber ) {
            logWriter.println(">>> WARNING: Expected requests for 'SINGLE_STEP' = " + 
                    receivedMethodEntryEvents);
        }
        if ( outOfMemory ) {
            if ( testCaseStatus == FAILURE ) {
                terminateDebuggee(FAILURE, "MARKER_13");
                return failed
                ("## FAILURE while sending requests for 'BREAKPOINT' and 'SINGLE_STEP' events");
            } else {
                terminateDebuggee(SUCCESS, "MARKER_14");
                return passed
                ("==> OutOfMemory while sending requests for 'BREAKPOINT' and 'SINGLE_STEP' events - Expected result!");
            }
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println
            ("## FAILURE: Can NOT set all requests for ''BREAKPOINT' and 'SINGLE_STEP' events!");
            terminateDebuggee(FAILURE, "MARKER_15");
            return failed
            ("## FAILURE while sending requests for 'BREAKPOINT' and 'SINGLE_STEP' events");
        }
        
        logWriter.println
        ("==> Resume debuggee to allow all threads to continue test method execution and cause " +
                "'BREAKPOINT' and 'SINGLE_STEP' events...");
        resumeDebuggee("#3");

        logWriter.println("\n");
        logWriter.println
        ("==> Check that all expected 'BREAKPOINT' and 'SINGLE_STEP' events are generated...");
        receivingEventsStatus = SUCCESS;
        int receivedBreakpointsEvents = 0;
        int receivedSingleStepEvents = 0;
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        boolean toBreak = false;
        for (int i=0; i < startedThreadsNumber; i++) {
            while ( breakpointRequestIDs[i] != NO_REQUEST_ID ) {
                ParsedEvent event = receiveEvent(NO_REQUEST_ID, JDWPConstants.EventKind.BREAKPOINT, 
                        startedThreadsIDs[i], currentTimeout());
                if ( event == null ) {
                    receivingEventsStatus = FAILURE;
                    logWriter.println
                    ("##FAILURE: Can NOT receive expected 'BREAKPOINT' event for thread name = " +
                            startedThreadsNames[i]);
                    logWriter.println("##         Event index = " + i);
                    toBreak = true;   
                    break;
                }
                receivedBreakpointsEvents++;
                int requestID = event.getRequestID();
                if ( requestID != breakpointRequestIDs[i] ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected requestID for received 'BREAKPOINT' event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received requestID = " + requestID);
                        logWriter.println("##          Expected requestID = " + breakpointRequestIDs[i]);
                    }
                    receivingEventsStatus = FAILURE;
                    break;
                }
                Location location = ((Event_BREAKPOINT)event).getLocation();
                boolean isLocationOK = true;
                if ( breakpointLocation.tag != location.tag ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected tag in 'BREAKPOINT' location:"); 
                    if ( toPrn ) {
                        logWriter.println("##          Received location tag = " + 
                                location.tag + "(" + JDWPConstants.TypeTag.getName(location.tag));
                        logWriter.println("##          Expected location tag = " + 
                                breakpointLocation.tag + "(" + 
                                JDWPConstants.TypeTag.getName(breakpointLocation.tag));
                    }
                    isLocationOK = false;
                }
                if ( breakpointLocation.classID != location.classID ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected classID in 'BREAKPOINT' location:"); 
                    if ( toPrn ) {
                        logWriter.println("##          Received location classID = " + 
                                location.classID);
                        logWriter.println("##          Expected location classID = " + 
                                breakpointLocation.classID);
                    }
                    isLocationOK = false;
                }
                if ( breakpointLocation.methodID != location.methodID ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected methodID in 'BREAKPOINT' location:"); 
                    if ( toPrn ) {
                        logWriter.println("##          Received location methodID = " + 
                                location.methodID);
                        logWriter.println("##          Expected location methodID = " + 
                                breakpointLocation.methodID);
                    }
                    isLocationOK = false;
                }
                if ( breakpointLocation.index != location.index ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected index in 'BREAKPOINT' location:"); 
                    if ( toPrn ) {
                        logWriter.println("##          Received location index = " + 
                                location.index);
                        logWriter.println("##          Expected location index = " + 
                                breakpointLocation.index);
                    }
                    isLocationOK = false;
                }
                if ( ! isLocationOK ) {
                    receivingEventsStatus = FAILURE;
                    break;
                }
                break;
            }
            if ( toBreak ) {
                break;   
            }
            if ( singleStepRequestIDs[i] != NO_REQUEST_ID ) {
                ParsedEvent event = receiveEvent(NO_REQUEST_ID, JDWPConstants.EventKind.SINGLE_STEP, 
                        startedThreadsIDs[i], currentTimeout());
                if ( event == null ) {
                    receivingEventsStatus = FAILURE;
                    logWriter.println
                    ("##FAILURE: Can NOT receive expected 'SINGLE_STEP' event for thread name = " +
                            startedThreadsNames[i]);
                    logWriter.println("##         Event index = " + i);
                    break;
                }
                receivedSingleStepEvents++;
                int requestID = event.getRequestID();
                if ( requestID != singleStepRequestIDs[i] ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected requestID for received 'SINGLE_STEP' event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received requestID = " + requestID);
                        logWriter.println("##          Expected requestID = " + singleStepRequestIDs[i]);
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                Location location = ((Event_SINGLE_STEP)event).getLocation();
                boolean isLocationOK = true;
                if ( breakpointLocation.tag != location.tag ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected tag in 'SINGLE_STEP' location:"); 
                    if ( toPrn ) {
                        logWriter.println("##          Received location tag = " + 
                                location.tag + "(" + JDWPConstants.TypeTag.getName(location.tag));
                        logWriter.println("##          Expected location tag = " + 
                                breakpointLocation.tag + "(" + 
                                JDWPConstants.TypeTag.getName(breakpointLocation.tag));
                    }
                    isLocationOK = false;
                }
                if ( breakpointLocation.classID != location.classID ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected classID in 'SINGLE_STEP' location:"); 
                    if ( toPrn ) {
                        logWriter.println("##          Received location classID = " + 
                                location.classID);
                        logWriter.println("##          Expected location classID = " + 
                                breakpointLocation.classID);
                    }
                    isLocationOK = false;
                }
                if ( breakpointLocation.methodID != location.methodID ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected methodID in 'SINGLE_STEP' location:"); 
                    if ( toPrn ) {
                        logWriter.println("##          Received location methodID = " + 
                                location.methodID);
                        logWriter.println("##          Expected location methodID = " + 
                                breakpointLocation.methodID);
                    }
                    isLocationOK = false;
                }
                if ( breakpointLocation.index != location.index ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected index in 'SINGLE_STEP' location:"); 
                    if ( toPrn ) {
                        logWriter.println("##          Received location index = " + 
                                location.index);
                        logWriter.println("##          Expected location index = " + 
                                breakpointLocation.index);
                    }
                    isLocationOK = false;
                }
                if ( ! isLocationOK ) {
                    receivingEventsStatus = FAILURE;
                    continue;
                }
            }
        }
        logWriter.println
        ("==> Number of received 'BREAKPOINT' events = " + receivedBreakpointsEvents );
        logWriter.println
        ("==> Number of received 'SINGLE_STEP' events = " + receivedSingleStepEvents );
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of receiving all expected 'BREAKPOINT' and 'SINGLE_STEP' events  = " + 
                measurableCodeRunningTimeMlsec);
        if ( receivingEventsStatus == FAILURE ) {
            logWriter.println
            ("## FAILURE while receiving 'BREAKPOINT' and 'SINGLE_STEP' events!");
            terminateDebuggee(FAILURE, "MARKER_15.1");
            return failed("## FAILURE while receiving 'BREAKPOINT' and 'SINGLE_STEP' events! ");
        } else {
            logWriter.println
            ("==> OK - all expected 'BREAKPOINT' and 'SINGLE_STEP' events are received!");
        }

        logWriter.println
        ("==> Resume debuggee to allow all threads to continue test method execution and cause " +
                "repeated 'SINGLE_STEP' events...");
        resumeDebuggee("#4");

        logWriter.println("\n");
        logWriter.println
        ("==> Check that all repeated 'SINGLE_STEP' events are generated...");
        long begExpectedCodeIndex = getMethodCodeIndexForLine(threadRefTypeID, testMethodName, 3);
        long endExpectedCodeIndex = getMethodCodeIndexForLine(threadRefTypeID, testMethodName, 4) - 1;
        if ( (begExpectedCodeIndex == BAD_LONG_VALUE) || (endExpectedCodeIndex == BAD_LONG_VALUE) ) {
            logWriter.println
            ("## FAILURE: Can NOT get line code index for 'testMethod'!");
            terminateDebuggee(FAILURE, "MARKER_15_1.1");
            return failed
            ("## FAILURE: Can NOT get line code index for 'testMethod'! - ");
        }
        receivingEventsStatus = SUCCESS;
        receivedSingleStepEvents = 0;
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        limitedPrintlnInit(20);
        for (int i=0; i < startedThreadsNumber; i++) {
            if ( singleStepRequestIDs[i] != NO_REQUEST_ID ) {
                ParsedEvent event = receiveEvent(NO_REQUEST_ID, NO_EVENT_KIND, 
                        startedThreadsIDs[i], currentTimeout());
                if ( event == null ) {
                    receivingEventsStatus = FAILURE;
                    logWriter.println
                    ("##FAILURE: Can NOT receive repeated 'SINGLE_STEP' event for thread name = " +
                            startedThreadsNames[i]);
                    logWriter.println("##         Event index = " + i);
                    break;
                }
                receivedSingleStepEvents++;
                byte eventKind = event.getEventKind();
                if ( eventKind != JDWPConstants.EventKind.SINGLE_STEP ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received event kind = " + eventKind +
                                "(" + JDWPConstants.EventKind.getName(eventKind));
                        logWriter.println
                        ("##          Expected event kind = " + JDWPConstants.EventKind.SINGLE_STEP +
                                "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.SINGLE_STEP));
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                int requestID = event.getRequestID();
                if ( requestID != singleStepRequestIDs[i] ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected requestID for repeated 'SINGLE_STEP' event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received requestID = " + requestID);
                        logWriter.println("##          Expected requestID = " + singleStepRequestIDs[i]);
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                Location location = ((Event_SINGLE_STEP)event).getLocation();
                long methodID = location.methodID;
                String methodName = debuggeeWrapper.vmMirror.getMethodName(threadRefTypeID, methodID);
                if ( ! testMethodName.equals(methodName) ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected method name for 'SINGLE_STEP' event");
                    if ( toPrn ) {
                        logWriter.println("##          Received method name = " + methodName);
                        logWriter.println("##          Expected method name = " + testMethodName);
                    }
                    receivingEventsStatus = FAILURE;
                }
                long codeIndex = location.index;
                if ( (codeIndex < begExpectedCodeIndex) || (codeIndex > endExpectedCodeIndex) ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected location's code index for 'SINGLE_STEP' event");
                    if ( toPrn ) {
                        logWriter.println("##          Thread name = " + startedThreadsNames[i]);
                        logWriter.println("##          Method name = " + methodName);
                        logWriter.println("##          Received code index = " + codeIndex);
                        logWriter.println("##          Expected scope for code index = (" 
                                + begExpectedCodeIndex + " , " + endExpectedCodeIndex + ")");
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
            }
        }
        logWriter.println
        ("==> Number of received repeated 'SINGLE_STEP' events = " + receivedSingleStepEvents );
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of receiving all repeated 'SINGLE_STEP' events  = " + 
                measurableCodeRunningTimeMlsec);
        if ( receivingEventsStatus == FAILURE ) {
            logWriter.println
            ("## FAILURE while receiving repeated 'SINGLE_STEP' events!");
            terminateDebuggee(FAILURE, "MARKER_15.2");
            return failed("## FAILURE while receiving repeated 'SINGLE_STEP' events! ");
        } else {
            logWriter.println
            ("==> OK - all repeated 'SINGLE_STEP' events are received!");
        }

        clearingRequestsStatus = SUCCESS;
        logWriter.println("\n");
        logWriter.println
        ("==> Clear all requests for 'BREAKPOINT' and 'SINGLE_STEP' events " +
                "for each thread started by debuggee...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        limitedPrintlnInit(20);
        for (int i=0; i < startedThreadsNumber; i++) {
            if ( breakpointRequestIDs[i] != NO_REQUEST_ID ) {
                reply = clearEventRequest
                (JDWPConstants.EventKind.BREAKPOINT, breakpointRequestIDs[i]);
                if ( checkReplyForError
                        (reply, JDWPConstants.Error.NONE,
                        "EventRequest.Clear command", LIMITED_PRINT) ) {
                    limitedPrintln
                    ("## FAILURE: Can NOT clear event request for 'BREAKPOINT' event" +
                            " for thread name = " + startedThreadsNames[i]);
                    clearingRequestsStatus = FAILURE;
                }
            }
            if ( singleStepRequestIDs[i] != NO_REQUEST_ID ) {
                reply = clearEventRequest
                (JDWPConstants.EventKind.SINGLE_STEP, singleStepRequestIDs[i]);
                if ( checkReplyForError
                        (reply, JDWPConstants.Error.NONE,
                        "EventRequest.Clear command", LIMITED_PRINT) ) {
                    limitedPrintln
                    ("## FAILURE: Can NOT clear event request for 'SINGLE_STEP' event" +
                            " for thread name = " + startedThreadsNames[i]);
                    clearingRequestsStatus = FAILURE;
                }
            }
        }
        if ( clearingRequestsStatus == FAILURE ) {
            testCaseStatus = FAILURE;
        } else {
            logWriter.println
            ("==> OK -  all requests for 'BREAKPOINT' and 'SINGLE_STEP' events are cleared!");
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of clearing  all requests for 'BREAKPOINT' and 'SINGLE_STEP' events = " + 
                measurableCodeRunningTimeMlsec);
        
        logWriter.println("\n");
        logWriter.println("==> Resume debuggee to continue all threads suspended by events...");
        resumeDebuggee("#5");
        
        logWriter.println("\n");
        logWriter.println("==> Wait for all threads finish in debuggee...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_02' Thread Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_16");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Thread Signal from debuggee");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for all threads finish in debuggee = " + measurableCodeRunningTimeMlsec);
        
        logWriter.println("\n");
        logWriter.println("==> Resume debuggee to allow it to finish...");
        resumeDebuggee("#6");

        logWriter.println("\n");
        logWriter.println("==> Wait for VM_DEATH event ... ");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        ParsedEvent event = receiveEvent(
                NO_REQUEST_ID, NO_EVENT_KIND, ANY_THREAD, currentTimeout());
        byte eventKind = event.getEventKind();
        if ( eventKind != JDWPConstants.EventKind.VM_DEATH ) {
            logWriter.println("## FAILURE: Unexpected event is received:");
            logWriter.println("##          Received event kind = " + eventKind +
                    "(" + JDWPConstants.EventKind.getName(eventKind));
            logWriter.println
            ("##          Expected event kind = " + JDWPConstants.EventKind.VM_DEATH +
                    "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.VM_DEATH));
            testCaseStatus = FAILURE;
        } else {
            logWriter.println("==> OK - VM_DEATH event is received!");
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for VM_DEATH event = " + measurableCodeRunningTimeMlsec);

        logWriter.println("\n");
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testEvent013: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testEvent013: FAILED");
            return failed("testEvent013:");
        } else {
            logWriter.println("==> testEvent013: OK");
            return passed("testEvent013: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testEvent013: FAILED");
    return failed("==> testEvent013: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new EventTest013().test(args));
	}
}
