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
public class MonitorContendedEntered0101 {
    static boolean isMonitorTaken = false;

    static void sleep_a_bit(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // ignore - never happen
        }
    }
    
    static public void main(String args[]) {
        final Object obj = new Object();

        Thread.currentThread().setName("MonitorContendedEntered0101.Main");
        
        new Thread("special_thread_2") {
            public void run() {
                synchronized(obj) {
                    isMonitorTaken = true;

                    // ok, give main thread a time to start waiting & 
                    // then release the monitor
                    sleep_a_bit(1000);
                }
            }
        }.start();

        // wait until aux thread gets the obj monitor
        while (!isMonitorTaken) {
            sleep_a_bit(10);
        }

        // wait on monitor - here the event must be generated
        synchronized (obj) {

            // do anything simple
            isMonitorTaken = false;
        }
        return;
    }
}

