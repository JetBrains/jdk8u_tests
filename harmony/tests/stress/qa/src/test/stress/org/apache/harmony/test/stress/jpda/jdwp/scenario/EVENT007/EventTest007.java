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

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT007;

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
 * This test case exercises the JDWP agent when only two event
 * requests are sent with suspendPolicy = NONE, which cause
 * very big number of asynchronous events in many threads in debuggee. 
 * Before each thread enters in test method to cause expected events
 * the debuggee creates memory stress until OutOfMemory happens.
 * The tests expects that at once all expected 'METHOD_ENTRY' events
 * and all expected 'METHOD_EXIT' events are received.
 */
public class EventTest007 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT007.EventDebuggee007";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";
    public final static String THREAD_CLASS_SIGNATURE = 
        "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/EVENT007/EventDebuggee007_Thread;";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case exercises the JDWP agent when only two event
     * requests are sent with suspendPolicy = NONE, which cause
     * very big number of asynchronous events in many threads in debuggee. 
     * Before each thread enters in test method to cause expected events
     * the debuggee creates memory stress until OutOfMemory happens.
     * The tests expects that at once all expected 'METHOD_ENTRY' events
     * and all expected 'METHOD_EXIT' events are received.
     */
    public Result  testEvent007() {

        logWriter.println("==> testEvent007: START (" + new Date() + ")...");
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
        int methodEntryRequestID = NO_REQUEST_ID;
        int methodExitRequestID = NO_REQUEST_ID;
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsNames[i] = EventDebuggee007.THREAD_NAME_PATTERN + i;
            startedThreadsIDs[i] = NO_THREAD_ID;
        }
        int allThreads = reply.getNextValueAsInt();
        int testCaseStatus = SUCCESS;
        for (int i=0; i < allThreads; i++) {
            long threadID = reply.getNextValueAsThreadID();
            String threadName = null;
            try {
                threadName = debuggeeWrapper.vmMirror.getThreadName(threadID);
            } catch (Throwable thrown ) {
                // ignore
                continue;
            }
            int threadIndex = 0;
            for (; threadIndex < startedThreadsNumber; threadIndex++) {
                if ( startedThreadsNames[threadIndex].equals(threadName) ) {
                    startedThreadsIDs[threadIndex] = threadID;
                    break;
                }
            }
        }
            
        logWriter.println
        ("==> Send requests for 'METHOD_ENTRY' event...");
        ReplyPacket setEventReply = null;
        setEventReply = setEventRequest(JDWPConstants.EventKind.METHOD_ENTRY,
                JDWPConstants.SuspendPolicy.NONE, ANY_THREAD, threadRefTypeID, 0);
        if ( setEventReply == null ) {
            logWriter.println("## FAILURE: Can NOT set event request:");
            logWriter.println("##          Event kind = " + JDWPConstants.EventKind.METHOD_ENTRY +
                    "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.METHOD_ENTRY));
            testCaseStatus = FAILURE;
        } else {
            if ( checkReplyForError(setEventReply, JDWPConstants.Error.NONE,
                "EventRequest.set command for METHOD_ENTRY") ) {
                testCaseStatus = FAILURE;
            } else {
                methodEntryRequestID = setEventReply.getNextValueAsInt();
            }
        }

        logWriter.println
        ("==> Send requests for 'METHOD_EXIT' event...");
        setEventReply = setEventRequest(JDWPConstants.EventKind.METHOD_EXIT,
                JDWPConstants.SuspendPolicy.NONE, ANY_THREAD, threadRefTypeID, 0);
        if ( setEventReply == null ) {
            logWriter.println("## FAILURE: Can NOT set event request:");
            logWriter.println("##          Event kind = " + JDWPConstants.EventKind.METHOD_EXIT +
                    "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.METHOD_EXIT));
            testCaseStatus = FAILURE;
        } else {
            if ( checkReplyForError(setEventReply, JDWPConstants.Error.NONE,
                "EventRequest.set command for METHOD_EXIT") ) {
                testCaseStatus = FAILURE;
            } else {
                methodExitRequestID = setEventReply.getNextValueAsInt();
            }
        }

        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time (mlsecs) of preparing requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events " +
        "= " + measurableCodeRunningTimeMlsec);

        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE: Can NOT set requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events!");
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed
            ("## FAILURE while sending requests for 'METHOD_ENTRY' and 'METHOD_EXIT' events");
        }
    
        logWriter.println("==> Resume debuggee and wait for creating memory stress in debuggee...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        resumeDebuggee("#1.1");

        debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_09.5");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for creating memory stress in debuggee = " + 
                measurableCodeRunningTimeMlsec);

        logWriter.println("\n");
        logWriter.println
        ("==> Resume debuggee to allow all threads to enter in test method and cause events...");
        resumeDebuggee("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for all debuggee's threads exit from test method...");

        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_10");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee");
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
            while ( methodEntryRequestID != NO_REQUEST_ID ) {
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
                if ( requestID != methodEntryRequestID ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected requestID for received event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received requestID = " + requestID);
                        logWriter.println("##          Expected requestID = " + methodEntryRequestID);
                    }
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
            if ( methodExitRequestID != NO_REQUEST_ID ) {
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
                if ( requestID != methodExitRequestID ) {
                    boolean toPrn = limitedPrintln
                    ("## FAILURE: Unexpected requestID for received event for thread name = " +
                            startedThreadsNames[i]);
                    if ( toPrn ) {
                        logWriter.println("##          Received requestID = " + requestID);
                        logWriter.println("##          Expected requestID = " + methodExitRequestID);
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
            terminateDebuggee(FAILURE, "MARKER_10.1");
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
        if ( methodEntryRequestID != NO_REQUEST_ID ) {
            reply = clearEventRequest
            (JDWPConstants.EventKind.METHOD_ENTRY, methodEntryRequestID);
            if ( checkReplyForError
                    (reply, JDWPConstants.Error.NONE,
                    "EventRequest.Clear command") ) {
                logWriter.println("\n");
                logWriter.println
                ("## FAILURE: Can NOT clear event request for 'METHOD_ENTRY' event");
                clearingRequestsStatus = FAILURE;
            }
        }
        if ( methodExitRequestID != NO_REQUEST_ID ) {
            reply = clearEventRequest
            (JDWPConstants.EventKind.METHOD_EXIT, methodExitRequestID);
            if ( checkReplyForError
                    (reply, JDWPConstants.Error.NONE,
                    "EventRequest.Clear command") ) {
                logWriter.println("\n");
                logWriter.println
                ("## FAILURE: Can NOT clear event request for 'METHOD_EXIT' event");
                clearingRequestsStatus = FAILURE;
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
        printlnForDebug("receiving 'SIGNAL_READY_04' Thread Signal from debuggee...");
        debuggeeSignal =  receiveThreadSignalWithWait();
        if ( ! SIGNAL_READY_04.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_04' Thread Signal from debuggee");
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
        logWriter.println("==> testEvent007: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testEvent007: FAILED");
            return failed("testEvent007:");
        } else {
            logWriter.println("==> testEvent007: OK");
            return passed("testEvent007: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testEvent007: FAILED");
    return failed("==> testEvent007: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new EventTest007().test(args));
	}
}
