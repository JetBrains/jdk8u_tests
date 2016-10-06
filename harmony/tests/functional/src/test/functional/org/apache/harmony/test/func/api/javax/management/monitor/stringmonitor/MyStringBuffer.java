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
package org.apache.harmony.test.func.api.javax.management.monitor.stringmonitor;

/**
 * "Guinea-pig" for StringMonitor tests
 * 
 */

public class MyStringBuffer implements MyStringBufferMBean, Runnable {

    public static final String MSB_NAME_TEMPLATE = "org.apache.harmony.test.func."
            + "api.javax.management.monitor.stringmonitor:type=MyStringBuffer,id=";

    private String value = "match";

    public String getString() {
        return value;
    }

    public void setString(String s) {
        value = s;
    }

    public void run() {
        try {
            Thread.sleep(200);
            value = "match";
            Thread.sleep(200);
            value = "differ";
            Thread.sleep(200);
            value = "match";
            Thread.sleep(200);
            value = "mat" + "ch";
            Thread.sleep(200);
            value = "Match";
            Thread.sleep(200);
            value = "";
            Thread.sleep(200);
            value = "match ";
            Thread.sleep(200);
            value = " match ";
            Thread.sleep(200);
            value = "differ";
            Thread.sleep(200);
            value = "match";
            Thread.sleep(200);
            System.err.println("MyStringBuffer: Tape finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
