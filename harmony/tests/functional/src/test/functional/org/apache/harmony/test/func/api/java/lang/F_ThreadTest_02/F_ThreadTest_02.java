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
 * Created on 12.11.2004
 */
package org.apache.harmony.test.func.api.java.lang.F_ThreadTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * 
 *    Usage: 
 *        java.lang.Thread 
 *
 **/

public class F_ThreadTest_02 extends ScenarioTest {
    public long lngResult;
    public int task1() {
        try 
        {
            lngResult = 0;
            Runnable r = new MyRunnable();
            Thread myThread = new Thread(r);
            myThread.start();
            while(myThread.isAlive()) {
                if(lngResult>30000) {
                    if(!myThread.isInterrupted()) {
                        log.info(Long.toString(lngResult));
                        myThread.interrupt();
                        log.info("Thread stopping");
                        break;
                    }
                }
            }
            log.info("Thread stopped");
        } 
        catch (Exception e) 
        {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }
    public int task2() {
        try 
        {
            lngResult = 0;
            Runnable r = new MyRunnable();
            Thread myThread = new Thread(r, "myThread");
            myThread.start();
            while(myThread.isAlive()) {
                if(lngResult>30000) {
                    log.info(Long.toString(lngResult));
                    myThread.interrupt();
                    break;
                }
            }
            while(myThread.isInterrupted()) {
                log.info("Thread '"+myThread.getName()+"' interrupting");
                if(!myThread.isAlive())
                    log.info("Thread stopped");
                    return pass();
            }
        } 
        catch (Exception e) 
        {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }
    public int test()
    {
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
        System.exit(new F_ThreadTest_02().test(args));
    }
    public class MyRunnable implements Runnable {
        public void run() {
           log.info("Thread started");
           for (int i=0; i<1000; i++) {
               lngResult+=i;
           }
        }
     } 
}