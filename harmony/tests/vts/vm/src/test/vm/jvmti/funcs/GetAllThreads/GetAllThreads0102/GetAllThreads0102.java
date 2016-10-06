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
public class GetAllThreads0102 {

    public final static Object sync0 = new Object();

    static public void main(String args[]) {

        TestThread_T_02_02 thread_0  = new TestThread_T_02_02("Thread for company");
        TestThread_T_02_02 thread_1  = new TestThread_T_02_02("Thread for company");
        TestThread_T_02_02 thread_2  = new TestThread_T_02_02("Thread for company");
        TestThread_T_02_02 thread_3  = new TestThread_T_02_02("Thread for company");
        TestThread_T_02_02 thread_4  = new TestThread_T_02_02("Thread for company");

        thread_0.start();
        thread_1.start();
        thread_2.start();
        thread_3.start();
        thread_4.start();

        try {
	    Thread.sleep(1000);
	} catch (Throwable tex) {
	    System.out.println("\nError during <sleep> 1 :" + tex);
        }

        /*
         * Transfer control to native part.
         */
        new Thread("agent") {
            public void run() {
                return;
            }
        }.start();

        killThread(thread_0);
        killThread(thread_1);
        killThread(thread_2);
        killThread(thread_3);
        killThread(thread_4);

        return;        
    }

    public static void killThread(TestThread_T_02_02 thread) {
        thread.stop = true;
        try {
            thread.join();
        } catch (Throwable tex) {
            System.err.print("Exception was encountered during thread join :" + tex);
        }
    }
}

class TestThread_T_02_02 extends Thread {
    boolean stop = false;
  
    TestThread_T_02_02(String name) {
        super(name);
    }    
    
    public void run() {
        for (int i = 0; !stop; ) {
            try {
                this.sleep(5000);
            } catch (Throwable tex) { }
        }
    }
}

