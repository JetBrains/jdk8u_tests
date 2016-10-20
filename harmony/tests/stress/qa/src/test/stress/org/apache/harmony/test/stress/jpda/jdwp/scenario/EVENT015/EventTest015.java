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
 * Created on 16.08.2006 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT015;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ParsedEvent;
import org.apache.harmony.share.framework.jpda.jdwp.ParsedEvent.*;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;


/**
 * EventTest015 class implements the JDWP stress test for 
 * composite THREAD_START events and composite THREAD_END events. 
 */
public class EventTest015 extends StressTestCase {
    
    final int EVENT_REQUESTS_NUMBER = EVENT015_EVENT_REQUESTS_NUMBER;

    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT015.EventDebuggee015";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case exercises the JDWP agent when very big number of event
     * requests are sent, that should cause only one real event in debuggee 
     * but the test should receive very big number of events: one event by one request.
     * At first the test sends two sets of event requests: THREAD_START requests
     * and THREAD_END request for the same thread in debuggee. After
     * thread starts, the test expects to receive all expected THREAD_START events.
     * Then, after thread finishes, the test expects to receive all expected 
     * THREAD_END events.    
     */
    public Result  testEvent015() {

        logWriter.println("==> testEvent015: START (" + new Date() + ")...");
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
        String testedThreadName = "EventDebuggee015_Thread";
        logWriter.println(" ");
        logWriter.println("==> Wait for debuggee to create '" + testedThreadName + "'...");
        String debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
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
        
        logWriter.println("==> Get testedThreadID value for '" + testedThreadName + "'...");
        long  testedThreadID =
            getObjectIDValueForStaticField(debuggeeRefTypeID, "eventDebuggee015Thread");
        if ( testedThreadID == BAD_OBJECT_ID ) {
            logWriter.println("## FAILURE: Can NOT get testedThreadID");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get testedThreadID");
        }
        logWriter.println("==> testedThreadID = " + testedThreadID);
        
        logWriter.println(" ");
        logWriter.println("==> Prepare and send requests for THREAD_START and THREAD_END events " +
                "for '" + testedThreadName + "'...");
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        
        int[] threadStartRequestIDs = new int[EVENT_REQUESTS_NUMBER];
        int threadStartRequests = 0;
        int[] threadEndRequestIDs = new int[EVENT_REQUESTS_NUMBER];
        int threadEndRequests = 0;

        for (int i=0; i < EVENT_REQUESTS_NUMBER; i++) {
            threadStartRequestIDs[i] = NO_REQUEST_ID;
            threadEndRequestIDs[i] = NO_REQUEST_ID;
        }
        int testCaseStatus = SUCCESS;
        boolean outOfMemory = false;
        for (int i=0; i < EVENT_REQUESTS_NUMBER; i++) {
            ReplyPacket setEventReply = setEventRequest(JDWPConstants.EventKind.THREAD_START,
                JDWPConstants.SuspendPolicy.EVENT_THREAD, testedThreadID, NO_CLASS_ID, currentTimeout());
            if ( setEventReply == null ) {
                testCaseStatus = FAILURE;
                logWriter.println("## FAILURE: Can NOT set event request:");
                logWriter.println("##          Event kind = " + JDWPConstants.EventKind.THREAD_START +
                        "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.THREAD_START));
                logWriter.println("##          Thread name = " + testedThreadName);
                break;   
            } else {
                if ( checkReplyForError(setEventReply, expectedErrors,
                    "EventRequest.Set command for THREAD_START event") ) {
                    logWriter.println("##          Thread name = " + testedThreadName);
                    testCaseStatus = FAILURE;
                    break;
                }
                if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                        "EventRequest.Set command for THREAD_START event") ) {
                    logWriter.println("##          Thread name = " + testedThreadName);
                    outOfMemory = true;
                    break;
                }
                threadStartRequestIDs[i] = setEventReply.getNextValueAsInt();
                threadStartRequests++;
            }

            setEventReply = setEventRequest(JDWPConstants.EventKind.THREAD_END,
                JDWPConstants.SuspendPolicy.EVENT_THREAD, testedThreadID, NO_CLASS_ID, currentTimeout());
            if ( setEventReply == null ) {
                testCaseStatus = FAILURE;
                logWriter.println("## FAILURE: Can NOT set event request:");
                logWriter.println("##          Event kind = " + JDWPConstants.EventKind.THREAD_END +
                        "(" + JDWPConstants.EventKind.getName(JDWPConstants.EventKind.THREAD_END));
                logWriter.println("##          Thread name = " + testedThreadName);
                break;   
            } else {
                if ( checkReplyForError(setEventReply, expectedErrors,
                    "EventRequest.Set command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + testedThreadName);
                    testCaseStatus = FAILURE;
                    break;
                }
                if ( printExpectedError(setEventReply, JDWPConstants.Error.OUT_OF_MEMORY,
                        "EventRequest.Set command for THREAD_END event") ) {
                    logWriter.println("##          Thread name = " + testedThreadName);
                    outOfMemory = true;
                    break;
                }
                threadEndRequestIDs[i] = setEventReply.getNextValueAsInt();
                threadEndRequests++;
            }
        }

        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
            ("==> Time (mlsecs) of preparing requests for THREAD_START and THREAD_END events = " + 
            measurableCodeRunningTimeMlsec);
        logWriter.println("==> Successful requests for THREAD_START event = " + threadStartRequests);
        logWriter.println("==> Successful requests for THREAD_END event = " + threadEndRequests);
        if ( outOfMemory ) {
            logWriter.println
            ("==> OutOfMemory while sending requests for THREAD_START and THREAD_END events " +
            "- it is expected result! ");
            logWriter.println("==> testEvent015 is broken off! ");
            terminateDebuggee(SUCCESS, "MARKER_07");
            return passed
                ("==> OutOfMemory while sending requests for THREAD_START and THREAD_END events " +
                "- Expected result! ");
        }
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("## FAILURE while sending requests for THREAD_START and THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_08");
            return failed
            ("## FAILURE while sending requests for THREAD_START and THREAD_END events! ");
        }
        
