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
 
package org.apache.harmony.test.func.reg.jit.btest5153;

import java.nio.ByteBuffer;

public class Btest5153 {   

    public static void main(String[] args) throws Exception {
        MyClass test = new MyClass();
        try {
            test.test1(null);
            System.err.println("FAILED!");
        } catch (NullPointerException e) {
            System.err.println("PASSED!");
        }
    }

    public static class MyClass {
        protected void test1(ByteBuffer out) { 
            System.err.println("Start MyClass test...");
            int outLeft = out.remaining();
            System.err.println("---------");
            for (int i = 0; i < 1; i++) {
                   out.get();
            }
            System.err.println("End MyClass test...");
        }
    }

}
