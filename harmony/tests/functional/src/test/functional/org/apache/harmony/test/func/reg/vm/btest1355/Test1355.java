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
package org.apache.harmony.test.func.reg.vm.btest1355;

public class Test1355 extends Thread {

    public static void main(String[] args) {
        System.err.println("Start Test1355...");
        Test1355 test = new Test1355();
        test.test();
    }

    public void test() {
        
        final int thread_count = 254;
        for (int i = 0; i < thread_count; i++) {
            System.err.print("\nStart of thread " + i );
            new Test1355().start();
            System.err.print("  .... done");
        }
        synchronized (Test1355.class) {
            Test1355.class.notifyAll();
        }
    }
    
    public Test1355() {
        setDaemon(true);
    }

    public void run() {
        synchronized (this.getClass()) {
            try {
                this.getClass().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
