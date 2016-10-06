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
package org.apache.harmony.test.func.api.java.lang.F_ThreadGroupTest_01;

import java.util.Random;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * Created on 19.11.2004
 * 
 *    Usage: 
 *        java.lang.ThreadGroup 
 *
 **/

public class F_ThreadGroupTest_01 extends ScenarioTest
{
    public ThreadGroup parentThreadGroup = null;
    public ThreadGroup firstChildThreadGroup = null;
    public ThreadGroup secondChildThreadGroup = null;

    public static int intResult = 0; 
    public int task1() {
        try {
            parentThreadGroup = new ThreadGroup("parentThreadGroup");
            firstChildThreadGroup = new ThreadGroup(parentThreadGroup, "firstChildThreadGroup");
            Thread myFirstThread = new Thread(firstChildThreadGroup, new myRunnable("First"));
            secondChildThreadGroup = new ThreadGroup(parentThreadGroup, "secondChildThreadGroup");
            Thread mySecondThread = new Thread(secondChildThreadGroup, new myRunnable("Second"));
            myFirstThread.start();
            mySecondThread.start();
        } catch(Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass("task1 passed");
    }
    public int test()
    {
        try {
            if (task1() != Result.PASS) 
                return fail("test NOT passed");
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public static void main(String[] args)
    {
        System.exit(new F_ThreadGroupTest_01().test(args));
    }
    public class myRunnable implements Runnable 
    {
        private String name;

        public myRunnable (String strName) {
            name = strName;
        }
        public void run() {
            try {
                ThreadGroup g = null;
                   SecurityManager security = System.getSecurityManager();
                   if (security != null) {
                       g = security.getThreadGroup();
                   }
                   if (g == null) {
                       g = Thread.currentThread().getThreadGroup();
                   }
                   g.checkAccess();
                   log.info(name+" thread from group "+g.getName()+ " which is subgroup of "+g.getParent().getName()+" started");
                   while(true) {
                       nextStep();
                       Thread.sleep(1);
                   }
            } catch (Exception e) {
                error("test failed - " + e.getMessage());
            }
        }
        private void nextStep() {
            synchronized(F_ThreadGroupTest_01.class) {
                if(intResult<=100) {
                    Random rndInt = new Random();
                    intResult = intResult + rndInt.nextInt(10);
                    if(intResult>100) {
                        log.info(name+" thread win with result "+intResult);
                        Thread.currentThread().getThreadGroup().getParent().interrupt();
                    } else {
                        log.info(name+" thread result "+intResult);
                    }
                } else {
                    log.info(name+" thread lose");
                }
            }
        }
    }
}