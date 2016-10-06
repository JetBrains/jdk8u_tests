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
package org.apache.harmony.test.func.api.java.lang.F_ThreadTest_01;

import java.io.*;
import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * Created on 12.11.2004
 * 
 *    Usage: 
 *        java.lang.Thread 
 *
 **/

public class F_ThreadTest_01 extends ScenarioTest {
    private String firstInputFileName;
    public Thread myFirstThread = null;
    public Thread mySecondThread = null;
    public int intCounter = 0;
    public boolean blnInterrupt = false;
    public int task1() {
        Thread myObject1 = new myClass1();
        Thread myObject2 = new myClass2();
        myObject1.setPriority(Thread.MIN_PRIORITY);
        myObject2.setPriority(Thread.MAX_PRIORITY);
        try {
            myObject1.start();
            myObject2.start();
            while (myObject1.isAlive()) { 
                //..
            }
        } catch(Exception e) {
            return error("test failed - " + e.getMessage());
        }
        if (myObject2.isInterrupted())
            return error("test failed - file reading error");
        return pass("task1 passed");
    }
    public int task2()
    {
        return pass("task2 passed");
    }
    public int test()
    {
        firstInputFileName = testArgs[0];
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
        System.exit(new F_ThreadTest_01().test(args));
    }

    public class myClass1 extends Thread  
    {
        public void run() {
            String str;
            while (true) {
                try {
                    if(blnInterrupt){
                        log.info("Counter stopped");
                        return;
                    }
                    intCounter = intCounter + 1;
                    str=""+intCounter;
                    log.info(str);
                    Thread.sleep(1);
                } catch(InterruptedException e) {
                    error("test failed - " + e.getMessage());
                }
            }
        }
    }
    public class myClass2 extends Thread  
    {
        public void run(){
            try {
                FileInputStream in = new FileInputStream(firstInputFileName);
                BufferedReader inbr = new BufferedReader(new InputStreamReader(in));
                String strResult = "";
                String line;
                while(true) {
                    if((line = inbr.readLine()) != null) {
                        strResult = strResult + line + "\r\n";
                        Thread.sleep(1);
                    }else{
                        blnInterrupt = true;
                        log.info("File size = "+strResult.length());
                        return;
                    }
                }
            } catch (Exception e) {
                blnInterrupt = true;
                interrupt();
            }
        }
    }
}
