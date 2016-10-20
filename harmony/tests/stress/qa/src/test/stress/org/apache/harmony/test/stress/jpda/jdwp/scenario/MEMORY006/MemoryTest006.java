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
 * Created on 16.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY006;

import java.util.Date;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressTestCase;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;


/**
 * This test case exercises the JDWP command <code>VirtualMachine.AllThreads</code>
 * for very big number of threads started by debuggee. 
 */
public class MemoryTest006 extends StressTestCase {
    
    public final static String DEBUGGEE_CLASS_NAME = 
        "org.apache.harmony.test.stress.jpda.jdwp.scenario.MEMORY006.MemoryDebuggee006";
    public final static String DEBUGGEE_SIGNATURE = 
        "L" + DEBUGGEE_CLASS_NAME.replace('.','/') + ";";

	protected String getDebuggeeClassName() {
		return DEBUGGEE_CLASS_NAME;
	}

    /**
     * This test case exercises the JDWP command <code>VirtualMachine.AllThreads</code>
     * for very big number of threads started by debuggee. 
     */
    public Result  testMemory006() {

        logWriter.println("==> testMemory006: START (" + new Date() + ")...");
        if ( waitForDebuggeeClassLoad(DEBUGGEE_CLASS_NAME) == FAILURE ) {
            return failed("## FAILURE while DebuggeeClassLoad.");
        }
        if ( setupSignalWithWait() == FAILURE ) {
            terminateDebuggee(FAILURE, "MARKER_01");
            return failed("## FAILURE while setupSignalWithWait.");
        }
        resumeDebuggee("#1");
try {
        printlnForDebug("receiving 'SIGNAL_READY_01' Signal from debuggee...");
        String debuggeeSignal =  receiveSignalWithWait();
        if ( ! SIGNAL_READY_01.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_02");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_01' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        logWriter.println("\n");
        logWriter.println("==> Send VirtualMachine.AllThreads just after debuggee started...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllThreadsCommand);
        long commandStartTimeMlsec = System.currentTimeMillis();
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        long commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
        printlnForDebug("reply.getLength()= " + reply.getLength());

        int testCaseStatus = SUCCESS;

        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "VirtualMachine::AllThreads command") ) {
            testCaseStatus = FAILURE;
        } else {
            logWriter.println("\n");
            int threadsNumber = reply.getNextValueAsInt();
            logWriter.println("==> Received threads number = " + threadsNumber);
            try {
                for (int i = 0; i < threadsNumber; i++ ) {
                    long threadID = reply.getNextValueAsThreadID();
                }
            } catch ( Throwable thrown ) {
                logWriter.println("## FAILURE: Exception while reading thread IDs " + thrown);
                testCaseStatus = FAILURE;
            }
            if ( testCaseStatus != FAILURE ) {
                logWriter.println("==> VirtualMachine.AllThreads just after debuggee started - OK");
            }
        }
        resumeDebuggee("#2");

        logWriter.println("\n");
        logWriter.println("==> Wait for debuggee to create and start very big number of threads...");

        printlnForDebug("receiving 'SIGNAL_READY_02' Signal from debuggee...");
        debuggeeSignal =  receiveSignalWithWait();
        if ( ! SIGNAL_READY_02.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_02' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);

        logWriter.println("==> Get startedThreadsNumber value from debuggee...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            terminateDebuggee(FAILURE, "MARKER_04");
           return failed("## Can NOT get debuggeeRefTypeID");
        }
        printlnForDebug("debuggeeRefTypeID = " + debuggeeRefTypeID);

        int startedThreadsNumber =
            getIntValueForStaticField(debuggeeRefTypeID, "startedThreadsNumber");
        if ( startedThreadsNumber == BAD_INT_VALUE ) {
            logWriter.println("## FAILURE: Can NOT get startedThreadsNumber");
            terminateDebuggee(FAILURE, "MARKER_05");
            return failed("## Can NOT get startedThreadsNumber");
        }
        logWriter.println("==> startedThreadsNumber = " + startedThreadsNumber);
        
        // Create array of expected threads names which are started
        String[] startedThreadsNames = new String[startedThreadsNumber];
        boolean[] startedThreadsFound = new boolean[startedThreadsNumber];
        for (int i=0; i < startedThreadsNumber; i++) {
            startedThreadsNames[i] = MemoryDebuggee006.THREAD_NAME_PATTERN + i;
            startedThreadsFound[i] = false;
        }
        
        logWriter.println("\n");
        logWriter.println("==> Send VirtualMachine.AllThreads after creating and starting " + 
                "very big number of threads in debuggee...");
        packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllThreadsCommand);
        try {
            commandStartTimeMlsec = System.currentTimeMillis();
            reply = debuggeeWrapper.vmMirror.performCommand(packet, currentTimeout());
            commandRunTimeMlsec = System.currentTimeMillis() - commandStartTimeMlsec;
            logWriter.println("==> command running time(mlsecs) = " + commandRunTimeMlsec);
            
        } catch ( Throwable thrown ) {
            logWriter.println("## FAILURE: Exception while performCommand() = " + thrown);
            terminateDebuggee(FAILURE, "MARKER_06");
            return failed("## FAILURE: Exception while performCommand()");
        }
        int[] expectedErrors = {JDWPConstants.Error.NONE, JDWPConstants.Error.OUT_OF_MEMORY};
        if ( checkReplyForError(reply, expectedErrors,
                               "VirtualMachine::AllThreads command") ) {
            resumeDebuggee("#3");
            return failed("## ArrayReference::GetValues command ERROR");
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());
        if ( ! printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                                  "VirtualMachine::AllThreads command") ) {
            logWriter.println("\n");
            logWriter.println("==> Check if all expected threads are returned...");
            int checkStatus = SUCCESS;
            int allThreadsNumber = reply.getNextValueAsInt();
            logWriter.println("==> Received threads number = " + allThreadsNumber);
            for (int i = 0; i < allThreadsNumber; i++ ) {
                long threadID = reply.getNextValueAsThreadID();
                String threadName = getThreadName(threadID);
                for (int j = 0; j < startedThreadsNumber; j++ ) {
                    if ( startedThreadsNames[j].equals(threadName) ) {
                        startedThreadsFound[j] = true;
                    }
                }
            }
            limitedPrintlnInit(20);
            for (int i = 0; i < startedThreadsNumber; i++ ) {
                if ( ! startedThreadsFound[i] ) {
                    boolean toPrn = limitedPrintln("## FAILURE: Expected thread is NOT found out");
                    if ( toPrn ) {
                        logWriter.println("##          Thread name  = " + startedThreadsNames[i]);
                    }
                    checkStatus = FAILURE;
                }
            }
            if ( checkStatus != FAILURE ) {
                logWriter.println("==> Check if all expected threads are returned - OK.");
            } else {
                testCaseStatus = FAILURE;
            }
        }

        resumeDebuggee("#4");

        logWriter.println("\n");
        logWriter.println("==> Wait for finish of all threads in debuggee...");

        long measurableCodeStartTimeMlsec = System.currentTimeMillis();
        printlnForDebug("receiving 'SIGNAL_READY_03' Signal from debuggee...");
        debuggeeSignal =  receiveSignalWithWait(currentTimeout());
        if ( ! SIGNAL_READY_03.equals(debuggeeSignal) ) {
            terminateDebuggee(FAILURE, "MARKER_03");
            return failed
            ("## FAILURE while receiving 'SIGNAL_READY_03' Signal from debuggee");
        }
        printlnForDebug("received debuggee Signal = " + debuggeeSignal);
        long measurableCodeRunningTimeMlsec = System.currentTimeMillis() - measurableCodeStartTimeMlsec;
        logWriter.println("==> Time(mlsecs) of waiting for finish of all started threads in debuggee= " + 
                measurableCodeRunningTimeMlsec);

        resumeDebuggee("#5");

        long testRunTimeMlsec = System.currentTimeMillis() - testStartTimeMlsec;
        logWriter.println("==> testMemory006: running time(mlsecs) = " + testRunTimeMlsec);
        if ( testCaseStatus == FAILURE ) {
            logWriter.println("==> testMemory006: FAILED");
            return failed("testMemory006:");
        } else {
            logWriter.println("==> testMemory006: OK");
            return passed("testMemory006: OK");
        }
} catch (Throwable thrown) {
    logWriter.println("## FAILURE: Unexpected Exception:");
    printStackTraceToLogWriter(thrown);
    terminateDebuggee(FAILURE, "MARKER_999");
    logWriter.println("==> testMemory006: FAILED");
    return failed("==> testMemory006: Unexpected Exception! ");
}
    }

	public static void main(String[] args) {
        System.exit(new MemoryTest006().test(args));
	}
}
