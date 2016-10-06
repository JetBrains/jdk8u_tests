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
 * @author Aleksei Y. Semenov
 * @version $Revision: 1.2 $
 *
 */ 

public class MonitorContendedEnter0101 extends Thread {

	public static Object monitor = null;

	public static void main(String args[]){
		
		monitor = new Object();
		Thread secondThread = new MonitorContendedEnter0101("MonitorContendedEnter0101.secondThread");  

		synchronized(monitor){
			// locked a monitor
			System.out.println("main thread: locked the monitor");

			// monitor second thread
			secondThread.start();

			// wait and hold the lock until the second thread blocks
			while (!secondThread.getState().equals(Thread.State.valueOf("BLOCKED"))){
				Thread.yield();
				System.out.println("main thread: Waiting for second thread to attempt to lock a monitor");
			}
			
		}

	}

	public MonitorContendedEnter0101(String name) {
	 	super(name);
	}


	// second thread code
	public void run() {
		synchronized (monitor) {
			// do something simple
			this.getName().trim();
			System.out.println("secondThread: locked the monitor");
		}
	}
}
