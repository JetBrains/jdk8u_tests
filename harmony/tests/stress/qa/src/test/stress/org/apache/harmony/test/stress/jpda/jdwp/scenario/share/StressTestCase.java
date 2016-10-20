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
 * @author Anatoly F. Bondarenko, Aleksander V. Budniy
 * @version $Revision: 1.2 $
 */

/**
 * Created on 06.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.share;

import java.util.Vector;

import org.apache.harmony.test.stress.jpda.jdwp.share.JDWPQARawTestCase;
import org.apache.harmony.test.stress.jpda.jdwp.share.JDWPQADebuggeeWrapper;

import org.apache.harmony.share.framework.jpda.DebuggeeRegister;
import org.apache.harmony.share.framework.jpda.jdwp.ArrayRegion;
import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Event;
import org.apache.harmony.share.framework.jpda.jdwp.EventMod;
import org.apache.harmony.share.framework.jpda.jdwp.EventPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;
import org.apache.harmony.share.framework.jpda.jdwp.Value;
import org.apache.harmony.share.framework.jpda.jdwp.exceptions.ReplyErrorCodeException;
import org.apache.harmony.share.framework.jpda.jdwp.exceptions.TimeoutException;
import org.apache.harmony.share.framework.jpda.jdwp.ParsedEvent;
import org.apache.harmony.share.framework.jpda.jdwp.ParsedEvent.*;
import org.apache.harmony.share.framework.jpda.jdwp.Location;


/**
 * StressTestCase is super class for all tests classes of
 * JDWP stress tests
 */
public abstract class StressTestCase extends JDWPQARawTestCase {
    
    static final boolean TRIAL_RUN = false; // true - for trial runs; false - for actual runs
    
    // configurational values
    // Below values for actual runs, values for trial runs - see static initializer
    
    // EVENT tests
    static public int EVENT009_REQUESTS_NUMBER_FOR_EACH_THREAD = 10;

    static public int EVENT010_REQUESTS_NUMBER_FOR_EACH_THREAD = 10;
    
    static public int EVENT015_EVENT_REQUESTS_NUMBER = 500;
    
    static public int EVENT016_EVENT_REQUESTS_NUMBER_PER_THREAD = 150;

    static public int EVENT017_EVENT_REQUESTS_NUMBER_PER_THREAD = 150;

    // THREAD tests
    static public int THREAD001_COMMANDS_TO_RUN = 100;
    
    static public int THREAD003_COMMANDS_TO_RUN = 100;
    
    static public int THREAD005_COMMANDS_TO_RUN = 100;
    
    static public int THREAD007_COMMANDS_TO_RUN = 100;
    
    static public int THREAD009_COMMANDS_TO_RUN = 100;
    
    static public int THREAD011_COMMANDS_TO_RUN = 100;

    // END of configurational values
    
    static {
        if ( TRIAL_RUN ) {
            // configurational values
            // Below values for trial runs, values for actual runs - see declarations above
            
            // EVENT tests
            EVENT009_REQUESTS_NUMBER_FOR_EACH_THREAD = 2;

            EVENT010_REQUESTS_NUMBER_FOR_EACH_THREAD = 2;
            
            EVENT015_EVENT_REQUESTS_NUMBER = 4;
            
            EVENT016_EVENT_REQUESTS_NUMBER_PER_THREAD = 2;
            
            EVENT017_EVENT_REQUESTS_NUMBER_PER_THREAD = 2;

            // THREAD tests
            THREAD001_COMMANDS_TO_RUN = 4;
            
            THREAD003_COMMANDS_TO_RUN = 4;
            
            THREAD005_COMMANDS_TO_RUN = 4;
            
            THREAD007_COMMANDS_TO_RUN = 4;
            
            THREAD009_COMMANDS_TO_RUN = 4;
            
            THREAD011_COMMANDS_TO_RUN = 4;
        }
    }

    protected JDWPQADebuggeeWrapper debuggeeWrapper;
    protected EventPacket vmStartEvent;
    protected DebuggeeRegister debuggeeRegister;
    protected boolean server = true;
    
    protected final int SUCCESS = 0;
    protected final int FAILURE = -1;
    protected final int YES = 0;
    protected final int NO = -1;
    protected final String OUT_OF_MEMORY = "OUT_OF_MEMORY";
    
    protected final long BAD_ARRAY_ID = -1;
    protected final long BAD_OBJECT_ID = -1;
    protected final int BAD_INT_VALUE = Integer.MIN_VALUE;
    protected final long BAD_LONG_VALUE = Long.MIN_VALUE;

    protected long testStartTimeMlsec = Long.MAX_VALUE;
    protected long testTimeout = 0;

    // DEBUG_FLAG - turn on/off extra tests logging which may be
    // useful for tests runs evaluations
    protected int DEBUG_FLAG = YES;

    protected final boolean FOR_DEBUG = true;
    protected Throwable isException = null;
    protected boolean isTimeout = false;
    protected boolean isOutOfMemory = false;
    
    private Vector receivedParsedEvents = new Vector();

    private final String stressDebuggeeSignature =
        "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/share/StressDebuggee;";
    private final byte stressDebuggeeTypeTag = JDWPConstants.TypeTag.CLASS;
    private long stressDebuggeeRefTypeID = 0;

    protected final static String SIGNAL_FAILURE = "FAILURE";
    protected final static String SIGNAL_READY_01 = "READY_01";
    protected final static String SIGNAL_READY_02 = "READY_02";
    protected final static String SIGNAL_READY_03 = "READY_03";
    protected final static String SIGNAL_READY_04 = "READY_04";
    protected final static String SIGNAL_READY_05 = "READY_05";
    protected final static String SIGNAL_READY_06 = "READY_06";
    protected final static String SIGNAL_READY_07 = "READY_07";
    protected final static String SIGNAL_READY_08 = "READY_08";
    protected final static String SIGNAL_READY_09 = "READY_09";
    protected final static String SIGNAL_READY_10 = "READY_10";

    protected final static int NO_REQUEST_ID = 0;
    protected final static byte NO_EVENT_KIND = 0;
    protected final static long NO_THREAD_ID = 0;
    protected final static long ANY_THREAD = NO_THREAD_ID;
    protected final static long NO_CLASS_ID = 0;
    protected final static long ANY_CLASS = NO_CLASS_ID;
    
    private int signalWithWaitRequestID = NO_REQUEST_ID;
    private int threadSignalWithWaitRequestID = NO_REQUEST_ID;
    private long threadSignalWithWaitThreadID = NO_THREAD_ID;
    private int signalWithContinueRequestID = NO_REQUEST_ID;
    private int suspendThreadByEventRequestID = NO_REQUEST_ID;
    
    private int limitOfPrintln = 0;
    private int numberOfLimitedPrintln = 0;
    protected boolean lastLimitedPrintlnOK = true;
    protected final static boolean LIMITED_PRINT = true;
    protected final static boolean UNLIMITED_PRINT = false;
    
    public class RefTypeArray {
        private long[] array = new long[200000];

        private int length = 0;

        public long getRefType(int i) {
            return array[i];
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }

        public void setRefType(int i, long refType) {
            array[i] = refType;
        }

    }

    protected void limitedPrintlnInit(int limit) {
        numberOfLimitedPrintln = 0;
        limitOfPrintln = limit;
    }
    
    protected boolean limitedPrintln(String message) {
        lastLimitedPrintlnOK = true;
        if ( numberOfLimitedPrintln == limitOfPrintln ) {
            logWriter.println("==> WARNING: Limit for 'limitedPrintln' is reached!");
            numberOfLimitedPrintln++;
        }
        if ( numberOfLimitedPrintln < limitOfPrintln ) {
            logWriter.println(message);
            numberOfLimitedPrintln++;
            return true;
        }
        lastLimitedPrintlnOK = false;
        return false;
    }
    
    protected void printlnForDebug(String message) {
        if ( DEBUG_FLAG == YES ) {
            logWriter.println("=========> DEBUG info: " + message);
        }
    }
    
    static Object waitTimeObject = new Object();
    static protected void waitMlsecsTime(long mlsecsTime) { 
        synchronized(waitTimeObject) {
            try {
                waitTimeObject.wait(mlsecsTime);
            } catch (Throwable throwable) {
                 // ignore
            }
        }
    }

    protected boolean checkReplyForError (ReplyPacket reply, int expectedError, String commandName) {
        return checkReplyForError (reply, expectedError, commandName, UNLIMITED_PRINT);
    }
 
    protected boolean checkReplyForError (ReplyPacket reply, int expectedError, String commandName,
            boolean limitedPrint) {
        short errorCode = reply.getErrorCode();
        if ( errorCode == expectedError ) {
            return false;
        }
        if ( commandName == null ) {
            commandName = "Unknown command";   
        }
        boolean toPrintExtraInfo = true;
        String mainInfo = "## FAILURE: " +  commandName 
        + " returns unexpected ERROR = " + errorCode 
        + "(" + JDWPConstants.Error.getName(errorCode) + ")";
        if ( limitedPrint ) {
            toPrintExtraInfo = limitedPrintln(mainInfo);
        } else {
            logWriter.println(mainInfo);
        }
        if ( toPrintExtraInfo ) {
            logWriter.println("## Expected ERROR = " + expectedError 
                    + "(" + JDWPConstants.Error.getName(expectedError) + ")");
        }
        return true;
    }

        
    protected boolean checkReplyForError (ReplyPacket reply, int[] expectedErrors, String commandName) {
        return checkReplyForError (reply, expectedErrors, commandName, UNLIMITED_PRINT);
    }

    protected boolean checkReplyForError (ReplyPacket reply, int[] expectedErrors, String commandName,
            boolean limitedPrint) {
        short errorCode = reply.getErrorCode();
        for ( int i=0; i < expectedErrors.length; i++) {
            if ( errorCode == expectedErrors[i] ) {
                return false;
            }
        }
        if ( commandName == null ) {
            commandName = "Unknown command";   
        }
        boolean toPrintExtraInfo = true;
        String mainInfo = "## FAILURE: " +  commandName 
        + " returns unexpected ERROR = " + errorCode 
        + "(" + JDWPConstants.Error.getName(errorCode) + ")";
        if ( limitedPrint ) {
            toPrintExtraInfo = limitedPrintln(mainInfo);
        } else {
            logWriter.println(mainInfo);
        }
        if ( toPrintExtraInfo ) {
            for ( int i=0; i < expectedErrors.length; i++) {
                logWriter.println("## Expected ERROR = " + expectedErrors[i] 
                        + "(" + JDWPConstants.Error.getName(expectedErrors[i]) + ")");
            }
        }
        return true;
    }

    protected boolean printExpectedError (ReplyPacket reply, int expectedError, String commandName) {
        short errorCode = reply.getErrorCode();
        if ( errorCode != expectedError ) {
            return false;
        }
        if ( commandName == null ) {
            commandName = "Unknown command";   
        }
        logWriter.println("==> " +  commandName 
                + " returns EXPECTED ERROR = " + errorCode 
                + "(" + JDWPConstants.Error.getName(errorCode) + ")");
        return true;
    }

    protected void printStackTraceToLogWriter(Throwable thrown, boolean forDebug) {
        StackTraceElement[] traceElems = thrown.getStackTrace();
        if ( forDebug ) {
            printlnForDebug("## StackTrace for " +  thrown);
        } else {
            logWriter.println("## StackTrace for " +  thrown);
        }
        for ( int i=0; i < traceElems.length; i++) {
            String currentElem = traceElems[i].toString();
            if ( forDebug ) {
                printlnForDebug("   " +  currentElem);
            } else {
                logWriter.println("   " +  currentElem);
            }
        }
    }

    protected void printStackTraceToLogWriter(Throwable thrown) {
        printStackTraceToLogWriter(thrown, false /* for debug parameter */);
    }
    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#internalSetUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp(); 
                
