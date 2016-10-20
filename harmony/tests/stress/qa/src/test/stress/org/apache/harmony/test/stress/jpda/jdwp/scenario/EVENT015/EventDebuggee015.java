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
 * Created on 15.08.2006 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.EVENT015;

import org.apache.harmony.test.stress.jpda.jdwp.scenario.share.StressDebuggee;

public class EventDebuggee015 extends StressDebuggee {
    public static final String TESTED_THREAD_NAME = "EventDebuggee015_Thread"; 

    static EventDebuggee015 eventDebuggee015This;

    static volatile boolean threadToFinish = false;

    static EventDebuggee015_Thread eventDebuggee015Thread = null;

    public void run() {
        
        logWriter.println("--> EventDebuggee015: START...");
        eventDebuggee015This = this;

        logWriter.println("--> EventDebuggee015: Create EventDebuggee015_Thread...");
        eventDebuggee015Thread = new EventDebuggee015_Thread(TESTED_THREAD_NAME);
        logWriter.println("--> EventDebuggee015: EventDebuggee015_Thread created!");

        printlnForDebug("EventDebuggee015: sendSignalAndWait(SIGNAL_READY_01)");
        sendSignalAndWait(SIGNAL_READY_01);
        printlnForDebug("EventDebuggee015: After sendSignalAndWait(SIGNAL_READY_01)");

        logWriter.println("--> EventDebuggee015: Start EventDebuggee015_Thread...");
        EventDebuggee015_StarterThread starter = new EventDebuggee015_StarterThread();
        starter.start();
        
        while ( ! eventDebuggee015Thread.isAlive() ) {
            waitMlsecsTime(100);
        }
        logWriter.println("--> EventDebuggee015: EventDebuggee015_Thread started!");

        printlnForDebug("EventDebuggee015: sendThreadSignalAndWait(SIGNAL_READY_02)");
        sendThreadSignalAndWait(SIGNAL_READY_02);
        printlnForDebug("EventDebuggee015: After sendThreadSignalAndWait(SIGNAL_READY_02)");

        logWriter.println
        ("--> EventDebuggee015: Send signal to EventDebuggee015_Thread to finish...");
        threadToFinish = true;

        printlnForDebug("EventDebuggee015: sendThreadSignalAndWait(SIGNAL_READY_03)");
        sendThreadSignalAndWait(SIGNAL_READY_03);
        printlnForDebug("EventDebuggee015: After sendThreadSignalAndWait(SIGNAL_READY_03)");

        logWriter.println("--> EventDebuggee015: FINISH...");

    }

    public static void main(String [] args) {
        runDebuggee(EventDebuggee015.class);
    }

}

class EventDebuggee015_Thread extends Thread {

    public EventDebuggee015_Thread(String name) {
        super(name);
    }
    
    public void run() {
        while ( ! EventDebuggee015.threadToFinish ) {
            EventDebuggee015.waitMlsecsTime(100);
        }
    }
}

class EventDebuggee015_StarterThread extends Thread {
    
    public void run() {
        EventDebuggee015.eventDebuggee015Thread.start();
    }
}


