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

import java.util.Random;

import org.apache.harmony.test.func.api.javax.management.monitor.share.Predictable;
import org.apache.harmony.share.Base;

/**
 * Randomizer object used in CounterMonitorDifferenceTest.
 * 
 * It's implements runnable interface and when runs, produces random monotonous
 * sequence of numbers (if CounterMonitorDifferenceTest.isMonotonous = true) or
 * random seed of numbers (if CounterMonitorDifferenceTest.isMonotonous =
 * false). It's can be stopped any time and notifies if increment is higher than
 * expected threshold value. For more information, see
 * CounterMonitorDifferenceTest description.
 * 
 */

public class Randomizer implements RandomizerMBean, Runnable, Predictable {

    /**
     * Object name template for randomizer
     */
    public static final String RANDOMIZER_NAME = "org.apache.harmony.test.func."
            + "api.javax.management.monitor.countermonitor:type=Randomizer,id=";

    /**
     * Default boundary value for randomizer
     */
    public static final int DEFAULT_THRESHOLD = 12;

    private final boolean isMonotonous;

    private final int sleepTime;

    private int boundary;

    private int increment;

    private int value;

    private boolean stop = false;

    public Randomizer(int bndr, boolean prpsdIsMonotonous, int prpsdSleepTime) {
        this.isMonotonous = prpsdIsMonotonous;
        this.sleepTime = prpsdSleepTime;
        boundary = bndr;
        value = 0;
    }

    public Randomizer(boolean prpsdIsMonotonous) {
        this.isMonotonous = prpsdIsMonotonous;
        boundary = DEFAULT_THRESHOLD;
        this.sleepTime = 1000;
        value = 0;

    }
    
    public Randomizer(boolean prpsdIsMonotonous, int proposedSleepTime) {
        this.isMonotonous = prpsdIsMonotonous;
        boundary = DEFAULT_THRESHOLD;
        this.sleepTime = proposedSleepTime;
        value = 0;

    }

    public void run() {
        Random random = new Random();
        while (!stop) {
            try {
                Thread.sleep(sleepTime);
                int localIncrement;
                if (isMonotonous) {
                    localIncrement = random.nextInt(boundary) + (boundary / 2);
                    value = value + localIncrement;
                    // System.out.print(value + " ");
                } else {
                    localIncrement = value;
                    value = random.nextInt(boundary * 2) - boundary;
                    // System.out.print(value + " ");
                    if ((localIncrement < 0) && (value > 0)) {
                        localIncrement = Math.abs(localIncrement) + value;
                        // System.out.println(localIncrement + "=LI");
                    }
                }
                if (localIncrement >= boundary) {
                    Base.log.info("Randomizer: Threshold Reached. "
                            + "Increment=" + localIncrement + " Threshold="
                            + boundary);
                    increment = localIncrement;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void stopCounter() {
        stop = true;
    }

    public void setIncrement(Number proposed) {
        increment = proposed.intValue();
    }

    public Number getIncrement() {
        return new Integer(increment);
    }

    public int getValue() {
        return value;
    }

    public int getThreshold() {
        return boundary;
    }
}
