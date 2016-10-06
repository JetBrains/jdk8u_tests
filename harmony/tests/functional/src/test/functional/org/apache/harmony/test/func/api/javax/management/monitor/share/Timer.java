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
package org.apache.harmony.test.func.api.javax.management.monitor.share;

import org.apache.harmony.share.Base;

/**
 * Timer class sleeps for definite sleepTime(in milliseconds) and then ends
 * test.
 * 
 */

public class Timer extends Thread {

    private final long sleepTime;

    private final Object sync;

    private boolean res = true;

    public Timer(long proposedSleepTime, Object s) {
        this.sync = s;
        this.sleepTime = proposedSleepTime;
    }

    public boolean getRes() {
        return res;
    }

    public void run() {
        try {
            Thread.sleep(sleepTime);
            Base.log.add("Timer: Sleep time is over.");
            res = false;
            synchronized (sync) {
                sync.notify();
            }
        } catch (InterruptedException e) {
            res = true;
            return;
        } catch (Throwable e) {
            res = false;
            Base.log.add("Timer: ERROR: Internal Exception!");
            e.printStackTrace();
            synchronized (sync) {
                sync.notify();
            }
            return;
        }
    }
}
