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

package org.apache.harmony.test.func.reg.vm.btest3489;

public class Wrapper3489 {

     public static void main(String args[]) {
          try{
            Btest3489.testn();
            System.err.println("Error, expected VerifyError was not thrown.");
            System.err.println("The test failed!");
            System.exit(99);
        } catch (VerifyError e) {
            System.err.println("Expected VerifyError was thrown: " + e);
            System.err.println("The test passed!");
        } catch (Exception e) {
            System.err.println("Unexpected exception was thrown:");
             e.printStackTrace();
            System.err.println("The test failed!");
            System.exit(99);             
        }

    }
}