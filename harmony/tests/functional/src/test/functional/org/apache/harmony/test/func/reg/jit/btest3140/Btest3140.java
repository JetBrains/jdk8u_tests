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
 
package org.apache.harmony.test.func.reg.jit.btest3140;

public class Btest3140 {   

    public static void main(String[] args) {
        try {
            Test3140 cl = new Test3140();
            System.err.println(cl.test(args));
            System.err.println("PASSED!");
        } catch (Throwable e) {
            System.out.println("Unexpected error was thrown:");
            e.printStackTrace();
            System.exit (-99);
        }
    }
}
