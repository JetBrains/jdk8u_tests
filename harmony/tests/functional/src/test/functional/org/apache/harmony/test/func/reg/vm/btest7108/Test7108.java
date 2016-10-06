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
/**
 */

package org.apache.harmony.test.func.reg.vm.btest7108;

public class Test7108 extends Thread {

    static boolean completed = false;

    public Test7108(String name) {
        super(name);
    }

    public static void main(String[] args) {
        new Test7108("Btest7108_thread").test();
        System.exit(0);
    }

    public void test() {

        this.start();

        synchronized (this) {
            try {
                while (!completed)
                    this.wait(100);
            } catch (java.lang.InterruptedException ie) {}
        }
    }

    public void run() {
        completed = true;
    }
}

