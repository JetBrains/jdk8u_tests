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
 * Created on 03.11.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.FRAME002;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.Location;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;


/**
 * This tests case exercises the JDWP commands: VirtualMachine.AllThreads,
 * ThreadReference.Frames and StackFrame.ThisObject for very big number
 * of threads running in debuggee and under memory stress in debuggee.
 */

public class FrameTest002 extends StressTestCase {

    public final static String DEBUGGEE_CLASS_NAME = "org.apache.harmony.test.stress.jpda.jdwp.scenario.FRAME002.FrameDebuggee002";

    public final static String DEBUGGEE_SIGNATURE = "L"
            + DEBUGGEE_CLASS_NAME.replace('.', '/') + ";";

    public final static String THREAD_CLASS_NAME = "org.apache.harmony.test.stress.jpda.jdwp.scenario.FRAME002.FrameDebuggee002_Thread";

    public final static String THREAD_CLASS_SIGNATURE = "L"
            + THREAD_CLASS_NAME.replace('.', '/') + ";";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }

    /**
     * This tests case exercises the JDWP commands: VirtualMachine.AllThreads,
     * ThreadReference.Frames and StackFrame.ThisObject for very big number
     * of threads running in debuggee and under memory stress in debuggee.
     * The test's steps:
     * - Debuggee: creates separate thread and starts it big number of times -
     *    each thread runs some recursive method some number of times
     *    and then it is suspended by some event; 
     * - Debugger: runs VirtualMachine.AllThreads command;
     *    - for all started and suspended threads runs ThreadReference.Frames command;
     *    - checks if all returned locations contain expected ClassID and MethodID;
     *    - checks if StackFrame.ThisObject commands return expected ObjectID or
     *      null for static methods;
     * - Debugger: resumes some threads which continue to run recursive methods some more
     *    times and wait for some signal to continue and to be suspended again;
     *    - checks if for all resumed treads ThreadReference.Frames command for
     *      causes THREAD_NOT_SUSPENDED;
     *    - checks if for all resumed treads StackFrame.ThisObject command 
     *      causes THREAD_NOT_SUSPENDED;
     * - Debuggee: Creates memory stress until OutOfMemory;
     * - Debuggee: sends to each resumed thread signal to continue and be suspeneded again;
     * - Debugger: for all threads runs ThreadReference.Frames command again and checks that:
     *    - all returned FrameIDs for all threads have unique values as before;
     *    - for all threads which were not resumed returned frameIDs are the same as before;
     *    - returned locations are correct as before;
     *    - for all threads StackFrame.ThisObject command for all
     *      FrameIDs returns the correct ObjectIDs as before;
     *
     * Expected results: 
     *    - NO errors excluding OutOfMemory and expected errors;
     */
    public Result testFrame002() {

        logWriter.println("==> testFrame002: START (" + new Date() + ")...");
        if (waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if ( setupThreadSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupThreadSignalWithWait.");
        }
        if ( setupSuspendThreadByEvent() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed("## FAILURE while setupSuspendThreadByEvent.");
        }
        resumeDebuggee("#1");
try {
        logWriter.println("==> Get debuggeeRefTypeID...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        logWriter.println("\n");
        logWriter.println
        ("==> Wait for debuggee to create and run threads with recursive methods ('SIGNAL_READY_01')...");

        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_01' Thread Signal from debuggee...");
        String debuggeeSignal =  receiveThreadSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_04");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Thread Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Thread Signal = " + debuggeeSignal);
        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for debuggee to start all threads = " + measurableCodeRunningTimeMlsec);

        logWriter.println("==> Get threadRefTypeID...");
        long threadRefTypeID = debuggeeWrapper.vmMirror.getClassID(THREAD_CLASS_SIGNATURE);
        if ( threadRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get threadRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get threadRefTypeID!");
        }
        logWriter.println("==> threadRefTypeID = " + threadRefTypeID);

        String staticCheckedMethod = "staticThreadMethod";
        long staticCheckedMethodID = 
            debuggeeWrapper.vmMirror.getMethodID(threadRefTypeID, staticCheckedMethod);
        if ( staticCheckedMethodID == -1 ) {
            logWriter.println
            ("## FAILURE: Can NOT get staticCheckedMethodID = " 
                    + "; Method name = " + staticCheckedMethod);
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## Can NOT get staticCheckedMethodID!");
        }
        logWriter.println("==> staticCheckedMethodID = " + staticCheckedMethodID);
        String instanceCheckedMethod = "recursiveMethod";
        long instanceCheckedMethodID = 
            debuggeeWrapper.vmMirror.getMethodID(threadRefTypeID, instanceCheckedMethod);
        if ( instanceCheckedMethodID == -1 ) {
            logWriter.println
            ("## FAILURE: Can NOT get instanceCheckedMethodID = " 
                    + "; Method name = " + instanceCheckedMethod);
            terminateDebuggee(FAILURE, "MARKER_07");
            return failed("## Can NOT get instanceCheckedMethodID!");
        }
        logWriter.println("==> instanceCheckedMethodID = " + instanceCheckedMethodID);

        logWriter.println("==> Get startedThreadsNumber value from debuggee...");
        int startedThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "startedThreadsNumber");
        if ( startedThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_08");
            return failed("## Can NOT get startedThreadsNumber");
        }
        logWriter.println("==> startedThreadsNumber = " + startedThreadsNumber);
        int threadsNumberToResume = startedThreadsNumber/2;
        logWriter.println("==> threadsNumberToResume = " + threadsNumberToResume);
        
        logWriter.println("\n");
        logWriter.println
        ("==> Send VirtualMachine.AllThreads after big number of threads started in debuggee...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllThreadsCommand);
         measurableCodeStartTimeMlsec = System.currentTimeMillis();
        ReplyPacket reply = performCommand(packet);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Command running time(mlsecs) = " + measurableCodeRunningTimeMlsec);
        
        if ( isException != null ) {
            logWriter.println("## Command - VirtualMachine.AllThreads");
            terminateDebuggee(FAILURE, "MARKER_09");
            return failed("## FAILURE in VirtualMachine.AllThreads command! ");
        }
        if ( isOutOfMemory ) {
            logWriter.println("==> Command - VirtualMachine.AllThreads");
            terminateDebuggee(SUCCESS, "MARKER_09.1");
            return passed("==> OUT_OF_MEMORY - Expected result! ");
        }
        if ( checkReplyForError(reply, JDWPConstants.Error.NONE,
                "VirtualMachine.AllThreads command") ) {
            terminateDebuggee(FAILURE, "MARKER_09.2");
            return failed
            ("## FAILURE in VirtualMachine.AllThreads command! ");
        }

        logWriter.println("\n");
        logWriter.println
        ("==> Check that all expected threadIDs are found out among returned by AllThreads command...");

        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        
        String[] startedThreadsNames = new String[startedThreadsNumber];
        long[] startedThreadsIDs = new long[startedThreadsNumber];
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsNames[i] = FrameDebuggee002.THREAD_NAME_PATTERN + i;
            startedThreadsIDs[i] = NO_THREAD_ID;
        }
        int allThreads = reply.getNextValueAsInt();
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
        int testCaseStatus = SUCCESS;
        limitedPrintlnInit(10);
        for (int i=0; i < startedThreadsNumber; i++) {
            if ( startedThreadsIDs[i] == NO_THREAD_ID ) {
                limitedPrintln("## FAILURE: Can NOT find threadID for thread = " +
                        startedThreadsNames[i]);
                testCaseStatus = FAILURE;
            }
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println("==> Time(mlsecs) of checking for all expected threads= " + 
                measurableCodeRunningTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_11");
            return failed
            ("## FAILURE: NOT all of the expected threadIDs are found out! ");
        }
        logWriter.println
        ("==> OK - all expected threadIDs are found!");
            
        logWriter.println("\n");
        logWriter.println("==> Wait for suspending by event of all expected threads(" + 
                startedThreadsNumber + ")...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        for (int i=0; i < startedThreadsNumber; i++) {
            long waitResult = waitForSuspendThreadByEvent(startedThreadsIDs[i], currentTimeout());
            if ( waitResult == FAILURE ) {
                logWriter.println("## FAILURE while waiting for suspend thread = " +
                        startedThreadsNames[i]);
                terminateDebuggee(FAILURE, "MARKER_12");
                return failed("## FAILURE while waiting for suspending of all started threads!");
            }
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println("==> Time(mlsecs) of waiting for suspending of all started threads= " + 
                measurableCodeRunningTimeMlsec);
        logWriter.println("==> OK - all expected threads are suspended by event!");

        logWriter.println("\n");
        logWriter.println("==> Run ThreadReference.Frames commands for threads (" +
                startedThreadsNumber + ") and check results...");

        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        int[] framesPerThreadsNumber = new int[startedThreadsNumber];
        long[][] threadsFrameIDs = new long[startedThreadsNumber][];
        limitedPrintlnInit(10);
        boolean frameIDsUniquenessFaliure = false;
        boolean toCheckFrameIDsUniqueness = false;
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        for (int i = 0; i < startedThreadsNumber; i++) {
            packet = new CommandPacket(
                    JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                    JDWPCommands.ThreadReferenceCommandSet.FramesCommand);
            packet.setNextValueAsThreadID(startedThreadsIDs[i]);
            packet.setNextValueAsInt(0);
            packet.setNextValueAsInt(-1);
            reply = performCommand(packet, currentTimeout());
            
            framesPerThreadsNumber[i] = 0;
            threadsFrameIDs[i] = null;
            if ( isException != null ) {
                logWriter.println("## Command - ThreadReference.Frames");
                logWriter.println("## Thread name = " + startedThreadsNames[i]);
                terminateDebuggee(FAILURE, "MARKER_13");
                return failed("## FAILURE in ThreadReference.Frames command! ");
            }
            if ( isOutOfMemory ) {
                logWriter.println("==> Command - ThreadReference.Frames");
                logWriter.println("==> Thread name = " + startedThreadsNames[i]);
                terminateDebuggee(SUCCESS, "MARKER_13.1");
                if ( testCaseStatus == FAILURE ) {
                    return failed("## FAILUREs were found out! ");
                } else {
                    return passed("==> OUT_OF_MEMORY - Expected result! ");
                }
            }
            if ( checkReplyForError(reply, JDWPConstants.Error.NONE,
                    "ThreadReference.Frames command", LIMITED_PRINT) ) {
                if ( lastLimitedPrintlnOK ) {
                    logWriter.println("## Thread name = " + startedThreadsNames[i]);
                }
                testCaseStatus = FAILURE;
                continue;
            }
            int frames = reply.getNextValueAsInt();
            if ( frames == 0 ) {
                limitedPrintln
                ("## FAILURE: ThreadReference.Frames command returns unexpected number of frames = 0");
                testCaseStatus = FAILURE;
                continue;
            }
            threadsFrameIDs[i] = new long[frames];
            int foundStaticCheckedMethods = 0;
            int foundInstanceCheckedMethods = 0;
            for (int j = 0; j < frames; j++) {
                long frameID = reply.getNextValueAsFrameID();
                // check that current frameID has unique value
                for (int k = 0; k <= i; k++) {
                    if ( ! toCheckFrameIDsUniqueness ) {
                        break;   
                    }
                    if ( threadsFrameIDs[k] == null ) {
                        continue;   
                    }
                    boolean toBreak = false;
                    for (int m = 0; m < framesPerThreadsNumber[k]; m++) {
                        if ( threadsFrameIDs[k][m] == frameID ) {
                            if ( limitedPrintln("## FAILURE: Uniqueness of frameIDs is broken:") ) {
                                logWriter.println("##          Not unique frameID value = " + frameID);
                                logWriter.println("##          First thread name = " + startedThreadsNames[k]);
                                logWriter.println("##          First threadID = " + startedThreadsIDs[k]);
                                logWriter.println("##          Second thread name = " + startedThreadsNames[i]);
                                logWriter.println("##          Second threadID = " + startedThreadsIDs[i]);
                            }
                            frameIDsUniquenessFaliure = true;
                            toBreak = true;
                            break;   
                        }
                    }
                    if ( toBreak ) {
                        break;   
                    }
                }
                threadsFrameIDs[i][j] = frameID;
                framesPerThreadsNumber[i]++;
                Location location = reply.getNextValueAsLocation();
                long locationClassID = location.classID;
                long locationMethodID = location.methodID;

                String methodName = getMethodName(locationClassID, locationMethodID);
                if ( methodName == null ) {
                    limitedPrintln("## FAILURE: Can NOT get method name for thread = " +
                        startedThreadsNames[i] + "; frameID = " + frameID + "; methodID = " + 
                        locationMethodID);
                    testCaseStatus = FAILURE;
                    continue;
                }
                if ( methodName == OUT_OF_MEMORY ) {
                    logWriter.println("==> OutOfMemory while getting method name for Thread name = " +
                            startedThreadsNames[i]);
                    terminateDebuggee(SUCCESS, "MARKER_14");
                    if ( testCaseStatus == FAILURE ) {
                        return failed
                        ("## FAILUREs were found out! ");
                    }
                    return passed
                    ("==> OutOfMemory while getting method name - Expected result!");
                }
                long expectedMethodID = 0;
                if ( ! methodName.equals(staticCheckedMethod) ) {
                    if ( ! methodName.equals(instanceCheckedMethod) ) {
                        // do not check
                        continue;
                    } else {
                        foundInstanceCheckedMethods++;
                        expectedMethodID = instanceCheckedMethodID; 
                    }
                } else {
                    foundStaticCheckedMethods++;
                    expectedMethodID = staticCheckedMethodID; 
                }
                if ( locationClassID != threadRefTypeID) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected classID in Location returned " +
                            " by ThreadReference.Frames command for thread = " +
                            startedThreadsNames[i] + "; frameID = " + frameID);
                    if ( toPrn ) {
                        logWriter.println("##          Returned classID = " + locationClassID);
                        logWriter.println("##          Expected classID = " + threadRefTypeID);
                    }
                    testCaseStatus = FAILURE;
                }

                if ( locationMethodID != expectedMethodID) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected methodID in Location returned " +
                            " by ThreadReference.Frames command for thread = " +
                            startedThreadsNames[i] + "; frameID = " + frameID);
                    if ( toPrn ) {
                        logWriter.println("##          Returned methodID = " + locationMethodID);
                        logWriter.println("##          Expected methodID = " + expectedMethodID);
                    }
                    testCaseStatus = FAILURE;
                }

                packet = new CommandPacket(
                        JDWPCommands.StackFrameCommandSet.CommandSetID,
                        JDWPCommands.StackFrameCommandSet.ThisObjectCommand);
                packet.setNextValueAsThreadID(startedThreadsIDs[i]);
                packet.setNextValueAsFrameID(frameID);
                ReplyPacket currentReply = performCommand(packet, currentTimeout());
                if ( isException != null ) {
                    logWriter.println("## Command - StackFrame.ThisObject");
                    logWriter.println("## Thread name = " + startedThreadsNames[i]);
                    logWriter.println("## Frames' number = " + j);
                    terminateDebuggee(FAILURE, "MARKER_15");
                    return failed("## FAILURE in StackFrame.ThisObject command! ");
                }
                if ( isOutOfMemory ) {
                    logWriter.println("==> Command - StackFrame.ThisObject");
                    logWriter.println("==> Thread name = " + startedThreadsNames[i]);
                    logWriter.println("==> Frames' number = " + j);
                    terminateDebuggee(SUCCESS, "MARKER_15.1");
                    if ( testCaseStatus == FAILURE ) {
                        return failed("## FAILUREs were found out! ");
                    } else {
                        return passed("==> OUT_OF_MEMORY - Expected result! ");
                    }
                }
                if ( checkReplyForError(currentReply, JDWPConstants.Error.NONE,
                        "StackFrame.ThisObject command", LIMITED_PRINT) ) {
                    if ( lastLimitedPrintlnOK ) {
                        logWriter.println("## Thread name = " + startedThreadsNames[i]);
                        logWriter.println("## Frames' number = " + j);
                    }
                    testCaseStatus = FAILURE;
                    continue;
                }
                long expectedThisObjectID = 0;
                if ( expectedMethodID == instanceCheckedMethodID ) {
                    expectedThisObjectID = startedThreadsIDs[i];
                }
                byte thisObjectTag = currentReply.getNextValueAsByte(); 
                long returnedThisObjectID = currentReply.getNextValueAsObjectID(); 
                if ( returnedThisObjectID != expectedThisObjectID) {
                    boolean toPrn = limitedPrintln("## FAILURE: StackFrame.ThisObject returns unexpected " +
                            " objectID for thread = " +
                            startedThreadsNames[i] + "; frameID = " + frameID);
                    if ( toPrn ) {
                        logWriter.println("##          Returned objectID = " + returnedThisObjectID);
                        logWriter.println("##          Expected objectID = " + expectedThisObjectID);
                    }
                    testCaseStatus = FAILURE;
                }
            }
            if ( foundStaticCheckedMethods == 0 ) {
                limitedPrintln("## FAILURE: Frames for method = '" + staticCheckedMethod +
                        "()' for thread = " +
                        startedThreadsNames[i] + " are not found out!");
                testCaseStatus = FAILURE;
            }
            if ( foundInstanceCheckedMethods == 0 ) {
                limitedPrintln("## FAILURE: Frames for method = '" + instanceCheckedMethod +
                        "()' for thread = " +
                        startedThreadsNames[i] + " are not found out!");
                testCaseStatus = FAILURE;
            }
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of running ThreadReference.Frames commands and checking results = " + 
                measurableCodeRunningTimeMlsec);
        if ( testCaseStatus != FAILURE ) {
            if ( ! frameIDsUniquenessFaliure ) {
                logWriter.println("==> OK - all ThreadReference.Frames commands give expected results!");
            }
        } else {
            terminateDebuggee(FAILURE, "MARKER_16");
            return failed
            ("## FAILUREs while run ThreadReference.Frames commands first time! ");
        }

        logWriter.println("\n");
        logWriter.println("==> Resume some threads (" + threadsNumberToResume + ")...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        
        for (int i=0; i < threadsNumberToResume; i++) {
            reply = resumeThread(startedThreadsIDs[i], null);
            if ( checkReplyForError(reply, expectedErrors,
                    "ThreadReference.Resume command") ) {
                terminateDebuggee(FAILURE, "MARKER_17");
                return failed("## FAILUREs while running ThreadReference.Resume command! ");
            }
            if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "ThreadReference.Resume command") ) {
                logWriter.println("==> Thread name = " +
                        startedThreadsNames[i]);
                terminateDebuggee(SUCCESS, "MARKER_18");
                return passed
                ("==> OutOfMemory while running ThreadReference.Resume command - Expected result!");
            }
        }
        
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of resuming some threads = " + measurableCodeRunningTimeMlsec);
        
        logWriter.println("\n");
        logWriter.println("==> Run ThreadReference.Frames and StackFrame.ThisObject commands " +
                " for all resumed threads (" + threadsNumberToResume + ") - errors should be returned...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        
        int[] expectedErrors_2 = 
            {JDWPConstants.Error.THREAD_NOT_SUSPENDED, JDWPConstants.Error.OUT_OF_MEMORY};
        int[] expectedErrors_3 = 
        {JDWPConstants.Error.INVALID_FRAMEID, JDWPConstants.Error.OUT_OF_MEMORY};
        for (int i=0; i < threadsNumberToResume; i++) {
            packet = new CommandPacket(
                    JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                    JDWPCommands.ThreadReferenceCommandSet.FramesCommand);
            packet.setNextValueAsThreadID(startedThreadsIDs[i]);
            packet.setNextValueAsInt(0);
            packet.setNextValueAsInt(-1);
            reply = performCommand(packet, currentTimeout());
            
            if ( isException != null ) {
                logWriter.println("## Command - ThreadReference.Frames");
                logWriter.println("## Thread name = " + startedThreadsNames[i]);
                terminateDebuggee(FAILURE, "MARKER_19");
                return failed("## FAILURE in ThreadReference.Frames command! ");
            }
            if ( isOutOfMemory ) {
                logWriter.println("==> Command - ThreadReference.Frames");
                logWriter.println("==> Thread name = " + startedThreadsNames[i]);
                terminateDebuggee(SUCCESS, "MARKER_19.1");
                return passed("==> OUT_OF_MEMORY in ThreadReference.Frames command - Expected result! ");
            }
            if ( checkReplyForError(reply, JDWPConstants.Error.THREAD_NOT_SUSPENDED,
                    "ThreadReference.Frames command") ) {
                logWriter.println("## Thread name = " + startedThreadsNames[i]);
                terminateDebuggee(FAILURE, "MARKER_20");
                return failed("## FAILURE in ThreadReference.Frames command! ");
            }

            packet = new CommandPacket(
                    JDWPCommands.StackFrameCommandSet.CommandSetID,
                    JDWPCommands.StackFrameCommandSet.ThisObjectCommand);
            packet.setNextValueAsThreadID(startedThreadsIDs[i]);
            packet.setNextValueAsFrameID(threadsFrameIDs[i][0]);
            reply = performCommand(packet, currentTimeout());
            if ( isException != null ) {
                logWriter.println("## Command - StackFrame.ThisObject");
                logWriter.println("## Thread name = " + startedThreadsNames[i]);
                logWriter.println("## Frames' number = " + 0);
                terminateDebuggee(FAILURE, "MARKER_21");
                return failed("## FAILURE in StackFrame.ThisObject command! ");
            }
            if ( isOutOfMemory ) {
                logWriter.println("==> Command - StackFrame.ThisObject");
                logWriter.println("==> Thread name = " + startedThreadsNames[i]);
                logWriter.println("==> Frames' number = " + 0);
                terminateDebuggee(SUCCESS, "MARKER_21.1");
                return passed("==> OUT_OF_MEMORY in StackFrame.ThisObject command - Expected result! ");
            }
            if ( checkReplyForError(reply, JDWPConstants.Error.THREAD_NOT_SUSPENDED,
                    "StackFrame.ThisObject command") ) {
                logWriter.println("## Thread name = " + startedThreadsNames[i]);
                logWriter.println("## Frames' number = " + 0);
                terminateDebuggee(FAILURE, "MARKER_22");
                return failed("## FAILURE in StackFrame.ThisObject command! ");
            }
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of running ThreadReference.Frames and StackFrame.ThisObject commands = " + 
                measurableCodeRunningTimeMlsec);
        logWriter.println("==> OK - all ThreadReference.Frames and StackFrame.ThisObject commands " +
        "return expected errors!");
 
        logWriter.println("\n");
        logWriter.println
        ("==> Resume debuggee main thread to create memory stress in debuggee...");
        reply = resumeSignalThread("#2");
        if ( checkReplyForError(reply, expectedErrors,
            "ThreadReference.Resume command") ) {
            terminateDebuggee(FAILURE, "MARKER_23");
            return failed("## FAILURE while resuming debuggee main thread! ");
        }
        if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                "ThreadReference.Resume command") ) {
            terminateDebuggee(SUCCESS, "MARKER_24");
            return passed
            ("==> OutOfMemory while resuming debuggee main thread - Expected result!");
        }

        logWriter.println("\n");
        logWriter.println("==> Wait for debugge to create memory stress...");

        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal = receiveThreadSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_02.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_25");
            return failed("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of waiting for debugge to create memory stress = " + 
                measurableCodeRunningTimeMlsec);

        logWriter.println("\n");
        logWriter.println
        ("==> Resume debuggee main thread to suspend by event all resumed threads again...");
        reply = resumeSignalThread("#3");
        if ( checkReplyForError(reply, expectedErrors,
            "ThreadReference.Resume command") ) {
            terminateDebuggee(FAILURE, "MARKER_26");
            return failed("## FAILURE while resuming debuggee main thread! ");
        }
        if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                "ThreadReference.Resume command") ) {
            terminateDebuggee(SUCCESS, "MARKER_27");
            return passed
            ("==> OutOfMemory while resuming debuggee main thread - Expected result!");
        }
        logWriter.println("\n");
        logWriter.println("==> Wait for suspending by event of all resumed threads (" +
                threadsNumberToResume + ")...");
        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        for (int i=0; i < threadsNumberToResume; i++) {
            long waitResult = waitForSuspendThreadByEvent(startedThreadsIDs[i], currentTimeout());
            if ( waitResult == FAILURE ) {
                logWriter.println("## FAILURE while waiting for suspend thread = " +
                        startedThreadsNames[i]);
                terminateDebuggee(FAILURE, "MARKER_28");
                return failed("## FAILURE while waiting for suspending of all resumed threads!");
            }
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println("==> Time(mlsecs) of waiting for suspending of all resumed threads= " + 
                measurableCodeRunningTimeMlsec);
        logWriter.println("==> OK - all resumed threads are suspended by event again!");

        logWriter.println("\n");
        logWriter.println
        ("==> Run again ThreadReference.Frames commands for all threads (" +
                startedThreadsNumber + ") and check results...");

        measurableCodeStartTimeMlsec = System.currentTimeMillis();
        limitedPrintlnInit(10);
        for (int i = 0; i < startedThreadsNumber; i++) {
            packet = new CommandPacket(
                    JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                    JDWPCommands.ThreadReferenceCommandSet.FramesCommand);
            packet.setNextValueAsThreadID(startedThreadsIDs[i]);
            packet.setNextValueAsInt(0);
            packet.setNextValueAsInt(-1);
            reply = performCommand(packet, currentTimeout());
            
            if ( i < threadsNumberToResume ) {
                framesPerThreadsNumber[i] = 0;
                threadsFrameIDs[i] = null;
            }
            
            if ( isException != null ) {
                logWriter.println("## Command - ThreadReference.Frames");
                logWriter.println("## Thread name = " + startedThreadsNames[i]);
                terminateDebuggee(FAILURE, "MARKER_29");
                return failed("## FAILURE in ThreadReference.Frames command! ");
            }
            if ( isOutOfMemory ) {
                logWriter.println("==> Command - ThreadReference.Frames");
                logWriter.println("==> Thread name = " + startedThreadsNames[i]);
                terminateDebuggee(SUCCESS, "MARKER_29.1");
                if ( testCaseStatus == FAILURE ) {
                    return failed("## FAILUREs were found out! ");
                } else {
                    return passed("==> OUT_OF_MEMORY - Expected result! ");
                }
            }
            if ( checkReplyForError(reply, JDWPConstants.Error.NONE,
                    "ThreadReference.Frames command", LIMITED_PRINT) ) {
                if ( lastLimitedPrintlnOK ) {
                    logWriter.println("## Thread name = " + startedThreadsNames[i]);
                }
                if ( i >= threadsNumberToResume ) {
                    if ( ! lastLimitedPrintlnOK ) {
                        checkReplyForError(reply, JDWPConstants.Error.NONE,
                                "ThreadReference.Frames command");
                        logWriter.println("## Thread name = " + startedThreadsNames[i]);
                    }
                    terminateDebuggee(FAILURE, "MARKER_29.2");
                    return failed("## FAILURE in ThreadReference.Frames command! ");
                }
                testCaseStatus = FAILURE;
                continue;
            }
            int frames = reply.getNextValueAsInt();
            if ( frames == 0 ) {
                if ( i >= threadsNumberToResume ) {
                    logWriter.println
                    ("## FAILURE: ThreadReference.Frames command returns unexpected number of frames = 0");
                    terminateDebuggee(FAILURE, "MARKER_29.3");
                    return failed("## FAILURE in ThreadReference.Frames command! ");
                }
                limitedPrintln
                ("## FAILURE: ThreadReference.Frames command returns unexpected number of frames = 0");
                testCaseStatus = FAILURE;
                continue;
            }
            if ( i < threadsNumberToResume ) {
                threadsFrameIDs[i] = new long[frames];
            } else {
                if ( framesPerThreadsNumber[i] != frames ) {
                    logWriter.println
                    ("## FAILURE: ThreadReference.Frames command returns unexpected number of frames" +
                            " for thread which was not resumed:");
                    logWriter.println("##      Thread name = " + startedThreadsNames[i]);
                    logWriter.println("##      Returned number of frames = " + frames);
                    logWriter.println("##      Expected number of frames = " + framesPerThreadsNumber[i]);
                    terminateDebuggee(FAILURE, "MARKER_29.4");
                    return failed("## FAILURE in ThreadReference.Frames command! ");
                }
            }
            int foundStaticCheckedMethods = 0;
            int foundInstanceCheckedMethods = 0;
            for (int j = 0; j < frames; j++) {
                long frameID = reply.getNextValueAsFrameID();
                // check that current frameID has unique value
                for (int k = 0; k <= i; k++) {
                    if ( threadsFrameIDs[k] == null ) {
                        continue;   
                    }
                    boolean toBreak = false;
                    for (int m = 0; m < framesPerThreadsNumber[k]; m++) {
                        if ( ! toCheckFrameIDsUniqueness ) {
                            break;   
                        }
                        if ( k == i ) {
                            if ( m == j ) {
                                break;   
                            }
                        }
                        if ( threadsFrameIDs[k][m] == frameID ) {
                            if ( limitedPrintln("## FAILURE: Uniqueness of frameIDs is broken:") ) {
                                logWriter.println("##          Not unique frameID value = " + frameID);
                                logWriter.println("##          First thread name = " + startedThreadsNames[k]);
                                logWriter.println("##          First threadID = " + startedThreadsIDs[k]);
                                logWriter.println("##          Second thread name = " + startedThreadsNames[i]);
                                logWriter.println("##          Second threadID = " + startedThreadsIDs[i]);
                            }
                            testCaseStatus = FAILURE;
                            toBreak = true;
                            break;   
                        }
                    }
                    if ( toBreak ) {
                        break;   
                    }
                }
                if ( i < threadsNumberToResume ) {
                    threadsFrameIDs[i][j] = frameID;
                    framesPerThreadsNumber[i]++;
                } else {
                    if ( threadsFrameIDs[i][j] != frameID ) {
                        logWriter.println
                        ("## FAILURE: ThreadReference.Frames command returns unexpected frameID" +
                                " for thread which was not resumed:");
                        logWriter.println("##      Thread name = " + startedThreadsNames[i]);
                        logWriter.println("##      Returned frameID = " + frameID);
                        logWriter.println("##      Expected frameID = " + threadsFrameIDs[i][j]);
                        terminateDebuggee(FAILURE, "MARKER_29.5");
                        return failed("## FAILURE in ThreadReference.Frames command! ");
                    }
                }
                Location location = reply.getNextValueAsLocation();
                long locationClassID = location.classID;
                long locationMethodID = location.methodID;

                String methodName = getMethodName(locationClassID, locationMethodID);
                if ( methodName == null ) {
                    limitedPrintln("## FAILURE: Can NOT get method name for thread = " +
                            startedThreadsNames[i] + "; frameID = " + frameID + "; methodID = " + 
                            locationMethodID);
                    testCaseStatus = FAILURE;
                    continue;
                }
                if ( methodName == OUT_OF_MEMORY ) {
                    logWriter.println("==> OutOfMemory while getting method name for Thread name = " +
                            startedThreadsNames[i]);
                    terminateDebuggee(SUCCESS, "MARKER_30");
                    if ( testCaseStatus == FAILURE ) {
                        return failed
                        ("## FAILUREs were found out! ");
                    }
                    return passed
                    ("==> OutOfMemory while getting method name - Expected result!");
                }
                long expectedMethodID = 0;
                if ( ! methodName.equals(staticCheckedMethod) ) {
                    if ( ! methodName.equals(instanceCheckedMethod) ) {
                        // do not check
                        continue;
                    } else {
                        foundInstanceCheckedMethods++;
                        expectedMethodID = instanceCheckedMethodID; 
                    }
                } else {
                    foundStaticCheckedMethods++;
                    expectedMethodID = staticCheckedMethodID; 
                }
                if ( locationClassID != threadRefTypeID) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected classID in Location returned " +
                            " by ThreadReference.Frames command for thread = " +
                            startedThreadsNames[i] + "; frameID = " + frameID);
                    if ( toPrn ) {
                        logWriter.println("##          Returned classID = " + locationClassID);
                        logWriter.println("##          Expected classID = " + threadRefTypeID);
                    }
                    testCaseStatus = FAILURE;
                }

                if ( locationMethodID != expectedMethodID) {
                    boolean toPrn = limitedPrintln("## FAILURE: Unexpected methodID in Location returned " +
                            " by ThreadReference.Frames command for thread = " +
                            startedThreadsNames[i] + "; frameID = " + frameID);
                    if ( toPrn ) {
                        logWriter.println("##          Returned methodID = " + locationMethodID);
                        logWriter.println("##          Expected methodID = " + expectedMethodID);
                    }
                    testCaseStatus = FAILURE;
                }

                packet = new CommandPacket(
                        JDWPCommands.StackFrameCommandSet.CommandSetID,
                        JDWPCommands.StackFrameCommandSet.ThisObjectCommand);
                packet.setNextValueAsThreadID(startedThreadsIDs[i]);
                packet.setNextValueAsFrameID(frameID);
                ReplyPacket currentReply = performCommand(packet, currentTimeout());
                if ( isException != null ) {
                    logWriter.println("## Command - TStackFrame.ThisObject");
                    logWriter.println("## Thread name = " + startedThreadsNames[i]);
                    logWriter.println("## Frames' number = " + j);
                    terminateDebuggee(FAILURE, "MARKER_31");
                    return failed("## FAILURE in StackFrame.ThisObject command! ");
                }
                if ( isOutOfMemory ) {
                    logWriter.println("==> Command = StackFrame.ThisObject");
                    logWriter.println("==> Thread name = " + startedThreadsNames[i]);
                    logWriter.println("==> Frames' number = " + j);
                    terminateDebuggee(SUCCESS, "MARKER_31.1");
                    if ( testCaseStatus == FAILURE ) {
                        return failed("## FAILUREs were found out! ");
                    } else {
                        return passed("==> OUT_OF_MEMORY - Expected result! ");
                    }
                }
                if ( checkReplyForError(currentReply, JDWPConstants.Error.NONE,
                        "StackFrame.ThisObject command", LIMITED_PRINT) ) {
                    if ( lastLimitedPrintlnOK ) {
                        logWriter.println("## Thread name = " + startedThreadsNames[i]);
                        logWriter.println("## Frames' number = " + j);
                    }
                    testCaseStatus = FAILURE;
                    continue;
                }
                long expectedThisObjectID = 0;
                if ( expectedMethodID == instanceCheckedMethodID ) {
                    expectedThisObjectID = startedThreadsIDs[i];
                }
                byte thisObjectTag = currentReply.getNextValueAsByte(); 
                long returnedThisObjectID = currentReply.getNextValueAsObjectID(); 
                if ( returnedThisObjectID != expectedThisObjectID) {
                    boolean toPrn = limitedPrintln("## FAILURE: StackFrame.ThisObject returns unexpected " +
                            " objectID for thread = " +
                            startedThreadsNames[i] + "; frameID = " + frameID);
                    if ( toPrn ) {
                        logWriter.println("##          Returned objectID = " + returnedThisObjectID);
                        logWriter.println("##          Expected objectID = " + expectedThisObjectID);
                    }
                    testCaseStatus = FAILURE;
                }
            }
            if ( foundStaticCheckedMethods == 0 ) {
                limitedPrintln("## FAILURE: Frames for method = '" + staticCheckedMethod +
                        "()' for thread = " +
                        startedThreadsNames[i] + " are not found out!");
                testCaseStatus = FAILURE;
            }
            if ( foundInstanceCheckedMethods == 0 ) {
                limitedPrintln("## FAILURE: Frames for method = '" + instanceCheckedMethod +
                        "()' for thread = " +
                        startedThreadsNames[i] + " are not found out!");
                testCaseStatus = FAILURE;
            }
        }
        measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println
        ("==> Time(mlsecs) of running ThreadReference.Frames commands and checking results = " + 
                measurableCodeRunningTimeMlsec);
        if ( testCaseStatus != FAILURE ) {
            logWriter.println("==> OK - all repeated ThreadReference.Frames commands give expected results!");
        }
        if ( frameIDsUniquenessFaliure ) {
            testCaseStatus = FAILURE;
        }
        
        resumeDebuggee("#4");

        logWriter.println("\n");
        logWriter.println("==> Wait for finish of all threads (" + startedThreadsNumber + ") in debuggee...");

        printlnForDebug("receiving 'SIGNAL_READY_03' Signal from debuggee...");
        debuggeeSignal = receiveThreadSignalWithWait(currentTimeout());
        if (!SIGNAL_READY_03.equals(debuggeeSignal)) {
            terminateDebuggee(FAILURE, "MARKER_32");
            return failed("## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee! ");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        resumeDebuggee("#5");

        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testFrame002: running time(mlsecs) = "
                + testRunTimeMlsec);
        if (testCaseStatus == FAILURE) {
            logWriter.println("==> testFrame002: FAILED");
            return failed("testFrame002:");
        } else {
            logWriter.println("==> testFrame002: OK");
            return passed("testFrame002: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testFrame002: FAILED");
    return failed("==> testFrame002: Unexpected Exception! ");
}
    }

    public static void main(String[] args) {
        System.exit(new FrameTest002().test(args));
    }
}