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
package org.apache.harmony.test.func.jit.HLO.peel.Synchronize1;

import java.util.ArrayList;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 19.06.2006 
 */

public class Synchronize1 extends Test {
    
    private static final int count = 50;
    public static ThreadGroup group = new ThreadGroup("TestGroup");
    public static ArrayList list = new ArrayList(count);
    
    public static void main(String[] args) {
        System.exit(new Synchronize1().test(args));
    }

    public int test() {
        log.info("Start Synchronize1 test ...");
        TestThread[] threads = new TestThread[count];
        for(int i=0; i<count; i++) {
            list.add(new Integer(i));
            list.add(new Integer(i+1));
            list.add(new Integer(i+2));
            list.add(new Integer(i+3));
        }
        log.info("Initial list size: " + list.size());
        try {
            for(int i=0; i<count; i++) {
                threads[i] = new TestThread(group, i);
            }
            for(int i=0; i<count; i++) {
                threads[i].start();
            }
            for(int i=0; i<count; i++) {
                threads[i].join();
            }
            list.trimToSize();
            log.info("Produced list size: " + list.size());
                
            if (TestThread.error) {
                log.info("Unexpected " + TestThread.throwable + 
                        " occurred in thread " + TestThread.name);
                log.add(TestThread.throwable);
                return fail("TEST FAILED");
            } else {
                if (list.size() != 0) return fail("TEST FAILED: " +
                "Produced list size != 0");
                return pass();
            }
            
        } catch (Throwable e) {
            log.info("Unexpected " + e + " occurred");
            log.add(e);
            return fail("TEST FAILED");
        }
    }

}

class TestThread extends Thread {
    
    public static boolean error = false;
    public static Throwable throwable;
    public static int name;
    public static int threads = 0;
    
    private int num;
    
    public TestThread(ThreadGroup group, int num) {
        super(group, "");
        this.num = num;
    }
    
    public void run() {
        try {
            for(int j=0; j<100000; j++) {
                synchronized(Synchronize1.list) {
                    if (Synchronize1.list.size() > 0) {
                        Synchronize1.list.remove(new Integer(num));
                        Synchronize1.list.remove(new Integer(num+1));
                        Synchronize1.list.remove(new Integer(num+2));
                        Synchronize1.list.remove(new Integer(num+3));
                    } else {
                        break;
                    }
                }
            }
        } catch (Throwable e) {
            error = true;
            throwable = e;
            name = num;
            Synchronize1.group.interrupt();
        }
    }
}
