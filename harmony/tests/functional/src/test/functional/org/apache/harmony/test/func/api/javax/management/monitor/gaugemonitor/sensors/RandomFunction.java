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

import java.util.Random;

import org.apache.harmony.share.Base;

public class RandomFunction implements RandomFunctionMBean, Runnable {

    public static final String SENSOR_NAME_TEMPLATE = "org.apache.harmony.test.func."
            + "api.javax.management.monitor.gaugemonitor:type=Sensor,id=";

    private double value;

    public static boolean isHigh;

    public static boolean isNotify = false;

    public static boolean stop = false;

    private final Object sync;

    public RandomFunction(Object prpsdSync) {
        this.sync = prpsdSync;
    }

    public double getValue() {
        return value;
    }

    private void note(double val) {
        if (isNotify) {
            if (isHigh && (val > 25)) {
                Base.log.add("RandomFunction: High value reached");
            }
            if (!isHigh && (val < -25)) {
                Base.log.add("RandomFunction: Low value reached");
            }
        }
    }

    public void run() {
        Random random = new Random();
        while (!stop) {
            try {
                value = 50 - (100 * random.nextDouble());
                // System.out.print(value + " ");
                note(value);
                Thread.sleep(100);
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