        if (settings.getDebuggeeLaunchKind().equals("manual")) {
            debuggeeWrapper = new JDWPQAManualDebuggeeWrapper(settings,
                    logWriter, server);
        } else {
            debuggeeWrapper = new JDWPQADebuggeeWrapper(settings,
                    logWriter, server);
        }
        debuggeeRegister = new DebuggeeRegister();
        debuggeeRegister.register(debuggeeWrapper);
        testStartTimeMlsec = System.currentTimeMillis();
        testTimeout = getTestTimeout();
        
        
        debuggeeWrapper.start();
        logWriter.println("Launched debuggee VM process and established connection");
        
        vmStartEvent = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.VM_INIT);
        logWriter.println("Received VM_START event");

        debuggeeWrapper.vmMirror.adjustTypeLength();
        logWriter.println("Adjusted VM-dependent type lengths");
        logWriter.println("====> testStartTimeMlsec = " + testStartTimeMlsec);
        logWriter.println("====> testTimeout = " + testTimeout);
    }
    
    protected int waitForDebuggeeClassLoad(String debuggeeClassName) {
        logWriter.println("==> Wait for debuggee class load...");
        ReplyPacket reply = debuggeeWrapper.vmMirror.setClassPrepared(debuggeeClassName);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest::Set (ClassPrepared) command") ) {
            return FAILURE;
        }
        debuggeeWrapper.vmMirror.resume();
        try {
            EventPacket event = debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.CLASS_PREPARE);
        } catch ( Throwable thrown) {   
            logWriter.println("## EXCEPTION while wait for debuggee class load: "
                    + thrown);
            printStackTraceToLogWriter(thrown, FOR_DEBUG);
            return FAILURE;
        }
        stressDebuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(stressDebuggeeSignature);
        if ( stressDebuggeeRefTypeID == -1 ) {
            logWriter.println("## Can NOT get stressDebuggeeRefTypeID");
            logWriter.println("## stressDebuggeeSignature = " + stressDebuggeeSignature);
            return FAILURE;
        }
        logWriter.println("==> Debuggee class load - OK.");
        return SUCCESS;
    }
    
    /* (non-Javadoc)
     * @see jpda.jdwp.share.JDWPRawTestCase#internalTearDown()
     */
    protected void tearDown() throws Exception
    {      
        if (debuggeeRegister != null) {
            debuggeeRegister.stopAllRegistered();
            logWriter.println("Finished debuggee VM process and closed connection");
        }
        
        super.tearDown();        
    }
    
    protected long getTestTimeout() {
        return settings.getTimeout();
    }
    
    protected void setTestTimeout(long newTimeout) {
        settings.setTimeout(newTimeout);
    }

    protected ReplyPacket setEventRequest(byte eventKind,
            byte suspendPolicy, long threadID, long classID, long timeout) {
        if ( timeout == 0 ) {
            timeout = currentTimeout();
        }
        CommandPacket packet = new CommandPacket(
                JDWPCommands.EventRequestCommandSet.CommandSetID,
                JDWPCommands.EventRequestCommandSet.SetCommand);
        packet.setNextValueAsByte(eventKind);
        packet.setNextValueAsByte(suspendPolicy);
        int modifiers = 0;
        if ( threadID != NO_THREAD_ID ) {
            modifiers++;
        }
        if ( classID != NO_CLASS_ID ) {
            modifiers++;
        }
        packet.setNextValueAsInt(modifiers);
        if ( threadID != NO_THREAD_ID ) {
            packet.setNextValueAsByte(EventMod.ModKind.ThreadOnly);
            packet.setNextValueAsThreadID(threadID);
        }
        if ( classID != NO_CLASS_ID ) {
            packet.setNextValueAsByte(EventMod.ModKind.ClassOnly);
            packet.setNextValueAsThreadID(classID);
        }
        ReplyPacket reply = null;
        try { 
            reply = debuggeeWrapper.vmMirror.performCommand(packet, timeout);
        } catch (Throwable thrown ) {
            logWriter.println("## setEventRequest(): EXCEPTION while performCommand() (Timeout = "
                    + timeout + "): " + thrown);
            return null;
        }
        return reply;        
    }
    
    protected ReplyPacket clearEventRequest(byte eventKind, int requestID) {
        CommandPacket packet = new CommandPacket(
                JDWPCommands.EventRequestCommandSet.CommandSetID,
                JDWPCommands.EventRequestCommandSet.ClearCommand);
        packet.setNextValueAsByte(eventKind);
        packet.setNextValueAsInt(requestID);
        ReplyPacket reply = null;
        try { 
            reply = debuggeeWrapper.vmMirror.performCommand(packet);
        } catch (Throwable thrown ) {
            logWriter.println("## clearEventRequest(): EXCEPTION while performCommandt(): " + thrown);
            return null;
        }
        return reply;        
    }
    
    protected ReplyPacket setFieldModification(String classSignature,
            byte suspendPolicy, String fieldName)
            throws ReplyErrorCodeException {
        ReplyPacket request = null;
        long typeID = -1;
        long fieldID = -1;

        // Request referenceTypeID for class
        typeID = debuggeeWrapper.vmMirror.getClassID(classSignature);

        // Request fields in class
        request = debuggeeWrapper.vmMirror.getFieldsInClass(typeID);

        // Get fieldID from received packet
        fieldID = debuggeeWrapper.vmMirror.getFieldID(request, fieldName);

        // Prepare corresponding event
        byte eventKind = JDWPConstants.EventKind.FIELD_MODIFICATION;
        EventMod[] mods = new EventMod[] { new EventMod() };
        mods[0].fieldID = fieldID;
        mods[0].declaring = typeID;
        mods[0].modKind = EventMod.ModKind.FieldOnly;
        Event event = new Event(eventKind, suspendPolicy, mods);

        // Set event
        return debuggeeWrapper.vmMirror.setEvent(event);
    }

    protected  int setupSignalWithWait() {
        if ( signalWithWaitRequestID == NO_REQUEST_ID ) {
            String methodName = "signalWithSuspendDebuggeeMethod";
            Location location = getMethodEntryLocation(stressDebuggeeRefTypeID, methodName);
            if ( location == null ) {
                logWriter.println
                ("## setupSignalWithWait(): Can NOT get location for '" + methodName + "'");
                return FAILURE;
            }
            
            ReplyPacket reply = setBreakpointRequest(
                    JDWPConstants.SuspendPolicy.ALL, ANY_THREAD, location, 0);
            if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest.Set (BREAKPOINT) command") ) {
                return FAILURE;
            }
            signalWithWaitRequestID = reply.getNextValueAsInt();
        }
        printlnForDebug("setupSignalWithWait(): signalWithWaitRequestID = " + signalWithWaitRequestID);
        return SUCCESS;
    }
    
    protected  void clearSignalWithWait() {
        if ( signalWithWaitRequestID != NO_REQUEST_ID ) {
            ReplyPacket reply = debuggeeWrapper.vmMirror.clearEvent
            (JDWPConstants.EventKind.BREAKPOINT, signalWithWaitRequestID);
            checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest.Clear (BREAKPOINT) command");
            signalWithWaitRequestID = NO_REQUEST_ID;
        }
    }
    
    protected int setupThreadSignalWithWait() {
        if ( threadSignalWithWaitRequestID == NO_REQUEST_ID ) {
            String methodName = "signalWithSuspendThreadMethod";
            Location location = getMethodEntryLocation(stressDebuggeeRefTypeID, methodName);
            if ( location == null ) {
                logWriter.println
                ("## setupThreadSignalWithWait(): Can NOT get location for '" + methodName + "'");
                return FAILURE;
            }
            
            ReplyPacket reply = setBreakpointRequest(
                    JDWPConstants.SuspendPolicy.EVENT_THREAD, ANY_THREAD, location, 0);
            if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest.Set (BREAKPOINT) command") ) {
                return FAILURE;
            }
            threadSignalWithWaitRequestID = reply.getNextValueAsInt();
        }
        printlnForDebug("setupThreadSignalWithWait(): threadSignalWithWaitRequestID = " + 
                threadSignalWithWaitRequestID);
        return SUCCESS;
    }
    
    protected  void clearThreadSignalWithWait() {
        if ( threadSignalWithWaitRequestID != NO_REQUEST_ID ) {
            ReplyPacket reply = debuggeeWrapper.vmMirror.clearEvent
            (JDWPConstants.EventKind.BREAKPOINT, threadSignalWithWaitRequestID);
            checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest.Clear (BREAKPOINT) command");
            threadSignalWithWaitRequestID = NO_REQUEST_ID;
        }
    }
    
    protected int setupSignalWithContinue() {
        if ( threadSignalWithWaitRequestID == NO_REQUEST_ID ) {
            String methodName = "signalWithoutSuspendMethod";
            Location location = getMethodEntryLocation(stressDebuggeeRefTypeID, methodName);
            if ( location == null ) {
                logWriter.println
                ("## setupSignalWithContinue(): Can NOT get location for '" + methodName + "'");
                return FAILURE;
            }
            
            ReplyPacket reply = setBreakpointRequest(
                    JDWPConstants.SuspendPolicy.NONE, ANY_THREAD, location, 0);
            if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest.Set (BREAKPOINT) command") ) {
                return FAILURE;
            }
            signalWithContinueRequestID = reply.getNextValueAsInt();
        }
        printlnForDebug("setupSignalWithContinue(): signalWithContinueRequestID = " + 
                signalWithContinueRequestID);
        return SUCCESS;
    }
    
    protected void clearSignalWithContinue() {
        if ( signalWithContinueRequestID != NO_REQUEST_ID ) {
            ReplyPacket reply = debuggeeWrapper.vmMirror.clearEvent
            (JDWPConstants.EventKind.BREAKPOINT, signalWithContinueRequestID);
            checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest.Clear (BREAKPOINT) command");
            signalWithContinueRequestID = NO_REQUEST_ID;
        }
    }
    
    protected int setupSuspendThreadByEvent() {
        if ( suspendThreadByEventRequestID == NO_REQUEST_ID ) {
            String methodName = "suspendThreadByEventMethod";
            Location location = getMethodEntryLocation(stressDebuggeeRefTypeID, methodName);
            if ( location == null ) {
                logWriter.println
                ("## setupSuspendThreadByEvent(): Can NOT get location for '" + methodName + "'");
                return FAILURE;
            }
            
            ReplyPacket reply = setBreakpointRequest(
                    JDWPConstants.SuspendPolicy.EVENT_THREAD, ANY_THREAD, location, 0);
            if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest.Set (BREAKPOINT) command") ) {
                return FAILURE;
            }
            suspendThreadByEventRequestID = reply.getNextValueAsInt();
        }
        return SUCCESS;
    }
    
    protected void clearSuspendThreadByEvent() {
        if ( suspendThreadByEventRequestID != NO_REQUEST_ID ) {
            ReplyPacket reply = debuggeeWrapper.vmMirror.clearEvent
            (JDWPConstants.EventKind.BREAKPOINT, suspendThreadByEventRequestID);
            checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "EventRequest::Clear (BREAKPOINT) command");
            suspendThreadByEventRequestID = NO_REQUEST_ID;
        }
    }
    
    protected String receiveSignalWithWait() {
        return receiveSignal(signalWithWaitRequestID, 0);   
    }
    
    protected String receiveSignalWithWait(long timeout) {
        return receiveSignal(signalWithWaitRequestID, timeout);   
    }
    
    protected String receiveThreadSignalWithWait() {
        return receiveSignal(threadSignalWithWaitRequestID, 0);   
    }
    
    protected String receiveThreadSignalWithWait(long timeout) {
        return receiveSignal(threadSignalWithWaitRequestID, timeout);   
    }
    
    protected String receiveSignalWithContinue() {
        return receiveSignal(signalWithContinueRequestID, 0);   
    }
    
    protected String receiveSignalWithContinue(long timeout) {
        return receiveSignal(signalWithContinueRequestID, timeout);   
    }
    
    private boolean isExpectedEvent(ParsedEvent event, int expectedRequestID,
                                    byte expectedEventKind, long expectedThreadID) {
        if ( expectedRequestID != NO_REQUEST_ID ) {
            if ( event.getRequestID() != expectedRequestID ) {
                return false;
            }
        }
        if ( expectedEventKind != NO_EVENT_KIND ) {
            if ( event.getEventKind() != expectedEventKind ) {
                return false;
            }
        }
        if ( expectedThreadID != NO_THREAD_ID ) {
            long eventThreadID = NO_THREAD_ID;
            try {
                eventThreadID = ((EventThread)event).getThreadID();
            } catch (Throwable thrown ) {
                // ignore   
            }
            if ( eventThreadID != expectedThreadID ) {
                return false;
            }
        }
        return true;   
    }
    
    private ParsedEvent getReceivedParsedEvent(int expectedRequestID,
            byte expectedEventKind, long expectedThreadID) {
        ParsedEvent result = null;
        int eventsNumber = receivedParsedEvents.size();
        for (int i=0; i < eventsNumber; i++) {
            ParsedEvent parsedEvent = (ParsedEvent)receivedParsedEvents.get(i);
            if ( isExpectedEvent(parsedEvent, expectedRequestID, expectedEventKind, expectedThreadID) ) {
                receivedParsedEvents.remove(i);
                result = parsedEvent;
                break;
            }
        }
        return result;   
    }
    
    private String receiveSignal(int expectedRequestID, long timeout) {
        if ( expectedRequestID == NO_REQUEST_ID ) {
            logWriter.println
            ("## receiveSignal(): Incorrect call to receiveSignal: setupSignal() was not run correctly!");
            return null;
        }
        if ( timeout == 0 ) {
            timeout = currentTimeout();
        }
        printlnForDebug("receiveSignal(): with timeout(mlsecs) = " + timeout + "...");
        
        ParsedEvent foundEvent = 
            receiveEvent(expectedRequestID, NO_EVENT_KIND, ANY_THREAD, timeout);

        String message = null;
        if ( foundEvent != null )  {
            String stringFieldName = "signalWithoutSuspendMethod";
            if ( expectedRequestID == signalWithWaitRequestID ) {
                stringFieldName = "signalWithSuspendDebuggee";
            }
            if ( expectedRequestID == threadSignalWithWaitRequestID ) {
                threadSignalWithWaitThreadID = ((EventThread)foundEvent).getThreadID();
                stringFieldName = "signalWithSuspendThread";
            }
            message = getStringValueForStaticField(stressDebuggeeRefTypeID, stringFieldName);
        }
        
        if ( message == null ) {
            logWriter.println("## receiveSignal: Can NOT receive signal from debuggee!");
        }
        printlnForDebug("receiveSignal(): signal = '" + message + "'");
        return message;
    }
    
    protected long waitForSuspendThreadByEvent(long expectedThreadID) {
        return waitForSuspendThreadByEvent(expectedThreadID, 0);
    }
    
    protected long waitForSuspendThreadByEvent(long expectedThreadID, long timeout) {
        if ( timeout == 0 ) {
            timeout = currentTimeout();
        }
        if ( suspendThreadByEventRequestID == NO_REQUEST_ID ) {
            logWriter.println
            ("## waitForSuspendThreadByEvent(): Incorrect call: " +
                    "setupSuspendThreadByEvent() was not run correctly!");
            return FAILURE;
        }
        
        // at first - check receivedParsedEvents
        ParsedEvent foundEvent = 
            receiveEvent(suspendThreadByEventRequestID, NO_EVENT_KIND, expectedThreadID, timeout);
        if ( foundEvent == null )  {
            return FAILURE;
        }
        return ((EventThread)foundEvent).getThreadID();
    }
    
    protected ParsedEvent receiveEvent(byte expectedEventKind) {
        return receiveEvent(NO_REQUEST_ID, expectedEventKind, NO_THREAD_ID, 0);         
    }
    
    protected ParsedEvent receiveEvent(byte expectedEventKind, long timeout) {
        return receiveEvent(NO_REQUEST_ID, expectedEventKind, NO_THREAD_ID, timeout);         
    }
    
    protected ParsedEvent receiveEvent(long expectedThreadID) {
        return receiveEvent(NO_REQUEST_ID, NO_EVENT_KIND, expectedThreadID, 0);         
    }
    
    protected ParsedEvent receiveEvent(long expectedThreadID, long timeout) {
        return receiveEvent(NO_REQUEST_ID, NO_EVENT_KIND, expectedThreadID, timeout);         
    }
    
    protected ParsedEvent receiveEvent(int expectedRequestID, byte expectedEventKind,
                                     long expectedThreadID, long timeout) {
        if ( timeout == 0 ) {
            timeout = currentTimeout();
        }
        
        // at first - check receivedParsedEvents
        ParsedEvent foundEvent = 
            getReceivedParsedEvent(expectedRequestID, expectedEventKind, expectedThreadID);
        isTimeout = false;
        if ( foundEvent == null )  {
            long endTimeMlsecForWait = System.currentTimeMillis() + timeout;
            EventPacket eventPacket = null;
            while ( true ) {
                long currentLocalTimeout = endTimeMlsecForWait - System.currentTimeMillis();
                if ( currentLocalTimeout <= 0 ) {
                    printlnForDebug("## receiveEvent(): Timeout while receiveEvent() (Timeout = "
                            + timeout + ")");
                    isTimeout = true;
                    break;
                }
                try {
                    eventPacket = debuggeeWrapper.vmMirror.receiveEvent(currentLocalTimeout);
                } catch ( TimeoutException tout) {   
                    printlnForDebug("## receiveEvent(): TimeoutException while receiveEvent() (Timeout = "
                            + timeout + "): " + tout);
                    printStackTraceToLogWriter(tout, FOR_DEBUG);
                    isTimeout = true;
                    break;
                } catch ( Throwable thrown) {   
                    printlnForDebug("## receiveEvent(): EXCEPTION while receiveEvent() (Timeout = "
                            + timeout + "): " + thrown);
                    printStackTraceToLogWriter(thrown, FOR_DEBUG);
                    break;
                }
                if ( eventPacket != null ) {
                    try {
                        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(eventPacket);
                        for ( int i=0; i < parsedEvents.length; i++ ) {
                            if ( isExpectedEvent(parsedEvents[i], expectedRequestID, expectedEventKind, expectedThreadID) ) {
                                if ( foundEvent == null ) {
                                    foundEvent = parsedEvents[i];
                                } else {
                                    receivedParsedEvents.add(parsedEvents[i]);
                                }
                            } else {
                                receivedParsedEvents.add(parsedEvents[i]);
                            }
                        }
                    } catch (Throwable thrown ) {
                        printlnForDebug
                        ("## receiveEvent(): EXCEPTION while parsing received eventPacket:" + thrown);
                        printStackTraceToLogWriter(thrown, FOR_DEBUG);
                        break;
                    }
                }
                if ( foundEvent != null )  {
                    break;   
                }
                waitMlsecsTime(100);
            }
        }
        
        if ( foundEvent == null ) {
            printlnForDebug("## receiveEvent(): Can NOT receive expected event:");
            if ( expectedRequestID != NO_REQUEST_ID ) {
                printlnForDebug("##               expectedRequestID = " + expectedRequestID);
            }
            if ( expectedEventKind != NO_EVENT_KIND ) {
                printlnForDebug("##               expectedEventKind = " + expectedEventKind +
                        "(" + JDWPConstants.EventKind.getName(expectedEventKind) + ")");
            }
            if ( expectedThreadID != NO_THREAD_ID ) {
                printlnForDebug("##               expectedThreadID = " + expectedThreadID);
            }
        }
        return foundEvent;
    }
    
    protected void resumeDebuggee() {
        resumeDebuggee(null);
    }
    
    protected void resumeDebuggee(String marker) {
        if ( marker == null ) {
            marker = "";
        } else {
            marker = " Marker = " + marker;
        }
        printlnForDebug("Resuming Debuggee:" + marker + "...");
        debuggeeWrapper.vmMirror.resume();
    }
    
    protected ReplyPacket resumeThread(long threadID, String marker) {
        if ( marker != null ) {
            marker = " Marker = " + marker;
          printlnForDebug("Resuming Thread: ThreadID = " + threadID + "; "+ marker + "...");
        }
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                JDWPCommands.ThreadReferenceCommandSet.ResumeCommand);
        packet.setNextValueAsThreadID(threadID);
        return debuggeeWrapper.vmMirror.performCommand(packet);
    }
    
    protected ReplyPacket resumeSignalThread(String marker) {
        if ( threadSignalWithWaitThreadID == NO_THREAD_ID ) {
            printlnForDebug
            (">>> WARNING: resumeSignalThread(): Incorrect call: " +
                    "setupThreadSignalWithWait() was not run correctly!");
        }
        if ( marker == null ) {
            marker = "";
        } else {
            marker = " Marker = " + marker;
        }
        printlnForDebug("Resuming SignalThread: ThreadID = " + 
                threadSignalWithWaitThreadID + "; "+ marker + "...");
        return resumeThread(threadSignalWithWaitThreadID, null);
    }
    
    protected void terminateDebuggee(int exitCode) {
        terminateDebuggee(exitCode, null);
    }
    
    protected void terminateDebuggee(int exitCode, String marker) {
        if ( marker == null ) {
            marker = "";
        } else {
            marker = " Marker = " + marker;
        }
        printlnForDebug
        ("Terminating Debuggee: " + marker + "...");
        long timeout = 1000;
        setTestTimeout(timeout);
        try {
            debuggeeWrapper.stop();
        } catch ( Throwable thrown) {
            logWriter.println
            ("## terminateDebuggee: debuggeeWrapper.stop(): Exception happens: ");
            printStackTraceToLogWriter(thrown);
        }
    }

    public ReplyPacket performCommand(CommandPacket command, long timeout) {
        isException = null;
        isTimeout = false;
        isOutOfMemory = false;
        if ( timeout == 0 ) {
            timeout = currentTimeout();   
        }
        ReplyPacket reply = null;
        try {
            reply = debuggeeWrapper.vmMirror.performCommand(command,timeout);
        } catch ( TimeoutException timeoutException ) {
            logWriter.println
            ("## performCommand(): FAILURE - TimeoutException happens: " + timeoutException);
            isException = timeoutException;
            isTimeout = true;
            return null;
        } catch ( Throwable  thrown ) {   
            logWriter.println
            ("## performCommand(): FAILURE - Exception happens: " + thrown);
            isException = thrown;
            printStackTraceToLogWriter(thrown, FOR_DEBUG);
            return null;
        }
        if ( reply.getErrorCode() == JDWPConstants.Error.OUT_OF_MEMORY ) {
            logWriter.println
            ("====> performCommand(): WARNING - OUT_OF_MEMORY happens! ");
            isOutOfMemory = true;
        }
        return reply;
    }

    public ReplyPacket performCommand(CommandPacket command) {
        return performCommand(command, 0);    
    }

    public int sendCommand(CommandPacket command) {
        int commandId = FAILURE;
        try {
            commandId = debuggeeWrapper.vmMirror.sendCommand(command);
        } catch (Throwable thrown) {
            printlnForDebug
            ("## sendCommand(): EXCEPTION while sending command packet: " + thrown);
            printStackTraceToLogWriter(thrown, FOR_DEBUG);
        }
        return commandId;    
    }

    public ReplyPacket receveReply(int commandId, long timeout) { 
        ReplyPacket replyPacket = null;
        try {
            replyPacket = debuggeeWrapper.vmMirror.receiveReply(commandId, timeout);
        } catch (Throwable thrown) {
            logWriter.println
                ("## receveReply(): EXCEPTION while receiving reply: " + thrown);
            logWriter.println
                ("##              Command ID = " + commandId);
            logWriter.println
                ("##              Timeout (mlsecs) = " + timeout);
            printStackTraceToLogWriter(thrown, FOR_DEBUG);
        }
        return replyPacket;    
    }

    public ReplyPacket receveReply(int commandId) { 
        ReplyPacket replyPacket = null;
        try {
            replyPacket = debuggeeWrapper.vmMirror.receiveReply(commandId);
        } catch (Throwable thrown) {
            logWriter.println
                ("## receveReply(): EXCEPTION while receiving reply: " + thrown);
            logWriter.println
                ("##              Command ID = " + commandId);
            logWriter.println
                ("##              Timeout (mlsecs) = DEFAULT timeout");
            printStackTraceToLogWriter(thrown, FOR_DEBUG);
        }
        return replyPacket;    
    }

    protected int waitForThreadSuspendedStatus(long threadID) {
        return waitForThreadSuspendedStatus(threadID, 0);    
    }

    protected int waitForThreadSuspendedStatus(long threadID, long timeout) {
        if ( timeout == 0 ) {
            timeout = currentTimeout();
        }
        long endTimeMlsecForWait = System.currentTimeMillis() + timeout;
        while ( true ) {
            if ( System.currentTimeMillis() >= endTimeMlsecForWait ) {
                printlnForDebug("## waitForThreadSuspendedStatus(): Timeout happens!");
                return FAILURE;
            }
            CommandPacket commandPacket = new CommandPacket(
                    JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                    JDWPCommands.ThreadReferenceCommandSet.StatusCommand);
            commandPacket.setNextValueAsThreadID(threadID);
            ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(commandPacket);
            isOutOfMemory = false;                                
            short errorCode = reply.getErrorCode();
            if ( errorCode == JDWPConstants.Error.NONE ) {
                int threadStatus = reply.getNextValueAsInt();
                if ( threadStatus == JDWPConstants.ThreadStatus.RUNNING ) {
                    int suspendStatus = reply.getNextValueAsInt();    
                    if ( suspendStatus == JDWPConstants.SuspendStatus.SUSPEND_STATUS_SUSPENDED ) {
                        break;
                    }
                }
            } else {
                if ( errorCode == JDWPConstants.Error.OUT_OF_MEMORY ) {
                    isOutOfMemory = true;                                
                }
            }
            waitMlsecsTime(100);
        }
        return SUCCESS;
    }
    
    protected long currentTimeout() {
        return currentTimeout(testStartTimeMlsec, testTimeout);   
    }
    
    protected long currentTimeout(long testStartTime, long testTimeout) {
        long currentTimeout =  testTimeout - (System.currentTimeMillis() - testStartTime);
        if ( currentTimeout <= 0 ) {
            currentTimeout = 1;   
        }
        return currentTimeout;   
    }
    
    protected boolean isTestTimeout() {
        if ( currentTimeout() == 1 ) {
            logWriter.println
            (">>> WARNING: Test Timeout(" + testTimeout + ") happens!");
            return true;   
        }
        return false;   
    }
    
    protected Value getClassFieldValue (long classID, long fieldID) {

        Value valueToReturn = null;
        CommandPacket getValuesCommand = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.GetValuesCommand);
        getValuesCommand.setNextValueAsReferenceTypeID(classID);
        getValuesCommand.setNextValueAsInt(1);
        getValuesCommand.setNextValueAsFieldID(fieldID);
        
        ReplyPacket getValuesReply = debuggeeWrapper.vmMirror.performCommand(getValuesCommand);
        getValuesCommand = null;
        if ( checkReplyForError
                (getValuesReply, JDWPConstants.Error.NONE,
                "ReferenceType::GetValues") ) {
            return null;
        }

        int returnedValuesNumber = getValuesReply.getNextValueAsInt();
        if ( returnedValuesNumber != 1 ) {
            return null;
        }
        return getValuesReply.getNextValueAsValue();
    }

    protected String getClassSignature(long classID) {
        String classSignature = null;
        try {
            classSignature = debuggeeWrapper.vmMirror.getReferenceTypeSignature(classID);
        } catch ( Throwable thrown ) {
            logWriter.println
            ("## getClassSignature(): EXCEPTION while vmMirror.getReferenceTypeSignature():" + thrown);
            printStackTraceToLogWriter(thrown, FOR_DEBUG);
        }
        return classSignature;   
    }

    protected long getArraIDForStaticArrayField(long classID, String arrayFieldName) {

        long arrayFieldID = 
            debuggeeWrapper.vmMirror.getFieldID(classID, arrayFieldName);
        if ( arrayFieldID == -1 ) {
            logWriter.println
            ("## getArraIDForStaticArrayField(): Can NOT get arrayFieldID for field = '" 
                    + arrayFieldName + "'");
            return BAD_ARRAY_ID;
        }
        
        Value arrayFieldValue = getClassFieldValue (classID, arrayFieldID);
        if ( arrayFieldValue == null ) {
            logWriter.println
            ("## getArraIDForStaticArrayField(): Can NOT get arrayFieldValue for field = '" 
                    + arrayFieldName + "'");
            return BAD_ARRAY_ID;
        }
        
        byte tag = arrayFieldValue.getTag();
        if ( tag != JDWPConstants.Tag.ARRAY_TAG ) {
            logWriter.println
            ("## getArraIDForStaticArrayField(): Unexpected value tag for field = '" 
                    + arrayFieldName + "'");
            logWriter.println("## Received tag = " + tag
                    + "(" + JDWPConstants.Tag.getName(tag) + ")");
            logWriter.println
            ("## Expected tag = " + JDWPConstants.Tag.ARRAY_TAG + "(ARRAY_TAG)");
            return BAD_ARRAY_ID;
        }
        return arrayFieldValue.getLongValue();
    }

    protected ArrayRegion getArrayRegion(long arrayID, int arrayLength) {

        CommandPacket packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.GetValuesCommand);
        packet.setNextValueAsArrayID(arrayID);
        packet.setNextValueAsInt(0); // firstIndex
        packet.setNextValueAsInt(arrayLength);
        
        ReplyPacket reply = null;
        try {
            reply = debuggeeWrapper.vmMirror.performCommand(packet, currentTimeout());
        } catch (Throwable thrown) {
            logWriter.println
            ("## getArrayRegion(): Unexpected exception while perform ArrayReference.GetValues command: " 
                + thrown);
            printStackTraceToLogWriter(thrown);
            return null;
        }
        if ( checkReplyForError(reply, JDWPConstants.Error.NONE, "ArrayReference.GetValues command") ) {
            return null;
        }
        return reply.getNextValueAsArrayRegion();
    }

    protected long getObjectIDFromArrayRegion(ArrayRegion arrayRegion, int index) {

        int regionLength = arrayRegion.getLength();
        if ( (index < 0) || (index >= regionLength) ) {
            logWriter.println("## getObjectIDFromArrayRegion(): Passed index (" + index + 
                ") is beyond the bounds of ArrayRegion!");
            return BAD_OBJECT_ID;
        }
        Value value = arrayRegion.getValue(index);
        
        byte tag = value.getTag();
        byte[] expectedTags = {
            JDWPConstants.Tag.OBJECT_TAG,
            JDWPConstants.Tag.THREAD_TAG,
            JDWPConstants.Tag.THREAD_GROUP_TAG,
            JDWPConstants.Tag.CLASS_LOADER_TAG,
            JDWPConstants.Tag.CLASS_OBJECT_TAG,
        };
        boolean expectedTag = false;
        for (int i=0; i < expectedTags.length; i++) {
            if ( tag == expectedTags[i] ) {
                expectedTag = true;
                break;
            }
        }
        if ( ! expectedTag ) {
            logWriter.println
            ("## ggetObjectIDFromArrayRegion(): Unexpected tag of value in ArrayRegion:");
            logWriter.println("## Tag of value = " + tag
                    + "(" + JDWPConstants.Tag.getName(tag) + ")");
            logWriter.println("## Expected tag = " + JDWPConstants.Tag.OBJECT_TAG 
                + "(OBJECT_TAG) or tag, compatible with OBJECT_TAG");
            return BAD_OBJECT_ID;
        }
        return value.getLongValue();
    }

    protected int getIntValueForStaticField(long classID, String fieldName) {

        long fieldID = 
            debuggeeWrapper.vmMirror.getFieldID(classID, fieldName);
        if ( fieldID == -1 ) {
            logWriter.println
            ("## getIntValueForStaticField(): Can NOT get fieldID for field = '" 
                    + fieldName + "'");
            return BAD_INT_VALUE;
        }
        
        Value fieldValue = getClassFieldValue (classID, fieldID);
        if ( fieldValue == null ) {
            logWriter.println
            ("## getIntValueForStaticField(): Can NOT get fieldValue for field = '" 
                    + fieldName + "'");
            return BAD_INT_VALUE;
        }
        
        byte tag = fieldValue.getTag();
        if ( tag != JDWPConstants.Tag.INT_TAG ) {
            logWriter.println
            ("## getIntValueForStaticField(): Unexpected value tag for field = '" 
                    + fieldName + "'");
            logWriter.println("## Received tag = " + tag
                    + "(" + JDWPConstants.Tag.getName(tag) + ")");
            logWriter.println
            ("## Expected tag = " + JDWPConstants.Tag.INT_TAG + "(INT_TAG)");
            return BAD_INT_VALUE;
        }
        return fieldValue.getIntValue();
    }

    protected String getStringValueForStaticField(long classID, String fieldName) {

        long fieldID = 
            debuggeeWrapper.vmMirror.getFieldID(classID, fieldName);
        if ( fieldID == -1 ) {
            logWriter.println
            ("## getStringValueForStaticField(): Can NOT get fieldID for field = '" 
                    + fieldName + "'");
            return null;
        }
        
        Value fieldValue = getClassFieldValue (classID, fieldID);
        if ( fieldValue == null ) {
            logWriter.println
            ("## getStringValueForStaticField(): Can NOT get fieldValue for field = '" 
                    + fieldName + "'");
            return null;
        }
        
        byte tag = fieldValue.getTag();
        if ( tag != JDWPConstants.Tag.STRING_TAG ) {
            logWriter.println
            ("## getStringValueForStaticField(): Unexpected value tag for field = '" 
                    + fieldName + "'");
            logWriter.println("## Received tag = " + tag
                    + "(" + JDWPConstants.Tag.getName(tag) + ")");
            logWriter.println
            ("## Expected tag = " + JDWPConstants.Tag.STRING_TAG + "(STRING_TAG)");
            return null;
        }
        long stringID = fieldValue.getLongValue();
        CommandPacket packet = new CommandPacket(
                JDWPCommands.StringReferenceCommandSet.CommandSetID,
                JDWPCommands.StringReferenceCommandSet.ValueCommand);
        packet.setNextValueAsStringID(stringID);
        
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "StringReference.Value command from getStringValueForStaticField()") ) {
            return null;
        }
        return reply.getNextValueAsString();
    }

    protected long getObjectIDValueForStaticField(long classID, String fieldName) {

        long fieldID = 
            debuggeeWrapper.vmMirror.getFieldID(classID, fieldName);
        if ( fieldID == -1 ) {
            logWriter.println
            ("## getObjectIDValueForStaticField(): Can NOT get fieldID for field = '" 
                    + fieldName + "'");
            return BAD_OBJECT_ID;
        }
        
        Value fieldValue = getClassFieldValue (classID, fieldID);
        if ( fieldValue == null ) {
            logWriter.println
            ("## getObjectIDValueForStaticField(): Can NOT get fieldValue for field = '" 
                    + fieldName + "'");
            return BAD_OBJECT_ID;
        }
        
        byte tag = fieldValue.getTag();
        byte[] expectedTags = {
            JDWPConstants.Tag.OBJECT_TAG,
            JDWPConstants.Tag.THREAD_TAG,
            JDWPConstants.Tag.THREAD_GROUP_TAG,
            JDWPConstants.Tag.CLASS_LOADER_TAG,
            JDWPConstants.Tag.CLASS_OBJECT_TAG,
        };
        boolean expectedTag = false;
        for (int i=0; i < expectedTags.length; i++) {
            if ( tag == expectedTags[i] ) {
                expectedTag = true;
                break;
            }
        }
        if ( ! expectedTag ) {
            logWriter.println
            ("## getObjectIDValueForStaticField(): Unexpected value tag for field = '" 
                    + fieldName + "'");
            logWriter.println("## Tag of field = " + tag
                    + "(" + JDWPConstants.Tag.getName(tag) + ")");
            logWriter.println("## Expected tag = " + JDWPConstants.Tag.OBJECT_TAG 
                + "(OBJECT_TAG) or tag, compatible with OBJECT_TAG");
            return BAD_OBJECT_ID;
        }
        return fieldValue.getLongValue();
    }

    protected int setStaticIntField (long classID, String fieldName, int newValue) {

        long fieldID = 
            debuggeeWrapper.vmMirror.getFieldID(classID, fieldName);
        if ( fieldID == -1 ) {
            logWriter.println
            ("## setStaticIntField(): Can NOT get fieldID for field = '" 
                    + fieldName + "'");
            return FAILURE;
        }
        
        Value valueToReturn = null;
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.SetValuesCommand);
        packet.setNextValueAsReferenceTypeID(classID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsFieldID(fieldID);
        packet.setNextValueAsInt(newValue);
        
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "ReferenceType::GetValues") ) {
            return FAILURE;
        }
        return SUCCESS;
    }

    protected String getObjectSignature(long objectID) {

        CommandPacket command = new CommandPacket(
                JDWPCommands.ObjectReferenceCommandSet.CommandSetID,
                JDWPCommands.ObjectReferenceCommandSet.ReferenceTypeCommand);
        command.setNextValueAsObjectID(objectID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(command);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "ObjectReference::ReferenceType command") ) {
            return null;
        }

        byte refTypeTag = reply.getNextValueAsByte();
        long objectRefTypeID = reply.getNextValueAsReferenceTypeID();

        command = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.SignatureCommand);
        command.setNextValueAsReferenceTypeID(objectRefTypeID);
        reply = debuggeeWrapper.vmMirror.performCommand(command);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "ReferenceType::Signature command") ) {
            return null;
        }
        return reply.getNextValueAsString();
    }
    
    protected Location getMethodEntryLocation(long classID, String methodName) {
        long methodID = debuggeeWrapper.vmMirror.getMethodID(classID, methodName);
        if ( methodID == -1 ) {
            logWriter.println
            ("## getClassMethodEntryLocation(): Can NOT get methodID for classID = " 
                    + classID + "; Method name = " + methodName);
            return null;
        }
        
        CommandPacket packet = new CommandPacket(
                JDWPCommands.MethodCommandSet.CommandSetID,
                JDWPCommands.MethodCommandSet.LineTableCommand);
        packet.setNextValueAsClassID(classID);
        packet.setNextValueAsMethodID(methodID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "Method.LineTable command") ) {
            return null;
        }
        long methodStartCodeIndex = reply.getNextValueAsLong();
        Location location = new Location();
        location.tag = JDWPConstants.TypeTag.CLASS;
        location.classID =  classID;
        location.methodID = methodID;
        location.index = methodStartCodeIndex;
        return location;
    }

    protected Location getMethodLocationForLine(long classID, String methodName, int methodLineNumber) {
        long methodID = debuggeeWrapper.vmMirror.getMethodID(classID, methodName);
        if ( methodID == -1 ) {
            logWriter.println
            ("## getClassMethodEntryLocation(): Can NOT get methodID for classID = " 
                    + classID + "; Method name = " + methodName);
            return null;
        }
        
        CommandPacket packet = new CommandPacket(
                JDWPCommands.MethodCommandSet.CommandSetID,
                JDWPCommands.MethodCommandSet.LineTableCommand);
        packet.setNextValueAsClassID(classID);
        packet.setNextValueAsMethodID(methodID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "Method.LineTable command") ) {
            return null;
        }
        long methodStartCodeIndex = reply.getNextValueAsLong();
        long methodEndCodeIndex = reply.getNextValueAsLong();
        int linesNumber = reply.getNextValueAsInt();
        long resultIndex = -1;
        int totalLineNumber = 0;
        for (int i=0; i < linesNumber; i++) {
            long lineCodeIndex = reply.getNextValueAsLong();
            int currentLine = reply.getNextValueAsInt();
            if ( i == 0 ) {
                totalLineNumber = currentLine + methodLineNumber -1;
            }
            if ( currentLine == totalLineNumber ) {
                resultIndex = lineCodeIndex;
                break;
            }
        }
        if ( resultIndex == -1 ) {
            return null;
        }
        Location location = new Location();
        location.tag = JDWPConstants.TypeTag.CLASS;
        location.classID =  classID;
        location.methodID = methodID;
        location.index = resultIndex;
        return location;
    }

    protected long getMethodCodeIndexForLine(long classID, String methodName, int methodLineNumber) {
        long methodID = debuggeeWrapper.vmMirror.getMethodID(classID, methodName);
        if ( methodID == -1 ) {
            logWriter.println
            ("## getClassMethodEntryLocation(): Can NOT get methodID for classID = " 
                    + classID + "; Method name = " + methodName);
            return BAD_LONG_VALUE;
        }
        
        CommandPacket packet = new CommandPacket(
                JDWPCommands.MethodCommandSet.CommandSetID,
                JDWPCommands.MethodCommandSet.LineTableCommand);
        packet.setNextValueAsClassID(classID);
        packet.setNextValueAsMethodID(methodID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "Method.LineTable command") ) {
            return BAD_LONG_VALUE;
        }
        long methodStartCodeIndex = reply.getNextValueAsLong();
        long methodEndCodeIndex = reply.getNextValueAsLong();
        int linesNumber = reply.getNextValueAsInt();
        long resultIndex = -1;
        int totalLineNumber = 0;
        for (int i=0; i < linesNumber; i++) {
            long lineCodeIndex = reply.getNextValueAsLong();
            int currentLine = reply.getNextValueAsInt();
            if ( i == 0 ) {
                totalLineNumber = currentLine + methodLineNumber -1;
            }
            if ( currentLine == totalLineNumber ) {
                resultIndex = lineCodeIndex;
                break;
            }
        }
        if ( resultIndex == -1 ) {
            return BAD_LONG_VALUE;
        }
        return resultIndex;
    }

    protected void printMethodLineTable(
            long classID, String className /* may be null */, String methodName) {
        if ( DEBUG_FLAG != YES ) {
            return;
        }
        long methodID = debuggeeWrapper.vmMirror.getMethodID(classID, methodName);
        if ( methodID == -1 ) {
            logWriter.println
            ("## printMethodLineTable(): Can NOT get methodID for classID = " 
                    + classID + "; Method name = " + methodName);
            return;
        }
        
        CommandPacket packet = new CommandPacket(
                JDWPCommands.MethodCommandSet.CommandSetID,
                JDWPCommands.MethodCommandSet.LineTableCommand);
        packet.setNextValueAsClassID(classID);
        packet.setNextValueAsMethodID(methodID);
        ReplyPacket lineTableReply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (lineTableReply, JDWPConstants.Error.NONE,
                "Method.LineTable command") ) {
            return;
        }
        if ( className == null ) {
            logWriter.println("=== Line Table for method: " + methodName + " ===");
        } else {
            logWriter.println("=== Line Table for method: " + methodName + " of class: " 
                    + className + " ===");
        }
        long methodStartCodeIndex = lineTableReply.getNextValueAsLong();
        logWriter.println("==> Method Start Code Index = " + methodStartCodeIndex);
        long methodEndCodeIndex = lineTableReply.getNextValueAsLong();
        logWriter.println("==> Method End Code Index = " + methodEndCodeIndex);
        
        int linesNumber = lineTableReply.getNextValueAsInt();
        logWriter.println("==> Number of lines = " + linesNumber);
        for (int i=0; i < linesNumber; i++) {
            long lineCodeIndex = lineTableReply.getNextValueAsLong();
            int lineNumber = lineTableReply.getNextValueAsInt();
            logWriter.println("====> Line Number " + lineNumber + " : Initial code index = " + lineCodeIndex);
        }
        logWriter.println("=== End of Line Table " + methodName + " ===");
    }

    protected long getMethodStartCodeIndex(long classID, String methodName) {
        long methodID = debuggeeWrapper.vmMirror.getMethodID(classID, methodName);
        if ( methodID == -1 ) {
            logWriter.println
            ("## getMethodStartCodeIndex(): Can NOT get methodID for classID = " 
                    + classID + "; Method name = " + methodName);
            return BAD_LONG_VALUE;
        }
        
        CommandPacket packet = new CommandPacket(
                JDWPCommands.MethodCommandSet.CommandSetID,
                JDWPCommands.MethodCommandSet.LineTableCommand);
        packet.setNextValueAsClassID(classID);
        packet.setNextValueAsMethodID(methodID);
        ReplyPacket lineTableReply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (lineTableReply, JDWPConstants.Error.NONE,
                "Method.LineTable command") ) {
            return BAD_LONG_VALUE;
        }
        long methodStartCodeIndex = lineTableReply.getNextValueAsLong();
        return methodStartCodeIndex;
    }
    
    protected long getMethodEndCodeIndex(long classID, String methodName) {
        long methodID = debuggeeWrapper.vmMirror.getMethodID(classID, methodName);
        if ( methodID == -1 ) {
            logWriter.println
            ("## getMethodEndCodeIndex(): Can NOT get methodID for classID = " 
                    + classID + "; Method name = " + methodName);
            return BAD_LONG_VALUE;
        }
        
        CommandPacket packet = new CommandPacket(
                JDWPCommands.MethodCommandSet.CommandSetID,
                JDWPCommands.MethodCommandSet.LineTableCommand);
        packet.setNextValueAsClassID(classID);
        packet.setNextValueAsMethodID(methodID);
        ReplyPacket lineTableReply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (lineTableReply, JDWPConstants.Error.NONE,
                "Method.LineTable command") ) {
            return BAD_LONG_VALUE;
        }
        long methodStartCodeIndex = lineTableReply.getNextValueAsLong();
        long methodEndCodeIndex = lineTableReply.getNextValueAsLong();
        return methodEndCodeIndex;
    }
    
    protected ReplyPacket setBreakpointRequest(
            byte suspendPolicy, long threadID, Location location, long timeout) {
        if ( timeout == 0 ) {
            timeout = currentTimeout();
        }
        CommandPacket packet = new CommandPacket(
                JDWPCommands.EventRequestCommandSet.CommandSetID,
                JDWPCommands.EventRequestCommandSet.SetCommand);
        packet.setNextValueAsByte(JDWPConstants.EventKind.BREAKPOINT);
        packet.setNextValueAsByte(suspendPolicy);
        int modifiers = 1;
        if ( threadID != NO_THREAD_ID ) {
            modifiers++;
        }
        packet.setNextValueAsInt(modifiers);
        if ( threadID != NO_THREAD_ID ) {
            packet.setNextValueAsByte(EventMod.ModKind.ThreadOnly);
            packet.setNextValueAsThreadID(threadID);
        }
        packet.setNextValueAsByte(EventMod.ModKind.LocationOnly);
        packet.setNextValueAsLocation(location);

        ReplyPacket reply = null;
        try { 
            reply = debuggeeWrapper.vmMirror.performCommand(packet, timeout);
        } catch (Throwable thrown ) {
            logWriter.println("## setBreakpointRequest(): EXCEPTION while performCommandt() (Timeout = "
                    + timeout + "): " + thrown);
            printStackTraceToLogWriter(thrown, FOR_DEBUG);
            return null;
        }
        return reply;        
    }
    
    protected ReplyPacket setSingleStepRequest(
            byte suspendPolicy, long threadID, int stepSize, int stepDepth, long timeout) {
        if ( timeout == 0 ) {
            timeout = currentTimeout();
        }
        CommandPacket packet = new CommandPacket(
                JDWPCommands.EventRequestCommandSet.CommandSetID,
                JDWPCommands.EventRequestCommandSet.SetCommand);
        packet.setNextValueAsByte(JDWPConstants.EventKind.SINGLE_STEP);
        packet.setNextValueAsByte(suspendPolicy);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsByte(EventMod.ModKind.Step);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsInt(stepSize);
        packet.setNextValueAsInt(stepDepth);

        ReplyPacket reply = null;
        try { 
            reply = debuggeeWrapper.vmMirror.performCommand(packet, timeout);
        } catch (Throwable thrown ) {
            logWriter.println("## setSingleStepRequest(): EXCEPTION while performCommandt() (Timeout = "
                    + timeout + "): " + thrown);
            printStackTraceToLogWriter(thrown, FOR_DEBUG);
            return null;
        }
        return reply;        
    }

    protected String getThreadName(long threadID) {
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                JDWPCommands.ThreadReferenceCommandSet.NameCommand);
        packet.setNextValueAsThreadID(threadID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( checkReplyForError
                (reply, JDWPConstants.Error.NONE,
                "ThreadReference::Name command") ) {
            return null;
        }
        return reply.getNextValueAsString();
    }
    
    public String getMethodName(long classID, long methodID) {
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.MethodsCommand);
        packet.setNextValueAsReferenceTypeID(classID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if ( reply.getErrorCode() == JDWPConstants.Error.OUT_OF_MEMORY ) {
             return OUT_OF_MEMORY;   
        }
        if ( checkReplyForError(reply, JDWPConstants.Error.NONE,
                "ReferenceType.Methods command") ) {
            return null;   
        }

        int modBits, declared = reply.getNextValueAsInt();
        long mID;
        String value = null;
        String methodName = "", methodSign;
        for (int i = 0; i < declared; i++) {
            mID = reply.getNextValueAsMethodID();
            methodName = reply.getNextValueAsString();
            methodSign = reply.getNextValueAsString();
            modBits = reply.getNextValueAsInt();
            if (mID == methodID) {
                value = methodName;
                break;
            }
        }
        return value;
    }
   
    protected ReplyPacket performIDSizes() {
        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.IDSizesCommand);

        ReplyPacket reply = performCommand(packet);
        return reply;        
    }

