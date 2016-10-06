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
public class ResumeThread0102 {
    static public void main(String args[]) {
        TestThread_T_05_02 thread = new TestThread_T_05_02("SuperPuperTest");
        thread.setDaemon(true);
        thread.start();

        while (!thread.started) {
            try {
                Thread.sleep(100);
            } catch (Throwable tex) {}
        }

        /*
         * TODO: Remove all deprecated methods from test.
         */
        thread.suspend();
        
        /*
         * Transfer control to native part.
         */
        new Thread ("agent") {
            public void run() {
                return;
            }
        }.start();

        try {
            Thread.sleep(5000);
        } catch (Throwable tex) {}

        thread.resume();
        
        killThread(thread);

        return;        
    }

    public static void killThread(TestThread_T_05_02 thread) {
        thread.stop = true;
        try {
            thread.join();
        } catch (Throwable tex) {
            System.err.print("Exception was encountered during thread join :" + tex);
        }
    }
}

class TestThread_T_05_02 extends Thread {
    boolean stop = false;
    public boolean started = false;
    
    TestThread_T_05_02(String name) {
        super(name);
    }    
    
    public void run() {
        started = true;
        int stopper = 0;
        while (!stop) {
            System.err.print("");
            int sum = 0;
            stopper++;
            for (int i = 0; i < 1000000; i++) {
                int a = i;
                int b = i * 4;
                int c = a + b;
                sum += c;
            }
        }
    }    
}


