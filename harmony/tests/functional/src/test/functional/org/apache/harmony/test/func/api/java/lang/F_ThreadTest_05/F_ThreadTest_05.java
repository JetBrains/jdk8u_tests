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
 * Created on 27.01.2005
 * Last modification G.Seryakova
 * Last modified on 27.01.2005
 * 
 * Set, get ContextClassLoader for threads.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_ThreadTest_05;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.net.URL;
import java.io.*;

/**
 * Set, get ContextClassLoader for threads.
 * 
 */
public class F_ThreadTest_05 extends ScenarioTest {
    int stat = 0;

    public static void main(String[] args) {
        System.exit(new F_ThreadTest_05().test(args));
    }

    class MyThread extends Thread {

        public MyThread(ThreadGroup group, String name) {
            super(group, name);
        }

        public void run() {
            ClassLoader loader = getContextClassLoader();

            if (loader == null) {
                log.info(getName() + " - null");
            } else {
                URL res = loader.getResource("input.txt");
                String strResult = "";
                try {
                    BufferedReader inbr = new BufferedReader(new InputStreamReader(res.openStream()));                    
                    strResult = inbr.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }                
                log.info(getName() + " - " + getContextClassLoader().toString()
                    + " - " + strResult);
            }
            incStat();
        }
    }

    public int test() {
        ThreadGroup group = new ThreadGroup("My Group");
        Thread arr[] = new Thread[20];

        for (int i = 0; i < 5; i++) {
            arr[i] = new Thread(group, "Thread-" + i);
            arr[i].start();
        }

        for (int i = 5; i < 10; i++) {
            arr[i] = new Thread(group, "Thread-" + i);
            arr[i].run();
        }

        for (int i = 10; i < 15; i++) {
            arr[i] = new MyThread(group, "Thread-" + i);
            arr[i].setContextClassLoader(null);
            arr[i].start();
        }

        for (int i = 15; i < 20; i++) {
            arr[i] = new MyThread(group, "Thread-" + i);
            arr[i].setContextClassLoader(ClassLoader.getSystemClassLoader());
            arr[i].start();
        }

        joinGroup(group);

        if (stat != 10) {
            return fail("One of child threads has error.");
        }
        return pass();
    }

    private synchronized void incStat() {
        stat++;
    }
    
    private void joinGroup(ThreadGroup group) {
        Thread thr[] = new Thread[20];
        group.enumerate(thr);
        for (int i = 0; i < 20; i++) {
            if (thr[i] != null) {
                thr[i].checkAccess();
                log.info(thr[i].getName());
                try {
                    thr[i].join();
                } catch (InterruptedException e) {
                }
            }
        }

    }
}