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

package org.apache.harmony.test.func.reg.jit.btest4521;

public class Btest4521 {

    public static void main(String[] args) {
        boolean ret = true;
        
        System.err.println("Run zz1...");
        try {            
            new zz1().test(args);
            System.err.println(
                    "Expected AbstractMethodError exception was not thrown!");
            System.err.println("Testcase failed!");
            ret = false;
        } catch (AbstractMethodError e) {
            System.err.println("Expected exception was thrown: " + e);
            System.err.println("Testcase passed!");
        } catch (Throwable e) {
            System.err.println("Unexpected exception was thrown:");
            e.printStackTrace();
            System.err.println("Testcase failed!");
            ret = false;
        }
        
        System.err.println();
        System.err.println("Run zz11...");
        try {            
            new zz11().test(args);
            System.err.println(
            "Expected AbstractMethodError exception was not thrown!");
            System.err.println("Testcase failed!");
            ret = false;
        } catch (AbstractMethodError e) {
            System.err.println("Expected exception was thrown: " + e);
            System.err.println("Testcase passed!");
        } catch (Throwable e) {
            System.err.println("Unexpected exception was thrown:");
            e.printStackTrace();
            System.err.println("Testcase failed!");
            ret = false;
        }
        
        if (!ret) {
            System.exit(-99);
        }
    }

}
