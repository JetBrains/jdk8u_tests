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
/*
 * Created on 16.03.2005
 * Last modification G.Seryakova
 * Last modified on 16.03.2005
 * 
 * Test for access to system properties in threads. For classes: System, Integer, Boolean, Long.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_SystemTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Test for access to system properties in threads. For classes: System,
 * Integer, Boolean, Long.
 * 
 */
public class F_SystemTest_03 extends ScenarioTest {
    int ind = 0;

    public static void main(String[] args) {
        System.exit(new F_SystemTest_03().test(args));
    }

    class MyThread extends Thread {
        public void run() {
            String expect = "false,true,false,|"
                    + "null,null,0,1,2147483647,-2147483648,null,null,|"
                    + "-1,-1,0,1,2147483647,-2147483648,-1,-1,|"
                    + "-2,-2,0,1,2147483647,-2147483648,-2,-2,|"
                    + "null,null,0,1,2147483647,-2147483648,9223372036854775807,-9223372036854775808,|"
                    + "-3,-3,0,1,2147483647,-2147483648,9223372036854775807,-9223372036854775808,|"
                    + "-4,-4,0,1,2147483647,-2147483648,9223372036854775807,-9223372036854775808,";
            StringBuffer res = new StringBuffer();
            for (int i = 0; i < 3; i++) {
                res.append(Boolean.getBoolean(i + "SystemTest_03") + ",");
            }
            res.append('|');
            for (int i = 0; i < 8; i++) {
                res.append(Integer.getInteger(i + "SystemTest_03") + ",");
            }
            res.append('|');
            for (int i = 0; i < 8; i++) {
                res.append(Integer.getInteger(i + "SystemTest_03", -1) + ",");
            }
            res.append('|');
            for (int i = 0; i < 8; i++) {
                res.append(Integer.getInteger(i + "SystemTest_03", new Integer(-2))+ ",");
            }
            res.append('|');
            for (int i = 0; i < 8; i++) {
                res.append(Long.getLong(i + "SystemTest_03") + ",");
            }
            res.append('|');
            for (int i = 0; i < 8; i++) {
                res.append(Long.getLong(i + "SystemTest_03", -3) + ",");
            }
            res.append('|');
            for (int i = 0; i < 8; i++) {
                res.append(Long.getLong(i + "SystemTest_03", new Long(-4)) + ",");
            }
            if (!expect.equals(res.toString())) {
                log.info("result is " + res.toString()
                        + ", but expected result is " + expect);
            } else {
                inc();
            }
        }
    }

    public int test() {
        System.setProperty("1SystemTest_03", "true");
        System.setProperty("2SystemTest_03", "0");
        System.setProperty("3SystemTest_03", "1");
        System.setProperty("4SystemTest_03", Integer.toString(Integer.MAX_VALUE));
        System.setProperty("5SystemTest_03", Integer.toString(Integer.MIN_VALUE));
        System.setProperty("6SystemTest_03", Long.toString(Long.MAX_VALUE));
        System.setProperty("7SystemTest_03", Long.toString(Long.MIN_VALUE));

        Thread arr[] = new MyThread[5];
        log.info("1");
        for (int i = 0; i < 5; i++) {
            arr[i] = new MyThread();
            arr[i].start();
        }
        log.info("2");
        for (int i = 0; i < 5; i++) {
            try {
                arr[i].join();
            } catch (InterruptedException e) {
                fail("thread " + i + "was interrupted.");
            }
        }
        log.info("3");
        if (ind == 5) {
            return pass();
        } else {
            return fail("");
        }
    }

    private synchronized void inc() {
        ind++;
    }
}