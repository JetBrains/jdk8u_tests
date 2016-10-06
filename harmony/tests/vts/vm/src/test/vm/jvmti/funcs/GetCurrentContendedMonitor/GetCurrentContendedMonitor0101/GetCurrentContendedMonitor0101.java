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
 * @version $Revision: 1.1 $
 *
 */
public class GetCurrentContendedMonitor0101 {
    public static boolean all_threads_can_start = false;
    public final static Object sync0 = new Object();

    public static void main(String[] args) {
        TestThread_T_11_1 tr1 = new TestThread_T_11_1("superpuperthread_1");
        TestThread_T_11_2 tr2 = new TestThread_T_11_2("superpuperthread_2");

        tr1.start();

        /*
         * Wait while thread #1 will started
         */
        while (!all_threads_can_start) {
	    try {
	        Thread.sleep(500);
            } catch (Throwable te) {
                te.printStackTrace();
            }
        }

        tr2.start();

        return;
    }
}

class TestThread_T_11_1 extends Thread {

    TestThread_T_11_1(String name) {
        super(name);
        return;
    }

    public void run() {
        synchronized (GetCurrentContendedMonitor0101.sync0) {
            GetCurrentContendedMonitor0101.all_threads_can_start = true;

            try {
                Thread.sleep(3000);
            } catch (Throwable te) {
                te.printStackTrace();
            }
            /*
             * Transfer control to native part.
             */
            new Thread ("agent") {
                public void run() {
                   return;
                }
            }.start();
        
            try {
                Thread.sleep(1000);
            } catch (Throwable te) {
                te.printStackTrace();
            }

            return;
        }
    }
}

class TestThread_T_11_2 extends Thread {

    TestThread_T_11_2(String name) {
        super(name);
        return;
    }    

    public void run() {
        synchronized (GetCurrentContendedMonitor0101.sync0) {
            GetCurrentContendedMonitor0101.all_threads_can_start = true;
            return;
        }
    }
}

