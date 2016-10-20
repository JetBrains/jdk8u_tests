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
 * Created on 14.10.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT004;

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
 * requests are sent with suspendPolicy = NONE and with 'ThreadOnly'
 * modifier, which cause very big number of asynchronous events in many
 * threads in debuggee. 
 * The tests expects that at once all expected 'METHOD_ENTRY' events
 * and all expected 'METHOD_EXIT' events are received.
 */
public class EventTest004 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT004.EventDebuggee004";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    public final static String THREAD_CLASS_SIGNATURE = 
        "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/EVENT004/EventDebuggee004_Thread;";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case exercises the JDWP agent when very big number of event
     * requests are sent with suspendPolicy = NONE and with 'ThreadOnly'
     * modifier, which cause very big number of asynchronous events in many
     * threads in debuggee. 
     * The tests expects that at once all expected 'METHOD_ENTRY' events
     * and all expected 'METHOD_EXIT' events are received.
     */
    public Result  testEvent004() {

        logWriter.println("==> testEvent004: START (" + new Date() + ")...");
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
        ("==> Prepare and send requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events...");

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
        int[] methodExitRequestIDs = new int[startedThreadsNumber];
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsNames[i] = EventDebuggee004.THREAD_NAME_PATTERN + i;
            startedThreadsIDs[i] = NO_THREAD_ID;
            methodEntryRequestIDs[i] = NO_REQUEST_ID;
            methodExitRequestIDs[i] = NO_REQUEST_ID;
        }
        int methodEntryRequestsNumber = 0;
        int methodExitRequestsNumber = 0;
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
                    JDWPConstants.SuspendPolicy.NONE, threadID, threadRefTypeID, 0);
            if ( setEventReply == null ) {
                testCaseStatus = FAILURE;
                logWriter.println("## FAILURE: Can NOT set event request:");
                logWriter.println("##          Event kind = " + JDWPConstants.EventKind.METHOD_ENTRY +
                        "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.METHOD_ENTRY));
                logWriter.println("##          Thread name = " + threadName);
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

            setEventReply = setEventRequest(JDWPConstants.EventKind.METHOD_EXIT,
                    JDWPConstants.SuspendPolicy.NONE, threadID, threadRefTypeID, 0);
            if ( setEventReply == null ) {
                testCaseStatus = FAILURE;
                logWriter.println("## FAILURE: Can NOT set event request:");
                logWriter.println("##          Event kind = " + JDWPConstants.EventKind.METHOD_EXIT +
                        "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.METHOD_EXIT));
                logWriter.println("##          Thread name = " + threadName);
                break;   
            } else {
                if ( checkReplyForError(setEventReply, expectedErrors,
                    "EventRequest.set command for METHOD_EXIT", LIMITED_PRINT) ) {
                    testCaseStatus = FAILURE;
                    break;
                }
                if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                        "EventRequest.set command for METHOD_EXIT") ) {
                    outOfMemory = true;
                    break;
                }
                methodExitRequestIDs[threadIndex] = setEventReply.getNextValueAsInt();
                methodExitRequestsNumber++;
            }
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time (mlsecs) of preparing requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events " +
        "for each thread started by debuggee = " + measurableCodeRunningTimeMlsec);
        logWriter.println("==> Successful requests for 'METHOD_ENTRY' = " + methodEntryRequestsNumber);
        if ( methodEntryRequestsNumber != startedThreadsNumber ) {
            logWriter.println(">>> WARNING: Expected requests for 'METHOD_ENTRY' = " + startedThreadsNumber);
        }
        logWriter.println("==> Successful requests for 'METHOD_EXIT' = " + methodExitRequestsNumber);
        if ( methodExitRequestsNumber != startedThreadsNumber ) {
            logWriter.println(">>> WARNING: Expected requests for 'METHOD_EXIT' = " + startedThreadsNumber);
        }
        if ( outOfMemory ) {
            if ( testCaseStatus == FAILURE ) {
                terminateDebuggee(FAILURE, "MARKER_09");
                return failed
                ("## FAILURE while sending requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events");
            } else {
                terminateDebuggee(SUCCESS, "MARKER_10");
                return passed
                ("==> OutOfMemory while sending requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events - Expected result!");
            }
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE: Can NOT set all requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events!");
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed
            ("## FAILURE while sending requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events");
        }

        logWriter.println("\n");
        logWriter.println
        ("==> Resume debuggee to allow all threads to enter in test method and cause events...");
        resumeDebuggee("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for all debuggee's threads exit from test method...");

        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_10");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time (mlsecs) of waiting for all debuggee's threads exit from test method = " + 
                measurableCodeRunningTimeMlsec);

        logWriter.println("\n");
        logWriter.println
        ("==> Check that all expected 'METHOD_ENTRY' and 'METHOD_EXIT' events are generated...");
        int receivingEventsStatus = SUCCESS;
        int receivedMethodEntryEvents = 0;
        int receivedMethodExitEvents = 0;
        String expectedMethodName = "testMethod";
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        limitedPrintlnInit(20);
        boolean toBreak = false;
        for (int i=0; i < startedThreadsNumber; i++) {
            while ( methodEntryRequestIDs[i] != NO_REQUEST_ID ) {
                ParsedEvent event = receiveEvent(NO_REQUEST_ID, JDWPConstants.EventKind.METHOD_ENTRY, 
                        startedThreadsIDs[i], currentTimeout());
                if ( event == null ) {
                    receivingEventsStatus = FAILURE;
                    logWriter.println
                    ("## FAILURE: Can NOT receive expected 'METHOD_ENTRY' event for thread name = " +
                            startedThreadsNames[i]);
                    logWriter.println("##         Event index = " + i);
                    toBreak = true;   
                    break;
                }
                int requestID = event.getRequestID();
                if ( requestID != methodEntryRequestIDs[i] ) {
                    logWriter.println
                    ("## FAILURE: Unexpected requestID for received event for thread name = " +
                            startedThreadsNames[i]);
                    logWriter.println("##          Received requestID = " + requestID);
                    logWriter.println("##          Expected requestID = " + methodEntryRequestIDs[i]);
                    receivingEventsStatus = FAILURE;
                    break;
                }
                byte eventKind = event.getEventKind();
                if ( eventKind != JDWPConstants.EventKind.METHOD_ENTRY ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received event kind = " + eventKind +
                                "(" + JDWPConstants.EventKind.getName(eventKind));
                        logWriter.println("##          Expected event kind = " + JDWPConstants.EventKind.METHOD_ENTRY +
                                "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.METHOD_ENTRY));
                    }
                    receivingEventsStatus = FAILURE;
                    break;
                }
                Location location = ((Event_METHOD_ENTRY)event).getLocation();
                long methodID = location.methodID;
                String methodName = debuggeeWrapper.vmMirror.getMethodName(threadRefTypeID, methodID);
                if ( ! expectedMethodName.equals(methodName) ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected method name for 'METHOD_ENTRY' event");
                    if ( toPrn ) {
                        logWriter.println("##          Received method name = " + methodName);
                        logWriter.println("##          Expected method name = " + expectedMethodName);
                    }
                    receivingEventsStatus = FAILURE;
                    break;
                }
                receivedMethodEntryEvents++;
                break;
            }
            if ( toBreak ) {
                break;   
            }
            if ( methodExitRequestIDs[i] != NO_REQUEST_ID ) {
                ParsedEvent event = receiveEvent(NO_REQUEST_ID, JDWPConstants.EventKind.METHOD_EXIT, 
                        startedThreadsIDs[i], currentTimeout());
                if ( event == null ) {
                    receivingEventsStatus = FAILURE;
                    logWriter.println
                    ("## FAILURE: Can NOT receive expected 'METHOD_EXIT' event for thread name = " +
                            startedThreadsNames[i]);
                    logWriter.println("##         Event index = " + i);
                    break;   
                }
                int requestID = event.getRequestID();
                if ( requestID != methodExitRequestIDs[i] ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected requestID for received event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received requestID = " + requestID);
                        logWriter.println("##          Expected requestID = " + methodExitRequestIDs[i]);
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                byte eventKind = event.getEventKind();
                if ( eventKind != JDWPConstants.EventKind.METHOD_EXIT ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received event kind = " + eventKind +
                                "(" + JDWPConstants.EventKind.getName(eventKind));
                        logWriter.println(
                                "##          Expected event kind = " + JDWPConstants.EventKind.METHOD_EXIT +
                                "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.METHOD_EXIT));
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                Location location = ((Event_METHOD_EXIT)event).getLocation();
                long methodID = location.methodID;
                String methodName = debuggeeWrapper.vmMirror.getMethodName(threadRefTypeID, methodID);
                if ( ! expectedMethodName.equals(methodName) ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected method name for 'METHOD_EXIT' event");
                    if ( toPrn ) {
                        logWriter.println("##          Received method name = " + methodName);
                        logWriter.println("##          Expected method name = " + expectedMethodName);
                    }
                    receivingEventsStatus = FAILURE;
                    continue;
                }
                receivedMethodExitEvents++;
            }
        }
        logWriter.println
        ("==> Number of received 'METHOD_ENTRY' events = " + receivedMethodEntryEvents );
        logWriter.println
        ("==> Number of received 'METHOD_EXIT' events = " + receivedMethodExitEvents );
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of receiving all expected 'METHOD_ENTRY' and 'METHOD_EXIT' events  = " + 
                measurableCodeRunningTimeMlsec);
        if ( receivingEventsStatus == FAILURE ) {
            logWriter.println
            ("## FAILURE while receiving 'METHOD_ENTRY' and 'METHOD_EXIT' events!");
            terminateDebuggee(FAILURE, "MARKER_10.0.1");
            return failed("## FAILURE while receiving 'METHOD_ENTRY' and 'METHOD_EXIT' events! ");
        } else {
            logWriter.println
            ("==> OK - all expected 'METHOD_ENTRY' and 'METHOD_EXIT' events are received!");
        }
        
        int clearingRequestsStatus = SUCCESS;
        logWriter.println("\n");
        logWriter.println
        ("==> Clear requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events...");
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
            if ( methodExitRequestIDs[i] != NO_REQUEST_ID ) {
                reply = clearEventRequest
                (JDWPConstants.EventKind.METHOD_EXIT, methodExitRequestIDs[i]);
                if ( checkReplyForError
                        (reply, JDWPConstants.Error.NONE,
                        "EventRequest.Clear command", LIMITED_PRINT) ) {
                    limitedPrintln
                    ("## FAILURE: Can NOT clear event request for 'METHOD_EXIT' event" +
                            " for thread name = " + startedThreadsNames[i]);
                    clearingRequestsStatus = FAILURE;
                }
            }
        }
        if ( clearingRequestsStatus == FAILURE ) {
            testCaseStatus = FAILURE;
        } else {
            logWriter.println
            ("==> OK -  requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events are cleared!");
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of clearing  all requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events = " + 
                measurableCodeRunningTimeMlsec);

        logWriter.println("\n");
        logWriter.println("==> Resume debuggee to allow all threads to finish...");
        resumeDebuggee("#3");
        
        logWriter.println("\n");
        logWriter.println("==> Wait for all threads finish in debuggee...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_03' Thread Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait();
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Thread Signal from debuggee");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for all threads finish in debuggee = " + measurableCodeRunningTimeMlsec);
        
        logWriter.println("\n");
        logWriter.println("==> Resume debuggee to allow it to finish...");
        resumeDebuggee("#4");

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
        logWriter.println("==> testEvent004: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testEvent004: FAILED");
            return failed("testEvent004:");
        } else {
            logWriter.println("==> testEvent004: OK");
            return passed("testEvent004: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testEvent004: FAILED");
    return failed("==> testEvent004: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new EventTest004().test(args));
	}
}
