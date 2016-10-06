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
package org.apache.harmony.test.func.reg.vm.btest7095;

import java.util.logging.Logger; 
import org.apache.harmony.test.share.reg.HangTest;

/**
 * Checks that no deadlock happen during test execution.
 */
public class Btest7095 extends HangTest {

    public static void main(String[] args) {
        System.exit(new Btest7095().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        TestThread.main();

        return pass();
    }
}

class TestThread extends Thread {
    static void main() {
        TestThread t = new TestThread();
        t.start();
        try {
            t.join(100000);
        } catch (Throwable th) {}
    }

    public void run() {
        try {
            Thread.sleep(2);
            getContextClassLoader();
        } catch (Throwable t) {}
    }
}

