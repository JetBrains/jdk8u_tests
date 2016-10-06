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
package org.apache.harmony.test.func.api.java.lang.F_ThreadLocalTest_01.auxiliary;

import java.util.*;
import org.apache.harmony.share.DRLLogging;

public class ThreadLocalClass_02
{
    private static DRLLogging logger;
    
    public ThreadLocalClass_02 (DRLLogging log) {
        logger = log;
    }
    
    private static class MyThreadLocal extends ThreadLocal {
        Random random = new Random();
        protected Object initialValue() {
            if (super.initialValue() == null) {
                return new Integer(random.nextInt(1000));
            } else {
                logger.info("Default implementation of initialValue() return not null");
                return new Integer(-1);
            }
        }
    }

    static ThreadLocal threadLocal = new MyThreadLocal();

    static volatile int counter = 0;

    static Random random = new Random();

    private static void displayValues() {
        logger.info(threadLocal.get() + "\t" + counter + "\t" + Thread.currentThread().getName());
    }
   
    public void start() {
        Runnable runner = new Runnable() {
            public void run() {
                synchronized(ThreadLocalClass_02.class) {
                    counter++;
                }
                displayValues();
                try {
                    Thread.sleep (((Integer)threadLocal.get()).intValue());
                    displayValues();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        displayValues();
        Thread t[] = new Thread[5];
        for (int i=0; i<5; i++) {
            t[i] = new Thread(runner);
            t[i].start();
        }
        try {
            for (int i=0; i<5; i++) {
                t[i].checkAccess();
                t[i].join();
            }
        } catch (InterruptedException e){            
        }
    }
}
