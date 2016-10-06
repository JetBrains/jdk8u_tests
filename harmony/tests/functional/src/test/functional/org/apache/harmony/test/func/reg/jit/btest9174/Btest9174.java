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

package org.apache.harmony.test.func.reg.jit.btest9174;

public class Btest9174 {
    Object obj;

    public static void main(String[] args) {
        System.exit(new Btest9174().test());
    }

    int test() {
        try {
            method();
            System.err.println("FAILED - NullPointerException wasn't thrown");
            return 1;
        } catch (NullPointerException e) {
            System.err.println("OK - expected exception was thrown:");
            System.err.println(e);
            return 0;
        }
    }
    
    void method() {
        for (int i = 0; i < 100000; i++) {
            obj.getClass();
        }
    }

}

