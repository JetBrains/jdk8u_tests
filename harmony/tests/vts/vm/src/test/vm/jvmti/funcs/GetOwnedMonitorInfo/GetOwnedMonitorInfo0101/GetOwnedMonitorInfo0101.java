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
 * Test for GetOwnedMonitorInfo JVMTI function.
 * <p> Test scenario: </p>
 * <ol>
 *   <li> Main thread starts "Owner" thread and waits for notification. </li>
 *   <li> Owner thread occupies 10 monitors, notifies main thread and waits.
 *     </li>
 *   <li> Main thread invokes agent's exception callback which checks owned
 *     monitors for Owner thread. </li>
 *   <li> Main thread notifies Owner thread. </li>
 * </ol>
 */
public class GetOwnedMonitorInfo0101 {

    static final int MONITORS_NUMBER = 10;
    static Object[] monitors = new Object[MONITORS_NUMBER];
    static Object lock = new Object();

    public static void main(String[] args) {
        for (int i = 0; i < monitors.length; i++) {
            monitors[i] = new Object();
        }

        System.err.println("[JAVA] " + monitors.length +
                " monitor objects created");

        Thread ownerThread = new Thread("Owner") {
            public void run() {
                occupyAllMonitors();
            }
        };

        synchronized (lock) {
            System.err.println("[JAVA] Starting owner thread...");
            ownerThread.start();

            try {
                System.err.println("[JAVA] Waiting for owner thread to " +
                        "occupy monitors...");
                lock.wait();

                try {
                    System.err.println("[JAVA] Invoking agent...");
                    throw new InvokeAgentException(monitors, ownerThread);
                } catch (InvokeAgentException exc) {
                    System.err.println("[JAVA] Returned from agent callback.");
                }

                System.err.println("[JAVA] Notifying owner thread...");
                lock.notify();

            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
    }

    static void occupyAllMonitors() {
        System.err.println("[JAVA] Owner: Occupying monitors...");

        occupyMonitor(0);

        System.err.println("[JAVA] Owner: All monitors released.");
    }

    static void occupyMonitor(int monitorNumber) {
        if (monitorNumber < monitors.length) {
            synchronized (monitors[monitorNumber]) {
                occupyMonitor(monitorNumber + 1);
            }
        } else {
            top();
        }
    }

    static void top() {
        System.err.println("[JAVA] Owner: " + monitors.length +
                " monitors occupied.");

        synchronized (lock) {
            System.err.println("[JAVA] Owner: Notifying main thread...");
            lock.notify();

            System.err.println("[JAVA] Owner: Waiting for main thread to " +
                    "check owned monitors...");
            try {
                lock.wait();
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }

        System.err.println("[JAVA] Owner: Releasing monitors...");
    }
}

class InvokeAgentException extends Exception {

    Object[] monitors;
    Thread ownerThread;

    InvokeAgentException(Object[] monitors, Thread thread) {
        this.monitors = monitors;
        this.ownerThread = thread;
    }
}