        logWriter.println
        ("==> Resume debuggee to allow created thread '" + testedThreadName + "' to start...");
        resumeDebuggee("#2");

        logWriter.println(" ");
        logWriter.println("==> Wait for created '" + testedThreadName + "' start...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_08.01");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        logWriter.println
        ("==> Check that all expected THREAD_START events for all requests are received from JDWP agent...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        int receivedThreadStartEvents = 0;
        byte expectedEventKind = JDWPConstants.EventKind.THREAD_START;
        for (int i=0; i < EVENT_REQUESTS_NUMBER; i++) {
            ParsedEvent receivedEvent = receiveEvent(threadStartRequestIDs[i], NO_EVENT_KIND, 
                    NO_THREAD_ID, currentTimeout());
            if ( receivedEvent == null ) {
                testCaseStatus = FAILURE;
                logWriter.println
                ("## FAILURE: Can NOT receive expected THREAD_START event for: Thread name = " +
                        testedThreadName + "; RequestID = " + threadStartRequestIDs[i]);
                break;   
            }
            byte receivedEventKind = receivedEvent.getEventKind();
            if ( receivedEventKind != expectedEventKind ) {
                logWriter.println("## FAILURE: Unexpected event is received for given RequestId:");
                logWriter.println("##          Civen RequestId = " + threadStartRequestIDs[i]);
                logWriter.println("##          Received event kind = " + receivedEventKind +
                        "(" + JDWPConstants.EventKind.getName(receivedEventKind) + ")");
                logWriter.println("##          Expected event kind = " + expectedEventKind +
                        "(" + JDWPConstants.EventKind.getName(expectedEventKind) + ")");
                testCaseStatus = FAILURE;
                break;
            }

            long receivedThreadID = ((Event_THREAD_START)receivedEvent).getThreadID();
            String receivedThreadName = getThreadName(receivedThreadID);
            if ( receivedThreadName == null ) {
                receivedThreadName = "Unknown_Thread_Name";
            }
            if ( receivedThreadID != testedThreadID ) {
                logWriter.println("## FAILURE: Received " + 
                    JDWPConstants.EventKind.getName(expectedEventKind) + " event has unexpected threadID:");
                logWriter.println("##          RequestId = " + threadStartRequestIDs[i]);
                logWriter.println("##          Received threadID = " + receivedThreadID +
                        "(; Thread name = '" + receivedThreadName + "')");
                logWriter.println("##          Expected threadID = " + testedThreadID +
                        "(; Thread name = '" + testedThreadName + "')");
                testCaseStatus = FAILURE;
                break;
            } else {
                if ( ! testedThreadName.equals(receivedThreadName) ) {
                    logWriter.println("## FAILURE: Thread for received " + 
                        JDWPConstants.EventKind.getName(expectedEventKind) + " event has unexpected name:");
                    logWriter.println("##          RequestId = " + threadStartRequestIDs[i]);
                    logWriter.println("##          Received thread name = '" + receivedThreadName + "'");
                    logWriter.println("##          Expected thread name = '" + testedThreadName + "'");
                    testCaseStatus = FAILURE;
                    break;
                }
            }
            receivedThreadStartEvents++;
        }
        logWriter.println
        ("==> Number of successful received THREAD_START events = " + receivedThreadStartEvents );
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of receiving THREAD_START events = " + 
                measurableCodeRunningTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println
            ("## FAILURE while receiving THREAD_START events!");
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed("## FAILURE while receiving THREAD_START events! ");
        } else {
            logWriter.println("==> OK - all expected THREAD_START events are received!");
        }

