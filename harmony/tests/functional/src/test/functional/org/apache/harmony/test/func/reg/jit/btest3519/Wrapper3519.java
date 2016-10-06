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

package org.apache.harmony.test.func.reg.jit.btest3519;

public class Wrapper3519 {

     public static void main(String args[]) {
        try {        
            Btest3519 aTest = new Btest3519();
            aTest.test();
            System.err.println("Expected LinkageError was not thrown!");
            System.err.println("The test failed!");
            System.exit(99);
        } catch (LinkageError e) {
            System.err.println("Expected LinkageError was thrown:");
            e.printStackTrace();
            System.err.println("The test passed!");
        } catch (Throwable e) {            
            System.err.println("Unexpected exception was thrown:");
            e.printStackTrace();
            System.err.println("The test failed!");
            System.exit(99);
        }
    }
}