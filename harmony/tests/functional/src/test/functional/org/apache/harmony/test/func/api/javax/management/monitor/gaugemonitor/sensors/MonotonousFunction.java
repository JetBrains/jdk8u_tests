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
package org.apache.harmony.test.func.api.javax.management.monitor.gaugemonitor.sensors;

import org.apache.harmony.share.Base;

public class MonotonousFunction implements MonotonousFunctionMBean, Runnable {

    public static final String SENSOR_NAME_TEMPLATE = "org.apache.harmony.test.func."
            + "api.javax.management.monitor.gaugemonitor:type=Sensor,id=";

    private final int sleepTime;

    private double value = -11;

    private final Object sync;
    
    public MonotonousFunction() {
        this.sleepTime = 1000;
        this.sync = null;
    }

    public MonotonousFunction(Object prpsdSync, int prpsdSleepTime) {
        this.sleepTime = prpsdSleepTime;
        this.sync = prpsdSync;
    }

    public void setValue(double proposedValue) {
        this.value = proposedValue;
    }

    public double getValue() {
        return value;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(sleepTime);
                value += 0.1;
                // System.out.print(value+" ");
            } catch (InterruptedException e) {
                Base.log.add("Monotonous Function: Thread Interrupted.");
                e.printStackTrace();
                synchronized (sync) {
                    sync.notify();
                }
            }

        }
    }

}
