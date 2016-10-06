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
 * Created on 20.10.2005
 * Last modification G.Seryakova
 * Last modified on 07.11.2005
 * 
 * Test for threads.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_ThreadTest_08;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Test for threads.
 * 
 */
public class F_ThreadTest_08 extends ScenarioTest {
    int     res            = 0;
    Object obj = new Object();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream ps;

    public static void main(String[] args) {
        System.exit(new F_ThreadTest_08().test());
    }

    class MyThread extends Thread {
        
        public void run() {
            synchronized(obj) {
                obj.notify();
            }
            destroy();
        }
    }

    public int test() {
        ps = System.err;
        
        if (!task1()) {
            fail("FAIL in task1().");
            res++;
        } else {
            log.info("task1 passed.");
        }
        if (!task2()) {
            fail("FAIL in task2().");
            res++;
        } else {
            log.info("task2 passed.");
        }
        
        if (!task3()) {
            fail("FAIL in task3().");
            res++;
        } else {
            log.info("task3 passed.");
        }        
        
        if (res > 0) {
            return fail("TEST FAILED.");
        } else {
            return pass();
        }
    }  
    
    private boolean task1() {
        out.reset();
        System.setErr(new PrintStream(out));
        Thread.dumpStack();
        System.setErr(ps);
        log.info(out.toString());
        String str[] = out.toString().split("\n");
        if (str[1].indexOf("java.lang.Thread.dumpStack(") == -1) {
            fail("FAIL: fail in 2 str. of dumpStack().");
            return false;
        }
        if (str[2].indexOf("org.apache.harmony.test.func.api.java.lang.F_ThreadTest_08.F_ThreadTest_08.task1(") == -1) {
            fail("FAIL: fail in 3 str. of dumpStack().");
            return false;
        }
        if (str[3].indexOf("org.apache.harmony.test.func.api.java.lang.F_ThreadTest_08.F_ThreadTest_08.test(") == -1) {
            fail("FAIL: fail in 4 str. of dumpStack().");
            return false;
        }
//        if (str[4].indexOf("org.apache.harmony.test.func.api.java.lang.F_ThreadTest_08.F_ThreadTest_08.main([Ljava.lang.String;") == -1) {
//            fail("FAIL: fail in 5 str. of dumpStack().");
//            return false;
//        }        
        return true;
    }
    
    private boolean task2() {
        out.reset();
        System.setErr(new PrintStream(out));
        Thread t = new MyThread();
        synchronized(obj) {
            t.start();
            try {
                obj.wait();
                t.join();
            } catch (InterruptedException e) {
                fail("FAIL: unexpected InterruptedException in wait().");
                res++;
            }
        }
        System.setErr(ps);
        log.info(out.toString());
        String str = out.toString();
        int ind = -1;
        if ((ind = str.indexOf("java.lang.NoSuchMethodError")) == -1) {
            fail("FAIL: fail in 1 str. in task2.");
            return false;
        }
        if ((ind = str.indexOf("java.lang.Thread.destroy(", ind)) == -1) {
            fail("FAIL: fail in 2 str. in task2.");
            return false;
        }
        if ((ind = str.indexOf(" org.apache.harmony.test.func.api.java.lang.F_ThreadTest_08.F_ThreadTest_08$MyThread.run(", ind)) == -1) {
            fail("FAIL: fail in 3 str. in task2.");
            return false;
        }
        return true;
    }
    
    private boolean task3() {
        out.reset();
        ThreadGroup tg = new ThreadGroup("group1.");
        Thread th = new Thread(tg, "thread1.");
        Throwable ex = new Throwable("test.");
        System.setErr(new PrintStream(out));
        tg.uncaughtException(th, ex);
        String str =out.toString();
        out.reset();
        ex.printStackTrace();
        System.setErr(ps);
        log.info(str);
        log.info(out.toString());
        if (str.indexOf(out.toString()) == -1) {
            fail("FAIL: in task3 result string isn't expected.");
            return false;
        }
        return true;
    }
}