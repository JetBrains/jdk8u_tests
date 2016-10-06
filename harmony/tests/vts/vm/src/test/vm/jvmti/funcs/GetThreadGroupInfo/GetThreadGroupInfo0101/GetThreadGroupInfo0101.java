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
public class GetThreadGroupInfo0101 {

    static public void main(String args[]) {

        ThreadGroup tG_1 = new ThreadGroup("First testing ThreadGroup");
        TestThread_TG_02 thread_tG_1
            = new TestThread_TG_02(tG_1, "FirstThreadGroup_FirstThread");
        TestThread_TG_02 thread_tG_2
            = new TestThread_TG_02(tG_1, "FirstThreadGroup_SecondThread");
        TestThread_TG_02 thread_tG_3
            = new TestThread_TG_02(tG_1, "FirstThreadGroup_ThirdThread");

        tG_1.setMaxPriority(10 /* MAX_PRIORITY */);
	thread_tG_1.start();
	thread_tG_2.start();
	thread_tG_3.start();

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

        killThread(thread_tG_1);
	killThread(thread_tG_2);
	killThread(thread_tG_3);

        return;        
    }

    public static void killThread(TestThread_TG_02 thread) {
        thread.stop = true;
        try {
            thread.join();
        } catch (Throwable tex) {
            System.err.print("Exception was encountered during thread join :" + tex);
        }
    }
}

class TestThread_TG_02 extends Thread {
    boolean stop = false;
    
    TestThread_TG_02(ThreadGroup tG, String name) {
        super(tG, name);
    }    
    
    public void run() {
        while(!stop) {
            try {
                Thread.sleep(100);
            } catch (Throwable te) {
                te.printStackTrace();
            }
        }
    }
}

