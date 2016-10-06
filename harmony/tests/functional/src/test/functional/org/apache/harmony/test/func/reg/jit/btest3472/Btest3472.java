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

package org.apache.harmony.test.func.reg.jit.btest3472;

public class Btest3472 {
    float floatArr[] = new float[1];

    public void foo(double num1) {}

    public static void main(String argv[]) {        
        Btest3472 t = new Btest3472();
        t.test();
        
    }

    public void test() {
        System.err.println("Starting test...");
        floatArr[0] = (float) 0;
        foo(floatArr[0]++);
        System.err.println("foo() method works correctly!");
    }
    
}
