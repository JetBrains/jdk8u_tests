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
 
package org.apache.harmony.test.func.reg.jit.btest5034;

public class Btest5034 {   
    
    public static void main(String[] args) throws Exception {        
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            System.err.println("NullPointerException was thrown!");
        }
        
        try {
            System.err.println("Start");
            new MyObj(null).test();
            System.err.println("FAILED!");
        } catch (NullPointerException e) {
            System.err.println("PASSED!");
        } catch (Exception e) {
            System.err.println("FAILED!");
        }
    }

    static class MyObj {
        Object is;
        
        MyObj(Object s) {
            is = s;
        }
        
        void test() {
            is.hashCode();
        }
    }
}
