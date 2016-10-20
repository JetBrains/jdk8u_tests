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
 * Created on 23.08.2006 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT017;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.ArrayRegion;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ParsedEvent;
import org.apache.harmony.share.framework.jpda.jdwp.ParsedEvent.*;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;


/**
 * EventTest017 class implements the JDWP stress test for 
 * composite THREAD_END events. 
 */
public class EventTest017 extends StressTestCase {
    
    final int EVENT_REQUESTS_NUMBER_PER_THREAD = EVENT017_EVENT_REQUESTS_NUMBER_PER_THREAD;

    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT017.EventDebuggee017";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case exercises the JDWP agent when very big number of event
     * requests for THREAD_END event are sent.
     * The stage 1:
     *  - Debuggee - creates some 'tested' threads.
     *  - Test - sends requests for THREAD_END events for 'checked' threads,
     *    which are the part of all 'tested' threads. For each 'checked' thread
     *    big number of requests are sent.
     *  - Debuggee - starts all 'tested' threads and fihishes them.
     *  - Test - checks that all expected THREAD_END events for all requests
     *    are received from JDWP agent: for each 'checked' thread big number
     *    of THREAD_END events should be received. No any THREAD_END event 
     *    should be received for 'tested' threads, for which requests were not sent. 
     *  - Test - clear all requests for THREAD_END events for all 'checked' threads.
     * The stage 2:
     *  - Debuggee - creates all 'tested' threads again.
     *  - Test - sends requests for THREAD_END events for all 'checked' threads,
     *    then cleares all sent requests for the some part of 'checked' threads.
     *  - Debuggee - starts all 'tested' threads and fihishes them.
     *  - Test - checks that only expected THREAD_END events for all active requests
     *    are received from JDWP agent.
     *  - Test - clear all active requests for THREAD_END events.
     * The stage 3:
     *  - Debuggee - creates all 'tested' threads again.
     *  - Test - sends requests for THREAD_END events for all 'checked' threads,
     *    then cleares all sent requests for all 'checked' threads.
     *  - Debuggee - starts all 'tested' threads and fihishes them.
     *  - Test - checks that no any events except VM_DEATH event
     *    are received from JDWP agent.
     */
    public Result  testEvent017() {

        logWriter.println("==> testEvent017: START (" + new Date() + ")...");
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
        logWriter.println(" ");
        logWriter.println("==> Wait for debuggee to create tested threads...");
        String debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            String failureMessage = "## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee! ";
            if ( SIGNAL_FAILURE.equals(debuggeeSignal) ) {
                failureMessage = "## SIGNAL_FAILURE received from debuggee! ";
            }
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed(failureMessage);
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        logWriter.println("==> Get debuggeeRefTypeID...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed("## Can NOT get debuggeeRefTypeID! ");
        }
        logWriter.println("==> debuggeeRefTypeID = " + debuggeeRefTypeID);
        
        int checkedThreadsNumber = EventDebuggee017.CHECKED_THREADS_NUMBER;
        String[] checkedThreadNames = new String[checkedThreadsNumber];
        long[] checkedThreadIDs = new long[checkedThreadsNumber];
        logWriter.println("==> checkedThreadsNumber = " + checkedThreadsNumber);
        
        logWriter.println("==> Get checkedThreadIDs for all checked threads...");
        long checkedThreadsArrayID = getArraIDForStaticArrayField(debuggeeRefTypeID, "checkedThreads");
        if ( checkedThreadsArrayID == BAD_ARRAY_ID ) {
            logWriter.println("## FAILURE: Can NOT get checkedThreadsArrayID");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get checkedThreadsArrayID! ");
        }
        ArrayRegion checkedThreadsArrayRegion = getArrayRegion(checkedThreadsArrayID, checkedThreadsNumber);
        if ( checkedThreadsArrayRegion == null ) {
            logWriter.println("## FAILURE: Can NOT get checkedThreadsArrayRegion");
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## Can NOT get checkedThreadsArrayRegion! ");
        }
        
        for ( int i=0; i < checkedThreadsNumber; i++) {
            checkedThreadNames[i] = EventDebuggee017.TESTED_THREAD_NAME_PATTERN + i;
            checkedThreadIDs[i] = getObjectIDFromArrayRegion(checkedThreadsArrayRegion, i);
            if ( checkedThreadIDs[i] == BAD_OBJECT_ID ) {
                String failureMessage = "## FAILURE: Can NOT get checkedThreadID for '" 
                        + checkedThreadNames[i] + "' thread! ";
                logWriter.println(failureMessage);
                terminateDebuggee(FAILURE, "MARKER_07");
                return failed(failureMessage);
            }
        }
        logWriter.println("==> OK - all checkedThreadIDs are got!");
        
        logWriter.println(" ");
        logWriter.println("==> Prepare and send requests for THREAD_END events " +
                "for all checked threads...");
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        
        int[][] threadEndRequestIDs = new int[checkedThreadsNumber][];
        int threadEndRequests = 0;

        int testCaseStatus = SUCCESS;
        boolean outOfMemory = false;
        boolean toBreak = false;
        for (int threadIndex=0; threadIndex < checkedThreadsNumber; threadIndex++) {
            threadEndRequestIDs[threadIndex] = new int[EVENT_REQUESTS_NUMBER_PER_THREAD];
            long checkedThreadID = checkedThreadIDs[threadIndex];
            String checkedThreadName = checkedThreadNames[threadIndex];
            
            for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                ReplyPacket setEventReply = setEventRequest(JDWPConstants.EventKind.THREAD_END,
                    JDWPConstants.SuspendPolicy.EVENT_THREAD, checkedThreadID, NO_CLASS_ID, currentTimeout());
                if ( setEventReply == null ) {
                    testCaseStatus = FAILURE;
                    logWriter.println("## FAILURE: Can NOT set event request:");
                    logWriter.println("##          Event kind = " + JDWPConstants.EventKind.THREAD_END +
                            "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.THREAD_END));
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    toBreak = true;
                    break;   
                } else {
                    if ( checkReplyForError(setEventReply, expectedErrors,
                        "EventRequest.Set command for THREAD_END event") ) {
                        logWriter.println("##          Thread name = " + checkedThreadNames);
                        testCaseStatus = FAILURE;
                        toBreak = true;
                        break;
                    }
                    if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                            "EventRequest.Set command for THREAD_END event") ) {
                        logWriter.println("##          Thread name = " + checkedThreadNames);
                        outOfMemory = true;
                        toBreak = true;
                        break;
                    }
                    threadEndRequestIDs[threadIndex][i] = setEventReply.getNextValueAsInt();
                    threadEndRequests++;
                }
            }
            if ( toBreak ) {
                break;
            }
        }

        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
            ("==> Time (mlsecs) of preparing requests for THREAD_END events = " + 
            measurableCodeRunningTimeMlsec);
        logWriter.println("==> Successful requests for THREAD_END event = " + threadEndRequests);
        if ( outOfMemory ) {
            logWriter.println
            ("==> OutOfMemory while sending requests for THREAD_END events " +
            "- it is expected result! ");
            logWriter.println("==> testEvent017 is broken off! ");
            terminateDebuggee(SUCCESS, "MARKER_08");
            return passed
                ("==> OutOfMemory while sending requests for THREAD_END events " +
                "- Expected result! ");
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while sending requests for THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed
            ("## FAILURE while sending requests for THREAD_END events! ");
        }
        
        logWriter.println
        ("==> Resume debuggee to start all tested threads and to finish them...");
        resumeDebuggee("#2");

        logWriter.println(" ");
        logWriter.println("==> Wait for all tested threads finish...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            String failureMessage = "## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee! ";
            if ( SIGNAL_FAILURE.equals(debuggeeSignal) ) {
                failureMessage = "## SIGNAL_FAILURE received from debuggee! ";
            }
            terminateDebuggee(FAILURE, "MARKER_10");
            return failed(failureMessage);
        }
        
        logWriter.println
        ("==> Check that all expected THREAD_END events for all requests are received from JDWP agent...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        int receivedThreadEndEvents = 0;
        byte expectedEventKind = JDWPConstants.EventKind.THREAD_END;
        while ( true ) {
            ParsedEvent receivedEvent = receiveEvent(NO_REQUEST_ID, NO_EVENT_KIND, 
                    NO_THREAD_ID, currentTimeout());
            if ( receivedEvent == null ) {
                testCaseStatus = FAILURE;
                logWriter.println
                ("## FAILURE: Can NOT receive next expected THREAD_END event!");
                break;   
            }
            
            int receivedEventRequestID = receivedEvent.getRequestID();
            int foundThreadIndex = -1;
            int foundRequestIndex = -1;
            for (int threadIndex=0; threadIndex < checkedThreadsNumber; threadIndex++) {
                for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                    if ( receivedEventRequestID == threadEndRequestIDs[threadIndex][i] ) {
                        foundRequestIndex = i;
                        break;
                    }
                }
                if ( foundRequestIndex != -1 ) {
                    foundThreadIndex = threadIndex;
                    break;
                }
            }
            byte receivedEventKind = receivedEvent.getEventKind();
            long receivedThreadID = 0;
            String receivedThreadName = null;
            try {
                receivedThreadID = ((EventThread)receivedEvent).getThreadID();
                receivedThreadName = getThreadName(receivedThreadID);
                if ( receivedThreadName == null ) {
                    receivedThreadName = "Unknown_Thread_Name";
                }
            } catch (Throwable thrown) {
                // ignore
            }
            if ( foundRequestIndex == -1 ) {
                testCaseStatus = FAILURE;
                logWriter.println("## FAILURE: Unexpected event is received:");
                logWriter.println("##          RequestId = " + receivedEventRequestID + 
                    "; Test did NOT send event request with such ID!");
                logWriter.println("##          Event kind = " + receivedEventKind +
                        "(" + JDWPConstants.EventKind.getName(receivedEventKind) + ")");
                if ( receivedEventKind != expectedEventKind ) {
                    logWriter.println("##          Expected event kind = " + expectedEventKind +
                            "(" + JDWPConstants.EventKind.getName(expectedEventKind) + ")");
                }
                if ( receivedThreadName != null ) {
                    logWriter.println("##          Event threadID = " + receivedThreadID +
                            "(; Thread name = '" + receivedThreadName + "')");
                }
                testCaseStatus = FAILURE;
                break;
            }

            if ( receivedEventKind != expectedEventKind ) {
                logWriter.println("## FAILURE: Unexpected event is received:");
                logWriter.println("##          RequestId = " + receivedEventRequestID);
                logWriter.println("##          Received event kind = " + receivedEventKind +
                        "(" + JDWPConstants.EventKind.getName(receivedEventKind) + ")");
                logWriter.println("##          Expected event kind = " + expectedEventKind +
                        "(" + JDWPConstants.EventKind.getName(expectedEventKind) + ")");
                if ( receivedThreadName != null ) {
                    logWriter.println("##          Event threadID = " + receivedThreadID +
                            "(; Thread name = '" + receivedThreadName + "')");
                }
                testCaseStatus = FAILURE;
                break;
            }

            long expectedThreadID = checkedThreadIDs[foundThreadIndex];
            String expectedThreadName = checkedThreadNames[foundThreadIndex];
            if ( receivedThreadID != expectedThreadID ) {
                logWriter.println("## FAILURE: Received " + 
                    JDWPConstants.EventKind.getName(expectedEventKind) + " event has unexpected threadID:");
                logWriter.println("##          RequestId = " + receivedEventRequestID);
                logWriter.println("##          Received threadID = " + receivedThreadID +
                        "(; Thread name = '" + receivedThreadName + "')");
                logWriter.println("##          Expected threadID = " + expectedThreadID +
                        "(; Thread name = '" + expectedThreadName + "')");
                testCaseStatus = FAILURE;
                break;
            } else {
                if ( ! expectedThreadName.equals(receivedThreadName) ) {
                    logWriter.println("## FAILURE: Thread for received " + 
                        JDWPConstants.EventKind.getName(expectedEventKind) + " event has unexpected name:");
                    logWriter.println("##          RequestId = " + receivedEventRequestID);
                    logWriter.println("##          ThreadID = " + receivedThreadID);
                    logWriter.println("##          Received thread name = '" + receivedThreadName + "'");
                    logWriter.println("##          Expected thread name = '" + expectedThreadName + "'");
                    testCaseStatus = FAILURE;
                    break;
                }
            }
            receivedThreadEndEvents++;
            if ( receivedThreadEndEvents == threadEndRequests ) {
                break;
            }
        }
        logWriter.println
        ("==> Number of successful received THREAD_END events = " + receivedThreadEndEvents );
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of receiving THREAD_END events = " + 
                measurableCodeRunningTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while receiving THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed("## FAILURE while receiving THREAD_END events! ");
        }
        if ( receivedThreadEndEvents != threadEndRequests ) {
            logWriter.println("## FAILURE: Unexpected number of received THREAD_END events!");
            logWriter.println("##          Expected number of THREAD_END events = " + threadEndRequests);
            terminateDebuggee(FAILURE, "MARKER_12");
            return failed("## FAILURE: Unexpected number of received THREAD_END events! ");
        }
        logWriter.println("==> OK - all expected THREAD_END events are received!");

        logWriter.println(" ");
        logWriter.println("==> Clear all active requests for THREAD_END events " +
                "for all checked threads...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        int clearedThreadEndRequests = 0;
        toBreak = false;
        for (int threadIndex=0; threadIndex < checkedThreadsNumber; threadIndex++) {
            long checkedThreadID = checkedThreadIDs[threadIndex];
            String checkedThreadName = checkedThreadNames[threadIndex];
            
            for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                ReplyPacket reply = clearEventRequest
                (JDWPConstants.EventKind.THREAD_END, threadEndRequestIDs[threadIndex][i]);
                if ( checkReplyForError(reply, expectedErrors,
                    "EventRequest.Clear command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    testCaseStatus = FAILURE;
                    toBreak = true;
                    break;
                }
                if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "EventRequest.Clear command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    outOfMemory = true;
                    toBreak = true;
                    break;
                }
                clearedThreadEndRequests++;
            }
            if ( toBreak ) {
                break;
            }
        }

        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println("==> Time (mlsecs) of clearing requests for THREAD_END events = " + 
            measurableCodeRunningTimeMlsec);
        logWriter.println
        ("==> Successful cleared requests for THREAD_END events = " + clearedThreadEndRequests);
        if ( outOfMemory ) {
            logWriter.println
            ("==> OutOfMemory while clearing requests for THREAD_END events " +
            "- it is expected result! ");
            logWriter.println("==> testEvent017 is broken off! ");
            terminateDebuggee(SUCCESS, "MARKER_12.01");
            return passed
                ("==> OutOfMemory while clearing requests for THREAD_END events " +
                "- Expected result! ");
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while clearing requests for THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_12.02");
            return failed
            ("## FAILURE while clearing requests for THREAD_END events! ");
        }
        
        logWriter.println("==> OK - all active requests for THREAD_END events for " 
                + "all checked threads are cleared!");
        
        logWriter.println(" ");
        logWriter.println
        ("==> Resume debuggee to create all tested threads again.");
        resumeDebuggee("#3");

        logWriter.println(" ");
        logWriter.println("==> Wait for debuggee to create tested threads again...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            String failureMessage = "## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee! ";
            if ( SIGNAL_FAILURE.equals(debuggeeSignal) ) {
                failureMessage = "## SIGNAL_FAILURE received from debuggee! ";
            }
            terminateDebuggee(FAILURE, "MARKER_13");
            return failed(failureMessage);
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        logWriter.println(" ");
        logWriter.println("==> Get checkedThreadIDs for all new createed checked threads...");
        checkedThreadsArrayRegion = getArrayRegion(checkedThreadsArrayID, checkedThreadsNumber);
        if ( checkedThreadsArrayRegion == null ) {
            logWriter.println("## FAILURE: Can NOT get checkedThreadsArrayRegion");
            terminateDebuggee(FAILURE, "MARKER_14");
            return failed("## Can NOT get checkedThreadsArrayRegion! ");
        }
        
        for ( int i=0; i < checkedThreadsNumber; i++) {
            checkedThreadNames[i] = EventDebuggee017.TESTED_THREAD_NAME_PATTERN + i;
            checkedThreadIDs[i] = getObjectIDFromArrayRegion(checkedThreadsArrayRegion, i);
            if ( checkedThreadIDs[i] == BAD_OBJECT_ID ) {
                String failureMessage = "## FAILURE: Can NOT get checkedThreadID for '" 
                        + checkedThreadNames[i] + "' thread! ";
                logWriter.println(failureMessage);
                terminateDebuggee(FAILURE, "MARKER_15");
                return failed(failureMessage);
            }
        }
        logWriter.println("==> OK - all checkedThreadIDs are got!");

        logWriter.println(" ");
        logWriter.println("==> Prepare and send requests for THREAD_END events " +
                "for all checked threads again...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        
        threadEndRequests = 0;

        toBreak = false;
        for (int threadIndex=0; threadIndex < checkedThreadsNumber; threadIndex++) {
            long checkedThreadID = checkedThreadIDs[threadIndex];
            String checkedThreadName = checkedThreadNames[threadIndex];
            
            for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                ReplyPacket setEventReply = setEventRequest(JDWPConstants.EventKind.THREAD_END,
                    JDWPConstants.SuspendPolicy.EVENT_THREAD, checkedThreadID, NO_CLASS_ID, currentTimeout());
                if ( setEventReply == null ) {
                    testCaseStatus = FAILURE;
                    logWriter.println("## FAILURE: Can NOT set event request:");
                    logWriter.println("##          Event kind = " + JDWPConstants.EventKind.THREAD_END +
                            "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.THREAD_END));
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    toBreak = true;
                    break;   
                } else {
                    if ( checkReplyForError(setEventReply, expectedErrors,
                        "EventRequest.Set command for THREAD_END event") ) {
                        logWriter.println("##          Thread name = " + checkedThreadNames);
                        testCaseStatus = FAILURE;
                        toBreak = true;
                        break;
                    }
                    if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                            "EventRequest.Set command for THREAD_END event") ) {
                        logWriter.println("##          Thread name = " + checkedThreadNames);
                        outOfMemory = true;
                        toBreak = true;
                        break;
                    }
                    threadEndRequestIDs[threadIndex][i] = setEventReply.getNextValueAsInt();
                    threadEndRequests++;
                }
            }
            if ( toBreak ) {
                break;
            }
        }

        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
            ("==> Time (mlsecs) of preparing requests for THREAD_END events = " + 
            measurableCodeRunningTimeMlsec);
        logWriter.println("==> Successful requests for THREAD_END event = " + threadEndRequests);
        if ( outOfMemory ) {
            logWriter.println
            ("==> OutOfMemory while sending requests for THREAD_END events " +
            "- it is expected result! ");
            logWriter.println("==> testEvent017 is broken off! ");
            terminateDebuggee(SUCCESS, "MARKER_16");
            return passed
                ("==> OutOfMemory while sending requests for THREAD_END events " +
                "- Expected result! ");
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while sending requests for THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_17");
            return failed
            ("## FAILURE while sending requests for THREAD_END events! ");
        }
        
        int threadsNumberForClearRequests = checkedThreadsNumber/2;
        logWriter.println(" ");
        logWriter.println("==> Clear requests for THREAD_END events " +
                "for part of checked threads - last " + threadsNumberForClearRequests + " threads...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        clearedThreadEndRequests = 0;
        toBreak = false;
        int firstThreadIndex = checkedThreadsNumber - threadsNumberForClearRequests;
        for (int threadIndex=firstThreadIndex; threadIndex < checkedThreadsNumber; threadIndex++) {
            long checkedThreadID = checkedThreadIDs[threadIndex];
            String checkedThreadName = checkedThreadNames[threadIndex];
            
            for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                ReplyPacket reply = clearEventRequest
                (JDWPConstants.EventKind.THREAD_END, threadEndRequestIDs[threadIndex][i]);
                if ( checkReplyForError(reply, expectedErrors,
                    "EventRequest.Clear command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    testCaseStatus = FAILURE;
                    toBreak = true;
                    break;
                }
                if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "EventRequest.Clear command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    outOfMemory = true;
                    toBreak = true;
                    break;
                }
                clearedThreadEndRequests++;
            }
            if ( toBreak ) {
                break;
            }
        }

        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
            ("==> Time (mlsecs) of clearing requests for THREAD_END events = " + 
            measurableCodeRunningTimeMlsec);
        logWriter.println
        ("==> Successful cleared requests for THREAD_END events = " + clearedThreadEndRequests);
        if ( outOfMemory ) {
            logWriter.println
            ("==> OutOfMemory while clearing requests for THREAD_END events " +
            "- it is expected result! ");
            logWriter.println("==> testEvent017 is broken off! ");
            terminateDebuggee(SUCCESS, "MARKER_18");
            return passed
                ("==> OutOfMemory while clearing requests for THREAD_END events " +
                "- Expected result! ");
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while clearing requests for THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_19");
            return failed
            ("## FAILURE while clearing requests for THREAD_END events! ");
        }
        
        logWriter.println("==> OK - all requests for THREAD_END events for last " 
                + threadsNumberForClearRequests + " threads are cleared!");
        int threadsNumberWithActiveRequests = checkedThreadsNumber - threadsNumberForClearRequests;
        int activeThreadEndRequests = threadsNumberWithActiveRequests * EVENT_REQUESTS_NUMBER_PER_THREAD;
        logWriter.println(" ");
        logWriter.println("==> Number of still active requests for THREAD_END events = " 
            + activeThreadEndRequests + "; Threads number with active requests = " 
            + threadsNumberWithActiveRequests);

        logWriter.println
        ("==> Resume debuggee to start all tested threads and finish them...");
        resumeDebuggee("#4");

        logWriter.println(" ");
        logWriter.println("==> Wait for all tested threads finish...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        if ( ! SIGNAL_READY_04.equals(debuggeeSignal) ) {
            String failureMessage = "## FAILURE while receiving 'SIGNAL_READY_04' Signal from debuggee! ";
            if ( SIGNAL_FAILURE.equals(debuggeeSignal) ) {
                failureMessage = "## SIGNAL_FAILURE received from debuggee! ";
            }
            terminateDebuggee(FAILURE, "MARKER_20");
            return failed(failureMessage);
        }
        
        logWriter.println(" ");
        logWriter.println
        ("==> Check that all expected THREAD_END events for all " 
                + "still active requests are received from JDWP agent...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        receivedThreadEndEvents = 0;
        while ( true ) {
            ParsedEvent receivedEvent = receiveEvent(NO_REQUEST_ID, NO_EVENT_KIND, 
                    NO_THREAD_ID, currentTimeout());
            if ( receivedEvent == null ) {
                testCaseStatus = FAILURE;
                logWriter.println
                ("## FAILURE: Can NOT receive next expected THREAD_END event!");
                break;   
            }
            
            int receivedEventRequestID = receivedEvent.getRequestID();
            int foundThreadIndex = -1;
            int foundRequestIndex = -1;
            for (int threadIndex=0; threadIndex < threadsNumberWithActiveRequests; threadIndex++) {
                for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                    if ( receivedEventRequestID == threadEndRequestIDs[threadIndex][i] ) {
                        foundRequestIndex = i;
                        break;
                    }
                }
                if ( foundRequestIndex != -1 ) {
                    foundThreadIndex = threadIndex;
                    break;
                }
            }
            byte receivedEventKind = receivedEvent.getEventKind();
            long receivedThreadID = 0;
            String receivedThreadName = null;
            try {
                receivedThreadID = ((EventThread)receivedEvent).getThreadID();
                receivedThreadName = getThreadName(receivedThreadID);
                if ( receivedThreadName == null ) {
                    receivedThreadName = "Unknown_Thread_Name";
                }
            } catch (Throwable thrown) {
                // ignore
            }
            if ( foundRequestIndex == -1 ) {
                testCaseStatus = FAILURE;
                logWriter.println("## FAILURE: Unexpected event is received:");
                logWriter.println("##          RequestId = " + receivedEventRequestID + 
                    "; Test did NOT send event request with such ID!");
                logWriter.println("##          Event kind = " + receivedEventKind +
                        "(" + JDWPConstants.EventKind.getName(receivedEventKind) + ")");
                if ( receivedEventKind != expectedEventKind ) {
                    logWriter.println("##          Expected event kind = " + expectedEventKind +
                            "(" + JDWPConstants.EventKind.getName(expectedEventKind) + ")");
                }
                if ( receivedThreadName != null ) {
                    logWriter.println("##          Event threadID = " + receivedThreadID +
                            "(; Thread name = '" + receivedThreadName + "')");
                }
                testCaseStatus = FAILURE;
                break;
            }

            if ( receivedEventKind != expectedEventKind ) {
                logWriter.println("## FAILURE: Unexpected event is received:");
                logWriter.println("##          RequestId = " + receivedEventRequestID);
                logWriter.println("##          Received event kind = " + receivedEventKind +
                        "(" + JDWPConstants.EventKind.getName(receivedEventKind) + ")");
                logWriter.println("##          Expected event kind = " + expectedEventKind +
                        "(" + JDWPConstants.EventKind.getName(expectedEventKind) + ")");
                if ( receivedThreadName != null ) {
                    logWriter.println("##          Event threadID = " + receivedThreadID +
                            "(; Thread name = '" + receivedThreadName + "')");
                }
                testCaseStatus = FAILURE;
                break;
            }

            long expectedThreadID = checkedThreadIDs[foundThreadIndex];
            String expectedThreadName = checkedThreadNames[foundThreadIndex];
            if ( receivedThreadID != expectedThreadID ) {
                logWriter.println("## FAILURE: Received " + 
                    JDWPConstants.EventKind.getName(expectedEventKind) + " event has unexpected threadID:");
                logWriter.println("##          RequestId = " + receivedEventRequestID);
                logWriter.println("##          Received threadID = " + receivedThreadID +
                        "(; Thread name = '" + receivedThreadName + "')");
                logWriter.println("##          Expected threadID = " + expectedThreadID +
                        "(; Thread name = '" + expectedThreadName + "')");
                testCaseStatus = FAILURE;
                break;
            } else {
                if ( ! expectedThreadName.equals(receivedThreadName) ) {
                    logWriter.println("## FAILURE: Thread for received " + 
                        JDWPConstants.EventKind.getName(expectedEventKind) + " event has unexpected name:");
                    logWriter.println("##          RequestId = " + receivedEventRequestID);
                    logWriter.println("##          ThreadID = " + receivedThreadID);
                    logWriter.println("##          Received thread name = '" + receivedThreadName + "'");
                    logWriter.println("##          Expected thread name = '" + expectedThreadName + "'");
                    testCaseStatus = FAILURE;
                    break;
                }
            }
            receivedThreadEndEvents++;
            if ( receivedThreadEndEvents == activeThreadEndRequests ) {
                break;
            }
        }
        logWriter.println
        ("==> Number of successful received THREAD_END events = " + receivedThreadEndEvents );
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of receiving THREAD_END events = " + 
                measurableCodeRunningTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while receiving THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_21");
            return failed("## FAILURE while receiving THREAD_END events! ");
        }
        if ( receivedThreadEndEvents != activeThreadEndRequests ) {
            logWriter.println("## FAILURE: Unexpected number of received THREAD_END events!");
            logWriter.println("##          Expected number of THREAD_END events = " 
                + activeThreadEndRequests);
            terminateDebuggee(FAILURE, "MARKER_22");
            return failed("## FAILURE: Unexpected number of received THREAD_END events! ");
        }

        logWriter.println("==> OK - all expected THREAD_END events are received!");
        
        logWriter.println(" ");
        logWriter.println("==> Clear active requests for THREAD_END events " +
                "for part of checked threads - the first " + threadsNumberWithActiveRequests + " threads...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        clearedThreadEndRequests = 0;
        toBreak = false;
        for (int threadIndex=0; threadIndex < threadsNumberWithActiveRequests; threadIndex++) {
            long checkedThreadID = checkedThreadIDs[threadIndex];
            String checkedThreadName = checkedThreadNames[threadIndex];
            
            for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                ReplyPacket reply = clearEventRequest
                (JDWPConstants.EventKind.THREAD_END, threadEndRequestIDs[threadIndex][i]);
                if ( checkReplyForError(reply, expectedErrors,
                    "EventRequest.Clear command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    testCaseStatus = FAILURE;
                    toBreak = true;
                    break;
                }
                if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "EventRequest.Clear command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    outOfMemory = true;
                    toBreak = true;
                    break;
                }
                clearedThreadEndRequests++;
            }
            if ( toBreak ) {
                break;
            }
        }

        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
            ("==> Time (mlsecs) of clearing requests for THREAD_END events = " + 
            measurableCodeRunningTimeMlsec);
        logWriter.println
        ("==> Successful cleared requests for THREAD_END events = " + clearedThreadEndRequests);
        if ( outOfMemory ) {
            logWriter.println
            ("==> OutOfMemory while clearing requests for THREAD_END events " +
            "- it is expected result! ");
            logWriter.println("==> testEvent017 is broken off! ");
            terminateDebuggee(SUCCESS, "MARKER_22.01");
            return passed
                ("==> OutOfMemory while clearing requests for THREAD_END events " +
                "- Expected result! ");
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while clearing requests for THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_22.02");
            return failed
            ("## FAILURE while clearing requests for THREAD_END events! ");
        }
        
        logWriter.println("==> OK - all active requests for THREAD_END events for the first " 
                + threadsNumberWithActiveRequests + " threads are cleared!");
        
        logWriter.println(" ");
        logWriter.println
        ("==> Resume debuggee to create all tested threads again.");
        resumeDebuggee("#5");

        logWriter.println(" ");
        logWriter.println("==> Wait for debuggee to create tested threads again...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_05.equals(debuggeeSignal) ) {
            String failureMessage = "## FAILURE while receiving 'SIGNAL_READY_05' Signal from debuggee! ";
            if ( SIGNAL_FAILURE.equals(debuggeeSignal) ) {
                failureMessage = "## SIGNAL_FAILURE received from debuggee! ";
            }
            terminateDebuggee(FAILURE, "MARKER_23");
            return failed(failureMessage);
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        logWriter.println(" ");
        logWriter.println("==> Get checkedThreadIDs for all new createed checked threads...");
        checkedThreadsArrayRegion = getArrayRegion(checkedThreadsArrayID, checkedThreadsNumber);
        if ( checkedThreadsArrayRegion == null ) {
            logWriter.println("## FAILURE: Can NOT get checkedThreadsArrayRegion");
            terminateDebuggee(FAILURE, "MARKER_24");
            return failed("## Can NOT get checkedThreadsArrayRegion! ");
        }
        
        for ( int i=0; i < checkedThreadsNumber; i++) {
            checkedThreadNames[i] = EventDebuggee017.TESTED_THREAD_NAME_PATTERN + i;
            checkedThreadIDs[i] = getObjectIDFromArrayRegion(checkedThreadsArrayRegion, i);
            if ( checkedThreadIDs[i] == BAD_OBJECT_ID ) {
                String failureMessage = "## FAILURE: Can NOT get checkedThreadID for '" 
                        + checkedThreadNames[i] + "' thread! ";
                logWriter.println(failureMessage);
                terminateDebuggee(FAILURE, "MARKER_25");
                return failed(failureMessage);
            }
        }
        logWriter.println("==> OK - all checkedThreadIDs are got!");

        logWriter.println(" ");
        logWriter.println("==> Prepare and send requests for THREAD_END events " +
                "for all checked threads again...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        
        threadEndRequests = 0;

        toBreak = false;
        for (int threadIndex=0; threadIndex < checkedThreadsNumber; threadIndex++) {
            long checkedThreadID = checkedThreadIDs[threadIndex];
            String checkedThreadName = checkedThreadNames[threadIndex];
            
            for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                ReplyPacket setEventReply = setEventRequest(JDWPConstants.EventKind.THREAD_END,
                    JDWPConstants.SuspendPolicy.EVENT_THREAD, checkedThreadID, NO_CLASS_ID, currentTimeout());
                if ( setEventReply == null ) {
                    testCaseStatus = FAILURE;
                    logWriter.println("## FAILURE: Can NOT set event request:");
                    logWriter.println("##          Event kind = " + JDWPConstants.EventKind.THREAD_END +
                            "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.THREAD_END));
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    toBreak = true;
                    break;   
                } else {
                    if ( checkReplyForError(setEventReply, expectedErrors,
                        "EventRequest.Set command for THREAD_END event") ) {
                        logWriter.println("##          Thread name = " + checkedThreadNames);
                        testCaseStatus = FAILURE;
                        toBreak = true;
                        break;
                    }
                    if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                            "EventRequest.Set command for THREAD_END event") ) {
                        logWriter.println("##          Thread name = " + checkedThreadNames);
                        outOfMemory = true;
                        toBreak = true;
                        break;
                    }
                    threadEndRequestIDs[threadIndex][i] = setEventReply.getNextValueAsInt();
                    threadEndRequests++;
                }
            }
            if ( toBreak ) {
                break;
            }
        }

        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
            ("==> Time (mlsecs) of preparing requests for THREAD_END events = " + 
            measurableCodeRunningTimeMlsec);
        logWriter.println("==> Successful requests for THREAD_END event = " + threadEndRequests);
        if ( outOfMemory ) {
            logWriter.println
            ("==> OutOfMemory while sending requests for THREAD_END events " +
            "- it is expected result! ");
            logWriter.println("==> testEvent017 is broken off! ");
            terminateDebuggee(SUCCESS, "MARKER_26");
            return passed
                ("==> OutOfMemory while sending requests for THREAD_END events " +
                "- Expected result! ");
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while sending requests for THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_27");
            return failed
            ("## FAILURE while sending requests for THREAD_END events! ");
        }
        
        logWriter.println(" ");
        logWriter.println("==> Clear requests for THREAD_END events " +
                "for all checked threads...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        clearedThreadEndRequests = 0;
        toBreak = false;
        for (int threadIndex=0; threadIndex < checkedThreadsNumber; threadIndex++) {
            long checkedThreadID = checkedThreadIDs[threadIndex];
            String checkedThreadName = checkedThreadNames[threadIndex];
            
            for (int i=0; i < EVENT_REQUESTS_NUMBER_PER_THREAD; i++) {
                ReplyPacket reply = clearEventRequest
                (JDWPConstants.EventKind.THREAD_END, threadEndRequestIDs[threadIndex][i]);
                if ( checkReplyForError(reply, expectedErrors,
                    "EventRequest.Clear command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    testCaseStatus = FAILURE;
                    toBreak = true;
                    break;
                }
                if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "EventRequest.Clear command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + checkedThreadNames);
                    outOfMemory = true;
                    toBreak = true;
                    break;
                }
                clearedThreadEndRequests++;
            }
            if ( toBreak ) {
                break;
            }
        }

        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
            ("==> Time (mlsecs) of clearing requests for THREAD_END events = " + 
            measurableCodeRunningTimeMlsec);
        logWriter.println
        ("==> Successful cleared requests for THREAD_END events = " + clearedThreadEndRequests);
        if ( outOfMemory ) {
            logWriter.println
            ("==> OutOfMemory while clearing requests for THREAD_END events " +
            "- it is expected result! ");
            logWriter.println("==> testEvent017 is broken off! ");
            terminateDebuggee(SUCCESS, "MARKER_28");
            return passed
                ("==> OutOfMemory while clearing requests for THREAD_END events " +
                "- Expected result! ");
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while clearing requests for THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_29");
            return failed
            ("## FAILURE while clearing requests for THREAD_END events! ");
        }
        
        logWriter.println("==> OK - all requests for THREAD_END events for all " 
                + "checked threads are cleared!");
        logWriter.println("==> There are NO any active requests for THREAD_END events!");
        
        logWriter.println(" ");
        logWriter.println
        ("==> Resume debuggee to start all tested threads again "
            + "and to finish them.");
        resumeDebuggee("#6");
        
        logWriter.println(" ");
        logWriter.println
        ("==> Wait for all tested threads finish...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_06.equals(debuggeeSignal) ) {
            String failureMessage = "## FAILURE while receiving 'SIGNAL_READY_06' Signal from debuggee! ";
            if ( SIGNAL_FAILURE.equals(debuggeeSignal) ) {
                failureMessage = "## SIGNAL_FAILURE received from debuggee! ";
            }
            terminateDebuggee(FAILURE, "MARKER_30");
            return failed(failureMessage);
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        logWriter.println(" ");
        logWriter.println
        ("==> Resume debuggee for it's finish - NO any events except VM_DEATH event have to happen!");
        resumeDebuggee("#7");
        
        logWriter.println(" ");
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

        logWriter.println(" ");
        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testEvent017: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testEvent017: FAILED");
            return failed("testEvent017:");
        } else {
            logWriter.println("==> testEvent017: OK");
            return passed("testEvent017: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testEvent017: FAILED");
    return failed("==> testEvent017: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new EventTest017().test(args));
	}
}
