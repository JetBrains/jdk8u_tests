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
 
package org.apache.harmony.test.func.reg.jit.btest5710;

public class Btest5710 {   
    
    Object obj;

    public static void main(String [] args) {
        (new Btest5710()).test();
    }

    public void test() {
        System.err.println("Start Btest5710 test..."); 
        try {
            sync();
            System.err.println("FAILED: NullPointerException was expected");
            System.exit(-99);
        } catch (NullPointerException e) { 
            System.err.println("PASSED: " + e);
        } catch (Throwable e) { 
            System.err.println("Unexpected exception was thrown:");
            e.printStackTrace();
            System.err.println("FAILED");
            System.exit(-99);
        }
    }

    void sync() {
        synchronized (obj) {
            obj = new Object();
        }
    }

}
