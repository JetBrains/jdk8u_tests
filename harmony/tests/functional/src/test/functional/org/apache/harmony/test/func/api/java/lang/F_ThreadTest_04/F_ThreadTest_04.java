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
package org.apache.harmony.test.func.api.java.lang.F_ThreadTest_04;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * Created on 12.11.2004
 * 
 *    Usage: 
 *        java.lang.Thread 
 *
 **/

public class F_ThreadTest_04 extends ScenarioTest {

    public int task1()
    {
        try {
            new PingPong("ping", 100).start();
            new PingPong("PONG", 100).start();
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass("task1 passed");
    }
    public int task2()
    {
        try {
            Runnable ping = new RunPingPong("ping", 100);
            Runnable pong = new RunPingPong("PONG", 100);
            Thread firstThread = new Thread(ping);
            Thread secondThread = new Thread(pong);
            firstThread.start();
            secondThread.start();
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass("task2 passed");
    }

    public int test() {
        try 
        {
            if (task1() != Result.PASS || task2() != Result.PASS) 
                return fail("test NOT passed");
        } 
        catch (Exception e) 
        {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public static void main(String[] args)
    {
        System.exit(new F_ThreadTest_04().test(args));
    }

    class PingPong extends Thread {
        String word;
        int delay;
        PingPong(String whatToSay, int delayTime) {
             word = whatToSay;
             delay = delayTime;
        }

        public void run() {
            try {
                for (int i=0;i<=10;i++) {
                    log.info(word + " ");
                    sleep(delay);
                }
             } catch (InterruptedException e) {
                  return;
             }
        }
   }
    class RunPingPong implements Runnable {
        String word;
        int delay;
        RunPingPong(String whatToSay, int delayTime) {
             word = whatToSay;
             delay = delayTime;
        }

        public void run() {
             try {
                 for (int i=0;i<=10;i++) {
                     log.info(word + " ");
                     Thread.sleep(delay); 
                 }
             } catch (InterruptedException e) {
                 return;
             }
        }
    }
}
