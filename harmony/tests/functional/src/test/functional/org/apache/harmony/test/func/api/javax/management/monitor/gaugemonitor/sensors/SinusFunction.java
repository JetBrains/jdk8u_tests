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

public class SinusFunction implements SinusFunctionMBean, Runnable {

    public static final String SENSOR_NAME_TEMPLATE = "org.apache.harmony.test.func."
            + "api.javax.management.monitor.gaugemonitor:type=Sensor,id=";

    private final int sleepTime;

    private double value = 0;

    private final Object sync;

    public SinusFunction(Object prpsdSync, int prpsdSleepTime) {
        this.sync = prpsdSync;
        this.sleepTime = prpsdSleepTime;
    }

    public Short getShort() {
        return new Short((short) (100 * value));
    }

    public Integer getInteger() {
        return new Integer((int) (100 * value));
    }

    public Byte getByte() {
        return new Byte((byte) (100 * value));
    }

    public double getDouble() {
        return value;
    }

    public Long getLong() {
        return new Long((long) (100 * value));
    }

    public Float getFloat() {
        return new Float(value);
    }

    public void run() {
        double time = 0;
        while (true) {
            try {
                Thread.sleep(sleepTime);
                // double previous = value;
                value = Math.sin(time += (Math.PI / 12));
                // System.out.print(value - previous + " ");
            } catch (InterruptedException e) {
                Base.log.add("Periodic Function: Thread Interrupted.");
                e.printStackTrace();
                synchronized (sync) {
                    sync.notify();
                }
            }

        }
    }

}