//  Below - author is Aleksander V. Budniy

    protected boolean checkClass(String signature, String sourceFile,
            RefTypeArray refTypesArray) {
        boolean success = true;

        int beginIndex = signature.lastIndexOf("/");
        int endIndex = signature.lastIndexOf(";");
        String className = signature.substring(beginIndex + 1, endIndex);

        logWriter.println("Check " + className + " ...");
        logWriter
                .println("==> Send VirtualMachine::ClassesBySignature command");
        long commandStartTimeMlsec = System.currentTimeMillis();
        ReplyPacket reply = debuggeeWrapper.vmMirror
                .getClassBySignature(signature);
        long commandRunTimeMlsec = System.currentTimeMillis()
                - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = "
                + commandRunTimeMlsec);
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        if (checkReplyForError(reply, expectedErrors,
                "VirtualMachine::ClassesBySignature command")) {
            return false;
        }
        if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                "VirtualMachine::ClassesBySignature command")) {
            return success;
        }

        limitedPrintlnInit(20);
        int numberOfClasses = reply.getNextValueAsInt();
        if ( numberOfClasses == 0 ) {
            logWriter.println("##FAILURE: Expected class is NOT found among loaded classes in debuggee!");
            logWriter.println("##         Class signature = " + signature);
            return false;
        }
        int foundErrors = 0;
        for (int i = 0; i < numberOfClasses; i++) {
            if (foundErrors > 10) {
                logWriter.println("##FAILURE: too many errors! Break.");
                break;
            }
            reply.getNextValueAsByte();
            long refType = reply.getNextValueAsReferenceTypeID();

            reply.getNextValueAsInt();
            if (refType == 0) {
                limitedPrintln("#FAILURE: returned refType for " + className
                        + " is null");
                return false;
            }

            for (int index = 0; index < refTypesArray.getLength(); index++) {
                if (refType == refTypesArray.getRefType(index)) {
                    success = false;
                    limitedPrintln("#FAILURE: class with signature = "
                            + signature
                            + " is already exist among loaded classes");
                    foundErrors++;
                    break;
                }
            }

            logWriter.println("==> Send ReferenceType::Signature command ");
            commandStartTimeMlsec = System.currentTimeMillis();

            CommandPacket commandPacket = new CommandPacket(
                    JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                    JDWPCommands.ReferenceTypeCommandSet.SignatureCommand);
            commandPacket.setNextValueAsReferenceTypeID(refType);
            ReplyPacket replyPacket = null;
            try {
                replyPacket = debuggeeWrapper.vmMirror.performCommand(
                        commandPacket, currentTimeout());
            } catch (Throwable thrown) {
                limitedPrintln("## FAILURE: Exception while performCommand() = "
                        + thrown);
                return false;

            }
            if (checkReplyForError(replyPacket, expectedErrors,
                    "ReferenceType::Signature command", LIMITED_PRINT)) {
                success = false;
                continue;
            }
            if ( printExpectedError(replyPacket, JDWPConstants.Error.OUT_OF_MEMORY,
                   "ReferenceType::Signature command")) {
                continue;
            }
            String classSignature = replyPacket.getNextValueAsString();

            commandRunTimeMlsec = System.currentTimeMillis()
                    - commandStartTimeMlsec;
            logWriter.println("==> command running time(mlsecs) = "
                    + commandRunTimeMlsec);

            String source = getSource(refType);
            if (!source.equals("ERROR")) {
                if (!source.equals(sourceFile)) {
                    success = false;
                    limitedPrintln("#FAILURE: returned sourceFile for "
                            + className + " is " + source + " instead of "
                            + sourceFile);
                    foundErrors++;
                }
            } else {
                success = false;
                limitedPrintln("#FAILURE: during getting source file of "
                        + className + " class");
                foundErrors++;
            }

            if (!classSignature.equals(signature)) {
                success = false;
                limitedPrintln("#FAILURE: returned signature for " + className
                        + ": " + signature + " instead of " + signature);
                foundErrors++;
            }
        }
        if (foundErrors > 0) {
            success = false;
        }
        return success;
    }

    protected boolean saveRefTypeIDs(RefTypeArray refTypesArray,
            String[] signaturesArray, byte[] tagsArray) {
        logWriter.println("==> Saving classes..");
        boolean success = true;
        int classesToMiss = 500;
        logWriter.println("==> Send VirtualMachine::AllClasses command ");

        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllClassesCommand);
        ReplyPacket reply = null;
        try {
            long commandStartTimeMlsec = System.currentTimeMillis();

            reply = debuggeeWrapper.vmMirror.performCommand(packet, currentTimeout());

            long commandRunTimeMlsec = System.currentTimeMillis()
                    - commandStartTimeMlsec;
            logWriter.println("==> command running time(mlsecs) = "
                    + commandRunTimeMlsec);

        } catch (Throwable thrown) {
            logWriter.println("## FAILURE: Exception while performCommand() = "
                    + thrown);
            return false;

        }
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        if (checkReplyForError(reply, expectedErrors,
                "VirtualMachine::AllClasses command")) {
            return false;
        }

        printlnForDebug("reply.getLength()= " + reply.getLength());
        if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                "VirtualMachine::AllClasses command")) {
            return success;
        }

        int newClasses = reply.getNextValueAsInt();
        logWriter.println("==> Number of reference types  " + newClasses);

        refTypesArray.setLength(newClasses);
        logWriter.println("\n");

        logWriter
                .println("==> Checking information, received from AllClasses command...");
        logWriter.println("==> classes = " + newClasses);
        limitedPrintlnInit(20);
        int foundErrors = 0;
        int checkedClasses = 0;
        long startOfCheck = System.currentTimeMillis();
        long maxCommandDuration = 0;
        int numberOfLongCommand = 0;
        long longestLoop = 0;
        for (int i = 0; i < newClasses; i++) {
            long startOfLoop = System.currentTimeMillis();
            if (foundErrors > 30) {
                logWriter.println("##FAILURE: too many errors! Break;");
                break;
            }

            tagsArray[i] = reply.getNextValueAsByte();
            long refTypeID = reply.getNextValueAsReferenceTypeID();
            refTypesArray.setRefType(i, refTypeID);

            signaturesArray[i] = reply.getNextValueAsString();
            int status = reply.getNextValueAsInt();

            if (i % classesToMiss == 0) {

                long fase1 = System.currentTimeMillis();

                long startOfCommand = System.currentTimeMillis();
                CommandPacket commandPacket = new CommandPacket(
                        JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                        JDWPCommands.ReferenceTypeCommandSet.SignatureCommand);
                commandPacket.setNextValueAsReferenceTypeID(refTypeID);
                ReplyPacket replyPacket = null;
                try {
                    replyPacket = debuggeeWrapper.vmMirror.performCommand(
                            commandPacket, currentTimeout());
                } catch (Throwable thrown) {

                    limitedPrintln("## FAILURE: Exception while performCommand() = "
                            + thrown);
                    limitedPrintln("##       classes were checked: "
                            + checkedClasses);
                    limitedPrintln("##       classes for check remain: "
                            + (newClasses - checkedClasses * classesToMiss));
                    long endOfCommand = System.currentTimeMillis();
                    limitedPrintln("##       last command duration: "
                            + (endOfCommand - startOfCommand));
                    limitedPrintln("##       the longest command duration: "
                            + maxCommandDuration);
                    limitedPrintln("##       the longest loop duration: "
                            + longestLoop);
                    limitedPrintln("##       the longest command checked class: "
                            + signaturesArray[i]);
                    limitedPrintln("##       all checking duration: "
                            + (endOfCommand - startOfCheck));
                    return false;

                }
                long commandDuration = System.currentTimeMillis()
                        - startOfLoop;
                if (commandDuration > maxCommandDuration) {
                    numberOfLongCommand = i;
                    maxCommandDuration = commandDuration;
                }
                checkedClasses++;
                if (checkReplyForError(replyPacket, expectedErrors,
                        "ReferenceType::Signature command", LIMITED_PRINT)) {
                    success = false;
                    continue;
                }
                if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "ReferenceType::Signature command")) {
                    continue;
                }
                String receivedSignature = replyPacket
                        .getNextValueAsString();

                if (!receivedSignature.equals(signaturesArray[i])) {
                    limitedPrintln("#FAILURE: signature from AllClasses command = "
                            + signaturesArray[i]
                            + " instead of "
                            + receivedSignature);
                    foundErrors++;
                    success = false;
                }

                long fase2 = System.currentTimeMillis();

                ReplyPacket receivedReply = debuggeeWrapper.vmMirror
                        .getClassBySignature(signaturesArray[i]);
                if (checkReplyForError(receivedReply, expectedErrors,
                        "VirtualMachine::ClassesBySignature command",
                        LIMITED_PRINT)) {
                    success = false;
                    continue;
                }
                if ( printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "VirtualMachine::ClassesBySignature")) {
                    continue;
                }
                long fase3 = System.currentTimeMillis();

                int classesWithSignature = receivedReply
                        .getNextValueAsInt();
                for (int j = 0; j < classesWithSignature; j++) {
                    byte tag = receivedReply.getNextValueAsByte();
                    long classID = receivedReply
                            .getNextValueAsReferenceTypeID();
                    int classStatus = receivedReply.getNextValueAsInt();

                    if (tagsArray[i] != tag) {
                        limitedPrintln("#FAILURE: tag from AllClasses command = "
                                + tagsArray[i]
                                + "("
                                + JDWPConstants.Tag.getName(tagsArray[i])
                                + ")" + " instead of " + tag);
                        foundErrors++;
                        success = false;
                    }
                    if (classStatus != status) {
                        limitedPrintln("#FAILURE: status from AllClasses command = "
                                + status
                                + "("
                                + JDWPConstants.ClassStatus.getName(status)
                                + ")" + " instead of " + classStatus);
                        foundErrors++;
                        success = false;
                    }
                }

            }

        }
        logWriter.println("==> The longest command duration: "
                + maxCommandDuration);
        logWriter.println("==> The longest command checked class: "
                + signaturesArray[numberOfLongCommand]);
        logWriter.println("==> The longest loop duration: " + longestLoop);
        if (foundErrors > 0) {
            success = false;
        }

        return success;
    }

    protected boolean checkClasses(RefTypeArray refTypesArray,
            String[] signaturesArray, byte[] tagsArray, long classes) {
        logWriter.println("==> Checking classes...");
        long classesNotFound = classes;
        boolean success = true;
        logWriter.println("==> Send VirtualMachine::AllClasses command ");

        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.AllClassesCommand);
        ReplyPacket reply = null;
        try {
            long commandStartTimeMlsec = System.currentTimeMillis();
            reply = debuggeeWrapper.vmMirror.performCommand(packet, currentTimeout());
            long commandRunTimeMlsec = System.currentTimeMillis()
                    - commandStartTimeMlsec;
            logWriter.println("==> command running time(mlsecs) = "
                    + commandRunTimeMlsec);

        } catch (Throwable thrown) {
            logWriter.println("## FAILURE: Exception while performCommand() = "
                    + thrown);
            return false;
        }
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        if (checkReplyForError(reply, expectedErrors,
                "VirtualMachine::AllClasses command")) {
            return false;
        }
        printlnForDebug("reply.getLength()= " + reply.getLength());
        if (printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
            "VirtualMachine::AllClasses command")) {
            return success;
        }
        int newClasses = reply.getNextValueAsInt();
        logWriter.println("==> Number of reference types = " + newClasses);

        logWriter.println("\n");
        logWriter
                .println("Checking information received from AllClasses command...");
        limitedPrintlnInit(20);
        int foundErrors = 0;
        int checkedClasses = 0;
        long startOfCheck = System.currentTimeMillis();
        long maxCommandDuration = 0;
        int numberOfLongCommand = 0;
        for (int i = 0; i < newClasses; i++) {
            if (foundErrors > 10) {
                logWriter.println("##FAILURE: too many errors! Break;");
                break;
            }

            byte refTypeTag = reply.getNextValueAsByte();
            long typeID = reply.getNextValueAsReferenceTypeID();
            String signature = reply.getNextValueAsString();
            int status = reply.getNextValueAsInt();
            for (int j = 0; j < refTypesArray.getLength(); j++) {
                if (refTypesArray.getRefType(j) == typeID) {
                    classesNotFound--;
                    if (tagsArray[j] != refTypeTag) {
                        limitedPrintln("#FAILURE: class with refType = "
                                + refTypesArray.getRefType(j)
                                + " has unexpected tag = "
                                + refTypeTag
                                + " instead of " + tagsArray[j]);
                        foundErrors++;
                        success = false;
                    }
                    if (!signaturesArray[j].equals(signature)) {
                        limitedPrintln("#FAILURE: class with refType = "
                                + refTypesArray.getRefType(j)
                                + " has unexpected signature = "
                                + signature
                                + " instead of "
                                + signaturesArray[j]);
                        foundErrors++;
                        success = false;
                    }
                }
            }
            if (newClasses % 100 == 0) {
                long startOfCommand = System.currentTimeMillis();
                CommandPacket commandPacket = new CommandPacket(
                        JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                        JDWPCommands.ReferenceTypeCommandSet.SignatureCommand);
                commandPacket.setNextValueAsReferenceTypeID(typeID);
                ReplyPacket replyPacket = null;
                try {
                    replyPacket = debuggeeWrapper.vmMirror.performCommand(
                            commandPacket, currentTimeout());
                } catch (Exception thrown) {
                    limitedPrintln("## FAILURE: Exception while performCommand() = "
                            + thrown);
                    limitedPrintln("##       classes were checked: "
                            + checkedClasses);
                    long endOfCommand = System.currentTimeMillis();
                    limitedPrintln("##       last sent command duration: "
                            + (endOfCommand - startOfCommand));
                    limitedPrintln("##       the longest command duration: "
                            + maxCommandDuration);
                    limitedPrintln("##       the longest command checked class: "
                            + signaturesArray[i]);

                    limitedPrintln("##       all checking duration: "
                            + (endOfCommand - startOfCheck));

                    return false;

                }
                long commandDuration = System.currentTimeMillis()
                        - startOfCommand;
                if (commandDuration > maxCommandDuration) {
                    numberOfLongCommand = i;
                    maxCommandDuration = commandDuration;
                }
                checkedClasses++;
                if (checkReplyForError(replyPacket, expectedErrors,
                        "ReferenceType::Signature command", LIMITED_PRINT)) {
                    success = false;
                    continue;
                }
                if (printExpectedError(replyPacket, JDWPConstants.Error.OUT_OF_MEMORY,
                    "ReferenceType::Signature command")) {
                    // ignore
                    continue;
                }
                String trueSignature = replyPacket.getNextValueAsString();

                if (!trueSignature.equals(signature)) {
                    limitedPrintln("## FAILURE: class with refType = "
                            + typeID + " has unexpected signature = "
                            + signature + " instead of " + trueSignature);
                    foundErrors++;
                    success = false;
                }
            }
        }
        logWriter.println("==> The longest command duration (mlsecs): "
                + maxCommandDuration);
        logWriter.println("==> The longest command checked class: "
                + signaturesArray[numberOfLongCommand]);

        if (foundErrors > 0) {
            success = false;
        }

        if (classesNotFound != 0) {
            logWriter.println("## FAILURE: classes were not found: "
                    + classesNotFound);
            success = false;
        }
        logWriter.println("==> Classes are checked");
        return success;
    }

    protected boolean checkNewClasses(RefTypeArray refTypesArray,
            String[] signaturesArray, byte[] tagsArray, int testNumber) {
        logWriter.println("==> Checking new classes...");
        boolean success = true;
        for (int i = 1; i < 6; i++) {
            
            String checkedNewClassName = "RefType00" + testNumber + "_TestClass0" + i;
            String classSignature = "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/REFTYPE00"
                + testNumber + "/"
                + checkedNewClassName + ";";
            ReplyPacket reply = debuggeeWrapper.vmMirror
                    .getClassBySignature(classSignature);
            int[] expectedErrors = { JDWPConstants.Error.NONE,
                    JDWPConstants.Error.OUT_OF_MEMORY };
            if (checkReplyForError(reply, expectedErrors,
                    "VirtualMachine::ClassesBySignature command")) {
                success = false;
                continue;
            }
            if (printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "VirtualMachine::AllClasses command")) {
                continue;
            }
            int numberOfClasses = reply.getNextValueAsInt();
            if ( numberOfClasses == 0 ) {
                logWriter.println("##FAILURE: Expected class is NOT found among loaded classes in debuggee!");
                logWriter.println("##         Class signature = " + classSignature);
                success =  false;
                continue;
            }
            reply.getNextValueAsByte();
            long typeID = reply.getNextValueAsReferenceTypeID();
            for (int j = 0; j < refTypesArray.getLength(); j++) {
                if (typeID == refTypesArray.getRefType(j)) {
                    logWriter
                            .println("#WARNING: refTypeID = " + typeID + " of new class "
                                    + checkedNewClassName
                                    + " already exists among classes loaded before ");
                }
            }
            String sourceFile = getSource(typeID);
            if (!sourceFile.equals("ERROR")) {
                if (!sourceFile.equals("RefTypeDebuggee00" + testNumber
                        + ".java")) {
                    success = false;
                    logWriter.println("#FAILURE: class " + checkedNewClassName
                            + " has unexpected source file " + sourceFile
                            + " instead of " + "RefTypeDebuggee00" + testNumber
                            + ".java");
                }
            } else {
                success = false;
                logWriter.println("#FAILURE: during getting source file");
            }
            CommandPacket commandPacket = new CommandPacket(
                    JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                    JDWPCommands.ReferenceTypeCommandSet.SignatureCommand);
            commandPacket.setNextValueAsReferenceTypeID(typeID);
            ReplyPacket replyPacket = null;
            try {
                replyPacket = debuggeeWrapper.vmMirror.performCommand(
                        commandPacket, currentTimeout());
            } catch (Throwable thrown) {
                logWriter
                        .println("## FAILURE: Exception while performCommand() = "
                                + thrown);
                return false;

            }
            if (checkReplyForError(replyPacket, expectedErrors,
                    "ReferenceType::Signature command")) {
                success = false;
                continue;
            }
            if (printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                    "VirtualMachine::AllClasses command")) {
                continue;
            }
            String signature = replyPacket.getNextValueAsString();
            if (!signature
                    .equals("Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/REFTYPE00"
                            + testNumber + "/" 
                            + checkedNewClassName + ";")) {
                success = false;
                logWriter
                        .println("#FAILURE: received class signature = "
                                + signature
                                + " not equals to signature Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/REFTYPE00"
                                + testNumber + "/" + checkedNewClassName + ";");
            }
        }
        logWriter.println("==> New classes are checked");
        return success;
    }

    protected int getStatus(long threadID) {
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                JDWPCommands.ThreadReferenceCommandSet.StatusCommand);
        packet.setNextValueAsThreadID(threadID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        if (checkReplyForError(reply, expectedErrors,
                "ReferenceType::SourceFile command")) {
            return -1;
        }
        return reply.getNextValueAsInt();
    }

    protected int getSuspendStatus(long threadID) {
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                JDWPCommands.ThreadReferenceCommandSet.StatusCommand);
        packet.setNextValueAsThreadID(threadID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        if (checkReplyForError(reply, expectedErrors,
                "ReferenceType::SourceFile command")) {
            return -1;
        }
        reply.getNextValueAsInt();
        return reply.getNextValueAsInt();
    }

    protected String getSource(long refTypeID) {
        String sourceFile;
        CommandPacket sourceFileCommand = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.SourceFileCommand);
        sourceFileCommand.setNextValueAsClassID(refTypeID);
        ReplyPacket sourceFileReply = debuggeeWrapper.vmMirror
                .performCommand(sourceFileCommand);

        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        if (checkReplyForError(sourceFileReply, expectedErrors,
                "ReferenceType::SourceFile command")) {
            return "ERROR";
        }

        sourceFile = sourceFileReply.getNextValueAsString();
        return sourceFile;
    }

    public int checkArray(String arrayName, long[] arrayForCompare,
            String DEBUGGEE_SIGNATURE) {
        int success = 0;
        logWriter.println("==> Get values from debuggee's object array...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror
                .getClassID(DEBUGGEE_SIGNATURE);
        if (debuggeeRefTypeID == -1) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            return -1;
        }
        long arrayID = getArraIDForStaticArrayField(debuggeeRefTypeID,
                arrayName);
        if ( arrayID == BAD_ARRAY_ID ) {
            return -1;   
        }

        long commandStartTimeMlsec = System.currentTimeMillis();
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.GetValuesCommand);
        packet.setNextValueAsArrayID(arrayID);
        packet.setNextValueAsInt(0);
        packet.setNextValueAsInt(arrayForCompare.length);
        ReplyPacket reply = null;
        try {
            reply = debuggeeWrapper.vmMirror.performCommand(packet, currentTimeout());
        } catch (Throwable thrown) {
            logWriter.println("## FAILURE: Exception while performCommand() = "
                    + thrown);
            return -1;

        }

        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        if (checkReplyForError(reply, expectedErrors,
                "ArrayReference::GetValues command")) {
            return -1;
        }
        if (printExpectedError(reply, JDWPConstants.Error.OUT_OF_MEMORY,
                "ArrayReference::GetValues command")) {
            return success;
        }
        ArrayRegion region = reply.getNextValueAsArrayRegion();
        byte arrayTag = region.getTag();
        if (arrayTag != JDWPConstants.Tag.OBJECT_TAG) {
            logWriter.println("#FAILURE: received tag of region of objects = "
                    + arrayTag + "(" + JDWPConstants.Tag.getName(arrayTag)
                    + " instead of 76 (OBJECT_TAG)");
            success = -1;
        }
        int arrayLength = region.getLength();
        if (arrayLength != arrayForCompare.length) {
            logWriter
                    .println("#FAILURE: unexpected received length of array of objects =  "
                            + arrayLength
                            + " instead of "
                            + arrayForCompare.length);
            success = -1;
        }

        limitedPrintlnInit(20);
        int foundErrors = 0;
        for (int i = 0; i < region.getLength(); i++) {
            if (foundErrors > 10) {
                logWriter.println("##FAILURE: too many errors! Break;");
                break;
            }
            Value value = region.getValue(i);
            byte elementTag = value.getTag();
            if (elementTag != JDWPConstants.Tag.OBJECT_TAG) {
                limitedPrintln("#FAILURE: received tag of object = "
                        + elementTag + "("
                        + JDWPConstants.Tag.getName(arrayTag)
                        + " instead of 76 (OBJECT_TAG)");
                foundErrors++;
                success = -1;
            }
            String valueInString = value.toString();
            String partOfValue = valueInString.substring(0, 8);
            if (!partOfValue.equals("ObjectID")) {
                limitedPrintln("#FAILURE: received string representation of object value = "
                        + partOfValue + " instead of ObjectID");
                foundErrors++;
                success = -1;
            }

            arrayForCompare[i] = value.getLongValue();
        }
        long commandRunTimeMlsec = System.currentTimeMillis()
                - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = "
                + commandRunTimeMlsec);
        return success;
    }

    public int compareArrays(long[] arrayForCompare1, long[] arrayForCompare2) {
        logWriter.println("==> Comparing arrays...");

        int success = 0;
        int numberOfEquals = 0;

        for (int i = 0; i < arrayForCompare1.length; i++) {
            if ( arrayForCompare1[i] == arrayForCompare2[i] ) {
                numberOfEquals++;
            }
        }
        if (numberOfEquals != arrayForCompare1.length) {
            logWriter
                    .println("##FAILURE: objects in first array of objects are differ from the second array");
            logWriter.println("##FAILURE: "
                    + (arrayForCompare1.length - numberOfEquals)
                    + " objects are not equal");
            success = -1;
        }
        return success;
    }

    protected int disposeObj(int number, long[] objIDsArray) {

        CommandPacket packet = new CommandPacket(
                JDWPCommands.VirtualMachineCommandSet.CommandSetID,
                JDWPCommands.VirtualMachineCommandSet.DisposeObjectsCommand);
        if (number == -1)
            number = objIDsArray.length;

        logWriter.println("==> Disposing " + number + " objects...");
        packet.setNextValueAsInt(number);
        for (int i = 0; i < number; i++) {
            packet.setNextValueAsObjectID(objIDsArray[i]);
            packet.setNextValueAsInt(1);
        }
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if (checkReplyForError(reply, JDWPConstants.Error.NONE,
                "VirtualMachine::DisposeObject command")) {
            return -1;
        }

        return 1;
    }

    public int checkThreads(long[] threadIDs, String[] threadNamnes, int testNumber, int disposeObjects) {
        logWriter.println("==> Checking threads (number of trhreads to check = " + threadIDs.length +")...");
        int success = 1;
        limitedPrintlnInit(20);
        int foundErrors = 0;
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        for (int i = 0; i < threadIDs.length; i++) {
            if (foundErrors > 10) {
                logWriter.println("##FAILURE: too many errors! Break;");
                break;
            }

            CommandPacket packet = new CommandPacket(
                    JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                    JDWPCommands.ThreadReferenceCommandSet.StatusCommand);
            packet.setNextValueAsThreadID(threadIDs[i]);
            ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
            int error = reply.getErrorCode();

            if (i < disposeObjects) {
                if (error != JDWPConstants.Error.INVALID_OBJECT) {
                    logWriter.println("##FAILURE: StatusCommand for thread '" + threadNamnes[i] +
                        "' doesn't return expedted INVALID_OBJECT error as thread was disposed!");
                    logWriter.println("##         Returned error = " + error + 
                        "(" + JDWPConstants.Error.getName(error) + ")");
                    foundErrors++;
                }
            } else {
                if (!checkReplyForError(reply, expectedErrors,
                        "ThreadReference::Status command", LIMITED_PRINT)) {

                    int threadStatus = reply.getNextValueAsInt();
                    if (threadStatus != JDWPConstants.ThreadStatus.WAIT) {
                        if (threadStatus != JDWPConstants.ThreadStatus.RUNNING) {
                            limitedPrintln("##FAILURE: ThreadReference.Status command reutrns unexpected status!");
                            limitedPrintln("##         Thread name = '" + threadNamnes[i] + "'");
                            limitedPrintln("##         Returned status = " + threadStatus + "(" +
                                    JDWPConstants.ThreadStatus.getName(threadStatus) + ")");
                            limitedPrintln("##         Expected status: 4 (WAIT) or 1 (RUNNING)");
                            foundErrors++;
                        }
                    }

                } else {
                    foundErrors++;
                }
            }

            packet = new CommandPacket(
                    JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                    JDWPCommands.ThreadReferenceCommandSet.NameCommand);
            packet.setNextValueAsThreadID(threadIDs[i]);
            reply = debuggeeWrapper.vmMirror.performCommand(packet);

            error = reply.getErrorCode();
            if (i < disposeObjects) {
                if (error != JDWPConstants.Error.INVALID_OBJECT) {
                    logWriter.println("##FAILURE: NameCommand for thread '" + threadNamnes[i] +
                        "' doesn't return expedted INVALID_OBJECT error as thread was disposed!");
                    logWriter.println("##         Returned error = " + error + 
                        "(" + JDWPConstants.Error.getName(error) + ")");
                    foundErrors++;
                }
            } else {
                if (!checkReplyForError(reply, expectedErrors,
                        "ThreadReference::Name command", LIMITED_PRINT)) {

                    String name = reply.getNextValueAsString();
                    if ( !threadNamnes[i].equals(name) ) {
                        limitedPrintln("##FAILURE: ThreadReference::Name command reutrns unexpected name!");
                        limitedPrintln("##         Returned name = '" + name + "'");
                        limitedPrintln("##         Expected name = '" + threadNamnes[i] + "'");
                        foundErrors++;
                    }
                } else {
                    foundErrors++;
                }
            }

            packet = new CommandPacket(
                    JDWPCommands.ObjectReferenceCommandSet.CommandSetID,
                    JDWPCommands.ObjectReferenceCommandSet.ReferenceTypeCommand);
            packet.setNextValueAsObjectID(threadIDs[i]);
            reply = debuggeeWrapper.vmMirror.performCommand(packet);
            error = reply.getErrorCode();
            if (i < disposeObjects) {
                if (error != JDWPConstants.Error.INVALID_OBJECT) {
                    logWriter.println("##FAILURE: ObjectReference.ReferenceType command for thread '" 
                        + threadNamnes[i] +
                        "' doesn't return expedted INVALID_OBJECT error as thread was disposed!");
                    logWriter.println("##         Returned error = " + error + 
                        "(" + JDWPConstants.Error.getName(error) + ")");
                    foundErrors++;
                }
            } else {
                if (!checkReplyForError(reply, expectedErrors,
                        "ObjectReference::ReferenceType command", LIMITED_PRINT)) {

                    reply.getNextValueAsByte();
                    long refType = reply.getNextValueAsReferenceTypeID();
                    packet = new CommandPacket(
                            JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                            JDWPCommands.ReferenceTypeCommandSet.SignatureCommand);
                    packet.setNextValueAsReferenceTypeID(refType);
                    reply = debuggeeWrapper.vmMirror.performCommand(packet);

                    String returnedThreadSignature = reply.getNextValueAsString();
                    String expectedThreadSignature = "Lorg/apache/harmony/test/stress/jpda/jdwp/scenario/OBJECT00"
                        + testNumber
                        + "/ObjectDebuggee00"
                        + testNumber + "_Thread;";
                    if ( !expectedThreadSignature.equals(returnedThreadSignature) ) {
                        limitedPrintln("##FAILURE: ReferenceType.Signature command reutrns unexpected signature for thread!");
                        limitedPrintln("##         Thread name = '" + threadNamnes[i] + "'");
                        limitedPrintln("##         Returned signature = '" + returnedThreadSignature + "'");
                        limitedPrintln("##         Expected signature = '" + expectedThreadSignature + "'");
                        foundErrors++;
                    }

                    String source = getSource(refType);
                    String expectedSource = "ObjectDebuggee00" + testNumber + ".java";
                    if ( !expectedSource.equals(source) ) {
                        limitedPrintln("##FAILURE: ReferenceType.SourceFile command reutrns unexpected signature for thread!");
                        limitedPrintln("##         Thread name = '" + threadNamnes[i] + "'");
                        limitedPrintln("##         Returned source file = '" + source + "'");
                        limitedPrintln("##         Expected source file = '" + expectedSource + "'");
                        foundErrors++;
                    }

                } else {
                    foundErrors++;
                }
            }
        }
        if ( foundErrors > 0 ) {
            success = 0;
        }
        return success;
    }

    public int setArray(String arrayName, long[] objectArray,
            String DEBUGGEE_SIGNATURE) {
        int success = 0;
        logWriter
                .println("==> Set objects from one debuggee array to another...");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror
                .getClassID(DEBUGGEE_SIGNATURE);
        if (debuggeeRefTypeID == -1) {
            logWriter.println("## FAILURE: Can NOT get debuggeeRefTypeID");
            return -1;
        }
        long arrayID = getArraIDForStaticArrayField(debuggeeRefTypeID, arrayName);
        if ( arrayID == BAD_ARRAY_ID ) {
            return -1;   
        }

        long commandStartTimeMlsec = System.currentTimeMillis();
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.SetValuesCommand);
        packet.setNextValueAsArrayID(arrayID);
        packet.setNextValueAsInt(0);
        packet.setNextValueAsInt(objectArray.length);

        for (int i = 0; i < objectArray.length; i++) {
            packet.setNextValueAsObjectID(objectArray[i]);
        }
        int[] expectedErrors = { JDWPConstants.Error.NONE,
                JDWPConstants.Error.OUT_OF_MEMORY };
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        if (checkReplyForError(reply, expectedErrors,
                "ArrayReference::SetValues command", LIMITED_PRINT)) {
            success = -1;
        }

        long commandRunTimeMlsec = System.currentTimeMillis()
                - commandStartTimeMlsec;
        logWriter.println("==> command running time(mlsecs) = "
                + commandRunTimeMlsec);
        return success;
    }

}