        logWriter.println
        ("==> Resume debuggee to allow created thread '" + testedThreadName + "' to finish...");
        resumeDebuggee("#3");
        
        logWriter.println(" ");
        logWriter.println("==> Wait for debuggee to send signal to created thread '" + testedThreadName + "' to finish...");
        debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_10");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        
        logWriter.println
        ("==> Check that all expected THREAD_END events for all requests are received from JDWP agent...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        int receivedThreadEndEvents = 0;
        expectedEventKind = JDWPConstants.EventKind.THREAD_END;
        for (int i=0; i < EVENT_REQUESTS_NUMBER; i++) {
            ParsedEvent receivedEvent = receiveEvent(threadEndRequestIDs[i], NO_EVENT_KIND, 
                    NO_THREAD_ID, currentTimeout());
            if ( receivedEvent == null ) {
                testCaseStatus = FAILURE;
                logWriter.println
                ("## FAILURE: Can NOT receive expected THREAD_END event for: Thread name = " +
                        testedThreadName + "; RequestID = " + threadEndRequestIDs[i]);
                break;   
            }
            byte receivedEventKind = receivedEvent.getEventKind();
            if ( receivedEventKind != expectedEventKind ) {
                logWriter.println("## FAILURE: Unexpected event is received for given RequestId:");
                logWriter.println("##          Civen RequestId = " + threadEndRequestIDs[i]);
                logWriter.println("##          Received event kind = " + receivedEventKind +
                        "(" + JDWPConstants.EventKind.getName(receivedEventKind) + ")");
                logWriter.println("##          Expected event kind = " + expectedEventKind +
                        "(" + JDWPConstants.EventKind.getName(expectedEventKind) + ")");
                testCaseStatus = FAILURE;
                break;
            }

            long receivedThreadID = ((Event_THREAD_DEATH)receivedEvent).getThreadID();
            String receivedThreadName = getThreadName(receivedThreadID);
            if ( receivedThreadName == null ) {
                receivedThreadName = "Unknown_Thread_Name";
            }
            if ( receivedThreadID != testedThreadID ) {
                logWriter.println("## FAILURE: Received " + 
                    JDWPConstants.EventKind.getName(expectedEventKind) + " event has unexpected threadID:");
                logWriter.println("##          RequestId = " + threadStartRequestIDs[i]);
                logWriter.println("##          Received threadID = " + receivedThreadID +
                        "(; Thread name = '" + receivedThreadName + "'");
                logWriter.println("##          Expected threadID = " + testedThreadID +
                        "(; Thread name = '" + testedThreadName + "'");
                testCaseStatus = FAILURE;
                break;
            } else {
                if ( ! testedThreadName.equals(receivedThreadName) ) {
                    logWriter.println("## FAILURE: Thread for received " + 
                        JDWPConstants.EventKind.getName(expectedEventKind) + " event has unexpected name:");
                    logWriter.println("##          RequestId = " + threadStartRequestIDs[i]);
                    logWriter.println("##          Received thread name = '" + receivedThreadName + "'");
                    logWriter.println("##          Expected thread name = '" + testedThreadName + "'");
                    testCaseStatus = FAILURE;
                    break;
                }
            }
            receivedThreadEndEvents++;
        }
        logWriter.println
        ("==> Number of successful received THREAD_END events = " + receivedThreadEndEvents );
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of receiving THREAD_END events = " + 
                measurableCodeRunningTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println
            ("## FAILURE while receiving THREAD_END events!");
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed("## FAILURE while receiving THREAD_START events! ");
        } else {
            logWriter.println("==> OK - all expected THREAD_END events are received!");
        }

        logWriter.println(" ");
        logWriter.println("==> Resume debuggee to allow it to finish...");
        resumeDebuggee("#4");

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
        logWriter.println("==> testEvent015: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testEvent015: FAILED");
            return failed("testEvent015:");
        } else {
            logWriter.println("==> testEvent015: OK");
            return passed("testEvent015: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testEvent015: FAILED");
    return failed("==> testEvent015: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new EventTest015().test(args));
	}
}
