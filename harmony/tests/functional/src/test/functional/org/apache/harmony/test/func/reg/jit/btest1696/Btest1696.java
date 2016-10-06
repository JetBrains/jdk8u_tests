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

package org.apache.harmony.test.func.reg.jit.btest1696;

public class Btest1696 {
    public int a;
    public RetValue b;

    public static void main(String[] args) {
        System.err.println("Start main Btest1696");
        (new Btest1696()).f1();
        System.err.println("Finish main Btest1696");
    }
       
    public void f2() {
        int aaa[] = new int[1];
        try {
            aaa[2] = a; // Generate exception 
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Expected exception was thrown: " + e);
        }
    }

    public RetValue f1() {
        RetValue val;
        try {
            a = 0;
            f2();
        } finally {
            System.err.println("I am in finally f1 block!");
            val = this.b;
            this.b = null;
        }
        return val;
    }

}

class RetValue {
    private int value;
    public RetValue(int value) {
        this.value = value;
    }
    public String toString() {
        return Integer.toString(value);
    }
}
