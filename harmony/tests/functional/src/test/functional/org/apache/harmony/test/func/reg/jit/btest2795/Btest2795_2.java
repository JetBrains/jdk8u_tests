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

/*
 * Please, note that you need to compile Abstract2795 and Interface2795 classes
 * after you compiled Btest2795_1 and Btest2795_2 classes to reproduce the 
 * failure. Please, see makefile for more details.
*/

package org.apache.harmony.test.func.reg.jit.btest2795;

public class Btest2795_2 {
    public static void main(String [] argv) {
           System.err.println("Start Btest2795_2 testcase...");
        try {            
            new Abstract2795();
            System.err.println("Expected InstantiationError was not thrown!");
            System.err.println("The testcase failed!");
            System.exit(99);
        } catch (InstantiationError e) {
            System.err.println(
                    "Expected InstantiationError exception was thrown: " + e);
            System.err.println("The testcase passed!");
        } catch (Throwable e) {
            System.err.println("Unexpected exception was thrown:");
            e.printStackTrace(System.out);
            System.err.println("The testcase failed!");
            System.exit(99);
        }
    }
}

class Abstract2795  {
}
