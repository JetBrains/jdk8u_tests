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
package org.apache.harmony.test.func.jit.HLO.inline.Synchronization.Synchronize1;

import java.io.CharArrayReader;
import java.io.IOException;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 25.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Synchronize1 extends Test {

    String result = "";
    private static final String pattern = "Hello World !";
    private static final CharArrayReader reader = new CharArrayReader(new char[] {'H',
            'e','l','l','o', ' ','W','o','r','l', 'd', ' ', '!'});
    
    boolean exception = false;
    
    public static void main(String[] args) {
        System.exit(new Synchronize1().test(args));
    }

    public int test() {
        log.info("Start Synchronize1 test...");
        TestThread thread1 = new TestThread(1);
        TestThread thread2 = new TestThread(2);
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            log.add(e);
            return fail("TEST FAILED: unexpected exception " + e);
        }
        if (result.equals(pattern)) {
            if (!exception) return pass();
            else return fail("TEST FAILED");
        } else {
            return fail("TEST FAILED: Result \"" + result + "\" != \"Hello World !\"");
        }
    }

    class TestThread extends Thread {
        
        int threadNumber;
        int letter;
        
        TestThread (int number) {
            super();
            threadNumber = number;
            start();
        }
        
        public void run() {
            try {
                for (int i=0; i<100000; i++)
                while (letter != -1) inlineMethod();
            } catch (Throwable e) {
                log.add(e);
                log.info("Unexpected exception in thread " + threadNumber);
                exception = true;
            }
        }
        
        private final void inlineMethod() throws IOException {
            synchronized(reader) {
                letter = reader.read();
                if (letter != -1) result = result + (char)letter;
            }
        }
    }
}



