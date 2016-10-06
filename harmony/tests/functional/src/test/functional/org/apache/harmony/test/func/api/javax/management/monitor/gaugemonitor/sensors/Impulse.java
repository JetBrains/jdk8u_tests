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

public class Impulse implements ImpulseMBean, Runnable {

    public static final String SENSOR_NAME_TEMPLATE = "org.apache.harmony.test.func."
            + "api.javax.management.monitor.gaugemonitor:type=Sensor,id=";

    private int sleepTime;

    private int leapTime;

    private Number leapValue;

    private Number sleepValue;

    private Number value;

    private boolean isPause = false;

    private final Object obj = new Object();

    public Impulse(int prpsdSleepTime, int prpsdLeapTime,
            Number prpsdSleepValue, Number prpsdLeapValue) {
        this.sleepValue = prpsdSleepValue;
        this.sleepTime = prpsdSleepTime;
        this.leapTime = prpsdLeapTime;
        this.leapValue = prpsdLeapValue;
        value = prpsdLeapValue;
    }

    public void setSleepValue(Number proposedValue) {
        this.sleepValue = proposedValue;
    }

    public Number getSleepValue() {
        return this.sleepValue;
    }

    public void setPause(boolean offer) {
        this.isPause = offer;
        if (!offer) {
            synchronized (obj) {
                obj.notify();
            }
        }
    }

    public void setLeapValue(Number proposedValue) {
        this.leapValue = proposedValue;
    }

    public Number getLeapValue() {
        return this.leapValue;
    }

    public void setValue(Number proposedValue) {
        this.value = proposedValue;
    }

    public Number getValue() {
        return this.value;
    }

    public void run() {
        try {
            if (isPause) {
                synchronized (obj) {
                    obj.wait();
                }
            }
            Thread.sleep(sleepTime / 2);
            while (true) {
                // System.out.print("^" + leapValue + " ");
                value = leapValue;
                Thread.sleep(leapTime);
                if (isPause) {
                    synchronized (obj) {
                        obj.wait();
                    }
                }
                // System.out.print("v" + sleepValue + " ");
                value = sleepValue;
                Thread.sleep(sleepTime);
                if (isPause) {
                    synchronized (obj) {
                        obj.wait();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
