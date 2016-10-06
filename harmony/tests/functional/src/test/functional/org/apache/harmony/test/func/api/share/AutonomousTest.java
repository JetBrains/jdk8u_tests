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
/**
 */

package org.apache.harmony.test.func.api.share;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.harmony.share.MultiCase;

public abstract class AutonomousTest extends MultiCase {
    /* 4 seconds, gets overridden in parsArgs() with value from $paintTimeout */
    private static int       paintTimeout = 4000;

    private final static int sleepTime    = 1000; /* in milliseconds */
    protected HashMap          cmdArgs      = null;

    public static int getPaintTimeout() {
        return paintTimeout;
    }

    protected void parseArgs(String[] args) {
        super.parseArgs(args);
        HashMap allParams = new HashMap();
        for (int i = 0; i < args.length - 1; i += 2) {
            allParams.put(args[i], args[i + 1]);
        }
        cmdArgs = (HashMap) allParams.clone();

        try {
            AutonomousTest.paintTimeout = (new Integer((String) allParams
                    .get("-paintTimeout"))).intValue();
        } catch (NumberFormatException e) {
            AutonomousTest.paintTimeout = 4000;
            e.printStackTrace();
        }        
    }

    protected static void waitEventQueueToClear() {
        try {
            /* Wait for all events to be processed */
            EventQueue.invokeAndWait(new Thread());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected static void sleep(int time) {
        try {
            /* Wait to track the visual control */
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected static void sleep() {
        sleep(sleepTime);
    }
}