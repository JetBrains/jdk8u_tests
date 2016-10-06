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
package org.apache.harmony.test.func.api.javax.management.monitor.countermonitor;

import org.apache.harmony.test.func.api.javax.management.monitor.share.Predictable;
import org.apache.harmony.share.Base;

/**
 * This is counter class, Intended for javax.management.monitor.CounterMonitor
 * testing. It's implements runnable interface and when runs, Counter.value
 * increases to 1. Counter can rollover to rollOverValue value when exceeds
 * boundary value. It's also can be stopped any time.
 * 
 */

public class Counter implements CounterMBean, Runnable, Predictable {

    /** This String uses as ObjectName template */
    public static final String COUNTER_NAME_TEMPLATE = "org.apache.harmony.test.func."
            + "api.javax.management.monitor.countermonitor:type=Counter,id=";

    /**
     * Default threshold value used in other classes for representing default
     * fields
     */
    public static final int DEFAULT_THRESHOLD = 13;

    /**
     * Counter ticks every sleepTime milliseconds
     */
    private final int sleepTime;

    /**
     * Flag for counter to stop
     */
    private boolean stop = false;

    /**
     * Counter rollovers to this value
     */
    private int rollOverFrom = -1;

    private int rollOverTo = -1;

    /**
     * ? value to change
     */
    private int value = 0;

    private int increment = 0;

    /**
     * Constructor, defines both "tick" time and start value
     * 
     * @param startValue
     * @param proposedSleepTime
     */
    public Counter(int startValue, int proposedSleepTime) {
        this.sleepTime = proposedSleepTime;
        this.value = startValue;
    }

    /**
     * Constructor, defines "tick" time only.
     * 
     * @param proposedSleepTime
     */
    public Counter(int proposedSleepTime) {
        this.sleepTime = proposedSleepTime;
        this.value = 0;
    }

    /**
     * Constructor, uses default parameters (sleepTime = 1000, value = 0)
     */
    public Counter() {
        this.sleepTime = 1000;
        this.value = 0;
    }

    /**
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * setter for the Value parameter. Defines increment too due to proposed
     * value can be higher than current increment
     * 
     * @param proposedValue
     */
    public void setValue(int proposedValue) {
        this.value = proposedValue;
        this.increment = proposedValue;
    }

    /**
     * Setter for the rollOver values.
     * 
     * @param from
     * @param to
     */
    public void setRollOverValue(int from, int to) {
        if ((from > 0) && (to >= from)) {
            Base.log.add("Counter: RolloverValue is incorrect. "
                    + "Setting default (0) value.");
        } else {
            this.rollOverFrom = from;
            this.rollOverTo = to;
        }
    }

    public void setIncrement(Number prpsdIcrement) {
        this.increment = prpsdIcrement.intValue();
    }

    public Number getIncrement() {
        return new Integer(increment);
    }

    public void run() {
        while (!stop) {
            try {
                if ((rollOverTo >= 0) && (value >= rollOverFrom)) {
                    Base.log.add("Counter: Counter rollovers to " + rollOverTo
                            + ". Modulus=" + rollOverFrom);
                    Thread.sleep(1000);
                    value = rollOverTo;
                }
                Thread.sleep(sleepTime);
                value++;
                // System.out.print(value + " ");
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Method for counter to stop
     */
    public void StopCounter() {
        this.stop = true;
        this.value = 0;
    }
}