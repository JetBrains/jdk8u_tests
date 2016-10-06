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

public class RandomThree implements RandomThreeMBean, Runnable {

    public static final String SENSOR_NAME_TEMPLATE = "org.apache.harmony.test.func."
            + "api.javax.management.monitor.gaugemonitor:type=Sensor,id=";

    private double value;

    public static boolean isHigh;

    public static boolean stop;

    private final Object sync;

    public RandomThree(Object prpsdSync) {
        this.sync = prpsdSync;
    }

    public double getValue() {
        return value;
    }

    public void run() {
        Random random = new Random();
        while (!stop) {
            try {
                switch (random.nextInt(3)) {
                case 0:
                    value = 4.999999999999999d;
                    if (!isHigh) {
                        Base.log.add("RandomThree: Low value reached");
                    }
                    break;
                case 1:
                    value = 5;
                    break;
                case 2:
                    value = 5.000000000000001d;
                    if (isHigh) {
                        Base.log.add("RandomThree: High value reached");
                    }
                    break;
                }
                // System.out.print(value + " ");
                Thread.sleep(500);
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
