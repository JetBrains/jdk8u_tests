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
public class GetObjectMonitorUsage0102 {
    public static Object obj_ = new Object();
    public static Object obj = new Object();    
    static int thread_counter = 0;

    static public void main(String args[]) {

        TestThread_GOMU_0102 t1 = new TestThread_GOMU_0102("agent 1");
        TestThread_GOMU_0102 t2 = new TestThread_GOMU_0102("agent 2");
        TestThread_GOMU_0102 t3 = new TestThread_GOMU_0102("agent 3");

        t1.start();
        t2.start();
        t3.start();
        
        return;
    }
}

class TestThread_GOMU_0102 extends Thread {
    static boolean test = false;

    TestThread_GOMU_0102(String name) {
        super(name);
    }

    public void run() {
        synchronized(GetObjectMonitorUsage0102.obj_) {            
            GetObjectMonitorUsage0102.thread_counter++;
        }

        synchronized(GetObjectMonitorUsage0102.obj) {
            
            if (!test) {
                test = true;                
                while ( GetObjectMonitorUsage0102.thread_counter < 3 ) {
                    try {
                        Thread.sleep(500);
                    } catch (Throwable tex) { }
                }
                special_method( GetObjectMonitorUsage0102.obj );                
            }            
        }        
    }

    static public void special_method(Object obj) {
        Object _OBJ = obj;
        /*
         * Transfer control to native part.
         */
        try {
            throw new InterruptedException();
        } catch (Throwable tex) { }
        return;
    }    
}


