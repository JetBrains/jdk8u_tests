/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.apache.harmony.vts.test.vm.jvmti;

/** 
 * @author Valentin Al. Sitnick
 * @version $Revision: 1.2 $
 *
 */

/**
 * Java part of test for SuspendThreadList JVMTI function.
 *
 */ 
public final class SuspendThreadList0103 {

    static boolean kill = false;
    
    public static void main(String args[]) {

        TestThread_T_04_03 thread_1 = new TestThread_T_04_03(1);
        TestThread_T_04_03 thread_2 = new TestThread_T_04_03(2);
        TestThread_T_04_03 thread_3 = new TestThread_T_04_03(3);

        /*
         * Sometimes in case of buggy implementation the 
         * threads do not die after Suspend/Resume functions
         * were called. The threads need to be daemon.
         */
        thread_1.setDaemon(true);
        thread_2.setDaemon(true);
        thread_3.setDaemon(true);        

        thread_1.start();
        thread_2.start();
        thread_3.start();        

        /*
         * 1. Sometimes in case of buggy implementation even
         * the daemon thread does not die if its "run" method 
         * was not called. Wait until "run" method is called.
         * 
         * 2. Does not use wait/notify because it may be broken.
         */
        while (TestThread_T_04_03.counter < 3) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException iex) {
                System.err.print("\n\tJAVA: unexpected Exception: " + iex);
            }
        }

        /*
         * Transfer control to native part.
         */
        new Thread ("agent") {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Throwable tex) { }

                SuspendThreadList0103.kill = true;
                
                return;                
            }
        }.start();

        while (!kill) {
            try {
                Thread.sleep(500);
            } catch (Throwable tex) { }
        }

        thread_1.killThread();
        thread_2.killThread();
        thread_3.killThread();

        return;        
    }
}

class TestThread_T_04_03 extends Thread {
    static int counter = 0;
    private boolean stop = false;
    private int number;
    
    TestThread_T_04_03(int number) {
        super("SuperPuperTest_" + number);
        this.number = number;
    }    

    void printMsg(String msg) {
        System.err.print("\n\tJAVA: thread[" + number + "]: " + msg);
    }

    void printDbg(String msg) {
        //Uncomment for getting debug details.
        //System.err.println("thread[" + number + "]: " + msg);
    }

    void killThread() {
        stop = true;
 
        try {

            /*
             * Sometimes in case of buggy implementation the 
             * "join" method never returns.
             * Workaround this situation.
             */
            long ctime = System.currentTimeMillis();
            printDbg("\tstart joining...");
            join(1000);

            if (System.currentTimeMillis() - ctime >= 1000) {
                printMsg("\tjoining failed...");
            } else {
                printDbg("\tjoining done...");
            }
        } catch (InterruptedException e) {
            printMsg("\tjoining interrupted..");
        }
    }

    public void run() {
        printDbg("\tstarted...");

        synchronized(TestThread_T_04_03.class) {
            counter++;
        }

        // wait until all thread are running
        while (counter < 3) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException iex) {
                System.err.print("\n\tJAVA: unexpected Exception: " + iex);
            }
        }
        printDbg("\tcounter = " + counter);
 
        // wait for the stop flag is set
        while (!stop) {
            System.err.print("");
            int sum = 0;
            for (int i = 0; i < 1000000; i++) {
                int a = i;
                int b = i * 4;
                int c = a + b;
                sum += c;
            }            
        }
        printDbg("\tfinished...");
    }
}


